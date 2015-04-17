package com.orange.user;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import www.orange.utils.CompressPicture;
import www.orange.utils.MyProgressPopUpWindow;

import com.example.acts.MainActivity;
import com.example.acts.R;
import com.orange.church.Activity_ChurchInfo;
import com.orange.login.Activity_Login;
import com.orange.login.Activity_Register2;
import com.orange.net.UpLoadUserInfo;
import com.orange.read.Activity_ReadBible2;
import com.orange.test.Activity_BibleTestMain;
import com.orange.view.CircleImageView;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.DialogInterface.OnDismissListener;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.DatePicker.OnDateChangedListener;

public class Activity_DetailUserInfo extends Activity {

	private CircleImageView portrait;
	private TextView name_tv;
	private TextView age_tv;
	private TextView agender_tv;
	private TextView qq_tv;
	private TextView area_tv;
	private TextView church_tv;
	private UpLoadUserInfo upload;
	private SharedPreferences sp;
	private String userName="";
	private File dir;
	private File file;
	private int CAMERA_RESULT;
	private int SELECT_PICTURE;
	private AlertDialog alertDialog;
	private TextView sv_menu_right;
	private boolean isUpdateProtrait;
	private Editor editor;
	protected boolean isSelectBirthday;
	private int mYear;
	private int mMonth;
	private int mDay;
	private int bir_year;
	private String gender="";
	private int width;
	private int height;
	private AlertDialog.Builder builder;
	protected String birthday="";
	private AlertDialog dialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_detailuserinfo);

		/*
		 * intent.putExtra("userName", sp.getString("userName", ""));
		 * intent.putExtra("name", sp.getString("name", ""));
		 * intent.putExtra("from", "Personal_Activity");
		 */

		Intent intent = getIntent();
		userName = intent.getStringExtra("userName");

		sv_menu_right = (TextView) findViewById(R.id.sv_menu_right);

		portrait = (CircleImageView) findViewById(R.id.portrait);
		name_tv = (TextView) findViewById(R.id.name_tv);
		age_tv = (TextView) findViewById(R.id.age_tv);
		agender_tv = (TextView) findViewById(R.id.agender_tv);
		qq_tv = (TextView) findViewById(R.id.qq_tv);
		area_tv = (TextView) findViewById(R.id.area_tv);
		//church_tv = (TextView) findViewById(R.id.church_tv);

		/*
		 * TextView name_tv = (TextView) views.get(1); TextView age_tv =
		 * (TextView) views.get(2); TextView location_tv = (TextView)
		 * views.get(3); ImageView gender_iv = (ImageView) views.get(4);
		 * TextView portraitUti = (TextView) views.get(5);
		 */

		List<View> views = new ArrayList<View>();

		views.add(portrait);
		views.add(name_tv);
		views.add(age_tv);
		views.add(area_tv);
		views.add(agender_tv);
		views.add(qq_tv);
		//views.add(church_tv);

		dialog = new MyProgressPopUpWindow(
				Activity_DetailUserInfo.this, "正在加载中...").createADialog();

		sp = getSharedPreferences("UserInfo", Context.MODE_PRIVATE);
		editor = sp.edit();
		upload = new UpLoadUserInfo(getApplicationContext(), dialog);
		upload.getAUserDetail(userName, views);

		dir = new File(Environment.getExternalStorageDirectory()
				.getAbsoluteFile() + "/圣经流利说");
		if (!dir.exists()) {
			dir.mkdir();
		}
		file = new File(dir + "/user_" + userName.trim() + ".jpg");
		
		WindowManager manager = getWindowManager();
		Display display = manager.getDefaultDisplay();
		width = display.getWidth();
		height = display.getHeight();
		
		builder = new AlertDialog.Builder(
				Activity_DetailUserInfo.this, R.style.Dialog1);

	}

	public void protrait(View view) {
		AlertDialog.Builder builder = new AlertDialog.Builder(
				Activity_DetailUserInfo.this, AlertDialog.THEME_HOLO_LIGHT);
		// AlertDialog strengthenDialog=new
		// AlertDialog.Builder(Activity_ReciteMain.this,).create();
		builder.setTitle("选择图片来源");
		builder.setItems(new String[] { "相机", "图库" },
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						if (file.exists()) {
							file.delete();
						}
						if (which == 0) {
							Intent i = new Intent(
									android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
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
							startActivityForResult(
									Intent.createChooser(intent, "选择图片"),
									SELECT_PICTURE);
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
					Log.i("TestFile",
							"SD card is not avaiable/writeable right now.");
					return;
				}
				CompressPicture.getimage(file.getAbsolutePath(), file);
			}
			if (requestCode == CAMERA_RESULT) {

				String sdStatus = Environment.getExternalStorageState();
				if (!sdStatus.equals(Environment.MEDIA_MOUNTED)) { // 检测sd是否可用
					Log.i("TestFile",
							"SD card is not avaiable/writeable right now.");
					return;
				}
				CompressPicture.getimage(file.getAbsolutePath(), file);

			}
			Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
			portrait.setImageBitmap(bitmap);
			portrait.invalidate();
			sv_menu_right.setVisibility(View.VISIBLE);
			sv_menu_right.setClickable(true);
			isUpdateProtrait = true;
		}

	}

	public void name(View view) {
		
		builder.setInverseBackgroundForced(true);
		LayoutInflater inflater = LayoutInflater.from(getApplicationContext());
		View view1 = inflater.inflate(R.layout.dialog_newbookmark, null);
		TextView title=(TextView)view1.findViewById(R.id.title);
		title.setText("昵称");
		Button cancel = (Button) view1.findViewById(R.id.cancel);
		Button certain = (Button) view1.findViewById(R.id.certain);
		final EditText et = (EditText) view1.findViewById(R.id.newbookmark_et);
		et.setHint("请输入昵称");

		cancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				alertDialog.cancel();
			}
		});
		certain.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				alertDialog.cancel();
				name_tv.setText(et.getText().toString().trim());
				sv_menu_right.setClickable(true);
				sv_menu_right.setVisibility(View.VISIBLE);
			}
		});

		alertDialog = builder.create();
		alertDialog.getWindow().clearFlags(
				WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
		alertDialog.show();
		alertDialog.setContentView(view1);
		alertDialog.getWindow().setLayout(7 * width / 8, 500);
	}

	public void age(View view) {
		final Calendar c = Calendar.getInstance();
		mYear = c.get(Calendar.YEAR);
		mMonth = c.get(Calendar.MONTH);
		mDay = c.get(Calendar.DAY_OF_MONTH);
		View view2 = LayoutInflater.from(this).inflate(
				R.layout.activity_dialog, null);
		final AlertDialog.Builder builder = new AlertDialog.Builder(
				Activity_DetailUserInfo.this, AlertDialog.THEME_HOLO_LIGHT);
		builder.setView(view2);
		// builder.setTitle(mYear+"-"+"mMonth"+"-"+mDay);
		final AlertDialog datePickerDialog = builder.create();
		DatePicker datePicker = (DatePicker) view2
				.findViewById(R.id.datePicker);
		TextView certain = (TextView) view2.findViewById(R.id.certain);
		certain.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (isSelectBirthday) {
					age_tv.setText(mYear - bir_year + "");
					sv_menu_right.setVisibility(View.VISIBLE);
					sv_menu_right.setClickable(true);
				}
				datePickerDialog.dismiss();
			}
		});
		datePicker.setCalendarViewShown(false);
		datePicker.init(mYear, mMonth, mDay, new OnDateChangedListener() {

			@Override
			public void onDateChanged(DatePicker view, int year,
					int monthOfYear, int dayOfMonth) {
				int month=monthOfYear+1;
				birthday=year+"-"+month+"-"+dayOfMonth;
				bir_year = year;
				isSelectBirthday = true;
			}
		});
		datePickerDialog.show();
	}
	
	public void gender(View view)
	{
		
		AlertDialog.Builder builder = new AlertDialog.Builder(Activity_DetailUserInfo.this, AlertDialog.THEME_HOLO_LIGHT);

		builder.setTitle("选择教会类型");
		builder.setItems(new String[] { "男", "女" }, new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {

				if (which == 0) {
					gender = "男";
					agender_tv.setText(gender);
					alertDialog.dismiss();
					sv_menu_right.setVisibility(View.VISIBLE);
					sv_menu_right.setClickable(true);
				} else if (which == 1) {
					gender = "女";
					agender_tv.setText(gender);
					alertDialog.dismiss();
					sv_menu_right.setVisibility(View.VISIBLE);
					sv_menu_right.setClickable(true);
				}
			}
		});

		alertDialog = builder.create();
		alertDialog.show();
		

	
	}
	
	public void qq(View view)
	{
		builder.setInverseBackgroundForced(true);
		LayoutInflater inflater = LayoutInflater.from(getApplicationContext());
		View view1 = inflater.inflate(R.layout.dialog_newbookmark, null);
		Button cancel = (Button) view1.findViewById(R.id.cancel);
		Button certain = (Button) view1.findViewById(R.id.certain);
		TextView title=(TextView)view1.findViewById(R.id.title);
		title.setText("QQ账号");
		final EditText et = (EditText) view1.findViewById(R.id.newbookmark_et);
		et.setHint("请输入QQ账号");
		cancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				alertDialog.cancel();
			}
		});
		certain.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				alertDialog.cancel();
				qq_tv.setText(et.getText().toString().trim());
				sv_menu_right.setClickable(true);
				sv_menu_right.setVisibility(View.VISIBLE);
			}
		});

		alertDialog = builder.create();
		alertDialog.getWindow().clearFlags(
				WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
		alertDialog.show();
		alertDialog.setContentView(view1);
		alertDialog.getWindow().setLayout(7 * width / 8, 500);
	}
	
	public void sv_menu_right(View view)
	{
		String churchName=sp.getString("churchName", "");
		String portraitUri=sp.getString("portraitUri", "");
		String password=sp.getString("password", "");
		String name=name_tv.getText().toString().trim();
		String qq=qq_tv.getText().toString().trim();
		if(birthday.equals(""))
		{
			birthday=sp.getString("birthday", "");
		}
		if(gender.equals(""))
		{
			gender=sp.getString("gender", "男");
		}
		dialog.show();
		upload.updateData(churchName, portraitUri, userName, password, name, birthday, gender, qq, isUpdateProtrait,sv_menu_right);
	}
	
	public void back(View view) {
		Intent intent = new Intent(Activity_DetailUserInfo.this, MainActivity.class);
		intent.putExtra("from",2);
		startActivity(intent);
		finish();
		overridePendingTransition(R.anim.zoomin, R.anim.zoomout);
	}

}
