package com.orange.view;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.CornerPathEffect;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathEffect;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.WindowManager;
import android.widget.TextView;

public class MyTextView extends TextView {
	private List<Map<String, Integer>> myList = new ArrayList<Map<String, Integer>>();
	float contentWidth;
	float xTemp;
	float yTemp;
	int temp;
	Context mContext;
	public MyTextView(Context context) {
		super(context);
		mContext = context;
		// TODO Auto-generated constructor stub
	}
	public MyTextView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}
	
	public MyTextView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
	}
	
	protected void onDraw(Canvas canvas) {
		WindowManager wm = (WindowManager) mContext.getSystemService("window");
		int windowWidth = wm.getDefaultDisplay().getWidth();
		int windowHeight = wm.getDefaultDisplay().getHeight();
		Paint paint = new Paint();
		
		paint.setStyle(Paint.Style.STROKE);
		paint.setColor(Color.BLACK);
		paint.setAntiAlias(true);
		CornerPathEffect effect=new CornerPathEffect(5);
		paint.setPathEffect(effect);
		int paddingTop = getPaddingTop();
		int paddingBottom = getPaddingBottom();
		int scrollY = getScrollY();
		int scrollX = getScrollX() + windowWidth;
		int innerHeight = scrollY + getHeight() - paddingBottom;
		int lineHeight = getLineHeight();
		int baseLine = scrollY + (lineHeight - ((scrollY - paddingTop) % lineHeight));
		int x = 8;
		while (baseLine < innerHeight) {
			if(yTemp>baseLine&&yTemp<baseLine+lineHeight)
			{
				canvas.drawLine(100, baseLine, scrollX - x, baseLine, paint);
			}
			baseLine += lineHeight;
		}
		if(contentWidth<scrollX-xTemp-30)
		{
			canvas.drawLine(xTemp, baseLine, scrollX - x, baseLine, paint);
		}
		
		super.onDraw(canvas);
		/*Paint paint = new Paint();      
        paint.setStyle(Paint.Style.STROKE);      
        paint.setColor(Color.DKGRAY);      
        Path path = new Path();           
        path.moveTo(0, 10);      
        path.lineTo(480,10);            
        PathEffect effects = new DashPathEffect(new float[]{5,5,5,5},1);      
        paint.setPathEffect(effects);      
        canvas.drawPath(path, paint);  */ 
	}
	
	public void setTemp(int x)
	{
		temp=x;
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// TODO Auto-generated method stub
		xTemp=event.getX();
		yTemp=event.getY();
		invalidate();
		return super.onTouchEvent(event);
	}
	
	public void setUnderLine(List<Map<String, Integer>> list)
	{
		myList=list;
		contentWidth=(myList.get(0).get("end")-myList.get(0).get("start"))*getOneWidth();
	}
	//获取一个字符的宽度
	public float getOneWidth()
	{
		TextPaint tp=new TextPaint();
		tp.setTextSize(getTextSize());
		return tp.measureText("圣");
	}
	
}
