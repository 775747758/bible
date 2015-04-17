package com.orange.net;

import java.io.File;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
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
import android.os.Environment;
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
import com.orange.church.Church;
import com.orange.church.ChurchAdapter;
import com.orange.church.NewMemberAdapter;
import com.orange.church.User;
import com.orange.login.Activity_Register;
import com.orange.map.Activity_Map;

public class UpLoadToten {
	private Context context;
	private FrontiaStorage mCloudStorage;
	private AlertDialog reDialog;
	private ArrayList<Map<String, String>> NewMemberData;
	private ListView newMember_lv;

	public UpLoadToten(Context context, AlertDialog reDialog) {
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

	public void applyChurch(String churchName, String state, String userName, String name, String portraitUri, String pastorName) {
		// mFile.setNativePath(portraitUri);
		// mFile.setRemotePath(portraitUri.substring(portraitUri.lastIndexOf("/")+1,
		// portraitUri.length()));
		final FrontiaData[] datas = new FrontiaData[1];
		datas[0] = new FrontiaData();
		datas[0].put("tableType", "token");
		datas[0].put("churchName", churchName);
		datas[0].put("state", state);
		datas[0].put("userName", userName);
		datas[0].put("name", name);
		datas[0].put("portraitUri", portraitUri);
		//datas[0].put("pastorName", pastorName);
		mCloudStorage.insertData(datas[0], new FrontiaStorageListener.DataInsertListener() {

			@Override
			public void onSuccess() {
				reDialog.dismiss();
				Toast.makeText(context, "申请已发出！", Toast.LENGTH_SHORT).show();

			}

			@Override
			public void onFailure(int errCode, String errMsg) {
				reDialog.dismiss();
				Toast.makeText(context, "注册失败！请检查网络！", Toast.LENGTH_SHORT).show();
				// isSuccessful=false;
			}

		});

	}

	public void getNewMemberData(ListView mMember_lv,final TextView userName_tv,String churchName) {

		NewMemberData = new ArrayList<Map<String, String>>();
		newMember_lv = mMember_lv;
		FrontiaQuery query1 = new FrontiaQuery();
		query1.equals("tableType", "token");
		FrontiaQuery query2 = new FrontiaQuery();
		query2.equals("churchName", churchName);
		
		FrontiaQuery query = query1.and(query2);

		mCloudStorage.findData(query, new DataInfoListener() {

			@Override
			public void onSuccess(List<FrontiaData> dataList) {
				Log.i("shenqing数目", dataList.size() + "");
				StringBuilder sb = new StringBuilder();
				for (FrontiaData d : dataList) {
					if (d.get("state").toString().equals("apply")) {
						userName_tv.setText(d.get("userName").toString());
						Map<String, String> map = new HashMap<String, String>();
						map.put("name", d.get("name").toString());
						File file = new File((String) d.get("portraitUri"));
						String picUri = d.get("portraitUri").toString();
						String picName = picUri.substring(picUri.lastIndexOf("/") + 1);
						String dir = Environment.getExternalStorageDirectory().getAbsoluteFile() + "/圣经流利说";
						Log.i("picUri", picUri+"  "+dir);
						map.put("portraitUri", d.get("portraitUri").toString());
						NewMemberData.add(map);

						if (!file.exists()) {
							Log.i("下载", picName);
							downloadFile(picName, picUri);
						}

					}

				}
				newMember_lv.setAdapter(new NewMemberAdapter(NewMemberData, context));
				reDialog.dismiss();

			}

			@Override
			public void onFailure(int errCode, String errMsg) {
				Log.i("查询失败", "查询失败");

			}
		});

	}
	
	public void downloadFile(String picName,final String picUri) {

		FrontiaFile mFile=new FrontiaFile();
		mFile.setNativePath(picUri);
		mFile.setRemotePath(picName);
		mCloudStorage.downloadFile(mFile, new FileProgressListener() {

			@Override
			public void onProgress(String source, long bytes, long total) {

			}

		}, new FileTransferListener() {

			@Override
			public void onSuccess(String source, String newTargetName) {
				
				//if(from.equals("getChurchData"))
				//{
					newMember_lv.setAdapter(new ChurchAdapter(NewMemberData, context));
				//}
				/*if(from.equals("getAChurch"))
				{
					Bitmap bitmap=BitmapFactory.decodeFile(picUri);
					iv.setImageBitmap(bitmap);
				}*/
				
			}

			@Override
			public void onFailure(String source, int errCode, String errMsg) {
			

			}

		});
	}
	
	public  void updateData(String churchName,String userName) {
		
        FrontiaQuery q0 = new FrontiaQuery();
        q0.equals("tableType", "token");
        FrontiaQuery q1 = new FrontiaQuery();
        q1.equals("churchName", churchName);
        FrontiaQuery q2 = new FrontiaQuery();
        q2.equals("userName", userName);

        FrontiaQuery query = new FrontiaQuery();
        query=q0.and(q1).and(q2);
        
        FrontiaData newData = new FrontiaData();
        newData.put("tableType","token");
        newData.put("state","yes");
        newData.put("churchName",churchName);
        newData.put("userName",userName);

        mCloudStorage.updateData(
						query,
						newData,
						new DataOperationListener() {

							@Override
							public void onSuccess(long count) {
								//Toast.makeText(context, "已添加！", Toast.LENGTH_SHORT).show();
							}

							@Override
							public void onFailure(int errCode, String errMsg) {
								
							}
						});
	}
	
	
	public void isApply(final String churchName, String state, final String userName, final String name, final String portraitUri, final String pastorName)
	{
		
		
		
		FrontiaQuery query1 = new FrontiaQuery();
		query1.equals("tableType", "token");
		FrontiaQuery query2 = new FrontiaQuery();
		query2.equals("churchName", churchName);
		FrontiaQuery query3 = new FrontiaQuery();
		query3.equals("userName", userName);
		
		FrontiaQuery query = query1.and(query2);

		mCloudStorage.findData(query, new DataInfoListener() {

			@Override
			public void onSuccess(List<FrontiaData> dataList) {
				Log.i("shenqing数目", dataList.size() + "");
				StringBuilder sb = new StringBuilder();
				if(dataList.size()==0)
				{
					reDialog.dismiss();
					applyChurch(churchName, "apply", userName,name, portraitUri, pastorName);
					return;
				}
				for (FrontiaData d : dataList) {
					if (d.get("state").toString().equals("apply")) {
						reDialog.dismiss();
						Toast.makeText(context, "您已经申请过该教会，请耐心等候！", Toast.LENGTH_SHORT).show();
					}
					else
					{
						reDialog.dismiss();
						applyChurch(churchName, "apply", userName,name, portraitUri, pastorName);
					}

				}
				

			}

			@Override
			public void onFailure(int errCode, String errMsg) {
				Log.i("查询失败", "查询失败");

			}
		});

	}

}
