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
import com.suken.bridgedetection.http.HttpTask;
import com.suken.bridgedetection.http.OnReceivedHttpResponseListener;
import com.suken.bridgedetection.storage.GXLuXianInfo;
import com.suken.bridgedetection.storage.GXLuXianInfoDao;
import com.suken.bridgedetection.storage.UserInfo;

import android.os.Bundle;

public class HomePageActivity extends BaseActivity {
	
	private UserInfo mCurrentUser = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mCurrentUser = (UserInfo) getIntent().getSerializableExtra("userInfo");
		setContentView(R.layout.activity_home_page);
		syncData();
	}
	
	private void syncData(){
		final OnReceivedHttpResponseListener listener = new OnReceivedHttpResponseListener() {

			@Override
			public void onRequestSuccess(String result) {
				JSONObject obj = JSON.parseObject(result);
				List<GXLuXianInfo> list = JSON.parseArray(obj.getString("datas"), GXLuXianInfo.class);
				new GXLuXianInfoDao().create(list);
			}

			@Override
			public void onRequestFail(String resultCode, String result) {
				toast(result + "(" + resultCode + ")");
			}
		};
		BackgroundExecutor.execute(new Runnable() {
			
			@Override
			public void run() {
				showLoading("同步数据中...");
				List<NameValuePair> list = new ArrayList<NameValuePair>();
				BasicNameValuePair pair = new BasicNameValuePair("userId", mCurrentUser.getUserId());
				list.add(pair);
				pair = new BasicNameValuePair("token", mCurrentUser.getToken());
				list.add(pair);
				pair = new BasicNameValuePair("did", BridgeDetectionApplication.mDeviceId);
				list.add(pair);
				new HttpTask(listener).executePost(list, "/bpmp/m/base/getLuXianByUID.ht");
				dismissLoading();
			}
		});
	}


}
