package com.example.acts;

import com.orange.read.Activity_SelectVolume;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class Activity_AppFunction extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_appfunction);
	}

	public void back(View view) {
		Intent intent = new Intent(Activity_AppFunction.this, Activity_SelectVolume.class);
		startActivity(intent);
		finish();
		overridePendingTransition(R.anim.zoomin, R.anim.zoomout);
	}

}
