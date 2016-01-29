package com.suken.bridgedetection.storage;

import com.suken.bridgedetection.BridgeDetectionApplication;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class SharePreferenceManager {

	private static SharePreferenceManager mInstance;
	private static final String BRIDGEDETECTION_PREFERENCE = "bridgedetection";

	private SharePreferenceManager() {
	}

	public static SharePreferenceManager getInstance() {
		if (mInstance == null) {
			mInstance = new SharePreferenceManager();
		}
		return mInstance;
	}

	private Editor getEditablePreference() {
		SharedPreferences preference = BridgeDetectionApplication.getInstance()
				.getSharedPreferences(BRIDGEDETECTION_PREFERENCE, Context.MODE_PRIVATE);
		return preference.edit();
	}

	private SharedPreferences getPreference() {
		SharedPreferences preference = BridgeDetectionApplication.getInstance()
				.getSharedPreferences(BRIDGEDETECTION_PREFERENCE, Context.MODE_PRIVATE);
		return preference;
	}

	public boolean readBoolean(String key, boolean defaltValue) {
		return getPreference().getBoolean(key, defaltValue);
	}

	public void updateBoolean(String key, boolean value) {
		getEditablePreference().putBoolean(key, value).commit();
	}
	
	public String readString(String key, String defaltValue) {
		return getPreference().getString(key, defaltValue);
	}

	public void updateString(String key, String value) {
		getEditablePreference().putString(key, value).commit();
	}

}
