package com.swishlabs.prototyping;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.multidex.MultiDexApplication;
import android.util.Log;

import com.crashlytics.android.Crashlytics;
import com.lidroid.xutils.DbUtils;
import com.swishlabs.prototyping.activity.MainActivity;
import com.swishlabs.prototyping.data.DataManager;
import com.swishlabs.prototyping.data.ServiceManager;
import com.swishlabs.prototyping.data.store.Database;
import com.swishlabs.prototyping.data.store.DatabaseManager;
import com.swishlabs.prototyping.util.AndroidLocationServices;
import com.swishlabs.prototyping.util.DeviceInfoHelper;
import com.swishlabs.prototyping.util.Enums;
import com.swishlabs.prototyping.util.Logger;
import com.swishlabs.prototyping.util.NotifyDispatcher;
import com.swishlabs.prototyping.util.SharedPreferenceUtil;

import java.lang.Thread.UncaughtExceptionHandler;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import io.fabric.sdk.android.Fabric;

import static com.lidroid.xutils.DbUtils.DbUpgradeListener;


public class MyApplication extends MultiDexApplication implements UncaughtExceptionHandler{
    
	public static String TAG="MyApplication";
//	public User currentUserInfo = null;
//	private UserService userService = null;
	private static DeviceInfoHelper deviceInfoHelper =null;
	
	private ArrayList<Activity> activityList;				
	
	private static boolean loginStatus = false;		
	private static Enums.NetStatus netStatus = null;
	private static String userId;								
	public static Object mLock;

    public DatabaseManager mDatabaseManager;
    public Database mDatabase;

	private static MyApplication instance;

	private DbUtils mFinalDb;

	private final String DBName = "afinal.db";

	public static final int DB_VERSION = 1;


	//////////////////////////////
	private boolean isInitOk = false;

	private ExecutorService mExecutor = Executors.newCachedThreadPool();

//	private UserInfo mOnlineUser;

//	private FinalDb mFinalDb;

	private List<Activity> mCacheTaskActivity = new ArrayList<Activity>();



	private NotifyDispatcher<DataType> mNotifydispatcher = new NotifyDispatcher<DataType>();

////////////////////////////
	
	public static MyApplication getInstance(){
		return instance;
	}

	public void exit(){
		for(Activity activity : activityList){
			try {
				activity.finish();
			} catch (Exception e) {
				Log.e(TAG, e.getMessage());
			}
		}
		
		
//		Intent locationIntent = new Intent(this, LocationService.class);
	//	stopService(locationIntent);
				
		System.exit(0);		
	}

	public void asyncLocationUpdates(){
		Intent locationServices = new Intent(getApplicationContext(), AndroidLocationServices.class);
		startService(locationServices);
	}

    public void logout(){
        SharedPreferenceUtil.setString(Enums.PreferenceKeys.userId.toString(), "");
        SharedPreferenceUtil.setString(Enums.PreferenceKeys.token.toString(), "");
        SharedPreferenceUtil.setString(Enums.PreferenceKeys.email.toString(), "");
        SharedPreferenceUtil.setString(Enums.PreferenceKeys.firstname.toString(), "");
        SharedPreferenceUtil.setString(Enums.PreferenceKeys.lastname.toString(), "");
        SharedPreferenceUtil.setString(Enums.PreferenceKeys.username.toString(), "");
        SharedPreferenceUtil.setString(Enums.PreferenceKeys.countryCode.toString(), "");
        SharedPreferenceUtil.setString(Enums.PreferenceKeys.currencyCode.toString(), "");
        SharedPreferenceUtil.setBoolean(Enums.PreferenceKeys.loginStatus.toString(), false);
        MyApplication.setLoginStatus(false);
//        activity.finish();
//        Intent mIntent = new Intent(activity, SplashActivity.class);
//        startActivity(mIntent);
    }
	
	public DeviceInfoHelper getDeviceInfoHelper(){
		if(deviceInfoHelper == null)
			deviceInfoHelper = new DeviceInfoHelper();
		return deviceInfoHelper;
	}


	
	public static String getUserId() {
		return userId;
	}

	public static void setUserId(String userId) {
		MyApplication.userId = userId;
	}
	
	public static Enums.NetStatus getNetStatus() {
		return netStatus;
	}

	public static void setNetStatus(Enums.NetStatus netStatus) {
		MyApplication.netStatus = netStatus;
	}
	
	public static boolean getLoginStatus() {
		return loginStatus;
	}

	public static void setLoginStatus(boolean loginStatus) {
		MyApplication.loginStatus = loginStatus;
	}

/*	public void addActivity(Activity activity){
		activityList.add(activity);
	}*/
	
	public ArrayList<Activity> getActivities(){
		refreshActivities();
		return activityList;
	}
	public void refreshActivities(){
		for(int i=0;i<activityList.size();i++){
			if(activityList.get(i).isFinishing())
				activityList.remove(i);
		}
	}

	HashMap<String, Object> intentHashMap;

	public HashMap<String, Object> getIntentHashMap() {
		if(intentHashMap==null){
			intentHashMap=new HashMap<String, Object>();
		}
		return intentHashMap;
	}

	public void setIntentHashMap(HashMap<String, Object> intentHashMap) {
		this.intentHashMap = intentHashMap;
	}
	

	@Override
	public void onCreate() {
		super.onCreate();
		Fabric.with(this, new Crashlytics());
		Log.i(TAG, "Application onCreate()");
		instance=this;
		mLock=new Object();
		ServiceManager.init(this);
//		asyncLocationUpdates();
		intentHashMap=new HashMap<String, Object>();
		loginStatus = SharedPreferenceUtil.getBoolean(Enums.PreferenceKeys.loginStatus.toString(), false);
		activityList = new ArrayList<Activity>();

		mFinalDb = DbUtils.create(this, DBName, DB_VERSION, new DbUpgradeListener() {
			@Override
			public void onUpgrade(DbUtils dbUtils, int i, int i1) {

			}
		});


		DataManager.getInstance().initialize();
//		Thread.setDefaultUncaughtExceptionHandler(this);
//	    loadDatabase();
	}

    public void loadDatabase(){
        mDatabaseManager = new DatabaseManager();
        mDatabase = mDatabaseManager.openDatabase("Intrepid.db");
    }

	
	public int getVersionCode() {
        try {
            return getPackageManager().getPackageInfo(getPackageName(), 0).versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            return -1;
        }
	}
	
	public void uncaughtException(Thread thread, Throwable ex) {
		Logger.e(ex);
		exit();
        android.os.Process.killProcess(android.os.Process.myPid());  
        System.exit(1);	
		
	}

	/////////////////////////////////////
	/**
	 * Execute a asynchronize task.
	 *
	 * @param task
	 */
	public void execute(Runnable task) {
		mExecutor.execute(task);
	}

	public void addActivity(Activity item) {
		mCacheTaskActivity.add(item);
	}

	public void removeActivity(Activity item) {
		mCacheTaskActivity.remove(item);
	}

/*	public void removeMoreActivity(){
		for (Activity item : mCacheTaskActivity) {
			if(item instanceof MoreActivity){
				item.finish();
				break;
			}
		}
	}*/

	public void removeAllExceptMain(){
		for (Activity item : mCacheTaskActivity) {
			if(item instanceof MainActivity){

			}else{
				item.finish();
			}
		}
	}

	public List<Activity> getActivityList(){
		return mCacheTaskActivity;
	}

/*	public void exit() {
		for (Activity item : mCacheTaskActivity) {
			item.finish();
		}
	}*/


	public void registerDataListener(DataType type, NotifyDispatcher.IDataSourceListener<DataType> listener) {
		mNotifydispatcher.registerDataListener(type, listener);
	}

	public void unRegisterDataListener(NotifyDispatcher.IDataSourceListener<DataType> listener) {
		mNotifydispatcher.unRegisterDataListener(listener);
	}

	/**
	 * Notify a data source changed for each registered listener by speical
	 * type.
	 *
	 * @param type
	 */
	public void notifyDataChanged(DataType type) {
//		LogUtil.d(TAG, " try to notify " + type.toString());
		mNotifydispatcher.notifyDataChanged(type);
	}



	/**
	 * Define some state to mark which type data has changed.
	 *
	 */
	public enum  DataType{
		/**
		 * online user info changed
		 */
		userinfo,
		/**
		 * custom order data changed
		 */
		order,

		/**
		 * accept order data changed
		 */
		accpet_order,
		/**
		 * attention relationship changed.
		 */
		attention,
		/**
		 * push a message.
		 */
		notify_msg,
		/**
		 * push a order info
		 */
		notify_order,

		/**
		 * switch order tab page.
		 */
		switch_order_tab,

		/**
		 * switch square tab page.
		 */
		switch_square_tab,

		/**
		 * switch myself tab page.
		 */
		switch_myself_publish_tab,
		/**
		 * Draft data changed.
		 */
		notify_draft,

		/**
		 * A normal video posted successfully
		 */
		video_posted,
		/**
		 * A custom video posted successfully
		 */
		video_custom_posted,
		/**
		 * A bless video posted successfully
		 */
		video_bless_posted,

		/**
		 * A bless video posted successfully
		 */
		anchor_apply_passed

	};
//////////////////////////////////////
}