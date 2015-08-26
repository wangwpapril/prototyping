package com.swishlabs.prototyping.net;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TResponse {

	@SerializedName("code")
	@Expose
	private String code;

	@SerializedName("msg")
	@Expose
	private String message;

	@SerializedName("content")
	@Expose
	private String content;

	public String getCode() {
		return code;
	}

	public String getMsg() {
		return message;
	}

	public String getContent() {
		return content;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public void setMessage(String msg) {
		this.message = msg;
	}

	public void setContent(String content) {
		this.content = content;
	}

}
