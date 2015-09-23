package com.swishlabs.prototyping.entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import net.tsz.afinal.annotation.sqlite.Table;

import java.io.Serializable;

/**
 * Created by wwang on 15-09-17.
 */

@Table(name = "User")
public class User implements Serializable{

    private int id;

    @Expose
    @SerializedName("id")
    private int userId;

    @Expose
    @SerializedName("username")
    private String userName;

    @Expose
    @SerializedName("firstname")
    private String firstName;

    @Expose
    @SerializedName("lastname")
    private String lastName;

    @Expose
    @SerializedName("profile_pic")
    private String profilePic;

    @Expose
    @SerializedName("longitude")
    private double longitude;

    @Expose
    @SerializedName("latitude")
    private double latitude;

    @Expose
    @SerializedName("user")
    private User user;

    @Expose
    @SerializedName("alliance")
    private Alliance alliance;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getProfilePic() {
        return profilePic;
    }

    public void setProfilePic(String profilePic) {
        this.profilePic = profilePic;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Alliance getAlliance() {
        return alliance;
    }

    public void setAlliance(Alliance alliance) {
        this.alliance = alliance;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }
}
