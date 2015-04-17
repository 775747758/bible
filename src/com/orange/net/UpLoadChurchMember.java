package com.orange.net;

import java.io.File;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import www.orange.updateapk.Conf;
import www.orange.utils.Date;
import www.orange.utils.MD5;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.app.TabActivity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Message;
import android.sax.StartElementListener;
import android.util.Log;
import android.widget.EditText;
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
import com.orange.church.Activity_ChurchMember;
import com.orange.church.Church;
import com.orange.church.ChurchAdapter;
import com.orange.church.ChurchMemberAdapter;
import com.orange.church.NewMemberAdapter;
import com.orange.church.User;
import com.orange.login.Activity_Register;
import com.orange.map.Activity_Map;

public class UpLoadChurchMember {
	private Context context;
	private FrontiaStorage mCloudStorage;
	private AlertDialog reDialog;
	private FrontiaFile mFile;
	private ArrayList<Map<String, String>> NewMemberData;
	private ListView newMember_lv;
	private ArrayList<Map<String, String>> UserData;
	private ListView user_lv;
	private ChurchMemberAdapter churchMemberAdapter;

	public UpLoadChurchMember(Context context, AlertDialog reDialog) {
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

	public void addMember(String churchName, String userName, String name, String portraitUri, final String from) {
		// mFile.setNativePath(portraitUri);
		// mFile.setRemotePath(portraitUri.substring(portraitUri.lastIndexOf("/")+1,
		// portraitUri.length()));
		final FrontiaData[] datas = new FrontiaData[1];
		datas[0] = new FrontiaData();
		datas[0].put("tableType", "churchMember");
		datas[0].put("churchName", churchName);
		datas[0].put("userName", userName);
		datas[0].put("name", name);
		datas[0].put("portraitUri", portraitUri);
		mCloudStorage.insertData(datas[0], new FrontiaStorageListener.DataInsertListener() {

			@Override
			public void onSuccess() {
				if(from.equals("Activity_UserInfo"))
				{
					reDialog.dismiss();
					Toast.makeText(context, "添加成功！", Toast.LENGTH_SHORT).show();
				}
			}

			@Override
			public void onFailure(int errCode, String errMsg) {
				reDialog.dismiss();
				Toast.makeText(context, "注册失败！请检查网络！", Toast.LENGTH_SHORT).show();
				// isSuccessful=false;
			}

		});

	}

	public void getChurchMember(ListView mUser_lv, String mChurchName, final TextView userName_tv) {

		final String churchName = mChurchName;
		UserData = new ArrayList<Map<String, String>>();
		user_lv = mUser_lv;
		FrontiaQuery query = new FrontiaQuery();
		query.equals("tableType", "churchMember");

		mCloudStorage.findData(query, new DataInfoListener() {

			@Override
			public void onSuccess(List<FrontiaData> dataList) {
				//Log.i("数目", dataList.size() + "");
				StringBuilder sb = new StringBuilder();
				for (FrontiaData d : dataList) {
					if (d.get("churchName").toString().equals(churchName)) {
						//Log.i("jinru", "jinru");

						Map<String, String> map = new HashMap<String, String>();
						map.put("username", (String) d.get("userName"));
						userName_tv.setText((String) d.get("userName"));
						map.put("name", (String) d.get("name"));
						//Log.i("name", (String) d.get("name"));
						
						String picUri = d.get("portraitUri").toString();
						File file = new File(picUri);
						String picName = picUri.substring(picUri.lastIndexOf("/") + 1);

						map.put("portraitUri",picUri);
						UserData.add(map);

						if (!file.exists()) {
							downloadFile(picName, picUri, "getChurchMember");
						}
						
					}
				}
				churchMemberAdapter = new ChurchMemberAdapter(UserData, context);
				user_lv.setAdapter(churchMemberAdapter);
				reDialog.dismiss();

			}

			@Override
			public void onFailure(int errCode, String errMsg) {
				reDialog.dismiss();
				Toast.makeText(context, "查询失败！请检查网络！", Toast.LENGTH_SHORT).show();

			}
		});

	}
	
	public void downloadFile(String picName, final String picUri, final String from) {
		FrontiaFile mFile = new FrontiaFile();
		mFile.setNativePath(picUri);
		mFile.setRemotePath(picName);
		Log.i("检查", picUri+"11111");
		mCloudStorage.downloadFile(mFile, new FileProgressListener() {

			@Override
			public void onProgress(String source, long bytes, long total) {

			}

		}, new FileTransferListener() {

			@Override
			public void onSuccess(String source, String newTargetName) {
				if (from.equals("getChurchMember")) {
					Log.i("数据的数量", UserData.size() + "");
					user_lv.setAdapter(new ChurchMemberAdapter(UserData, context));
				}

			}

			@Override
			public void onFailure(String source, int errCode, String errMsg) {

			}

		});
	}

	public void deleteData(String churchName, String userName, final Intent intent) {
		// FrontiaQuery中有很多查询条件，你可以尝试多种查询条件，相当于sql语句中的wher
		FrontiaQuery q1 = new FrontiaQuery();
		q1.equals("tableType", "churchMember");
		FrontiaQuery q2 = new FrontiaQuery();
		q2.equals("churchName", churchName);
		FrontiaQuery q3 = new FrontiaQuery();
		q3.equals("userName", userName);
		FrontiaQuery query = q1.and(q2).and(q3);

		mCloudStorage.deleteData(query, new DataOperationListener() {

			@Override
			public void onSuccess(long count) {
				Log.i("删除", "shanchu");
				Toast.makeText(context, "删除成功！", Toast.LENGTH_SHORT).show();
				reDialog.dismiss();
				// intent.putExtra("from", 1);
				context.startActivity(intent);
			}

			@Override
			public void onFailure(int errCode, String errMsg) {
				reDialog.dismiss();
				Toast.makeText(context, "删除失败！请检查网络！", Toast.LENGTH_SHORT).show();
			}
		});
	}

	public void isAMember(final String userName) {
		FrontiaQuery query = new FrontiaQuery();
		query.equals("tableType", "churchMember");
		SharedPreferences sp = context.getSharedPreferences("config", Context.MODE_PRIVATE);
		final Editor editor = sp.edit();
		mCloudStorage.findData(query, new DataInfoListener() {
			private int isAMember=0;
			private String churchName="";
			@Override
			public void onSuccess(List<FrontiaData> dataList) {
				Log.i("数目", dataList.size() + "");
				StringBuilder sb = new StringBuilder();
				for (FrontiaData d : dataList) {
					if (d.get("userName").toString().equals(userName))
					{
						isAMember=1;
						churchName=d.get("churchName").toString();
					}
				}
				
				
				if (isAMember==1) {
					Log.i("是", "是");
					editor.putInt("isAMember", 1);
					editor.putString("churchName",churchName);
					editor.commit();
				} else {
					Log.i("是", "是");
					editor.putInt("isAMember", 0);
					editor.putString("churchName","");
					editor.commit();
					
					final SharedPreferences spChurch = context.getSharedPreferences("ChurchInfo", Context.MODE_PRIVATE);
					Editor editorChurch = spChurch.edit();
					if(!editorChurch.putString("churchName","").equals(""))
					{
						editorChurch.putString("churchName","");
						editorChurch.putString("pastorName", "");
						editorChurch.putString("churchType","");
						editorChurch.putString("churchMission", "");
						editorChurch.putString("churchSight", "");
						editorChurch.putString("churchIconUri", "");
						editorChurch.putString("churchLocation", "");
						editorChurch.putString("churchCount", "");
						editorChurch.putString("churchCreateDate", "");
						editorChurch.putString("churchNotice","");
						editorChurch.commit();
					
					}
					
				}

			}

			@Override
			public void onFailure(int errCode, String errMsg) {
				Log.i("验证是否是一个成员失败", "验证是否是一个成员失败");
			}
		});
	}
	
	public  void deleteAllMember(String churchName,final Activity activity) {
		// FrontiaQuery中有很多查询条件，你可以尝试多种查询条件，相当于sql语句中的wher
		FrontiaQuery q1 = new FrontiaQuery();
		q1.equals("tableType", "churchMember");
		FrontiaQuery q2 = new FrontiaQuery();
		q2.equals("churchName", churchName);
		FrontiaQuery query = q1.and(q2);
		

		mCloudStorage.deleteData(query, new DataOperationListener() {

			@Override
			public void onSuccess(long count) {
				Log.i("删除", "shanchu");
				
				Toast.makeText(context, "删除成功！", Toast.LENGTH_SHORT).show();
				SharedPreferences sp =context.getSharedPreferences("config", Context.MODE_PRIVATE);
				Editor editor=sp.edit();
				editor.putInt("isAMember", 0);
				editor.commit();
				
				SharedPreferences spChurch = context.getSharedPreferences("ChurchInfo", Context.MODE_PRIVATE);
				Editor editorChurch = spChurch.edit();
				editorChurch.putString("churchName", "");
				editorChurch.putString("pastorName", "");
				editorChurch.putString("churchType", "");
				editorChurch.putString("churchMission","");
				editorChurch.putString("churchSight", "");
				editorChurch.putString("churchIconUri", "");
				editorChurch.putString("churchLocation", "");
				editorChurch.putString("churchCount","");
				editorChurch.putString("churchCreateDate","");
				editorChurch.putString("churchNotice","");
				editorChurch.commit();
				
				//通知tabhost更新
				MainActivity t= (MainActivity) (activity.getParent());                  
				Message message = new Message();  
				message.what=100;
				t.myHandler.sendMessage(message);
				
				reDialog.dismiss();
				//intent.putExtra("from", 1);
				//context.startActivity(intent);
			}

			@Override
			public void onFailure(int errCode, String errMsg) {
				reDialog.dismiss();
				Toast.makeText(context, "删除失败！请检查网络！", Toast.LENGTH_SHORT).show();
			}
		});
	}
	
	public  void deleteOneMember(String churchName,String userName,final Activity activity) {
		// FrontiaQuery中有很多查询条件，你可以尝试多种查询条件，相当于sql语句中的wher
		FrontiaQuery q1 = new FrontiaQuery();
		q1.equals("tableType", "churchMember");
		FrontiaQuery q2 = new FrontiaQuery();
		q2.equals("churchName", churchName);
		FrontiaQuery q3 = new FrontiaQuery();
		q3.equals("userName", userName);
		FrontiaQuery query = q1.and(q2).and(q3);
		

		mCloudStorage.deleteData(query, new DataOperationListener() {

			@Override
			public void onSuccess(long count) {
				Log.i("删除", "shanchu");
				
				Toast.makeText(context, "删除成功！", Toast.LENGTH_SHORT).show();
				SharedPreferences sp =context.getSharedPreferences("config", Context.MODE_PRIVATE);
				Editor editor=sp.edit();
				editor.putInt("isAMember", 0);
				editor.commit();
				
				SharedPreferences spChurch = context.getSharedPreferences("ChurchInfo", Context.MODE_PRIVATE);
				Editor editorChurch = spChurch.edit();
				editorChurch.putString("churchName", "");
				editorChurch.putString("pastorName", "");
				editorChurch.putString("churchType", "");
				editorChurch.putString("churchMission","");
				editorChurch.putString("churchSight", "");
				editorChurch.putString("churchIconUri", "");
				editorChurch.putString("churchLocation", "");
				editorChurch.putString("churchCount","");
				editorChurch.putString("churchCreateDate","");
				editorChurch.putString("churchNotice","");
				editorChurch.commit();
				
				//通知tabhost更新
				MainActivity t= (MainActivity) (activity.getParent());                  
				Message message = new Message();  
				message.what=100;
				t.myHandler.sendMessage(message);
				//intent.putExtra("from", 1);
				//context.startActivity(intent);
				reDialog.dismiss();
			}

			@Override
			public void onFailure(int errCode, String errMsg) {
				reDialog.dismiss();
				Toast.makeText(context, "删除失败！请检查网络！", Toast.LENGTH_SHORT).show();
			}
		});
	}

}
