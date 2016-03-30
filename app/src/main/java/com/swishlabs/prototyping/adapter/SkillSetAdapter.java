package com.swishlabs.prototyping.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import com.swishlabs.prototyping.R;
import com.swishlabs.prototyping.data.DataManager;
import com.swishlabs.prototyping.entity.Profile;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by will on 16/3/28.
 */
public class SkillSetAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int TYPE_ITEM = 0;
    private static final int TYPE_ADD = 1;

    private List<String> dataList;

    public SkillSetAdapter() {
        Profile myProfile = DataManager.getInstance().getMyProfile();
        if (myProfile.getSkillSet() != null) {
            dataList = new ArrayList<>(Arrays.asList(myProfile.getSkillSet().split(",")));
        }

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        if (viewType == TYPE_ITEM) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.skillset_item_layout, parent, false);
            return new MyViewHolder(view);
        }else {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.skillset_item_add_layout, parent, false);
            return new AddViewHolder(view);

        }
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {

        if (holder instanceof MyViewHolder) {
            ((MyViewHolder)holder).etSkill.setText(dataList.get(position));
            ((MyViewHolder)holder).ivIcon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dataList.remove(position);
                    notifyDataSetChanged();
                }
            });
        }else {

            ((AddViewHolder)holder).ivAddIcon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dataList.add(dataList.size(), ((AddViewHolder) holder).etSkill.getText().toString());
                    ((AddViewHolder) holder).etSkill.setText("");
                    notifyDataSetChanged();
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        if (dataList != null) {
            return dataList.size() + 1;
        }else {
            return 1;
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (dataList != null && position + 1 < getItemCount()) {
            return TYPE_ITEM;
        }else {
            return TYPE_ADD;
        }
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {
        private ImageView ivIcon;
        private EditText etSkill;

        public MyViewHolder(View itemView) {
            super(itemView);
            ivIcon = (ImageView) itemView.findViewById(R.id.skillset_icon);
            etSkill = (EditText) itemView.findViewById(R.id.skillset_input);

        }
    }

    public class AddViewHolder extends RecyclerView.ViewHolder {

        private ImageView ivAddIcon;
        private EditText etSkill;

        public AddViewHolder(View itemView) {
            super(itemView);
            ivAddIcon = (ImageView) itemView.findViewById(R.id.skillset_icon);
            etSkill = (EditText) itemView.findViewById(R.id.skillset_input);
        }
    }
}
