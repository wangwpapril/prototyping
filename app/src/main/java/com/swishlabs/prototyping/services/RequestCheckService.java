package com.swishlabs.prototyping.services;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;

import com.swishlabs.prototyping.entity.Profile;
import com.swishlabs.prototyping.net.IResponse;
import com.swishlabs.prototyping.net.WebApi;
import com.swishlabs.prototyping.util.Enums;
import com.swishlabs.prototyping.util.GsonUtil;
import com.swishlabs.prototyping.util.SharedPreferenceUtil;

import org.json.JSONException;
import org.json.JSONObject;

public class RequestCheckService extends Service {
    private static final int BACKBROUND_CHECK_REQUEST = 1;
    public static final String ACTION_CHECK_REQUEST = "Check Request";

    private Notification notification;
    protected WebApi mWebApi;
    private NotificationManager mNM;

    public RequestCheckService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        mWebApi = WebApi.getInstance(this);
        mNM = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);

        checkRequest();

/*        if(checkRequest()) {
            Notification.Builder builder = new Notification.Builder(this)
                    .setSmallIcon(android.support.design.R.drawable.notification_template_icon_bg)
                    .setContentTitle("grabop test")
                    .setContentText("content")
                    .setAutoCancel(true)
                    .setOngoing(true);

            notification = builder.build();

            mNM.notify(1, notification);

        }*/

        return START_REDELIVER_INTENT;
    }

    private boolean checkRequest() {
        String sessionId = SharedPreferenceUtil.getString(Enums.PreferenceKeys.sessionId.toString(), null);
        if(sessionId != null)
            getProfile(sessionId);
        return false;
    }

    private void getProfile(String id) {

        mWebApi.getProfile(id, new IResponse<Profile>() {

            @Override
            public void onSucceed(Profile result) {

                Notification.Builder builder = new Notification.Builder(getApplicationContext())
                        .setSmallIcon(android.support.design.R.drawable.notification_template_icon_bg)
                        .setContentTitle("grabop test")
                        .setContentText("content")
                        .setAutoCancel(true)
                        .setOngoing(false);

                notification = builder.build();

                mNM.notify(1, notification);

//                mFinalDb.deleteAll(Profile.class);
                //               List<Profile> profile = mFinalDb.findAll(Profile.class);
            }

            @Override
            public void onFailed(String code, String errMsg) {
//                dismissProgressDlg();
//                ToastUtil.showToast(getBaseContext(), errMsg);
            }

            @Override
            public Profile asObject(String rspStr) {
                try {
                    JSONObject json = new JSONObject(rspStr);

                    Profile profile = GsonUtil.jsonToObject(Profile.class, rspStr);

                    return profile;
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

                return null;
            }
        });
    }

}
