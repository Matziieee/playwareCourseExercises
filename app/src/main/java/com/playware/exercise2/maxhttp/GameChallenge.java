package com.playware.exercise2.maxhttp;

import org.json.JSONArray;

public class GameChallenge {
    private int gcid;
    private String deviceToken;
    private String challengerName;
    private String challengedName;
    private int gameId;
    private int gameTypeId;
    private int groupId;
    private ChallengeStatus cStatus;
    private String createdDate;
    private String updatedDate;
    private JSONArray summaryObject;

    public JSONArray getSummaryObject() {
        return summaryObject;
    }

    public void setSummaryObject(JSONArray summaryObject) {
        this.summaryObject = summaryObject;
    }

    public GameChallenge() {
    }

    public int getGcid() {
        return gcid;
    }

    public void setGcid(int gcid) {
        this.gcid = gcid;
    }

    public String getDeviceToken() {
        return deviceToken;
    }

    public void setDeviceToken(String deviceToken) {
        this.deviceToken = deviceToken;
    }

    public String getChallengerName() {
        return challengerName;
    }

    public void setChallengerName(String challengerName) {
        this.challengerName = challengerName;
    }

    public String getChallengedName() {
        return challengedName;
    }

    public void setChallengedName(String challengedName) {
        this.challengedName = challengedName;
    }

    public int getGameId() {
        return gameId;
    }

    public void setGameId(int gameId) {
        this.gameId = gameId;
    }

    public int getGameTypeId() {
        return gameTypeId;
    }

    public void setGameTypeId(int gameTypeId) {
        this.gameTypeId = gameTypeId;
    }

    public int getGroupId() {
        return groupId;
    }

    public void setGroupId(int groupId) {
        this.groupId = groupId;
    }

    public ChallengeStatus getcStatus() {
        return cStatus;
    }

    public void setcStatus(ChallengeStatus cStatus) {
        this.cStatus = cStatus;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    public String getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(String updatedDate) {
        this.updatedDate = updatedDate;
    }

    @Override
    public String toString() {
        return "GameChallenge{" +
                "gcid=" + gcid +
                ", challengerName='" + challengerName + '\'' +
                ", challengedName='" + challengedName + '\'' +
                ", gameId=" + gameId +
                ", gameTypeId=" + gameTypeId +
                ", cStatus=" + cStatus.getAsString() +
                '}';
    }
}
