package com.orange.about;

import java.util.List;

import com.baidu.frontia.Frontia;
import com.baidu.frontia.FrontiaData;
import com.baidu.frontia.FrontiaQuery;
import com.baidu.frontia.api.FrontiaStorage;
import com.baidu.frontia.api.FrontiaStorageListener;
import com.baidu.frontia.api.FrontiaStorageListener.DataInfoListener;
import com.example.acts.R;
import com.example.acts.R.drawable;
import com.example.acts.R.id;
import com.example.acts.R.layout;
import com.orange.login.Activity_Register;
import com.orange.map.Activity_Map;

import android.R.integer;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnCreateContextMenuListener;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

public class Activity_Talk extends Activity {

	private LinearLayout chat_linearlayout;
	private TextView cend;
	private FrontiaStorage mCloudStorage;
	private String suggestion;
	private EditText et;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_talk);

		boolean isInit = Frontia.init(this.getApplicationContext(), "GdZNITcYcI3bhiwKx7duVbR7");
		if (!isInit) {
			Toast.makeText(getApplicationContext(), "您的app key 错误", 3).show();
			return;
		}
		mCloudStorage = Frontia.getStorage();

		et = (EditText) findViewById(R.id.et);

		chat_linearlayout = (LinearLayout) findViewById(R.id.chat_linearlayout);

		cend = (TextView) findViewById(R.id.cend);

		cend.setOnClickListener(new OnClickListener() {

			@SuppressLint("NewApi")
			@Override
			public void onClick(View v) {
				suggestion = et.getText().toString().trim();
				// setMaxWidth(int maxpixels)

				addData(mCloudStorage);
				// admin(mCloudStorage);

			}
		});

	}

	public void addData(FrontiaStorage mCloudStorage) {
		final FrontiaData[] datas = new FrontiaData[1];
		datas[0] = new FrontiaData();
		datas[0].put("suggestion", suggestion);

		mCloudStorage.insertData(datas[0], new FrontiaStorageListener.DataInsertListener() {

			@SuppressLint("NewApi")
			@Override
			public void onSuccess() {

				Toast.makeText(getApplicationContext(), "发送成功！我们会认真考虑您的建议，如果想与作者进行沟通，请在作者的微博上留言，谢谢！", Toast.LENGTH_SHORT).show();
				TextView chaTextView = new TextView(getApplicationContext());
				chaTextView.setBackground(getResources().getDrawable(R.drawable.chattv_round_defalt));
				chaTextView.setPadding(40, 0, 40, 0);
				chaTextView.setGravity(Gravity.CENTER);
				chaTextView.setTextColor(Color.BLACK);
				chaTextView.setTextSize(22);
				chaTextView.setText(suggestion);
				WindowManager wManager = getWindowManager();
				int maxWidth = wManager.getDefaultDisplay().getWidth() - 100;
				chaTextView.setMaxWidth(maxWidth);
				LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
				lp.topMargin = 15;
				// lp.setMargins(0, 10, 0, 0);
				lp.gravity = Gravity.RIGHT;
				chaTextView.setLayoutParams(lp);
				chat_linearlayout.addView(chaTextView);

			}

			@Override
			public void onFailure(int errCode, String errMsg) {
				Toast.makeText(getApplicationContext(), "注册失败！请检查网络！", Toast.LENGTH_SHORT).show();

			}

		});

	}

}
