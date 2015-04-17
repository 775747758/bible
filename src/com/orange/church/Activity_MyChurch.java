package com.orange.church;

import www.orange.utils.MyProgressPopUpWindow;

import com.example.acts.R;
import com.orange.net.UpLoadChurchInfo;
import com.orange.net.UpLoadChurchMember;
import com.orange.net.UpLoadUserInfo;
import com.orange.user.Activity_UserInfo;
import com.orange.view.CircleImageView;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.PopupMenu.OnMenuItemClickListener;

public class Activity_MyChurch extends Activity implements OnMenuItemClickListener{
	
	private CircleImageView church_iv;
	private TextView churchName_tv;
	private LinearLayout menu_ll;
	private SharedPreferences spC;
	private SharedPreferences spU;
	private UpLoadChurchMember upload;
	private String churchName;
	private String userName;
	private UpLoadChurchInfo uploadChurch;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_mychurch);
		
		church_iv=(CircleImageView)findViewById(R.id.church_iv);
		churchName_tv=(TextView)findViewById(R.id.churchName_tv);
		menu_ll=(LinearLayout)findViewById(R.id.menu_ll);
		
		SharedPreferences sp = getSharedPreferences("ChurchInfo", Context.MODE_PRIVATE);
		String churchUri=sp.getString("churchIconUri", "");
		churchName=sp.getString("churchName", "");
		if(!churchUri.equals(""))
		{
			Bitmap bitmap=BitmapFactory.decodeFile(churchUri);
			church_iv.setImageBitmap(bitmap);
		}
		if(!churchName.equals(""))
		{
			churchName_tv.setText(churchName);
		}
	
		spC = getSharedPreferences("ChurchInfo", Context.MODE_PRIVATE);
		String pastorName=spC.getString("pastorName", "");
		spU = getSharedPreferences("UserInfo", Context.MODE_PRIVATE);
		userName=spU.getString("userName", "");
	
	}

	
	public void churchInfo(View view)
	{
		Intent intent = new Intent(Activity_MyChurch.this, Activity_ChurchInfo.class);
		intent.putExtra("from", "Activity_MyChurch");
		startActivity(intent);
		overridePendingTransition(R.anim.zoomin, R.anim.zoomout);
	}
	public void churchMember(View view)
	{
		Intent intent = new Intent(Activity_MyChurch.this, Activity_ChurchMember.class);
		startActivity(intent);
		overridePendingTransition(R.anim.zoomin, R.anim.zoomout);
	}
	public void PublishChurchNotice(View view)
	{
		Intent intent = new Intent(Activity_MyChurch.this, Activity_PublishChurchNotice.class);
		startActivity(intent);
		overridePendingTransition(R.anim.zoomin, R.anim.zoomout);
	}
	public void ChurchNotice(View view)
	{
		Intent intent = new Intent(Activity_MyChurch.this, Activity_ChurchNotice.class);
		startActivity(intent);
		overridePendingTransition(R.anim.zoomin, R.anim.zoomout);
	}
	public void ChurchTest(View view)
	{
		SharedPreferences sp = getSharedPreferences("ChurchInfo", Context.MODE_PRIVATE);
		String pastorName=sp.getString("pastorName", "");
		SharedPreferences spUser = getSharedPreferences("UserInfo", Context.MODE_PRIVATE);
		String name=spUser.getString("name", "");
		Intent intent = null;
		if(name.equals(pastorName))
		{
			intent=new Intent(Activity_MyChurch.this, Activity_AdminChurchTest.class);
		}
		else
		{
			intent=new Intent(Activity_MyChurch.this, Activity_ChurchTest.class);
		}
		
		startActivity(intent);
		overridePendingTransition(R.anim.zoomin, R.anim.zoomout);
	}
	
	public void menu_right(View view) {
		SharedPreferences spChurch = getSharedPreferences("ChurchInfo", Context.MODE_PRIVATE);
		String pastorName=spChurch.getString("pastorName", "");
		SharedPreferences spUser = getSharedPreferences("UserInfo", Context.MODE_PRIVATE);
		String name=spUser.getString("name", "");
		
		PopupMenu popup = new PopupMenu(this, menu_ll);
		popup.setOnMenuItemClickListener(this);
		MenuInflater inflater = popup.getMenuInflater();
		
		if(name.equals(pastorName))
		{
			inflater.inflate(R.menu.menu_mychurch_paster, popup.getMenu());
		}
		else
		{
			inflater.inflate(R.menu.menu_mychurch_normal, popup.getMenu());
		}
		popup.show();
	}


	@Override
	public boolean onMenuItemClick(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.leave:
			AlertDialog dialog=new MyProgressPopUpWindow(Activity_MyChurch.this,"正在加载中...").createADialog();
			upload=new UpLoadChurchMember(this, dialog);
			upload.deleteOneMember(churchName, userName, Activity_MyChurch.this);
			break;
		case R.id.depart:
			dialog=new MyProgressPopUpWindow(Activity_MyChurch.this,"正在加载中...").createADialog();
			uploadChurch=new UpLoadChurchInfo(this, dialog);
			uploadChurch.deleteChurch(churchName, spC.getString("churchIconUri", ""));
			upload=new UpLoadChurchMember(this, dialog);
			upload.deleteOneMember(churchName, userName, Activity_MyChurch.this);
			break;
		default:
			break;
		}
		return false;
	}
}
