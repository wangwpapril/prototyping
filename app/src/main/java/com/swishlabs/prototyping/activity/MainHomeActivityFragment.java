package com.swishlabs.prototyping.activity;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.swishlabs.prototyping.R;
import com.swishlabs.prototyping.helper.OnStartDragListener;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainHomeActivityFragment extends Fragment implements OnStartDragListener {

    public MainHomeActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_main_home, container, false);
    }

    @Override
    public void onStartDrag(RecyclerView.ViewHolder viewHolder) {

    }
}
