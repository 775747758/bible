package com.orange.church;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import www.orange.utils.CompressPicture;
import www.orange.utils.MyProgressPopUpWindow;
import www.orange.utils.compressPic;

import com.example.acts.R;
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
import android.graphics.BitmapFactory.Options;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class Activity_CreateChurch extends Activity {

	private File dir;
	private File file;
	private int CAMERA_RESULT;
	private int SELECT_PICTURE;
	private AlertDialog alertDialog;
	private AlertDialog typeAlertDialog;
	private String churchType;
	private TextView church_type;
	private boolean isSelectPortrait;
	private CircleImageView church_icon;
	private EditText churchname_et;
	private EditText mission_et;
	private EditText sight_et;
	private ProgressDialog reDialog;
	private UpLoadChurchInfo upLoadChurchInfo;
	private String churchName;
	private AlertDialog dialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_createchurch);

		church_type = (TextView) findViewById(R.id.church_type);
		church_icon = (CircleImageView) findViewById(R.id.church_icon);
		mission_et = (EditText) findViewById(R.id.mission_et);
		sight_et = (EditText) findViewById(R.id.sight_et);

		Intent intent = getIntent();
		churchName = intent.getStringExtra("churchName");

		dir = new File(Environment.getExternalStorageDirectory().getAbsoluteFile() + "/圣经流利说");
		if (!dir.exists()) {
			dir.mkdir();
		}
		file = new File(dir + "/church_" + churchName.trim() + ".jpg");

		
		
	}

	public void church_icon(View view) {
		AlertDialog.Builder builder = new AlertDialog.Builder(Activity_CreateChurch.this, AlertDialog.THEME_HOLO_LIGHT);
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
					// i.putExtra("output", Uri.fromFile(file));
					// i.putExtra("outputFormat", "JPEG");
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

	public void church_type(View view) {
		AlertDialog.Builder builder = new AlertDialog.Builder(Activity_CreateChurch.this, AlertDialog.THEME_HOLO_LIGHT);
		// AlertDialog strengthenDialog=new
		// AlertDialog.Builder(Activity_ReciteMain.this,).create();
		builder.setTitle("选择教会类型");
		builder.setItems(new String[] { "三自教会", "细胞教会", "家庭教会" }, new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {

				if (which == 0) {
					churchType = "三自教会";
					church_type.setText(churchType);
				} else if (which == 1) {
					churchType = "细胞教会";
					church_type.setText(churchType);
				} else {
					churchType = "家庭教会";
					church_type.setText(churchType);
				}
			}
		});

		typeAlertDialog = builder.create();
		typeAlertDialog.show();
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
				CompressPicture.getimage(file.getAbsolutePath(), file);

			}
			if (requestCode == CAMERA_RESULT) {
				String sdStatus = Environment.getExternalStorageState();
				if (!sdStatus.equals(Environment.MEDIA_MOUNTED)) { // 检测sd是否可用
					Log.i("TestFile", "SD card is not avaiable/writeable right now.");
					return;
				}
				CompressPicture.getimage(file.getAbsolutePath(), file);

			}
			/*
			 * Bundle bundle = data.getExtras(); Bitmap bitmap = (Bitmap)
			 * bundle.get("data");// 获取相机返回的数据，并转换为Bitmap图片格式 new
			 * compressPic().compressBmpToFile(bitmap, file);
			 */
			isSelectPortrait = true;
			/*
			 * Bitmap bitmap=BitmapFactory.decodeFile(file.getAbsolutePath());
			 * BitmapFactory.Options opts=new Options();
			 * opts.inJustDecodeBounds=false; opts.in
			 */
			Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
			church_icon.setImageBitmap(bitmap);
			church_icon.invalidate();
		}

	}

	public void next(View view) {

		if (churchType == null || churchType.equals("")) {
			Toast.makeText(getApplicationContext(), "请选择教会类型！", Toast.LENGTH_SHORT).show();
			return;
		}
		if (!isSelectPortrait) {
			Toast.makeText(getApplicationContext(), "请设置教会标志！", Toast.LENGTH_SHORT).show();
			return;
		}
		if (mission_et.getText().toString().length() < 1 || mission_et.getText().toString().length() > 100) {
			Toast.makeText(getApplicationContext(), "请填写教会使命！", Toast.LENGTH_SHORT).show();
			return;
		}
		if (sight_et.getText().toString().length() > 100) {
			Toast.makeText(getApplicationContext(), "您填写的教会异象字数太多！", Toast.LENGTH_SHORT).show();
			return;
		}
		dialog=new MyProgressPopUpWindow(Activity_CreateChurch.this,"正在创建中...").createADialog();
		upLoadChurchInfo = new UpLoadChurchInfo(this, dialog);
		SharedPreferences sp = getSharedPreferences("UserInfo", Context.MODE_PRIVATE);

		Church church = new Church(sp.getString("name", ""), churchName, churchType, mission_et.getText().toString().trim(), sight_et.getText().toString()
				.trim(), file.getAbsolutePath());
		upLoadChurchInfo.addData(church);

	}

}
