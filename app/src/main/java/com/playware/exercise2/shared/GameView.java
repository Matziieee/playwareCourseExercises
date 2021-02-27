package com.playware.exercise2.shared;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import androidx.annotation.NonNull;

public abstract class GameView extends SurfaceView implements Runnable {
    private SurfaceHolder holder;
    private boolean isRunning = false;
    private Thread gameThread;
    private final static int MAX_FPS = 30;
    private final static int FRAME_PERIOD = 1000 / MAX_FPS;
    private int screenHeight;
    private int screenWidth;

    public GameView(Context context, AttributeSet as){
        super(context,as);
        this.init();
    }

    private void init(){
        holder = getHolder();
        holder.addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(@NonNull SurfaceHolder holder) {
            }

            @Override
            public void surfaceChanged(@NonNull SurfaceHolder holder, int format, int width, int height) {
                screenHeight = height;
                screenWidth = width;
            }

            @Override
            public void surfaceDestroyed(@NonNull SurfaceHolder holder) {
                pause();
            }
        });
        resume();
    }

    public void pause(){
        isRunning = false;
        boolean retry = true;
        while(retry){
            try{
                gameThread.join();
                retry = false;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
    public void resume(){
        isRunning = true;
        gameThread = new Thread(this);
        gameThread.start();
    }

    @Override
    public void run() {
        while(isRunning){
        if(!holder.getSurface().isValid()){
            continue;
        }
        long started = System.currentTimeMillis();

        step();
        Canvas canvas = holder.lockCanvas();
        if(canvas != null){
            render(canvas);
            holder.unlockCanvasAndPost(canvas);
        }
        float deltaTime = System.currentTimeMillis() - started;
        int sleepTime = (int) (FRAME_PERIOD - deltaTime);

        if(sleepTime > 0) {
            try {
                gameThread.sleep(sleepTime);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        while (sleepTime < 0){
            step();
            sleepTime += FRAME_PERIOD;
        }}
    }

    abstract protected void step();
    abstract protected void render(Canvas canvas);
}
