package com.example.acts;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.util.Log;

//DatabaseHelper作为一个访问SQLite的助手类，提供两个方面的功能，
//第一，getReadableDatabase(),getWritableDatabase()可以获得SQLiteDatabse对象，通过该对象可以对数据库进行操作
//第二，提供了onCreate()和onUpgrade()两个回调函数，允许我们在创建和升级数据库时，进行自己的操作

public class DatabaseHelper extends SQLiteOpenHelper {

	private static final int VERSION = 6;

	// 在SQLiteOepnHelper的子类当中，必须有该构造函数
	/*public DatabaseHelper(Context context) {
		super(context, "bible.db", null, 1);
	}*/
	
	public DatabaseHelper(Context context,String dbName) {
		super(context, dbName, null, 6);
	}

	// 该函数是在第一次创建数据库的时候执行,实际上是在第一次得到SQLiteDatabse对象的时候，才会调用这个方法
	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		Log.i("create", "create");
		// System.out.println("create a Database");
		// execSQL函数用于执行SQL语句
		// db.execSQL("create table recitebible(volume varchar(20),chapter varchar(20),content varchar(300),PRIMARY KEY(volume,chapter))");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		// System.out.println("update a Database");
	}

}
