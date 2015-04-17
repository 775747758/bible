package com.orange.church;

import java.util.List;
import java.util.Map;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
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
import com.orange.net.UpLoadChurchNotice;
import com.orange.view.CircleImageView;

public class ChurchNoticeAdapter extends BaseAdapter
{

	Context context;
	List<Map<String, String>> noticeData=null;
	public ChurchNoticeAdapter(List<Map<String, String>> noticeData,Context context) {
		this.noticeData = noticeData;
		this.context=context;
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return noticeData.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return noticeData.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		final int mPosition=position;
		Log.i("执行", "执行");
		convertView = LinearLayout.inflate(context, R.layout.listitem_churchnotice, null);
		
		ImageView delete_iv=(ImageView)convertView.findViewById(R.id.delete_iv);
		
		SharedPreferences sp = context.getSharedPreferences("ChurchInfo", Context.MODE_PRIVATE);
		String pastorName=sp.getString("pastorName", "");
		SharedPreferences spUser = context.getSharedPreferences("UserInfo", Context.MODE_PRIVATE);
		String name=spUser.getString("name", "");
		if(name.equals(pastorName))
		{
			delete_iv.setVisibility(View.VISIBLE);
		}
		
		
		TextView date=(TextView)convertView.findViewById(R.id.date);
		final TextView content=(TextView)convertView.findViewById(R.id.content);
		delete_iv.setOnClickListener(new OnClickListener() {
			
			private ProgressDialog reDialog;

			@Override
			public void onClick(View v) {
				reDialog = new ProgressDialog(context);
				reDialog.setMessage("正在查询中...");
				UpLoadChurchNotice upLoadChurchNotice=new UpLoadChurchNotice(context, reDialog);
				SharedPreferences sp = context.getSharedPreferences("ChurchInfo", Context.MODE_PRIVATE);
				upLoadChurchNotice.deleteData(sp.getString("churchName", ""), content.getText().toString().trim());
				noticeData.remove(position);
				notifyDataSetChanged();
			}
		});
		
		
		date.setText(noticeData.get(position).get("date"));
		content.setText(noticeData.get(position).get("content"));
		
		return convertView;
	}
	
}