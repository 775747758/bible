package com.example.acts;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import com.orange.biblefood.Activity_BibleFood;
import com.orange.biblefood.Activity_BibleFoodSelect;
import com.orange.friendscircle.Activity_FriendsCircle;
import com.orange.read.Activity_BookMark;
import com.orange.read.Activity_SelectVolume;
import com.orange.read.Activity_SelectVolume1;
import com.orange.read.Activity_search;
import com.orange.readnote.Activity_ReadNote;
import com.orange.recite.Activity_ReciteMain;
import com.orange.test.Activity_BibleTestMain;
import com.wujie.example.note.MainActivity;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnCreateContextMenuListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class Main_Activity extends Activity {

	private TextView today_section;
	private ArrayList<Map<String, String>> RecommendData=new ArrayList<Map<String,String>>();
	private Calendar c;
	private int mDay=-1;
	private SharedPreferences sp;
	private EditText search_et;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main1);
		
		
		search_et=(EditText)findViewById(R.id.search_et);
		today_section=(TextView)findViewById(R.id.today_section);
		
		sp = getSharedPreferences("config", Context.MODE_PRIVATE);
		
		c = Calendar.getInstance();
		mDay = c.get(Calendar.DAY_OF_MONTH);
		
		today_section.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				ClipboardManager cmb = (ClipboardManager) getSystemService(Main_Activity.this.CLIPBOARD_SERVICE);
				ClipData clip = ClipData.newPlainText("text",today_section.getText().toString().trim() );
				cmb.setPrimaryClip(clip);
				Toast.makeText(getApplicationContext(), "ÒÑ¸´ÖÆ", Toast.LENGTH_SHORT).show();
			}
		});
		
		new AsyncTask<Void, Void, Void>() {

			@Override
			protected Void doInBackground(Void... params) {
				loadRecommendBible();
				return null;
			}
			
			protected void onPostExecute(Void result) {
				Random rnd = new Random();
				int number = rnd.nextInt(RecommendData.size()-1);
				
				if(sp.getInt("date", -1)==-1||mDay!=sp.getInt("date", -1))
				{
					Editor editor = sp.edit();
					editor.putInt("date", mDay);
					editor.putString("today_section", RecommendData.get(number).get("content")+" ("+RecommendData.get(number).get("name")+")");
					editor.commit();
				}
				today_section.setText(sp.getString("today_section", ""));
			
			};
		}.execute();
		 
	}
	
	@Override
	protected void onResume() {
		today_section.setText(sp.getString("today_section", ""));
		super.onResume();
	}
	


	public void read(View view) {
		Intent intent = new Intent(Main_Activity.this, Activity_SelectVolume1.class);
		startActivity(intent);
		overridePendingTransition(R.anim.zoomin, R.anim.zoomout);
	}
	
	public void recite(View view) {
		Intent intent = new Intent(Main_Activity.this, Activity_ReciteMain.class);
		startActivity(intent);
		overridePendingTransition(R.anim.zoomin, R.anim.zoomout);
	}
	
	public void friendscircle(View view)
	{
		Intent intent = new Intent(Main_Activity.this, Activity_FriendsCircle.class);
		startActivity(intent);
		overridePendingTransition(R.anim.zoomin, R.anim.zoomout);
	}
	
	public void biblefood(View view)
	{
		Intent intent = new Intent(Main_Activity.this, Activity_BibleFoodSelect.class);
		startActivity(intent);
		overridePendingTransition(R.anim.zoomin, R.anim.zoomout);
	}
	
	public void bibletest(View view)
	{
		Intent intent = new Intent(Main_Activity.this, Activity_BibleTestMain.class);
		startActivity(intent);
		overridePendingTransition(R.anim.zoomin, R.anim.zoomout);
	}
	
	
	public void think(View view)
	{
		Intent intent = new Intent(Main_Activity.this, MainActivity.class);
		startActivity(intent);
		overridePendingTransition(R.anim.zoomin, R.anim.zoomout);
	}
	public void readnote(View view)
	{
		Intent intent = new Intent(Main_Activity.this, Activity_ReadNote.class);
		startActivity(intent);
		overridePendingTransition(R.anim.zoomin, R.anim.zoomout);
	}
	public void bookamrk(View view)
	{
		Intent intent = new Intent(Main_Activity.this, Activity_BookMark.class);
		startActivity(intent);
		overridePendingTransition(R.anim.zoomin, R.anim.zoomout);
	}
	
	
	private void loadRecommendBible() {
		DatabaseHelper dbHelper = new DatabaseHelper(Main_Activity.this,"bible.db");
		SQLiteDatabase db = dbHelper.getReadableDatabase();
		Cursor cursor = db.rawQuery("select title,name,content from recomendbible", null);
		RecommendData = new ArrayList<Map<String, String>>();
		while (cursor.moveToNext()) {
			String recommendTitle = cursor.getString(cursor.getColumnIndex("title"));
			String recommendName = cursor.getString(cursor.getColumnIndex("name"));
			String recommendContent = cursor.getString(cursor.getColumnIndex("content"));
			Log.i("content", recommendContent);
			Map<String, String> map = new HashMap<String, String>();
			map.put("title", recommendTitle);
			map.put("name", recommendName);
			map.put("content", recommendContent);
			RecommendData.add(map);
			// TitlrData.add(map);
		}
		cursor.close();
	}
	
	public void beginSearch(View view) {
		Intent intent = new Intent(Main_Activity.this, Activity_search.class);
		intent.putExtra("from", "MainActivity");
		intent.putExtra("searchContent", search_et.getText().toString());
		startActivity(intent);
		overridePendingTransition(R.anim.zoomin, R.anim.zoomout);
		

	}

}
