package com.swishlabs.prototyping.fragment;


import android.graphics.Color;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.swishlabs.prototyping.R;
import com.swishlabs.prototyping.activity.MainActivity;
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
    private SearchView mSearchView;
    SearchView.SearchAutoComplete searchText;

    ConnectionsManager connectionsManager;
    ProfileConnectionFragment profileConnectionFragment;

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
        initSearchView(view);

        mAdapter = new ConnectionsRecyclerAdapter(getContext());
        mAdapter.setOnItemClickListener(new ConnectionsRecyclerAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void OnItemClick(int position) {

                switch (position) {
                    case ConnectionsRecyclerAdapter.PROFILE_ICON_CLICK:
                        profileConnectionFragment = ProfileConnectionFragment.newInstance(mListProfile.get(position));
                        FragmentManager fm = getActivity().getSupportFragmentManager();
                        FragmentTransaction ft = fm.beginTransaction();
                        ft.add(R.id.container, profileConnectionFragment, profileConnectionFragment.getClass().getSimpleName());
                        ft.addToBackStack(((MainActivity) getActivity()).getCurrentFragment().getTag());
                        ft.commit();

                        profileConnectionFragment.addListener(new ProfileConnectionFragment.OnFragmentInteractionListener() {
                            @Override
                            public void onFragmentInteraction() {
                                FragmentManager fm = getActivity().getSupportFragmentManager();
                                FragmentTransaction ft = fm.beginTransaction();
                                ft.remove(profileConnectionFragment);
//                        ft.add(R.id.container, fragment, fragment.getClass().getSimpleName());
//                        ft.addToBackStack(null);
                                ft.commit();
                                fm.popBackStack();

                            }
                        });
                        break;
                    case ConnectionsRecyclerAdapter.PHONE_ICON_CLICK:
                        break;
                }
//                ((CardStackFragment)((MainActivity) getActivity()).mCardStackFragment).setData(mListProfile);
//                ((MainActivity) getActivity()).switchFragment(((MainActivity) getActivity()).mCardStackFragment);
            }
        });


        mPullToRefreshRV = (PullToRefreshRecyclerView) view.findViewById(R.id.connection_card_list);

        mPullToRefreshRV.setMode(PullToRefreshBase.Mode.BOTH);
        mPullToRefreshRV.setClickable(true);
        mRecyclerView = mPullToRefreshRV.getRefreshableView();
        mRecyclerView.setHasFixedSize(true);
        //       mRecyclerView.setClipToPadding(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
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

    private void initSearchView(View view) {
        mSearchView = (SearchView) view.findViewById(R.id.connection_search);
        mSearchView.setIconifiedByDefault(false);
        mSearchView.clearFocus();

        View searchPlateView = mSearchView.findViewById(getResources().getIdentifier("android:id/search_plate",null,null));
        if (searchPlateView != null)
            searchPlateView.setBackgroundColor(Color.TRANSPARENT);

        // icon
        ImageView searchIcon = (ImageView) mSearchView.findViewById(android.support.v7.appcompat.R.id.search_mag_icon);
        searchIcon.setImageResource(android.R.drawable.ic_search_category_default);
        searchIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        // clear button
        ImageView searchClose = (ImageView) mSearchView.findViewById(android.support.v7.appcompat.R.id.search_close_btn);
        searchClose.setImageResource(R.drawable.close);

        searchText = (SearchView.SearchAutoComplete) mSearchView.findViewById(android.support.v7.appcompat.R.id.search_src_text);
        searchText.setTextColor(getResources().getColor(R.color.black));
        searchText.setHintTextColor(getResources().getColor(R.color.gray));
        searchText.setHint("Search");
        searchText.setTextSize(18f);

    }

}
