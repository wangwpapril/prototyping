package com.swishlabs.prototyping.net;


import org.json.JSONException;

/**
 * Define a common callback interface, which is used in executing a web-requeset operation. 
 * @author Administrator
 *
 * @param <T>
 */
public interface IResponse<T> {
    
	/**
	 * Operation success.
	 * @param data
	 */
	void onSucceed(T result);

	void onFailed(String code, String errMsg);
	
	T asObject(String rspStr) throws JSONException;

}

