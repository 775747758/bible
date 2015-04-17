package com.orange.church;

import com.example.acts.MainActivity;
import com.example.acts.R;
import com.orange.test.Activity_BibleTestMain;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class Activity_Brief extends Activity{
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_brief);
	}
	
	public void back(View view) {
		Intent intent = new Intent(Activity_Brief.this, MainActivity.class);
		intent.putExtra("from",1);
		startActivity(intent);
		finish();
		overridePendingTransition(R.anim.zoomin, R.anim.zoomout);
	}

}
