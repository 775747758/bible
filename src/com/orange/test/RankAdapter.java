package com.orange.test;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import javax.xml.validation.Validator;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.acts.R;
import com.orange.view.CircleImageView;

public class RankAdapter extends BaseAdapter {

	Context context;
	List<Map<String, String>> userData = null;

	public RankAdapter(List<Map<String, String>> userData, Context context) {
		this.userData = sortData(userData);
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
	public View getView(int position, View convertView, ViewGroup parent) {
		convertView = LinearLayout.inflate(context,
				R.layout.listitem_bibletestrank, null);

		CircleImageView portrait = (CircleImageView) convertView
				.findViewById(R.id.portrait);
		TextView name = (TextView) convertView.findViewById(R.id.name_tv);
		TextView userName = (TextView) convertView.findViewById(R.id.username);
		TextView grade_tv = (TextView) convertView.findViewById(R.id.grade_tv);
		TextView rank_tv = (TextView) convertView.findViewById(R.id.rank_tv);
		/*Bitmap bitmap = BitmapFactory.decodeFile(userData.get(position).get(
				"portraitUri"));
		portrait.setImageBitmap(bitmap);*/

		userName.setText(userData.get(position).get("userName"));
		name.setText(userData.get(position).get("name"));
		grade_tv.setText(userData.get(position).get("grade"));
		if(position==0)
		{
			rank_tv.setText("状元"); 
			rank_tv.setTextColor(Color.parseColor("#3738D6"));
		}
		else if(position==1)
		{
			rank_tv.setText("榜眼");
			rank_tv.setTextColor(Color.parseColor("#FFAD01"));
		}
		else if(position==2)
		{
			rank_tv.setText("探花");
			rank_tv.setTextColor(Color.parseColor("#95497F"));
		}
		else
		{
			rank_tv.setText(position+1+"");
		}
		return convertView;
	}

	/*public void sort()
	{
		//userData.
		for(int i=0;i<userData.size();i++)
		{
			for(int j=0;j<userData.size();j++)
			{
				if(Integer.parseInt(userData.get(i).get("grade")))
			}
		}
	}*/
	
	// 对数据进行大小排序、大-->小
	private static List<Map<String, String>> sortData(List<Map<String, String>> list) {
		System.out.println("对WCM数据进行排序");
		if (list.size()>1) {
			Comparator<Map<String, String>> mapComprator = new Comparator<Map<String, String>>() {
				@Override
				public int compare(Map<String, String> o1,
						Map<String, String> o2) {
					// do compare.
					if (Integer.valueOf(o1.get("grade").toString()) < Integer
							.valueOf(o2.get("grade").toString())) {
						return 1;
					} else {
						return -1;
					}
				}
			};
			Collections.sort(list, mapComprator);
		} else {
			new Exception("排序没有取到数据");
		}
		return list;
	}
}