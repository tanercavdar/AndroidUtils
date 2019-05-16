package tr.com.bracket.aiku360.utils;

import android.content.Context;
import android.widget.Toast;

import com.valdesekamdem.library.mdtoast.MDToast;

public class ToastUtils {

    private static Context _context;

    public static void init(Context context){
        _context=context;
    }

    public static void showErrorToast(String message) {
        _showToast(message, MDToast.TYPE_ERROR);
    }

    public static void showInfoToast(String message) {
        _showToast(message, MDToast.TYPE_INFO);
    }

    public static void showSuccessToast(String message) {
        _showToast(message, MDToast.TYPE_SUCCESS);
    }

    public static void showSuccessToast() {
        _showToast("İşlem Başarılı", MDToast.TYPE_SUCCESS);
    }

    private static void _showToast(String message, int type) {
      MDToast.makeText(_context, message, Toast.LENGTH_SHORT, type).show();
    }

}
