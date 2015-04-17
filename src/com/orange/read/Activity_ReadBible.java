package com.orange.read;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import www.orange.utils.UMShare;

import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.example.acts.DatabaseHelper;
import com.example.acts.R;
import com.orange.recite.Activity_ReciteMain;
import com.umeng.socialize.media.QQShareContent;
import com.umeng.socialize.media.QZoneShareContent;


@SuppressLint("NewApi")
public class Activity_ReadBible extends Fragment implements OnClickListener {

	
	// 设置分享内容
	private int isClick=1;
	SharedPreferences sp1;
	private TextView tv;
	private int[] relPicIds = new int[] { R.color.pic1, R.color.pic2, R.color.pic3, R.color.pic4, R.color.pic5, R.color.pic6, R.color.pic7, R.color.pic8,
			R.color.pic9, R.color.pic10, R.color.pic11, R.color.pic12 };

	private int isExitLight = 0;
	private int isExitFontSize = 0;
	private int isLongClick = 0;
	private int position;
	private String volume;
	private String simpleVolume;
	private String chapter;

	private String data;

	private List<Map<String, Integer>> list = new ArrayList<Map<String, Integer>>();
	private List<Map<String, String>> listReadBible = new ArrayList<Map<String, String>>();
	private List<Integer> positionStart;
	private List<Integer> positionEnd;
	private List<Integer> ids = new ArrayList<Integer>();
	private List<TextView> tvs = new ArrayList<TextView>();

	private List<Integer> shareContentIndex = new ArrayList<Integer>();
	private List<Integer> indexpositionStart = new ArrayList<Integer>();
	private List<Integer> indexpositionEnd = new ArrayList<Integer>();
	private List<Integer> highLightStart = new ArrayList<Integer>();
	private List<Integer> highLightEnd = new ArrayList<Integer>();
	private List<Integer> titlepositionStart = new ArrayList<Integer>();
	private List<Integer> titlepositionEnd = new ArrayList<Integer>();
	private LinearLayout sharelayout;
	private TranslateAnimation animation;
	private String Path;
	private File file;
	private TranslateAnimation animationout;
	private LinearLayout fontsize;
	private RelativeLayout rl;
	private LinearLayout ground;
	private TranslateAnimation animationin;
	private LinearLayout store;
	private TextView title_bar_name;
	private View view;
	private ViewFlipper contentViewFlipper;
	private int chapterIndexInBook;
	private SharedPreferences sp;
	private SpannableString spannableString1;

	private List<View> contentView = new ArrayList<View>();
	private int CurrentPageIndex = 0;// 当前页面下标
	private int nextDB = 5;// 需加载的下一条数据库数据位置
	private int preDB = 5;// 需加载的上一条数据库数据位置
	private int maxDB = 10;// 数据库数据最大条数
	private ImageView toLeft;
	private ImageView toRight;
	private DatabaseHelper dbHelper;
	private SQLiteDatabase db;
	private TextView title_bar_book;
	private TextView title_bar_chapter;
	private int bookIndex;
	private ImageView copy_iv;
	private int id;
	private ImageView highlight;
	private int firstFlag;

	private ImageView share_iv;
	private ImageView font_iv;
	private ImageView subfontsize;
	private ImageView addfontsize;
	private SeekBar seekbar;
	private ImageView light_iv;
	private LinearLayout light;
	private RelativeLayout titleBar;
	private LinearLayout ll;
	private TextView hint_tv;
	private LinearLayout search_ll;
	private String keyword;
	private ImageView recite_iv;
	private UMShare umShare;

	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		
		umShare=new UMShare(getActivity());
		view = inflater.inflate(R.layout.activity_readbible, null);

		hint_tv=(TextView) view.findViewById(R.id.hint_tv);
		title_bar_book = (TextView) view.findViewById(R.id.title_bar_book);

		title_bar_chapter = (TextView) view.findViewById(R.id.title_bar_chapter);
		
		seekbar=(SeekBar)view.findViewById(R.id.seekbar);
		seekbar.setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				
				return false;
			}
		});
		seekbar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

			@Override
			public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub
				
			}
			
			
		});
		recite_iv=(ImageView) view.findViewById(R.id.recite_iv);
		recite_iv.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				add2sqls();
				Toast.makeText(getActivity(), "已添加到背诵经文页面！", Toast.LENGTH_SHORT).show();
				if (isLongClick == 1) {
					isLongClick = 0;
					animationout = new TranslateAnimation(0, 0, 0, 250);
					animationout.setDuration(500);
					animationout.setFillAfter(true);
					store.setAnimation(animationout);
					store.setVisibility(View.INVISIBLE);
				}
				
			}
		});
		copy_iv = (ImageView) view.findViewById(R.id.copy_iv);

		copy_iv.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				String result = getShareContent();
				Log.i("wechat", "result：" + result);

				if (result.equals("")) {
					Toast.makeText(getActivity(), "请选择要复制的经文", Toast.LENGTH_SHORT).show();
					return;
				}
				if (isLongClick == 1) {
					isLongClick = 0;
					animationout = new TranslateAnimation(0, 0, 0, 250);
					animationout.setDuration(500);
					animationout.setFillAfter(true);
					store.setAnimation(animationout);
					store.setVisibility(View.VISIBLE);
				}
				sharelayout.setAnimation(animationout);
				sharelayout.setVisibility(View.INVISIBLE);
				ClipboardManager cmb = (ClipboardManager) getActivity().getSystemService(getActivity().CLIPBOARD_SERVICE);
				ClipData clip = ClipData.newPlainText("text",result);
				cmb.setPrimaryClip(clip);
				Toast.makeText(getActivity(), "已复制", Toast.LENGTH_SHORT).show();

			}
		});

		highlight = (ImageView) view.findViewById(R.id.highlight);
		highlight.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				setHighLight();

			}
		});

		share_iv = (ImageView) view.findViewById(R.id.share_iv);
		share_iv.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				String shareString=getShareContent();
				QQShareContent qqShareContent = new QQShareContent();
				qqShareContent.setShareContent(shareString);
				qqShareContent.setTargetUrl("http://weibo.com/775747758");
				
				QZoneShareContent qzone = new QZoneShareContent();
				qzone.setShareContent(shareString);
				qzone.setTargetUrl("http://weibo.com/775747758");
	
				umShare.getController().setShareMedia(qqShareContent);
				umShare.getController().setShareContent(shareString);
				//umShare.getController().setAppWebSite("http://weibo.com/775747758");
				umShare.getController().openShare(getActivity(), false);

			}
		});
		
		font_iv=(ImageView) view.findViewById(R.id.font_iv);
		font_iv.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if(isExitLight==1)
				{
					//animationout = new TranslateAnimation(Animation.RELATIVE_TO_SELF,0,Animation.RELATIVE_TO_SELF, 0,Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF,1);
					animationout = new TranslateAnimation(0, 0, 0, 250);
					animationout.setDuration(500);
					animationout.setFillAfter(false);
					light.setAnimation(animationout);
					isExitLight=0;
				}
				if(isExitFontSize==0)
				{
					animationin = new TranslateAnimation(0, 0, 250, 0);
					animationin.setDuration(500);
					animationin.setFillAfter(true);
					fontsize.setAnimation(animationin);
					isExitFontSize=1;
				}
				else{
					animationout = new TranslateAnimation(0, 0, 0, 250);
					animationout.setDuration(500);
					animationout.setFillAfter(false);
					fontsize.setAnimation(animationout);
					isExitFontSize=0;
				}
				
				
			}
		});
		subfontsize = (ImageView) view.findViewById(R.id.subfontsize);
		subfontsize.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				subfontsize();
				
			}
		});
		addfontsize = (ImageView) view.findViewById(R.id.addfontsize);
		addfontsize.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				addfontsize();
				
			}
		});
		
		light_iv=(ImageView) view.findViewById(R.id.light_iv);
		light_iv.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if(isExitFontSize==1)
				{
					animationout = new TranslateAnimation(0, 0, 0, 250);
					animationout.setDuration(500);
					animationout.setFillAfter(false);
					fontsize.setAnimation(animationout);
					isExitFontSize=0;
				}
				if(isExitLight==0)
				{
					animationin = new TranslateAnimation(0, 0, 250, 0);
					animationin.setDuration(500);
					animationin.setFillAfter(true);
					light.setAnimation(animationin);
					isExitLight=1;
				}
				else{
					animationout = new TranslateAnimation(0, 0, 0, 250);
					animationout.setDuration(500);
					animationout.setFillAfter(false);
					light.setAnimation(animationout);
					isExitLight=0;
				}
				
				
			}
		});
		

		// title_bar_name.setBackground(getResources().getDrawable(R.drawable.selector_titlebar_name));

		rl = (RelativeLayout) view.findViewById(R.id.rl);
		contentViewFlipper = (ViewFlipper) view.findViewById(R.id.contentViewFlipper);
		toLeft = (ImageView) view.findViewById(R.id.toleft);
		toLeft.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// String chapterContent=createContent();
				if (chapterIndexInBook == 1) {
					Toast.makeText(getActivity(), "已经是第一章啦！", Toast.LENGTH_SHORT).show();
				} else {
					next(0);

				}
				RightFragment rightfram = (RightFragment) getFragmentManager().findFragmentByTag("right_frame");
				rightfram.store();
				transDataToRigahtFragment();
				keyword=null;

			}

		});

		toRight = (ImageView) view.findViewById(R.id.toright);
		toRight.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// contentViewFlipper.setInAnimation(getActivity(),R.anim.enter_alpha);
				// contentViewFlipper.setOutAnimation(getActivity(),R.anim.out_alpha);
				if (chapterIndexInBook == 1189) {
					Toast.makeText(getActivity(), "已经是最后一章啦！", Toast.LENGTH_SHORT).show();
				} else {
					next(1);
				}
				RightFragment rightfram = (RightFragment) getFragmentManager().findFragmentByTag("right_frame");
				rightfram.store();
				transDataToRigahtFragment();
				keyword=null;
			}
		});

		Path = Environment.getExternalStorageDirectory().getAbsolutePath();
		file = new File(Path + "/sharetowechat.png");

		store = (LinearLayout) view.findViewById(R.id.store);
		ll=(LinearLayout) view.findViewById(R.id.bottom_ll);
		
		search_ll=(LinearLayout) view.findViewById(R.id.search_ll);
		search_ll.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getActivity(), Activity_search.class);
				startActivity(intent);
				getActivity().overridePendingTransition(R.anim.zoomin, R.anim.zoomout);
				
			}
		});
		light = (LinearLayout) view.findViewById(R.id.light);
		fontsize = (LinearLayout) view.findViewById(R.id.fontsize);
		//sharelayout = (LinearLayout) view.findViewById(R.id.sharelayout);
		titleBar = (RelativeLayout) view.findViewById(R.id.titleBar);

		sp = getActivity().getSharedPreferences("readbible_config", Context.MODE_PRIVATE);

		rl.setBackgroundColor(getResources().getColor(sp.getInt("background_color", relPicIds[0])));

		Intent intent = getActivity().getIntent();
		volume = intent.getStringExtra("volume");
		if(intent.getStringExtra("keyword")!=null)
		{
			keyword=intent.getStringExtra("keyword");
		}
		
		simpleVolume = intent.getStringExtra("simpleVolume");
		chapter = intent.getStringExtra("chapter");
		title_bar_book.setText(volume);
		title_bar_chapter.setText(chapter);
		hint_tv.setText(volume+"  "+chapter);

		//initViewPager();
		new MyAsyncTask().execute();

		title_bar_book.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				RightFragment rightfram = (RightFragment) getFragmentManager().findFragmentByTag("right_frame");
				rightfram.store();
				Log.i("setOnClickListener", "setOnClickListener");
				Intent intent = new Intent(getActivity(), Activity_SelectVolume.class);
				intent.putExtra("activity", "Activity_ReadBible");

				// startActivity(intent);
				startActivityForResult(intent, 1);
				getActivity().finish();
				getActivity().overridePendingTransition(R.anim.top2bottom_out, R.anim.top2bottom_in);
				
			}
		});

		title_bar_chapter.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				RightFragment rightfram1 = (RightFragment) getFragmentManager().findFragmentByTag("right_frame");
				rightfram1.store();
				Intent intent = new Intent(getActivity(), Activity_SelectVolume_Sec.class);
				startActivityForResult(intent, 1);
				getActivity().finish();
				getActivity().overridePendingTransition(R.anim.top2bottom_out, R.anim.top2bottom_in);

			}
		});

		return view;

	}

	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		Log.i("onActivityCreated", "onActivityCreated");

	}

	public String readFromSQL() {
		dbHelper = new DatabaseHelper(getActivity(),"bible.db");
		db = dbHelper.getReadableDatabase();
		String reult = "";
		positionStart = new ArrayList<Integer>();
		positionEnd = new ArrayList<Integer>();
		int len = 1;
		Cursor cursorTemp = db.rawQuery(
				"select id,firstFlag,bookIndex,sectionText,isTitle,sectionIndex, chapterIndexInBook from holybible where bookName=? AND chapterIndex=? ",
				new String[] { volume.trim(), chapter.trim() });
		int i = 1;
		SpannableString spanText;
		while (cursorTemp.moveToNext()) {
			String temp = null;
			String getDataString = "";
			int isTitle = cursorTemp.getInt(cursorTemp.getColumnIndex("isTitle"));
			int sectionIndex = cursorTemp.getInt(cursorTemp.getColumnIndex("sectionIndex"));
			chapterIndexInBook = cursorTemp.getInt(cursorTemp.getColumnIndex("chapterIndexInBook"));
			id = cursorTemp.getInt(cursorTemp.getColumnIndex("id"));
			// RightFragment rightfram = (RightFragment)
			// getFragmentManager().findFragmentByTag("right_frame");

			bookIndex = cursorTemp.getInt(cursorTemp.getColumnIndex("bookIndex"));
			firstFlag = cursorTemp.getInt(cursorTemp.getColumnIndex("firstFlag"));
			CurrentPageIndex = chapterIndexInBook - 1;
			getDataString = cursorTemp.getString(cursorTemp.getColumnIndex("sectionText"));
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
			if (isTitle == 0) {
				Log.i("是title", i + "");
				if (sectionIndex >= 2) {
					indexpositionStart.add(Integer.parseInt(len + ""));
					indexpositionEnd.add(Integer.parseInt((len + (sectionIndex + "").length()) + ""));
				}
				if (firstFlag == 1) {
					positionStart.add(Integer.parseInt(len + ""));
					highLightStart.add(Integer.parseInt(len + ""));
					len = len + temp.length();
					highLightEnd.add(Integer.parseInt(len + ""));
					positionEnd.add(Integer.parseInt(len + ""));
					shareContentIndex.add(sectionIndex);
					ids.add(id);
				} else {
					positionStart.add(Integer.parseInt(len + ""));
					len = len + temp.length();
					positionEnd.add(Integer.parseInt(len + ""));
					shareContentIndex.add(sectionIndex);
					ids.add(id);
				}

			} else {
				titlepositionStart.add(Integer.parseInt(len + ""));
				len = len + temp.length();
				titlepositionEnd.add(Integer.parseInt(len + ""));

			}

			i++;
		}
		cursorTemp.close();
		data = reult;
		Log.i("data", reult);
		return reult;
	}

	public int findChapter(int position) {
		int reult = 0;
		for (int i = 0; i < positionStart.size(); i++) {
			if (i != positionStart.size() - 1) {

				if (positionStart.get(i) < position && positionEnd.get(i) > position) {

					reult = i;
					break;
				}
			} else {
				Log.i("最后", "进入");
				reult = positionStart.size() - 1;
				break;
			}

		}
		return reult;

	}

	public int createARand(int begin, int end) {
		Random rnd = new Random();
		int number = begin + rnd.nextInt(end - begin + 1);
		return number;
	}

	// 得到分享内容
	public String getShareContent() {

		String result = "";
		for (int i = 0; i < list.size(); i++) {
			if (i == list.size() - 1) {
				result = result + data.substring(list.get(i).get("start"), list.get(i).get("end"));
			} else {
				result = result + data.substring(list.get(i).get("start"), list.get(i).get("end")) + "...";
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
			return result + "]\n#圣经流利说#";
		}

	}

	public void subfontsize() {
		SharedPreferences sp = getActivity().getSharedPreferences("readbible_config", Context.MODE_PRIVATE);
		float fontSize = sp.getFloat("fontsize", 24);
		Log.i("fontsize", "OLDfontsize:" + fontSize);
		fontSize = fontSize - 1;
		for(int i=0;i<tvs.size();i++)
		{
			tvs.get(i).setTextSize(fontSize);
		}
		//tv.setTextSize(fontSize);
		Log.i("fontsize", "NEWfontsize:" + fontSize);
		Editor editor = sp.edit();
		editor.putFloat("fontsize", fontSize);
		editor.commit();

	}

	public void addfontsize() {
		SharedPreferences sp = getActivity().getSharedPreferences("readbible_config", Context.MODE_PRIVATE);
		float fontSize = sp.getFloat("fontsize", 24);
		Log.i("fontsize", "OLDfontsize:" + fontSize);
		fontSize = fontSize + 1;
		Log.i("fontsize", "NEWfontsize:" + fontSize);
		for(int i=0;i<tvs.size();i++)
		{
			tvs.get(i).setTextSize(fontSize);
		}
		//tv.setTextSize(fontSize);

		Editor editor = sp.edit();
		editor.putFloat("fontsize", fontSize);
		editor.commit();
	}
	public void add2sqls() {
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				
				ContentValues values = new ContentValues();
				DatabaseHelper dbHelper = new DatabaseHelper(getActivity(),"bible.db");
				SQLiteDatabase db = dbHelper.getWritableDatabase();

				for (int i = 0; i < list.size(); i++) {
					int id1 = list.get(i).get("id");
					Log.i("updateHighLight进入", "updateHighLight进入");
					values.put("secontFlag", 1);
					db.update("holybible", values, "id=?", new String[] { id1 + "" });
				}
			}
		}).start();
		

	}

	


	public void back(View view) {

		Intent intent = new Intent(getActivity(), Activity_SelectVolume_Sec.class);
		startActivity(intent);
		getActivity().finish();
		getActivity().overridePendingTransition(R.anim.zoomin, R.anim.zoomout);
	}

	public void recite(View view) {

		Intent intent = new Intent(getActivity(), Activity_ReciteMain.class);
		startActivity(intent);
		getActivity().finish();
		getActivity().overridePendingTransition(R.anim.zoomin, R.anim.zoomout);
	}

	@SuppressLint("InlinedApi")
	private void next(int isNext) {
		contentViewFlipper.removeAllViews();
		int viewCount = contentViewFlipper.getChildCount();
		if (viewCount == 2) {
			contentViewFlipper.removeViewAt(1);
		}
		// 下一个view
		if (isNext == 1) {
			chapterIndexInBook = chapterIndexInBook + 1;
			contentViewFlipper.setInAnimation(AnimationUtils.loadAnimation(getActivity(), R.anim.left_to_right_in));
			contentViewFlipper.setOutAnimation(AnimationUtils.loadAnimation(getActivity(), R.anim.left_to_right_out));
		} else {
			chapterIndexInBook = chapterIndexInBook - 1;
			contentViewFlipper.setInAnimation(AnimationUtils.loadAnimation(getActivity(), R.anim.right_to_left_in));
			contentViewFlipper.setOutAnimation(AnimationUtils.loadAnimation(getActivity(), R.anim.right_to_left_out));
		}
		new MyAsyncTask2().execute(chapterIndexInBook+"");
		
		if (0 != viewCount) {

			contentViewFlipper.setDisplayedChild(0);

		}

		title_bar_book.setText(volume);
		title_bar_chapter.setText(chapter);
		hint_tv.setText(volume+"  "+chapter);

	}

	private void initViewPager(String data) {
		spannableString1 = new SpannableString(data);
		setSpan(spannableString1);
		tv = new TextView(getActivity());
		tv.setText(spannableString1);
		tv.setTextSize(sp.getFloat("fontsize", 20));
		tv.setLineSpacing(12.0f, 1.0f);
		tv.setPadding(30, 0, 30, 100);
		tv.setTextColor(Color.BLACK);
		contentViewFlipper.addView(tv);
		tv.setOnLongClickListener(new MyOnLongClickListener());
		tv.setOnTouchListener(new MyOnTouchListener());
		tv.setOnClickListener(new MyOnClicListener());
		tvs.add(tv);
		nextDB += 1;
		transDataToRigahtFragment();
	}

	

	@Override
	public void onResume() {
		transDataToRigahtFragment();
		super.onResume();
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		// Log.i("成功", "成功");
		if (resultCode == 1) {
			Log.i("成功", "成功");
		}
		super.onActivityResult(requestCode, resultCode, data);

	}

	private void transDataToRigahtFragment() {
		// uri的前缀
		String prefix = "file:///mnt/sdcard" + "/0000/";
		// uri的后缀
		String postfix = ".htm";
		String uri = "";
		RightFragment rightfram = (RightFragment) getFragmentManager().findFragmentByTag("right_frame");
		if (bookIndex < 10) {
			if (Integer.parseInt(chapter) < 10) {
				uri = "0" + bookIndex + "_0" + chapter;
			} else {
				uri = "0" + bookIndex + "_" + chapter;
			}

		} else {
			uri = bookIndex + "_" + chapter;
		}

		rightfram.setTranslate(prefix + uri + postfix);
		rightfram.setEnglish(chapterIndexInBook);
		rightfram.setVolume(volume + ":" + chapter);
		rightfram.setChapterIndexInBook(chapterIndexInBook);

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub

	}

	public SpannableString showUnderLine() {
		int start;
		int end;
		int index;
		int idtemp;
		Map<String, Integer> map = new HashMap<String, Integer>();
		if (findChapter(position) != -1) {
			start = positionStart.get(findChapter(position)) - 1;
			end = positionEnd.get(findChapter(position)) - 1;
			index = shareContentIndex.get(findChapter(position));
			idtemp = ids.get(findChapter(position));
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

		SpannableString spannableString = new SpannableString(data);
		for (int i = 0; i < list.size(); i++) {
			spannableString.setSpan(new UnderlineSpan(), list.get(i).get("start"), list.get(i).get("end"), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

		}
		setSpan(spannableString);
		/*
		 * for (int i = 0; i < titlepositionStart.size(); i++) {
		 * spannableString.setSpan(new StyleSpan(Typeface.BOLD),
		 * titlepositionStart.get(i) - 1, titlepositionEnd.get(i) - 1,
		 * Spanned.SPAN_EXCLUSIVE_EXCLUSIVE); spannableString.setSpan(new
		 * AbsoluteSizeSpan(40), titlepositionStart.get(i) - 1,
		 * titlepositionEnd.get(i) - 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		 * 
		 * } for (int i = 0; i < indexpositionStart.size(); i++) {
		 * spannableString.setSpan(new AbsoluteSizeSpan(30),
		 * indexpositionStart.get(i) - 1, indexpositionEnd.get(i) - 1,
		 * Spanned.SPAN_EXCLUSIVE_EXCLUSIVE); }
		 */
		return spannableString;
		// spannableString.setSpan(new AbsoluteSizeSpan(100), 8, 9,
		// Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
	}

	public void setHighLight() {

		SpannableString spannableString = new SpannableString(data);
		for (int i = 0; i < list.size(); i++) {
			highLightStart.add(list.get(i).get("start"));
			highLightEnd.add(list.get(i).get("end"));

		}
		setSpan(spannableString);
		tv.setText(spannableString);
		updateHighLight();
	}

	private void setSpan(SpannableString spannableString) {
		for (int i = 0; i < highLightStart.size(); i++) {
			spannableString.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.highlight)), highLightStart.get(i) - 1, highLightEnd.get(i) - 1,
					Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		}
		for (int i = 0; i < titlepositionStart.size(); i++) {
			spannableString.setSpan(new StyleSpan(Typeface.BOLD), titlepositionStart.get(i) - 1, titlepositionEnd.get(i) - 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
			spannableString.setSpan(new AbsoluteSizeSpan(40), titlepositionStart.get(i) - 1, titlepositionEnd.get(i) - 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

		}
		for (int i = 0; i < indexpositionStart.size(); i++) {
			spannableString.setSpan(new AbsoluteSizeSpan(30), indexpositionStart.get(i) - 1, indexpositionEnd.get(i) - 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		}
		int start=0;
		if(keyword!=null)
		{
			while(true)
			{
				if(data.indexOf(keyword,start)!=data.lastIndexOf(keyword))
				{
					spannableString.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.keyword)), data.indexOf(keyword,start), data.indexOf(keyword,start)+keyword.length(),// 2+resultString.indexOf("示")+1
							Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
						start= data.indexOf(keyword,start)+1;
				}
				else
				{
					break;
				}
			}
		}
		
		//result.setText(spannableString);
	}

	public void updateHighLight() {
		Log.i("updateHighLight", "updateHighLight");
		ContentValues values = new ContentValues();
		DatabaseHelper dbHelper = new DatabaseHelper(getActivity(),"bible.db");
		SQLiteDatabase db = dbHelper.getWritableDatabase();

		for (int i = 0; i < list.size(); i++) {
			int id1 = list.get(i).get("id");
			Log.i("updateHighLight进入", "updateHighLight进入");
			values.put("firstFlag", 1);
			db.update("holybible", values, "id=?", new String[] { id1 + "" });
		}

	}

	
	
	public void exitLongclick()
	{
		if (isLongClick == 1) {
			tvs.get(chapterIndexInBook-1).setText(showUnderLine());
			isLongClick = 0;
			animationout = new TranslateAnimation(0, 0, 0, 250);
			animationout.setDuration(200);
			animationout.setFillAfter(true);
			store.setAnimation(animationout);
			store.setVisibility(View.INVISIBLE);
			
		}
	}
	
	class MyOnClicListener implements OnClickListener
	{

		@Override
		public void onClick(View v) {
			TextView curTv=(TextView)v;
			//v.getParent().requestDisallowInterceptTouchEvent(true);
			if (isLongClick == 1) {
				curTv.setText(showUnderLine());
				//tv.setText(showUnderLine());
			}
			else
			{
				//Toast.makeText(getActivity(), "isClick"+isClick, Toast.LENGTH_SHORT).show();
				if(isClick==1)
				{
					if(isExitLight==1)
					{
						animationout = new TranslateAnimation(0, 0, 0, 250);
						animationout.setDuration(200);
						animationout.setFillAfter(false);
						light.setAnimation(animationout);
						isExitLight=0;
					}
					if(isExitFontSize==1)
					{
						animationout = new TranslateAnimation(0, 0, 0, 250);
						animationout.setDuration(200);
						animationout.setFillAfter(false);
						fontsize.setAnimation(animationout);
						isExitFontSize=0;
					}
					if (isLongClick == 1) {
						isLongClick = 0;
						animationout = new TranslateAnimation(0, 0, 0, 250);
						animationout.setDuration(200);
						animationout.setFillAfter(true);
						store.setAnimation(animationout);
						store.setVisibility(View.INVISIBLE);
						
					}
					
					animationin = new TranslateAnimation(0, 0, 0, -250);
					animationin.setDuration(500);
					animationin.setFillAfter(true);
					titleBar.setAnimation(animationin);
					titleBar.setVisibility(View.INVISIBLE);
					
					animationin = new TranslateAnimation(0, 0, 0, 250);
					animationin.setDuration(500);
					animationin.setFillAfter(true);
					ll.setAnimation(animationin);
					ll.setVisibility(View.INVISIBLE);
					isClick=0;
				}
				else{
					
					
					animationout = new TranslateAnimation(0, 0, -250, 0);
					animationout.setDuration(500);
					animationout.setFillAfter(true);
					titleBar.setAnimation(animationout);
					titleBar.setVisibility(View.VISIBLE);
					
					animationout = new TranslateAnimation(0, 0, 250, 0);
					animationout.setDuration(500);
					animationout.setFillAfter(true);
					ll.setAnimation(animationout);
					ll.setVisibility(View.VISIBLE);
					isClick=1;
				}
				
			}
			
		
			
		}
		
	}
	
	class MyOnTouchListener implements OnTouchListener{

		@Override
		public boolean onTouch(View v, MotionEvent event) {
			TextView curTv=(TextView)v;
			position = curTv.getOffsetForPosition(event.getX(), event.getY());
			Log.i("touch", "touch" + position);
			
			

			return false;
		
		}
		
	}
	class MyOnLongClickListener implements OnLongClickListener {

		@Override
		public boolean onLongClick(View v) {
			TextView curTv=(TextView)v;
			if(isClick==0)
			{
				animationout = new TranslateAnimation(0, 0, 250, 0);
				animationout.setDuration(500);
				animationout.setFillAfter(true);
				ll.setAnimation(animationout);
				ll.setVisibility(View.VISIBLE);
				isClick=1;
			}
			if (isLongClick == 0) {
				isLongClick = 1;
				animationout = new TranslateAnimation(0, 0, 250, 0);
				animationout.setDuration(500);
				animationout.setFillAfter(true);
				store.setAnimation(animationout);
				store.setVisibility(View.VISIBLE);
				
			}
			curTv.setText(showUnderLine());
			return true;
		}
	}
	
	
	class  MyAsyncTask extends AsyncTask<Void, TextView, String>
	{
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
			return readFromSQL();
		}
		
		
	}
	
	class MyAsyncTask2 extends AsyncTask<String, TextView, String>
	{

		@Override
		protected void onPreExecute() {
			shareContentIndex.clear();
			positionStart.clear();
			positionEnd.clear();
			indexpositionStart.clear();
			indexpositionEnd.clear();
			titlepositionStart.clear();
			titlepositionEnd.clear();
			shareContentIndex.clear();
			super.onPreExecute();
		}
		@Override
		protected String doInBackground(String... params) {

			String reult = "";
			int len = 1;
			Cursor cursorTemp = db.rawQuery(
					"select id,bookIndex,sectionText,isTitle,sectionIndex,bookName,chapterIndex from holybible where chapterIndexInBook=? ", new String[] { params[0]
							+ "" });
			int i = 1;
			while (cursorTemp.moveToNext()) {
				String temp = null;
				String getDataString = "";
				int isTitle = cursorTemp.getInt(cursorTemp.getColumnIndex("isTitle"));
				int sectionIndex = cursorTemp.getInt(cursorTemp.getColumnIndex("sectionIndex"));
				getDataString = cursorTemp.getString(cursorTemp.getColumnIndex("sectionText"));
				volume = cursorTemp.getString(cursorTemp.getColumnIndex("bookName"));
				chapter = cursorTemp.getInt(cursorTemp.getColumnIndex("chapterIndex")) + "";
				bookIndex = cursorTemp.getInt(cursorTemp.getColumnIndex("bookIndex"));
				id = cursorTemp.getInt(cursorTemp.getColumnIndex("id"));
				if (sectionIndex > 1) {
					getDataString = sectionIndex + " " + getDataString;
				}
				if (isTitle == 1) {
					if (i == 1) {
						temp = getDataString + "\n        ";
					} else {
						temp = "\n" + getDataString + "\n        ";
					}

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
					if (firstFlag == 1) {
						positionStart.add(Integer.parseInt(len + ""));
						highLightStart.add(Integer.parseInt(len + ""));
						len = len + temp.length();
						highLightEnd.add(Integer.parseInt(len + ""));
						positionEnd.add(Integer.parseInt(len + ""));
						shareContentIndex.add(sectionIndex);
						ids.add(id);
					} else {
						positionStart.add(Integer.parseInt(len + ""));
						len = len + temp.length();
						positionEnd.add(Integer.parseInt(len + ""));
						shareContentIndex.add(sectionIndex);
						ids.add(id);
					}
				} else {
					titlepositionStart.add(Integer.parseInt(len + ""));
					len = len + temp.length();
					titlepositionEnd.add(Integer.parseInt(len + ""));

				}
				i++;
			}
			cursorTemp.close();
			data = reult;
			return reult;
		}
		@Override
		protected void onPostExecute(String result) {
			SpannableString spannableString = new SpannableString(result);
			setSpan(spannableString);
			TextView tv1 = new TextView(getActivity());
			// tv.setGravity(Gravity.CENTER);
			tv1.setText(spannableString);
			tv1.setTextSize(sp.getFloat("fontsize", 20));
			tv1.setLineSpacing(12.0f, 1.0f);
			tv1.setPadding(30, 0, 30, 100);
			// tv.setHeight(LayoutParams.MATCH_PARENT);
			// tv.setWidth(LayoutParams.MATCH_PARENT);
			tv1.setTextColor(Color.BLACK);
			// tv.setAnimation(AnimationUtils.loadAnimation(getActivity(),
			// R.anim.left_to_right_in));
			tv1.setClickable(true);
			tv1.setOnLongClickListener(new MyOnLongClickListener());
			tv1.setOnTouchListener(new MyOnTouchListener());
			tv1.setOnClickListener(new MyOnClicListener());
			tvs.add(tv1);
			contentViewFlipper.addView(tv1);
			//return tv1;
			super.onPostExecute(result);
		}
		
	}


}
