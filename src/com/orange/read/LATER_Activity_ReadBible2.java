package com.orange.read;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import www.orange.utils.UMShare;
import net.simonvt.menudrawer.MenuDrawer;
import net.simonvt.menudrawer.Position;

import com.example.acts.DatabaseHelper;
import com.example.acts.R;
import com.orange.read.Activity_ReadBible.MyAsyncTask;
import com.orange.read.Activity_ReadBible.MyOnClicListener;
import com.orange.read.Activity_ReadBible.MyOnLongClickListener;
import com.orange.read.Activity_ReadBible.MyOnTouchListener;
import com.orange.read.RightFragment.MyOnPageChangeListener;
import com.orange.read.RightFragment.MyPagerAdapter;
import com.umeng.socialize.media.QQShareContent;
import com.umeng.socialize.media.QZoneShareContent;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;
import android.widget.SeekBar.OnSeekBarChangeListener;

public class LATER_Activity_ReadBible2 extends BaseListSample {

	private int number = 0;
	private int leftEdge = 1;
	private int rightEdge = 1;
	private int[] relPicIds = new int[] { R.color.pic1, R.color.pic2,
			R.color.pic3, R.color.pic4, R.color.pic5, R.color.pic6,
			R.color.pic7, R.color.pic8, R.color.pic9, R.color.pic10,
			R.color.pic11, R.color.pic12 };
	private List<Map<String, String>> bibleData = new ArrayList<Map<String, String>>();
	private ViewPager vp;
	private RelativeLayout backgroundRl;
	private String volume;
	private String keyword;
	private String simpleVolume;
	private String chapter;
	private DatabaseHelper dbHelper;
	private SQLiteDatabase db;
	private ArrayList<Integer> positionStart;
	private ArrayList<Integer> positionEnd;
	private int chapterIndexInBook;
	private int id;
	private int bookIndex;
	private int firstFlag;
	private int CurrentPageIndex;
	private ArrayList<Integer> indexpositionStart = new ArrayList<Integer>();
	private ArrayList<Integer> indexpositionEndnew = new ArrayList<Integer>();
	private ArrayList<Integer> highLightStartnew = new ArrayList<Integer>();
	private ArrayList<Integer> highLightEnd = new ArrayList<Integer>();
	private ArrayList<Integer> shareContentIndex = new ArrayList<Integer>();
	private ArrayList<Integer> ids = new ArrayList<Integer>();
	private ArrayList<Integer> titlepositionStart = new ArrayList<Integer>();
	private ArrayList<Integer> titlepositionEnd = new ArrayList<Integer>();
	private String data;
	private ArrayList<TextView> tvs = new ArrayList<TextView>();
	private TextView hint_tv;
	private TextView title_bar_book;
	private TextView title_bar_chapter;
	private ArrayList<Integer> highLightStart = new ArrayList<Integer>();
	private ArrayList<Integer> indexpositionEnd = new ArrayList<Integer>();
	private SharedPreferences spReadConfig;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		/*
		 * mMenuDrawer = MenuDrawer.attach(this, MenuDrawer.Type.BEHIND,
		 * Position.LEFT, MenuDrawer.MENU_DRAG_CONTENT);
		 */
		mMenuDrawer.setContentView(R.layout.activity_readbible1);
		mMenuDrawer.setMenuView(R.layout.activity_readbible);
		mMenuDrawer.setSlideDrawable(R.drawable.ic_drawer);
		mMenuDrawer.setDrawerIndicatorEnabled(true);
		// 初始化界面
		initView();
		// 取得之前activity传过来的值
		getIntentValue();
		// 取圣经数据
		new MyAsyncTask().execute();

	}

	private void initView() {
		// 初始化数据库
		dbHelper = new DatabaseHelper(getApplicationContext(), "bible.db");
		db = dbHelper.getReadableDatabase();
		// 初始化控件
		hint_tv = (TextView) findViewById(R.id.hint_tv);
		title_bar_book = (TextView) findViewById(R.id.title_bar_book);

		title_bar_chapter = (TextView) findViewById(R.id.title_bar_chapter);
		vp = (ViewPager) findViewById(R.id.vp);
		backgroundRl = (RelativeLayout) findViewById(R.id.rl);
		spReadConfig = getSharedPreferences("readbible_config",
				Context.MODE_PRIVATE);
		backgroundRl.setBackgroundColor(getResources().getColor(
				spReadConfig.getInt("background_color", relPicIds[0])));

	}

	private void getIntentValue() {
		Intent intent = getIntent();
		volume = intent.getStringExtra("volume");
		if (intent.getStringExtra("keyword") != null) {
			keyword = intent.getStringExtra("keyword");
		}
		chapterIndexInBook = intent.getIntExtra("ChapterIndexInBook", 0);
		leftEdge = rightEdge = chapterIndexInBook;
		simpleVolume = intent.getStringExtra("simpleVolume");
		chapter = intent.getStringExtra("chapter");
		title_bar_book.setText(volume);
		title_bar_chapter.setText(chapter);
		// hint_tv.setText(volume+"  "+chapter);
	}

	public String readFromSQL(int index, String ori) {
		Cursor cursorTemp = null;
		if (number == 0) {
			if (index == 1 || index == 2 || index == 3) {
				cursorTemp = db
						.rawQuery(
								"select bookName,chapterIndex,id,firstFlag,bookIndex,sectionText,isTitle,sectionIndex, chapterIndexInBook from holybible where chapterIndexInBook in(?,?,?,?,?) ",
								new String[] { "1", "2", "3", "4", "5" });
				leftEdge = 0;
			} else {
				cursorTemp = db
						.rawQuery(
								"select bookName,chapterIndex,id,firstFlag,bookIndex,sectionText,isTitle,sectionIndex, chapterIndexInBook from holybible where chapterIndexInBook in(?,?,?,?,?) ",
								new String[] { index - 2 + "", index - 1 + "",
										index + "", "" + index + 1,
										"" + index + 2 });
				leftEdge = leftEdge - 3;
			}
		} else {
			if (ori.equals("left")) {

				if (leftEdge < 6) {

					String temp1[] = new String[leftEdge];
					for (int i = 1; i <= leftEdge; i++) {
						temp1[i] = i + "";
					}
					cursorTemp = db
							.rawQuery(
									"select bookName,chapterIndex,id,firstFlag,bookIndex,sectionText,isTitle,sectionIndex, chapterIndexInBook from holybible where chapterIndexInBook in(?,?,?,?,?) ",
									temp1);
					leftEdge = 0;
				} else {

					cursorTemp = db
							.rawQuery(
									"select bookName,chapterIndex,id,firstFlag,bookIndex,sectionText,isTitle,sectionIndex, chapterIndexInBook from holybible where chapterIndexInBook in(?,?,?,?,?) ",
									new String[] { leftEdge - 4 + "",
											leftEdge - 3 + "",
											leftEdge - 2 + "",
											leftEdge - 1 + "", "" + leftEdge });
					leftEdge = leftEdge - 5;
				}

			} else if (ori.equals("right")) {
				if (1189 - rightEdge < 5) {

					String temp1[] = new String[1189-rightEdge+1];
					for (int i = rightEdge; i <= 1189; i++) {
						temp1[i] = i + "";
					}
					cursorTemp = db
							.rawQuery(
									"select bookName,chapterIndex,id,firstFlag,bookIndex,sectionText,isTitle,sectionIndex, chapterIndexInBook from holybible where chapterIndexInBook in(?,?,?,?,?) ",
									temp1);
					rightEdge = 1190;
				} else {

					cursorTemp = db
							.rawQuery(
									"select bookName,chapterIndex,id,firstFlag,bookIndex,sectionText,isTitle,sectionIndex, chapterIndexInBook from holybible where chapterIndexInBook in(?,?,?,?,?) ",
									new String[] { rightEdge + 4 + "",
											rightEdge + 3 + "",
											rightEdge + 2 + "",
											rightEdge + 1 + "", "" + rightEdge });
					rightEdge = rightEdge + 5;
				}
			}
		}

		String reult = "";
		positionStart = new ArrayList<Integer>();
		positionEnd = new ArrayList<Integer>();
		int len = 1;
		int i = 1;
		SpannableString spanText;
		while (cursorTemp.moveToNext()) {
			Map<String, String> map = new HashMap<String, String>();
			String temp = null;
			int isTitle = cursorTemp.getInt(cursorTemp
					.getColumnIndex("isTitle"));
			int sectionIndex = cursorTemp.getInt(cursorTemp
					.getColumnIndex("sectionIndex"));
			int chapterIndexInBook = cursorTemp.getInt(cursorTemp
					.getColumnIndex("chapterIndexInBook"));
			int id = cursorTemp.getInt(cursorTemp.getColumnIndex("id"));
			int bookIndex = cursorTemp.getInt(cursorTemp
					.getColumnIndex("bookIndex"));
			int firstFlag = cursorTemp.getInt(cursorTemp
					.getColumnIndex("firstFlag"));
			String getDataString = cursorTemp.getString(cursorTemp
					.getColumnIndex("sectionText"));
			String bookName = cursorTemp.getString(cursorTemp
					.getColumnIndex("bookName"));
			int chapterIndex = cursorTemp.getInt(cursorTemp
					.getColumnIndex("chapterIndex"));
			map.put("isTitle", isTitle + "");
			map.put("sectionIndex", sectionIndex + "");
			map.put("chapterIndexInBook", chapterIndexInBook + "");
			map.put("id", id + "");
			map.put("bookIndex", bookIndex + "");
			map.put("firstFlag", firstFlag + "");
			map.put("bookName", bookName);
			map.put("chapterIndex", chapterIndex + "");
			if (sectionIndex > 1) {
				getDataString = sectionIndex + " " + getDataString;
			}
			if (isTitle == 1) {
				if (i == 1) {
					temp = getDataString + "\n        ";
					/*
					 * spanText = new SpannableString(temp);
					 * spanText.setSpan(new StyleSpan(Typeface.BOLD), 0,
					 * temp.length(), SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE);
					 */

				} else {
					temp = "\n" + getDataString + "\n        ";
				}

			} else {
				temp = getDataString + "  ";
			}
			reult = reult + temp;
			map.put("reult", reult);
			if (isTitle == 0) {
				Log.i("是title", i + "");
				if (sectionIndex >= 2) {
					indexpositionStart.add(Integer.parseInt(len + ""));
					map.put("indexpositionStart", len + "");
					indexpositionEnd
							.add(Integer.parseInt((len + (sectionIndex + "")
									.length()) + ""));
					map.put("indexpositionEnd",
							(len + (sectionIndex + "").length()) + "");
				}
				if (firstFlag == 1) {
					positionStart.add(Integer.parseInt(len + ""));
					highLightStart.add(Integer.parseInt(len + ""));
					map.put("positionStart", len + "");
					map.put("highLightStart", len + "");
					len = len + temp.length();
					highLightEnd.add(Integer.parseInt(len + ""));
					positionEnd.add(Integer.parseInt(len + ""));
					shareContentIndex.add(sectionIndex);
					// ids.add(id);

					map.put("highLightEnd", len + "");
					map.put("positionEnd", len + "");
					map.put("shareContentIndex", sectionIndex + "");

				} else {
					positionStart.add(Integer.parseInt(len + ""));
					map.put("positionStart", len + "");
					len = len + temp.length();
					positionEnd.add(Integer.parseInt(len + ""));
					shareContentIndex.add(sectionIndex);

					map.put("positionEnd", len + "");
					map.put("shareContentIndex", sectionIndex + "");
					// ids.add(id);
				}

			} else {
				titlepositionStart.add(Integer.parseInt(len + ""));
				map.put("titlepositionStart", len + "");
				len = len + temp.length();
				titlepositionEnd.add(Integer.parseInt(len + ""));
				map.put("titlepositionEnd", len + "");

			}

			i++;
			bibleData.add(map);
		}
		cursorTemp.close();
		data = reult;
		Log.i("data", reult);
		number++;
		return reult;
	}

	private void initViewPager(String data) {

		SpannableString spannableString1 = new SpannableString(data);
		//setSpan(spannableString1);

		for (int i = 0; i < 1089; i++) {
			TextView tv = new TextView(getApplicationContext());

			tv.setTextSize(spReadConfig.getFloat("fontsize", 20));
			tv.setLineSpacing(12.0f, 1.0f);
			tv.setPadding(30, 0, 30, 100);
			tv.setTextColor(Color.BLACK);
			// tv.setOnLongClickListener(new MyOnLongClickListener());
			// tv.setOnTouchListener(new MyOnTouchListener());
			// tv.setOnClickListener(new MyOnClicListener());
			tvs.add(tv);
		}
		tvs.get(CurrentPageIndex).setText(spannableString1);
		// nextDB += 1;
		// transDataToRigahtFragment();
		vp.setOnPageChangeListener(new MyOnPageChangeListener());
		vp.setAdapter(new MyPagerAdapter());
		vp.setCurrentItem(CurrentPageIndex);
	}

	private void setSpan(SpannableString spannableString,int index) {
		for (int i = 0; i < highLightStart.size(); i++) {
			spannableString.setSpan(new ForegroundColorSpan(getResources()
					.getColor(R.color.highlight)), Integer.parseInt(bibleData.get(index).get("highLightStart")) - 1,
					Integer.parseInt(bibleData.get(index).get("highLightEnd")) - 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		}
		for (int i = 0; i < titlepositionStart.size(); i++) {
			spannableString.setSpan(new StyleSpan(Typeface.BOLD),
					titlepositionStart.get(i) - 1, titlepositionEnd.get(i) - 1,
					Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
			spannableString.setSpan(new AbsoluteSizeSpan(40),
					titlepositionStart.get(i) - 1, titlepositionEnd.get(i) - 1,
					Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

		}
		for (int i = 0; i < indexpositionStart.size(); i++) {
			spannableString.setSpan(new AbsoluteSizeSpan(30),
					indexpositionStart.get(i) - 1, indexpositionEnd.get(i) - 1,
					Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		}
		int start = 0;
		if (keyword != null) {
			while (true) {
				if (data.indexOf(keyword, start) != data.lastIndexOf(keyword)) {
					spannableString.setSpan(new ForegroundColorSpan(
							getResources().getColor(R.color.keyword)), data
							.indexOf(keyword, start),
							data.indexOf(keyword, start) + keyword.length(),// 2+resultString.indexOf("示")+1
							Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
					start = data.indexOf(keyword, start) + 1;
				} else {
					break;
				}
			}
		}

		// result.setText(spannableString);
	}

	class MyAsyncTask extends AsyncTask<Void, TextView, String> {
		@Override
		protected void onPreExecute() {

			super.onPreExecute();
		}

		@Override
		protected void onPostExecute(String result) {
			initViewPager(result);
			super.onPostExecute(result);
		}

		@Override
		protected String doInBackground(Void... params) {
				return readFromSQL(chapterIndexInBook,"");
		}

	}

	class MyAsyncTask2 extends AsyncTask<String, Void, Void> {

		@Override
		protected Void doInBackground(String... params) {
			readFromSQL(0, params[0]);
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			
			super.onPostExecute(result);
		}

		

	}

	class MyOnPageChangeListener implements OnPageChangeListener {

		private boolean isScrolling;
		private int lastValue;
		private boolean right;
		private boolean left;

		@Override
		public void onPageScrollStateChanged(int arg0) {
			 if (arg0 == 1) {  
	                isScrolling = true;  
	            } else {  
	                isScrolling = false;  
	            } 

		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
			if (isScrolling) {  
                if (lastValue > arg2) {  
                    // 递减，向右侧滑动  
                    right = true;  
                    left = false;  
                } else if (lastValue < arg2) {  
                    // 递减，向右侧滑动  
                    right = false;  
                    left = true;  
                } else if (lastValue == arg2) {  
                    right = left = false;  
                }  
            }  
            lastValue = arg2;

		}

		@SuppressLint("NewApi")
		@Override
		public void onPageSelected(int arg0) {
			if(right)
			{
				chapterIndexInBook+=1;
			}
			if(left)
			{
				chapterIndexInBook-=1;
			}
			Log.i("chapterIndexInBook", chapterIndexInBook+"");
			new MyAsyncTask2().execute(chapterIndexInBook + "");
		}

	}

	class MyPagerAdapter extends PagerAdapter {

		@Override
		public Object instantiateItem(View container, int position) {
			((ViewPager) container).addView(tvs.get(position));
			return tvs.get(position);
		}

		@Override
		public void destroyItem(View container, int position, Object object) {
			((ViewPager) container).removeView(tvs.get(position));
			// super.destroyItem(container, position, object);

		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return tvs.size();
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			// TODO Auto-generated method stub
			return arg0 == arg1;
		}

	}

}
