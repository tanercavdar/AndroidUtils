package tr.com.bracket.aiku360.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class NetworkUtils {

    private static Context _context;
    private static ConnectivityManager _connectivityManager;

    private NetworkUtils(){

    }

    public static void init(Context context){
        _context=context;
        _connectivityManager = (ConnectivityManager) _context.getSystemService(Context.CONNECTIVITY_SERVICE);
    }

    public static boolean isNetworkAvaible() {
        try {
            NetworkInfo activeNetworkInfo = _connectivityManager.getActiveNetworkInfo();
            return activeNetworkInfo != null && activeNetworkInfo.isConnected();
        } catch (Exception e) {
            return false;
        }
    }

}
