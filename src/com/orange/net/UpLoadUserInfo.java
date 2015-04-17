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
import android.os.Environment;
import android.sax.StartElementListener;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
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
import com.example.acts.Personal_Activity;
import com.example.acts.R;
import com.orange.church.Church;
import com.orange.church.ChurchAdapter;
import com.orange.church.User;
import com.orange.login.Activity_Register;
import com.orange.map.Activity_Map;
import com.orange.view.CircleImageView;

public class UpLoadUserInfo {
	private Context context;
	private FrontiaStorage mCloudStorage;
	private String usernameMD5;
	private String passwordMD5;
	private String name;
	private AlertDialog reDialog;
	private FrontiaFile mFile;
	private boolean isExist;
	private String portraitUri;
	private CircleImageView iv;
	private ListView user_lv;
	private ArrayList<Map<String, String>> UserData;
	private int temp;
	private Activity currentActivity;

	public UpLoadUserInfo(Context context, AlertDialog reDialog) {
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

	public void addData(User mUser,final Activity activity) {
		final User user = mUser;
		portraitUri = user.getPortraitUri();
		mFile.setNativePath(portraitUri);
		mFile.setRemotePath(portraitUri.substring(
				portraitUri.lastIndexOf("/") + 1, portraitUri.length()));
		uploadFile();
		name = user.getName();

		final FrontiaData[] datas = new FrontiaData[1];
		datas[0] = new FrontiaData();
		datas[0].put("churchName", "");
		datas[0].put("portraitUri", portraitUri);
		datas[0].put("userName", user.getUserName());
		datas[0].put("password", user.getPassword());
		datas[0].put("name", user.getName());
		datas[0].put("birthday", user.getBirthday());
		datas[0].put("gender", user.getGender());
		datas[0].put("qq", user.getQq());
		datas[0].put("tableType", "User");
		final SharedPreferences sp = context.getSharedPreferences("UserInfo",
				Context.MODE_PRIVATE);

		Log.i("kannkannanan", sp.getString("city", ""));
		datas[0].put("userLocation", sp.getString("city", ""));
		datas[0].put("userLatitude", sp.getInt("latitude", 0));
		datas[0].put("userLongtitude", sp.getInt("longtitude", 0));

		mCloudStorage.insertData(datas[0],
				new FrontiaStorageListener.DataInsertListener() {

					@Override
					public void onSuccess() {
						new Thread(new Runnable() {

							@Override
							public void run() {

								Editor editor = sp.edit();
								editor.putString("churchName", "");
								editor.putString("portraitUri", portraitUri);
								editor.putString("userName", user.getUserName());
								editor.putString("password", user.getPassword());
								editor.putString("name", user.getName());
								editor.putString("birthday", user.getBirthday());
								editor.putString("gender", user.getGender());
								editor.putString("qq", user.getQq());
								editor.putString("userLocation",
										sp.getString("location", ""));
								editor.putInt("userLatitude",
										sp.getInt("latitude", 0) );
								editor.putInt("userLongtitude",
										sp.getInt("longtitude", 0));
								editor.putBoolean("isLogin", true);
								editor.commit();
							}
						}).start();
						reDialog.dismiss();
						activity.finish();
						Toast.makeText(context, "注册成功！！", Toast.LENGTH_SHORT)
								.show();
						Intent intent = new Intent(context, MainActivity.class);
						intent.putExtra("from", 2);
						context.startActivity(intent);
						// overridePendingTransition(R.anim.zoomin,
						// R.anim.zoomout);

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

	public void isExist(Intent intent, String username,final Activity activity) {
		final String usernameMd5 = username;

		final Intent mintent = intent;
		// FrontiaQuery中有很多查询条件，你可以尝试多种查询条件，相当于sql语句中的where
		FrontiaQuery query = new FrontiaQuery();
		query.equals("tableType", "User");

		mCloudStorage.findData(query, new DataInfoListener() {

			@Override
			public void onSuccess(List<FrontiaData> dataList) {
				StringBuilder sb = new StringBuilder();
				int i = 0;
				for (FrontiaData d : dataList) {
					if (d.get("userName").toString().equals(usernameMd5)) {
						i = 1;
						reDialog.dismiss();
						Log.i("查询成功", "查询成功");
						Toast.makeText(context, "已经有人用过此用户名，请更换！",
								Toast.LENGTH_SHORT).show();
						return;

					}
				}
				if (i == 0) {
					reDialog.dismiss();
					activity.finish();
					context.startActivity(mintent);

				}

			}

			@Override
			public void onFailure(int errCode, String errMsg) {
				Log.i("查询失败", "查询失败");

			}
		});

	}

	public void login(Intent intent, String username, String password,Activity activity) {
		
		currentActivity=activity;
		final SharedPreferences sp = context.getSharedPreferences("UserInfo",
				Context.MODE_PRIVATE);
		final Editor editor = sp.edit();

		final String usernameMd5 = username;
		final String passwordMd5 = password;
		final Intent mintent = intent;
		// FrontiaQuery中有很多查询条件，你可以尝试多种查询条件，相当于sql语句中的where

		FrontiaQuery query = new FrontiaQuery();
		query.equals("tableType", "User");

		temp = 0;
		mCloudStorage.findData(query, new DataInfoListener() {

			@Override
			public void onSuccess(List<FrontiaData> dataList) {
				Log.i("rrrr", "ddddddddddddd"+usernameMd5+":"+passwordMd5);
				for (FrontiaData d : dataList) {
					
					
					Log.i("userName", d.get("userName").toString()+":"+d.get("password").toString());
					if (d.get("userName").toString().equals(usernameMd5)
							&& d.get("password").toString().equals(passwordMd5)) {
						
						File dirFile = new File(Environment.getExternalStorageDirectory()
								.getAbsoluteFile() + "/圣经流利说");
						if (!dirFile.exists()) {
							dirFile.mkdir();
						}
						String dir = Environment.getExternalStorageDirectory()
								.getAbsoluteFile() + "/圣经流利说";
						File file = new File(dir + "/user_"
								+ usernameMd5.trim() + ".jpg");
						if (file.exists()) {
							file.delete();
						}
						editor.putString("churchName", d.get("churchName")
								.toString());
						editor.putString("portraitUri", d.get("portraitUri")
								.toString());
						editor.putString("userName", d.get("userName")
								.toString());
						editor.putString("password", d.get("password")
								.toString());
						editor.putString("name", d.get("name").toString());
						editor.putString("birthday", d.get("birthday")
								.toString());
						editor.putString("gender", d.get("gender").toString());
						editor.putString("userLocation", d.get("userLocation")
								.toString());
						editor.putInt("userLatitude", Integer.parseInt(d.get("userLatitude")
								.toString()));
						editor.putInt("userLongtitude",
								Integer.parseInt(d.get("userLongtitude").toString()));
						editor.putString("qq", d.get("qq").toString());
						editor.putBoolean("isLogin", true);
						editor.commit();
						downloadFile("user_" + usernameMd5.trim() + ".jpg",
								file.getAbsoluteFile().toString(), "login",
								mintent);
						temp = 1;
					}

				}
				if (temp == 0) {
					reDialog.dismiss();
					Log.i("查询失败", "查询失败");
					Toast.makeText(context, "用户名或密码错误！", Toast.LENGTH_SHORT)
							.show();
				}

			}

			@Override
			public void onFailure(int errCode, String errMsg) {
				reDialog.dismiss();
				Log.i("查询失败", "查询失败");
				Toast.makeText(context, "请检查网络是否打开！", Toast.LENGTH_SHORT)
						.show();

			}
		});

	}

	public void getAUser(final String userName, final List<View> views) {
		// FrontiaQuery中有很多查询条件，你可以尝试多种查询条件，相当于sql语句中的where
		FrontiaQuery query2 = new FrontiaQuery();
		query2.equals("tableType", "User");
		
		FrontiaQuery query1 = new FrontiaQuery();
		query1.equals("userName",userName);
		
		FrontiaQuery query = new FrontiaQuery();
		query=query2.and(query1);
		mCloudStorage.findData(query, new DataInfoListener() {
			@Override
			public void onSuccess(List<FrontiaData> dataList) {
				Log.i("onSuccess", "onSuccess");
				StringBuilder sb = new StringBuilder();
				int i = 0;
				for (FrontiaData d : dataList) {

					Log.i("useer", "进入");
					String dir = Environment.getExternalStorageDirectory()
							.getAbsoluteFile() + "/圣经流利说";
					File file = new File(dir + "/user_" + userName.trim()
							+ ".jpg");
					if (file.exists()) {
						file.delete();
					}
					downloadFile("/user_" + userName.trim() + ".jpg", file
							.getAbsoluteFile().toString(), "getAUser", null);
					iv = (CircleImageView) views.get(0);
					TextView name_tv = (TextView) views.get(1);
					TextView age_tv = (TextView) views.get(2);
					TextView location_tv = (TextView) views.get(3);
					ImageView gender_iv = (ImageView) views.get(4);
					TextView portraitUti = (TextView) views.get(5);
					TextView qq_tv = (TextView) views.get(6);

					portraitUti.setText(d.get("portraitUri").toString());

					final Calendar c = Calendar.getInstance();
					int mYear = c.get(Calendar.YEAR);
					String age = d
							.get("birthday")
							.toString()
							.substring(
									0,
									d.get("birthday").toString()
											.indexOf("-"));
					Log.i("age", age);
					name_tv.setText(d.get("name").toString());
					qq_tv.setText(d.get("qq").toString());
					age_tv.setText((mYear - Integer.parseInt(age)) + "");
					location_tv.setText(d.get("userLocation").toString());
					Log.i("地区", "地区" + d.get("userLocation").toString());
					String gender = d.get("gender").toString();
					if (gender.equals("男")) {
						gender_iv.setImageDrawable(context.getResources()
								.getDrawable(R.drawable.male));
					} else {
						gender_iv.setImageDrawable(context.getResources()
								.getDrawable(R.drawable.famale));
					}
					reDialog.dismiss();
					return;

				
				}

			}

			@Override
			public void onFailure(int errCode, String errMsg) {
				Toast.makeText(context, "请检查网络是否打开！", Toast.LENGTH_SHORT)
						.show();
				reDialog.dismiss();
			}
		});

	}
	
	public void getAUserDetail(final String userName, final List<View> views) {
		// FrontiaQuery中有很多查询条件，你可以尝试多种查询条件，相当于sql语句中的where
		FrontiaQuery query = new FrontiaQuery();
		query.equals("tableType", "User");
		mCloudStorage.findData(query, new DataInfoListener() {
			@Override
			public void onSuccess(List<FrontiaData> dataList) {
				Log.i("onSuccess", "onSuccess");
				StringBuilder sb = new StringBuilder();
				int i = 0;
				for (FrontiaData d : dataList) {
					if (d.get("userName").toString().equals(userName)) {
						Log.i("userName", d.get("userName").toString());
						String dir = Environment.getExternalStorageDirectory()
								.getAbsoluteFile() + "/圣经流利说";
						File file = new File(dir + "/user_" + userName.trim()
								+ ".jpg");
						if (file.exists()) {
							file.delete();
						}
						downloadFile("/user_" + userName.trim() + ".jpg", file
								.getAbsoluteFile().toString(), "getAUserDetail", null);
						iv = (CircleImageView) views.get(0);
						
						/*views.add(name_tv);
						views.add(age_tv);
						views.add(area_tv);
						views.add(agender_tv);
						views.add(portrait);
						views.add(qq_tv);
						views.add(church_tv);*/
						TextView name_tv = (TextView) views.get(1);
						TextView age_tv = (TextView) views.get(2);
						TextView location_tv = (TextView) views.get(3);
						TextView agender_tv = (TextView) views.get(4);
						TextView qq_tv = (TextView) views.get(5);
						//TextView church_tv = (TextView) views.get(6);

						final Calendar c = Calendar.getInstance();
						int mYear = c.get(Calendar.YEAR);
						String age = d
								.get("birthday")
								.toString()
								.substring(
										0,
										d.get("birthday").toString()
												.indexOf("-"));
						Log.i("age", age);
						name_tv.setText(d.get("name").toString());
						age_tv.setText((mYear - Integer.parseInt(age)) + "");
						
						if(d.get("userLocation").toString().contains("null")||d.get("userLocation").toString().equals(""))
						{
							location_tv.setText("未识别");
						}
						else
						{
							location_tv.setText(d.get("userLocation").toString());
						}
						
						Log.i("地区", "地区" + d.get("userLocation").toString());
						String gender = d.get("gender").toString();
						agender_tv.setText(gender);
						qq_tv.setText(d.get("qq").toString());
						//church_tv.setText(d.get("churchName").toString());
						reDialog.dismiss();
						return;

					}
				}

			}

			@Override
			public void onFailure(int errCode, String errMsg) {
				Toast.makeText(context, "请检查网络是否打开！", Toast.LENGTH_SHORT)
						.show();
				reDialog.dismiss();
			}
		});

	}

	public void downloadFile(String picName, final String picUri,
			final String from, final Intent intent) {
		Log.i("图像问题0", "图像问题0");
		mFile.setNativePath(picUri);
		mFile.setRemotePath(picName);
		mCloudStorage.downloadFile(mFile, new FileProgressListener() {

			@Override
			public void onProgress(String source, long bytes, long total) {

			}

		}, new FileTransferListener() {

			@Override
			public void onSuccess(String source, String newTargetName) {
				Log.i("图像问题1", "图像问题1");
				if (from.equals("getChurchMember")) {
					user_lv.setAdapter(new ChurchAdapter(UserData, context));
				}
				if (from.equals("getAUser")||from.equals("getAUserDetail")) {
					Log.i("图像问题", "图像问题");
					Bitmap bitmap = BitmapFactory.decodeFile(picUri);
					iv.setImageBitmap(bitmap);
				}
				if (from.equals("login")) {
					reDialog.dismiss();
					currentActivity.finish();
					context.startActivity(intent);
					Log.i("login", "login");
					
					/*
					 * Bitmap bitmap=BitmapFactory.decodeFile(picUri);
					 * portrait.setImageBitmap(bitmap);
					 */
				}
				
				

			}

			@Override
			public void onFailure(String source, int errCode, String errMsg) {
				Log.i("图像问题s", "图像问题s");
			}

		});
	}
	
	public void downloadFile(String picName, final String picUri,final CircleImageView iv) {
		Log.i("图像问题0", "图像问题0");
		mFile.setNativePath(picUri);
		mFile.setRemotePath(picName);
		mCloudStorage.downloadFile(mFile, new FileProgressListener() {

			@Override
			public void onProgress(String source, long bytes, long total) {

			}

		}, new FileTransferListener() {

			@Override
			public void onSuccess(String source, String newTargetName) {
				
				Bitmap bitmap = BitmapFactory.decodeFile(picUri);
				iv.setImageBitmap(bitmap);
				reDialog.dismiss();
				Log.i("onSuccess", "onSuccess"+"2222");
			}

			@Override
			public void onFailure(String source, int errCode, String errMsg) {
				Log.i("图像问题s", "图像问题s");
				reDialog.dismiss();
			}

		});
	}

	public void getChurchMember(ListView mUser_lv, String mChurchName) {

		final String churchName = mChurchName;
		UserData = new ArrayList<Map<String, String>>();
		user_lv = mUser_lv;
		FrontiaQuery query = new FrontiaQuery();
		query.equals("tableType", "User");

		mCloudStorage.findData(query, new DataInfoListener() {

			@Override
			public void onSuccess(List<FrontiaData> dataList) {
				Log.i("数目", dataList.size() + "");
				StringBuilder sb = new StringBuilder();
				for (FrontiaData d : dataList) {
					if (d.get("churchName") != null
							&& !d.get("churchName").toString().equals("")) {
						Log.i("有", "you1");
						if (d.get("churchName").toString().equals(churchName)) {
							Map<String, String> map = new HashMap<String, String>();
							map.put("userName", (String) d.get("userName"));
							map.put("name", (String) d.get("name"));
							File file = new File((String) d
									.get("churchIconUri"));
							String picUri = d.get("portraitUri").toString();
							String picName = picUri.substring(picUri
									.lastIndexOf("/") + 1);
							Log.i("图片", picName);

							map.put("portraitUri", d.get("portraitUri")
									.toString());
							UserData.add(map);

							if (!file.exists()) {
								Log.i("下载", picName);
								downloadFile(picName, picUri,
										"getChurchMember", null);
							}

						}

					}
				}

				user_lv.setAdapter(new ChurchAdapter(UserData, context));
				reDialog.dismiss();

			}

			@Override
			public void onFailure(int errCode, String errMsg) {
				Log.i("查询失败", "查询失败");

			}
		});

	}

	public void findAllData(final TextView tv) {
		// 空的FrontiaQuery表示query所有的数据(具有可读权限数据才能被查到)
		FrontiaQuery query = new FrontiaQuery();

		mCloudStorage.findData(query, new DataInfoListener() {

			@Override
			public void onSuccess(List<FrontiaData> dataList) {
				Log.i("dataList", dataList.size() + "");

				StringBuilder sb = new StringBuilder();
				int i = 0;
				for (FrontiaData d : dataList) {
					Log.i("data", d.toJSON().toString());
					sb.append(i).append(":").append(d.toJSON().toString())
							.append("\n");
					i++;
				}
				tv.setText("find data\n" + sb.toString());

			}

			@Override
			public void onFailure(int errCode, String errMsg) {

			}
		});

	}
	
	
	public void updateData(final String churchName,final String portraitUri,final String userName,String password,final String name,final String birthday,final String gender,final String qq,final boolean isUpdateProtrait,final TextView sv_menu_right) {
		
		/*mFile.setNativePath(portraitUri);
		mFile.setRemotePath(portraitUri.substring(
				portraitUri.lastIndexOf("/") + 1, portraitUri.length()));*/
		
		
        FrontiaQuery query1 = new FrontiaQuery();
        query1.equals("tableType", "User");
        FrontiaQuery query2 = new FrontiaQuery();
        query2.equals("userName",userName);
        FrontiaQuery query=query1.and(query2);

        FrontiaData newData = new FrontiaData();
        final SharedPreferences sp = context.getSharedPreferences("UserInfo",
				Context.MODE_PRIVATE);
        newData.put("churchName", churchName);
        newData.put("portraitUri", portraitUri);
        newData.put("userName", userName);
        newData.put("password", password);
        newData.put("name",name);
        newData.put("birthday",birthday);
		newData.put("gender",gender);
		newData.put("qq", qq);
		newData.put("tableType", "User");
		newData.put("userLocation",sp.getString("city", ""));
		newData.put("userLatitude", sp.getInt("latitude", 0));
		newData.put("userLongtitude", sp.getInt("longtitude", 0));

        mCloudStorage.updateData(
						query,
						newData,
						new DataOperationListener() {

							@Override
							public void onSuccess(long count) {
								Editor editor = sp.edit();
								editor.putString("name", name);
								editor.putString("birthday",birthday);
								editor.putString("gender", gender);
								editor.putString("qq",qq);
								editor.commit();
								if(isUpdateProtrait)
								{
									deleteFile();
								}
								reDialog.dismiss();
								Toast.makeText(context, "修改成功！", Toast.LENGTH_SHORT).show();
								sv_menu_right.setClickable(false);
								sv_menu_right.setVisibility(View.GONE);
							}

							@Override
							public void onFailure(int errCode, String errMsg) {
								reDialog.dismiss();
								Toast.makeText(context, "修改失败！请检查网络！", Toast.LENGTH_SHORT).show();
							}
						});
	}
	
	public void deleteFile() {
/*		FrontiaFile file=new FrontiaFile();
		file.setNativePath(nativePath);
		file.setRemotePath(remotePath);*/
		mCloudStorage.deleteFile(mFile,
                new FileOperationListener() {

                    @Override
                    public void onSuccess(String source) {
                      Log.i("删除成功","删除成功" );
                      uploadFile();
                      
                    }

                    @Override
                    public void onFailure(String source, int errCode,
                                          String errMsg) {
                    	 Log.i("删除失败","删除失败" );
                    }

                });
	}
	
	

}
