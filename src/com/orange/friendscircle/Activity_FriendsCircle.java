package com.orange.friendscircle;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;

import www.orange.utils.MyProgressPopUpWindow;

import com.example.acts.Activity_Option;
import com.example.acts.MainActivity;
import com.example.acts.R;

import com.markupartist.android.widget.PullToRefreshListView;
import com.markupartist.android.widget.PullToRefreshListView.OnRefreshListener;
import com.orange.church.Activity_ChurchInfo;
import com.orange.church.Activity_ChurchMember;
import com.orange.login.Activity_Login;
import com.orange.net.UpLoadChurchMember;
import com.orange.net.UpLoadTalk;
import com.orange.read.Activity_SelectVolume;
import com.orange.recite.Activity_ReciteMain;
import com.orange.test.Activity_BibleTestMain;
import com.orange.user.Activity_DetailUserInfo;
import com.orange.user.Activity_UserInfo;
import com.orange.view.CircleImageView;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.Editable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextWatcher;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AbsListView.OnScrollListener;
import android.widget.PopupMenu.OnMenuItemClickListener;

public class Activity_FriendsCircle extends Activity implements
		OnMenuItemClickListener {

	private ArrayAdapter<String> adapter;
	private PullToRefreshListView mPullRefreshListView;
	private ArrayList<String> mListItems;
	private ProgressDialog reDialog;
	private RelativeLayout send_layout;
	private ImageView send_iv; 
	private EditText content_et;
	private int loadCount=5;
	private UpLoadTalk upLoadTalk;
	private View footView;
	protected int scrolledX;
	protected int scrolledY;
	protected int positionBegin;
	private Intent intent;
	private AlertDialog dialog;
	private CircleImageView siv_img;
	private SharedPreferences spUser;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_friendscircle);

		send_layout = (RelativeLayout) findViewById(R.id.send_layout);

		spUser = getSharedPreferences("UserInfo",
				Context.MODE_PRIVATE);
		
		View view=getheadView();
		siv_img=(CircleImageView)view.findViewById(R.id.siv_img);
		TextView myName_tv=(TextView)view.findViewById(R.id.myName_tv);
		if(!spUser.getString("userName", "").equals(""))
		{
			Bitmap bitmap=BitmapFactory.decodeFile(spUser.getString("portraitUri", ""));
			siv_img.setImageBitmap(bitmap);
			myName_tv.setText(spUser.getString("name", ""));
		}
		
		siv_img.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(spUser.getBoolean("isLogin", false))
				{
					Intent intent = new Intent(Activity_FriendsCircle.this,Activity_DetailUserInfo.class);
					intent.putExtra("userName", spUser.getString("userName", ""));
					intent.putExtra("name", spUser.getString("name", ""));
					intent.putExtra("from", "Activity_FriendsCircle");
					startActivity(intent);
					finish();
					overridePendingTransition(R.anim.zoomin, R.anim.zoomout);
				}
				else
				{
					Intent intent = new Intent(Activity_FriendsCircle.this,Activity_Login.class);
					startActivity(intent);
					overridePendingTransition(R.anim.zoomin, R.anim.zoomout);
				}
				
			}
		});
		
		
		
		LayoutInflater inflater = LayoutInflater.from(this);
		footView = inflater.inflate(R.layout.dialog_progress, null);

		mPullRefreshListView = (PullToRefreshListView) findViewById(R.id.pull_refresh_list);
		mPullRefreshListView.setOnItemLongClickListener(new OnItemLongClickListener() {

			private AlertDialog Alert;

			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view,
					int position, long id) {
				final TextView content = (TextView) view.findViewById(R.id.content);
				AlertDialog.Builder builder = new AlertDialog.Builder(Activity_FriendsCircle.this,AlertDialog.THEME_HOLO_LIGHT);  
				//AlertDialog strengthenDialog=new AlertDialog.Builder(Activity_ReciteMain.this,).create();
				builder.setTitle("");
				builder.setItems(new String[] {"复制"}, new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						if(which==0)
						{
							ClipboardManager cmb = (ClipboardManager) getSystemService(Activity_FriendsCircle.this.CLIPBOARD_SERVICE);
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
		
		intent=new Intent(Activity_FriendsCircle.this,Activity_UserInfo.class);
		intent.putExtra("from", "Activity_FriendsCircle");
		mPullRefreshListView.addHeaderView(view);
		mPullRefreshListView.setOnRefreshListener(new OnRefreshListener() {
			@Override
			public void onRefresh() {
				ProgressDialog reDialog = new ProgressDialog(
						Activity_FriendsCircle.this);
				reDialog.setMessage("正在查询中...");
				// reDialog.show();
				UpLoadTalk upLoadTalk = new UpLoadTalk(
						Activity_FriendsCircle.this, reDialog, send_layout,intent);
				SharedPreferences sp = getSharedPreferences("ChurchInfo",
						Context.MODE_PRIVATE);
				
				// upLoadTalk.addTalk(sp.getString("churchName", "no"),
				// content_et.getText().toString().trim(),spUser.getString("name",
				// ""), spUser.getString("userName", ""),
				// spUser.getString("portraitUri", ""),spUser.getString("city",
				// ""), intent);
				upLoadTalk.getAllTalk(mPullRefreshListView,loadCount,scrolledX,scrolledY,footView);
				
			}
		});
		
		mPullRefreshListView.setOnScrollListener(new OnScrollListener() {

		

			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				switch (scrollState) {
				case OnScrollListener.SCROLL_STATE_IDLE:
					int position = view.getLastVisiblePosition();
					positionBegin = view.getFirstVisiblePosition();
					
					if (position == loadCount) {
						mPullRefreshListView.addFooterView(footView);
						loadCount=loadCount+100;
						
						scrolledX = mPullRefreshListView.getScrollX();
						scrolledY = mPullRefreshListView.getScrollY();
						
						Log.i("scrolledX", scrolledX+"daddddd"+scrolledY);
						upLoadTalk.getAllTalk(mPullRefreshListView,loadCount,scrolledX,scrolledY,footView);
						//mPullRefreshListView.scrollTo(scrolledX, scrolledY);
					}

					break;

				default:
					break;
				}

			}

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
				// TODO Auto-generated method stub

			}
		});
		
		dialog=new MyProgressPopUpWindow(Activity_FriendsCircle.this,"正在加载中...").createADialog();
		// reDialog.show();
		upLoadTalk = new UpLoadTalk(this, dialog, send_layout,intent);
		upLoadTalk.getAllTalk(mPullRefreshListView,loadCount,scrolledX,scrolledY,footView);
	}

	public void menu_right(View view) {
		PopupMenu popup = new PopupMenu(this, view);
		popup.setOnMenuItemClickListener(Activity_FriendsCircle.this);
		MenuInflater inflater = popup.getMenuInflater();
		inflater.inflate(R.menu.menu_friendscircle, popup.getMenu());
		popup.show();
	}

	@Override
	public boolean onMenuItemClick(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.all:
			Log.i("全部", "ddddd");
			ProgressDialog reDialog = new ProgressDialog(
					Activity_FriendsCircle.this);
			reDialog.setMessage("正在查询中...");
			// reDialog.show();
			UpLoadTalk upLoadTalk = new UpLoadTalk(Activity_FriendsCircle.this,
					reDialog, send_layout,intent);
			SharedPreferences sp = getSharedPreferences("ChurchInfo",
					Context.MODE_PRIVATE);
			SharedPreferences spUser = getSharedPreferences("UserInfo",
					Context.MODE_PRIVATE);
			// upLoadTalk.addTalk(sp.getString("churchName", "no"),
			// content_et.getText().toString().trim(),spUser.getString("name",
			// ""), spUser.getString("userName", ""),
			// spUser.getString("portraitUri", ""),spUser.getString("city", ""),
			// intent);
			upLoadTalk.getAllTalk(mPullRefreshListView,loadCount,scrolledX,scrolledY,footView);
			break;
		case R.id.church:
			Log.i("教会", "ddddd");
			SharedPreferences sp2 = getSharedPreferences("ChurchInfo",
					Context.MODE_PRIVATE);

			if (sp2.getString("churchName", "").equals("")) {
				Toast.makeText(getApplicationContext(), "您还没有加入教会！",
						Toast.LENGTH_SHORT).show();
			} else {
				ProgressDialog reDialog2 = new ProgressDialog(
						Activity_FriendsCircle.this);
				reDialog2.setMessage("正在查询中...");
				// reDialog.show();
				UpLoadTalk upLoadTalk2 = new UpLoadTalk(
						Activity_FriendsCircle.this, reDialog2, send_layout,intent);
				upLoadTalk2.getMyChurchTalk(mPullRefreshListView,
						sp2.getString("churchName", "no"));
			}

			break;

		default:
			break;
		}
		return true;
	}

	private View getheadView() {
		View view = LayoutInflater.from(Activity_FriendsCircle.this).inflate(
				R.layout.friendscircle_header, null);
		return view;
	}

	public void add(View view) {
		SharedPreferences spUser = getSharedPreferences("UserInfo",
				Context.MODE_PRIVATE);
		if (spUser.getString("userName", "").equals("")) {
			Toast.makeText(getApplicationContext(), "请您先去注册，再发表说说！",
					Toast.LENGTH_SHORT).show();
		} else {
			Intent intent = new Intent(Activity_FriendsCircle.this,
					Activity_PublishTalk.class);
			startActivity(intent);
			overridePendingTransition(R.anim.zoomin, R.anim.zoomout);
		}

	}
	
	public void back(View view) {
		Intent intent = new Intent(Activity_FriendsCircle.this, MainActivity.class);
		intent.putExtra("from",0);
		startActivity(intent);
		finish();
		overridePendingTransition(R.anim.zoomin, R.anim.zoomout);
	}

}
