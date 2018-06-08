package maxzonov.kudago.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPreferenceManager {

    private SharedPreferences prefs;

    public SharedPreferenceManager(Context context) {
        prefs = context.getSharedPreferences("city_key", Context.MODE_PRIVATE);
    }

    public String readFromPrefs(String key) {

        String result = "";

        if (key.equals("current_city_slug")) {
            result = prefs.getString(key, "msk");
        } else if (key.equals("current_city_name")) {
            result = prefs.getString(key, "Москва");
        }

        return result;
    }

    public void writeToPrefs(String key, String value) {
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(key, value);
        editor.commit();
    }
}
