package com.orange.read;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.example.acts.DatabaseHelper;
import com.example.acts.MainActivity;
import com.example.acts.R;
import com.orange.recite.Activity_ReciteMain;
import com.orange.test.Activity_BibleTestMain;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils.TruncateAt;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ScrollView;
import android.widget.TextView;

public class Activity_BookMark extends Activity implements OnClickListener ,OnLongClickListener{
	
	private DatabaseHelper dbHelper;
	private SQLiteDatabase db;
	private List<Map<String, String>> bookMarkList=new ArrayList<Map<String, String>>();
	private LinearLayout parent_view;
	private int i=0;
	private List<LinearLayout> list=new ArrayList<LinearLayout>();
	private int j;
	private AlertDialog Alert;
	private TextView nobookmark_tv;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_bookmark);
		
		
		
		new AsyncTask<Void, Void, Void>() {
			
			
			
			@Override
			protected Void doInBackground(Void... params) {
				getBookMarkData();
				return null;
			}
			protected void onPostExecute(Void result) {
				
				Log.i("bookMarkList", bookMarkList.size()+"hh");
				initView();
				if(bookMarkList.size()==0)
				{
					nobookmark_tv.setVisibility(View.VISIBLE);
				}
			//	setOnClick();
			};
		}.execute();
	}
	
	public void getBookMarkData() {
		dbHelper = new DatabaseHelper(getApplicationContext(),"bible.db");
		db = dbHelper.getReadableDatabase();
		Cursor cursorTemp = db.rawQuery("select chapterIndexInBook, name ,date, volume ,chapter from bookmark",null);
		while (cursorTemp.moveToNext()) 
		{
			Map<String, String> map=new HashMap<String, String>();
			String name = cursorTemp.getString(cursorTemp.getColumnIndex("name"));
			String date = cursorTemp.getString(cursorTemp.getColumnIndex("date"));
			String volume = cursorTemp.getString(cursorTemp.getColumnIndex("volume"));
			String chapter = cursorTemp.getString(cursorTemp.getColumnIndex("chapter"));
			int chapterIndexInBook = cursorTemp.getInt(cursorTemp.getColumnIndex("chapterIndexInBook"));
			map.put("name", name);
			map.put("date", date);
			map.put("volume", volume);
			map.put("chapter", chapter);
			map.put("chapterIndexInBook", chapterIndexInBook+"");
			bookMarkList.add(map);
		}
		db.close();
	}
	
	public void initView()
	{
		parent_view=(LinearLayout)findViewById(R.id.parent_view);
		nobookmark_tv=(TextView)findViewById(R.id.nobookmark_tv);
		for(i=0;i<bookMarkList.size();i++)
		{
			
			LinearLayout outLinearLayout=new LinearLayout(getApplicationContext());
			LinearLayout.LayoutParams layoutParams =new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,340);
			layoutParams.setMargins(40, 40, 40, 0);
			outLinearLayout.setOrientation(LinearLayout.VERTICAL);
			outLinearLayout.setBackgroundResource(R.drawable.selector_dialog_button);
			outLinearLayout.setLayoutParams(layoutParams);
			outLinearLayout.setClickable(true);
			outLinearLayout.setTag(i+"");
			outLinearLayout.setId(i);
			outLinearLayout.setOnClickListener(this);
			outLinearLayout.setOnLongClickListener(this);
			
			
			TextView volume_chapter=new TextView(getApplicationContext());
			LinearLayout.LayoutParams layoutParamsTv =new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,200);
			layoutParamsTv.setMargins(20, 20, 20, 0);
			volume_chapter.setBackgroundColor(Color.parseColor("#F9F4E7"));
			volume_chapter.setGravity(Gravity.CENTER);
			volume_chapter.setTextColor(Color.parseColor("#000000"));
			volume_chapter.setTextSize(18);
			volume_chapter.setLayoutParams(layoutParamsTv);
			volume_chapter.setText(bookMarkList.get(i).get("volume")+"  "+bookMarkList.get(i).get("chapter"));
			
			LinearLayout inLinearLayout=new LinearLayout(getApplicationContext());
			LinearLayout.LayoutParams layoutParamsIn =new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,140);
			inLinearLayout.setOrientation(LinearLayout.HORIZONTAL);
			inLinearLayout.setLayoutParams(layoutParamsIn);
			
			TextView name_tv=new TextView(getApplicationContext());
			LinearLayout.LayoutParams layoutParamsName =new LinearLayout.LayoutParams(0,LayoutParams.MATCH_PARENT,1.0f);
			name_tv.setWidth(0);
			name_tv.setHeight(LayoutParams.MATCH_PARENT);
			name_tv.setGravity(Gravity.CENTER);
			name_tv.setTextColor(getResources().getColor(R.color.hinttextcolor));
			name_tv.setTextSize(18);
			name_tv.setLayoutParams(layoutParamsName);
			name_tv.setText(bookMarkList.get(i).get("name"));
			//android:ellipsize="end" android:singleLine="true"
			name_tv.setSingleLine(true);
			name_tv.setEllipsize(TruncateAt.END);
			
			TextView date_tv=new TextView(getApplicationContext());
			LinearLayout.LayoutParams layoutParamsDate =new LinearLayout.LayoutParams(0,LayoutParams.MATCH_PARENT,1.0f);
			date_tv.setWidth(0);
			date_tv.setHeight(LayoutParams.MATCH_PARENT);
			date_tv.setGravity(Gravity.CENTER);
			date_tv.setTextColor(getResources().getColor(R.color.hinttextcolor));
			date_tv.setTextSize(18);
			date_tv.setLayoutParams(layoutParamsName);
			date_tv.setText(bookMarkList.get(i).get("date"));
			
			inLinearLayout.addView(name_tv);
			inLinearLayout.addView(date_tv);
			outLinearLayout.addView(volume_chapter);
			outLinearLayout.addView(inLinearLayout);
			
			
			parent_view.addView(outLinearLayout);
			list.add(outLinearLayout);
			
		}
	}
	
	
	@Override
	public void onClick(View v) {
		for(int k=0;k<list.size();k++)
		{
			if(v.getTag().equals(""+k))
			{
				Intent intent = new Intent(Activity_BookMark.this, Activity_ReadBible2.class);
				intent.putExtra("volume", bookMarkList.get(k).get("volume"));
				intent.putExtra("simpleVolume", "");
				intent.putExtra("ChapterIndexInBook",Integer.parseInt(bookMarkList.get(k).get("chapterIndexInBook")));
				//Log.i("tag", Integer.parseInt(bookMarkList.get(1).get("chapterIndexInBook"))+"ccc");
				intent.putExtra("chapter", bookMarkList.get(k).get("chapter"));
				intent.putExtra("bookmarkName", bookMarkList.get(k).get("name"));
				startActivity(intent);
				overridePendingTransition(R.anim.bottom2top_in, R.anim.bottom2top_out);
			}
		}
		
	}

	@Override
	public boolean onLongClick(final View v) {
		AlertDialog.Builder builder = new AlertDialog.Builder(Activity_BookMark.this,AlertDialog.THEME_HOLO_LIGHT);  
		builder.setItems(new String[] { "É¾³ý"}, new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				if(which==0)
				{
					parent_view.removeView(v);
					Alert.dismiss();
					new Thread(new Runnable() {
						
						@Override
						public void run() {
							
							deleteBookMark(bookMarkList.get(v.getId()).get("name"));
						}
					}).start();
					
				}
			}
		});
		
		Alert = builder.create();
		Alert.show();
		
		return false;
	}
	
	public void deleteBookMark(String name)
	{
		
		dbHelper = new DatabaseHelper(getApplicationContext(),"bible.db");
		db = dbHelper.getReadableDatabase();
		db.delete("bookmark", "name=?", new String[]{name});
		db.close();
	}
	
	public void back(View view) {
		Intent intent = new Intent(Activity_BookMark.this, MainActivity.class);
		intent.putExtra("from",0);
		startActivity(intent);
		finish();
		overridePendingTransition(R.anim.zoomin, R.anim.zoomout);
	}

}
