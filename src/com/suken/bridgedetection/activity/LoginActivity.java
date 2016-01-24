package com.suken.bridgedetection.activity;

import com.suken.bridgedetection.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class LoginActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login_page);
	}

	public void startMain(View view){
		startActivity(new Intent(this, HomePageActivity.class));
	}
	
	
	public void jump(View view){
		int id = view.getId();
		if(id == R.id.jumpmain){
			startActivity(new Intent(this, HomePageActivity.class));
		} else if(id == R.id.jumplist){
			startActivity(new Intent(this, BridgeDetectionListActivity.class));
		} else if(id == R.id.jumpform){
			startActivity(new Intent(this, BridgeFormActivity.class));
		}
	}
	
}
