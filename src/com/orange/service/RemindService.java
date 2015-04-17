package com.orange.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
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

public class RemindService extends Service {

	private ArrayList<Map<String, String>> data = new ArrayList<Map<String, String>>();
	private SharedPreferences spConfig;

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		
		AlarmManager am= (AlarmManager) getSystemService(ALARM_SERVICE);  
		spConfig =getSharedPreferences("SystemConfig", Context.MODE_PRIVATE);
		Calendar c=Calendar.getInstance();  
        c.set(2014, 12, 1, 20, 0);
       
       
       
       Intent intent = new Intent(this, remindReceiver.class);
       PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, intent, 0);
       am.setRepeating(AlarmManager.RTC_WAKEUP, spConfig.getLong("remindtime", c.getTimeInMillis()) + (10*1000), (24*60*60*1000), pendingIntent);

	}
	
	class  remindReceiver extends BroadcastReceiver
	{

		@Override
		public void onReceive(Context context, Intent intent) {
			Toast.makeText(context, "闹钟提示：时间到！", Toast.LENGTH_LONG).show();  
			Log.i("122222", "闹钟提示：时间到");
		}
		
	}

	
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


}
