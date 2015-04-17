package com.wujie.example.note;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.example.acts.R;
import com.wujie.database.ClassDao;
import com.wujie.database.NoteDao;
import com.wujie.example.note.MainActivity.MyBaseAda;
import com.wujie.help.ClassInfo;
import com.wujie.help.EditImage;
import com.wujie.help.FileOp;
import com.wujie.help.NoteInfo;
import com.wujie.help.TopBarTransparent;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;

import android.app.AlertDialog.Builder;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.ContactsContract.Directory;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextWatcher;
import android.text.style.ForegroundColorSpan;
import android.text.style.ImageSpan;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.MotionEvent;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.RelativeLayout.LayoutParams;

public class WriteNotePage extends Activity {

	private BottomBar sbAda;
	int flag_list=0;
	int flag_num=0;
	int h;
	int galleryId;
	int textSize=16;
	int screenWidth;
	int screenHeight;
	private MyEditText noteText;
	private EditText title;
	int width;
	Button b;
	RelativeLayout r1;
	RelativeLayout r2;
	private static final int PHOTO_SUCCESS = 0;
	private static final int CAMERA_SUCCESS = 2;
	private static final ArrayList<Button> Button = null; 
	private ListView toolList;
	private ListView numList;
	private NoteDao noteDao;
	NumListAda numada;
	int fd=0;
	FileOp fileOp;
	String allnum[]={"一.","二.","三.","四.","五.","六.","七.","八.","九.","十."};
	private String filePath="/data/data/com.example.acts/";
	private String sdimg="/mnt/sdcard/Note/";
	private Calendar cal;
	int activityFlag;
	TextView noteClassText;
	int noteId;
	AlertDialog myDialog;
	
	String initWeek()
	{
		String week="";
        int weeknum=cal.get(Calendar.DAY_OF_WEEK);
        switch(weeknum)
        {
        case 1:
        	week="星期一";
        break;
        case 2:
        	week="星期二";
        break;
        case 3:
        	week="星期三";
        break;
        case 4:
        	week="星期四";
        break;
        case 5:
        	week="星期五";
        break;
        case 6:
        	week="星期六";
        break;
        case 7:
        	week="星期日";
        break;
        }
        return week;
	}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState); 
		fileOp=new FileOp();
		cal=Calendar.getInstance(); 
		noteDao=new NoteDao(this);
		WindowManager wm = (WindowManager)this.getSystemService(Context.WINDOW_SERVICE);
		screenWidth = wm.getDefaultDisplay().getWidth();
		screenHeight = wm.getDefaultDisplay().getHeight();
		float sdf = this.getResources().getDisplayMetrics().density;
		width= (int) (320 * sdf + 0.5f);//将dp转换为像素�??
        setContentView(R.layout.write_note_page);
        TopBarTransparent.transparentTitleBar(this);
		sbAda=new BottomBar();
		GridView smartBar=(GridView)this.findViewById(R.id.smart_bar);
		smartBar.setAdapter(sbAda);
		h=smartBar.getLayoutParams().height;
		Intent extarIntent=getIntent();
		String className;
		noteId=extarIntent.getIntExtra("noteid",0);
		activityFlag=extarIntent.getIntExtra("flag",1);
		r1=(RelativeLayout)this.findViewById(R.id.topt);
		r2=(RelativeLayout)this.findViewById(R.id.topbart);
		
		 File fi1=new File(filePath+"fontsize");
		 File fi2=new File(filePath+"topcolor");
		 int textCo=R.drawable.wujie_blue;
			Scanner pw1;
			Scanner pw2;
			if(fi1.exists()&&fi2.exists())
			{
				//Toast.makeText(WriteNotePage.this,"哈哈哈哈�?", 1).show();
				try {
					pw1=new Scanner(fi1);
					pw2=new Scanner(fi2);
					textSize=pw1.nextInt();
					textCo=pw2.nextInt();
					pw1.close();
					pw2.close();
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				r1.setBackgroundColor(textCo);	
				r2.setBackgroundColor(textCo);
			}
			
		
		noteText=(MyEditText)this.findViewById(R.id.content);
		noteText.setTextSize(textSize);
		title=(EditText)this.findViewById(R.id.title);
		TextView dateText=(TextView)this.findViewById(R.id.date);
		dateText.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				onBackPressed();
			}
		});
		
		TextView timeText=(TextView)this.findViewById(R.id.time);
		timeText.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				onBackPressed();
			}
		});
		noteClassText=(TextView)this.findViewById(R.id.note_class);
		
		if( activityFlag==0)
		{
			NoteInfo noteinfo=noteDao.findById(String.valueOf(noteId));
		
			if(noteinfo.getYear()==cal.get(Calendar.YEAR))dateText.setText(String.valueOf(noteinfo.getMonth())+"/"+String.valueOf(noteinfo.getDay()));
			else dateText.setText(String.valueOf(noteinfo.getYear())+"/"+String.valueOf(noteinfo.getMonth())+"/"+String.valueOf(noteinfo.getDay()));
			timeText.setText(noteinfo.getWeek()+" "+noteinfo.getTime());
			noteClassText.setText(noteinfo.getNoteClass());
			title.setText(noteinfo.getTitle());
			noteText.setText(noteinfo.getText());
			initImg();
		}
		else if( activityFlag==1)
		{
			className= extarIntent.getStringExtra("noteClass");
			noteClassText.setText(className);
			dateText.setText(String.valueOf(cal.get(Calendar.MONTH)+1)+"/"+String.valueOf(cal.get(Calendar.DAY_OF_MONTH)));
			timeText.setText(initWeek()+" "+cal.get(Calendar.HOUR_OF_DAY)+":"+cal.get(Calendar.MINUTE));
			
			
		}
		else if(activityFlag==2)
		{
			noteClassText.setText("未分类");
			dateText.setText(String.valueOf(cal.get(Calendar.MONTH)+1)+"/"+String.valueOf(cal.get(Calendar.DAY_OF_MONTH)));
			timeText.setText(initWeek()+" "+cal.get(Calendar.HOUR_OF_DAY)+":"+cal.get(Calendar.MINUTE));
		}
		
        toolList=(ListView)this.findViewById(R.id.tool_list);
        MyListAdapter listAda=new MyListAdapter();
		toolList.setAdapter(listAda);
		toolList.setVisibility(View.INVISIBLE);
		numList=(ListView)this.findViewById(R.id.tool_list1);
		numada=new NumListAda();
		numList.setAdapter(numada);
		numList.setVisibility(View.INVISIBLE);
		
		noteText.setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View arg0, MotionEvent arg1) {
				// TODO Auto-generated method stub
				
				if(flag_list==1)
				{
					toolList.setVisibility(View.INVISIBLE);
					flag_list=0;
				}
				return false;
			}
		});
		File fe = new File("/mnt/sdcard/Note");
		if(!fe.exists())fe.mkdir();
		onNoteClassText();
        //Toast.makeText(WriteNotePage.this,String.valueOf(month)+"�?"+time+" "+week, 1).show();
       // noteText.setText(String.valueOf(month)+"�?"+time+" "+week);
		
		
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		ContentResolver resolver = getContentResolver(); 
		
		//imagePath=getRealPathFromURI(originalUri);
        if (resultCode == Activity.RESULT_OK) {
        	switch (requestCode)
        	{
	        	case PHOTO_SUCCESS:
	        	String imagePath = null;
	        	Uri originalUri = data.getData(); 
	        	imagePath=getRealPathFromURI(originalUri);
	        	Bitmap bitmap = null;
	    			try {
	    				 
	    				Bitmap originalBitmap = BitmapFactory.decodeStream(resolver.openInputStream(originalUri));
	    				bitmap = resizeImage(originalBitmap, width, width*2/3);
	    			} catch (FileNotFoundException e) {
	    				e.printStackTrace();
	    			}
	    			if(bitmap != null){
	    				//根据Bitmap对象创建ImageSpan对象
	    				ImageSpan imageSpan = new ImageSpan(WriteNotePage.this, bitmap);
	    				//创建�?个SpannableString对象，以便插入用ImageSpan对象封装的图�?
	    				SpannableString spannableString = new SpannableString("[local]"+imagePath+"[/local]");
	    				//  用ImageSpan对象替换face
	    				spannableString.setSpan(imageSpan, 0, imagePath.length()+15, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
	    				//将�?�择的图片追加到EditText中光标所在位�?
	    				int index = noteText.getSelectionStart(); //获取光标�?在位�?
	    				Editable edit_text = noteText.getEditableText();
	    				if(index <0 || index >= edit_text.length()){
	    					edit_text.append(spannableString);
	    				}else{
	    					edit_text.insert(index, spannableString);
	    				}
	    			
	    				
	    			}else{
	    				Toast.makeText(WriteNotePage.this, "获取图片失败", Toast.LENGTH_SHORT).show();
	    			}
	        		break;
	        	case CAMERA_SUCCESS:
	        		Bundle extras = data.getExtras(); 
					Bitmap originalBitmap1 = (Bitmap) extras.get("data");
					if(originalBitmap1 != null){
						String timeStr=cal.getTime().toLocaleString();
						
						try {
							saveMyBitmap(timeStr,originalBitmap1);
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						bitmap = resizeImage(originalBitmap1, width, width*2/3);
						//根据Bitmap对象创建ImageSpan对象
						ImageSpan imageSpan = new ImageSpan(WriteNotePage.this, bitmap);
						//创建�?个SpannableString对象，以便插入用ImageSpan对象封装的图�?
						SpannableString spannableString = new SpannableString("[local]"+"/mnt/sdcard/Note/"+timeStr+".png"+"[/local]");
						//  用ImageSpan对象替换face
						spannableString.setSpan(imageSpan, 0,("[local]"+"/mnt/sdcard/Note/"+timeStr+".png"+"[/local]").length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
						//将�?�择的图片追加到EditText中光标所在位�?
						int index = noteText.getSelectionStart(); //获取光标�?在位�?
						Editable edit_text = noteText.getEditableText();
						if(index <0 || index >= edit_text.length()){
							edit_text.append(spannableString);
						}else{
							edit_text.insert(index, spannableString);
						}
					}else{
						Toast.makeText(WriteNotePage.this, "获取图片失败", Toast.LENGTH_SHORT).show();
					}
					break;
				default:
					break;
        	
        	}
        	
		    
		   }

	}
	private Bitmap resizeImage(Bitmap originalBitmap, int newWidth, int newHeight){
		int width = originalBitmap.getWidth();
		int height = originalBitmap.getHeight();
		
		//计算宽�?�高缩放�?
		float scanleWidth = (float)newWidth/width;
		float scanleHeight = (float)newHeight/height;
		//创建操作图片用的matrix对象 Matrix
		Matrix matrix = new Matrix();
		// 缩放图片动作
		matrix.postScale(scanleWidth,scanleHeight);
		//旋转图片 动作
		//matrix.postRotate(45); 
		// 创建新的图片Bitmap
		Bitmap resizedBitmap = Bitmap.createBitmap(originalBitmap,0,0,width,height,matrix,true);
		return resizedBitmap;
	}
	public String getRealPathFromURI(Uri contentUri) {
	       String[] proj = { MediaStore.Images.Media.DATA };
	       Cursor cursor = managedQuery(contentUri, proj, null, null, null);
	       int column_index
	  = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
	       cursor.moveToFirst();
	       return cursor.getString(column_index);
	   }
	//底部数字序号的ListView的监听器
	class NumListAda extends BaseAdapter
	{

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return 2;
		}

		@Override
		public Object getItem(int arg0) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public long getItemId(int arg0) {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public View getView(int arg0, View arg1, ViewGroup arg2) {
			// TODO Auto-generated method stub
			if(arg0==0)
			{
				View v=View.inflate(WriteNotePage.this, R.layout.wujie_all_num, null);
				GridView gv=(GridView)v.findViewById(R.id.all_num);
				gv.setAdapter(new AllNumGridAda());
				return v;
				
			}
			if(arg0==1)
			{
				View v=View.inflate(WriteNotePage.this, R.layout.wujie_num_style, null);
				GridView gv=(GridView)v.findViewById(R.id.num_style);
				gv.setAdapter(new NumGridAda());
				
				return v;
			}
			
			return null;
			
		}
		
	}
	//数字序号ListView第一栏的网格布局的监听器
	class AllNumGridAda extends BaseAdapter
	{

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return 10;
		}

		@Override
		public Object getItem(int arg0) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public long getItemId(int arg0) {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public View getView(int arg0, View arg1, ViewGroup arg2) {
			// TODO Auto-generated method stub
			final int d=arg0;
			Button bt=new Button(getApplicationContext());
			bt.setBackgroundColor(Color.TRANSPARENT);
			bt.setText(allnum[arg0]);
			bt.setTextSize(15);
			bt.setGravity(Gravity.CENTER);
			bt.setTextColor(Color.GRAY);
			GridView.LayoutParams g=new GridView.LayoutParams(h,h);
			bt.setLayoutParams(g);
			bt.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					int index = noteText.getSelectionStart(); //获取光标�?在位�?
    				Editable edit_text = noteText.getEditableText();
    				if(index <0 || index >= edit_text.length()){
    					edit_text.append(allnum[d]+" ");
    				}else{
    					edit_text.insert(index, allnum[d]+" ");
    				}
    				flag_num=0;
					numList.setVisibility(View.INVISIBLE);
				}
			});
			return bt;
		}
		
	}
	//数字序号ListView第二栏的网格布局的监听器
	class NumGridAda extends BaseAdapter
	{

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return 5;
		}

		@Override
		public Object getItem(int arg0) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public long getItemId(int arg0) {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public View getView(int arg0, View arg1, ViewGroup arg2) {
			// TODO Auto-generated method stub
			final int d=arg0;
			String num[]={"一.","1.","(1)","①","●"};
			Button bt=new Button(getApplicationContext());
			bt.setBackgroundColor(Color.TRANSPARENT);
			bt.setText(num[arg0]);
			bt.setGravity(Gravity.CENTER);
			bt.setTextColor(Color.GRAY);
			bt.setTextSize(15);
			GridView.LayoutParams g=new GridView.LayoutParams(h,h);
			bt.setLayoutParams(g);
			bt.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					
					if(d==1){String anum[]={"1.","2.","3.","4.","5.","6.","7.","8.","9.","10."};allnum=anum;}
					if(d==2){String anum[]={"(1)","(2)","(3)","(4)","(5)","(6)","(7)","(8)","(9)","(10)"};allnum=anum;}
					if(d==3){String anum[]={"①","②","③","④","⑤","⑥","⑦","⑧","⑨","⑩"};allnum=anum;}
					if(d==0){String anum[]={"一.","二.","三.","四.","五.","六.","七.","八.","九.","十."};allnum=anum;}	
					if(d==4){String anum[]={"●","○","★","☆","■","□","▲","△","♥","☺"};allnum=anum;}
					numList.setAdapter(new NumListAda());
					
				}
			});
			return bt;
		}
		
	}
	
	class BottomBar extends BaseAdapter
	{

		
		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return 5;
		}
		@Override
		public Object getItem(int arg0) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public long getItemId(int arg0) {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public View getView(int arg0, View arg1, ViewGroup arg2) {
			// TODO Auto-generated method stub
			final int d=arg0;
			Button bt=new Button(getApplicationContext());
			final int bk[]={R.drawable.wujie_pic,R.drawable.wujie_photo,R.drawable.wujie_menu,R.drawable.wujie_num,R.drawable.wujie_share};
			
			bt.setBackgroundResource(bk[arg0]); 
			//bt.setBackgroundColor(Color.BLUE);
			GridView.LayoutParams g=new GridView.LayoutParams(h,h);
			bt.setLayoutParams(g);
			bt.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					if(d==0)
					{
						if(flag_list==1)
						{
							toolList.setVisibility(View.INVISIBLE);
							flag_list=0;
							return ;
						}
						if(flag_num==1)
						{
							numList.setVisibility(View.INVISIBLE);
							flag_num=0;
							return ;
						}
						Intent intent=new Intent();
						intent.setAction(Intent.ACTION_PICK);
						intent.setType("image/*");
						startActivityForResult(intent, 0);
						
					}
					if(d==1)
					{
						if(flag_list==1)
						{
							toolList.setVisibility(View.INVISIBLE);
							flag_list=0;
							return ;
						}
						if(flag_num==1)
						{
							numList.setVisibility(View.INVISIBLE);
							flag_num=0;
							return ;
						}
						Intent getImageByCamera= new Intent("android.media.action.IMAGE_CAPTURE");   
						startActivityForResult(getImageByCamera, CAMERA_SUCCESS); 
					}
					if(d==2)
					{
						if(flag_list==0)
						{
							if(flag_num==1)
							{
								numList.setVisibility(View.INVISIBLE);
								flag_num=0;
								return ;
							}
							toolList.setVisibility(View.VISIBLE);
							flag_list=1;
						}
						else
						{
							toolList.setVisibility(View.INVISIBLE);
							flag_list=0;
						}
						
					}
					if(d==3)
					{
						
						if(flag_list==1)
						{
							toolList.setVisibility(View.INVISIBLE);
							flag_list=0;
							return ;
						}
						if(flag_num==0)
						{
							
							numList.setVisibility(View.VISIBLE);
							flag_num=1;
						}
						else 
						{
							numList.setVisibility(View.INVISIBLE);
							flag_num=0;
						}
						
						
					}
					if(d==4)
					{
						
						if(flag_list==1)
						{
							toolList.setVisibility(View.INVISIBLE);
							flag_list=0;
							return ;
						}
						if(flag_num==1)
						{
							
							numList.setVisibility(View.INVISIBLE);
							flag_num=0;
							return ;
						}
//						int curStart=noteText.getSelectionStart();
//						int curEnd=noteText.getSelectionEnd();
//						if(curStart== curEnd){Toast.makeText(WriteNotePage.this, "您还没有选择要高亮显示的文字", 0).show();return;}
//						SpannableString ss = new SpannableString(noteText.getText());
//						ss.setSpan(new ForegroundColorSpan(Color.RED),curStart ,curEnd,Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//						noteText.setText(ss);
//                        noteText.setSelection(curEnd);
//                        Toast.makeText(WriteNotePage.this, noteText.getText().toString(), 0).show();
						String nNewText=title.getText().toString()+"\n"+noteText.getText().toString().replaceAll("\\[local\\](/.*)*\\[/local\\]","");
						Intent sendIntent = new Intent();  
				    	sendIntent.setAction(Intent.ACTION_SEND);  
				    	sendIntent.setType("text/*");  
				    	sendIntent.putExtra(Intent.EXTRA_TEXT,nNewText); 
				    	startActivity(sendIntent);
                        
					}
					
				}
			});
			return bt;
		}

		
		
	}
	class ColorGridAdapter extends BaseAdapter
	{

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return 5;
		}

		@Override
		public Object getItem(int arg0) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public long getItemId(int arg0) {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public View getView(int arg0, View arg1, ViewGroup arg2) {
			// TODO Auto-generated method stub
			final int d=arg0;
			Button bt=new Button(getApplicationContext());
			final int bk[]={R.drawable.wujie_yellow,R.drawable.wujie_blue,R.drawable.wujie_green,R.drawable.wujie_red,R.drawable.wujie_gray};
			final int topColor[]={Color.rgb(237, 125,49),Color.rgb(91, 155,213),Color.rgb(58, 172,132),Color.rgb(224,136,170),Color.rgb(127, 127,127)};
			bt.setBackgroundResource(bk[arg0]); 
			GridView.LayoutParams g=new GridView.LayoutParams(h*3/5,h*3/5);
			bt.setLayoutParams(g);
            bt.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					r1.setBackgroundColor(topColor[d]);
					r2.setBackgroundColor(topColor[d]);
					File fi=new File(filePath+"topcolor");
					PrintWriter pw;
					try {
						pw=new PrintWriter(fi);
						pw.write(String.valueOf(topColor[d]));
						pw.close();
					} catch (FileNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
				}
			});
			return bt;
		}
		
	}
	class MyListAdapter extends BaseAdapter
	{

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return 2;
		}

		@Override
		public Object getItem(int arg0) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public long getItemId(int arg0) {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public View getView(int arg0, View arg1, ViewGroup arg2) {
			// TODO Auto-generated method stub
			
			if(arg0==0)
			{
				View v = View.inflate(WriteNotePage.this, R.layout.wujie_list_color, null);
				GridView gridColor=(GridView)v.findViewById(R.id.color_select);
				gridColor.setAdapter(new ColorGridAdapter());
				return v;
				
			}
			if(arg0==1)
			{
				View v = View.inflate(WriteNotePage.this, R.layout.wujie_list_font_size, null);
				Button asc=(Button)v.findViewById(R.id.asc);
				Button desc=(Button)v.findViewById(R.id.desc);
				asc.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View arg0) {
						// TODO Auto-generated method stub
						if(textSize>=50)Toast.makeText(WriteNotePage.this, "字体已达到最大", 0).show();
						else
						{
							textSize++;
							noteText.setTextSize(textSize);
							
						}
						
						 
					}
				});
                desc.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View arg0) {
						// TODO Auto-generated method stub
						if(textSize<=10)Toast.makeText(WriteNotePage.this, "字体已达到最小", 0).show();
						else
						{
							textSize--;
							noteText.setTextSize(textSize);
						}
					}
				});
                File fi=new File(filePath+"fontsize");
				PrintWriter pw;
				try {
					
					pw=new PrintWriter(fi);
					pw.write(String.valueOf(textSize));
					pw.close();
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				return v;
			}
			
			return null;
		}
		
	}
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		if(flag_list==1)
		{
			toolList.setVisibility(View.INVISIBLE);
			flag_list=0;
			return ;
		}
		if(flag_num==1)
		{
			
			numList.setVisibility(View.INVISIBLE);
			flag_num=0;
			return ;
		}
		super.onBackPressed();
		
		Pattern p=Pattern.compile("\\[local\\](/.*)*\\[/local\\]");//图片路径的正则式
        Matcher m=p.matcher(noteText.getText().toString());
        ArrayList <EditImage>imgList=new ArrayList<EditImage>(); 
        while(m.find())
        {
        	String res=m.group();
        	String pa=res.substring(7, res.length()-8);
        	EditImage img=new EditImage(pa,m.start());
        	imgList.add(img);
        	
        }
        String nNewText=noteText.getText().toString().replaceAll("\\[local\\](/.*)*\\[/local\\]","");
       //Toast.makeText(WriteNotePage.this,String.valueOf(imgList.get(1).getStart()), Toast.LENGTH_SHORT).show();
        String timeStr=cal.getTime().toLocaleString();
        int i,imgnum=0;
        
        String text=noteText.getText().toString();
        int year=cal.get(Calendar.YEAR);
        int month=cal.get(Calendar.MONTH)+1;
        int day=cal.get(Calendar.DAY_OF_MONTH);
        String time=String.valueOf(cal.get(Calendar.HOUR_OF_DAY))+":"+String.valueOf(cal.get(Calendar.MINUTE));
        String week="";
        int weeknum=cal.get(Calendar.DAY_OF_WEEK);
        
        switch(weeknum)
        {
        case 1:
        	week="星期一";
        break;
        case 2:
        	week="星期二";
        break;
        case 3:
        	week="星期三";
        break;
        case 4:
        	week="星期四";
        break;
        case 5:
        	week="星期五";
        break;
        case 6:
        	week="星期六";
        break;
        case 7:
        	week="星期日";
        break;
        }
        String className="";
        String titleText=title.getText().toString();
        if(titleText.equals("")) titleText="无标题";
        ClassDao s=new ClassDao(getApplicationContext());
        
        if((!s.findName((noteClassText.getText().toString())))&&(!text.equals("")))
        {
     	   s.add(noteClassText.getText().toString(),0,null);
     	 
        }
        int j;
        String fintext =nNewText;
        
        
        List<EditImage> SeditImageList=noteDao.findByImgId(String.valueOf(noteId),this); 
        if(activityFlag==0)
        {
        	
        	for(j=0;j<SeditImageList.size();j++)
    		{
    			noteDao.deleteImg(SeditImageList.get(j).getPath());
    			//fileOp.deleteFile(editImageList.get(j).getPath());
    			
    		}
        	
        	noteDao.updateNote(fintext,noteClassText.getText().toString(),titleText,noteId);
        	Toast.makeText(WriteNotePage.this,"已保存", Toast.LENGTH_SHORT).show();
        }
        else if(((activityFlag==1)||((activityFlag==2)))&&(!text.equals("")))
        	{
        	    noteDao.addNote(fintext, year, month, day, time, week,noteClassText.getText().toString(),titleText);
        	    Toast.makeText(WriteNotePage.this,"已保存", Toast.LENGTH_SHORT).show();
        	}
        List<NoteInfo> klis=noteDao.findBySql("select * from note", null);
        int fj=1;
        if(klis.size()>0)fj=klis.get(0).getId();
        if(activityFlag==0)
        {
        	for(i=0;i<imgList.size();i++)
    		{
            	//Toast.makeText(WriteNotePage.this, "加载图片失败", Toast.LENGTH_SHORT).show();
    			imgnum++;
    			String oldImgName=imgList.get(i).getPath();
    			String newImgName=sdimg+timeStr+String.valueOf(imgnum)+"."+oldImgName.substring(oldImgName.length()-3,oldImgName.length());
    			fileOp.copyFile(imgList.get(i).getPath(),newImgName);
    			noteDao.addImg(noteId, newImgName, imgList.get(i).getStart());
//    			if(i==0)fintext =fintext.substring(imgList.get(i).getStart(),imgList.get(i).getPath().length()+imgList.get(i).getStart());
//    			else fintext =fintext.substring(imgList.get(i).getStart()-imgList.get(i-1).getStart(),imgList.get(i).getStart()-imgList.get(i-1).getStart()+imgList.get(i).getPath().length());
    		}
        }
        else
        {
        	for(i=0;i<imgList.size();i++)
    		{
            	//Toast.makeText(WriteNotePage.this, "加载图片失败", Toast.LENGTH_SHORT).show();
    			imgnum++;
    			String oldImgName=imgList.get(i).getPath();
    			String newImgName=sdimg+timeStr+String.valueOf(imgnum)+"."+oldImgName.substring(oldImgName.length()-3,oldImgName.length());
    			fileOp.copyFile(imgList.get(i).getPath(),newImgName);
    			noteDao.addImg(fj, newImgName, imgList.get(i).getStart());
//    			if(i==0)fintext =fintext.substring(imgList.get(i).getStart(),imgList.get(i).getPath().length()+imgList.get(i).getStart());
//    			else fintext =fintext.substring(imgList.get(i).getStart()-imgList.get(i-1).getStart(),imgList.get(i).getStart()-imgList.get(i-1).getStart()+imgList.get(i).getPath().length());
    		}
        }
        if(activityFlag==0)
        {
        	 
        	for(j=0;j<SeditImageList.size();j++)
    		{
    			//noteDao.deleteImg(editImageList.get(j).getPath());
    			fileOp.deleteFile(SeditImageList.get(j).getPath());
    		}
        	
        }
        
       Intent noteList=new Intent(WriteNotePage.this,NoteList.class);
       noteList.putExtra("classname",noteClassText.getText().toString());
       startActivity(noteList);
       overridePendingTransition(R.anim.wujie_in_from_left, R.anim.wujie_out_to_right); 
       
       finish();
        
		
	}
	public void initImg()
    {
		ContentResolver resolver = getContentResolver(); 
		String iPath="";
    	int j;
    	if(activityFlag==0)
         {
         	List<EditImage> editImageList=noteDao.findByImgId(String.valueOf(noteId),this);
         	for(j=0;j<editImageList.size();j++)
     		{
         		//Toast.makeText(WriteNotePage.this, "来此", Toast.LENGTH_SHORT).show();
         		iPath=editImageList.get(j).getPath();

         		File gf=new File(iPath);
         		Bitmap bitmap=decodeFile(gf);
         		bitmap = resizeImage(bitmap, width, width*2/3);
         		

    			if(bitmap != null){
    				
    				//根据Bitmap对象创建ImageSpan对象
    				ImageSpan imageSpan = new ImageSpan(WriteNotePage.this, bitmap);
    				//创建�?个SpannableString对象，以便插入用ImageSpan对象封装的图�?
    				SpannableString spannableString = new SpannableString("[local]"+iPath+"[/local]");
    				//  用ImageSpan对象替换face
    				spannableString.setSpan(imageSpan, 0, iPath.length()+15, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
    				//将�?�择的图片追加到原来图片�?在位�?
    				int index = editImageList.get(j).getStart(); //获取原来图片�?在位�?
    				Editable edit_text = noteText.getEditableText();
    				if(index <0 || index >= edit_text.length()){
    					edit_text.append(spannableString);
    				}else{
    					edit_text.insert(index, spannableString);
    				}
    			
    				
    			}else{
    				Toast.makeText(WriteNotePage.this, "加载图片失败", Toast.LENGTH_SHORT).show();
    			}
     		}
         	 
         }
    }
	Uri getUri(String picPath)
	{
		Uri mUri = Uri.parse("content://media/external/images/media"); 
        Uri mImageUri = null;

        Cursor cursor = managedQuery(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI, null, null,
                null, MediaStore.Images.Media.DEFAULT_SORT_ORDER);
        cursor.moveToFirst();

        while (!cursor.isAfterLast()) {
            String data = cursor.getString(cursor
                    .getColumnIndex(MediaStore.MediaColumns.DATA));
            if (picPath.equals(data)) {
                int ringtoneID = cursor.getInt(cursor
                        .getColumnIndex(MediaStore.MediaColumns._ID));
                mImageUri = Uri.withAppendedPath(mUri, ""
                        + ringtoneID);
                break;
            }
            cursor.moveToNext();
        }
        return mImageUri;
	}
	private Bitmap decodeFile(File f) {
        try {
            // decode image size
            BitmapFactory.Options o = new BitmapFactory.Options();
            o.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(new FileInputStream(f), null, o);
 
            // Find the correct scale value. It should be the power of 2.
            final int REQUIRED_SIZE = 300;
            int width_tmp = o.outWidth, height_tmp = o.outHeight;
            int scale = 1;
            while (true) {
                if (width_tmp / 2 < REQUIRED_SIZE
                        || height_tmp / 2 < REQUIRED_SIZE)
                    break;
                width_tmp /= 2;
                height_tmp /= 2;
                scale *= 2;
            }
 
            // decode with inSampleSize
            BitmapFactory.Options o2 = new BitmapFactory.Options();
            o2.inSampleSize = scale;
            return BitmapFactory.decodeStream(new FileInputStream(f), null, o2);
        } catch (FileNotFoundException e) {
        }
        return null;
    }

	void onNoteClassText()
	{
		
	    noteClassText.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				myDialog = new AlertDialog.Builder(WriteNotePage.this).create(); 
				myDialog.show();
		        myDialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
			    myDialog.getWindow().setContentView(R.layout.wujie_class_dlg);
			    
			    final ListView title=(ListView)myDialog.getWindow().findViewById(R.id.list_class_name);
			    List<Map<String,Object>>list=new ArrayList<Map<String,Object>>();
			   
			    List<ClassInfo>infoList=new ArrayList<ClassInfo>();
			    final ClassDao cd=new ClassDao(getApplicationContext());
			    infoList=cd.findAll();
		     	int i;
		     	for(i=0;i<infoList.size();i++)
		     	{
		     		 Map<String,Object> map=new HashMap<String, Object>();
		     		 map.put("content", infoList.get(i).getName());
		     		list.add(map);
		     	}
		     	  Map<String,Object> map=new HashMap<String, Object>();
	     		 map.put("content", "新建分类");
	     		 list.add(map);
		     	String form[]={"content"};
		    	int to[]={R.id.dlg_list_item};
		    	SimpleAdapter sAda=new SimpleAdapter(getApplicationContext(), list,R.layout.wujie_list_class_dlg,form, to);
		    	title.setAdapter(sAda);
		    	final List<Map<String,Object>>li=list;
		    	title.setOnItemClickListener(new OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> arg0, View arg1,
							int arg2, long arg3) {
						// TODO Auto-generated method stub
						if(arg2==li.size()-1)
						{
							
					    	final AlertDialog myDialog1 = new AlertDialog.Builder(WriteNotePage.this).create(); 
							myDialog1.show();
							
		                    myDialog1.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
						    myDialog1.getWindow().setContentView(R.layout.wujie_create_newclass_dlg);
						    myDialog1.getWindow().findViewById(R.id.confirm).setOnClickListener(new OnClickListener() {
								
								@Override
								public void onClick(View arg0) {
									// TODO Auto-generated method stub
									EditText edit=(EditText)myDialog1.getWindow().findViewById(R.id.name);
									String newName=edit.getText().toString().trim();
									if(newName.equals(""))
									{
										 Toast.makeText(WriteNotePage.this, "分类名字不能为空!", 0).show();
										return;
									}
									if(cd.findName(newName))
									{
										 Toast.makeText(WriteNotePage.this, "不能起重复名字!", 0).show();
										 return;
									}
									else
									{
										li.remove(li.size()-1);
										Map<String,Object> map1=new HashMap<String, Object>();
							     		map1.put("content",newName);
							     		li.add(map1);
							     		Map<String,Object> map2=new HashMap<String, Object>();
							     		map2.put("content", "新建分类");
							     		li.add(map2);
							     		String form[]={"content"};
								    	int to[]={R.id.dlg_list_item};
								    	SimpleAdapter sAda=new SimpleAdapter(getApplicationContext(), li,R.layout.wujie_list_class_dlg,form, to);
								    	title.setAdapter(sAda);
										myDialog1.cancel();
									}
									
								}
							});
						    myDialog1.getWindow().findViewById(R.id.cancel).setOnClickListener(new OnClickListener() {
								
								@Override
								public void onClick(View arg0) {
									// TODO Auto-generated method stub
									myDialog1.cancel();
								}
							});
						}
						else
						{
							String selectClassName=(String)li.get(arg2).get("content");
							noteClassText.setText(selectClassName);
							myDialog.cancel();
						}
						
						
					}
				});
			}
			    
			    
			    
			
		});
	}
	public void saveMyBitmap(String bitName,Bitmap mBitmap) throws IOException {  
		    File f = new File("/mnt/sdcard/Note/" + bitName + ".png");  
		   
		    f.createNewFile();  
		    FileOutputStream fOut = null; 
		    try {  
		    	
		            fOut = new FileOutputStream(f);  
		           
		    } catch (FileNotFoundException e) {  
		            e.printStackTrace();  
		    }  
		    mBitmap.compress(Bitmap.CompressFormat.PNG, 100, fOut);  
		    
		    try {  
		            fOut.flush();  
		    } catch (IOException e) {  
		            e.printStackTrace();  
		    }  
		    try {  
		            fOut.close();  
		    } catch (IOException e) {  
		            e.printStackTrace();  
		    }  
		}  
    public void onBackNote(View v)
    {
    	onBackPressed();
    }
	public void transparentTitleBar()
	{
		Window window = getWindow();
		//下面的语句会导致报错，需要在资源文件中添加下面的语句�?<color name="title_bar">#015092</color>
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
		//�?
		//状�?�栏透明且黑色字�?
		extraFlagField.invoke(window, tranceFlag | darkModeFlag, tranceFlag | darkModeFlag);
		//清除黑色字体 
		extraFlagField.invoke(window, 0, darkModeFlag);
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

