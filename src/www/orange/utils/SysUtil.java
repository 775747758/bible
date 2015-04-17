package www.orange.utils;

import java.util.Timer;
import java.util.TimerTask;

import com.example.acts.Activity_Splash;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class SysUtil {
	public static final int EXIT_APPLICATION = 0x0001;
	private static Boolean isExit = false;
	private static Boolean hasTask = false;
	private static Context mContext;

	public SysUtil(Context context) {
		this.mContext = context;
	}

	// 完全退出应用
	public static void exit() {

		Timer tExit = new Timer();
		TimerTask task = new TimerTask() {

			@Override
			public void run() {
				isExit = false;
				hasTask = false;
			}
		};

		if (isExit == false) {
			isExit = true;
			Toast.makeText(mContext, "再按一次后退键退出应用程序", Toast.LENGTH_SHORT).show();
			if (!hasTask) {
				tExit.schedule(task, 2000);
			}
		} else {
			System.exit(0);
		}

		// 1.5 - 2.1之前下面两行是ok的,2.2之后就不行了，所以不通用
		// ActivityManager am =
		// (ActivityManager)mContext.getSystemService(Context.ACTIVITY_SERVICE);
		// am.restartPackage("com.tutor.exit");

		// Intent mIntent = new Intent();
		// mIntent.setClass(mContext, MainActivity.class);
		// 这里设置flag还是比较 重要的
		// mIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		// 发出退出程序指示
		// mIntent.putExtra("flag", EXIT_APPLICATION);
		// mContext.startActivity(mIntent);
	}
}