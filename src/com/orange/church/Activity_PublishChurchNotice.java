package com.orange.church;

import www.orange.utils.MyProgressPopUpWindow;

import com.example.acts.MainActivity;
import com.example.acts.Main_Activity;
import com.example.acts.R;
import com.orange.net.UpLoadChurchNotice;
import com.orange.test.Activity_BibleTestMain;
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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

public class Activity_PublishChurchNotice extends Activity {
	
	private EditText content_et;
	private ImageView send_iv;
	private ProgressDialog reDialog;
	private AlertDialog dialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_publishchurchnotice);
		
		content_et=(EditText)findViewById(R.id.content_et);
		send_iv=(ImageView)findViewById(R.id.send_iv);
		
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
			Intent intent = new Intent(Activity_PublishChurchNotice.this, MainActivity.class);
			intent.putExtra("from", 1);
			dialog=new MyProgressPopUpWindow(Activity_PublishChurchNotice.this,"正在发布中...").createADialog();
			UpLoadChurchNotice upLoadChurchNotice=new UpLoadChurchNotice(this, dialog);
			SharedPreferences sp = getSharedPreferences("ChurchInfo", Context.MODE_PRIVATE);
			upLoadChurchNotice.addNotice(sp.getString("churchName", ""), content_et.getText().toString().trim(), intent);
		}
	}
	
	public void back(View view) {
		Intent intent = new Intent(Activity_PublishChurchNotice.this, Activity_MyChurch.class);
		startActivity(intent);
		finish();
		overridePendingTransition(R.anim.zoomin, R.anim.zoomout);
	}

}
