package wxm.com.maca.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Point;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.easemob.EMCallBack;
import com.easemob.EMError;
import com.easemob.chat.EMChatManager;
import com.easemob.exceptions.EaseMobException;

import cn.jpush.android.api.JPushInterface;
import wxm.com.maca.module.MyUser;
import wxm.com.maca.ui.MyApplication;

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
        MyUser.setEasemobId(userName);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    EMChatManager.getInstance().createAccountOnServer(userName, password);
                } catch (EaseMobException e) {
                    e.printStackTrace();
                    int errorCode= e.getErrorCode();
                    if (errorCode==EMError.NONETWORK_ERROR){
                        Toast.makeText(MyApplication.applicationContext,"网络异常",Toast.LENGTH_SHORT).show();
                        Log.d(TAG,"网络异常");
                    }
                    else if (errorCode==EMError.USER_ALREADY_EXISTS){
                        Log.d(TAG,"用户已存在");
                        Toast.makeText(MyApplication.applicationContext,"用户名已存在",Toast.LENGTH_SHORT).show();
                    }
                    else if (errorCode==EMError.UNAUTHORIZED){
                        Log.d(TAG,"无权限");
                        Toast.makeText(MyApplication.applicationContext,"无权限",Toast.LENGTH_SHORT).show();
                    }else {
                        Log.d(TAG,e.getMessage());
                    }
                }
            }
        }).start();
    }

    public static void Login(Context context) {
        Log.d(Config.HX, "LoginHX");
        SharedPreferences prefs = context.getSharedPreferences("wxm.com.androiddesign", Context.MODE_PRIVATE);
        final String easemobId=prefs.getString("easemobId", "error");
        EMChatManager.getInstance().login(easemobId, "7777777", new EMCallBack() {
            @Override
            public void onSuccess() {
                Log.i(Config.HX, "LoginSuccess");
                MyUser.setEasemobId(easemobId);
                //设置用户极光别名
                JPushInterface.setAlias(MyApplication.applicationContext, MyUser.easemobId, null);
            }

            @Override
            public void onError(int i, String s) {
                Log.e(Config.HX, "LoginError");
                Log.i(Config.HX, easemobId);
            }

            @Override
            public void onProgress(int i, String s) {
                Log.i(Config.HX, easemobId + " " + i + " " + s);
            }
        });
    }

    public static void setTextView(TextView tv) {
        tv.setSingleLine(false);
        tv.setEllipsize(TextUtils.TruncateAt.END);
        int n = 2; // the exact number of lines you want to display
        tv.setLines(n);
    }

    public static String getEditViewText(EditText editText){
        return editText.getText().toString();
    }

    public static void chooseImage(Activity activity,int requestCode){
        Intent photoPickerIntent = new Intent(Intent.ACTION_GET_CONTENT);
        photoPickerIntent.setType("image/*");
        Intent chooseImage = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        chooseImage.setType("image/*");
        Intent chooserIntent = Intent.createChooser(photoPickerIntent, "Select Image");
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[]{chooseImage});
        photoPickerIntent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        activity.startActivityForResult(chooserIntent,requestCode);
    }

    public static int getPixels(Context context,int dp){
        final float scale=context.getResources().getDisplayMetrics().density;
        return (int)(dp*scale+0.5);
    }
}
