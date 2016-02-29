package com.swishlabs.prototyping.fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.swishlabs.prototyping.R;
import com.swishlabs.prototyping.customViews.ToggleButton;
import com.swishlabs.prototyping.util.Enums;
import com.swishlabs.prototyping.util.SharedPreferenceUtil;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link SettingFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link SettingFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SettingFragment extends BaseFragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    private ToggleButton mSwitchNotification;
    private TextView mButtonSave;
    private TextView mButtonCancel;
    private SeekBar mSeekDistance;
    private TextView mSeekDistanceText;
    private int _progress;
    private int oldProgress;
    private boolean checked = false;
    private ImageView mDrawerImage;
    private Button mBackButt;
    private TextView mTitile;
    private int mDistance;


    public SettingFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SettingFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SettingFragment newInstance(String param1, String param2) {
        SettingFragment fragment = new SettingFragment();
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
        View view = inflater.inflate(R.layout.fragment_setting, container, false);

        mTitile = (TextView)view.findViewById(R.id.titleText);
//        mTitile.setTypeface(TypefaceManager.GetInstance(this.getAssets()).getTypeFace(FontsTypeface.HelveticaNeue_Light));

//        ((TextView)findViewById(R.id.switchText)).setTypeface(TypefaceManager.GetInstance(this.getAssets()).getTypeFace(FontsTypeface.HelveticaNeue_Light));
//        ((TextView)findViewById(R.id.distanceText)).setTypeface(TypefaceManager.GetInstance(this.getAssets()).getTypeFace(FontsTypeface.HelveticaNeue_Light));


        mSwitchNotification = (ToggleButton)view.findViewById(R.id.showNotification_switch);
//		mSwitchNotification.setChecked(GrabopApplication.SharedPreference.getBoolean(GrabOpSharedPreference.SHOW_NOTIFICATION, false));
//        checked = GrabopApplication.SharedPreference.getBoolean(GrabOpSharedPreference.SHOW_NOTIFICATION,false);
        checked = SharedPreferenceUtil.getBoolean(Enums.PreferenceKeys.showNotification.toString(), false);
        if(checked)
            mSwitchNotification.setToggleOn();
        else
            mSwitchNotification.setToggleOff();

        mSwitchNotification.setOnToggleChanged(new ToggleButton.OnToggleChanged() {
            @Override
            public void onToggle(boolean on) {
                checked = on;

            }
        });
        //put all the settings here

//        mBackButt = (Button)view.findViewById(R.id.back_button);
//        mBackButt.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
////                showCloseDialog();
//            }
//        });
        mSeekDistance = (SeekBar)view.findViewById(R.id.distanceSlider);
        mDistance = SharedPreferenceUtil.getInt(Enums.PreferenceKeys.distance.toString(), 5);
        mSeekDistance.setProgress(mDistance);
        oldProgress = mSeekDistance.getProgress();
        _progress = mSeekDistance.getProgress();
        mSeekDistance.setMax(100);

        mSeekDistanceText = (TextView)view.findViewById(R.id.distanceAmountText);
        mSeekDistanceText.setText(mDistance + "");
//        mSeekDistanceText.setTypeface(TypefaceManager.GetInstance(this.getAssets()).getTypeFace(FontsTypeface.HelveticaNeue_Light));

        mSeekDistance.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress,
                                          boolean fromUser) {
                if (progress % 5 == 0) {
                    _progress = progress;
                    oldProgress = _progress;
                    seekBar.setProgress(progress);
                    mSeekDistanceText.setText(progress + "");
                } else
                    seekBar.setProgress(oldProgress);
            }
        });


        //buttons
        mButtonSave = (TextView)view.findViewById(R.id.save_button_defaultsetting);
        mButtonSave.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                saveAllSettings();
            }
        });
//        mButtonSave.setTypeface(TypefaceManager.GetInstance(this.getAssets()).getTypeFace(FontsTypeface.HelveticaNeue_Light));

        mButtonCancel = (TextView)view.findViewById(R.id.cancel_button_defaultsetting);
        mButtonCancel.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                showCloseDialog();
            }
        });
//        mButtonCancel.setTypeface(TypefaceManager.GetInstance(this.getAssets()).getTypeFace(FontsTypeface.HelveticaNeue_Light));

        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    private void showCloseDialog()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("All configurations will be lost.. Do you want to continue?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
//                Intent backHomeActivity = new Intent();
//                setResult(RESULT_CANCELED, backHomeActivity);
//                finish();
            }
        });

        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        Dialog dialog = builder.create();
        dialog.show();
    }

    private void saveAllSettings()
    {
        SharedPreferenceUtil.setBoolean(Enums.PreferenceKeys.showNotification.toString(), checked);
        SharedPreferenceUtil.setInt(Enums.PreferenceKeys.distance.toString(), _progress);

//        // go back to previous activity
//        Intent backHomeActivity = new Intent();
//        setResult(RESULT_OK, backHomeActivity);
//        finish();
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
//    public interface OnFragmentInteractionListener {
//        // TODO: Update argument type and name
//        void onFragmentInteraction(Uri uri);
//    }
}
