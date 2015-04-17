package com.orange.church;

import java.util.List;
import java.util.Map;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.acts.R;
import com.orange.view.CircleImageView;

public class ChurchAdapter extends BaseAdapter
{

	Context context;
	List<Map<String, String>> userData=null;
	public ChurchAdapter(List<Map<String, String>> userData,Context context) {
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
		convertView = LinearLayout.inflate(context, R.layout.listitem_churchmember, null);
		
		CircleImageView portrait=(CircleImageView)convertView.findViewById(R.id.portrait);
		TextView name=(TextView)convertView.findViewById(R.id.name);
		Bitmap bitmap=BitmapFactory.decodeFile(userData.get(position).get("churchIconUri"));
		portrait.setImageBitmap(bitmap);
		
		name.setText(userData.get(position).get("churchName"));
		
		return convertView;
	}
	
}