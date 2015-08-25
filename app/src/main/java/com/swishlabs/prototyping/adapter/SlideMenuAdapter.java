package com.swishlabs.prototyping.adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.swishlabs.prototyping.MyApplication;
import com.swishlabs.prototyping.R;

import java.util.Arrays;
import java.util.List;
import java.util.Set;


public class SlideMenuAdapter extends BaseAdapter {
	
	private List<String> mMenuList=null;	
	private Context mContext;
	
	private Set<String> mMenuHint = null;
	
	private MyApplication mApp;
	
	public SlideMenuAdapter(Context context, List<String> menuList, Set<String> menuHintList){
		mMenuList = menuList;
		mContext = context;		
		mMenuHint = menuHintList;
		mApp = (MyApplication) context.getApplicationContext();
	}	
	
	public SlideMenuAdapter(Context context, String[] menuList, Set<String> menuHintList){
		mMenuList = Arrays.asList(menuList);
		mContext = context;
		mMenuHint = menuHintList;
		mApp = (MyApplication) context.getApplicationContext();
	}	


	@Override
	public int getCount() {
		return mMenuList == null ? 0 : mMenuList.size();
	}

	@Override
	public String getItem(int position) {
		return mMenuList.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	void bindNotifyMsgHintCount(View parentView) {
/*		HintInfo item = mApp.getHintInfo();
		TextView txtView = (TextView) parentView.findViewById(R.id.txtViewNumber);
		txtView.setText(item.getDisplayMsgCount());*/
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null){
			convertView = LayoutInflater.from(mContext).inflate(R.layout.adapter_slidemenu, null);
		}		
		String menu = getItem(position);
		View iconHint = convertView.findViewById(R.id.imgViewIconHint);
		if (mMenuHint != null && mMenuHint.contains(menu)) {
			iconHint.setVisibility(View.VISIBLE);
			bindNotifyMsgHintCount(convertView);
		} else {
			iconHint.setVisibility(View.INVISIBLE);
		}
		
		TextView txtView  = (TextView) convertView.findViewById(R.id.txtViewMenuName);
		txtView.setText(getItem(position));
		txtView.setTextColor(mContext.getResources().getColor(R.color.white));
				
		if (!isEnabled(position)) {
			txtView.setTextColor(mContext.getResources().getColor(R.color.blue));
		}
		return convertView;
	}
	
	@Override
	public boolean isEnabled(int position) {
//		if (getItem(position).equals(mContext.getString(R.string.menu_apply_anchor))){
//			if(mApp.getOnlineUser().isAnchor()){
//				return false;
//			}
//		}
		return super.isEnabled(position);
	}
	
}
