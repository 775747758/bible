package com.wujie.example.note;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.PrintWriter;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.example.acts.R;
import com.wujie.database.ClassDao;
import com.wujie.database.NoteDao;
import com.wujie.help.ClassInfo;
import com.wujie.help.FastBlur;
import com.wujie.help.NoteInfo;
import com.wujie.help.TopBarTransparent;



import android.os.Bundle;
import android.os.Environment;
import android.os.IBinder;
import android.app.Activity;
import android.app.AlertDialog;


import android.app.TimePickerDialog;
import android.app.TimePickerDialog.OnTimeSetListener;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Bitmap;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.ViewTreeObserver.OnPreDrawListener;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
public class MainActivity extends Activity {
    private ListView classList;
    AlertDialog myDialog;
    AlertDialog mDlg;
   private Calendar cal;
    private ClassDao myClass;
    private NoteDao myDao;
    MyBaseAda myBaseAda;
    String recordPath;
    String sdRecordPath;
    List<ClassInfo> list;
    int height;
    Button hmenu;
    AlertDialog adlg;
    
    @Override
	public void onBackPressed()
    {
    	finish();
    }
    
	 @Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.wujie_page_note_class);
		TopBarTransparent.transparentTitleBar(this);
		hmenu=(Button)this.findViewById(R.id.title_menu);
		onMenu();
		myClass=new ClassDao(this);
		myDao=new NoteDao(this);
		
		DisplayMetrics metric = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metric);
        
         height = metric.heightPixels;   // 屏幕高度（像素）
		 classList=(ListView)this.findViewById(R.id.classList);
		
		int classHeight=(int)(0.3*height);
		
		 classList.setPadding(classList.getPaddingLeft(), classHeight-50, classList.getPaddingRight(), classList.getPaddingBottom());
		 list=myClass.findAll();
		 
         myBaseAda=new MyBaseAda();
       
        classList.setAdapter(myBaseAda);
        classList.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
					int arg2, long arg3)
			{
									// TODO Auto-generated method stub
									
									if(arg2==list.size())return false;
									final int which=arg2;
									String hname[];
									if(list.get( which).getIfsecret()==0)
									{
										 hname=new String[]{"重命名","设置密码","删除分类"};
									}
									else
									{
										 hname=new String[]{"重命名","修改密码","取消密码","删除分类"};
								
									}
									//mcrejhguihrhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhh
									myDialog = new AlertDialog.Builder(MainActivity.this).create(); 
									myDialog.show();
								    myDialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
								    myDialog.getWindow().setContentView(R.layout.wujie_class_dlg);
								    TextView dlgTitle=(TextView)myDialog.getWindow().findViewById(R.id.dlg_title);
								    dlgTitle.setText("功能");
								     ListView title=(ListView)myDialog.getWindow().findViewById(R.id.list_class_name);
								     List<Map<String,Object>>listMap=new ArrayList<Map<String,Object>>();
								   
								   
								 	int i;
								 	for(i=0;i<hname.length;i++)
								 	{
								 		 Map<String,Object> map=new HashMap<String, Object>();
								 		 map.put("content", hname[i]);
								 		 listMap.add(map);
								 	}
								 	 
								 	String form[]={"content"};
									int to[]={R.id.dlg_list_item};
									SimpleAdapter sAda=new SimpleAdapter(getApplicationContext(), listMap,R.layout.wujie_list_class_dlg,form, to);
									title.setAdapter(sAda);
									final List<Map<String,Object>>li=listMap;
									title.setOnItemClickListener(new OnItemClickListener() {
								
										@Override
										public void onItemClick(AdapterView<?> arg0, View arg1,
												int arg2, long arg3) {
											// TODO Auto-generated method stub
											if(arg2==0)
											{
												myDialog.cancel();
												mDlg = new AlertDialog.Builder(MainActivity.this).create(); 
												mDlg.show();
												mDlg.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
												mDlg.getWindow().setContentView(R.layout.wujie_create_newclass_dlg);
												mDlg.getWindow().findViewById(R.id.confirm).setOnClickListener(new OnClickListener() {
													
													@Override
													public void onClick(View arg0) {
														// TODO Auto-generated method stub
														EditText edit=(EditText)mDlg.getWindow().findViewById(R.id.name);
														String newName=edit.getText().toString().trim();
														if(newName.equals(""))
														{
															Toast.makeText(MainActivity.this, "分类名字不能为空!", 0).show();
															return;
														}
														if(myClass.findName(newName))
														{
															 Toast.makeText(MainActivity.this, "不能起重复名字!", 0).show();
															 return;
														}
														else
														{
															
														
															myClass.update("update noteclass set name=? where id=?",new Object[]{newName,list.get(which).getId()} );
															myDao.update("update note set class=? where class=?", new Object[]{newName,list.get(which).getName()});
															list=myClass.findAll();
															myBaseAda=new MyBaseAda();
															classList.setAdapter(myBaseAda);
															mDlg.cancel();
															
														}
														
													}
												});
												mDlg.getWindow().findViewById(R.id.cancel).setOnClickListener(new OnClickListener() {
													
													@Override
													public void onClick(View arg0) {
														// TODO Auto-generated method stub
														mDlg.cancel();
													}
												});
											}
											if(arg2==1)
											{
												myDialog.cancel();
												if(list.get( which).getIfsecret()==0)
												{
													mDlg = new AlertDialog.Builder(MainActivity.this).create(); 
													mDlg.show();
													mDlg.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
													mDlg.getWindow().setContentView(R.layout.wujie_two_cecret_dlg);
													mDlg.getWindow().findViewById(R.id.confirm).setOnClickListener(new OnClickListener() {
														
														@Override
														public void onClick(View arg0) {
															// TODO Auto-generated method stub
															 EditText edit1=(EditText)mDlg.getWindow().findViewById(R.id.new_secret);
															 EditText edit2=(EditText)mDlg.getWindow().findViewById(R.id.again_secret);
															 String secretStr1=edit1.getText().toString().trim();
															 String secretStr2=edit2.getText().toString().trim();
															 if(secretStr1.equals(""))
															 {
																	Toast.makeText(MainActivity.this, "密码不能为空!", 0).show();
																	return;
															 }
															 if(secretStr1.equals(secretStr2))
															 {
																    myClass.update("update noteclass set ifsecret=? ,secret=? where id=?",new Object[]{"1",secretStr1,list.get(which).getId()} );
																	list=myClass.findAll();
																	myBaseAda=new MyBaseAda();
																	classList.setAdapter(myBaseAda);
																	mDlg.cancel();
																	Toast.makeText(MainActivity.this, "密码设置成功!", 0).show();
															 }
															 else  Toast.makeText(MainActivity.this, "两次输入的密码不一致!", 0).show();
															 
													      }
													});
													mDlg.getWindow().findViewById(R.id.cancel).setOnClickListener(new OnClickListener() {
														
														@Override
														public void onClick(View arg0) {
															// TODO Auto-generated method stub
															mDlg.cancel();
														}
													});
												    
												}
												else
												{
													mDlg = new AlertDialog.Builder(MainActivity.this).create(); 
													mDlg.show();
													mDlg.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
													mDlg.getWindow().setContentView(R.layout.wujie_secret_dlgg);
												    TextView title=(TextView)mDlg.getWindow().findViewById(R.id.title);
												    title.setText(list.get(which).getName());
												     EditText anewSEdit=(EditText)mDlg.getWindow().findViewById(R.id.secret);
												    anewSEdit.setHint("请输入原密码");
												    mDlg.getWindow().findViewById(R.id.confirm).setOnClickListener(new OnClickListener() {
														
														@Override
														public void onClick(View arg0) {
															// TODO Auto-generated method stub
															EditText newSEdit=(EditText)mDlg.getWindow().findViewById(R.id.secret);
															if(newSEdit.getText().toString().equals(""))
															{
																Toast.makeText(MainActivity.this, "密码不能为空!", 1).show();
																return;
								
															}
															if(newSEdit.getText().toString().equals(list.get(which).getSecret()))
															{
																mDlg.cancel();
																mDlg = new AlertDialog.Builder(MainActivity.this).create(); 
																mDlg.show();
																mDlg.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
																mDlg.getWindow().setContentView(R.layout.wujie_two_cecret_dlg);
																mDlg.getWindow().findViewById(R.id.confirm).setOnClickListener(new OnClickListener() {
																	
																	@Override
																	public void onClick(View arg0) {
																		// TODO Auto-generated method stub
																		 EditText edit1=(EditText)mDlg.getWindow().findViewById(R.id.new_secret);
																		 EditText edit2=(EditText)mDlg.getWindow().findViewById(R.id.again_secret);
																		 
																		 String secretStr1=edit1.getText().toString().trim();
																		 String secretStr2=edit2.getText().toString().trim();
																		 if(secretStr1.equals(""))
																		 {
																				Toast.makeText(MainActivity.this, "密码不能为空!", 0).show();
																				return;
																		 }
																		 if(secretStr1.equals(secretStr2))
																		 {
																			    myClass.update("update noteclass set ifsecret=? ,secret=? where id=?",new Object[]{"1",secretStr1,list.get(which).getId()} );
																				list=myClass.findAll();
																				myBaseAda=new MyBaseAda();
																				classList.setAdapter(myBaseAda);
																				mDlg.cancel();
																				Toast.makeText(MainActivity.this, "密码修改成功!", 0).show();
																		 }
																		 else  Toast.makeText(MainActivity.this, "两次输入的密码不一致!", 0).show();
																		 
																      }
																});
																mDlg.getWindow().findViewById(R.id.cancel).setOnClickListener(new OnClickListener() {
																	
																	@Override
																	public void onClick(View arg0) {
																		// TODO Auto-generated method stub
																		mDlg.cancel();
																	}
																});
																
															}
															else
															{
																Toast.makeText(MainActivity.this, "密码错误，请重新输入!", 0).show();
																
															}
														}
													});
												    mDlg.getWindow().findViewById(R.id.cancel).setOnClickListener(new OnClickListener() {
														
														@Override
														public void onClick(View arg0) {
															// TODO Auto-generated method stub
															mDlg.cancel();
														}
													});
												    
												}
												
											    
											}
											if(arg2==2)
											{
												myDialog.cancel();
												if(list.get( which).getIfsecret()==0)
												{
													mDlg = new AlertDialog.Builder(MainActivity.this).create();  
													mDlg.show();  
													mDlg.getWindow().setContentView(R.layout.wujie_confirm_dlg);  
													mDlg.getWindow().findViewById(R.id.remain).setOnClickListener(new View.OnClickListener() {  
											                             @Override  
											                           public void onClick(View v) {  
											                            	myClass.delete(list.get(which).getId());
											                            	myDao.delete(list.get(which).getName());
											     							list=myClass.findAll();
											    		                    myBaseAda=new MyBaseAda();
											     							classList.setAdapter(myBaseAda);
											     							mDlg.cancel();
											     							Toast.makeText(MainActivity.this, "分类删除成功!", 0).show();
													                            }  
													                        }); 
															mDlg.getWindow().findViewById(R.id.noremain).setOnClickListener(new View.OnClickListener() {  
													         @Override  
													                public void onClick(View v) { 
													        	 mDlg.cancel();
								 
													                }  
													        }); 
														}
														else
														{
															mDlg = new AlertDialog.Builder(MainActivity.this).create(); 
															mDlg.show();
															mDlg.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
															mDlg.getWindow().setContentView(R.layout.wujie_secret_dlgg);
														    TextView title=(TextView)mDlg.getWindow().findViewById(R.id.title);
														    title.setText(list.get(which).getName());
														    mDlg.getWindow().findViewById(R.id.confirm).setOnClickListener(new OnClickListener() {
																
																@Override
																public void onClick(View arg0) {
																	// TODO Auto-generated method stub
															EditText newSEdit=(EditText)mDlg.getWindow().findViewById(R.id.secret);
															if(newSEdit.getText().toString().equals(""))
															{
																Toast.makeText(MainActivity.this, "密码不能为空!", 0).show();
																return;
								
															}
															if(newSEdit.getText().toString().equals(list.get(which).getSecret()))
															{
																myClass.update("update noteclass set ifsecret=? ,secret=? where id=?",new Object[]{"0","",list.get(which).getId()} );
																list=myClass.findAll();
																myBaseAda=new MyBaseAda();
																classList.setAdapter(myBaseAda);
																mDlg.cancel();
																Toast.makeText(MainActivity.this, "密码已取消!", 0).show();
															}
															else
															{
																Toast.makeText(MainActivity.this, "密码错误，请重新输入!", 0).show();
																return;
															}
															
														}
													});
												    mDlg.getWindow().findViewById(R.id.cancel).setOnClickListener(new OnClickListener() {
														
														@Override
														public void onClick(View arg0) {
															// TODO Auto-generated method stub
															mDlg.cancel();
														}
													});
													
												}
											}
											if(arg2==3)
											{
												myDialog.cancel();
												mDlg = new AlertDialog.Builder(MainActivity.this).create();  
												mDlg.show();  
												mDlg.getWindow().setContentView(R.layout.wujie_confirm_dlg);  
												mDlg.getWindow().findViewById(R.id.remain).setOnClickListener(new View.OnClickListener() {  
										                             @Override  
										                           public void onClick(View v) {  
										                            	myClass.delete(list.get(which).getId());
										     							myDao.delete(list.get(which).getName());
										     							list=myClass.findAll();
										     							myBaseAda=new MyBaseAda();
										     							classList.setAdapter(myBaseAda);
										     							mDlg.cancel();
										     							Toast.makeText(MainActivity.this, "分类删除成功!", 0).show();
													     							
													                            }  
													                        }); 
												mDlg.getWindow().findViewById(R.id.noremain).setOnClickListener(new View.OnClickListener() {  
													         @Override  
													                public void onClick(View v) { 
													        	 mDlg.cancel();
								 
													                }  
													        }); 
															
											}
														
											}});
									return false;
													
												
				
			}});
        classList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				if(myDialog!=null)
				{
					if(myDialog.isShowing())return;
				}
				if(arg2==list.size())
				{
					
					myDialog = new AlertDialog.Builder(MainActivity.this).create(); 
					myDialog.show();
					
                    myDialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
				    myDialog.getWindow().setContentView(R.layout.wujie_create_newclass_dlg);
				    myDialog.getWindow().findViewById(R.id.confirm).setOnClickListener(new OnClickListener() {
						
						@Override
						public void onClick(View arg0) {
							// TODO Auto-generated method stub
							EditText edit=(EditText)myDialog.getWindow().findViewById(R.id.name);
							String newName=edit.getText().toString().trim();
							if(newName.equals(""))
							{
								 Toast.makeText(MainActivity.this, "分类名字不能为空!", 0).show();
								return;
							}
							if(myClass.findName(newName))
							{
								 Toast.makeText(MainActivity.this, "不能起重复名字!", 0).show();
								 return;
							}
							else
							{
								myClass.add(newName, 0,"");
								list=myClass.findAll();
								myBaseAda=new MyBaseAda();
								classList.setAdapter(myBaseAda);
								myDialog.cancel();
							}
							
						}
					});
				    myDialog.getWindow().findViewById(R.id.cancel).setOnClickListener(new OnClickListener() {
						
						@Override
						public void onClick(View arg0) {
							// TODO Auto-generated method stub
							myDialog.cancel();
						}
					});
				    
				}
				else
				{
					final int pwhich=arg2;
					if(list.get(arg2).getIfsecret()==0)
					{
						
						Intent i=new Intent(MainActivity.this,NoteList.class);
						i.putExtra("classname", list.get(arg2).getName());
						startActivity(i);//打开另个activity
					}
					else
					{
						myDialog = new AlertDialog.Builder(MainActivity.this).create(); 
						myDialog.show();
	                    myDialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
					    myDialog.getWindow().setContentView(R.layout.wujie_secret_dlgg);
					    TextView title=(TextView)myDialog.getWindow().findViewById(R.id.title);
					    title.setText(list.get(arg2).getName());
					    myDialog.getWindow().findViewById(R.id.confirm).setOnClickListener(new OnClickListener() {
							
							@Override
							public void onClick(View arg0) {
								// TODO Auto-generated method stub
								EditText newSEdit=(EditText)myDialog.getWindow().findViewById(R.id.secret);
								if(newSEdit.getText().toString().equals(""))
								{
									Toast.makeText(MainActivity.this, "密码不能为空!", 0).show();
									return;

								}
								if(newSEdit.getText().toString().equals(list.get(pwhich).getSecret()))
								{
									myDialog.cancel();
									Intent i=new Intent(MainActivity.this,NoteList.class);
									i.putExtra("classname", list.get(pwhich).getName());
									startActivity(i);
								}
								else
								{
									Toast.makeText(MainActivity.this, "密码错误，请重新输入!", 0).show();
									return;
								}
								
							}
						});
	                    myDialog.getWindow().findViewById(R.id.cancel).setOnClickListener(new OnClickListener() {
							
							@Override
							public void onClick(View arg0) {
								// TODO Auto-generated method stub
								myDialog.cancel();
							}
						});
					}
					
				}
				
			}
		});
		
		
	}
	 
	 @Override
	 protected void onStart() {
		 
	 	// TODO Auto-generated method stub
	 	super.onStart();
	 	list=myClass.findAll();
        myBaseAda=new MyBaseAda();
       classList.setAdapter(myBaseAda);
	 }
public void onMenu()
{
	 
		  hmenu.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				myDialog = new AlertDialog.Builder(MainActivity.this).create(); 
				myDialog.show();
		        myDialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
			    myDialog.getWindow().setContentView(R.layout.wujie_class_dlg);
			    TextView dlgTitle=(TextView)myDialog.getWindow().findViewById(R.id.dlg_title);
			    dlgTitle.setText("功能");
			    final ListView title=(ListView)myDialog.getWindow().findViewById(R.id.list_class_name);
			    List<Map<String,Object>>listMap=new ArrayList<Map<String,Object>>();
			   
			   int i;
		     	
		     		 Map<String,Object> map=new HashMap<String, Object>();
		     		 map.put("content","备份");
		     		listMap.add(map);
		     	
		     	  Map<String,Object> map1=new HashMap<String, Object>();
	     		 map1.put("content", "恢复备份");
	     		listMap.add(map1);
		     	String form[]={"content"};
		    	int to[]={R.id.dlg_list_item};
		    	SimpleAdapter sAda=new SimpleAdapter(getApplicationContext(),listMap,R.layout.wujie_list_class_dlg,form, to);
		    	title.setAdapter(sAda);
		    	final List<Map<String,Object>>li=listMap;
		    	title.setOnItemClickListener(new OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> arg0, View arg1,
							int arg2, long arg3) {
						// TODO Auto-generated method stub
						
						if(arg2==0)
						{
							myDialog.cancel();
							     recordPath="/data/data/com.example.acts/databases/holynote";
								 sdRecordPath=Environment.getExternalStorageDirectory().getAbsolutePath()+"/Note/holynote";
								 File sdfile=new File(Environment.getExternalStorageDirectory().getAbsolutePath()+"/Note");
								 File file=new File("/data/data/com.example.acts/databases/holynote");
								
								if(!file.exists())
								Toast.makeText(MainActivity.this,"您还没有创建笔记",0).show();
					      		else
					      		{
					      			if(!sdfile.exists())sdfile.mkdir();
					      			File f=new File(sdRecordPath);
					      			if(f.exists())deleteFile(sdRecordPath);
					          		copyFile(recordPath,sdRecordPath);
					          		Toast.makeText(MainActivity.this,"备份成功!备份目录:\n"+sdRecordPath,1).show();
					          		
					      		}
								
								
						}
						if(arg2==1)
						{
							myDialog.cancel();
							recordPath="/data/data/com.example.acts/databases/holynote";
							sdRecordPath=Environment.getExternalStorageDirectory().getAbsolutePath()+"/Note/holynote";
							File f=new File(sdRecordPath);
				      		if(!f.exists())
							{
							    Toast.makeText(MainActivity.this,"您还没有创建备份",0).show();
							    return ;
							}
				      		

				      		new AlertDialog.Builder(MainActivity.this).setTitle("恢复备份")  
				      		        .setMessage("恢复操作会把现有记录全部删除,您确认要恢复吗？")
				      		        .setPositiveButton("确定", new DialogInterface.OnClickListener(){  
				      		          
										File file=new File("/data/data/com.example.acts/databases/holynote");

				      		            public void onClick(DialogInterface dialog, int which) {  
				      	                // TODO Auto-generated method stub  
				      		        	 if(file.exists())
				      	      			{
				      	      				deleteFile(recordPath);
				      	      			}
				      	      			copyFile(sdRecordPath,recordPath);
				      	      			Toast.makeText(MainActivity.this,"已成功恢复备份!",0).show();
				      	      		    list=myClass.findAll();
									    myBaseAda=new MyBaseAda();
									    classList.setAdapter(myBaseAda);
				      	      		     
				      		            }  
				      		       })
				      		        .setNegativeButton("取消", new DialogInterface.OnClickListener() {  
				      		              
				      		            @Override  
				      		            public void onClick(DialogInterface dialog, int which) {  
				      		                // TODO Auto-generated method stub  
				      		                  
				      		           }  
				      		        }).show();  
						}
						
					}
				});
				
			}
		});
		
	 }
	class MyBaseAda extends BaseAdapter
	{

		public MyBaseAda()
		{
			
		}
		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			
			return list.size()+1;
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
			View v = View.inflate(MainActivity.this, R.layout.wujie_class_list_item, null);
			
			v.setBackgroundResource(R.drawable.wujie_class_list_item_pressed);
			TextView nameText=(TextView)v.findViewById(R.id.class_name);
			ImageView lockImg=(ImageView)v.findViewById(R.id.lock);
			TextView numText=(TextView)v.findViewById(R.id.num);
			if(arg0==list.size())
			{
				nameText.setText("新建分类");
				lockImg.setVisibility(View.GONE);
				numText.setText("");
				return v;
			}
			String name=list.get(arg0).getName();
			nameText.setText(name);
			int ifLock=list.get(arg0).getIfsecret();
			if(ifLock==1)
			{
				lockImg.setVisibility(View.VISIBLE);
			}
			else
			{
				lockImg.setVisibility(View.GONE);
			}
			
			List<NoteInfo> namelist=myDao.findBySql("select * from note where class=? ", new String[]{list.get(arg0).getName()});
			numText.setText(String.valueOf(namelist.size()));
			return v;
		}
		
		
		
	}
	
	public boolean copyFile(String oldPath, String newPath) { 
   	   boolean isok = true;
          try { 
              int bytesum = 0; 
              int byteread = 0; 
              File oldfile = new File(oldPath); 
              if (oldfile.exists()) { //文件存在时 
                  InputStream inStream = new FileInputStream(oldPath); //读入原文件 
                  FileOutputStream fs = new FileOutputStream(newPath); 
                  byte[] buffer = new byte[1024]; 
                  int length; 
                  while ( (byteread = inStream.read(buffer)) != -1) { 
                      bytesum += byteread; //字节数 文件大小 
                      //System.out.println(bytesum); 
                      fs.write(buffer, 0, byteread); 
                  } 
                  fs.flush(); 
                  fs.close(); 
                  inStream.close(); 
              }
              else
              {
   			isok = false;
   		   }
          } 
          catch (Exception e) { 
             // System.out.println("复制单个文件操作出错"); 
             // e.printStackTrace(); 
              isok = false;
          } 
          return isok;

      } 

      /** 
        * 复制整个文件夹内容 
        * @param oldPath String 原文件路径 如：c:/fqf 
        * @param newPath String 复制后路径 如：f:/fqf/ff 
        * @return boolean 
        */ 
      public boolean copyFolder(String oldPath, String newPath) { 
   	   boolean isok = true;
          try { 
              (new File(newPath)).mkdirs(); //如果文件夹不存在 则建立新文件夹 
              File a=new File(oldPath); 
              String[] file=a.list(); 
              File temp=null; 
              for (int i = 0; i < file.length; i++) { 
                  if(oldPath.endsWith(File.separator)){ 
                      temp=new File(oldPath+file[i]); 
                  } 
                  else
                  { 
                      temp=new File(oldPath+File.separator+file[i]); 
                  } 

                  if(temp.isFile()){ 
                      FileInputStream input = new FileInputStream(temp); 
                      FileOutputStream output = new FileOutputStream(newPath + "/" + 
                              (temp.getName()).toString()); 
                      byte[] b = new byte[1024 * 5]; 
                      int len; 
                      while ( (len = input.read(b)) != -1) { 
                          output.write(b, 0, len); 
                      } 
                      output.flush(); 
                      output.close(); 
                      input.close(); 
                  } 
                  if(temp.isDirectory()){//如果是子文件夹 
                      copyFolder(oldPath+"/"+file[i],newPath+"/"+file[i]); 
                  } 
              } 
          } 
          catch (Exception e) { 
       	    isok = false;
          } 
          return isok;
      }
   	////////////////////////////////////////////////////////////
      public boolean DeleteFolder(String sPath) {  
    	  boolean  flag = false;  
    	   File file = new File(sPath);  
    	    // 判断目录或文件是否存在  
    	    if (!file.exists()) {  // 不存在返回 false  
    	        return flag;  
    	    } else {  
    	        // 判断是否为文件  
    	       if (file.isFile()) {  // 为文件时调用删除文件方法  
    	            return deleteFile(sPath);  
    	      } else {  // 为目录时调用删除目录方法  
    	           return deleteDirectory(sPath);  
    	       }  
    	    }  
    	}
   public boolean deleteFile(String sPath) {  
    	    boolean flag = false;  
    	   File file = new File(sPath);  
    	   // 路径为文件且不为空则进行删除  
    	    if (file.isFile() && file.exists()) {  
    	       file.delete();  
    	       flag = true;  
    	   }  
    	   return flag;  
    	}  
      public boolean deleteDirectory(String sPath) {  
	      //如果sPath不以文件分隔符结尾，自动添加文件分隔符  
	     if (!sPath.endsWith(File.separator)) {  
	         sPath = sPath + File.separator;  
	       }  
	       File dirFile = new File(sPath);  
	      //如果dir对应的文件不存在，或者不是一个目录，则退出  
	       if (!dirFile.exists() || !dirFile.isDirectory()) {  
	           return false;  
	       }  
	       boolean flag = true;  
	       //删除文件夹下的所有文件(包括子目录)  
	       File[] files = dirFile.listFiles();  
	     for (int i = 0; i < files.length; i++) {  
	          //删除子文件  
	           if (files[i].isFile()) {  
	               flag = deleteFile(files[i].getAbsolutePath());  
	               if (!flag) break;  
	           } //删除子目录  
	           else {  
	               flag = deleteDirectory(files[i].getAbsolutePath());  
	               if (!flag) break;  
	           }  
	       }  
	       if (!flag) return false;  
	       //删除当前目录  
	       if (dirFile.delete()) {  
	           return true;  
	       } else {  
	           return false;  
	       }  
	   }  
      public void  onAddNote(View view)
      {
    	  Intent addNewNote=new Intent(MainActivity.this,WriteNotePage.class);
    	  addNewNote.putExtra("flag", 2);
    	  startActivity(addNewNote);
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

