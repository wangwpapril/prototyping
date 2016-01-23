package com.swishlabs.prototyping.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.swishlabs.prototyping.R;
import com.swishlabs.prototyping.customViews.cardstack.SwipeDeck;
import com.swishlabs.prototyping.entity.Profile;

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

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    private SwipeDeck cardStack;

    private List<Profile> mListProfile;

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

//        mListProfile = new ArrayList<>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_card_stack, container, false);

        cardStack = (SwipeDeck) view.findViewById(R.id.swipe_deck);

        final SwipeDeckAdapter adapter = new SwipeDeckAdapter(mListProfile, inflater);

        cardStack.setAdapter(adapter);

        cardStack.setLeftImage(R.id.accept_image);

        cardStack.setRightImage(R.id.reject_image);

        cardStack.setEventCallback(new SwipeDeck.SwipeEventCallback() {
            @Override
            public void cardSwipedLeft() {

            }

            @Override
            public void cardSwipedRight() {

            }

            @Override
            public void cardClicked() {

            }

            @Override
            public void cardsDepleted() {

            }
        });

        return view;
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
//            View v = convertView;
            if (convertView == null) {
//                LayoutInflater inflater = getLayoutInflater(context);
                // normally use a viewholder
                holder = new ViewHolder();
                convertView = inflater.inflate(R.layout.profileview_layout, parent, false);

                holder.user_name = (TextView) convertView.findViewById(R.id.profile_user_name);
                holder.title = (TextView) convertView.findViewById(R.id.profile_title);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            //((TextView) v.findViewById(R.id.textView2)).setText(data.get(position));

            holder.user_name.setText(mListProfile.get(position).getUserName());
            holder.title.setText(mListProfile.get(position).getOccupation());
            convertView.setTag(holder);
            return convertView;
        }

        private class ViewHolder {
            public TextView user_name;
            public TextView title;
        }

    }
}
