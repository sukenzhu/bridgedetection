package com.suken.bridgedetection;

import android.app.Application;
import android.content.Context;
import android.os.Environment;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import com.suken.bridgedetection.activity.BaseActivity;
import com.suken.bridgedetection.location.LocationManager;
import com.suken.bridgedetection.storage.UserInfo;
import com.suken.bridgedetection.util.UiUtil;

import java.io.*;

public class BridgeDetectionApplication extends Application {

	private static BridgeDetectionApplication mInstance;
	
	public static String mDeviceId = "";
	
	public static UserInfo mCurrentUser = null;
	public static boolean mIsOffline = false;
	public static BaseActivity mCurrentActivity = null;

	@Override
	public void onCreate() {
		mInstance = this;
		LocationManager.getInstance();
		super.onCreate();
		TelephonyManager manager  = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
		mDeviceId = manager.getDeviceId();
		UiUtil.setAlarm(this);
		Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
			@Override
			public void uncaughtException(Thread thread, Throwable ex) {
				String exStr = Log.getStackTraceString(ex);
				try {
					write(exStr);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		});
	}

	private void write(String message) throws IOException {
		String state = Environment.getExternalStorageState();
		if (TextUtils.equals(state, Environment.MEDIA_MOUNTED)) {
			String dir = "/sdcard/com.suken.bridgedetection";
			File dirFile = new File(dir);
			if (dirFile != null && !dirFile.exists()) {
				dirFile.mkdir();
			}
			String path = dir + "/log.log";
			File file = new File(path);
			if (file != null) {
				long size = file.length();
				if (size > 1 * 1024 * 1024 * 5) {
					file.delete();
				}
				if (!file.exists()) {
					file.createNewFile();
				}
				FileOutputStream fos = new FileOutputStream(file, true);
				BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fos));
				bw.newLine();
				bw.write(message);
				bw.flush();
				fos.close();
				bw.close();
			}
		}
	}

	public static BridgeDetectionApplication getInstance() {
		return mInstance;
	}

	@Override
	public void onTerminate() {
		super.onTerminate();
		System.exit(0);
	}
}
