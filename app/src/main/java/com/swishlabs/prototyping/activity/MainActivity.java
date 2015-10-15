package com.swishlabs.prototyping.activity;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.lidroid.xutils.exception.DbException;
import com.swishlabs.prototyping.MyApplication;
import com.swishlabs.prototyping.R;
import com.swishlabs.prototyping.entity.Profile;
import com.swishlabs.prototyping.entity.Service;
import com.swishlabs.prototyping.fragment.BaseFragment;
import com.swishlabs.prototyping.fragment.MyProfileFragment;
import com.swishlabs.prototyping.fragment.PreHomeFragment;
import com.swishlabs.prototyping.fragment.SwipeFragment;
import com.swishlabs.prototyping.services.RequestCheckService;

import java.util.List;

public class MainActivity extends BaseFragmentActivity {


    private BaseFragment mPreHomeFragment;
    private BaseFragment mCurrFragment;
    private BaseFragment mSwipeFragment;
    private BaseFragment mMyProfileFragment;
    private ImageView mDrawerImage;
    private MyApplication mApp;

    private PendingIntent pendingIntent;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        mDrawerImage = (ImageView)findViewById(R.id.drawerImage);
        mDrawerImage.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                getSlidingMenu().showMenu();

            }
        });

 //       getSlidingMenu().setSlidingEnabled(true);


        mPreHomeFragment = PreHomeFragment.newInstance(null, null);
        mSwipeFragment = SwipeFragment.newInstance(null,null);
        mMyProfileFragment = MyProfileFragment.newInstance(null,null);

        switchFragment(mMyProfileFragment);

//        List<Profile> profile = mFinalDb.findAll(Profile.class);
        List<Profile> profileList = null;
        try {
            profileList = mFinalDb.findAll(Profile.class);
        } catch (DbException e) {
            e.printStackTrace();
        }

        List<Service> services = null;
        try {
            services = mFinalDb.findAll(Service.class);
        } catch (DbException e) {
            e.printStackTrace();
        }

//        List<Service> services = mFinalDb.findAll(Service.class);

    }

    void switchFragment(BaseFragment fragment) {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.container, fragment, fragment.getClass().getSimpleName());
        ft.commitAllowingStateLoss();
        mCurrFragment = fragment;
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onResume() {
        super.onResume();
//        startCheckService();
    }

    public void startCheckService(){
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, RequestCheckService.class);
        intent.setAction(RequestCheckService.ACTION_CHECK_REQUEST);
        pendingIntent = PendingIntent.getService(this, 1, intent, PendingIntent.FLAG_CANCEL_CURRENT);
        alarmManager.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, 0, 10*1000, pendingIntent);
    }

}
