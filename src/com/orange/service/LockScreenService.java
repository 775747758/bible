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
		return Service.START_STICKY;//ճ�Խ���
		
	}
	
	@Override
	public void onCreate() {
		//����intent
				toMainIntent = new Intent(LockScreenService.this, Activity_Lock.class);//#����Main.classΪҪ��ת���Ľ��棬�ȵ�����ʱҪ�򿪵Ľ���
				toMainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				/*ע��㲥*/
				IntentFilter mScreenOnFilter = new IntentFilter("android.intent.action.SCREEN_ON");
				mScreenOnFilter.setPriority(2147483647);
				registerReceiver(mScreenOnReceiver, mScreenOnFilter);
				
				/*ע��㲥*/
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
		//�����˷���
		startActivity(new Intent(LockScreenService.this,LockScreenService.class));
	}
	
	private BroadcastReceiver screenReceiver = new BroadcastReceiver() {

		

		@SuppressWarnings("static-access")
		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			if (action.equals("android.intent.action.SCREEN_ON") || action.equals("android.intent.action.SCREEN_OFF")) {

				//�ر�����
				keyguardManager = (KeyguardManager) context.getSystemService(context.KEYGUARD_SERVICE);
				keyguardLock = keyguardManager.newKeyguardLock("");
				keyguardLock.disableKeyguard();//����
				//��������
				startActivity(toMainIntent);
			}

		}
	};
	
	
	//��Ļ�����Ĺ㲥,����Ҫ����Ĭ�ϵ���������
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
		
		//��Ļ�䰵/�����Ĺ㲥 �� ����Ҫ����KeyguardManager����Ӧ����ȥ�����Ļ����
		private BroadcastReceiver mScreenOffReceiver = new BroadcastReceiver(){
			@Override
			public void onReceive(Context context , Intent intent) {
				String action = intent.getAction() ;
				
			    
				if(action.equals("android.intent.action.SCREEN_OFF")
						|| action.equals("android.intent.action.SCREEN_ON") ){
					Log.i("����", "ddd");
					keyguardManager = (KeyguardManager)context.getSystemService(Context.KEYGUARD_SERVICE);
					keyguardLock = keyguardManager.newKeyguardLock("zdLock 1"); 
					keyguardLock.disableKeyguard();
					startActivity(toMainIntent);
				}
			}
			
		};

}
