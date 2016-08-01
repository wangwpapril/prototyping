package com.swishlabs.prototyping.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.swishlabs.prototyping.R;
import com.swishlabs.prototyping.helper.ItemTouchHelperViewHolder;
import com.swishlabs.prototyping.util.FlipCard;

/**
 * Created by willwang on 16-07-29.
 */
public class OffersRecyclerAdapter extends RecyclerView.Adapter<OffersRecyclerAdapter.ItemViewHolder> {

    private OnItemClickListener mListener;

    public void setItemClickListener(OnItemClickListener listener) {
        this.mListener = listener;
    }

    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.service_card_layout, parent, false);
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

    public interface OnItemClickListener {
        void onItemClick();
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder implements ItemTouchHelperViewHolder{
        private RelativeLayout first_card;
        private RelativeLayout second_card;

        public ItemViewHolder(View itemView) {
            super(itemView);
            first_card = (RelativeLayout) itemView.findViewById(R.id.service_card_first);
            second_card = (RelativeLayout) itemView.findViewById(R.id.service_card_second);
            second_card.setVisibility(View.GONE);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (first_card.getVisibility() == View.VISIBLE) {
                        FlipCard.flipCardVertical(first_card, second_card);
                    }else {
                        FlipCard.flipCardVertical(second_card, first_card);
                    }
                }
            });
        }

        @Override
        public void onItemSelected() {

        }

        @Override
        public void onItemClear() {

        }
    }
}
