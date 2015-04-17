package com.orange.church;

import java.util.ArrayList;
import java.util.Map;

import com.example.acts.MainActivity;
import com.example.acts.R;
import com.orange.net.UpLoadChurchNotice;
import com.orange.net.UpLoadChurchTest;
import com.orange.recite.Activity_ReciteMain;
import com.orange.test.Activity_BibleTestMain;
import com.orange.user.Activity_UserInfo;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnLongClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

public class Activity_AdminChurchTest extends Activity {

	private ListView members;
	private ProgressDialog reDialog;
	private ArrayList<Map<String, String>> testMember;
	private AdminChurchTestAdapter adapter;
	private TextView notest_tv;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_adminchurchtest);

		members = (ListView) findViewById(R.id.members);
		notest_tv=(TextView) findViewById(R.id.notest_tv);

		reDialog = new ProgressDialog(this);
		reDialog.setMessage("正在查询中...");
		reDialog.show();
		UpLoadChurchTest upLoadChurchTest = new UpLoadChurchTest(this, reDialog);
		SharedPreferences sp = getSharedPreferences("ChurchInfo", Context.MODE_PRIVATE);
		testMember = new ArrayList<Map<String, String>>();
		adapter = new AdminChurchTestAdapter(testMember, this);
		upLoadChurchTest.getChurchMember(notest_tv,members, sp.getString("churchName", ""), testMember, adapter);

		members.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				TextView name = (TextView) arg1.findViewById(R.id.name_tv);
				TextView result = (TextView) arg1.findViewById(R.id.result_tv);
				Intent intent = new Intent(Activity_AdminChurchTest.this, Activity_AdminTestResult.class);
				intent.putExtra("result", result.getText().toString());
				intent.putExtra("name", name.getText().toString());
				startActivity(intent);
				overridePendingTransition(R.anim.zoomin, R.anim.zoomout);

			}
		});

		members.setOnItemLongClickListener(new OnItemLongClickListener() {

			private AlertDialog recommendAlert;

			@Override
			public boolean onItemLongClick(AdapterView<?> arg0, View arg1, final int arg2, long arg3) {
				final TextView name = (TextView) arg1.findViewById(R.id.name_tv);
				final TextView result = (TextView) arg1.findViewById(R.id.result_tv);
				final TextView date = (TextView) arg1.findViewById(R.id.date_tv);
				AlertDialog.Builder builder = new AlertDialog.Builder(Activity_AdminChurchTest.this, AlertDialog.THEME_HOLO_LIGHT);
				// AlertDialog strengthenDialog=new
				// AlertDialog.Builder(Activity_ReciteMain.this,).create();
				builder.setItems(new String[] { "删除" }, new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {

						testMember.remove(arg2);
						adapter.notifyDataSetChanged();

						ProgressDialog reDialog = new ProgressDialog(Activity_AdminChurchTest.this);
						reDialog.setMessage("正在查询中...");
						UpLoadChurchTest upLoadChurchTest = new UpLoadChurchTest(getApplicationContext(), reDialog);

						SharedPreferences sp = getSharedPreferences("ChurchInfo", Context.MODE_PRIVATE);
						upLoadChurchTest.deleteData(sp.getString("churchName", ""), result.getText().toString(), name.getText().toString(), date.getText()
								.toString());
						
					}
				});
				recommendAlert = builder.create();
				recommendAlert.show();
				return true;

			}
		});
	}
	
	public void back(View view) {
		Intent intent = new Intent(Activity_AdminChurchTest.this, Activity_MyChurch.class);
		startActivity(intent);
		finish();
		overridePendingTransition(R.anim.zoomin, R.anim.zoomout);
	}

}
