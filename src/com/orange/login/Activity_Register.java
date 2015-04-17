package com.orange.login;

import java.util.ArrayList;
import java.util.List;

import com.baidu.frontia.Frontia;
import com.baidu.frontia.FrontiaData;
import com.baidu.frontia.FrontiaQuery;
import com.baidu.frontia.api.FrontiaStorage;
import com.baidu.frontia.api.FrontiaStorageListener;
import com.baidu.frontia.api.FrontiaStorageListener.DataInfoListener;
import com.example.acts.R;
import com.orange.map.Activity_Map;
import com.orange.test.Activity_BibleTestMain;
import com.orange.test.Activity_BibleTest_End;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class Activity_Register extends Activity {
	private String name;
	private String username;
	private String password;
	private boolean isSuccessful;
	private ProgressDialog reDialog;
	private TextView tv;
	private EditText usernameEditText;
	private EditText passwordEditText;
	private EditText nameEditText;
	private Button registerButton;
	private FrontiaStorage mCloudStorage;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_register);

		boolean isInit = Frontia.init(this.getApplicationContext(), "GdZNITcYcI3bhiwKx7duVbR7");
		if (!isInit) {
			Toast.makeText(getApplicationContext(), "您的app key 错误", 3).show();
			return;
		}

		mCloudStorage = Frontia.getStorage();

		nameEditText = (EditText) findViewById(R.id.name);
		usernameEditText = (EditText) findViewById(R.id.username);
		passwordEditText = (EditText) findViewById(R.id.password);
		registerButton = (Button) findViewById(R.id.button);

		registerButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				name = nameEditText.getText().toString().trim();
				username = usernameEditText.getText().toString().trim();
				password = passwordEditText.getText().toString().trim();
				if (name == "" || username == "" || password == "") {
					Toast.makeText(getApplicationContext(), "信息不能为空！", Toast.LENGTH_SHORT).show();
					return;
				}
				if (name.length() > 10) {
					Toast.makeText(getApplicationContext(), "您输入的昵称太长，请重新输入！", Toast.LENGTH_SHORT).show();
					nameEditText.setText("");
					return;
				}
				if (username.length() < 8 || username.length() > 12) {
					Toast.makeText(getApplicationContext(), "您输入的用户名不符合规范，请重新输入！", Toast.LENGTH_SHORT).show();
					usernameEditText.setText("");
					return;
				}
				if (password.length() < 8 || password.length() > 12) {
					Toast.makeText(getApplicationContext(), "您输入的密码不符合规范，请重新输入！", Toast.LENGTH_SHORT).show();
					passwordEditText.setText("");
					return;
				}
				// new
				// AlertDialog.Builder(RegisterActivity.this,AlertDialog.THEME_HOLO_LIGHT).setTitle("请选择经卷").setMessage().show();
				reDialog = new ProgressDialog(Activity_Register.this);
				reDialog.setMessage("正在注册中...");
				reDialog.show();
				isExist(mCloudStorage);

			}
		});

	}

	public void addData(FrontiaStorage mCloudStorage) {
		final FrontiaData[] datas = new FrontiaData[1];
		datas[0] = new FrontiaData();
		datas[0].put("username", username);
		datas[0].put("password", password);
		datas[0].put("name", name);
		SharedPreferences sp = getSharedPreferences("userInfo", Context.MODE_PRIVATE);

		datas[0].put("myLocation", sp.getString("location", ""));
		datas[0].put("myLatitude", sp.getInt("latitude", 0));
		datas[0].put("myLongtitude", sp.getInt("longtitude", 0));

		mCloudStorage.insertData(datas[0], new FrontiaStorageListener.DataInsertListener() {

			@Override
			public void onSuccess() {
				new Thread(new Runnable() {

					@Override
					public void run() {
						SharedPreferences sp = getSharedPreferences("userInfo", Context.MODE_PRIVATE);
						Editor editor = sp.edit();
						editor.putString("username", username);
						editor.putString("password", password);
						editor.putString("name", name);
						editor.putInt("islogin", 1);
						editor.commit();
					}
				}).start();
				reDialog.dismiss();
				Toast.makeText(getApplicationContext(), "注册成功！！", Toast.LENGTH_SHORT).show();
				Intent intent = new Intent(Activity_Register.this, Activity_Map.class);
				startActivity(intent);
				finish();
				overridePendingTransition(R.anim.zoomin, R.anim.zoomout);
			}

			@Override
			public void onFailure(int errCode, String errMsg) {
				reDialog.dismiss();
				Toast.makeText(getApplicationContext(), "注册失败！请检查网络！", Toast.LENGTH_SHORT).show();
				// isSuccessful=false;
			}

		});

	}

	public void isExist(FrontiaStorage mCloudStorage) {
		// FrontiaQuery中有很多查询条件，你可以尝试多种查询条件，相当于sql语句中的where
		FrontiaQuery query = new FrontiaQuery();
		query.equals("username", username);

		mCloudStorage.findData(query, new DataInfoListener() {

			@Override
			public void onSuccess(List<FrontiaData> dataList) {
				StringBuilder sb = new StringBuilder();
				int i = 0;
				for (FrontiaData d : dataList) {
					if (d.get("username").toString().equals(username)) {
						reDialog.dismiss();
						Toast.makeText(getApplicationContext(), "已经有人用过此用户名，请更换！", Toast.LENGTH_SHORT).show();
						return;
					}
				}
				addData(Frontia.getStorage());
				reDialog.dismiss();
			}

			@Override
			public void onFailure(int errCode, String errMsg) {

			}
		});

	}

	public void back(View view) {
		Intent intent = new Intent(Activity_Register.this, Activity_Login.class);
		startActivity(intent);
		finish();
		overridePendingTransition(R.anim.test_main_righttoleft, R.anim.test_main_lefttoright);
	}

}
