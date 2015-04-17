package com.orange.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;

import com.example.acts.DatabaseHelper;
import com.example.acts.R;
import com.orange.test.Activity_BibleReview;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

public class TimeListenerService extends Service {

	private ArrayList<Map<String, String>> data = new ArrayList<Map<String, String>>();

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		Log.i("onCreate", "onCreate");
		readFromfile();
		// registerReceiver(Intent.ACTION_TIME_TICK, null)
		IntentFilter javaFilter = new IntentFilter();
		javaFilter.addAction(Intent.ACTION_DATE_CHANGED);
		javaFilter.setPriority(Integer.MAX_VALUE);
		registerReceiver(receiver, javaFilter);

	}

	BroadcastReceiver receiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {

			SharedPreferences sp = getSharedPreferences("config", Context.MODE_PRIVATE);
			Editor editor = sp.edit();

			if (data.size() > 1) {
				editor.putString("taday_position", createARand(0, data.size() - 1) + "");
				editor.commit();
			}
			Date date = new Date();
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

			editor.putString("Time", dateFormat.format(date));
			editor.commit();

		}

	};

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		Log.i("onDestroy", "onDestroy");
		// Intent intent=new Intent(this,TimeListenerService2.class);
		// startService(intent);

		AlarmManager am = (AlarmManager) this.getSystemService(Context.ALARM_SERVICE);
		Date d = new Date();
		Intent intent = new Intent(getApplicationContext(), TimeListenerService2.class); // 可以通过构建Intent启动任何的东东，activity,service等
		PendingIntent pi = PendingIntent.getService(getApplicationContext(), 1, intent, PendingIntent.FLAG_UPDATE_CURRENT);// getService用来启动服务，也可以getActivity启动activity等
		am.set(AlarmManager.RTC_WAKEUP, (d.getTime() + 5000), pi); // 5秒后重新启动
	}

	// 当service因内存不足被kill，当内存又有的时候，service又被重新创建
	@SuppressWarnings("deprecation")
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// TODO Auto-generated method stub
		Log.i("onStartCommand", "onStartCommand");
		flags = START_STICKY;
		return super.onStartCommand(intent, flags, startId);

	}

	private void readFromfile() {

		DatabaseHelper dbHelper = new DatabaseHelper(TimeListenerService.this,"bible.db");
		SQLiteDatabase db = dbHelper.getReadableDatabase();
		db.execSQL("create table  if not exists  recitebible(volume varchar(20),chapter varchar(20),content varchar(300),PRIMARY KEY(volume,chapter))");
		Cursor cursor = db.query("recitebible", new String[] { "volume", "chapter", "content" }, null, null, null, null, null);
		String Chapter = new String();
		String Content = new String();
		if (cursor.getCount() != 0) {
			while (cursor.moveToNext()) {
				Chapter = cursor.getString(cursor.getColumnIndex("volume")) + "  " + cursor.getString(cursor.getColumnIndex("chapter"));
				Content = cursor.getString(cursor.getColumnIndex("content"));
				Map<String, String> map = new HashMap<String, String>();
				map.put("Content", Content);
				map.put("Chapter", Chapter);
				data.add(map);
			}
		}

	}

	public int createARand(int begin, int end) {
		Random rnd = new Random();
		int number = begin + rnd.nextInt(end - begin + 1);
		return number;
	}

}
