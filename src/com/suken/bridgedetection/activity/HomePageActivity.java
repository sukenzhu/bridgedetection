package com.suken.bridgedetection.activity;

import com.suken.bridgedetection.R;
import com.suken.bridgedetection.storage.SharePreferenceManager;
import com.suken.bridgedetection.util.UiUtil;

import android.os.Bundle;
import android.text.TextUtils;

public class HomePageActivity extends BaseActivity {

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
		if (flag && needSync) {
			UiUtil.syncData(this);
		}
	}

}
