package com.swishlabs.prototyping.data.api.model;

/**
 * Created by wwang on 15-04-16.
 */
public class Currency {
    String currencyCode;
    String imageUrl;
    public Currency(String code, String url) {
        this.currencyCode = code;
        this.imageUrl = url;
    }

    public String getCurrencyCode() { return currencyCode; }
    public String getImageUrl() { return  imageUrl; }
}
