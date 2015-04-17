package com.orange.read;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.example.acts.DatabaseHelper;
import com.example.acts.MainActivity;
import com.example.acts.R;
import com.orange.recite.MyAllSectionAdapter;
import com.orange.test.Activity_BibleTestMain;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ActionBar.LayoutParams;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.Layout;
import android.text.Selection;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextWatcher;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AbsListView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.PopupWindow;
import android.widget.PopupMenu.OnMenuItemClickListener;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

@TargetApi(Build.VERSION_CODES.HONEYCOMB)
@SuppressLint("NewApi")
public class Activity_search extends Activity {

	private String KeyWord;
	private static PopupWindow popWin = null; // 弹出窗口
	private static View popView = null;
	private String rangeName = "全部";
	private View divider;
	private boolean isSpinnerExit;
	private ImageView spinner_iv;
	private TextView spinner_tv;
	private ListView range_lv;
	private String[] range;
	private ListView section_lv;
	private DatabaseHelper dbHelper;
	private SQLiteDatabase db;
	private EditText search_et;
	private int chapterIndexInBook;
	private int id;
	private int bookIndex;
	private String sectionText;
	private String bookName;
	private int isTitle;
	private int sectionIndex;
	private int chapterIndex;
	private TextView beginSearch_tv;
	private SimpleAdapter simpleAdapter;
	private int addCount = 30;
	List<Map<String, String>> data;
	private MyAdapter mAdapter;
	private TextView result;
	private SpannableString spannableString;
	private String shortBookName;
	private List<String> bookNameList = new ArrayList<String>();
	private List<String> shortBookNameList = new ArrayList<String>();

	private List<Integer> chapterIndexList = new ArrayList<Integer>();
	private List<Integer> keywordStart = new ArrayList<Integer>();
	private List<Integer> keywordEnd = new ArrayList<Integer>();
	private List<Integer> sectionIndexList = new ArrayList<Integer>();
	private List<Integer> chapterIndexInBookList = new ArrayList<Integer>();
	private AlertDialog RangeAlert;
	private Builder builder;
	private View view1;
	private int width;
	private int height;
	private LayoutInflater inflater;
	private String from;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_search);

		result = (TextView) findViewById(R.id.result);
		divider = findViewById(R.id.divider);
		spinner_iv = (ImageView) findViewById(R.id.spinner_iv);
		spinner_tv = (TextView) findViewById(R.id.spinner_tv);
		spinner_tv.setText(rangeName);
		inflater = LayoutInflater.from(getApplicationContext());
		
		
		WindowManager manager = getWindowManager();
		Display display = manager.getDefaultDisplay();
		width = display.getWidth();
		height = display.getHeight();
		
		
		
		
		
		
		/*popView = inflater.inflate(R.layout.popwindow_search, null);

		popWin = new PopupWindow(popView, 450, 600, true);
		popWin.setAnimationStyle(R.style.UpdatePopupAnimation);
		ColorDrawable dw = new ColorDrawable(0x00000000);
		popWin.setBackgroundDrawable(dw);
		popWin.setOnDismissListener(new OnDismissListener() {

			@Override
			public void onDismiss() {
				
			}
		});*/
		

		range = new String[] { "全部", "旧约", "新约", "摩西五经(创-申)", "历史书(书-斯)", "诗歌，智慧书(伯-歌)", "大先知书(赛-但)", "小先知书(何-玛)", "四福音(太-约)", "教会历史(徒)", "保罗书信(罗-门)",
				"其他书信(来-犹)", "启示录(启)" };
		
		
		Intent intent=getIntent();
		from=intent.getStringExtra("from");
		String searchContent=intent.getStringExtra("searchContent");
		search_et = (EditText) findViewById(R.id.search_et);
		search_et.setText(searchContent);
		beginSearch(search_et);
		//使光标停在末端
		CharSequence text=search_et.getText();
		if(text instanceof Spannable)
		{
			Spannable spanText=(Spannable)text;
			Selection.setSelection(spanText, text.length());
		}
		section_lv = (ListView) findViewById(R.id.section_lv);
		section_lv.setOnScrollListener(new OnScrollListener() {

			private int scrolledX;
			private int scrolledY;

			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				switch (scrollState) {
				case OnScrollListener.SCROLL_STATE_IDLE:
					int position = view.getLastVisiblePosition();
					int count = simpleAdapter.getCount();
					Log.i("position", position + "  " + count);
					if (position == count - 1) {
						if (addCount + 30 > data.size()) {
							addCount = data.size();
						}
						if (addCount == data.size()) {
							Toast.makeText(getApplicationContext(), "您搜索的内容已经显示完毕！", Toast.LENGTH_SHORT).show();
							String resultString = "共搜索到" + data.size() + "个结果，当前显示" + addCount + "条结果";
							spannableString = new SpannableString(resultString);
							spannableString.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.keyword)), 4, 4 + (data.size() + "").length(),// 2+resultString.indexOf("示")+1
									Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
							spannableString.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.keyword)), resultString.indexOf("示") + 1,
									resultString.indexOf("条"), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
							result.setText(spannableString);
							break;
						}
						scrolledX = section_lv.getScrollX();
						scrolledY = section_lv.getScrollY();
						addCount += 30;
						mAdapter.addMoreData(fillData());
						mAdapter.notifyDataSetChanged();
						section_lv.scrollTo(scrolledX, scrolledY);

						String resultString = "共搜索到" + data.size() + "个结果，当前显示" + addCount + "条结果";
						spannableString = new SpannableString(resultString);
						spannableString.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.keyword)), 4, 4 + (data.size() + "").length(),// 2+resultString.indexOf("示")+1
								Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
						spannableString.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.keyword)), resultString.indexOf("示") + 1,
								resultString.indexOf("条"), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
						result.setText(spannableString);

					}

					break;

				default:
					break;
				}

			}

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
				// TODO Auto-generated method stub

			}
		});

		section_lv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {

				Intent intent = new Intent(Activity_search.this, Activity_ReadBible2.class);
				intent.putExtra("volume", bookNameList.get(arg2));
				intent.putExtra("simpleVolume", shortBookNameList.get(arg2));
				intent.putExtra("keyword", KeyWord);
				intent.putExtra("chapter", chapterIndexList.get(arg2) + "");
				intent.putExtra("ChapterIndexInBook", chapterIndexInBookList.get(arg2));
				startActivity(intent);
				finish();
				overridePendingTransition(R.anim.zoomin, R.anim.zoomout);
			}
		});
		
		section_lv.setOnItemLongClickListener(new OnItemLongClickListener() {

			private PopupWindow copyPopWin;
			private TextView copy_tv;

			@Override
			public boolean onItemLongClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				final int posiotion=arg2;
				View alertdialog_copy=LayoutInflater.from(getApplicationContext()).inflate(R.layout.alertdialog_copy, null);
				copy_tv=(TextView)alertdialog_copy.findViewById(R.id.copy_tv);
				
				final AlertDialog menuDialog = new AlertDialog.Builder(Activity_search.this,R.style.CustomDialog).create();
				menuDialog.setView(alertdialog_copy);
				//menuDialog.setMessage("复制"); // 获取对话框当前的参数值
				menuDialog.setInverseBackgroundForced(true);
				menuDialog.show();
				/*WindowManager.LayoutParams layoutParams = menuDialog.getWindow().getAttributes();  
		        layoutParams.width =500 ;  
		        layoutParams.height = 300;  
		        menuDialog.getWindow().setAttributes(layoutParams);*/
				
				
				/*AlertDialog.Builder dialog=new AlertDialog.Builder(getApplicationContext(), AlertDialog.THEME_TRADITIONAL);
				dialog.setItems(new String[]{"复制"}, new OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						
						//Toast.makeText(getApplicationContext(), "已复制", Toast.LENGTH_SHORT).show();
					}
				});
				
				dialog.setView(alertdialog_copy);
				dialog.setTitle("123");
				dialog.show();*/
				/*View alertdialog_copy=LayoutInflater.from(getApplicationContext()).inflate(R.layout.alertdialog_copy, null);
				copyPopWin = new PopupWindow(alertdialog_copy,LayoutParams.FILL_PARENT , LayoutParams.FILL_PARENT , true);
				copyPopWin.setAnimationStyle(R.style.UpdatePopupAnimation);
				ColorDrawable dw = new ColorDrawable();
				copyPopWin.setBackgroundDrawable(dw);
				copyPopWin.showAtLocation((View)section_lv.getParent(), Gravity.CENTER, 0, 0);*/
				copy_tv.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						
						ClipboardManager cmb = (ClipboardManager)getSystemService(CLIPBOARD_SERVICE);
						//ClipData clip = ClipData.newPlainText("text",data.get(posiotion).get("section")+"  ["+shortBookNameList.get(posiotion)+" "+chapterIndexList.get(posiotion)+":"+sectionIndexList.get(posiotion)+"]"+"\n#圣经流利说#");
						ClipData clip = ClipData.newPlainText("text",data.get(posiotion).get("section")+"  ["+data.get(posiotion).get("chapter")+"]"+"\n#圣经流利说#");
						cmb.setPrimaryClip(clip);
						menuDialog.dismiss();
					}
				});
				return false;
			}
		});

	}

	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	@SuppressLint("NewApi")
	public void spinner(View view) {
		
		if(isSpinnerExit)
		{
			RangeAlert.dismiss();
		}
		else
		{
			builder = new AlertDialog.Builder(
					Activity_search.this, R.style.Dialog);
			builder.setInverseBackgroundForced(true);
			view1 = inflater.inflate(R.layout.dialog_range, null);
			RangeAlert = builder.create();
			range_lv = (ListView) view1.findViewById(R.id.range_lv);
			ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.listitem_search_range, R.id.item_tv, range);
			range_lv.setAdapter(adapter);
			range_lv.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {

					rangeName = range[arg2];
					spinner_tv.setText(rangeName);
					RangeAlert.dismiss();
				}
			});
			RangeAlert.setOnDismissListener(new OnDismissListener() {
				
				@Override
				public void onDismiss(DialogInterface dialog) {
					spinner_iv.setImageDrawable(getResources().getDrawable(R.drawable.spinner_bottom));
					spinner_tv.setTextColor(Color.BLACK);
					isSpinnerExit = false;
					
				}
			});
			RangeAlert.show();
			isSpinnerExit=true;
			RangeAlert.setContentView(view1);
			RangeAlert.getWindow().setLayout(2 * width / 3,height-200 );
		}
		
		
	}

	public List<Map<String, String>> readFromSQL(String keyword) {
		clear();
		dbHelper = new DatabaseHelper(this,"bible.db");
		db = dbHelper.getReadableDatabase();
		Cursor cursorTemp = null;
		List<Map<String, String>> searchData = new ArrayList<Map<String, String>>();
		if (rangeName.equals("全部")) {
			cursorTemp = db
					.rawQuery(
							"select chapterIndex,bookIndex,shortBookName,bookName,sectionText,isTitle,sectionIndex, chapterIndexInBook from holybible where sectionText like ? ",
							new String[] { "%" + keyword + "%" });
		} else if (rangeName.equals("旧约")) {
			cursorTemp = db
					.rawQuery(
							"select chapterIndex,bookIndex,shortBookName,bookName,sectionText,isTitle,sectionIndex, chapterIndexInBook from holybible where sectionText like ? AND isNew='0' ",
							new String[] { "%" + keyword + "%" });
		} else if (rangeName.equals("新约")) {
			cursorTemp = db
					.rawQuery(
							"select chapterIndex,bookIndex,shortBookName,bookName,sectionText,isTitle,sectionIndex, chapterIndexInBook from holybible where sectionText like ? AND isNew='1' ",
							new String[] { "%" + keyword + "%" });
		} else {
			cursorTemp = db
					.rawQuery(
							"select chapterIndex,bookIndex,shortBookName,bookName,sectionText,isTitle,sectionIndex, chapterIndexInBook from holybible where sectionText like ? AND belongTo=? ",
							new String[] { "%" + keyword + "%", rangeName });
		}
		if (cursorTemp.getCount() == 0) {
			return null;
		}

		int i = 1;
		SpannableString spanText;
		while (cursorTemp.moveToNext()) {
			String temp = null;
			String getDataString = "";
			isTitle = cursorTemp.getInt(cursorTemp.getColumnIndex("isTitle"));
			sectionIndex = cursorTemp.getInt(cursorTemp.getColumnIndex("sectionIndex"));
			chapterIndexInBook = cursorTemp.getInt(cursorTemp.getColumnIndex("chapterIndexInBook"));
			bookIndex = cursorTemp.getInt(cursorTemp.getColumnIndex("bookIndex"));
			chapterIndex = cursorTemp.getInt(cursorTemp.getColumnIndex("chapterIndex"));
			sectionText = cursorTemp.getString(cursorTemp.getColumnIndex("sectionText"));
			bookName = cursorTemp.getString(cursorTemp.getColumnIndex("bookName"));
			shortBookName = cursorTemp.getString(cursorTemp.getColumnIndex("shortBookName"));

			bookNameList.add(bookName);
			shortBookNameList.add(shortBookName);
			chapterIndexList.add(chapterIndex);
			chapterIndexInBookList.add(chapterIndexInBook);
			
			if(sectionIndex!=0)
			{
				sectionIndexList.add(sectionIndex);
			}
			
			if (isTitle == 0) {
				Map<String, String> map = new HashMap<String, String>();
				map.put("chapter", bookName + " " + chapterIndex + ":" + sectionIndex);
				map.put("section", sectionText);
				searchData.add(map);
			}
		}
		cursorTemp.close();
		return searchData;
	}

	public void beginSearch(View view) {
		
		if (search_et.getText().toString() == null || search_et.getText().toString().equals("")) {
			result.setText("请输入关键词！");
			return;
		}
		KeyWord = search_et.getText().toString();
		data = readFromSQL(KeyWord);

		if (data == null || data.size() == 0) {
			result.setText("没有您所要搜索的关键词！");
			
			return;
		}
		if (data.size() <= 30) {
			mAdapter = new MyAdapter(data);
		} else {
			mAdapter = new MyAdapter(data.subList(0, 30));
		}
		String resultString;
		section_lv.setAdapter(mAdapter);
		if(data.size()<30)
		{
			resultString = "共搜索到" + data.size() + "个结果，当前显示"+data.size()+"条结果";
		} 
		else
		{
			resultString = "共搜索到" + data.size() + "个结果，当前显示30条结果";
		}
		
		spannableString = new SpannableString(resultString);
		spannableString.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.keyword)), 4, 4 + (data.size() + "").length(),// 2+resultString.indexOf("示")+1
				Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		spannableString.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.keyword)), resultString.indexOf("示") + 1, resultString.indexOf("条"),
				Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		result.setText(spannableString);
		
		//关闭输入法
		InputMethodManager inputMethodManager = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
		inputMethodManager.hideSoftInputFromWindow(Activity_search.this.getCurrentFocus().getWindowToken()
		,InputMethodManager.HIDE_NOT_ALWAYS);

	}

	private List<Map<String, String>> fillData() {
		if(data!=null)
		{
			if(addCount>=data.size())
			{
				return data;
			}
		}
		
		return data.subList(0, addCount);
	}

	class MyAdapter extends BaseAdapter {

		public List<Map<String, String>> data;
		private TextView chapter_tv;
		private TextView section_tv;

		public MyAdapter(List<Map<String, String>> data) {
			this.data = data;
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
		public View getView(int position, View convertView, ViewGroup parent) {
			convertView = LinearLayout.inflate(getApplicationContext(), R.layout.listitem_search_section, null);
			simpleAdapter = new SimpleAdapter(getApplicationContext(), fillData(), R.layout.listitem_search_section, new String[] { "chapter", "section" },
					new int[] { R.id.chapter_tv, R.id.section_tv });
			chapter_tv = (TextView) convertView.findViewById(R.id.chapter_tv);
			chapter_tv.setText(data.get(position).get("chapter"));
			section_tv = (TextView) convertView.findViewById(R.id.section_tv);
			
			String resultString = "共搜索到" + data.size() + "个结果，当前显示" + addCount + "条结果";
			
			String itemSection = data.get(position).get("section");
			spannableString = new SpannableString(itemSection);
			spannableString.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.keyword)), itemSection.indexOf(KeyWord), KeyWord.length()
					+ itemSection.indexOf(KeyWord),// 2+resultString.indexOf("示")+1
					Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
			section_tv.setText(spannableString);
			return convertView;
		}

		public void addMoreData(List<Map<String, String>> list) {
			data = list;
			notifyDataSetChanged();
		}

	}
	
	public void back(View view) {
		
		if(from.equals("MainActivity"))
		{
			Intent intent = new Intent(Activity_search.this, MainActivity.class);
			intent.putExtra("from",0);
			startActivity(intent);
			finish();
			overridePendingTransition(R.anim.zoomin, R.anim.zoomout);
		}
		else if(from.equals("Activity_ReadBible2"))
		{
			finish();
			overridePendingTransition(R.anim.zoomin, R.anim.zoomout);
		}
		
	}
	
	public void clear()
	{
		chapterIndexInBookList.clear();
		bookNameList.clear();
		shortBookNameList.clear();
		chapterIndexList.clear();
		keywordStart.clear();
		keywordEnd.clear();
		sectionIndexList.clear();
	}
	
}
