package com.example.mengou.ui;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.easemob.EMCallBack;
import com.easemob.chat.EMChatManager;
import com.easemob.exceptions.EaseMobException;
import com.example.mengou.R;
import com.example.mengou.R.id;
import com.example.mengou.R.layout;

public class LoginActivity extends ActionBarActivity {

	private EditText accountET;
	private EditText pwdET;
	private Button login;
	private Button registerBT;
	private ProgressDialog progressDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);

		Log.e("mengou", "mengou");

		/**************************************************************
		 * Intent intent = new Intent(this, Myservice.class); *
		 * startService(intent); *
		 ***************************************************************/

		accountET = (EditText) findViewById(R.id.et_account);
		pwdET = (EditText) findViewById(R.id.et_pwd);
		login = (Button) findViewById(R.id.btn_login);
		registerBT = (Button) findViewById(R.id.btn_register);
		// 登陆
		login.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				showLoginProgressDialog();
				// 登录到聊天服务器
				EMChatManager.getInstance().login(accountET.getText().toString().trim(), pwdET.getText().toString().trim(), new EMCallBack() {

					@Override
					public void onError(int arg0, final String errorMsg) {
						runOnUiThread(new Runnable() {
							public void run() {
								closeLoginProgressDialog();
								Toast.makeText(LoginActivity.this, "登录聊天服务器失败：" + errorMsg, Toast.LENGTH_SHORT).show();
							}
						});
					}

					@Override
					public void onProgress(int arg0, String arg1) {
					}

					@Override
					public void onSuccess() {
						runOnUiThread(new Runnable() {
							public void run() {
								closeLoginProgressDialog();
								startActivity(new Intent(LoginActivity.this, MainActivity.class));
								Toast.makeText(LoginActivity.this, "登录聊天服务器成功", Toast.LENGTH_SHORT).show();
								finish();
							}
						});

					}
				});
			}
		});
		// 注册
		registerBT.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				register();
			}
		});
	}

	private void register() {
		new Thread(new Runnable() {

			@Override
			public void run() {
				try {
					EMChatManager.getInstance().createAccountOnServer(accountET.getText().toString().trim(), pwdET.getText().toString().trim());
//					Toast.makeText(getApplicationContext(), "注册成功", 0).show();
					System.out.println("注册成功！");
				} catch (EaseMobException e) {
					e.printStackTrace();
				}

			}
		}).start();
	}


	/**
	 * 显示提示dialog
	 */
	private void showLoginProgressDialog() {
		if (progressDialog == null) {
			progressDialog = new ProgressDialog(this);
			progressDialog.setMessage("正在登陆...");
			progressDialog.setCanceledOnTouchOutside(false);
		}
		progressDialog.show();
	}

	/**
	 * 关闭提示dialog
	 */
	private void closeLoginProgressDialog() {
		if (progressDialog != null && progressDialog.isShowing()) {
			progressDialog.dismiss();
		}
	}

}
