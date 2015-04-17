package com.wujie.database;



import java.util.ArrayList;
import java.util.List;


import com.wujie.example.note.NoteList;
import com.wujie.example.note.WriteNotePage;
import com.wujie.help.EditImage;
import com.wujie.help.NoteInfo;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Toast;



public class NoteDao {
	private MySqLiteOpenHelper mySql;
	public NoteDao(Context context) {
		// TODO Auto-generated constructor stub
		mySql=new MySqLiteOpenHelper(context);
	}
	public void addNote(String text,int year,int month,int day,String time,String week,String classNmae,String title)
	{
		SQLiteDatabase db=mySql.getWritableDatabase();
    	db.execSQL("insert into note(text ,year,month,day,time,week,class,title)values(?,?,?,?,?,?,?,?)",new Object[]{text,year,month,day,time,week,classNmae,title});
    	db.close();
	}
	public void updateNote(String text,String classNmae,String title,int id)
	{
		SQLiteDatabase db=mySql.getWritableDatabase();
    	db.execSQL("update note set text=? ,class=?,title=? where noteid=? ",new Object[]{text,classNmae,title,id});
    	db.close();
	}
	public void update(String s1,Object[]s2)
	{
		SQLiteDatabase db=mySql.getWritableDatabase();
    	db.execSQL(s1,s2);
    	db.close();
	}
	public void addImg(int noteid,String path,int start)
	{
		SQLiteDatabase db=mySql.getWritableDatabase();
    	db.execSQL("insert into img(noteid,path,start)values(?,?,?)",new Object[]{noteid,path,start});
    	db.close();
	}
	public List<EditImage> findByImgId(String noteId,Context context)
	{
		
		SQLiteDatabase db=mySql.getReadableDatabase();
    	Cursor cursor=db.rawQuery("select * from img where noteid=?",new String []{noteId}); 
    	//Toast.makeText(context, "¹²ÓÐ"+String.valueOf(cursor.getCount()), 1).show();
		List<EditImage> list=new ArrayList<EditImage>();
    	while(cursor.moveToNext())
    	{
			 int id=cursor.getInt(cursor.getColumnIndex("imgid"));
		     int noteIde=cursor.getInt(cursor.getColumnIndex("noteid"));
		     int start=cursor.getInt(cursor.getColumnIndex("start"));
		     String path=cursor.getString(cursor.getColumnIndex("path"));
		     list.add(new EditImage(path, start));
		     
    	}
    	cursor.close();
    	db.close();
		return list;
	}
	
	public void deleteImg(String name)
	{
		SQLiteDatabase db=mySql.getWritableDatabase();
    	db.execSQL("delete from img where path=?",new Object[]{name});
    	db.close();
	}
	public void deleteImg(int id)
	{
		SQLiteDatabase db=mySql.getWritableDatabase();
    	db.execSQL("delete from img where noteid=?",new Object[]{id});
    	db.close();
	}
	public List<NoteInfo> findByClass(String noteClassName)
	{
		
		//NoteInfo noteInfo=new NoteDao(context);
		SQLiteDatabase db=mySql.getReadableDatabase();
    	Cursor cursor=db.rawQuery("select * from note where class=?",new String []{noteClassName}); 
		//Cursor cursor=db.rawQuery("select * from note",null);
		
    	List<NoteInfo> list=new ArrayList<NoteInfo>();
    	while(cursor.moveToNext())
    	{
			 int id=cursor.getInt(cursor.getColumnIndex("noteid"));
		     String text=cursor.getString(cursor.getColumnIndex("text"));
		     int year=cursor.getInt(cursor.getColumnIndex("year"));
		     int month=cursor.getInt(cursor.getColumnIndex("month"));
		     int day=cursor.getInt(cursor.getColumnIndex("day"));
		     String time=cursor.getString(cursor.getColumnIndex("time"));
		     String week=cursor.getString(cursor.getColumnIndex("week"));
		     String noteClass=cursor.getString(cursor.getColumnIndex("class"));
		     String title=cursor.getString(cursor.getColumnIndex("title"));
		     list.add(new NoteInfo(id, text, year, month, day, time, week, noteClass, title));
		     
    	}
    	list=inverteList(list);
    	cursor.close();
    	db.close();
		return list;
	}
	public NoteInfo findById(String noteClassName)
	{
		
		//NoteInfo noteInfo=new NoteDao(context);
		SQLiteDatabase db=mySql.getReadableDatabase();
    	Cursor cursor=db.rawQuery("select * from note where noteid=?",new String []{noteClassName}); 
		//Cursor cursor=db.rawQuery("select * from note",null);
		
    	
    	cursor.moveToNext();
    	
		 int id=cursor.getInt(cursor.getColumnIndex("noteid"));
	     String text=cursor.getString(cursor.getColumnIndex("text"));
	     int year=cursor.getInt(cursor.getColumnIndex("year"));
	     int month=cursor.getInt(cursor.getColumnIndex("month"));
	     int day=cursor.getInt(cursor.getColumnIndex("day"));
	     String time=cursor.getString(cursor.getColumnIndex("time"));
	     String week=cursor.getString(cursor.getColumnIndex("week"));
	     String noteClass=cursor.getString(cursor.getColumnIndex("class"));
	     String title=cursor.getString(cursor.getColumnIndex("title"));
	     
		cursor.close();
    	db.close();
		return new NoteInfo(id, text, year, month, day, time, week, noteClass, title);
	}
	public List<NoteInfo> findBySql(String sql,String noteClassName[])
	{
		
		//NoteInfo noteInfo=new NoteDao(context);
		SQLiteDatabase db=mySql.getReadableDatabase();
    	Cursor cursor=db.rawQuery(sql,noteClassName); 
		//Cursor cursor=db.rawQuery("select * from note",null);
		
    	List<NoteInfo> list=new ArrayList<NoteInfo>();
    	while(cursor.moveToNext())
    	{
			 int id=cursor.getInt(cursor.getColumnIndex("noteid"));
		     String text=cursor.getString(cursor.getColumnIndex("text"));
		     int year=cursor.getInt(cursor.getColumnIndex("year"));
		     int month=cursor.getInt(cursor.getColumnIndex("month"));
		     int day=cursor.getInt(cursor.getColumnIndex("day"));
		     String time=cursor.getString(cursor.getColumnIndex("time"));
		     String week=cursor.getString(cursor.getColumnIndex("week"));
		     String noteClass=cursor.getString(cursor.getColumnIndex("class"));
		     String title=cursor.getString(cursor.getColumnIndex("title"));
		     list.add(new NoteInfo(id, text, year, month, day, time, week, noteClass, title));
		     
    	}
    	list=inverteList(list);
    	cursor.close();
    	db.close();
		return list;
	}
	public List<NoteInfo> inverteList(List<NoteInfo> list)
	{
		List<NoteInfo> l=new ArrayList<NoteInfo>();
		int i;
		for(i=list.size()-1;i>=0;i--)
		{
			l.add(list.get(i));
		}
		return l;
	}
	public void delete(int id)
	{
		SQLiteDatabase db=mySql.getWritableDatabase();
    	db.execSQL("delete from note where noteid=?",new Object[]{id});
    	db.close();
	}
	public void delete(String name)
	{
		SQLiteDatabase db=mySql.getWritableDatabase();
    	db.execSQL("delete from note where class=?",new Object[]{name});
    	db.close();
	}
}
