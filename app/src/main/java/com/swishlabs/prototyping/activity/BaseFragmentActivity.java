package com.swishlabs.prototyping.activity;

import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.maps.model.LatLng;
import com.lidroid.xutils.DbUtils;
import com.swishlabs.prototyping.R;
import com.swishlabs.prototyping.adapter.SlideMenuAdapter;
import com.swishlabs.prototyping.customViews.app.SlidingFragmentActivity;
import com.swishlabs.prototyping.customViews.app.SlidingMenu;
import com.swishlabs.prototyping.fragment.BaseFragment;
import com.swishlabs.prototyping.services.GPSTracker;

public class BaseFragmentActivity extends SlidingFragmentActivity implements BaseFragment.OnFragmentInteractionListener {

    protected SlideMenuAdapter mSlideMenuAdapter;
    private RelativeLayout mBehindMenu;
    private TextView mTxtViewLoginState;
    private ImageView mImgViewAvata;
    private int[] mSlidingMenuIds;
    protected ListView listView;

    protected DbUtils mFinalDb;


    public static int MYPROFILE = 0;
    public static int DEFAULTSETTING = 1;
    public static int MYCONTACTS = 2;
    public static int SIGNOUT = 3;
    public static int HELP = 4;
    public static int HOWITWORKS = 5;
    public static int ABOUTGRABOP = 6;
    public static int TERMSOFUSE = 7;


    protected GPSTracker gpsTracker;
    protected LatLng mCurrLocation;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

 //       getSlidingMenu().setSlidingEnabled(true);

        mSlideMenuAdapter = new SlideMenuAdapter(this, getResources().getStringArray(R.array.slide_menu_list), null);
        initSlidingLayout();

        gpsTracker = new GPSTracker(this);

//        if(gpsTracker.canGetLocation()) {
//
//            mCurrLocation = new LatLng(gpsTracker.getLatitude(),gpsTracker.getLongitude());
//
//        }else {
////            gpsTracker.showSettingsAlert();
//            mCurrLocation = new LatLng(0.0, 0.0);
//        }


        mFinalDb = DbUtils.create(this);

    }


    public GPSTracker getGpsTracker() {
        return gpsTracker;
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_base, menu);
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

    private void initSlidingLayout()
    {
        mBehindMenu = (RelativeLayout) LayoutInflater.from(this).inflate(R.layout.fragment_behindmenu, null);

        setBehindContentView(mBehindMenu);

        View view = mBehindMenu.findViewById(R.id.viewGrpUserInfo);
        view.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
//				switchFramgment(mFragmentSquare);
                //	mTabViewPerson.performClick();
//				Intent intent = new Intent(getBaseContext(), UserHomeActivity.class);
//				startActivity(intent);
                getSlidingMenu().toggle();
            }
        });

        mTxtViewLoginState = (TextView) mBehindMenu.findViewById(R.id.txtViewLoginState);

        mImgViewAvata = (ImageView) mBehindMenu.findViewById(R.id.imgViewAvata);


/*        mBackgroundAvata = (ImageView)mBehindMenu.findViewById(R.id.imgBackground);
        Bitmap bmp;
        if (ResourceManager.UserProfile.pictureBase64 != null && ResourceManager.UserProfile.pictureBase64.length() > 0)
        {
            GrabOpImageSave imageSave = new GrabOpImageSave();
            bmp = imageSave.fromBase64ToBitmapHigherResolution(ResourceManager.UserProfile.pictureBase64);
        }
        else
        {
            bmp = BitmapFactory.decodeResource(getResources(), R.drawable.basepicture);
        }

        mBackgroundAvata.setAlpha(0.5f);
        if (ResourceManager.UserProfile.pictureBlur != null)
            holder.mBackgroundAvata.setImageBitmap(ResourceManager.UserProfile.pictureBlur);
        else
            BlurEffect.blur(this, bmp, holder.mBackgroundAvata, 5);*/

/*        mUserNameText = (TextView)holder.mBehindMenu.findViewById(R.id.txtViewLoginState);
        if (ResourceManager.UserProfile.userName != null)
            holder.mUserNameText.setText(ResourceManager.UserProfile.userName);
        else
            holder.mUserNameText.setText("Unloggedin");

        holder.mImgViewAvata = (ImageView) holder.mBehindMenu.findViewById(R.id.imgViewAvata);
        if (ResourceManager.UserProfile.pictureBase64 != null && ResourceManager.UserProfile.pictureBase64.length() > 0)
        {
            GrabOpImageSave imageSave = new GrabOpImageSave();
            bmp = imageSave.fromBase64ToBitmap(ResourceManager.UserProfile.pictureBase64);
        }
        else
        {
            bmp = BitmapFactory.decodeResource(getResources(), R.drawable.avata);
        }
        bmp = TransformImage.getRoundedCroppedBitmap(bmp);
        holder.mImgViewAvata.setImageBitmap(bmp);*/

        getSlidingMenu().setBehindOffset((int) (100 * getResources().getDisplayMetrics().density));
        getSlidingMenu().setSlidingEnabled(true);
        getSlidingMenu().setShadowWidth(3);
        getSlidingMenu().setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
//        getSlidingMenu().setMode(SlidingMenu.RIGHT);

        listView = (ListView) mBehindMenu.findViewById(R.id.listViewSlideMenu);

        listView.setAdapter(mSlideMenuAdapter);


        mSlidingMenuIds = getResources().getIntArray(R.array.slide_menu_id_list);
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
