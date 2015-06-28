package com.swishlabs.prototyping.http;


import com.swishlabs.prototyping.pal.IHttp;

import java.util.Hashtable;
import java.util.Map;

public class HttpRequest {

	public static final String METHOD_GET = "GET";
	public static final String METHOD_POST = "POST";
	public static final String METHOD_DELET = "DELETE";
	public static final String METHOD_PUT = "PUT";

	private int mRequestID;

	private boolean mIsCancel = false;

	private String mUrl;

	private String mMethod;

	private Map<String, String> mHeader;

	private String mPostData;

	private Map<String, Object> mMultiPartFile;

	private HttpCallBack mCallback;

	private boolean mStreamCallBack;
	
	private byte[] rawData = null; 

	private Map<String, String> mMultiPartParams;

	private IHttp.OnPostProgressListener mPostProgressListener;
	
	public HttpRequest(String url) {
		this(url, METHOD_GET);
	}
		
	public HttpRequest(String url, String method) {
		this.mUrl = url;
		mMethod = method;
		mRequestID = HttpEngine.getNextRequestID();
		mIsCancel = false;
	}

	public int getRequestID() {
		return mRequestID;
	}

	public void addHeaderField(String key, String value) {
		if (mHeader == null) {
			mHeader = new Hashtable<String, String>();
		}
		mHeader.put(key, value);
	}

	public void setHeaderField(Map<String, String> header) {
		mHeader = header;
	}

	public Map<String, String> getHeaderField() {
		return mHeader;
	}

	public String getMethod() {
		return mMethod;
	}

	public String getUrl() {
		return mUrl;
	}

	public void postData(String data) {
		mPostData = data;
		mMethod = METHOD_POST;	
	}
	
	/**
	 * This data can be compress as Gzip formater.
	 * @param data
	 */
	public void postRawData(byte[] data) {
		rawData = data;
		mMethod = METHOD_POST;
	}
	
	public byte[] getPostRawtData(){
		return rawData;
	}

	
	public String getPostData(){
		return mPostData;
	}

	public void postMultiPartFile(Map<String, Object> files) {
		mMultiPartFile = files;
		if (files != null) {
			mMethod = METHOD_POST;
		}
	}


	public void setHttpCallBack(HttpCallBack callback) {
		mCallback = callback;
	}

	public HttpCallBack getHttpCallBack() {
		return mCallback;
	}

	public Map<String, Object> getMultiPartFile() {
		return mMultiPartFile;
	}

	public void setRequestMethod(String method) {
		mMethod = method;
	}

	public void doCancel() {
		mIsCancel = true;
	}

	public boolean isCancel() {
		return mIsCancel;
	}

	public void setStreamCallBack(boolean streamCallBack) {
		mStreamCallBack = streamCallBack;
	}

	public boolean isStreamCallBack() {
		return mStreamCallBack;
	}
	
	public void setMultiPartParams(Map<String,String> params) {
		mMultiPartParams = params;
	}

	public Map<String, String> getMultiPartParams() {
		return mMultiPartParams;
	}

	public IHttp.OnPostProgressListener getPostProgressListener() {
		return mPostProgressListener;
	}

	public void setPostProgressListener(IHttp.OnPostProgressListener mPostProgressListener) {
		this.mPostProgressListener = mPostProgressListener;
	}

	
}
