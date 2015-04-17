package com.wujie.database;

import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class MySqLiteOpenHelper extends SQLiteOpenHelper {

	public MySqLiteOpenHelper(Context context) {
		super(context, "holynote",null, 1);
		// TODO Auto-generated constructor stub
	}
    @Override
	public void onCreate(SQLiteDatabase arg0) {
		// TODO Auto-generated method stub
         arg0.execSQL("create table note (noteid integer primary key autoincrement,"+
		              "text varchar(100)," +
		              "year integer," +
		              "month integer," +
		              "day integer," +
		              "time varchar(20)," +
		              "week varchar(5)," +
		              "class varchar(20)," +
		              "title varchar(20) )");
         arg0.execSQL("create table img( imgid integer primary key autoincrement," +
		              "noteid integer,"+
         		      "path varchar(20)," +
         		      "start integer)");
         arg0.execSQL("create table noteclass (id integer primary key autoincrement," +
            		" name varchar(100), ifsecret integer, secret varchar(20) )");
	}

	@Override
	public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub

	}

}
