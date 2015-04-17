package com.orange.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.PathEffect;
import android.util.AttributeSet;
import android.view.WindowManager;
import android.widget.EditText;

public class MyEditText extends EditText {
	Context mContext;

	public MyEditText(Context context) {
		super(context);
		mContext = context;
	}

	public MyEditText(Context context, AttributeSet attrs) {
		super(context, attrs);
		mContext = context;
	}

	public MyEditText(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		mContext = context;
	}

	protected void onDraw(Canvas canvas) {
		WindowManager wm = (WindowManager) mContext.getSystemService("window");
		int windowWidth = wm.getDefaultDisplay().getWidth();
		int windowHeight = wm.getDefaultDisplay().getHeight();
		Paint paint = new Paint();
		
		paint.setStyle(Paint.Style.STROKE);
		paint.setColor(Color.BLACK);
		paint.setAntiAlias(true);
		PathEffect effect=new DashPathEffect(new float[]{5,5,5,5}, 1);
		paint.setPathEffect(effect);
		int paddingTop = getPaddingTop();
		int paddingBottom = getPaddingBottom();
		int scrollY = getScrollY();
		int scrollX = getScrollX() + windowWidth;
		int innerHeight = scrollY + getHeight() - paddingBottom;
		int lineHeight = getLineHeight();
		int baseLine = scrollY + (lineHeight - ((scrollY - paddingTop) % lineHeight));
		int x =8;
		while (baseLine < innerHeight) {
			canvas.drawLine(x, baseLine, scrollX - x, baseLine, paint);
			baseLine += lineHeight;
		}
		super.onDraw(canvas);
	}
}
