package com.orange.church;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import www.orange.utils.MyProgressPopUpWindow;

import com.example.acts.MainActivity;
import com.example.acts.R;
import com.orange.net.UpLoadChurchNotice;
import com.orange.net.UpLoadChurchTest;
import com.orange.test.Activity_BibleTestMain;
import com.orange.user.Activity_UserInfo;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

public class Activity_ChurchTest extends Activity {
	
	List<HashMap<String, String>> data=new ArrayList<HashMap<String,String>>();
	private ListView test;
	private ProgressDialog reDialog;
	private UpLoadChurchTest upLoadChurchTest;
	private SharedPreferences spChurchInfo;
	private SharedPreferences spUserInfo;
	private AlertDialog dialog; 
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_churchtest);
		test=(ListView)findViewById(R.id.test);
		spChurchInfo = getSharedPreferences("ChurchInfo", Context.MODE_PRIVATE);
		spUserInfo = getSharedPreferences("UserInfo", Context.MODE_PRIVATE);
		/*for(int i=0;i<4;i++)
		{
			HashMap<String, String> map=new HashMap<String, String>();  
			map.put("content", "��Ը����ϴ");
			map.put("churchName", sp.getString("churchName", ""));
			data.add(map);
		}
		SimpleAdapter adapter=new SimpleAdapter(this,data,R.layout.listitem_churchtest,new String[]{"content"},new int[]{R.id.content});*/

		
		/*reDialog = new ProgressDialog(this);
		reDialog.setMessage("���ڲ�ѯ��...");
		UpLoadChurchTest upLoadChurchTest =new UpLoadChurchTest(this, reDialog);
		List<String> allData=new ArrayList<String>();
				allData.add("�μ���ʶ�����ȷ�СȺ�̻�������ۣ�ÿ�µڶ���һ���ϣ�");
		allData.add("�μ�ӭ��Ү�յĿγ̣�ÿ�µ�һ�ܣ�");
		
	allData.add("�ȶ��μӾۻ�");
		allData.add("��ҪΪ�ҵ���");
		allData.add("ÿ������һ�����˷�����");
		allData.add("�ȶ��μ�ÿ����ѵѧϰ");
		
		
		
		
		allData.add("�μ�ʥ���о�����ѧϰ");
		
		
		allData.add("ѧϰ����ϵ�пγ�");
		allData.add("ÿ�������ͽ̻�ļ��˷���ͨ����������");
		
		allData.add("��Ը��μ������³��Ľ̻����ϵ���ᣨÿ�µ�һ����һ��6:30~9:30");
		allData.add("��Ը��μ���ʶҮ�ռ��̻�������ۻᣨÿ�µ�һ��������7:00~9:00");
		allData.add("��Ը����ϴ");
		allData.add("��Ը�����Ϊ������ʳ����");
		allData.add("��Ը��ÿ���ȶ������ޣ����棬����");
		allData.add("��Ը��ÿ����˵���һСʱ����");
		allData.add("��Ը��ÿ��μӳ��������ߵ���");
		allData.add("��Ը��μӱ������е����");
		allData.add("��Ը��ÿ��Ϊÿ���̻�ļ��˺����ߵ���");
		allData.add("��Ը��ÿ�������ĵ���100������");
		allData.add("��Ը��Ϊ5+5�������󵻸�");
		allData.add("��Ը��ÿ������һ�����˷�����");
		allData.add("��Ը��μ�ʥ�����ۣ��о�ѧϰ");
		allData.add("��Ը��������ͬ��������");
		allData.add("��Ը���Ķ��Ƽ��������鼮");
		allData.add("��Ը�Ȿ��ѧϰ����ϵ�пγ̣�����Σ���������������ȣ�");
		allData.add("��Ը�Ȿ�ܺͽ̻���˽�ͨ������֧Ԯ");
		allData.add("��Ը�����������������");
		allData.add("��Ը�����Ϊ������ʳ����");
		allData.add("��Ը��Ϊ���ؽ̻��ж������ߵ��棬�ռ����ϣ��Ӵ�δ���ߵȣ�");
		allData.add("��Ը����ʮ��֮һ���ף���Ǯ��ʱ��ȣ�");
		allData.add("��Ը�����ʮ��֮�����Ϸ��ף���Ǯ��ʱ��ȣ�");
		allData.add("��Ը�������̵ܽ�����Ϣ");
		
		upLoadChurchTest.addAllTest(allData, spChurchInfo.getString("churchName", ""));*/
		dialog=new MyProgressPopUpWindow(Activity_ChurchTest.this,"���ڲ�ѯ��...").createADialog();
		upLoadChurchTest =new UpLoadChurchTest(this, dialog);
		upLoadChurchTest.getChurchTest(test, spChurchInfo.getString("churchName", ""));
	}
	
	public void commit(View view)
	{
		Log.i("��JIAO", upLoadChurchTest.getResult());
		dialog=new MyProgressPopUpWindow(Activity_ChurchTest.this,"���ڲ�ѯ��...").createADialog();
		Intent intent = new Intent(Activity_ChurchTest.this, MainActivity.class);
		intent.putExtra("from", 1);
		upLoadChurchTest.commitResult(spChurchInfo.getString("churchName", ""), spUserInfo.getString("name", ""), upLoadChurchTest.getResult(),intent);
	}
	
	
	public void back(View view) {
		Intent intent = new Intent(Activity_ChurchTest.this, Activity_MyChurch.class);
		startActivity(intent);
		finish();
		overridePendingTransition(R.anim.zoomin, R.anim.zoomout);
	}
	

}
