package com.swishlabs.prototyping.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.swishlabs.prototyping.R;
import com.swishlabs.prototyping.helper.ItemTouchHelperAdapter;
import com.swishlabs.prototyping.helper.ItemTouchHelperViewHolder;

/**
 * Created by wwang on 15-10-07.
 */
public class PreHomeRecyclerAdapter extends RecyclerView.Adapter<PreHomeRecyclerAdapter.ItemViewHolder>
        implements ItemTouchHelperAdapter{
    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.prehome_gridview_profiles_layout,parent,false);
        ItemViewHolder itemViewHolder = new ItemViewHolder(view);
        return itemViewHolder;
    }

    @Override
    public void onBindViewHolder(ItemViewHolder holder, int position) {

    }


    @Override
    public int getItemCount() {
        return 10;
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

    public class ItemViewHolder extends RecyclerView.ViewHolder implements ItemTouchHelperViewHolder {
        public ItemViewHolder(View itemView) {
            super(itemView);
        }

        @Override
        public void onItemSelected() {
//            itemView.setBackgroundColor(Color.BLUE);
        }

        @Override
        public void onItemClear() {
  //          itemView.setBackgroundColor(0);

        }
    }
}
