package com.orange.church;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.example.acts.MainActivity;
import com.example.acts.R;
import com.orange.net.UpLoadChurchNotice;
import com.orange.net.UpLoadChurchTest;
import com.orange.test.Activity_BibleTestMain;
import com.orange.user.Activity_UserInfo;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

public class Activity_AdminTestResult extends Activity {
	
	List<HashMap<String, String>> data=new ArrayList<HashMap<String,String>>();
	private ListView test;
	private ProgressDialog reDialog;
	private TextView title_bar_name;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_admintestresult);
		test=(ListView)findViewById(R.id.test);
		title_bar_name=(TextView)findViewById(R.id.title_bar_name);
		Intent intent=getIntent();
		String name=intent.getStringExtra("name");
		String result=intent.getStringExtra("result");
		title_bar_name.setText(name);
		
		reDialog = new ProgressDialog(this);
		reDialog.setMessage("正在查询中...");
		reDialog.show();
		UpLoadChurchTest upLoadChurchTest=new UpLoadChurchTest(this, reDialog);
		upLoadChurchTest.getTestResult(test, result);

	}
	
	public void back(View view) {
		Intent intent = new Intent(Activity_AdminTestResult.this, Activity_AdminChurchTest.class);
		startActivity(intent);
		finish();
		overridePendingTransition(R.anim.zoomin, R.anim.zoomout);
	}
	
	

}
