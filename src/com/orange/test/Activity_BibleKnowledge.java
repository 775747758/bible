package com.orange.test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;

import com.example.acts.DatabaseHelper;
import com.example.acts.R;
import com.orange.test.Activity_BibleReview.MyThread;
import com.todddavies.components.progressbar.ProgressWheel;

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
import android.widget.ProgressBar;
import android.widget.TextView;

public class Activity_BibleKnowledge extends Activity {

	
	private boolean isCompletion=false;
	private int isCorrectComp = -1;
	private int FINISH = 101;
	private int ONEFINISH = 100;
	private int COMPLETION = 345;
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
	private ArrayList<String> wrongData=new ArrayList<String>();
	private List<Map<String, String>> bibleTestData = new ArrayList<Map<String, String>>();
	private List<Map<String, String>> completionData = new ArrayList<Map<String, String>>();
	private myHandler myHandler;
	private boolean isRun = true;
	private int userAnswer=-1;
	private int trueOption;
	private int trueNum;
	private TextView scripture4;
	private Thread thread;
	private Handler myHandler2;
	private int curr_index = 1;
	private float progress = 100;
	private LinearLayout completion;
	private LinearLayout selection;
	private TextView completionQuestion;
	private LayoutInflater inflater;
	public int count=0;
	private Button certain;
	public String[] answerArray;
	public String question;
	public int num;
	private int time=40;
	

	// private int secProgress=10;

	// private List<Integer> is=new ArrayList<Integer>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_bibleknowledge);

		completion = (LinearLayout) findViewById(R.id.completion);
		selection = (LinearLayout) findViewById(R.id.selection);
		completionQuestion = (TextView) findViewById(R.id.completionQuestion);
		certain = (Button) findViewById(R.id.certain);

		inflater = LayoutInflater.from(this);

		pb = (ProgressWheel) findViewById(R.id.bts_menu_right);
		pb.setText(10 + "");
		pb.setTextColor(Color.WHITE);
		/*
		 * pb.setProgressDrawable(getResources().getDrawable(
		 * R.drawable.bts_progressbar));// 设置进度条的样式文件progressbar
		 * pb.setMax(100);// 设置进度条总刻度为100
		 */
		currentIndex = (TextView) findViewById(R.id.currentIndex);
		totalNum = (TextView) findViewById(R.id.totalNum);

		answer = (TextView) findViewById(R.id.answer);
		again = (TextView) findViewById(R.id.again);
		continues = (TextView) findViewById(R.id.continues);

		scripture1 = (TextView) findViewById(R.id.scripture1);
		scripture2 = (TextView) findViewById(R.id.scripture2);
		scripture3 = (TextView) findViewById(R.id.scripture3);
		scripture4 = (TextView) findViewById(R.id.scripture4);
		scripture4.setVisibility(View.GONE);
		options = new TextView[] { scripture1, scripture2, scripture3,
				scripture4 };

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
				userAnswer = 1;
				justifyAnswer();
				isRun = false;
				if(num>7)
				{
					setLimitTime();
				}
				else
				{
					progress = 100;
					pb.setText(10+"");
				}
				
				pb.setProgress(360);
				//pb.resetCount();
			}

		});
		scripture2.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				continues.setVisibility(View.VISIBLE);
				again.setVisibility(View.VISIBLE);
				userAnswer = 2;
				justifyAnswer();
				isRun = false;
				if(num>7)
				{
					setLimitTime();
				}
				else
				{
					progress = 100;
					pb.setText(10+"");
				}
				pb.setProgress(360);
			}

		});
		scripture3.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				continues.setVisibility(View.VISIBLE);
				again.setVisibility(View.VISIBLE);
				userAnswer = 3;
				justifyAnswer();
				isRun = false;
				if(num>7)
				{
					setLimitTime();
				}
				else
				{
					progress = 100;
					pb.setText(10+"");
				}
				pb.setProgress(360);
			}

		});
		scripture4.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				continues.setVisibility(View.VISIBLE);
				again.setVisibility(View.VISIBLE);
				userAnswer = 4;
				justifyAnswer();
				isRun = false;
				if(num>7)
				{
					setLimitTime();
				}
				else
				{
					progress = 100;
					pb.setText(10+"");
				}
				pb.setProgress(360);
			}

		});
	}

	private void readFromfile() {

		DatabaseHelper dbHelper = new DatabaseHelper(
				Activity_BibleKnowledge.this, "bibletest.db");
		SQLiteDatabase db = dbHelper.getReadableDatabase();
		Cursor cursor = db
				.rawQuery(
						"select isFourChoice,question,A,B,C,D,answer from selectquestion ",
						null);
		if (cursor.getCount() != 0) {
			while (cursor.moveToNext()) {
				int isFourChoice = cursor.getInt(cursor
						.getColumnIndex("isFourChoice"));
				String question = cursor.getString(cursor
						.getColumnIndex("question"));
				String choiceA = cursor.getString(cursor.getColumnIndex("A"));
				String choiceB = cursor.getString(cursor.getColumnIndex("B"));
				String choiceC = cursor.getString(cursor.getColumnIndex("C"));
				String choiceD = cursor.getString(cursor.getColumnIndex("D"));
				String answer = cursor.getString(cursor
						.getColumnIndex("answer"));
				Map<String, String> map = new HashMap<String, String>();
				map.put("question", question);
				map.put("choiceA", choiceA);
				map.put("choiceB", choiceB);
				map.put("choiceC", choiceC);
				map.put("choiceD", choiceD);
				map.put("answer", answer);
				map.put("isFourChoice", isFourChoice + "");
				bibleTestData.add(map);
			}
		}

		Cursor cursorComp = db.rawQuery(
				"select question,answer from completion ", null);
		if (cursorComp.getCount() != 0) {
			while (cursorComp.moveToNext()) {

				String question = cursorComp.getString(cursorComp
						.getColumnIndex("question"));
				String answer = cursorComp.getString(cursorComp
						.getColumnIndex("answer"));
				Map<String, String> map = new HashMap<String, String>();
				map.put("question", question);
				map.put("answer", answer);
				completionData.add(map);
			}
		}

		Log.i("completionData", completionData.size() + "");

		cursor.close();
		cursorComp.close();

	}

	class myHandler extends Handler {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			if (msg.what == DATA_FINISH) {
				thread = new Thread(new MyThread());
				thread.start();
			}
			//填空题模式
			if (isCompletion) {

				answerArray = null;
				if (completionData.get(msg.arg2).get("answer").split(",").length > 1) {
					answerArray = completionData.get(msg.arg2).get("answer")
							.split(",");
				}
				else
				{
					answerArray=new String[]{completionData.get(msg.arg2).get("answer")};
				}

				certain.setVisibility(View.VISIBLE);
				selection.setVisibility(View.GONE);
				completion.setVisibility(View.VISIBLE);

				question = completionData.get(msg.arg2).get("question");

				count = question.split("\\*").length - 1;
				Log.i("count", count+"");
				question = question.replaceAll("\\*", "________");

				
				completionQuestion.setText(question);

				if (msg.arg1 == ONEFINISH)// ||isRun==false)
				{
					certain.setVisibility(View.GONE);
					continues.setVisibility(View.VISIBLE);
					again.setVisibility(View.VISIBLE);
				}
				else {
					for (int i = 0; i < count; i++) {
						view = inflater.inflate(R.layout.cell_bibletest_dependence,
								null);
						completion.addView(view);
					}
				}
				if (msg.what == FINISH) {
					Intent intent = new Intent(Activity_BibleKnowledge.this,
							Activity_BibleTest_End.class);
					intent.putExtra("testNum", 10);
					intent.putExtra("trueNum", trueNum);
					Bundle bundle=new Bundle();
					bundle.putStringArrayList("wrongData", wrongData);
					intent.putExtras(bundle);
					startActivity(intent);
					finish();
					overridePendingTransition(R.anim.zoomin, R.anim.zoomout);
				}
				
			} 
			//选择题模式
			else {
				int temp=msg.arg2;
				chapter.setText(bibleTestData.get(temp).get("question"));

				if (msg.arg1 == ONEFINISH)// ||isRun==false)
				{
					if (userAnswer == -1) {
						wrongData.add(chapter.getText().toString().trim());
						Log.i("选择错误", wrongData.toString());
						answer.setCompoundDrawablesWithIntrinsicBounds(
								R.drawable.wronganswer, 0, 0, 0);
					}
					scripture1.setClickable(false);
					scripture2.setClickable(false);
					scripture3.setClickable(false);
					if(scripture4.getVisibility()!=View.GONE)
					{
						scripture4.setClickable(false);
					}
					continues.setVisibility(View.VISIBLE);
					again.setVisibility(View.VISIBLE);
				}
				// 第一次
				else {
					if(bibleTestData.get(temp).get("answer").endsWith("A"))
					{
						trueOption=1;
					}
					else if(bibleTestData.get(temp).get("answer").endsWith("B"))
					{
						trueOption=2;
					}
					else if(bibleTestData.get(temp).get("answer").endsWith("C"))
					{
						trueOption=3;
					}
					else if(bibleTestData.get(temp).get("answer").endsWith("D"))
					{
						trueOption=4;
					}
					scripture1.setText(bibleTestData.get(temp).get(
							"choiceA"));
					scripture2.setText(bibleTestData.get(temp).get(
							"choiceB"));
					scripture3.setText(bibleTestData.get(temp).get(
							"choiceC"));
					
					if (bibleTestData.get(temp).get("isFourChoice")
							.equals("1")) {
						Log.i("4", "有"+"  "+bibleTestData.get(temp).toString());
						scripture4.setVisibility(View.VISIBLE);
						scripture4.setText(bibleTestData.get(temp).get(
								"choiceD"));
					}
					else
					{
						scripture4.setVisibility(View.GONE);
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
			while (num <= 10) {
				try {
					int index = 0;
					if (num >= 7) {
						index = createARand(1, completionData.size() - 1,
								rand_completion);
					} else {
						index = createARand(1, bibleTestData.size() - 1,
								rand_chapter);
					}
					rand_option.add(Integer.parseInt(index + ""));
					Message message = Message.obtain();
					message.arg2 = index;
					if (num > 7) {
						// 该填空题了
						message.what = COMPLETION;
						isCompletion=true;
					}
					myHandler.sendMessage(message);

					threadin = new Thread(new Runnable() {

						@Override
						public void run() {

							int a = 0;
							if(num>7)
							{
								while (a < time*10 && isRun == true) {
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
							else
							{
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
			wrongData.add(chapter.getText().toString().trim());
			answer.setCompoundDrawablesWithIntrinsicBounds(
					R.drawable.wronganswer, 0, 0, 0);
			// answer.setText("错误");
		}
	}

	public void clear() {

		rand_option.clear();
		answer.setVisibility(View.INVISIBLE);
		isRun = true;
		userAnswer = -1;
		trueOption = -1;
		scripture4.setVisibility(View.GONE);
		//pb.resetCount();
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
			}
			else
			{
				int tempProgress=0;
				if(num>7)
				{
					Log.i("progress", progress * 360 /(time*10)+"ccc");
					pb.setProgress(Math.round(progress * 360 /(time*10)));
					progress--;
					tempProgress = (int) (progress / 10);
				}
				else
				{
					pb.setProgress(Math.round(progress * 360 / 100));
					progress--;
					tempProgress = (int) (progress / 10);
				}
				
				pb.setText(tempProgress + "");
			}
			
				
		}
	}
	public void again(View view)
	{
		Intent intent = new Intent(Activity_BibleKnowledge.this,
				Activity_BibleTestMain.class);
		startActivity(intent);
		finish();
		overridePendingTransition(R.anim.zoomin, R.anim.zoomout);
	}
	public void continues(View view) {
		if(num==10)
		{
			Intent intent = new Intent(Activity_BibleKnowledge.this,
					Activity_BibleTest_End.class);
			intent.putExtra("from", "bibleQuestion");
			intent.putExtra("testNum", 10);
			intent.putExtra("trueNum", trueNum);
			Bundle bundle=new Bundle();
			bundle.putStringArrayList("wrongData", wrongData);
			intent.putExtras(bundle);
			startActivity(intent);
			finish();
			overridePendingTransition(R.anim.zoomin, R.anim.zoomout);
		}
		else
		{
			certain.setVisibility(View.GONE);
			completion.removeViews(1, count);
			if(num>6)
			{
				progress = time*10;
			}
			else
			{
				progress = 100;
			}
			
			//pb.setText(10 + "");
			//pb.resetCount();
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
			scripture4.setClickable(true);
			synchronized (thread) {
				thread.notify();
			}
		}
		
	}

	public void certain(View view) {
		isRun = false;
		view.setVisibility(View.GONE);
		continues.setVisibility(View.VISIBLE);
		again.setVisibility(View.VISIBLE);
		Log.i("again", 2+"");
		boolean isCorrect = true;
		boolean isComplet = false;
		//boolean isCorrect = true;
		for (int i = 0; i < count; i++) {
			EditText et = (EditText) completion.getChildAt(i + 1).findViewById(
					R.id.answer);
			et.setEnabled(false);
			
			if (answerArray[i].equals(et.getText().toString().trim())) {
				isComplet=true;
				et.setTextColor(Color.GREEN);
			}
			else if("".equals(et.getText().toString().trim()))
			{
				isCorrect=false;
					et.setTextColor(getResources().getColor(R.color.hinttextcolor));
					et.setText("您未填写任何内容");
				
			}
			else {
				isComplet=true;
				isCorrect = false;
				isCorrectComp = 0;
				et.setTextColor(Color.RED);
			}
		}
		// 用于判断：-1表示没有填空，0表示填错了，1表示填写正确
		if (isComplet&&isCorrect) {
			isCorrectComp = 1;
			trueNum = trueNum + 1;
		}
		if(!isCorrect)
		{
			wrongData.add(question);
			Log.i("选择错误", wrongData.toString());
			
		}
		
			
			
			
	}
	
	
	public void setLimitTime()
	{
		time=40;
		progress=10*time;
		pb.setText(time+"");
	}

}
