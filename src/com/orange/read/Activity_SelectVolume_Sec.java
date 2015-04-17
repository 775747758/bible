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

		titles = new String[] { "������", "��������", "��δ��", "������", "������", "Լ���Ǽ�", "ʿʦ��", "·�ü�", "��ĸ������", "��ĸ������", "��������", "��������", "����־��", "����־��", "��˹����", "��ϣ�׼�",
				"��˹����", "Լ����", "ʫƪ", "����", "������", "�Ÿ�", "��������", "Ү������", "Ү���װ���", "��������", "��������", "��������", "Լ����", "��Ħ˾��", "��͵�����", "Լ����", "������", "�Ǻ���", "���͹���",
				"��������", "������", "����������", "��������", "��̫����", "��ɸ���", "·�Ӹ���", "Լ������", "ʹͽ�д�", "������", "���ֶ�ǰ��", "���ֶ����", "����̫��", "�Ը�����", "��������", "��������", "����������ǰ��",
				"���������Ⱥ���", "��Ħ̫ǰ��", "��Ħ̫����", "�����", "��������", "ϣ������", "�Ÿ���", "�˵�ǰ��", "�˵ú���", "Լ��һ��", "Լ������", "Լ������", "�̴���", "��ʾ¼" };
		simpleTitle = new String[] { "��", "��", "��", "��", "��", "��", "ʿ", "��", "����", "����", "����", "����", "����", "����", "��", "��", "˹", "��", "ʫ", "��", "��", "��", "��",
				"Ү", "��", "��", "��", "��", "��", "Ħ", "��", "��", "��", "��", "��", "��", "��", "��", "��", "̫", "��", "·", "Լ", "ͽ", "��", "��ǰ", "�ֺ�", "��", "��", "��", "��",
				"��ǰ", "����", "��ǰ", "���", "��", "��", "��", "��", "��ǰ", "�˺�", "Լһ", "Լ��", "Լ��", "��", "��" };
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

		// 66��ʥ��
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
