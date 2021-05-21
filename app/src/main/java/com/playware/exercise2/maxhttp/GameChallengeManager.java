package com.playware.exercise2.maxhttp;

import android.content.SharedPreferences;
import android.os.AsyncTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class GameChallengeManager {
    public static String endpoint = "https://centerforplayware.com/api/index.php";
    public static int GROUP_ID = 5001;


    public boolean deleteGameChallenge(String gcid, String token){
        RemoteHttpRequest requestPackage = new RemoteHttpRequest();
        requestPackage.setMethod("GET");
        requestPackage.setUrl(endpoint);
        requestPackage.setParam("method","deleteGameChallenge");
        requestPackage.setParam("device_token", token);
        requestPackage.setParam("group_id", ""+GROUP_ID);
        requestPackage.setParam("gcid", gcid);
        Downloader downloader = new Downloader(); //Instantiation of the Async task
        try {
            String result = downloader.execute(requestPackage).get();
            JSONObject o = new JSONObject(result);
            return o.getBoolean("response");
        } catch (ExecutionException | InterruptedException | JSONException e) {
            e.printStackTrace();
        }
        return false;
    }
    public boolean postGameChallenge(String deviceToken, int mode){
        RemoteHttpRequest requestPackage = new RemoteHttpRequest();
        requestPackage.setMethod("POST");
        requestPackage.setUrl(endpoint);
        requestPackage.setParam("method","postGameChallenge"); // The method name
        requestPackage.setParam("device_token", deviceToken); // Your device token
        requestPackage.setParam("game_id", "42069"); // The game ID (From the Game class > setGameId() function
        requestPackage.setParam("game_type_id", mode+"");// The game type ID (From the GameType class creation > first parameter)
        requestPackage.setParam("challenger_name", deviceToken);// The challenger name
        requestPackage.setParam("group_id", ""+GROUP_ID);


        Downloader downloader = new Downloader(); //Instantiation of the Async task
        try {
            String result = downloader.execute(requestPackage).get();
            JSONObject o = new JSONObject(result);
            return o.getBoolean("response");
        } catch (ExecutionException | InterruptedException | JSONException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean postGameChallenge(String deviceToken, String gameId, String name) {
        RemoteHttpRequest requestPackage = new RemoteHttpRequest();
        requestPackage.setMethod("POST");
        requestPackage.setUrl(endpoint);
        requestPackage.setParam("method","postGameChallenge"); // The method name
        requestPackage.setParam("device_token", deviceToken); // Your device token
        requestPackage.setParam("game_id",gameId); // The game ID (From the Game class > setGameId() function
        requestPackage.setParam("game_type_id", "1");// The game type ID (From the GameType class creation > first parameter)
        requestPackage.setParam("challenger_name",name);// The challenger name
        requestPackage.setParam("group_id", ""+GROUP_ID);


        Downloader downloader = new Downloader(); //Instantiation of the Async task
        try {
            String result = downloader.execute(requestPackage).get();
            JSONObject o = new JSONObject(result);
            return o.getBoolean("response");
        } catch (ExecutionException | InterruptedException | JSONException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean postGameChallengeAccept(String token, GameChallenge gc){
        RemoteHttpRequest requestPackage = new RemoteHttpRequest();
        requestPackage.setMethod("POST");
        requestPackage.setUrl(endpoint);
        requestPackage.setParam("method","postGameChallengeAccept");
        requestPackage.setParam("device_token", token); // Your device token
        requestPackage.setParam("challenged_name", token); // The name of the person accepting the challenge
        requestPackage.setParam("gcid", ""+gc.getGcid());
        Downloader downloader = new Downloader(); //Instantiation of the Async task
        try {
            String result = downloader.execute(requestPackage).get();
            JSONObject o = new JSONObject(result);
            return o.getBoolean("response");
        } catch (ExecutionException | InterruptedException | JSONException e) {
            e.printStackTrace();
        }
        return false;// The game challenge id you want to accept
    }

    public boolean postGameChallengeAccept(String deviceToken, String id, String name) {
        RemoteHttpRequest requestPackage = new RemoteHttpRequest();
        requestPackage.setMethod("POST");
        requestPackage.setUrl(endpoint);
        requestPackage.setParam("method","postGameChallengeAccept"); // The method name
        //todo change token to actual device token
        requestPackage.setParam("device_token", "changeThis"); // Your device token
        requestPackage.setParam("challenged_name", name); // The name of the person accepting the challenge
        requestPackage.setParam("gcid", id); // The game challenge id you want to accept


        Downloader downloader = new Downloader(); //Instantiation of the Async task
        try {
            String result = downloader.execute(requestPackage).get();
            JSONObject o = new JSONObject(result);
            return o.getBoolean("response");
        } catch (ExecutionException | InterruptedException | JSONException e) {
            e.printStackTrace();
        }
        return false;
    }

    public ArrayList<GameChallenge> getGameChallenges(String deviceToken) {
        RemoteHttpRequest requestPackage = new RemoteHttpRequest();
        requestPackage.setMethod("GET");
        requestPackage.setUrl(endpoint);
        requestPackage.setParam("method","getGameChallenge"); // The method name
        requestPackage.setParam("device_token", deviceToken); // Your device token

        Downloader downloader = new Downloader(); //Instantiation of the Async task

        try {
            String result = downloader.execute(requestPackage).get();
            JSONObject o = new JSONObject(result);
            return this.makeListFromJObject(o);
        } catch (ExecutionException | InterruptedException | JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    private ArrayList<GameChallenge> makeListFromJObject(JSONObject obj) throws JSONException {
        ArrayList<GameChallenge> list = new ArrayList<>();
        JSONArray challenges = obj.getJSONArray("results");
        for (int i = 0; i < challenges.length(); i++) {
            GameChallenge gc = new GameChallenge();
            JSONObject jObj = challenges.getJSONObject(i);
            //if obj doesn't have our group id, skip it.
            if(Integer.parseInt(jObj.getString("group_id")) != GROUP_ID) continue;
            //GCid
            gc.setGcid(Integer.parseInt(jObj.getString("gcid")));
            //token
            gc.setDeviceToken(jObj.getString("device_token"));
            gc.setDeviceToken_c(jObj.getString("device_token_c"));
            //challenger name
            gc.setChallengerName(jObj.getString("challenger_name"));
            //challenged name
            gc.setChallengedName(jObj.getString("challenged_name"));
            //game id
            gc.setGameId(Integer.parseInt(jObj.getString("game_id")));
            //game type id
            gc.setGameTypeId(Integer.parseInt(jObj.getString("game_type_id")));
            //group id
            gc.setGroupId(Integer.parseInt(jObj.getString("group_id")));
            //c_status
            gc.setcStatus(parseStatus(Integer.parseInt(jObj.getString("c_status"))));
            //created
            gc.setCreatedDate(jObj.getString("created"));
            //updated
            gc.setUpdatedDate(jObj.getString("updated"));

            //summary
            if(jObj.has("summary")){
                gc.setSummaryObject(jObj.getJSONArray("summary"));
            }

            list.add(gc);

        }
        return list;
    }

    private ChallengeStatus parseStatus(int code){
        switch (code){
            case 0:
                return ChallengeStatus.ZERO;
            case 1:
                return ChallengeStatus.ONE;
            case 2:
                return ChallengeStatus.TWO;
            case 3:
                return ChallengeStatus.THREE;
            case 4:
                return ChallengeStatus.FOUR;
        }
        return null;
    }

    private class Downloader extends AsyncTask<RemoteHttpRequest, String, String> {

        @Override
        protected String doInBackground(RemoteHttpRequest... params) {
            return HttpManager.getData(params[0]);
        }
    }
}
