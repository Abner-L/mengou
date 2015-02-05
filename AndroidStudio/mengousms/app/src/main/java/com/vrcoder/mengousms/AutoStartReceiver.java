package com.vrcoder.mengousms;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import android.util.Log;


/**
 * Created by abner-l on 15/2/5.
 */
public class AutoStartReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.e("mengousms", "mengousms start...");

        Intent startServiceINT = new Intent(context, SmsService.class);
        context.startService(startServiceINT);
    }
}
