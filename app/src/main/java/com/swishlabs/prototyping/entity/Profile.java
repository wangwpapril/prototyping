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
    @SerializedName("company")
    private String company;

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
    @SerializedName("post")
    private String post;

    @Expose
    @SerializedName("trusted")
    private boolean trusted;

    @Expose
    @SerializedName("city")
    private int city;

    private int oppNum;

    public Profile(int sessionId, String userName, String avatarUrl) {
        this.sessionId = sessionId;
        this.userName = userName;
        this.avatarUrl = avatarUrl;
    }
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

    public int getOppNum() {
        return oppNum;
    }

    public void setOppNum(int oppNum) {
        this.oppNum = oppNum;
    }

    public String getPost() {
        return post;
    }

    public void setPost(String post) {
        this.post = post;
    }

    public int getCity() {
        return city;
    }

    public void setCity(int city) {
        this.city = city;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public static class Builder {
        private int sessionId;
        private String firstName;
        private String lastName;
        private String displayName;
        private String userName;
        private String avatarUrl;
        private String job;
        private String portraitUrl;
        private String coverPhotoUrl;

        public Builder setId(int id) {
            this.sessionId = id;
            return this;
        }

        public Builder setUserName(String userName) {
            this.userName = userName;
            return this;
        }

        public Builder setUserAvatar(String userAvatar) {
            this.avatarUrl = userAvatar;
            return this;
        }

        public Builder setFirstName(String firstName) {
            this.firstName = firstName;
            return this;
        }

        public Builder setLastName(String lastName) {
            this.lastName = lastName;
            return this;
        }

        public Builder setDisplayName(String displayName) {
            this.displayName = displayName;
            return this;
        }

        public Builder setJob(String job) {
            this.job = job;
            return this;
        }

        public Builder setPortraitUrl(String portraitUrl) {
            this.portraitUrl = portraitUrl;
            return this;
        }

        public Builder setCoverPhotoUrl(String coverPhotoUrl) {
            this.coverPhotoUrl = coverPhotoUrl;
            return this;
        }

        public Profile build() {
            return new Profile(sessionId, userName, avatarUrl);
        }
    }

}
