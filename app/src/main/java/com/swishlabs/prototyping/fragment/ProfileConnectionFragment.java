package com.swishlabs.prototyping.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.swishlabs.prototyping.R;
import com.swishlabs.prototyping.entity.Profile;
import com.swishlabs.prototyping.util.CircleTransform;
import com.swishlabs.prototyping.util.FlipCard;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ProfileConnectionFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ProfileConnectionFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileConnectionFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private Profile mProfile;
//    private String mParam2;

    private RelativeLayout front_view;
    private RelativeLayout back_view;

    private ImageView profile_avatar_front;
    private ImageView profile_avatar_back;
    private TextView profile_name_front;
    private TextView profile_name_back;
    private TextView profile_title_front;
    private TextView profile_title_back;
    private TextView profile_location;
    private TextView profile_distance;
    private TextView profile_post;
    private TextView profile_email;

    private OnFragmentInteractionListener mListener;

    public ProfileConnectionFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProfileConnectionFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProfileConnectionFragment newInstance(Profile param1) {
        ProfileConnectionFragment fragment = new ProfileConnectionFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_PARAM1, param1);
//        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mProfile = (Profile)getArguments().getSerializable(ARG_PARAM1);
//            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.profile_card_connection_layout, container, false);

        RelativeLayout profile_card = (RelativeLayout) view.findViewById(R.id.connection_card);
        ((View)profile_card).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FlipCard.flipCard(front_view, back_view);

            }
        });

        ImageView close = (ImageView) view.findViewById(R.id.close);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onButtonPressed();
             }
        });

        front_view = (RelativeLayout) view.findViewById(R.id.profile_card_front);
        back_view = (RelativeLayout) view.findViewById(R.id.profile_card_back);

        profile_avatar_front = (ImageView) view.findViewById(R.id.profile_avatar_image);
        Picasso.with(getActivity()).load(mProfile.getAvatarUrl()).transform(new CircleTransform())
                .fit().into(profile_avatar_front);
        profile_avatar_back = (ImageView) view.findViewById(R.id.profile_back_avatar);
        Picasso.with(getActivity()).load(mProfile.getAvatarUrl()).transform(new CircleTransform())
                .fit().into(profile_avatar_back);

        profile_location = (TextView) view.findViewById(R.id.profile_address);
//        profile_location.setText((mProfile.getCity() == null)? "" : mProfile.getCity());

        profile_name_front = (TextView) view.findViewById(R.id.profile_user_name);
        profile_name_back = (TextView) view.findViewById(R.id.connection_username);

        String userName = mProfile.getDisplayName() == null? "":mProfile.getDisplayName();
        profile_name_front.setText(userName);
        profile_name_back.setText(userName);

        profile_title_front = (TextView) view.findViewById(R.id.profile_title);
        profile_title_back = (TextView) view.findViewById(R.id.connection_occupation);

        String userTitle = mProfile.getOccupation() == null? "":mProfile.getOccupation();
        profile_title_front.setText(userTitle);
        profile_title_back.setText(userTitle);

        profile_email = (TextView) view.findViewById(R.id.profile_email_address);
        profile_email.setText(mProfile.getEmail() == null? "":mProfile.getEmail());

        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed() {
        if (mListener != null) {
            mListener.onFragmentInteraction();
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
//        if (context instanceof OnFragmentInteractionListener) {
//            mListener = (OnFragmentInteractionListener) context;
//        } else {
//            throw new RuntimeException(context.toString()
//                    + " must implement OnFragmentInteractionListener");
//        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public void addListener(OnFragmentInteractionListener listener) {
        mListener = listener;
    }
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
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction();
    }
}
