package tr.com.bracket.trade.utils;

import android.content.Context;
import android.util.Base64;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Random;

public class EncryptUtils {

    private static Context _context;

    public static void init(Context context) {
        _context = context;
    }

    private static String encryptBase64(String text) {
        return Base64.encodeToString((text).getBytes(StandardCharsets.UTF_8), Base64.NO_WRAP);
    }

    private static String descryptBase64(String encryptedText) {
        return new String(Base64.decode(encryptedText, Base64.NO_WRAP), StandardCharsets.UTF_8);
    }

    private static String hexToString(String hex) {
        try {
            int l = hex.length();
            byte[] data = new byte[l / 2];
            for (int i = 0; i < l; i += 2) {
                data[i / 2] = (byte) ((Character.digit(hex.charAt(i), 16) << 4)
                        + Character.digit(hex.charAt(i + 1), 16));
            }

            return new String(data, StandardCharsets.UTF_8);
        } catch (Exception e) {
            return "";
        }
    }

    private static String stringToHex(String text) {
        byte[] ba = text.getBytes();
        StringBuilder str = new StringBuilder();
        for (int i = 0; i < ba.length; i++)
            str.append(String.format("%x", ba[i]));
        return str.toString();
    }

    public String stringToJavaCodeWithEncryptCustom_ONLY_DEBUG(String text) {
        return stringToJavaCode_ONLY_DEBUG(customEncrypt(text));
    }

    public static String stringToJavaCode_ONLY_DEBUG(String text) {
        Random r = new Random(System.currentTimeMillis());
        byte[] b = text.getBytes();
        int c = b.length;
        StringBuilder sb = new StringBuilder();
        sb.append("(new Object() {");
        sb.append("int t;");
        sb.append("public String toString() {");
        sb.append("byte[] buf = new byte[");
        sb.append(c);
        sb.append("];");

        for (int i = 0; i < c; ++i) {
            int t = r.nextInt();
            int f = r.nextInt(24) + 1;

            t = (t & ~(0xff << f)) | (b[i] << f);

            sb.append("t = ");
            sb.append(t);
            sb.append(";");
            sb.append("buf[");
            sb.append(i);
            sb.append("] = (byte) (t >>> ");
            sb.append(f);
            sb.append(");");
        }

        sb.append("return new String(buf);");
        sb.append("}}.toString())");
        return sb.toString();
    }


    public static String customEncrypt(String text) {
        try {
            if (text.equals("")) {
                return text;
            }
            String sInput = text + _context.getPackageName();
            sInput = encryptBase64(sInput);
            sInput = stringToHex(sInput);
            String sResult = "";
            for (int i = 0; i < sInput.length(); i += 2) {
                sResult = sResult + Character.toString(sInput.charAt(i + 1)) + Character.toString(sInput.charAt(i));
            }
            if (sResult.length() > 4) {
                sResult = sResult.substring(0, 1) + sResult.substring(3, 4) + sResult.substring(2, 3) + sResult.substring(1, 2) + sResult.substring(4);
            }
            return sResult;
        } catch (Exception e) {
            return null;
        }
    }

    public static String customDescrypt(String encryptedText) {
        try {
            if (encryptedText.equals("")) {
                return encryptedText;
            }
            String sResult = "";
            if (encryptedText.length() > 4) {
                encryptedText = encryptedText.substring(0, 1) + encryptedText.substring(3, 4) + encryptedText.substring(2, 3) + encryptedText.substring(1, 2) + encryptedText.substring(4, encryptedText.length());
            }
            for (int i = 0; i < encryptedText.length(); i += 2) {
                sResult = sResult + Character.toString(encryptedText.charAt(i + 1)) + Character.toString(encryptedText.charAt(i));
            }
            sResult = hexToString(sResult);
            sResult = descryptBase64(sResult);
            sResult = sResult.replace(_context.getPackageName(), "");
            return sResult;
        } catch (Exception e) {
            return null;
        }
    }

    public static String encryptMD5(String text) {
        try {
            final MessageDigest digest = MessageDigest.getInstance("md5");
            digest.update(text.getBytes());
            final byte[] bytes = digest.digest();
            final StringBuilder sb = new StringBuilder();
            for (byte aByte : bytes) {
                sb.append(String.format("%02X", aByte));
            }
            return sb.toString().toLowerCase();
        } catch (Exception exc) {
            return "";
        }
    }


}
