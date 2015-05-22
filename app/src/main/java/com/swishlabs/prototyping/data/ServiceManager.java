package com.swishlabs.prototyping.data;

import android.content.Context;

import com.swishlabs.prototyping.data.store.Database;
import com.swishlabs.prototyping.data.store.DatabaseManager;

public class ServiceManager {
    static Context context;
//    static NetworkService networkService;
    static DatabaseManager databaseManager;
    static Database mDatabase;
//    static DataService dataService;

    public static void init(Context ctx) {
        context = ctx;
//        dataService = new DataService();
//        networkService = new NetworkService();
        databaseManager = new DatabaseManager(ctx);
        mDatabase = databaseManager.openDatabase("Intrepid.db");
    }
/*    public static NetworkService getNetworkService() {
        return networkService;
    }


*/
    public static DatabaseManager getDatabaseManager() {
        return databaseManager;
    }

    public static Database getDatabase() { return mDatabase; }
/*    public static DataService getDataService() {
        return dataService;
    }
 */   
}
