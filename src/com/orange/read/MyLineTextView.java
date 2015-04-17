package com.orange.read;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.WindowManager;
import android.widget.TextView;


public class MyLineTextView extends TextView {
	
	private List<Integer> positionStart=new ArrayList<Integer>();
	private List<Integer> positionEnd=new ArrayList<Integer>();
	
	private Paint paint;
	private int position;
	Context context;
	
	public MyLineTextView(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context=context;
		paint = new Paint();
		paint.setColor(Color.BLUE);
		paint.setStyle(Paint.Style.STROKE);
	}
	
	public MyLineTextView(Context context) {
		super(context);
		this.context=context;
		paint = new Paint();
		paint.setColor(Color.BLUE);
		paint.setStyle(Paint.Style.STROKE);
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		position = getOffsetForPosition(event.getX(), event.getY());
		invalidate();
		return super.onTouchEvent(event);
	}
	
	/**
	 * 重画界面重画界面重画界面重画界面重画界面重画界面重画界面重画界面
	 * 重画界面重画界面重画界面重画界面重画界面重画界面重画界面重画界面
	 * @see android.widget.TextView#onDraw(android.graphics.Canvas)
	 */
	@Override
	protected void onDraw(Canvas canvas) {
		
		WindowManager wm = (WindowManager) context.getSystemService("window");
		int windowWidth = wm.getDefaultDisplay().getWidth();
		int windowHeight = wm.getDefaultDisplay().getHeight();
		int width=getWidth();
		int lineHeight = this.getLineHeight();
		
		int topPadding = this.getPaddingTop();
		int leftPadding = this.getPaddingLeft();
		int rightPadding = this.getPaddingRight();
		
		float textSize = getTextSize();
		setGravity(Gravity.LEFT|Gravity.TOP);
		if(positionStart.size()!=0)
		{
			int y = (int)(topPadding + textSize);
			int wordALine=Math.round((width-leftPadding-rightPadding)/getTextSize());
			int start=0;
			int end=0;
			int index=findChapter(position);
			int line=0;
			int lineNum;
			int scrollX = getScrollX();
			if(index!=-1)
			{
				line=positionStart.get(index)/wordALine;
				start=positionStart.get(index)%wordALine;
				Log.i("log", "line:"+line+"  "+start);
				if(positionEnd.get(index)-positionStart.get(index)<=wordALine)
				{
					Log.i("log", "小");
					int count=positionEnd.get(index)-positionStart.get(index);
					//canvas.drawLine(leftPadding+getTextSize()*start, y + 10, getTextSize()*getText().length(), y + 10, paint);
					canvas.drawLine(leftPadding+getTextSize()*3, (lineHeight)*line + 10, leftPadding+getTextSize()*count, (lineHeight)*line + 10, paint);
					//canvas.translate(0, 0);
				}
				else
				{
					Log.i("log", "大");
					int firstLineWords=positionEnd.get(index)-start;
					canvas.drawLine(leftPadding+getTextSize()*firstLineWords, (lineHeight)*line + 10, getRight() - leftPadding, (lineHeight)*line + 10, paint);
					//canvas.translate(0, 0);
					int linecount=(positionEnd.get(index)-positionStart.get(index)-firstLineWords)/wordALine;
					int lastLineWords=(positionEnd.get(index)-positionStart.get(index)-firstLineWords)%wordALine;
					if(linecount==0)
					{
						Log.i("log", "0行");
						canvas.drawLine(leftPadding, (lineHeight)*(line+1) + 10, leftPadding+getTextSize()*lastLineWords, (lineHeight)*(line+1) + 10, paint);
						//canvas.translate(0, 0);
					}
					else
					{
						Log.i("log", "多行");
						int height=(lineHeight)*line + 10;
						for(int i=0;i<linecount;i++)
						{
							Log.i("log", "画行");
							height=height+lineHeight;
							canvas.drawLine(leftPadding, height,getRight() - leftPadding,height, paint);
							//canvas.translate(0, 0);
						}
						height=height+lineHeight;
						Log.i("log", "画最后行");
						canvas.drawLine(leftPadding, height, leftPadding+getTextSize()*lastLineWords, height, paint);
						//canvas.translate(0, 0);
					}
				}
			}
				
			/*	
				//y += lineHeight;
				canvas.translate(0, 0);
			
			canvas.translate(0, 0);*/
		}
		
		
		
		super.onDraw(canvas);
	}
	
	public void setList(ArrayList<Integer> positionStart,ArrayList<Integer> positionEnd)
	{
		this.positionStart=positionStart;
		this.positionEnd=positionEnd;
		invalidate();
	}
	
	public int findChapter(int position) {
		int reult = 0;
		for (int i = 0; i < positionStart.size(); i++) {
			if (i != positionStart.size() - 1) {

				if (positionStart.get(i) < position && positionEnd.get(i) > position) {

					reult = i;
					break;
				}
			} else {
				Log.i("最后", "进入");
				reult = positionStart.size() - 1;
				break;
			}

		}
		return reult;

	}
}
