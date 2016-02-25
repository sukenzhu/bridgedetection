package com.suken.bridgedetection.fragment;

import com.suken.bridgedetection.R;
import com.suken.bridgedetection.activity.BridgeDetectionListActivity;
import com.suken.bridgedetection.location.LocationManager;
import com.suken.bridgedetection.location.LocationResult;
import com.suken.bridgedetection.location.OnLocationFinishedListener;
import com.suken.bridgedetection.storage.SharePreferenceManager;
import com.suken.bridgedetection.util.OnSyncDataFinishedListener;
import com.suken.bridgedetection.util.UiUtil;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

public class HomePageFragment extends BaseFragment implements OnClickListener, OnLocationFinishedListener, OnSyncDataFinishedListener {

	private TextView mjingdu;
	private TextView mWeidu;
	private TextView mDeviceId;
	private TextView mLastLogin = null;
	private TextView time = null;

	private View mContentView = null;
	private boolean mIsGpsSuccess = false;
	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			time.setText(UiUtil.formatNowTime());
			sendEmptyMessageDelayed(0, 1000);
		}
	};

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return inflater.inflate(R.layout.home_fragment_view, container, false);
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		mContentView = view;
		mjingdu = (TextView) view.findViewById(R.id.home_jingdu);
		mWeidu = (TextView) view.findViewById(R.id.home_weidu);
		mDeviceId = (TextView) view.findViewById(R.id.home_deviceId);
		mDeviceId.setText("设备号:" + UiUtil.genDeviceId());
		mLastLogin = (TextView) view.findViewById(R.id.last_login);
		mLastLogin.setText("上次登录:" + SharePreferenceManager.getInstance().readString("上次登录", UiUtil.formatNowTime("yyyy-MM-dd")));
		time = (TextView) view.findViewById(R.id.home_time);
		time.setText(UiUtil.formatNowTime());
		handler.sendEmptyMessageDelayed(0, 1000);

		new HomeFragmentItemController(this, mContentView, R.drawable.richangyanghu, "日常养护");
		new HomeFragmentItemController(this, mContentView, R.drawable.zhiliangchoujian, "质量抽检");
		new HomeFragmentItemController(this, mContentView, R.drawable.zhuanxianggongcheng, "专项工程");
		new HomeFragmentItemController(this, mContentView, R.drawable.qiaoliangjiancha, "桥梁检查");
		new HomeFragmentItemController(this, mContentView, R.drawable.suidaojiancha, "隧道检查");
		new HomeFragmentItemController(this, mContentView, R.drawable.qiaoliangxuncha, "桥梁巡查");
		new HomeFragmentItemController(this, mContentView, R.drawable.suidaoxuncha, "隧道巡查");
		new HomeFragmentItemController(this, mContentView, R.drawable.luzhengxunshi, "路政巡视");
		new HomeFragmentItemController(this, mContentView, R.drawable.jidianxuncha, "机电巡查");
		new HomeFragmentItemController(this, mContentView, R.drawable.yingjishijian, "应急事件");
	}

	@Override
	public void onClick(View v) {
		int vid = v.getId();
		if (vid == R.drawable.qiaoliangjiancha || vid == R.drawable.qiaoliangxuncha || vid == R.drawable.suidaojiancha || vid == R.drawable.suidaoxuncha) {
			Intent intent = new Intent(getActivity(), BridgeDetectionListActivity.class);
			intent.putExtra("type", vid);
			startActivity(intent);
		} else {
			toast("敬请期待!");
		}
	}

	@Override
	public void onLocationFinished(LocationResult result) {
		if (result.isSuccess) {
			mIsGpsSuccess = true;
			mjingdu.setText("经度:" + result.latitude);
			mWeidu.setText("纬度:" + result.longitude);
			TextView tv = (TextView) getActivity().findViewById(R.id.syncLocationTv);
			tv.setText("定位成功");
			tv.setTextColor(Color.WHITE);
		} else {
			TextView tv = (TextView) getActivity().findViewById(R.id.syncLocationTv);
			tv.setText("定位失败");
			tv.setTextColor(Color.RED);
		}
	}

	@Override
	public void onResume() {
		super.onResume();
		android.location.LocationManager locationManager = (android.location.LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
		if (locationManager.isProviderEnabled(android.location.LocationManager.GPS_PROVIDER) && !mIsGpsSuccess) {
			mContentView.postDelayed(new Runnable() {
				@Override
				public void run() {
					if(!getActivity().isFinishing()){
						LocationManager.getInstance().syncLocation(HomePageFragment.this);
					}
				}
			}, 200);
		}
	}

	@Override
	public void onSyncFinished(boolean isSuccess) {
		getActivity().runOnUiThread(new Runnable() {
			
			@Override
			public void run() {
				initGPS();
			}
		});
	}

	private void initGPS() {
		android.location.LocationManager locationManager = (android.location.LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
		// 判断GPS模块是否开启，如果没有则开启
		if (!locationManager.isProviderEnabled(android.location.LocationManager.GPS_PROVIDER)) {
			AlertDialog.Builder dialog = new AlertDialog.Builder(this.getActivity());
			dialog.setTitle("提醒");
			dialog.setMessage("请打开GPS");
			dialog.setPositiveButton("确定", new android.content.DialogInterface.OnClickListener() {

				@Override
				public void onClick(DialogInterface arg0, int arg1) {
					// 转到手机设置界面，用户设置GPS
					Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
					startActivityForResult(intent, 0); // 设置完成后返回到原来的界面
				}
			});
			dialog.setNeutralButton("取消", new android.content.DialogInterface.OnClickListener() {

				@Override
				public void onClick(DialogInterface arg0, int arg1) {
					arg0.dismiss();
				}
			});
			dialog.show();
		} else {
			LocationManager.getInstance().syncLocation(this);
		}
	}

}