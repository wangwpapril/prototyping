package com.swishlabs.prototyping.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.swishlabs.prototyping.R;
import com.swishlabs.prototyping.adapter.MainHomeListAdapter;
import com.swishlabs.prototyping.helper.FixedGridLayoutManager;
import com.swishlabs.prototyping.helper.OnStartDragListener;
import com.swishlabs.prototyping.helper.SimpleItemTouchHelperCallback;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainHomeActivityFragment extends Fragment implements OnStartDragListener {

    private RecyclerView mRecyclerView;
//    private RecyclerView.Adapter<CustomViewHolder> mAdapter;
    private ItemTouchHelper mItemTouchHelper;


    public MainHomeActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_main_home, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);

        MainHomeListAdapter adapter = new MainHomeListAdapter(getActivity(), this);

        mRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new FixedGridLayoutManager());
        mRecyclerView.setAdapter(adapter);
//        mRecyclerView.setScrollContainer(false);

        ItemTouchHelper.Callback callback = new SimpleItemTouchHelperCallback(adapter);
        mItemTouchHelper = new ItemTouchHelper(callback);
        mItemTouchHelper.attachToRecyclerView(mRecyclerView);

    }

    @Override
    public void onStartDrag(RecyclerView.ViewHolder viewHolder) {
        mItemTouchHelper.startDrag(viewHolder);

    }
}
