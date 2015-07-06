package wxm.com.androiddesign.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.*;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.geocode.*;
import wxm.com.androiddesign.R;

/**
 * Created by Administrator on 2015/7/3.
 */
public class LocationActivity extends Activity{
    public static String Latitude = "Latitude";
    public static String Longtitude = "Longtitude";
    public static String Address= "Address";
    private String maddress;
    /**
     * Called when the activity is first created.
     */
    // 定位相关
    LocationClient mLocClient;
    MyLocationListenner myListener = new MyLocationListenner();

    BitmapDescriptor bdA = BitmapDescriptorFactory
            .fromResource(R.drawable.icon_marka);

    MapView mMapView;
    BaiduMap mBaiduMap;
    Marker mMarkerA;
    boolean isFirstLoc = true;// 是否首次定位
    TextView hintText;
    GeoCoder mSearch = null; // 反地址搜索模块

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);
        hintText = (TextView)findViewById(R.id.hintText);

        //获取地图控件引用
        mMapView = (MapView) findViewById(R.id.bmapView);
        mBaiduMap = mMapView.getMap();

        //GPS定位初始化
        InitGPS();

        // 初始化搜索模块，注册事件监听
        mSearch = GeoCoder.newInstance();
        mSearch.setOnGetGeoCodeResultListener(new OnGetGeoCoderResultListener() {
            @Override
            public void onGetGeoCodeResult(GeoCodeResult geoCodeResult) {

            }

            @Override
            public void onGetReverseGeoCodeResult(ReverseGeoCodeResult result) {

                if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
                    Toast.makeText(LocationActivity.this, "抱歉，未能找到对应地址", Toast.LENGTH_LONG)
                            .show();
                    return;
                }
                maddress = result.getAddress();
                hintText.setText(hintText.getText() + " " + result.getAddress());
            }
        });


    }

    private void InitGPS()
    {
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

    private void setLocationText(LatLng loc)
    {
        hintText.setText("Current Location: [" + loc.latitude + ", "
                + loc.longitude + "]");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //在activity执行onDestroy时执行mMapView.onDestroy()，实现地图生命周期管理
        mMapView.onDestroy();
        bdA.recycle();
    }
    @Override
    protected void onResume() {
        super.onResume();
        //在activity执行onResume时执行mMapView. onResume ()，实现地图生命周期管理
        mMapView.onResume();
    }
    @Override
    protected void onPause() {
        super.onPause();
        //在activity执行onPause时执行mMapView. onPause ()，实现地图生命周期管理
        mMapView.onPause();
    }

    private void initOverlay(LatLng original) {
        // add marker overlay

        OverlayOptions ooA = new MarkerOptions().position(original).icon(bdA)
                .zIndex(9).draggable(true);
        mMarkerA = (Marker) (mBaiduMap.addOverlay(ooA));


        mBaiduMap.setOnMarkerDragListener(new BaiduMap.OnMarkerDragListener() {
            public void onMarkerDrag(Marker marker) {

                LatLng loc = new LatLng(marker.getPosition().latitude,marker.getPosition().longitude);
                setLocationText(loc);
            }

            public void onMarkerDragEnd(Marker marker) {
                LatLng loc = new LatLng(marker.getPosition().latitude,marker.getPosition().longitude);

                setLocationText(loc);
                mSearch.reverseGeoCode(new ReverseGeoCodeOption().location(loc));
                setLocationResult(loc);

                Toast.makeText(
                        LocationActivity.this,
                        "You Choose Location: [" + loc.latitude + ", "
                                + loc.longitude + "]",
                        Toast.LENGTH_LONG).show();
            }

            public void onMarkerDragStart(Marker marker) {
            }
        });
    }


    private void setLocationResult(LatLng local)
    {
        Intent i = new Intent();
        //the type of latitude and longitude is double
        i.putExtra(LocationActivity.Latitude, local.latitude);
        i.putExtra(LocationActivity.Longtitude, local.longitude);
        i.putExtra(LocationActivity.Address, maddress);
        setResult(RESULT_OK, i);
    }

    public class MyLocationListenner implements BDLocationListener {

        @Override
        public void onReceiveLocation(BDLocation location) {

            // map view 销毁后不在处理新接收的位置
            if (location == null || mMapView == null)
                return;
            MyLocationData locData = new MyLocationData.Builder()
                    .accuracy(location.getRadius())
                            // 此处设置开发者获取到的方向信息，顺时针0-360
                    .direction(100).latitude(location.getLatitude())
                    .longitude(location.getLongitude()).build();
            mBaiduMap.setMyLocationData(locData);

            if (isFirstLoc) {
                isFirstLoc = false;
                LatLng loc = new LatLng(location.getLatitude(),
                        location.getLongitude());
                MapStatusUpdate u = MapStatusUpdateFactory.newLatLng(loc);
                mBaiduMap.animateMapStatus(u);
                setLocationResult(loc);
                initOverlay(loc);
                setLocationText(loc);
                mSearch.reverseGeoCode(new ReverseGeoCodeOption().location(loc));
            }
        }
    }
}
