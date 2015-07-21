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

import com.afollestad.materialdialogs.MaterialDialog;
import com.google.gson.Gson;

import org.apache.http.HttpStatus;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedOutputStream;
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
import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import wxm.com.androiddesign.MessageService;
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
    @Bind(R.id.username_text_input_layout)
    TextInputLayout username_layout;
    @Bind(R.id.password_text_input_layout)
    TextInputLayout password_layout;
    @Bind(R.id.username_edit_text)
    EditText user_name;
    @Bind(R.id.password_edit_text)
    EditText password;
    String mResult = "";

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

        //
        //dismiss();
        myClickHandler();
    }

    public void myClickHandler() {
        ConnectivityManager check = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = check.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            //do some thing
            User user = new User();
            user.setAction("login");
            user.setUserId(user_name.getText().toString());
            user.setUserPassword(password.getText().toString());

            new LoginTask(getActivity()).execute(new Gson().toJson(user));
            Intent i = new Intent();
            i.setClass(getActivity(), MessageService.class);
            i.putExtra("userId",user.getUserId());
            getActivity().startService(i);
        } else {
            username_layout.setError("网络连接错误");
        }
    }

    private class LoginTask extends AsyncTask<String, Void, Boolean> {
        MaterialDialog materialDialog;
        Context context;

        public LoginTask(Context context) {
            this.context = context;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dismiss();
            materialDialog = new MaterialDialog.Builder(context)
                    .title(R.string.login_title)
                    .content(R.string.please_wait)
                    .progress(true, 0)
                    .progressIndeterminateStyle(false)
                    .show();
        }

        @Override
        protected Boolean doInBackground(String... params) {
            getJSON(params[0]);
            if (mResult != "") {
                if (mResult.contains("false")) {
                    return false;
                } else if (mResult.contains("true"))
                    return true;
            }
            return false;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            super.onPostExecute(result);
            if (result == true) {
                materialDialog.dismiss();
                loginCallBack.onLongin(new Gson().fromJson(mResult, User.class));
                //username_layout.setError("用户名错误");
            } else {
                materialDialog.dismiss();
                new MaterialDialog.Builder(context)
                        .title("登陆失败")
                        .content("请重新登陆")
                        .positiveText("确定")
                        .show();
            }
        }
    }

    public void getJSON(String json) {
        try {
            URL murl = new URL("http://101.200.191.149:8080/bootstrapRepository/ClientPostServlet");

            HttpURLConnection connection = (HttpURLConnection) murl.openConnection();
            connection.setRequestProperty("Content-type", "application/json");
            connection.setDoInput(true);
            connection.setDoOutput(true);
            connection.setRequestMethod("POST");
            connection.setUseCaches(false);
            connection.setReadTimeout(5000);
            connection.setConnectTimeout(5000);
            connection.connect();
            OutputStream outStrm = connection.getOutputStream();


            //HttpURLconnection写数据与发送数据
            ObjectOutputStream objOutputStrm = new ObjectOutputStream(outStrm);
            objOutputStrm.writeObject(json);
            objOutputStrm.flush();                              //数据输出
            objOutputStrm.close();

            Log.d("connection", json);

            InputStream ins;

            int status = connection.getResponseCode();
            if (status >= HttpStatus.SC_BAD_REQUEST) {
                ins = connection.getErrorStream();
                Log.d("connection", "" + status);
            } else {
                ins = connection.getInputStream();
                Log.d("connection", "" + status);

                ObjectInputStream objinput = new ObjectInputStream(ins);
                mResult = (String) objinput.readObject();

                Log.d("connection", mResult);
            }


        } catch (IOException e) {
            Log.d("Exception", e.toString());
            Log.d("connection", "Excption");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    @OnClick(R.id.signup_btn)
    public void SignUp() {
        dismiss();
        Intent intent = new Intent(getActivity(), SignUpActivity.class);
        getActivity().startActivityForResult(intent,MainActivity.SIGNUP);
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
        public void onLongin(User user);
    }
}
