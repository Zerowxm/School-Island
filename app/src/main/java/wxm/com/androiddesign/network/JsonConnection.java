package wxm.com.androiddesign.network;

import android.app.Service;
import android.content.Intent;
import android.net.Uri;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import org.apache.http.HttpStatus;
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

/**
 * Created by zero on 2015/7/9.
 */
public class JsonConnection {
    public static String getJSON(String json) {
        String mResult="";
        try {
            URL murl = new URL("http://101.200.191.149:8080/bootstrapRepository/ClientPostServlet");
            HttpURLConnection connection = (HttpURLConnection) murl.openConnection();
            connection.setRequestProperty("Content-type", "application/json");
            connection.setDoInput(true);
            connection.setDoOutput(true);
            connection.setRequestMethod("POST");
            connection.setUseCaches(false);

            connection.connect();
            OutputStream outStrm = connection.getOutputStream();
            Log.d("connection", "json"+json);
            //HttpURLconnection写数据与发送数据
            ObjectOutputStream objOutputStrm = new ObjectOutputStream(outStrm);
            objOutputStrm.writeObject(json);
            objOutputStrm.flush();                              //数据输出
            objOutputStrm.close();
            Log.d("connection", "写入成功");
            InputStream ins ;

            int status = connection.getResponseCode();
            if(status >= HttpStatus.SC_BAD_REQUEST) {
                ins = connection.getErrorStream();
                Log.d("connection", "失败 "+status);
            }
            else {
                ins = connection.getInputStream();
                Log.d("connection", "成功 "+status);
            }
            ObjectInputStream objinput = new ObjectInputStream(ins);

            mResult = (String)objinput.readObject();

            Log.d("connection","读入成功"+ mResult);

            return mResult;

        } catch (IOException e) {
            Log.d("Exception", e.toString());
            Log.d("connection", "Excption");
        }   catch (ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            Log.d("connection", "con");
        }
        Log.d("connection", mResult);
        return mResult;
    }
    public static void getJsonObject(String json) {
        StringBuffer stringBuffer=null;
        new Thread(new Runnable() {
            @Override
            public void run() {
                BufferedReader bufferedReader;
                StringBuffer stringBuffer;
                String line;
                try {
                    URL murl = new URL("http://101.200.191.149:8080/FirstWeb/ClientPostServlet");
                    HttpURLConnection connection = (HttpURLConnection) murl.openConnection();
                    connection.setDoInput(true);
                    connection.setDoOutput(true);
                    connection.setRequestMethod("GET");
                    connection.setUseCaches(false);
                    connection.connect();
                    connection.setConnectTimeout(5000);
                    int responseCode = connection.getResponseCode();
                    if (responseCode == 200) {
                        bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                        stringBuffer = new StringBuffer();
                        while ((line = bufferedReader.readLine()) != null) {
                            stringBuffer.append(line + "\n");
                        }
                        bufferedReader.close();
                        Log.d("json","http"+stringBuffer.toString());

                    } else {
                        Log.i("json", "访问失败" + responseCode);
                    }


                } catch (IOException e) {
                    Log.d("Exception",e.toString());
                }

            }

        }).start();

    }

    public static void submitJson(final String json){

        new Thread(new Runnable() {
            @Override
            public void run() {
                BufferedWriter bufferedWriter;
                BufferedReader bufferedReader;
                StringBuffer result=new StringBuffer();
                try {
                    URL murl = new URL("http://101.200.191.149:8080/FirstWeb/ClientPostServlet");
                    HttpURLConnection connection = (HttpURLConnection) murl.openConnection();
                    connection.setDoInput(true);
                    connection.setDoOutput(true);
                    connection.setRequestMethod("POST");
                    connection.setUseCaches(false);
                    connection.connect();
                    bufferedWriter = new BufferedWriter(new OutputStreamWriter(connection.getOutputStream()));
                    bufferedWriter.write(json);
                    //bufferedWriter
                    bufferedWriter.flush();
                    bufferedWriter.close();

                    bufferedReader=new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    String line;
                    while ((line=bufferedReader.readLine())!=null){
                        result.append(line);
                    }


                } catch (IOException e) {
                    Log.d("Exception",e.toString());
                }

            }

        }).start();
        //return result.toString();
    }

}
