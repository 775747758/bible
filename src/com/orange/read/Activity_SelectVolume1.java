package com.orange.read;

/*
 * 此activity是圣经经卷选择界面
 */

import java.text.Collator;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.example.acts.MainActivity;
import com.example.acts.NonScrollGridView;
import com.example.acts.R;
import com.example.acts.R.id;
import com.example.acts.R.layout;
import com.orange.test.Activity_BibleTestMain;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.VpnService;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnCreateContextMenuListener;
import android.view.animation.TranslateAnimation;
import android.view.ViewGroup;
import android.webkit.WebView.FindListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

public class Activity_SelectVolume1 extends Activity{

	LayoutInflater inflater;
	ListView lv;
	private ArrayList<GridView> gvList;
	private int[] contentNums;
	private ViewPager select_viewpager;
	private List<View> vlist;
	private TextView tv_old;
	private TextView tv_new;
	int currIndex = 0;
	private NonScrollGridView gv_old;
	private NonScrollGridView gv_new;
	private String[] title_old;
	private String[] simpleTitle_old;
	private int[] contentNum_old;
	private String[] title_new;
	private String[] simpleTitle_new;
	private int[] contentNum_new;
	private String[][] title;
	private String[][] simpleTitle;
	private int whitch;
	private TextView title_bar_name;
	private ImageView grid_iv;
	private ImageView list_iv;
	private boolean isList = true;
	private ListView bible_lv;
	private TextView oldbible_tv;
	private TextView newbible_tv;
	private TextView chapter_tv;
	private View oldbible_v;
	private View newbible_v;
	private View chapter_v;
	private NonScrollGridView gv_chapter;
	private ListView bible_new_lv;
	private ListView bible_old_lv;
	private SharedPreferences sp;
	private Editor editor;
	private MyPagerAdapter myPagerAdapter;
	private TextView letter_tv;
	private TextView tradition_tv;
	private List<Map<String, String>> oldList;
	private List<Map<String, String>> oldList_backup;
	private List<Map<String, String>> newList_backup;
	private List<Map<String, String>> newList;
	private List<Map<String, String>> tempList;
	private List<Map<String, String>> chapterList;
	private ChapterListAdapter simpleAdapter_list_old;
	private ChapterListAdapter simpleAdapter_list_new;
	private ChapterGrideAdapter simpleAdapter_gride_old;
	private ChapterGrideAdapter simpleAdapter_gride_new;
	private String newListIndex="";
	private String oldListIndex="";
	protected int chapterIndex;
	private int bookIndex;
	private String[] titles;
	private String[] simpleTitles;
	private String from="no";
	private String bookName="";
	private int chapterIndexFrom;
	private String type="";
	private LinearLayout list_ll;
	private LinearLayout grid_ll;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_selectvolume1);

		sp = getSharedPreferences("selectBible", Context.MODE_PRIVATE);
		editor = sp.edit();
		
		Intent intent=getIntent();
		if(intent.getStringExtra("from")!=null)
		{
			type=intent.getStringExtra("type");
			from=intent.getStringExtra("from");
			if(intent.getStringExtra("bookIndex")!=null)
			{
				bookIndex=Integer.parseInt(intent.getStringExtra("bookIndex"));
			}
			
			bookName=intent.getStringExtra("bookName");
		}
		

		select_viewpager = (ViewPager) findViewById(R.id.select_viewpager);
		title_bar_name = (TextView) findViewById(R.id.title_bar_name);
		grid_iv = (ImageView) findViewById(R.id.grid_iv);
		list_iv = (ImageView) findViewById(R.id.list_iv);
		
		
		list_ll=(LinearLayout)findViewById(R.id.list_ll);
		grid_ll=(LinearLayout)findViewById(R.id.grid_ll);
		
		

		oldbible_tv = (TextView) findViewById(R.id.oldbible_tv);
		newbible_tv = (TextView) findViewById(R.id.newbible_tv);
		chapter_tv = (TextView) findViewById(R.id.chapter_tv);
		oldbible_v = findViewById(R.id.oldbible_v);
		newbible_v = findViewById(R.id.newbible_v);
		chapter_v = findViewById(R.id.chapter_v);
		letter_tv=(TextView) findViewById(R.id.letter_tv);
		tradition_tv=(TextView) findViewById(R.id.tradition_tv);

		oldbible_tv.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				select_viewpager.setCurrentItem(0);
				oldbible_v.setVisibility(View.VISIBLE);
				newbible_v.setVisibility(View.INVISIBLE);
				chapter_v.setVisibility(View.INVISIBLE);
				
				oldbible_tv.setTextColor(getResources().getColor(R.color.titleBar));
				newbible_tv.setTextColor(Color.parseColor("#8A7065"));
				chapter_tv.setTextColor(Color.parseColor("#8A7065"));
			}
		});
		newbible_tv.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				select_viewpager.setCurrentItem(1);
				oldbible_v.setVisibility(View.INVISIBLE);
				newbible_v.setVisibility(View.VISIBLE);
				chapter_v.setVisibility(View.INVISIBLE);
				
				oldbible_tv.setTextColor(Color.parseColor("#8A7065"));
				newbible_tv.setTextColor(getResources().getColor(R.color.titleBar));
				chapter_tv.setTextColor(Color.parseColor("#8A7065"));
			}
		});

		chapter_tv.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				select_viewpager.setCurrentItem(2);
				oldbible_v.setVisibility(View.INVISIBLE);
				newbible_v.setVisibility(View.INVISIBLE);
				chapter_v.setVisibility(View.VISIBLE);
				
				oldbible_tv.setTextColor(Color.parseColor("#8A7065"));
				newbible_tv.setTextColor(Color.parseColor("#8A7065"));
				chapter_tv.setTextColor(getResources().getColor(R.color.titleBar));
			}
		});

		final View selectvolume_list_old = LayoutInflater.from(this).inflate(
				R.layout.selectvolume_list_old, null);
		final View selectvolume_list_new = LayoutInflater.from(this).inflate(
				R.layout.selectvolume_list_new, null);
		final View selectvolume_chapter = LayoutInflater.from(this).inflate(
				R.layout.selectvolume_chapter, null);
		final View selectvolume_gride_new = LayoutInflater.from(this).inflate(
				R.layout.selectvolume_gride_new, null);
		final View selectvolume_gride_old = LayoutInflater.from(this).inflate(
				R.layout.selectvolume_gride_old, null);

		bible_new_lv = (ListView) selectvolume_list_new
				.findViewById(R.id.bible_new_lv);
		
		bible_old_lv = (ListView) selectvolume_list_old
				.findViewById(R.id.bible_old_lv);
		gv_chapter = (NonScrollGridView) selectvolume_chapter
				.findViewById(R.id.gv_chapter);
		gv_new = (NonScrollGridView) selectvolume_gride_new
				.findViewById(R.id.gv_new);
		gv_old = (NonScrollGridView) selectvolume_gride_old
				.findViewById(R.id.gv_old);

		vlist = new ArrayList<View>();

		if (sp.getBoolean("isList", true)) {
			grid_iv.setImageResource(R.drawable.gride_default);
			list_iv.setImageResource(R.drawable.list_pressed);
			
			vlist.add(selectvolume_list_old);
			vlist.add(selectvolume_list_new);
			vlist.add(selectvolume_chapter);
		} else {
			grid_iv.setImageResource(R.drawable.gride_pressed);
			list_iv.setImageResource(R.drawable.list_default);
			vlist.add(selectvolume_gride_old);
			vlist.add(selectvolume_gride_new);
			vlist.add(selectvolume_chapter);
		}
		
		myPagerAdapter=new MyPagerAdapter();
		select_viewpager.setAdapter(myPagerAdapter);
		select_viewpager.setOnPageChangeListener(new MyOnPageChangeListener());
		
		
		Log.i("from", from);
		if(from.equals("old"))
		{
			select_viewpager.setCurrentItem(0, true);
			
			oldbible_v.setVisibility(View.VISIBLE);
			newbible_v.setVisibility(View.INVISIBLE);
			chapter_v.setVisibility(View.INVISIBLE);
			
			oldbible_tv.setTextColor(getResources().getColor(R.color.titleBar));
			newbible_tv.setTextColor(Color.parseColor("#8A7065"));
			chapter_tv.setTextColor(Color.parseColor("#8A7065"));
		}
		else if(from.equals("new"))
		{
			select_viewpager.setCurrentItem(1, true);
			
			oldbible_v.setVisibility(View.INVISIBLE);
			newbible_v.setVisibility(View.VISIBLE);
			chapter_v.setVisibility(View.INVISIBLE);
			
			oldbible_tv.setTextColor(Color.parseColor("#8A7065"));
			newbible_tv.setTextColor(getResources().getColor(R.color.titleBar));
			chapter_tv.setTextColor(Color.parseColor("#8A7065"));
		}
		if(type.equals("chapter"))
		{
			select_viewpager.setCurrentItem(2, true);
			
			oldbible_v.setVisibility(View.INVISIBLE);
			newbible_v.setVisibility(View.INVISIBLE);
			chapter_v.setVisibility(View.VISIBLE);
			
			oldbible_tv.setTextColor(Color.parseColor("#8A7065"));
			newbible_tv.setTextColor(Color.parseColor("#8A7065"));
			chapter_tv.setTextColor(getResources().getColor(R.color.titleBar));
		}

		int width = getWindowManager().getDefaultDisplay().getWidth();// 屏幕宽度
		// int height=getWindowManager().getDefaultDisplay().getHeight();//屏幕高度

		// img3.setLayoutParams(ps);

		oldList = new ArrayList<Map<String, String>>();
		newList = new ArrayList<Map<String, String>>();
		chapterList = new ArrayList<Map<String, String>>();
		
		titles = new String[] { "创世记", "出埃及记", "利未记", "民数记", "申命记", "约书亚记", "士师记", "路得记", "撒母耳记上", "撒母耳记下", "列王纪上", "列王纪下", "历代志上", "历代志下", "以斯拉记", "尼希米记",
				"以斯帖记", "约伯记", "诗篇", "箴言", "传道书", "雅歌", "以赛亚书", "耶利米书", "耶利米哀歌", "以西结书", "但以理书", "何西阿书", "约珥书", "阿摩司书", "俄巴底亚书", "约拿书", "弥迦书", "那鸿书", "哈巴谷书",
				"西番雅书", "哈该书", "撒迦利亚书", "玛拉基书", "马太福音", "马可福音", "路加福音", "约翰福音", "使徒行传", "罗马书", "哥林多前书", "哥林多后书", "加拉太书", "以弗所书", "腓立比书", "歌罗西书", "帖撒罗尼迦前书",
				"帖撒罗尼迦后书", "提摩太前书", "提摩太后书", "提多书", "腓利门书", "希伯来书", "雅各书", "彼得前书", "彼得后书", "约翰一书", "约翰二书", "约翰三书", "犹大书", "启示录" };
		simpleTitles = new String[] { "创", "出", "利", "民", "申", "书", "士", "得", "撒上", "撒下", "王上", "王下", "代上", "代下", "拉", "尼", "斯", "伯", "诗", "箴", "传", "歌", "赛",
				"耶", "哀", "结", "但", "何", "珥", "摩", "俄", "拿", "弥", "鸿", "哈", "番", "该", "亚", "玛", "太", "可", "路", "约", "徒", "罗", "林前", "林后", "加", "弗", "腓", "西",
				"帖前", "帖后", "提前", "提后", "多", "门", "来", "雅", "彼前", "彼后", "约一", "约二", "约三", "犹", "启" };

		title_old = new String[] { "创世记", "出埃及记", "利未记", "民数记", "申命记", "约书亚记",
				"士师记", "路得记", "撒母耳记上", "撒母耳记下", "列王纪上", "列王纪下", "历代志上", "历代志下",
				"以斯拉记", "尼希米记", "以斯帖记", "约伯记", "诗篇", "箴言", "传道书", "雅歌", "以赛亚书",
				"耶利米书", "耶利米哀歌", "以西结书", "但以理书", "何西阿书", "约珥书", "阿摩司书",
				"俄巴底亚书", "约拿书", "弥迦书", "那鸿书", "哈巴谷书", "西番雅书", "哈该书", "撒迦利亚书",
				"玛拉基书" };
		//Arrays.sort(title_old);
		//Log.i("title_old", title_old.toString());
		simpleTitle_old = new String[] { "创", "出", "利", "民", "申", "书", "士",
				"得", "撒上", "撒下", "王上", "王下", "代上", "代下", "拉", "尼", "斯", "伯",
				"诗", "箴", "传", "歌", "赛", "耶", "哀", "结", "但", "何", "珥", "摩",
				"俄", "拿", "弥", "鸿", "哈", "番", "该", "亚", "玛" };
		contentNum_old = new int[] { 50, 40, 27, 36, 34, 24, 21, 4, 31, 24, 22,
				25, 29, 36, 10, 13, 10, 42, 150, 31, 12, 8, 66, 52, 5, 48, 12,
				14, 3, 9, 1, 4, 7, 3, 3, 3, 2, 14, 4 };

		title_new = new String[] { "马太福音", "马可福音", "路加福音", "约翰福音", "使徒行传",
				"罗马书", "哥林多前书", "哥林多后书", "加拉太书", "以弗所书", "腓立比书", "歌罗西书",
				"帖撒罗尼迦前书", "帖撒罗尼迦后书", "提摩太前书", "提摩太后书", "提多书", "腓利门书", "希伯来书",
				"雅各书", "彼得前书", "彼得后书", "约翰一书", "约翰二书", "约翰三书", "犹大书", "启示录" };
		simpleTitle_new = new String[] { "太", "可", "路", "约", "徒", "罗", "林前",
				"林后", "加", "弗", "腓", "西", "帖前", "帖后", "提前", "提后", "多", "门",
				"来", "雅", "彼前", "彼后", "约一", "约二", "约三", "犹", "启" };
		contentNum_new = new int[] { 28, 16, 24, 21, 28, 16, 16, 13, 6, 6, 4,
				4, 5, 3, 6, 4, 3, 1, 13, 5, 5, 3, 5, 1, 1, 1, 22 };

		title = new String[][] { title_old, title_new };
		contentNums = new int[] { 50, 40, 27, 36, 34, 24, 21, 4, 31, 24, 22, 25, 29, 36, 10, 13, 10, 42, 150, 31, 12, 8, 66, 52, 5, 48, 12, 14, 3, 9, 1, 4, 7,
				3, 3, 3, 2, 14, 4, 28, 16, 24, 21, 28, 16, 16, 13, 6, 6, 4, 4, 5, 3, 6, 4, 3, 1, 13, 5, 5, 3, 5, 1, 1, 1, 22 };
		simpleTitle = new String[][] { simpleTitle_old, simpleTitle_new };

		for (int j = 0; j < contentNum_new.length; j++) {
			Map<String, String> map = new HashMap<String, String>();
			map.put("title", title_new[j]);
			map.put("simpleTitle", simpleTitle_new[j]);
			map.put("contentNum", contentNum_new[j] + "");
			newList.add(map);
		}

		for (int j = 0; j < contentNum_old.length; j++) {
			Map<String, String> map = new HashMap<String, String>();
			map.put("title", title_old[j]);
			map.put("simpleTitle", simpleTitle_old[j]);
			map.put("contentNum", contentNum_old[j] + "");
			oldList.add(map);
		}
		if (sp.getBoolean("isOldbible", true)) {
			for (int i = 1; i <= contentNum_old[sp.getInt("chapterIndex", 0)]; i++) {
				Map<String, String> map = new HashMap<String, String>();
				map.put("index", i + "");
				chapterList.add(map);
			}
		} else {
			for (int i = 1; i <= contentNum_new[sp.getInt("chapterIndex", 0)]; i++) {
				Map<String, String> map = new HashMap<String, String>();
				map.put("index", i + "");
				chapterList.add(map);
			}
		}
		//备份旧的数据顺序
		//new ArrayList<Map<String, String>>(sortData(oldList));
		oldList_backup=new ArrayList<Map<String, String>>(oldList);
		newList_backup=new ArrayList<Map<String, String>>(newList);

		// NonScrollGridView gridview2 = (NonScrollGridView)
		// findViewById(R.id.gv2);
		if(!sp.getBoolean("isTradition", true))
		{
			
			letter_tv.setTextColor(getResources().getColor(R.color.titleBar));
			tradition_tv.setTextColor(Color.parseColor("#8A7065"));
			
			
			Collections.copy(oldList, sortData(oldList));
			Collections.copy(newList, sortData(newList));
			
			/*bible_old_lv.setAdapter(simpleAdapter_list_old);
			bible_new_lv.setAdapter(simpleAdapter_list_new);
			gv_old.setAdapter(simpleAdapter_gride_old);
			gv_new.setAdapter(simpleAdapter_gride_new);
			myPagerAdapter.notifyDataSetChanged();
			select_viewpager.setAdapter(myPagerAdapter);
			select_viewpager.setCurrentItem(currIndex);*/
		}
		simpleAdapter_list_old = new ChapterListAdapter(oldList, this,"旧");
		bible_old_lv.setAdapter(simpleAdapter_list_old);

		simpleAdapter_list_new = new ChapterListAdapter(newList, this,"新");
		bible_new_lv.setAdapter(simpleAdapter_list_new);

		simpleAdapter_gride_old = new ChapterGrideAdapter(oldList, this, "旧");

		gv_old.setAdapter(simpleAdapter_gride_old);
		gv_old.setSelector(new ColorDrawable(Color.TRANSPARENT));

		simpleAdapter_gride_new =  new ChapterGrideAdapter(newList, this, "新");
		// 去掉gridview的黄色背景
		gv_new.setSelector(new ColorDrawable(Color.TRANSPARENT));

		
		
		if(from.equals("old"))
		{
			if(chapterList.size()!=0)
			{
				chapterList.clear();
			}
			for (int i = 1; i <= contentNums[bookIndex-1]; i++) {
				Map<String, String> map = new HashMap<String, String>();
				map.put("index", i + "");
				chapterList.add(map);
			}
		}
		else if(from.equals("new"))
		{
			if(chapterList.size()!=0)
			{
				chapterList.clear();
			}
			for (int i = 1; i <= contentNums[bookIndex-1]; i++) {
				Map<String, String> map = new HashMap<String, String>();
				map.put("index", i + "");
				chapterList.add(map);
			}
		}
		
		final SimpleAdapter simpleAdapter_chapter = new SimpleAdapter(this,
				chapterList, R.layout.cell_select_temp2,
				new String[] { "index" }, new int[] { R.id.tv });
		gv_chapter.setAdapter(simpleAdapter_chapter);
		gv_new.setSelector(new ColorDrawable(Color.TRANSPARENT));
		gv_new.setAdapter(simpleAdapter_gride_new);
		// gvList = new ArrayList<GridView>();
		// gvList.add(gv_old);
		// gvList.add(gv_new);

		/*
		 * for(int i=0;i<gvList.size();i++) { gvList.get(i).setSelector(new
		 * ColorDrawable(Color.TRANSPARENT));
		 * gvList.get(i).setOnItemClickListener(this); }
		 */

		grid_ll.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (isList) {
					vlist.clear();
					vlist.add(selectvolume_gride_old);
					vlist.add(selectvolume_gride_new);
					vlist.add(selectvolume_chapter);
					myPagerAdapter.notifyDataSetChanged();
					select_viewpager.setAdapter(myPagerAdapter);
					grid_iv.setImageResource(R.drawable.gride_pressed);
					list_iv.setImageResource(R.drawable.list_default);
					editor.putBoolean("isList", false);
					editor.commit();
					isList=false;
					select_viewpager.setCurrentItem(currIndex);
				}
			}
		});

		list_ll.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (!isList) {
					vlist.clear();
					vlist.add(selectvolume_list_old);
					vlist.add(selectvolume_list_new);
					vlist.add(selectvolume_chapter);
					myPagerAdapter.notifyDataSetChanged();
					select_viewpager.setAdapter(myPagerAdapter);
					
					grid_iv.setImageResource(R.drawable.gride_default);
					list_iv.setImageResource(R.drawable.list_pressed);
					editor.putBoolean("isList", true);
					editor.commit();
					isList=true;
					select_viewpager.setCurrentItem(currIndex);
				}
			}
		});
		
		tradition_tv.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				tradition_tv.setTextColor(getResources().getColor(R.color.titleBar));
				letter_tv.setTextColor(Color.parseColor("#8A7065"));
				
				Collections.copy(oldList, oldList_backup);
				Collections.copy(newList, newList_backup);
				
				bible_old_lv.setAdapter(simpleAdapter_list_old);
				bible_new_lv.setAdapter(simpleAdapter_list_new);
				gv_old.setAdapter(simpleAdapter_gride_old);
				gv_new.setAdapter(simpleAdapter_gride_new);
				
				myPagerAdapter.notifyDataSetChanged();
				select_viewpager.setAdapter(myPagerAdapter);
				select_viewpager.invalidate();
				select_viewpager.setCurrentItem(currIndex);
				editor.putBoolean("isTradition", true);
				editor.commit();
			}
		});
		
		letter_tv.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				letter_tv.setTextColor(getResources().getColor(R.color.titleBar));
				tradition_tv.setTextColor(Color.parseColor("#8A7065"));
				
				Collections.copy(oldList, sortData(oldList));
				Collections.copy(newList, sortData(newList));
				
				bible_old_lv.setAdapter(simpleAdapter_list_old);
				bible_new_lv.setAdapter(simpleAdapter_list_new);
				gv_old.setAdapter(simpleAdapter_gride_old);
				gv_new.setAdapter(simpleAdapter_gride_new);
				myPagerAdapter.notifyDataSetChanged();
				select_viewpager.setAdapter(myPagerAdapter);
				select_viewpager.setCurrentItem(currIndex);
				editor.putBoolean("isTradition", false);
				editor.commit();
			}
		});
		
		bible_new_lv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				newListIndex=((TextView)view.findViewById(R.id.chapter_tv)).getText().toString();
				bookIndex=39+position;
				simpleAdapter_list_new.notifyDataSetInvalidated();
				
				select_viewpager.setCurrentItem(2, true);
				
				oldbible_v.setVisibility(View.INVISIBLE);
				newbible_v.setVisibility(View.INVISIBLE);
				chapter_v.setVisibility(View.VISIBLE);
				
				oldbible_tv.setTextColor(Color.parseColor("#8A7065"));
				newbible_tv.setTextColor(Color.parseColor("#8A7065"));
				chapter_tv.setTextColor(getResources().getColor(R.color.titleBar));
				
				chapterList.clear();
				for (int i = 1; i <= contentNum_new[position]; i++) {
					Map<String, String> map = new HashMap<String, String>();
					map.put("index", i + "");
					chapterList.add(map);
				}
				gv_chapter.setAdapter(simpleAdapter_chapter);
			}
		});
		
		bible_old_lv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				oldListIndex=((TextView)view.findViewById(R.id.chapter_tv)).getText().toString();
				bookIndex=position;
				simpleAdapter_list_old.notifyDataSetInvalidated();
				select_viewpager.setCurrentItem(2, true);
				
				oldbible_v.setVisibility(View.INVISIBLE);
				newbible_v.setVisibility(View.INVISIBLE);
				chapter_v.setVisibility(View.VISIBLE);
				
				oldbible_tv.setTextColor(Color.parseColor("#8A7065"));
				newbible_tv.setTextColor(Color.parseColor("#8A7065"));
				chapter_tv.setTextColor(getResources().getColor(R.color.titleBar));
				
				chapterList.clear();
				for (int i = 1; i <= contentNum_old[position]; i++) {
					Map<String, String> map = new HashMap<String, String>();
					map.put("index", i + "");
					chapterList.add(map);
				}
				gv_chapter.setAdapter(simpleAdapter_chapter);
			}
		});
		gv_old.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				
				oldListIndex=((TextView)view.findViewById(R.id.tv1)).getText().toString();
				bookIndex=position;
				simpleAdapter_gride_old.notifyDataSetInvalidated();
				select_viewpager.setCurrentItem(2, true);
				
				oldbible_v.setVisibility(View.INVISIBLE);
				newbible_v.setVisibility(View.INVISIBLE);
				chapter_v.setVisibility(View.VISIBLE);
				
				oldbible_tv.setTextColor(Color.parseColor("#8A7065"));
				newbible_tv.setTextColor(Color.parseColor("#8A7065"));
				chapter_tv.setTextColor(getResources().getColor(R.color.titleBar));
				
				chapterList.clear();
				for (int i = 1; i <= contentNum_old[position]; i++) {
					Map<String, String> map = new HashMap<String, String>();
					map.put("index", i + "");
					chapterList.add(map);
				}
				gv_chapter.setAdapter(simpleAdapter_chapter);
			}
		});
		
		gv_new.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				newListIndex=((TextView)view.findViewById(R.id.tv1)).getText().toString();
				bookIndex=39+position;
				simpleAdapter_gride_new.notifyDataSetInvalidated();
				select_viewpager.setCurrentItem(2, true);
				
				oldbible_v.setVisibility(View.INVISIBLE);
				newbible_v.setVisibility(View.INVISIBLE);
				chapter_v.setVisibility(View.VISIBLE);
				
				oldbible_tv.setTextColor(Color.parseColor("#8A7065"));
				newbible_tv.setTextColor(Color.parseColor("#8A7065"));
				chapter_tv.setTextColor(getResources().getColor(R.color.titleBar));
				
				chapterList.clear();
				for (int i = 1; i <= contentNum_new[position]; i++) {
					Map<String, String> map = new HashMap<String, String>();
					map.put("index", i + "");
					chapterList.add(map);
				}
				gv_chapter.setAdapter(simpleAdapter_chapter);
			}
		});
		
		gv_chapter.setOnItemClickListener(new OnItemClickListener() {

			

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				chapterIndex=position;
				editor.putInt("chapterIndex", chapterIndex);
				/*Intent selectvolumeIntent = getIntent();
				setResult(1, selectvolumeIntent);*/
		// ActivityReadMain
				Intent intent = new Intent(Activity_SelectVolume1.this, Activity_ReadBible2.class);
				/*if (isChange) {
					
					
					intent.putExtra("volume", titles[currentBookIndex]);
					intent.putExtra("simpleVolume", simpleTitle[currentBookIndex]);
				} else {
					intent.putExtra("volume", title);
					intent.putExtra("simpleVolume", simpleVolume);
				}*/
				intent.putExtra("volume", titles[bookIndex]);
				intent.putExtra("simpleVolume", simpleTitles[bookIndex]);
				intent.putExtra("ChapterIndexInBook",calcChapterIndexInBook(bookIndex,position+1));
				intent.putExtra("chapter", position + 1 + "");
				startActivity(intent);
				overridePendingTransition(R.anim.bottom2top_in, R.anim.bottom2top_out);
			}
		});
		
		
	}

	class MyPagerAdapter extends PagerAdapter {

		@Override
		public Object instantiateItem(View container, int position) {
			((ViewPager) container).addView(vlist.get(position),0);
			return vlist.get(position);
		}

		@Override
		public void destroyItem(View container, int position, Object object) {
			((ViewPager) container).removeView(vlist.get(position));
			// super.destroyItem(container, position, object);

		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return vlist.size();
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
			// TODO Auto-generated method stub

		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
			// TODO Auto-generated method stub

		}

		@SuppressLint("NewApi")
		@Override
		public void onPageSelected(int arg0) {
			switch (arg0) {
			case 0:
				oldbible_v.setVisibility(View.VISIBLE);
				newbible_v.setVisibility(View.INVISIBLE);
				chapter_v.setVisibility(View.INVISIBLE);
				
				oldbible_tv.setTextColor(getResources().getColor(R.color.titleBar));
				newbible_tv.setTextColor(Color.parseColor("#8A7065"));
				chapter_tv.setTextColor(Color.parseColor("#8A7065"));
				/*
				 * if (currIndex == 1) {
				 * 
				 * tv_old.setBackground(getResources().getDrawable(R.drawable.
				 * left_select_rounded_layout));
				 * tv_new.setBackground(getResources
				 * ().getDrawable(R.drawable.right_rounded_layout)); //
				 * tv_old.setTextColor
				 * (getResources().getColor(R.color.select_volume_tab_select_font
				 * )); // tv_new.setTextColor(getResources().getColor(R.color.
				 * select_volume_tab_defalt_font)); }
				 */
				
				break;
			case 1:
				oldbible_v.setVisibility(View.INVISIBLE);
				newbible_v.setVisibility(View.VISIBLE);
				chapter_v.setVisibility(View.INVISIBLE);
				
				oldbible_tv.setTextColor(Color.parseColor("#8A7065"));
				newbible_tv.setTextColor(getResources().getColor(R.color.titleBar));
				chapter_tv.setTextColor(Color.parseColor("#8A7065"));
				/*
				 * if (currIndex == 0) {
				 * tv_old.setBackground(getResources().getDrawable
				 * (R.drawable.left_rounded_layout));
				 * tv_new.setBackground(getResources
				 * ().getDrawable(R.drawable.right_select_rounded_layout)); //
				 * tv_old.setTextColor(getResources().getColor(R.color.
				 * select_volume_tab_defalt_font)); //
				 * tv_new.setTextColor(getResources
				 * ().getColor(R.color.select_volume_tab_select_font));
				 * 
				 * }
				 */
				
				break;
			case 2:
				oldbible_v.setVisibility(View.INVISIBLE);
				newbible_v.setVisibility(View.INVISIBLE);
				chapter_v.setVisibility(View.VISIBLE);
				
				oldbible_tv.setTextColor(Color.parseColor("#8A7065"));
				newbible_tv.setTextColor(Color.parseColor("#8A7065"));
				chapter_tv.setTextColor(getResources().getColor(R.color.titleBar));

			}
			currIndex = arg0;
		}

	}

	

	/*//
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == 1) {
			Intent readbibleIntent = getIntent();
			setResult(1, readbibleIntent);
		}
	}*/
	
	private static List<Map<String, String>> sortData(List<Map<String, String>> list) {
		System.out.println("对WCM数据进行排序");
		if (list.size()>1) {
			Comparator<Map<String, String>> mapComprator = new Comparator<Map<String, String>>() {
				@Override
				public int compare(Map<String, String> o1,
						Map<String, String> o2) {
					Comparator comp= Collator.getInstance(java.util.Locale.CHINESE);
					String[] temp={o1.get("title").toString(),o2.get("title").toString()};
					Arrays.sort(temp,comp);
					// do compare.
					if (!o1.get("title").toString().equals(temp[0])) {
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
	
	public class ChapterListAdapter extends BaseAdapter
	{

		List<String> result=new ArrayList<String>();
		Context context;
		List<Map<String, String>> data=null;
		String Bible="";
		public ChapterListAdapter(List<Map<String, String>> data,Context context,String Bible) {
			this.data = data;
			this.context=context;
			this.Bible=Bible;
		}
		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return data.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return data.get(position);
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(final int position, View convertView, ViewGroup parent) {
			convertView = LinearLayout.inflate(context, R.layout.listitem_selectvolume, null);
			TextView chapter_tv=(TextView)convertView.findViewById(R.id.chapter_tv);
			TextView count_tv=(TextView)convertView.findViewById(R.id.count_tv);
			chapter_tv.setText(data.get(position).get("title"));
			count_tv.setText(data.get(position).get("contentNum"));
			
			if(Bible.equals("旧")&&oldListIndex.equals(data.get(position).get("title")))
			{
				chapter_tv.setTextColor(Color.parseColor("#009688"));
				count_tv.setTextColor(Color.parseColor("#009688"));
			}
			if(Bible.equals("新")&&newListIndex.equals(data.get(position).get("title")))
			{
				chapter_tv.setTextColor(Color.parseColor("#009688"));
				count_tv.setTextColor(Color.parseColor("#009688"));
			}
			if(from.equals("old")&&bookName.equals(data.get(position).get("title")))
			{
				chapter_tv.setTextColor(Color.parseColor("#009688"));
				count_tv.setTextColor(Color.parseColor("#009688"));
			}
			if(from.equals("new")&&bookName.equals(data.get(position).get("title")))
			{
				chapter_tv.setTextColor(Color.parseColor("#009688"));
				count_tv.setTextColor(Color.parseColor("#009688"));
			}
			return convertView;
		}
		
		
		
	}
	
	public class ChapterGrideAdapter extends BaseAdapter
	{

		List<String> result=new ArrayList<String>();
		Context context;
		List<Map<String, String>> data=null;
		String Bible="";
		public ChapterGrideAdapter(List<Map<String, String>> data,Context context,String Bible) {
			this.data = data;
			this.context=context;
			this.Bible=Bible;
		}
		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return data.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return data.get(position);
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(final int position, View convertView, ViewGroup parent) {
			convertView = LinearLayout.inflate(context, R.layout.cell_select_temp1, null);
			TextView tv1=(TextView)convertView.findViewById(R.id.tv1);
			TextView tv2=(TextView)convertView.findViewById(R.id.tv2);
			
			
			if(Bible.equals("旧")&&oldListIndex.equals(data.get(position).get("title")))
			{
				tv1.setTextColor(Color.parseColor("#009688"));
				tv2.setTextColor(Color.parseColor("#009688"));
			}
			if(Bible.equals("新")&&newListIndex.equals(data.get(position).get("title")))
			{
				tv1.setTextColor(Color.parseColor("#009688"));
				tv2.setTextColor(Color.parseColor("#009688"));
			}
			
			if(from.equals("old")&&bookName.equals(data.get(position).get("title")))
			{
				tv1.setTextColor(Color.parseColor("#009688"));
				tv2.setTextColor(Color.parseColor("#009688"));
			}
			if(from.equals("new")&&bookName.equals(data.get(position).get("title")))
			{
				tv1.setTextColor(Color.parseColor("#009688"));
				tv2.setTextColor(Color.parseColor("#009688"));
			}
			tv1.setText(data.get(position).get("title"));
			tv2.setText(data.get(position).get("simpleTitle"));
			return convertView;
		}
		
		
		
	}
	
	private int calcChapterIndexInBook(int bookIndex,int chapterIndex)
	{
		Log.i("bookIndex", bookIndex+"pp");
		int temp=0;
		if(bookIndex==0)
		{
			temp=chapterIndex;
		}
		else
		{
			for(int i=0;i<bookIndex;i++)
			{
				temp=temp+contentNums[i];
			}
			temp=temp+chapterIndex;
		}
		
		Log.i("bookIndex", temp+"pp");
		return temp;
		
	}
	
	public void back(View view) {
		Intent intent = new Intent(Activity_SelectVolume1.this, MainActivity.class);
		intent.putExtra("from",0);
		startActivity(intent);
		finish();
		overridePendingTransition(R.anim.zoomin, R.anim.zoomout);
	}

}
