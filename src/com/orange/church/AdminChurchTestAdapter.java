package com.orange.church;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.acts.R;
import com.orange.view.CircleImageView;

public class AdminChurchTestAdapter extends BaseAdapter
{


	Context context;
	List<Map<String, String>> data=null;
	public AdminChurchTestAdapter(List<Map<String, String>> data,Context context) {
		this.data = data;
		this.context=context;
		
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
	public View getView(final int position, View convertView, ViewGroup parent) {
		convertView = LinearLayout.inflate(context, R.layout.listitem_adminchurchmembertest, null);
		
		TextView name=(TextView)convertView.findViewById(R.id.name_tv);
		TextView date=(TextView)convertView.findViewById(R.id.date_tv);
		TextView result=(TextView)convertView.findViewById(R.id.result_tv);
		name.setText(data.get(position).get("name"));
		date.setText(data.get(position).get("date"));
		result.setText(data.get(position).get("result"));
		return convertView;
	}

}