package com.wujie.help;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import com.example.acts.R;

import android.app.Activity;
import android.content.Context;
import android.view.Window;


public class TopBarTransparent {

	public TopBarTransparent() {
		// TODO Auto-generated constructor stub
	}
	 static public void transparentTitleBar(Activity t)
	  	{
	  		Window window = t.getWindow();
	  		//下面的语句会导致报错，需要在资源文件中添加下面的语句：<color name="title_bar">#015092</color>
	  		window.setBackgroundDrawableResource(R.color.title_bar);

	  		Class clazz = window.getClass();
	  		try {
	  		int tranceFlag = 0;
	  		int darkModeFlag = 0;
	  		Class layoutParams = Class.forName("android.view.MiuiWindowManager$LayoutParams");

	  		Field field = layoutParams.getField("EXTRA_FLAG_STATUS_BAR_TRANSPARENT");
	  		tranceFlag = field.getInt(layoutParams);

	  		field = layoutParams.getField("EXTRA_FLAG_STATUS_BAR_DARK_MODE");
	  		darkModeFlag = field.getInt(layoutParams);

	  		Method extraFlagField = clazz.getMethod("setExtraFlags", int.class, int.class);
	  		//只需要状态栏透明
	  		extraFlagField.invoke(window, tranceFlag, tranceFlag);
	  		//或
	  		//状态栏透明且黑色字体
	  		extraFlagField.invoke(window, tranceFlag | darkModeFlag, tranceFlag | darkModeFlag);
	  		//清除黑色字体 
	  		extraFlagField.invoke(window, 0, darkModeFlag);
	  		} catch (NoSuchMethodException e) {
	  		e.printStackTrace();
	  		} catch (ClassNotFoundException e) {
	  		e.printStackTrace();
	  		} catch (NoSuchFieldException e) {
	  		e.printStackTrace();
	  		} catch (IllegalAccessException e) {
	  		e.printStackTrace();
	  		} catch (IllegalArgumentException e) {
	  		e.printStackTrace();
	  		} catch (InvocationTargetException e) {
	  		e.printStackTrace();
	  		}
	  	}
}
