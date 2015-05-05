package com.swishlabs.prototyping.data.api.model;

import java.io.Serializable;

import org.json.JSONException;
import org.json.JSONObject;

public class User implements Serializable {

	private static final long serialVersionUID = 1L;
	public String id;
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

        if(obj == null){
            company = new Company(null);
            return;
        }
		id = obj.getString("id");
		userName = obj.getString("username");
		email = obj.getString("email");
		firstName = obj.getString("first_name");
		lastName = obj.getString("last_name");
		phone = obj.getString("phone");
		countryCode = obj.getString("country_code");
		currencyCode = obj.getString("currency_code");
		ipAddress = obj.getString("ip_address");
		lastIpAddress = obj.getString("last_ip_address");
		languageCode = obj.getString("language_code");
		role = obj.getString("role");
		activated = obj.getString("activated");
		activationCode = obj.getString("activation_code");
		lastLoginAt = obj.getString("last_login_at");
		activatedAt = obj.getString("activated_at");
		createdAt = obj.getString("created_at");
		updatedAt = obj.getString("updated_at");
		token = obj.getString("token");
		forgotPasswordToken = obj.getString("forgot_password_token");
		
		if (obj.has("company")) {

            Object tmp = obj.get("company");
            if(tmp instanceof JSONObject) {
                JSONObject cp = obj.getJSONObject("company");
                company = new Company(cp);
            }else {
                company = new Company(null);
            }
		}
		
	}

}
