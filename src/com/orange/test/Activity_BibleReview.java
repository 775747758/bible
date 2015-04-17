package com.orange.test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.acts.DatabaseHelper;
import com.example.acts.R;
import com.todddavies.components.progressbar.ProgressWheel;

public class Activity_BibleReview extends Activity {

	
	private int COUNT_OK=103;
	private boolean isReverseQuestion = false;
	private int isCorrectComp = -1;
	private int FINISH = 101;
	private int ONEFINISH = 100;
	private int REVERSE_QUESTION = 345;
	private View view;
	private ArrayList<Integer> rand_option = new ArrayList<Integer>();
	private ArrayList<Integer> rand_chapter = new ArrayList<Integer>();
	private ArrayList<Integer> rand_completion = new ArrayList<Integer>();
	private int DATA_FINISH = 10000;
	private ProgressWheel pb;
	private TextView currentIndex;
	private TextView totalNum;
	private TextView answer;
	private TextView again;
	private TextView continues;
	private TextView scripture1;
	private TextView scripture2;
	private TextView scripture3;
	private TextView[] options;
	private TextView chapter;
	private ArrayList<String> wrongData = new ArrayList<String>();
	private ArrayList<String> wrongDataIndex = new ArrayList<String>();
	//private List<Map<String, String>> wrongDataIndex = new ArrayList<Map<String, String>>();
	private List<Map<String, String>> bibleTestData = new ArrayList<Map<String, String>>();
	private myHandler myHandler;
	private boolean isRun = true;
	private int userAnswer = -1;
	private int trueOption=-1;
	private int trueNum;
	private TextView scripture4;
	private Thread thread;
	private Handler myHandler2;
	private int curr_index = 1;
	private float progress = 100;
	private Button certain;
	public String[] answerArray;
	public String question;
	public int num;
	private String rangeString;
	private int sectionCount;
	public int index;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_biblereview);

		Intent intent = getIntent();
		rangeString = intent.getStringExtra("range");

		certain = (Button) findViewById(R.id.certain);
		pb = (ProgressWheel) findViewById(R.id.bts_menu_right);
		pb.setText(10 + "");
		pb.setTextColor(Color.WHITE);

		currentIndex = (TextView) findViewById(R.id.currentIndex);
		totalNum = (TextView) findViewById(R.id.totalNum);

		answer = (TextView) findViewById(R.id.answer);
		again = (TextView) findViewById(R.id.again);
		continues = (TextView) findViewById(R.id.continues);

		scripture1 = (TextView) findViewById(R.id.scripture1);
		scripture2 = (TextView) findViewById(R.id.scripture2);
		scripture3 = (TextView) findViewById(R.id.scripture3);

		options = new TextView[] { scripture1, scripture2, scripture3};

		chapter = (TextView) findViewById(R.id.chapter);

		totalNum.setText("/" + 10);
		currentIndex.setText(curr_index + "");

		myHandler = new myHandler();
		myHandler2 = new MyHandler2();

		new Thread(new Runnable() {

			@Override
			public void run() {
				readFromfile();
				Message msg = Message.obtain();
				msg.what = DATA_FINISH;
				myHandler.sendMessage(msg);
			}
		}).start();

		scripture1.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				continues.setVisibility(View.VISIBLE);
				again.setVisibility(View.VISIBLE);
				userAnswer = 0;
				justifyAnswer();
				isRun = false;
				if (num > sectionCount) {
					progress = 100;
					pb.setText(20 + "");
				} else {
					progress = 100;
					pb.setText(10 + "");
				}

				pb.setProgress(360);
				// pb.resetCount();
			}

		});
		scripture2.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				continues.setVisibility(View.VISIBLE);
				again.setVisibility(View.VISIBLE);
				userAnswer = 1;
				justifyAnswer();
				isRun = false;
				if (num > sectionCount) {
					progress = 100;
					pb.setText(20 + "");
				} else {
					progress = 100;
					pb.setText(10 + "");
				}
				pb.setProgress(360);
			}

		});
		scripture3.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				continues.setVisibility(View.VISIBLE);
				again.setVisibility(View.VISIBLE);
				userAnswer = 2;
				justifyAnswer();
				isRun = false;
				if (num > sectionCount) {
					progress = 100;
					pb.setText(20 + "");
				} else {
					progress = 100;
					pb.setText(10 + "");
				}
				pb.setProgress(360);
			}

		});

	}

	private void readFromfile() {

		DatabaseHelper dbHelper = new DatabaseHelper(Activity_BibleReview.this,
				"bible.db");
		SQLiteDatabase db = dbHelper.getReadableDatabase();
		String sql = "";
		if (rangeString.equals("all")) {
			sql = "select distinct secontFlag,id, bookName,chapterIndex,secontFlag,sectionText,sectionIndex from holybible where secontFlag in (1,2,3)";
		} else if (rangeString.equals("fluent")) {
			sql = "select distinct secontFlag,id, bookName,chapterIndex,secontFlag,sectionText,sectionIndex from holybible where secontFlag=3";
		} else if (rangeString.equals("strengthen")) {
			sql = "select distinct secontFlag,id, bookName,chapterIndex,secontFlag,sectionText,sectionIndex from holybible where secontFlag=2";
		} else if (rangeString.equals("recommend")) {
			sql = "select title,name,content from recomendbible";
		}
		final Cursor cursor = db.rawQuery(sql, null);
		
				if(rangeString.equals("recommend"))
				{
					sectionCount =10;
				}
				else
				{
					sectionCount = cursor.getCount();
				}
				
		
		
		if (rangeString.equals("recommend")) {
			while (cursor.moveToNext()) {
				String recommendTitle = cursor.getString(cursor
						.getColumnIndex("title"));
				String recommendName = cursor.getString(cursor
						.getColumnIndex("name"));
				String recommendContent = cursor.getString(cursor
						.getColumnIndex("content"));
				Log.i("content", recommendContent);
				Map<String, String> map = new HashMap<String, String>();
				map.put("title", recommendTitle);
				map.put("name", recommendName);
				map.put("content", recommendContent);
				bibleTestData.add(map);
			}
		} else {
			while (cursor.moveToNext()) {
				int secontFlag = cursor.getInt(cursor
						.getColumnIndex("secontFlag"));
				int id = cursor.getInt(cursor.getColumnIndex("id"));
				int sectionIndex = cursor.getInt(cursor
						.getColumnIndex("sectionIndex"));
				int chapterIndex = cursor.getInt(cursor
						.getColumnIndex("chapterIndex"));
				String sectionText = cursor.getString(cursor
						.getColumnIndex("sectionText"));
				String bookName = cursor.getString(cursor
						.getColumnIndex("bookName"));

				Map<String, String> map = new HashMap<String, String>();
				map.put("id", id + "");
				map.put("sectionIndex", sectionIndex + "");
				map.put("chapterIndex", chapterIndex + "");
				map.put("sectionText", sectionText);
				map.put("bookName", bookName);
				map.put("secontFlag", secontFlag + "");
				bibleTestData.add(map);
				Log.i("ReciteData", sectionText);

			}
		}

		cursor.close();

	}

	class myHandler extends Handler {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
		
				
			
			if (msg.what == DATA_FINISH) {
				totalNum.setText("/" + 2*sectionCount);
				thread = new Thread(new MyThread());
				thread.start();
				
			}
			//考经节
			if (isReverseQuestion) {
				if(rangeString.equals("recommend"))
				{
					chapter.setText( bibleTestData.get(msg.arg2).get("content"));
				}
				else
				{
					chapter.setText( bibleTestData.get(msg.arg2).get("sectionText"));
				}
				/*chapter.setText( bibleTestData.get(msg.arg2).get("content") + " "
						+ bibleTestData.get(msg.arg2).get("chapterIndex") + ":"
						+ bibleTestData.get(msg.arg2).get("sectionIndex"));*/

				if (msg.arg1 == ONEFINISH)// ||isRun==false)
				{
					if (userAnswer == -1) {
						answer.setCompoundDrawablesWithIntrinsicBounds(
								R.drawable.wronganswer, 0, 0, 0);
					}
					options[trueOption].setTextColor(getResources().getColor(R.color.goodPink));
					scripture1.setClickable(false);
					scripture2.setClickable(false);
					scripture3.setClickable(false);
					continues.setVisibility(View.VISIBLE);
					again.setVisibility(View.VISIBLE);
				}
				// 第一次
				else {
					Random rnd = new Random();
					int number = rnd.nextInt(3);
					int option = number;
					trueOption = option;
					for (int i = 0; i < 3; i++) {
						if (i == option) {
							if(rangeString.equals("recommend"))
							{
								options[option].setText(bibleTestData.get(msg.arg2).get("name"));
							}
							else
							{
								options[option].setText(bibleTestData.get(msg.arg2).get("bookName") + " "
										+ bibleTestData.get(msg.arg2).get("chapterIndex") + ":"
										+ bibleTestData.get(msg.arg2).get("sectionIndex"));
							}
							
						} else // if(msg.arg1!=-1)
						{
							int tempChoice = createARand(0, sectionCount-1, rand_completion);
							if(rangeString.equals("recommend"))
							{
								options[i].setText(bibleTestData.get(tempChoice).get("name"));
							}
							else
							{
								options[i].setText(bibleTestData.get(tempChoice).get("bookName") + " "
										+ bibleTestData.get(tempChoice).get("chapterIndex") + ":"
										+ bibleTestData.get(tempChoice).get("sectionIndex"));
							}
						}
					}
					
				}
			}
			// 考经文
			else {
				if(rangeString.equals("recommend"))
				{
					chapter.setText(bibleTestData.get(msg.arg2).get("name"));
				}
				else
				{
					chapter.setText(bibleTestData.get(msg.arg2).get("bookName") + " "
							+ bibleTestData.get(msg.arg2).get("chapterIndex") + ":"
							+ bibleTestData.get(msg.arg2).get("sectionIndex"));
				}

				if (msg.arg1 == ONEFINISH)// ||isRun==false)
				{
					if (userAnswer == -1) {
						if(!rangeString.equals("recommend"))
						{
							wrongData.add(bibleTestData.get(msg.arg2).get("bookName") + " "
									+ bibleTestData.get(msg.arg2).get("chapterIndex") + ":"
									+ bibleTestData.get(msg.arg2).get("sectionIndex"));
							wrongDataIndex.add(bibleTestData.get(msg.arg2).get("id"));
						}
						//wrongData.add(chapter.getText().toString().trim());
						answer.setCompoundDrawablesWithIntrinsicBounds(
								R.drawable.wronganswer, 0, 0, 0);
					}
					options[trueOption].setTextColor(getResources().getColor(R.color.goodPink));
					scripture1.setClickable(false);
					scripture2.setClickable(false);
					scripture3.setClickable(false);
					continues.setVisibility(View.VISIBLE);
					again.setVisibility(View.VISIBLE);
				}
				// 第一次
				else {
					Random rnd = new Random();
					int number = rnd.nextInt(3);
					int option = number;
					trueOption = option;
					for (int i = 0; i < 3; i++) {
						if (i == option) {
							if(rangeString.equals("recommend"))
							{
								options[option].setText(bibleTestData.get(msg.arg2).get("content"));
							}
							else
							{
								options[option].setText(bibleTestData.get(msg.arg2).get("sectionText"));
							}
							
						} else // if(msg.arg1!=-1)
						{
							int tempChoice = createARand(0, sectionCount-1, rand_chapter);
							if(rangeString.equals("recommend"))
							{
								options[i].setText(bibleTestData.get(tempChoice).get("content"));
							}
							else
							{
								options[i].setText(bibleTestData.get(tempChoice).get("sectionText"));
							}
						}
					}
					
				}
			}

		}
	}

	public class MyThread implements Runnable {
		private Thread threadin;

		@Override
		public void run() {
			num = 1;
			while (num <= 2 * sectionCount) {
				try {
					index = 0;
					if (num > sectionCount) {
						index = createARand(0, bibleTestData.size() - 1,
								rand_completion);
					} else {
						
						index = createARand(0, bibleTestData.size() - 1,
								rand_chapter);
					}
					//rand_option.add(Integer.parseInt(index + ""));
					Message message = Message.obtain();
					message.arg2 = index;
					if (num > sectionCount) {
						// 该填空题了
						message.what = REVERSE_QUESTION;
						isReverseQuestion = true;
					}
					myHandler.sendMessage(message);

					threadin = new Thread(new Runnable() {

						@Override
						public void run() {

							int a = 0;
							if (num > sectionCount) {
								while (a < 200 && isRun == true) {
									try {

										Thread.sleep(100);

									} catch (InterruptedException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}
									a++;
									Message msg = Message.obtain();
									Log.i("a", a + "");
									msg.arg1 = a;
									myHandler2.sendMessage(msg);
								}
							} else {
								while (a < 100 && isRun == true) {
									try {

										Thread.sleep(100);

									} catch (InterruptedException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}
									a++;
									Message msg = Message.obtain();
									Log.i("a", a + "");
									msg.arg1 = a;
									myHandler2.sendMessage(msg);
								}
							}

							Message m = Message.obtain();
							m.what = ONEFINISH;
							myHandler2.sendMessage(m);
						}
					});

					threadin.start();

					synchronized (Thread.currentThread()) {
						Thread.currentThread().wait();
					}

					Message message2 = Message.obtain();
					message2.arg1 = ONEFINISH;
					message2.arg2 = index;
					myHandler.sendMessage(message2);
					synchronized (Thread.currentThread()) {
						Thread.currentThread().wait();
					}
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				num++;
			}
			Message message3 = Message.obtain();
			message3.what = FINISH;
			myHandler.sendMessage(message3);
		}

	}

	public int createARand(int begin, int end, ArrayList<Integer> randoms) {
		boolean isExist = false;
		Random rnd = new Random();
		int number = begin + rnd.nextInt(end - begin + 1);
		Iterator<Integer> iterator = randoms.iterator();
		while (iterator.hasNext()) {
			if (number == iterator.next().intValue()) {
				isExist = true;
			}
		}
		if (!isExist) {
			randoms.add(Integer.parseInt(number + ""));
			return number;
		} else {
			return createARand(begin, end, randoms);
		}

	}

	public void justifyAnswer() {
		answer.setVisibility(View.VISIBLE);
		if (userAnswer != -1 && trueOption != -1 && trueOption == userAnswer) {
			trueNum = trueNum + 1;
			answer.setCompoundDrawablesWithIntrinsicBounds(
					R.drawable.trueanswer, 0, 0, 0);
			// answer.setText("正确");
		} else {
			if(!rangeString.equals("recommend"))
			{
				String chapter=bibleTestData.get(index).get("bookName") + " "
						+ bibleTestData.get(index).get("chapterIndex") + ":"
						+ bibleTestData.get(index).get("sectionIndex")+"\r\n"+bibleTestData.get(index).get("sectionText");
				if(!wrongData.contains(chapter))
				{
					wrongData.add(chapter);
					wrongDataIndex.add(bibleTestData.get(index).get("id"));
				}
				
			}
			else
			{
				wrongData.add(bibleTestData.get(index).get("name")+"\r\n"+bibleTestData.get(index).get("content"));
			}
			//wrongData.add(chapter.getText().toString().trim());
			answer.setCompoundDrawablesWithIntrinsicBounds(
					R.drawable.wronganswer, 0, 0, 0);
			// answer.setText("错误");
		}
	}

	public void clear() {

		rand_completion.clear();
		rand_chapter.clear();
		rand_option.clear();
		answer.setVisibility(View.INVISIBLE);
		isRun = true;
		userAnswer = -1;
		trueOption = -1;
		// pb.resetCount();
	}

	public class MyHandler2 extends Handler {
		@Override
		public void handleMessage(Message msg) {
			// if(progress/36==)
			// TODO Auto-generated method stub
			super.handleMessage(msg);

			if (msg.what == ONEFINISH) {
				synchronized (thread) {
					thread.notify();
				}
				pb.setProgress(0);
				pb.setText(0 + "");
			} else {
				int tempProgress = 0;
				if (num > sectionCount) {
					pb.setProgress(Math.round(progress * 360 / 100));
					progress--;
					tempProgress = (int) (progress / 10);
				} else {
					pb.setProgress(Math.round(progress * 360 / 100));
					progress--;
					tempProgress = (int) (progress / 10);
				}

				pb.setText(tempProgress + "");
			}

		}
	}

	public void again(View view) {
		Intent intent = new Intent(Activity_BibleReview.this,
				Activity_BibleTestMain.class);
		startActivity(intent);
		finish();
		overridePendingTransition(R.anim.zoomin, R.anim.zoomout);
	}

	public void continues(View view) {
		if (num == 2*sectionCount) {
			Intent intent = new Intent(Activity_BibleReview.this,
					Activity_BibleTest_End.class);
			intent.putExtra("from", rangeString);
			if(rangeString.equals("recommend"))
			{
				intent.putExtra("testNum", 20);
			}
			else
			{
				intent.putExtra("testNum", 2*sectionCount);
			}
			
			intent.putExtra("trueNum", trueNum);
			Bundle bundle = new Bundle();
			bundle.putStringArrayList("wrongData", wrongData);
			bundle.putStringArrayList("wrongDataIndex", wrongDataIndex);
			intent.putExtras(bundle);
			startActivity(intent);
			finish();
			overridePendingTransition(R.anim.zoomin, R.anim.zoomout);
		} else {
			//certain.setVisibility(View.GONE);
			if (num > 2*sectionCount-1) {
				progress = 100;
			} else {
				progress = 100;
			}

			curr_index++;
			currentIndex.setText(curr_index + "");
			clear();
			for (int i = 0; i < 3; i++) {
				options[i].setTextColor(Color.BLACK);
			}

			continues.setVisibility(View.INVISIBLE);
			again.setVisibility(View.INVISIBLE);
			scripture1.setClickable(true);
			scripture2.setClickable(true);
			scripture3.setClickable(true);

			synchronized (thread) {
				thread.notify();
			}
		}

	}

	

}
