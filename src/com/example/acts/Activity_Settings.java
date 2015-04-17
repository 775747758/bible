package com.example.acts;

import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;

import www.orange.updateapk.createApopwindow;
import www.orange.utils.MyProgressPopUpWindow;
import www.orange.utils.UMShare;

import com.baidu.android.feedback.FeedbackManager;
import com.baidu.frontia.Frontia;
import com.orange.administrator.Admin_FeedBack;
import com.orange.church.Activity_ChurchInfo;
import com.orange.read.Activity_ReadBible2;
import com.orange.readnote.Activity_EditNote;
import com.orange.receiver.AlarmReceiver;
import com.orange.service.LockScreenService;
import com.orange.user.Activity_UserInfo;
import com.orange.view.ElasticScrollView;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import android.widget.ToggleButton;

public class Activity_Settings extends Activity {
	
	private LinearLayout temp;
	private ElasticScrollView sv;
	private LinearLayout temp1;
	private ToggleButton toggleLogin;
	private SharedPreferences spConfig;
	private Editor editor;
	private ToggleButton toggleLockScreen;
	private TextView time_tv;
	private PopupWindow popupWindow; 
	private UMShare umShare;
	private Calendar calendar;
	private PendingIntent sender;
	 public static final String API_KEY = "GdZNITcYcI3bhiwKx7duVbR7";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setting);
		umShare = new UMShare(this);
		
		initFeedback();
		
		Intent intent = new Intent(Activity_Settings.this, AlarmReceiver.class);
		sender = PendingIntent.getBroadcast(Activity_Settings.this, 0, intent, 0);
		
		sv = (ElasticScrollView) findViewById(R.id.sv);
		temp=(LinearLayout)findViewById(R.id.temp);
		temp1=(LinearLayout)findViewById(R.id.temp1);
		
		boolean isInit = Frontia.init(getApplicationContext(), "GdZNITcYcI3bhiwKx7duVbR7");
		if (!isInit) {
			Toast.makeText(getApplicationContext(), "您的app key 错误", 3).show();
			return;
		}
		
		spConfig =getSharedPreferences("SystemConfig", Context.MODE_PRIVATE);
		editor = spConfig.edit();
		
		
		time_tv=(TextView)findViewById(R.id.time_tv);
		time_tv.setText(spConfig.getInt("hour", 8)+" : "+spConfig.getInt("minute", 0));
		toggleLogin=(ToggleButton)findViewById(R.id.toggleLogin);
		
		toggleLogin.setOnCheckedChangeListener(new OnCheckedChangeListener(){
            public void onCheckedChanged(CompoundButton buttonView,
                    boolean isChecked) {
            	//toggleLogin.setChecked(isChecked);
				editor.putBoolean("isAutoLogin", isChecked);
				editor.commit();
            }

        });
		toggleLockScreen=(ToggleButton)findViewById(R.id.toggleLockScreen);
		toggleLockScreen.setOnCheckedChangeListener(new OnCheckedChangeListener(){
            public void onCheckedChanged(CompoundButton buttonView,
                    boolean isChecked) {
            	//toggleLockScreen.setChecked(isChecked);
				editor.putBoolean("isLockScreen", isChecked);
				editor.commit();
				if(isChecked){
					startService(new Intent(Activity_Settings.this, LockScreenService.class));
				}
				else{
					Intent iService=new Intent(Activity_Settings.this,LockScreenService.class);
					iService.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);    
					stopService(iService);
				}
            }

        });
		
	}
	public void back(View view) {
		Intent intent = new Intent(Activity_Settings.this, MainActivity.class);
		intent.putExtra("from",0);
		startActivity(intent);
		finish();
		overridePendingTransition(R.anim.zoomin, R.anim.zoomout);
	}

	public void time(View view)
	{
		calendar = Calendar.getInstance();   
        final int hour   = calendar.get(Calendar.HOUR_OF_DAY);  
        final int minute = calendar.get(Calendar.MINUTE);     
  
        final TimePickerDialog timePickerDialog = new TimePickerDialog(this, setting, hour, minute, true);  
        timePickerDialog.setTitle("提醒时间");
        timePickerDialog.show();  
	}
	
	//当点击TimePickerDialog控件的设置按钮时，调用该方法    
    TimePickerDialog.OnTimeSetListener setting = new TimePickerDialog.OnTimeSetListener()    
    {  
        @Override  
        public void onTimeSet(TimePicker view, int hour, int minute)   
        {  
        	time_tv.setText(hour+" : "+minute);
        	editor.putInt("hour", hour);
        	editor.putInt("minute", minute);
        	calendar.set(2014, 12, 0, hour, minute);
        	editor.putLong("remindtime", calendar.getTimeInMillis());
			editor.commit();
			
			
			long firstTime = SystemClock.elapsedRealtime();	// 开机之后到现在的运行时间(包括睡眠时间)
            long systemTime = System.currentTimeMillis();

            Calendar calendar = Calendar.getInstance();
		 	calendar.setTimeInMillis(System.currentTimeMillis());
		 	calendar.setTimeZone(TimeZone.getTimeZone("GMT+8")); // 这里时区需要设置一下，不然会有8个小时的时间差
		 	calendar.set(Calendar.MINUTE, minute);
		 	calendar.set(Calendar.HOUR_OF_DAY, hour);
		 	calendar.set(Calendar.SECOND, 0);
		 	calendar.set(Calendar.MILLISECOND, 0);

		 	// 选择的每天定时时间
		 	long selectTime = calendar.getTimeInMillis();	

		 	// 如果当前时间大于设置的时间，那么就从第二天的设定时间开始
		 	if(systemTime > selectTime) {
		 		Toast.makeText(Activity_Settings.this, "设置的时间小于当前时间", Toast.LENGTH_SHORT).show();
		 		calendar.add(Calendar.DAY_OF_MONTH, 1);
		 		selectTime = calendar.getTimeInMillis();
		 	}

		 	// 计算现在时间到设定时间的时间差
		 	long time = selectTime - systemTime;
	 		firstTime += time;

            // 进行闹铃注册
            AlarmManager manager = (AlarmManager)getSystemService(ALARM_SERVICE);
            manager.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP,
                            firstTime+10*1000,(24*60*60*1000), sender);

           // Toast.makeText(Activity_Settings.this, "设置重复闹铃成功! ", Toast.LENGTH_LONG).show();
        }   
        
    };
	
    
    public void update(View view)
    {
    	
    	AlertDialog dialog=new MyProgressPopUpWindow(Activity_Settings.this,"正在检测中...").createADialog();
    	createApopwindow.isNew(getApplicationContext(),Activity_Settings.this, getWindowManager(), view, dialog);
    	
    }
    
    public void feedback(View view)
    {
    	FeedbackManager.getInstance(getApplicationContext()).startFeedbackActivity();
    	/*Intent intent = new Intent(Activity_Settings.this, Admin_FeedBack.class);
		startActivity(intent);
		overridePendingTransition(R.anim.zoomin, R.anim.zoomout);*/
    }
    
    private void initFeedback() {
        FeedbackManager fm = FeedbackManager.getInstance(this);
        fm.register(API_KEY);
    }
    
    public void remark(View view)
    {
    	Uri uri = Uri.parse("market://details?id="+getPackageName());
		Intent intent = new Intent(Intent.ACTION_VIEW,uri);
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		if(isIntentAvailable(getApplicationContext(), intent))
		{
			startActivity(intent);
		}
		else
		{
			Toast.makeText(getApplicationContext(), "您的手机上还没有安装任何应用商店！", Toast.LENGTH_SHORT).show();
		}
    }
    public void about(View view)
    {
    		Intent intent = new Intent(Activity_Settings.this, Activity_AboutApp.class);
    		startActivity(intent);
    		overridePendingTransition(R.anim.zoomin, R.anim.zoomout);
    }
    
    public void recommend(View view)
    {
    	umShare.share("圣经流利说","推荐您安装使用《圣经流利说》，下载地址：http://app.mi.com/detail/59904", "http://app.mi.com/detail/59904", Activity_Settings.this);
    }
    //检测是否有应用商店
    public static boolean isIntentAvailable(Context context, Intent intent) {
        final PackageManager packageManager = context.getPackageManager();
        List<ResolveInfo> list = packageManager.queryIntentActivities(intent,
                PackageManager.GET_ACTIVITIES);
        return list.size() > 0;
    }

}
