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
	
}
