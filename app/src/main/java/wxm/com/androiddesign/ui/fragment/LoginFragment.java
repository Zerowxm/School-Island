package wxm.com.androiddesign.ui.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Point;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;

import com.afollestad.materialdialogs.MaterialDialog;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformDb;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.sina.weibo.SinaWeibo;
import cn.sharesdk.tencent.qq.QQ;
import wxm.com.androiddesign.R;
import wxm.com.androiddesign.module.MyUser;
import wxm.com.androiddesign.module.User;
import wxm.com.androiddesign.network.JsonConnection;
import wxm.com.androiddesign.ui.MainActivity;
import wxm.com.androiddesign.utils.MyUtils;

/**
 * Created by zero on 2015/6/29.
 */
public class LoginFragment extends DialogFragment {

    private static final String TAG="LoginFragment";
    public static final int MAIN=0x1;
    public static final int SIGN=0x2;
    LoginCallBack loginCallBack;
    @Bind(R.id.email_edit_text)
    EditText user_email;
    @Bind(R.id.password_edit_text)
    EditText password;

    AppCompatActivity activity;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    String mResult = "";
    int mType;
    String mLoginType;
    User user = new User();

    public static LoginFragment newInstance(int type){
        LoginFragment loginFragment=new LoginFragment();
        Bundle args=new Bundle();
        args.putInt("Type", type);
        loginFragment.setArguments(args);
        return loginFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mType=getArguments().getInt("Type");
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        if(outState==null)
            super.onSaveInstanceState(outState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        View view = inflater.inflate(R.layout.fragment_login, container);
        ButterKnife.bind(this, view);
        Point size= MyUtils.getScreenSize(getActivity());
        getDialog().getWindow().setLayout(size.x-30, WindowManager.LayoutParams.WRAP_CONTENT);

        //getDialog().getWindow().get
        return view;
    }

    @OnClick(R.id.login_btn)
    public void Login() {
        myClickHandler();
    }

    @OnClick(R.id.sina_login_btn)
    public void onSinaLogin(){
        Log.v(TAG, "Sina");
        Platform sina= ShareSDK.getPlatform(SinaWeibo.NAME);
        if(sina.isValid()){
            Log.v(TAG, "已授权");
            MyUser.setLoginType(MyUser.SINA);
            getInfo(sina);
        }
    }

    @OnClick(R.id.qq_login_btn)
    public void onQQLogin(){
        Log.v(TAG, "QQ");
        Platform qq= ShareSDK.getPlatform(QQ.NAME);
        if(qq.isValid()){
            Log.v(TAG, "已授权");
            MyUser.setLoginType(MyUser.QQ);
            getInfo(qq);
        }
    }

    public void getInfo(Platform platform){
        PlatformDb db=platform.getDb();
        String mId=db.getUserId();
        MyUser.setUserId(mId);
        JSONObject jsonObject=new JSONObject();

        try {
            jsonObject.put("action","login"+MyUser.loginType);
            jsonObject.put("userId", mId);
            Log.d(TAG, jsonObject.toString());
            SharedPreferences prefs = activity.getSharedPreferences("wxm.com.androiddesign", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = prefs.edit();
            editor.putString("UserId", mId);
            editor.putString("LoginType", MyUser.loginType);
            editor.apply();
            new LoginTask(getActivity()).execute(jsonObject.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void myClickHandler() {
        ConnectivityManager check = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = check.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            //do some thing

            user.setAction("loginemail");
            user.setUserEmail(user_email.getText().toString());
            user.setUserPassword(password.getText().toString());
            MyUser.setLoginType(MyUser.EMAIL);
            new LoginTask(getActivity()).execute(new Gson().toJson(user));

        } else {
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
            mResult = JsonConnection.getJSON(params[0]);
            Log.d(TAG,mResult);
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
                User mUser = new Gson().fromJson(mResult, User.class);
                if(MyUser.loginType==MyUser.EMAIL){
                    MyUser.setUserEmail(mUser.getUserEmail());
                    MyUser.setUserPassword(mUser.getUserPassword());
                    SharedPreferences prefs = activity.getSharedPreferences("wxm.com.androiddesign", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = prefs.edit();
                    editor.putString("UserEmail",mUser.getUserEmail());
                    editor.putString("UserPassword", mUser.getUserPassword());
                    editor.apply();
                }

                //mUser.setUserPassword(user.getUserPassword());
                if (mType==SIGN){
                    Intent intent = new Intent(activity,MainActivity.class);
                    activity.startActivity(intent);
                    activity.finish();
                }
                else if(mType==MAIN){
                    loginCallBack.onLongin(mUser);
                }
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

    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.activity=(AppCompatActivity)activity;
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
