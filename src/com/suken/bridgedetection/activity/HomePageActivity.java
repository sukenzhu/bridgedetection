package com.suken.bridgedetection.activity;

import java.util.ArrayList;
import java.util.List;

import android.content.res.Configuration;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import com.alibaba.fastjson.JSONObject;
import com.googlecode.androidannotations.api.BackgroundExecutor;
import com.suken.bridgedetection.BridgeDetectionApplication;
import com.suken.bridgedetection.R;
import com.suken.bridgedetection.RequestType;
import com.suken.bridgedetection.fragment.HomePageFragment;
import com.suken.bridgedetection.fragment.LeftFragment;
import com.suken.bridgedetection.http.HttpTask;
import com.suken.bridgedetection.http.OnReceivedHttpResponseListener;
import com.suken.bridgedetection.storage.SharePreferenceManager;
import com.suken.bridgedetection.util.UiUtil;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;

public class HomePageActivity extends BaseActivity implements DialogInterface.OnClickListener, OnReceivedHttpResponseListener {

	private HomePageFragment mHomeFragment;
	private Fragment mGpsFragment;
	private Fragment mIpFragment;
	private FragmentManager mFragManager = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if(mIsFinished) {
			return;
		}
		setContentView(R.layout.activity_home_page);
		boolean flag = getIntent().getBooleanExtra("isOnline", true);
		String time = SharePreferenceManager.getInstance().readString(BridgeDetectionApplication.mCurrentUser.getAccount() + "lastSyncTime", "");
		boolean needSync = true;
		if (!TextUtils.isEmpty(time)) {
			if (System.currentTimeMillis() - Long.parseLong(time) < 24 * 60 * 60 * 1000) {
				needSync = false;
			}
		}
		mFragManager = getSupportFragmentManager();
		mHomeFragment = (HomePageFragment) mFragManager.findFragmentById(R.id.home_fragment);
		mGpsFragment = mFragManager.findFragmentById(R.id.gps_fragment);
		mIpFragment = mFragManager.findFragmentById(R.id.ip_fragment);
		if ((flag && needSync) || !BridgeDetectionApplication.mHasCacheUser ) {
			UiUtil.syncData(this, false, mHomeFragment);
			SharePreferenceManager.getInstance().updateString(BridgeDetectionApplication.mCurrentUser.getAccount() + "lastSyncTime", "" + System.currentTimeMillis());
		} else {
			mHomeFragment.onSyncFinished(true);
		}
		FragmentTransaction ft = mFragManager.beginTransaction();
		ft.hide(mGpsFragment);
		ft.hide(mIpFragment);
		ft.commit();
	}

	public void updateFragment(int resId) {
		switch (resId) {
		case R.id.left_frag_home: {
			FragmentTransaction ft = mFragManager.beginTransaction();
			ft.hide(mGpsFragment);
			ft.hide(mIpFragment);
			ft.show(mHomeFragment);
			ft.commit();
		}
			break;
		case R.id.left_frag_ip: {
			FragmentTransaction ft = mFragManager.beginTransaction();
			ft.hide(mGpsFragment);
			ft.show(mIpFragment);
			ft.hide(mHomeFragment);
			ft.commit();
		}
			break;
		case R.id.left_frag_video:
			break;
		case R.id.left_frag_update:
			UiUtil.update(this);
			break;
		case R.id.left_frag_exit: {
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setTitle("退出").setMessage("确定退出？").setPositiveButton("确定", this).setNegativeButton("取消", this).show();
		}
			break;
		case R.id.left_frag_gps: {
			FragmentTransaction ft = mFragManager.beginTransaction();
			ft.show(mGpsFragment);
			ft.hide(mIpFragment);
			ft.hide(mHomeFragment);
			ft.commit();
		}
			break;

		default:
			break;
		}
	}

	

	private void exit() {
		BackgroundExecutor.execute(new Runnable() {
			@Override
			public void run() {
				showLoading("正在注销登录...");
				List<NameValuePair> list = new ArrayList<NameValuePair>();
				BasicNameValuePair pair = new BasicNameValuePair("userId", BridgeDetectionApplication.mCurrentUser.getUserId());
				list.add(pair);
				pair = new BasicNameValuePair("token", BridgeDetectionApplication.mCurrentUser.getToken());
				list.add(pair);
				new HttpTask(HomePageActivity.this, RequestType.exit).executePost(list);
				dismissLoading();
			}
		});

	}

	@Override
	public void onClick(DialogInterface dialog, int which) {
		if (which == Dialog.BUTTON_POSITIVE) {
			dialog.dismiss();
			BackgroundExecutor.execute(new Runnable() {

				@Override
				public void run() {
					exit();
				}
			});
		} else {
			dialog.dismiss();
			selectHome();
		}
	}

	public void selectHome() {
		LeftFragment fragment = (LeftFragment) mFragManager.findFragmentById(R.id.left_fragment);
		fragment.selectHome();
	}

	@Override
	public void onRequestSuccess(RequestType type, JSONObject obj) {
		if (type == RequestType.exit) {
			toast("注销成功！");
			finish();
		}
	}

	@Override
	public void onRequestFail(RequestType type, String resultCode, String result) {
		if (type == RequestType.update) {
			runOnUiThread(new Runnable() {

				@Override
				public void run() {
					AlertDialog.Builder builder = new AlertDialog.Builder(HomePageActivity.this);
					OnClickListener listener = new OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							if (which == Dialog.BUTTON_POSITIVE) {
								UiUtil.update(HomePageActivity.this);
							} else {
								dialog.dismiss();
								selectHome();
							}
						}
					};
					builder.setTitle("更新").setMessage("检测更新失败，是否重试？").setPositiveButton("确定", listener).setNegativeButton("取消", listener).show();
				}
			});
		} else if (type == RequestType.exit) {
			finish();
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		if(mIsNeedTerminal){
			BridgeDetectionApplication.getInstance().onTerminate();
		}
	}

	public  void hidden(){
		mHomeFragment.onSelected();
	}
}
