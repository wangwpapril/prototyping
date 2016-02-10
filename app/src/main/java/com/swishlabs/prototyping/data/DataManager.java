package com.swishlabs.prototyping.data;

import com.swishlabs.prototyping.entity.Profile;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by will on 16/2/5.
 */
public class DataManager {
    private static DataManager instance;
    private List<Profile> profileAroundList;
    private int profileAroundOffset;
    private boolean profileAroundMoreData;
    private List<Profile> connectionList;
    private int connectionOffset;
    private boolean connectionMoreData;
    private List<Profile> receivedRequestList;
    private int receivedRequestOffset;
    private boolean receivedRequestMoreData;
    private List<Profile> sentRequestList;
    private int sentRequestOffset;
    private boolean sentRequestMoreData;

    public static DataManager getInstance() {
        if (instance == null) {
            synchronized (DataManager.class) {
                instance = new DataManager();
            }
        }
        return instance;
    }

    private DataManager() {
        profileAroundList = new ArrayList<>();
        connectionList = new ArrayList<>();
        receivedRequestList = new ArrayList<>();
        sentRequestList = new ArrayList<>();
    }

    public void initialize() {
        profileAroundList.clear();
        connectionList.clear();
        receivedRequestList.clear();
        sentRequestList.clear();
    }

    public List<Profile> getProfileAroundList() {
        return profileAroundList;
    }

    public void setProfileAroundList(List<Profile> profileAroundList) {
        this.profileAroundList = profileAroundList;
    }

    public List<Profile> getConnectionList() {
        return connectionList;
    }

    public void setConnectionList(List<Profile> connectionList) {
        this.connectionList = connectionList;
    }

    public int getProfileAroundOffset() {
        return profileAroundOffset;
    }

    public void setProfileAroundOffset(int profileAroundOffset) {
        this.profileAroundOffset = profileAroundOffset;
    }

    public boolean getProfileAroundMoreData() {
        return profileAroundMoreData;
    }

    public void setProfileAroundMoreData(boolean profileAroundMoreData) {
        this.profileAroundMoreData = profileAroundMoreData;
    }

    public int getConnectionOffset() {
        return connectionOffset;
    }

    public void setConnectionOffset(int connectionOffset) {
        this.connectionOffset = connectionOffset;
    }

    public boolean isConnectionMoreData() {
        return connectionMoreData;
    }

    public void setConnectionMoreData(boolean connectionMoreData) {
        this.connectionMoreData = connectionMoreData;
    }

    public List<Profile> getReceivedRequestList() {
        return receivedRequestList;
    }

    public void setReceivedRequestList(List<Profile> receivedRequestList) {
        this.receivedRequestList = receivedRequestList;
    }

    public int getReceivedRequestOffset() {
        return receivedRequestOffset;
    }

    public void setReceivedRequestOffset(int receivedRequestOffset) {
        this.receivedRequestOffset = receivedRequestOffset;
    }

    public boolean isReceivedRequestMoreData() {
        return receivedRequestMoreData;
    }

    public void setReceivedRequestMoreData(boolean receivedRequestMoreData) {
        this.receivedRequestMoreData = receivedRequestMoreData;
    }

    public List<Profile> getSentRequestList() {
        return sentRequestList;
    }

    public void setSentRequestList(List<Profile> sentRequestList) {
        this.sentRequestList = sentRequestList;
    }

    public int getSentRequestOffset() {
        return sentRequestOffset;
    }

    public void setSentRequestOffset(int sentRequestOffset) {
        this.sentRequestOffset = sentRequestOffset;
    }

    public boolean isSentRequestMoreData() {
        return sentRequestMoreData;
    }

    public void setSentRequestMoreData(boolean sentRequestMoreData) {
        this.sentRequestMoreData = sentRequestMoreData;
    }
}
