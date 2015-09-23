package com.swishlabs.prototyping.entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import net.tsz.afinal.annotation.sqlite.Table;

import java.io.Serializable;

/**
 * Created by wwang on 15-05-22.
 */

@Table(name = "Profile")
public class Profile implements Serializable {

    private int id;

    @Expose
    @SerializedName("id")
    private int sessionId;

    @Expose
    @SerializedName("username")
    private String userName;

    @Expose
    @SerializedName("email")
    private String email;

    @Expose
    @SerializedName("firstname")
    private String firstName;

    @Expose
    @SerializedName("lastname")
    private String lastName;

    @Expose
    @SerializedName("profile_pic")
    private String avatarUrl;

    @Expose
    @SerializedName("background")
    private String backGroundUrl;

    @Expose
    @SerializedName("displayname")
    private String displayName;

    @Expose
    @SerializedName("occupation")
    private String occupation;

    @Expose
    @SerializedName("phone")
    private String phone;

    @Expose
    @SerializedName("longitude")
    private double longitude;

    @Expose
    @SerializedName("latitude")
    private double latitude;

    @Expose
    @SerializedName("skillset")
    private String skillSet;

    @Expose
    @SerializedName("trusted")
    private boolean trusted;

    public String getUserName() { return userName; }

    public String getEmail() { return email; }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getDisplayName() { return displayName; }

    public String getPhone() {
        return phone;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public String getBackGroundUrl() {
        return backGroundUrl;
    }

    public String getOccupation() {
        return occupation;
    }

    public String getSkillSet() { return skillSet; }

    public boolean getTrusted() { return trusted; }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public void setBackGroundUrl(String backGroundUrl) {
        this.backGroundUrl = backGroundUrl;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setDisplayName(String displayName) { this.displayName = displayName; }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public void setOccupation(String occupation) {
        this.occupation = occupation;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setSkillSet(String skillSet) {
        this.skillSet = skillSet;
    }

    public void setTrusted(boolean trusted ) { this.trusted = trusted; }

    public int getSessionId() {
        return sessionId;
    }

    public void setSessionId(int sessionId) {
        this.sessionId = sessionId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
