package tr.com.bracket.trade.utils.views;

import android.text.InputFilter;
import android.text.Spanned;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DigitFilter  implements InputFilter
{
    Pattern mPattern;

    public DigitFilter(int digitsBeforeZero,int digitsAfterZero)
    {
        mPattern=Pattern.compile("[0-9]{0," + (digitsBeforeZero) + "}+((\\.[0-9]{0," + (digitsAfterZero) + "})?)||(\\.)?");
    }

    @Override
    public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend)
    {
        StringBuilder sb = new StringBuilder(dest);
        sb.insert(dstart, source, start, end);

        Matcher matcher = mPattern.matcher(sb.toString());
        if(!matcher.matches())
            return "";
        return null;
    }
}