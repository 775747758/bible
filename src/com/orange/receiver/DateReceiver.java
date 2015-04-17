package com.orange.receiver;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.Log;
import android.widget.Toast;

public class DateReceiver extends BroadcastReceiver {
	public String category1[] = new String[] { "  使徒行传  " };

	public String category2[][] = new String[][] { new String[] { "  1  ", "  2  ", "  3  ", "  4  ", "  5  ", "  6  ", "  7  ", "  8  ", "  9  ", "10", "11",
			"12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "23", "24", "26", "27", "28" } };
	public String category3[][] = new String[][] { new String[] { "  3  ", "  7  ", "  8  ", "  14  " },
			new String[] { "  18  ", "  28  ", "  32  ", "  42  ", "  44  ", "  46  " }, new String[] { "  6  ", "  19  " },
			new String[] { "  12  ", "  19  ", "  20  ", "  24  ", "  31  ", "  32  ", "  33  " },
			new String[] { "  29  ", "  30  ", "  31  ", "  41  ", "  42  " }, new String[] { "  3  ", "  4  ", "  10  " },
			new String[] { "  34  ", "  49  ", "  50  ", "  55  " }, new String[] { "  4  ", "  6  ", "  20  ", "  21  ", "  22  ", "  36  ", "  37  " },
			new String[] { "  15  ", "  16  ", "  31  ", "  36  " },
			new String[] { "  2  ", "  4  ", "  15  ", "  16  ", "  34  ", "  35  ", "  41  ", "  42  ", "  43  " },
			new String[] { "  17  ", "  18  ", "  21  ", "  23  ", "  24  " }, new String[] { "  5  ", "  23  " },
			new String[] { "  2  ", "  3  ", "  33  ", "  40  ", "  47  " }, new String[] { "  3  ", "  22  " },
			new String[] { "  8  ", "  9  ", "  10  ", "  11  ", "  26  " }, new String[] { "  5  ", "  25  ", "  31  " }, new String[] { "  11  " },
			new String[] { "  9  ", "  10  " }, new String[] { "  8  " },
			new String[] { "  20  ", "  22  ", "  23  ", "  24  ", "  28  ", "  32  ", "  35  " }, new String[] { "  13  " }, new String[] { "  11  " },
			new String[] { "  15  ", "  16  " }, new String[] { "  18" }, new String[] { "  24  ", "  25  " }, new String[] { "  15  ", "  30  ", "  31  " } };

	@Override
	public void onReceive(Context context, Intent intent) {

		Log.i("广播接受者", "收到信息");

		// TODO Auto-generated method stub
		SharedPreferences sp = context.getSharedPreferences("config", Context.MODE_PRIVATE);
		Editor editor = sp.edit();

		int tatay_scripture_volume_index = sp.getInt("tatay_scripture_volume_index", 0);
		int tatay_scripture_chapter_index = sp.getInt("tatay_scripture_chapter_index", 0);
		int tatay_scripture_scripture_index = sp.getInt("tatay_scripture_scripture_index", 0);

		Date date = new Date();
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

		editor.putString("Time", dateFormat.format(date));
		editor.commit();

		if ((tatay_scripture_scripture_index + 1) == category3[tatay_scripture_chapter_index].length) {
			if ((tatay_scripture_chapter_index + 1) == category2[tatay_scripture_volume_index].length) {
				if ((tatay_scripture_volume_index + 1) == category1.length) {
					Toast.makeText(context, "所有经文已经背完", Toast.LENGTH_LONG).show();
				} else {
					editor.putString("tatay_scripture_volume", category1[tatay_scripture_volume_index + 1]);
					editor.commit();
					tatay_scripture_volume_index = tatay_scripture_volume_index + 1;
					editor.putInt("tatay_scripture_volume_index", tatay_scripture_volume_index + 1);
				}
			} else {
				editor.putString("tatay_scripture_chapter", category2[tatay_scripture_volume_index][tatay_scripture_chapter_index + 1]);
				editor.putString("tatay_scripture_scripture", category3[tatay_scripture_chapter_index + 1][0]);
				editor.putInt("tatay_scripture_chapter_index", tatay_scripture_chapter_index + 1);
				editor.putInt("tatay_scripture_scripture_index", 0);
				editor.commit();
			}
		} else {
			editor.putString("tatay_scripture_scripture", category3[tatay_scripture_chapter_index][tatay_scripture_scripture_index + 1]);
			editor.putInt("tatay_scripture_scripture_index", tatay_scripture_scripture_index + 1);
			editor.commit();
		}

	}

}
