package com.orange.user;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

import www.orange.utils.MyProgressPopUpWindow;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.provider.ContactsContract.CommonDataKinds.Organization;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.inputmethod.InputMethodManager;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.acts.R;
import com.orange.net.UpLoadChurchNotice;
import com.orange.net.UpLoadDeleteTalk;
import com.orange.net.UpLoadTalk;
import com.orange.read.ActivityReadMain;
import com.orange.read.Activity_search;
import com.orange.view.CircleImageView;

public class MyTalkAdapter extends BaseAdapter {
	private ArrayList<Map<String, String>> commentData;

	private Context context;
	private List<Map<String, String>> talkData = null;
	private int mYear;
	private int mMonth;
	private int mDay;
	private int mHour;
	private int mMinute;
	private RelativeLayout send_layout;
	private int clickPosition;
	private Intent intent;

	private ImageView send_iv;

	private EditText content_et;

	protected AlertDialog Alert;

	public MyTalkAdapter(List<Map<String, String>> noticeData, Context context,Intent intent) {
		this.talkData = noticeData;
		this.context = context;
		this.intent=intent;
		Log.i("talkData", talkData.toString());
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return talkData.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return talkData.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		final int mPosition = position;
		Log.i("执行", "执行");
		convertView = LinearLayout.inflate(context,
				R.layout.listitem_friendscircle, null);

		final ImageView talk_iv = (ImageView) convertView
				.findViewById(R.id.talk_iv);
		
		final ImageView delete_iv = (ImageView) convertView
				.findViewById(R.id.delete_iv);
		
		delete_iv.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				UpLoadDeleteTalk upLoadTalk = new UpLoadDeleteTalk(context);
				upLoadTalk.deleteATalk(talkData.get(position).get("UUID"));
				
				talkData.remove(position);
				notifyDataSetChanged();
				
			}
		});
		talk_iv.setVisibility(View.GONE);
		LinearLayout comment_container = (LinearLayout) convertView
				.findViewById(R.id.comment_container);
		
		
		for (int i = 0; i < commentData.size(); i++) {
			
			if (talkData.get(position).get("UUID")
					.equals(commentData.get(i).get("talkUuid"))) {
				comment_container.setVisibility(View.VISIBLE);
				LinearLayout.LayoutParams ll_params = new LinearLayout.LayoutParams(
						LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);
				LinearLayout comment = new LinearLayout(context);
				comment.setLayoutParams(ll_params);
				comment.setOrientation(LinearLayout.HORIZONTAL);

				final TextView tv_comment_name = new TextView(context);

				LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
						LinearLayout.LayoutParams.WRAP_CONTENT,
						LinearLayout.LayoutParams.WRAP_CONTENT); // , 1是可选写的
				lp.setMargins(2, 0, 0, 0);
				tv_comment_name.setLayoutParams(lp);
				tv_comment_name.setText(commentData.get(i).get("name"));
				tv_comment_name.setTextColor(Color.parseColor("#7888a9"));
				tv_comment_name.setTextSize(14);
				final String username=commentData.get(i).get("userName");
				tv_comment_name.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						Log.i("wer", talkData.get(position).get("name")+"HHH");
						Log.i("werrrrr", talkData.get(position).get("userName")+"HHH");
						intent.putExtra("userName",username);
						intent.putExtra("name",tv_comment_name.getText().toString());
						context.startActivity(intent);
						//overridePendingTransition(R.anim.zoomin, R.anim.zoomout);
						
					}
				});

				final TextView tv_comment_content = new TextView(context);
				tv_comment_content.setLayoutParams(lp);
				tv_comment_content.setText(": "+commentData.get(i).get("comment"));
				tv_comment_content.setTextColor(Color.GRAY);
				tv_comment_content.setTextSize(14);
				
				tv_comment_content.setOnLongClickListener(new OnLongClickListener() {
					
					@Override
					public boolean onLongClick(View v) {
						AlertDialog.Builder builder = new AlertDialog.Builder(context,AlertDialog.THEME_HOLO_LIGHT);  
						//AlertDialog strengthenDialog=new AlertDialog.Builder(Activity_ReciteMain.this,).create();
						builder.setTitle("");
						builder.setItems(new String[] {"复制"}, new DialogInterface.OnClickListener() {
							
							@Override
							public void onClick(DialogInterface dialog, int which) {
								if(which==0)
								{
									ClipboardManager cmb = (ClipboardManager) context.getSystemService(context.CLIPBOARD_SERVICE);
									ClipData clip = ClipData.newPlainText("text",tv_comment_content.getText().toString().substring(": ".length()) );
									cmb.setPrimaryClip(clip);
									Alert.dismiss();
								}
								
								
							}
						});
						
						Alert = builder.create();
						Alert.show();
						return true;
					}
				});

				comment.addView(tv_comment_name);
				comment.addView(tv_comment_content);

				comment_container.addView(comment);
			}
		}
		

		CircleImageView portrait = (CircleImageView) convertView
				.findViewById(R.id.protrait);
		
		portrait.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Log.i("wer", talkData.get(position).get("name")+"HHH");
				Log.i("werrrrr", talkData.get(position).get("userName")+"HHH");
				intent.putExtra("userName",talkData.get(position).get("userName"));
				intent.putExtra("name",talkData.get(position).get("name"));
				context.startActivity(intent);
				//overridePendingTransition(R.anim.zoomin, R.anim.zoomout);
				
			}
		});
		TextView name = (TextView) convertView.findViewById(R.id.name);
		name.setClickable(true);
		name.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Log.i("wer", talkData.get(position).get("name")+"HHH");
				Log.i("werrrrr", talkData.get(position).get("userName")+"HHH");
				intent.putExtra("userName",talkData.get(position).get("userName"));
				intent.putExtra("name",talkData.get(position).get("name"));
				context.startActivity(intent);
				//overridePendingTransition(R.anim.zoomin, R.anim.zoomout);
				
			}
		});
		TextView content = (TextView) convertView.findViewById(R.id.content);
		TextView city = (TextView) convertView.findViewById(R.id.city);
		TextView date = (TextView) convertView.findViewById(R.id.date);
		File file=new File(talkData.get(position).get("portraitUri"));
		if(file.exists())
		{
			Bitmap bitmap = BitmapFactory.decodeFile(talkData.get(position).get(
					"portraitUri"));
			portrait.setImageBitmap(bitmap);
		}
		
		Log.i("name", talkData.get(position).get("name") + "");
		name.setText(talkData.get(position).get("name"));
		content.setText(talkData.get(position).get("content"));
		city.setText(talkData.get(position).get("city"));

		Calendar c = Calendar.getInstance();
		mYear = c.get(Calendar.YEAR);
		mMonth = c.get(Calendar.MONTH) + 1;
		mDay = c.get(Calendar.DAY_OF_MONTH);
		mHour = c.get(Calendar.HOUR_OF_DAY);
		mMinute = c.get(Calendar.MINUTE);

		if (mYear > Integer.parseInt(talkData.get(position).get("year"))) {
			date.setText(talkData.get(position).get("year") + "年"
					+ talkData.get(position).get("month") + "月"
					+ talkData.get(position).get("day"));
		} else {
			if (mMonth > Integer.parseInt(talkData.get(position).get("month"))) {
				date.setText(talkData.get(position).get("month") + "月"
						+ talkData.get(position).get("day"));
			} else {
				int talkDate = Integer.parseInt(talkData.get(position).get(
						"day"));

				if (mDay > talkDate) {
					if (mDay - talkDate == 1) {
						date.setText("昨天");
					} else {
						date.setText((mDay - talkDate - 1) + "天前");
					}
				} else {
					int talkHour = Integer.parseInt(talkData.get(position).get(
							"hour"));
					if (mHour > talkHour) {
						date.setText((mHour - talkHour) + "小时前");
					} else {
						int talkminute = Integer.parseInt(talkData
								.get(position).get("minute"));
						if (mMinute > talkminute) {
							date.setText((mMinute - talkminute) + "分前");
						} else {
							date.setText("刚刚");
						}
					}
				}
			}

		}
		
		return convertView;
	}

	public void addCommentData(ArrayList<Map<String, String>> commentData) {
		Log.i("commentData", commentData.size() + "");
		this.commentData = commentData;
		notifyDataSetChanged();
	}
	
	public int getClickPosition()
	{
		Log.i("clickPosition", clickPosition+"");
		return clickPosition;
	}
	
	

}