package com.orange.recite;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.example.acts.R;
import com.example.acts.R.color;
import com.example.acts.R.id;
import com.example.acts.R.layout;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap.Config;
import android.graphics.Color;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class MyAllSectionAdapter extends BaseAdapter {
	Map<String, String> volumeMap;

	private Context context;
	private List<Map<String, String>> dataList;
	private float downX; // ����ʱ���ȡ��x����
	private float upX; // ��ָ�뿪ʱ���x����
	private Button button; // ����ִ��ɾ����button
	private Animation animation; // ɾ��ʱ��Ķ���

	public MyAllSectionAdapter(Context context, List<Map<String, String>> itemMap) {
		this.context = context;
		this.dataList = itemMap;
		animation = AnimationUtils.loadAnimation(context, R.anim.push_out); // ��xml��ȡһ������
	}

	@Override
	public int getCount() {
		return dataList.size();
	}

	@Override
	public Object getItem(int position) {
		return dataList.get(position);
	}

	@Override
	public long getItemId(int itemId) {
		return itemId;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parentView) {
		ViewHolder holder = null;

			holder = new ViewHolder();
			convertView = LinearLayout.inflate(context, R.layout.listitem_allscriptures, null);
			holder.chapter = (TextView) convertView.findViewById(R.id.chapter);
			holder.content = (TextView) convertView.findViewById(R.id.content);
		if(position%2==0)
		{
			convertView.setBackgroundColor(Color.WHITE);
			//holder.chapter.setBackgroundColor(Color.WHITE);
			//holder.content.setBackgroundColor(Color.WHITE);
		}
		else
		{
			convertView.setBackgroundColor(android.graphics.Color.parseColor("#F6EBE9"));
			//holder.chapter.setBackgroundColor(android.graphics.Color.parseColor("#F6EBE9"));
			//holder.content.setBackgroundColor(android.graphics.Color.parseColor("#F6EBE9"));
		}
		holder.chapter.setText(dataList.get(position).get("bookName") + " " + dataList.get(position).get("chapterIndex") + ":"
				+ dataList.get(position).get("sectionIndex"));
		holder.content.setText(dataList.get(position).get("sectionText"));
		
		return convertView;

	}
	

	/**
	 * ����ListViewҪ��ȡ�Ŀؼ�
	 */
	class ViewHolder {
		TextView chapter;
		TextView content;
	}
}