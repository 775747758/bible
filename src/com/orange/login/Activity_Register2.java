package com.orange.login;

import java.io.File;
import java.security.NoSuchAlgorithmException;
import java.util.Calendar;

import www.orange.utils.CompressPicture;
import www.orange.utils.MD5;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.DatePicker;
import android.widget.DatePicker.OnDateChangedListener;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.acts.R;
import com.orange.church.User;
import com.orange.net.UpLoadUserInfo;
import com.orange.view.CircleImageView;
import com.umeng.socialize.bean.Gender;

public class Activity_Register2 extends Activity {

	private CircleImageView portrait_imageview;
	private File dir;
	private int SELECT_PICTURE;
	private int CAMERA_RESULT;
	private AlertDialog alertDialog;
	private File file;
	private int mYear;
	private int mMonth;
	private int mDay;

	private DatePicker datePicker;
	private AlertDialog datePickerDialog;
	private TextView date_tv;
	private TextView certain;
	private TextView birthday_tv;
	private RadioButton man_radio;
	private RadioButton woman_radio;
	private boolean isManChecked=true;
	private String username;
	private String password;
	private EditText name_et;
	private boolean isSelectPortrait;
	private boolean isSelectBirthday;
	private ProgressDialog reDialog;
	private UpLoadUserInfo upload;
	private String gender;
	private String usernameMD5;
	private String passwordMD5;
	private String qq;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_register2);
		
		reDialog = new ProgressDialog(this);
		reDialog.setMessage("正在注册中...");
		
		upload=new UpLoadUserInfo(this, reDialog);
		
		Intent intent=getIntent();
		username=intent.getStringExtra("username");
		password=intent.getStringExtra("password");
		qq=intent.getStringExtra("qq");
		
		usernameMD5="";
		passwordMD5="";
		try {
			usernameMD5 = MD5.getMD5(username);
			passwordMD5 = MD5.getMD5(password);
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		
		name_et=(EditText)findViewById(R.id.name_et);
		portrait_imageview = (CircleImageView) findViewById(R.id.portrait_imageview);
		birthday_tv=(TextView)findViewById(R.id.birthday_tv);
		man_radio=(RadioButton)findViewById(R.id.man_radio);
		woman_radio=(RadioButton)findViewById(R.id.woman_radio);
		
		man_radio.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if(isManChecked)
				{
					man_radio.setChecked(false);
					woman_radio.setChecked(true);
					isManChecked=false;
				}
				else
				{
					man_radio.setChecked(true);
					woman_radio.setChecked(false);
					isManChecked=true;
				}
				
				
			}	
		});
		woman_radio.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				
				if(isManChecked)
				{
					man_radio.setChecked(false);
					woman_radio.setChecked(true);
					isManChecked=false;
				}
				else
				{
					man_radio.setChecked(true);
					woman_radio.setChecked(false);
					isManChecked=true;
				}
			}	
		});
		
		dir = new File(Environment.getExternalStorageDirectory().getAbsoluteFile() + "/圣经流利说");
		if (!dir.exists()) {
			dir.mkdir();
		}

		file = new File(dir + "/user_" + usernameMD5.trim() + ".jpg");

		
		final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);
      
        
        View view=LayoutInflater.from(this).inflate(R.layout.activity_dialog, null);
        final AlertDialog.Builder builder=new AlertDialog.Builder(Activity_Register2.this, AlertDialog.THEME_HOLO_LIGHT);
        builder.setView(view);
        //builder.setTitle(mYear+"-"+"mMonth"+"-"+mDay);
        datePickerDialog = builder.create();
        datePicker=(DatePicker) view.findViewById(R.id.datePicker);
        date_tv=(TextView) view.findViewById(R.id.date_tv);
        date_tv.setText(mYear+"-"+mMonth+"-"+mDay);
        certain=(TextView) view.findViewById(R.id.certain);
        certain.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				isSelectBirthday=true;
				birthday_tv.setText(mYear+"-"+mMonth+"-"+mDay);
				datePickerDialog.dismiss();
			}
		});
        datePicker.setCalendarViewShown(false);
        datePicker.init(mYear, mMonth, mDay, new OnDateChangedListener() {
        	
			@Override
			public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
				
				int month=monthOfYear+1;
				date_tv.setText(year+"-"+month+"-"+dayOfMonth);
				mYear=year;
				mMonth=month;
				mDay=dayOfMonth;
			}
		});
        
        
        
        
	}
	public void portrait(View view) {
		AlertDialog.Builder builder = new AlertDialog.Builder(Activity_Register2.this, AlertDialog.THEME_HOLO_LIGHT);
		// AlertDialog strengthenDialog=new
		// AlertDialog.Builder(Activity_ReciteMain.this,).create();
		builder.setTitle("选择图片来源");
		builder.setItems(new String[] { "相机", "图库" }, new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				if (file.exists()) {
					file.delete();
				}
				if (which == 0) {
					Intent i = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
					i.putExtra("output", Uri.fromFile(file));
					i.putExtra("outputFormat", "JPEG");
					startActivityForResult(i, CAMERA_RESULT);
				} else {
					Intent intent = new Intent();
					intent.setAction(Intent.ACTION_PICK);
					intent.setType("image/*");
					intent.putExtra("crop", "true");
					intent.putExtra("output", Uri.fromFile(file));
					intent.putExtra("outputFormat", "JPEG");
					startActivityForResult(Intent.createChooser(intent, "选择图片"), SELECT_PICTURE);
				}
			}
		});

		alertDialog = builder.create();
		alertDialog.show();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == RESULT_OK) {
			if (requestCode == SELECT_PICTURE) {
				String sdStatus = Environment.getExternalStorageState();
				if (!sdStatus.equals(Environment.MEDIA_MOUNTED)) { // 检测sd是否可用
					Log.i("TestFile", "SD card is not avaiable/writeable right now.");
					return;
				}
				CompressPicture.getimage(file.getAbsolutePath(),file);
			}
			if (requestCode == CAMERA_RESULT) {
				String sdStatus = Environment.getExternalStorageState();
				if (!sdStatus.equals(Environment.MEDIA_MOUNTED)) { // 检测sd是否可用
					Log.i("TestFile", "SD card is not avaiable/writeable right now.");
					return;
				}
				CompressPicture.getimage(file.getAbsolutePath(),file);

			}
			isSelectPortrait=true;
			Bitmap bitmap=BitmapFactory.decodeFile(file.getAbsolutePath());
			portrait_imageview.setImageBitmap(bitmap);
			portrait_imageview.invalidate();
		}

	}
	  
	public void selectBirthday(View view)
	{
		datePickerDialog.show();
	}
	
	public void returnn(View view)
	{
		Intent intent = new Intent(Activity_Register2.this, Activity_Register1.class);
		startActivity(intent);
		overridePendingTransition(R.anim.zoomin, R.anim.zoomout);
	}
	
	public void next(View view)
	{
		if(name_et.getText().toString().length()<1||name_et.getText().toString().length()>10)
		{
			Toast.makeText(getApplicationContext(), "您输入的昵称不符合规范，请重新输入！", Toast.LENGTH_SHORT).show();
			return;
		}
		if(!isSelectBirthday)
		{
			Toast.makeText(getApplicationContext(), "请选择生日！", Toast.LENGTH_SHORT).show();
			return;
		}
		if(!isSelectPortrait)
		{
			Toast.makeText(getApplicationContext(), "请设置头像！", Toast.LENGTH_SHORT).show();
			return;
		}
		if(isManChecked)
		{
			gender="男";
		}
		else
		{
			gender="女";
		}

		User user=new User(usernameMD5, passwordMD5, name_et.getText().toString(), file.getAbsolutePath(), date_tv.getText().toString(),gender,qq);
		upload.addData(user,Activity_Register2.this);
		overridePendingTransition(R.anim.zoomin, R.anim.zoomout);
	
	}
	

}
