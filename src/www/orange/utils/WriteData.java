package www.orange.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.example.acts.Activity_Splash;

import android.content.Context;
import android.content.SharedPreferences;

public class WriteData {

	public void mark(Context context) {
		SharedPreferences sp = context.getSharedPreferences("mark", Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = sp.edit();
		editor.putString("have", "1");
		editor.commit();
	}

	public void select(List<Map<String, String>> data, String filename) {
		try {
			File file = new File(filename);
			// 写入的时候接在后面写
			FileOutputStream os = new FileOutputStream(file, true);
			String str = new String();
			for (int i = 0; i < data.size(); i++) {
				Map map = data.get(i);
				String chapter = map.get("iconid").toString();
				String content = map.get("nametext").toString();
				os.write((chapter + "  " + content + "\n").getBytes());
			}
			os.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
