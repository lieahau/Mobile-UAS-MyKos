package id.ac.umn.mykos;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import java.util.Collection;

public class SharedPrefHandler {
    static final String PREF_SETTING = "PREF_SETTING";

    static final int ID_NUMERIC = 0;
    static final int ID_ALPHABET = 1;

    static  final String KEY_ID = "KEY_ID";
    static  final String KEY_DUEDATE = "KEY_DUEDATE";
    static  final String KEY_NUMBEROFROOM = "KEY_NUMBEROFROOM";
    static  final String KEY_LANDINGPAGE = "KEY_LANDINGPAGE";

    static final int LANDING_DASHBOARD = 0;
    static final int LANDING_OVERVIEW = 1;

    // Accept String and Int only
    public static void SetPref(Activity activity, String key, int value){
        SharedPreferences pref = activity.getSharedPreferences(PREF_SETTING, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putInt(key, (int)value);
        editor.apply();
    }

    public static void SetPref(Activity activity, String key, String value){
        SharedPreferences pref = activity.getSharedPreferences(PREF_SETTING, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(key, (String)value);
        editor.apply();
    }

    public static int GetPrefInt(Activity activity, String key){
        SharedPreferences pref = activity.getSharedPreferences(PREF_SETTING, Context.MODE_PRIVATE);
        if(pref.contains(key)){
            return pref.getInt(key, 0);
        }

        return 0;
    }

    public static String GetPrefString(Activity activity, String key){
        SharedPreferences pref = activity.getSharedPreferences(PREF_SETTING, Context.MODE_PRIVATE);
        if(pref.contains(key)){
            return pref.getString(key, "");
        }

        return "";
    }

}
