package com.example.acts;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.util.Log;

//DatabaseHelper��Ϊһ������SQLite�������࣬�ṩ��������Ĺ��ܣ�
//��һ��getReadableDatabase(),getWritableDatabase()���Ի��SQLiteDatabse����ͨ���ö�����Զ����ݿ���в���
//�ڶ����ṩ��onCreate()��onUpgrade()�����ص����������������ڴ������������ݿ�ʱ�������Լ��Ĳ���

public class DatabaseHelper extends SQLiteOpenHelper {

	private static final int VERSION = 6;

	// ��SQLiteOepnHelper�����൱�У������иù��캯��
	/*public DatabaseHelper(Context context) {
		super(context, "bible.db", null, 1);
	}*/
	
	public DatabaseHelper(Context context,String dbName) {
		super(context, dbName, null, 6);
	}

	// �ú������ڵ�һ�δ������ݿ��ʱ��ִ��,ʵ�������ڵ�һ�εõ�SQLiteDatabse�����ʱ�򣬲Ż�����������
	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		Log.i("create", "create");
		// System.out.println("create a Database");
		// execSQL��������ִ��SQL���
		// db.execSQL("create table recitebible(volume varchar(20),chapter varchar(20),content varchar(300),PRIMARY KEY(volume,chapter))");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		// System.out.println("update a Database");
	}

}
