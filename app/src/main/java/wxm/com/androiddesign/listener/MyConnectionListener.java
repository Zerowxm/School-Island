package wxm.com.androiddesign.listener;

import android.util.Log;

import com.easemob.EMConnectionListener;
import com.easemob.EMError;

import wxm.com.androiddesign.utils.Config;

/**
 * Created by Zero on 9/16/2015.
 */
public class MyConnectionListener implements EMConnectionListener {
    @Override
    public void onConnected() {
        Log.d(Config.HX,"环信已连接");
    }

    @Override
    public void onDisconnected(final int error) {
        if (error== EMError.USER_REMOVED){
            Log.d(Config.HX,"账号已移除");
        }else if (error==EMError.CONNECTION_CONFLICT){
            Log.d(Config.HX,"账号在另外一个设备登录");
        }else {
            Log.d(Config.HX,"错误");
        }
    }
}
