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

import com.swishlabs.prototyping.MyApplication;
import com.swishlabs.prototyping.R;
import com.swishlabs.prototyping.data.api.callback.ControllerContentTask;
import com.swishlabs.prototyping.data.api.callback.IControllerContentCallback;
import com.swishlabs.prototyping.data.api.model.Connection;
import com.swishlabs.prototyping.data.api.model.Constants;
import com.swishlabs.prototyping.data.api.model.User;
import com.swishlabs.prototyping.data.store.beans.UserTable;
import com.swishlabs.prototyping.util.Enums;
import com.swishlabs.prototyping.util.SharedPreferenceUtil;
import com.swishlabs.prototyping.util.StringUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;


public class LoginActivity extends BaseActivity {

	protected static final String TAG = "LoginActivity";
	
	private Button loginBtn;
	private EditText emailTextField, passwordTextField;
	private TextView signUp,learnMore,termsOfUseBtn;


    @Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.login_layout);
		MyApplication.getInstance().addActivity(this);

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

        learnMore = (TextView) findViewById(R.id.learnMore);
        learnMore.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
        learnMore.setOnClickListener(this);

        termsOfUseBtn = (TextView) findViewById(R.id.termsofuse);
        termsOfUseBtn.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
        termsOfUseBtn.setOnClickListener(this);

        loginBtn.setOnClickListener(this);
        passwordTextField.setTransformationMethod(PasswordTransformationMethod
				.getInstance());
	}

	@Override
	protected void initTitle(){
	}

    protected void getAssistanceProvider(Object obj){
//        JSONObject jsonData = (JSONObject)obj;
//        String test = "";
    }
	

	@Override
	public void onClick(View v) {
        if (v == loginBtn) {

            String email = emailTextField.getText().toString();
            String password = passwordTextField.getText().toString();
            if (TextUtils.isEmpty(email)) {
                StringUtil.showAlertDialog(getResources().getString(
                        R.string.login_title_name), getResources()
                        .getString(R.string.login_email_input_error), this);
                return;
            } else if (!StringUtil.isEmail(email)) {
//                StringUtil.showAlertDialog(getResources().getString(R.string.login_title_name),
  //                      getResources().getString(R.string.email_format_error), this);
    //            return;
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
                    user = new User(jsonObj);
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
                            getAssistanceProvider(temp);
                            if(temp instanceof JSONObject){
                                virtualWalletPdf = ((JSONObject) temp).optString("virtual_wallet_pdf");

                            }
                        }

                        SharedPreferenceUtil.setString(Enums.PreferenceKeys.virtualWalletPdf.toString(), virtualWalletPdf);

                     }else {
//                        StringUtil.showAlertDialog(getResources().getString(R.string.login_title_name), getResources().getString(R.string.login_failed), context);
  //                      return;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    StringUtil.showAlertDialog(getResources().getString(R.string.login_title_name), getResources().getString(R.string.login_failed), context);
                    return;
                }

                if(user != null) {
//                        UserTable.getInstance().saveUser(user);
//                        User ww = null;
//                        ww = UserTable.getInstance().getUser(user.id);

                    SharedPreferenceUtil.setString(Enums.PreferenceKeys.sessionId.toString(), user.sessionId);
                    SharedPreferenceUtil.setString(Enums.PreferenceKeys.username.toString(),user.userName);

/*                    SharedPreferenceUtil.setString(Enums.PreferenceKeys.token.toString(), user.token);
                        SharedPreferenceUtil.setString(Enums.PreferenceKeys.email.toString(),user.email);
                        SharedPreferenceUtil.setString(Enums.PreferenceKeys.firstname.toString(),user.firstName);
                        SharedPreferenceUtil.setString(Enums.PreferenceKeys.lastname.toString(),user.lastName);
                        SharedPreferenceUtil.setString(Enums.PreferenceKeys.countryCode.toString(),user.countryCode);
                        SharedPreferenceUtil.setString(Enums.PreferenceKeys.currencyCode.toString(),user.currencyCode);
                        SharedPreferenceUtil.setBoolean(getApplicationContext(), Enums.PreferenceKeys.loginStatus.toString(), true);
*/
                    MyApplication.setLoginStatus(true);

                    getConnection();
//                        Intent mIntent = new Intent(LoginActivity.this, TripPagesActivity.class);
  //                      startActivity(mIntent);
    //                    LoginActivity.this.finish();
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
                    Constants.LOGIN_URL, icc,
                    Enums.ConnMethod.POST, false);

            JSONObject user = new JSONObject();
            try {
                user.put("UserName", email);
                user.put("Password", password);
            } catch (JSONException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }

/*            JSONObject login = new JSONObject();
            try {
                login.put("user", user);
            } catch (JSONException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }*/

            cct.execute(user.toString());


        } else if (v == signUp) {
            Intent mIntent = new Intent(LoginActivity.this, SignupActivity.class);
            startActivity(mIntent);

        } else if (v == termsOfUseBtn) {
            Intent mIntent = new Intent(LoginActivity.this, LegalActivity.class);
            startActivity(mIntent);

        } else if (v == learnMore){
            Intent mIntent = new Intent(LoginActivity.this, LearnMoreActivity.class);
            startActivity(mIntent);

        }

    }

    private void getProfile(){
        IControllerContentCallback icc = new IControllerContentCallback() {

            public void handleSuccess(String content) {
                JSONObject profile;
                try {
                    profile = new JSONObject(content);
 //                   JSONObject ra = currencyInfo.getJSONObject("rates");
   //                 rate = currencyInfo.getJSONObject("rates").getDouble(currencyCode);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            public void handleError(Exception e){

            }
        };

        String sessionId = SharedPreferenceUtil.getString(Enums.PreferenceKeys.sessionId.toString(), null);

        ControllerContentTask cct = new ControllerContentTask(
                Constants.PROFILE_URL+sessionId+"/" + Constants.FORMAT_JSON, icc,
                Enums.ConnMethod.GET,false);

        String ss = null;
        cct.execute(ss);

    }


    private void getConnection(){
        IControllerContentCallback icc = new IControllerContentCallback() {

            public void handleSuccess(String content) {
                JSONArray jsonArr;
                try {
                    jsonArr = (JSONArray) new JSONTokener(content).nextValue();
                    int size = jsonArr.length();

                    if (jsonArr != null)
                    {
                        for (int i=0; i < jsonArr.length(); i++)
                        {
                            JSONObject tmpJson = jsonArr.getJSONObject(i);

                            Connection connection = new Connection();
                            connection.fromId = tmpJson.getInt("fromid");
                            connection.toId = tmpJson.getInt("toid");
                            String tempDate = tmpJson.getString("dateConnRequested");

                            //connection.dateConnectionRequest = new Date(tmpJson.getString("dateConnRequested"));
                            connection.notified = tmpJson.getBoolean("notified");
                            connection.status = tmpJson.getString("status");

                            if (connection.status.equals(Connection.CONNECT))
                            {
/*                                ResourceManager.UserProfile.contactsId = UserTable.getInstance().getContact();

                                if (connection.fromId != ResourceManager.UserProfile.id)
                                {
                                    if (!ResourceManager.UserProfile.contactsId.contains(connection.fromId))
                                    {
                                        UserTable.getInstance().addContact(connection.fromId);
                                        ResourceManager.UserProfile.contactsId.add(connection.fromId);

                                        NewContact tmpNewContact = new NewContact(connection.fromId);
                                        ResourceManager.UserProfile.newContactsId.add(tmpNewContact);
                                    }
                                }
                                else if (connection.toId != ResourceManager.UserProfile.id)
                                {
                                    if (!ResourceManager.UserProfile.contactsId.contains(connection.toId))
                                    {
                                        UserTable.getInstance().addContact(connection.toId);
                                        ResourceManager.UserProfile.contactsId.add(connection.toId);

                                        NewContact tmpNewContact = new NewContact(connection.toId);
                                        ResourceManager.UserProfile.newContactsId.add(tmpNewContact);
                                    }
                                }*/
                            }
                            //connection.dateConnectionConfirmed = new Date(tmpJson.getString("dateConnConfirmed"));

//                            mUser.connections.add(connection);

                        }
                    }
                } catch (JSONException e) {
//                    return false;
                }

            }

            public void handleError(Exception e){

            }
        };

        String sessionId = SharedPreferenceUtil.getString(Enums.PreferenceKeys.sessionId.toString(), null);
        String url = Constants.PROFILE_URL+sessionId+"/connections" + "?offset=" +
                "0" + "&format=json";

        ControllerContentTask cct = new ControllerContentTask(
                url, icc,
                Enums.ConnMethod.GET,false);

        String ss = null;
        cct.execute(ss);

    }
}