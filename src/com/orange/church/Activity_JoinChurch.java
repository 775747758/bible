package com.orange.church;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.example.acts.Activity_Option;
import com.example.acts.R;
import com.orange.net.UpLoadChurchInfo;
import com.orange.read.Activity_SelectVolume;
import com.orange.view.CircleImageView;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

public class Activity_JoinChurch extends Activity{
	
	
	private List<Map<String, String>> ChurchData = new ArrayList<Map<String, String>>();
	private ListView churchs;
	private UpLoadChurchInfo upLoadChurchInfo;
	private ProgressDialog reDialog;
	private EditText churchname_et;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_joinchurch);
		
		churchs=(ListView)findViewById(R.id.churchs);
		churchname_et=(EditText)findViewById(R.id.churchname_et);
		
		reDialog = new ProgressDialog(this);
		reDialog.setMessage("正在查询中...");
		upLoadChurchInfo=new UpLoadChurchInfo(this, reDialog);
		
		churchs.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				
				TextView tv=(TextView)arg1.findViewById(R.id.name);
				Intent intent = new Intent(Activity_JoinChurch.this, Activity_ChurchInfo.class);
				intent.putExtra("from", "Activity_JoinChurch");
				intent.putExtra("churchName",tv.getText().toString());
				Log.i("churchName", tv.getText().toString());
				startActivity(intent);
				overridePendingTransition(R.anim.zoomin, R.anim.zoomout);
				
			}
		});
		
	}
	
	public void searchChurch(View view)
	{
		upLoadChurchInfo.getChurchData(churchs,churchname_et.getText().toString());
	}

}
