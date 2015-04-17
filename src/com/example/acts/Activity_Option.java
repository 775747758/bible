package com.example.acts;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.R.integer;
import android.annotation.SuppressLint;
import android.app.ActionBar.LayoutParams;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;


import com.orange.read.Activity_SelectVolume;

public class Activity_Option extends Activity {
	private List<String> volumeList = new ArrayList<String>();
	private List<String> chapterList = new ArrayList<String>();
	private List<String> nodeList = new ArrayList<String>();

	private TextView cancel;
	private TextView certern;

	TextView segment;
	TextView segment_today;
	TextView content;
	TextView content_today;
	private int SELECT_PICTURE;
	private int CAMERA_RESULT;
	private File galleryFile;
	private File cameraFile;
	private String Path;
	private ColorPickerDialog dialog;
	private ArrayList<Map<String, String>> Data;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_option);

		SharedPreferences sp1 = getSharedPreferences("config", Context.MODE_PRIVATE);

		segment = (TextView) findViewById(R.id.segment);
		segment_today = (TextView) findViewById(R.id.segment_today);
		content = (TextView) findViewById(R.id.content);
		content_today = (TextView) findViewById(R.id.content_taday);

		Path = Environment.getExternalStorageDirectory().getAbsolutePath();
		this.galleryFile = new File(Path + "/backround.jpg");
		this.cameraFile = new File(Path + "/backround.jpg");

		int segment_color = sp1.getInt("segment_color", R.color.pink);
		int content_color = sp1.getInt("content_color", R.color.white);

		int segment_color_taday = sp1.getInt("segment_color_today", R.color.green);
		int content_color_taday = sp1.getInt("content_color_today", R.color.green);

		if (segment_color_taday == R.color.green) {
			segment_today.setTextColor(getResources().getColor(segment_color_taday));
		} else {
			segment_today.setTextColor(segment_color_taday);
		}

		if (content_color_taday == R.color.green) {
			content_today.setTextColor(getResources().getColor(content_color_taday));
		} else {
			content_today.setTextColor(content_color_taday);
		}

		if (segment_color == R.color.pink && content_color == R.color.white) {
			content.setTextColor(getResources().getColor(content_color));
			segment.setTextColor(getResources().getColor(segment_color));
		}
		if (segment_color == R.color.pink && content_color != R.color.white) {
			content.setTextColor(segment_color);
			segment.setTextColor(getResources().getColor(segment_color));
		}

		if (segment_color != R.color.pink && content_color == R.color.white) {
			content.setTextColor(getResources().getColor(content_color));
			segment.setTextColor(segment_color);
		}
		if (segment_color != R.color.pink && content_color != R.color.white) {
			content.setTextColor(content_color);
			segment.setTextColor(segment_color);
		}

	}

	public void bg_camera(View view) {

		Intent i = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
		i.putExtra("output", Uri.fromFile(cameraFile));
		i.putExtra("outputFormat", "JPEG");
		startActivityForResult(i, CAMERA_RESULT);
	}

	public void bg_gallery(View view) {

		Intent intent = new Intent();
		intent.setAction(Intent.ACTION_PICK);
		intent.setType("image/*");
		// startActivityForResult(intent, 0);
		intent.putExtra("crop", "true");
		intent.putExtra("output", Uri.fromFile(galleryFile));
		intent.putExtra("outputFormat", "JPEG");
		startActivityForResult(Intent.createChooser(intent, "选择图片"), SELECT_PICTURE);
		// System.out.println(packagePath);
		// Toast.makeText(OptionActivity.this, Path, Toast.LENGTH_LONG).show();
	}

	public void segment_color(View view) {

		dialog = new ColorPickerDialog(Activity_Option.this, "经节颜色设置", new ColorPickerDialog.OnColorChangedListener() {
			@Override
			public void colorChanged(int color) {
				// TODO Auto-generated method stub
				segment.setTextColor(color);
				final int colortemp = color;
				new Thread(new Runnable() {

					@Override
					public void run() {
						SharedPreferences sp = getSharedPreferences("config", Context.MODE_PRIVATE);
						Editor editor = sp.edit();
						editor.putInt("segment_color", colortemp);
						editor.commit();

					}
				}).start();

			}
		});
		dialog.show();
	}

	public void content_color(View view) {

		dialog = new ColorPickerDialog(Activity_Option.this, "经节颜色设置", new ColorPickerDialog.OnColorChangedListener() {
			@Override
			public void colorChanged(int color) {
				// TODO Auto-generated method stub
				content.setTextColor(color);
				final int colortemp = color;
				new Thread(new Runnable() {

					@Override
					public void run() {
						SharedPreferences sp = getSharedPreferences("config", Context.MODE_PRIVATE);
						Editor editor = sp.edit();
						editor.putInt("content_color", colortemp);
						editor.commit();

					}
				}).start();

			}
		});
		dialog.show();
	}

	public void segment_color_today(View view) {

		dialog = new ColorPickerDialog(Activity_Option.this, "经节颜色设置", new ColorPickerDialog.OnColorChangedListener() {
			@Override
			public void colorChanged(int color) {
				// TODO Auto-generated method stub
				segment_today.setTextColor(color);
				final int colortemp = color;
				new Thread(new Runnable() {

					@Override
					public void run() {
						SharedPreferences sp = getSharedPreferences("config", Context.MODE_PRIVATE);
						Editor editor = sp.edit();
						editor.putInt("segment_color_today", colortemp);
						editor.commit();

					}
				}).start();

			}
		});
		dialog.show();
	}

	public void content_color_today(View view) {

		dialog = new ColorPickerDialog(Activity_Option.this, "经文颜色设置", new ColorPickerDialog.OnColorChangedListener() {
			@Override
			public void colorChanged(int color) {
				// TODO Auto-generated method stub
				content_today.setTextColor(color);
				final int colortemp = color;
				new Thread(new Runnable() {

					@Override
					public void run() {
						SharedPreferences sp = getSharedPreferences("config", Context.MODE_PRIVATE);
						Editor editor = sp.edit();
						editor.putInt("content_color_today", colortemp);
						editor.commit();

					}
				}).start();

			}
		});
		dialog.show();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		/*
		 * if(data!=null) { Uri uri=data.getData();//图片路径 SharedPreferences
		 * sp=getSharedPreferences("config", Context.MODE_PRIVATE); Editor
		 * editor=sp.edit(); editor.putString("lv_backgrond_uri",uri+"");
		 * editor.commit(); }
		 */
		if (resultCode == RESULT_OK) {
			if (requestCode == SELECT_PICTURE) {
				new Thread(new Runnable() {

					@Override
					public void run() {
						SharedPreferences sp = getSharedPreferences("config", Context.MODE_PRIVATE);
						Editor editor = sp.edit();
						editor.putString("lv_backgrond_uri", galleryFile.getAbsolutePath());
						editor.putInt("lv_backgrond", 100);
						editor.commit();

					}
				}).start();

				// button.setBackgroundDrawable(Drawable.createFromPath(tempFile.getAbsolutePath()));
			}
			if (requestCode == CAMERA_RESULT) {
				new Thread(new Runnable() {

					@Override
					public void run() {
						SharedPreferences sp = getSharedPreferences("config", Context.MODE_PRIVATE);
						Editor editor = sp.edit();
						editor.putString("lv_backgrond_uri", cameraFile.getAbsolutePath());
						editor.putInt("lv_backgrond", 100);
						editor.commit();

					}
				}).start();

				// button.setBackgroundDrawable(Drawable.createFromPath(tempFile.getAbsolutePath()));
			}

		}

	}

	public void back(View view) {
		Intent intent = new Intent(Activity_Option.this, Activity_SelectVolume.class);
		startActivity(intent);
		finish();
		overridePendingTransition(R.anim.zoomin, R.anim.zoomout);
	}

	public void addshortcut(View view) {
		Intent intent = new Intent();
		intent.setAction("com.android.launcher.action.INSTALL_SHORTCUT");
		intent.putExtra(Intent.EXTRA_SHORTCUT_NAME, "圣经流利说");
		intent.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE, Intent.ShortcutIconResource.fromContext(this, R.drawable.ic_launcher_round));
		intent.putExtra(Intent.EXTRA_SHORTCUT_INTENT, new Intent(this, Activity_Splash.class));
		sendBroadcast(intent);
		Toast.makeText(getApplicationContext(), "已添加成功！", Toast.LENGTH_SHORT).show();

	}

}
