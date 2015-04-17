package com.orange.read;

import com.example.acts.R;
import com.example.acts.R.id;
import com.example.acts.R.layout;

import net.loonggg.view.SlidingMenu;
import android.content.Intent;
import android.os.Bundle;

import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.TextView;
import android.widget.ViewFlipper;

public class ActivityReadMain extends FragmentActivity {

	private int chapterIndexInBook = 0;
	private SlidingMenu mSlidingMenu;// 侧边栏的view
	// private LeftFragment leftFragment; // 左侧边栏的碎片化view
	private RightFragment rightFragment; // 右侧边栏的碎片化view
	private Activity_ReadBible centerFragment;// 中间内容碎片化的view
	private FragmentTransaction ft; // 碎片化管理的事务
	private ViewFlipper viewFlipper;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 去标题栏
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_readmain);
		mSlidingMenu = (SlidingMenu) findViewById(R.id.slidingMenu);
		/*
		 * mSlidingMenu.setLeftView(getLayoutInflater().inflate(
		 * R.layout.left_frame, null));
		 */
		mSlidingMenu.setRightView(getLayoutInflater().inflate(R.layout.right_frame, null));
		mSlidingMenu.setCenterView(getLayoutInflater().inflate(R.layout.center_frame, null));
		View view = LayoutInflater.from(this).inflate(R.layout.activity_readbible, null);

		viewFlipper = (ViewFlipper) view.findViewById(R.id.contentViewFlipper);

		ft = this.getSupportFragmentManager().beginTransaction();

		Log.i("Bundle", chapterIndexInBook + "");
		rightFragment = new RightFragment();
		ft.replace(R.id.right_frame, rightFragment, "right_frame");

		centerFragment = new Activity_ReadBible();
		ft.replace(R.id.center_frame, centerFragment);

		ft.commit();

	}

	public void llronclick(View v) {
		switch (v.getId()) {

		default:
			break;
		}
	}

	public void showLeft() {
		mSlidingMenu.showLeftView();
	}

	public void showRight() {
		mSlidingMenu.showRightView();
	}

	public void toright(View view) {
		Log.i("toright", "toright");
		viewFlipper.showNext();
	}

	public void toleft(View view) {
		Log.i("toleft", "toleft");
		viewFlipper.showPrevious();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(keyCode==KeyEvent.KEYCODE_BACK)
		{
			centerFragment.exitLongclick();
		}
		return false;
	}
}
