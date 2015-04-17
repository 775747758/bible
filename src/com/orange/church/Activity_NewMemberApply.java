package com.orange.church;

import com.example.acts.MainActivity;
import com.example.acts.R;
import com.orange.net.UpLoadToten;
import com.orange.test.Activity_BibleTestMain;
import com.orange.user.Activity_UserInfo;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class Activity_NewMemberApply extends Activity{
	
	private ListView newmembers;
	private ProgressDialog reDialog;
	private String userName;
	private TextView userName_tv;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_newmemberapply);
		newmembers=(ListView)findViewById(R.id.newmembers);
		reDialog = new ProgressDialog(this);
		reDialog.setMessage("正在查询中...");
		UpLoadToten upload=new UpLoadToten(this, reDialog);
		userName_tv=new TextView(this);
		SharedPreferences sp = getSharedPreferences("ChurchInfo", Context.MODE_PRIVATE);
		String churchName=sp.getString("churchName", "");
		upload.getNewMemberData(newmembers,userName_tv,churchName);
		
		newmembers.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				
				TextView tv=(TextView)arg1.findViewById(R.id.name);
				
				Intent intent = new Intent(Activity_NewMemberApply.this, Activity_UserInfo.class);
				intent.putExtra("from", "Activity_NewMemberApply");
				Log.i("userName", userName_tv.getText().toString());
				intent.putExtra("userName",userName_tv.getText().toString());
				startActivity(intent);
				overridePendingTransition(R.anim.zoomin, R.anim.zoomout);
				
			}
		});
	}
	
	public void back(View view) {
		Intent intent = new Intent(Activity_NewMemberApply.this, Activity_ChurchMember.class);
		startActivity(intent);
		finish();
		overridePendingTransition(R.anim.zoomin, R.anim.zoomout);
	}

}
