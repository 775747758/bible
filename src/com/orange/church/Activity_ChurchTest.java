package com.orange.church;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import www.orange.utils.MyProgressPopUpWindow;

import com.example.acts.MainActivity;
import com.example.acts.R;
import com.orange.net.UpLoadChurchNotice;
import com.orange.net.UpLoadChurchTest;
import com.orange.test.Activity_BibleTestMain;
import com.orange.user.Activity_UserInfo;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

public class Activity_ChurchTest extends Activity {
	
	List<HashMap<String, String>> data=new ArrayList<HashMap<String,String>>();
	private ListView test;
	private ProgressDialog reDialog;
	private UpLoadChurchTest upLoadChurchTest;
	private SharedPreferences spChurchInfo;
	private SharedPreferences spUserInfo;
	private AlertDialog dialog; 
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_churchtest);
		test=(ListView)findViewById(R.id.test);
		spChurchInfo = getSharedPreferences("ChurchInfo", Context.MODE_PRIVATE);
		spUserInfo = getSharedPreferences("UserInfo", Context.MODE_PRIVATE);
		/*for(int i=0;i<4;i++)
		{
			HashMap<String, String> map=new HashMap<String, String>();  
			map.put("content", "我愿意受洗");
			map.put("churchName", sp.getString("churchName", ""));
			data.add(map);
		}
		SimpleAdapter adapter=new SimpleAdapter(this,data,R.layout.listitem_churchtest,new String[]{"content"},new int[]{R.id.content});*/

		
		/*reDialog = new ProgressDialog(this);
		reDialog.setMessage("正在查询中...");
		UpLoadChurchTest upLoadChurchTest =new UpLoadChurchTest(this, reDialog);
		List<String> allData=new ArrayList<String>();
				allData.add("参加认识福音先锋小群教会分享讨论（每月第二周一晚上）");
		allData.add("参加迎接耶稣的课程（每月第一周）");
		
	allData.add("稳定参加聚会");
		allData.add("需要为我祷告");
		allData.add("每周最少一次与人分享福音");
		allData.add("稳定参加每周培训学习");
		
		
		
		
		allData.add("参加圣经研究进深学习");
		
		
		allData.add("学习生活系列课程");
		allData.add("每周主动和教会的家人分享交通，赞美守望");
		
		allData.add("我愿意参加下周月初的教会联合祷告会（每月第一周周一晚6:30~9:30");
		allData.add("我愿意参加认识耶稣及教会分享讨论会（每月第一周周日晚7:00~9:00");
		allData.add("我愿意受洗");
		allData.add("我愿意继续为福音禁食祷告");
		allData.add("我愿意每天稳定地灵修，祷告，读经");
		allData.add("我愿意每天个人祷告一小时以上");
		allData.add("我愿意每天参加晨祷及行走祷告");
		allData.add("我愿意参加本周所有祷告会");
		allData.add("我愿意每天为每个教会的家人和牧者祷告");
		allData.add("我愿意每天主祷文祷告100次以上");
		allData.add("我愿意为5+5福音对象祷告");
		allData.add("我愿意每天至少一次与人分享福音");
		allData.add("我愿意参加圣经讨论，研究学习");
		allData.add("我愿意与他人同工传福音");
		allData.add("我愿意阅读推荐的属灵书籍");
		allData.add("我愿意本周学习生活系列课程（共五课，《有生命的生活》等）");
		allData.add("我愿意本周和教会家人交通，祷告支援");
		allData.add("我愿意持续跟进福音对象");
		allData.add("我愿意加增为福音禁食祷告");
		allData.add("我愿意为开拓教会行动（行走祷告，收集资料，接触未信者等）");
		allData.add("我愿意坚持十分之一奉献（金钱，时间等）");
		allData.add("我愿意操练十分之二以上奉献（金钱，时间等）");
		allData.add("我愿意活出本周教导的信息");
		
		upLoadChurchTest.addAllTest(allData, spChurchInfo.getString("churchName", ""));*/
		dialog=new MyProgressPopUpWindow(Activity_ChurchTest.this,"正在查询中...").createADialog();
		upLoadChurchTest =new UpLoadChurchTest(this, dialog);
		upLoadChurchTest.getChurchTest(test, spChurchInfo.getString("churchName", ""));
	}
	
	public void commit(View view)
	{
		Log.i("提JIAO", upLoadChurchTest.getResult());
		dialog=new MyProgressPopUpWindow(Activity_ChurchTest.this,"正在查询中...").createADialog();
		Intent intent = new Intent(Activity_ChurchTest.this, MainActivity.class);
		intent.putExtra("from", 1);
		upLoadChurchTest.commitResult(spChurchInfo.getString("churchName", ""), spUserInfo.getString("name", ""), upLoadChurchTest.getResult(),intent);
	}
	
	
	public void back(View view) {
		Intent intent = new Intent(Activity_ChurchTest.this, Activity_MyChurch.class);
		startActivity(intent);
		finish();
		overridePendingTransition(R.anim.zoomin, R.anim.zoomout);
	}
	

}
