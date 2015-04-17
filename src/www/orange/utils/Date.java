package www.orange.utils;

import java.util.Calendar;

public class Date {

	private static int mYear;
	private static int mMonth;
	private static int mDay;

	public static String getCurrentDate() {
		final Calendar c = Calendar.getInstance();
		mYear = c.get(Calendar.YEAR);
		mMonth = c.get(Calendar.MONTH);
		mDay = c.get(Calendar.DAY_OF_MONTH);
		return mYear + "-" + mMonth + "-" + mDay;
	}

}
