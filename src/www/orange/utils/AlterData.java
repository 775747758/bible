package www.orange.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;

import android.widget.EditText;

public class AlterData {

	public void alterData(EditText editText, String chapter) {
		BufferedReader reader2 = null;
		String contentt = editText.getText().toString().trim();// ȡ���޸ĺ�ľ���
		// �Ƚ�new.txt�ļ��еľ��Ķ�ȡ��String��
		try {
			File file = new File("/data/data/com.example.acts/new.txt");
			reader2 = new BufferedReader(new FileReader(file));
			String line = new String();
			String all = new String("");
			StringBuffer strb = new StringBuffer();
			while ((line = reader2.readLine()) != null) {
				// ������޸ĵľ��ڣ�����޸Ĺ��ľ����滻
				if (line.startsWith(chapter)) {
					all = strb.append(chapter + "  " + contentt + "\n").toString();
				} else {
					all = strb.append(line + "\n").toString();
				}
			}
			FileOutputStream os1 = new FileOutputStream(file);
			os1.write(all.getBytes());
			os1.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
			} catch (Exception e) {
			}
		}
	}
}
