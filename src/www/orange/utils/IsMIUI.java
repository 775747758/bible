package www.orange.utils;

import android.os.Build;

public class IsMIUI {
	
	public static boolean isMIUI()
	{
		if(Build.MANUFACTURER.equals("Xiaomi"))
		{
			if(System.getProperty("ro.miui.ui.version.name", "").equals(" V6"))
			{
				return true;
			}
			else
			{
				return false;
			}
		}
		else
		{
			return false;
		}
	}

}
