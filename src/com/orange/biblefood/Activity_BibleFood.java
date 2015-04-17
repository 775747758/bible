package com.orange.biblefood;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import www.orange.utils.UMShare;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActionBar.LayoutParams;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ScrollView;
import android.widget.TextView;

import com.disegnator.robotocalendar.RobotoCalendarView;
import com.disegnator.robotocalendar.RobotoCalendarView.RobotoCalendarListener;
import com.example.acts.DatabaseHelper;
import com.example.acts.MainActivity;
import com.example.acts.R;
import com.orange.readnote.Activity_EditNote;
import com.orange.view.SelectedTextView;
import com.umeng.socialize.media.QQShareContent;
import com.umeng.socialize.media.QZoneShareContent;
import com.umeng.socialize.sso.UMSsoHandler;
import com.umeng.socialize.weixin.media.CircleShareContent;
import com.umeng.socialize.weixin.media.WeiXinShareContent;

public class Activity_BibleFood extends Activity implements RobotoCalendarListener{

	private int position;
	private ViewPager viewpager;
	private String bookid;
	private DatabaseHelper dbHelper;
	private SQLiteDatabase db;
	private List<View> vList;
	private List<String> titleList = new ArrayList<String>();
	private List<String> dateList = new ArrayList<String>();
	private List<String> contentList = new ArrayList<String>();
	private String date;
	private MyHandler handler;
	private String bookName;
	private final Calendar mCalendar = Calendar.getInstance();
	private LayoutInflater inflater;
	private View popView;
	private RobotoCalendarView robotoCalendarPicker;
	private Calendar currentCalendar;
	private int currentMonthIndex;
	private PopupWindow popWin;
	private View titlebar;
	private TextView title_bar_date;
	private TextView title_bar_book;
	private UMShare umShare;
	private String title;
	private String content;
	public boolean isScrolled;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_biblefood);
		
		umShare = new UMShare(this);

		viewpager = (ViewPager) findViewById(R.id.viewpager);
		title_bar_date = (TextView) findViewById(R.id.title_bar_date);
		title_bar_book=(TextView) findViewById(R.id.title_bar_book);
		
		title_bar_book.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				Intent intent = new Intent(Activity_BibleFood.this, Activity_BibleFoodSelect.class);
				startActivity(intent);
				overridePendingTransition(R.anim.zoomin, R.anim.zoomout);
			}
		});
		
		inflater = LayoutInflater.from(this);
		
		currentMonthIndex = 0;
		popView = inflater.inflate(R.layout.popup_biblefood, null);
		titlebar = inflater.inflate(R.layout.title_bar_biblefood, null);
		robotoCalendarPicker=(RobotoCalendarView)popView.findViewById(R.id.robotoCalendarPicker);
		currentCalendar = Calendar.getInstance(Locale.getDefault());
		robotoCalendarPicker.markDayAsCurrentDay(currentCalendar.getTime());
		robotoCalendarPicker.setRobotoCalendarListener(this);
		
		popWin = new PopupWindow(popView, LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT, true);
		// popWin.
		// popWin.setBackgroundDrawable(new ColorDrawable(0x99000000));
		popWin.setAnimationStyle(R.style.UpdatePopupAnimation);
		ColorDrawable dw = new ColorDrawable(0x00000000);
		popWin.setBackgroundDrawable(dw);
		
		title_bar_date.setOnClickListener(new OnClickListener() {
			
			private String tag;
			@Override
			public void onClick(View v) {
				popWin.showAsDropDown(v);
				popWin.showAtLocation(v, Gravity.CENTER, 0, 0);
				
			}
		});

		Intent intent = getIntent();
		bookid = intent.getStringExtra("bookId");
		bookName=intent.getStringExtra("bookName");
		vList = new ArrayList<View>();
		handler = new MyHandler();

		new Thread(new Runnable() {

			@Override
			public void run() {

				readFromSQL();
			}
		}).start();
	}

	

	public void readFromSQL() {
		SimpleDateFormat sdf = new SimpleDateFormat("MMdd");
		Date d = new Date();
		date = sdf.format(d);
		dbHelper = new DatabaseHelper(getApplicationContext(), "read.db");
		db = dbHelper.getReadableDatabase();
		Cursor cursorTemp = db
				.rawQuery(
						"select bookId,title,content,date from article where bookId=? order by  date",
						new String[] { bookid });
		while (cursorTemp.moveToNext()) {

			int bookID = cursorTemp.getInt(cursorTemp.getColumnIndex("bookId"));
			title = cursorTemp.getString(cursorTemp
					.getColumnIndex("title"));
			content = cursorTemp.getString(cursorTemp
					.getColumnIndex("content"));
			//Log.i("content", content);
			if(bookID==4)
			{
				if (content.contains("<") || content.contains(">")) {
					content = content.substring(content.lastIndexOf("a") + 2)
							.trim();
				}
			}
			else
			{
				if (content.contains("<") || content.contains(">")) {
					content = content.substring(content.lastIndexOf(">") + 1)
							.trim();
				}
			}
			
			
			
			//Log.i("content", content);

			SpannableString spannableString = new SpannableString(content);
			spannableString.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.titlefont)),0,
					content.indexOf("\r\n") + 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

			spannableString.setSpan(new StyleSpan(Typeface.BOLD), 0, content.indexOf("\r\n") + 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
			String datee = cursorTemp.getString(cursorTemp
					.getColumnIndex("date"));
			dateList.add(datee);
			titleList.add(title);
			contentList.add(content);
			ScrollView sv = new ScrollView(getApplicationContext());
			TextView tv1 = new TextView(getApplicationContext());
			tv1.setText(spannableString);
			tv1.setTextSize(20);
			tv1.setLineSpacing(12.0f, 1.0f);
			tv1.setPadding(30, 0, 30, 100);
			tv1.setTextColor(Color.BLACK);
			
			if(bookID==5||bookID==8)
			{
				int index=title.indexOf("日")+2;
				if(index!=-1)
				{
					title=title.substring(index).trim();
				}
				TextView tv = new TextView(getApplicationContext());
				tv.setGravity(Gravity.CENTER);
				tv.setText(title);
				tv.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
				tv.setTextColor(getResources().getColor(R.color.titlefont));
				tv.setTextSize(22);
				tv.setLineSpacing(12.0f, 1.0f);
				tv.setPadding(10, 20, 10, 20);
				LinearLayout ll=new LinearLayout(getApplicationContext());
				ll.setOrientation(LinearLayout.VERTICAL);
				ll.addView(tv);
				ll.addView(tv1);
				sv.addView(ll);
				//sv.addView(tv1);
			}
			else if(bookID==4)
			{
				int index=title.indexOf("*");
				if(index!=-1)
				{
					title=title.substring(0,index).trim();
				}
				TextView tv = new TextView(getApplicationContext());
				tv.setGravity(Gravity.CENTER);
				tv.setText(title);
				tv.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
				tv.setTextColor(getResources().getColor(R.color.titlefont));
				tv.setTextSize(22);
				tv.setLineSpacing(12.0f, 1.0f);
				tv.setPadding(10, 20, 10, 20);
				LinearLayout ll=new LinearLayout(getApplicationContext());
				ll.setOrientation(LinearLayout.VERTICAL);
				ll.addView(tv);
				ll.addView(tv1);
				sv.addView(ll);
			}
			else
			{
				sv.addView(tv1);
			}
			
			
			vList.add(sv);
		}
		cursorTemp.close();
		Message message = Message.obtain();
		message.what = 1;
		handler.sendMessage(message);

	}

	class MyPagerAdapter extends PagerAdapter {

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
			isScrolled=true;
			position=arg0;
			int month=Integer.parseInt(dateList.get(arg0).substring(0, 2));
			int day=Integer.parseInt(dateList.get(arg0).substring(2));
			title_bar_book.setText(bookName);
			title_bar_date.setText(month+"月"+day+"日");

		}

	}

	class MyHandler extends Handler {
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			if (msg.what == 1) {
				viewpager.setOnPageChangeListener(new MyOnPageChangeListener());
				viewpager.setAdapter(new MyPagerAdapter());
				viewpager.setCurrentItem(dateList.indexOf(date));
				
				int month=Integer.parseInt(dateList.get(dateList.indexOf(date)).substring(0, 2));
				int day=Integer.parseInt(dateList.get(dateList.indexOf(date)).substring(2));
				title_bar_book.setText(bookName);
				title_bar_date.setText(month+"月"+day+"日");
			}
		}
	}

	@Override
	public void onDateSelected(Date date) {
		robotoCalendarPicker.markDayAsSelectedDay(date);
		SimpleDateFormat sdf = new SimpleDateFormat("MMdd");
		String dateString = sdf.format(date);
		popWin.dismiss();
		viewpager.setCurrentItem(dateList.indexOf(dateString));
		
		
	}

	@Override
	public void onRightButtonClick() {
		currentMonthIndex++;
		updateCalendar();
		
	}

	@Override
	public void onLeftButtonClick() {
		currentMonthIndex--;
		updateCalendar();
		
	}
	
	private void updateCalendar() {
		currentCalendar = Calendar.getInstance(Locale.getDefault());
		currentCalendar.add(Calendar.MONTH, currentMonthIndex);
		robotoCalendarPicker.initializeCalendar(currentCalendar);
	}
	public void back(View view) {
		Intent intent = new Intent(Activity_BibleFood.this, Activity_BibleFoodSelect.class);
		startActivity(intent);
		finish();
		overridePendingTransition(R.anim.zoomin, R.anim.zoomout);
	}
	
	public void share(View view)
	{
		if(isScrolled)
		{
			umShare.share(titleList.get(position), contentList.get(position), "http://weibo.com/775747758", Activity_BibleFood.this);
		}
		else
		{
			umShare.share(titleList.get(dateList.indexOf(date)), contentList.get(dateList.indexOf(date)), "http://weibo.com/775747758", Activity_BibleFood.this);
		}
		
	}
	
	@Override 
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
	    super.onActivityResult(requestCode, resultCode, data);
	    /**使用SSO授权必须添加如下代码 */
	    UMSsoHandler ssoHandler = umShare.getController().getConfig().getSsoHandler(requestCode) ;
	    if(ssoHandler != null){
	       ssoHandler.authorizeCallBack(requestCode, resultCode, data);
	    }
	}

}
