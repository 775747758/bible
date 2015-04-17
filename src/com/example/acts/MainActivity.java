package com.example.acts;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.Inflater;

import www.orange.utils.ChenJin;
import www.orange.utils.IsMIUI;
import www.orange.utils.MyProgressPopUpWindow;

import com.orange.church.Activity_MyChurch;
import com.orange.church.Church_Activity;
import com.orange.login.Activity_Login;
import com.orange.net.UpLoadChurchInfo;
import com.orange.net.UpLoadChurchMember;
import com.orange.read.Activity_ReadBible2;
import com.orange.service.LockScreenService;
import com.orange.test.Activity_BibleTestMain;
import com.orange.view.CircleImageView;
import com.special.ResideMenu.ResideMenu;
import com.special.ResideMenu.ResideMenuItem;
import com.todddavies.components.progressbar.main;


import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.app.TabActivity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.TabHost.TabSpec;
import android.widget.Toast;

public class MainActivity extends TabActivity implements View.OnClickListener{

	public TabHost tabHost;
	private LayoutInflater inflater;
	private ResideMenu resideMenu;
	private ResideMenuItem itemHome;
	private ResideMenuItem itemLogin;
	private int from;
	private ProgressDialog reDialog;
	private ResideMenuItem itemLogout;
	private SharedPreferences sp;
	private boolean isLogin;
	private List<ResideMenuItem> list1=new ArrayList<ResideMenuItem>();
	private List<ResideMenuItem> list2=new ArrayList<ResideMenuItem>();
	private int REFRESH=100;
	private int SETLOGOUT=101;
	public Myhandler myHandler=new Myhandler();
	private ResideMenuItem itemConfig;
	private AlertDialog dialog;
	private String[] filepath;
	private SharedPreferences spConfig;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		ChenJin.set(MainActivity.this);
		setContentView(R.layout.activity_tabhost);
		
		//SharedPreferences sp=getPreferences(Main_Activity.this,MODE_PRIVATE);
		spConfig =getSharedPreferences("SystemConfig", Context.MODE_PRIVATE);
		//editor.putBoolean("isAutoLogin", isChecked);
		if(spConfig.getBoolean("isLockScreen", false)){
			startService(new Intent(MainActivity.this, LockScreenService.class));
		}
		
		
		
		
		inflater = LayoutInflater.from(this);
		tabHost = getTabHost();
		tabHost.addTab(getMainTab());
		tabHost.addTab(getChurchTab());
		tabHost.addTab(getPersonalTab());
		 FrameLayout fl=tabHost.getTabContentView();
		 
		 Log.i("er", fl.getChildCount()+"ii");
		Intent intent=getIntent();
		from=intent.getIntExtra("from", 0);
		if(from==2)
		{
			tabHost.setCurrentTab(2);
		}
		else if(from==1)
		{
			tabHost.setCurrentTab(1);
		}
		else
		{
			tabHost.setCurrentTab(0);
		}
		sp = getSharedPreferences("UserInfo", Context.MODE_PRIVATE);
		
		isLogin=sp.getBoolean("isLogin", false);
		
		resideMenu = new ResideMenu(this);
        resideMenu.setBackground(R.drawable.menu_background);
        resideMenu.attachToActivity(this);
        resideMenu.setSwipeDirectionDisable(ResideMenu.DIRECTION_RIGHT);
        resideMenu.setScaleValue(0.6f);
        
        itemLogin=new ResideMenuItem(this, R.drawable.icon_profile,"登陆");
        itemLogout=new ResideMenuItem(this, R.drawable.icon_logout,"退出");
        
        itemConfig=new ResideMenuItem(this, R.drawable.icon_settings,"设置");
       // resideMenu.addMenuItem(itemConfig);
        list1.add(itemConfig);
        list1.add(itemLogin);
        
        list2.add(itemConfig);
        list2.add(itemLogout);
       
        if(isLogin)
        {
        	resideMenu.setMenuItems(list2,  ResideMenu.DIRECTION_LEFT);
        	//resideMenu.addMenuItem(itemLogout, ResideMenu.DIRECTION_LEFT);
        }
        else
        {
        	resideMenu.setMenuItems(list1,  ResideMenu.DIRECTION_LEFT);
        	 //resideMenu.addMenuItem(itemLogin, ResideMenu.DIRECTION_LEFT);
        }
        itemLogin.setOnClickListener(this);
        itemLogout.setOnClickListener(this);
        itemConfig.setOnClickListener(this);
        
        
		
		final AsyncTask<Void, Void, Void> translateAsyn=new AsyncTask<Void, Void, Void>() {
			
			
			@Override
			protected Void doInBackground(Void... params) {
				
				return null;
			}
			protected void onPostExecute(Void result) {
				//dialog.dismiss();
			};
		};
		//translateAsyn.execute();
		File file = new File(Environment.getExternalStorageDirectory()
				.getAbsoluteFile() + "/0000/999.htm");
		if(!file.exists())
		{
			dialog=new MyProgressPopUpWindow(MainActivity.this,"第一次进入配置中...").createADialog();
			new Thread(new Runnable() {
				
				@Override
				public void run() {
					copyTranslate();
					Message message=Message.obtain();
			        message.what=105;
			        myHandler.sendMessage(message);
				}
			}).start();
		}
		
		
	}
	
	@Override
	protected void onNewIntent(Intent intent) {
		setIntent(intent);
		super.onNewIntent(intent);
	}
	
	
	@Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        return resideMenu.dispatchTouchEvent(ev);
    }

	private TabSpec getMainTab() {
		TabSpec spec = tabHost.newTabSpec("主页");
		Intent intent = new Intent(this, Main_Activity.class);
		spec.setContent(intent);
		spec.setIndicator(getIndicatorView("主页", R.drawable.selector_tabweiget_main));
		return spec;
	}

	private TabSpec getChurchTab() {
		//SharedPreferences sp = getSharedPreferences("ChurchInfo", Context.MODE_PRIVATE);
		
		TabSpec spec = tabHost.newTabSpec("教会");//Activity_MyChurch
		//String churchName=sp.getString("churchName", "");
		Intent intent =null;
		SharedPreferences sp =getSharedPreferences("config", Context.MODE_PRIVATE);
		int isAMember=sp.getInt("isAMember", 0);
		intent=new Intent(this,Church_Activity.class);
		if(isAMember==0)
		{
			intent=new Intent(this,Church_Activity.class);
		}
		else
		{
			reDialog = new ProgressDialog(this);
			reDialog.setMessage("正在查询中...");
			UpLoadChurchInfo upload = new UpLoadChurchInfo(this, reDialog);
			upload.getAChurch(sp.getString("churchName", ""));
			intent=new Intent(this,Activity_MyChurch.class);
		}
		/*if(churchName.equals(""))
		{
			intent=new Intent(this,Church_Activity.class);
		}
		else
		{
			intent=new Intent(this,Activity_MyChurch.class);
			//intent.putExtra("from", "MainActivity");
		}*/
		spec.setContent(intent);
		spec.setIndicator(getIndicatorView("教会", R.drawable.selector_tabweiget_church));
		return spec;
	}

	private TabSpec getPersonalTab() {
		TabSpec spec = tabHost.newTabSpec("个人");
		Intent intent = new Intent(this, Personal_Activity.class);
		spec.setContent(intent);
		spec.setIndicator(getIndicatorView("个人", R.drawable.selector_tabweiget_personal));
		return spec;
	}

	private View getIndicatorView(String name, int iconid) {
		View view = inflater.inflate(R.layout.tab_main_nav, null);
		//ImageView ivicon = (ImageView) view.findViewById(R.id.ivIcon);
		TextView tvtitle = (TextView) view.findViewById(R.id.tvTitle);
		//ivicon.setImageResource(iconid);
		tvtitle.setText(name);
		return view;
	}
	@Override
	public void onClick(View v) {
		 if (v == itemLogin)
		 {
			 Intent intent=new Intent(MainActivity.this,Activity_Login.class);
			 overridePendingTransition(R.anim.zoomin, R.anim.zoomout);
			 startActivity(intent);
	     }
		 if(v == itemLogout)
		 {
			 	Editor editor=sp.edit();
				editor.putBoolean("isLogin", false);
				editor.putString("churchName", "");
				editor.putString("userName", "");
				editor.putString("password", "");
				editor.putString("name","");
				editor.putString("birthday", "");
				editor.putString("gender","");
				editor.putString("qq","");
				editor.commit();
				resideMenu.setMenuItems(list1,  ResideMenu.DIRECTION_LEFT);
				//tabHost.invalidate();
				//resideMenu.addMenuItem(itemLogin, ResideMenu.DIRECTION_LEFT);
				//resideMenu.invalidate();
				
		 }
		if(v==itemConfig)
		{
			Intent intent=new Intent(MainActivity.this,Activity_Settings.class);
			 overridePendingTransition(R.anim.zoomin, R.anim.zoomout);
			 startActivity(intent);
		}
		 
		 
		 
	}
	
	public class Myhandler extends Handler
	{
		@Override
		public void handleMessage(Message msg) {
			if(msg.what==REFRESH)
			{
				tabHost.setCurrentTab(0);  
				tabHost.clearAllTabs();  
				tabHost.addTab(getMainTab());
				tabHost.addTab(getChurchTab());
				tabHost.addTab(getPersonalTab());
			}
			if(msg.what==SETLOGOUT)
			{
				setLogout();
			}
			if(msg.what==105)
			{
				dialog.dismiss();
			}
			super.handleMessage(msg);
		}
	}
	
	public void setLogout()
	{
		resideMenu.setMenuItems(list1,  ResideMenu.DIRECTION_LEFT);
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
	
	
}
