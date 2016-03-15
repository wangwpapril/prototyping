package com.swishlabs.prototyping.fragment;

import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.swishlabs.prototyping.R;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link MyProfileDetailsFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link MyProfileDetailsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MyProfileDetailsFragment extends BaseFragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private static final int SELECT_IMAGE = 1;
    private static final int CAMERA_PHOTO = 2;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public MyProfileDetailsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MyProfileDetailsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MyProfileDetailsFragment newInstance(String param1, String param2) {
        MyProfileDetailsFragment fragment = new MyProfileDetailsFragment();
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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_my_profile_details, container, false);
        view.findViewById(R.id.camera_icon).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (buttonPressedListener != null)
                    buttonPressedListener.onButtonPressed(CAMERA_PHOTO);
            }
        });

        view.findViewById(R.id.folder_icon).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (buttonPressedListener != null)
                    buttonPressedListener.onButtonPressed(SELECT_IMAGE);
            }
        });

        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

}
