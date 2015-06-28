package com.swishlabs.prototyping.pal.internal;

import android.content.Context;
import android.net.wifi.WifiManager;
import android.telephony.TelephonyManager;


public class PalPlatform {

	public static boolean isOpenWIFI(Context mContext) {

		WifiManager mWifiManager = (WifiManager) mContext
				.getSystemService(Context.WIFI_SERVICE);
		if (mWifiManager.isWifiEnabled()) {
			if (mWifiManager.getWifiState() == WifiManager.WIFI_STATE_ENABLED) {
				return true;
			} else
				return false;
		} else {
			return false;
		}
	}

	
	public static void setWifiEnable(Context context,boolean enable){
		
		WifiManager wifimanager = (WifiManager) context
		.getSystemService(Context.WIFI_SERVICE);
		wifimanager.setWifiEnabled(enable);
	}
	
	
	public static String getIMEI(Context mContext) {
		TelephonyManager telephonyManager = (TelephonyManager) mContext
				.getSystemService(Context.TELEPHONY_SERVICE);
		String imei = telephonyManager.getDeviceId();
		return imei;
	
	}

	
	public static String getIMSI(Context mContext) {
		TelephonyManager telephonyManager = (TelephonyManager) mContext
		.getSystemService(Context.TELEPHONY_SERVICE);
		
		
		String imsi = telephonyManager.getSubscriberId();
		return imsi;	
	}
	
	
}
