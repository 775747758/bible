package com.orange.user;

import java.util.ArrayList;
import java.util.List;

import www.orange.utils.MyProgressPopUpWindow;

import com.example.acts.MainActivity;
import com.example.acts.R;
import com.orange.church.Activity_ChurchMember;
import com.orange.login.Activity_Login;
import com.orange.net.UpLoadChurchInfo;
import com.orange.net.UpLoadChurchMember;
import com.orange.net.UpLoadToten;
import com.orange.net.UpLoadUserInfo;
import com.orange.test.Activity_BibleTestMain;
import com.orange.view.CircleImageView;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.PopupMenu.OnMenuItemClickListener;
import android.widget.TextView;

public class Activity_UserInfo extends Activity implements OnMenuItemClickListener{
	
	private CircleImageView portrait;
	private TextView name_tv;
	private TextView age_tv;
	private TextView location_tv;
	private Button agree_bt;
	private ProgressDialog reDialog;
	private ImageView gender_iv;
	private String userName;
	private UpLoadUserInfo upload;
	private TextView portraitUti;
	private String from;
	private LinearLayout menu_ll;
	private String remoteName;
	private TextView qq_tv;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_userinfo);
		
		
		
		Intent intent=getIntent();
		userName=intent.getStringExtra("userName");
		remoteName=intent.getStringExtra("name");
		//Log.i("测验Usename", userName);
		from=intent.getStringExtra("from");
		agree_bt=(Button)findViewById(R.id.agree_bt);
		
		menu_ll=(LinearLayout)findViewById(R.id.menu_ll);
		
		portrait=(CircleImageView)findViewById(R.id.portrait);
		name_tv=(TextView)findViewById(R.id.name_tv);
		age_tv=(TextView)findViewById(R.id.age_tv);
		location_tv=(TextView)findViewById(R.id.location_tv);
		gender_iv=(ImageView)findViewById(R.id.gender_iv);
		portraitUti=new TextView(this);
		qq_tv=(TextView)findViewById(R.id.qq_tv);
		List<View> views=new ArrayList<View>();
		views.add(portrait);
		views.add(name_tv);
		views.add(age_tv);
		views.add(location_tv);
		views.add(gender_iv);
		views.add(portraitUti);
		views.add(qq_tv);
		SharedPreferences sp = getSharedPreferences("ChurchInfo", Context.MODE_PRIVATE);
		String pastorName=sp.getString("pastorName", "");
		SharedPreferences spUser = getSharedPreferences("UserInfo", Context.MODE_PRIVATE);
		String nativeName=spUser.getString("name", "");
		
		
		if(from.equals("Activity_ChurchMember")||from.equals("Activity_FriendsCircle"))
		{
			if(nativeName.equals(pastorName))
			{
				menu_ll.setVisibility(View.VISIBLE);
			}
			if(nativeName.equals(remoteName))
			{
				menu_ll.setVisibility(View.INVISIBLE);
			}
			
		}
		else
		{
			agree_bt.setVisibility(View.VISIBLE);
		}
		
		
		AlertDialog dialog=new MyProgressPopUpWindow(Activity_UserInfo.this,"正在加载中...").createADialog();
		upload=new UpLoadUserInfo(this, dialog);
		upload.getAUser(userName, views);
	}
	
	public void agree(View view)
	{
		AlertDialog dialog1=new MyProgressPopUpWindow(Activity_UserInfo.this,"正在提交中...").createADialog();
		UpLoadToten uploadToten=new UpLoadToten(getApplicationContext(), dialog1);
		SharedPreferences sp = getSharedPreferences("ChurchInfo", Context.MODE_PRIVATE);
		uploadToten.updateData(sp.getString("churchName", ""), userName);
		UpLoadChurchMember upLoadChurchMember=new UpLoadChurchMember(getApplicationContext(), dialog1);
		upLoadChurchMember.addMember(sp.getString("churchName", ""), userName,name_tv.getText().toString(),portraitUti.getText().toString(),"Activity_UserInfo");
	}
	
	public void menu_right(View view) {
		
		PopupMenu popup = new PopupMenu(this, menu_ll);
		popup.setOnMenuItemClickListener(this);
		MenuInflater inflater = popup.getMenuInflater();
		inflater.inflate(R.menu.menu_userinfo, popup.getMenu());
		popup.show();
	}
	
	

	@Override
	public boolean onMenuItemClick(MenuItem item) {
		Log.i("哈哈哈哈进入", "哈哈哈哈进入");
		switch (item.getItemId()) {
		case R.id.delete:
			Log.i("哈哈", "哈哈");
			SharedPreferences sp = getSharedPreferences("ChurchInfo", Context.MODE_PRIVATE);
			reDialog = new ProgressDialog(getApplicationContext());
			reDialog.setMessage("正在查询中...");
			UpLoadChurchMember upLoadChurchMember=new UpLoadChurchMember(getApplicationContext(), reDialog);
			Intent intent = new Intent(this, Activity_ChurchMember.class);
			upLoadChurchMember.deleteData(sp.getString("churchName", ""), userName,intent);
			break;

		default:
			break;
		}
		return true;
	}
	
	public void back(View view) {
		Intent intent = new Intent(Activity_UserInfo.this, MainActivity.class);
		intent.putExtra("from",0);
		startActivity(intent);
		finish();
		overridePendingTransition(R.anim.zoomin, R.anim.zoomout);
	}

}
