package com.orange.church;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import www.orange.utils.CompressPicture;
import www.orange.utils.Date;
import www.orange.utils.MyProgressPopUpWindow;

import com.example.acts.MainActivity;
import com.example.acts.R;
import com.orange.login.Activity_Register2;
import com.orange.net.UpLoadChurchInfo;
import com.orange.net.UpLoadToten;
import com.orange.test.Activity_BibleTestMain;
import com.orange.view.CircleImageView;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class Activity_ChurchInfo extends Activity {

	private SharedPreferences sp;
	private String pastorName;
	private String churchIconUri;
	private String churchName;
	private String churchType;
	private String churchCreateDate;
	private String churchLocation;
	private String churchCount;
	private String churchMission;
	private String churchSight;
	private String churchNotice;
	private CircleImageView portrait;
	private TextView name_tv;
	private TextView pastorName_tv;
	private TextView type_tv;
	private TextView createdate_tv;
	private TextView area_tv;
	private TextView count_tv;
	private TextView mission_tv;
	private TextView sight_tv;
	private TextView notice_tv;
	private ProgressDialog reDialog;
	private Button join_bt;
	private RelativeLayout portrait_l;
	private File dir;
	private File file;
	private int CAMERA_RESULT;
	private int SELECT_PICTURE;
	private TextView sv_menu_right;
	private LinearLayout type_l;
	private LinearLayout mission_l;
	private LinearLayout sight_l;
	private LinearLayout notice_l;
	private boolean isUpdateProtrait = false;
	private String churchNameIntent;
	private String pastorNameSp;
	private String nameSp;
	private String from;
	private int applyCount;
	private AlertDialog dialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_churchinfo);

		join_bt = (Button) findViewById(R.id.join_bt);

		sv_menu_right = (TextView) findViewById(R.id.sv_menu_right);

		portrait = (CircleImageView) findViewById(R.id.portrait);
		portrait_l = (RelativeLayout) findViewById(R.id.protrait_l);

		name_tv = (TextView) findViewById(R.id.name_tv);
		pastorName_tv = (TextView) findViewById(R.id.pastorName_tv);

		type_tv = (TextView) findViewById(R.id.type_tv);
		type_l = (LinearLayout) findViewById(R.id.type_l);

		createdate_tv = (TextView) findViewById(R.id.createdate_tv);
		area_tv = (TextView) findViewById(R.id.area_tv);
		count_tv = (TextView) findViewById(R.id.count_tv);

		mission_tv = (TextView) findViewById(R.id.mission_tv);
		mission_l = (LinearLayout) findViewById(R.id.mission_l);

		sight_tv = (TextView) findViewById(R.id.sight_tv);
		sight_l = (LinearLayout) findViewById(R.id.sight_l);

		notice_tv = (TextView) findViewById(R.id.notice_tv);
		notice_l = (LinearLayout) findViewById(R.id.notice_l);

		List<View> views = new ArrayList<View>();
		views.add(portrait);
		views.add(name_tv);
		views.add(pastorName_tv);
		views.add(type_tv);
		views.add(createdate_tv);
		views.add(area_tv);
		views.add(count_tv);
		views.add(mission_tv);
		views.add(sight_tv);
		views.add(notice_tv);

		Intent intent = getIntent();
		from = intent.getStringExtra("from");
		churchNameIntent = intent.getStringExtra("churchName");

		sp = getSharedPreferences("ChurchInfo", Context.MODE_PRIVATE);
		pastorName = sp.getString("pastorName", "");
		churchIconUri = sp.getString("churchIconUri", "");
		churchName = sp.getString("churchName", "");
		churchType = sp.getString("churchType", "");
		churchCreateDate = sp.getString("churchCreateDate", "");
		churchLocation = sp.getString("churchLocation", "");
		churchCount = sp.getString("churchCount", "");
		churchMission = sp.getString("churchMission", "");
		churchSight = sp.getString("churchSight", "");
		churchNotice = sp.getString("churchNotice", "");

		dir = new File(Environment.getExternalStorageDirectory().getAbsoluteFile() + "/圣经流利说");
		if (!dir.exists()) {
			dir.mkdir();
		}

		// if(from.equals("Activity_JoinChurch"))

		dialog=new MyProgressPopUpWindow(Activity_ChurchInfo.this,"正在查询中...").createADialog();
		UpLoadChurchInfo upload = new UpLoadChurchInfo(this, dialog);
		//upload.getAChurch(churchNameIntent, views);
		if (from.equals("Activity_JoinChurch")) {
			join_bt.setVisibility(View.VISIBLE);
			file = new File(dir + "/church_" + churchNameIntent.trim() + ".jpg");
			upload.getAChurch(churchNameIntent, views);
		} else if (from.equals("Activity_MyChurch")) {
			file = new File(dir + "/church_" + churchName.trim() + ".jpg");
			upload.getAChurch(churchName, views);
		}
		else
		{
			upload.getAChurch(churchNameIntent, views);
		}
		/*
		 * if (from == null) { sp = getSharedPreferences("ChurchInfo",
		 * Context.MODE_PRIVATE); pastorName = sp.getString("pastorName", "");
		 * churchIconUri = sp.getString("churchIconUri", ""); churchName =
		 * sp.getString("churchName", ""); churchType =
		 * sp.getString("churchType", ""); churchCreateDate =
		 * sp.getString("churchCreateDate", ""); churchLocation =
		 * sp.getString("churchLocation", ""); churchCount =
		 * sp.getString("churchCount", ""); churchMission =
		 * sp.getString("churchMission", ""); churchSight =
		 * sp.getString("churchSight", ""); churchNotice =
		 * sp.getString("churchNotice", "");
		 * 
		 * Bitmap bitmap = BitmapFactory.decodeFile(churchIconUri);
		 * portrait.setImageBitmap(bitmap); name_tv.setText(churchName);
		 * pastorName_tv.setText(pastorName); type_tv.setText(churchType);
		 * createdate_tv.setText(churchCreateDate);
		 * area_tv.setText(churchLocation); count_tv.setText(churchCount);
		 * mission_tv.setText(churchMission); sight_tv.setText(churchSight);
		 * notice_tv.setText(churchNotice);
		 * 
		 * } else { join_bt.setVisibility(View.VISIBLE);
		 * join_bt.setVisibility(View.VISIBLE); reDialog = new
		 * ProgressDialog(this); reDialog.setMessage("正在查询中...");
		 * UpLoadChurchInfo upload = new UpLoadChurchInfo(this, reDialog);
		 * upload.getAChurch(churchName, views); }
		 */

		final SharedPreferences sp1 = getSharedPreferences("ChurchInfo", Context.MODE_PRIVATE);
		pastorNameSp = sp1.getString("pastorName", "");
		SharedPreferences spUser = getSharedPreferences("UserInfo", Context.MODE_PRIVATE);
		nameSp = spUser.getString("name", "");
		if (nameSp.equals(pastorName)) {
			sv_menu_right.setVisibility(View.VISIBLE);
			sv_menu_right.setClickable(false);
			portrait_l.setOnClickListener(new OnClickListener() {

				private AlertDialog alertDialog;

				@Override
				public void onClick(View v) {
					AlertDialog.Builder builder = new AlertDialog.Builder(Activity_ChurchInfo.this, AlertDialog.THEME_HOLO_LIGHT);
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
			});

			type_l.setOnClickListener(new OnClickListener() {

				private AlertDialog typeAlertDialog;

				@Override
				public void onClick(View v) {
					SharedPreferences spf = getSharedPreferences("ChurchInfo", Context.MODE_PRIVATE);
					final Editor editor = spf.edit();

					AlertDialog.Builder builder = new AlertDialog.Builder(Activity_ChurchInfo.this, AlertDialog.THEME_HOLO_LIGHT);
					// AlertDialog strengthenDialog=new
					// AlertDialog.Builder(Activity_ReciteMain.this,).create();
					builder.setTitle("选择教会类型");
					builder.setItems(new String[] { "三自教会", "细胞教会", "家庭教会" }, new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {

							if (which == 0) {
								churchType = "三自教会";

								type_tv.setText(churchType);
							} else if (which == 1) {
								churchType = "细胞教会";
								type_tv.setText(churchType);
							} else {
								churchType = "家庭教会";
								type_tv.setText(churchType);
							}
						}
					});

					typeAlertDialog = builder.create();
					typeAlertDialog.show();
					typeAlertDialog.setOnDismissListener(new OnDismissListener() {

						@Override
						public void onDismiss(DialogInterface dialog) {
							editor.putString("churchType", churchType);
							editor.commit();
							sv_menu_right.setClickable(true);
						}
					});

				}
			});

			mission_l.setOnClickListener(new OnClickListener() {

				SharedPreferences spf = getSharedPreferences("ChurchInfo", Context.MODE_PRIVATE);
				final Editor editor = spf.edit();

				@Override
				public void onClick(View v) {
					AlertDialog missionAlertDialog;
					final EditText et = new EditText(getApplicationContext());
					et.setText(churchMission);
					et.setBackgroundColor(Color.TRANSPARENT);
					et.setTextColor(Color.BLACK);
					et.setGravity(Gravity.CENTER);
					AlertDialog.Builder builder = new AlertDialog.Builder(Activity_ChurchInfo.this, AlertDialog.THEME_HOLO_LIGHT);
					// AlertDialog strengthenDialog=new
					// AlertDialog.Builder(Activity_ReciteMain.this,).create();
					builder.setTitle("教会使命");
					builder.setView(et);
					builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {

						public void onClick(DialogInterface dialog, int which) {
							dialog.dismiss();
						}
					});
					builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {

						public void onClick(DialogInterface dialog, int which) {
							dialog.dismiss();
							editor.putString("churchMission", et.getText().toString().trim());
							editor.commit();
							mission_tv.setText(et.getText().toString().trim());
							sv_menu_right.setClickable(true);
						}
					});

					missionAlertDialog = builder.create();
					missionAlertDialog.show();

				}
			});

			sight_l.setOnClickListener(new OnClickListener() {

				SharedPreferences spf = getSharedPreferences("ChurchInfo", Context.MODE_PRIVATE);
				final Editor editor = spf.edit();

				@Override
				public void onClick(View v) {
					AlertDialog sightAlertDialog;
					final EditText et = new EditText(getApplicationContext());
					et.setText(churchSight);
					et.setBackgroundColor(Color.TRANSPARENT);
					et.setTextColor(Color.BLACK);
					et.setGravity(Gravity.CENTER);
					AlertDialog.Builder builder = new AlertDialog.Builder(Activity_ChurchInfo.this, AlertDialog.THEME_HOLO_LIGHT);
					// AlertDialog strengthenDialog=new
					// AlertDialog.Builder(Activity_ReciteMain.this,).create();
					builder.setTitle("教会异象");
					builder.setView(et);
					builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {

						public void onClick(DialogInterface dialog, int which) {
							dialog.dismiss();
						}
					});
					builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {

						public void onClick(DialogInterface dialog, int which) {
							dialog.dismiss();
							editor.putString("churchSight", et.getText().toString().trim());
							editor.commit();
							sight_tv.setText(et.getText().toString().trim());
							sv_menu_right.setClickable(true);
						}
					});
					sightAlertDialog = builder.create();
					sightAlertDialog.show();

				}
			});

			notice_l.setOnClickListener(new OnClickListener() {

				SharedPreferences spf = getSharedPreferences("ChurchInfo", Context.MODE_PRIVATE);
				final Editor editor = spf.edit();

				@Override
				public void onClick(View v) {
					AlertDialog sightAlertDialog;
					final EditText et = new EditText(getApplicationContext());
					et.setText(churchNotice);
					et.setBackgroundColor(Color.TRANSPARENT);
					et.setTextColor(Color.BLACK);
					et.setGravity(Gravity.CENTER);
					AlertDialog.Builder builder = new AlertDialog.Builder(Activity_ChurchInfo.this, AlertDialog.THEME_HOLO_LIGHT);
					// AlertDialog strengthenDialog=new
					// AlertDialog.Builder(Activity_ReciteMain.this,).create();
					builder.setTitle("信仰告白");
					builder.setView(et);
					builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {

						public void onClick(DialogInterface dialog, int which) {
							dialog.dismiss();
						}
					});
					builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {

						public void onClick(DialogInterface dialog, int which) {
							dialog.dismiss();
							editor.putString("churchNotice", et.getText().toString().trim());
							editor.commit();
							notice_tv.setText(et.getText().toString().trim());
							sv_menu_right.setClickable(true);
						}
					});
					sightAlertDialog = builder.create();
					sightAlertDialog.show();

				}
			});

		}

	}

	public void join(View view) {
		if (applyCount >= 1) {
			Toast.makeText(getApplicationContext(), "不能重复提交", Toast.LENGTH_SHORT).show();
		} else {
			applyCount++;
			dialog=new MyProgressPopUpWindow(Activity_ChurchInfo.this,"正在提交中...").createADialog();
			UpLoadToten upload = new UpLoadToten(getApplicationContext(), dialog);
			SharedPreferences sp = getSharedPreferences("UserInfo", Context.MODE_PRIVATE);
			if (from.equals("Activity_JoinChurch")) {
				upload.isApply(churchNameIntent, "apply", sp.getString("userName", ""), sp.getString("name", ""), sp.getString("portraitUri", ""),
						pastorName_tv.getText().toString().trim());
			} else if (from.equals("Activity_MyChurch")) {
				upload.applyChurch(churchName, "apply", sp.getString("userName", ""), sp.getString("name", ""), sp.getString("portraitUri", ""), pastorName_tv
						.getText().toString().trim());
			}

		}

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
			Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
			portrait.setImageBitmap(bitmap);
			portrait.invalidate();
			sv_menu_right.setClickable(true);
			isUpdateProtrait = true;
		}

	}

	public void sv_menu_right(View view) {

		dialog=new MyProgressPopUpWindow(Activity_ChurchInfo.this,"正在提交中...").createADialog();
		UpLoadChurchInfo update = new UpLoadChurchInfo(this, dialog);
		SharedPreferences sp = getSharedPreferences("ChurchInfo", Context.MODE_PRIVATE);
		update.updateData(churchName, sp.getString("pastorName", ""), sp.getString("churchType", ""), sp.getString("churchMission", ""),
				sp.getString("churchSight", ""), sp.getString("churchIconUri", ""), sp.getString("churchLocation", ""), sp.getString("churchLatitude", ""),
				sp.getString("churchLongtitude", ""), sp.getString("churchCreateDate", ""), sp.getString("churchCount", ""),sp.getString("churchNotice", ""), isUpdateProtrait);

	}
	
	public void back(View view) {
		if (from.equals("Activity_JoinChurch")) 
		{
			Intent intent = new Intent(Activity_ChurchInfo.this,Activity_ChurchMember.class);
			startActivity(intent);
		}
		else if (from.equals("Activity_MyChurch")) {
			Intent intent = new Intent(Activity_ChurchInfo.this,Activity_MyChurch.class);
			startActivity(intent);
		}
		finish();
		overridePendingTransition(R.anim.zoomin, R.anim.zoomout);
	}
}
