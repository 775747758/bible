package com.orange.read;

import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import net.simonvt.menudrawer.MenuDrawer;
import net.simonvt.menudrawer.MenuDrawer.OnDrawerStateChangeListener;
import www.orange.utils.ChenJin;
import www.orange.utils.LightnessControl;
import www.orange.utils.MyProgressPopUpWindow;
import www.orange.utils.UMShare;
import android.R.menu;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ActionBar.LayoutParams;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.PagerTabStrip;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.BackgroundColorSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Display;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import android.view.animation.TranslateAnimation;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.PopupWindow;
import android.widget.PopupMenu.OnMenuItemClickListener;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.internal.view.menu.MenuBuilder;
import com.actionbarsherlock.internal.view.menu.MenuPopupHelper;
import com.actionbarsherlock.internal.widget.PopupWindowCompat;
import com.baidu.baidutranslate.openapi.TranslateClient;
import com.baidu.baidutranslate.openapi.callback.ITransResultCallback;
import com.baidu.baidutranslate.openapi.entity.TransResult;
import com.example.acts.DatabaseHelper;
import com.example.acts.MainActivity;
import com.example.acts.R;
import com.orange.church.Activity_ChurchInfo;
import com.orange.readnote.Activity_EditNote;
import com.orange.recite.Activity_ReciteMain;
import com.orange.test.Activity_BibleTestMain;
import com.umeng.socialize.media.QQShareContent;
import com.umeng.socialize.media.QZoneShareContent;

public class Activity_ReadBible2 extends BaseListSample {

	private List<Map<String, Integer>> list = new ArrayList<Map<String, Integer>>();
	private List<ArrayList<Integer>> indexpositionStartL = new ArrayList<ArrayList<Integer>>();
	private List<ArrayList<Integer>> indexpositionEndL = new ArrayList<ArrayList<Integer>>();
	private List<ArrayList<Integer>> highLightStartL = new ArrayList<ArrayList<Integer>>();
	private List<ArrayList<Integer>> highLightEndL = new ArrayList<ArrayList<Integer>>();
	private List<ArrayList<Integer>> shareContentIndexL = new ArrayList<ArrayList<Integer>>();
	private List<ArrayList<Integer>> idsL = new ArrayList<ArrayList<Integer>>();
	private List<ArrayList<Integer>> titlepositionStartL = new ArrayList<ArrayList<Integer>>();
	private List<ArrayList<Integer>> titlepositionEndL = new ArrayList<ArrayList<Integer>>();
	private List<ArrayList<Integer>> positionStartL = new ArrayList<ArrayList<Integer>>();
	private List<ArrayList<Integer>> positionEndL = new ArrayList<ArrayList<Integer>>();
	private List<Map<String, String>> bibleData = new ArrayList<Map<String, String>>();
	private List<SpannableStringBuilder> spanL = new ArrayList<SpannableStringBuilder>();
	private int number = 0;
	private int leftEdge = 1;
	private int rightEdge = 1;
	private int[] relPicIds = new int[] { R.color.pic1, R.color.pic2,
			R.color.pic3, R.color.pic4, R.color.pic5, R.color.pic6,
			R.color.pic7, R.color.pic8, R.color.pic9, R.color.pic10,
			R.color.pic11, R.color.pic12 };
	private ViewPager vp;
	private RelativeLayout backgroundRl;
	private String volume;
	private String keyword;
	private String simpleVolume;
	private String chapter;
	private DatabaseHelper dbHelper;
	private SQLiteDatabase db;

	private int chapterIndexInBook;
	private int id;
	private int bookIndex;
	private int firstFlag;
	private int CurrentPageIndex;

	private ArrayList<ScrollView> tvs = new ArrayList<ScrollView>();
	private TextView hint_tv;
	private TextView title_bar_book;
	private TextView title_bar_chapter;
	public boolean right;
	public boolean left;

	private String oldDate = "";
	private String oldVolume = "";
	private String oldChapter = "";
	private String oldNoteContent = "";
	private List<String> titleList;
	private List<View> vList;
	LayoutInflater inflater;
	private ViewPager vpRight;
	private TextView english_tv;
	private TextView tv;
	String volumeRight;
	String chapterRight;

	// 访问数据库需要的
	private List<Integer> positionStart = new ArrayList<Integer>();
	private List<Integer> positionEnd = new ArrayList<Integer>();
	private List<Integer> indexpositionStart = new ArrayList<Integer>();
	private List<Integer> indexpositionEnd = new ArrayList<Integer>();
	private List<Integer> titlepositionStart = new ArrayList<Integer>();
	private List<Integer> titlepositionEnd = new ArrayList<Integer>();
	private int chapterIndexInBookRight;
	private TextView note;
	private WebView webView;
	private TextView date_tv;
	private TextView bible_tv;
	private View activity_read_note;
	private EditText title_tv;
	private String oldTitle;
	private String enSectionText;
	private MyEnglishAsyncTask myEnglishAsyncTask;
	private RelativeLayout right_rl;
	private int winWidth;
	private String[] titles;
	private String[] simpleTitle;
	private int[] contentNums;
	private RelativeLayout bottom_menu;
	private boolean isMenu_buttonUp = true;
	private boolean isFontSizeUp = false;
	private ImageView up_iv;
	int uptop = 0;
	int upleft = 0;
	int upright = 0;
	int upbottom = 0;
	public boolean isTitleBarAppear = true;
	private RelativeLayout titleBar;
	private LinearLayout righr_menu;
	private RelativeLayout fontsize;
	private SharedPreferences spReadbible_config;
	private Editor editorReadbible_config;
	private int defaultSectionFontSize = 20;
	private int defaultSectionIndexFontSize = 15;
	private int fontSize = 0;
	private boolean abc;
	private RelativeLayout light;
	public boolean isLightUp;
	private SeekBar seekbar;
	private int mBrightnessProgress;
	private TextView system_tv;
	protected boolean isSystemPressed;
	private RelativeLayout longclick_menu;
	public boolean isLongClick;
	public int touchPosition;
	private TextView highlight;
	private boolean isFontColorUp;
	private RelativeLayout fontcolor;
	int[] fontcolors = { R.color.highlight, R.color.highlight1,
			R.color.highlight2, R.color.highlight3, R.color.highlight4,
			R.color.highlight5 };
	private int colorIndex;
	private int[] fontcolorPressedDrawable = { R.drawable.fontcolor1_pressed,
			R.drawable.fontcolor2_pressed, R.drawable.fontcolor3_pressed,
			R.drawable.fontcolor4_pressed, R.drawable.fontcolor5_pressed,
			R.drawable.fontcolor6_pressed };
	private int[] fontcolorDefaultDrawable = { R.drawable.fontcolor1_default,
			R.drawable.fontcolor2_default, R.drawable.fontcolor3_default,
			R.drawable.fontcolor4_default, R.drawable.fontcolor5_default,
			R.drawable.fontcolor6_default };
	private List<ImageView> fontColorIvs = new ArrayList<ImageView>();
	private UMShare umShare;
	private TextView note_book_tv;
	private ScaleGestureDetector mScaleDetector;
	private GestureDetector mGestureDetector;
	protected String strSelection;
	private TranslateClient client;
	private AlertDialog fluentAlert;
	private LinearLayout popView;
	private PopupWindow popWin;
	private List<String> bookMarkList = new ArrayList<String>();
	private String bookMarkName1;
	public AlertDialog dialog;
	private String prefix;
	private String postfix;
	protected String translateResult = "";
	private float screenBrightness;
	private View addFontSizeView;
	private ImageView addfontsize;
	private ImageView subfontsize;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		new getBookMarkAsynTask().execute();
		
		/**隐藏软键盘**/
        View view = getWindow().peekDecorView();
        if (view != null) {
            InputMethodManager inputmanger = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            inputmanger.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }

		//initTransClient();// 初始化翻译相关功能
		// initLang();// 初始化语音方向
		umShare = new UMShare(this);
		spReadbible_config = getSharedPreferences("readbible_config",
				Context.MODE_PRIVATE);
		editorReadbible_config = spReadbible_config.edit();

		// ChenJin.set(Activity_ReadBible2.this);

		getScreenBrightness();

		titles = new String[] { "创世记", "出埃及记", "利未记", "民数记", "申命记", "约书亚记",
				"士师记", "路得记", "撒母耳记上", "撒母耳记下", "列王纪上", "列王纪下", "历代志上", "历代志下",
				"以斯拉记", "尼希米记", "以斯帖记", "约伯记", "诗篇", "箴言", "传道书", "雅歌", "以赛亚书",
				"耶利米书", "耶利米哀歌", "以西结书", "但以理书", "何西阿书", "约珥书", "阿摩司书",
				"俄巴底亚书", "约拿书", "弥迦书", "那鸿书", "哈巴谷书", "西番雅书", "哈该书", "撒迦利亚书",
				"玛拉基书", "马太福音", "马可福音", "路加福音", "约翰福音", "使徒行传", "罗马书", "哥林多前书",
				"哥林多后书", "加拉太书", "以弗所书", "腓立比书", "歌罗西书", "帖撒罗尼迦前书", "帖撒罗尼迦后书",
				"提摩太前书", "提摩太后书", "提多书", "腓利门书", "希伯来书", "雅各书", "彼得前书", "彼得后书",
				"约翰一书", "约翰二书", "约翰三书", "犹大书", "启示录" };
		simpleTitle = new String[] { "创", "出", "利", "民", "申", "书", "士", "得",
				"撒上", "撒下", "王上", "王下", "代上", "代下", "拉", "尼", "斯", "伯", "诗",
				"箴", "传", "歌", "赛", "耶", "哀", "结", "但", "何", "珥", "摩", "俄",
				"拿", "弥", "鸿", "哈", "番", "该", "亚", "玛", "太", "可", "路", "约",
				"徒", "罗", "林前", "林后", "加", "弗", "腓", "西", "帖前", "帖后", "提前",
				"提后", "多", "门", "来", "雅", "彼前", "彼后", "约一", "约二", "约三", "犹",
				"启" };
		contentNums = new int[] { 50, 40, 27, 36, 34, 24, 21, 4, 31, 24, 22,
				25, 29, 36, 10, 13, 10, 42, 150, 31, 12, 8, 66, 52, 5, 48, 12,
				14, 3, 9, 1, 4, 7, 3, 3, 3, 2, 14, 4, 28, 16, 24, 21, 28, 16,
				16, 13, 6, 6, 4, 4, 5, 3, 6, 4, 3, 1, 13, 5, 5, 3, 5, 1, 1, 1,
				22 };

		mMenuDrawer.setContentView(R.layout.activity_readbible1);
		mMenuDrawer.setMenuView(R.layout.slidemenu_readbible_right);
		mMenuDrawer.setSlideDrawable(R.drawable.ic_drawer);
		mMenuDrawer.setTouchMode(MenuDrawer.TOUCH_MODE_BEZEL);
		mMenuDrawer.setOffsetMenuEnabled(true);// 后面的menu动不动
		// mMenuDrawer.setAlpha(0);
		// mMenuDrawer.setAllowIndicatorAnimation(animate);
		winWidth = getWindowManager().getDefaultDisplay().getWidth();
		mMenuDrawer.setMenuSize(winWidth - 100);

		mMenuDrawer
				.setOnDrawerStateChangeListener(new OnDrawerStateChangeListener() {

					@Override
					public void onDrawerStateChange(int oldState, int newState) {
						if (newState == MenuDrawer.STATE_CLOSED) {
							Log.i("setOnDrawerStateChangeListener",
									"setOnDrawerStateChangeListener");
							if (note.getText() != null
									|| !"".equals(note.getText().toString())) {
								store();
							}

						}

					}

					@Override
					public void onDrawerSlide(float openRatio, int offsetPixels) {
						// TODO Auto-generated method stub

					}
				});
		// mMenuDrawer.setOffsetMenuEnabled(true);
		// mMenuDrawer.setDrawerIndicatorEnabled(true);
		// mMenuDrawer.setTouchMode(MenuDrawer.TOUCH_MODE_FULLSCREEN);
		createList();
		// 初始化界面
		initView();
		// 取得之前activity传过来的值
		getIntentValue();
		// 取圣经数据
		new MyAsyncTask().execute();

		myEnglishAsyncTask = new MyEnglishAsyncTask();
		myEnglishAsyncTask.execute();

		// initRightMenu();

	}

	@Override
	public void onStop() {
		if (note.getText() != null || !"".equals(note.getText().toString())) {
			store();
		}
		super.onStop();

	}

	private void initView() {
		// 初始化数据库
		dbHelper = new DatabaseHelper(getApplicationContext(), "bible.db");
		db = dbHelper.getReadableDatabase();
		// 初始化控件
		addfontsize=(ImageView)findViewById(R.id.addfontsize);
		subfontsize=(ImageView)findViewById(R.id.subfontsize);
		hint_tv = (TextView) findViewById(R.id.hint_tv);
		titleBar = (RelativeLayout) findViewById(R.id.titleBar);
		hint_tv = (TextView) findViewById(R.id.hint_tv);
		title_bar_book = (TextView) findViewById(R.id.title_bar_book);
		title_bar_book.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(Activity_ReadBible2.this,
						Activity_SelectVolume1.class);
				if (chapterIndexInBook <= 929) {
					intent.putExtra("from", "old");
				} else {
					intent.putExtra("from", "new");
				}
				intent.putExtra("chapterIndex",
						bibleData.get(1).get("chapterIndex"));
				intent.putExtra("bookName", bibleData.get(1).get("bookName"));
				startActivity(intent);
				overridePendingTransition(R.anim.bottom2top_in,
						R.anim.bottom2top_out);
			}
		});
		righr_menu = (LinearLayout) findViewById(R.id.righr_menu);
		righr_menu.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (mMenuDrawer.isActivated()) {
					mMenuDrawer.closeMenu();
				} else {
					mMenuDrawer.openMenu();
				}

			}
		});

		fontcolor = (RelativeLayout) findViewById(R.id.fontcolor);
		longclick_menu = (RelativeLayout) findViewById(R.id.longclick_menu);
		bottom_menu = (RelativeLayout) findViewById(R.id.bottom_menu);
		fontsize = (RelativeLayout) findViewById(R.id.fontsize);
		light = (RelativeLayout) findViewById(R.id.light);
		seekbar = (SeekBar) findViewById(R.id.seekbar);
		seekbar.setMax(255);
		seekbar.setProgress(spReadbible_config.getInt("progress",
				getScreenBrightness()));
		screenBrightness = (float) LightnessControl
				.GetLightness(Activity_ReadBible2.this);
		// Log.i("screenBrightness", "rr"+screenBrightness);
		setScreenLight();

		system_tv = (TextView) findViewById(R.id.system_tv);
		system_tv.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (isSystemPressed) {
					system_tv.setBackgroundResource(R.drawable.system_default);
					isSystemPressed = false;

					WindowManager.LayoutParams lp = getWindow().getAttributes();
					lp.screenBrightness = screenBrightness / 255;
					getWindow().setAttributes(lp);
					seekbar.setProgress((int) (screenBrightness));

				} else {
					system_tv.setBackgroundResource(R.drawable.system_pressed);
					/*Settings.System.putInt(getContentResolver(),
							Settings.System.SCREEN_BRIGHTNESS_MODE, 1);*/
					isSystemPressed = true;

					screenBrightness = LightnessControl
							.GetLightness(Activity_ReadBible2.this);

					WindowManager.LayoutParams lp = getWindow().getAttributes();
					lp.screenBrightness = -1;
					getWindow().setAttributes(lp);
				}

				/*
				 * if(LightnessControl.isAutoBrightness(Activity_ReadBible2.this)
				 * ) {
				 * LightnessControl.stopAutoBrightness(Activity_ReadBible2.this
				 * ); } else {
				 * LightnessControl.startAutoBrightness(Activity_ReadBible2
				 * .this); }
				 */

			}
		});

		title_bar_chapter = (TextView) findViewById(R.id.title_bar_chapter);
		title_bar_chapter.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(Activity_ReadBible2.this,
						Activity_SelectVolume1.class);
				if (chapterIndexInBook <= 929) {
					intent.putExtra("from", "old");
				} else {
					intent.putExtra("from", "new");
				}
				intent.putExtra("type", "chapter");
				intent.putExtra("bookIndex", bibleData.get(1).get("bookIndex"));
				Log.i("cha45", bibleData.get(1).get("chapterIndex") + "jj");
				intent.putExtra("bookName", bibleData.get(1).get("bookName"));
				startActivity(intent);
				overridePendingTransition(R.anim.bottom2top_in,
						R.anim.bottom2top_out);

			}
		});
		vp = (ViewPager) findViewById(R.id.vp);
		backgroundRl = (RelativeLayout) findViewById(R.id.rl);
		backgroundRl.setBackgroundColor(getResources().getColor(
				spReadbible_config.getInt("background_color", R.color.white)));

		// highlight=(TextView)findViewById(R.id.highlight);
		ImageView fontcolor6 = (ImageView) findViewById(R.id.fontcolor6);
		ImageView fontcolor5 = (ImageView) findViewById(R.id.fontcolor5);
		ImageView fontcolor4 = (ImageView) findViewById(R.id.fontcolor4);
		ImageView fontcolor3 = (ImageView) findViewById(R.id.fontcolor3);
		ImageView fontcolor2 = (ImageView) findViewById(R.id.fontcolor2);
		ImageView fontcolor1 = (ImageView) findViewById(R.id.fontcolor1);

		fontColorIvs.add(fontcolor1);
		fontColorIvs.add(fontcolor2);
		fontColorIvs.add(fontcolor3);
		fontColorIvs.add(fontcolor4);
		fontColorIvs.add(fontcolor5);
		fontColorIvs.add(fontcolor6);
		fontColorIvs.get(spReadbible_config.getInt("fontcolor_index", 4))
				.setImageResource(
						fontcolorPressedDrawable[spReadbible_config.getInt(
								"fontcolor_index", 4)]);

	}

	private void getIntentValue() {
		Intent intent = getIntent();
		if (intent.getStringExtra("bookmarkName") != null) {
			bookMarkName1 = intent.getStringExtra("bookmarkName");
		}
		volume = intent.getStringExtra("volume");
		if (intent.getStringExtra("keyword") != null) {
			keyword = intent.getStringExtra("keyword");
		}
		chapterIndexInBook = intent.getIntExtra("ChapterIndexInBook", 1);
		Log.i("chapterIndexInBookjj", chapterIndexInBook + "jj");
		leftEdge = rightEdge = chapterIndexInBook;
		simpleVolume = intent.getStringExtra("simpleVolume");
		chapter = intent.getStringExtra("chapter");
		hint_tv.setText(volume + "  " + chapter);
		title_bar_book.setText(volume);
		title_bar_chapter.setText(chapter);
		// hint_tv.setText(volume+"  "+chapter);
	}

	public SpannableString readEnglishFromSQL(int index) {
		Log.i("readFromSQL", "readFromSQL");
		positionStart.clear();
		positionEnd.clear();
		indexpositionStart.clear();
		indexpositionEnd.clear();
		titlepositionStart.clear();
		titlepositionEnd.clear();
		dbHelper = new DatabaseHelper(getApplicationContext(), "bible.db");
		db = dbHelper.getReadableDatabase();
		String reult = "";
		int len = 1;
		Cursor cursorTemp = db
				.rawQuery(
						"select enSectionText,isTitle,sectionIndex,bookName,chapterIndex from holybible where chapterIndexInBook=? ",
						new String[] { index + "" });
		int i = 1;
		while (cursorTemp.moveToNext()) {
			String temp = null;
			String getDataString = "";
			int isTitle = cursorTemp.getInt(cursorTemp
					.getColumnIndex("isTitle"));
			int sectionIndex = cursorTemp.getInt(cursorTemp
					.getColumnIndex("sectionIndex"));
			getDataString = cursorTemp.getString(cursorTemp
					.getColumnIndex("enSectionText"));
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
					indexpositionEnd
							.add(Integer.parseInt((len + (sectionIndex + "")
									.length()) + ""));
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

		Log.i("indexpositionStart", indexpositionStart.size() + "hh");
		/*
		 * for (int k = 0; k < indexpositionStart.size(); k++) {
		 * spannableString.setSpan(new AbsoluteSizeSpan(30),
		 * indexpositionStart.get(k) - 1, indexpositionEnd.get(k) - 1,
		 * Spanned.SPAN_EXCLUSIVE_EXCLUSIVE); }
		 */

		return spannableString;

	}

	public List<Map<String, String>> readFromSQL() {

		Cursor cursorTemp = null;

		for (int k = 0; k < 3; k++) {
			Map<String, String> map = new HashMap<String, String>();
			cursorTemp = db
					.rawQuery(
							"select enSectionText,bookName,chapterIndex,id,firstFlag,bookIndex,sectionText,isTitle,sectionIndex, chapterIndexInBook from holybible where chapterIndexInBook=?",
							new String[] { chapterIndexInBook - 1 + k + "" });
			Log.i("cursorTemp", cursorTemp.getCount() + "yy");
			String reult = "";
			int len = 1;
			// int enLen = 1;
			int i = 1;
			while (cursorTemp.moveToNext()) {

				String temp = null;
				// String enTemp=null;
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
				// enSectionText =
				// cursorTemp.getString(cursorTemp.getColumnIndex("enSectionText"));
				int chapterIndex = cursorTemp.getInt(cursorTemp
						.getColumnIndex("chapterIndex"));
				// 记录一次就够了
				if (i == 1) {
					// map.put("bookIndex", bookIndex + "");
					map.put("bookIndex", bookIndex + "");
					map.put("bookName", bookName);
					map.put("chapterIndex", chapterIndex + "");
				}

				if (sectionIndex > 1) {
					getDataString = sectionIndex + " " + getDataString;
					// enSectionText=sectionIndex + " " + enSectionText;
				}
				if (isTitle == 1) {
					// enTemp="\n        ";
					if (i == 1) {
						temp = getDataString + "\n        ";
						// enTemp="\n        ";

					} else {
						temp = "\n" + getDataString + "\n        ";
					}

				} else {
					// enTemp=enSectionText + "  ";
					temp = getDataString + "  ";
				}
				reult = reult + temp;
				// enSectionText=enSectionText+enTemp;
				// map.put("reult", reult);
				if (isTitle == 0) {
					Log.i("是title", i + "");
					if (sectionIndex >= 2) {
						indexpositionStartL.get(k).add(
								Integer.parseInt(len + ""));
						indexpositionEndL.get(k).add(
								Integer.parseInt((len + (sectionIndex + "")
										.length()) + ""));

						// indexpositionStart.add(Integer.parseInt(enLen + ""));
						// indexpositionEnd.add(Integer.parseInt((enLen +
						// (sectionIndex + "").length()) + ""));
					}
					if (firstFlag == 1) {
						positionStartL.get(k).add(Integer.parseInt(len + ""));
						// positionStart.add(Integer.parseInt(len + ""));
						highLightStartL.get(k).add(Integer.parseInt(len + ""));
						len = len + temp.length();
						// enLen=enLen+enTemp.length();
						highLightEndL.get(k).add(Integer.parseInt(len + ""));
						positionEndL.get(k).add(Integer.parseInt(len + ""));
						// positionEnd.add(Integer.parseInt(len + ""));
						shareContentIndexL.get(k).add(sectionIndex);
						idsL.get(k).add(id);

					} else {
						positionStartL.get(k).add(Integer.parseInt(len + ""));
						// positionStart.add(Integer.parseInt(len + ""));
						len = len + temp.length();
						// enLen=enLen+enTemp.length();
						positionEndL.get(k).add(Integer.parseInt(len + ""));
						// positionEnd.add(Integer.parseInt(len + ""));
						shareContentIndexL.get(k).add(sectionIndex);
						idsL.get(k).add(id);
					}

				} else {
					titlepositionStartL.get(k).add(Integer.parseInt(len + ""));
					len = len + temp.length();
					titlepositionEndL.get(k).add(Integer.parseInt(len + ""));

				}

				i++;

			}
			reult = reult + "\n";
			map.put("reult", reult);
			// map.put("enResult", enSectionText);
			bibleData.add(map);
			cursorTemp.close();
			// data = reult;
			// Log.i("dataacv", enSectionText);

		}
		// Log.i("bibleDatasize", bibleData.size()+"hhh");
		return bibleData;
	}

	private void initViewPager(List<Map<String, String>> data) {

		Log.i("ert0", bibleData.size() + "ttt");
		setSpan(data);
		Log.i("ert1", bibleData.size() + "ttt");
		for (int i = 0; i < 1189; i++) {
			ScrollView sv = new ScrollView(getApplicationContext());
			sv.setPadding(30, 10, 30, 90);
			sv.setVerticalScrollBarEnabled(false);
			sv.setBackgroundColor(getResources().getColor(R.color.white));// relPicIds[0]
			TextView tv = new TextView(getApplicationContext());
			// tv.setPadding(left, top, right, bottom)
			// tv.setMovementMethod(ScrollingMovementMethod.getInstance());
			tv.setTextSize(spReadbible_config.getInt("fontsize",
					defaultSectionFontSize));
			tv.setLineSpacing(12.0f, 1.0f);
			// tv.setPadding(30, 10, 30, 100);
			tv.setTextColor(Color.parseColor("#3F3F3E"));
			tv.setBackgroundColor(getResources().getColor(R.color.white));
			// Typeface face = Typeface.createFromAsset (getAssets() ,
			// "fonts/Roboto-Black.ttf" );
			tv.setTypeface(Typeface.SERIF);
			tv.setOnClickListener(new MyOnClicListener());
			tv.setOnLongClickListener(new MyOnLongClicListener());
			tv.setOnTouchListener(new MyOnTouchListener());
			// tv.setMovementMethod(ScrollingMovementMethod.getInstance());
			// tv.setOnLongClickListener(new MyOnLongClickListener());
			// tv.setOnTouchListener(new MyOnTouchListener());
			// tv.setOnClickListener(new MyOnClicListener());
			sv.addView(tv);
			tvs.add(sv);
		}
		if (chapterIndexInBook == 1) {
			((TextView) tvs.get(chapterIndexInBook - 1).getChildAt(0))
					.setText(spanL.get(1));
			// tvs.get(chapterIndexInBook-1).setList(positionStartL.get(1),positionEndL.get(1));
			((TextView) tvs.get(chapterIndexInBook).getChildAt(0))
					.setText(spanL.get(2));
			// tvs.get(chapterIndexInBook).setList(positionStartL.get(2),positionEndL.get(2));
		} else if (chapterIndexInBook == 1189) {
			((TextView) tvs.get(chapterIndexInBook - 2).getChildAt(0))
					.setText(spanL.get(0));
			// tvs.get(chapterIndexInBook-2).setList(positionStartL.get(0),positionEndL.get(0));
			((TextView) tvs.get(chapterIndexInBook - 1).getChildAt(0))
					.setText(spanL.get(1));
			// tvs.get(chapterIndexInBook-1).setList(positionStartL.get(1),positionEndL.get(1));
		} else {
			((TextView) tvs.get(chapterIndexInBook - 2).getChildAt(0))
					.setText(spanL.get(0));
			// tvs.get(chapterIndexInBook-2).setList(positionStartL.get(0),positionEndL.get(0));
			((TextView) tvs.get(chapterIndexInBook - 1).getChildAt(0))
					.setText(spanL.get(1));
			// tvs.get(chapterIndexInBook-1).setList(positionStartL.get(1),positionEndL.get(1));
			((TextView) tvs.get(chapterIndexInBook).getChildAt(0))
					.setText(spanL.get(2));
			// tvs.get(chapterIndexInBook).setList(positionStartL.get(2),positionEndL.get(2));
		}
		Log.i("ert2", bibleData.size() + "ttt");
		// nextDB += 1;
		// transDataToRigahtFragment();
		vp.setOnPageChangeListener(new MyOnPageChangeListener());
		Log.i("ert3", bibleData.size() + "ttt");
		vp.setAdapter(new MyPagerAdapter());
		Log.i("ert4", bibleData.size() + "ttt");
		vp.setCurrentItem(chapterIndexInBook - 1);

		Log.i("ert", bibleData.size() + "ttt");
	}

	public void setSpan(List<Map<String, String>> data) {

		if (fontSize == 0) {
			fontSize = spReadbible_config.getInt("fontsize",
					defaultSectionFontSize);
			Log.i("ggg", fontSize + "dd");
		}

		for (int j = 0; j < 3; j++) {

			SpannableStringBuilder spannableString = new SpannableStringBuilder(
					data.get(j).get("reult"));

			if (j == 1 && list.size() != 0) {
				// 77BFD1
				for (int i = 0; i < list.size(); i++) {
					spannableString
							.setSpan(
									new BackgroundColorSpan(Color
											.parseColor("#77BFD1")), list
											.get(i).get("start"), list.get(i)
											.get("end"),
									Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
					spannableString.setSpan(new ForegroundColorSpan(
							getResources().getColor(R.color.white)), list
							.get(i).get("start"), list.get(i).get("end"),
							Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
				}
			}
			// editorReadbible_config.putInt("fontcolor_index", 0);
			for (int i = 0; i < highLightStartL.get(j).size(); i++) {
				if (chapterIndexInBook == 1) {
					if (j != 0) {
						spannableString
								.setSpan(
										new ForegroundColorSpan(
												getResources()
														.getColor(
																fontcolors[spReadbible_config
																		.getInt("fontcolor_index",
																				0)])),
										highLightStartL.get(j).get(i) - 1,
										highLightEndL.get(j).get(i) - 1,
										Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
					}
				} else {
					spannableString.setSpan(new ForegroundColorSpan(
							getResources().getColor(fontcolors[colorIndex])),
							highLightStartL.get(j).get(i) - 1, highLightEndL
									.get(j).get(i) - 1,
							Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
				}

			}
			for (int i = 0; i < titlepositionStartL.get(j).size(); i++) {

				if (chapterIndexInBook == 1) {
					if (j != 0) {

						spannableString.setSpan(new StyleSpan(Typeface.BOLD),
								titlepositionStartL.get(j).get(i) - 1,
								titlepositionEndL.get(j).get(i) - 1,
								Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
						// spannableString.setSpan(new AbsoluteSizeSpan((int)
						// (2*fontSize)), titlepositionStartL.get(j).get(i) - 1,
						// titlepositionEndL.get(j).get(i) - 1,
						// Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
					}
				} else {

					spannableString.setSpan(new StyleSpan(Typeface.BOLD),
							titlepositionStartL.get(j).get(i) - 1,
							titlepositionEndL.get(j).get(i) - 1,
							Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
					// spannableString.setSpan(new AbsoluteSizeSpan((int)
					// (2*fontSize)), titlepositionStartL.get(j).get(i) - 1,
					// titlepositionEndL.get(j).get(i) - 1,
					// Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
				}

			}
			for (int i = 0; i < indexpositionStartL.get(j).size(); i++) {

				if (chapterIndexInBook == 1) {
					if (j != 0) {
						Log.i("config", (int) (2 * fontSize) + "ff");
						spannableString.setSpan(new AbsoluteSizeSpan(
								2 * fontSize - 10), indexpositionStartL.get(j)
								.get(i) - 1,
								indexpositionEndL.get(j).get(i) - 1,
								Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
					}
				} else {
					Log.i("config", (int) (2 * fontSize) - 10 + "ff");
					spannableString.setSpan(new AbsoluteSizeSpan(
							2 * fontSize - 10),
							indexpositionStartL.get(j).get(i) - 1,
							indexpositionEndL.get(j).get(i) - 1,
							Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
				}
			}
			int start = 0;
			if (keyword != null) {
				while (true) {
					if (data.get(j).get("reult").indexOf(keyword, start) != data
							.get(j).get("reult").lastIndexOf(keyword)) {
						spannableString.setSpan(new ForegroundColorSpan(
								getResources().getColor(R.color.keyword)), data
								.get(j).get("reult").indexOf(keyword, start),
								data.get(j).get("reult")
										.indexOf(keyword, start)
										+ keyword.length(),// 2+resultString.indexOf("示")+1
								Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
						start = data.get(j).get("reult")
								.indexOf(keyword, start) + 1;
					} else {
						break;
					}
				}
			}

			spanL.add(spannableString);
			// result.setText(spannableString);
		}

	}

	class MyAsyncTask extends
			AsyncTask<Void, TextView, List<Map<String, String>>> {
		@Override
		protected void onPreExecute() {
			dialog = new MyProgressPopUpWindow(Activity_ReadBible2.this,
					"正在加载中...").createADialog();
			super.onPreExecute();
		}

		@Override
		protected void onPostExecute(List<Map<String, String>> result) {
			List<Map<String, String>> list = new ArrayList<Map<String, String>>();
			list.addAll(result);
			Log.i("qwe", list.size() + "qqq");
			initViewPager(result);
			Log.i("qwer", list.size() + "qqq");
			// initRightMenu(list);
			dialog.cancel();
			super.onPostExecute(result);
		}

		@Override
		protected List<Map<String, String>> doInBackground(Void... params) {
			return readFromSQL();
		}

	}

	class MyAsyncTask2 extends
			AsyncTask<String, Void, List<Map<String, String>>> {

		@Override
		protected void onPreExecute() {
			for (int i = 0; i < 3; i++) {
				shareContentIndexL.get(i).clear();
				positionStartL.get(i).clear();
				positionEndL.get(i).clear();
				indexpositionStartL.get(i).clear();
				indexpositionEndL.get(i).clear();
				titlepositionStartL.get(i).clear();
				titlepositionEndL.get(i).clear();
				shareContentIndexL.get(i).clear();

			}
			spanL.clear();
			bibleData.clear();
			super.onPreExecute();
		}

		@Override
		protected List<Map<String, String>> doInBackground(String... params) {
			return readFromSQL();
		}

		@Override
		protected void onPostExecute(List<Map<String, String>> result) {
			setSpan(result);
			if (right) {
				if (chapterIndexInBook != 1) {
					Log.i("tvs", chapterIndexInBook - 2 + "  " + spanL.get(0));
					((TextView) tvs.get(chapterIndexInBook - 2).getChildAt(0))
							.setText(spanL.get(0));
					// tvs.get(chapterIndexInBook-2).setList(positionStartL.get(0),positionEndL.get(0));
				}
			}
			if (left) {
				if (chapterIndexInBook != 1189) {
					Log.i("tvs", chapterIndexInBook + "  " + spanL.get(2));
					((TextView) tvs.get(chapterIndexInBook).getChildAt(0))
							.setText(spanL.get(2));
					// tvs.get(chapterIndexInBook).setList(positionStartL.get(2),positionEndL.get(2));
				}

			}
			// chapter=bibleData.get(1).get("chapterIndex");
			title_bar_chapter.setText(bibleData.get(1).get("chapterIndex"));
			title_bar_book.setText(bibleData.get(1).get("bookName"));
			note_book_tv.setText(bibleData.get(1).get("bookName") + "  "
					+ bibleData.get(1).get("chapterIndex"));
			hint_tv.setText(bibleData.get(1).get("bookName") + "  "
					+ bibleData.get(1).get("chapterIndex"));
			super.onPostExecute(result);
		}

	}

	class MyEnglishAsyncTask extends AsyncTask<Void, Void, SpannableString> {

		@Override
		protected SpannableString doInBackground(Void... params) {
			Log.i("chapterIndexInBook", "chapterIndexInBook:"
					+ chapterIndexInBook);
			return readEnglishFromSQL(chapterIndexInBook);
		}

		@Override
		protected void onPostExecute(SpannableString result) {
			initRightMenu(result);
			super.onPostExecute(result);

		}

	}

	// 翻页加载
	class MyEnglishAsyncTask2 extends AsyncTask<Void, Void, SpannableString> {

		@Override
		protected SpannableString doInBackground(Void... params) {
			Log.i("chapterIndexInBook", "chapterIndexInBook:"
					+ chapterIndexInBook);
			return readEnglishFromSQL(chapterIndexInBook);
		}

		@Override
		protected void onPostExecute(SpannableString result) {
			english_tv.setText(result);
			webView.loadUrl(prefix + chapterIndexInBook + postfix);
			super.onPostExecute(result);

		}

	}

	class MyOnPageChangeListener implements OnPageChangeListener {

		private boolean isScrolling;
		private int lastValue;

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

			chapterIndexInBook = arg0 + 1;

			Log.i("chapterIndexInBook", chapterIndexInBook + "yy");
			new MyAsyncTask2().execute(chapterIndexInBook + "");
			new MyEnglishAsyncTask2().execute();
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

	public void createList() {
		for (int i = 0; i < 3; i++) {
			ArrayList<Integer> indexpositionStart = new ArrayList<Integer>();
			indexpositionStartL.add(indexpositionStart);
		}
		for (int i = 0; i < 3; i++) {
			ArrayList<Integer> indexpositionEnd = new ArrayList<Integer>();
			indexpositionEndL.add(indexpositionEnd);
		}
		for (int i = 0; i < 3; i++) {
			ArrayList<Integer> highLightStart = new ArrayList<Integer>();
			highLightStartL.add(highLightStart);
		}
		for (int i = 0; i < 3; i++) {
			ArrayList<Integer> highLightEnd = new ArrayList<Integer>();
			highLightEndL.add(highLightEnd);
		}
		for (int i = 0; i < 3; i++) {
			ArrayList<Integer> shareContentIndex = new ArrayList<Integer>();
			shareContentIndexL.add(shareContentIndex);
		}
		for (int i = 0; i < 3; i++) {
			ArrayList<Integer> ids = new ArrayList<Integer>();
			idsL.add(ids);
		}
		for (int i = 0; i < 3; i++) {
			ArrayList<Integer> titlepositionStart = new ArrayList<Integer>();
			titlepositionStartL.add(titlepositionStart);
		}
		for (int i = 0; i < 3; i++) {
			ArrayList<Integer> titlepositionEnd = new ArrayList<Integer>();
			titlepositionEndL.add(titlepositionEnd);
		}
		for (int i = 0; i < 3; i++) {
			ArrayList<Integer> positionStart = new ArrayList<Integer>();
			positionStartL.add(positionStart);
		}
		for (int i = 0; i < 3; i++) {
			ArrayList<Integer> positionEnd = new ArrayList<Integer>();
			positionEndL.add(positionEnd);
		}
	}

	public void initRightMenu(SpannableString spannableString) {
		// Log.i("ert5", result.size()+"rrr");
		right_rl = (RelativeLayout) findViewById(R.id.right_rl);
		FrameLayout.LayoutParams linearParams = (FrameLayout.LayoutParams) right_rl
				.getLayoutParams();
		linearParams.width = winWidth - 100;// 控件的宽强制设成30

		right_rl.setLayoutParams(linearParams); // 使设置好的布局参数应用到控件</pre>
		vpRight = (ViewPager) findViewById(R.id.vpRight);
		PagerTabStrip tab = (PagerTabStrip) findViewById(R.id.tabRight);
		tab.setTabIndicatorColor(Color.parseColor("#C6C6C6"));
		inflater = LayoutInflater.from(getApplicationContext());
		View activity_read_english = inflater.inflate(
				R.layout.activity_read_english, null);
		activity_read_note = inflater
				.inflate(R.layout.activity_read_note, null);

		View activity_read_translate = inflater.inflate(
				R.layout.activity_read_translate, null);

		// tv = (TextView) view.findViewById(R.id.tv);
		// tv.setText(index);

		english_tv = (TextView) activity_read_english
				.findViewById(R.id.english_tv);

		setSelectedTextView(english_tv);

		vpRight.requestDisallowInterceptTouchEvent(true);

		// registerForContextMenu(english_tv);
		// Selection.setSelection(english_tv.getEditableText(), 0, 3);
		english_tv.setBackgroundColor(getResources().getColor(R.color.white));
		note = (TextView) activity_read_note.findViewById(R.id.book_text);
		note_book_tv = (TextView) activity_read_note
				.findViewById(R.id.note_book_tv);
		title_tv = (EditText) activity_read_note.findViewById(R.id.title_tv);
		date_tv = (TextView) activity_read_note.findViewById(R.id.date_tv);
		date_tv.setText(getCurrentDate());

		isDataExits(true);

		// Log.i("bibleDatarrr", bibleData.size()+"rrr");
		/*
		 * SpannableString spannableString = new
		 * SpannableString(result.get(1).get("enResult"));
		 * 
		 * for (int k = 0; k < indexpositionStart.size(); k++) {
		 * spannableString.setSpan(new AbsoluteSizeSpan(30),
		 * indexpositionStart.get(k) - 1, indexpositionEnd.get(k) - 1,
		 * Spanned.SPAN_EXCLUSIVE_EXCLUSIVE); }
		 */
		if (spannableString != null) {
			english_tv.setText(spannableString);
		}

		// Typeface face = Typeface.createFromAsset (getAssets() ,
		// "fonts/jiacujiashuti.ttf" );
		// english_tv.setTypeface (face);

		// note.setText("年底发可能蜂鸟网，到年底复活币备份，逗你玩非南方网");
		webView = (WebView) activity_read_translate.findViewById(R.id.webview);
		webView.setBackgroundColor(0);
		WebSettings web = webView.getSettings();
		web.setJavaScriptEnabled(true);
		web.setSupportZoom(false);
		// web.setTextSize(TextSize.LARGER);
		web.setMinimumFontSize(20);
		webView.setBackgroundColor(getResources().getColor(R.color.white));
		webView.getSettings().setBuiltInZoomControls(true);
		prefix = "file:///mnt/sdcard" + "/0000/";
		// uri的后缀
		postfix = ".htm";
		webView.loadUrl(prefix + chapterIndexInBook + postfix);

		RelativeLayout read_english = (RelativeLayout) activity_read_english
				.findViewById(R.id.read_english);
		LinearLayout read_note = (LinearLayout) activity_read_note
				.findViewById(R.id.read_note);
		RelativeLayout read_translate = (RelativeLayout) activity_read_translate
				.findViewById(R.id.read_translate);

		vList = new ArrayList<View>();
		titleList = new ArrayList<String>();

		titleList.add("英文");
		titleList.add("笔记");
		titleList.add("注释");

		vList.add(read_english);
		vList.add(read_note);
		vList.add(read_translate);
		vpRight.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageSelected(int arg0) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onPageScrollStateChanged(int arg0) {
				// TODO Auto-generated method stub

			}
		});
		vpRight.setAdapter(new MyRightPagerAdapter());
	}

	public String getCurrentDate() {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd");
		Date date = new Date(System.currentTimeMillis());
		return formatter.format(date);
	}

	class MyRightPagerAdapter extends PagerAdapter {

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

	/*
	 * public void menu_button() {
	 * 
	 * 
	 * 
	 * 
	 * if(isMenu_buttonUp) { Log.i("isMenu_buttonUp", "点击效果回复");
	 * TranslateAnimation animationout = new TranslateAnimation(0, 0, -140, 10);
	 * animationout.setDuration(100); animationout.setFillAfter(true);
	 * up_iv.startAnimation(animationout);
	 * 
	 * TranslateAnimation animationout1 = new TranslateAnimation(0, 0, 0, 50);
	 * animationout1.setDuration(100); animationout1.setFillAfter(true);
	 * bottom_menu.startAnimation(animationout1);
	 * bottom_menu.setVisibility(View.GONE); isMenu_buttonUp=false; } else {
	 * Log.i("isMenu_buttonUp", "点击"); TranslateAnimation animationin = new
	 * TranslateAnimation(0, 0, 0, -75); animationin.setDuration(100);
	 * animationin.setFillAfter(false); animationin.setAnimationListener(new
	 * AnimationListener() {
	 * 
	 * @Override public void onAnimationStart(Animation animation) {
	 * up_iv.setImageResource(R.drawable.menu_down);
	 * up_iv.setVisibility(View.GONE);
	 * 
	 * //up_iv.layout(up_iv.getLeft(), up_iv.getTop()-150,
	 * up_iv.getRight(),up_iv.getBottom()-150); up_iv.layout(upleft, uptop,
	 * upright, upbottom); up_iv.setVisibility(View.VISIBLE); }
	 * 
	 * @Override public void onAnimationRepeat(Animation animation) { // TODO
	 * Auto-generated method stub
	 * 
	 * }
	 * 
	 * @Override public void onAnimationEnd(Animation animation) {
	 * //up_iv.clearAnimation();
	 * 
	 * //up_iv.setVisibility(View.VISIBLE);
	 * 
	 * } }); up_iv.startAnimation(animationin);
	 * 
	 * bottom_menu.setVisibility(View.VISIBLE); TranslateAnimation animationin1
	 * = new TranslateAnimation(0, 0, 60, -10); animationin1.setDuration(100);
	 * animationin1.setFillAfter(true);
	 * bottom_menu.startAnimation(animationin1); isMenu_buttonUp=true; }
	 * 
	 * }
	 */
	class MyOnLongClicListener implements OnLongClickListener {

		@Override
		public boolean onLongClick(View v) {
			if (isTitleBarAppear) {
				TranslateAnimation animationout1 = new TranslateAnimation(0, 0,
						0, 100);
				animationout1.setDuration(200);
				animationout1.setFillAfter(true);

				TranslateAnimation animationout2 = new TranslateAnimation(0, 0,
						0, -90);
				animationout2.setDuration(200);
				animationout2.setFillAfter(true);
				bottom_menu.startAnimation(animationout1);
				bottom_menu.setVisibility(View.GONE);
				bottom_menu.setClickable(false);
				titleBar.startAnimation(animationout2);
				// titleBar.setVisibility(View.GONE);
				isTitleBarAppear = false;
			}

			if (isLightUp) {
				light.setVisibility(View.GONE);
				bottom_menu.setClickable(false);
				isLightUp = false;

				light.setVisibility(View.GONE);
				light.setClickable(false);
				isLightUp = false;
			}

			if (isFontSizeUp) {
				fontsize.setVisibility(View.GONE);
				fontsize.setClickable(false);
				isFontSizeUp = false;
				fontsize.setVisibility(View.GONE);
				fontsize.setClickable(false);
				isFontSizeUp = false;
			}

			if (!isLongClick) {
				showUnderLine();
				Log.i("showUnderLine", "showUnderLine();");
				TranslateAnimation animationin1 = new TranslateAnimation(0, 0,
						100, 0);
				animationin1.setDuration(200);
				animationin1.setFillAfter(true);
				longclick_menu.clearAnimation();
				longclick_menu.setVisibility(View.VISIBLE);
				longclick_menu.setAnimation(animationin1);
				isLongClick = true;
			}

			return true;
		}

	}

	class MyOnClicListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			if (!isLongClick) {
				if (isTitleBarAppear) {
					TranslateAnimation animationout1 = new TranslateAnimation(
							0, 0, 0, 100);
					animationout1.setDuration(200);
					animationout1.setFillAfter(true);

					TranslateAnimation animationout2 = new TranslateAnimation(
							0, 0, 0, -90);
					animationout2.setDuration(200);
					animationout2.setFillAfter(true);
					bottom_menu.startAnimation(animationout1);
					bottom_menu.setVisibility(View.GONE);
					bottom_menu.setClickable(false);
					titleBar.startAnimation(animationout2);
					// titleBar.setVisibility(View.GONE);
					isTitleBarAppear = false;
				} else {
					// titleBar.setVisibility(View.VISIBLE);
					bottom_menu.setVisibility(View.VISIBLE);
					TranslateAnimation animationin1 = new TranslateAnimation(0,
							0, 100, 0);
					animationin1.setDuration(200);
					animationin1.setFillAfter(true);

					TranslateAnimation animationin2 = new TranslateAnimation(0,
							0, -90, 0);
					animationin2.setDuration(200);
					animationin2.setFillAfter(true);
					bottom_menu.startAnimation(animationin1);
					titleBar.startAnimation(animationin2);
					isTitleBarAppear = true;
				}

				if (isLightUp) {
					light.setVisibility(View.GONE);
					light.setClickable(false);
					isLightUp = false;

					light.setVisibility(View.GONE);
					light.setClickable(false);
					isLightUp = false;
				}

				if (isFontSizeUp) {
					fontsize.setVisibility(View.GONE);
					fontsize.setClickable(false);
					isFontSizeUp = false;

					fontsize.setVisibility(View.GONE);
					fontsize.setClickable(false);
					isFontSizeUp = false;
				}
			} else {
				showUnderLine();
			}

		}

	}

	class MyOnTouchListener implements OnTouchListener {

		@Override
		public boolean onTouch(View v, MotionEvent event) {
			TextView curTv = (TextView) v;
			touchPosition = curTv.getOffsetForPosition(event.getX(),
					event.getY());
			Log.i("touch", "touch" + touchPosition);

			return false;

		}

	}

	public void fontsize(View view) {

		if (!isLongClick) {
			if (isFontSizeUp) {
				fontsize.setVisibility(View.GONE);
				fontsize.setClickable(false);
				isFontSizeUp = false;
			} else {
				fontsize.setVisibility(View.VISIBLE);
				isFontSizeUp = true;
			}

			if (isLightUp) {
				light.setVisibility(View.GONE);
				light.setClickable(false);
				isLightUp = false;

				light.setVisibility(View.GONE);
				light.setClickable(false);
				isLightUp = false;
			}
		}

	}

	public void light(View view) {
		if (!isLongClick) {
			if (isLightUp) {
				light.setVisibility(View.GONE);
				light.setClickable(false);
				isLightUp = false;
			} else {
				light.setVisibility(View.VISIBLE);
				isLightUp = true;
			}

			if (isFontSizeUp) {
				fontsize.setVisibility(View.GONE);
				fontsize.setClickable(false);
				isFontSizeUp = false;

				fontsize.setVisibility(View.GONE);
				fontsize.setClickable(false);
				isFontSizeUp = false;
			}
		}

	}

	public void addfontsize(View view) {
		abc = true;
	
		fontSize = spReadbible_config
				.getInt("fontsize", defaultSectionFontSize);
		fontSize = fontSize + 1;
		Log.i("fontsize", fontSize + "ff");
		if (fontSize < 10 || fontSize > 50) {
			Toast.makeText(getApplicationContext(), "已经达到临界值！",
					Toast.LENGTH_SHORT).show();
		} else {
			addfontsize.setClickable(false);
			editorReadbible_config.putInt("fontsize", fontSize);
			editorReadbible_config.commit();
			if (fontSize == defaultSectionFontSize) {
				Toast.makeText(getApplicationContext(), "当前文字大小为默认值",
						Toast.LENGTH_SHORT).show();
			}
			
			new MyAddFontSizeAsy().execute();

		}

	}

	public void subfontsize(View view) {

		abc = true;
		fontSize = spReadbible_config
				.getInt("fontsize", defaultSectionFontSize);
		fontSize = fontSize - 1;
		Log.i("fontsize", fontSize + "ff");
		if (fontSize < 10 || fontSize > 50) {
			Toast.makeText(getApplicationContext(), "已经达到临界值！",
					Toast.LENGTH_SHORT).show();
		} else {
			subfontsize.setClickable(false);
			editorReadbible_config.putInt("fontsize", fontSize);
			editorReadbible_config.commit();
			if (fontSize == defaultSectionFontSize) {
				Toast.makeText(getApplicationContext(), "当前文字大小为默认值",
						Toast.LENGTH_SHORT).show();
			}

			new MySubFontSizeAsy().execute();
		}

	}

	class MySubFontSizeAsy extends AsyncTask<Void, Void, Void> {

		@Override
		protected void onPreExecute() {
			for (int i = 0; i < tvs.size(); i++) {
				((TextView) tvs.get(i).getChildAt(0)).setTextSize(fontSize);
			}
			english_tv.setTextSize(fontSize);
			spanL.clear();
			super.onPreExecute();
		}

		@Override
		protected Void doInBackground(Void... params) {

			setSpan(bibleData);

			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			((TextView) tvs.get(chapterIndexInBook - 1).getChildAt(0))
					.setText(spanL.get(1));
			subfontsize.setClickable(true);
			super.onPostExecute(result);
		}

	}

	class MyAddFontSizeAsy extends AsyncTask<Void, Void, Void> {

		@Override
		protected void onPreExecute() {
			for (int i = 0; i < tvs.size(); i++) {
				((TextView) tvs.get(i).getChildAt(0)).setTextSize(fontSize);
			}
			english_tv.setTextSize(fontSize);
			spanL.clear();
			super.onPreExecute();
		}

		@Override
		protected Void doInBackground(Void... params) {

			setSpan(bibleData);

			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			((TextView) tvs.get(chapterIndexInBook - 1).getChildAt(0))
					.setText(spanL.get(1));
			super.onPostExecute(result);
			addfontsize.setClickable(true);
		}

	}

	/**
	 * 获取屏幕亮度
	 */
	private int getScreenBrightness() {

		int nowBrightnessValue = 0;
		ContentResolver resolver = getContentResolver();
		try {
			nowBrightnessValue = android.provider.Settings.System.getInt(
					resolver, Settings.System.SCREEN_BRIGHTNESS); // SCREEN_BRIGHTNESS
		} catch (Exception e) {
			e.printStackTrace();
		}
		Log.i("light", nowBrightnessValue + "");
		return nowBrightnessValue;
	}

	private void setScreenLight() {

		mBrightnessProgress = getScreenBrightness();
		// seekbar.setProgress(255);
		seekbar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
			@Override
			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser) {

				// TODO Auto-generated method stub
				if (fromUser) {
					WindowManager.LayoutParams lp = getWindow().getAttributes();
					lp.screenBrightness = (float) (progress) / 255f;
					Log.i("screenBrightness", lp.screenBrightness + "uu");
					getWindow().setAttributes(lp);
					editorReadbible_config.putInt("progress", progress);
				}
			}

			@Override
			public void onStartTrackingTouch(SeekBar arg0) {

			}

			@Override
			public void onStopTrackingTouch(SeekBar arg0) {

				// TODO Auto-generated method stub

			}
		});
	}

	public void recite(View view) {
		if (isLongClick) {
			updateReciteBible();
			exitLongClick();
			// list.clear();
			spanL.clear();
			setSpan(bibleData);
			((TextView) tvs.get(chapterIndexInBook - 1).getChildAt(0))
					.setText(spanL.get(1));
		}

	}

	public void showUnderLine() {

		int start;
		int end;
		int index;
		int idtemp;
		Map<String, Integer> map = new HashMap<String, Integer>();
		if (findChapter(touchPosition) != -1) {
			start = positionStartL.get(1).get(findChapter(touchPosition)) - 1;
			end = positionEndL.get(1).get(findChapter(touchPosition)) - 1;
			index = shareContentIndexL.get(1).get(findChapter(touchPosition));
			idtemp = idsL.get(1).get(findChapter(touchPosition));
			map.put("start", start);
			map.put("end", end);
			map.put("index", index);
			map.put("id", idtemp);
		}

		if (list.contains(map)) {
			list.remove(map);
		} else {
			list.add(map);
		}
		spanL.clear();
		setSpan(bibleData);
		((TextView) tvs.get(chapterIndexInBook - 1).getChildAt(0))
				.setText(spanL.get(1));
		/*
		 * //SpannableString spannableString = new SpannableString(data); for
		 * (int i = 0; i < list.size(); i++) { spannableString.setSpan(new
		 * UnderlineSpan(), list.get(i).get("start"), list.get(i).get("end"),
		 * Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		 * 
		 * } setSpan(spannableString);
		 * 
		 * return spannableString;
		 */

	}

	public int findChapter(int position) {
		int reult = 0;
		for (int i = 0; i < positionStartL.get(1).size(); i++) {
			if (i != positionStartL.get(1).size() - 1) {

				if (positionStartL.get(1).get(i) < position
						&& positionEndL.get(1).get(i) > position) {

					reult = i;
					break;
				}
			} else {
				Log.i("最后", "进入");
				reult = positionStartL.get(1).size() - 1;
				break;
			}
		}
		Log.i("reult", reult + "pp");
		return reult;
	}

	public void highlight(View view) {

		if (isLongClick) {
			if (isFontColorUp) {
				fontcolor.setVisibility(View.GONE);
				isFontColorUp = false;
			} else {
				fontcolor.setVisibility(View.VISIBLE);
				isFontColorUp = true;
			}
		}
	}

	public void fontcolor1(View view) {
		{
			updateHighLight();
			fontColorIvs.get(spReadbible_config.getInt("fontcolor_index", 4))
					.setImageResource(
							fontcolorDefaultDrawable[spReadbible_config.getInt(
									"fontcolor_index", 4)]);
			((ImageView) view).setImageResource(R.drawable.fontcolor1_pressed);
			editorReadbible_config.putInt("fontcolor_index", 0);
			editorReadbible_config.commit();
			colorIndex = 0;
		}
	}

	public void fontcolor2(View view) {
		if (isLongClick) {
			updateHighLight();
			fontColorIvs.get(spReadbible_config.getInt("fontcolor_index", 4))
					.setImageResource(
							fontcolorDefaultDrawable[spReadbible_config.getInt(
									"fontcolor_index", 4)]);
			((ImageView) view).setImageResource(R.drawable.fontcolor2_pressed);
			editorReadbible_config.putInt("fontcolor_index", 1);
			editorReadbible_config.commit();
			colorIndex = 1;
	
		}
	}

	public void fontcolor3(View view) {
		if (isLongClick) {
			updateHighLight();
			fontColorIvs.get(spReadbible_config.getInt("fontcolor_index", 4))
					.setImageResource(
							fontcolorDefaultDrawable[spReadbible_config.getInt(
									"fontcolor_index", 4)]);
			((ImageView) view).setImageResource(R.drawable.fontcolor3_pressed);
			editorReadbible_config.putInt("fontcolor_index", 2);
			editorReadbible_config.commit();
			colorIndex = 2;
	
		}
	}

	public void fontcolor4(View view) {
		if (isLongClick) {
			updateHighLight();
			fontColorIvs.get(spReadbible_config.getInt("fontcolor_index", 4))
					.setImageResource(
							fontcolorDefaultDrawable[spReadbible_config.getInt(
									"fontcolor_index", 4)]);
			((ImageView) view).setImageResource(R.drawable.fontcolor4_pressed);
			editorReadbible_config.putInt("fontcolor_index", 3);
			editorReadbible_config.commit();
			colorIndex = 3;
		
		}
	}

	public void fontcolor5(View view) {
		if (isLongClick) {
			updateHighLight();
			fontColorIvs.get(spReadbible_config.getInt("fontcolor_index", 4))
					.setImageResource(
							fontcolorDefaultDrawable[spReadbible_config.getInt(
									"fontcolor_index", 4)]);
			((ImageView) view).setImageResource(R.drawable.fontcolor5_pressed);
			editorReadbible_config.putInt("fontcolor_index", 4);
			editorReadbible_config.commit();
			colorIndex = 4;
	
		}
	}

	public void fontcolor6(View view) {
		if (isLongClick) {
			updateHighLight();
			fontColorIvs.get(spReadbible_config.getInt("fontcolor_index", 4))
					.setImageResource(
							fontcolorDefaultDrawable[spReadbible_config.getInt(
									"fontcolor_index", 4)]);
			((ImageView) view).setImageResource(R.drawable.fontcolor6_pressed);
			editorReadbible_config.putInt("fontcolor_index", 5);
			editorReadbible_config.commit();
			colorIndex = 5;
		
		}
	}

	public void setHighLightSpan() {
		for (int i = 0; i < list.size(); i++) {
			highLightStartL.get(1).add(list.get(i).get("start"));
			highLightEndL.get(1).add(list.get(i).get("end"));
		}
		list.clear();
		spanL.clear();
		setSpan(bibleData);
		((TextView) tvs.get(chapterIndexInBook - 1).getChildAt(0))
				.setText(spanL.get(1));
	}

	public void exitLongClick() {
		isLongClick = false;
		isFontColorUp = false;
		fontcolor.setVisibility(View.GONE);
		TranslateAnimation animationout1 = new TranslateAnimation(0, 0, 0, 200);
		animationout1.setDuration(200);
		animationout1.setFillAfter(true);
		longclick_menu.clearAnimation();
		longclick_menu.setAnimation(animationout1);

	}

	public void updateHighLight() {
		new AsyncTask<Void, Void, Void>() {

			@Override
			protected Void doInBackground(Void... params) {
				ContentValues values = new ContentValues();
				DatabaseHelper dbHelper = new DatabaseHelper(
						getApplicationContext(), "bible.db");
				SQLiteDatabase db = dbHelper.getWritableDatabase();

				Log.i("list", list.size() + "iii");
				for (int i = 0; i < list.size(); i++) {
					int id1 = list.get(i).get("id");
					values.put("firstFlag", 1);
					db.update("holybible", values, "id=?", new String[] { id1
							+ "" });
				}
				return null;
			}

			protected void onPostExecute(Void result) {

				setHighLightSpan();
				exitLongClick();
			};

		}.execute();

	}

	public void updateReciteBible() {

		new AsyncTask<Void, Void, Void>() {

			@Override
			protected Void doInBackground(Void... params) {
				ContentValues values = new ContentValues();
				DatabaseHelper dbHelper = new DatabaseHelper(
						getApplicationContext(), "bible.db");
				SQLiteDatabase db = dbHelper.getWritableDatabase();

				Log.i("list", list.size() + "ddd");
				for (int i = 0; i < list.size(); i++) {
					int id1 = list.get(i).get("id");
					Log.i("updateHighLight进入", "updateHighLight进入");
					values.put("secontFlag", 1);
					db.update("holybible", values, "id=?", new String[] { id1
							+ "" });
				}
				return null;
			}

			protected void onPostExecute(Void result) {
				list.clear();
				Toast.makeText(getApplicationContext(), "已经添加到背圣经模块！",
						Toast.LENGTH_SHORT).show();
			};

		}.execute();

	}

	// 得到分享内容
	public String getShareContent(String from) {
		sortData(list);
		String result = "";
		for (int i = 0; i < list.size(); i++) {
			if (i == list.size() - 1) {
				String temp = bibleData
						.get(1)
						.get("reult")
						.substring(list.get(i).get("start"),
								list.get(i).get("end"));
				temp = temp.replace(list.get(i).get("index") + "", "");
				// Log.i("index111", list.get(i).get("index")+"gg"+temp);
				result = result + temp;
			} else {
				String temp = bibleData
						.get(1)
						.get("reult")
						.substring(list.get(i).get("start"),
								list.get(i).get("end"))
						+ "...";
				temp = temp.replace(list.get(i).get("index") + "", "");
				result = result + temp;
			}

		}

		if (result.equals("")) {
			return "";
		} else {
			result = result + "[" + volume + "  " + chapter + ":"; // +"]"+"\n#圣经流利说#";
			for (int i = 0; i < list.size(); i++) {
				if (i == list.size() - 1) {
					result = result + list.get(i).get("index");
				} else {
					result = result + list.get(i).get("index") + ",";
				}

			}
			if (from.endsWith("note")) {
				return result + "]";
			} else {
				return result + "]\n#圣经流利说#";
			}

		}

	}

	private static List<Map<String, Integer>> sortData(
			List<Map<String, Integer>> list) {
		System.out.println("对WCM数据进行排序");
		if (list.size() > 1) {
			Comparator<Map<String, Integer>> mapComprator = new Comparator<Map<String, Integer>>() {
				@Override
				public int compare(Map<String, Integer> o1,
						Map<String, Integer> o2) {

					// do compare.
					if (o1.get("id").intValue() > o2.get("id").intValue()) {
						return 1;
					} else {
						return -1;
					}
				}
			};
			Collections.sort(list, mapComprator);
		} else {
			new Exception("排序没有取到数据");
		}
		return list;
	}

	public void note(View view) {
		if (isLongClick) {
			mMenuDrawer.toggleMenu();
			vpRight.setCurrentItem(1);
			String temp = note.getText().toString();
			note.setText(temp + "\r      " + getShareContent("note"));
			exitLongClick();
			list.clear();
			spanL.clear();
			setSpan(bibleData);
			((TextView) tvs.get(chapterIndexInBook - 1).getChildAt(0))
					.setText(spanL.get(1));
		}
	}

	public void copy(View view) {
		if (isLongClick) {
			list.clear();
			spanL.clear();
			setSpan(bibleData);
			((TextView) tvs.get(chapterIndexInBook - 1).getChildAt(0))
					.setText(spanL.get(1));
			ClipboardManager cmb = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
			ClipData clip = ClipData.newPlainText("text",
					getShareContent("copy"));
			cmb.setPrimaryClip(clip);
			Toast.makeText(getApplicationContext(), "已复制", Toast.LENGTH_SHORT)
					.show();
			exitLongClick();
		}

	}

	public void share(View view) {
		if (isLongClick) {
			String shareString = getShareContent("share");
			umShare.share("圣经流利说",shareString, "http://weibo.com/775747758", Activity_ReadBible2.this);
			list.clear();
			spanL.clear();
			setSpan(bibleData);
			((TextView) tvs.get(chapterIndexInBook - 1).getChildAt(0))
					.setText(spanL.get(1));
		}

	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if (isLongClick) {
				list.clear();
				exitLongClick();
				list.clear();
				spanL.clear();
				setSpan(bibleData);
				((TextView) tvs.get(chapterIndexInBook - 1).getChildAt(0))
						.setText(spanL.get(1));
			} else {
				upBookMark(bookMarkName1);
				finish();
			}

		}

		return true;
	}

	public void setSelectedTextView(TextView text) {
		text.setTextIsSelectable(true);
		text.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				TextView tv = (TextView) v;
				// TODO Auto-generated method stub
				if (tv.getSelectionEnd() - tv.getSelectionStart() != 0) {
					char[] bufTmp = new char[128]; // 需做文字长度是否超出短信范围判断
					strSelection = tv
							.getText()
							.toString()
							.substring(tv.getSelectionStart(),
									tv.getSelectionEnd());
					//initLang();
				}

				return false;
			}
		});
		/*
		 * text.setOnLongClickListener(new OnLongClickListener() {
		 * 
		 * @Override public boolean onLongClick(View v) {
		 * if(!"".equals(strSelection)&&strSelection!=null) { initLang(); }
		 * return false; } });
		 */
	}

	private void initTransClient() {
		client = new TranslateClient(Activity_ReadBible2.this,
				"GdZNITcYcI3bhiwKx7duVbR7");

		// 这里可以设置为在线优先、离线优先、 只在线、只离线 4种模式，默认为在线优先。
		client.setPriority(TranslateClient.Priority.OFFLINE_FIRST);

	}

	private void initLang() {

		client.translate(strSelection, "en", "zh", new ITransResultCallback() {

			@Override
			public void onResult(TransResult result) {

				translateResult = result.trans_result;

				/*
				 * // 翻译结果回调
				 * 
				 * 
				 * if (result == null) { Log.d("TransOpenApiDemo",
				 * "Trans Result is null");
				 * 
				 * } else
				 * if("".equals(translateResult)||!result.trans_result.equals
				 * (translateResult)) { translateResult=result.trans_result;
				 * Toast.makeText(getApplicationContext(), strSelection + "\n" +
				 * result.trans_result, Toast.LENGTH_SHORT).show(); }
				 */
				Toast.makeText(getApplicationContext(),
						strSelection + "\n" + result.trans_result,
						Toast.LENGTH_SHORT).show();

			}
		});
	}

	public void storeNote() {

		ContentValues values = new ContentValues();
		DatabaseHelper dbHelper = new DatabaseHelper(getApplicationContext(),
				"bible.db");
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		db.execSQL("create table  if not exists  note(title varchar(30),chapterIndexInBook int,volume varchar(20),chapter varchar(20),noteContent text,date varchar(20) )");
		values.put("chapterIndexInBook", chapterIndexInBook);
		values.put("volume", bibleData.get(1).get("bookName"));
		values.put("chapter", bibleData.get(1).get("chapterIndex"));
		values.put("noteContent", note.getText().toString());
		values.put("date", getCurrentDate());
		values.put("title", title_tv.getText().toString());
		db.insert("note", null, values);

	}

	public boolean isDataExits(boolean isSetData) {
		Log.i("进入isDataExits", "isDataExits");
		dbHelper = new DatabaseHelper(getApplicationContext(), "bible.db");
		db = dbHelper.getReadableDatabase();
		String reult = "";
		int count = 0;
		Cursor cursor = db
				.rawQuery(
						"SELECT count(*) FROM sqlite_master WHERE type='table' AND name='note'",
						null);
		if (cursor.moveToNext()) {
			count = cursor.getInt(0);
		}
		if (count == 0) {
			Log.i("不存在表", "不存在表");
			return false;

		} else {

			Cursor cursorTemp = db
					.rawQuery(
							"select title,noteContent from note where chapterIndexInBook=?",
							new String[] { chapterIndexInBook + "" });
			if (cursorTemp.getCount() == 0) {
				Log.i("存在表，但不存在", "存在表，但不存在");
				if (isSetData) {
					title_tv.setText("");
					note.setText("");
				}

				return false;
			} else {

				while (cursorTemp.moveToNext()) {
					if (isSetData) {
						String title = cursorTemp.getString(cursorTemp
								.getColumnIndex("title"));

						String noteContent = cursorTemp.getString(cursorTemp
								.getColumnIndex("noteContent"));
						title_tv.setText(title);
						note.setText(noteContent);
					}

				}

				return true;
			}
		}

	}

	public void updateNote() {
		ContentValues values = new ContentValues();
		DatabaseHelper dbHelper = new DatabaseHelper(getApplicationContext(),
				"bible.db");
		SQLiteDatabase db = dbHelper.getWritableDatabase();

		values.put("noteContent", note.getText().toString());
		db.update("note", values, "chapterIndexInBook=?",
				new String[] { chapterIndexInBook + "" });
	}

	public void store() {
		if (isDataExits(false)) {
			Log.i("updateNote", "updateNote");
			updateNote();
		} else {
			Log.i("storeNote", "storeNote");
			storeNote();
		}
	}

	class getBookMarkAsynTask extends AsyncTask<Void, Void, Void> {
		@Override
		protected Void doInBackground(Void... params) {
			getBookMarkData();
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
		}
	}

	public void bookmark(View view) {
		openOptionsMenu();
	}

	public void upBookMark(String bookMarkName) {
		ContentValues values = new ContentValues();
		DatabaseHelper dbHelper = new DatabaseHelper(getApplicationContext(),
				"bible.db");
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		values.put("chapterIndexInBook", chapterIndexInBook);
		values.put("volume", bibleData.get(1).get("bookName"));
		values.put("chapter", bibleData.get(1).get("chapterIndex"));
		values.put("date", getCurrentDate());

		db.update("bookmark", values, "name=?", new String[] { bookMarkName });
		db.close();

		Log.i("存入", "存入" + bookMarkName);
	}

	public void storeNewBookmark(String bookMarkName) {

		ContentValues values = new ContentValues();
		DatabaseHelper dbHelper = new DatabaseHelper(getApplicationContext(),
				"bible.db");
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		db.execSQL("create table  if not exists  note(title varchar(30),chapterIndexInBook int,volume varchar(20),chapter varchar(20),noteContent text,date varchar(20) )");
		values.put("chapterIndexInBook", chapterIndexInBook);
		values.put("volume", volume);

		values.put("name", bookMarkName);
		values.put("chapterIndexInBook", chapterIndexInBook);
		values.put("volume", volume);
		values.put("chapter", chapter);
		values.put("date", getCurrentDate());

		db.insert("bookmark", null, values);
		db.close();
	}

	public void getBookMarkData() {
		dbHelper = new DatabaseHelper(getApplicationContext(), "bible.db");
		db = dbHelper.getReadableDatabase();
		Cursor cursorTemp = db.rawQuery("select name from bookmark", null);
		while (cursorTemp.moveToNext()) {
			String name = cursorTemp.getString(cursorTemp
					.getColumnIndex("name"));
			bookMarkList.add(name);
		}
		bookMarkList.add("新建书签");
		db.close();
	}

	public void createNewBookMark() {
		WindowManager manager = getWindowManager();
		Display display = manager.getDefaultDisplay();
		int width = display.getWidth();
		int height = display.getHeight();

		AlertDialog.Builder builder = new AlertDialog.Builder(
				Activity_ReadBible2.this, R.style.Dialog1);
		builder.setInverseBackgroundForced(true);
		LayoutInflater inflater = LayoutInflater.from(getApplicationContext());
		View view1 = inflater.inflate(R.layout.dialog_newbookmark, null);
		Button cancel = (Button) view1.findViewById(R.id.cancel);
		Button certain = (Button) view1.findViewById(R.id.certain);
		final EditText newbookmark_et = (EditText) view1
				.findViewById(R.id.newbookmark_et);

		cancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				fluentAlert.cancel();
				finish();
			}
		});
		certain.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				fluentAlert.cancel();
				finish();
				new Thread(new Runnable() {

					@Override
					public void run() {
						storeNewBookmark(newbookmark_et.getText().toString());
					}
				}).start();

			}
		});

		fluentAlert = builder.create();

		fluentAlert.getWindow().clearFlags(
				WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
		fluentAlert.show();
		fluentAlert.setContentView(view1);
		fluentAlert.getWindow().setLayout(7 * width / 8, 500);
		fluentAlert.setTitle("测试");

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		for (int i = 0; i < bookMarkList.size(); i++) {
			menu.add(0, Menu.FIRST + i, 0, bookMarkList.get(i));
		}
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		for (int i = 0; i < bookMarkList.size(); i++) {
			if (item.getItemId() == Menu.FIRST + i) {
				if (i == bookMarkList.size() - 1) {
					createNewBookMark();
				} else {
					upBookMark(bookMarkList.get(i));
					Toast.makeText(getApplicationContext(), "已添加书签！",
							Toast.LENGTH_SHORT).show();
					Intent main = new Intent(getApplicationContext(), MainActivity.class);
					startActivity(main); // 启动新的Activity（OneActivity）
					finish();
					overridePendingTransition(R.anim.zoomin, R.anim.zoomout);
				}

			}
		}
		return true;
	}

	public void home(View view) {
		Intent main = new Intent(getApplicationContext(), MainActivity.class);
		startActivity(main); // 启动新的Activity（OneActivity）
		finish();
		overridePendingTransition(R.anim.zoomin, R.anim.zoomout);
	}

	public void search(View view) {
		Intent main = new Intent(getApplicationContext(), Activity_search.class);
		main.putExtra("from", "Activity_ReadBible2");
		startActivity(main); // 启动新的Activity（OneActivity）
		overridePendingTransition(R.anim.zoomin, R.anim.zoomout);
	}

}
