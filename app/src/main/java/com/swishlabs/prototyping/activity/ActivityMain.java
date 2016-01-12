package com.swishlabs.prototyping.activity;

import android.app.Activity;
import android.appwidget.AppWidgetManager;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.swishlabs.prototyping.MyApplication;
import com.swishlabs.prototyping.R;

public class ActivityMain extends Activity{
	
	protected final static String TAG = Activity.class.getSimpleName();
	private MyApplication mApp;
	private long exitTime;
	
	private Button signButton;
	private Button registerButton;
	private AppWidgetManager mManager;
	private AppWidgetManager mAppManager;
	
	public static boolean autoLoginIn;

	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
//		Logg.d(TAG,"onCreate");
		
//		startService(new Intent(this, CheckRequestService.class));
		


		mApp = (MyApplication) getApplication();
		
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

		if(MyApplication.getLoginStatus()) {
			Intent mIntent;
			mIntent = new Intent(ActivityMain.this,LoginActivity.class);
			startActivity(mIntent);
			finish();

		}else{
			setContentView(R.layout.activity_main1);
			setListeners();
		}

	}


	private void setListeners() {
		// TODO Auto-generated method stub
		signButton = (Button)findViewById(R.id.butSignIn);
		signButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent homeIntent = new Intent(getBaseContext(), LoginActivity.class);
				startActivity(homeIntent);
				finish();
			}
		});
		registerButton = (Button)findViewById(R.id.butSignUp);
		registerButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent registerIntent = new Intent(getBaseContext(), SignupActivity.class);
				startActivity(registerIntent);
				finish();
			}
		});
		
	}
	
	@Override
	protected void onStop(){
		super.onStop();
	}
	
	@Override
	protected void onStart(){
		super.onStart();

//		showUserAvatar(mApp.getOnlineUser());
		

	}
	
	@Override
	protected void onResume(){
		super.onResume();
	}
	
	@Override
	protected void onDestroy(){
		super.onDestroy();
		mApp.removeActivity(this);
	}
	
	@Override
	public void onBackPressed() {
		if ((SystemClock.elapsedRealtime() - exitTime) > 2000) {
//			ToastUtil.show(mApp, "Press one more time to exit the app!", true);
			exitTime = SystemClock.elapsedRealtime();
		} else {
 			quit();
		}
	}
	
	private void quit(){
		mApp.exit();
		finish();
	}
}