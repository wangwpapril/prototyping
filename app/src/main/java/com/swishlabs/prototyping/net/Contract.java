package com.swishlabs.prototyping.net;


import com.swishlabs.prototyping.BuildConfig;

/**
 * Define all of constants about server access protocol
 * 
 * @author HouLin 2014.1.6
 */
public class Contract {
	
	// API doc reference https://qing.wps.cn/l/87491d37d5a64be5b6d25a7e5e0c4cd8.
	
	/**
	 * Release outer web server URL
	 * 
	 */
	// final static String BASE_URL = "http://118.186.211.40";
		
	/**
	 * Debug outer web server Url
	 */
//	final static String BASE_URL = "http://211.154.146.11";
	
	
	/**
	 * Debug inner web server Url for development
	 */
	final static String BASE_URL; 
				
	/**
	 * Debug inter web server for test
	 */
//	final static String BASE_URL = "http://10.10.15.146:9002";
	
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
//			BASE_URL = "http://221.123.155.65/fenghuangapi";
			BASE_URL = "http://211.151.175.157/fenghuangapi";
			
		} else {
			BASE_URL = "http://211.151.175.157/fenghuangapi";			
		}
	}
	
	public final static String APP_KEY = "123abc";

	public final static String VENDOR_KEY = "123abc";
	
	public final static String APP_PLATFORM = "android";
	
	
	public final static String KEY_HEAD_APP_KEY = "X-Openservice-Application-Name";
	public final static String KEY_HEAD_VENDOR = "X-Openservice-Vendor-Name";	
	public final static String KEY_HEAD_PLATFORM = "X-Openservice-Application-Platform";	
	public final static String KEY_HEAD_TOKEN = "X-Openservice-User-Token";
		

	interface FuncUrls {
		public static final String REGISTER = BASE_URL + "/index.php?r=Useraction/register";
		public static final String LOGIN = BASE_URL + "/index.php?r=/Useraction/login";		
		public static final String SEND_CODE = BASE_URL + "/index.php?r=/Useraction/sendcode";
		
		public static final String USER_INFO = BASE_URL + "/index.php?r=User/info";
		
		public static final String OAUTH = BASE_URL + "/index.php?r=Useraction/oauth";
		
		public static final String VIDEOS = BASE_URL + "/index.php?r=Videos/videos";
		
		public static final String FOCUS_IMAGES = BASE_URL + "/index.php?r=Videos/focusimages";
		
		public static final String ATTENTION_USERS = BASE_URL + "/index.php?r=User/attentionlist";
		
		public static final String USER_INFO_MODIFY = BASE_URL + "/index.php?r=User/infoedit";
		
		public static final String VIDEOS_FAVORITE = BASE_URL + "/index.php?r=User/favorite";
		
		public static final String VIDEOS_CUSTOM = BASE_URL + "/index.php?r=Custom/video";
		
		public static final String VIDEOS_CUSTOM_MAKE = BASE_URL + "/index.php?r=Custom/commit";
				
		public static final String CUSTOM_ORDERS_ACCEPT = BASE_URL + "/index.php?r=Custom/acceptlist";
		
		public static final String GET_ORDER = BASE_URL + "/index.php?r=Custom/accept";
		
		public static final String GET_CITY_LIST = BASE_URL + "/index.php?r=User/citylist";
		
		public static final String SEARCH = BASE_URL + "/index.php?r=Videos/search";
		
		public static final String ATTENTION = BASE_URL + "/index.php?r=User/attention";
				
		public static final String ATTENTION_CANCEL = BASE_URL + "/index.php?r=user/cancelattention";
		
		public static final String USER_VIDEOS = BASE_URL + "/index.php?r=user/myvideos";
		
		public static final String ANCHOR_APPLY = BASE_URL + "/index.php?r=User/anchorapply";
		
		public static final String ORDER_ASSIGN = BASE_URL + "/index.php?r=Custom/assign";
		
		public static final String VIDEO_RECORD = BASE_URL + "/index.php?r=Custom/videorecord";
		
		public static final String FAVORITE_VIDEO = BASE_URL + "/index.php?r=User/store";
		
		public static final String PRAISE_VIDEO = BASE_URL + "/index.php?r=User/praise";
		
		public static final String SHARE_VIDEO = BASE_URL + "/index.php?r=User/share";
		
		public static final String PLAY_VIDEO = BASE_URL + "/index.php?r=User/playcount";
		
		
		public static final String INFORM_VIDEO = BASE_URL + "/index.php?r=User/report";
		
		public static final String TAG_LIST = BASE_URL + "/index.php?r=custom/gettags";
		
		public static final String ORDER_CONFIRM = BASE_URL + "/index.php?r=Custom/confirm";
		
		public static final String ORDER_REFUSE = BASE_URL + "/index.php?r=Custom/reject";		
		
		public static final String ORDER_COMMENT = BASE_URL + "/index.php?r=Custom/comment";
		
		public static final String ATTENTION_VIDEOS = BASE_URL + "/index.php?r=user/attentionnews";
		
		public static final String DELETE_VIDEO = BASE_URL + "/index.php?r=User/videodelete";
		
		public static final String CHANGE_PASSSWORD = BASE_URL + "/index.php?r=Useraction/changepwd";
		
				
		public static final String FEEDBACK = BASE_URL + "/index.php?r=User/feedback";
		
		public static final String ANCHOR_BANLANCE = BASE_URL + "/index.php?r=User/balance";
		
		public static final String GET_NOTIFYCATION = BASE_URL + "/index.php?r=User/notice";
		
		public static final String POST_CLIENT_ID = BASE_URL + "/index.php?r=User/clientid";
		
		public static final String GET_UPDATE = BASE_URL + "/index.php?r=User/update";
		
		public static final String ANCHOR_APPLY_INVITE = BASE_URL + "/index.php?r=User/remind";
		
		public static final String ANCHOR_APPLY_MODIFY = BASE_URL + "/index.php?r=User/anchoredit";
		
		public static final String STAR_ANCHOR_BLESS_LIST = BASE_URL + "/index.php?r=User/zhufulist";
		
		public static final String STAR_ANCHOR_BLESS = BASE_URL + "/index.php?r=User/zhufu";		

	}
	
	
	interface LocationKeys{
		public static final String DISTANCE = "moveMileage";
		public static final String LATITUDE = "latitude";
		public static final String LONGITUDE = "longitude";
		public static final String CITYNAME = "cityName";
	}

	interface CommonKeys {
		public static final String TYPE = "type";

		public static final String PAGE_INDEX = "pageindex";
		
		public static final String PAGE_COUNT = "pagecount";
		
		public static final String ACCEPT_STATE = "accept";
		
		public static final String ORDER_ID = "orderid";

		public static final String VIDEO_ID = "videoid";
		
		public static final String ID = "id";
		
		public static final String USER_ID = "userid";
		public static final String IMEI = "Imei";
				
		public static final String CONTENT = "content";
		
		public static final String COMMENT = "comment";
		
		public static final String REASON_TYPE = "reasontype";
		
		public static final String USER_COMMENT = "usercomment";
		
		public static final String USERT_EVALULATE = "userrating";
		
		public static final String CLIENT_ID = "cid";
									
	}

	interface AccountKeys {
		public static final String PHONE = "phone";
		public static final String PASSWORD = "password";
		public static final String VERIFYCODE = "verifycode";
		public static final String OPENDID = "openid";
	}
	
	interface ChangePwdKey {
		public static final String OLDPWD = "oldpwd";
		public static final String NEWPWD = "newpwd";
	}

	
	interface UserInfoKeys {
		public static final String ADDRESS = "Address";
		public static final String NAME = "Name";
		public static final String BIRTHDAY = "Birthday";
		public static final String GENDER = "Gender";
		public static final String DataState = "DataState";
		public static final String CreateTime = "CreateTime";
		public static final String UpdateTime = "UpdateTime";
	}

	interface NewsKeys {
		public static final String FAVORITE_ID = "FavoriteId";
		public static final String FAVORITE_TYPE = "FavoriteType";
		public static final String NEWS_ID = "newsID";	
	}

	interface ContactKeys {
		public static final String SKIP = "skip";
		public static final String TAKE = "take";
	}

	interface CarInfoKeys {
		public static final String LICENSE_NUMBER = "LicenseNumber";
		public static final String CREATE_LICENSE_NUMBER_TIME = "CreateLicenseNumberTime";
		public static final String CAR_TYPE_ID = "CarTypeId";
		public static final String MILEAGE = "Mileage";
		public static final String COLOR = "Color";
	}
	
}
