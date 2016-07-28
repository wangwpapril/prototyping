package com.swishlabs.prototyping.fragment;

import android.app.Fragment;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.swishlabs.prototyping.R;
import com.swishlabs.prototyping.customViews.SwipeDismissList;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link MyProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MyProfileFragment extends BaseFragment {

    private TabLayout tabLayout;
    private Button btnRestore;
    private ViewPager viewPager;
    private Adapter adapter;

    public MyProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        tabLayout = (TabLayout) view.findViewById(R.id.tabContainer);

        viewPager = (ViewPager) view.findViewById(R.id.viewpager);
        if (viewPager != null) {
            setupViewPager(viewPager);
        }

        tabLayout.setupWithViewPager(viewPager);

//        btnRestore = (Button) view.findViewById(R.id.butRestore);
//        btnRestore.setEnabled(false);

        return view;
    }

    private void setupViewPager(ViewPager viewPager) {
        adapter = new Adapter(getChildFragmentManager());
        adapter.addFragment(MyProfileDetailsFragment.newInstance("",""), "Details");
        adapter.addFragment(MyOffersFragment.newInstance("", ""), "Offers");
        adapter.addFragment(ReceivedRequestFragment.newInstance("Needs"), "Needs");
        viewPager.setAdapter(adapter);
    }

    class Adapter extends FragmentPagerAdapter {
        private final List<android.support.v4.app.Fragment> mFragments = new ArrayList<>();
        private final List<String> mFragmentTitles = new ArrayList<>();

        public Adapter(FragmentManager fm) {
            super(fm);
        }

        public void addFragment(android.support.v4.app.Fragment fragment, String title) {
            mFragments.add(fragment);
            mFragmentTitles.add(title);
        }

        @Override
        public android.support.v4.app.Fragment getItem(int position) {
            return mFragments.get(position);
        }

        @Override
        public int getCount() {
            return mFragments.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitles.get(position);
        }

//        private Drawable getIcon(int position) {
//            return getResources().getDrawable(icons[position]);
//        }

    }

    public void updateAvatar(String url) {
        android.support.v4.app.Fragment fragment = adapter.getItem(viewPager.getCurrentItem());
        if (fragment instanceof MyProfileDetailsFragment) {
            ((MyProfileDetailsFragment) fragment).updateAvatar(url);
        }
    }

}
