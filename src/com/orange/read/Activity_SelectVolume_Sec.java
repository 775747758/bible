package com.orange.read;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.example.acts.R;
import com.example.acts.R.anim;
import com.example.acts.R.id;
import com.example.acts.R.layout;
import com.orange.test.Activity_BibleTestMain;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsoluteLayout.LayoutParams;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.SimpleAdapter;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView;
import android.widget.Toast;

public class Activity_SelectVolume_Sec extends Activity implements OnItemClickListener {
	private boolean isChange = false;
	private int currentBookIndex;
	private int contentNum;
	private ArrayList<Map<String, Integer>> list;
	private String title;
	private TextView title_bar_name;
	private String simpleVolume;
	private ViewPager viewPager;
	private int book_index;
	private String[] titles;
	private String[] simpleTitle;
	private int[] contentNums;
	private List<View> vList;
	private String activity = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_selectvolume_sec);

		viewPager = (ViewPager) findViewById(R.id.select_sec_viewpager);

		titles = new String[] { "创世记", "出埃及记", "利未记", "民数记", "申命记", "约书亚记", "士师记", "路得记", "撒母耳记上", "撒母耳记下", "列王纪上", "列王纪下", "历代志上", "历代志下", "以斯拉记", "尼希米记",
				"以斯帖记", "约伯记", "诗篇", "箴言", "传道书", "雅歌", "以赛亚书", "耶利米书", "耶利米哀歌", "以西结书", "但以理书", "何西阿书", "约珥书", "阿摩司书", "俄巴底亚书", "约拿书", "弥迦书", "那鸿书", "哈巴谷书",
				"西番雅书", "哈该书", "撒迦利亚书", "玛拉基书", "马太福音", "马可福音", "路加福音", "约翰福音", "使徒行传", "罗马书", "哥林多前书", "哥林多后书", "加拉太书", "以弗所书", "腓立比书", "歌罗西书", "帖撒罗尼迦前书",
				"帖撒罗尼迦后书", "提摩太前书", "提摩太后书", "提多书", "腓利门书", "希伯来书", "雅各书", "彼得前书", "彼得后书", "约翰一书", "约翰二书", "约翰三书", "犹大书", "启示录" };
		simpleTitle = new String[] { "创", "出", "利", "民", "申", "书", "士", "得", "撒上", "撒下", "王上", "王下", "代上", "代下", "拉", "尼", "斯", "伯", "诗", "箴", "传", "歌", "赛",
				"耶", "哀", "结", "但", "何", "珥", "摩", "俄", "拿", "弥", "鸿", "哈", "番", "该", "亚", "玛", "太", "可", "路", "约", "徒", "罗", "林前", "林后", "加", "弗", "腓", "西",
				"帖前", "帖后", "提前", "提后", "多", "门", "来", "雅", "彼前", "彼后", "约一", "约二", "约三", "犹", "启" };
		contentNums = new int[] { 50, 40, 27, 36, 34, 24, 21, 4, 31, 24, 22, 25, 29, 36, 10, 13, 10, 42, 150, 31, 12, 8, 66, 52, 5, 48, 12, 14, 3, 9, 1, 4, 7,
				3, 3, 3, 2, 14, 4, 28, 16, 24, 21, 28, 16, 16, 13, 6, 6, 4, 4, 5, 3, 6, 4, 3, 1, 13, 5, 5, 3, 5, 1, 1, 1, 22 };

		title_bar_name = (TextView) findViewById(R.id.title_bar_name);

		Intent intent = getIntent();// intent.putExtra("simpleTitles",simpleTitles[i][position]);

		contentNum = intent.getIntExtra("contentNum", 0);
		title = intent.getStringExtra("title");
		simpleVolume = intent.getStringExtra("simpleTitles");
		title_bar_name.setText(title);
		book_index = intent.getIntExtra("book_index", 0);
		
		
		Log.i("rd",book_index+"ooo" );

		vList = new ArrayList<View>();

		// 66卷圣经
		for (int i = 0; i < 66; i++) {
			list = new ArrayList<Map<String, Integer>>();
			for (int j = 1; j <= contentNums[i]; j++) {
				Map<String, Integer> map = new HashMap<String, Integer>();
				map.put("index", Integer.parseInt(j + ""));
				list.add(map);
			}

			GridView gv = new GridView(this);
			gv.setGravity(Gravity.CENTER);
			gv.setHorizontalSpacing(10);
			gv.setVerticalSpacing(6);
			gv.setPadding(10, 10, 10, 10);
			gv.setNumColumns(4);
			gv.setSelector(new ColorDrawable(Color.TRANSPARENT));

			SimpleAdapter simpleAdapter = new SimpleAdapter(this, list, R.layout.cell_select_temp2, new String[] { "index" }, new int[] { R.id.tv });
			gv.setAdapter(simpleAdapter);
			gv.setOnItemClickListener(this);
			// gv.setLayoutParams(new ViewGroup.LayoutParams);
			vList.add(gv);
			// viewPager.addView(gv);

		}
		viewPager.setOnPageChangeListener(new MyOnPageChangeListener());
		viewPager.setAdapter(new MyPagerAdapter());
		viewPager.setCurrentItem(book_index);

	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

		Intent selectvolumeIntent = getIntent();
		setResult(1, selectvolumeIntent);
// ActivityReadMain
		Intent intent = new Intent(Activity_SelectVolume_Sec.this, Activity_ReadBible2.class);
		if (isChange) {
			
			
			intent.putExtra("volume", titles[currentBookIndex]);
			intent.putExtra("simpleVolume", simpleTitle[currentBookIndex]);
		} else {
			intent.putExtra("volume", title);
			intent.putExtra("simpleVolume", simpleVolume);
		}
		intent.putExtra("ChapterIndexInBook",calcChapterIndexInBook(position+1));
		Log.i("1234", calcChapterIndexInBook(position+1)+"oo");
		intent.putExtra("chapter", position + 1 + "");
		startActivity(intent);
		// finish();
		Log.i("activity", activity);

		overridePendingTransition(R.anim.bottom2top_in, R.anim.bottom2top_out);

	}

	public void back(View view) {
		Intent intent = new Intent(Activity_SelectVolume_Sec.this, Activity_SelectVolume.class);
		startActivity(intent);
		finish();
		overridePendingTransition(R.anim.zoomin, R.anim.zoomout);
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
			isChange = true;
			currentBookIndex = arg0;
			title_bar_name.setText(titles[arg0]);

		}

	}
	private int calcChapterIndexInBook(int chapterIndex)
	{
		int temp=0;
		if(book_index==0)
		{
			temp=chapterIndex;
		}
		else
		{
			for(int i=0;i<book_index;i++)
			{
				temp=temp+contentNums[i];
			}
			temp=temp+chapterIndex;
		}
		Log.i("calcChapterIndexInBook", temp+"ll");
		return temp;
		
	}

}
