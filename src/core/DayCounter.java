package core;

import java.util.Calendar;
import java.util.Date;

public class DayCounter {
	public static int daysUntil(Date fromDate, Date toDate) {
		Calendar f = Calendar.getInstance();
		f.setTime(fromDate);
		Calendar t = Calendar.getInstance();
		t.setTime(toDate);

		return daysUntil(f, t);
	}
	
    public static int daysUntil(Calendar fromDate, Calendar toDate) {
        return daysSinceEpoch(toDate) - daysSinceEpoch(fromDate);
    }

    public static int daysSinceEpoch(Calendar day) {
        int year = day.get(Calendar.YEAR);
        int month = day.get(Calendar.MONTH);
        int daysThisYear = cumulDaysToMonth[month] + day.get(Calendar.DAY_OF_MONTH) - 1;
        if ((month > 1) && isLeapYear(year)) {
            daysThisYear++;
        }

        return daysToYear(year) + daysThisYear;
    }

    public static boolean isLeapYear(int year) {
        return (year % 400 == 0) || ((year % 100 != 0) && (year % 4 == 0));
    }

    static int daysToYear(int year) {
        return (365 * year) + numLeapsToYear(year);
    }

    static int numLeapsToYear(int year) {
        int num4y = (year - 1) / 4;
        int num100y = (year - 1) / 100;
        int num400y = (year - 1) / 400;
        return num4y - num100y + num400y;
    }

    private static final int[] cumulDaysToMonth = { 0, 31, 59, 90, 120, 151,
            181, 212, 243, 273, 304, 334 };
}
