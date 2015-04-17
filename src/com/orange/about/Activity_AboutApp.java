package com.orange.about;

import java.util.List;

import www.orange.updateapk.createApopwindow;

import com.baidu.frontia.Frontia;
import com.baidu.frontia.FrontiaData;
import com.baidu.frontia.FrontiaQuery;
import com.baidu.frontia.api.FrontiaStorage;
import com.baidu.frontia.api.FrontiaStorageListener.DataInfoListener;
import com.example.acts.R;

import com.orange.read.Activity_SelectVolume;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

public class Activity_AboutApp extends Activity {

	private FrontiaStorage mCloudStorage;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_aboutapp);

		mCloudStorage = Frontia.getStorage();
		// 获取版本信息
		PackageManager manager = this.getPackageManager();
		PackageInfo info;
		try {
			info = manager.getPackageInfo(this.getPackageName(), 0);
			String appname = getString(R.string.app_name);
			String version = info.versionName;
			TextView textview1 = (TextView) findViewById(R.id.version);
			TextView textview2 = (TextView) findViewById(R.id.appname);
			textview1.setText("软件名称 : " + appname);
			textview2.setText("软件版本 : " + version);
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void back(View view) {
		Intent intent = new Intent(Activity_AboutApp.this, Activity_SelectVolume.class);
		startActivity(intent);
		finish();
		overridePendingTransition(R.anim.zoomin, R.anim.zoomout);
	}

	public void update(View view) {
		ProgressDialog reDialog;
		reDialog = new ProgressDialog(Activity_AboutApp.this);
		reDialog.setMessage("正在检测中...");
		reDialog.show();
		//createApopwindow.isNew(getApplicationContext(), getWindowManager(), view, reDialog);
	}

	public void feedback(View view) {
		Intent intent = new Intent(Activity_AboutApp.this, Activity_Talk.class);
		startActivity(intent);
		finish();
		overridePendingTransition(R.anim.zoomin, R.anim.zoomout);
	}

	public void rating(View view) {
		Uri uri = Uri.parse("market://details?id=" + getPackageName());
		Intent intent = new Intent(Intent.ACTION_VIEW, uri);
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		startActivity(intent);
		overridePendingTransition(R.anim.zoomin, R.anim.zoomout);

	}

	public void function_introduce(View view) {

		Intent intent = new Intent(Activity_AboutApp.this, Activity_AppFunction.class);
		startActivity(intent);
		// finish();
		overridePendingTransition(R.anim.zoomin, R.anim.zoomout);
	}

	/*
	 * public void see(View view) {
	 * 
	 * // FrontiaQuery中有很多查询条件，你可以尝试多种查询条件，相当于sql语句中的where FrontiaQuery query =
	 * new FrontiaQuery();
	 * 
	 * mCloudStorage.findData(query, new DataInfoListener() {
	 * 
	 * @Override public void onSuccess(List<FrontiaData> dataList) {
	 * StringBuilder sb = new StringBuilder(); int i = 0; for(FrontiaData d :
	 * dataList){ Log.i("查看", d.toJSON().toString());
	 * //Toast.makeText(getApplicationContext(), d.get("suggestion").toString(),
	 * Toast.LENGTH_SHORT).show(); } }
	 * 
	 * @Override public void onFailure(int errCode, String errMsg) {
	 * 
	 * } });
	 * 
	 * }
	 */

}
