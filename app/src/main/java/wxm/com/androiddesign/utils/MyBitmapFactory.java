package wxm.com.androiddesign.utils;

import android.graphics.Bitmap;
import android.util.Base64;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;

/**
 * Created by zero on 2015/7/13.
 */
public class MyBitmapFactory {
    public static String BitmapToString(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] b = baos.toByteArray();
//        String temp = Base64.encodeToString(b, Base64.DEFAULT);
        String temp="";
        try {
            temp = new String(b, "ISO-8859-1");
        Log.d("bitmap", "" + b.length);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return temp;
    }

    public static Bitmap StringToBitmap(String encodeString) {
        try {
            byte[] encodeByte = Base64.decode(encodeString, Base64.DEFAULT);
            Bitmap bitmap = android.graphics.BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
            return bitmap;
        } catch (Exception e) {
            e.getMessage();
            return null;
        }
    }
}
