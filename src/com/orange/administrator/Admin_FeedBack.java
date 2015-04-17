package com.orange.administrator;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.baidu.android.feedback.FeedbackListener;
import com.baidu.android.feedback.FeedbackManager;
import com.baidu.android.feedback.message.FBMessage;
import com.example.acts.R;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.ListView;
import android.widget.SimpleAdapter;

public class Admin_FeedBack extends Activity{
	
	private ListView list;
	private FeedbackManager fm;
	 public static final String API_KEY = "GdZNITcYcI3bhiwKx7duVbR7";
	 ArrayList<Map<String,String>> mData= new ArrayList<Map<String,String>>();
	private SimpleDateFormat sdf;
	private SimpleAdapter adapter;
	private Handler handler;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.admin_feedback);
		
		initFeedback();
		
		list=(ListView)findViewById(R.id.list);
		
		
		handler=new Handler();  
		
		sdf=new SimpleDateFormat("yyyy-MM-dd");

		adapter = new SimpleAdapter(this,mData,android.R.layout.simple_list_item_2,  
		        new String[]{"date","text"},new int[]{android.R.id.text1,android.R.id.text2});
		list.setAdapter(adapter);
		
		
		fm.fetchFeedback(0, 1000, true , new FeedbackListener() {
			
			@Override
			public void onSendResult(int arg0, FBMessage arg1) {
				
				
			}
			
			@Override
			public void onFetchResult(int arg0, List arg1, boolean arg2) {
				
				Log.i("arg1", arg1.size()+"ddd");
				
				for(Object message:arg1)
				{
					FBMessage me=(FBMessage)message;
					Map<String,String> item = new HashMap<String,String>();  
			        item.put("date",sdf.format(new Date(Long.parseLong(me.getTime()+""))));  
			        item.put("text", me.getMsgBody());  
			        mData.add(item); 
			        Log.i("q111", me.getMsgBody());
				}
				handler.post(runnableUi);   
				
			}
		});
		Log.i("mData", mData.size()+"kkk");
		
		
	}

	 private void initFeedback() {
	        fm = FeedbackManager.getInstance(this);
	        
	        fm.register(API_KEY);
	    }
	 
	 Runnable   runnableUi=new  Runnable(){  
	        @Override  
	        public void run() {  
	            //更新界面  
	           adapter.notifyDataSetChanged();
	        }  
	          
	    };  
}
