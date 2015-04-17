package com.orange.readnote;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.example.acts.DatabaseHelper;
import com.example.acts.MainActivity;
import com.example.acts.R;
import com.orange.read.Activity_SelectVolume;
import com.orange.read.Activity_SelectVolume_Sec;
import com.orange.test.Activity_BibleTestMain;
import com.orange.view.ElasticListView;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class Activity_ReadNote extends Activity {

	private ElasticListView notes;
	private DatabaseHelper dbHelper;
	private SQLiteDatabase db;
	private List<Map<String, String>> noteData = new ArrayList<Map<String, String>>();
	private List<Map<String, String>> searchData = new ArrayList<Map<String, String>>();
	private TranslateAnimation animationout;
	private TranslateAnimation animationin;
	private RelativeLayout delete_rl;
	private List<Integer> listDelData=new ArrayList<Integer>();
	public ReadNoteAdapter readNoteAdapter;
	private EditText search_et;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_readnote);

		delete_rl=(RelativeLayout)findViewById(R.id.delete_rl);
		notes = (ElasticListView) findViewById(R.id.notes);
		search_et=(EditText)findViewById(R.id.search_et);
		search_et.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void afterTextChanged(Editable s) {
				
				search(s.toString());
			}
		});
		new MyAsyncTask().execute();
		
		animationin = new TranslateAnimation(0, 0,
				100, 0);
		animationin.setDuration(200);
		animationin.setFillAfter(true);
		
		animationout = new TranslateAnimation(0, 0,
				0, 100);
		animationout.setDuration(200);
		animationout.setFillAfter(true);
	}

	public void getNote() {
		// 初始化数据库
		dbHelper = new DatabaseHelper(getApplicationContext(), "bible.db");
		db = dbHelper.getReadableDatabase();
		Cursor cursorTemp = db
				.rawQuery(
						"select chapterIndexInBook, title,volume,chapter,noteContent,date from note ",
						null);
		while (cursorTemp.moveToNext()) {
			Map<String, String> map = new HashMap<String, String>();
			String title = cursorTemp.getString(cursorTemp
					.getColumnIndex("title"));
			String volume = cursorTemp.getString(cursorTemp
					.getColumnIndex("volume"));
			String chapter = cursorTemp.getString(cursorTemp
					.getColumnIndex("chapter"));
			String noteContent = cursorTemp.getString(cursorTemp
					.getColumnIndex("noteContent"));
			String date = cursorTemp.getString(cursorTemp
					.getColumnIndex("date"));
			int chapterIndexInBook = cursorTemp.getInt(cursorTemp
					.getColumnIndex("chapterIndexInBook"));
			map.put("volume", volume);
			map.put("chapter", chapter);
			map.put("noteContent", noteContent);
			map.put("date", date);
			map.put("title", title);
			map.put("chapterIndexInBook", chapterIndexInBook+"");
			noteData.add(map);
		}
		cursorTemp.close();
		db.close();
	}

	class MyAsyncTask extends
			AsyncTask<Void, Void, Void> {

		
		@Override
		protected Void doInBackground(Void... params) {
			getNote();
			return null;
		}
		
		@Override
		protected void onPostExecute(Void result) {
			readNoteAdapter=new ReadNoteAdapter(noteData, getApplicationContext(),false,"");
			notes.setAdapter(readNoteAdapter);
			notes.setOnItemLongClickListener(new OnItemLongClickListener() {

				@Override
				public boolean onItemLongClick(AdapterView<?> parent,
						View view, int position, long id) {
					CheckBox checkbox=(CheckBox)view.findViewById(R.id.checkbox);
					checkbox.setChecked(true);
					readNoteAdapter=new ReadNoteAdapter(noteData, getApplicationContext(),true,"");
					notes.setAdapter(readNoteAdapter);
					delete_rl.setVisibility(View.VISIBLE);
					delete_rl.setAnimation(animationin);
					return false;
				}
			});
			notes.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
					Intent intent = new Intent(Activity_ReadNote.this, Activity_EditNote.class);
					intent.putExtra("volume", noteData.get(position).get("volume"));
					intent.putExtra("chapter", noteData.get(position).get("chapter"));
					intent.putExtra("noteContent", noteData.get(position).get("noteContent"));
					intent.putExtra("date", noteData.get(position).get("date"));
					intent.putExtra("title", noteData.get(position).get("title"));
					intent.putExtra("chapterIndexInBook", noteData.get(position).get("chapterIndexInBook"));
					startActivity(intent);
					overridePendingTransition(R.anim.zoomin, R.anim.zoomout);
					
				}
			});
			super.onPostExecute(result);
		}
		
	}
	
	public void delete(View view)
	{
		readNoteAdapter.deleteItem();
		readNoteAdapter=new ReadNoteAdapter(noteData, getApplicationContext(),false,"");
		notes.setAdapter(readNoteAdapter);
		delete_rl.setAnimation(animationout);
		delete_rl.setVisibility(View.GONE);
	}
	
	public void search(String keyWord)
	{
		searchData.clear();
		for(int i=0;i<noteData.size();i++)
		{
			String title=noteData.get(i).get("title");
			String noteContent=noteData.get(i).get("noteContent");
			if(title.contains(keyWord)||noteContent.contains(keyWord))
			{
				searchData.add(noteData.get(i));
			}
		}
		readNoteAdapter=new ReadNoteAdapter(searchData, getApplicationContext(),false,keyWord);
		notes.setAdapter(readNoteAdapter);
	}
	
	public void back(View view) {
		Intent intent = new Intent(Activity_ReadNote.this, MainActivity.class);
		intent.putExtra("from",0);
		startActivity(intent);
		finish();
		overridePendingTransition(R.anim.zoomin, R.anim.zoomout);
	}

}
