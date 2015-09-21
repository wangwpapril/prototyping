package com.swishlabs.prototyping.entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import net.tsz.afinal.annotation.sqlite.Table;

import java.io.Serializable;

/**
 * Created by wwang on 15-09-17.
 */

@Table(name = "Alliance")
public class Alliance implements Serializable {

    @Expose
    @SerializedName("id")
    private int id;

    @Expose
    @SerializedName("displaymembers")
    private boolean displaymembers;

    @Expose
    @SerializedName("isfull")
    private boolean isfull;

    @Expose
    @SerializedName("serviceid")
    private int serviceid;

    @Expose
    @SerializedName("createdat")
    private String createdat;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isDisplaymembers() {
        return displaymembers;
    }

    public void setDisplaymembers(boolean displaymembers) {
        this.displaymembers = displaymembers;
    }

    public boolean isfull() {
        return isfull;
    }

    public void setIsfull(boolean isfull) {
        this.isfull = isfull;
    }

    public int getServiceid() {
        return serviceid;
    }

    public void setServiceid(int serviceid) {
        this.serviceid = serviceid;
    }

    public String getCreatedat() {
        return createdat;
    }

    public void setCreatedat(String createdat) {
        this.createdat = createdat;
    }
}
