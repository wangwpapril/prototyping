package com.swishlabs.prototyping.entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import net.tsz.afinal.annotation.sqlite.Table;

import java.io.Serializable;

/**
 * Created by wwang on 15-09-17.
 */

@Table(name = "ProfileAround")
public class ProfileAround implements Serializable{


    private int id;

    @Expose
    @SerializedName("id")
    private int sessionId;

    @Expose
    @SerializedName("distance")
    private int distance;

    public int getSessionId() {
        return sessionId;
    }

    public void setSessionId(int sessionId) {
        this.sessionId = sessionId;
    }

    public int getDistance() {
        return distance;
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
