package com.example.acts;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import www.orange.utils.ClearCache;
import www.orange.utils.MyProgressPopUpWindow;
import www.orange.utils.WriteData;

import com.baidu.frontia.Frontia;
import com.baidu.frontia.api.FrontiaStorage;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.LocationData;
import com.baidu.mapapi.map.MapController;
import com.baidu.platform.comapi.basestruct.GeoPoint;
import com.orange.church.Activity_ChurchNotice;
import com.orange.login.Activity_Register1;
import com.orange.net.UpLoadChurchInfo;
import com.orange.net.UpLoadChurchMember;
import com.orange.service.TimeListenerService;

public class GuideActivity extends Activity {

	private ViewPager guideViewPager;
	private ViewPagerAdapter guideViewAdapter;
	private ArrayList<View> mViews;
	private final int images[] = { R.drawable.guide_page1,
			R.drawable.guide_page2, R.drawable.guide_page3 };
	private ImageView[] guideDots;
	private int currentIndex;
	private TextView startBtn;
	int[] contentNum = new int[] { 50, 40, 27, 36, 34, 24, 21, 4, 31, 24, 22,
			25, 29, 36, 10, 13, 10, 42, 150, 31, 12, 8, 66, 52, 5, 48, 12, 14,
			3, 9, 1, 4, 7, 3, 3, 3, 2, 14, 4, 28, 16, 24, 21, 28, 16, 16, 13,
			6, 6, 4, 4, 5, 3, 6, 4, 3, 1, 13, 5, 5, 3, 5, 1, 1, 1, 22 };
	SharedPreferences sp;
	WriteData writedata = new WriteData();
	List<Map<String, String>> data = new ArrayList<Map<String, String>>();
	// ReadData readdata;
	ImageView img;
	BufferedReader reader;
	AlphaAnimation alphaanimation;

	MapController mMapController = null;
	LocationClient mLocClient;
	LocationData locData = null;
	double longtitude;
	double latitude;
	private FrontiaStorage mCloudStorage;
	private String[] filepath;
	private ProgressDialog reDialog;
	private ProgressDialog progressDialog;

	private SharedPreferences spConfig;

	private Editor editorConfig;
	private TextView register;
	private AlertDialog dialog;
	private ProgressDialog dialog2;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.guide_view);
		dialog=new MyProgressPopUpWindow(GuideActivity.this,"正在拷贝数据中...").createADialog();
		//dialog2 = ProgressDialog.show(this, "提示", "正在登陆中");  
		initGuidView();
		initDot();

		spConfig = getSharedPreferences("SystemConfig", Context.MODE_PRIVATE);
		editorConfig = spConfig.edit();

		

		boolean isInit = Frontia.init(this.getApplicationContext(),
				"GdZNITcYcI3bhiwKx7duVbR7");
		if (!isInit) {
			Toast.makeText(getApplicationContext(), "您的app key 错误", 3).show();
			return;
		}

		mCloudStorage = Frontia.getStorage();
		//upLoad();
		uploadChurchInfo();

		

		alphaanimation = new AlphaAnimation(0.0f, 1.0f);

		SharedPreferences sp = getSharedPreferences("UserInfo",
				Context.MODE_PRIVATE);
		if (!sp.getString("userName", "").equals("")) {
			UpLoadChurchMember upLoadChurchMember = new UpLoadChurchMember(
					this, reDialog);
			upLoadChurchMember.isAMember(sp.getString("userName", ""));
		}
		alphaanimation.setDuration(2 * 1000);
		
		
		
		
		final AsyncTask<Void, Void, Void> BibleTestDBAsyn=new AsyncTask<Void, Void, Void>() {
			@Override
			protected Void doInBackground(Void... params) {
				copyBibleTestDB();
				return null;
			}
			protected void onPostExecute(Void result) {
				editorConfig.putBoolean("isInstall", true);
				editorConfig.commit();
				Log.i("haola", "haola");
				 startBtn.setClickable(true);
				 startBtn.setSelected(false);
				 register.setClickable(true);
				 register.setSelected(false);
				startBtn.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						Log.i("haola", "click");
						Intent intent = new Intent(GuideActivity.this,
								MainActivity.class);
						startActivity(intent);
						GuideActivity.this.finish();
					}
				});
				register.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						Intent intent = new Intent(GuideActivity.this,
								Activity_Register1.class);
						intent.putExtra("from", "GuideActivity");
						startActivity(intent);
						GuideActivity.this.finish();
					}
				});
				dialog.dismiss();
			};
		};
		
		final AsyncTask<Void, Void, Void> readDBAsyn=new AsyncTask<Void, Void, Void>() {
			@Override
			protected Void doInBackground(Void... params) {
				copyReadDB();
				return null;
			}
			protected void onPostExecute(Void result) {
				BibleTestDBAsyn.execute();
			};
		};
		
		
		
		final AsyncTask<Void, Void, Void> bibleDBAsyn=new AsyncTask<Void, Void, Void>() {
			@Override
			protected Void doInBackground(Void... params) {
				copyBibleDB();
				return null;
			}
			protected void onPostExecute(Void result) {
				readDBAsyn.execute();
			};
		};
		
		bibleDBAsyn.execute();
		
		
		
		/*new Thread(new Runnable(){   

		    public void run(){   

		        try {
					Thread.sleep(15*1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		        Message message=Message.obtain();
		        message.what=101;
		        new MyHandler().sendMessage(message);

		    }   

		}).start();*/
		
		
		Log.i("table", "create1");
		
		
	}
	
	

	private void initGuidView() {
		guideViewPager = (ViewPager) findViewById(R.id.guide_view_pager);
		mViews = new ArrayList<View>();

		LayoutInflater inflater = LayoutInflater.from(this);

		mViews.add(inflater.inflate(R.layout.boot_one, null));
		mViews.add(inflater.inflate(R.layout.boot_two, null));
		mViews.add(inflater.inflate(R.layout.boot_three, null));
		mViews.add(inflater.inflate(R.layout.boot_four, null));

		img = (ImageView) mViews.get(3).findViewById(R.id.logo);
		guideViewAdapter = new ViewPagerAdapter(mViews);
		guideViewPager.setAdapter(guideViewAdapter);
		guideViewPager.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageSelected(int arg0) {
				//Log.i("qwe", "onPageSelected");
				setCurrentDot(arg0);
				if(arg0==3)
				{
					Log.i("haola", "3");
					img.setAnimation(alphaanimation);
				}
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				Log.i("qwe", "onPageScrolled");
			}

			@Override
			public void onPageScrollStateChanged(int arg0) {
				Log.i("qwe", "onPageScrollStateChanged");

			}
		});

		startBtn = (TextView) mViews.get(3).findViewById(R.id.start_btn);
		startBtn.setSelected(true);
		
		register=(TextView) mViews.get(3).findViewById(R.id.register);
		register.setSelected(true);

	}

	private void initDot() {
		LinearLayout layout = (LinearLayout) findViewById(R.id.guide_dots);

		guideDots = new ImageView[mViews.size()];

		for (int i = 0; i < mViews.size(); i++) {
			guideDots[i] = (ImageView) layout.getChildAt(i);
			guideDots[i].setSelected(false);
		}

		currentIndex = 0;
		guideDots[currentIndex].setSelected(true);
	}

	private void setCurrentDot(int position) {
		if (position < 0 || position > mViews.size() - 1
				|| currentIndex == position) {
			return;
		}

		guideDots[position].setSelected(true);
		guideDots[currentIndex].setSelected(false);
		currentIndex = position;
	}

	public class MyLocationListenner implements BDLocationListener {

		@Override
		public void onReceiveLocation(BDLocation arg0) {
			// System.out.print(arg0.getAddrStr());

			if (arg0 != null) {
				// 将当前位置转换成地理坐标点

				Log.i("111", longtitude + "swsss");

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
				// 使用这个api时一定要设置option.setAddrType("all");
				editor.putString("location", arg0.getAddrStr());
				editor.putString("city",
						arg0.getProvince() + " " + arg0.getCity());
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
		mLocClient.registerLocationListener(new MyLocationListenner());
		LocationClientOption option = new LocationClientOption();
		option.setPriority(LocationClientOption.GpsFirst);
		option.setAddrType("all");
		option.setOpenGps(true);// 打开gps
		// option.setCoorType("bd09ll"); // 设置坐标类型
		option.setScanSpan(500);
		mLocClient.setLocOption(option);
		mLocClient.start();

	}

	public void copyBibleTestDB() {
		// Log.i("哈哈哈哈哈", "执行");
		File file = new File(
				"/data/data/com.example.acts/databases/bibletest.db");
		File dir = new File("/data/data/com.example.acts/databases");
		if (!dir.exists()) {
			dir.mkdir();
		}
		;
		try {
			InputStream in = getResources().getAssets().open("bibletest.db");
			FileOutputStream fos = new FileOutputStream(file);
			int count = 0;
			byte[] buffer = new byte[1024]; // 创建byte数组
			while ((count = in.read(buffer)) > 0) {
				fos.write(buffer);
			}
			in.close();
			fos.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void copyBibleDB() {
		// Log.i("哈哈哈哈哈", "执行");
		File file = new File("/data/data/com.example.acts/databases/bible.db");
		File dir = new File("/data/data/com.example.acts/databases");
		if (!dir.exists()) {
			dir.mkdir();
		}
		try {
			
			InputStream in = getResources().getAssets().open("bible.db");
			
			FileOutputStream fos = new FileOutputStream(file);
			int count = 0;
			byte[] buffer = new byte[1024]; // 创建byte数组
			while ((count = in.read(buffer)) > 0) {
				fos.write(buffer);
				Log.i("sssssss", "dddd");
			}
			in.close();
			fos.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		//Log.i("sssssss", "dddd");
	}

	public void copyReadDB() {
		// Log.i("哈哈哈哈哈", "执行");
		File file = new File("/data/data/com.example.acts/databases/read.db");
		File dir = new File("/data/data/com.example.acts/databases");
		if (!dir.exists()) {
			dir.mkdir();
		}
		;
		try {
			InputStream in = getResources().getAssets().open("read.db");
			FileOutputStream fos = new FileOutputStream(file);
			int count = 0;
			byte[] buffer = new byte[1024]; // 创建byte数组
			while ((count = in.read(buffer)) > 0) {
				fos.write(buffer);
			}
			in.close();
			fos.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void copyTranslate() {
		FileOutputStream fos = null;
		InputStream in = null;
		File file = null;
		File dir = null;
		/*
		 * String uri=""; for(int i=1;i<=66;i++) { for(int
		 * j=1;j<=contentNum[i];j++) { if(i<10) { if(j<10) { uri="0"+i+"_0"+j; }
		 * else { uri="0"+i+"_"+j; }
		 * 
		 * } else { uri=i+"_"+j; }
		 */

		dir = new File(Environment.getExternalStorageDirectory()
				.getAbsoluteFile() + "/0000");
		if (!dir.exists()) {
			dir.mkdir();
		};
		try {
			filepath = getResources().getAssets().list("translate");
			for (String path : filepath) {
				in = getResources().getAssets().open("translate/" + path);
				Log.i("assets", "translate/" + path);
				fos = new FileOutputStream(new File(Environment
						.getExternalStorageDirectory().getAbsoluteFile()
						+ "/0000/" + path));
				Log.i("内存", Environment.getExternalStorageDirectory()
						+ "/0000/" + path);
				int count = 0;
				byte[] buffer = new byte[1024]; // 创建byte数组
				while ((count = in.read(buffer)) > 0) {
					fos.write(buffer);
				}

			}
			in.close();
			fos.close();

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void uploadChurchInfo() {
		SharedPreferences spUser = getSharedPreferences("UserInfo",
				Context.MODE_PRIVATE);
		SharedPreferences sp = getSharedPreferences("ChurchInfo",
				Context.MODE_PRIVATE);
		if (sp.getString("churchName", "").equals("")) {
			Log.i("已经进入教会", "已经进入教会");
			if (spUser.getBoolean("isLogin", false)) {
				Log.i("已经denglu", "已经");
				reDialog = new ProgressDialog(this);
				reDialog.setMessage("正在查询中...");
				UpLoadChurchInfo upload = new UpLoadChurchInfo(this, reDialog);
				upload.isAMember(spUser.getString("userName", ""));
			}

		}

	}
	
	class MyHandler extends Handler
	{
		@Override
		public void handleMessage(Message msg) {
			if(msg.what==101)
			{
				dialog.dismiss();
			}
			super.handleMessage(msg);
		}
	}

}
