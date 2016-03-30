package com.swishlabs.prototyping.fragment;

import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.swishlabs.prototyping.R;
import com.swishlabs.prototyping.adapter.CustomLinearLayoutManager;
import com.swishlabs.prototyping.adapter.SkillSetAdapter;
import com.swishlabs.prototyping.data.DataManager;
import com.swishlabs.prototyping.entity.Profile;
import com.swishlabs.prototyping.util.CircleTransform;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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

    public static final int SELECT_IMAGE = 1;
    public static final int CAMERA_PHOTO = 2;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    private Profile myProfile;

    private EditText etFirstName;
    private EditText etLastName;
    private EditText etPost;
    private EditText etEmail;
    private EditText etPhone;
    private EditText etTitle;
    private EditText etCompany;
    private TextView etCounter;

    private ImageView ivAvatar;

    private List<String> skillsetList;
    private RecyclerView recyclerView;
    private SkillSetAdapter skillSetAdapter;

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

        myProfile = DataManager.getInstance().getMyProfile();
        if (myProfile.getSkillSet() != null) {
            skillsetList = new ArrayList<>(Arrays.asList(myProfile.getSkillSet().split(",")));
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

        etFirstName = (EditText) view.findViewById(R.id.first_name_input);
        if (myProfile.getFirstName() != null) {
            etFirstName.setText(myProfile.getFirstName());
        }
        etLastName = (EditText) view.findViewById(R.id.last_name_input);
        if (myProfile.getLastName() != null) {
            etLastName.setText(myProfile.getLastName());
        }
        etPost = (EditText) view.findViewById(R.id.statement_input);
        if (myProfile.getPost() != null) {
            etPost.setText(myProfile.getPost());
        }
        etPost.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() > 120) {
                    s.delete(s.length()-1, s.length());
                    return;
                }
                etCounter.setText(Integer.toString(120-s.length()));

            }
        });
        etCounter = (TextView) view.findViewById(R.id.statement_count);
        etCounter.setText(Integer.toString(120-etPost.getText().length()));

        etEmail = (EditText) view.findViewById(R.id.email_input);
        if (myProfile.getEmail() != null) {
            etEmail.setText(myProfile.getEmail());
        }
        etPhone = (EditText) view.findViewById(R.id.phone_input);
        if (myProfile.getPhone() != null) {
            etPhone.setText(myProfile.getPhone());
        }
        etTitle = (EditText) view.findViewById(R.id.job_title_input);
//        if (myProfile.get)
        etCompany = (EditText) view.findViewById(R.id.company_input);

        ivAvatar = (ImageView) view.findViewById(R.id.profile_avatar);
        if (myProfile.getAvatarUrl() != null) {
            Picasso.with(getActivity()).load(myProfile.getAvatarUrl()).transform(new CircleTransform())
                    .fit().into(ivAvatar);
        }

//        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView = (RecyclerView) view.findViewById(R.id.skillset_list);
        final CustomLinearLayoutManager layoutManager = new CustomLinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);

        recyclerView.setHasFixedSize(false);
        recyclerView.setLayoutManager(layoutManager);
        skillSetAdapter = new SkillSetAdapter();
        recyclerView.setAdapter(skillSetAdapter);

        recyclerView.setNestedScrollingEnabled(false); // Disables scrolling for RecyclerView, CustomLinearLayoutManager used instead of MyLinearLayoutManager
        // recyclerView.setHasFixedSize(false);

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                int visibleItemCount = layoutManager.getChildCount();
                int totalItemCount = layoutManager.getItemCount();
                int lastVisibleItemPos = layoutManager.findLastVisibleItemPosition();
                Log.i("getChildCount", String.valueOf(visibleItemCount));
                Log.i("getItemCount", String.valueOf(totalItemCount));
                Log.i("lastVisibleItemPos", String.valueOf(lastVisibleItemPos));
                if ((visibleItemCount + lastVisibleItemPos) >= totalItemCount) {
                    Log.i("LOG", "Last Item Reached!");
                }
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

//    public class SkillSetAdapter extends RecyclerView.Adapter<SkillSetAdapter.MyViewHolder> {
//
//        public SkillSetAdapter() {
//
//        }
//
//        @Override
//        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.skillset_item_layout, parent, false);
//            return new MyViewHolder(view);
//        }
//
//        @Override
//        public void onBindViewHolder(SkillSetAdapter.MyViewHolder holder, int position) {
//
//        }
//
//        @Override
//        public int getItemCount() {
//            return 6;
//        }
//
//        public class MyViewHolder extends RecyclerView.ViewHolder {
//            public MyViewHolder(View itemView) {
//                super(itemView);
//            }
//        }
//    }


}
