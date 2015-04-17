package com.example.acts;

import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.content.Context;
import android.view.KeyEvent;
import android.widget.Toast;

public class ExitApp {
	private Context context;
	private static Boolean isExit = false;
	private Timer tExit;
	private TimerTask task;

	public ExitApp(Context context) {
		super();
		this.context = context;
		tExit = new Timer();
		task = new TimerTask() {
			@Override
			public void run() {
				isExit = false;
			}
		};
	}

	public void exit() {
		if (isExit == false) {
			isExit = true;
			Toast.makeText(context, "再按一次后退键退出应用程序", Toast.LENGTH_SHORT).show();
			if (task != null)
				task.cancel(); // 将原任务从队列中移除
			tExit.schedule(task, 2000);
		} else {
			System.exit(0);
		}
	}

}
