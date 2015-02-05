package com.vrcoder.mengousms;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;

public class MainActivity extends ActionBarActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		Log.e("mengousms", "mengousms start...");
		Intent intent = new Intent(this, SmsService.class);
		startService(intent);
	}

}
