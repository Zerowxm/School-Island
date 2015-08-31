package wxm.com.androiddesign.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Point;
import android.util.DisplayMetrics;
import android.util.Size;
import android.view.Display;
import android.view.WindowManager;

/**
 * Created by Zero on 8/28/2015.
 */
public class MyUtils {
    public static Point getScreenSize(Context context){
        DisplayMetrics displaymetrics = new DisplayMetrics();
        Display display;
        if(context instanceof  Activity){
            display=((Activity)context).getWindowManager().getDefaultDisplay();
        }
        else {
            WindowManager wm=(WindowManager)context.getSystemService(Context.WINDOW_SERVICE);
            display=wm.getDefaultDisplay();
        }
        display.getMetrics(displaymetrics);
        Point size=new Point();
        display.getSize(size);
        return size;
    }
}
