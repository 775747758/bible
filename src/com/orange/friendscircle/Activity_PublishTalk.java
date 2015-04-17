package com.orange.friendscircle;

import java.util.ArrayList;

import www.orange.utils.MyProgressPopUpWindow;

import com.example.acts.MainActivity;
import com.example.acts.R;
import com.markupartist.android.widget.PullToRefreshListView;
import com.orange.church.Activity_PublishChurchNotice;
import com.orange.net.UpLoadChurchNotice;
import com.orange.net.UpLoadTalk;
import com.orange.user.Activity_DetailUserInfo;
import com.orange.user.Activity_MyTalk;
import com.orange.user.Activity_UserInfo;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

public class Activity_PublishTalk extends Activity {
	
	private EditText content_et;
	private ImageView send_iv;
	private ProgressDialog reDialog;
	private AlertDialog dialog;
	private String from="";


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_publishtalk);
		
		
		content_et=(EditText)findViewById(R.id.content_et);
		send_iv=(ImageView)findViewById(R.id.send_iv);
		
		Intent intent=getIntent();
		if(intent.getStringExtra("from")!=null)
		{
			from="Personal_Activity";
			
		}
		intent.getStringExtra("from");
		
		content_et.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				
				
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void afterTextChanged(Editable s) {
				
				
				if(s.length()==0)
				{
					send_iv.setImageDrawable(getResources().getDrawable(R.drawable.send_pressed));
					//send_iv.setClickable(false);
				}
				else
				{
					send_iv.setImageDrawable(getResources().getDrawable(R.drawable.send_default));
					//send_iv.setClickable(true);
				}
			}
		});
	}
	
	
	public void send(View view)
	{
		if(content_et.getText().toString().trim().equals(""))
		{
			Toast.makeText(getApplicationContext(), "请输入内容！", Toast.LENGTH_SHORT).show();
			return;
		}
		else if (content_et.getText().toString().length()>200)
		{
			Toast.makeText(getApplicationContext(), "请输入内容太长！", Toast.LENGTH_SHORT).show();
			return;
		}
		
		else
		{
			Intent intent = new Intent(Activity_PublishTalk.this, Activity_FriendsCircle.class);
			dialog = new MyProgressPopUpWindow(
					Activity_PublishTalk.this, "正在加载中...").createADialog();
			
			//intent=new Intent(Activity_PublishTalk.this,Activity_FriendsCircle.class);
			intent.putExtra("from", "Activity_PublishTalk");
			
			
			UpLoadTalk upLoadTalk=new UpLoadTalk(this, dialog,null,intent);
			SharedPreferences sp = getSharedPreferences("ChurchInfo", Context.MODE_PRIVATE);
			SharedPreferences spUser = getSharedPreferences("UserInfo", Context.MODE_PRIVATE);
			upLoadTalk.addTalk(sp.getString("churchName", "no"), content_et.getText().toString().trim(),spUser.getString("name", ""), spUser.getString("userName", ""), spUser.getString("portraitUri", ""),spUser.getString("city", ""), intent,Activity_PublishTalk.this);
		}
	}
	
	public void back(View view) {
		Intent intent = null;
		if(from.equals("Personal_Activity"))
		{
			intent = new Intent(Activity_PublishTalk.this, Activity_MyTalk.class);
		}
		else
		{
			intent = new Intent(Activity_PublishTalk.this, Activity_FriendsCircle.class);
		}
		
		startActivity(intent);
		finish();
		overridePendingTransition(R.anim.zoomin, R.anim.zoomout);
	}

}
