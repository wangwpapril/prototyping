package com.swishlabs.prototyping.activity;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.swishlabs.prototyping.MyApplication;
import com.swishlabs.prototyping.data.ServiceManager;
import com.swishlabs.prototyping.data.store.Database;
import com.swishlabs.prototyping.data.store.DatabaseManager;
import com.swishlabs.prototyping.util.Common;


public abstract class BaseActivity extends Activity implements OnClickListener{

	protected BaseActivity context;
	protected TextView tvTitleName;
	protected ImageView ivTitleBack;
	protected ImageView ivTitleRight;

    public DatabaseManager mDatabaseManager;
    public Database mDatabase;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		requestWindowFeature(Window.FEATURE_NO_TITLE);
		Log.i(MyApplication.TAG, "Base Activity onCreate()");
		MyApplication.getInstance().addActivity(this);
		context = this;
		Common.context = this;
        loadDatabase();
	}

    public void loadDatabase(){
//		mDatabaseManager = new DatabaseManager(this.getBaseContext());
//		mDatabase = mDatabaseManager.openDatabase("Intrepid.db");
		mDatabaseManager = ServiceManager.getDatabaseManager();
		mDatabase = ServiceManager.getDatabase();
    }

	protected void initTitleView() {

	}

	protected abstract void initTitle();
	

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		Log.i(MyApplication.TAG, "Base Activity onDestory()");
		
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		
	}

	public void onBackClick(View view){
		onBackPressed();
	}

	@Override
	protected void onResume() {
		super.onResume();
		Common.context = this;
	}

	@Override
	protected void onPause() {
		super.onPause();
		Common.context = null;
	}
	
}
