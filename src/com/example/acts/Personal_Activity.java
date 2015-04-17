package com.example.acts;

import java.io.File;

import www.orange.utils.MyProgressPopUpWindow;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.orange.friendscircle.Activity_FriendsCircle;
import com.orange.login.Activity_Login;
import com.orange.map.Activity_Map;
import com.orange.map.Activity_MapMain;
import com.orange.net.UpLoadUserInfo;
import com.orange.user.Activity_DetailUserInfo;
import com.orange.user.Activity_MyTalk;
import com.orange.user.Activity_UserInfo;
import com.orange.view.CircleImageView;

public class Personal_Activity extends Activity {

	private CircleImageView portrait;
	private TextView name_tv;
	private String portraitUri;
	private boolean isLogin;
	private SharedPreferences sp;
	private AlertDialog dialog;
	private UpLoadUserInfo upload;
	private String userName;
	private String picName;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_personal);

		portrait = (CircleImageView) findViewById(R.id.portrait);
		name_tv = (TextView) findViewById(R.id.name_tv);

		sp = getSharedPreferences("UserInfo", Context.MODE_PRIVATE);

		isLogin = sp.getBoolean("isLogin", false);

	}

	@Override
	protected void onResume() {
		Log.i("执行", "执行");
		if (sp.getBoolean("isLogin", false)) {
			// name_tv.setClickable(false);
			// portrait.setClickable(false);
			portraitUri = sp.getString("portraitUri", "");
			picName = portraitUri.substring(portraitUri
					.lastIndexOf("/") + 1);
			
			downPortrait();

			name_tv.setText(sp.getString("name", "点击登录"));
		} else {
			Log.i("进入", "进入");
			portrait.setImageResource(R.drawable.ic_launcher);
			name_tv.setText("点击登陆");
		}
		super.onResume();
	}

	public void userInfo(View view) {
		if (sp.getBoolean("isLogin", false)) {
			Intent intent = new Intent(Personal_Activity.this,
					Activity_DetailUserInfo.class);

			intent.putExtra("userName", sp.getString("userName", ""));
			intent.putExtra("name", sp.getString("name", ""));
			intent.putExtra("from", "Personal_Activity");
			startActivity(intent);
			overridePendingTransition(R.anim.zoomin, R.anim.zoomout);
		} else {
			Toast.makeText(getApplicationContext(), "您还没有登录，请先登录！",
					Toast.LENGTH_SHORT).show();
		}

	}

	public void exit(View view) {
		if (sp.getBoolean("isLogin", false)) {
			portrait.setImageResource(R.drawable.ic_launcher);
			name_tv.setText("点击登录");
			name_tv.setClickable(true);
			Editor editor = sp.edit();
			editor.putBoolean("isLogin", false);
			editor.putString("churchName", "");
			editor.putString("userName", "");
			editor.putString("password", "");
			editor.putString("name", "");
			editor.putString("birthday", "");
			editor.putString("gender", "");
			editor.putString("qq", "");
			editor.commit();

			MainActivity t = (MainActivity) getParent();
			Message message = new Message();
			message.what = 101;
			t.myHandler.sendMessage(message);

		} else {
			Log.i("isLogin", "isLogin");
			Toast.makeText(getApplicationContext(), "您还没有登录！",
					Toast.LENGTH_SHORT).show();
		}

	}

	public void login(View view) {
		if (sp.getBoolean("isLogin", false)) {
			Intent intent = new Intent(Personal_Activity.this,
					Activity_DetailUserInfo.class);
			intent.putExtra("userName", sp.getString("userName", ""));
			intent.putExtra("name", sp.getString("name", ""));
			intent.putExtra("from", "Personal_Activity");
			startActivity(intent);
			overridePendingTransition(R.anim.zoomin, R.anim.zoomout);
		} else {
			Intent intent = new Intent(Personal_Activity.this,
					Activity_Login.class);
			startActivity(intent);
			overridePendingTransition(R.anim.zoomin, R.anim.zoomout);
		}

	}

	public void map(View view) {
		if (sp.getBoolean("isLogin", false)) {
			Intent intent = new Intent(Personal_Activity.this,
					Activity_MapMain.class);
			startActivity(intent);
			intent.putExtra("from", "Personal_Activity");
			overridePendingTransition(R.anim.zoomin, R.anim.zoomout);
		} else {
			Toast.makeText(getApplicationContext(), "您还没有登录，请先登录！",
					Toast.LENGTH_SHORT).show();
		}
	}

	public void mytalk(View view) {
		if (sp.getBoolean("isLogin", false)) {
			Intent intent = new Intent(Personal_Activity.this,
					Activity_MyTalk.class);
			startActivity(intent);
			overridePendingTransition(R.anim.zoomin, R.anim.zoomout);
		} else {
			Toast.makeText(getApplicationContext(), "您还没有登录，请先登录！",
					Toast.LENGTH_SHORT).show();
		}
	}
	
	public void downPortrait()
	{
		userName=sp.getString("userName", "");
		String dir = Environment.getExternalStorageDirectory()
				.getAbsoluteFile() + "/圣经流利说";
		
		File dirFile = new File(Environment.getExternalStorageDirectory()
				.getAbsoluteFile() + "/圣经流利说");
		if (!dirFile.exists()) {
			dirFile.mkdir();
		}
		File file = new File(dir + "/user_" + userName.trim()
				+ ".jpg");

		if (!file.exists()) {
			dialog = new MyProgressPopUpWindow(
					Personal_Activity.this, "正在加载中...").createADialog();
			upload = new UpLoadUserInfo(getApplicationContext(), dialog);
			
			Log.i("portraitUri", "portraitUri"+portraitUri);
			
			upload.downloadFile("user_" + userName.trim() + ".jpg", file
					.getAbsoluteFile().toString(),portrait);
		}
		else
		{
			Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
			portrait.setImageBitmap(bitmap);
		}
	}

}
