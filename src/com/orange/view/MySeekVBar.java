package com.orange.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.SeekBar;

public class MySeekVBar extends SeekBar {

	
	public MySeekVBar(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}
	
	public MySeekVBar(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// TODO Auto-generated method stub
		return true;
	}
	
	
	

}
