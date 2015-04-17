package com.wujie.database;

import java.util.ArrayList;
import java.util.List;

import com.wujie.help.ClassInfo;



import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class ClassDao {

	private MySqLiteOpenHelper mySql;
    public ClassDao(Context  context)
    {
    	mySql=new MySqLiteOpenHelper(context);
    }
    public void add(String name,int ifsecret,String secret )
    {
    	SQLiteDatabase db=mySql.getWritableDatabase();
    	db.execSQL("insert into noteclass(name ,ifsecret,secret)values(?,?,?)",new Object[]{name,ifsecret,secret});
    	db.close();
    }
    public List<ClassInfo> findAll()
    {
    	SQLiteDatabase db=mySql.getReadableDatabase();
    	Cursor cursor=db.rawQuery("select * from noteclass",null);
    	List<ClassInfo> prayList=new ArrayList<ClassInfo>();
    	while(cursor.moveToNext())
    	{
    		int id=cursor.getInt(cursor.getColumnIndex("id"));
    		String name=cursor.getString(cursor.getColumnIndex("name"));
    		int ifsecret=cursor.getInt(cursor.getColumnIndex("ifsecret"));
    		String secret=cursor.getString(cursor.getColumnIndex("secret"));
    		ClassInfo one=new ClassInfo(id,name,ifsecret,secret);
    		prayList.add(one);
    	}
    	cursor.close();
    	db.close();
    	return prayList;
    	
    }
    public boolean findName(String cname)
    {
    	SQLiteDatabase db=mySql.getReadableDatabase();
    	Cursor cursor=db.rawQuery("select * from noteclass where name=?",new String[]{cname} );
    	List<ClassInfo> prayList=new ArrayList<ClassInfo>();
    	while(cursor.moveToNext())
    	{
    		int id=cursor.getInt(cursor.getColumnIndex("id"));
    		String name=cursor.getString(cursor.getColumnIndex("name"));
    		int ifsecret=cursor.getInt(cursor.getColumnIndex("ifsecret"));
    		String secret=cursor.getString(cursor.getColumnIndex("secret"));
    		ClassInfo one=new ClassInfo(id,name,ifsecret,secret);
    		prayList.add(one);
    	}
    	cursor.close();
    	db.close();
    	if(prayList.size()==0)return false;
    	else return true;
    	
    	
    }
    public void update(String s,Object[]itom)
    {
    	SQLiteDatabase db=mySql.getWritableDatabase();
    	db.execSQL(s,itom);
    	db.close();
    }
    public void delete(int s)
    {
    	SQLiteDatabase db=mySql.getWritableDatabase();
    	db.execSQL("delete from noteclass where id=?",new Object[]{s});
    	db.close();
    }
}
