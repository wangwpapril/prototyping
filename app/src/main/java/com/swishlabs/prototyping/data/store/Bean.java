package com.swishlabs.prototyping.data.store;

import com.swishlabs.prototyping.data.ServiceManager;

public abstract class Bean {
    protected Database db;
    public Bean() {
        db = ServiceManager.getDatabaseManager().openDatabase(getDatabaseName());
        createTable();
    }
    public String getDatabaseName() {
        return "Intrepid.db";
    }
    public Database getDatabase() {
        return db;
    }
    /* Abstract create table method */
    public abstract void createTable();
}
