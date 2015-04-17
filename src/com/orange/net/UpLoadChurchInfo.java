package com.orange.net;

import java.io.File;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import www.orange.utils.Date;
import www.orange.utils.MD5;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
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
import com.baidu.frontia.api.FrontiaStorageListener.FileOperationListener;
import com.baidu.frontia.api.FrontiaStorageListener.FileProgressListener;
import com.baidu.frontia.api.FrontiaStorageListener.FileTransferListener;
import com.example.acts.MainActivity;
import com.example.acts.R;
import com.orange.church.Church;
import com.orange.church.ChurchAdapter;
import com.orange.church.ChurchMemberAdapter;
import com.orange.map.Activity_Map;
import com.orange.view.CircleImageView;

public class UpLoadChurchInfo {
	private Context context;
	private FrontiaStorage mCloudStorage;
	private AlertDialog reDialog;
	private FrontiaFile mFile;
	private String churchIconUri;
	private List<Map<String, String>> ChurchData;
	private ListView church_lv; 
	CircleImageView iv;

	public UpLoadChurchInfo(Context context, AlertDialog reDialog) {
		this.context = context;
		this.reDialog = reDialog;
		boolean isInit = Frontia.init(context, "GdZNITcYcI3bhiwKx7duVbR7");
		if (!isInit) {
			Toast.makeText(context, "您的app key 错误", 3).show();
			return;
		}
		mFile = new FrontiaFile();
		mCloudStorage = Frontia.getStorage();
	}

	public void isJoinAChurch(String  mPastorName, Intent intent) {
		final String pastorName = mPastorName;

		final Intent mintent = intent;
		// FrontiaQuery中有很多查询条件，你可以尝试多种查询条件，相当于sql语句中的where
		FrontiaQuery query = new FrontiaQuery();
		query.equals("Name", pastorName);

		mCloudStorage.findData(query, new DataInfoListener() {

			@Override
			public void onSuccess(List<FrontiaData> dataList) {
				StringBuilder sb = new StringBuilder();
				int i = 0;
				for (FrontiaData d : dataList) {
					if (d.get("Name").toString().equals(pastorName)) {
						i = 1;
						reDialog.dismiss();
						Log.i("查询成功", "查询成功");
						Toast.makeText(context, "教会名字重复，请更换教会名称！", Toast.LENGTH_SHORT).show();
						return;

					}
				}
				if (i == 0) {
					reDialog.dismiss();
					context.startActivity(mintent);

				}

			}

			@Override
			public void onFailure(int errCode, String errMsg) {
				Log.i("查询失败", "查询失败");

			}
		});

	}
	
	public void isExistChurch(String  mChurchName, final Intent intent) {
		final String churchName = mChurchName;

		final Intent mintent = intent;
		// FrontiaQuery中有很多查询条件，你可以尝试多种查询条件，相当于sql语句中的where
		FrontiaQuery query = new FrontiaQuery();
		query.equals("tableType", "Church");

		mCloudStorage.findData(query, new DataInfoListener() {

			@Override
			public void onSuccess(List<FrontiaData> dataList) {
				StringBuilder sb = new StringBuilder();
				int i = 0;
				for (FrontiaData d : dataList) {
					if (d.get("churchName").toString().equals(churchName)) {
						i = 1;
						reDialog.dismiss();
						Log.i("查询成功", "查询成功");
						Toast.makeText(context, "教会名字重复，请更换教会名称！", Toast.LENGTH_SHORT).show();
						return;

					}
				}
				if (i == 0) {
					reDialog.dismiss();
					intent.putExtra("churchName", churchName);
					context.startActivity(mintent);

				}

			}

			@Override
			public void onFailure(int errCode, String errMsg) {
				reDialog.dismiss();
				Toast.makeText(context, "查询失败，请检查网络！", Toast.LENGTH_SHORT).show();

			}
		});

	}

	public void addData(Church mChurch) {
		final Church church = mChurch;
		churchIconUri = church.getChurchIconUri();
		mFile.setNativePath(church.getChurchIconUri());
		mFile.setRemotePath(churchIconUri.substring(churchIconUri.lastIndexOf("/") + 1, churchIconUri.length()));
		uploadFile();
		final FrontiaData[] datas = new FrontiaData[1];
		datas[0] = new FrontiaData();
		datas[0].put("churchName", church.getChurchName());
		datas[0].put("pastorName", church.getPastorName());
		datas[0].put("churchType", church.getChurchType());
		datas[0].put("churchMission", church.getChurchMission());
		datas[0].put("churchSight", church.getChurchSight());
		datas[0].put("churchIconUri", church.getChurchIconUri());
		
		final SharedPreferences sp = context.getSharedPreferences("ChurchInfo", Context.MODE_PRIVATE);
		final SharedPreferences sp1 = context.getSharedPreferences("UserInfo", Context.MODE_PRIVATE);
		datas[0].put("churchLocation", sp1.getString("city", ""));
		datas[0].put("churchLatitude", sp1.getInt("latitude", 0));
		datas[0].put("churchLongtitude", sp1.getInt("longtitude", 0));
		datas[0].put("tableType", church.getTableType());
		datas[0].put("churchCreateDate", Date.getCurrentDate());
		datas[0].put("churchCount", "0");
		datas[0].put("churchNotice", "");
		mCloudStorage.insertData(datas[0], new FrontiaStorageListener.DataInsertListener() {

			@Override
			public void onSuccess() {
				new Thread(new Runnable() {

					@Override
					public void run() {
						Editor editor = sp.edit();
						editor.putString("churchName", church.getChurchName());
						editor.putString("pastorName", church.getPastorName());
						editor.putString("churchType", church.getChurchType());
						editor.putString("churchMission", church.getChurchMission());
						editor.putString("churchSight", church.getChurchSight());
						editor.putString("churchIconUri", church.getChurchIconUri());
						editor.putString("churchLocation", sp1.getString("city", ""));
						editor.putString("churchCount", "0");
						editor.putString("churchCreateDate", Date.getCurrentDate());
						editor.putString("churchNotice","");
						editor.commit();
						
					}
				}).start();
				ProgressDialog reDialogTemp = new ProgressDialog(context);
				reDialogTemp.setMessage("正在查询中...");
				SharedPreferences sp = context.getSharedPreferences("ChurchInfo", Context.MODE_PRIVATE);
				UpLoadChurchMember upLoadChurchMember=new UpLoadChurchMember(context, reDialog);
				upLoadChurchMember.addMember(church.getChurchName(), sp1.getString("userName", ""),church.getPastorName(),sp1.getString("portraitUri", ""),"UpLoadChurchInfo");
				reDialog.dismiss();
				Toast.makeText(context, "创建教会成功！", Toast.LENGTH_SHORT).show();
				Intent intent = new Intent(context, MainActivity.class);
				intent.putExtra("from", 1);
				context.startActivity(intent);
				// overridePendingTransition(R.anim.zoomin, R.anim.zoomout);

			}

			@Override
			public void onFailure(int errCode, String errMsg) {
				reDialog.dismiss();
				Toast.makeText(context, "创建教会失败！请检查网络！", Toast.LENGTH_SHORT).show();
				// isSuccessful=false;
			}

		});

	}

	protected void uploadFile() {
		mCloudStorage.uploadFile(mFile, new FileProgressListener() {
			@Override
			public void onProgress(String source, long bytes, long total) {

			}
		}, new FileTransferListener() {
			@Override
			public void onSuccess(String source, String newTargetName) {
				Log.i("上传头像成功", "上传头像成功");
			}

			@Override
			public void onFailure(String source, int errCode, String errMsg) {
				Log.i("上传头像失败", "上传头像失败");
			}
		});
	}

	public void getChurchData(ListView mChurch_lv, String mChurchName) {
		
		final String churchName = mChurchName;
		ChurchData = new ArrayList<Map<String, String>>();
		church_lv = mChurch_lv;
		FrontiaQuery query = new FrontiaQuery();
		query.equals("tableType", "Church");

		mCloudStorage.findData(query, new DataInfoListener() {

			@Override
			public void onSuccess(List<FrontiaData> dataList) {
				Log.i("数目", dataList.size() + "");
				StringBuilder sb = new StringBuilder();
				int i=0;
				for (FrontiaData d : dataList) {
					if (d.get("churchName").toString().contains(churchName)) {
						Map<String, String> map = new HashMap<String, String>();
						map.put("churchName", (String) d.get("churchName"));
						File file = new File((String) d.get("churchIconUri"));
						String picUri=d.get("churchIconUri").toString();
						String picName=picUri.substring(picUri.lastIndexOf("/")+1);
						Log.i("图片", picName);
						
						map.put("churchIconUri", d.get("churchIconUri").toString());
						ChurchData.add(map);

						if (!file.exists()) {
							Log.i("下载", picName);
							downloadFile(picName,picUri,"getChurchData");
						}
						
					}
					i++;
				}
				church_lv.setAdapter(new ChurchAdapter(ChurchData, context));
				reDialog.dismiss();

			}

			@Override
			public void onFailure(int errCode, String errMsg) {
				reDialog.dismiss();
				Toast.makeText(context, "查询失败！请检查网络！", Toast.LENGTH_SHORT).show();

			}
		});

	}

	public void downloadFile(String picName,final String picUri,final String from) {

		mFile.setNativePath(picUri);
		mFile.setRemotePath(picName);
		mCloudStorage.downloadFile(mFile, new FileProgressListener() {

			@Override
			public void onProgress(String source, long bytes, long total) {

			}

		}, new FileTransferListener() {

			@Override
			public void onSuccess(String source, String newTargetName) {
				
				if(from.equals("getChurchData"))
				{
					church_lv.setAdapter(new ChurchAdapter(ChurchData, context));
				}
				if(from.equals("getAChurch"))
				{
					Bitmap bitmap=BitmapFactory.decodeFile(picUri);
					iv.setImageBitmap(bitmap);
				}
				reDialog.dismiss();
				
			}

			@Override
			public void onFailure(String source, int errCode, String errMsg) {
			
					
			}

		});
	}
	
	public void getAChurch(final String  churchName,final List<View> views) {
		// FrontiaQuery中有很多查询条件，你可以尝试多种查询条件，相当于sql语句中的where
		FrontiaQuery query = new FrontiaQuery();
		query.equals("tableType", "Church");
		mCloudStorage.findData(query, new DataInfoListener() {

			@Override
			public void onSuccess(List<FrontiaData> dataList) {
				StringBuilder sb = new StringBuilder();
				int i = 0;
				for (FrontiaData d : dataList) {
					if (d.get("churchName").toString().equals(churchName)) {
						String dir = Environment.getExternalStorageDirectory().getAbsoluteFile() + "/圣经流利说";
						File file = new File(dir + "/church_"+churchName.trim()+".jpg");
						if(file.exists())
						{
							file.delete();
						}
						downloadFile( "/church_"+churchName.trim()+".jpg", file.getAbsoluteFile().toString(), "getAChurch");
						iv=(CircleImageView)views.get(0);
						TextView name_tv=(TextView)views.get(1);
						TextView pastorName_tv=(TextView)views.get(2);
						TextView type_tv=(TextView)views.get(3);
						TextView createdate_tv=(TextView)views.get(4);
						TextView area_tv=(TextView)views.get(5);
						TextView count_tv=(TextView)views.get(6);
						TextView mission_tv=(TextView)views.get(7);
						TextView sight_tv=(TextView)views.get(8);
						TextView notice_tv=(TextView)views.get(9);
						
						name_tv.setText(churchName);
						pastorName_tv.setText(d.get("pastorName").toString());
						type_tv.setText(d.get("churchType").toString());
						createdate_tv.setText(d.get("churchCreateDate").toString());
						area_tv.setText(d.get("churchLocation").toString());
						Log.i("地区", d.get("churchLocation").toString()+"bgikbibi");
						if(d.get("churchCount")==null)
						{
							count_tv.setText("0");
						}
						else
						{
							count_tv.setText(d.get("churchCount").toString());
						}
						
						mission_tv.setText(d.get("churchMission").toString());
						sight_tv.setText(d.get("churchSight").toString());
						if(d.get("churchNotice")==null)
						{
							notice_tv.setText("");
						}
						else
						{
							notice_tv.setText(d.get("churchNotice").toString());
						}
						//reDialog.dismiss();
						return;

					}
				}
			}

			@Override
			public void onFailure(int errCode, String errMsg) {
				reDialog.dismiss();
				Toast.makeText(context, "查询失败！请检查网络！", Toast.LENGTH_SHORT).show();
			}
		});

	}
	
	public void isAMember(final String userName) {
		FrontiaQuery query = new FrontiaQuery();
		query.equals("tableType", "churchMember");
		
		/*datas[0] = new FrontiaData();
		datas[0].put("tableType", "churchMember");
		datas[0].put("churchName", churchName);
		datas[0].put("userName", userName);
		datas[0].put("name", name);
		datas[0].put("portraitUri", portraitUri);*/

		mCloudStorage.findData(query, new DataInfoListener() {

			@Override
			public void onSuccess(List<FrontiaData> dataList) {
				Log.i("数目", dataList.size() + "");
				StringBuilder sb = new StringBuilder();
				for (FrontiaData d : dataList) {
					if (d.get("userName").toString().equals(userName)) {
						getAChurch(d.get("churchName").toString());
					}
				}
				reDialog.dismiss();

			}

			@Override
			public void onFailure(int errCode, String errMsg) {
				Log.i("查询失败", "查询失败");

			}
		});

	}
	
	public void getAChurch(final String  churchName) {
		final SharedPreferences sp = context.getSharedPreferences("ChurchInfo", Context.MODE_PRIVATE);
		// FrontiaQuery中有很多查询条件，你可以尝试多种查询条件，相当于sql语句中的where
		FrontiaQuery query = new FrontiaQuery();
		query.equals("tableType", "Church");
		mCloudStorage.findData(query, new DataInfoListener() {

			@Override
			public void onSuccess(List<FrontiaData> dataList) {
				StringBuilder sb = new StringBuilder();
				int i = 0;
				for (FrontiaData d : dataList) {
					if (d.get("churchName").toString().equals(churchName)) {
						String dir = Environment.getExternalStorageDirectory().getAbsoluteFile() + "/圣经流利说";
						File file = new File(dir + "/church_"+churchName.trim()+".jpg");
						if(file.exists())
						{
							file.delete();
						}
						downloadFile( "/church_"+churchName.trim()+".jpg", file.getAbsoluteFile().toString(), "");
						
						Editor editor = sp.edit();
						editor.putString("churchName", churchName);
						editor.putString("pastorName", d.get("pastorName").toString());
						editor.putString("churchType", d.get("churchType").toString());
						editor.putString("churchMission", d.get("churchMission").toString());
						editor.putString("churchSight", d.get("churchSight").toString());
						if(d.get("churchNotice")==null)
						{
							editor.putString("churchNotice","");
						}
						else
						{
							editor.putString("churchNotice", d.get("churchNotice").toString());
						}
						
						editor.putString("churchIconUri", d.get("churchIconUri").toString());
						editor.putString("churchLocation", d.get("churchLocation").toString());
						if( d.get("churchCount")==null)
						{
							editor.putString("churchCount", "0");
						}
						else
						{
							editor.putString("churchCount",d.get("churchCount").toString());
						}
						
						editor.putString("churchCreateDate", d.get("churchCreateDate").toString());
						// editor.putString("churchNotice",Date.getCurrentDate());
						editor.commit();
						reDialog.dismiss();
						return;

					}
				}
			}

			@Override
			public void onFailure(int errCode, String errMsg) {
				reDialog.dismiss();
				Toast.makeText(context, "查询失败！请检查网络！", Toast.LENGTH_SHORT).show();
			}
		});

	}
	
	
	public void updateData(final String churchName,String pastorName,String churchType,String churchMission,String churchSight,final String churchIconUri,String churchLocation,String churchLatitude,String churchLongtitude,String churchCreateDate,String churchCount,String churchNotice,final boolean isUpdateProtrait) {
        FrontiaQuery query1 = new FrontiaQuery();
        query1.equals("tableType", "Church");
        FrontiaQuery query2 = new FrontiaQuery();
        query2.equals("churchName",churchName);
        FrontiaQuery query=query1.and(query2);

        FrontiaData newData = new FrontiaData();

        newData.put("churchName", churchName);
        newData.put("pastorName", pastorName);
        newData.put("churchType", churchType);
        newData.put("churchMission", churchMission);
        newData.put("churchSight",churchSight);
        newData.put("churchNotice",churchNotice);
        newData.put("churchIconUri",churchIconUri);
		newData.put("churchLocation",churchLocation);
		newData.put("churchLatitude", churchLatitude);
		newData.put("churchLongtitude", churchLongtitude);
		newData.put("tableType","Church");
		newData.put("churchCreateDate", churchCreateDate);
		newData.put("churchCount",churchCount);

        mCloudStorage.updateData(
						query,
						newData,
						new DataOperationListener() {

							@Override
							public void onSuccess(long count) {
								
								if(isUpdateProtrait)
								{
									// uploadFile(churchIconUri,  "church_"+churchName.trim()+".jpg");
									deleteFile(churchIconUri,  "church_"+churchName.trim()+".jpg","updateData");
								}
								reDialog.dismiss();
								Toast.makeText(context, "修改成功！", Toast.LENGTH_SHORT).show();
								
							}

							@Override
							public void onFailure(int errCode, String errMsg) {
								reDialog.dismiss();
								Toast.makeText(context, "修改失败！请检查网络！", Toast.LENGTH_SHORT).show();
							}
						});
	}
	
	
	public void deleteFile(final String nativePath ,final String remotePath,final String from) {
		FrontiaFile file=new FrontiaFile();
		file.setNativePath(nativePath);
		file.setRemotePath(remotePath);
		mCloudStorage.deleteFile(file,
                new FileOperationListener() {

                    @Override
                    public void onSuccess(String source) {
                      Log.i("删除成功","删除成功" );
                      if(from.equals("updateData"))
                      {
                    	  uploadFile(nativePath, remotePath); 
                      }
                      if(from.equals("deleteChurch"))
                      {
                    	 // Toast.makeText(context, "删除成功！", Toast.LENGTH_SHORT).show();
                      }
                     
                      
                    }

                    @Override
                    public void onFailure(String source, int errCode,
                                          String errMsg) {
                    	 Log.i("删除失败","删除失败" );
                    }

                });
	}
	
	protected void uploadFile(String nativePath ,String remotePath) {
		FrontiaFile file=new FrontiaFile();
		file.setNativePath(nativePath);
		file.setRemotePath(remotePath);
		mCloudStorage.uploadFile(file, new FileProgressListener() {
			@Override
			public void onProgress(String source, long bytes, long total) {

			}
		}, new FileTransferListener() {
			@Override
			public void onSuccess(String source, String newTargetName) {
				Log.i("更新头像成功", "上传头像成功");
			}

			@Override
			public void onFailure(String source, int errCode, String errMsg) {
				Log.i("更新头像失败", "上传头像失败");
			}
		});
	}

	
	public  void deleteChurch(final String churchName,final String churchIconUri) {
		// FrontiaQuery中有很多查询条件，你可以尝试多种查询条件，相当于sql语句中的wher
		FrontiaQuery q1 = new FrontiaQuery();
		q1.equals("tableType", "Church");
		FrontiaQuery q2 = new FrontiaQuery();
		q2.equals("churchName", churchName);
		FrontiaQuery query = q1.and(q2);
		

		mCloudStorage.deleteData(query, new DataOperationListener() {

			@Override
			public void onSuccess(long count) {
				deleteFile(churchIconUri,  "church_"+churchName.trim()+".jpg","deleteChurch");
				Log.i("删除", "shanchu");
				
			}

			@Override
			public void onFailure(int errCode, String errMsg) {
				reDialog.dismiss();
				Toast.makeText(context, "删除失败！请检查网络！", Toast.LENGTH_SHORT).show();
			}
		});
	}
	

}
