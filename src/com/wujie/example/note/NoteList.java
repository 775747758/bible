package com.wujie.example.note;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.example.acts.R;
import com.wujie.database.NoteDao;
import com.wujie.help.EditImage;
import com.wujie.help.FileOp;
import com.wujie.help.NoteInfo;
import com.wujie.help.TopBarTransparent;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class NoteList extends Activity {
    private String noteClass;
    private SimpleAdapter sAda;
    private NoteDao noteDao;
    private Calendar cal;
    private ListView noteList;
    private Button addNote;
    private EditText searchEdit;
    private String searchText="%%";
    private List<NoteInfo> nlist;
    private int lock=1;
    private AlertDialog adlg;
    private Button deleteBT;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		transparentTitleBar();
		//EditText不自动获取焦点
		//InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
		//imm.hideSoftInputFromWindow(edit_answer.getWindowToken(),0);就是关闭软键盘的方法。

		setContentView(R.layout.wujie_page_list);
		noteDao=new NoteDao(this);
		noteClass="未定义";
		Intent inte=getIntent();
		noteClass=inte.getStringExtra("classname");
		TextView title=(TextView)this.findViewById(R.id.title);
		title.setText(noteClass);
		noteList=(ListView)this.findViewById(R.id.note_list);
		addNote=(Button)this.findViewById(R.id.add);
		searchEdit=(EditText)this.findViewById(R.id.search_text);
		deleteBT=(Button)this.findViewById(R.id.delete);
		deleteBT.setVisibility(View.INVISIBLE);
		onAdd();
		cal=Calendar.getInstance();
		nlist=noteDao.findByClass(noteClass);
		initSimpleAdapter();
		noteList.setAdapter(sAda);
		noteList.setOnItemClickListener(new OnItemClickListener() {

			

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				if(adlg!=null)
				{
					if(adlg.isShowing())return;
				}
				
				
				TextView t=(TextView)arg1.findViewById(R.id.note_id);
				final int noteId=Integer.parseInt(t.getText().toString());
				Intent addNoteIntent=new Intent(NoteList.this, WriteNotePage.class);
				addNoteIntent.putExtra("noteid", noteId);
				addNoteIntent.putExtra("flag", 0);//是通过点击ListView的列表项进入WriteNotePage页面的。
				
				startActivity(addNoteIntent);
				finish();
              }
			
		});
		noteList.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				// TODO Auto-generated method stub
				
				TextView t=(TextView)arg1.findViewById(R.id.note_id);
				final int noteId=Integer.parseInt(t.getText().toString());
				 adlg=new AlertDialog.Builder(NoteList.this)
				 .setTitle("标题") 
				 .setItems(new String[]{"删除"},new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface arg0, int arg1) {
						// TODO Auto-generated method stub
						noteDao.delete(noteId);
						FileOp fileOp=new FileOp();
						
						List<EditImage> imgList=noteDao.findByImgId(String.valueOf(noteId),null);
						int i;
						for(i=0;i<imgList.size();i++)
						{
							noteDao.deleteImg(imgList.get(i).getPath());
			    			fileOp.deleteFile(imgList.get(i).getPath());
						}
						nlist=noteDao.findBySql("select * from note where class=? and text like ?",new String[]{noteClass,searchText});
						initSimpleAdapter();
						noteList.setAdapter(sAda);
						
					}
				})
				
				 .show();
				
				return false;
			}
		});
		searchEdit.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
				// TODO Auto-generated method stub
				
				searchText="%"+arg0+"%";
				nlist=noteDao.findBySql("select * from note where class=? and (text like ? or title like ? )",new String[]{noteClass,searchText,searchText});
				
				
				initSimpleAdapter();
				noteList.setAdapter(sAda);
			}
			
			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
					int arg3) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void afterTextChanged(Editable arg0) {

				// TODO Auto-generated method stub
				if(arg0.length()==0)deleteBT.setVisibility(View.INVISIBLE);
				else deleteBT.setVisibility(View.VISIBLE);
			}
		});
		
	}
	public void onDelete(View view)
	{
		searchEdit.setText("");
	}
	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		nlist=noteDao.findBySql("select * from note where class=? and (text like ? or title like ? )",new String[]{noteClass,searchText,searchText});
		initSimpleAdapter();
		noteList.setAdapter(sAda);
	}
    void initSimpleAdapter()
    {
    	List<Map<String,Object>>list=new ArrayList<Map<String,Object>>();
    	
    	
    	//Toast.makeText(NoteList.this,String.valueOf(nlist.size()), 1).show();
    	int i;
    	String dateTime="";
    	for(i=0;i<nlist.size();i++)
    	{
    		Map<String,Object> map=new HashMap<String,Object>();
    		map.put("content",nlist.get(i).getTitle());
    		map.put("noteid",nlist.get(i).getId());
    		if(nlist.get(i).getYear()==cal.get(Calendar.YEAR))
    		{
    			if(nlist.get(i).getDay()==cal.get(Calendar.DAY_OF_MONTH))
    			{
    				dateTime=nlist.get(i).getTime();
    			}
    			else dateTime=String.valueOf(nlist.get(i).getMonth())+"/"+String.valueOf(nlist.get(i).getDay());
    				
    		}
    		else dateTime=String.valueOf(nlist.get(i).getYear())+"/"+String.valueOf(nlist.get(i).getMonth())+"/"+String.valueOf(nlist.get(i).getDay());
    		map.put("date", dateTime);
        	list.add(map);
    	}
    	String form[]={"content","date","noteid"};
    	int to[]={R.id.content,R.id.date,R.id.note_id};
    	sAda=new SimpleAdapter(this, list,R.layout.wujie_list_note_list,form, to);
    }
    void onAdd()
    {
    	addNote.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent=new Intent(NoteList.this,WriteNotePage.class);
				intent.putExtra("noteid",-1);
				intent.putExtra("flag", 1);//是通过点击Add按钮进入WriteNotePage页面的。
				intent.putExtra("noteClass", noteClass);
				startActivity(intent);
				finish();
			}
		});
    	
    }
    public void transparentTitleBar()
  	{
  		Window window = getWindow();
  		//下面的语句会导致报错，需要在资源文件中添加下面的语句：<color name="title_bar">#015092</color>
  		window.setBackgroundDrawableResource(R.color.title_bar);

  		Class clazz = window.getClass();
  		try {
  		int tranceFlag = 0;
  		int darkModeFlag = 0;
  		Class layoutParams = Class.forName("android.view.MiuiWindowManager$LayoutParams");

  		Field field = layoutParams.getField("EXTRA_FLAG_STATUS_BAR_TRANSPARENT");
  		tranceFlag = field.getInt(layoutParams);

  		field = layoutParams.getField("EXTRA_FLAG_STATUS_BAR_DARK_MODE");
  		darkModeFlag = field.getInt(layoutParams);

  		Method extraFlagField = clazz.getMethod("setExtraFlags", int.class, int.class);
  		//只需要状态栏透明
  		extraFlagField.invoke(window, tranceFlag, tranceFlag);
  		//或
  		//状态栏透明且黑色字体
  		extraFlagField.invoke(window, tranceFlag | darkModeFlag, tranceFlag | darkModeFlag);
  		//清除黑色字体 
  		//extraFlagField.invoke(window, 0, darkModeFlag);
  		} catch (NoSuchMethodException e) {
  		e.printStackTrace();
  		} catch (ClassNotFoundException e) {
  		e.printStackTrace();
  		} catch (NoSuchFieldException e) {
  		e.printStackTrace();
  		} catch (IllegalAccessException e) {
  		e.printStackTrace();
  		} catch (IllegalArgumentException e) {
  		e.printStackTrace();
  		} catch (InvocationTargetException e) {
  		e.printStackTrace();
  		}
  	}
}
