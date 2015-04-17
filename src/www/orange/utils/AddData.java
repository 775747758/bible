package www.orange.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;

public class AddData {

	public List<Map<String, String>> add2list(Context context, String[] chapters, String volume) {
		List<Map<String, String>> data = new ArrayList<Map<String, String>>();
		ReadData readdata = new ReadData(context);

		for (int i = 0; i < chapters.length; i++) {
			Map<String, String> map = new HashMap<String, String>();
			map.put("nametext", readdata.read("acts.txt", chapters[i]));
			map.put("iconid", "¡¾" + volume + chapters[i] + "¡¿");
			data.add(map);
		}
		return data;
	}
}
