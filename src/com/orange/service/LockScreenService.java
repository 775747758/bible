package com.orange.service;



import com.example.acts.Activity_Lock;
import com.example.acts.MainActivity;
import com.orange.test.Activity_BibleTestMain;

import android.app.KeyguardManager;
import android.app.KeyguardManager.KeyguardLock;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.util.Log;

public class LockScreenService extends Service {

	private Intent toMainIntent;
	private KeyguardManager keyguardManager;
	private KeyguardLock keyguardLock;

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		return Service.START_STICKY;//粘性进程
		
	}
	
	@Override
	public void onCreate() {
		//设置intent
				toMainIntent = new Intent(LockScreenService.this, Activity_Lock.class);//#设置Main.class为要跳转到的界面，既当解锁时要打开的界面
				toMainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				/*注册广播*/
				IntentFilter mScreenOnFilter = new IntentFilter("android.intent.action.SCREEN_ON");
				mScreenOnFilter.setPriority(2147483647);
				registerReceiver(mScreenOnReceiver, mScreenOnFilter);
				
				/*注册广播*/
				IntentFilter mScreenOffFilter = new IntentFilter("android.intent.action.SCREEN_OFF");
				mScreenOffFilter.setPriority(2147483647);
				registerReceiver(mScreenOffReceiver, mScreenOffFilter);
				
				
		super.onCreate();
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		unregisterReceiver(mScreenOnReceiver);
		unregisterReceiver(mScreenOffReceiver);
		//重启此服务
		startActivity(new Intent(LockScreenService.this,LockScreenService.class));
	}
	
	private BroadcastReceiver screenReceiver = new BroadcastReceiver() {

		

		@SuppressWarnings("static-access")
		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			if (action.equals("android.intent.action.SCREEN_ON") || action.equals("android.intent.action.SCREEN_OFF")) {

				//关闭锁屏
				keyguardManager = (KeyguardManager) context.getSystemService(context.KEYGUARD_SERVICE);
				keyguardLock = keyguardManager.newKeyguardLock("");
				keyguardLock.disableKeyguard();//解锁
				//打开主界面
				startActivity(toMainIntent);
			}

		}
	};
	
	
	//屏幕变亮的广播,我们要隐藏默认的锁屏界面
		private BroadcastReceiver mScreenOnReceiver = new BroadcastReceiver(){

			@Override
			public void onReceive(Context context , Intent intent) {
				

				if(intent.getAction().equals("android.intent.action.SCREEN_ON")){
					keyguardManager = (KeyguardManager)context.getSystemService(Context.KEYGUARD_SERVICE);
					keyguardLock = keyguardManager.newKeyguardLock("zdLock 1"); 
					keyguardLock.disableKeyguard();
				}
			}
			
		};
		
		//屏幕变暗/变亮的广播 ， 我们要调用KeyguardManager类相应方法去解除屏幕锁定
		private BroadcastReceiver mScreenOffReceiver = new BroadcastReceiver(){
			@Override
			public void onReceive(Context context , Intent intent) {
				String action = intent.getAction() ;
				
			    
				if(action.equals("android.intent.action.SCREEN_OFF")
						|| action.equals("android.intent.action.SCREEN_ON") ){
					Log.i("安亮", "ddd");
					keyguardManager = (KeyguardManager)context.getSystemService(Context.KEYGUARD_SERVICE);
					keyguardLock = keyguardManager.newKeyguardLock("zdLock 1"); 
					keyguardLock.disableKeyguard();
					startActivity(toMainIntent);
				}
			}
			
		};

}
