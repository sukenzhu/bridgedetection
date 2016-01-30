package com.suken.bridgedetection.activity;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.googlecode.androidannotations.api.BackgroundExecutor;
import com.suken.bridgedetection.BridgeDetectionApplication;
import com.suken.bridgedetection.R;
import com.suken.bridgedetection.RequestType;
import com.suken.bridgedetection.http.HttpTask;
import com.suken.bridgedetection.http.OnReceivedHttpResponseListener;
import com.suken.bridgedetection.storage.UserInfo;
import com.suken.bridgedetection.storage.UserInfoDao;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

public class LoginActivity extends BaseActivity {

	private List<UserInfo> mUserInfos = null;

	private EditText mNameView = null;
	private EditText mPwdView = null;
	private UserInfoDao mUserDao;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mUserDao = new UserInfoDao();
		mUserInfos = mUserDao.queryAll();
		if(mUserInfos != null && mUserInfos.size() > 0){
			jumpToHome(mUserInfos.get(0), false);
			finish();
		}
		setContentView(R.layout.activity_login_page);
		mNameView = (EditText) findViewById(R.id.username);
		mPwdView = (EditText) findViewById(R.id.userpwd);
	}

	public void onlineLogin(View view) {
		String name = mNameView.getText().toString();
		String pwd = mPwdView.getText().toString();
		if (TextUtils.isEmpty(name)) {
			toast("用户名不能为空");
			return;
		}
		if (TextUtils.isEmpty(pwd)) {
			toast("密码不能为空");
			return;
		}
		login(name, pwd);
	}

	public void offlineLogin(View view) {
		if (mUserInfos == null || mUserInfos.size() == 0) {
			toast("请先联网登陆一次");
			return;
		}
		String name = mNameView.getText().toString();
		String pwd = mPwdView.getText().toString();
		if (TextUtils.isEmpty(name)) {
			toast("用户名不能为空");
			return;
		}
		if (TextUtils.isEmpty(pwd)) {
			toast("密码不能为空");
			return;
		}
		UserInfo info = null;
		for (UserInfo user : mUserInfos) {
			if (TextUtils.equals(name, user.getAccount()) && TextUtils.equals(pwd, user.getPassword())) {
				info = user;
				break;
			}
		}
		if (info != null) {
			jumpToHome(info, true);
		} else {
			toast("用户名或密码不正确");
		}
	}

	private void jumpToHome(UserInfo info, boolean isOnline) {
		BridgeDetectionApplication.mCurrentUser = info;
		Intent intent = new Intent(this, HomePageActivity.class);
		intent.putExtra("userInfo", info);
		intent.putExtra("isOnline", true);
		startActivity(intent);
	}

	private void login(final String name, final String pwd) {
		final OnReceivedHttpResponseListener listener = new OnReceivedHttpResponseListener() {

			@Override
			public void onRequestSuccess(RequestType type, String result) {
				JSONObject obj = JSON.parseObject(result);
				UserInfo info = obj.getObject("userInfo", UserInfo.class);
				info.setPassword(pwd);
				mUserDao.create(info);
				jumpToHome(info, true);
			}

			@Override
			public void onRequestFail(RequestType type, String resultCode, String result) {
				toast(result + "(" + resultCode + ")");
			}
		};

		BackgroundExecutor.execute(new Runnable() {

			@Override
			public void run() {
				showLoading("登录中...");
				List<NameValuePair> list = new ArrayList<NameValuePair>();
				BasicNameValuePair pair = new BasicNameValuePair("account", name);
				list.add(pair);
				pair = new BasicNameValuePair("password", pwd);
				list.add(pair);
				pair = new BasicNameValuePair("did", BridgeDetectionApplication.mDeviceId);
				list.add(pair);
				new HttpTask(listener, RequestType.login).executePost(list);
				dismissLoading();
				finish();
			}
		});
	}

}
