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
import com.swishlabs.prototyping.activity.MainActivity;
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
    private ToggleButton mSwitchUnit;
    private TextView mButtonSave;
    private TextView mButtonCancel;
    private SeekBar mSeekDistance;
    private TextView mSeekDistanceText;
    private TextView mTextOn;
    private TextView mTextOff;
    private TextView mTextKm;
    private TextView mTextMi;

    private int _progress;
    private int oldProgress;
    private boolean notiChecked = false;
    private boolean unitChecked = false;
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

        mTextOn = (TextView) view.findViewById(R.id.text_on);
        mTextOff = (TextView) view.findViewById(R.id.text_off);
        mTextKm = (TextView) view.findViewById(R.id.text_km);
        mTextMi = (TextView) view.findViewById(R.id.text_mi);

        mSwitchNotification = (ToggleButton)view.findViewById(R.id.showNotification_switch);
        if (SharedPreferenceUtil.getBoolean(Enums.PreferenceKeys.showNotification.toString(), false)) {
            mSwitchNotification.setToggleOn();
            mTextOn.setTextColor(getActivity().getResources().getColor(R.color.dark_blue));
            mTextOff.setTextColor(getActivity().getResources().getColor(R.color.gray));
        }else {
            mSwitchNotification.setToggleOff();
            mTextOn.setTextColor(getActivity().getResources().getColor(R.color.gray));
            mTextOff.setTextColor(getActivity().getResources().getColor(R.color.dark_blue));
        }

        mSwitchNotification.setOnToggleChanged(new ToggleButton.OnToggleChanged() {
            @Override
            public void onToggle(boolean on) {
                notiChecked = on;

                if (on) {
                    mTextOn.setTextColor(getActivity().getResources().getColor(R.color.dark_blue));
                    mTextOff.setTextColor(getActivity().getResources().getColor(R.color.gray));

                }else {
                    mTextOn.setTextColor(getActivity().getResources().getColor(R.color.gray));
                    mTextOff.setTextColor(getActivity().getResources().getColor(R.color.dark_blue));

                }
            }
        });

        mSeekDistance = (SeekBar)view.findViewById(R.id.distanceSlider);
        mDistance = SharedPreferenceUtil.getInt(Enums.PreferenceKeys.distance.toString(), 5);
        mSeekDistance.setProgress(mDistance);
        oldProgress = mSeekDistance.getProgress();
        _progress = mSeekDistance.getProgress();
        mSeekDistance.setMax(100);

        mSeekDistanceText = (TextView)view.findViewById(R.id.unitText);
        mSeekDistanceText.setText(mDistance + "Km");

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
                    mSeekDistanceText.setText(progress + "Km");
                } else
                    seekBar.setProgress(oldProgress);
            }
        });


        mSwitchUnit = (ToggleButton) view.findViewById(R.id.unit_switch);
        if (SharedPreferenceUtil.getBoolean(Enums.PreferenceKeys.showUnit.toString(), false)) {
            mSwitchUnit.setToggleOn();
            mTextKm.setTextColor(getActivity().getResources().getColor(R.color.dark_blue));
            mTextMi.setTextColor(getActivity().getResources().getColor(R.color.gray));
        }else {
            mSwitchUnit.setToggleOff();
            mTextKm.setTextColor(getActivity().getResources().getColor(R.color.gray));
            mTextMi.setTextColor(getActivity().getResources().getColor(R.color.dark_blue));
        }

        mSwitchUnit.setOnToggleChanged(new ToggleButton.OnToggleChanged() {
            @Override
            public void onToggle(boolean on) {
                unitChecked = on;

                if (on) {
                    mTextKm.setTextColor(getActivity().getResources().getColor(R.color.dark_blue));
                    mTextMi.setTextColor(getActivity().getResources().getColor(R.color.gray));

                }else {
                    mTextKm.setTextColor(getActivity().getResources().getColor(R.color.gray));
                    mTextMi.setTextColor(getActivity().getResources().getColor(R.color.dark_blue));

                }
            }
        });
            //buttons
        mButtonSave = (TextView)view.findViewById(R.id.save_button_defaultsetting);
        mButtonSave.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                saveAllSettings();
                ((MainActivity) getActivity()).removeCurrFragment();
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
//                getActivity().getSupportFragmentManager().beginTransaction().remove(((MainActivity) getActivity()).mCurrFragment).commit();
//                getActivity().getSupportFragmentManager().popBackStack();
                ((MainActivity) getActivity()).removeCurrFragment();
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
        SharedPreferenceUtil.setBoolean(Enums.PreferenceKeys.showNotification.toString(), notiChecked);
        SharedPreferenceUtil.setBoolean(Enums.PreferenceKeys.showUnit.toString(), unitChecked);

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
