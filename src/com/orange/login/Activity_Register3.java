package com.orange.login;

import java.security.NoSuchAlgorithmException;

import www.orange.utils.MD5;

import com.example.acts.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class Activity_Register3 extends Activity {
	
	private String username;
	private String password;
	private EditText qq_et;
	private String qq="";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register3);
		
		Intent intent=getIntent();
		username=intent.getStringExtra("username");
		password=intent.getStringExtra("password");
		
		qq_et=(EditText)findViewById(R.id.qq_et);
	}
	
	public void returnn(View view)
	{
		Intent intent = new Intent(Activity_Register3.this, Activity_Register1.class);
		startActivity(intent);
		overridePendingTransition(R.anim.zoomin, R.anim.zoomout);
	}
	
	public void next(View view)
	{
		qq = qq_et.getText().toString().trim();
		if ("".equals(qq)||qq==null) {
			Toast.makeText(getApplicationContext(), "您还没有输入qq账号！", Toast.LENGTH_SHORT).show();
			return;
		}
		
		Intent intent = new Intent(Activity_Register3.this, Activity_Register2.class);
		intent.putExtra("username", username);
		intent.putExtra("password", password);
		intent.putExtra("qq", qq);
		startActivity(intent);
		overridePendingTransition(R.anim.zoomin, R.anim.zoomout);
	}

}
