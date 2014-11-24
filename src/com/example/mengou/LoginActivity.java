package com.example.mengou;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
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

public class LoginActivity extends ActionBarActivity {

	private EditText accountET;
	private EditText pwdET;
	private Button login;
	private Button register;
	private ProgressDialog progressDialog;
	private String account;
	private String pwd;

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
		register = (Button) findViewById(R.id.btn_register);
		account = accountET.getText().toString().trim();
		pwd = pwdET.getText().toString().trim();

		// 登陆
		login.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				showLoginProgressDialog();
				// 登录到聊天服务器
				EMChatManager.getInstance().login(account, pwd,
						new EMCallBack() {

							@Override
							public void onError(int arg0, final String errorMsg) {
								runOnUiThread(new Runnable() {
									public void run() {
										closeLoginProgressDialog();
										Toast.makeText(LoginActivity.this, "登录聊天服务器失败：" + errorMsg, Toast.LENGTH_SHORT)
												.show();
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
										startActivity(new Intent(LoginActivity.this, LoginActivity.class));
										Toast.makeText(LoginActivity.this, "登录聊天服务器成功", Toast.LENGTH_SHORT).show();
										finish();
									}
								});

							}
						});
			}
		});
		// 注册
		register.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				CreateAccountTask task = new CreateAccountTask();
				task.execute(account, pwd);

			}
		});
	}

	private class CreateAccountTask extends AsyncTask<String, Void, String> {
		protected String doInBackground(String... args) {
			String userid = args[0];
			String pwd = args[1];
			try {
				EMChatManager.getInstance().createAccountOnServer(userid, pwd);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return userid;
		}
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
