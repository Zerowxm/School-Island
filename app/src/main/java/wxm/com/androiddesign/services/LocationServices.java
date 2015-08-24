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
    public static String City = "厦门市";
    public static String Address;
    MyLocationListenner myListener = new MyLocationListenner();

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private void InitGPS() {

        mLocClient = new LocationClient(this);
        mLocClient.registerLocationListener(myListener);
        LocationClientOption option = new LocationClientOption();
        option.setOpenGps(true);
        option.setCoorType("bd09ll");
        option.setScanSpan(0);
        option.setIsNeedAddress(true);
        option.setIgnoreKillProcess(false);
        mLocClient.setLocOption(option);
        mLocClient.start();
    }

    @Override
    public void onCreate() {
        Log.e("CJ", "onLocationServiceCreate");
        InitGPS();
        super.onCreate();
    }

    @Override
    public void onDestroy() {
        Log.e("CJ", "onLocationServiceDestory");
        super.onDestroy();
    }

    private class MyLocationListenner implements BDLocationListener {

        @Override
        public void onReceiveLocation(BDLocation location) {


            if (location == null)
                return;
            Latitude = location.getLatitude();
            Longitude = location.getLongitude();
            City = location.getCity();
            Address = location.getAddrStr();
            Log.i("CJ", "Latitude, Longitude, City, Address" + Latitude + " " + Longitude + " " + City + " " + Address);
        }
    }
}
