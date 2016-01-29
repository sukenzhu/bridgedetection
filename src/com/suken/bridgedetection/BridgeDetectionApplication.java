package com.suken.bridgedetection;

import com.suken.bridgedetection.location.LocationManager;

import android.app.Application;
import android.content.Context;
import android.telephony.TelephonyManager;

public class BridgeDetectionApplication extends Application {

	private static BridgeDetectionApplication mInstance;
	
	public static String mDeviceId = "";

	@Override
	public void onCreate() {
		mInstance = this;
		LocationManager.getInstance();
		super.onCreate();
		TelephonyManager manager  = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
		mDeviceId = manager.getDeviceId();
	}

	public static BridgeDetectionApplication getInstance() {
		return mInstance;
	}

}
