package com.orange.map;

import java.util.List;
import java.util.Map;

import android.R.integer;
import android.content.Context;
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

public class MapListAdapter extends BaseAdapter
{

	Context context;
	List<Map<String, String>> userData=null;
	public MapListAdapter(List<Map<String, String>> userData,Context context) {
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
		Log.i("执行", "执行");
		convertView = LinearLayout.inflate(context, R.layout.listitem_map, null);
		
		CircleImageView portrait=(CircleImageView)convertView.findViewById(R.id.portrait);
		TextView name=(TextView)convertView.findViewById(R.id.name);
		TextView distance=(TextView)convertView.findViewById(R.id.distance);
		//TextView username=(TextView)convertView.findViewById(R.id.username);
		if(!userData.get(position).get("portraitUri").equals(""))
		{
			Bitmap bitmap=BitmapFactory.decodeFile(userData.get(position).get("portraitUri"));
			portrait.setImageBitmap(bitmap);
		}
		
		name.setText(userData.get(position).get("name"));
		String distanceString=userData.get(position).get("distance");
		if(distanceString.equals("0.0"))
		{
			distance.setText("100米以内");
		}
		else if(Double.parseDouble(distanceString)<100)
		{
			distance.setText("100米以内");
		}
		else if(Double.parseDouble(distanceString)<200&&Double.parseDouble(distanceString)>100)
		{
			distance.setText("200米以内");
		}
		else if(Double.parseDouble(distanceString)<300&&Double.parseDouble(distanceString)>200)
		{
			distance.setText("300米以内");
		}
		else if(Double.parseDouble(distanceString)<400&&Double.parseDouble(distanceString)>300)
		{
			distance.setText("400米以内");
		}
		else if(Double.parseDouble(distanceString)<500&&Double.parseDouble(distanceString)>400)
		{
			distance.setText("500米以内");
		}
		else if(Double.parseDouble(distanceString)<600&&Double.parseDouble(distanceString)>500)
		{
			distance.setText("600米以内");
		}
		else if(Double.parseDouble(distanceString)<700&&Double.parseDouble(distanceString)>600)
		{
			distance.setText("700米以内");
		}
		else if(Double.parseDouble(distanceString)<800&&Double.parseDouble(distanceString)>700)
		{
			distance.setText("800米以内");
		}
		else if(Double.parseDouble(distanceString)<900&&Double.parseDouble(distanceString)>800)
		{
			distance.setText("900米以内");
		}
		else if(Double.parseDouble(distanceString)<1000&&Double.parseDouble(distanceString)>900)
		{
			distance.setText("1千米以内");
		}
		else if(Double.parseDouble(distanceString)<2000&&Double.parseDouble(distanceString)>1000)
		{
			distance.setText("2千米以内");
		}
		else if(Double.parseDouble(distanceString)<3000&&Double.parseDouble(distanceString)>2000)
		{
			distance.setText("3千米以内");
		}
		
		//distance.setText(userData.get(position).get("distance"));
		return convertView;
	}
	
}