package com.orange.user;

import www.orange.utils.MyProgressPopUpWindow;

import com.example.acts.MainActivity;
import com.example.acts.R;
import com.orange.friendscircle.Activity_FriendsCircle;
import com.orange.friendscircle.Activity_PublishTalk;
import com.orange.net.UpLoadTalk;
import com.orange.test.Activity_BibleTestMain;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.AdapterView.OnItemLongClickListener;

public class Activity_MyTalk extends Activity {
	
	private ListView mytalk_list;
	private AlertDialog dialog;
	private UpLoadTalk upLoadTalk;
	private Intent intent;
	private TextView notalk_tv;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_mytalk);
		
		mytalk_list=(ListView)findViewById(R.id.mytalk_list);
		
		notalk_tv=(TextView)findViewById(R.id.notalk_tv);
		
		mytalk_list.setOnItemLongClickListener(new OnItemLongClickListener() {

			private AlertDialog Alert;

			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view,
					int position, long id) {
				final TextView content = (TextView) view.findViewById(R.id.content);
				AlertDialog.Builder builder = new AlertDialog.Builder(Activity_MyTalk.this,AlertDialog.THEME_HOLO_LIGHT);  
				//AlertDialog strengthenDialog=new AlertDialog.Builder(Activity_ReciteMain.this,).create();
				builder.setTitle("");
				builder.setItems(new String[] {"复制"}, new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						if(which==0)
						{
							ClipboardManager cmb = (ClipboardManager) getSystemService(Activity_MyTalk.this.CLIPBOARD_SERVICE);
							ClipData clip = ClipData.newPlainText("text",content.getText().toString() );
							cmb.setPrimaryClip(clip);
							Alert.dismiss();
						}
						
						
					}
				});
				
				Alert = builder.create();
				Alert.show();
				return false;
			}
		});
		
		SharedPreferences spUser = getSharedPreferences("UserInfo",
				Context.MODE_PRIVATE);
		
		dialog = new MyProgressPopUpWindow(
				Activity_MyTalk.this, "正在加载中...").createADialog();
		
		intent=new Intent(Activity_MyTalk.this,Activity_UserInfo.class);
		intent.putExtra("from", "Activity_MyTalk");
		upLoadTalk = new UpLoadTalk(this, dialog, new RelativeLayout(this),intent);
		upLoadTalk.getOwnTalk(mytalk_list, spUser.getString("userName", ""),notalk_tv);
	}
	
	public void publicTalk(View view)
	{
		Intent intent = new Intent(Activity_MyTalk.this,
				Activity_PublishTalk.class);
		intent.putExtra("from", "Personal_Activity");
		startActivity(intent);
		finish();
		overridePendingTransition(R.anim.zoomin, R.anim.zoomout);
	}
	public void back(View view) {
		Intent intent = new Intent(Activity_MyTalk.this, MainActivity.class);
		intent.putExtra("from",2);
		startActivity(intent);
		finish();
		overridePendingTransition(R.anim.zoomin, R.anim.zoomout);
	}

}
