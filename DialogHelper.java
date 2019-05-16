package tr.com.bracket.aiku360.utils;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import tr.com.bracket.aiku360.R;
import tr.com.bracket.aiku360.R;
import com.valdesekamdem.library.mdtoast.MDToast;

import java.util.ArrayList;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DialogHelper {

    Context _context;

    public DialogHelper(Context context) {
        this._context = context;
    }

    public void showToast(String message) {
        Toast.makeText(_context, message, Toast.LENGTH_SHORT).show();
    }

    public void listDialog(String title, String[] items, DialogInterface.OnClickListener... listener) {
        // new AlertDialog.Builder(_context)
        //         .setTitle(title)
        //         .setItems(items, listener)
        //         .show();
        AlertDialog.Builder builder = new AlertDialog.Builder(_context);
        builder.setTitle(title);
        builder.setItems(items, listener.length > 0 ? listener[0] : null);
        if (listener.length == 0) {
            builder.setPositiveButton("Tamam", null);
        }
        AlertDialog alert = builder.create();
        ListView listView = alert.getListView();
        listView.setDivider(new GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT, new int[]{0, 0xFF757575, 0}));
        //listView.setDivider(new ColorDrawable(this._context.getResources().getColor(R.color.colorPrimary)));
        listView.setDividerHeight(1);
        alert.show();
    }

    public void askDialog(String title, String message, DialogInterface.OnClickListener yesListener) {
        new AlertDialog.Builder(_context)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton("Evet", yesListener)
                .setNegativeButton("Hayır", null)
                .show();
    }

    public void basicDialog(String title, String message, DialogInterface.OnClickListener... okListener) {
        new AlertDialog.Builder(_context)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton("Tamam", okListener ==null  ? null : (okListener.length > 0 ? okListener[0] : null)  )
                .show();
    }

    public ProgressDialog getProgressDialog(String title, String message) {
        ProgressDialog dialog = new ProgressDialog(_context);
        dialog.setCancelable(false);
        dialog.setTitle(title);
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.setMessage(message.isEmpty() ? "Lütfen bekleyin" : message);
        return dialog;
    }

    public AlertDialog getListDialog(String title, List<Map<String, String>> mapList) {
        AlertDialog.Builder builder = new AlertDialog.Builder(_context);
        builder.setTitle("");
        builder.setItems(new CharSequence[]{"test"}, null);
        AlertDialog alert = builder.create();
        ListView listView = alert.getListView();
        //
        List<Map<String, String>> data = new ArrayList<Map<String, String>>();
        for (int i = 0; i < mapList.size(); i++) {
            Map model = mapList.get(i);
            Map<String, String> datum = new HashMap<String, String>(2);
            datum.put("title", model.keySet().toArray()[0].toString());
            datum.put("subTitle", model.values().toArray()[0].toString());
            data.add(datum);
        }
        SimpleAdapter adapter = new SimpleAdapter(_context, data,
                android.R.layout.simple_list_item_2,
                new String[]{"title", "subTitle"},
                new int[]{android.R.id.text1,
                        android.R.id.text2});
        //
        listView.setAdapter(adapter);
        //listView.setDivider(new ColorDrawable(Color.LTGRAY));
        listView.setDividerHeight(1);
        return alert;
    }



   // public void showSuccessToast(String message) {
   //     Toast toast = Toast.makeText(_context, message, Toast.LENGTH_SHORT);
   //     View view = toast.getView();
   //     TextView tv = view.findViewById(android.R.id.message);
   //     tv.setTextColor(Color.parseColor("#FFFFFF"));
   //     tv.setCompoundDrawablesRelativeWithIntrinsicBounds(android.R.drawable.ic_dialog_info, 0, 0, 0);
   //     view.setBackgroundColor(Color.parseColor("#66BB6A"));
   //     // MDToast mdToast = MDToast.makeText(_context, message, Toast.LENGTH_SHORT, MDToast.TYPE_INFO);
   //     // mdToast.show();
   //     toast.show();
   // }

}
