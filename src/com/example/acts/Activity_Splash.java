package com.example.acts;

import java.util.Calendar;
import java.util.TimeZone;

import com.baidu.frontia.Frontia;
import com.baidu.frontia.api.FrontiaStorage;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.LocationData;
import com.baidu.platform.comapi.basestruct.GeoPoint;
import com.orange.receiver.AlarmReceiver;
import com.orange.service.RemindService;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.util.Log;
import android.widget.Toast;

public class Activity_Splash extends Activity {
	
	private SharedPreferences spConfig;
	private SharedPreferences sp;
	private LocationClient mLocClient;
	private MyLocationListenner myLocationListenner;
	private FrontiaStorage mCloudStorage;
	private LocationData locData;
	private Editor editor;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash);
		
		boolean isInit = Frontia.init(this.getApplicationContext(),
				"GdZNITcYcI3bhiwKx7duVbR7");
		if (!isInit) {
			Toast.makeText(getApplicationContext(), "����app key ����", 3).show();
			return;
		}

		mCloudStorage = Frontia.getStorage();
		
		spConfig = getSharedPreferences("SystemConfig", Context.MODE_PRIVATE);
		sp = getSharedPreferences("UserInfo", Context.MODE_PRIVATE);
		editor = spConfig.edit();
		init();
		
		if (spConfig.getBoolean("isInstall", false)) 
		{
			Intent main = new Intent(getApplicationContext(),
					MainActivity.class);
			startActivity(main); // �����µ�Activity��OneActivity��
			finish();
			overridePendingTransition(R.anim.zoomin, R.anim.zoomout);
		}
		else
		{
			Intent main = new Intent(getApplicationContext(),
					GuideActivity.class);
			startActivity(main); // �����µ�Activity��OneActivity��
			finish();
			overridePendingTransition(R.anim.zoomin, R.anim.zoomout);
		}
	}

	
	public void setRemind()
	{
		Intent intent = new Intent(Activity_Splash.this, AlarmReceiver.class);
		PendingIntent sender = PendingIntent.getBroadcast(Activity_Splash.this, 0, intent, 0);
		
		long firstTime = SystemClock.elapsedRealtime();	// ����֮�����ڵ�����ʱ��(����˯��ʱ��)
        long systemTime = System.currentTimeMillis();

        Calendar calendar = Calendar.getInstance();
	 	calendar.setTimeInMillis(System.currentTimeMillis());
	 	calendar.setTimeZone(TimeZone.getTimeZone("GMT+8")); // ����ʱ����Ҫ����һ�£���Ȼ����8��Сʱ��ʱ���
	 	calendar.set(Calendar.MINUTE, spConfig.getInt("minute", 0));
	 	calendar.set(Calendar.HOUR_OF_DAY, spConfig.getInt("hour", 20));
	 	calendar.set(Calendar.SECOND, 0);
	 	calendar.set(Calendar.MILLISECOND, 0);

	 	// ѡ���ÿ�춨ʱʱ��
	 	long selectTime = calendar.getTimeInMillis();	

	 	// �����ǰʱ��������õ�ʱ�䣬��ô�ʹӵڶ�����趨ʱ�俪ʼ
	 	if(systemTime > selectTime) {
	 		//Toast.makeText(Activity_Settings.this, "���õ�ʱ��С�ڵ�ǰʱ��", Toast.LENGTH_SHORT).show();
	 		calendar.add(Calendar.DAY_OF_MONTH, 1);
	 		selectTime = calendar.getTimeInMillis();
	 	}

	 	// ��������ʱ�䵽�趨ʱ���ʱ���
	 	long time = selectTime - systemTime;
 		firstTime += time;

        // ��������ע��
        AlarmManager manager = (AlarmManager)getSystemService(ALARM_SERVICE);
        manager.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP,
                        firstTime+10*1000,(24*60*60*1000), sender);
		//startService(new Intent(Activity_Splash.this, RemindService.class));
	}


	public void init()
	{
		upLoad();
		if(!spConfig.getBoolean("isAutoLogin", true))
		{
			Editor editor1=sp.edit();
			editor1.putBoolean("isLogin", false);
			editor1.putString("churchName", "");
			editor1.putString("userName", "");
			editor1.putString("password", "");
			editor1.putString("name","");
			editor1.putString("birthday", "");
			editor1.putString("gender","");
			editor1.putString("qq","");
			editor1.commit();
		}
		setRemind();
		
	}
	
	
	public class MyLocationListenner implements BDLocationListener {

		@Override
		public void onReceiveLocation(BDLocation arg0) {
			// System.out.print(arg0.getAddrStr());

			if (arg0 != null) {
				// ����ǰλ��ת���ɵ��������
				int longtitude = (int) (arg0.getLongitude() * 1000000);
				int latitude = (int) (arg0.getLatitude() * 1000000);

				GeoPoint myPoint = new GeoPoint(longtitude, latitude);
				// Toast.makeText(getApplicationContext(),
				// getLocationAddress(myPoint), 5).show();
				SharedPreferences sp = getSharedPreferences("UserInfo",
						Context.MODE_PRIVATE);
				Editor editor = sp.edit();
				editor.putInt("latitude", longtitude);
				editor.putInt("longtitude", latitude);
				// ʹ�����apiʱһ��Ҫ����option.setAddrType("all");
				if("null".equals(arg0.getProvince())||arg0.getProvince()==null)
				{
					editor.putString("location", "δʶ��");
					editor.putString("city","δʶ��");
				}
				else
				{
					editor.putString("location", arg0.getAddrStr());
					editor.putString("city",arg0.getProvince()+"  "+arg0.getCity());
				}
				editor.commit();
				
				
				
				
			}

		}

		@Override
		public void onReceivePoi(BDLocation arg0) {
			// TODO Auto-generated method stub

		}

	}

	public void upLoad() {
		mLocClient = new LocationClient(getApplicationContext());
		locData = new LocationData();
		myLocationListenner=new MyLocationListenner();
		mLocClient.registerLocationListener(myLocationListenner);
		LocationClientOption option = new LocationClientOption();
		option.setPriority(LocationClientOption.GpsFirst);
		option.setAddrType("all");
		option.setOpenGps(true);// ��gps
		// option.setCoorType("bd09ll"); // ������������
		option.setScanSpan(500);
		mLocClient.setLocOption(option);
		mLocClient.start();
	}
}
