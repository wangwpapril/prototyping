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
    }

    public void initialize() {
        profileAroundList.clear();
        connectionList.clear();
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
}
