package com.orange.read;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.example.acts.DatabaseHelper;
import com.example.acts.R;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.PagerTabStrip;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.AnimationUtils;
import android.webkit.WebView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ViewFlipper;

public class RightFragment extends Fragment {

	private String oldDate="";
	private String oldVolume="";
	private String oldChapter="";
	private String oldNoteContent="";
	private List<String> titleList;
	private List<View> vList;
	LayoutInflater inflater;
	private ViewPager vp;
	private TextView english_tv;
	private TextView tv;
	String volume;
	String chapter;

	// 访问数据库需要的
	private List<Integer> positionStart = new ArrayList<Integer>();
	private List<Integer> positionEnd = new ArrayList<Integer>();
	private List<Integer> indexpositionStart = new ArrayList<Integer>();
	private List<Integer> indexpositionEnd = new ArrayList<Integer>();
	private List<Integer> titlepositionStart = new ArrayList<Integer>();
	private List<Integer> titlepositionEnd = new ArrayList<Integer>();
	private DatabaseHelper dbHelper;
	private SQLiteDatabase db;
	private int chapterIndexInBook;
	private TextView note;
	private WebView webView;
	private TextView date_tv;
	private TextView bible_tv;
	private View activity_read_note;
	private EditText title_tv;
	private String oldTitle;

	@SuppressWarnings("deprecation")
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		Log.i("onCreateView", "onCreateView");
		View view = inflater.inflate(R.layout.right_fragment, null);
		vp = (ViewPager) view.findViewById(R.id.vp);
		PagerTabStrip tab = (PagerTabStrip) view.findViewById(R.id.tab);
		tab.setTabIndicatorColor(Color.WHITE);
		View activity_read_english = inflater.inflate(R.layout.activity_read_english, null);
		activity_read_note = inflater.inflate(R.layout.activity_read_note, null);
		View activity_read_translate = inflater.inflate(R.layout.activity_read_translate, null);

		tv = (TextView) view.findViewById(R.id.tv);
		// tv.setText(index);

		english_tv = (TextView) activity_read_english.findViewById(R.id.english_tv);
		note = (TextView) activity_read_note.findViewById(R.id.book_text);
		title_tv=(EditText) activity_read_note.findViewById(R.id.title_tv);
		date_tv = (TextView) activity_read_note.findViewById(R.id.date_tv);
		date_tv.setText(getCurrentDate());

		
		// note.setText("年底发可能蜂鸟网，到年底复活币备份，逗你玩非南方网");
		webView = (WebView) activity_read_translate.findViewById(R.id.webview);
		webView.setBackgroundColor(0);
		 webView.setBackgroundColor(Color.parseColor("#ffffdd")); 
		webView.getSettings().setBuiltInZoomControls(true);

		LinearLayout read_english = (LinearLayout) activity_read_english.findViewById(R.id.read_english);
		LinearLayout read_note = (LinearLayout) activity_read_note.findViewById(R.id.read_note);
		LinearLayout read_translate = (LinearLayout) activity_read_translate.findViewById(R.id.read_translate);

		vList = new ArrayList<View>();
		titleList = new ArrayList<String>();

		titleList.add("英文");
		titleList.add("笔记");
		titleList.add("注释");

		vList.add(read_english);
		vList.add(read_note);
		vList.add(read_translate);

		vp.setOnPageChangeListener(new MyOnPageChangeListener());
		vp.setAdapter(new MyPagerAdapter());
		
		return view;
	}

	class MyPagerAdapter extends PagerAdapter {

		@Override
		public CharSequence getPageTitle(int position) {
			// TODO Auto-generated method stub
			return titleList.get(position);
		}

		@Override
		public Object instantiateItem(View container, int position) {
			((ViewPager) container).addView(vList.get(position));
			return vList.get(position);
		}

		@Override
		public void destroyItem(View container, int position, Object object) {
			((ViewPager) container).removeView(vList.get(position));
			// super.destroyItem(container, position, object);

		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return vList.size();
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			// TODO Auto-generated method stub
			return arg0 == arg1;
		}

	}

	class MyOnPageChangeListener implements OnPageChangeListener {

		@Override
		public void onPageScrollStateChanged(int arg0) {
			Log.i("123", "123");

		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
			// TODO Auto-generated method stub

		}

		@SuppressLint("NewApi")
		@Override
		public void onPageSelected(int arg0) {

		}

	}

	public SpannableString readFromSQL(int index) {
		Log.i("readFromSQL", "readFromSQL");
		positionStart.clear();
		positionEnd.clear();
		indexpositionStart.clear();
		indexpositionEnd.clear();
		titlepositionStart.clear();
		titlepositionEnd.clear();
		dbHelper = new DatabaseHelper(getActivity(),"bible.db");
		db = dbHelper.getReadableDatabase();
		String reult = "";
		int len = 1;
		Cursor cursorTemp = db.rawQuery("select enSectionText,isTitle,sectionIndex,bookName,chapterIndex from holybible where chapterIndexInBook=? ",
				new String[] { index + "" });
		int i = 1;
		while (cursorTemp.moveToNext()) {
			String temp = null;
			String getDataString = "";
			int isTitle = cursorTemp.getInt(cursorTemp.getColumnIndex("isTitle"));
			int sectionIndex = cursorTemp.getInt(cursorTemp.getColumnIndex("sectionIndex"));
			getDataString = cursorTemp.getString(cursorTemp.getColumnIndex("enSectionText"));
			// volume =
			// cursorTemp.getString(cursorTemp.getColumnIndex("bookName"));
			// chapter =
			// cursorTemp.getInt(cursorTemp.getColumnIndex("chapterIndex")) +
			// "";
			if (sectionIndex > 1) {
				getDataString = sectionIndex + " " + getDataString;
			}
			if (isTitle == 1) {

				temp = "\n        ";

			} else {
				temp = getDataString + "  ";
			}
			reult = reult + temp;
			if (isTitle == 0) {
				Log.i("是title", i + "");
				if (sectionIndex >= 2) {
					indexpositionStart.add(Integer.parseInt(len + ""));
					indexpositionEnd.add(Integer.parseInt((len + (sectionIndex + "").length()) + ""));
				}
				positionStart.add(Integer.parseInt(len + ""));
				len = len + temp.length();
				positionEnd.add(Integer.parseInt(len + ""));
			}
			i++;
		}
		cursorTemp.close();
		// data = reult;
		SpannableString spannableString = new SpannableString(reult);

		for (int k = 0; k < indexpositionStart.size(); k++) {
			spannableString.setSpan(new AbsoluteSizeSpan(30), indexpositionStart.get(k) - 1, indexpositionEnd.get(k) - 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		}

		return spannableString;

	}

	public void setTranslate(String str) {
		webView.loadUrl(str);
	}

	public void setEnglish(int chapterIndexInBook1) {
		
		english_tv.setText(readFromSQL(chapterIndexInBook1));
		Log.i("setEnglish", "setEnglish");
	}
	public void setChapterIndexInBook(int chapterIndexInBook1)
	{
		Log.i("setChapterIndexInBook", chapterIndexInBook1+"");
		chapterIndexInBook=chapterIndexInBook1;
		if(isDataExits())
		{
			Log.i("存在", "isDataExits");
			getOldData();
			date_tv.setText(oldDate+"旧");
			title_tv.setText(oldTitle);
			note.setText(oldNoteContent);
			//date_tv.setText(oldDate+"旧");
		}
	}

	@Override
	public void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		Log.i("onStop", "onStop");
		store();
	}

	public void storeNote() {

		ContentValues values = new ContentValues();
		DatabaseHelper dbHelper = new DatabaseHelper(getActivity(),"bible.db");
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		db.execSQL("create table  if not exists  note(title varchar(30),chapterIndexInBook int,volume varchar(20),chapter varchar(20),noteContent text,date varchar(20) )");
		values.put("chapterIndexInBook", chapterIndexInBook);
		values.put("volume", volume);

		values.put("chapter", chapter);
		values.put("noteContent", note.getText().toString());
		values.put("date", getCurrentDate());
		values.put("title", title_tv.getText().toString());
		db.insert("note", null, values);

	}

	public boolean isDataExits() {
		Log.i("进入isDataExits", "isDataExits");
		dbHelper = new DatabaseHelper(getActivity(),"bible.db");
		db = dbHelper.getReadableDatabase();
		String reult = "";
		int count =0;
		Cursor cursor = db.rawQuery("SELECT count(*) FROM sqlite_master WHERE type='table' AND name='note'",null);
		if(cursor.moveToNext())
		{
			count = cursor.getInt(0);
		}
		if(count==0)
		{
			Log.i("不存在表", "不存在表");
			return false;
			
		}
		else
		{
			
			Cursor cursorTemp = db.rawQuery("select noteContent from note where chapterIndexInBook=?", new String[] { chapterIndexInBook + "" });
			if (cursorTemp.getCount() == 0) {
				Log.i("存在表，但不存在", "存在表，但不存在");
				title_tv.setText("");
				note.setText("");
				return false;
			} else {
				Log.i("存在表，存在", "存在表，存在");
				return true;
			}
		}
		
		

	}

	public void updateNote() {
		ContentValues values = new ContentValues();
		DatabaseHelper dbHelper = new DatabaseHelper(getActivity(),"bible.db");
		SQLiteDatabase db = dbHelper.getWritableDatabase();

		values.put("noteContent", note.getText().toString());
		db.update("note", values, "chapterIndexInBook=?", new String[] { chapterIndexInBook + "" });
	}

	public String getCurrentDate() {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy年MM月dd日");
		Date date = new Date(System.currentTimeMillis());
		return formatter.format(date);
	}

	public void setVolume(String data) {
		Log.i("setVolume", "volume:"+volume+"  chapter:"+chapter);
		volume = data.split(":")[0];
		chapter = data.split(":")[1];
		bible_tv = (TextView) activity_read_note.findViewById(R.id.bible_tv);
		bible_tv.setText(data);
	}

	public void getOldData() {
		dbHelper = new DatabaseHelper(getActivity(),"bible.db");
		db = dbHelper.getReadableDatabase();
		Cursor cursorTemp = db
				.rawQuery("select title,volume,chapter,noteContent,date from note where chapterIndexInBook=?", new String[] { chapterIndexInBook + "" });
		if(cursorTemp.moveToNext())
		{
			oldVolume = cursorTemp.getString(cursorTemp.getColumnIndex("volume"));
			oldChapter = cursorTemp.getString(cursorTemp.getColumnIndex("chapter"));
			oldNoteContent = cursorTemp.getString(cursorTemp.getColumnIndex("noteContent"));
			oldDate = cursorTemp.getString(cursorTemp.getColumnIndex("date"));
			oldTitle = cursorTemp.getString(cursorTemp.getColumnIndex("title"));
		}
		
		
		Log.i("getOldData", "oldNoteContent："+oldNoteContent+"  oldTitle:"+oldTitle);
		cursorTemp.close();
	}
	
	public void store()
	{
		if(isDataExits())
		{
			Log.i("updateNote", "updateNote");
			updateNote();
		}
		else
		{
			Log.i("storeNote", "storeNote");
			storeNote();
		}
	}
	
	
	

	

}
