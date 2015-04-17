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

public class ChurchTestAdapter extends BaseAdapter
{

	List<String> result=new ArrayList<String>();
	Context context;
	List<Map<String, String>> data=null;
	public ChurchTestAdapter(List<Map<String, String>> data,Context context) {
		this.data = data;
		this.context=context;
		for(int i=0;i<data.size();i++)
		{
			result.add("0");
		}
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
		Log.i("Ö´ÐÐ", "Ö´ÐÐ");
		convertView = LinearLayout.inflate(context, R.layout.listitem_churchtest, null);
		
		final ImageView button=(ImageView)convertView.findViewById(R.id.button);
		TextView content=(TextView)convertView.findViewById(R.id.content);
		content.setText(data.get(position).get("content"));
		
		if(result.get(position).endsWith("1"))
		{
			button.setImageDrawable(context.getResources().getDrawable(R.drawable.button_finish));
		}
		
		button.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(result.get(position).equals("0"))
				{
					button.setImageDrawable(context.getResources().getDrawable(R.drawable.button_finish));
					result.set(position, "1");
				}
				else
				{
					button.setImageDrawable(context.getResources().getDrawable(R.drawable.button_nofinish));
					result.set(position, "0");
				}
				
			}
		});
		/*if(data.get(position).get("isFinish").equals("yes"))
		{
			button.setImageDrawable(context.getResources().getDrawable(R.drawable.button_finish));
		}
		else
		{
			button.setImageDrawable(context.getResources().getDrawable(R.drawable.button_nofinish));
		}*/
		
		return convertView;
	}
	
	public String getMemberTest()
	{
		String temp="";
		Log.i("SHUMU", result.size()+"");
		for(int i=0;i<result.size();i++)
		{
			if(i!=result.size()-1)
			{
				temp=temp+result.get(i)+",";
			}
			else
			{
				temp=temp+result.get(i);
			}
			
		}
		Log.i("result", temp+"JIEGUO");
		return temp;
	}
	
}