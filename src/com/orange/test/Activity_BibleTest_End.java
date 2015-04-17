package com.orange.test;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.frontia.Frontia;
import com.baidu.frontia.FrontiaData;
import com.baidu.frontia.FrontiaFile;
import com.baidu.frontia.api.FrontiaStorage;
import com.baidu.frontia.api.FrontiaStorageListener;
import com.example.acts.DatabaseHelper;
import com.example.acts.MainActivity;
import com.example.acts.R;

public class Activity_BibleTest_End extends Activity {
	private int max;
	private ArrayList<String> wrongData;
	private ArrayList<String> wrongDataIndex;
	private int testNum;
	private int trueNum;
	private TextView grade;
	private TextView num;
	private ListView lv;
	private String from;
	private Button add2mem;
	private boolean isLogin;
	private int mYear;
	private int mMonth;
	private int mDay;
	private int mHour;
	private int mMinute;
	private String nativeName;
	private String userName;
	private String portraitUri;
	private FrontiaStorage mCloudStorage;
	private FrontiaFile mFile;
	private String churchName;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_bibletest_end);
		
		boolean isInit = Frontia.init(this, "GdZNITcYcI3bhiwKx7duVbR7");
		if (!isInit) {
			Toast.makeText(this, "您的app key 错误", 3).show();
			return;
		}
		// mFile = new FrontiaFile();
		mCloudStorage = Frontia.getStorage();

		SharedPreferences spUser = getSharedPreferences("UserInfo",
				Context.MODE_PRIVATE);
		
		SharedPreferences spChurch = getSharedPreferences("ChurchInfo", Context.MODE_PRIVATE);
		churchName = spChurch.getString("churchName", "");

		isLogin = spUser.getBoolean("isLogin", false);
		
		nativeName=spUser.getString("name", "");
		userName=spUser.getString("userName", "");
		portraitUri=spUser.getString("portraitUri", "");
		

		lv = (ListView) findViewById(R.id.bibletest_lv);
		grade = (TextView) findViewById(R.id.grade);
		num = (TextView) findViewById(R.id.num);
		add2mem = (Button) findViewById(R.id.add2mem);

		Intent intent = getIntent();
		from = intent.getStringExtra("from");
		if (!from.equals("all")) {
			add2mem.setVisibility(View.GONE);
		}
		testNum = intent.getIntExtra("testNum", 0);
		trueNum = intent.getIntExtra("trueNum", 0);
		Bundle bundle = intent.getExtras();
		wrongData = bundle.getStringArrayList("wrongData");
		wrongDataIndex = bundle.getStringArrayList("wrongDataIndex");

		Log.i("wrongData", wrongData.toString());
		// data = (ArrayList<Map<String, String>>)
		// intent.getSerializableExtra("all");
		num.setText("  正确/总数: " + trueNum + "/" + testNum);
		if (testNum == 0 || trueNum == 0) {
			grade.setText(" 总分: " + 0);
			max = 0;
		} else {
			max = 100 * trueNum / testNum;
			grade.setText(" 总分: " + max);

		}
		
		if(isLogin)
		{
			submitGrade();
			Toast.makeText(getApplicationContext(),"想知道你成绩的排名吗？那就去看看排行榜吧！", Toast.LENGTH_SHORT).show();
		}
		else
		{
			Toast.makeText(getApplicationContext(),"想知道你成绩的排名吗？那就赶快去注册个账号吧！", Toast.LENGTH_SHORT).show();
		}

		lv.setAdapter(new ArrayAdapter<String>(this,
				R.layout.listitem_bibletest_end, R.id.bibletest_tv, wrongData));
		SharedPreferences spTest = getSharedPreferences("test_config",
				Context.MODE_PRIVATE);
		Editor editor = spTest.edit();
		if (spTest.getInt("test_maxgrade", max) <= max) {
			editor.putInt("test_maxgrade", max);
		}
		editor.commit();

	}



	public void add2mem(View view) {
		ContentValues values = new ContentValues();
		DatabaseHelper dbHelper = new DatabaseHelper(getApplicationContext(),
				"bible.db");
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		values.put("secontFlag", 2);
		for (int i = 0; i < wrongDataIndex.size(); i++) {
			db.update("holybible", values, "id=?",
					new String[] { wrongDataIndex.get(i) });
		}
		db.close();
		Toast.makeText(getApplicationContext(), "添加成功！请到背诵模块的强化栏目查看！",
				Toast.LENGTH_SHORT).show();
	}
	
	public void submitGrade() {
		
		final Calendar c = Calendar.getInstance();
		mYear = c.get(Calendar.YEAR);
		mMonth = c.get(Calendar.MONTH) + 1;
		mDay = c.get(Calendar.DAY_OF_MONTH);
		mHour=c.get(Calendar.HOUR_OF_DAY);
		mMinute=c.get(Calendar.MINUTE);
		final FrontiaData[] datas = new FrontiaData[1];
		datas[0] = new FrontiaData();
		datas[0].put("tableType", "bibleQuestion");
		datas[0].put("name", nativeName);
		datas[0].put("userName", userName);
		datas[0].put("portraitUri", portraitUri);
		datas[0].put("grade", max);
		datas[0].put("year", mYear+"");
		datas[0].put("month", mMonth+"");
		datas[0].put("day", mDay+"");
		datas[0].put("hour", mHour+"");
		datas[0].put("minute", mMinute+"");
		datas[0].put("churchName", churchName);
		
		mCloudStorage.insertData(datas[0],
				new FrontiaStorageListener.DataInsertListener() {

					@Override
					public void onSuccess() {
						Log.i("grade", "成功");
					}

					@Override
					public void onFailure(int errCode, String errMsg) {
						Log.i("grade", "失败");
					}

				});

	}
	
	public void back(View view) {
		Intent intent = new Intent(Activity_BibleTest_End.this,
				Activity_BibleTestMain.class);
		startActivity(intent);
		finish();
		overridePendingTransition(R.anim.test_main_righttoleft,
				R.anim.test_main_lefttoright);
	}

}
