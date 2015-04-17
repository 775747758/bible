package com.orange.net;

import java.io.File;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

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
import android.view.View;
import android.widget.ListView;
import android.widget.RelativeLayout;
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
import com.markupartist.android.widget.PullToRefreshListView;
import com.orange.church.Activity_ChurchMember;
import com.orange.church.Church;
import com.orange.church.ChurchAdapter;
import com.orange.church.ChurchMemberAdapter;
import com.orange.church.ChurchNoticeAdapter;
import com.orange.church.NewMemberAdapter;
import com.orange.church.User;
import com.orange.friendscircle.TalkAdapter;
import com.orange.login.Activity_Register;
import com.orange.map.Activity_Map;
import com.orange.user.MyTalkAdapter;

public class UpLoadDeleteTalk {
	private Context context;
	private FrontiaStorage mCloudStorage;

	public UpLoadDeleteTalk(Context context) {
		this.context = context;

		boolean isInit = Frontia.init(context, "GdZNITcYcI3bhiwKx7duVbR7");
		if (!isInit) {
			Toast.makeText(context, "您的app key 错误", 3).show();
			return;
		}
		// mFile = new FrontiaFile();
		mCloudStorage = Frontia.getStorage();
	}

	
	public void deleteATalk(String uuid) {
		// FrontiaQuery中有很多查询条件，你可以尝试多种查询条件，相当于sql语句中的wher
		FrontiaQuery query1 = new FrontiaQuery();
		query1.equals("talkUuid", uuid);

		FrontiaQuery query2 = new FrontiaQuery();
		query2.equals("UUID", uuid);
		
		FrontiaQuery query =query1.or(query2);
		mCloudStorage.deleteData(query, new DataOperationListener() {

			@Override
			public void onSuccess(long count) {
				Log.i("删除", "shanchu");
				Toast.makeText(context, "删除成功！", Toast.LENGTH_SHORT).show();
			}

			@Override
			public void onFailure(int errCode, String errMsg) {
				Toast.makeText(context, "删除失败！请检查网络！", Toast.LENGTH_SHORT)
						.show();
			}
		});
	}
	
}
