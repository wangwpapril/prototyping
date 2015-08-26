package com.swishlabs.prototyping.net;

import android.net.Uri;


public interface Constant {			
	public static final float VIDEO_RATIO = 1.0f;	
	public static final int PAGE_COUNT = 10;		
	
	public final static String ACTION_NOTIFICATION_CLICK = "com.huachuang.action.notification.click";
	

	public interface Sms{
		public static final Uri CONTENT_URI_INBOX = Uri.parse("content://sms/inbox");
		
		public static final Uri MESSAGE_URI = Uri.parse("content://mms-sms/");
		
		public final static class Columns {			
			public static final String ID = "_id";
			public static final String TYPE = "type";
		    public static final String THREAD_ID = "thread_id";
		    public static final String ADDRESS = "address";
		    public static final String PERSON_ID = "person";
		    public static final String DATE = "date";
		    public static final String READ = "read";
		    public static final String BODY = "body";
		}
	}
		
	
	public interface PreferKeys{
		
		public final static String KEY_USER_ACCOUNT = "account";
		public final static String KEY_USER_PSW = "password";
	
		
		public final static String KEY_TOKEN = "token";		
		
		public final static String KEY_VERSION = "version";
		
		public final static String KEY_LOGIN_WAY = "login_way";
		
		public final static String KEY_VIDEO_QUALITY = "video_quality";
		
		public final static String KEY_FIRST_CREATE_ORDER = "first_create_order";
		
		public final static String KEY_FIRST_LOGIN = "is_first_login";
		
		public final static String KEY_ONLINE_USER = "user_info";
		
		public final static String KEY_PUSH_SWITCH = "push_switch";
		
		public final static String KEY_GETUI_CLIENTID = "getui_client_id";
		
		public final static String KEY_NOTIFY_ORDER = "notify_order";
		
		public final static String KEY_NOTIFY_ORDER_TYPE = "notify_order_type";
		
		public final static String KEY_NOTIFY_MSG = "notify_msg";		
				
		public final static String KEY_START_APPLY_ANCHOR = "start_apply_anchor";
		
		public final static String KEY_HAS_DISPLAY_GUIDE_PAGE = "key_has_display_guide_page";
		
		public final static String KEY_HAS_DISPLAY_SCRAWL = "key_has_display_scrawl";
		
		public final static String KEY_IS_FORCE_UPDATE = "key_is_force_update";
		
		public final static String KEY_SHOW_UPDATE_COUNT = "key_show_update_count";
		
		public final static String KEY_BACK_TO_SQUARE = "key_back_to_square";
	}

	public interface PreferValues{
		
		public final static String VALUE_HIGHT_QUALITY = "hight";
		
		public final static String VALUE_LOW_QUALITY = "low";
			
	}
	
	
	public interface BundleKeys{
		
		public final static String KEY_PHONE = "phone";
			
		public final static String KEY_PASSWORD = "password";
		
		public final static String KEY_IS_APPLY_ANCHOR = "is_apply_anchor";
		
		
	}
	
	public interface ResponseCode{
		
		public final static int RES_ERROR_RELOGIN = 401;
		
	}

	public interface BroadConstant{
		
		public final static String BROAD_RE_LOGIN = "broad_re_login";
		
	}
	
}
