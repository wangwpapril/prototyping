package com.swishlabs.prototyping.data.api.model;

/**
 * Created by wwang on 15-05-22.
 */

public class Profile {
    public String id;
    public String userName;
    public String email;
    public String firstName;
    public String lastName;
    public String avatarUrl;
    public String backGroundUrl;
    public String occupation;
    public String phone;
    public double longitude;
    public double latitude;

    public String getId() { return id; }

    public String getUserName() { return userName; }

    public String getEmail() { return email; }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

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

    public void setId(String id) {
        this.id = id;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

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
}
