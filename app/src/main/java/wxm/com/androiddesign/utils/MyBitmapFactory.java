package wxm.com.androiddesign.utils;

import android.graphics.Bitmap;
import android.util.Base64;

import java.io.ByteArrayOutputStream;

/**
 * Created by zero on 2015/7/13.
 */
public class MyBitmapFactory {
    public static String BitmapToString(Bitmap bitmap){
        ByteArrayOutputStream baos=new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG,100,baos);
        byte[] b=baos.toByteArray();
        String temp= Base64.encodeToString(b,Base64.DEFAULT);
        return temp;
    }

    public static Bitmap StringToBitmap(String encodeString){
        try {
            byte[] encodeByte=Base64.decode(encodeString,Base64.DEFAULT);
            Bitmap bitmap= android.graphics.BitmapFactory.decodeByteArray(encodeByte,0,encodeByte.length);
            return bitmap;
        }catch (Exception e){
            e.getMessage();
            return null;
        }
    }
}