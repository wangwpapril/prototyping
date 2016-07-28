package com.swishlabs.prototyping.activity;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.SystemClock;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;

import com.cloudinary.Cloudinary;
import com.cloudinary.android.Utils;
import com.swishlabs.prototyping.MyApplication;
import com.swishlabs.prototyping.R;
import com.swishlabs.prototyping.entity.UserProfilePrefs;
import com.swishlabs.prototyping.fragment.BaseFragment;
import com.swishlabs.prototyping.fragment.CardStackFragment;
import com.swishlabs.prototyping.fragment.ConnectionsFragment;
import com.swishlabs.prototyping.fragment.MessageFragment;
import com.swishlabs.prototyping.fragment.MyOffersFragment;
import com.swishlabs.prototyping.fragment.MyProfileDetailsFragment;
import com.swishlabs.prototyping.fragment.MyProfileFragment;
import com.swishlabs.prototyping.fragment.PreHomeFragment;
import com.swishlabs.prototyping.fragment.ProfileConnectionFragment;
import com.swishlabs.prototyping.fragment.RequestsFragment;
import com.swishlabs.prototyping.fragment.SettingFragment;
import com.swishlabs.prototyping.fragment.SwipeFragment;
import com.swishlabs.prototyping.helper.CameraHelper;
import com.swishlabs.prototyping.services.RequestCheckService;
import com.swishlabs.prototyping.util.ContentUriUtils;
import com.swishlabs.prototyping.util.PictureUtils;
import com.swishlabs.prototyping.util.ToastUtil;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends BaseFragmentActivity implements MyProfileDetailsFragment.onButtonPressedListener,
        MyOffersFragment.OnFragmentInteractionListener{

    protected final static int REQUEST_CODE_SELECT_PHOTO = 200;

    protected final static int REQUEST_CODE_CAMERA_PHOTO = 201;

    protected final static int REQUEST_CODE_CROP_IMAGE_SELECT = 202;

    protected final static int REQUEST_CODE_CROP_IMAGE_CAMERA = 203;

    private BaseFragment mPreHomeFragment;
    public BaseFragment mCurrFragment;
    public BaseFragment mSwipeFragment;
    private BaseFragment mMyProfileFragment;
    public BaseFragment mCardStackFragment;
    private BaseFragment mSettingFragment;
    private BaseFragment mConnectionsFragment;
    private BaseFragment mRequestsFragment;
    private BaseFragment mMessageFragment;
    private ImageView mDrawerImage;
    private MyApplication mApp;

    private PendingIntent pendingIntent;

    private MainActivity instance;
    private long exitTime;
    private File mFileCamera;
    private File mFileCrop;
    private File  mFileSelect;

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

        mApp = MyApplication.getInstance();

        if (gpsTracker.canGetLocation()) {
            UserProfilePrefs.getInstance().setMyLocation(gpsTracker.getLocation());
        }else {
            gpsTracker.showSettingsAlert();
        }

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
        mMyProfileFragment = new MyProfileFragment();
        mCardStackFragment = CardStackFragment.newInstance(null,null);
        mSettingFragment = SettingFragment.newInstance(null,null);
        mConnectionsFragment = ConnectionsFragment.newInstance(null, null);
        mRequestsFragment = new RequestsFragment();
        mMessageFragment = new MessageFragment();

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

                    if (position == SETTINGS) {
                        switchFragment(mSettingFragment);
/*                        Intent defaultSettingIntent = new Intent(getBaseContext(), ActivityDefaultSetting.class);
                        startActivityForResult(defaultSettingIntent, DEFAULT_SETTING);*/
                    }

                    if (position == MESSAGES) {
                        switchFragment(mMessageFragment);
                    }

                    if (position == CONNECTIONS) {
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

                    if (position == CONNECTIONS) {
                        switchFragment(mConnectionsFragment);
                    }

                    if (position == REQUESTS) {
                        switchFragment(mRequestsFragment);
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

    }

    public void switchFragment(BaseFragment fragment) {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.container, fragment, fragment.getClass().getSimpleName());
        if (fragment instanceof SettingFragment) {
            ft.addToBackStack(mCurrFragment.getTag());
        }
        ft.commit();
        mCurrFragment = fragment;
    }

    public void removeCurrFragment() {
        if (mCurrFragment == null)
            return;

        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.remove(mCurrFragment).commit();
        mCurrFragment = (BaseFragment) fm.findFragmentByTag(fm.getBackStackEntryAt(fm.getBackStackEntryCount() -1).getName());
        fm.popBackStack();
//        mCurrFragment = fm.addOnBackStackChangedListener(new FragmentManager.OnBackStackChangedListener() {
//            @Override
//            public void onBackStackChanged() {
//                fm.
//            }
//        });

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

    @Override
    public void onBackPressed() {
        Fragment fragment = getSupportFragmentManager().findFragmentByTag(ProfileConnectionFragment.class.getSimpleName());
        if (fragment == null) {
            if ((SystemClock.elapsedRealtime() - exitTime) > 2000) {
                ToastUtil.showToast(this, "Press one more time to exit the app!");
                exitTime = SystemClock.elapsedRealtime();
            } else {
                super.onBackPressed();
            }

        }else {
            super.onBackPressed();
        }
    }

    public BaseFragment getCurrentFragment() {
        return mCurrFragment;
    }

    @Override
    public void onButtonPressed(int button) {

        switch (button) {
            case MyProfileDetailsFragment.CAMERA_PHOTO:
                openCamera();
                break;
            case MyProfileDetailsFragment.SELECT_IMAGE:
                openImageGallery();
                break;
        }

    }

    private void openCamera()
    {
//        Intent cameraIntent = new Intent(this, MyCamera.class);
//
//        cameraIntent.putExtra("USERNAME", ResourceManager.UserProfile.userName.toString());
//
//        startActivityForResult(cameraIntent, CAMERA_PHOTO);

        Intent intent_camera = new Intent("android.media.action.IMAGE_CAPTURE");

        mFileCamera = CameraHelper.getOutputMediaFile(CameraHelper.MEDIA_TYPE_IMAGE);
        intent_camera.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(mFileCamera));
        startActivityForResult(intent_camera, REQUEST_CODE_CAMERA_PHOTO);

    }

    private void openImageGallery()
    {
//        Intent imageI = new Intent();
//        imageI.setType("image/*");
//        imageI.setAction(Intent.ACTION_PICK);
//        startActivityForResult(Intent.createChooser(imageI, "Select image.."), MyProfileDetailsFragment.SELECT_IMAGE);

        Intent intent=new Intent(Intent.ACTION_GET_CONTENT);//ACTION_OPEN_DOCUMENT
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("image/*");
        startActivityForResult(intent, REQUEST_CODE_SELECT_PHOTO);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != Activity.RESULT_OK || REQUEST_CODE_CAMERA_PHOTO != requestCode && data == null) {
            return;
        }

        switch (requestCode) {
            case REQUEST_CODE_CAMERA_PHOTO:
                String sdStatus = Environment.getExternalStorageState();
                if (sdStatus.equals(Environment.MEDIA_MOUNTED)) {
                    startActivityForResult(buildCropImageIntent(mFileCamera), REQUEST_CODE_CROP_IMAGE_CAMERA);
                }
                break;
            case REQUEST_CODE_SELECT_PHOTO:
                final Uri originalUri = data.getData();
                String filePath = ContentUriUtils.getPath(this, originalUri);
                mFileSelect = new File(filePath);
                startActivityForResult(buildCropImageIntent(mFileSelect), REQUEST_CODE_CROP_IMAGE_SELECT);
                break;
            case REQUEST_CODE_CROP_IMAGE_SELECT:
            case REQUEST_CODE_CROP_IMAGE_CAMERA:
                String fileName = "";
                Uri uri = data.getData();
                if (uri != null) {
                    fileName = ContentUriUtils.getPath(this, uri);
                } else if (mFileCrop.exists()) {
                    fileName = mFileCrop.getAbsolutePath();
                } else {
                    if (requestCode == REQUEST_CODE_CROP_IMAGE_CAMERA) {
                        fileName = mFileCamera.getAbsolutePath();
                    } else {
                        fileName = mFileSelect.getAbsolutePath();
                    }
                }
                final String finalPath = fileName;

                doSave(fileName);
//
//                MyApplication.getInstance().execute(new Runnable() {
//
//                    @Override
//                    public void run() {
//                        handlePickedImage(finalPath);
//                    }
//                });

                break;
            default:
                break;
        }

    }

    private Intent buildCropImageIntent(File file) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(Uri.fromFile(file), "image/*");
        intent.putExtra("crop", "true");
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        intent.putExtra("outputX", 96);
        intent.putExtra("outputY", 96);
        intent.putExtra("scale", true);
        intent.putExtra("noFaceDetection", true);
        intent.putExtra("return-data", true);
        mFileCrop = CameraHelper.getOutputMediaFile(CameraHelper.MEDIA_TYPE_IMAGE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(mFileCrop));
        return intent;
    }

    private void handlePickedImage(String fileName) {
        Bitmap myBitmap = PictureUtils.getSmallBitmap(fileName);
        int angle = PictureUtils.getAngle(fileName);
        if (angle != 0) {
            Matrix m = new Matrix();
            int width = myBitmap.getWidth();
            int height = myBitmap.getHeight();
            m.setRotate(angle); // 旋转angle度
            myBitmap = Bitmap.createBitmap(myBitmap, 0, 0, width, height, m, true);// 从新生成图片
        }
        new File(fileName).delete();
        final Bitmap roundBitmap = PictureUtils.toRoundBitmap(myBitmap);

//        doSave()
//        runOnUiThread(new Runnable() {
//            public void run() {
//                mImgViewAvatar.setImageBitmap(roundBitmap);
//                mNewAvatar = roundBitmap;
////                doSave();
//            }
//        });
    }

    private void doSave(final String fileName) {
        mApp.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    Cloudinary cloudinary = new Cloudinary(Utils.cloudinaryUrlFromContext(getApplicationContext()));

                    Map map = cloudinary.uploader().upload(fileName, null);

                    if (map != null) {
                        String url = map.get("url").toString();
                        if (url != null) {
                            if (mCurrFragment instanceof MyProfileFragment) {
                                ((MyProfileFragment) mCurrFragment).updateAvatar(url);

                            }
                        }
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        });
    }
}
