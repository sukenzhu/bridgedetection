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
		setContentView(R.layout.activity_home_page);
		boolean flag = getIntent().getBooleanExtra("isOnline", true);
		String time = SharePreferenceManager.getInstance().readString("lastSyncTime", "");
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
		if (flag && needSync) {
			UiUtil.syncData(this, false, mHomeFragment);
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
			update();
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

	private void update() {
		BackgroundExecutor.execute(new Runnable() {

			@Override
			public void run() {
				showLoading("检查更新中...");
				List<NameValuePair> list = new ArrayList<NameValuePair>();
				BasicNameValuePair pair = new BasicNameValuePair("userId", BridgeDetectionApplication.mCurrentUser.getUserId());
				list.add(pair);
				pair = new BasicNameValuePair("token", BridgeDetectionApplication.mCurrentUser.getToken());
				list.add(pair);
				new HttpTask(HomePageActivity.this, RequestType.update).executePost(list);
				dismissLoading();
			}
		});
	}

	private void exit() {
		showLoading("正在注销登录...");
		List<NameValuePair> list = new ArrayList<NameValuePair>();
		BasicNameValuePair pair = new BasicNameValuePair("userId", BridgeDetectionApplication.mCurrentUser.getUserId());
		list.add(pair);
		pair = new BasicNameValuePair("token", BridgeDetectionApplication.mCurrentUser.getToken());
		list.add(pair);
		new HttpTask(this, RequestType.exit).executePost(list);
		dismissLoading();
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

	private void selectHome() {
		LeftFragment fragment = (LeftFragment) mFragManager.findFragmentById(R.id.left_fragment);
		fragment.selectHome();
	}

	@Override
	public void onRequestSuccess(RequestType type, String result) {
		if (type == RequestType.exit) {
			toast("注销成功！");
			finish();
		} else if (type == RequestType.update) {
			JSONObject obj = JSON.parseObject(result);
			int versioncode = obj.getInteger("versioncode");
			final String url = obj.getString("apkurl");
			try {
				PackageInfo info = getPackageManager().getPackageInfo(getPackageName(), 0);
				if (versioncode > info.versionCode) {
					todownload(url);
				}
			} catch (NameNotFoundException e) {
				e.printStackTrace();
			}
		}
	}

	private void todownload(final String url) {
		runOnUiThread(new Runnable() {

			@Override
			public void run() {
				AlertDialog.Builder builder = new AlertDialog.Builder(HomePageActivity.this);
				OnClickListener listener = new OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						if (which == Dialog.BUTTON_POSITIVE) {
							// 下载
							final DownloadManager downloadManager = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
							DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
							request.setDestinationInExternalPublicDir("bridgedetection",  "bridgedetection.apk");
							final long downloadId = downloadManager.enqueue(request);
							IntentFilter filter = new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE);
							BroadcastReceiver receiver = new BroadcastReceiver() {
								@Override
								public void onReceive(Context context, Intent intent) {
									long reference = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);
									if (downloadId == reference) {
										Uri uri = downloadManager.getUriForDownloadedFile(downloadId);
										Intent a = new Intent();
										a.setAction(Intent.ACTION_VIEW);
										a.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK); 
										a.setDataAndType(uri, "application/vnd.android.package-archive");
										startActivity(a);
									}
								}
							};
							registerReceiver(receiver, filter);
						} else {
							dialog.dismiss();
							selectHome();
						}
					}
				};
				builder.setTitle("更新").setMessage("检测到新版本，是否更新？").setPositiveButton("确定", listener).setNegativeButton("取消", listener).show();
			}
		});

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
								update();
							} else {
								dialog.dismiss();
								selectHome();
							}
						}
					};
					builder.setTitle("更新").setMessage("检测更新失败，是否重试？").setPositiveButton("确定", listener).setNegativeButton("取消", listener).show();
				}
			});
		}
	}

}
