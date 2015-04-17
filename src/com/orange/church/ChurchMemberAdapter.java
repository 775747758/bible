package com.orange.church;

import java.util.List;
import java.util.Map;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.acts.R;
import com.orange.view.CircleImageView;

public class ChurchMemberAdapter extends BaseAdapter
{

	Context context;
	List<Map<String, String>> userData=null;
	public ChurchMemberAdapter(List<Map<String, String>> userData,Context context) {
		this.userData = userData;
		this.context=context;
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
		Log.i("Ö´ÐÐ", "Ö´ÐÐ");
		convertView = LinearLayout.inflate(context, R.layout.listitem_churchmember, null);
		
		CircleImageView portrait=(CircleImageView)convertView.findViewById(R.id.portrait);
		TextView name=(TextView)convertView.findViewById(R.id.name);
		TextView username=(TextView)convertView.findViewById(R.id.username);
		TextView minister=(TextView)convertView.findViewById(R.id.minister);
		Bitmap bitmap=BitmapFactory.decodeFile(userData.get(position).get("portraitUri"));
		portrait.setImageBitmap(bitmap);
		
		SharedPreferences spChurch = context.getSharedPreferences("ChurchInfo", Context.MODE_PRIVATE);
		String pastorName=spChurch.getString("pastorName", "");
		
		if(pastorName.equals(userData.get(position).get("name")))
		{
			minister.setText("ÄÁÕß");
		}
		name.setText(userData.get(position).get("name"));
		username.setText(userData.get(position).get("username"));
		return convertView;
	}
	
}