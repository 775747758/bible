package www.orange.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.util.EncodingUtils;

import android.content.Context;
import android.content.Intent;
import android.os.Message;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.example.acts.R;

public class ReadData {
	Context context;

	public ReadData(Context context) {
		super();
		this.context = context;
	}

	public void readFromMem(ListView listview) {
		// lv1是不熟经文listview
		List<Map<String, Object>> data = new ArrayList<Map<String, Object>>();
		// LayoutInflater mInflater = getLayoutInflater();
		// final ListView lv1 = (ListView) (mInflater.inflate(R.layout.mem,
		// null)).findViewById(R.id.lv1);
		// 从不熟经文mem.txt文件中读取经文
		BufferedReader reader = null;
		Map<String, Object> map1 = new HashMap<String, Object>();
		Intent intent = new Intent();
		try {
			reader = new BufferedReader(new FileReader("/data/data/com.example.acts/mem.txt"));
			// char[] data=new char[1024*1024];
			String str = new String();
			while ((str = reader.readLine()) != null) {
				intent.getStringExtra("chapter");
				// 截取经节
				String chapter = str.substring(0, intent.getStringExtra("chapter").length());
				// 截取经文
				String content = str.substring(intent.getStringExtra("chapter").length() + 2);
				map1.put("nametext", content);
				map1.put("iconid", chapter);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				reader.close();
			} catch (Exception e) {
			}
		}
		data.add(map1);
		SimpleAdapter simpleAdapter = new SimpleAdapter(context, data, R.layout.listitem_memscriptures, new String[] { "nametext", "iconid" }, new int[] {
				R.id.tv4, R.id.tv3 });
		listview.setAdapter(simpleAdapter);
	}

	public static List<Map<String, String>> readFromMem() {
		List<Map<String, String>> data = new ArrayList<Map<String, String>>();

		String Chapter = new String();
		String Content = new String();
		BufferedReader reader2 = null;

		try {
			File file = new File("/data/data/com.example.acts/mem.txt");
			reader2 = new BufferedReader(new FileReader(file));
			String str = new String();
			while ((str = reader2.readLine()) != null) {
				Chapter = str.subSequence(0, 8).toString().trim();
				Content = str.substring(8).toString().trim();
				Map<String, String> map = new HashMap<String, String>();
				map.put("Content", Content);
				map.put("Chapter", Chapter);
				data.add(map);
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				reader2.close();
			} catch (Exception e) {
			}
		}
		return data;
	}

	public String getFromAsset(String fileName) {
		String result = "";
		try {
			InputStream in = context.getResources().getAssets().open(fileName);
			int length = in.available(); // 获取文件的字节数
			byte[] buffer = new byte[length]; // 创建byte数组
			in.read(buffer); // 将文件中的数据读取到byte数组中
			result = EncodingUtils.getString(buffer, "GB2312"); // 将字节数据转换成string
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	// 将assets文件夹中的acts.txt复制到act.txt
	public void copyFile(String filename, String newPath) {

		File file = new File(newPath);
		try {
			FileOutputStream fos = new FileOutputStream(file);
			// this.openFileOutput(newPath,MODE_WORLD_WRITEABLE);
			String old = getFromAsset(filename);
			fos.write(old.getBytes());
			fos.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// 查找act.txt中以chapter为经节的经文
	public String read(String filename, String chapter) {
		BufferedReader reader = null;
		String str = new String();
		try {
			copyFile(filename, "/data/data/com.example.acts/act.txt");
			reader = new BufferedReader(new FileReader("/data/data/com.example.acts/act.txt"));
			while ((str = reader.readLine()) != null) {
				if (str.startsWith(chapter + " "))
					break;
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				reader.close();
			} catch (Exception e) {
			}
		}
		return str.substring(chapter.length() + 2);
	}

}
