package com.swishlabs.prototyping.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.swishlabs.prototyping.R;
import com.swishlabs.prototyping.entity.Profile;
import com.swishlabs.prototyping.helper.ItemTouchHelperAdapter;
import com.swishlabs.prototyping.helper.ItemTouchHelperViewHolder;
import com.swishlabs.prototyping.util.CircleTransform;
import com.swishlabs.prototyping.util.FlipCard;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wwang on 15-10-07.
 */
public class ConnectionsRecyclerAdapter extends RecyclerView.Adapter<ConnectionsRecyclerAdapter.ItemViewHolder>
        implements ItemTouchHelperAdapter{
    public static final int PHONE_ICON_CLICK = 1;
    public static final int EMAIL_ICON_CLICK = 2;
    public static final int OPP_ICON_CLICK = 3;
    public static final int PROFILE_ICON_CLICK = 4;

    protected List<Profile> mListProfile;
    protected Context mContext;
    protected OnRecyclerViewItemClickListener mOnItemClickListener = null;

    public ConnectionsRecyclerAdapter(Context context) {
        mListProfile = new ArrayList<Profile>();
        mContext = context;
    }

    public void setData(List<Profile> listProfile) {
        mListProfile = listProfile;
    }

    public void setOnItemClickListener(OnRecyclerViewItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }

    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.connection_card_layout,parent,false);
        ItemViewHolder itemViewHolder = new ItemViewHolder(view, mOnItemClickListener);
        return itemViewHolder;
    }

    @Override
    public void onBindViewHolder(ItemViewHolder holder, int position) {

        holder.profileNameV.setText( mListProfile.get(position).getUserName());
        holder.profileOccuV.setText( mListProfile.get(position).getOccupation());
        holder.profileNameV2.setText(mListProfile.get(position).getUserName());
//        holder.profileComp.setText( mListProfile.get(position).);
        if(mListProfile.get(position).getAvatarUrl() != null)
            Picasso.with(mContext).load(mListProfile.get(position).getAvatarUrl()).transform(new CircleTransform())
                .fit().into(holder.profileImageV);


    }


    @Override
    public int getItemCount() {
        return mListProfile.size();
    }

    @Override
    public boolean onItemMove(int fromPosition, int toPosition) {
 //       Collections.swap(mItems, fromPosition, toPosition);
        notifyItemMoved(fromPosition, toPosition);
        return true;
//        return false;
    }

    @Override
    public void onItemDismiss(int position) {
 //       mItems.remove(position);
        notifyItemRemoved(position);

    }

    public static interface OnRecyclerViewItemClickListener {
        void OnItemClick(int position);
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder implements ItemTouchHelperViewHolder, View.OnClickListener {

        private ImageView profileImageV;
        private TextView profileNameV;
        private TextView profileOccuV;
        private TextView profileComp;
        private RelativeLayout first_card;
        private RelativeLayout second_card;
        private TextView profileNameV2;
        private ImageView profileIcon;

        protected OnRecyclerViewItemClickListener mOnItemClickListener = null;

        public ItemViewHolder(View itemView, OnRecyclerViewItemClickListener listener) {
            super(itemView);
            this.mOnItemClickListener = listener;
            profileImageV = (ImageView) itemView.findViewById(R.id.connection_avatar);
            profileNameV = (TextView) itemView.findViewById(R.id.connection_username);
            profileOccuV = (TextView) itemView.findViewById(R.id.connection_occupation);
            profileComp = (TextView) itemView.findViewById(R.id.connection_company);

            first_card = (RelativeLayout) itemView.findViewById(R.id.connection_card_first);
            second_card = (RelativeLayout) itemView.findViewById(R.id.connection_card_second);
            profileNameV2 = (TextView) itemView.findViewById(R.id.connetion_card_username2);

            profileIcon = (ImageView) itemView.findViewById(R.id.profile_icon);
            profileIcon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(mOnItemClickListener != null) {
                        mOnItemClickListener.OnItemClick(PROFILE_ICON_CLICK);
                    }
                }
            });
//            itemView.setOnClickListener(this);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    FlipCard.flipCardVertical(first_card, second_card);
                }
            });

        }

        @Override
        public void onItemSelected() {
            itemView.setAlpha(0.5F);
//            itemView.setBackgroundColor(Color.BLUE);
        }

        @Override
        public void onItemClear() {
  //          itemView.setBackgroundColor(0);

        }

        @Override
        public void onClick(View v) {
//            if(mOnItemClickListener != null) {
//                mOnItemClickListener.OnItemClick(v, getPosition());
//            }

        }
    }
}
