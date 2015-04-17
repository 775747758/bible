package com.orange.test;

import java.util.HashMap;
import java.util.Map;

import com.example.acts.Activity_Option;
import com.example.acts.DatabaseHelper;
import com.example.acts.MainActivity;
import com.example.acts.R;

import com.orange.read.Activity_SelectVolume;
import com.orange.recite.Activity_ReciteMain;
import com.orange.service.LockScreenService;
import com.orange.test.Activity_BibleReview.MyHandler2;
import com.orange.test.Activity_BibleReview.MyThread;
import com.orange.test.Activity_BibleReview.myHandler;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class Activity_BibleTestMain extends Activity {
	private int DATA_FINISH = 10000;
	private RelativeLayout top_layout;
	private TextView grade;
	protected String rangeString;
	public Thread thread;
	private myHandler myHandler;
	private int allCount=0;
	private int fluentCount=0;
	private int strengthenCount=0;
	private int[] sectionCount={allCount,fluentCount,strengthenCount};
	private AlertDialog fluentAlert;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_bibletestmain1);
		
		
		
		new Thread(new Runnable() {

			@Override
			public void run() {
				readFromfile();

			}
		}).start();

		top_layout = (RelativeLayout) findViewById(R.id.top_layout);

		int winHeight = getWindowManager().getDefaultDisplay().getHeight();
		LinearLayout.LayoutParams ll_params = new LinearLayout.LayoutParams(
				LayoutParams.FILL_PARENT, winHeight * 2 / 3);
		top_layout.setLayoutParams(ll_params);

		grade = (TextView) findViewById(R.id.grade);

		SharedPreferences sp = getSharedPreferences("test_config",
				Context.MODE_PRIVATE);
		grade.setText(sp.getInt("test_maxgrade", 0) + "");
		
		

	}

	public void bibleknowledge(View view) {
		Intent intent = new Intent(Activity_BibleTestMain.this,
				Activity_BibleKnowledge.class);
		startActivity(intent);
		overridePendingTransition(R.anim.zoomin, R.anim.zoomout);
	}
	
	public void rank(View view) {
		Intent intent = new Intent(Activity_BibleTestMain.this,
				Activity_BibleTestRank.class);
		startActivity(intent);
		overridePendingTransition(R.anim.zoomin, R.anim.zoomout);
	}
	

	public void review(View view) {
		WindowManager manager = getWindowManager();
		Display display = manager.getDefaultDisplay();
		int width = display.getWidth();
		int height = display.getHeight();

		
		AlertDialog.Builder builder = new AlertDialog.Builder(
				Activity_BibleTestMain.this, R.style.Dialog);
		builder.setInverseBackgroundForced(true);
		LayoutInflater inflater = LayoutInflater.from(getApplicationContext());
		View view1 = inflater.inflate(R.layout.dialog_biblerange, null);
		// builder.setView(view1);
		TextView all_tv = (TextView) view1.findViewById(R.id.all_tv);
		TextView fluent_tv = (TextView) view1.findViewById(R.id.fluent_tv);
		TextView strengthen_tv = (TextView) view1
				.findViewById(R.id.strengthen_tv);
		TextView recommend_tv = (TextView) view1
				.findViewById(R.id.recommend_tv);

		all_tv.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				
				if (sectionCount[0] <= 4) {
					Toast.makeText(getApplicationContext(),
							"您在背圣经模块的“全部”栏目少于4节经文！还不能测试！建议先选择推荐！",
							Toast.LENGTH_SHORT).show();
				} else {
					fluentAlert.dismiss();
					Intent intent = new Intent(Activity_BibleTestMain.this,
							Activity_BibleReview.class);
					rangeString = "all";
					intent.putExtra("range", "all");
					startActivity(intent);
					overridePendingTransition(R.anim.zoomin, R.anim.zoomout);
				}
			}
		});
		fluent_tv.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (sectionCount[1] <= 4) {
					Toast.makeText(getApplicationContext(),
							"您在背圣经模块的“流利”栏目少于4节经文！还不能测试！建议先选择推荐！",
							Toast.LENGTH_SHORT).show();
				} else {
					fluentAlert.dismiss();
					Intent intent = new Intent(Activity_BibleTestMain.this,
							Activity_BibleReview.class);
					rangeString = "fluent";
					intent.putExtra("range", "fluent");
					startActivity(intent);
					overridePendingTransition(R.anim.zoomin, R.anim.zoomout);
				}

			}
		});
		strengthen_tv.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (sectionCount[2] <= 4) {
					Toast.makeText(getApplicationContext(),
							"您在背圣经模块的“加强”栏目少于4节经文！还不能测试！建议先选择推荐！",
							Toast.LENGTH_SHORT).show();
				} else {
					fluentAlert.dismiss();
					Intent intent = new Intent(Activity_BibleTestMain.this,
							Activity_BibleReview.class);
					rangeString = "strengthen";
					intent.putExtra("range", "strengthen");
					startActivity(intent);
					overridePendingTransition(R.anim.zoomin, R.anim.zoomout);
				}

			}
		});
		recommend_tv.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				fluentAlert.dismiss();
				Intent intent = new Intent(Activity_BibleTestMain.this,
						Activity_BibleReview.class);
				rangeString = "recommend";
				intent.putExtra("range", "recommend");
				startActivity(intent);
				overridePendingTransition(R.anim.zoomin, R.anim.zoomout);

			}
		});

		fluentAlert = builder.create();
		fluentAlert.show();
		fluentAlert.setContentView(view1);
		fluentAlert.getWindow().setLayout(4 * width / 5, 500);
		fluentAlert.setTitle("测试");

	}

	private void readFromfile() {

		DatabaseHelper dbHelper = new DatabaseHelper(
				Activity_BibleTestMain.this, "bible.db");
		SQLiteDatabase db = dbHelper.getReadableDatabase();
		String sql = "";
		Cursor cursor=null;
		for(int i=0;i<3;i++)
		{
			if(i==0)
			{
				sql = "select distinct id from holybible where secontFlag in (1,2,3)";
			}
			if(i==1)
			{
				sql = "select distinct id from holybible where secontFlag=3";
			}
			if(i==2)
			{
				sql = "select distinct id from holybible where secontFlag=2";
			}
			
			cursor = db.rawQuery(sql, null);
			sectionCount[i] = cursor.getCount();
		}
		
		cursor.close();

	}
	
	public void back(View view) {
		Intent intent = new Intent(Activity_BibleTestMain.this, MainActivity.class);
		intent.putExtra("from",0);
		startActivity(intent);
		finish();
		overridePendingTransition(R.anim.zoomin, R.anim.zoomout);
	}

}
