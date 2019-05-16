package tr.com.bracket.aiku360.utils;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;


import java.text.SimpleDateFormat;
import java.util.Date;

public class DatePicker {

    private DatePickerListener _listener;
    private Context _context;
   private String _title;
   private String _sDefaultDate;
   private final String _sDateFormat="dd.MM.yyyy";

   public String sSelectedDate ="";

    public DatePicker(Context _context, String... title) {
        this._context = _context;
        this._title = (title.length > 0 ? title[0] : "");
    }

    public void show(String sDefaulDate, DatePickerListener listener) {
        _listener = listener;
        _sDefaultDate =sDefaulDate.isEmpty() ? getNowDate() : sDefaulDate;
        int year = Integer.valueOf(dateToString(stringToDate(_sDefaultDate, _sDateFormat), "yyyy"));
        int month = Integer.valueOf(dateToString(stringToDate(_sDefaultDate, _sDateFormat), "M"));
        int day = Integer.valueOf(dateToString(stringToDate(_sDefaultDate, _sDateFormat), "dd"));
        final DatePickerDialog datePicker;
        datePicker = new DatePickerDialog(_context, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(android.widget.DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                //Tarih değiştikçe işlem yapılmasını istiyorsan kod yazabilirsin.
            }
        }, year, month - 1, day);
        datePicker.setTitle(_title);
        datePicker.setCancelable(true);
        datePicker.setButton(DatePickerDialog.BUTTON_NEGATIVE, "VAZGEÇ", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(_listener!= null)
                    _listener.onCancel();
            }
        });
        datePicker.setButton(DatePickerDialog.BUTTON_POSITIVE, "TAMAM", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String sDate = dateToString(stringToDate(datePicker.getDatePicker().getDayOfMonth() +
                        "." +
                        (datePicker.getDatePicker().getMonth() + 1) +
                        "." +
                        datePicker.getDatePicker().getYear(), _sDateFormat), _sDateFormat);
                Date dtDate= stringToDate(sDate,_sDateFormat);
                sSelectedDate=sDate;
                _listener.onSelect(sDate,dtDate);

            }
        });
        datePicker.show();
    }

    public String dateToString(Date dtDate, String sFormat) {
        if (dtDate == null)
            return "";
        else
            return (String) android.text.format.DateFormat.format(sFormat, dtDate);
    }

    public Date stringToDate(String sDate, String sInputFormat) {
        try {
            SimpleDateFormat format = new SimpleDateFormat(sInputFormat);
            return format.parse(sDate);
        } catch (Exception e) {
            return null;
        }
    }

    public String getNowDate() {
        SimpleDateFormat sdf = new SimpleDateFormat(_sDateFormat);
        return sdf.format(new Date());
    }

    public String getNowTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        return sdf.format(new Date());
    }

    public interface DatePickerListener {

        void onSelect(String sDate,Date dtDate);

        void onCancel();

    }

}
