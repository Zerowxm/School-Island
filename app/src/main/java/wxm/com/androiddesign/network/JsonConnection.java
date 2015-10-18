package wxm.com.androiddesign.network;

import android.app.Activity;
import android.app.Application;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.google.gson.Gson;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import wxm.com.androiddesign.module.User;
import wxm.com.androiddesign.ui.MainActivity;

/**
 * Created by zero on 2015/7/9.
 */
public class JsonConnection {
    //private static Context context = ;


    public static String getJSON(String json) {
        String mResult = "";

//        ConnectivityManager check = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
//        NetworkInfo networkInfo = check.getActiveNetworkInfo();
        if (true) {
            //do some thing
            try {

                //URL murl = new URL("http://192.241.229.214:8080/bootStrap/ClientPostServlet");
                //URL murl = new URL("http://192.168.199.217:8081/bootStrap/ClientPostServlet");
                //URL murl = new URL("http://124.172.185.128:8090/bootStrap/ClientPostServlet");
                //URL murl = new URL("http://192.168.1.108:8080/bootStrap/ClientPostServlet");
                //URL murl = new URL("http://192.168.199.217:8080/bootStrap/ClientPostServlet");
                //URL murl = new URL("http://192.168.199.142:8080/bootStrap/ClientPostServlet");


                URL murl = new URL("http://192.168.199.217:8080/bootStrap/ClientPostServlet");

                HttpURLConnection connection = (HttpURLConnection) murl.openConnection();
                connection.setRequestProperty("Content-type", "application/json");
                if (Build.VERSION.SDK_INT > 13) {
                    connection.setRequestProperty("Connection", "close");
                }
                connection.setDoInput(true);
                connection.setDoOutput(true);
                connection.setRequestMethod("POST");
                connection.setUseCaches(false);

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
                Log.d("connection", "写入成功"+status);
                ins = connection.getInputStream();

                Log.d("connection", "ObjectInputStream");
                ObjectInputStream objinput = new ObjectInputStream(ins);

                Log.d("connection", "input");
                mResult = (String) objinput.readObject();

                Log.d("connection", "读入成功" + mResult);

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
        return mResult;
    }
}
