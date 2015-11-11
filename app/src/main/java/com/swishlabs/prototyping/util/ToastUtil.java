package com.swishlabs.prototyping.util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.TextUtils;
import android.view.Gravity;
import android.widget.Toast;

import com.swishlabs.prototyping.R;


@SuppressLint("ShowToast")
public class ToastUtil {
	
	private  static void showToastImpl(Context context, CharSequence text, int duration) {
		if (TextUtils.isEmpty(text)) {
			text = "";
		}
		Toast toast = Toast.makeText(context, text, duration);
//		LayoutInflater inflate = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//		View v = inflate.inflate(R.layout.toast, null);
//		TextView txtView = (TextView) v.findViewById(R.id.txtViewContent);
//		toast.setView(v);
//		txtView.setText(text);
		toast.setGravity(Gravity.CENTER, 0, 0);
		toast.show();
	}
	
	
	public static void showToast(Context context, CharSequence text) {
//		showToastImpl(context, text, Toast.LENGTH_SHORT);
		if(context == null)
			return;
		
		if (text == null){
			text = context.getResources().getString(R.string.error_unknown);
		}
		
		Toast.makeText(context, text, Toast.LENGTH_LONG).show();
	}
	
	public static void showToast(Context context, int resId) {
		showToast(context, context.getString(resId));
	}
}

