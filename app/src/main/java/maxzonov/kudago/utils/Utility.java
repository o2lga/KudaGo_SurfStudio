package maxzonov.kudago.utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Utility {

    public static String convertDatesPeriod(long dateStart, long dateEnd) {

        Date dateStartFormatted = new Date(dateStart * 1000L);
        Date dateEndFormatted = new Date(dateEnd * 1000L);

        DateFormat formatDay = new SimpleDateFormat("dd");
        DateFormat formatMonth = new SimpleDateFormat("MMMM");

        String startDay = formatDay.format(dateStartFormatted);
        String startMonth = formatMonth.format(dateStartFormatted);
        String endDay = formatDay.format(dateEndFormatted);
        String endMonth = formatMonth.format(dateEndFormatted);

        String result;

        if (startMonth.equals(endMonth)) {
             result = startDay + " - " + endDay + " " + startMonth;
        } else if (startDay.equals(endDay) && startMonth.equals(endMonth)) {
            result = startDay + " " + startMonth;
        } else {
            result = startDay + " " + startMonth + " - " + endDay + " " + startMonth;
        }

        return result;
    }
}
