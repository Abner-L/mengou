package com.baasplus.mengousms.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;

import com.baasplus.mengousms.R;

public class MainActivity extends ActionBarActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
//		Intent intent = new Intent(this, SmsService.class);
//		startService(intent);
	}

	public void setting(View v){
		Intent startSettingAvINT = new Intent(MainActivity.this,SettingsActivity.class);
		startActivity(startSettingAvINT);
	}

}
