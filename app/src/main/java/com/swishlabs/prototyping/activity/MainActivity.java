package com.swishlabs.prototyping.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.swishlabs.prototyping.MyApplication;
import com.swishlabs.prototyping.R;
import com.swishlabs.prototyping.fragment.BaseFragment;
import com.swishlabs.prototyping.fragment.SwipeFragment;

public class MainActivity extends BaseFragmentActivity {


    private BaseFragment mFragmentPreHome;
    private BaseFragment mCurrFragment;
    private ImageView mDrawerImage;
    private MyApplication mApp;

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


        mFragmentPreHome = SwipeFragment.newInstance(null, null);
        switchFragment(mFragmentPreHome);
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


}
