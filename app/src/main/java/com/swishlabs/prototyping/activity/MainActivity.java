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
import android.widget.AdapterView;
import android.widget.ImageView;

import com.swishlabs.prototyping.MyApplication;
import com.swishlabs.prototyping.R;
import com.swishlabs.prototyping.fragment.BaseFragment;
import com.swishlabs.prototyping.fragment.CardStackFragment;
import com.swishlabs.prototyping.fragment.MyProfileFragment;
import com.swishlabs.prototyping.fragment.PreHomeFragment;
import com.swishlabs.prototyping.fragment.SettingFragment;
import com.swishlabs.prototyping.fragment.SwipeFragment;
import com.swishlabs.prototyping.services.RequestCheckService;

public class MainActivity extends BaseFragmentActivity {


    private BaseFragment mPreHomeFragment;
    private BaseFragment mCurrFragment;
    public BaseFragment mSwipeFragment;
    private BaseFragment mMyProfileFragment;
    public BaseFragment mCardStackFragment;
    private BaseFragment mSettingFragment;
    private ImageView mDrawerImage;
    private MyApplication mApp;

    private PendingIntent pendingIntent;

    private MainActivity instance;

//    private List<Profile> profileList;
//    private int profileOffset;
//    private boolean noMoreData;
//    private boolean initialData;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

//        Bundle bundle = getIntent().getExtras();
//        if (bundle != null){
//            profileList = (List<Profile>) bundle.getSerializable(LoginLoadingActivity.PROFILE_LIST);
//            profileOffset = bundle.getInt(LoginLoadingActivity.PROFILE_OFFSET);
//            noMoreData = bundle.getBoolean(LoginLoadingActivity.NO_MORE_DATA);
//            initialData = true;
//        }else {
//            profileList = null;
//            profileOffset = -1;
//            noMoreData = false;
//            initialData = false;
//        }

        instance = this;

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
        mCardStackFragment = CardStackFragment.newInstance(null,null);
        mSettingFragment = SettingFragment.newInstance(null,null);

        switchFragment(mPreHomeFragment);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position < mSlideMenuAdapter.getCount()) {
                    getSlidingMenu().toggle();

                    if (position == SIGNOUT) {
                        MyApplication.getInstance().logout();
                        Intent mainIntent = new Intent(getBaseContext(), ActivityMain.class);
                        mainIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(mainIntent);
                        finish();

 /*                       Editor editor = GrabopApplication.SharedPreference.edit();
                        editor.putBoolean(GrabOpSharedPreference.AUTOLOGIN, false);
                        editor.commit();

                        ResourceManager.isAdvanceSearchProfiles = false;

                        GrabOpRestful.ClearAllList();
                        ResourceManager.NewUser();
                        stopWidget();

                        Intent mainIntent = new Intent(getBaseContext(), ActivityMain.class);
                        mainIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(mainIntent);
                        finish();*/
                    }

                    if (position == DEFAULTSETTING) {
                        switchFragment(mSettingFragment);
/*                        Intent defaultSettingIntent = new Intent(getBaseContext(), ActivityDefaultSetting.class);
                        startActivityForResult(defaultSettingIntent, DEFAULT_SETTING);*/
                    }

                    if (position == MYCONTACTS) {
                        switchFragment(mPreHomeFragment);
 /*                       Intent mainIntent = new Intent(getBaseContext(), ActivityMyContacts.class);
                        startActivity(mainIntent);
                        finish();*/
                    }

                    if (position == MYPROFILE) {
                        switchFragment(mMyProfileFragment);
/*                        Intent myProfileIntent = new Intent(getBaseContext(), ActivityMyProfile.class);
                        startActivity(myProfileIntent);
                        finish();*/
                    }

                    if (position == MYHOME) {
                        switchFragment(mPreHomeFragment);
                    }

//                    GrabOpRestful.AddedNotifyId.clear();
                    //                  BackgroundTask.GetInstance(mActivity).destroy();

                } else {
                    // Intent intent = new Intent(getBaseContext(),
                    // CustomOrderActivity.class);
                    // startActivity(intent);
                    // getSlidingMenu().toggle();
                }

            }
        });


//        List<Profile> profile = mFinalDb.findAll(Profile.class);
//        List<Profile> profileList = null;
//        try {
//            profileList = mFinalDb.findAll(Profile.class);
//        } catch (DbException e) {
//            e.printStackTrace();
//        }
//
//        List<Service> services = null;
//        try {
//            services = mFinalDb.findAll(Service.class);
//        } catch (DbException e) {
//            e.printStackTrace();
//        }

//        List<Service> services = mFinalDb.findAll(Service.class);

    }

    public void switchFragment(BaseFragment fragment) {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.container, fragment, fragment.getClass().getSimpleName());
        ft.commitAllowingStateLoss();
        mCurrFragment = fragment;
    }

//    public List<Profile> getProfileList() {
//        return profileList;
//    }

//    public int getProfileOffset() {
//        return profileOffset;
//    }
//
//    public boolean getNoMoreData() {
//        return noMoreData;
//    }
//
//    public boolean getInitialData() {
//        return initialData;
//    }

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
        startCheckService();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        stopCheckService();
    }

    public void startCheckService(){
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, RequestCheckService.class);
        intent.setAction(RequestCheckService.ACTION_CHECK_REQUEST);
        pendingIntent = PendingIntent.getService(this, 1, intent, PendingIntent.FLAG_CANCEL_CURRENT);
        alarmManager.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, 0, 10 * 1000, pendingIntent);
    }

    public void stopCheckService(){
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(this.pendingIntent);
    }

}
