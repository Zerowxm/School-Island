package wxm.com.androiddesign.ui.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import wxm.com.androiddesign.R;
import wxm.com.androiddesign.ui.MainActivity;
import wxm.com.androiddesign.ui.SignUpActivity;

/**
 * Created by zero on 2015/6/29.
 */
public class LoginFragment extends DialogFragment{

    //@Bind(R.id.fab_login)
    //FloatingActionButton fab_longin;

    LoginCallBack loginCallBack;
    @Bind(R.id.username_edit_text)EditText user_name;
    @Bind(R.id.password_edit_text)EditText password;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        View view = inflater.inflate(R.layout.fragment_login, container);
        ButterKnife.bind(this,view);
        return view;
    }

    @OnClick(R.id.login_btn)
    public void Login(){
//        try {
//            //constants
//            URL url = new URL("http://myhost.com/ajax");
//            String message = new JSONObject().toString();
//
//            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
//            conn.setReadTimeout( 10000 /*milliseconds*/ );
//            conn.setConnectTimeout( 15000 /* milliseconds */ );
//            conn.setRequestMethod("POST");
//            conn.setDoInput(true);
//            conn.setDoOutput(true);
//            conn.setFixedLengthStreamingMode(message.getBytes().length);
//
//            //make some HTTP header nicety
//            conn.setRequestProperty("Content-Type", "application/json;charset=utf-8");
//            conn.setRequestProperty("X-Requested-With", "XMLHttpRequest");
//
//            //open
//            conn.connect();
//
//            //setup send
//            OutputStream os = new BufferedOutputStream(conn.getOutputStream());
//            os.write(message.getBytes());
//            //clean up
//            os.flush();
//
//            //do somehting with response
//            is = conn.getInputStream();
//            String contentAsString = readIt(is,len);
//        } catch (Exception e){
//
//        }finally {
//            //clean up
//            os.close();
//            is.close();
//            conn.disconnect();
//        }
        loginCallBack.onLongin(user_name.getText().toString(),"zerowxm@gmail.com");
        dismiss();
    }

    public String getJSON(String url, int timeout) {
        HttpURLConnection c = null;
        try {
            URL u = new URL(url);
            c = (HttpURLConnection) u.openConnection();
            //c.setDoInput(true);

            c.setRequestMethod("GET");
            c.setRequestProperty("Content-length", "0");
            c.setUseCaches(false);
            c.setAllowUserInteraction(false);
            c.setConnectTimeout(timeout);
            c.setReadTimeout(timeout);
            c.connect();
            int status = c.getResponseCode();

            switch (status) {
                case 200:
                case 201:
                    BufferedReader br = new BufferedReader(new InputStreamReader(c.getInputStream()));
                    StringBuilder sb = new StringBuilder();
                    String line;
                    while ((line = br.readLine()) != null) {
                        sb.append(line+"\n");
                    }
                    br.close();
                    return sb.toString();
            }

        } catch (MalformedURLException ex) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
        }finally
         {
            if (c != null) {
                try {
                    c.disconnect();
                } catch (Exception ex) {
                    Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        return null;
    }

    @OnClick(R.id.signup_btn)
    public void SignUp(){
        dismiss();
        Intent intent=new Intent(getActivity(), SignUpActivity.class);
        getActivity().startActivity(intent);
    }


    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if(activity instanceof MainActivity){
            loginCallBack=(LoginCallBack)activity;
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    public interface LoginCallBack{
        public void onLongin(String name,String email);
    }
}
