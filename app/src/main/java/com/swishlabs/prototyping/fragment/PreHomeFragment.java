package com.swishlabs.prototyping.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.swishlabs.prototyping.R;
import com.swishlabs.prototyping.adapter.PreHomeRecyclerAdapter;
import com.swishlabs.prototyping.customViews.pullrefresh.PullToRefreshBase;
import com.swishlabs.prototyping.customViews.pullrefresh.PullToRefreshRecyclerView;
import com.swishlabs.prototyping.helper.SimpleItemTouchHelperCallback;

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
        final GridLayoutManager layoutManager = new GridLayoutManager(getActivity(), spanCount);

        mPullToRefreshRV = (PullToRefreshRecyclerView) view.findViewById(R.id.pre_home_card_list);

 //       mPullToRefreshRV.setMode(PullToRefreshBase.Mode.BOTH);
        mRecyclerView = mPullToRefreshRV.getRefreshableView();
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setAdapter(adapter);
        mRecyclerView.setFocusableInTouchMode(true);
//        mRecyclerView.setScrollContainer(false);

 //       mPullToRefreshRV.addView(mRecyclerView,0);
        mPullToRefreshRV.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<RecyclerView>() {
            @Override
            public void onRefresh(PullToRefreshBase<RecyclerView> refreshView) {
                refreshView.getRefreshableView();

            }
        });

        mPullToRefreshRV.setOnPullEventListener(new PullToRefreshBase.OnPullEventListener<RecyclerView>() {
            @Override
            public void onPullEvent(PullToRefreshBase<RecyclerView> refreshView, PullToRefreshBase.State state, PullToRefreshBase.Mode direction) {

                refreshView.getRefreshableView();
            }
        });

        ItemTouchHelper.Callback callback = new SimpleItemTouchHelperCallback(adapter);
        mItemTouchHelper = new ItemTouchHelper(callback);
        mItemTouchHelper.attachToRecyclerView(mRecyclerView);

    }




}
