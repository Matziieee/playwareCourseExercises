package com.playware.exercise2.project;

import android.app.Activity;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.playware.exercise2.R;

import java.util.ArrayList;


public class ColorViewAdapter extends ArrayAdapter {

    Activity context;
    ArrayList<ColorBox> boxes;

    public ColorViewAdapter(Activity context, int resource, ArrayList<ColorBox> boxes) {
        super(context, resource, boxes);
        this.boxes = boxes;
        this.context = context;

    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        convertView = inflater.inflate(R.layout.color_view_frame,null);
        convertView.setBackgroundColor(boxes.get(position).getCurrentColor());

        return convertView;
    }
}
