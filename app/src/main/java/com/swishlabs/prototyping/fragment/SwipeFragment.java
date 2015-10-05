package com.swishlabs.prototyping.fragment;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.swishlabs.prototyping.R;
import com.swishlabs.prototyping.customViews.SwipeDismissList;
import com.swishlabs.prototyping.util.ViewAnimation;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link SwipeFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link SwipeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SwipeFragment extends BaseFragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    private SwipeDismissList mSwipeList;
    private static RelativeLayout mProfileFrameLayout;
    private RelativeLayout.LayoutParams params;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SwipeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SwipeFragment newInstance(String param1, String param2) {
        SwipeFragment fragment = new SwipeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public SwipeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

//        initUI();
    }

    private void initUI() {

        SwipeDismissList.OnDismissCallback callback = new SwipeDismissList.OnDismissCallback() {

            @Override
            public SwipeDismissList.Undoable onDismiss(RelativeLayout layout, int position, boolean isLeft) {
 //               setAction(position, isLeft);

//				ResourceManager.OtherProfiles.remove(position);
                return null;
            }

            @Override
            public void onShowMoreInfo(int position, View view) {
//                animateToNextActivity(view, position);
            }

            @Override
            public void onMoveToResetPosition(View view, float x, float y, float rotateDegree) {
 //               animateToResetPosition(view, x, y, rotateDegree);
            }
        };
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_swip, container, false);
        mProfileFrameLayout = (RelativeLayout)view.findViewById(R.id.profileCardLayout);
//        mProfileFrameLayout.bringToFront();

        SwipeDismissList.OnDismissCallback callback = new SwipeDismissList.OnDismissCallback() {

            @Override
            public SwipeDismissList.Undoable onDismiss(RelativeLayout layout, int position, boolean isLeft) {
                               setAction(position, isLeft);

//				ResourceManager.OtherProfiles.remove(position);
                return null;
            }

            @Override
            public void onShowMoreInfo(int position, View view) {
                animateToNextActivity(view, position);
            }

            @Override
            public void onMoveToResetPosition(View view, float x, float y, float rotateDegree) {
                animateToResetPosition(view, x, y, rotateDegree);
            }
        };

        mSwipeList= new SwipeDismissList(getActivity(), mProfileFrameLayout, callback, R.layout.profileview_layout);
        mSwipeList.setSwipeDirection(SwipeDismissList.SwipeDirection.BOTH);

        View profileView = inflater.inflate(R.layout.profileview_layout,null);
        View profileView1 = inflater.inflate(R.layout.profileview_layout,null);
        TextView mNameText = (TextView)profileView.findViewById(R.id.profile_user_name);
        mNameText.setText("wwp1");

        TextView mNameText1 = (TextView)profileView1.findViewById(R.id.profile_user_name);
        mNameText1.setText("wwp2");

        profileView.setEnabled(true);
        mSwipeList.addView(profileView);
        mSwipeList.addView(profileView1);

        params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        params.addRule(RelativeLayout.CENTER_HORIZONTAL);
        params.setMargins(0, 50, 0, 100);
//        params.bottomMargin = 150;
//        mSwipeList.setInitialPos(300, 300);

        mProfileFrameLayout.addView(profileView,0,params);
        mProfileFrameLayout.addView(profileView1,0,params);

        mSwipeList.setPosition(0, params);
//        mSwipeList.setInitialPos(300,300);

        return view;
    }

    @SuppressLint("NewApi")
    private void animateToNextActivity(final View view, final int position)
    {
        ViewAnimation animation = new ViewAnimation(view, view.getX(), 0, view.getY(), 0);
        animation.setDuration(500);
        animation.setFillEnabled(true);
        animation.setAnimationListener(new Animation.AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                params.addRule(RelativeLayout.CENTER_HORIZONTAL);

/*                if (ConfigurationScreen.CheckScreenSize(instance).equals(ConfigurationScreen.NORMAL))
                    params.setMargins(
                            0,
                            ResourceManager.HomeViewMarginTopNormal + ResourceManager.BACKGROUND_PROFILE_OFFSET,
                            0, 0);
                else if (ConfigurationScreen.CheckScreenSize(instance).equals(ConfigurationScreen.SMALL))
                    params.setMargins(
                            0,
                            ResourceManager.HomeViewMarginTopSmall + ResourceManager.BACKGROUND_PROFILE_OFFSET,
                            0, 0);*/

                view.setLayoutParams(params);

 //               goToMoreInfo(position);
            }
        });
        view.setAnimation(animation);

    }

    private void animateToResetPosition(final View view, float x, float y, float rotateDegree)
    {
        ViewAnimation animation = new ViewAnimation(view, x, 0, y, 0, rotateDegree, 0);
        animation.setDuration(500);
        animation.setFillEnabled(true);
        animation.setAnimationListener(new Animation.AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                params.addRule(RelativeLayout.CENTER_HORIZONTAL);
                params.setMargins(0, 50, 0, 100);

 /*               if (ConfigurationScreen.CheckScreenSize(instance).equals(ConfigurationScreen.NORMAL))
                    params.setMargins(
                            0,
                            ResourceManager.HomeViewMarginTopNormal + ResourceManager.BACKGROUND_PROFILE_OFFSET,
                            0, 0);
                else if (ConfigurationScreen.CheckScreenSize(instance).equals(ConfigurationScreen.SMALL))
                    params.setMargins(
                            0,
                            ResourceManager.HomeViewMarginTopSmall + ResourceManager.BACKGROUND_PROFILE_OFFSET,
                            0, 0);*/

                view.setLayoutParams(params);
            }
        });
        view.setAnimation(animation);

    }

    public int setAction(int position, boolean isLeft)
    {
        int pos = position;

        if (isLeft)
        {
//            BackgroundTask.GetInstance(this).reject(position, isFilterProfiles);
//            pos = holder.mProfileView.setNextPosition(position, isFilterProfiles);
            mSwipeList.setPosition(position, params);
        }
        else
        {
//            BackgroundTask.GetInstance(this).accept(position, isFilterProfiles);
//            mSwipeList.disableRow(position);
//            pos = holder.mProfileView.disabledViewAt(position, isFilterProfiles);

/*            if (isFilterProfiles)
            {
                User tmpUser = ResourceManager.filteredPreHomeProfiles.get(position);
                tmpUser.setEnabled(false);

                for (int i=0; i<ResourceManager.OtherProfiles.size(); i++)
                {
                    if (ResourceManager.OtherProfiles.get(i).equals(tmpUser))
                    {
                        ResourceManager.OtherProfiles.get(i).setEnabled(false);
                        break;
                    }
                }
            }
            else
                ResourceManager.OtherProfiles.get(position).setEnabled(false);

            WidgetProvider.setUserEnabled(ResourceManager.OtherProfiles.get(position), false);*/

            mSwipeList.setPosition(position, params);
        }

        return pos;
    }

    // TODO: Rename method, update argument and hook method into UI event
 /*   public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }*/

/*    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }*/

 /*   @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }
*/
    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
 /*   public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);
    }*/

}
