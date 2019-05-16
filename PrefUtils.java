package tr.com.bracket.aiku360.utils;

import android.content.Context;
import android.content.SharedPreferences;


import tr.com.bracket.aiku360.BuildConfig;

public class PrefUtils {

    private static Context _context;

    private static final String PREF_FILE_NAME = BuildConfig.APPLICATION_ID + "_prefs";

    public static void init(Context context) {
        _context = context;
    }

    public static void save(String sKey, String sValue) {
        SharedPreferences prefs = _context.getSharedPreferences(PREF_FILE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(EncryptUtils.customEncrypt( sKey), EncryptUtils.customEncrypt(sValue));
        editor.apply();
        editor.commit();
    }


    public static String read(String sKey, String sDefaultValue) {
        SharedPreferences prefs = _context.getSharedPreferences(PREF_FILE_NAME, Context.MODE_PRIVATE);
        String read=prefs.getString(EncryptUtils.customEncrypt(sKey), EncryptUtils.customEncrypt(sDefaultValue));
        return EncryptUtils.customDescrypt(read);
    }

    public static void delete(String sKey) {
        SharedPreferences prefs = _context.getSharedPreferences(PREF_FILE_NAME, Context.MODE_PRIVATE);
        prefs.edit().remove(EncryptUtils.customEncrypt(sKey)).apply();
    }

    public static void deleteAllPrefs() {
        _context.getSharedPreferences(PREF_FILE_NAME, Context.MODE_PRIVATE).edit().clear().apply();
    }

}
