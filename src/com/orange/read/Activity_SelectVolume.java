package com.orange.read;

/*
 * 此activity是圣经经卷选择界面
 */

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.example.acts.NonScrollGridView;
import com.example.acts.R;
import com.example.acts.R.id;
import com.example.acts.R.layout;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
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
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

public class Activity_SelectVolume extends Activity implements OnItemClickListener {

	LayoutInflater inflater;
	ListView lv;
	private ArrayList<GridView> gvList;
	private int[][] contentNums;
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

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_selectvolume);

		select_viewpager = (ViewPager) findViewById(R.id.select_viewpager);

		/*
		 * ImageView menuImg = (ImageView)findViewById(R.id.title_bar_menu_btn);
		 * menuImg.setOnClickListener(this);
		 */

		// LinearLayout ll3 = (LinearLayout) findViewById(R.id.ll3);//
		// 放置进度条的LinearLayout
		tv_old = (TextView) findViewById(R.id.tv_old);
		tv_new = (TextView) findViewById(R.id.tv_new);

		tv_old.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				select_viewpager.setCurrentItem(0);
			}
		});
		tv_new.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				select_viewpager.setCurrentItem(1);
			}
		});

		View view_old = LayoutInflater.from(this).inflate(R.layout.selectvolume_old, null);
		View view_new = LayoutInflater.from(this).inflate(R.layout.selectvolume_new, null);

		gv_old = (NonScrollGridView) view_old.findViewById(R.id.gv1);
		gv_new = (NonScrollGridView) view_new.findViewById(R.id.gv2);

		vlist = new ArrayList<View>();
		vlist.add(view_old);
		vlist.add(view_new);

		select_viewpager.setAdapter(new MyPagerAdapter());

		select_viewpager.setOnPageChangeListener(new MyOnPageChangeListener());

		int width = getWindowManager().getDefaultDisplay().getWidth();// 屏幕宽度
		// int height=getWindowManager().getDefaultDisplay().getHeight();//屏幕高度

		// img3.setLayoutParams(ps);

		List<Map<String, String>> list1 = new ArrayList<Map<String, String>>();
		List<Map<String, String>> list2 = new ArrayList<Map<String, String>>();

		List<List<Map<String, String>>> allList = new ArrayList<List<Map<String, String>>>();
		allList.add(list1);
		allList.add(list2);

		title_old = new String[] { "创世记", "出埃及记", "利未记", "民数记", "申命记", "约书亚记", "士师记", "路得记", "撒母耳记上", "撒母耳记下", "列王纪上", "列王纪下", "历代志上", "历代志下", "以斯拉记", "尼希米记",
				"以斯帖记", "约伯记", "诗篇", "箴言", "传道书", "雅歌", "以赛亚书", "耶利米书", "耶利米哀歌", "以西结书", "但以理书", "何西阿书", "约珥书", "阿摩司书", "俄巴底亚书", "约拿书", "弥迦书", "那鸿书", "哈巴谷书",
				"西番雅书", "哈该书", "撒迦利亚书", "玛拉基书" };
		simpleTitle_old = new String[] { "创", "出", "利", "民", "申", "书", "士", "得", "撒上", "撒下", "王上", "王下", "代上", "代下", "拉", "尼", "斯", "伯", "诗", "箴", "传", "歌",
				"赛", "耶", "哀", "结", "但", "何", "珥", "摩", "俄", "拿", "弥", "鸿", "哈", "番", "该", "亚", "玛" };
		contentNum_old = new int[] { 50, 40, 27, 36, 34, 24, 21, 4, 31, 24, 22, 25, 29, 36, 10, 13, 10, 42, 150, 31, 12, 8, 66, 52, 5, 48, 12, 14, 3, 9, 1, 4,
				7, 3, 3, 3, 2, 14, 4 };

		title_new = new String[] { "马太福音", "马可福音", "路加福音", "约翰福音", "使徒行传", "罗马书", "哥林多前书", "哥林多后书", "加拉太书", "以弗所书", "腓立比书", "歌罗西书", "帖撒罗尼迦前书", "帖撒罗尼迦后书",
				"提摩太前书", "提摩太后书", "提多书", "腓利门书", "希伯来书", "雅各书", "彼得前书", "彼得后书", "约翰一书", "约翰二书", "约翰三书", "犹大书", "启示录" };
		simpleTitle_new = new String[] { "太", "可", "路", "约", "徒", "罗", "林前", "林后", "加", "弗", "腓", "西", "帖前", "帖后", "提前", "提后", "多", "门", "来", "雅", "彼前", "彼后",
				"约一", "约二", "约三", "犹", "启" };
		contentNum_new = new int[] { 28, 16, 24, 21, 28, 16, 16, 13, 6, 6, 4, 4, 5, 3, 6, 4, 3, 1, 13, 5, 5, 3, 5, 1, 1, 1, 22 };

		title = new String[][] { title_old, title_new };
		contentNums = new int[][] { contentNum_old, contentNum_new };
		simpleTitle = new String[][] { simpleTitle_old, simpleTitle_new };

		for (int i = 0; i < contentNums.length; i++) {

			for (int j = 0; j < contentNums[i].length; j++) {
				Map<String, String> map = new HashMap<String, String>();
				map.put("title", title[i][j]);
				map.put("simpleTitle", simpleTitle[i][j]);
				allList.get(i).add(map);
			}

		}

		// NonScrollGridView gridview2 = (NonScrollGridView)
		// findViewById(R.id.gv2);
		SimpleAdapter simpleAdapter_old = new SimpleAdapter(this, list1, R.layout.cell_select_temp1, new String[] { "title", "simpleTitle" }, new int[] {
				R.id.tv1, R.id.tv2 });
		gv_old.setAdapter(simpleAdapter_old);
		// gv_old.setOnItemSelectedListener(this);
		gv_old.setOnItemClickListener(this);
		gv_old.setSelector(new ColorDrawable(Color.TRANSPARENT));

		SimpleAdapter simpleAdapter_new = new SimpleAdapter(this, list2, R.layout.cell_select_temp1, new String[] { "title", "simpleTitle" }, new int[] {
				R.id.tv1, R.id.tv2 });
		gv_new.setAdapter(simpleAdapter_new);
		// gv_new.setOnItemSelectedListener(this);
		gv_new.setOnItemClickListener(this);
		// 去掉gridview的黄色背景
		gv_new.setSelector(new ColorDrawable(Color.TRANSPARENT));

		gvList = new ArrayList<GridView>();
		gvList.add(gv_old);
		gvList.add(gv_new);

		/*
		 * for(int i=0;i<gvList.size();i++) { gvList.get(i).setSelector(new
		 * ColorDrawable(Color.TRANSPARENT));
		 * gvList.get(i).setOnItemClickListener(this); }
		 */
	}

	class MyPagerAdapter extends PagerAdapter {

		@Override
		public Object instantiateItem(View container, int position) {
			((ViewPager) container).addView(vlist.get(position));
			return vlist.get(position);
		}

		@Override
		public void destroyItem(View container, int position, Object object) {
			((ViewPager) container).removeView(vlist.get(position));
			super.destroyItem(container, position, object);

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
				if (currIndex == 1) {
					tv_old.setBackground(getResources().getDrawable(R.drawable.left_select_rounded_layout));
					tv_new.setBackground(getResources().getDrawable(R.drawable.right_rounded_layout));
					// tv_old.setTextColor(getResources().getColor(R.color.select_volume_tab_select_font));
					// tv_new.setTextColor(getResources().getColor(R.color.select_volume_tab_defalt_font));
				}
				break;
			case 1:
				if (currIndex == 0) {
					tv_old.setBackground(getResources().getDrawable(R.drawable.left_rounded_layout));
					tv_new.setBackground(getResources().getDrawable(R.drawable.right_select_rounded_layout));
					// tv_old.setTextColor(getResources().getColor(R.color.select_volume_tab_defalt_font));
					// tv_new.setTextColor(getResources().getColor(R.color.select_volume_tab_select_font));

				}
				break;

			}
			currIndex = arg0;
		}

	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		for (int i = 0; i < gvList.size(); i++) {

			if (arg0.getId() == gvList.get(i).getId()) {
				// 传一个在圣经所有卷书的位置
				int index = 0;
				if (i == 0) {
					index = arg2;
				} else {
					index = 39 + arg2;
				}
				Intent intent = new Intent(Activity_SelectVolume.this, Activity_SelectVolume_Sec.class);
				intent.putExtra("book_index", index);
				Log.i("book_index", index + "");
				intent.putExtra("contentNum", contentNums[i][arg2]);
				intent.putExtra("title", title[i][arg2]);
				intent.putExtra("simpleTitles", simpleTitle[i][arg2]);
				// startActivity(intent);
				startActivityForResult(intent, 1);

				// Toast.makeText(getApplicationContext(),
				// titles[i][position]+":"+contentNums[i][position],Toast.LENGTH_LONG).show();
			}
		}
	}

	//
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == 1) {
			Intent readbibleIntent = getIntent();
			setResult(1, readbibleIntent);
		}
	}
	
	

}
