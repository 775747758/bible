package com.orange.church;

import www.orange.utils.MyProgressPopUpWindow;

import com.example.acts.MainActivity;
import com.example.acts.R;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnLastItemVisibleListener;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.orange.map.Activity_MapMain;
import com.orange.net.UpLoadChurchNotice;
import com.orange.test.Activity_BibleTestMain;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

public class Activity_ChurchNotice extends Activity{
	private PullToRefreshListView notices;
	private ProgressDialog reDialog;
	private AlertDialog dialog;
	private UpLoadChurchNotice upLoadChurchNotice;
	private SharedPreferences sp;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_churchnotice);
		
		notices=(PullToRefreshListView)findViewById(R.id.notices);
		notices.setOnRefreshListener(new OnRefreshListener<ListView>() {
			@Override
			public void onRefresh(PullToRefreshBase<ListView> refreshView) {
				upLoadChurchNotice.getChurchNotice(notices, sp.getString("churchName", ""));
			}
		});
		notices.setOnLastItemVisibleListener(new OnLastItemVisibleListener() {

			@Override
			public void onLastItemVisible() {
				Toast.makeText(Activity_ChurchNotice.this, "已经是最后一个了！", Toast.LENGTH_SHORT).show();
			}
		});
		dialog=new MyProgressPopUpWindow(Activity_ChurchNotice.this,"正在查询中...").createADialog();
		upLoadChurchNotice=new UpLoadChurchNotice(this, dialog);
		sp = getSharedPreferences("ChurchInfo", Context.MODE_PRIVATE);
		upLoadChurchNotice.getChurchNotice(notices, sp.getString("churchName", ""));
	}
	
	public void back(View view) {
		Intent intent = new Intent(Activity_ChurchNotice.this, MainActivity.class);
		intent.putExtra("from",0);
		startActivity(intent);
		finish();
		overridePendingTransition(R.anim.zoomin, R.anim.zoomout);
	}

}
