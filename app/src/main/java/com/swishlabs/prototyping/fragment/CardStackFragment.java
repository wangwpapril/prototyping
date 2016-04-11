package com.swishlabs.prototyping.fragment;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.LayoutTransition;
import android.animation.ValueAnimator;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.Interpolator;
import android.view.animation.RotateAnimation;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.swishlabs.prototyping.R;
import com.swishlabs.prototyping.customViews.cardstack.SwipeDeck;
import com.swishlabs.prototyping.data.api.model.Image;
import com.swishlabs.prototyping.entity.Profile;
import com.swishlabs.prototyping.net.IResponse;
import com.swishlabs.prototyping.util.ConvertUtil;
import com.swishlabs.prototyping.util.Enums;
import com.swishlabs.prototyping.util.FlipCard;
import com.swishlabs.prototyping.util.SharedPreferenceUtil;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link CardStackFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link CardStackFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CardStackFragment extends BaseFragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String TAG = "CardStack";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    private SwipeDeck cardStack;

    private List<Profile> mListProfile;

    private boolean isShowNeeds;

    public CardStackFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CardStackFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CardStackFragment newInstance(String param1, String param2) {
        CardStackFragment fragment = new CardStackFragment();
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

        isShowNeeds = false;
//        mListProfile = new ArrayList<>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_card_stack, container, false);

        cardStack = (SwipeDeck) view.findViewById(R.id.swipe_deck);

        cardStack.setHardwareAccelerationEnabled(true);

        final SwipeDeckAdapter adapter = new SwipeDeckAdapter(mListProfile, inflater);

        cardStack.setAdapter(adapter);

        cardStack.setEventCallback(new SwipeDeck.SwipeEventCallback() {
            @Override
            public void cardSwipedLeft(int position) {

                sendRequest(mListProfile.get(position));
            }

            @Override
            public void cardSwipedRight(int position) {

            }

            @Override
            public void cardsDepleted() {

            }
        });

        cardStack.setLeftImage(R.id.accept_image);

        cardStack.setRightImage(R.id.reject_image);

        Button btn = (Button) view.findViewById(R.id.pass_button);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cardStack.swipeTopCardLeft(180);

            }
        });
        Button btn2 = (Button) view.findViewById(R.id.reject_button);
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cardStack.swipeTopCardRight(180);
            }
        });

//        Button btn3 = (Button) findViewById(R.id.button3);
//        btn3.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                testData.add("a sample string.");
////                ArrayList<String> newData = new ArrayList<>();
////                newData.add("some new data");
////                newData.add("some new data");
////                newData.add("some new data");
////                newData.add("some new data");
////
////                SwipeDeckAdapter adapter = new SwipeDeckAdapter(newData, context);
////                cardStack.setAdapter(adapter);
//                adapter.notifyDataSetChanged();
//            }
//        });

        return view;
    }

    private void sendRequest(Profile profile) {
        String sessionId = SharedPreferenceUtil.getString(Enums.PreferenceKeys.sessionId.toString(), "153");

        mWebApi.sendConnectionRequest(sessionId, Integer.toString(profile.getSessionId()), new IResponse<Object>() {
            @Override
            public void onSucceed(Object result) {

            }

            @Override
            public void onFailed(String code, String errMsg) {

            }

            @Override
            public Object asObject(String rspStr) throws JSONException {
                return null;
            }
        });
    }

    public void setData(List<Profile> data)
    {
        this.mListProfile = data;
    }
    // TODO: Rename method, update argument and hook method into UI event
//    public void onButtonPressed(Uri uri) {
//        if (mListener != null) {
//            mListener.onFragmentInteraction(uri);
//        }
//    }
//
//    @Override
//    public void onAttach(Context context) {
//        super.onAttach(context);
//        if (context instanceof OnFragmentInteractionListener) {
//            mListener = (OnFragmentInteractionListener) context;
//        } else {
//            throw new RuntimeException(context.toString()
//                    + " must implement OnFragmentInteractionListener");
//        }
//    }
//
//    @Override
//    public void onDetach() {
//        super.onDetach();
//        mListener = null;
//    }

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
//    public interface OnFragmentInteractionListener {
//        // TODO: Update argument type and name
//        void onFragmentInteraction(Uri uri);
//    }

    public class SwipeDeckAdapter extends BaseAdapter {

        private List<Profile> data;
        private LayoutInflater inflater ;

        public SwipeDeckAdapter(List<Profile> data, LayoutInflater inflater) {
            this.data = data;
            this.inflater = inflater;
        }

        @Override
        public int getCount() {
            return data.size();
        }

        @Override
        public Object getItem(int position) {
            return data.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            ViewHolder holder = null;
            Profile profile = mListProfile.get(position);
            List<String> skillList = new ArrayList<>();

            if (profile.getSkillSet() != null) {
                skillList = new ArrayList<>(Arrays.asList(profile.getSkillSet().split(",")));
            }

            if (convertView == null) {
//                LayoutInflater inflater = getLayoutInflater(context);
                // normally use a viewholder
                holder = new ViewHolder();
                convertView = inflater.inflate(R.layout.profile_card_swipe_layout, parent, false);
//                View backview = inflater.inflate(R.layout.profile_card_back_layout, parent,false);

                holder.user_name = (TextView) convertView.findViewById(R.id.profile_user_name);
                holder.title = (TextView) convertView.findViewById(R.id.profile_title);
                holder.company = (TextView) convertView.findViewById(R.id.profile_company);
                holder.front_avatar = (ImageView) convertView.findViewById(R.id.profile_avatar_image);
                holder.back_avatar = (ImageView) convertView.findViewById(R.id.profile_back_avatar);
                holder.distance = (TextView) convertView.findViewById(R.id.profile_distance);
                holder.post = (TextView) convertView.findViewById(R.id.profile_post);
                holder.opportunity = (TextView) convertView.findViewById(R.id.opportunity_number);
                holder.address = (TextView) convertView.findViewById(R.id.profile_address);
                holder.offer1 = (TextView) convertView.findViewById(R.id.profile_offer1);

                holder.offer2 = (TextView) convertView.findViewById(R.id.profile_offer2);
                holder.offer3 = (TextView) convertView.findViewById(R.id.profile_offer3);
                holder.needIcon = (ImageView) convertView.findViewById(R.id.need_icon);
                final ViewHolder finalHolder1 = holder;
                holder.needIcon.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
//                        final int stValue = ConvertUtil.dpToPx(20, getContext());

                        int w = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
                        int h = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
                        finalHolder1.needsContainer.measure(w, h);
                        final int heightNeeds = finalHolder1.needsContainer.getMeasuredHeight();
                        Log.e(TAG, "heightNeeds = " + heightNeeds);

                        if (!isShowNeeds) {
                            finalHolder1.needsContainer.setVisibility(View.VISIBLE);
                            LayoutTransition lt = new LayoutTransition();
                            lt.enableTransitionType(LayoutTransition.CHANGE_APPEARING);
                            finalHolder1.needsContainer.setLayoutTransition(lt);
//                            finalHolder1.needsContainer.getLayoutTransition().enableTransitionType(LayoutTransition.CHANGE_APPEARING);
                            ValueAnimator valueAnimator = ValueAnimator.ofInt(0, heightNeeds);
                            valueAnimator.setDuration(5000);
                            valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                                @Override
                                public void onAnimationUpdate(ValueAnimator animation) {
                                    Integer value = (Integer) animation.getAnimatedValue();
                                    finalHolder1.needsContainer.getLayoutParams().height = value.intValue();
                                    finalHolder1.needsContainer.requestLayout();
                                    finalHolder1.profileBottom1.invalidate();

                                }
                            });

                            valueAnimator.start();
                            isShowNeeds = true;

                            Interpolator accelerator = new AccelerateInterpolator();

                            final RotateAnimation animation = new RotateAnimation(0, 90, Animation.RELATIVE_TO_SELF,
                                    0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
                            animation.setDuration(500);
                            animation.setFillAfter(true);
                            animation.setInterpolator(accelerator);

                            v.startAnimation(animation);

                        }else {
                            ValueAnimator valueAnimator = ValueAnimator.ofInt(heightNeeds, 0);
                            valueAnimator.setDuration(500);
                            valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                                @Override
                                public void onAnimationUpdate(ValueAnimator animation) {
                                    Integer value = (Integer) animation.getAnimatedValue();
                                    finalHolder1.needsContainer.getLayoutParams().height = value.intValue();
                                    finalHolder1.needsContainer.requestLayout();
                                    finalHolder1.profileBottom1.invalidate();
                                }
                            });

                            valueAnimator.start();

                            isShowNeeds = false;

                            Interpolator accelerator = new AccelerateInterpolator();

                            final RotateAnimation animation = new RotateAnimation(90, 0, Animation.RELATIVE_TO_SELF,
                                    0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
                            animation.setDuration(500);
                            animation.setFillAfter(true);
                            animation.setInterpolator(accelerator);

                            v.startAnimation(animation);

                        }
                    }
                });
                holder.need1 = (TextView) convertView.findViewById(R.id.needs_1);
                holder.need2 = (TextView) convertView.findViewById(R.id.needs_2);
                holder.need3 = (TextView) convertView.findViewById(R.id.needs_3);

                holder.profileBottom1 = (RelativeLayout) convertView.findViewById(R.id.profile_bottom1);

                holder.needLabel = (TextView) convertView.findViewById(R.id.profile_back_need_lable);
                holder.needsContainer = (LinearLayout) convertView.findViewById(R.id.needs_container);

//                int w = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
//                int h = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
//                holder.needsContainer.measure(w, h);
//                final int heightNeeds = holder.needsContainer.getMeasuredHeight();
//                Log.e(TAG, "heightNeeds = " + heightNeeds);
//
//                final ViewHolder finalHolder2 = holder;
//                holder.needsContainer.post(new Runnable() {
//                    @Override
//                    public void run() {
//                        int height = finalHolder2.needsContainer.getHeight();
//                        Log.e(TAG, "height = " + height);
//                    }
//                });

                holder.front_view = (RelativeLayout) convertView.findViewById(R.id.profile_card_front);
                holder.back_view = (RelativeLayout) convertView.findViewById(R.id.profile_card_back);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            if (profile.getUserName() != null)
                holder.user_name.setText(profile.getUserName());

            if (profile.getOccupation() != null)
                holder.title.setText(profile.getOccupation());

            if (profile.getCompany() != null)
                holder.company.setText(profile.getCompany());

            if (profile.getPost() != null)
                holder.post.setText(profile.getPost());

//            holder.front_view.setVisibility(View.GONE);
//            holder.back_view.setVisibility(View.GONE);
            convertView.setTag(holder);
            final ViewHolder finalHolder = holder;
            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    ViewPropertyAnimator.animate(v).rotationY(90)
//                            .setDuration(300).setListener(null)
//                            .setInterpolator(new AccelerateInterpolator());
//
                    FlipCard.flipCard(finalHolder.front_view, finalHolder.back_view);

                }
            });
            return convertView;
        }

        private class ViewHolder {
            public TextView user_name;
            public TextView title;
            public TextView company;
            public ImageView front_avatar;
            public ImageView back_avatar;
            public TextView post;
            public TextView distance;
            public TextView opportunity;
            public TextView address;
            public TextView offer1;
            public TextView offer2;
            public TextView offer3;
            public RelativeLayout profileBottom1;
            public ImageView needIcon;
            public TextView needLabel;
            public LinearLayout needsContainer;
            public TextView need1;
            public TextView need2;
            public TextView need3;

            public RelativeLayout front_view;
            public RelativeLayout back_view;
        }

    }
}
