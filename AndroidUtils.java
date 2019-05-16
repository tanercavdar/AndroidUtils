package tr.com.bracket.aiku360.utils;

import android.content.Context;
import android.provider.Settings;

public class AndroidUtils {

    private static Context _context;

    public static void init(Context context) {
        _context = context;
    }

    public static String getDeviceID() {
        try {
            return Settings.Secure.getString(_context.getContentResolver(), Settings.Secure.ANDROID_ID);
        } catch (Exception e) {
            return "";
        }
    }

}
