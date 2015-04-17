package www.orange.updateapk;

import java.io.File;
import java.net.ContentHandler;
import java.util.List;

import com.baidu.frontia.Frontia;
import com.baidu.frontia.FrontiaFile;
import com.baidu.frontia.api.FrontiaStorage;
import com.baidu.frontia.api.FrontiaStorageListener.FileListListener;
import com.baidu.frontia.api.FrontiaStorageListener.FileProgressListener;
import com.baidu.frontia.api.FrontiaStorageListener.FileTransferListener;
import com.example.acts.R;
import com.orange.login.Activity_Login;
import com.orange.read.Activity_ReadBible2;

import android.R.integer;
import android.annotation.SuppressLint;
import android.app.ActionBar.LayoutParams;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.text.StaticLayout;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

public class createApopwindow {
	private static String newVersion;
	private static Notification.Builder builder;
	private static Notification notification;
	private static NotificationManager manager = null;
	private static Button cancle;
	private static Button certain;
	private static TextView versionTextView;
	private static TextView sizeTextView;
	private static long size;
	private static String version;
	private static boolean isNew;
	private static FrontiaStorage mCloudStorage;
	private static FrontiaFile mFile;

	private static PopupWindow popWin = null; // 弹出窗口
	private static AlertDialog Alert;
	private static int width;
	private static int height;
	private static View popView;
	private static Activity myActivity;

	@SuppressLint("NewApi")
	public static AlertDialog create(WindowManager wm, Context Mycontext,Activity activity) {

		myActivity=activity;
		final Context context = Mycontext;
		manager = (NotificationManager) context.getSystemService("notification");
		builder = new Notification.Builder(context);
		builder.setTicker("更新");
		builder.setSmallIcon(R.drawable.ic_launcher);
		builder.setContentTitle("正在下载更新包...");
		builder.setProgress(100, 0, false);
		notification = builder.build();

		
		mCloudStorage.listFiles(new FileListListener() {

			@Override
			public void onSuccess(List<FrontiaFile> list) {
				StringBuilder sb = new StringBuilder();
				for (FrontiaFile info : list) {
					size = info.getSize();
					version = info.getRemotePath().substring(info.getRemotePath().indexOf("_") + 1)
							.substring(0, info.getRemotePath().substring(info.getRemotePath().indexOf("_") + 1).lastIndexOf("."));
					sizeTextView = (TextView) popView.findViewById(R.id.size);
					versionTextView = (TextView) popView.findViewById(R.id.version);
					versionTextView.setText("最新版本：" + version);
					sizeTextView.setText("新版本大小：" + size / 1024 / 1014 + "M");
				}
			}

			@Override
			public void onFailure(int errCode, String errMsg) {
				Toast.makeText(context, "请检查网络！", Toast.LENGTH_SHORT).show();
			}

		});
		width = wm.getDefaultDisplay().getWidth();
		height = wm.getDefaultDisplay().getHeight();

		
		AlertDialog.Builder builder = new AlertDialog.Builder(
				activity, R.style.Dialog1);
		builder.setInverseBackgroundForced(true);
		LayoutInflater inflater = LayoutInflater.from(Mycontext);
		popView = inflater.inflate(R.layout.dialog_updateapk, null);
		Button cancel = (Button) popView.findViewById(R.id.cancel);
		Button certain = (Button) popView.findViewById(R.id.certain);
		final EditText newbookmark_et = (EditText) popView.findViewById(R.id.newbookmark_et);
		
		cancel.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				Alert.cancel();
			}
		});
		certain.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Alert.cancel();
				manager.notify(1001, notification);
				download(context);
				
			}
		});
		
		Alert = builder.create();
		
		Alert.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
		
		return Alert;
	}

	public static void isNew(final Context myContex, final Activity activity,final WindowManager myWm, View myView, final AlertDialog dialog) {

		final WindowManager wm = myWm;
		final View view = myView;
		final Context context = myContex;
		mFile = new FrontiaFile();

		mCloudStorage = Frontia.getStorage();
		mCloudStorage.listFiles(new FileListListener() {
			@Override
			public void onSuccess(List<FrontiaFile> list) {
				dialog.dismiss();
				try {
					PackageManager manager = context.getPackageManager();
					PackageInfo infos;
					infos = manager.getPackageInfo(context.getPackageName(), 0);
					//AlertDialog popWin = null;
					//popWin = createApopwindow.create(wm, context,activity);
					StringBuilder sb = new StringBuilder();
					for (FrontiaFile info : list) {
						String version = info.getRemotePath().substring(info.getRemotePath().indexOf("_") + 1)
								.substring(0, info.getRemotePath().substring(info.getRemotePath().indexOf("_") + 1).lastIndexOf("."));
						if (!version.equals(infos.versionName)) {
							newVersion = version;
							isNew = true;
							mFile.setNativePath("/sdcard/" + info.getRemotePath());
							mFile.setRemotePath(info.getRemotePath());
						}
					}
					if (isNew) {
						//dialog.dismiss();
						AlertDialog alert=create(myWm,myContex,activity);
						alert.show();
						alert.setContentView(popView);
						alert.getWindow().setLayout(7 * width / 8, 500);
						alert.setTitle("测试");
					} else {
						Toast.makeText(context, "已经是最新版本！", Toast.LENGTH_SHORT).show();
					}
				} catch (NameNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}


			@Override
			public void onFailure(int errCode, String errMsg) {
				dialog.dismiss();
				Toast.makeText(context, "请检查网络！", Toast.LENGTH_SHORT).show();
			}

		});

	}

	public static void download(Context Mycontext) {
		final Context context = Mycontext;
		mCloudStorage.downloadFile(mFile, new FileProgressListener() {

			@SuppressLint("NewApi")
			@Override
			public void onProgress(String source, long bytes, long total) {
				builder.setContentTitle("正在下载更新包...   " + (int) (bytes * 100 / total) + "%");
				builder.setProgress(100, (int) (bytes * 100 / total), false);
				manager.notify(1001, builder.build());
			}

		}, new FileTransferListener() {

			@SuppressLint("NewApi")
			@Override
			public void onSuccess(String source, String newTargetName) {
				File file = new File(newTargetName);
				Intent intent = new Intent();
				intent.setAction("android.intent.action.VIEW");
				intent.addCategory("android.intent.category.DEFAULT");
				intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
				PendingIntent pendingIntent = PendingIntent.getActivity(context, 1, intent, PendingIntent.FLAG_ONE_SHOT);
				builder.setContentTitle("让你停不下来背圣经！！");
				builder.setProgress(0, 0, false);
				builder.setContentIntent(pendingIntent);
				builder.setContentText("下载完毕，请点击安装");
				manager.notify(1001, builder.build());
			}

			@Override
			public void onFailure(String source, int errCode, String errMsg) {
				Toast.makeText(context, "请检查网络！", Toast.LENGTH_SHORT).show();
			}

		});
	}

}
