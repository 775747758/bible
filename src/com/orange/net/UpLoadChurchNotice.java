package com.orange.net;

import java.io.File;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import www.orange.updateapk.Conf;
import www.orange.utils.MD5;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.sax.StartElementListener;
import android.util.Log;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.frontia.Frontia;
import com.baidu.frontia.FrontiaData;
import com.baidu.frontia.FrontiaFile;
import com.baidu.frontia.FrontiaQuery;
import com.baidu.frontia.api.FrontiaStorage;
import com.baidu.frontia.api.FrontiaStorageListener;
import com.baidu.frontia.api.FrontiaStorageListener.DataInfoListener;
import com.baidu.frontia.api.FrontiaStorageListener.DataOperationListener;
import com.baidu.frontia.api.FrontiaStorageListener.FileProgressListener;
import com.baidu.frontia.api.FrontiaStorageListener.FileTransferListener;
import com.example.acts.MainActivity;
import com.example.acts.R;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.orange.church.Activity_ChurchMember;
import com.orange.church.Church;
import com.orange.church.ChurchAdapter;
import com.orange.church.ChurchMemberAdapter;
import com.orange.church.ChurchNoticeAdapter;
import com.orange.church.NewMemberAdapter;
import com.orange.church.User;
import com.orange.login.Activity_Register;
import com.orange.map.Activity_Map;

public class UpLoadChurchNotice {
	private Context context;
	private FrontiaStorage mCloudStorage;
	private AlertDialog reDialog;
	private FrontiaFile mFile;
	private ArrayList<Map<String, String>> NewMemberData;
	private ListView newMember_lv;
	private ArrayList<Map<String, String>> UserData;
	private ListView user_lv;
	private int mYear;
	private int mMonth;
	private int mDay;
	private ArrayList<Map<String, String>> noticeData;

	public UpLoadChurchNotice(Context context, AlertDialog reDialog) {
		this.context = context;
		this.reDialog = reDialog;
		boolean isInit = Frontia.init(context, "GdZNITcYcI3bhiwKx7duVbR7");
		if (!isInit) {
			Toast.makeText(context, "您的app key 错误", 3).show();
			return;
		}
		// mFile = new FrontiaFile();
		mCloudStorage = Frontia.getStorage();
	}

	public void addNotice(String churchName,String content,final Intent intent) {
		final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH)+1;
        mDay = c.get(Calendar.DAY_OF_MONTH);
		final FrontiaData[] datas = new FrontiaData[1];
		datas[0] = new FrontiaData();
		datas[0].put("tableType", "churchNotice");
		datas[0].put("churchName", churchName);
		datas[0].put("content", content);
		datas[0].put("date", mYear+"."+mMonth+"."+mDay);
		mCloudStorage.insertData(datas[0], new FrontiaStorageListener.DataInsertListener() {

			@Override
			public void onSuccess() {
				reDialog.dismiss();
				Toast.makeText(context, "发布成功！", Toast.LENGTH_SHORT).show();
				context.startActivity(intent);
			}

			@Override
			public void onFailure(int errCode, String errMsg) {
				reDialog.dismiss();
				Toast.makeText(context, "注册失败！请检查网络！", Toast.LENGTH_SHORT).show();
				// isSuccessful=false;
			}

		});

	}

	public void getChurchNotice(final PullToRefreshListView notice_lv, final String churchName) {
		FrontiaQuery query = new FrontiaQuery();
		query.equals("tableType", "churchNotice");

		noticeData=new ArrayList<Map<String,String>>();
		mCloudStorage.findData(query, new DataInfoListener() {

			@Override
			public void onSuccess(List<FrontiaData> dataList) {
				Log.i("数目", dataList.size() + "");
				StringBuilder sb = new StringBuilder();
				for (FrontiaData d : dataList) {
					if (d.get("churchName").toString().equals(churchName)) {
						Map<String, String> map = new HashMap<String, String>();
						map.put("date", (String) d.get("date"));
						map.put("content", (String) d.get("content"));
						noticeData.add(map);
					}
				}
				notice_lv.onRefreshComplete();
				Collections.reverse(noticeData);
				notice_lv.setAdapter(new ChurchNoticeAdapter(noticeData, context));
				reDialog.dismiss();

			}

			@Override
			public void onFailure(int errCode, String errMsg) {
				notice_lv.onRefreshComplete();
				reDialog.dismiss();
				Toast.makeText(context, "查询失败！请检查网络！", Toast.LENGTH_SHORT).show();

			}
		});

	}

	public void downloadFile(String picName, final String picUri, final String from) {

		mFile.setNativePath(picUri);
		mFile.setRemotePath(picName);
		mCloudStorage.downloadFile(mFile, new FileProgressListener() {

			@Override
			public void onProgress(String source, long bytes, long total) {

			}

		}, new FileTransferListener() {

			@Override
			public void onSuccess(String source, String newTargetName) {
				if (from.equals("getChurchMember")) {
					user_lv.setAdapter(new ChurchAdapter(UserData, context));
				}

			}

			@Override
			public void onFailure(String source, int errCode, String errMsg) {

			}

		});
	}

	public  void deleteData(String churchName,String content) {
		// FrontiaQuery中有很多查询条件，你可以尝试多种查询条件，相当于sql语句中的wher
		FrontiaQuery q1 = new FrontiaQuery();
		q1.equals("tableType", "churchNotice");
		FrontiaQuery q2 = new FrontiaQuery();
		q2.equals("churchName", churchName);
		FrontiaQuery q3 = new FrontiaQuery();
		q3.equals("content", content);
		FrontiaQuery query = q1.and(q2).and(q3);
		

		mCloudStorage.deleteData(query, new DataOperationListener() {

			@Override
			public void onSuccess(long count) {
				Log.i("删除", "shanchu");
				Toast.makeText(context, "删除成功！", Toast.LENGTH_SHORT).show();
				
				//intent.putExtra("from", 1);
				//context.startActivity(intent);
			}

			@Override
			public void onFailure(int errCode, String errMsg) {
				Toast.makeText(context, "删除失败！请检查网络！", Toast.LENGTH_SHORT).show();
			}
		});
	}

}
