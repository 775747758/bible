package com.example.acts;

import www.orange.utils.LockLayer;

import com.orange.service.LockScreenService;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class Activity_Lock extends Activity {
	public static int MSG_LOCK_SUCESS = 1;
	private ImageView drag;
	private MyHandler myHandler;
	int index=0;
	private int[] ids={R.drawable.slider_arrow_1,R.drawable.slider_arrow_2,R.drawable.slider_arrow_3};
	private MyThread myThread;
	private LinearLayout drag_ll;
	private TextView today_section;
	private SharedPreferences sp;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_lock);
		
		today_section=(TextView)findViewById(R.id.today_section);
		sp = getSharedPreferences("config", Context.MODE_PRIVATE);
		today_section.setText(sp.getString("today_section", ""));
		
		today_section.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				ClipboardManager cmb = (ClipboardManager) getSystemService(Activity_Lock.this.CLIPBOARD_SERVICE);
				ClipData clip = ClipData.newPlainText("text",today_section.getText().toString().trim() );
				cmb.setPrimaryClip(clip);
				Toast.makeText(getApplicationContext(), "ÒÑ¸´ÖÆ", Toast.LENGTH_SHORT).show();
				
			}

		});
		
		drag=(ImageView)findViewById(R.id.drag);
		startService(new Intent(Activity_Lock.this, LockScreenService.class));
		drag_ll=(LinearLayout)findViewById(R.id.drag_ll);
		drag_ll.setOnTouchListener(new OnTouchListener() {
			float startX=0;
			float startY=0;
			float endX=0;
			float endY=0;
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:
					startX=event.getX();
					startY=event.getY();
					break;
				case MotionEvent.ACTION_UP:
					endX=event.getX();
					endY=event.getY();
					break;
				default:
					break;
				}
				if(endX-startX>100)
				{
					
					Log.i("qwe", "ssss");
					finish();
				}
				return false;
			}
		});
		myHandler=new MyHandler();
		myThread=new MyThread();
		myThread.start();
		
	}

	/*
	 * //ÆÁ±ÎµôHome¼ü public void onAttachedToWindow() { finish();
	 * //this.getWindow()
	 * .setType(WindowManager.LayoutParams.TYPE_KEYGUARD_DIALOG);
	 * super.onAttachedToWindow(); }
	 */

	// ÆÁ±ÎµôBack¼ü
	public boolean onKeyDown(int keyCode, KeyEvent event) {

		if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
			return true;
		} else if (event.getKeyCode() == KeyEvent.KEYCODE_HOME) {
			finish();
			return true;
		} else {
			return super.onKeyDown(keyCode, event);
		}

	}
	
	
	class MyHandler extends Handler
	{
		@Override
		public void handleMessage(Message msg) {
			if(index==2)
			{
				index=0;
			}
			else
			{
				index+=1;
			}
			drag.setImageResource(ids[index]);
			super.handleMessage(msg);
		}
	}
	
	class MyThread extends Thread
	{
		@Override
		public void run() {
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			Message msg  =  myHandler.obtainMessage();
			msg.what=1;
			myHandler.sendMessage(msg);
			super.run();
			new MyThread().start();
		}
	}
	
}
