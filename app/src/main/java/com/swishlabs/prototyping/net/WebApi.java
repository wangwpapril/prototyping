package com.swishlabs.prototyping.net;

import android.content.Context;


/**
 * A proxy class, which is in charge of connnect with web-server. This
 * implements all of Net-IO functions.
 * 
 * @author Administrator
 * 
 */
public abstract class WebApi {

	private static WebApi instance;

	final static String TAG = "WebApi";

	public static synchronized WebApi getInstance(Context context) {
		if (instance == null) {
			instance = new WebApiImpl(context);
		}
		return instance;
	}
	
	/**
	 * True if user has logined.
	 * @return
	 */
//	public abstract boolean isLogined();
	
	/**
	 * Set a token info get from login response.
	 * @param token
	 */
	public abstract void setToken(String token); 

	/**
	 * Enable test model,only for unit test.
	 */
	public abstract void enableTestModel();


	/**
	 * Registed a new user.
	 * @param type
	 * @param phoneNum
	 * @param password
	 * @param code
	 * @param listener
	 */
	public abstract <T> void register(String firstName, String lastName, String userName, String email, String password, IResponse<T> listener);
/*
	public abstract <T> void forgetPsw(String phoneNum, String password, String code, IResponse<T> listener);
	
	public abstract <T> void changePsw(String oldPsw, String newPsw, IResponse<T> listener);
*/
	public abstract <T> void login(String userName, String passWord, IResponse<T> listener);

	public abstract <T> void getProfile(String id, IResponse<T> listener);

	public abstract <T> void getProfiles(String id, int offset, IResponse<T> listener);

	public abstract <T> void getConnections(String id, IResponse<T> listener);

	public abstract <T> void getService(String id, IResponse<T> listener);
	
	/**
	 * Login for third authroized.
	 * @param openid
	 * @param listener
	 */
//	public abstract <T> void login(String openId, int type, IResponse<T> listener);
	
	
//	public abstract <T> void sendCode(String phoneNun, IResponse<T> listener);
		
	
	/**
	 * Get user info.
	 * @param userId
	 * @param listener
	 */
//	public abstract <T> void getUserInfo(int userId, IResponse<T> listener);
	
	/**
	 * Get a video list base a special type.
	 * @param type
	 * @param pageIndex
	 * @param pageCount
	 * @param listener
	 */
//	public abstract <T> void getVideoList(int type, int pageIndex, int pageCount, IResponse<T> listener);
	
	/**
	 * Get focus images.
	 * @param pageIndex
	 * @param pageCount
	 * @param listener
	 */
//	public abstract <T> void getFocusImageList(int pageIndex, int pageCount, IResponse<T> listener);
	
	/**
	 * Get a anchor list to a user attentioned.
	 * 
	 * @param type 0: all, 1: only anchor
	 * @param pageIndex
	 * @param pageCount
	 * @param listener
	 */
//	public abstract <T> void getAttetionUsers(int type,int pageIndex, int pageCount, IResponse<T> listener);
	
	
	/**
	 * Get favorite videos
	 * @param pageIndex
	 * @param pageCount
	 * @param listener
	 */
//	public abstract <T> void getFavoriteVideos(int pageIndex, int pageCount, IResponse<T> listener);
	
//	public abstract <T> void getCustomVideos(int pageIndex, int pageCount, IResponse<T> listener);
	
	/**
	 * Create a order.
	 * @param order
	 * @param file
	 * @param listener
	 */
//	public abstract <T> void postCustomOrder(CustomOrderPost order, Map<String, Object> file,IResponse<T> listener);
	
	/**
	 * Modify user info.
	 * @param info
	 * @param listener
	 */
//	public abstract <T> void postUserInfo(UserInfo info, IResponse<T> listener);
	
	/**
	 * 
	 * @param type 0:my order. 1: my accept order, 2:all of public orders.
	 * @param pageIndex
	 * @param pageCount
	 * @param listener
	 */
//	public abstract <T> void getCustomOrders(int type, int pageIndex, int pageCount, IResponse<T> listener);
		
	
	/**
	 * 
	 * @param orderId
	 * @param isAccept 
	 * @param listener
	 */
/*	public abstract <T> void postGetOrder(int orderId,boolean isAccept, IResponse<T> listener);
	
	
	public abstract <T> void getCityList(IResponse<T> listener);
*/
	
	/**
	 * 
	 * @param type 0:video;1:user
	 * @param content
	 * @param pageIndex
	 * @param pageCount
	 * @param listener
	 */
/*	public abstract <T> void search(int type,String content,int pageIndex, int pageCount, IResponse<T> listener);
	
	
	public abstract <T> void postAtUser(int userId, IResponse<T> listener);
	
	public abstract <T> void postCancelAtUser(int userId, IResponse<T> listener);
			
	public abstract <T> void getUserVideos(int userId, int pageIndex, int pageCount, IResponse<T> listener);
	
	public abstract <T> void getFriendsVideos(int pageIndex, int pageCount, IResponse<T> listener);
*/
	/**
	 * 
	 * @param realName
	 * @param id
	 * @param idSnap A image data.
	 * @param price
	 * @param listener
	 * 
	 */
//	public abstract <T> void postApplyAnchor(AnchorApplyInfo info, IResponse<T> listener);
	
//	public abstract <T> void postOrderAssign(int userId, int orderId, IResponse<T> listener);
	
//	public abstract <T> void postVideoInfo(VideoInfoPost info, Map<String, Object> file, IResponse<T> listener,
//			IHttp.OnPostProgressListener progressListener);
	
	/**
	 * 
	 * @param orderId
	 * @param type 0 : positive ,1: negative
	 * @param listener
	 */
/*	public abstract <T> void favoriteVideo(int videoId,int type, IResponse<T> listener);
	
	public abstract <T> void praiseVideo(int videoId,IResponse<T> listener);
	
	public abstract <T> void shareVideo(int videoId,IResponse<T> listener);
	
	public abstract <T> void playVideo(int videoId,IResponse<T> listener);
	
	public abstract <T> void informVideo(int orderId,IResponse<T> listener);
	
	public abstract <T> void getTags(IResponse<T> listener);
	
	public abstract <T> void confirmOder(int orderId, int starCount, String comment, IResponse<T> listener);

	public abstract <T> void refuseOrder(int orderId, int reasonType, IResponse<T> listener);
	
	public abstract <T> void commentOrder(int orderId, int starCount, String comment, IResponse<T> listener);
	
	public abstract <T> void deleteVideo(int videoid, IResponse<T> listener);
	
	public abstract <T> void feedback(String content, IResponse<T> listener);
	
	public abstract <T> void getAnchorBanlace(IResponse<T> listener);
	
//	public abstract <T> void postAnchorInfo(AnchorBalanceInfo info, IResponse<T> listener);
	
	public abstract <T> void getNotifycationList(int pageIndex, int pageCount,IResponse<T> listener);
	
	public abstract <T> void postClientID(String clientID, IResponse<T> listener);
	
	public abstract <T> void getUpdateInfo(IResponse<T> listener);
	
	
	public abstract <T> void sendAnchorInvite(int userId, IResponse<T> listener);
	
	/**
	 * 
	 * @param type 0: my video list ,1:star-achor received bless video list
	 * @param pageIndex
	 * @param pageCount
	 * @param listener
	 */
/*	public abstract <T> void getBlessVideoList(int userId,int pageIndex, int pageCount, IResponse<T> listener);
	
	public abstract <T> void postBless(int userId,int videoId,IResponse<T> listener);
	*/
}
