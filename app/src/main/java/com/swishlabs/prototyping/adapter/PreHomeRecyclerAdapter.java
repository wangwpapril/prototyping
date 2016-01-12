package com.swishlabs.prototyping.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.swishlabs.prototyping.R;
import com.swishlabs.prototyping.entity.Profile;
import com.swishlabs.prototyping.helper.ItemTouchHelperAdapter;
import com.swishlabs.prototyping.helper.ItemTouchHelperViewHolder;
import com.swishlabs.prototyping.util.CircleTransform;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wwang on 15-10-07.
 */
public class PreHomeRecyclerAdapter extends RecyclerView.Adapter<PreHomeRecyclerAdapter.ItemViewHolder>
        implements ItemTouchHelperAdapter{

    protected List<Profile> mListProfile;
    protected Context mContext;
    protected OnRecyclerViewItemClickListener mOnItemClickListener = null;

    public PreHomeRecyclerAdapter(Context context) {
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
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.prehome_gridview_profiles_layout,parent,false);
        ItemViewHolder itemViewHolder = new ItemViewHolder(view, mOnItemClickListener);
        return itemViewHolder;
    }

    @Override
    public void onBindViewHolder(ItemViewHolder holder, int position) {

        holder.profileNameV.setText( mListProfile.get(position).getUserName());
        holder.profileOccuV.setText( mListProfile.get(position).getOccupation());
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
        void OnItemClick(View view, int position);
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder implements ItemTouchHelperViewHolder, View.OnClickListener {

        private ImageView profileImageV;
        private TextView profileNameV;
        private TextView profileOccuV;
        private TextView profileDisV;
        protected OnRecyclerViewItemClickListener mOnItemClickListener = null;

        public ItemViewHolder(View itemView, OnRecyclerViewItemClickListener listener) {
            super(itemView);
            this.mOnItemClickListener = listener;
            profileImageV = (ImageView) itemView.findViewById(R.id.profile_image);
            profileNameV = (TextView) itemView.findViewById(R.id.profile_user_name);
            profileOccuV = (TextView) itemView.findViewById(R.id.profile_occupation);
            profileDisV = (TextView) itemView.findViewById(R.id.profile_distance);
            itemView.setOnClickListener(this);

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
            if(mOnItemClickListener != null) {
                mOnItemClickListener.OnItemClick(v, getPosition());
            }

        }
    }
}
