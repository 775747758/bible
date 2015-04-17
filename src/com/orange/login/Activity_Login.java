package com.orange.login;

import java.security.NoSuchAlgorithmException;
import java.util.List;

import www.orange.utils.MD5;
import www.orange.utils.MyProgressPopUpWindow;

import com.baidu.frontia.Frontia;
import com.baidu.frontia.FrontiaData;
import com.baidu.frontia.FrontiaQuery;
import com.baidu.frontia.FrontiaUser;
import com.baidu.frontia.api.FrontiaAuthorization;
import com.baidu.frontia.api.FrontiaStorage;
import com.baidu.frontia.api.FrontiaStorageListener;
import com.baidu.frontia.api.FrontiaAuthorization.MediaType;
import com.baidu.frontia.api.FrontiaAuthorizationListener.AuthorizationListener;
import com.baidu.frontia.api.FrontiaStorageListener.DataInfoListener;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.BMapManager;
import com.baidu.mapapi.map.LocationData;
import com.baidu.mapapi.map.MapController;
import com.baidu.mapapi.map.MyLocationOverlay.LocationMode;
import com.baidu.platform.comapi.basestruct.GeoPoint;
import com.example.acts.MainActivity;
import com.example.acts.Main_Activity;
import com.example.acts.R;
import com.orange.church.Activity_ChurchInfo;
import com.orange.church.Activity_ChurchMember;
import com.orange.church.Activity_MyChurch;
import com.orange.map.Activity_Map;
import com.orange.net.UpLoadUserInfo;
import com.orange.read.Activity_SelectVolume;
import com.orange.test.Activity_BibleTestMain;
import com.orange.test.Activity_BibleTest_End;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;
import android.widget.CompoundButton.OnCheckedChangeListener;

public class Activity_Login extends Activity {
	BMapManager mBMapMan = null;

	private String username;
	private String password;
	private boolean isSuccessful;
	private ProgressDialog reDialog;
	// private TextView tv;
	private EditText usernameEditText;
	private EditText passwordEditText;
	private TextView loginButton;
	private FrontiaStorage mCloudStorage;

	private RadioButton autologin;

	private TextView register;

	private FrontiaAuthorization mAuthorization;

	protected String usernameMD5;

	protected String passwordMD5;

	private UpLoadUserInfo upload;

	protected AlertDialog dialog;

	private SharedPreferences spConfig;

	private Editor editor;

	private ToggleButton toggleAutoLogin;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		
		setContentView(R.layout.activity_login1);


		boolean isInit = Frontia.init(this.getApplicationContext(), "GdZNITcYcI3bhiwKx7duVbR7");
		if (!isInit) {
			Toast.makeText(getApplicationContext(), "您的app key 错误", 3).show();
			return;
		}

		mCloudStorage = Frontia.getStorage();

		mAuthorization = Frontia.getAuthorization();

		usernameEditText = (EditText) findViewById(R.id.username);
		passwordEditText = (EditText) findViewById(R.id.password);
		loginButton = (TextView) findViewById(R.id.login);
		register = (TextView) findViewById(R.id.register);

		loginButton.setOnClickListener(new OnClickListener() {

			

			@Override
			public void onClick(View v) {
				username = usernameEditText.getText().toString().trim();
				password = passwordEditText.getText().toString().trim();
				

				if (username.equals("") || password.equals("")) {
					Toast.makeText(getApplicationContext(), "用户名或密码不能为空！", Toast.LENGTH_SHORT).show();
					return;
				}
				
				try {
					usernameMD5=MD5.getMD5(username);
					passwordMD5=MD5.getMD5(password);
				} catch (NoSuchAlgorithmException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				//reDialog.show();
				//AlertDialog dialog=new MyProgressPopUpWindow(Activity_Login.this,"正在查询中...").createADialog();
				
				dialog=new MyProgressPopUpWindow(Activity_Login.this,"正在查询中...").createADialog();
				
				Intent intent = new Intent(Activity_Login.this, MainActivity.class);
				intent.putExtra("from", 2);
				 intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				upload=new UpLoadUserInfo(getApplicationContext(), dialog);
				upload.login(intent, usernameMD5, passwordMD5,Activity_Login.this);
				

			}
		});
		
		spConfig =getSharedPreferences("SystemConfig", Context.MODE_PRIVATE);
		editor = spConfig.edit();
		
		toggleAutoLogin=(ToggleButton)findViewById(R.id.toggleAutoLogin);
		
		toggleAutoLogin.setChecked(spConfig.getBoolean("isAutoLogin", true));
		
		toggleAutoLogin.setOnCheckedChangeListener(new OnCheckedChangeListener(){
            public void onCheckedChanged(CompoundButton buttonView,
                    boolean isChecked) {
            	//toggleLogin.setChecked(isChecked);
				editor.putBoolean("isAutoLogin", isChecked);
				editor.commit();
            }

        });

	}
	
	@Override
	protected void onResume() {
		
		
		
		super.onResume();
	}

	public void register(View view) {
		Intent intent = new Intent(Activity_Login.this, Activity_Register1.class);
		startActivity(intent);
		finish();
		overridePendingTransition(R.anim.zoomin, R.anim.zoomout);
	}

	public void back(View view) {
		Intent intent = new Intent(Activity_Login.this, MainActivity.class);
		startActivity(intent);
		finish();
		overridePendingTransition(R.anim.zoomin, R.anim.zoomout);
	}





}
