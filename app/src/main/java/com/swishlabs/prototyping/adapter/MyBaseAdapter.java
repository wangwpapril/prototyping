package com.swishlabs.prototyping.adapter;


import android.view.LayoutInflater;
import android.widget.BaseAdapter;
import com.swishlabs.prototyping.util.ImageLoader;


import com.swishlabs.prototyping.R;
import com.swishlabs.prototyping.activity.BaseActivity;

public abstract class MyBaseAdapter extends BaseAdapter {

	protected BaseActivity context;
	protected LayoutInflater layoutInflater;
	protected ImageLoader ImageLoader;
	protected void init(){
		layoutInflater = LayoutInflater.from(context);
		if (ImageLoader == null) {
			ImageLoader = new ImageLoader(context, R.drawable.ic_launcher);
		}
	}
}