package wxm.com.androiddesign.services;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.model.LatLng;

/**
 * Created by Curl on 2015/7/20.
 */
public class LocationServices extends Service {
    LocationClient mLocClient;
    public static double Latitude;
    public static double Longitude;
    MyLocationListenner myListener = new MyLocationListenner();
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private void InitGPS() {
        // 定位初始化
        mLocClient = new LocationClient(this);
        mLocClient.registerLocationListener(myListener);
        LocationClientOption option = new LocationClientOption();
        option.setOpenGps(true);// 打开gps
        option.setCoorType("bd09ll"); // 设置坐标类型
        option.setScanSpan(1000);    //设置定位间隔
        mLocClient.setLocOption(option);
        mLocClient.start();
    }

    @Override
    public void onCreate() {
        Log.e("CJ", "onServiceCreate");
        InitGPS();
        super.onCreate();
    }

    private class MyLocationListenner implements BDLocationListener {

        @Override
        public void onReceiveLocation(BDLocation location) {

            // map view 销毁后不在处理新接收的位置
            if (location == null)
                return;
            Latitude = location.getLatitude();
            Longitude = location.getLongitude();
            Log.e("CJ","location : " + Latitude + " " + Longitude);
        }
    }
}
