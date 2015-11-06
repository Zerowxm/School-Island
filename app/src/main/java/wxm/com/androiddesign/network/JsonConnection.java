package wxm.com.androiddesign.network;

import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import wxm.com.androiddesign.ui.MyApplication;
import wxm.com.androiddesign.utils.ACache;
import wxm.com.androiddesign.utils.NetState;

/**
 * Created by zero on 2015/7/9.
 */
public class JsonConnection {
    //private static Context context = ;


    public static String getJSON(String json) {
        String mResult = "";
        boolean shouldDownLoad = true;

        if (NetState.getNetworkType() == 0) {
            Log.d("netState", NetState.getNetworkType() + "");
            ACache aCache = ACache.get(MyApplication.applicationContext);
            String value = aCache.getAsString(json);
            if(value == null) {
                shouldDownLoad = true;
            }else{
                shouldDownLoad = false;
                return value;
            }
//            Toast.makeText(MyApplication.applicationContext, "请检查网络状态", Toast.LENGTH_SHORT).show();
            //Log.d("jsonarray",value);
        }else if (!NetState.isNetworkConnected()) {
            Toast.makeText(MyApplication.applicationContext, "当前网络不可用", Toast.LENGTH_SHORT).show();
            shouldDownLoad = false;
        }
        if(shouldDownLoad){
            if (true) {
                //do some thing
                try {
                    //URL murl = new URL("http://192.168.1.108:8080/bootStrap/ClientPostServlet");
                    //URL murl = new URL("http://192.168.1.103:8080/bootStrap/ClientPostServlet");
                    //URL murl = new URL("http://192.168.199.217:8080/bootStrap/ClientPostServlet");

                    // URL murl = new URL("http://192.168.199.142:8080/bootStrap/ClientPostServlet");


                    //URL murl = new URL("http://192.168.199.217:8080/bootStrap/ClientPostServlet");
                    URL murl = new URL("http://106.0.4.149:8081/bootStrap/ClientPostServlet");

                    HttpURLConnection connection = (HttpURLConnection) murl.openConnection();
                    connection.setRequestProperty("Content-type", "application/json");
                    if (Build.VERSION.SDK_INT > 13) {
                        connection.setRequestProperty("Connection", "close");
                    }
                    connection.setDoInput(true);
                    connection.setDoOutput(true);
                    connection.setRequestMethod("POST");
                    connection.setUseCaches(false);
                    connection.setReadTimeout(100000);
                    connection.connect();
                    OutputStream outStrm = connection.getOutputStream();
                    Log.d("connection", "json" + json);
                    //HttpURLconnection写数据与发送数据
                    ObjectOutputStream objOutputStrm = new ObjectOutputStream(outStrm);
                    objOutputStrm.writeObject(json);
                    objOutputStrm.flush();                              //数据输出
                    objOutputStrm.close();
                    Log.d("connection", "写入成功");
                    InputStream ins;

                    int status = connection.getResponseCode();
                    Log.d("connection", "写入成功" + status);
                    ins = connection.getInputStream();

                    Log.d("connection", "ObjectInputStream");
                    ObjectInputStream objinput = new ObjectInputStream(ins);

                    Log.d("connection", "input");
                    mResult = (String) objinput.readObject();

                    Log.d("connection", "读入成功" + mResult);

                    ACache aCache = ACache.get(MyApplication.applicationContext);
                    aCache.put(json,mResult,2 * ACache.TIME_DAY);
                    Log.d("jsonre",json);
                    return mResult;

                } catch (IOException e) {
                    Log.d("Exception", e.toString());
                    Log.d("connection", "Excption");
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                } finally {
                    Log.d("connection", "con");
                }
                Log.d("connection", mResult);
            } else {

            }
        }

        return mResult;
    }
}
