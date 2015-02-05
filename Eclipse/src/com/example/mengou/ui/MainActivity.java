package com.example.mengou.ui;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.easemob.chat.EMChat;
import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMMessage;
import com.easemob.chat.ImageMessageBody;
import com.easemob.chat.LocationMessageBody;
import com.easemob.chat.TextMessageBody;
import com.easemob.chat.VoiceMessageBody;
import com.example.mengou.R;

public class MainActivity extends ActionBarActivity implements OnClickListener {
	private EditText tvMsg;
	private TextView tvReceivedMsg;
	private Button btLogout;
	private NewMessageBroadcastReceiver msgReceiver;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		tvMsg = (EditText) findViewById(R.id.et_msg);
		tvReceivedMsg = (TextView) findViewById(R.id.tv_receive_msg);
		btLogout = (Button) findViewById(R.id.btn_logout);

		// 注册message receiver 接收消息
		msgReceiver = new NewMessageBroadcastReceiver();
		IntentFilter intentFilter = new IntentFilter(EMChatManager.getInstance().getNewMessageBroadcastAction());
		registerReceiver(msgReceiver, intentFilter);
		btLogout.setOnClickListener(this);

		// app初始化完毕
		EMChat.getInstance().setAppInited();

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	protected void onResume() {
		super.onResume();

	}

	@Override
	protected void onPause() {
		super.onPause();
	}

	@Override
	public void onDestroy() {
		// 注销接收聊天消息的message receiver
		if (msgReceiver != null) {
			try {
				unregisterReceiver(msgReceiver);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		super.onDestroy();
	}

	/**
	 * 发送消息
	 * 
	 * @param view
	 */
	public void onSendTxtMsg(View view) {
		Log.e("mengou", "发送按钮被点击了..");
		EMMessage msg = EMMessage.createSendMessage(EMMessage.Type.TXT);
		msg.setReceipt("mengou");
		TextMessageBody body = new TextMessageBody(tvMsg.getText().toString());
		msg.addBody(body);

		// 下面的code 展示了如何添加扩展属性
		msg.setAttribute("extStringAttr", "String Test Value");
		// msg.setAttribute("extBoolTrue", true);
		// msg.setAttribute("extBoolFalse", false);
		// msg.setAttribute("extIntAttr", 100);

		// send out msg
		try {
			EMChatManager.getInstance().sendMessage(msg);
			Log.e("mengou", "消息发送成功:" + msg.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/**
	 * 接收消息的BroadcastReceiver
	 * 
	 */
	private class NewMessageBroadcastReceiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			String msgId = intent.getStringExtra("msgid"); // 消息id
			Log.e("mengou", "接受到信息...");
			// 从SDK 根据消息ID 可以获得消息对象
			EMMessage message = EMChatManager.getInstance().getMessage(msgId);
			Log.d("main", "new message id:" + msgId + " from:" + message.getFrom() + " type:" + message.getType()
					+ " body:" + message.getBody());
			switch (message.getType()) {
			case TXT:
				TextMessageBody txtBody = (TextMessageBody) message.getBody();
				tvReceivedMsg.append("text message from:" + message.getFrom() + " text: #" + txtBody.getMessage()
						+ " \n\r");
				Log.e("mengou", "消息为：" + txtBody.getMessage());
				break;
			case IMAGE:
				ImageMessageBody imgBody = (ImageMessageBody) message.getBody();
				tvReceivedMsg.append("img message from:" + message.getFrom() + " thumbnail:"
						+ imgBody.getThumbnailUrl() + " remoteurl:" + imgBody.getRemoteUrl() + " \n\r");
				break;
			case VOICE:
				VoiceMessageBody voiceBody = (VoiceMessageBody) message.getBody();
				tvReceivedMsg.append("voice message from:" + message.getFrom() + " length:" + voiceBody.getLength()
						+ " remoteurl:" + voiceBody.getRemoteUrl() + " \n\r");
				break;
			case LOCATION:
				LocationMessageBody locationBody = (LocationMessageBody) message.getBody();
				tvReceivedMsg.append("location message from:" + message.getFrom() + " address:"
						+ locationBody.getAddress() + " \n\r");
				break;
			}
		}
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.btn_logout:
			EMChatManager.getInstance().logout();
			startActivity(new Intent(MainActivity.this, LoginActivity.class));
			MainActivity.this.finish();
			break;
		default:
			break;
		}
	}
	
	public void addContacts(View v){
		try {
			startActivity(new Intent(MainActivity.this,AddContactsAcitviy.class));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		MainActivity.this.finish();
	}

}
