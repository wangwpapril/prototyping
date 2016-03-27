package com.swishlabs.prototyping.util;


import com.swishlabs.prototyping.R;

public class Enums {

	public enum ConnMethod {
		GET("get"), POST("post"),PUT("put");
		private String type;

		private ConnMethod(String type){
			this.type = type;
		}

		public String getType(){
			return type;
		}

		public void setType(String type){
			this.type = type;
		}

	}
	
	public enum XmcBool{
		False("0"), True("1"), Error("2");
		
		private final String svalue;

		public String getValue() {
			return svalue;
		}
		
		public static XmcBool parseFromValue(String value){
			try {
				int temp = Integer.parseInt(value);
				return XmcBool.values()[temp];
			} catch (Exception e) {
				return XmcBool.Error;
			}
			
		}
		
		XmcBool(String value) {
				this.svalue = value;
		}
	}




	public enum PreferenceType{
		Boolean("Boolean"),
		String("String"),
		Int("Int"),
		Float("Float"),
		Long("Long");
		
		private final String svalue;

		public String getValue() {
			return svalue;
		}
		
		PreferenceType(String value) {
				this.svalue = value;
		}
	} 

	

	
	public enum PreferenceKeys{
		currentUser("current user"),
		sessionId("session id"),
        distance("distance"),
		netStatus("network status̬"),
        firstname("first name"),
        lastname("last name"),
        email("email"),
		username("user name"),
		token("token"),
		password("password"),
		loginStatus("login status"),
        countryCode("country code"),
        currencyCode("currency code"),
        virtualWalletPdf("virtual wallet pdf"),
        showNotification("show notification"),
		showUnit("show unit"),
		userAvatar("user avatar"),
		userId("user id") ;
	
		private final String svalue;

		public String getValue() {
			return svalue;
		}
		
		PreferenceKeys(String value) {
				this.svalue = value;
		}
	}
	


	public enum NetStatus
	{
        Disable(ToastHelper.getStringFromResources(R.string.network_disconnected)),
        WIFI(ToastHelper.getStringFromResources(R.string.network_wifi_connected)),
        MOBILE(ToastHelper.getStringFromResources(R.string.network_mobile_connected));
		
		private final String svalue;

		public String getValue() {
			return svalue;
		}
		
		NetStatus(String value) {
				this.svalue = value;
		}
	}
	
	
}
