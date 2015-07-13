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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.Gson;

import org.apache.http.HttpStatus;
import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import wxm.com.androiddesign.R;
import wxm.com.androiddesign.module.User;
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
        myClickHandler();
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
            User user=new User();
            user.setAction("login");
            user.setUserName(params[0]);
            user.setUserPassword(params[1]);
            Log.d("gson",new Gson().toJson(user));
            getJSON(new Gson().toJson(user));
            return true;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            super.onPostExecute(result);
            if(result==true){
                //dismiss();
                //username_layout.setError("用户名错误");
            }
            else {
                username_layout.setError("用户名错误");
            }
        }
    }

    public void getJSON(String json) {

            BufferedWriter bufferedWriter;
            BufferedReader bufferedReader;
            StringBuffer result = new StringBuffer();
            try {
                URL murl = new URL("http://101.200.191.149:8080/FirstWeb/ClientPostServlet");
                //URL murl = new URL("http://101.200.191.149:8080/FirstWeb/HttpTestServlet");
                HttpURLConnection connection = (HttpURLConnection) murl.openConnection();
                connection.setRequestProperty("Content-type", "application/json");
                connection.setDoInput(true);
                connection.setDoOutput(true);
                connection.setRequestMethod("POST");
                connection.setUseCaches(false);
                connection.setReadTimeout(5000);
                connection.setConnectTimeout(5000);

                Log.d("connection", "no");
                connection.connect();
                Log.d("connection", "yes1");
                bufferedWriter = new BufferedWriter(new OutputStreamWriter(connection.getOutputStream()));
                bufferedWriter.write(json);
                //bufferedWriter
                bufferedWriter.flush();
                bufferedWriter.close();
                Log.d("connection", "yes2");
                InputStream ins ;
                Log.d("connection", "yes2.5");
                int status = connection.getResponseCode();

                if(status >= HttpStatus.SC_BAD_REQUEST) {
                    ins = connection.getErrorStream();
                    Log.d("connection", "yes2.6");
                }
                else {
                    ins = connection.getInputStream();
                    Log.d("connection", "yes2.7");
                }
               bufferedReader = new BufferedReader(new InputStreamReader(ins));
                Log.d("connection", "yes3");
                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    result.append(line);
                }
                Log.d("connection", result.toString());
                ObjectInputStream objinput = new ObjectInputStream(connection.getInputStream());
                Log.d("connection", "yes3");
                String result2 = (String)objinput.readObject();
                Log.d("connection", "yes4");
                Log.d("connection", result.toString());


            } catch (IOException e) {
                Log.d("Exception", e.toString());
                Log.d("connection", "Excption");
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } finally {
                Log.d("connection", "con");
            }
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
