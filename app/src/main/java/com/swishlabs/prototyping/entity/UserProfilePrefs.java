/*
 * Copyright 2015 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.swishlabs.prototyping.entity;

import android.content.Context;

import com.swishlabs.prototyping.MyApplication;
import com.swishlabs.prototyping.util.Enums;
import com.swishlabs.prototyping.util.SharedPreferenceUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Storing user state
 */
public class UserProfilePrefs {

//    private static final String GRABOP_PREF = "GRABOP_PREF";
//    private static final String KEY_ACCESS_TOKEN = "KEY_ACCESS_TOKEN";
//    private static final String KEY_USER_ID = "KEY_USER_ID";
//    private static final String KEY_USER_NAME = "KEY_USER_NAME";
//    private static final String KEY_USER_AVATAR = "KEY_USER_AVATAR";

    private static volatile UserProfilePrefs singleton;
//    private final SharedPreferences prefs;

    private String sessionId;
    private boolean isLoggedIn = false;
    private int userId;
    private String username;
    private String userAvatar;
    private List<LoginStatusListener> loginStatusListeners;

    public static UserProfilePrefs get(Context context) {
        if (singleton == null) {
            synchronized (UserProfilePrefs.class) {
                singleton = new UserProfilePrefs(context);
            }
        }
        return singleton;
    }

    private UserProfilePrefs(Context context) {
        isLoggedIn = MyApplication.getLoginStatus();
        if (isLoggedIn) {
            sessionId = SharedPreferenceUtil.getString(Enums.PreferenceKeys.sessionId.toString(), null);
            username = SharedPreferenceUtil.getString(Enums.PreferenceKeys.username.toString(), null);
            userAvatar = SharedPreferenceUtil.getString(Enums.PreferenceKeys.userAvatar.toString(), null);
        }
    }

    public boolean isLoggedIn() {
        return isLoggedIn;
    }

//    public @Nullable
//    String getAccessToken() {
//        return accessToken;
//    }

//    public void setAccessToken(String accessToken) {
//        if (!TextUtils.isEmpty(accessToken)) {
//            this.accessToken = accessToken;
//            isLoggedIn = true;
//            prefs.edit().putString(KEY_ACCESS_TOKEN, accessToken).apply();
//            dispatchLoginEvent();
//        }
//    }

    public void setLoggedInUser(Profile user) {
        if (user != null) {
            sessionId = String.valueOf(user.getSessionId());
            username = user.getUserName();
            userAvatar = user.getAvatarUrl();

            SharedPreferenceUtil.setString(Enums.PreferenceKeys.sessionId.toString(), sessionId);
            SharedPreferenceUtil.setString(Enums.PreferenceKeys.username.toString(), username);
            SharedPreferenceUtil.setString(Enums.PreferenceKeys.userAvatar.toString(), userAvatar);
        }
    }

    public int getUserId() {
        return userId;
    }

    public String getUserName() {
        return username;
    }

    public String getUserAvatar() {
        return userAvatar;
    }

    public Profile getUserProfile() {
        return new Profile.Builder()
                .setId(Integer.valueOf(sessionId))
                .setUserName(username)
                .setUserAvatar(userAvatar)
                .build();
    }

    public void logout() {
        isLoggedIn = false;
        sessionId = null;
        username = null;
        userAvatar = null;
        SharedPreferenceUtil.setString(Enums.PreferenceKeys.sessionId.toString(), null);
        SharedPreferenceUtil.setString(Enums.PreferenceKeys.username.toString(), null);
        SharedPreferenceUtil.setString(Enums.PreferenceKeys.userAvatar.toString(), null);
    }

    public void addLoginStatusListener(LoginStatusListener listener) {
        if (loginStatusListeners == null) {
            loginStatusListeners = new ArrayList<>();
        }
        loginStatusListeners.add(listener);
    }

    public void removeLoginStatusListener(LoginStatusListener listener) {
        if (loginStatusListeners != null) {
            loginStatusListeners.remove(listener);
        }
    }

    private void dispatchLoginEvent() {
        if (loginStatusListeners != null && loginStatusListeners.size() > 0) {
            for (LoginStatusListener listener : loginStatusListeners) {
                listener.onLogin();
            }
        }
    }

    private void dispatchLogoutEvent() {
        if (loginStatusListeners != null && loginStatusListeners.size() > 0) {
            for (LoginStatusListener listener : loginStatusListeners) {
                listener.onLogout();
            }
        }
    }

    public interface LoginStatusListener {
        void onLogin();
        void onLogout();
    }

}
