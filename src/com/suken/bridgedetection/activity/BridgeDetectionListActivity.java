package com.suken.bridgedetection.activity;

import com.suken.bridgedetection.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

public class BridgeDetectionListActivity extends Activity {
	
	private ListView mList;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_list_page);
		mList = (ListView) findViewById(R.id.activity_list);
		mList.setAdapter(new ListPageAdapter(this));
		mList.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				startActivity(new Intent(BridgeDetectionListActivity.this, BridgeFormActivity.class));
			}
		});
	}
	
}
