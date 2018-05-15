package maxzonov.kudago.utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class Utility {

    public static String convertDatesPeriod(long dateStart, long dateEnd) {

        Date dateStartFormatted = new Date(dateStart * 1000L);
        Date dateEndFormatted = new Date(dateEnd * 1000L);

        DateFormat formatDay = new SimpleDateFormat("dd");
        DateFormat formatMonth = new SimpleDateFormat("MMMM");

        // TODO: different cities' timezones handling
        formatDay.setTimeZone(TimeZone.getTimeZone("GMT+3"));
        formatMonth.setTimeZone(TimeZone.getTimeZone("GMT+3"));

        String startDay = formatDay.format(dateStartFormatted);
        String startMonth = formatMonth.format(dateStartFormatted);
        String endDay = formatDay.format(dateEndFormatted);
        String endMonth = formatMonth.format(dateEndFormatted);

        String result;

        if (startDay.equals(endDay) && startMonth.equals(endMonth)) {
            result = startDay + " " + startMonth;
        } else if (startMonth.equals(endMonth)) {
            result = startDay + " - " + endDay + " " + startMonth;
        } else {
            result = startDay + " " + startMonth + " - " + endDay + " " + endMonth;
        }

        return result;
    }
}
