package com.swishlabs.prototyping.activity;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.swishlabs.prototyping.R;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainHomeActivityFragment extends Fragment {

    public MainHomeActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_main_home, container, false);
    }
}
