package com.swishlabs.prototyping.data.api.model;

import java.util.Date;

/**
 * Created by will on 15/5/20.
 */
public class Connection {
    public static final String SENT = "Sent";
    public static final String CONNECT = "confirmed";
    public static final String PASS = "rejected";


    public int fromId;
    public int toId;

    public Date dateConnectionRequest = new Date();

    public boolean notified;
    public String status;

    public Date dateConnectionConfirmed = new Date();

}
