package com.orange.receiver;

import com.example.acts.Activity_Settings;
import com.example.acts.MainActivity;
import com.example.acts.Main_Activity;
import com.example.acts.R;

import android.app.Notification;
import android.app.Notification.Builder;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

/**
 * 
 * @ClassName: AlarmReceiver  
 * @Description: 闹铃时间到了会进入这个广播，这个时�?可以做一些该做的业务�? * @author HuHood
 * @date 2013-11-25 下午4:44:30  
 *
 */
public class AlarmReceiver extends BroadcastReceiver {
	
	private NotificationManager manager;
	private Builder builder;
	private Notification notification;

	@Override
    public void onReceive(Context context, Intent intent) {
		manager = (NotificationManager) context.getSystemService("notification");
		
		Intent i = new Intent(context, MainActivity.class);
		PendingIntent contentIntent = PendingIntent.getActivity(context, 0,  i, PendingIntent.FLAG_CANCEL_CURRENT);
		builder = new Notification.Builder(context);
		builder.setTicker("圣经流利说");
		builder.setSmallIcon(R.drawable.ic_launcher_little);
		builder.setContentTitle("圣经流利说");
		builder.setContentText("亲，该背诵圣经了哦  ^_^");
		builder.setContentIntent(contentIntent);
		//builder.setProgress(100, 0, false);
		notification = builder.build();
		notification.defaults=Notification.DEFAULT_SOUND;
		manager.notify(1002, notification);
		
		//Toast.makeText(context, "闹铃响了, 可以做点事情了~~", Toast.LENGTH_LONG).show();
    }

}
