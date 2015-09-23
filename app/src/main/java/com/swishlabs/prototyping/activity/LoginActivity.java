package com.swishlabs.prototyping.activity;


import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.swishlabs.prototyping.MyApplication;
import com.swishlabs.prototyping.R;
import com.swishlabs.prototyping.data.api.callback.ControllerContentTask;
import com.swishlabs.prototyping.data.api.callback.IControllerContentCallback;
import com.swishlabs.prototyping.data.api.model.Constants;
import com.swishlabs.prototyping.data.api.model.User;
import com.swishlabs.prototyping.data.store.beans.UserTable;
import com.swishlabs.prototyping.entity.Profile;
import com.swishlabs.prototyping.entity.ProfileAround;
import com.swishlabs.prototyping.entity.Service;
import com.swishlabs.prototyping.net.IResponse;
import com.swishlabs.prototyping.net.WebApi;
import com.swishlabs.prototyping.util.Enums;
import com.swishlabs.prototyping.util.GsonUtil;
import com.swishlabs.prototyping.util.SharedPreferenceUtil;
import com.swishlabs.prototyping.util.StringUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class LoginActivity extends BaseActivity {

	protected static final String TAG = "LoginActivity";
	
	private Button loginBtn;
	private EditText emailTextField, passwordTextField;
	private TextView signUp,learnMore,termsOfUseBtn;

    protected WebApi mWebApi;



    @Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.login_layout);
		MyApplication.getInstance().addActivity(this);
        mWebApi = WebApi.getInstance(this);

        initView();
	}

	private void initView(){
		super.initTitleView();
        loginBtn = (Button) findViewById(R.id.butSignIn);
        emailTextField = (EditText) findViewById(R.id.signinEmailEditText);
        passwordTextField = (EditText) findViewById(R.id.signinPasswordEditText);

		signUp = (TextView) findViewById(R.id.sign_up);
        signUp.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
        signUp.setOnClickListener(this);

/*        learnMore = (TextView) findViewById(R.id.learnMore);
        learnMore.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
        learnMore.setOnClickListener(this);

        termsOfUseBtn = (TextView) findViewById(R.id.termsofuse);
        termsOfUseBtn.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
        termsOfUseBtn.setOnClickListener(this);*/

        loginBtn.setOnClickListener(this);
        passwordTextField.setTransformationMethod(PasswordTransformationMethod
                .getInstance());
	}

	@Override
	protected void initTitle(){
	}

	@Override
	public void onClick(View v) {
        if (v == loginBtn) {

            String email = emailTextField.getText().toString();
            String password = passwordTextField.getText().toString();

            doLogin(email, password);
            return;

        } else if (v == signUp) {
            Intent mIntent = new Intent(LoginActivity.this, SignupActivity.class);
            startActivity(mIntent);
            this.overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);

        } else if (v == termsOfUseBtn) {
            Intent mIntent = new Intent(LoginActivity.this, LegalActivity.class);
            startActivity(mIntent);
            this.overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);

        } else if (v == learnMore){
            Intent mIntent = new Intent(LoginActivity.this, LearnMoreActivity.class);
            startActivity(mIntent);
            this.overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);

        }

    }

    @Override
    public void onBackPressed(){
        MyApplication.getInstance().exit();
    }

    public void logInOld(String email, String password){

        if (TextUtils.isEmpty(email)) {
            StringUtil.showAlertDialog(getResources().getString(
                    R.string.login_title_name), getResources()
                    .getString(R.string.login_email_input_error), this);
            return;
        } else if (!StringUtil.isEmail(email)) {
            StringUtil.showAlertDialog(getResources().getString(R.string.login_title_name),
                    getResources().getString(R.string.email_format_error), this);
            return;
        }

        if (TextUtils.isEmpty(password)) {
            StringUtil.showAlertDialog(getResources().getString(
                    R.string.login_title_name), getResources()
                    .getString(R.string.login_password_null), this);
            return;
        }

        IControllerContentCallback icc = new IControllerContentCallback() {
            public void handleSuccess(String content) {

                JSONObject jsonObj = null, userObj = null;
                User user = null;

                try {
//                        jsonObj = new JSONObject(content);
//                        userObj = jsonObj.getJSONObject("user");
//                        user = new User(userObj);
//                    } catch (JSONException e) {
//                        // TODO Auto-generated catch block
//                        e.printStackTrace();
//                    }

                    jsonObj = new JSONObject(content);
                    if(jsonObj.has("error")) {
                        JSONArray errorMessage = jsonObj.getJSONObject("error").getJSONArray("message");
                        String message = String.valueOf((Object) errorMessage.get(0));
                        StringUtil.showAlertDialog(getResources().getString(R.string.login_title_name), message, context);
                        return;

                    }else if(jsonObj.has("user")) {
                        userObj = jsonObj.getJSONObject("user");
                        user = new User(userObj);
                        String virtualWalletPdf = null;
                        if(userObj.has("company")){
                            Object temp = userObj.getJSONObject("company").get("content");
//                            getAssistanceProvider(temp);
                            if(temp instanceof JSONObject){
                                virtualWalletPdf = ((JSONObject) temp).optString("virtual_wallet_pdf");

                            }
                        }

                        SharedPreferenceUtil.setString(Enums.PreferenceKeys.virtualWalletPdf.toString(), virtualWalletPdf);

                    }else {
                        StringUtil.showAlertDialog(getResources().getString(R.string.login_title_name), getResources().getString(R.string.login_failed), context);
                        return;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    StringUtil.showAlertDialog(getResources().getString(R.string.login_title_name), getResources().getString(R.string.login_failed), context);
                    return;
                }

                if(user != null) {
                    UserTable.getInstance().saveUser(user);
//                        User ww = null;
//                        ww = UserTable.getInstance().getUser(user.id);

                    SharedPreferenceUtil.setString(Enums.PreferenceKeys.userId.toString(), user.id);
                    SharedPreferenceUtil.setString(Enums.PreferenceKeys.token.toString(), user.token);
                    SharedPreferenceUtil.setString(Enums.PreferenceKeys.email.toString(),user.email);
                    SharedPreferenceUtil.setString(Enums.PreferenceKeys.firstname.toString(),user.firstName);
                    SharedPreferenceUtil.setString(Enums.PreferenceKeys.lastname.toString(),user.lastName);
                    SharedPreferenceUtil.setString(Enums.PreferenceKeys.username.toString(),user.userName);
                    SharedPreferenceUtil.setString(Enums.PreferenceKeys.countryCode.toString(),user.countryCode);
                    SharedPreferenceUtil.setString(Enums.PreferenceKeys.currencyCode.toString(),user.currencyCode);
                    SharedPreferenceUtil.setBoolean(getApplicationContext(), Enums.PreferenceKeys.loginStatus.toString(), true);
                    SharedPreferenceUtil.setApList(getApplicationContext(), user.getCompany().getApList());

                    MyApplication.setLoginStatus(true);


//                    Intent mIntent = new Intent(LoginActivity.this, TripPagesActivity.class);
//                    startActivity(mIntent);
  //                  LoginActivity.this.finish();
                } else {
                    StringUtil.showAlertDialog(getResources().getString(
                            R.string.login_title_name), "User is empty", context);
                    return;
                }

            }

            public void handleError(Exception e) {
                StringUtil.showAlertDialog(getResources().getString(
                        R.string.login_title_name), getResources().getString(R.string.login_failed), context);
                return;

            }
        };

        ControllerContentTask cct = new ControllerContentTask(
                Constants.BASE_URL + "users/login", icc,
                Enums.ConnMethod.POST, false);

        JSONObject user = new JSONObject();
        try {
            user.put("email", email);
            user.put("password", password);
        } catch (JSONException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }

        JSONObject login = new JSONObject();
        try {
            login.put("user", user);
        } catch (JSONException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }

        cct.execute(login.toString());



    }

    private void doLogin(String phoneNum, String psw) {

 /*       if (TextUtils.isEmpty(phoneNum)) {
            StringUtil.showAlertDialog(getResources().getString(
                    R.string.login_title_name), getResources()
                    .getString(R.string.login_email_input_error), this);
            return;
        } else if (!StringUtil.isEmail(email)) {
            StringUtil.showAlertDialog(getResources().getString(R.string.login_title_name),
                    getResources().getString(R.string.email_format_error), this);
            return;
        }*/

        if (TextUtils.isEmpty(psw)) {
            StringUtil.showAlertDialog(getResources().getString(
                    R.string.login_title_name), getResources()
                    .getString(R.string.login_password_null), this);
            return;
        }
//        showProgressDlg(R.string.login_ing);
        mWebApi.login(phoneNum, psw, new IResponse<String>() {

            @Override
            public void onSucceed(final String sessionId) {

                SharedPreferenceUtil.setString(Enums.PreferenceKeys.sessionId.toString(), sessionId);
//                SharedPreferenceUtil.setBoolean(getApplicationContext(), Enums.PreferenceKeys.loginStatus.toString(), true);

                getProfile(sessionId);
                getConnections(sessionId);
                getService(sessionId);
                getProfiles(sessionId);

                return;
/*                if (!TextUtils.isEmpty(tokenInfo)) {
//                    mWebApi.setToken(tokenInfo);
//                    PreferenceUtils.writeStrConfig(Constant.PreferKeys.KEY_TOKEN, tokenInfo, getBaseContext());
  //                  String cid = PreferenceUtils.readStrConfig(Constant.PreferKeys.KEY_GETUI_CLIENTID, getBaseContext(),"0");
                    mWebApi.postClientID(cid, null);
                    mWebApi.getUserInfo(-1, new IResponse<UserInfo>() {

                        @Override
                        public void onSuccessed(UserInfo result) {

                            PreferenceUtils.writeIntConfig(Constant.PreferKeys.KEY_LOGIN_WAY, 0, getBaseContext());
                            PreferenceUtils.writeBoolConfig(Constant.PreferKeys.KEY_BACK_TO_SQUARE, true, getBaseContext());

                            ToastUtil.showToast(getBaseContext(), getString(R.string.login_success));
                            mApp.setOnlineUser(result);
                            setResult(RESULT_OK);
                            finish();

//							Intent intent = new Intent(getBaseContext(), MainActivity.class);
//							intent.putExtra(EXTRA, mLoginType);
//							intent.putExtra(FLAG_LOGIN, true);
//							startActivity(intent);
                        }

                        @Override
                        public void onFailed(String code, String errMsg) {
                            dismissProgressDlg();
                            mWebApi.setToken("");
                            ToastUtil.showToast(mApp, errMsg);
                        }

                        @Override
                        public UserInfo asObject(String rspStr) {
                            UserInfo info= GsonUtil.jsonToObject(UserInfo.class, rspStr);
                            return info;
                        }
                    });

                } else {
                    ToastUtil.showToast(getBaseContext(), getString(R.string.login_failed));
                    dismissProgressDlg();
                }*/
            }

            @Override
            public void onFailed(String code, String errMsg) {
//                dismissProgressDlg();
//                ToastUtil.showToast(getBaseContext(), errMsg);
            }

            @Override
            public String asObject(String rspStr) {
                if(!TextUtils.isEmpty(rspStr)) {
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

//                mFinalDb.deleteAll(Profile.class);
                mFinalDb.save(result);

                List<Profile> profile = mFinalDb.findAll(Profile.class);
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

    private void getConnections(String id) {

        mWebApi.getConnections(id, new IResponse<List<Profile>>() {

            @Override
            public void onSucceed(List<Profile> result) {
//                mFinalDb.deleteAll(Profile.class);
                mFinalDb.save(result, Profile.class);

                List<Profile> profileList = mFinalDb.findAll(Profile.class);
                List<Profile> profile = mFinalDb.findAllByWhere(Profile.class, "sessionId = 117");

            }

            @Override
            public void onFailed(String code, String errMsg) {
//                dismissProgressDlg();
//                ToastUtil.showToast(getBaseContext(), errMsg);
            }

            @Override
            public List<Profile> asObject(String rspStr) throws JSONException {

                if (!TextUtils.isEmpty(rspStr)) {
                    TypeToken<List<Profile>> type = new TypeToken<List<Profile>>() {
                    };
                    return GsonUtil.jsonToList(type.getType(), rspStr);
                }
                return new ArrayList<Profile>();

            }
        });
    }

    private void getService(String id) {

        mWebApi.getService(id, new IResponse<List<Service>>() {

            @Override
            public void onSucceed(List<Service> result) {
                mFinalDb.save(result, Service.class);

                List<Service> services = mFinalDb.findAll(Service.class);

            }

            @Override
            public void onFailed(String code, String errMsg) {
//                dismissProgressDlg();
//                ToastUtil.showToast(getBaseContext(), errMsg);
            }

            @Override
            public List<Service> asObject(String rspStr) throws JSONException {

                if (!TextUtils.isEmpty(rspStr)) {
                    TypeToken<List<Service>> type = new TypeToken<List<Service>>() {
                    };
                    return GsonUtil.jsonToList(type.getType(), rspStr);
                }
                return new ArrayList<Service>();

            }
        });
    }

    private void getProfiles(String id) {

        mWebApi.getProfiles(id, new IResponse<List<ProfileAround>>() {

/*            @Override
            public void onSuccessed(List<Profile> result) {
                mFinalDb.save(result, Profile.class);

                List<Profile> profileList = mFinalDb.findAll(Profile.class);
                List<Profile> profile = mFinalDb.findAllByWhere(Profile.class, "id = 117");

            }*/

            @Override
            public void onSucceed(List<ProfileAround> result) {
                mFinalDb.save(result);

                List<ProfileAround> profileList = mFinalDb.findAll(ProfileAround.class);

            }

            @Override
            public void onFailed(String code, String errMsg) {
//                dismissProgressDlg();
//                ToastUtil.showToast(getBaseContext(), errMsg);
            }

            @Override
            public List<ProfileAround> asObject(String rspStr) throws JSONException {

                if (!TextUtils.isEmpty(rspStr)) {
                    TypeToken<List<ProfileAround>> type = new TypeToken<List<ProfileAround>>() {
                    };
                    return GsonUtil.jsonToList(type.getType(), rspStr);
                }
                return new ArrayList<ProfileAround>();

            }
        });
    }

}