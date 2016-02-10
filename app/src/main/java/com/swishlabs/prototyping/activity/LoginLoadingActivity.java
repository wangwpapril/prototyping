package com.swishlabs.prototyping.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.swishlabs.prototyping.R;
import com.swishlabs.prototyping.data.DataManager;
import com.swishlabs.prototyping.entity.ConnectionsManager;
import com.swishlabs.prototyping.entity.Profile;
import com.swishlabs.prototyping.entity.ProfilesAroundManager;
import com.swishlabs.prototyping.entity.ReceivedRequestManager;
import com.swishlabs.prototyping.entity.SentRequestManager;
import com.swishlabs.prototyping.entity.UserProfilePrefs;
import com.swishlabs.prototyping.net.IResponse;
import com.swishlabs.prototyping.net.WebApi;
import com.swishlabs.prototyping.util.AnimationLoader;
import com.swishlabs.prototyping.util.Enums;
import com.swishlabs.prototyping.util.GsonUtil;
import com.swishlabs.prototyping.util.SharedPreferenceUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class LoginLoadingActivity extends AppCompatActivity {
    public static final String PROFILE_LIST = "profile_list";
    public static final String PROFILE_OFFSET = "profile_offset";
    public static final String NO_MORE_DATA = "no_more_data";

    private TextView textStatus;
    private ImageView imageLogin;
    private ImageView profileImage;
    private AnimationLoader mAnimLogo;
    private List<Profile> mListProfile;
    private ProfilesAroundManager profilesAroundManager;
    private ConnectionsManager connectionsManager;
    private ReceivedRequestManager receivedRequestManager;
    private SentRequestManager sentRequestManager;

    protected WebApi mWebApi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_loading);

        mWebApi = WebApi.getInstance(this);

        imageLogin = (ImageView)findViewById(R.id.loginImage);
        profileImage = (ImageView)findViewById(R.id.profileImage);

        AnimationLoader.Builder builder = new AnimationLoader.Builder(this);
        builder.setPath("anim/anim_grabop_logo");
        builder.setView(imageLogin);
        builder.setTotalTask(8);
        builder.setDuration(2);

        mAnimLogo = builder.build();
        mAnimLogo.start();

        textStatus = (TextView)findViewById(R.id.textSend);

        doLogin();

        mListProfile = new ArrayList<Profile>();
        profilesAroundManager = new ProfilesAroundManager(this) {
            @Override
            public void onDataLoaded(List data) {
                mListProfile.addAll(data);
                DataManager.getInstance().setProfileAroundList(mListProfile);
                DataManager.getInstance().setProfileAroundOffset(profilesAroundManager.getOffset());
                DataManager.getInstance().setProfileAroundMoreData(profilesAroundManager.getMoreData());
//                ((PreHomeRecyclerAdapter)mRecyclerView.getAdapter()).setData(mListProfile);
//                mPullToRefreshRV.getRefreshableView().getAdapter().notifyDataSetChanged();
//                mPullToRefreshRV.onRefreshComplete();
                mAnimLogo.nextTask(10);
                textStatus.setText("Retrieving your connections.. please wait");
                connectionsManager.initialize();
                connectionsManager.loadData();

            }
        };

        connectionsManager = new ConnectionsManager(this) {
            @Override
            public void onDataLoaded(List data) {
                mAnimLogo.nextTask(5);
                DataManager.getInstance().setConnectionList(data);
                DataManager.getInstance().setConnectionOffset(getOffset());
                DataManager.getInstance().setConnectionMoreData(getMoreData());

                receivedRequestManager.initialize();
                receivedRequestManager.loadData();

            }
        };

        receivedRequestManager = new ReceivedRequestManager(this) {
            @Override
            public void onDataLoaded(List data) {
                mAnimLogo.nextTask(5);
                DataManager.getInstance().setReceivedRequestList(data);
                DataManager.getInstance().setReceivedRequestOffset(getOffset());
                DataManager.getInstance().setReceivedRequestMoreData(getMoreData());

                sentRequestManager.initialize();
                sentRequestManager.loadData();

            }
        };

        sentRequestManager = new SentRequestManager(this) {
            @Override
            public void onDataLoaded(List data) {
                DataManager.getInstance().setSentRequestList(data);
                DataManager.getInstance().setSentRequestOffset(getOffset());
                DataManager.getInstance().setSentRequestMoreData(getMoreData());
                mAnimLogo.lastTask();
                textStatus.setText("Data Retrieving Successfully");

                Intent intent = new Intent(LoginLoadingActivity.this, MainActivity.class);
                intent.setFlags(intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();

            }
        };
    }

    private void doLogin() {

        String userName = SharedPreferenceUtil.getString(Enums.PreferenceKeys.username.toString(), "");
        String psw = SharedPreferenceUtil.getString(Enums.PreferenceKeys.password.toString(), "");

        mWebApi.login(userName, psw, new IResponse<String>() {

            @Override
            public void onSucceed(final String sessionId) {

                SharedPreferenceUtil.setString(Enums.PreferenceKeys.sessionId.toString(), sessionId);
                SharedPreferenceUtil.setBoolean(Enums.PreferenceKeys.loginStatus.toString(), true);

                getProfile(sessionId);
//                getConnections(sessionId);
//                getService(sessionId);
//
                mAnimLogo.nextTask(4);

                textStatus.setText("Login Successful..\nRetrieving your profile from server.. please wait");

                return;
            }

            @Override
            public void onFailed(String code, String errMsg) {
//                ToastUtil.showToast(getBaseContext(), errMsg);
                AlertDialog.Builder builder = new AlertDialog.Builder(LoginLoadingActivity.this);
                builder.setMessage("username and password incorrect.. please try again");
                builder.setCancelable(false);
                builder.setPositiveButton("Retry", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        SharedPreferenceUtil.setBoolean(Enums.PreferenceKeys.loginStatus.toString(), false);

                        Intent signinIntent = new Intent(getBaseContext(), LoginActivity.class);
                        startActivity(signinIntent);
                        finish();
                    }
                });

                AlertDialog dialog = builder.create();
                dialog.show();

            }

            @Override
            public String asObject(String rspStr) {
                if (!TextUtils.isEmpty(rspStr)) {
                    try {
                        JSONObject json = new JSONObject(rspStr);
                        return json.optString("SessionId");

                    } catch (JSONException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }

                return "";
            }
        });
    }

    private void getProfile(String id) {

        mWebApi.getProfile(id, new IResponse<Profile>() {

            @Override
            public void onSucceed(Profile result) {

                UserProfilePrefs.get(LoginLoadingActivity.this).setLoggedInUser(result);

                mAnimLogo.nextTask(10
                );

                textStatus.setText("Retrieving users near you.. please wait");

                profilesAroundManager.initialize();
                profilesAroundManager.loadData();

//                mAnimLogo.stop();
//                Intent intent = new Intent(LoginLoadingActivity.this, MainActivity.class);
//                intent.setFlags(intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                startActivity(intent );
//                finish();
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
