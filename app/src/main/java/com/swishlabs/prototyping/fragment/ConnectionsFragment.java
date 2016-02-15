package com.swishlabs.prototyping.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.swishlabs.prototyping.R;
import com.swishlabs.prototyping.adapter.ConnectionsRecyclerAdapter;
import com.swishlabs.prototyping.customViews.pullrefresh.PullToRefreshBase;
import com.swishlabs.prototyping.customViews.pullrefresh.PullToRefreshRecyclerView;
import com.swishlabs.prototyping.data.DataManager;
import com.swishlabs.prototyping.entity.ConnectionsManager;
import com.swishlabs.prototyping.entity.Profile;
import com.swishlabs.prototyping.helper.SimpleItemTouchHelperCallback;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ConnectionsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ConnectionsFragment extends BaseFragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private RecyclerView mRecyclerView;
    private ItemTouchHelper mItemTouchHelper;
    private PullToRefreshRecyclerView mPullToRefreshRV;
    private ConnectionsRecyclerAdapter mAdapter;
    private List<Profile> mListProfile;

    ConnectionsManager connectionsManager;


    public ConnectionsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ConnectionsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ConnectionsFragment newInstance(String param1, String param2) {
        ConnectionsFragment fragment = new ConnectionsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

        mListProfile = new ArrayList<Profile>();
        connectionsManager = new ConnectionsManager(getContext()) {
            @Override
            public void onDataLoaded(List data) {
                mListProfile.addAll(data);
                ((ConnectionsRecyclerAdapter)mRecyclerView.getAdapter()).setData(mListProfile);
                mPullToRefreshRV.getRefreshableView().getAdapter().notifyDataSetChanged();
                mPullToRefreshRV.onRefreshComplete();

            }
        };

//        connectionsManager.initialize();
//        connectionsManager.loadData();
//
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_connections, container, false);

        mAdapter = new ConnectionsRecyclerAdapter(getContext());
        mAdapter.setOnItemClickListener(new ConnectionsRecyclerAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void OnItemClick(View view, int position) {

//                ((CardStackFragment)((MainActivity) getActivity()).mCardStackFragment).setData(mListProfile);
//                ((MainActivity) getActivity()).switchFragment(((MainActivity) getActivity()).mCardStackFragment);
            }
        });

        final int spanCount = 1;
        final RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getActivity(), spanCount);
//        final FixedGridLayoutManager layoutManager = new FixedGridLayoutManager();
        //      layoutManager.setTotalColumnCount(2);
//        layoutManager.offsetChildrenHorizontal(30);
        //      layoutManager.offsetChildrenVertical(30);



        mPullToRefreshRV = (PullToRefreshRecyclerView) view.findViewById(R.id.connection_card_list);

        mPullToRefreshRV.setMode(PullToRefreshBase.Mode.BOTH);
        mPullToRefreshRV.setClickable(true);
        mRecyclerView = mPullToRefreshRV.getRefreshableView();
        mRecyclerView.setHasFixedSize(true);
        //       mRecyclerView.setClipToPadding(true);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setFocusableInTouchMode(false);
        mRecyclerView.setFocusable(false);
        mRecyclerView.setClickable(true);
//        mRecyclerView.setScrollContainer(false);
//        mRecyclerView.addItemDecoration(new SpacesItemDecoration(40));


        mPullToRefreshRV.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<RecyclerView>() {
            @Override
            public void onRefresh(final PullToRefreshBase<RecyclerView> refreshView) {
                String label = DateUtils.formatDateTime(getContext(), System.currentTimeMillis(),
                        DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_ABBREV_ALL);

                refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(label);
  /*               new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        //                       simpleRecyclerViewAdapter.insert(moreNum++ + "  Refresh things", 0);
//                        mPullToRefreshRV.onRefreshComplete();
                        refreshView.onRefreshComplete();
//                        mPullToRefreshRV.setEnabled(true);
                        mRecyclerView.getLayoutManager().scrollToPosition(0);
                        mRecyclerView.getAdapter().notifyDataSetChanged();

                        //   ultimateRecyclerView.scrollBy(0, -50);
//                        linearLayoutManager.scrollToPosition(0);
//                        ultimateRecyclerView.setAdapter(simpleRecyclerViewAdapter);
//                        simpleRecyclerViewAdapter.notifyDataSetChanged();
                    }
                }, 1000);*/

            }
        });

        mPullToRefreshRV.setOnPullEventListener(new PullToRefreshBase.OnPullEventListener<RecyclerView>() {
            @Override
            public void onPullEvent(PullToRefreshBase<RecyclerView> refreshView, PullToRefreshBase.State state, PullToRefreshBase.Mode direction) {

                if(state == PullToRefreshBase.State.REFRESHING && direction == PullToRefreshBase.Mode.PULL_FROM_END) {
                    if (!connectionsManager.getMoreData()) {
                        connectionsManager.loadData();
                    }else {
                        mPullToRefreshRV.onRefreshComplete();
                    }

                }else if(state == PullToRefreshBase.State.MANUAL_REFRESHING) {
                    mListProfile.clear();
                    connectionsManager.initialize();
                    connectionsManager.loadData();
                }else if(state == PullToRefreshBase.State.REFRESHING && direction == PullToRefreshBase.Mode.PULL_FROM_START) {
                    mListProfile.clear();
                    connectionsManager.initialize();
                    connectionsManager.loadData();
                }else if(state == PullToRefreshBase.State.RESET && direction == PullToRefreshBase.Mode.PULL_FROM_START) {
//                    mPullToRefreshRV.setRefreshing(true);
                }
            }
        });

        ItemTouchHelper.Callback callback = new SimpleItemTouchHelperCallback(mAdapter);
        mItemTouchHelper = new ItemTouchHelper(callback);
        mItemTouchHelper.attachToRecyclerView(mRecyclerView);
        mListProfile = DataManager.getInstance().getConnectionList();
        connectionsManager.setOffset(DataManager.getInstance().getConnectionOffset());
        connectionsManager.setNoMoreData(DataManager.getInstance().getConnectionMoreData());
        ((ConnectionsRecyclerAdapter)mRecyclerView.getAdapter()).setData(mListProfile);
        mPullToRefreshRV.getRefreshableView().getAdapter().notifyDataSetChanged();
        mPullToRefreshRV.onRefreshComplete();

        return view;
    }

}
