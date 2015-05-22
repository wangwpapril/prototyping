package com.swishlabs.prototyping.data.api.model;

import java.io.Serializable;

import org.json.JSONException;
import org.json.JSONObject;

public class User implements Serializable {

	private static final long serialVersionUID = 1L;
	public String id;
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

	public Company getCompany() {
		return company;
	}
	
	public User(JSONObject obj) throws JSONException {

        if(obj == null){
            company = new Company(null);
            return;
        }
		id = obj.optString("id");
		userName = obj.optString("username");
		email = obj.optString("email");
		firstName = obj.optString("first_name");
		lastName = obj.optString("last_name");
		phone = obj.optString("phone");
		countryCode = obj.optString("country_code");
		currencyCode = obj.optString("currency_code");
		ipAddress = obj.optString("ip_address");
		lastIpAddress = obj.optString("last_ip_address");
		languageCode = obj.optString("language_code");
		role = obj.optString("role");
		activated = obj.optString("activated");
		activationCode = obj.optString("activation_code");
		lastLoginAt = obj.optString("last_login_at");
		activatedAt = obj.optString("activated_at");
		createdAt = obj.optString("created_at");
		updatedAt = obj.optString("updated_at");
		token = obj.optString("token");
		forgotPasswordToken = obj.optString("forgot_password_token");
		
		if (obj.has("company")) {

            Object tmp = obj.get("company");
            if(tmp instanceof JSONObject) {
                JSONObject cp = obj.getJSONObject("company");
                company = new Company(cp);
            }else {
                company = new Company(null);
            }
		}

		sessionId = obj.optString("SessionId");
		userName = obj.optString("UserName");
	}

}
