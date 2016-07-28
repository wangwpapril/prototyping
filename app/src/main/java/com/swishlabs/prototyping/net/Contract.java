package com.swishlabs.prototyping.net;


import com.swishlabs.prototyping.BuildConfig;

/**
 * Define all of constants about server access protocol
 * 
 * @author
 */
public class Contract {

	/**
	 *  Web server Url for development
	 */
	final static String BASE_URL; 
				
	/**
	 * Define a userinfo-full task type value
	 */
	public final static int TASK_COMPLETE_USER_INFO_TYPE = 1;
	
	/**
	 * Define a contact-full task type value
	 */
	public final static int TASK_COMPLETE_CONTACT_INFO_TYPE = 4;
	
		
	static {
		if (BuildConfig.DEBUG) {
			BASE_URL = "http://grabopwstest.us-west-2.elasticbeanstalk.com/";

		} else {
			BASE_URL = "http://grabopwstest.us-west-2.elasticbeanstalk.com/";
		}
	}

	public final static String REGISTER_URL = "http://grabopws.azurewebsites.net/register?format=json";
	public final static String LOGIN_URL = "http://grabopws.azurewebsites.net/auth?format=json";
	public final static String PROFILE_URL = "http://grabopws.azurewebsites.net/profiles/";
	public final static String CATEGORIES_URL = "http://grabopws.azurewebsites.net/categories";
	public final static String CONNECTIONS = "/connections/";
	public final static String SEARCHDISTANCE = "/profileswithinratio";
	public final static String CHATMESSAGE = "/messages";
	public final static String PICTURES = "/pictures";
	public final static String SERVICES = "/services";

	public final static String FORMAT_JSON = "?format=json";


	public final static String APP_KEY = "123abc";

	public final static String VENDOR_KEY = "123abc";
	
	public final static String APP_PLATFORM = "android";
	
	
	public final static String KEY_HEAD_APP_KEY = "X-Openservice-Application-Name";
	public final static String KEY_HEAD_VENDOR = "X-Openservice-Vendor-Name";	
	public final static String KEY_HEAD_PLATFORM = "X-Openservice-Application-Platform";	
	public final static String KEY_HEAD_TOKEN = "X-Openservice-User-Token";
		

	interface FuncUrls {
		public static final String REGISTER = BASE_URL + "register";
		public static final String LOGIN = BASE_URL + "auth";
		public static final String PROFILE_URL = BASE_URL + "profiles/";

	}
	
	
	interface LocationKeys{
		public static final String LATITUDE = "latitude=";
		public static final String LONGITUDE = "longitude=";
		public static final String CITYNAME = "cityName";
	}

	interface CommonKeys {
		public static final String RESPONSESTATUS = "ResponseStatus";

		public static final String DISTANCE = "distance=";
		
		public static final String ID = "id";

		public static final String PROFILESWITHINRATIO = "/profileswithinratio";

		public static final String OFFSET = "offset=";
		
	}

	interface AccountKeys {
		public static final String USERNAME = "UserName";
		public static final String PASSWORD = "Password";
		public static final String FIRSTNAME ="FirstName";
		public static final String LASTNAME = "LastName";
		public static final String SESSIONID = "SessionId";
	}
	
	interface ChangePwdKey {
		public static final String OLDPWD = "oldpwd";
		public static final String NEWPWD = "newpwd";
	}

	interface ConnectionKey {
		public static final String FROMID = "fromid";
		public static final String TOID = "toid";
		public static final String REQUESTDATE = "dateConnRequested";
		public static final String CONFIRMEDDATE = "dateConnConfirmed";
		public static final String STATUS = "status";
		public static final String NOTIFIED = "notified";
	}
	
	interface UserInfoKeys {
		public static final String ID = "id";
		public static final String USERNAME = "username";
		public static final String EMAIL = "email";
		public static final String FIRSTNAME = "firstname";
		public static final String LASTNAME = "lastname";
		public static final String DISPLAYNAME = "displayname";
		public static final String OCCUPATION = "occupation";
		public static final String PHONE = "phone";
		public static final String SKILLSET = "skillset";
	}

}
