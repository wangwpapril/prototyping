package com.swishlabs.prototyping.entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.swishlabs.prototyping.data.api.model.Parent;

import net.tsz.afinal.annotation.sqlite.Id;
import net.tsz.afinal.annotation.sqlite.OneToMany;
import net.tsz.afinal.annotation.sqlite.Table;
import net.tsz.afinal.db.sqlite.OneToManyLazyLoader;

import java.io.Serializable;

/**
 * Created by wwang on 15-09-17.
 */

@Table(name = "Service")
public class Service implements Serializable {

    private static final long serialVersionUID = -1634404573461075056L;

    @Id(column="id")
    private int id;

    @Expose
    @SerializedName("id")
    private int serviceId;

    @Expose
    @SerializedName("title")
    private String title;

    @Expose
    @SerializedName("summary")
    private String summary;

    @Expose
    @SerializedName("hourlyRateFrom")
    private int hourlyRateFrom;

    @Expose
    @SerializedName("hourlyRateTo")
    private int hourlyRateTo;
    @Expose
    @SerializedName("fixedPrice")
    private int fixedPrice;

    @Expose
    @SerializedName("serviceExchange")
    private boolean serviceExchange;

    @Expose
    @SerializedName("businessPartnership")
    private boolean businessPartnership;

    @Expose
    @SerializedName("internship")
    private boolean internship;

    @Expose
    @SerializedName("charity")
    private boolean charity;

    @Expose
    @SerializedName("hourlyRate")
    private boolean hourlyRate;

    @Expose
    @SerializedName("numberReviews")
    private int numberReviews;

    @Expose
    @SerializedName("numberViews")
    private int numberViews;

    @Expose
    @SerializedName("numberTrades")
    private int numberTrades;

    @Expose
    @SerializedName("serviceType")
    private int serviceType;

    @Expose
    @SerializedName("matchPercentage")
    private int matchPercentage;

    @Expose
    @SerializedName("user")
    private User user;

    @Expose
    @SerializedName("alliance")
    private Alliance alliance;

    @OneToMany(manyColumn = "serviceId")
    private OneToManyLazyLoader<Service,User> children;

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getHourlyRateFrom() {
        return hourlyRateFrom;
    }

    public void setHourlyRateFrom(int hourlyRateFrom) {
        this.hourlyRateFrom = hourlyRateFrom;
    }

    public int getHourlyRateTo() {
        return hourlyRateTo;
    }

    public void setHourlyRateTo(int hourlyRateTo) {
        this.hourlyRateTo = hourlyRateTo;
    }

    public int getFixedPrice() {
        return fixedPrice;
    }

    public void setFixedPrice(int fixedPrice) {
        this.fixedPrice = fixedPrice;
    }

    public boolean isServiceExchange() {
        return serviceExchange;
    }

    public void setServiceExchange(boolean serviceExchange) {
        this.serviceExchange = serviceExchange;
    }

    public boolean isBusinessPartnership() {
        return businessPartnership;
    }

    public void setBusinessPartnership(boolean businessPartnership) {
        this.businessPartnership = businessPartnership;
    }

    public boolean isInternship() {
        return internship;
    }

    public void setInternship(boolean internship) {
        this.internship = internship;
    }

    public boolean isCharity() {
        return charity;
    }

    public void setCharity(boolean charity) {
        this.charity = charity;
    }

    public boolean isHourlyRate() {
        return hourlyRate;
    }

    public void setHourlyRate(boolean hourlyRate) {
        this.hourlyRate = hourlyRate;
    }

    public int getNumberReviews() {
        return numberReviews;
    }

    public void setNumberReviews(int numberReviews) {
        this.numberReviews = numberReviews;
    }

    public int getNumberViews() {
        return numberViews;
    }

    public void setNumberViews(int numberViews) {
        this.numberViews = numberViews;
    }

    public int getNumberTrades() {
        return numberTrades;
    }

    public void setNumberTrades(int numberTrades) {
        this.numberTrades = numberTrades;
    }

    public int getServiceType() {
        return serviceType;
    }

    public void setServiceType(int serviceType) {
        this.serviceType = serviceType;
    }

    public int getMatchPercentage() {
        return matchPercentage;
    }

    public void setMatchPercentage(int matchPercentage) {
        this.matchPercentage = matchPercentage;
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

    public int getServiceId() {
        return serviceId;
    }

    public void setServiceId(int serviceId) {
        this.serviceId = serviceId;
    }

    public OneToManyLazyLoader<Service, User> getChildren() {
        return children;
    }

    public void setChildren(OneToManyLazyLoader<Service, User> children) {
        this.children = children;
    }
}
