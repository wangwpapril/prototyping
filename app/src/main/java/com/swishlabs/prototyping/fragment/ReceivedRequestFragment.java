package com.swishlabs.prototyping.fragment;


import android.graphics.Rect;
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
import com.swishlabs.prototyping.adapter.PreHomeRecyclerAdapter;
import com.swishlabs.prototyping.customViews.pullrefresh.PullToRefreshBase;
import com.swishlabs.prototyping.customViews.pullrefresh.PullToRefreshRecyclerView;
import com.swishlabs.prototyping.data.DataManager;
import com.swishlabs.prototyping.entity.BaseDataManager;
import com.swishlabs.prototyping.entity.Profile;
import com.swishlabs.prototyping.entity.ReceivedRequestManager;
import com.swishlabs.prototyping.entity.SentRequestManager;
import com.swishlabs.prototyping.helper.SimpleItemTouchHelperCallback;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class ReceivedRequestFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";

    private String mParam1;

    private RecyclerView mRecyclerView;
    private ItemTouchHelper mItemTouchHelper;
    private PullToRefreshRecyclerView mPullToRefreshRV;
    private PreHomeRecyclerAdapter mAdapter;
    private List<Profile> mListProfile;

    private BaseDataManager dataManager;

    public ReceivedRequestFragment() {
        // Required empty public constructor
    }

    public static ReceivedRequestFragment newInstance(String param1) {
        ReceivedRequestFragment fragment = new ReceivedRequestFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
        }

        mListProfile = new ArrayList<Profile>();

        if (mParam1.equals("Received")) {
            dataManager = new ReceivedRequestManager(getContext()) {
                @Override
                public void onDataLoaded(List data) {
                    mListProfile.addAll(data);
//                    ((RequestsRecyclerAdapter) mRecyclerView.getAdapter()).setData(mListProfile);
//                    mPullToRefreshRV.getRefreshableView().getAdapter().notifyDataSetChanged();
                    mAdapter.setData(mListProfile);
                    mAdapter.notifyDataSetChanged();
                    mPullToRefreshRV.onRefreshComplete();

                }
            };
        }else if (mParam1.equals("Sent")) {
            dataManager = new SentRequestManager(getContext()) {
                @Override
                public void onDataLoaded(List data) {
                    mListProfile.addAll(data);
//                    ((RequestsRecyclerAdapter) mRecyclerView.getAdapter()).setData(mListProfile);
//                    mPullToRefreshRV.getRefreshableView().getAdapter().notifyDataSetChanged();
                    mAdapter.setData(mListProfile);
                    mAdapter.notifyDataSetChanged();
                    mPullToRefreshRV.onRefreshComplete();

                }
            };
        }

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_received_request, container, false);

//        mAdapter = new RequestsRecyclerAdapter(getContext());
        mAdapter = new PreHomeRecyclerAdapter(getContext());
//        mAdapter.setOnItemClickListener(new RequestsRecyclerAdapter.OnRecyclerViewItemClickListener() {
//            @Override
//            public void OnItemClick(View view, int position) {
//
////                ((CardStackFragment)((MainActivity) getActivity()).mCardStackFragment).setData(mListProfile);
////                ((MainActivity) getActivity()).switchFragment(((MainActivity) getActivity()).mCardStackFragment);
//            }
//        });

        mAdapter.setOnItemClickListener(new PreHomeRecyclerAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void OnItemClick(View view, int position) {

            }
        });
        final int spanCount = 3;
        final RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getActivity(), spanCount);
//        final FixedGridLayoutManager layoutManager = new FixedGridLayoutManager();
        //      layoutManager.setTotalColumnCount(2);
//        layoutManager.offsetChildrenHorizontal(30);
        //      layoutManager.offsetChildrenVertical(30);



        mPullToRefreshRV = (PullToRefreshRecyclerView) view.findViewById(R.id.received_request_card_list);

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
        mRecyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                super.getItemOffsets(outRect, view, parent, state);
                outRect.top = 10;
                outRect.bottom = 10;
                outRect.left = 10;
                outRect.right = 10;
            }
        });
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
                    if (!dataManager.getMoreData()) {
                        if (mParam1.equals("Received")) {
                            ((ReceivedRequestManager)dataManager).loadData();
                        }else if (mParam1.equals("Sent")) {
                            ((SentRequestManager)dataManager).loadData();
                        }
                    }else {
                        mPullToRefreshRV.onRefreshComplete();
                    }

                }else if(state == PullToRefreshBase.State.MANUAL_REFRESHING) {
                    mListProfile.clear();
                    dataManager.initialize();
                    if (mParam1.equals("Received")) {
                        ((ReceivedRequestManager)dataManager).loadData();
                    }else if (mParam1.equals("Sent")) {
                        ((SentRequestManager)dataManager).loadData();
                    }
                }else if(state == PullToRefreshBase.State.REFRESHING && direction == PullToRefreshBase.Mode.PULL_FROM_START) {
                    mListProfile.clear();
                    dataManager.initialize();
                    if (mParam1.equals("Received")) {
                        ((ReceivedRequestManager)dataManager).loadData();
                    }else if (mParam1.equals("Sent")) {
                        ((SentRequestManager)dataManager).loadData();
                    }
                }else if(state == PullToRefreshBase.State.RESET && direction == PullToRefreshBase.Mode.PULL_FROM_START) {
//                    mPullToRefreshRV.setRefreshing(true);
                }
            }
        });

        ItemTouchHelper.Callback callback = new SimpleItemTouchHelperCallback(mAdapter);
        mItemTouchHelper = new ItemTouchHelper(callback);
        mItemTouchHelper.attachToRecyclerView(mRecyclerView);

        if (mParam1.equals("Received")) {
            mListProfile = DataManager.getInstance().getReceivedRequestList();
            dataManager.setOffset(DataManager.getInstance().getReceivedRequestOffset());
            dataManager.setNoMoreData(DataManager.getInstance().getReceivedRequestMoreData());

        }else if (mParam1.equals("Sent")) {
            mListProfile = DataManager.getInstance().getSentRequestList();
            dataManager.setOffset(DataManager.getInstance().getSentRequestOffset());
            dataManager.setNoMoreData(DataManager.getInstance().getSentRequestMoreData());

        }
//        ((PreHomeRecyclerAdapter)mRecyclerView.getAdapter()).setData(mListProfile);
//        mPullToRefreshRV.getRefreshableView().getAdapter().notifyDataSetChanged();
        mAdapter.setData(mListProfile);
        mAdapter.notifyDataSetChanged();
        mPullToRefreshRV.onRefreshComplete();

        return view;
    }

}
