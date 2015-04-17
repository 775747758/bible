package com.example.acts;

import www.orange.utils.UMShare;

import com.orange.read.Activity_SelectVolume;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class Activity_AboutApp extends Activity {

	

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_aboutapp);
		
		// 获取版本信息
		PackageManager manager = this.getPackageManager();
		PackageInfo info;
		try {
			info = manager.getPackageInfo(this.getPackageName(), 0);
			String appname = getString(R.string.app_name);
			String version = info.versionName;
			TextView textview1 = (TextView) findViewById(R.id.version);
			TextView textview2 = (TextView) findViewById(R.id.appname);
			textview1.setText("V" + version);
			textview2.setText(appname);
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void back(View view) {
		Intent intent = new Intent(Activity_AboutApp.this, Activity_Settings.class);
		startActivity(intent);
		finish();
		overridePendingTransition(R.anim.zoomin, R.anim.zoomout);
	}
	
	public void xinlang(View view)
	{
		 Uri uri = Uri.parse("http://weibo.com/recitebible");    
		 Intent it = new Intent(Intent.ACTION_VIEW, uri);    
		 startActivity(it);//http://app.mi.com/detail/59904
	}
}
