package com.orange.church;

import com.example.acts.MainActivity;
import com.example.acts.R;
import com.example.acts.R.layout;
import com.orange.login.Activity_Login;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnCreateContextMenuListener;
import android.widget.TextView;
import android.widget.Toast;

public class Church_Activity extends Activity {

	private boolean isLogin;
	private TextView church_type;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_church1);
		
	
		
		SharedPreferences sp = getSharedPreferences("UserInfo", Context.MODE_PRIVATE);
		
		isLogin=sp.getBoolean("isLogin", false);
	}
	
	public void createChurch(View view)
	{
		if(isLogin)
		{
			Intent intent = new Intent(Church_Activity.this, Activity_CreateChurch0.class);
			startActivity(intent);
			overridePendingTransition(R.anim.zoomin, R.anim.zoomout);
		}
		else
		{
			Toast.makeText(getApplicationContext(), "您还没有登录，请先登录！", Toast.LENGTH_SHORT).show();
		}
	}
	public void joinChurch(View view)
	{
		if(isLogin)
		{
			Intent intent = new Intent(Church_Activity.this, Activity_JoinChurch.class);
			startActivity(intent);
			overridePendingTransition(R.anim.zoomin, R.anim.zoomout);
		}
		else
		{
			Toast.makeText(getApplicationContext(), "您还没有登录，请先登录！", Toast.LENGTH_SHORT).show();
		}
	}
	
	public void brief(View view)
	{
		Intent intent = new Intent(Church_Activity.this, Activity_Brief.class);
		startActivity(intent);
		overridePendingTransition(R.anim.zoomin, R.anim.zoomout);
		
	}	
}
