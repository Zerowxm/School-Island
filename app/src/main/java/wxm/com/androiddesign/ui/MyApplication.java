package wxm.com.androiddesign.ui;

import android.app.Application;
import android.util.Log;

import com.baidu.mapapi.SDKInitializer;

/**
 * Created by Administrator on 2015/7/3.
 */
public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        SDKInitializer.initialize(getApplicationContext());
    }
}
