package com.orange.church;

import java.io.File;

import www.orange.utils.MyProgressPopUpWindow;

import com.example.acts.MainActivity;
import com.example.acts.R;
import com.orange.login.Activity_Login;
import com.orange.login.Activity_Register2;
import com.orange.net.UpLoadChurchInfo;
import com.orange.net.UpLoadUserInfo;
import com.orange.view.CircleImageView;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class Activity_CreateChurch0 extends Activity {

	private ProgressDialog reDialog;
	private UpLoadChurchInfo upLoadChurchInfo;
	private EditText churchName_et;
	private AlertDialog dialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_createchurch0);

		
		churchName_et=(EditText)findViewById(R.id.churchName_et);
		
	}

	public void next(View view) {
		
		if(churchName_et.getText().toString().trim().equals(""))
		{
			Toast.makeText(getApplicationContext(), "请填写教会名称！", Toast.LENGTH_SHORT).show();
			return;
		}
		dialog=new MyProgressPopUpWindow(Activity_CreateChurch0.this,"正在查询中...").createADialog();
		upLoadChurchInfo = new UpLoadChurchInfo(this, dialog);
		Intent intent = new Intent(Activity_CreateChurch0.this, Activity_CreateChurch.class);
		//startActivity(intent);
		overridePendingTransition(R.anim.zoomin, R.anim.zoomout);
		upLoadChurchInfo.isExistChurch(churchName_et.getText().toString(),intent);

	}
	public void returnn(View view)
	{
		Intent intent = new Intent(Activity_CreateChurch0.this, MainActivity.class);
		intent.putExtra("from", "1");
		startActivity(intent);
		finish();
		overridePendingTransition(R.anim.zoomin, R.anim.zoomout);
	}

}
