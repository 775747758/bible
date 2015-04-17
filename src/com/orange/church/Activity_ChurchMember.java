package com.orange.church;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import www.orange.utils.MyProgressPopUpWindow;

import com.example.acts.Activity_Option;
import com.example.acts.MainActivity;
import com.example.acts.R;
import com.orange.net.UpLoadChurchMember;
import com.orange.net.UpLoadToten;
import com.orange.net.UpLoadUserInfo;
import com.orange.read.Activity_SelectVolume;
import com.orange.test.Activity_BibleTestMain;
import com.orange.user.Activity_UserInfo;
import com.orange.view.CircleImageView;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class Activity_ChurchMember extends Activity {

	private List<Map<String, String>> UserData = new ArrayList<Map<String, String>>();
	private ListView members;
	private ProgressDialog reDialog;
	private TextView all;
	private RelativeLayout titlebar;
	private TextView userName_tv;
	private LinearLayout applyLL;
	private View divider;
	private AlertDialog dialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_churchmember);
		
		applyLL=(LinearLayout)findViewById(R.id.applyLL);
		divider=findViewById(R.id.divider);
		SharedPreferences sp = getSharedPreferences("ChurchInfo", Context.MODE_PRIVATE);
		String pastorName=sp.getString("pastorName", "");
		SharedPreferences spUser = getSharedPreferences("UserInfo", Context.MODE_PRIVATE);
		String name=spUser.getString("name", "");
		if(name.equals(pastorName))
		{
			Log.i("一样", pastorName+"  "+name+"  iooooo");
			applyLL.setVisibility(View.VISIBLE);
			divider.setVisibility(View.VISIBLE);
		}
		
		titlebar = (RelativeLayout) findViewById(R.id.titlebar);
		members = (ListView) findViewById(R.id.members);
		dialog=new MyProgressPopUpWindow(Activity_ChurchMember.this,"正在查询中...").createADialog();
		UpLoadChurchMember upload = new UpLoadChurchMember(this, dialog);
		
		userName_tv=new TextView(this);
		upload.getChurchMember(members, sp.getString("churchName", ""),userName_tv);
		
		
		members.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				TextView usernametv=(TextView)arg1.findViewById(R.id.username);
				TextView tv=(TextView)arg1.findViewById(R.id.name);
				
				Intent intent = new Intent(Activity_ChurchMember.this, Activity_UserInfo.class);
				intent.putExtra("from", "Activity_ChurchMember");
				Log.i("userName", userName_tv.getText().toString());
				intent.putExtra("userName",usernametv.getText().toString());
				intent.putExtra("name",tv.getText().toString());
				startActivity(intent);
				overridePendingTransition(R.anim.zoomin, R.anim.zoomout);
				
			}
		});
	}

	/*@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		menu.add(Menu.NONE, Menu.FIRST + 1, 2, "删除").setIcon(

		android.R.drawable.ic_menu_delete);
		return true;
	}*/
	
	/*@Override
	public boolean onMenuOpened(int featureId, Menu menu) {
		setOverflowIconVisible(featureId,menu);
		return true;
	}*/

	public void seeApply(View view) {
		Intent intent = new Intent(Activity_ChurchMember.this, Activity_NewMemberApply.class);
		startActivity(intent);
		overridePendingTransition(R.anim.zoomin, R.anim.zoomout);
	}

	public void menu_right(View view) {
		PopupMenu popup = new PopupMenu(this, view);
		MenuInflater inflater = popup.getMenuInflater();
		inflater.inflate(R.menu.menu_userinfo, popup.getMenu());
		popup.show();
	}

/*	public static void setOverflowIconVisible(int featureId, Menu menu) {
		if (featureId == Window.FEATURE_ACTION_BAR && menu != null) {
			if (menu.getClass().getSimpleName().equals("MenuBuilder")) {
				try {
					Method m = menu.getClass().getDeclaredMethod("setOptionalIconsVisible", Boolean.TYPE);
					m.setAccessible(true);
					m.invoke(menu, true);
				} catch (Exception e) {
				}
			}
		}
	}*/
	
	public void back(View view) {
		Intent intent = new Intent(Activity_ChurchMember.this, MainActivity.class);
		intent.putExtra("from",1);
		startActivity(intent);
		finish();
		overridePendingTransition(R.anim.zoomin, R.anim.zoomout);
	}

}
