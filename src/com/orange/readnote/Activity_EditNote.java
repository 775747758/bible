package com.orange.readnote;

import java.util.Calendar;

import www.orange.utils.UMShare;

import com.example.acts.DatabaseHelper;
import com.example.acts.MainActivity;
import com.example.acts.R;
import com.orange.read.Activity_ReadBible2;
import com.orange.test.Activity_BibleTestMain;
import com.umeng.socialize.media.QQShareContent;
import com.umeng.socialize.media.QZoneShareContent;
import com.umeng.socialize.weixin.media.WeiXinShareContent;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class Activity_EditNote extends Activity {

	private EditText et;
	private String volume;
	private String chapter;
	private String noteContent;
	private String date;
	private String title;
	private String chapterIndexInBook;
	private TextView title_bar_name;
	private TextView date_tv;
	private TextView chapter_tv;
	private UMShare umShare;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_editnote);
		
		umShare = new UMShare(this);

		title_bar_name = (TextView) findViewById(R.id.title_bar_name);
		date_tv = (TextView) findViewById(R.id.date_tv);
		et = (EditText) findViewById(R.id.et);
		chapter_tv=(TextView) findViewById(R.id.chapter_tv);
		
		Intent intent = getIntent();
		volume = intent.getStringExtra("volume");
		chapter = intent.getStringExtra("chapter");
		noteContent = intent.getStringExtra("noteContent");
		date = intent.getStringExtra("date");
		title = intent.getStringExtra("title");
		chapterIndexInBook = intent.getStringExtra("chapterIndexInBook");
		
		chapter_tv.setText(volume+"  "+chapter);

		et.setText(noteContent);
		et.setSelection(et.length());//调整光标到最后一行  
		
		
		Calendar c = Calendar.getInstance();
		int mYear = c.get(Calendar.YEAR);
		String[] dates = date.split("/");
		if (Integer.parseInt(dates[0]) == mYear) {
			date_tv.setText(date.substring(date.indexOf("/") + 1));
		} else {
			date_tv.setText(date);
		}
		
		title_bar_name.setText(title);

	}
	
	public void share(View view)
	{
		umShare.share(title_bar_name.getText().toString(), et.getText().toString().trim(), "http://weibo.com/775747758", Activity_EditNote.this);
	}
	
	@Override
	protected void onStop() {
		updateNote();
		super.onStop();
	}
	
	public void updateNote() {
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				ContentValues values = new ContentValues();
				DatabaseHelper dbHelper = new DatabaseHelper(getApplicationContext(),
						"bible.db");
				SQLiteDatabase db = dbHelper.getWritableDatabase();

				values.put("noteContent", et.getText().toString());
				db.update("note", values, "chapterIndexInBook=?",
						new String[] { chapterIndexInBook + "" });
				
			}
		}).start();
		
	}
	
	public void back(View view) {
		Intent intent = new Intent(Activity_EditNote.this, Activity_ReadNote.class);
		startActivity(intent);
		finish();
		overridePendingTransition(R.anim.zoomin, R.anim.zoomout);
	}

}
