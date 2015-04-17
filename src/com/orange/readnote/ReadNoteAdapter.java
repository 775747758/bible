package com.orange.readnote;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.acts.DatabaseHelper;
import com.example.acts.R;
import com.orange.view.CircleImageView;

public class ReadNoteAdapter extends BaseAdapter {
	String keyWord;
	boolean isAppear;
	Context context;
	List<Map<String, String>> userData = null;
	private List<Integer> listDelData = new ArrayList<Integer>();
	private DatabaseHelper dbHelper;
	private SQLiteDatabase db;

	public ReadNoteAdapter(List<Map<String, String>> userData, Context context,
			boolean isAppear, String keyWord) {
		this.keyWord = keyWord;
		this.isAppear = isAppear;
		this.userData = userData;
		this.context = context;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return userData.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return userData.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		convertView = LinearLayout.inflate(context, R.layout.listitem_readnote,
				null);
		TextView title = (TextView) convertView.findViewById(R.id.title);
		TextView chapter_tv = (TextView) convertView
				.findViewById(R.id.chapter_tv);
		TextView date_tv = (TextView) convertView.findViewById(R.id.date_tv);
		CheckBox checkbox = (CheckBox) convertView.findViewById(R.id.checkbox);
		checkbox.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				if (isChecked) {
					if (!listDelData.contains(position)) {
						listDelData.add(position);
						Log.i("listDelData", listDelData.toString());
					}

				} else {
					if (listDelData.contains(position)) {

						listDelData.remove(listDelData.indexOf(position));
						Log.i("listDelData", listDelData.toString());
					}
				}

			}
		});
		if (isAppear) {
			checkbox.setVisibility(View.VISIBLE);
			date_tv.setVisibility(View.INVISIBLE);
		}

		if (userData.get(position).get("title").equals("")
				|| userData.get(position).get("title") == null) {
			String content = userData.get(position).get("noteContent");
			if (!"".equals(keyWord)) {
				
				SpannableString spannableString=new SpannableString(content);
				
				spannableString.setSpan(new ForegroundColorSpan(context.getResources().getColor(R.color.keyword)), content.indexOf(keyWord),
						content.indexOf(keyWord)+ keyWord.length(),// 2+resultString.indexOf("ʾ")+1
						Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
				/*int start=0;
				 * while (true) {
					if (content.indexOf(keyWord, start) != content.lastIndexOf(keyWord)) {
						spannableString.setSpan(new ForegroundColorSpan(
								getResources().getColor(R.color.keyWord)), data
								.get(j).get("reult").indexOf(keyWord, start),
								content
										.indexOf(keyWord, start)
										+ keyWord.length(),// 2+resultString.indexOf("ʾ")+1
								Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
						start = content
								.indexOf(keyWord, start) + 1;
					} else {
						break;
					}
				}*/

				title.setText(spannableString);
			}
			else
			{
				title.setText(content);
			}

		} else {
			String content = userData.get(position).get("title");
			if (!"".equals(keyWord)) {
				
				SpannableString spannableString=new SpannableString(content);
				
				spannableString.setSpan(new ForegroundColorSpan(context.getResources().getColor(R.color.keyword)), content.indexOf(keyWord),
						content.indexOf(keyWord)+ keyWord.length(),// 2+resultString.indexOf("ʾ")+1
						Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
				

				title.setText(spannableString);
			}
			else
			{
				title.setText(content);
			}
		}

		final Calendar c = Calendar.getInstance();
		int mYear = c.get(Calendar.YEAR);
		String[] dates = userData.get(position).get("date").split("/");
		Log.i("dates", dates.length + "gg");
		if (Integer.parseInt(dates[0]) == mYear) {
			date_tv.setText(userData
					.get(position)
					.get("date")
					.substring(
							userData.get(position).get("date").indexOf("/") + 1));
		} else {
			date_tv.setText(userData.get(position).get("date"));
		}

		chapter_tv.setText(userData.get(position).get("volume") + "  "
				+ userData.get(position).get("chapter"));

		return convertView;
	}

	public void deleteItem() {
		dbHelper = new DatabaseHelper(context, "bible.db");
		db = dbHelper.getReadableDatabase();
		Collections.sort(listDelData);
		Log.i("listDelData", listDelData.toString() + "kk");
		for (int i = listDelData.size() - 1; i >= 0; i--) {
			db.delete("note", "chapterIndexInBook=?",
					new String[] { userData.get(listDelData.get(i).intValue())
							.get("chapterIndexInBook") });
			userData.remove(listDelData.get(i).intValue());
			listDelData.remove(i);

		}
		db.close();
		notifyDataSetChanged();
	}

}