package com.playware.exercise2.maxhttp;

public class GameSessionPostRequest {
    private String groupId;
    private String gameId;
    private String gameTypeId;
    private String gameScore;
    private String gameTime;
    private String numTiles;

    public GameSessionPostRequest(String groupId, String gameId, String gameTypeId, String gameScore, String gameTime, String numTiles) {
        this.groupId = groupId;
        this.gameId = gameId;
        this.gameTypeId = gameTypeId;
        this.gameScore = gameScore;
        this.gameTime = gameTime;
        this.numTiles = numTiles;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getGameId() {
        return gameId;
    }

    public void setGameId(String gameId) {
        this.gameId = gameId;
    }

    public String getGameTypeId() {
        return gameTypeId;
    }

    public void setGameTypeId(String gameTypeId) {
        this.gameTypeId = gameTypeId;
    }

    public String getGameScore() {
        return gameScore;
    }

    public void setGameScore(String gameScore) {
        this.gameScore = gameScore;
    }

    public String getGameTime() {
        return gameTime;
    }

    public void setGameTime(String gameTime) {
        this.gameTime = gameTime;
    }

    public String getNumTiles() {
        return numTiles;
    }

    public void setNumTiles(String numTiles) {
        this.numTiles = numTiles;
    }
}
