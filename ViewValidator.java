package tr.com.bracket.aiku360.utils;

import android.content.ContentValues;
import android.content.Context;
import android.widget.EditText;
import android.widget.Toast;

import com.valdesekamdem.library.mdtoast.MDToast;

public class ViewValidator {

     private ViewValidator() {

    }

    public static boolean isEmptyEditText(EditText editText, String... toastText) {
        String message = toastText.length > 0 ? toastText[0] : "";
        boolean invalid = false;
        if (editText == null)
            invalid = true;
        else if (editText.getText().toString().trim().equals(""))
            invalid = true;
        if (invalid && message.isEmpty() == false)
            ToastUtils.showErrorToast(message);
        return invalid;
    }

    public static boolean isEmptyEditTextWithZero(EditText editText, String... toastText) {
        String message = toastText.length > 0 ? toastText[0] : "";
        boolean invalid = false;
        String value = editText.getText().toString().trim();
        if (value.replace(".", "")
                .replace(",", "")
                .replace("0", "")
                .equals("")
                || value.startsWith(".")
                || value.startsWith(",")
                || value.endsWith(".")
                || value.endsWith(",")) {
            invalid = true;
        }
        if (invalid && message.isEmpty() == false)
            ToastUtils.showErrorToast(message);
        return invalid;
    }

}
