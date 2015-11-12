package com.swishlabs.prototyping.fragment;

import android.app.Fragment;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.text.TextUtils;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.reflect.TypeToken;
import com.swishlabs.prototyping.R;
import com.swishlabs.prototyping.adapter.PreHomeRecyclerAdapter;
import com.swishlabs.prototyping.customViews.pullrefresh.PullToRefreshBase;
import com.swishlabs.prototyping.customViews.pullrefresh.PullToRefreshRecyclerView;
import com.swishlabs.prototyping.entity.Profile;
import com.swishlabs.prototyping.entity.ProfileAround;
import com.swishlabs.prototyping.helper.SimpleItemTouchHelperCallback;
import com.swishlabs.prototyping.net.IResponse;
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
    private List<Profile> mListProfile;

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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_pre_home, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);

        PreHomeRecyclerAdapter adapter = new PreHomeRecyclerAdapter();

        final int spanCount = 2;
        final RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getActivity(), spanCount);
//        final FixedGridLayoutManager layoutManager = new FixedGridLayoutManager();
  //      layoutManager.setTotalColumnCount(2);
//        layoutManager.offsetChildrenHorizontal(30);
  //      layoutManager.offsetChildrenVertical(30);



        mPullToRefreshRV = (PullToRefreshRecyclerView) view.findViewById(R.id.pre_home_card_list);

 //       mPullToRefreshRV.setMode(PullToRefreshBase.Mode.BOTH);
        mRecyclerView = mPullToRefreshRV.getRefreshableView();
        mRecyclerView.setHasFixedSize(true);
 //       mRecyclerView.setClipToPadding(true);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setAdapter(adapter);
        mRecyclerView.setFocusableInTouchMode(false);
        mRecyclerView.setFocusable(false);
//        mRecyclerView.setScrollContainer(false);
        mRecyclerView.addItemDecoration(new SpacesItemDecoration(20));


 //       mPullToRefreshRV.addView(mRecyclerView,0);
        mPullToRefreshRV.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<RecyclerView>() {
            @Override
            public void onRefresh(final PullToRefreshBase<RecyclerView> refreshView) {
                String label = DateUtils.formatDateTime(getContext(), System.currentTimeMillis(),
                        DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_ABBREV_ALL);

                // Update the LastUpdatedLabel
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

                if(state == PullToRefreshBase.State.PULL_TO_REFRESH && direction == PullToRefreshBase.Mode.PULL_FROM_END) {

                    getProfiles("153");

                }else if(state == PullToRefreshBase.State.MANUAL_REFRESHING) {
                    mListProfile.clear();
                    getProfiles("153");
                }else if(state == PullToRefreshBase.State.PULL_TO_REFRESH && direction == PullToRefreshBase.Mode.PULL_FROM_START) {
                    mListProfile.clear();
                    getProfiles("153");
                }
            }
        });

        ItemTouchHelper.Callback callback = new SimpleItemTouchHelperCallback(adapter);
        mItemTouchHelper = new ItemTouchHelper(callback);
        mItemTouchHelper.attachToRecyclerView(mRecyclerView);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mPullToRefreshRV.setRefreshing(true);
            }
        }, 3000);

//        mPullToRefreshRV.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
//            @Override
//            public boolean onPreDraw() {
//                return false;
//            }
//        });
    }

    private void getProfiles(String id) {

        mWebApi.getProfiles(id, new IResponse<List<ProfileAround>>() {

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
