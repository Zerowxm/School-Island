package wxm.com.androiddesign.services;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by Curl on 2015/7/14.
 */
public class FetchItems {
    private String result = null;
    private String defaultUrl = "http://10.0.2.2:8080/FirstWeb/ClientPostServlet";

    public String fetchitems(String surl, String json) {
        try {
            if (surl == null)
                surl = defaultUrl;

            URL url = new URL(surl);
            URLConnection urlConnection = url.openConnection();
            HttpURLConnection httpURLConnection = (HttpURLConnection) urlConnection;
            //  Log.d("connection", "c1");

            //设置HttpURLConnextion参数设置
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setDoInput(true);
            httpURLConnection.setUseCaches(false);
            httpURLConnection.setRequestProperty("Content-type", "application/json");

            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setReadTimeout(5000);             //设置超时的时间
            httpURLConnection.setConnectTimeout(5000);          //设置连接超时的时间


            // Log.d("connection", "c2");

            //HttpURLConnection连接
            httpURLConnection.connect();

            // Log.d("connection", "c3");

            OutputStream outStrm = httpURLConnection.getOutputStream();

            //HttpURLconnection写数据与发送数据

            ObjectOutputStream objOutputStrm = new ObjectOutputStream(outStrm);
            objOutputStrm.writeObject(json);

            objOutputStrm.flush();                              //数据输出
            objOutputStrm.close();

            // Log.d("connection", "c4");

            //getInputstream会发出请求代码，至此一次http请求结束，后面写的对象输出流没有意义
            InputStream inStrm = httpURLConnection.getInputStream();

            ObjectInputStream objInputStrm = new ObjectInputStream(inStrm);

            // Log.d("connection", "c5");

            int status = httpURLConnection.getResponseCode();

            // =200表示成功响应
            if (status == 200) {
                //JSONObject inJson = (JSONObject)objInputStrm.readObject();
                //  Log.e("connection", "Connection Success");

                Object obj = objInputStrm.readObject();
                result = obj.toString();
            } else {
                //   Log.d("connection", "返回数据失败");
            }

            objInputStrm.close();
            //断开连接
            httpURLConnection.disconnect();

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return result;
    }
}
