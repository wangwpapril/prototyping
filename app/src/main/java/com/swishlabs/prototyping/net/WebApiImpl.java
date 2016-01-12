package com.swishlabs.prototyping.net;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;

import com.swishlabs.prototyping.R;
import com.swishlabs.prototyping.http.HttpCallBack;
import com.swishlabs.prototyping.http.HttpEngine;
import com.swishlabs.prototyping.http.HttpRequest;
import com.swishlabs.prototyping.pal.IHttp;

import org.apache.http.protocol.HTTP;

import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;


/**
 * 
 * @author HouLin 2014.1.6
 * 
 */
class WebApiImpl extends WebApi {

	final static String TAG = "WebApi";

	private HttpEngine mHttpEngine;

	private Context mContext;

	private String mToken;

	private Handler mUIHandler = new Handler();

	private boolean isTestModel = false;

	WebApiImpl(Context context) {
		mContext = context;
		mHttpEngine = new HttpEngine(context);
		initToken(context);
	}

	protected void initToken(Context context) {
/*		mToken = PreferenceUtils.readStrConfig(Constant.PreferKeys.KEY_TOKEN,
				context); */
	}

	@Override
	public void setToken(String token) {
		mToken = token;

	}

	@Override
	public void enableTestModel() {
		isTestModel = true;
	}

	class AdapterCallBack<T> implements HttpCallBack {
		private IResponse<T> rsp;

		public AdapterCallBack(HttpRequest request, IResponse<T> rsp) {
			this.rsp = rsp;
		}

		@Override
		public void onError(final int requestId, final int errCode,
				final byte[] errStr, IHttp http) {
			
			String errStrMsg = "";
			try {
				errStrMsg = errStr == null ? ""
						: new String(errStr, HTTP.UTF_8);
			} catch (UnsupportedEncodingException e1) {
				e1.printStackTrace();
			}
			
			if (rsp == null) {
				return;
			}

			if (isTestModel) {
				rsp.onFailed(String.valueOf(errCode), errStrMsg);
			} else {
				if (errCode > HttpEngine.NET_CODE
						|| TextUtils.isEmpty(errStrMsg) || errCode == 500) {
					errStrMsg = mContext.getString(R.string.network_error);
				}
				final String str = errStrMsg;
				Log.e("PAL_HTTP", "onError : errCode = " + errCode + "  errMsg = " + errStrMsg);
				mUIHandler.post(new Runnable() {
					@Override
					public void run() {
						rsp.onFailed(String.valueOf(errCode), str);
					}
				});
			}
			
			errorOperation(requestId, errCode, errStrMsg);
		}

		@Override
		public void onReceived(int requestId, byte[] data, IHttp http) {
			String rawStr = "";
			try {
				rawStr = data == null ? "" : new String(data, HTTP.UTF_8);
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			
			Log.e("bei", rawStr);
			if (rsp == null) {
				return;
			}
			T object = null;
			try {

				if (rawStr != null && rawStr.startsWith("\ufeff")) {
					rawStr = rawStr.substring(1);
				}

/*				JSONObject json = new JSONObject(rawStr);
				Log.w("PAL_HTTP", "body : " + json.toString());
				int code = json.getInt("result");
				String msg = json.getString("msg");
				if (code != 0) {
					onError(requestId, code, msg.getBytes(HTTP.UTF_8), http);
					return;
				}

//				String content = json.optString("content");
				object = rsp.asObject(json.toString());*/
				object = rsp.asObject(rawStr);
			} catch (final Exception e) {
				e.printStackTrace();
				try {
//					onError(requestId, 0, e.getMessage().getBytes(HTTP.UTF_8), http);
					onError(requestId, 0, mContext.getString(R.string.invalid_data_format).getBytes(HTTP.UTF_8), http);

				} catch (UnsupportedEncodingException e1) {
					e1.printStackTrace();
				}
				return;
			}
			if (isTestModel) {
				rsp.onSucceed(object);
			} else {
				final T target = object;
				mUIHandler.post(new Runnable() {

					@Override
					public void run() {
						rsp.onSucceed(target);
					}
				});
			}
		}

		@Override
		public void onReceived(int requestId, InputStream stream,
				long contentLength, IHttp http) {

		}

	}

	protected <T> void get(String url, Map<String, String> header,
			IResponse<T> listener) {
		get(url, header, null, listener);
	}

	protected <T> void get(String url, Map<String, String> header,
			Map<String, String> params, IResponse<T> listener) {
		HttpRequest request = new HttpRequest(url);
		request.setRequestMethod(HttpRequest.METHOD_GET);
		header.put("Accept-Encoding", "gzip");
		request.setMultiPartParams(params);
		request.setHeaderField(header);
		request.setHttpCallBack(new AdapterCallBack<T>(request, listener));
		if (isTestModel) {
			mHttpEngine.doRequest(request);
		} else {
			mHttpEngine.addRequest(request);
		}
	}

	protected <T> void put(String url, Map<String, String> header, String body,
			IResponse<T> listener) {
		HttpRequest request = new HttpRequest(url);
		if (!TextUtils.isEmpty(body)) {
			request.postData(body);
		}
		request.setRequestMethod(HttpRequest.METHOD_PUT);
		request.setHeaderField(header);
		request.setHttpCallBack(new AdapterCallBack<T>(request, listener));
		if (isTestModel) {
			mHttpEngine.doRequest(request);
		} else {
			mHttpEngine.addRequest(request);
		}
	}

	private <T> void post(String url, Map<String, String> header, String body,
			IResponse<T> listener) {
		HttpRequest request = new HttpRequest(url);
		if (!TextUtils.isEmpty(body)) {
			request.postData(body);
		}
		request.setRequestMethod(HttpRequest.METHOD_POST);
		request.setHeaderField(header);
		request.setHttpCallBack(new AdapterCallBack<T>(request, listener));
		if (isTestModel) {
			mHttpEngine.doRequest(request);
		} else {
			mHttpEngine.addRequest(request);
		}
	}

	protected <T> void post(String url, Map<String, String> header,
			byte[] rawData, IResponse<T> listener) {
		HttpRequest request = new HttpRequest(url);
		request.postRawData(rawData);
		request.setHeaderField(header);
		request.setHttpCallBack(new AdapterCallBack<T>(request, listener));
		if (isTestModel) {
			mHttpEngine.doRequest(request);
		} else {
			mHttpEngine.addRequest(request);
		}
	}

	private <T> void post(String url, Map<String, String> header, String body,
			Map<String, Object> files, IResponse<T> listener,
			IHttp.OnPostProgressListener progressListener) {
		HttpRequest request = new HttpRequest(url);
		request.postMultiPartFile(files);
		request.setPostProgressListener(progressListener);
		request.setHeaderField(header);
		request.postData(body);
		request.setHttpCallBack(new AdapterCallBack<T>(request, listener));
		
		if (isTestModel) {
			mHttpEngine.doRequest(request);
		} else {
			mHttpEngine.addRequest(request);
		}
	}

	private <T> void post(String url, Map<String, String> header,
			Map<String, String> params, IResponse<T> listener) {
		HttpRequest request = new HttpRequest(url);
		request.setMultiPartParams(params);
		request.setRequestMethod(HttpRequest.METHOD_POST);
		request.setHeaderField(header);
		request.setHttpCallBack(new AdapterCallBack<T>(request, listener));
		
		if (isTestModel) {
			mHttpEngine.doRequest(request);
		} else {
			mHttpEngine.addRequest(request);
		}
	}

	// private <T> void delete(String url, Map<String, String> header,
	// IResponse<T> listener) {
	// HttpRequest request = new HttpRequest(url);
	// request.setRequestMethod(HttpRequest.METHOD_DELET);
	// request.setHeaderField(header);
	// request.setHttpCallBack(new AdapterCallBack<T>(request, listener));
	// if (isTestModel) {
	// mHttpEngine.doRequest(request);
	// } else {
	// mHttpEngine.addRequest(request);
	// }
	// }

	private Map<String, String> buildHeader() {
		Map<String, String> header = new HashMap<String, String>();
		header.put(Contract.KEY_HEAD_PLATFORM, "android");
		header.put(Contract.KEY_HEAD_APP_KEY, Contract.APP_KEY);
		header.put(Contract.KEY_HEAD_VENDOR, Contract.VENDOR_KEY);
		if (!TextUtils.isEmpty(mToken)) {
			header.put(Contract.KEY_HEAD_TOKEN, mToken);
		}
		return header;
	}

	@Override
	public <T> void register(String firstName, String lastName, String userName, String email, String password,
			 IResponse<T> listener) {
		Map<String, String> params = new HashMap<String, String>();
		params.put(Contract.AccountKeys.PASSWORD, password);
		params.put(Contract.AccountKeys.USERNAME, userName);
		params.put(Contract.AccountKeys.FIRSTNAME, firstName);
		params.put(Contract.AccountKeys.LASTNAME, lastName);
		params.put(Contract.UserInfoKeys.EMAIL, email);
		post(Contract.FuncUrls.REGISTER + Contract.FORMAT_JSON, buildHeader(), params, listener);
	}

/*	@Override
	public <T> void forgetPsw(String phoneNum, String password, String code,
			IResponse<T> listener) {
		Map<String, String> params = new HashMap<String, String>();
		params.put(AccountKeys.PASSWORD, password);
		params.put(AccountKeys.PHONE, phoneNum);
		params.put(CommonKeys.TYPE, "1");
		post(FuncUrls.REGISTER, buildHeader(), params, listener);
	}

	@Override
	public <T> void changePsw(String oldpwd, String newpwd,
			IResponse<T> listener) {
		Map<String, String> params = new HashMap<String, String>();
		params.put(ChangePwdKey.NEWPWD, newpwd);
		params.put(ChangePwdKey.OLDPWD, oldpwd);
		post(FuncUrls.CHANGE_PASSSWORD, buildHeader(), params, listener);
	}*/

	@Override
	public <T> void login(String userName, String passWord,
			IResponse<T> listener) {
		Map<String, String> params = new HashMap<String, String>();
		params.put(Contract.AccountKeys.PASSWORD, passWord);
		params.put(Contract.AccountKeys.USERNAME, userName);
		post(Contract.FuncUrls.LOGIN + Contract.FORMAT_JSON , buildHeader(), params, listener);

//		post("http://grabopws.azurewebsites.net/auth?format=json", buildHeader(), params, listener);
	}

	@Override
	public <T> void getProfile(String id, IResponse<T> listener) {
		get(Contract.FuncUrls.PROFILE_URL + id, buildHeader(), null, listener);
	}

	@Override
	public <T> void getProfiles(String id, int offset, IResponse<T> listener) {
		get(Contract.FuncUrls.PROFILE_URL + id + "/profileswithinratio?distance=5.0&longitude=0.0&latitude=0.0&offset=" + offset + "&format=json",
				buildHeader(), null, listener);
	}

	@Override
	public <T> void getConnections(String id, IResponse<T> listener) {
		get(Contract.FuncUrls.PROFILE_URL + id +"/connections", buildHeader(), null, listener);
	}

	@Override
	public <T> void getService(String id, IResponse<T> listener) {
		get(Contract.FuncUrls.PROFILE_URL + id +"/services?offset=0", buildHeader(), null, listener);
	}


	/*	@Override
        public <T> void sendCode(String phoneNum, IResponse<T> listener) {
            Map<String, String> params = new HashMap<String, String>();
            params.put(AccountKeys.PHONE, phoneNum);
            post(FuncUrls.SEND_CODE, buildHeader(), params, listener);
        }

        @Override
        public void setToken(String token) {
            mToken = token;

        }

        @Override
        public boolean isLogined() {
            return !TextUtils.isEmpty(mToken);
        }

        @Override
        public <T> void getUserInfo(int userId, IResponse<T> listener) {
            Map<String, String> params = new HashMap<String, String>();
            if (userId > 0) {
                params.put(CommonKeys.USER_ID, String.valueOf(userId));
            }
            get(FuncUrls.USER_INFO, buildHeader(), params, listener);

        }

        @Override
        public <T> void getVideoList(int type, int pageIndex, int pageCount,
                IResponse<T> listener) {
            Map<String, String> params = new HashMap<String, String>();
            params.put(CommonKeys.TYPE, String.valueOf(type));
            params.put(CommonKeys.PAGE_COUNT, String.valueOf(pageCount));
            params.put(CommonKeys.PAGE_INDEX, String.valueOf(pageIndex));
            get(FuncUrls.VIDEOS, buildHeader(), params, listener);
        }

        @Override
        public <T> void getFocusImageList(int pageIndex, int pageCount,
                IResponse<T> listener) {
            Map<String, String> params = new HashMap<String, String>();
            params.put(CommonKeys.PAGE_COUNT, String.valueOf(pageCount));
            params.put(CommonKeys.PAGE_INDEX, String.valueOf(pageIndex));
            get(FuncUrls.FOCUS_IMAGES, buildHeader(), params, listener);

        }

        @Override
        public <T> void getAttetionUsers(int type, int pageIndex, int pageCount,
                IResponse<T> listener) {
            Map<String, String> params = new HashMap<String, String>();
            params.put(CommonKeys.TYPE, String.valueOf(type));
            params.put(CommonKeys.PAGE_COUNT, String.valueOf(pageCount));
            params.put(CommonKeys.PAGE_INDEX, String.valueOf(pageIndex));
            get(FuncUrls.ATTENTION_USERS, buildHeader(), params, listener);
        }

        @Override
        public <T> void getFavoriteVideos(int pageIndex, int pageCount,
                IResponse<T> listener) {
            Map<String, String> params = new HashMap<String, String>();
            params.put(CommonKeys.PAGE_COUNT, String.valueOf(pageCount));
            params.put(CommonKeys.PAGE_INDEX, String.valueOf(pageIndex));
            get(FuncUrls.VIDEOS_FAVORITE, buildHeader(), params, listener);
        }

        @Override
        public <T> void getCustomVideos(int pageIndex, int pageCount,
                IResponse<T> listener) {
            Map<String, String> params = new HashMap<String, String>();
            params.put(CommonKeys.PAGE_COUNT, String.valueOf(pageCount));
            params.put(CommonKeys.PAGE_INDEX, String.valueOf(pageIndex));
            get(FuncUrls.VIDEOS_CUSTOM, buildHeader(), params, listener);
        }

        @Override
        public <T> void postCustomOrder(CustomOrderPost order,
                Map<String, Object> file, IResponse<T> listener) {
            post(FuncUrls.VIDEOS_CUSTOM_MAKE, buildHeader(),
                    GsonUtil.objectToJson(order), file, listener, null);
        }

        @Override
        public <T> void postUserInfo(UserInfo info, IResponse<T> listener) {
            post(FuncUrls.USER_INFO_MODIFY, buildHeader(),
                    GsonUtil.objectToJson(info), listener);
        }

        @Override
        public <T> void getCustomOrders(int type, int pageIndex, int pageCount,
                IResponse<T> listener) {
            Map<String, String> params = new HashMap<String, String>();
            params.put(CommonKeys.PAGE_COUNT, String.valueOf(pageCount));
            params.put(CommonKeys.PAGE_INDEX, String.valueOf(pageIndex));
            params.put(CommonKeys.TYPE, String.valueOf(type));
            get(FuncUrls.CUSTOM_ORDERS_ACCEPT, buildHeader(), params, listener);
        }

        @Override
        public <T> void postGetOrder(int orderId, boolean isAccept,
                IResponse<T> listener) {
            Map<String, String> params = new HashMap<String, String>();
            params.put(CommonKeys.ORDER_ID, String.valueOf(orderId));
            params.put(CommonKeys.ACCEPT_STATE, String.valueOf(isAccept ? 1 : 0));
            post(FuncUrls.GET_ORDER, buildHeader(), params, listener);
        }

        @Override
        public <T> void getCityList(IResponse<T> listener) {
            get(FuncUrls.GET_CITY_LIST, buildHeader(), null, listener);
        }

        @Override
        public <T> void login(String openId, int type, IResponse<T> listener) {
            Map<String, String> params = new HashMap<String, String>();
            params.put(CommonKeys.TYPE, String.valueOf(type));
            params.put(AccountKeys.OPENDID, openId);
            post(FuncUrls.OAUTH, buildHeader(), params, listener);
        }

        @Override
        public <T> void search(int type, String content, int pageIndex,
                int pageCount, IResponse<T> listener) {
            Map<String, String> params = new HashMap<String, String>();
            params.put(CommonKeys.TYPE, String.valueOf(type));
            params.put(CommonKeys.CONTENT, content);
            params.put(CommonKeys.PAGE_INDEX, String.valueOf(pageIndex));
            params.put(CommonKeys.PAGE_COUNT, String.valueOf(pageCount));
            post(FuncUrls.SEARCH, buildHeader(), params, listener);
        }

        @Override
        public <T> void postAtUser(int userId, IResponse<T> listener) {
            Map<String, String> params = new HashMap<String, String>();
            params.put(CommonKeys.USER_ID, String.valueOf(userId));
            post(FuncUrls.ATTENTION, buildHeader(), params, listener);
        }

        @Override
        public <T> void postCancelAtUser(int userId, IResponse<T> listener) {
            Map<String, String> params = new HashMap<String, String>();
            params.put(CommonKeys.USER_ID, String.valueOf(userId));
            post(FuncUrls.ATTENTION_CANCEL, buildHeader(), params, listener);
        }

        @Override
        public <T> void getUserVideos(int userId, int pageIndex, int pageCount,
                IResponse<T> listener) {
            Map<String, String> params = new HashMap<String, String>();
            if (userId > 0) {
                params.put(CommonKeys.USER_ID, String.valueOf(userId));
                params.put(CommonKeys.PAGE_INDEX, String.valueOf(pageIndex));
                params.put(CommonKeys.PAGE_COUNT, String.valueOf(pageCount));
            }
            get(FuncUrls.USER_VIDEOS, buildHeader(), params, listener);
        }

        @Override
        public <T> void postApplyAnchor(AnchorApplyInfo info, IResponse<T> listener) {
            post(FuncUrls.ANCHOR_APPLY, buildHeader(), GsonUtil.objectToJson(info),
                    listener);
        }

        @Override
        public <T> void postOrderAssign(int userId, int orderId,
                IResponse<T> listener) {
            Map<String, String> params = new HashMap<String, String>();
            params.put(CommonKeys.USER_ID, String.valueOf(userId));
            params.put(CommonKeys.ORDER_ID, String.valueOf(orderId));
            post(FuncUrls.ORDER_ASSIGN, buildHeader(), params, listener);
        }

        @Override
        public <T> void postVideoInfo(VideoInfoPost info, Map<String, Object> file,
                IResponse<T> listener, OnPostProgressListener progressListener) {
            post(FuncUrls.VIDEO_RECORD, buildHeader(), GsonUtil.objectToJson(info),
                    file, listener, progressListener);
        }

        @Override
        public <T> void favoriteVideo(int videoId, int type, IResponse<T> listener) {
            Map<String, String> params = new HashMap<String, String>();
            params.put(CommonKeys.VIDEO_ID, String.valueOf(videoId));
            params.put(CommonKeys.TYPE, String.valueOf(type));
            post(FuncUrls.FAVORITE_VIDEO, buildHeader(), params, listener);
        }

        @Override
        public <T> void praiseVideo(int videoId, IResponse<T> listener) {
            Map<String, String> params = new HashMap<String, String>();
            params.put(CommonKeys.VIDEO_ID, String.valueOf(videoId));
            post(FuncUrls.PRAISE_VIDEO, buildHeader(), params, listener);

        }

        @Override
        public <T> void shareVideo(int videoId, IResponse<T> listener) {
            Map<String, String> params = new HashMap<String, String>();
            params.put(CommonKeys.VIDEO_ID, String.valueOf(videoId));
            post(FuncUrls.SHARE_VIDEO, buildHeader(), params, listener);
        }

        @Override
        public <T> void playVideo(int videoId, IResponse<T> listener) {
            Map<String, String> params = new HashMap<String, String>();
            params.put(CommonKeys.VIDEO_ID, String.valueOf(videoId));
            post(FuncUrls.PLAY_VIDEO, buildHeader(), params, listener);
        }

        @Override
        public <T> void informVideo(int orderId, IResponse<T> listener) {
            Map<String, String> params = new HashMap<String, String>();
            params.put(CommonKeys.VIDEO_ID, String.valueOf(orderId));
            post(FuncUrls.INFORM_VIDEO, buildHeader(), params, listener);

        }

        @Override
        public <T> void getTags(IResponse<T> listener) {
            get(FuncUrls.TAG_LIST, buildHeader(), listener);
        }

        @Override
        public <T> void confirmOder(int orderId, int starCount, String comment,
                IResponse<T> listener) {
            Map<String, String> params = new HashMap<String, String>();
            params.put(CommonKeys.ORDER_ID, String.valueOf(orderId));
            params.put(CommonKeys.USERT_EVALULATE, String.valueOf(starCount));
            params.put(CommonKeys.USER_COMMENT, comment);
            post(FuncUrls.ORDER_CONFIRM, buildHeader(), params, listener);
        }

        @Override
        public <T> void refuseOrder(int orderId, int reasonType,
                IResponse<T> listener) {
            Map<String, String> params = new HashMap<String, String>();
            params.put(CommonKeys.ORDER_ID, String.valueOf(orderId));
            params.put(CommonKeys.REASON_TYPE, String.valueOf(reasonType));
            post(FuncUrls.ORDER_REFUSE, buildHeader(), params, listener);

        }

        @Override
        public <T> void commentOrder(int orderId, int starCount, String comment,
                IResponse<T> listener) {
            Map<String, String> params = new HashMap<String, String>();
            params.put(CommonKeys.ORDER_ID, String.valueOf(orderId));
            params.put(CommonKeys.USERT_EVALULATE, String.valueOf(starCount));
            params.put(CommonKeys.USER_COMMENT, comment);
            post(FuncUrls.ORDER_COMMENT, buildHeader(), params, listener);

        }

        @Override
        public <T> void feedback(String content, IResponse<T> listener) {
            Map<String, String> params = new HashMap<String, String>();
            params.put(CommonKeys.CONTENT, content);
            post(FuncUrls.FEEDBACK, buildHeader(), params, listener);

        }

        @Override
        public <T> void getFriendsVideos(int pageIndex, int pageCount,
                IResponse<T> listener) {
            Map<String, String> params = new HashMap<String, String>();
            params.put(CommonKeys.PAGE_INDEX, String.valueOf(pageIndex));
            params.put(CommonKeys.PAGE_COUNT, String.valueOf(pageCount));
            get(FuncUrls.ATTENTION_VIDEOS, buildHeader(), params, listener);
        }

        @Override
        public <T> void deleteVideo(int videoid, IResponse<T> listener) {
            Map<String, String> params = new HashMap<String, String>();
            params.put(CommonKeys.VIDEO_ID, String.valueOf(videoid));
            post(FuncUrls.DELETE_VIDEO, buildHeader(), params, listener);
        }

        @Override
        public <T> void getAnchorBanlace(IResponse<T> listener) {
            get(FuncUrls.ANCHOR_BANLANCE, buildHeader(), listener);
        }

        @Override
        public <T> void getNotifycationList(int pageIndex, int pageCount,
                IResponse<T> listener) {
            Map<String, String> params = new HashMap<String, String>();
            params.put(CommonKeys.PAGE_INDEX, String.valueOf(pageIndex));
            params.put(CommonKeys.PAGE_COUNT, String.valueOf(pageCount));
            get(FuncUrls.GET_NOTIFYCATION, buildHeader(), params, listener);
        }

        @Override
        public <T> void postClientID(String clientID, IResponse<T> listener) {
            Map<String, String> params = new HashMap<String, String>();
            params.put(CommonKeys.CLIENT_ID, clientID);
            post(FuncUrls.POST_CLIENT_ID, buildHeader(), params, listener);
        }

        @Override
        public <T> void getUpdateInfo(IResponse<T> listener) {
            Map<String, String> params = new HashMap<String, String>();
            params.put("os", "android");
            params.put("osversion", PhoneStateUtils.getOsVersion());
            params.put("version", String.valueOf(CommonUtils.getVersionCode(mContext)));
            get(FuncUrls.GET_UPDATE, buildHeader(), params, listener);
        }

        @Override
        public <T> void sendAnchorInvite(int userId, IResponse<T> listener) {
            Map<String, String> params = new HashMap<String, String>();
            params.put(CommonKeys.USER_ID, String.valueOf(userId));
            get(FuncUrls.ANCHOR_APPLY_INVITE, buildHeader(), params, listener);
        }

        @Override
        public <T> void postAnchorInfo(AnchorBalanceInfo info, IResponse<T> listener) {
            post(FuncUrls.ANCHOR_APPLY_MODIFY, buildHeader(),
                    GsonUtil.objectToJson(info), listener);
        }

        @Override
        public <T> void getBlessVideoList(int userId, int pageIndex, int pageCount,
                IResponse<T> listener) {
            Map<String, String> params = new HashMap<String, String>();
            params.put(CommonKeys.USER_ID, String.valueOf(userId));
            params.put(CommonKeys.PAGE_COUNT, String.valueOf(pageCount));
            params.put(CommonKeys.PAGE_INDEX, String.valueOf(pageIndex));
            get(FuncUrls.STAR_ANCHOR_BLESS_LIST, buildHeader(), params, listener);
        }

        @Override
        public <T> void postBless(int userId, int videoId, IResponse<T> listener) {
            Map<String, String> params = new HashMap<String, String>();
            params.put(CommonKeys.USER_ID, String.valueOf(userId));
            params.put(CommonKeys.VIDEO_ID, String.valueOf(videoId));
            post(FuncUrls.STAR_ANCHOR_BLESS, buildHeader(), params, listener);
        }
    */
	private void errorOperation(int reqId, int errCode, String errMsg){
		if(errCode == Constant.ResponseCode.RES_ERROR_RELOGIN){
			Intent intent = new Intent(Constant.BroadConstant.BROAD_RE_LOGIN);
			mContext.sendBroadcast(intent);
		}
	}
	
}
