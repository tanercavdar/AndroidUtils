package tr.com.bracket.aiku360.utils;

import java.text.DecimalFormat;

public class NumberUtils {

    public static Double stringToDouble(String s) {
        double dblZero = 0.0;
        if (s == null)
            return dblZero;
        if (s.length() > 0)
            if (Character.isDigit(s.charAt(0)))
                return Double.parseDouble(s);
        if (s.isEmpty())
            return dblZero;
        if (s.equals(""))
            return dblZero;
        return dblZero;
    }

    public static String formatDouble(Double doubleNumber, Integer... nPrecision) {
        if(doubleNumber==null)
            doubleNumber = 0.0;
        int nVirgulluHane = nPrecision.length > 0 ? nPrecision[0] : 0;
        String sAddZero = "";
        for (int i = 0; i < nVirgulluHane; i++) {
            sAddZero = sAddZero + "0";
        }
        return new DecimalFormat("###,###,##0" + (sAddZero.equals("") ? "" : "." + sAddZero)).format(doubleNumber);
    }

    public static String formatInteger(Integer number, Integer... nPrecision) {
        if (number == null)
            return "";
        double doubleNumber = Double.valueOf(number.toString());
        int nVirgulluHane = nPrecision.length > 0 ? nPrecision[0] : 0;
        String sAddZero = "";
        for (int i = 0; i < nVirgulluHane; i++) {
            sAddZero = sAddZero + "0";
        }
        return new DecimalFormat("###,###,##0" + (sAddZero.equals("") ? "" : "." + sAddZero)).format(doubleNumber);
    }
    
    public static Double editTextToDouble(EditText editText) {
        return stringToDouble(editText.getText().toString().replace(",", "."));
    }

    public static Integer editTextToInteger(EditText editText) {
        if (editText.getText().toString().trim().isEmpty())
            return 0;
        else
            return Integer.valueOf(editText.getText().toString().trim());
    }

}
