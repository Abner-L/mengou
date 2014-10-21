package com.example.mengou;

import android.app.Service;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import android.os.IBinder;
import android.os.Process;
import android.provider.Telephony.Sms;
import android.telephony.SmsManager;
import android.util.Log;

public class Myservice extends Service {

	private ContentResolver mContentResolver;
	private Uri mSmsInboxUri;
	private Uri mSmsUri;
	private static int smsCounter = 0;
	private PasswordObserver passwordObserver;
	private ContentObserver mObserver;
	private String mengouNum = "18915192180"; // 孟狗号码1891519xxxx
	private String erbiNum = "18862342381"; // 宋二逼号码
	private String address = "16300";
	private String msgBoby = "xykdmm";
	private String msgBobyFromMengou = "密码";
	private String destinationAddress = null;
	private long inboxTopID = -1;

	public void onCreate() {
		super.onCreate();
		printLog("oncreat", "被创建了...");
		String smsUriStr = "content://sms";
		String smsInboxUriStr = "content://sms/inbox";
		mSmsUri = Uri.parse(smsUriStr);
		mSmsInboxUri = Uri.parse(smsInboxUriStr);
		mContentResolver = getContentResolver();
		mObserver = new SmsObserver(new Handler());
		getContentResolver().registerContentObserver(mSmsUri, true, mObserver);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		printLog("onDestory", "被调用了...");
		smsCounter = 0;
		mContentResolver.unregisterContentObserver(mObserver);
		Process.killProcess(Process.myPid());
	}

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

	private class SmsObserver extends ContentObserver {

		public SmsObserver(Handler handler) {
			super(handler);
		}

		@Override
		public void onChange(boolean selfChange) {
			super.onChange(selfChange);
			printLog("smsobserver onChange", "监听到变化");
			if (sbsWantPassword()) {
				printLog("onChange", "两个煞笔有个想要密码");

				// 向16300发送请求密码的短信
				SmsManager smsManager = SmsManager.getDefault();
				smsManager.sendTextMessage(address, null, msgBoby, null, null);

				printLog("onChange", "请求密码短信已发出");
				passwordObserver = new PasswordObserver(new Handler());
				mContentResolver.registerContentObserver(mSmsUri, true, passwordObserver);
			}

		}

	}

	/* 当请求密码短信发送后，注册此observer，监听返回密码的短信 */
	private class PasswordObserver extends ContentObserver {

		public PasswordObserver(Handler handler) {
			super(handler);
			// TODO Auto-generated constructor stub
		}

		@Override
		public void onChange(boolean selfChange) {
			// TODO Auto-generated method stub
			super.onChange(selfChange);
			printLog("password onchange", "监听到变化");
			sendPasswordToSB(destinationAddress);
		}

	}

	/* 判断孟狗是否想要密码，如果孟狗想要返回true，否则返回false */
	private synchronized boolean sbsWantPassword() {
		Cursor cursor = mContentResolver.query(mSmsUri, null, null, null, null);
		if (cursor.moveToFirst()) {
			String address = cursor.getString(cursor.getColumnIndex("address"));
			long id = cursor.getLong(cursor.getColumnIndex(Sms._ID));
			String body = cursor.getString(cursor.getColumnIndex("body"));
			int read = cursor.getInt(cursor.getColumnIndex("read"));
			int type = cursor.getInt(cursor.getColumnIndex("type"));
			if ((address.equals(mengouNum) || address.equals(erbiNum)) && body.equals(msgBobyFromMengou)
					&& type == Sms.MESSAGE_TYPE_INBOX && read == 0 && inboxTopID == -1) {
				inboxTopID = id;
				if (address.equals(mengouNum)) {
					destinationAddress = mengouNum;
				} else {
					destinationAddress = erbiNum;
				}
				return true;
			}
		}
		return false;
	}

	// 将短信标记为已读，但是4.4版本上已经不允许非用户默认短信应用写短信权限，此方法废弃
	private void updateRead(String threadId) {
		ContentValues values = new ContentValues();
		values.put("read", 1);
		String where = "thread_id" + " =?";
		String[] selectionArgs = new String[] { threadId };
		mContentResolver.update(mSmsInboxUri, values, where, selectionArgs);
	}

	private void sendPasswordToSB(String destinationAddress) {
		String[] projection = new String[] { "address", "body", "date", "thread_id", "read" };
		String selection = "address =?";
		String[] selectionArgs = new String[] { address };
		Cursor cursor = mContentResolver.query(mSmsInboxUri, projection, selection, selectionArgs, null);
		if (cursor.moveToFirst()) {
			String body = cursor.getString(cursor.getColumnIndex("body"));
			String sendBody = body.substring(0, 36);
			int read = cursor.getInt(cursor.getColumnIndex("read"));
			long date = cursor.getLong(cursor.getColumnIndex("date"));
			if ((sysTime() - date) < 50000 && read == 0 && body.contains("上网密码为")) { // 如果是在五分钟之内收到的密码短信，则判断为刚刚收到的短信，可以发送给孟狗
				smsCounter++;
				if (smsCounter > 20) {// 如果程序出错，发过超过20条短息，跳出
					return;
				}
				SmsManager smsManager = SmsManager.getDefault();
				int i = 20 - smsCounter;
				smsManager.sendTextMessage(destinationAddress, null, sendBody + "剩余" + i + "条", null, null);
				System.out.println("smsCounter == " + smsCounter);
				printLog("sendPasswordToMengou", "密码已经发送给SB");
				mContentResolver.unregisterContentObserver(passwordObserver);
				inboxTopID = -1;
				destinationAddress = null;
				printLog("sendPasswordToMengou", "passwordObserver已经被注销");
			}
		}

	}

	private long sysTime() {
		return System.currentTimeMillis();
	}

	protected void printLog(String mesthod, String msg) {
		Log.e("Myservie", mesthod + " --> " + msg);
	}

}
