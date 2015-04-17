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
		intent.putExtra("bookName", "�߳�Ϊ��");
		startActivity(intent);
		finish();
		overridePendingTransition(R.anim.zoomin, R.anim.zoomout);
	}
	
	public void food2(View view)
	{
		Intent intent = new Intent(Activity_BibleFoodSelect.this, Activity_BibleFood.class);
		intent.putExtra("bookId", "2");
		intent.putExtra("bookName", "��Į��Ȫ");
		startActivity(intent);
		finish();
		overridePendingTransition(R.anim.zoomin, R.anim.zoomout);
	}
	public void food4(View view)
	{
		Intent intent = new Intent(Activity_BibleFoodSelect.this, Activity_BibleFood.class);
		intent.putExtra("bookId", "4");
		intent.putExtra("bookName", "��������");
		startActivity(intent);
		finish();
		overridePendingTransition(R.anim.zoomin, R.anim.zoomout);
	}
	public void food5(View view)
	{
		Intent intent = new Intent(Activity_BibleFoodSelect.this, Activity_BibleFood.class);
		intent.putExtra("bookId", "5");
		intent.putExtra("bookName", "ÿ������");
		startActivity(intent);
		finish();
		overridePendingTransition(R.anim.zoomin, R.anim.zoomout);
	}
	public void food6(View view)
	{
		Intent intent = new Intent(Activity_BibleFoodSelect.this, Activity_BibleFood.class);
		intent.putExtra("bookId", "6");
		intent.putExtra("bookName", "��Ұ����ϯ");
		startActivity(intent);
		finish();
		overridePendingTransition(R.anim.zoomin, R.anim.zoomout);
	}
	public void food7(View view)
	{
		Intent intent = new Intent(Activity_BibleFoodSelect.this, Activity_BibleFood.class);
		intent.putExtra("bookId", "7");
		intent.putExtra("bookName", "��ˮ");
		startActivity(intent);
		finish();
		overridePendingTransition(R.anim.zoomin, R.anim.zoomout);
	}
	public void food8(View view)
	{
		Intent intent = new Intent(Activity_BibleFoodSelect.this, Activity_BibleFood.class);
		intent.putExtra("bookId", "8");
		intent.putExtra("bookName", "ÿ�վ�����");
		startActivity(intent);
		finish();
		overridePendingTransition(R.anim.zoomin, R.anim.zoomout);
	}
	public void food9(View view)
	{
		Intent intent = new Intent(Activity_BibleFoodSelect.this, Activity_BibleFood.class);
		intent.putExtra("bookId", "9");
		intent.putExtra("bookName", "�峿��¶");
		startActivity(intent);
		finish();
		overridePendingTransition(R.anim.zoomin, R.anim.zoomout);
	}
	public void food10(View view)
	{
		Intent intent = new Intent(Activity_BibleFoodSelect.this, Activity_BibleFood.class);
		intent.putExtra("bookId", "10");
		intent.putExtra("bookName", "��ҹ����");
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
