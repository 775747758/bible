package com.example.acts;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.orange.about.AppInfo;

import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Environment;

public class Share {
	private Map<String, String> data = new HashMap<String, String>();
	private Map<String, AppInfo> map = new HashMap<String, AppInfo>();
	private String sendContent;
	private AppInfo wechat;
	private AppInfo qq;
	private Context context;
	private List<AppInfo> shareAppInfos;
	private PackageManager pm;
	String Path;
	File file;

	public Share(Context context, PackageManager pm, Map<String, String> data) {

		Path = Environment.getExternalStorageDirectory().getAbsolutePath();
		file = new File(Path + "/sharetowechat.png");
		// file=new File(("com/orange/picture/email.png"));

		sendContent = "经文分享：";
		this.context = context;
		this.pm = pm;
		this.data = data;
		shareAppInfos = getShareAppList();
		String str[] = new String[10];

		Set<String> set = data.keySet();
		Iterator<String> iter = set.iterator();
		while (iter.hasNext()) {
			String key = iter.next();
			String value = data.get(key);
			sendContent = sendContent + key + "  " + value;
		}

		SharedPreferences sp = context.getSharedPreferences("config", Context.MODE_PRIVATE);
		Editor editor = sp.edit();

		Iterator appIter = shareAppInfos.iterator();
		int i = 0;

		while (appIter.hasNext()) {
			editor.putInt("segment_color", R.color.pink);
			map.put(i + "", (AppInfo) appIter.next());
			editor.putString(i + "", map.get(i + "").getAppPkgName() + "$$$$$$$" + map.get(i + "").getAppLauncherClassName());
			i++;
		}
		editor.commit();
	}

	public List<ResolveInfo> getShareApps(Context context) {
		List<ResolveInfo> mApps = new ArrayList<ResolveInfo>();
		Intent intent = new Intent(Intent.ACTION_SEND, null);
		intent.addCategory(Intent.CATEGORY_DEFAULT);
		intent.setType("text/plain");
		// intent.setType("*/*");
		PackageManager pManager = context.getPackageManager();
		mApps = pManager.queryIntentActivities(intent, PackageManager.COMPONENT_ENABLED_STATE_DEFAULT);
		return mApps;
	}

	private List<AppInfo> getShareAppList() {
		List<AppInfo> shareAppInfos = new ArrayList<AppInfo>();
		List<ResolveInfo> resolveInfos = getShareApps(context);
		if (null == resolveInfos) {
			return null;
		} else {
			for (ResolveInfo resolveInfo : resolveInfos) {
				AppInfo appInfo = new AppInfo();
				appInfo.setAppPkgName(resolveInfo.activityInfo.packageName);
				// showLog_I(TAG, "pkg>" + resolveInfo.activityInfo.packageName
				// + ";name>" + resolveInfo.activityInfo.name);
				appInfo.setAppLauncherClassName(resolveInfo.activityInfo.name);
				appInfo.setAppName(resolveInfo.loadLabel(pm).toString());
				appInfo.setAppIcon(resolveInfo.loadIcon(pm));
				shareAppInfos.add(appInfo);
			}
		}
		return shareAppInfos;
	}

	public Intent share2qq() {
		Intent shareIntent = new Intent();
		shareIntent.setAction(Intent.ACTION_SEND);
		shareIntent.setType("text/plain");
		shareIntent.setComponent(new ComponentName("com.tencent.mobileqq", "com.tencent.mobileqq.activity.JumpActivity"));
		shareIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		shareIntent.putExtra(Intent.EXTRA_TEXT, sendContent);
		return shareIntent;

	}

	public Intent share2wechat() {

		Intent shareIntent = new Intent();
		shareIntent.setAction(Intent.ACTION_SEND);
		shareIntent.setType("text/plain");
		shareIntent.setComponent(new ComponentName("com.tencent.mm", "com.tencent.mm.ui.tools.ShareImgUI"));
		shareIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		shareIntent.putExtra(Intent.EXTRA_TEXT, sendContent);
		return shareIntent;
	}

	public Intent share2friendscircle() {

		try {
			copyBigDataToSD(Path + "/sharetowechat.png");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Intent intent = new Intent();
		ComponentName componentName = new ComponentName("com.tencent.mm", "com.tencent.mm.ui.tools.ShareToTimeLineUI");
		intent.setComponent(componentName);
		intent.setAction(Intent.ACTION_SEND);
		intent.setType("image/*");
		intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(file));
		return intent;

	}

	public Intent share2qqzone() {

		Intent shareIntent = new Intent();
		shareIntent.setType("text/plain");
		shareIntent.setComponent(new ComponentName("com.qzone", "com.qzone.ui.operation.QZonePublishMoodActivity"));
		shareIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		shareIntent.putExtra(Intent.EXTRA_TEXT, sendContent);
		return shareIntent;

	}

	public Intent share2sina() {

		Intent shareIntent = new Intent();
		// shareIntent.setDataAndType(Uri.parse(sendContent), "text/plain");
		shareIntent.setAction(Intent.ACTION_SEND);
		shareIntent.setComponent(new ComponentName("com.sina.weibo", "com.sina.weibo.EditActivity"));
		shareIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		shareIntent.setType("text/plain");
		shareIntent.putExtra(Intent.EXTRA_TEXT, sendContent);
		return shareIntent;

	}

	public Intent share2message() {
		Intent it = new Intent(Intent.ACTION_VIEW);
		it.putExtra("sms_body", sendContent);
		it.setType("vnd.android-dir/mms-sms");
		return it;

	}

	public Intent share2mail() {
		Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);
		// 设置文本格式
		emailIntent.setComponent(new ComponentName("com.android.email", "com.android.email.activity.MessageCompose"));
		emailIntent.setType("text/plain");
		// 设置对方邮件地址
		emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL, "");
		// 设置标题内容
		emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "圣经");
		// 设置邮件文本内容
		emailIntent.putExtra(android.content.Intent.EXTRA_TEXT, sendContent);
		return emailIntent;

	}

	private void copyBigDataToSD(String strOutFileName) throws IOException {
		InputStream myInput;
		OutputStream myOutput = new FileOutputStream(strOutFileName);
		myInput = context.getAssets().open("sharetowechat.png");
		byte[] buffer = new byte[1024];
		int length = myInput.read(buffer);
		while (length > 0) {
			myOutput.write(buffer, 0, length);
			length = myInput.read(buffer);
		}

		myOutput.flush();
		myInput.close();
		myOutput.close();
	}

}
