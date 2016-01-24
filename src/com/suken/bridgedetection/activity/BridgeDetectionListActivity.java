package com.suken.bridgedetection.activity;

import com.suken.bridgedetection.R;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ListView;

public class BridgeDetectionListActivity extends Activity {
	
	private ListView mList;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_list_page);
		mList = (ListView) findViewById(R.id.activity_list);
		mList.setAdapter(new ListPageAdapter(this));
	}

	
	
	
	
	
}
