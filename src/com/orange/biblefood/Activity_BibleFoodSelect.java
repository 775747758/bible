package com.orange.biblefood;

import com.example.acts.Activity_Option;
import com.example.acts.MainActivity;
import com.example.acts.R;
import com.orange.read.Activity_SelectVolume;
import com.orange.test.Activity_BibleTestMain;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;

public class Activity_BibleFoodSelect extends Activity{
	
	private ViewPager viewpager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_biblefoodselect);
		
		//viewpager=(ViewPager)findViewById(R.id.viewpager);
	}
	
	public void food1(View view)
	{
		Intent intent = new Intent(Activity_BibleFoodSelect.this, Activity_BibleFood.class);
		intent.putExtra("bookId", "1");
		intent.putExtra("bookName", "竭诚为主");
		startActivity(intent);
		finish();
		overridePendingTransition(R.anim.zoomin, R.anim.zoomout);
	}
	
	public void food2(View view)
	{
		Intent intent = new Intent(Activity_BibleFoodSelect.this, Activity_BibleFood.class);
		intent.putExtra("bookId", "2");
		intent.putExtra("bookName", "荒漠甘泉");
		startActivity(intent);
		finish();
		overridePendingTransition(R.anim.zoomin, R.anim.zoomout);
	}
	public void food4(View view)
	{
		Intent intent = new Intent(Activity_BibleFoodSelect.this, Activity_BibleFood.class);
		intent.putExtra("bookId", "4");
		intent.putExtra("bookName", "生命隽语");
		startActivity(intent);
		finish();
		overridePendingTransition(R.anim.zoomin, R.anim.zoomout);
	}
	public void food5(View view)
	{
		Intent intent = new Intent(Activity_BibleFoodSelect.this, Activity_BibleFood.class);
		intent.putExtra("bookId", "5");
		intent.putExtra("bookName", "每日天粮");
		startActivity(intent);
		finish();
		overridePendingTransition(R.anim.zoomin, R.anim.zoomout);
	}
	public void food6(View view)
	{
		Intent intent = new Intent(Activity_BibleFoodSelect.this, Activity_BibleFood.class);
		intent.putExtra("bookId", "6");
		intent.putExtra("bookName", "旷野的筵席");
		startActivity(intent);
		finish();
		overridePendingTransition(R.anim.zoomin, R.anim.zoomout);
	}
	public void food7(View view)
	{
		Intent intent = new Intent(Activity_BibleFoodSelect.this, Activity_BibleFood.class);
		intent.putExtra("bookId", "7");
		intent.putExtra("bookName", "活水");
		startActivity(intent);
		finish();
		overridePendingTransition(R.anim.zoomin, R.anim.zoomout);
	}
	public void food8(View view)
	{
		Intent intent = new Intent(Activity_BibleFoodSelect.this, Activity_BibleFood.class);
		intent.putExtra("bookId", "8");
		intent.putExtra("bookName", "每日经历神");
		startActivity(intent);
		finish();
		overridePendingTransition(R.anim.zoomin, R.anim.zoomout);
	}
	public void food9(View view)
	{
		Intent intent = new Intent(Activity_BibleFoodSelect.this, Activity_BibleFood.class);
		intent.putExtra("bookId", "9");
		intent.putExtra("bookName", "清晨甘露");
		startActivity(intent);
		finish();
		overridePendingTransition(R.anim.zoomin, R.anim.zoomout);
	}
	public void food10(View view)
	{
		Intent intent = new Intent(Activity_BibleFoodSelect.this, Activity_BibleFood.class);
		intent.putExtra("bookId", "10");
		intent.putExtra("bookName", "静夜亮光");
		startActivity(intent);
		finish();
		overridePendingTransition(R.anim.zoomin, R.anim.zoomout);
	}
	
	public void back(View view) {
		Intent intent = new Intent(Activity_BibleFoodSelect.this, MainActivity.class);
		intent.putExtra("from",0);
		startActivity(intent);
		finish();
		overridePendingTransition(R.anim.zoomin, R.anim.zoomout);
	}
	

}
