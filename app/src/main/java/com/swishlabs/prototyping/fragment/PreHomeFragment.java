package com.swishlabs.prototyping.fragment;

import android.app.Fragment;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.text.TextUtils;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.model.LatLng;
import com.google.gson.reflect.TypeToken;
import com.swishlabs.prototyping.R;
import com.swishlabs.prototyping.activity.BaseFragmentActivity;
import com.swishlabs.prototyping.activity.MainActivity;
import com.swishlabs.prototyping.adapter.PreHomeRecyclerAdapter;
import com.swishlabs.prototyping.customViews.pullrefresh.PullToRefreshBase;
import com.swishlabs.prototyping.customViews.pullrefresh.PullToRefreshRecyclerView;
import com.swishlabs.prototyping.entity.ConnectionsManager;
import com.swishlabs.prototyping.entity.Profile;
import com.swishlabs.prototyping.entity.ProfileAround;
import com.swishlabs.prototyping.entity.ProfilesAroundManager;
import com.swishlabs.prototyping.helper.SimpleItemTouchHelperCallback;
import com.swishlabs.prototyping.net.IResponse;
import com.swishlabs.prototyping.services.GPSTracker;
import com.swishlabs.prototyping.util.GsonUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link PreHomeFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link PreHomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PreHomeFragment extends BaseFragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    private RecyclerView mRecyclerView;
    private ItemTouchHelper mItemTouchHelper;
    private PullToRefreshRecyclerView mPullToRefreshRV;
    private PreHomeRecyclerAdapter mAdapter;
    private List<Profile> mListProfile;
    private ProfilesAroundManager profilesAroundManager;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PreHomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PreHomeFragment newInstance(String param1, String param2) {
        PreHomeFragment fragment = new PreHomeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public PreHomeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

        mListProfile = new ArrayList<Profile>();
        profilesAroundManager = new ProfilesAroundManager(getContext()) {
            @Override
            public void onDataLoaded(List data) {
                mListProfile.addAll(data);
                ((PreHomeRecyclerAdapter)mRecyclerView.getAdapter()).setData(mListProfile);
                mPullToRefreshRV.getRefreshableView().getAdapter().notifyDataSetChanged();
                mPullToRefreshRV.onRefreshComplete();
            }
        };

        ConnectionsManager connectionsManager = new ConnectionsManager(getContext()) {
            @Override
            public void onDataLoaded(List data) {

            }
        };

        connectionsManager.initialize();
        connectionsManager.loadData();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        super.onCreateView(inflater,container,savedInstanceState);

        View view = inflater.inflate(R.layout.fragment_pre_home, container, false);
        mAdapter = new PreHomeRecyclerAdapter(getContext());
        mAdapter.setOnItemClickListener(new PreHomeRecyclerAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void OnItemClick(View view, int position) {

//                ((SwipeFragment)((MainActivity) getActivity()).mSwipeFragment).setProfileList(mListProfile);
//                ((MainActivity) getActivity()).switchFragment(((MainActivity) getActivity()).mSwipeFragment);

                ((CardStackFragment)((MainActivity) getActivity()).mCardStackFragment).setData(mListProfile);
                ((MainActivity) getActivity()).switchFragment(((MainActivity) getActivity()).mCardStackFragment);
            }
        });

        final int spanCount = 2;
        final RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getActivity(), spanCount);
//        final FixedGridLayoutManager layoutManager = new FixedGridLayoutManager();
        //      layoutManager.setTotalColumnCount(2);
//        layoutManager.offsetChildrenHorizontal(30);
        //      layoutManager.offsetChildrenVertical(30);



        mPullToRefreshRV = (PullToRefreshRecyclerView) view.findViewById(R.id.pre_home_card_list);

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
        mRecyclerView.addItemDecoration(new SpacesItemDecoration(40));


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

//                    getProfiles("153", mListProfile.size()-1);
                    if (!profilesAroundManager.getMoreData()) {
                        profilesAroundManager.loadData();
                    }else {
                        mPullToRefreshRV.onRefreshComplete();
                    }

                }else if(state == PullToRefreshBase.State.MANUAL_REFRESHING) {
                    mListProfile.clear();
//                    getProfiles("153", 0);
                    profilesAroundManager.initialize();
                    profilesAroundManager.loadData();
                }else if(state == PullToRefreshBase.State.REFRESHING && direction == PullToRefreshBase.Mode.PULL_FROM_START) {
                    mListProfile.clear();
//                    getProfiles("153", 0);
                    profilesAroundManager.initialize();
                    profilesAroundManager.loadData();
                }else if(state == PullToRefreshBase.State.RESET && direction == PullToRefreshBase.Mode.PULL_FROM_START) {
//                    mPullToRefreshRV.setRefreshing(true);
                }
            }
        });

        ItemTouchHelper.Callback callback = new SimpleItemTouchHelperCallback(mAdapter);
        mItemTouchHelper = new ItemTouchHelper(callback);
        mItemTouchHelper.attachToRecyclerView(mRecyclerView);

        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mPullToRefreshRV.setRefreshing(true);
//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                mPullToRefreshRV.setRefreshing(true);
//            }
//        }, 3000);
//
    }

    @Override
    public void onStart(){
        super.onStart();
//        mPullToRefreshRV.setRefreshing(true);

    }

    private void getProfiles(String id, int offset) {

        GPSTracker gpsTracker = ((BaseFragmentActivity) getActivity()).getGpsTracker();
        LatLng mCurrLocation;

        if(gpsTracker.canGetLocation()) {

            mCurrLocation = new LatLng(gpsTracker.getLatitude(),gpsTracker.getLongitude());

        }else {
//            gpsTracker.showSettingsAlert();
            mCurrLocation = new LatLng(0.0, 0.0);
        }

       mWebApi.getProfiles(id, 5.0, mCurrLocation.longitude,mCurrLocation.latitude, offset, new IResponse<List<ProfileAround>>() {

            @Override
            public void onSucceed(List<ProfileAround> result) {
                if(result != null && !result.isEmpty()){
                    for (ProfileAround profileId :result) {
                        mWebApi.getProfile(String.valueOf(profileId.getSessionId()), new IResponse<Profile>() {
                            @Override
                            public void onSucceed(Profile result) {

                                mListProfile.add(result);
                                ((PreHomeRecyclerAdapter)mRecyclerView.getAdapter()).setData(mListProfile);
                                mPullToRefreshRV.getRefreshableView().getAdapter().notifyDataSetChanged();
                            }

                            @Override
                            public void onFailed(String code, String errMsg) {

                            }

                            @Override
                            public Profile asObject(String rspStr) throws JSONException {
                                try {
                                    JSONObject json = new JSONObject(rspStr);

                                    Profile profile = GsonUtil.jsonToObject(Profile.class, rspStr);

                                    return profile;
                                } catch (JSONException e) {
                                    // TODO Auto-generated catch block
                                    e.printStackTrace();
                                }

                                return null;
                            }
                        });
                    }
                }

                mPullToRefreshRV.onRefreshComplete();

            }

            @Override
            public void onFailed(String code, String errMsg) {
                mPullToRefreshRV.onRefreshComplete();

            }

            @Override
            public List<ProfileAround> asObject(String rspStr) throws JSONException {

                if (!TextUtils.isEmpty(rspStr)) {
                    TypeToken<List<ProfileAround>> type = new TypeToken<List<ProfileAround>>() {
                    };
                    return GsonUtil.jsonToList(type.getType(), rspStr);
                }
                return new ArrayList<ProfileAround>();

            }
        });
    }


    public class SpacesItemDecoration extends RecyclerView.ItemDecoration {

        private int halfSpace;

        public SpacesItemDecoration(int space) {
            this.halfSpace = space / 2;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {

            if (parent.getPaddingLeft() != halfSpace) {
                parent.setPadding(halfSpace, halfSpace, halfSpace, halfSpace);
                parent.setClipToPadding(false);
            }

            outRect.top = halfSpace;
            outRect.bottom = halfSpace;
            outRect.left = halfSpace;
            outRect.right = halfSpace;
        }
    }


}
