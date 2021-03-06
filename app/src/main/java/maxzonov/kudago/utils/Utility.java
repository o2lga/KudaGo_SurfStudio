package maxzonov.kudago.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.TimeZone;

import maxzonov.kudago.model.City;
import maxzonov.kudago.model.main.Event;

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

    public static boolean isNetworkAvailable(Context context) {

        ConnectivityManager connectivityManager =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager != null) {
            NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
            return  (networkInfo != null && networkInfo.getState() == NetworkInfo.State.CONNECTED);
        } return false;
    }

    public static ArrayList<String> fillImagesArray(Event event) {
        ArrayList<String> imageUrls = new ArrayList<>();
        for (int i = 0; i < event.getImages().size(); i++) {
            imageUrls.add(event.getImages().get(i).getImageUrl());
        }
        return imageUrls;
    }

    public static ArrayList<String> fillCitiesArray(ArrayList<City> cities) {
        ArrayList<String> stringsCity = new ArrayList<>();
        int citySize = cities.size();
        for (int i = 0; i < citySize; i++) {
            City itemCity = cities.get(i);
            stringsCity.add(itemCity.getName());
        }
        return stringsCity;
    }
}
