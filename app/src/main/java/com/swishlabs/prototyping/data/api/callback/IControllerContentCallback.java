package com.swishlabs.prototyping.data.api.callback;

import org.json.JSONException;

public interface IControllerContentCallback {
	public void handleSuccess(String content) throws JSONException;
	public void handleError(Exception e);
}
