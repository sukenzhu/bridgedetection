package com.suken.bridgedetection.util;

import com.suken.bridgedetection.BridgeDetectionApplication;
import com.suken.bridgedetection.storage.CheckFormAndDetailDao;
import com.suken.bridgedetection.storage.SdxcFormAndDetailDao;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class AlarmReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		if (intent.getAction().equals("android.suken.action.checkdata")) {
			if(BridgeDetectionApplication.mCurrentUser != null) {
				// 清理数据
				new CheckFormAndDetailDao().deleteAllLocalData();
				new SdxcFormAndDetailDao().deleteAllLocalData();
			}
		} else if (intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED)) {
			// 开机自启动则启动
			UiUtil.setAlarm(context);
		}
	}

}
