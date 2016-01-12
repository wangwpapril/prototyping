package com.swishlabs.prototyping.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.swishlabs.prototyping.R;
import com.swishlabs.prototyping.net.IResponse;
import com.swishlabs.prototyping.net.WebApi;
import com.swishlabs.prototyping.util.StringUtil;

import org.json.JSONException;
import org.json.JSONObject;


public class SignupActivity extends BaseActivity {

	protected static final String TAG = "SignupActivity";
	private EditText signupEmail;
	private EditText signupUserName;
	private EditText signupPassword;
	private EditText signupPWconfirm;
	private EditText signupFirstName;
	private EditText signupLastName;

	private Button btnSignUp;
	private TextView termsOfUseBtn;

	private String email = null;
	private String userName = null;
	private String password = null;
	private String passwordConf = null;
	private String firstName = null;
	private String lastName = null;

	private WebApi mWebApi;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.register_layout);
		initView();

		mWebApi = WebApi.getInstance(this);

	}

	private void initView() {
		super.initTitleView();

		signupFirstName = (EditText) findViewById(R.id.regFirstnameEditText);

		signupLastName = (EditText) findViewById(R.id.regLastnameEditText);

		signupEmail = (EditText) findViewById(R.id.regEmailEditText);

        signupUserName = (EditText) findViewById(R.id.regUsernameEditText);

        signupPassword = (EditText) findViewById(R.id.regPasswordEditText);

        signupPWconfirm = (EditText) findViewById(R.id.regConfirmPasswordEditText);

		btnSignUp = (Button) findViewById(R.id.butRegister);
		btnSignUp.setOnClickListener(this);

	}
	
	@Override
	protected void initTitle() {
	}

	@Override
	public void onClick(View v) {

		if (v == btnSignUp){
			email = signupEmail.getText().toString();

			userName =signupUserName.getText().toString();
			password = signupPassword.getText().toString();
			passwordConf = signupPWconfirm.getText().toString();

			firstName = signupFirstName.getText().toString();
			lastName = signupLastName.getText().toString();
			
			if (TextUtils.isEmpty(email) ||TextUtils.isEmpty(userName)
					|| TextUtils.isEmpty(password) ||
					TextUtils.isEmpty(passwordConf)) {
                StringUtil.showAlertDialog(getResources().getString(
                        R.string.signup_title_name), getResources()
                        .getString(R.string.input_empty_error), this);

                return;
			}

			if(!password.equals(passwordConf)){
				StringUtil.showAlertDialog(getResources().getString(R.string.signup_title_name),
						getResources().getString(R.string.register_pw_error),this);
				return;

			}

            if(!StringUtil.isEmail(email)){
                StringUtil.showAlertDialog(getResources().getString(R.string.signup_title_name),
                        getResources().getString(R.string.email_format_error),this);
				return;
			}

            if(userName.length()<3){
                StringUtil.showAlertDialog(getResources().getString(R.string.signup_title_name),
                        getResources().getString(R.string.username_length_error),this);
                return;

            }

            if(password.length()<7){
                StringUtil.showAlertDialog(getResources().getString(R.string.signup_title_name),
                        getResources().getString(R.string.password_length_error),this);
                return;

            }

			doSignup();

		} else if (v == termsOfUseBtn) {
			Intent i = new Intent();
			i.setClass(context, LegalActivity.class);
			context.startActivity(i);
			this.overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
		} else if (v == ivTitleBack) {
			this.overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
			context.finish();
		}

	}

	private void doSignup() {
//        showProgressDlg(R.string.login_ing);
//		mWebApi.register("newapptest", "newapptest@hotmail.com", "1234", new IResponse<String>() {
			mWebApi.register(firstName, lastName, userName, email, password, new IResponse<String>() {

			@Override
			public void onSucceed(final String tokenInfo) {
				return;
			}

			@Override
			public void onFailed(String code, String errMsg) {
//                dismissProgressDlg();
//                ToastUtil.showToast(getBaseContext(), errMsg);
			}

			@Override
			public String asObject(String rspStr) {
				try {
					JSONObject json = new JSONObject(rspStr);
					//                  return json.getString("token");
					return "test";
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				return "";
			}
		});
	}

}