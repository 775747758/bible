package com.orange.login;

import java.security.NoSuchAlgorithmException;
import java.util.Timer;
import java.util.TimerTask;

import www.orange.utils.MD5;
import www.orange.utils.MyProgressPopUpWindow;

import com.example.acts.GuideActivity;
import com.example.acts.MainActivity;
import com.example.acts.R;
import com.orange.net.UpLoadUserInfo;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class Activity_Register1 extends Activity{
	
	private EditText usernameEditText;
	private EditText passwordEditText;
	private String username;
	private String password;
	private ProgressDialog reDialog;
	private UpLoadUserInfo upload;
	private String from;
	private AlertDialog dialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register1);
		
		Intent intent=getIntent();
		if(intent.getStringExtra("from")!=null)
		{
			from=intent.getStringExtra("from");
		}
		
		usernameEditText = (EditText) findViewById(R.id.username);
		passwordEditText = (EditText) findViewById(R.id.password);
		
		
		
		
	}
	
	public void returnn(View view)
	{
		/*Intent intent = new Intent(Activity_Register1.this, Activity_Register2.class);
		intent.putExtra("username", username);
		intent.putExtra("password", password);
		startActivity(intent);
		overridePendingTransition(R.anim.zoomin, R.anim.zoomout);*/
	}
	
	public void next(View view)
	{
		username = usernameEditText.getText().toString().trim();
		password = passwordEditText.getText().toString().trim();
		if (username.length() < 6 || username.length() > 16) {
			Toast.makeText(getApplicationContext(), "您输入的用户名不符合规范，请重新输入！", Toast.LENGTH_SHORT).show();
			usernameEditText.setText("");
			return;
		}
		if (password.length() < 6 || password.length() > 16) {
			Toast.makeText(getApplicationContext(), "您输入的密码不符合规范，请重新输入！", Toast.LENGTH_SHORT).show();
			passwordEditText.setText("");
			return;
		}
		
		Intent intent = new Intent(Activity_Register1.this, Activity_Register3.class);
		intent.putExtra("username", username);
		intent.putExtra("password", password);
		
		String usernameMD5="";
		try {
			usernameMD5 = MD5.getMD5(username);
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		dialog=new MyProgressPopUpWindow(Activity_Register1.this,"正在查询中...").createADialog();
		
		upload=new UpLoadUserInfo(this, dialog);
		upload.isExist(intent,usernameMD5,Activity_Register1.this);
		
		overridePendingTransition(R.anim.zoomin, R.anim.zoomout);
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK)
		{
			if("GuideActivity".equals("from"))
			{
				Intent intent = new Intent(Activity_Register1.this,
						MainActivity.class);
				startActivity(intent);
				finish();
			}
			else
			{
				finish();
			}
		}
		return true;
	}

}
