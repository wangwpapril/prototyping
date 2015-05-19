package com.swishlabs.prototyping.data.api.model;

import java.io.Serializable;

import org.json.JSONException;
import org.json.JSONObject;

public class User implements Serializable {

	private static final long serialVersionUID = 1L;
	public String sessionId;
	public String userName;

	public String email;
	public String firstName;
	public String lastName;
	public String phone;
	public String countryCode;
	public String currencyCode;
	public Company company;
	public String ipAddress;
	public String lastIpAddress;
	public String languageCode;
	public String role;
	public String activated;
	public String activationCode;
	public String lastLoginAt;
	public String activatedAt;
	public String createdAt;
	public String updatedAt;
	public String token;
	public String forgotPasswordToken;
	
	
	public User(JSONObject obj) throws JSONException {

		if (obj == null) {
			company = new Company(null);
			return;
		}
		sessionId = obj.getString("SessionId");
		userName = obj.getString("UserName");

	}
}
