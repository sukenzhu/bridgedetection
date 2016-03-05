package com.suken.bridgedetection;

import com.suken.bridgedetection.location.LocationManager;
import com.suken.bridgedetection.storage.UserInfo;
import com.suken.bridgedetection.util.UiUtil;

import android.app.Application;
import android.content.Context;
import android.telephony.TelephonyManager;

public class BridgeDetectionApplication extends Application {

	private static BridgeDetectionApplication mInstance;
	
	public static String mDeviceId = "";
	
	public static UserInfo mCurrentUser = null;
	public static boolean mIsOffline = false;

	@Override
	public void onCreate() {
		mInstance = this;
		LocationManager.getInstance();
		super.onCreate();
		TelephonyManager manager  = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
		mDeviceId = manager.getDeviceId();
		UiUtil.setAlarm(this);
	}

	public static BridgeDetectionApplication getInstance() {
		return mInstance;
	}
	
	

}
