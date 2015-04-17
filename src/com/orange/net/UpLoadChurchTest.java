package com.orange.net;

import java.io.File;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Calendar;
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
import android.view.View;
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
import com.orange.church.AdminChurchTestAdapter;
import com.orange.church.Church;
import com.orange.church.ChurchAdapter;
import com.orange.church.ChurchMemberAdapter;
import com.orange.church.ChurchNoticeAdapter;
import com.orange.church.ChurchTestAdapter;
import com.orange.church.NewMemberAdapter;
import com.orange.church.TestResultAdapter;
import com.orange.church.User;
import com.orange.login.Activity_Register;
import com.orange.map.Activity_Map;

public class UpLoadChurchTest {
	private Context context;
	private FrontiaStorage mCloudStorage;
	private AlertDialog reDialog;
	private FrontiaFile mFile;
	private ArrayList<Map<String, String>> UserData;
	private ListView user_lv;
	private int mYear;
	private int mMonth;
	private int mDay;
	private ArrayList<Map<String, String>> testData;
	private ChurchTestAdapter adapter;
	private ArrayList<Map<String, String>> testMember;

	public UpLoadChurchTest(Context context, AlertDialog reDialog) {
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

	public void addAllTest(final List<String> data, String churchName) {

		final FrontiaData[] datas = new FrontiaData[data.size()];
		for (int i = 0; i < data.size(); i++) {
			datas[i] = new FrontiaData();
			datas[i].put("tableType", "public");
			//datas[i].put("churchName", churchName);
			datas[i].put("content", data.get(i));
		}

		for (int i = 0; i < data.size(); i++) {
			// final int idx = i;
			mCloudStorage.insertData(datas[i], new FrontiaStorageListener.DataInsertListener() {

				@Override
				public void onSuccess() {
					Log.i("content", "123");
					
					// Toast.makeText(context, "发布成功！",
					// Toast.LENGTH_SHORT).show();
					// context.startActivity(intent);
				}

				@Override
				public void onFailure(int errCode, String errMsg) {
					Log.i("content", "失败");
					reDialog.dismiss();
					Toast.makeText(context, "提交失败！请检查网络！", Toast.LENGTH_SHORT).show();
					// reDialog.dismiss();
					// Toast.makeText(context, "注册失败！请检查网络！",
					// Toast.LENGTH_SHORT).show();
					// isSuccessful=false;
				}

			});
		}
		reDialog.dismiss();
	}

	public void addNotice(String churchName, String content, final Intent intent) {
		final Calendar c = Calendar.getInstance();
		mYear = c.get(Calendar.YEAR);
		mMonth = c.get(Calendar.MONTH);
		mDay = c.get(Calendar.DAY_OF_MONTH);
		final FrontiaData[] datas = new FrontiaData[1];
		datas[0] = new FrontiaData();
		datas[0].put("tableType", "churchNotice");
		datas[0].put("churchName", churchName);
		datas[0].put("content", content);
		datas[0].put("date", mYear + "." + mMonth + "." + mDay);
		mCloudStorage.insertData(datas[0], new FrontiaStorageListener.DataInsertListener() {

			@Override
			public void onSuccess() {
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

	public void getChurchTest(final ListView test_lv, final String churchName) {
		FrontiaQuery query = new FrontiaQuery();
		query.equals("tableType", "public");

		testData = new ArrayList<Map<String, String>>();
		mCloudStorage.findData(query, new DataInfoListener() {

			@Override
			public void onSuccess(List<FrontiaData> dataList) {
				Log.i("数目", dataList.size() + "");
				StringBuilder sb = new StringBuilder();
				for (FrontiaData d : dataList) {

					Map<String, String> map = new HashMap<String, String>();
					map.put("content", (String) d.get("content"));
					testData.add(map);

				}
				adapter=new ChurchTestAdapter(testData, context); 
				test_lv.setAdapter(adapter);
				reDialog.dismiss();

			}

			@Override
			public void onFailure(int errCode, String errMsg) {
				reDialog.dismiss();
				Toast.makeText(context, "查询失败！请检查网络！", Toast.LENGTH_SHORT).show();

			}
		});

	}
	public String getResult()
	{
		Log.i("adapter的", adapter.getMemberTest()+"JIEGUO");
		return adapter.getMemberTest();
	}

	
	
	public void getChurchMember(final TextView notest_tv,final ListView testMembers, final String churchName,final ArrayList<Map<String, String>> testMember,final AdminChurchTestAdapter adapter) {
		FrontiaQuery query = new FrontiaQuery();
		query.equals("tableType", "churchTestResult");
		mCloudStorage.findData(query, new DataInfoListener() {

			@Override
			public void onSuccess(List<FrontiaData> dataList) {
				Log.i("数目", dataList.size() + "");
				StringBuilder sb = new StringBuilder();
				for (FrontiaData d : dataList) {
					if (d.get("churchName").toString().equals(churchName)) {
						Map<String, String> map = new HashMap<String, String>();
						map.put("name", (String) d.get("name"));
						map.put("result", (String) d.get("result"));
						Log.i("结果", (String) d.get("result")+"没有");
						map.put("date", (String) d.get("date"));
						testMember.add(map);
					}
				}
				if(testMember.size()==0)
				{
					notest_tv.setVisibility(View.VISIBLE);
					testMembers.setVisibility(View.GONE);
				}
				else
				{
					testMembers.setAdapter(adapter);
				}
				reDialog.dismiss();

			}

			@Override
			public void onFailure(int errCode, String errMsg) {
				reDialog.dismiss();
				Toast.makeText(context, "查询失败！请检查网络！", Toast.LENGTH_SHORT).show();
				
			}
		});

	}

	public void deleteData(String churchName, String result,String name,String date) {
		// FrontiaQuery中有很多查询条件，你可以尝试多种查询条件，相当于sql语句中的wher
		FrontiaQuery q1 = new FrontiaQuery();
		q1.equals("tableType", "churchTestResult");
		FrontiaQuery q2 = new FrontiaQuery();
		q2.equals("churchName", churchName);
		FrontiaQuery q3 = new FrontiaQuery();
		q3.equals("result", result);
		FrontiaQuery q4 = new FrontiaQuery();
		q4.equals("name", name);
		FrontiaQuery q5 = new FrontiaQuery();
		q5.equals("date", date);
		FrontiaQuery query = q1.and(q2).and(q3).and(q4).and(q5);

		mCloudStorage.deleteData(query, new DataOperationListener() {

			@Override
			public void onSuccess(long count) {
				Log.i("删除", "shanchu");
				Toast.makeText(context, "删除成功！", Toast.LENGTH_SHORT).show();
				reDialog.dismiss();
				// intent.putExtra("from", 1);
				// context.startActivity(intent);
			}

			@Override
			public void onFailure(int errCode, String errMsg) {
				Toast.makeText(context, "删除失败！请检查网络！", Toast.LENGTH_SHORT).show();
			}
		});
	}
	
	public void commitResult(String churchName, String name,String result, final Intent intent) {
		
	
		final Calendar c = Calendar.getInstance();
		mYear = c.get(Calendar.YEAR);
		mMonth = c.get(Calendar.MONTH)+1;
		mDay = c.get(Calendar.DAY_OF_MONTH);
		final FrontiaData[] datas = new FrontiaData[1];
		datas[0] = new FrontiaData();
		datas[0].put("tableType", "churchTestResult");
		datas[0].put("churchName", churchName);
		datas[0].put("result", result);
		datas[0].put("name", name);
		datas[0].put("date", mYear + "." + mMonth + "." + mDay);
		mCloudStorage.insertData(datas[0], new FrontiaStorageListener.DataInsertListener() {

			@Override
			public void onSuccess() {
				reDialog.dismiss();
				Toast.makeText(context, "提交成功！", Toast.LENGTH_SHORT).show();
				context.startActivity(intent);
			}

			@Override
			public void onFailure(int errCode, String errMsg) {
				reDialog.dismiss();
				Toast.makeText(context, "提交失败！请检查网络！", Toast.LENGTH_SHORT).show();
				// isSuccessful=false;
			}

		});

	}
	
	public void getTestResult(final ListView test_lv, final String result) {
		FrontiaQuery query = new FrontiaQuery();
		query.equals("tableType", "public");
		//FrontiaData[] datas = new FrontiaData[data.size()];
		final String[] resultArray=result.split(",");
		Log.i("分解", result+"  "+resultArray.length+"");
		testData = new ArrayList<Map<String, String>>();
		mCloudStorage.findData(query, new DataInfoListener() {

			@Override
			public void onSuccess(List<FrontiaData> dataList) {
				Log.i("数目", dataList.size() + "");
				StringBuilder sb = new StringBuilder();
				int i=0;
				for (FrontiaData d : dataList) {

					Map<String, String> map = new HashMap<String, String>();
					map.put("content", (String) d.get("content"));
					map.put("isFinish", resultArray[i]);
					testData.add(map);
					i++;
				}
				test_lv.setAdapter(new TestResultAdapter(testData, context));
				reDialog.dismiss();

			}

			@Override
			public void onFailure(int errCode, String errMsg) {
				Toast.makeText(context, "查询失败！请检查网络！", Toast.LENGTH_SHORT).show();
				reDialog.dismiss();

			}
		});

	}

}
