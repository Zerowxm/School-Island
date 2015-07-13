package wxm.com.androiddesign.ui.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputLayout;
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
public class LoginFragment extends DialogFragment {

    //@Bind(R.id.fab_login)
    //FloatingActionButton fab_longin;

    LoginCallBack loginCallBack;
    @Bind(R.id.username_text_input_layout)TextInputLayout username_layout;
    @Bind(R.id.password_text_input_layout)TextInputLayout password_layout;
    @Bind(R.id.username_edit_text)
    EditText user_name;
    @Bind(R.id.password_edit_text)
    EditText password;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        View view = inflater.inflate(R.layout.fragment_login, container);
        ButterKnife.bind(this, view);
        return view;
    }

    @OnClick(R.id.login_btn)
    public void Login() {

        loginCallBack.onLongin(user_name.getText().toString(), "zerowxm@gmail.com");
        //dismiss();
        //myClickHandler();
    }

    public void myClickHandler() {
        ConnectivityManager check = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = check.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            //do some thing
            new LoginTask().execute(user_name.getText().toString(), password.getText().toString());
        } else {
            username_layout.setError("网络连接错误");
        }
    }

    private class LoginTask extends AsyncTask<String, Void, Boolean> {

        @Override
        protected Boolean doInBackground(String... params) {
            if (params[0].equals("123")) {
                return true;
            }
            else{

                return false;
            }
        }

        @Override
        protected void onPostExecute(Boolean result) {
            super.onPostExecute(result);
            if(result==true){
                dismiss();
                //username_layout.setError("用户名错误");
            }
            else {
                username_layout.setError("用户名错误");
            }
        }
    }

    public String getJSON(String url, int timeout) {
        HttpURLConnection c = null;
        try {
            URL u = new URL("http://101.200.191.149:8080/FirstWeb/ClientPostServlet");
            c = (HttpURLConnection) u.openConnection();


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
                        sb.append(line + "\n");
                    }
                    br.close();
                    return sb.toString();
            }

        } catch (MalformedURLException ex) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
        } finally {
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
    public void SignUp() {
        dismiss();
        Intent intent = new Intent(getActivity(), SignUpActivity.class);
        getActivity().startActivity(intent);
    }


    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (activity instanceof MainActivity) {
            loginCallBack = (LoginCallBack) activity;
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    public interface LoginCallBack {
        public void onLongin(String name, String email);
    }
}
