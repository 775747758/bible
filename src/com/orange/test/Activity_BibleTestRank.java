package com.orange.test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.baidu.frontia.Frontia;
import com.baidu.frontia.FrontiaData;
import com.baidu.frontia.FrontiaQuery;
import com.baidu.frontia.api.FrontiaStorage;
import com.baidu.frontia.api.FrontiaStorageListener.DataInfoListener;
import com.example.acts.MainActivity;
import com.example.acts.R;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.orange.church.ChurchNoticeAdapter;
import com.orange.recite.Activity_ReciteMain.MyOnClickListener;
import com.orange.recite.Activity_ReciteMain.MyOnPageChangeListener;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.PagerTabStrip;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.Animation;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class Activity_BibleTestRank extends Activity implements
		OnRefreshListener<ListView> {

	List<Map<String, String>> userData = new ArrayList<Map<String, String>>();
	List<Map<String, String>> myChurchData = new ArrayList<Map<String, String>>();
	private int currIndex = 0;// µ±Ç°Ò³¿¨±àºÅ
	private ViewPager mViewPager;
	private ArrayList<String> titleList;
	private TextView t1;
	private TextView t2;
	private ArrayList<View> listViews;
	private PullToRefreshListView plv_all;
	private PullToRefreshListView plv_church;
	private FrontiaStorage mCloudStorage;
	private boolean isLogin;
	private String churchName;
	private static final String[] STRINGS = { "2", "4", "4", "8", "20", "20",
			"72", "45", "72", "4", "7", "40", "100", "45" };

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_bibletestrank);

		boolean isInit = Frontia.init(this, "GdZNITcYcI3bhiwKx7duVbR7");
		if (!isInit) {
			Toast.makeText(this, "ÄúµÄapp key ´íÎó", 3).show();
			return;
		}
		// mFile = new FrontiaFile();
		mCloudStorage = Frontia.getStorage();

		SharedPreferences spUser = getSharedPreferences("UserInfo",
				Context.MODE_PRIVATE);

		isLogin = spUser.getBoolean("isLogin", false);
		
			getChurchNotice();
		

		
		SharedPreferences spChurch = getSharedPreferences("ChurchInfo", Context.MODE_PRIVATE);
		churchName = spChurch.getString("churchName", "");
		/*
		 * for(int i=0;i<STRINGS.length;i++) { Map<String, String> map=new
		 * HashMap<String, String>(); map.put("name", "µËÎÄ½Ü");
		 * map.put("userName", "userName"); map.put("grade", STRINGS[i]+"");
		 * userData.add(map); }
		 */

		mViewPager = (ViewPager) findViewById(R.id.vp_list);

		InitTextView();

		listViews = new ArrayList<View>();
		LayoutInflater mInflater = getLayoutInflater();

		plv_all = (PullToRefreshListView) mInflater.inflate(
				R.layout.cell_allrank, null);
		plv_church = (PullToRefreshListView) mInflater.inflate(
				R.layout.cell_churchrank, null);

		listViews.add(plv_all);
		listViews.add(plv_church);

		
		plv_all.setAdapter(new RankAdapter(userData, getApplicationContext()));
		plv_church.setAdapter(new RankAdapter(myChurchData, getApplicationContext()));
		
		plv_all.setOnRefreshListener(Activity_BibleTestRank.this);
		plv_church.setOnRefreshListener(Activity_BibleTestRank.this);

		mViewPager.setAdapter(new ListViewPagerAdapter());
		mViewPager.setCurrentItem(0);
		mViewPager.setOnPageChangeListener(new MyOnPageChangeListener());
		

	}

	private class ListViewPagerAdapter extends PagerAdapter {

		// public List<View> mListViews;

		/*
		 * public ListViewPagerAdapter(List<View> mListViews) { this.mListViews
		 * = mListViews; }
		 */

		@Override
		public View instantiateItem(ViewGroup container, int position) {
			((ViewPager) container).addView(listViews.get(position), 0);

			return listViews.get(position);
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			((ViewPager) container).removeView(listViews.get(position));

		}

		@Override
		public boolean isViewFromObject(View view, Object object) {
			return view == object;
		}

		@Override
		public int getCount() {
			return listViews.size();
		}

	}

	@Override
	public void onRefresh(PullToRefreshBase<ListView> refreshView) {
		// TODO Auto-generated method stub

	}

	

	private void InitTextView() {
		t1 = (TextView) findViewById(R.id.text1);
		t2 = (TextView) findViewById(R.id.text2);

		t1.setTextColor(Color.WHITE);
		t1.setBackgroundDrawable(getResources().getDrawable(
				R.drawable.rounded_recite_vptitle));

		t1.setOnClickListener(new MyOnClickListener(0));
		t2.setOnClickListener(new MyOnClickListener(1));
	}

	public class MyOnClickListener implements View.OnClickListener {
		private int index = 0;

		public MyOnClickListener(int i) {
			index = i;
		}

		@Override
		public void onClick(View v) {
			mViewPager.setCurrentItem(index);
		}
	};

	public class MyOnPageChangeListener implements OnPageChangeListener {

		// int one = offset * 2 + bmpW;// Ò³¿¨1 -> Ò³¿¨2 Æ«ÒÆÁ¿

		@Override
		public void onPageSelected(int arg0) {
			Animation animation = null;
			switch (arg0) {
			case 0:
				if (currIndex == 1) {
					t2.setTextColor(Color.BLACK);
					t1.setBackgroundDrawable(getResources().getDrawable(
							R.drawable.rounded_recite_vptitle));
					t1.setTextColor(Color.WHITE);
					t2.setBackgroundColor(Color.TRANSPARENT);
				}

				break;
			case 1:
				if (currIndex == 0) {
					t1.setTextColor(Color.BLACK);
					t2.setBackgroundDrawable(getResources().getDrawable(
							R.drawable.rounded_recite_vptitle));
					t2.setTextColor(Color.WHITE);
					t1.setBackgroundColor(Color.TRANSPARENT);
				}

				break;
			}
			currIndex = arg0;
		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
		}

		@Override
		public void onPageScrollStateChanged(int arg0) {
		}
	}

	public void getChurchNotice() {
		FrontiaQuery query = new FrontiaQuery();
		query.equals("tableType", "bibleQuestion");
		mCloudStorage.findData(query, new DataInfoListener() {
			@Override
			public void onSuccess(List<FrontiaData> dataList) {
				StringBuilder sb = new StringBuilder();
				for (FrontiaData d : dataList) {

					Map<String, String> map = new HashMap<String, String>();
					map.put("name", (String) d.get("name"));
					map.put("userName", (String) d.get("userName"));
					map.put("portraitUri", (String) d.get("portraitUri"));
					map.put("grade", d.get("grade").toString());
					map.put("year", (String) d.get("year"));
					map.put("month", (String) d.get("month"));
					map.put("day", (String) d.get("day"));
					map.put("hour", (String) d.get("hour"));
					map.put("minute", (String) d.get("minute"));
					map.put("churchName", (String) d.get("churchName"));
					if(!churchName.equals("")||churchName.equals(d.get("churchName").toString().trim()))
					{
						myChurchData.add(map);
					}
					userData.add(map);

				}
				Log.i("userData", userData.size()+"sjsss");
				plv_all.setAdapter(new RankAdapter(userData, getApplicationContext()));
				plv_church.setAdapter(new RankAdapter(myChurchData, getApplicationContext()));
			}

			@Override
			public void onFailure(int errCode, String errMsg) {
				

			}
		});

	}
	
	public void back(View view) {
		Intent intent = new Intent(Activity_BibleTestRank.this, Activity_BibleTestMain.class);
		startActivity(intent);
		finish();
		overridePendingTransition(R.anim.zoomin, R.anim.zoomout);
	}

}
