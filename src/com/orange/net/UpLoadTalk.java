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
import com.orange.friendscircle.Activity_PublishTalk;
import com.orange.friendscircle.TalkAdapter;
import com.orange.login.Activity_Register;
import com.orange.map.Activity_Map;
import com.orange.user.MyTalkAdapter;

public class UpLoadTalk {
	private Context context;
	private FrontiaStorage mCloudStorage;
	private AlertDialog reDialog;
	private FrontiaFile mFile;
	private int mYear;
	private int mMonth;
	private int mDay;
	private ArrayList<Map<String, String>> talkData;
	private ArrayList<Map<String, String>> myChurchTalkData;
	private PullToRefreshListView talk_lv;
	private ArrayList<Map<String, String>> AllTalkData;
	private int mHour;
	private int mMinute;
	RelativeLayout send_layout;
	private ArrayList<Map<String, String>> commentData;
	public TalkAdapter talkAdapter;
	private Intent intent;
	protected MyTalkAdapter myTalkAdapter;

	public UpLoadTalk(Context context, AlertDialog reDialog,RelativeLayout send_layout,Intent intent) {
		this.send_layout=send_layout;
		this.context = context;
		this.reDialog = reDialog;
		this.intent=intent;
		boolean isInit = Frontia.init(context, "GdZNITcYcI3bhiwKx7duVbR7");
		if (!isInit) {
			Toast.makeText(context, "您的app key 错误", 3).show();
			return;
		}
		// mFile = new FrontiaFile();
		mCloudStorage = Frontia.getStorage();
	}

	public void addTalk(String churchName, String content, String name,
			String userName, String portraitUri, String city,
			final Intent intent, final Activity activity) {
		UUID uuid = UUID.randomUUID(); 
		final Calendar c = Calendar.getInstance();
		mYear = c.get(Calendar.YEAR);
		mMonth = c.get(Calendar.MONTH) + 1;
		mDay = c.get(Calendar.DAY_OF_MONTH);
		mHour=c.get(Calendar.HOUR_OF_DAY);
		mMinute=c.get(Calendar.MINUTE);
		final FrontiaData[] datas = new FrontiaData[1];
		datas[0] = new FrontiaData();
		datas[0].put("tableType", "talk");
		datas[0].put("UUID", uuid.toString());
		datas[0].put("churchName", churchName);
		datas[0].put("name", name);
		datas[0].put("userName", userName);
		datas[0].put("portraitUri", portraitUri);
		datas[0].put("content", content);
		datas[0].put("city", city);
		datas[0].put("year", mYear+"");
		datas[0].put("month", mMonth+"");
		datas[0].put("day", mDay+"");
		datas[0].put("hour", mHour+"");
		datas[0].put("minute", mMinute+"");
		
		mCloudStorage.insertData(datas[0],
				new FrontiaStorageListener.DataInsertListener() {

					@Override
					public void onSuccess() {
						reDialog.dismiss();
						Toast.makeText(context, "发布成功！", Toast.LENGTH_SHORT)
								.show();
						context.startActivity(intent);
						activity.finish();
					}

					@Override
					public void onFailure(int errCode, String errMsg) {
						reDialog.dismiss();
						Toast.makeText(context, "注册失败！请检查网络！",
								Toast.LENGTH_SHORT).show();
						// isSuccessful=false;
					}

				});

	}
	
	public void addComment(String comment, String name,
			String userName,String talkUuid, final RelativeLayout send_layout2) {
		UUID uuid = UUID.randomUUID(); 
		
		final FrontiaData[] datas = new FrontiaData[1];
		datas[0] = new FrontiaData();
		datas[0].put("tableType", "comment");
		datas[0].put("UUID", uuid.toString());
		datas[0].put("talkUuid", talkUuid);
		datas[0].put("name", name);
		datas[0].put("userName", userName);
		datas[0].put("comment", comment);
		
		mCloudStorage.insertData(datas[0],
				new FrontiaStorageListener.DataInsertListener() {

					@Override
					public void onSuccess() {
						reDialog.dismiss();
						Toast.makeText(context, "评论成功！", Toast.LENGTH_SHORT)
								.show();
						send_layout2.setVisibility(View.GONE);
					}

					@Override
					public void onFailure(int errCode, String errMsg) {
						reDialog.dismiss();
						Toast.makeText(context, "注册失败！请检查网络！",
								Toast.LENGTH_SHORT).show();
						send_layout2.setVisibility(View.GONE);
						// isSuccessful=false;
					}

				});

	}
	
	public void getComment(final PullToRefreshListView talkLv,final TalkAdapter talkAdapter) {

		talk_lv = talkLv;
		FrontiaQuery query = new FrontiaQuery();
		query.equals("tableType", "comment");
		commentData = new ArrayList<Map<String, String>>();
		mCloudStorage.findData(query, new DataInfoListener() {

			@Override
			public void onSuccess(List<FrontiaData> dataList) {
				Log.i("数目", dataList.size() + "");
				StringBuilder sb = new StringBuilder();
				for (FrontiaData d : dataList) {
					Map<String, String> map = new HashMap<String, String>();
					map.put("UUID", (String) d.get("UUID"));
					map.put("talkUuid", (String) d.get("talkUuid"));
					map.put("comment", (String) d.get("comment"));
					map.put("name", (String) d.get("name"));
					map.put("userName", (String) d.get("userName"));
					commentData.add(map);
				}
				
				/*talk_lv.onRefreshComplete();
				talk_lv.setAdapter(new TalkAdapter(myChurchTalkData, context,send_layout));
				reDialog.dismiss();*/
				talkAdapter.addCommentData(commentData);
				talkLv.setAdapter(talkAdapter);
				talkLv.onRefreshComplete();
			}

			@Override
			public void onFailure(int errCode, String errMsg) {
				reDialog.dismiss();
				Toast.makeText(context, "查询失败！请检查网络！", Toast.LENGTH_SHORT)
						.show();

			}
		});

	}
	
	public void getComment(final ListView talkLv,final MyTalkAdapter talkAdapter) {

		FrontiaQuery query = new FrontiaQuery();
		query.equals("tableType", "comment");
		commentData = new ArrayList<Map<String, String>>();
		mCloudStorage.findData(query, new DataInfoListener() {

			@Override
			public void onSuccess(List<FrontiaData> dataList) {
				Log.i("数目", dataList.size() + "");
				StringBuilder sb = new StringBuilder();
				for (FrontiaData d : dataList) {
					Map<String, String> map = new HashMap<String, String>();
					map.put("UUID", (String) d.get("UUID"));
					map.put("talkUuid", (String) d.get("talkUuid"));
					map.put("comment", (String) d.get("comment"));
					map.put("name", (String) d.get("name"));
					map.put("userName", (String) d.get("userName"));
					commentData.add(map);
				}
				
				/*talk_lv.onRefreshComplete();
				talk_lv.setAdapter(new TalkAdapter(myChurchTalkData, context,send_layout));
				reDialog.dismiss();*/
				talkAdapter.addCommentData(commentData);
				talkLv.setAdapter(talkAdapter);
			}

			@Override
			public void onFailure(int errCode, String errMsg) {
				reDialog.dismiss();
				Toast.makeText(context, "查询失败！请检查网络！", Toast.LENGTH_SHORT)
						.show();

			}
		});

	}


	public void getMyChurchTalk(final PullToRefreshListView talkLv, final String churchName) {

		talk_lv = talkLv;
		FrontiaQuery query1 = new FrontiaQuery();
		query1.equals("tableType", "talk");

		FrontiaQuery query2 = new FrontiaQuery();
		query2.equals("churchName", churchName);

		FrontiaQuery query = query1.and(query2);

		myChurchTalkData = new ArrayList<Map<String, String>>();
		mCloudStorage.findData(query, new DataInfoListener() {

			@Override
			public void onSuccess(List<FrontiaData> dataList) {
				Log.i("数目", dataList.size() + "");
				StringBuilder sb = new StringBuilder();
				for (FrontiaData d : dataList) {
					Map<String, String> map = new HashMap<String, String>();
					map.put("UUID", (String) d.get("UUID"));
					map.put("year", (String) d.get("year"));
					map.put("month", (String) d.get("month"));
					map.put("day", (String) d.get("day"));
					map.put("hour", (String) d.get("hour"));
					map.put("minute", (String) d.get("minute"));
					map.put("content", (String) d.get("content"));
					map.put("portraitUri", (String) d.get("portraitUri"));
					map.put("name", (String) d.get("name"));
					map.put("userName", (String) d.get("userName"));
					map.put("city", (String) d.get("city"));
					map.put("churchName", (String) d.get("churchName"));
					myChurchTalkData.add(map);

					File file = new File((String) d.get("portraitUri"));
					String picUri = d.get("portraitUri").toString();
					String picName = picUri.substring(picUri.lastIndexOf("/") + 1);

					if (!file.exists()) {
						Log.i("下载", picName);
						downloadFile(picName, picUri, "getMyChurchTalk");
					}
				}
				Collections.reverse(AllTalkData);
				talk_lv.onRefreshComplete();
				talk_lv.setAdapter(new TalkAdapter(myChurchTalkData, context,send_layout,intent));
				reDialog.dismiss();

			}

			@Override
			public void onFailure(int errCode, String errMsg) {
				reDialog.dismiss();
				Toast.makeText(context, "查询失败！请检查网络！", Toast.LENGTH_SHORT)
						.show();

			}
		});

	}

	public void getAllTalk(final PullToRefreshListView alltalkLv,final int count,final int scrolledX,final int scrolledY,final View footView) {

		FrontiaQuery query = new FrontiaQuery();
		query.equals("tableType", "talk");
		
		
		AllTalkData = new ArrayList<Map<String, String>>();
		mCloudStorage.findData(query, new DataInfoListener() {

			@Override
			public void onSuccess(List<FrontiaData> dataList) {
				Log.i("数目", dataList.size() + "");
				StringBuilder sb = new StringBuilder();
				int i=0;
				int max=0;
				int min=0;
				if(dataList.size()<=count)
				{
					max=dataList.size();
					min=0;
				}
				else
				{
					min=dataList.size()-count;
					max=count;
				}
				for(int j=dataList.size()-1;j>min;j--)
				{
					FrontiaData d=dataList.get(j);
					Map<String, String> map = new HashMap<String, String>();
					map.put("UUID", (String) d.get("UUID"));
					map.put("year", (String) d.get("year"));
					map.put("month", (String) d.get("month"));
					map.put("day", (String) d.get("day"));
					map.put("hour", (String) d.get("hour"));
					map.put("minute", (String) d.get("minute"));
					map.put("content", (String) d.get("content"));
					map.put("portraitUri", (String) d.get("portraitUri"));
					map.put("name", (String) d.get("name"));
					map.put("userName", (String) d.get("userName"));
					map.put("city", (String) d.get("city"));
					map.put("churchName", (String) d.get("churchName"));
					AllTalkData.add(map);

					File file = new File((String) d.get("portraitUri"));
					String picUri = d.get("portraitUri").toString();
					String picName = picUri.substring(picUri.lastIndexOf("/") + 1);

					if (!file.exists()) {
						Log.i("下载", picName);
						downloadFile(picName, picUri, "getAllTalk");
					}
				}
				
				//Collections.reverse(AllTalkData);

				talkAdapter=new TalkAdapter(AllTalkData, context,send_layout,intent);
				getComment(alltalkLv, talkAdapter);

				reDialog.dismiss();
				Log.i("scrolled", scrolledX+"  "+scrolledY);
				//alltalkLv.setSelection(count);
				//alltalkLv.scrollTo(scrolledX, scrolledY);
				alltalkLv.removeFooterView(footView);
			}

			@Override
			public void onFailure(int errCode, String errMsg) {
				reDialog.dismiss();
				Toast.makeText(context, "查询失败！请检查网络！", Toast.LENGTH_SHORT)
						.show();

			}
		});

	}
	
	public void getOwnTalk(final ListView alltalkLv,String userName,final TextView notalk_tv) {

		FrontiaQuery query1 = new FrontiaQuery();
		query1.equals("tableType", "talk");
		
		FrontiaQuery query2 = new FrontiaQuery();
		query2.equals("userName", userName);

		FrontiaQuery query = query1.and(query2);
		AllTalkData = new ArrayList<Map<String, String>>();
		mCloudStorage.findData(query, new DataInfoListener() {

			@Override
			public void onSuccess(List<FrontiaData> dataList) {
				Log.i("数目", dataList.size() + "");
				if(dataList.size()==0)
				{
					notalk_tv.setVisibility(View.VISIBLE);
					alltalkLv.setVisibility(View.GONE);
					reDialog.dismiss();
					return;
				}
				for (FrontiaData d : dataList) 
				{
					Map<String, String> map = new HashMap<String, String>();
					map.put("UUID", (String) d.get("UUID"));
					map.put("year", (String) d.get("year"));
					map.put("month", (String) d.get("month"));
					map.put("day", (String) d.get("day"));
					map.put("hour", (String) d.get("hour"));
					map.put("minute", (String) d.get("minute"));
					map.put("content", (String) d.get("content"));
					map.put("portraitUri", (String) d.get("portraitUri"));
					map.put("name", (String) d.get("name"));
					map.put("userName", (String) d.get("userName"));
					map.put("city", (String) d.get("city"));
					map.put("churchName", (String) d.get("churchName"));
					AllTalkData.add(map);
					
					Collections.reverse(AllTalkData);
					File file = new File((String) d.get("portraitUri"));
					String picUri = d.get("portraitUri").toString();
					String picName = picUri.substring(picUri.lastIndexOf("/") + 1);

					if (!file.exists()) {
						Log.i("下载", picName);
						downloadFile(picName, picUri, "getAllTalk");
					}
				}
				
				//Collections.reverse(AllTalkData);

				myTalkAdapter=new MyTalkAdapter(AllTalkData, context,intent);
				getComment(alltalkLv, myTalkAdapter);

				reDialog.dismiss();
				
			}
			@Override
			public void onFailure(int errCode, String errMsg) {
				reDialog.dismiss();
				Toast.makeText(context, "查询失败！请检查网络！", Toast.LENGTH_SHORT)
						.show();

			}
		});

	}

	public void downloadFile(String picName, final String picUri,
			final String from) {
		mFile = new FrontiaFile();
		mFile.setNativePath(picUri);
		mFile.setRemotePath(picName);
		mCloudStorage.downloadFile(mFile, new FileProgressListener() {

			@Override
			public void onProgress(String source, long bytes, long total) {

			}

		}, new FileTransferListener() {

			@Override
			public void onSuccess(String source, String newTargetName) {
				/*
				 * if (from.equals("getMyChurchTalk")) { talk_lv.setAdapter(new
				 * TalkAdapter(myChurchTalkData, context)); }
				 */

			}

			@Override
			public void onFailure(String source, int errCode, String errMsg) {

			}

		});
	}

	/*public void deleteData(String churchName, String content) {
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

				// intent.putExtra("from", 1);
				// context.startActivity(intent);
			}

			@Override
			public void onFailure(int errCode, String errMsg) {
				Toast.makeText(context, "删除失败！请检查网络！", Toast.LENGTH_SHORT)
						.show();
			}
		});
	}
*/
	
	public void deleteAllData() {
		// FrontiaQuery中有很多查询条件，你可以尝试多种查询条件，相当于sql语句中的wher
		FrontiaQuery query = new FrontiaQuery();
		query.equals("tableType", "talk");
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
	
	public void deleteAllCommentData() {
		// FrontiaQuery中有很多查询条件，你可以尝试多种查询条件，相当于sql语句中的wher
		FrontiaQuery query = new FrontiaQuery();
		query.equals("tableType", "comment");
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
	
	public void load()
	{
		if (AllTalkData.size() <= 30) {
			talkAdapter =new TalkAdapter(AllTalkData, context,send_layout,intent);
		} else {
			talkAdapter = new TalkAdapter(AllTalkData.subList(0, 30), context,send_layout,intent);
		}
	}
}
