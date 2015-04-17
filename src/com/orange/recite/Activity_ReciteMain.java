package com.orange.recite;

import java.io.BufferedReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import www.orange.utils.UMShare;
import android.annotation.SuppressLint;
import android.app.ActionBar.LayoutParams;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.acts.DatabaseHelper;
import com.example.acts.GalleryFlow;
import com.example.acts.ImageAdapter;
import com.example.acts.MainActivity;
import com.example.acts.R;
import com.example.acts.SlideMenu;
import com.orange.read.Activity_ReadBible2;
import com.orange.test.Activity_BibleTestMain;
import com.orange.view.ElasticListView;
import com.umeng.socialize.media.QQShareContent;
import com.umeng.socialize.media.QZoneShareContent;

@SuppressLint("NewApi")
public class Activity_ReciteMain extends Activity implements OnClickListener {

	private RelativeLayout relativelayout;
	private ImageAdapter ia;
	// private MyHandle myhandle = new MyHandle();
	private Boolean isExit = false;
	private Boolean hasTask = false;

	Map<String, String> shareText = new HashMap<String, String>();
	List<Map<String, String>> dataa;
	HashMap<String, String> itemmap;
	List<Map<String, String>> Data;
	private ImageView seekbar_bg;
	private SlideMenu slideMenu;
	BufferedReader reader;
	MyAllSectionAdapter adapter;
	private ViewPager mPager;// 页卡内容
	private List<View> listViews; // Tab页面列表
	// private ImageView cursor;// 动画图片
	private TextView t1, t2, t3, t4;// 页卡头标
	private int offset = 0;// 动画图片偏移量
	private int currIndex = 0;// 当前页卡编号
	private int bmpW;// 动画图片宽
	String content;
	String chapter;
	ElasticListView listView2;
	ElasticListView listView1;

	SimpleAdapter simpleAdapterr;
	Button ib;
	GalleryFlow mygallery;
	int[] imageIds;
	String uri;
	private RelativeLayout bg_rl;
	private ElasticListView listView3;
	private ElasticListView listView4;
	private ArrayList<Map<String, String>> ReciteData;
	private ArrayList<Map<String, String>> RecommendData;
	private ArrayList<Map<String, String>> ListData = new ArrayList<Map<String, String>>();
	private List<Map<String, String>> TitlrData = new ArrayList<Map<String, String>>();
	private LayoutInflater inflater;
	private View popView;
	private PopupWindow popWin;
	private ImageView spinner_iv;
	private boolean isSpinnerExit;
	private LinearLayout category_ll;
	private GridView category_gv;
	private RecommendAdapter recommendAdapter;
	// private View conView;
	// private TextView title_tv;
	private DatabaseHelper dbHelper;
	private SQLiteDatabase db;
	private LinearLayout panel;
	private ImageView strengthen;
	private ImageView fluent;
	private ImageView copy;
	private ImageView share;
	private ImageView delete;
	private int position;
	private int winWidth;
	private AlphaAnimation inAnim;
	private AlphaAnimation outAnim;
	private UMShare umShare;
	private List<Map<String, String>> StrengthData = new ArrayList<Map<String, String>>();
	public MyStrengthenAdapter fluentAdapter;
	private List<Map<String, String>> FluentData = new ArrayList<Map<String, String>>();
	private MyStrengthenAdapter strengthAdapter;
	private int isSelectCategory;
	private ArrayList<Map<String, String>> specificData;
	// Intent intent=new Intent(MainActivity.this,Activity1.class);
	private TextView noallsection_tv;
	private TextView nostrengthen_tv;
	private TextView nofluent_tv;

	@SuppressLint("NewApi")
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 为界面上面加一个title
		// requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);

		setContentView(R.layout.activity_recitemain);
		umShare = new UMShare(Activity_ReciteMain.this);

		inAnim = new AlphaAnimation(0.0f, 1.0f);
		// TranslateAnimation Anim=new TranslateAnimation(0, -winWidth, 0, 0);
		// ScaleAnimation inAnim=new ScaleAnimation(1f, 0.0f, 1f, 0.0f,
		// Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
		inAnim.setDuration(200);
		inAnim.setFillAfter(true);

		outAnim = new AlphaAnimation(1.0f, 0.0f);
		// TranslateAnimation Anim=new TranslateAnimation(0, -winWidth, 0, 0);
		// ScaleAnimation inAnim=new ScaleAnimation(1f, 0.0f, 1f, 0.0f,
		// Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
		outAnim.setDuration(200);
		outAnim.setFillAfter(true);

		new Thread(new Runnable() {

			@Override
			public void run() {
				InitTextView();

			}
		}).start();
		loadReciteBible();
		loadRecommendBible();
		createListData();
		createTitleData();
		createStrengthData();
		createFluentData();
		mPager = (ViewPager) findViewById(R.id.vPager);
		
		
		listViews = new ArrayList<View>();
		LayoutInflater mInflater = getLayoutInflater();

		View view1 = mInflater.inflate(R.layout.cell_allsection, null);
		View view2 = mInflater.inflate(R.layout.cell_readmain_strengthen, null);
		View view3 = mInflater.inflate(R.layout.cell_readmain_fluent, null);
		View view4 = mInflater.inflate(R.layout.cell_readmain_recommend, null);
		
		noallsection_tv=(TextView)view1.findViewById(R.id.noallsection_tv);
		nostrengthen_tv=(TextView)view2.findViewById(R.id.nostrengthen_tv);
		nofluent_tv=(TextView)view3.findViewById(R.id.nofluent_tv);
		if(ReciteData.size()==0)
		{
			noallsection_tv.setVisibility(View.VISIBLE);
		}

		listViews.add(view4);
		listViews.add(view1);
		listViews.add(view2);
		listViews.add(view3);
		

		mPager.setAdapter(new MyPagerAdapter(listViews));
		mPager.setCurrentItem(0);
		mPager.setOnPageChangeListener(new MyOnPageChangeListener());

		Log.i("RecommendData", RecommendData.size() + "");

		// listView4.invalidateViews();

		inflater = LayoutInflater.from(this);

		category_ll = (LinearLayout) view4.findViewById(R.id.category_ll);
		popView = inflater.inflate(R.layout.popwindow_recommend, null);

		category_gv = (GridView) popView.findViewById(R.id.category_gv);
		SimpleAdapter simpleAdapter_old = new SimpleAdapter(getApplicationContext(), TitlrData, R.layout.cell_recommend_gv, new String[] { "title" },
				new int[] { R.id.category_tv });

		category_gv.setAdapter(simpleAdapter_old);
		category_gv.setSelector(new ColorDrawable(Color.TRANSPARENT));
		category_gv.setOnItemClickListener(new OnItemClickListener() {

			

			

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				if(arg2==0)
				{
					recommendAdapter = new RecommendAdapter(ListData);
					listView4.setAdapter(recommendAdapter);
					isSelectCategory=0;
				}
				else
				{
					specificData = new ArrayList<Map<String, String>>();
					for (int i = 0; i < ListData.size(); i++) {
						if (ListData.get(i).get("title").equals(TitlrData.get(arg2).get("title"))) {
							specificData.add(ListData.get(i));
						}
					}
					recommendAdapter = new RecommendAdapter(specificData);
					listView4.setAdapter(recommendAdapter);
					isSelectCategory=1;
				}
				
				
				popWin.dismiss();
			}

		});

		popWin = new PopupWindow(popView, LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT, true);
		// popWin.
		// popWin.setBackgroundDrawable(new ColorDrawable(0x99000000));
		popWin.setAnimationStyle(R.style.UpdatePopupAnimation);
		ColorDrawable dw = new ColorDrawable(0x00000000);
		popWin.setBackgroundDrawable(dw);
		popWin.setOnDismissListener(new OnDismissListener() {

			@Override
			public void onDismiss() {
				spinner_iv.setImageDrawable(getResources().getDrawable(R.drawable.spinner_bottom));
				// spinner_tv.setTextColor(Color.BLACK);
				isSpinnerExit = false;
			}
		});

		category_ll.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (isSpinnerExit) {
					spinner_iv.setImageDrawable(getResources().getDrawable(R.drawable.spinner_bottom));
					// spinner_tv.setTextColor(Color.BLACK);
					isSpinnerExit = false;
					// popupmenu.dismiss();
					popWin.dismiss();
				} else {
					spinner_iv.setImageDrawable(getResources().getDrawable(R.drawable.spinner_top));
					// spinner_tv.setTextColor(getResources().getColor(R.color.titleBar));
					isSpinnerExit = true;
					// popupmenu.show();
					popWin.showAsDropDown(category_ll);
					popWin.showAtLocation(category_ll, Gravity.CENTER, 0, 0);
				}

			}
		});
		View view = LayoutInflater.from(this).inflate(R.layout.listitem_allscriptures, null);

		WindowManager wm = getWindowManager();
		winWidth = wm.getDefaultDisplay().getWidth();

	}

	public void loadMem() {
		ContentValues values = new ContentValues();
		DatabaseHelper dbHelper = new DatabaseHelper(Activity_ReciteMain.this,"bible.db");
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		db.execSQL("create table  if not exists  membible(chapter varchar(20),content varchar(300),PRIMARY KEY(chapter))");
		Cursor cursor = db.query("membible", new String[] { "chapter", "content" }, null, null, null, null, null);
		dataa = new ArrayList<Map<String, String>>();
		while (cursor.moveToNext()) {
			Map<String, String> mapp = new HashMap<String, String>();
			String chapterr = cursor.getString(cursor.getColumnIndex("chapter"));
			String contentt = cursor.getString(cursor.getColumnIndex("content"));
			Map<String, String> map = new HashMap<String, String>();
			mapp.put("nametext", contentt);
			mapp.put("iconid", chapterr);
			dataa.add(mapp);
		}
		adapter = new MyAllSectionAdapter(getApplicationContext(), dataa);
		listView2.setAdapter(adapter);
	}

	/**
	 * 初始化头标
	 */
	private void InitTextView() {
		t1 = (TextView) findViewById(R.id.text1);
		t2 = (TextView) findViewById(R.id.text2);
		t3 = (TextView) findViewById(R.id.text3);
		t4 = (TextView) findViewById(R.id.text4);

		t1.setTextColor(Color.WHITE);
		t1.setBackgroundDrawable(getResources().getDrawable(R.drawable.rounded_recite_vptitle));

		t1.setOnClickListener(new MyOnClickListener(0));
		t2.setOnClickListener(new MyOnClickListener(1));
		t3.setOnClickListener(new MyOnClickListener(2));
		t4.setOnClickListener(new MyOnClickListener(3));
	}

	/**
	 * ViewPager适配器
	 */
	public class MyPagerAdapter extends PagerAdapter {
		public List<View> mListViews;
		private GridView category_gv;
		private LinearLayout category_ll;
		

		public MyPagerAdapter(List<View> mListViews) {
			this.mListViews = mListViews;
		}

		@Override
		public void destroyItem(View arg0, int arg1, Object arg2) {
			((ViewPager) arg0).removeView(listViews.get(arg1));
		}

		@Override
		public void finishUpdate(View arg0) {
		}

		@Override
		public int getCount() {
			return listViews.size();
		}

		@Override
		public Object instantiateItem(View arg0, int arg1) {
			((ViewPager) arg0).addView(mListViews.get(arg1), 0);
			switch (arg1) {
			case 1:
				final MyAllSectionAdapter myAdapter = new MyAllSectionAdapter(getApplicationContext(), ReciteData);
				listView1 = (ElasticListView) arg0.findViewById(R.id.defaultlv);
				listView1.setAdapter(myAdapter);
				listView1.setOnItemClickListener(new OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
						if(position!=arg2)
						{
							if(panel!=null)
							{
								panel.setAnimation(outAnim);
								panel.setVisibility(View.INVISIBLE);
								if(myAdapter!=null)
								{
									myAdapter.notifyDataSetChanged();
								}
							}
						}
						
					}
				});
				listView1.setOnItemLongClickListener(new OnItemLongClickListener() {

					private int curStrength;
					private int curFluent;

					@Override
					public boolean onItemLongClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
						strengthen = (ImageView) arg1.findViewById(R.id.strengthen);
						fluent = (ImageView) arg1.findViewById(R.id.fluent);
						copy = (ImageView) arg1.findViewById(R.id.copy);
						share = (ImageView) arg1.findViewById(R.id.share);
						delete = (ImageView) arg1.findViewById(R.id.delete);

						// 可以在点击其它条目时让本条目消失
						if (panel != null) {
							panel.setAnimation(outAnim);
							panel.setVisibility(View.INVISIBLE);
						}

						// myAdapter.notifyDataSetChanged();
						if (ReciteData.get(position).get("secontFlag").equals("2")) {
							Log.i("加强按下", "加强按下");
							curStrength = 1;
							strengthen.setImageResource(R.drawable.strengthen_pressed);
						} else {
							Log.i("加强", "加强");
							curStrength = 0;
							strengthen.setImageResource(R.drawable.strengthen);
						}
						if (ReciteData.get(position).get("secontFlag").equals("3")) {
							Log.i("流利按下", "流利按下");
							curFluent = 1;
							fluent.setImageResource(R.drawable.fluent_pressed);
						} else {
							Log.i("流利", "流利");
							curFluent = 0;
							fluent.setImageResource(R.drawable.fluent);
						}
						Log.i("进入item", "进入item");
						panel = (LinearLayout) arg1.findViewById(R.id.panel);
						// TextView title_content =
						// (TextView)arg1.findViewById(R.id.content);
						// TextView title_chapter =
						// (TextView)arg1.findViewById(R.id.chapter);
						panel.setLayoutParams(new RelativeLayout.LayoutParams(LayoutParams.FILL_PARENT, arg1.getHeight()));
						panel.setVisibility(View.VISIBLE);
						position = arg2;

						// panel.setVisibility(View.INVISIBLE);
						strengthen.setOnClickListener(new OnClickListener() {

							@Override
							public void onClick(View v) {
								Log.i("strengthen", ReciteData.get(position).get("secontFlag"));
								if (curStrength == 1) {
									// panel.setVisibility(View.VISIBLE);
									// strengthen.setImageResource(R.drawable.strengthen_pressed);
									Toast.makeText(getApplicationContext(), "本经文已经添加到流利页面！请重新选择！", Toast.LENGTH_SHORT).show();
								} else {
									strengthen.setImageResource(R.drawable.strengthen_pressed);
									fluent.setImageResource(R.drawable.fluent);
									addToStrengthen(position);
									Map<String, String> map = ReciteData.get(position);
									map.put("secontFlag", "2");
									ReciteData.set(position, map);
									StrengthData.add(map);
									nostrengthen_tv.setVisibility(View.GONE);
									strengthAdapter.notifyDataSetChanged();
									panel.setAnimation(outAnim);
									panel.setVisibility(View.INVISIBLE);
									// 不然会失灵
									myAdapter.notifyDataSetChanged();
									Toast.makeText(getApplicationContext(), "成功添加到加强页面！", Toast.LENGTH_SHORT).show();

								}

							}
						});
						fluent.setOnClickListener(new OnClickListener() {

							@Override
							public void onClick(View v) {
								// Log.i("strengthen",ReciteData.get(position).get("secontFlag"));
								if (curFluent == 1) {
									// panel.setVisibility(View.VISIBLE);
									Log.i("fluent", "fluent");
									// fluent.setImageResource(R.drawable.fluent_pressed);
									Toast.makeText(getApplicationContext(), "本经文已经添加到流利页面！请重新选择！", Toast.LENGTH_SHORT).show();
								} else {
									fluent.setImageResource(R.drawable.fluent_pressed);
									strengthen.setImageResource(R.drawable.strengthen);
									addTofluent(position);
									Map<String, String> map = ReciteData.get(position);
									map.put("secontFlag", "3");
									ReciteData.set(position, map);
									FluentData.add(map);
									nofluent_tv.setVisibility(View.GONE);
									if(fluentAdapter!=null)
									{
										fluentAdapter.notifyDataSetChanged();
									}
									
									panel.setAnimation(outAnim);
									panel.setVisibility(View.INVISIBLE);
									// 不然会失灵
									myAdapter.notifyDataSetChanged();
									Toast.makeText(getApplicationContext(), "成功添加到流利页面！", Toast.LENGTH_SHORT).show();
								}

							}
						});
						copy.setOnClickListener(new OnClickListener() {

							@Override
							public void onClick(View v) {
								copyAllSection(ReciteData,position);
								panel.setAnimation(outAnim);
								panel.setVisibility(View.INVISIBLE);
								// 不然会失灵
								myAdapter.notifyDataSetChanged();
								Toast.makeText(getApplicationContext(), "复制成功！", Toast.LENGTH_SHORT).show();
							}
						});
						share.setOnClickListener(new OnClickListener() {

							@Override
							public void onClick(View v) {
								
								share(getShareString(ReciteData,position));
								panel.setAnimation(outAnim);
								panel.setVisibility(View.INVISIBLE);
								// 不然会失灵
								myAdapter.notifyDataSetChanged();
								//Toast.makeText(getApplicationContext(), "复制成功！", Toast.LENGTH_SHORT).show();
							}
						});
						delete.setOnClickListener(new OnClickListener() {
							
							@Override
							public void onClick(View v) {
								
								deleteItem(ReciteData,0,position);
								ReciteData.remove(position);
								/*deleteItem(FluentData,1,position);
								FluentData.remove(position);
								fluentAdapter.notifyDataSetChanged();*/
								myAdapter.notifyDataSetChanged();
								
							}
						});
						return false;
					}
				});

				break;
			case 2:
				if(StrengthData.size()==0)
				{
					nostrengthen_tv.setVisibility(View.VISIBLE);
				}
				listView2 = (ElasticListView) arg0.findViewById(R.id.strengthen);
				strengthAdapter=new MyStrengthenAdapter(getApplicationContext(), StrengthData);
				listView2.setAdapter(strengthAdapter);
				listView2.setOnItemLongClickListener(new OnItemLongClickListener() {

					private AlertDialog strengthenAlert;
					
					@Override
					public boolean onItemLongClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
						position = arg2;
						String title=StrengthData.get(position).get("bookName") + " "
								+ StrengthData.get(position).get("chapterIndex") + ":" + StrengthData.get(position).get("sectionIndex");
						
						AlertDialog.Builder builder = new AlertDialog.Builder(Activity_ReciteMain.this,AlertDialog.THEME_HOLO_LIGHT);  
						//AlertDialog strengthenDialog=new AlertDialog.Builder(Activity_ReciteMain.this,).create();
						builder.setTitle(title);
						builder.setItems(new String[] { "复制", "分享","删除" }, new DialogInterface.OnClickListener() {
							
							@Override
							public void onClick(DialogInterface dialog, int which) {
								if(which==0)
								{
									copyAllSection(StrengthData,position);
									strengthenAlert.dismiss();
								}
								else if(which==1)
								{
									
									share(getShareString(StrengthData,position));
									strengthenAlert.dismiss();
								}
								else
								{
									deleteItem(StrengthData,1,position);
									StrengthData.remove(position);
									strengthAdapter.notifyDataSetChanged();
									//myAdapter.notifyDataSetChanged();
									strengthenAlert.dismiss();
								}
								
							}
						});
						
						strengthenAlert = builder.create();
						strengthenAlert.show();
						return false;
					}
				});
				break;
			case 3:
				if(FluentData.size()==0)
				{
					nofluent_tv.setVisibility(View.VISIBLE);
				}
				listView3 = (ElasticListView) arg0.findViewById(R.id.fluent);
				fluentAdapter=new MyStrengthenAdapter(getApplicationContext(), FluentData);
				listView3.setAdapter(fluentAdapter);
				listView3.setOnItemLongClickListener(new OnItemLongClickListener() {

					private AlertDialog strengthenAlert;
					private AlertDialog fluentAlert;
					
					@Override
					public boolean onItemLongClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
						position = arg2;
						String title=FluentData.get(position).get("bookName") + " "
								+ FluentData.get(position).get("chapterIndex") + ":" + FluentData.get(position).get("sectionIndex");
						
						AlertDialog.Builder builder = new AlertDialog.Builder(Activity_ReciteMain.this,AlertDialog.THEME_HOLO_LIGHT);  
						//AlertDialog strengthenDialog=new AlertDialog.Builder(Activity_ReciteMain.this,).create();
						builder.setTitle(title);
						builder.setItems(new String[] { "复制", "分享","删除" }, new DialogInterface.OnClickListener() {
							
							@Override
							public void onClick(DialogInterface dialog, int which) {
								if(which==0)
								{
									copyAllSection(FluentData,position);
									fluentAlert.dismiss();
								}
								else if(which==1)
								{
									
									share(getShareString(FluentData,position));
									fluentAlert.dismiss();
								}
								else
								{
									deleteItem(FluentData,1,position);
									if(FluentData.size()!=0)
									{
										FluentData.remove(position);
									}
									fluentAdapter.notifyDataSetChanged();
									//myAdapter.notifyDataSetChanged();
									fluentAlert.dismiss();
								}
								
							}
						});
						
						fluentAlert = builder.create();
						fluentAlert.show();
						return false;
					}
				});
				break;
			case 0:
				listView4 = (ElasticListView) arg0.findViewById(R.id.recommend);
				recommendAdapter = new RecommendAdapter(ListData);
				listView4.setAdapter(recommendAdapter);
				spinner_iv = (ImageView) arg0.findViewById(R.id.spinner_iv);
				listView4.setOnItemLongClickListener(new OnItemLongClickListener() {

					private AlertDialog strengthenAlert;
					private AlertDialog recommendAlert;
					
					@Override
					public boolean onItemLongClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
						position = arg2;
						if(ListData.get(arg2).get("name")!=null)
						{
							String title="";
							if(isSelectCategory==0)
							{
								title=ListData.get(position).get("name");
							}
							else if(isSelectCategory==1)
							{
								title=specificData.get(position).get("name");
							}
							
							AlertDialog.Builder builder = new AlertDialog.Builder(Activity_ReciteMain.this,AlertDialog.THEME_HOLO_LIGHT);  
							//AlertDialog strengthenDialog=new AlertDialog.Builder(Activity_ReciteMain.this,).create();
							builder.setTitle(title);
							builder.setItems(new String[] { "复制", "分享"}, new DialogInterface.OnClickListener() {
								
								@Override
								public void onClick(DialogInterface dialog, int which) {
									if(which==0)
									{
										if(isSelectCategory==0)
										{
											copyAllSection(ListData,position);
										}
										else if(isSelectCategory==1)
										{
											copyAllSection(specificData,position);
										}
										
										recommendAlert.dismiss();
									}
									else if(which==1)
									{
										if(isSelectCategory==0)
										{
											share(getShareString(ListData,position));
										}
										else if(isSelectCategory==1)
										{
											share(getShareString(specificData,position));
										}
										
										recommendAlert.dismiss();
									}
									
									
								}
							});
							
							recommendAlert = builder.create();
							recommendAlert.show();
						}
						
						return false;
					}
				});

				break;
			default:
				break;
			}

			return listViews.get(arg1);
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == (arg1);
		}

		@Override
		public void restoreState(Parcelable arg0, ClassLoader arg1) {
		}

		@Override
		public Parcelable saveState() {
			return null;
		}

		@Override
		public void startUpdate(View arg0) {
		}
	}

	/**
	 * 头标点击监听
	 */
	public class MyOnClickListener implements View.OnClickListener {
		private int index = 0;

		public MyOnClickListener(int i) {
			index = i;
		}

		@Override
		public void onClick(View v) {
			mPager.setCurrentItem(index);
		}
	};

	/**
	 * 页卡切换监听
	 */
	public class MyOnPageChangeListener implements OnPageChangeListener {

		// int one = offset * 2 + bmpW;// 页卡1 -> 页卡2 偏移量

		@Override
		public void onPageSelected(int arg0) {
			Animation animation = null;
			switch (arg0) {
			case 0:
				if (currIndex == 1) {
					t2.setTextColor(Color.BLACK);
					t1.setBackgroundDrawable(getResources().getDrawable(R.drawable.rounded_recite_vptitle));
					t1.setTextColor(Color.WHITE);
					t2.setBackgroundColor(Color.TRANSPARENT);
				}
				if (currIndex == 2) {
					t3.setTextColor(Color.BLACK);
					t1.setBackgroundDrawable(getResources().getDrawable(R.drawable.rounded_recite_vptitle));
					t1.setTextColor(Color.WHITE);
					t3.setBackgroundColor(Color.TRANSPARENT);
				}
				if (currIndex == 3) {
					t4.setTextColor(Color.BLACK);
					t1.setBackgroundDrawable(getResources().getDrawable(R.drawable.rounded_recite_vptitle));
					t1.setTextColor(Color.WHITE);
					t4.setBackgroundColor(Color.TRANSPARENT);
				}
				break;
			case 1:
				if (currIndex == 0) {
					t1.setTextColor(Color.BLACK);
					t2.setBackgroundDrawable(getResources().getDrawable(R.drawable.rounded_recite_vptitle));
					t2.setTextColor(Color.WHITE);
					t1.setBackgroundColor(Color.TRANSPARENT);
				}
				if (currIndex == 2) {
					t3.setTextColor(Color.BLACK);
					t2.setBackgroundDrawable(getResources().getDrawable(R.drawable.rounded_recite_vptitle));
					t2.setTextColor(Color.WHITE);
					t3.setBackgroundColor(Color.TRANSPARENT);
				}
				if (currIndex == 3) {
					t4.setTextColor(Color.BLACK);
					t2.setBackgroundDrawable(getResources().getDrawable(R.drawable.rounded_recite_vptitle));
					t2.setTextColor(Color.WHITE);
					t4.setBackgroundColor(Color.TRANSPARENT);
				}
				break;
			case 2:
				if (currIndex == 0) {
					t1.setTextColor(Color.BLACK);
					t3.setBackgroundDrawable(getResources().getDrawable(R.drawable.rounded_recite_vptitle));
					t3.setTextColor(Color.WHITE);
					t1.setBackgroundColor(Color.TRANSPARENT);
				}
				if (currIndex == 3) {
					t4.setTextColor(Color.BLACK);
					t3.setBackgroundDrawable(getResources().getDrawable(R.drawable.rounded_recite_vptitle));
					t3.setTextColor(Color.WHITE);
					t4.setBackgroundColor(Color.TRANSPARENT);
				}
				if (currIndex == 1) {
					t2.setTextColor(Color.BLACK);
					t3.setBackgroundDrawable(getResources().getDrawable(R.drawable.rounded_recite_vptitle));
					t3.setTextColor(Color.WHITE);
					t2.setBackgroundColor(Color.TRANSPARENT);
				}
				break;
			case 3:
				if (currIndex == 0) {
					t1.setTextColor(Color.BLACK);
					t4.setBackgroundDrawable(getResources().getDrawable(R.drawable.rounded_recite_vptitle));
					t4.setTextColor(Color.WHITE);
					t1.setBackgroundColor(Color.TRANSPARENT);
				}
				if (currIndex == 2) {
					t3.setTextColor(Color.BLACK);
					t4.setBackgroundDrawable(getResources().getDrawable(R.drawable.rounded_recite_vptitle));
					t4.setTextColor(Color.WHITE);
					t3.setBackgroundColor(Color.TRANSPARENT);
				}
				if (currIndex == 1) {
					t2.setTextColor(Color.BLACK);
					t4.setBackgroundDrawable(getResources().getDrawable(R.drawable.rounded_recite_vptitle));
					t4.setTextColor(Color.WHITE);
					t2.setBackgroundColor(Color.TRANSPARENT);
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

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.title_bar_menu_btn:
			if (slideMenu.isMainScreenShowing()) {
				slideMenu.openMenu();
			} else {
				slideMenu.closeMenu();
			}
			break;
		}
	}

	

	public void test(View view) {
		Intent intent = new Intent(Activity_ReciteMain.this, Activity_BibleTestMain.class);
		startActivity(intent);
		finish();
		overridePendingTransition(R.anim.zoomin, R.anim.zoomout);
	}

	private void loadReciteBible() {
		DatabaseHelper dbHelper = new DatabaseHelper(Activity_ReciteMain.this,"bible.db");
		SQLiteDatabase db = dbHelper.getReadableDatabase();
		ReciteData = new ArrayList<Map<String, String>>();
		Cursor cursorTemp1 = db.rawQuery(
				"select distinct secontFlag,id, bookName,chapterIndex,secontFlag,sectionText,sectionIndex from holybible where secontFlag in (1,2,3)", null);
		Log.i("cursorTemp1", cursorTemp1.getCount() + "");
		while (cursorTemp1.moveToNext()) {
			int secontFlag = cursorTemp1.getInt(cursorTemp1.getColumnIndex("secontFlag"));
			int id = cursorTemp1.getInt(cursorTemp1.getColumnIndex("id"));
			int sectionIndex = cursorTemp1.getInt(cursorTemp1.getColumnIndex("sectionIndex"));
			int chapterIndex = cursorTemp1.getInt(cursorTemp1.getColumnIndex("chapterIndex"));
			String sectionText = cursorTemp1.getString(cursorTemp1.getColumnIndex("sectionText"));
			String bookName = cursorTemp1.getString(cursorTemp1.getColumnIndex("bookName"));

			Map<String, String> map = new HashMap<String, String>();
			map.put("id", id + "");
			map.put("sectionIndex", sectionIndex + "");
			map.put("chapterIndex", chapterIndex + "");
			map.put("sectionText", sectionText);
			map.put("bookName", bookName);
			map.put("secontFlag", secontFlag + "");
			ReciteData.add(map);
			Log.i("ReciteData", sectionText);

		}
		cursorTemp1.close();
	}

	private void loadRecommendBible() {
		DatabaseHelper dbHelper = new DatabaseHelper(Activity_ReciteMain.this,"bible.db");
		SQLiteDatabase db = dbHelper.getReadableDatabase();
		Cursor cursor = db.rawQuery("select title,name,content from recomendbible", null);
		RecommendData = new ArrayList<Map<String, String>>();
		while (cursor.moveToNext()) {
			String recommendTitle = cursor.getString(cursor.getColumnIndex("title"));
			String recommendName = cursor.getString(cursor.getColumnIndex("name"));
			String recommendContent = cursor.getString(cursor.getColumnIndex("content"));
			Log.i("content", recommendContent);
			Map<String, String> map = new HashMap<String, String>();
			map.put("title", recommendTitle);
			map.put("name", recommendName);
			map.put("content", recommendContent);
			RecommendData.add(map);
			// TitlrData.add(map);
		}
		cursor.close();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			Timer tExit = new Timer();
			TimerTask task = new TimerTask() {
				@Override
				public void run() {
					isExit = false;
					hasTask = false;
				}
			};

			if (isExit == false) {
				isExit = true;
				Toast.makeText(getApplicationContext(), "再按一次后退键退出应用程序", Toast.LENGTH_SHORT).show();
				if (!hasTask) {
					tExit.schedule(task, 2000);
				}
			} else {

				System.exit(0);
				finish();

			}
		}

		return true;
	}

	private void createListData() {
		for (int i = 0; i < RecommendData.size() - 1; i++) {
			if (i == 0) {
				Map<String, String> map = new HashMap<String, String>();
				map.put("title", RecommendData.get(i).get("title"));
				ListData.add(map);
				ListData.add(RecommendData.get(i));
			} 
			else if(i!=RecommendData.size() - 2){
				if (RecommendData.get(i).get("title").equals(RecommendData.get(i + 1).get("title"))) {
					ListData.add(RecommendData.get(i));
				} else {
					ListData.add(RecommendData.get(i));
					Map<String, String> map = new HashMap<String, String>();
					map.put("title", RecommendData.get(i + 1).get("title"));
					ListData.add(map);
				}
			}

		}
		ListData.add(RecommendData.get(RecommendData.size() - 1));

	}

	private void createTitleData() {
		Map<String, String> map=new HashMap<String, String>();
		map.put("title", "全部");
		TitlrData.add(map);
		for (int i = 0; i < ListData.size(); i++) {
			if (ListData.get(i).get("name") == null) {
				TitlrData.add(ListData.get(i));
			}
		}
	}

	private void createStrengthData() {
		for (int i = 0; i < ReciteData.size(); i++) {
			if (Integer.parseInt(ReciteData.get(i).get("secontFlag")) == 2) {
				StrengthData.add(ReciteData.get(i));
			}
		}
		// Log.i("createStrengthData", StrengthData.size()+"");
	}

	private void createFluentData() {
		for (int i = 0; i < ReciteData.size(); i++) {
			if (Integer.parseInt(ReciteData.get(i).get("secontFlag")) == 3) {
				FluentData.add(ReciteData.get(i));
			}
		}
		// Log.i("createStrengthData", StrengthData.size()+"");
	}

	class RecommendAdapter extends BaseAdapter {

		public List<Map<String, String>> data;
		private TextView title_tv;

		public RecommendAdapter(List<Map<String, String>> data) {
			this.data = data;
		}

		@Override
		public int getCount() {
			Log.i("getCount", data.size() + "");
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
		public View getView(int position, View convertView, ViewGroup parent) {

			/*
			 * View view =null; //ViewHolder holder=null; if(convertView!=null)
			 * { Log.i("有缓存", "有缓存"); view=convertView;
			 * //holder=(ViewHolder)view.getTag(); } else { Log.i("无缓存", "无缓存");
			 * view = LinearLayout.inflate(getApplicationContext(),
			 * R.layout.listitem_recomend, null); //holder=new ViewHolder();
			 * 
			 * //view.setTag(holder); }
			 */
			convertView = LinearLayout.inflate(getApplicationContext(), R.layout.listitem_recomend, null);
			title_tv = (TextView) convertView.findViewById(R.id.title);

			if (data.get(position).get("name") == null) {
				title_tv.setTextColor(getResources().getColor(R.color.titleBar));
				title_tv.setTextSize(22);
				title_tv.setHeight(80);
				title_tv.setBackgroundColor(getResources().getColor(R.color.listview_title));
				title_tv.setText(data.get(position).get("title"));
			} else {
				title_tv.setText(data.get(position).get("name") + "\n" + data.get(position).get("content"));
			}

			return convertView;
		}

	}

	static class ViewHolder {
		TextView title_tv;
	}

	public void addToStrengthen(final int potition) {
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				ContentValues values = new ContentValues();
				DatabaseHelper dbHelper = new DatabaseHelper(getApplicationContext(),"bible.db");
				SQLiteDatabase db = dbHelper.getWritableDatabase();
				values.put("secontFlag", 2);
				Log.i("id", ReciteData.get(potition).get("id") + "");
				db.update("holybible", values, "id=?", new String[] { ReciteData.get(potition).get("id") + "" });
				
			}
		}).start();
		
	}

	public void addTofluent(final int potition) {
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				ContentValues values = new ContentValues();
				DatabaseHelper dbHelper = new DatabaseHelper(getApplicationContext(),"bible.db");
				SQLiteDatabase db = dbHelper.getWritableDatabase();
				values.put("secontFlag", 3);
				Log.i("id", ReciteData.get(potition).get("id") + "");
				db.update("holybible", values, "id=?", new String[] { ReciteData.get(potition).get("id") + "" });
				
			}
		}).start();
		
	}

	private void copyAllSection(List<Map<String, String>> Data,int position) {
		ClipboardManager cmb = (ClipboardManager) getSystemService(Activity_ReciteMain.this.CLIPBOARD_SERVICE);
		
		String copyString="";
		if(Data.equals(ListData))
		{
			copyString=ListData.get(position).get("content")+" ["+ListData.get(position).get("name")+"]\r\n#圣经流利说#";
		}
		else if(Data.equals(specificData))
		{
			copyString=specificData.get(position).get("content")+" ["+specificData.get(position).get("name")+"]\r\n#圣经流利说#";
		}
		else
		{
			copyString=Data.get(position).get("sectionText") + " [" + Data.get(position).get("bookName") + " "
					+ Data.get(position).get("chapterIndex") + ":" + Data.get(position).get("sectionIndex") + "]\r\n#圣经流利说#";
		}
		
		ClipData clip = ClipData.newPlainText("text",copyString );
		cmb.setPrimaryClip(clip);
		// Toast.makeText(getApplicationContext(), "已复制",
		// Toast.LENGTH_SHORT).show();
	}
	

	public void deleteItem(List<Map<String, String>> Data,int type,int potition) {
		ContentValues values = new ContentValues();
		DatabaseHelper dbHelper = new DatabaseHelper(getApplicationContext(),"bible.db");
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		values.put("secontFlag", type);
		//Log.i("id", ReciteData.get(potition).get("id") + "");
		db.update("holybible", values, "id=?", new String[] { Data.get(potition).get("id") + "" });
		if(Data.equals(ReciteData))
		{
			for(int i=0;i<StrengthData.size();i++)
			{
				if(ReciteData.get(potition).get("id").equals(StrengthData.get(i).get("id")))
				{
					StrengthData.remove(i);
					strengthAdapter.notifyDataSetChanged();
				}
			}
			for(int i=0;i<FluentData.size();i++)
			{
				if(ReciteData.get(potition).get("id").equals(FluentData.get(i).get("id")))
				{
					FluentData.remove(i);
					fluentAdapter.notifyDataSetChanged();
				}
			}
		}
	
	
	}

	private void share(String shareString) {
		umShare.share("圣经流利说",shareString, "http://weibo.com/775747758", Activity_ReciteMain.this);
	}
	public String getShareString(List<Map<String, String>> Data,int position)
	{
		String shareString="";
		if(Data.equals(ListData))
		{
			shareString=ListData.get(position).get("content")+" ["+ListData.get(position).get("name")+"]\r\n#圣经流利说#";
		}
		else if(Data.equals(specificData))
		{
			shareString=specificData.get(position).get("content")+" ["+specificData.get(position).get("name")+"]\r\n#圣经流利说#";
		}
		else
		{
			shareString=Data.get(position).get("sectionText") + " [" + Data.get(position).get("bookName") + " "
					+ Data.get(position).get("chapterIndex") + ":" + Data.get(position).get("sectionIndex") + "]\r\n#圣经流利说#";
		}
		
		return shareString;
	}
	
	public void back(View view) {
		Intent intent = new Intent(Activity_ReciteMain.this, MainActivity.class);
		intent.putExtra("from",0);
		startActivity(intent);
		finish();
		overridePendingTransition(R.anim.zoomin, R.anim.zoomout);
	}

	
	public void updateView()
	{
		strengthAdapter.notifyDataSetChanged();
	}
}
