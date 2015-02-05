package com.example.mengou;

import android.app.Application;
import android.content.Context;

public class MGApplication extends Application {

	public static Context appContext;
	@Override
	public void onCreate() {
		super.onCreate();
		appContext = this;
		EMChat.getInstance().setDebugMode(true);
		EMChat.getInstance().init(appContext);
	}

}
