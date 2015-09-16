package wxm.com.androiddesign.utils;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Point;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.Size;
import android.view.Display;
import android.view.WindowManager;

import com.easemob.EMCallBack;
import com.easemob.EMError;
import com.easemob.chat.EMChatManager;
import com.easemob.exceptions.EaseMobException;

/**
 * Created by Zero on 8/28/2015.
 */
public class MyUtils{
    public static final String TAG="Test";
    public static final int MAIN=1;
    public static final int LOGIN=2;

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

    public static void signupHX(final String userName, final String password){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    EMChatManager.getInstance().createAccountOnServer(userName, password);
                } catch (EaseMobException e) {
                    e.printStackTrace();
                    int errorCode= e.getErrorCode();
                    if (errorCode==EMError.NONETWORK_ERROR){
                        Log.d(TAG,"网络异常");
                    }
                    else if (errorCode==EMError.USER_ALREADY_EXISTS){
                        Log.d(TAG,"用户已存在");
                    }
                    else if (errorCode==EMError.UNAUTHORIZED){
                        Log.d(TAG,"无权限");
                    }else {
                        Log.d(TAG,e.getMessage());
                    }
                }
            }
        }).start();
    }

//    public static void Login(Context context) {
//        Log.d(TAG,"LoginHX");
//        SharedPreferences prefs = context.getSharedPreferences("wxm.com.androiddesign", Context.MODE_PRIVATE);
//        final String easemobId=prefs.getString("easemobId", "error");
//        EMChatManager.getInstance().login(easemobId, "7777777", new EMCallBack() {
//            @Override
//            public void onSuccess() {
//                Log.i(TAG, "LoginSuccess");
//                Log.i(TAG,easemobId);
//            }
//
//            @Override
//            public void onError(int i, String s) {
//                Log.e(TAG, "LoginError");
//                Log.i(TAG, easemobId);
//            }
//
//            @Override
//            public void onProgress(int i, String s) {
//                Log.i(TAG,easemobId+" "+i+" "+s);
//            }
//        });
//    }


}
