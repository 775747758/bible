package com.orange.login;

import java.util.List;

import android.os.Handler;
import android.os.Message;
import android.text.StaticLayout;

import com.baidu.frontia.FrontiaData;
import com.baidu.frontia.FrontiaQuery;
import com.baidu.frontia.api.FrontiaStorage;
import com.baidu.frontia.api.FrontiaStorageListener;
import com.baidu.frontia.api.FrontiaStorageListener.DataInfoListener;
import com.baidu.frontia.api.FrontiaStorageListener.DataOperationListener;

public class Login_utile {
	private static boolean isSuccessful;

	public static boolean deleteAllData(FrontiaStorage mCloudStorage) {

		FrontiaQuery q1 = new FrontiaQuery();

		mCloudStorage.deleteData(q1, new DataOperationListener() {

			@Override
			public void onSuccess(long count) {
				isSuccessful = true;
			}

			@Override
			public void onFailure(int errCode, String errMsg) {
				isSuccessful = false;
			}
		});
		return isSuccessful;
	}

	public static boolean deletData(FrontiaStorage mCloudStorage, String username, String password) {
		// FrontiaQuery中有很多查询条件，你可以尝试多种查询条件，相当于sql语句中的where
		FrontiaQuery q1 = new FrontiaQuery();
		q1.equals("username", username);
		FrontiaQuery q2 = new FrontiaQuery();
		q2.equals("password", password);
		FrontiaQuery query = q1.or(q2);

		mCloudStorage.deleteData(query, new DataOperationListener() {

			@Override
			public void onSuccess(long count) {
				isSuccessful = true;
			}

			@Override
			public void onFailure(int errCode, String errMsg) {
				isSuccessful = false;
			}
		});
		return isSuccessful;
	}

	public static boolean addData(FrontiaStorage mCloudStorage, String username, String password) {
		final FrontiaData[] datas = new FrontiaData[1];
		datas[0] = new FrontiaData();
		datas[0].put("username", username);
		datas[0].put("password", password);

		isSuccessful = false;

		mCloudStorage.insertData(datas[0], new FrontiaStorageListener.DataInsertListener() {

			@Override
			public void onSuccess() {
				isSuccessful = true;
			}

			@Override
			public void onFailure(int errCode, String errMsg) {
				isSuccessful = false;
			}

		});
		return isSuccessful;

	}

	public static boolean updateData(FrontiaStorage mCloudStorage, String oldUsername, String oldPassword, String newUsername, String newPassword) {
		FrontiaQuery q1 = new FrontiaQuery();
		q1.equals("username", oldUsername);
		FrontiaQuery q2 = new FrontiaQuery();
		q2.equals("password", oldPassword);
		FrontiaQuery query = q1.or(q2);

		FrontiaData newData = new FrontiaData();
		newData.put("username", newUsername);
		newData.put("password", newPassword);

		mCloudStorage.updateData(query, newData, new DataOperationListener() {

			@Override
			public void onSuccess(long count) {
				isSuccessful = true;
			}

			@Override
			public void onFailure(int errCode, String errMsg) {
				isSuccessful = false;
			}
		});
		return isSuccessful;
	}

	public static boolean isExist(FrontiaStorage mCloudStorage, String username) {
		// FrontiaQuery中有很多查询条件，你可以尝试多种查询条件，相当于sql语句中的where
		FrontiaQuery query = new FrontiaQuery();
		query.equals("username", username);

		mCloudStorage.findData(query, new DataInfoListener() {

			@Override
			public void onSuccess(List<FrontiaData> dataList) {
				isSuccessful = true;
			}

			@Override
			public void onFailure(int errCode, String errMsg) {
				isSuccessful = false;
			}
		});

		return isSuccessful;
	}

	public static boolean findExist(FrontiaStorage mCloudStorage, String username, String password) {
		// FrontiaQuery中有很多查询条件，你可以尝试多种查询条件，相当于sql语句中的where
		FrontiaQuery q1 = new FrontiaQuery();
		q1.equals("username", username);
		FrontiaQuery q2 = new FrontiaQuery();
		q2.equals("password", password);
		FrontiaQuery query = q1.or(q2);

		mCloudStorage.findData(query, new DataInfoListener() {

			@Override
			public void onSuccess(List<FrontiaData> dataList) {
				isSuccessful = true;
			}

			@Override
			public void onFailure(int errCode, String errMsg) {
				isSuccessful = false;
			}
		});

		return isSuccessful;
	}

}
