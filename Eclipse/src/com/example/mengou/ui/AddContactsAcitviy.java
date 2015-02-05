package com.example.mengou.ui;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.easemob.chat.EMContactManager;
import com.easemob.exceptions.EaseMobException;
import com.example.mengou.R;

public class AddContactsAcitviy extends ActionBarActivity {

	private EditText etSearch;
	private Button btSearch;
	private LinearLayout searchedUserLayout;
	private String addName;
	private TextView tvName;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_addcontacts);
		
		etSearch = (EditText) findViewById(R.id.et_search);
		btSearch = (Button) findViewById(R.id.btn_search);
		searchedUserLayout = (LinearLayout) findViewById(R.id.ll_user);
		tvName = (TextView) findViewById(R.id.name);
		
	}
	
	/**
	 * 搜索联系人
	 * @param v
	 */
	public void searchContacts(View v){
		addName = etSearch.getText().toString();
		if (TextUtils.isEmpty(addName)) {
			Toast.makeText(AddContactsAcitviy.this, "请输入用户名", Toast.LENGTH_SHORT).show();
			return;
		}
		
		searchedUserLayout.setVisibility(View.VISIBLE);
		tvName.setText(addName);
	}
	/**
	 * 添加联系人
	 * @param v
	 */
	public void addContact(View v){
		
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				try {
					EMContactManager.getInstance().addContact(addName, "我是你哥！");
					runOnUiThread(new Runnable() {
						public void run() {
							Toast.makeText(AddContactsAcitviy.this, "请求添加成功，等待对方验证！", Toast.LENGTH_LONG).show();
						}
					});
				} catch (EaseMobException e) {
					e.printStackTrace();
					runOnUiThread(new Runnable() {
						public void run() {
							
							Toast.makeText(AddContactsAcitviy.this, "添加失败！", Toast.LENGTH_LONG).show();
						}
					});
				}
			}
		}).start();
		
	}
	
	

}
