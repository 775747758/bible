package com.example.acts;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

public class ShortCutActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		// ��ӿ�ݷ�ʽ����
		if (getIntent().getAction().equals(Intent.ACTION_CREATE_SHORTCUT)) {
			Intent intent = new Intent();
			intent.putExtra(Intent.EXTRA_SHORTCUT_NAME, "ʥ������˵");
			intent.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE, Intent.ShortcutIconResource.fromContext(this, R.drawable.ic_launcher_round));
			intent.putExtra(Intent.EXTRA_SHORTCUT_INTENT, new Intent(this, Activity_Splash.class));
			setResult(RESULT_OK, intent);
			finish();
		}
	}

}
