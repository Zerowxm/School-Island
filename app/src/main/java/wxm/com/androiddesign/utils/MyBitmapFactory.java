package wxm.com.androiddesign.utils;

import android.graphics.Bitmap;
import android.util.Base64;
import android.util.Log;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
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
            byte[]bytes = zip(b);
            Log.d("bytete",b.length+"");
            Log.d("bytete",b.length+"");
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
            byte[]encodeBytes = unZip(encodeByte);
            Bitmap bitmap = android.graphics.BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
            return bitmap;
        } catch (Exception e) {
            e.getMessage();
            return null;
        }
    }

    /***
     * 压缩Zip
     *
     * @param data
     * @return
     */
    public static byte[] zip(byte[] data) {
        byte[] b = null;
        try {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            ZipOutputStream zip = new ZipOutputStream(bos);
            ZipEntry entry = new ZipEntry("zip");
            entry.setSize(data.length);
            zip.putNextEntry(entry);
            zip.write(data);
            zip.closeEntry();
            zip.close();
            b = bos.toByteArray();
            bos.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return b;
    }

    /***
     * 解压Zip
     *
     * @param data
     * @return
     */
    public static byte[] unZip(byte[] data) {
        byte[] b = null;
        try {
            ByteArrayInputStream bis = new ByteArrayInputStream(data);
            ZipInputStream zip = new ZipInputStream(bis);
            while (zip.getNextEntry() != null) {
                byte[] buf = new byte[1024];
                int num = -1;
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                while ((num = zip.read(buf, 0, buf.length)) != -1) {
                    baos.write(buf, 0, num);
                }
                b = baos.toByteArray();
                baos.flush();
                baos.close();
            }
            zip.close();
            bis.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return b;
    }

}
