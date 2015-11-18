package wxm.com.maca.utils;

import android.os.Build;

/**
 * Created by Zero on 9/16/2015.
 */
public class Config {
    public static String HX="huanxin";
    public static String appBar="appBar";
    public static int api_version= Build.VERSION.SDK_INT;
    public static int L=Build.VERSION_CODES.LOLLIPOP;

    public static boolean laterLollipop(){
        return api_version>=L;
    }
}
