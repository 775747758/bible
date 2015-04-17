package com.example.acts;

import www.orange.utils.ReadData;
import android.app.Activity;
import android.os.Bundle;
import android.widget.ListView;

public class Activity_Mem extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_memscriptures);
		ListView lv1 = (ListView) findViewById(R.id.memlv1);
		ReadData readdata = new ReadData(this);
		readdata.readFromMem(lv1);
	}
}
