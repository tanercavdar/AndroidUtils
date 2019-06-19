package tr.com.bracket.trade.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateTimeUtils {

    public Date stringToDate(String sDate, String sInputFormat) {
        try {
            SimpleDateFormat format = new SimpleDateFormat(sInputFormat);
            return format.parse(sDate);
        } catch (Exception e) {
            return null;
        }
    }

    public static String getNowDate(String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(new Date());
    }

    public static String getNowTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        return sdf.format(new Date());
    }

    public static String stringToString(String date, String inputFormat, String outputFormat) {
        try {
            SimpleDateFormat format = new SimpleDateFormat(inputFormat);
            Date newDate = null;
            newDate = format.parse(date);
            format = new SimpleDateFormat(outputFormat);
            return format.format(newDate);
        } catch (ParseException e) {
            return null;
        }
    }
    
      public static String dateToString(Date date,  String outputFormat) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(outputFormat);
            return sdf.format(date);
        } catch (Exception e) {
            return null;
        }
    }

}
