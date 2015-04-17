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

	// ��ȫ�˳�Ӧ��
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
			Toast.makeText(mContext, "�ٰ�һ�κ��˼��˳�Ӧ�ó���", Toast.LENGTH_SHORT).show();
			if (!hasTask) {
				tExit.schedule(task, 2000);
			}
		} else {
			System.exit(0);
		}

		// 1.5 - 2.1֮ǰ����������ok��,2.2֮��Ͳ����ˣ����Բ�ͨ��
		// ActivityManager am =
		// (ActivityManager)mContext.getSystemService(Context.ACTIVITY_SERVICE);
		// am.restartPackage("com.tutor.exit");

		// Intent mIntent = new Intent();
		// mIntent.setClass(mContext, MainActivity.class);
		// ��������flag���ǱȽ� ��Ҫ��
		// mIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		// �����˳�����ָʾ
		// mIntent.putExtra("flag", EXIT_APPLICATION);
		// mContext.startActivity(mIntent);
	}
}