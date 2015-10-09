package wxm.com.androiddesign.ui;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.sina.weibo.SinaWeibo;
import cn.sharesdk.tencent.qq.QQ;
import wxm.com.androiddesign.R;
import wxm.com.androiddesign.module.MyUser;
import wxm.com.androiddesign.module.User;
import wxm.com.androiddesign.network.JsonConnection;
import wxm.com.androiddesign.ui.fragment.LoginFragment;
import wxm.com.androiddesign.ui.fragment.SignUpFragment;
import wxm.com.androiddesign.utils.MyUtils;
import wxm.com.androiddesign.utils.PrefUtils;

public class SignUpActivity extends AppCompatActivity implements View.OnClickListener
        ,Handler.Callback,PlatformActionListener{
    private static final String TAG="LoginActivity";

    private static final int MSG_SMSSDK_CALLBACK=0x1;
    private static final int MSG_AUTH_CANCEL=0x2;
    private static final int MSG_AUTH_ERROR=0x3;
    private static final int MSG_AUTH_COMPLETE=0x4;

    private Handler handler;
    private String mType;
    MaterialDialog materialDialog;
    User user;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_layout);
        ButterKnife.bind(this);
        ((Button)findViewById(R.id.sina_login_btn)).setOnClickListener(this);
        ((Button)findViewById(R.id.qq_login_btn)).setOnClickListener(this);
        ((Button)findViewById(R.id.signup_btn)).setOnClickListener(this);
        handler=new Handler(this);
    }



    @OnClick(R.id.login_btn)
    public void onLogin(){
        showLoginDialog();
    }

    private void authorize(Platform platform){
        if (platform==null){
            Log.w(TAG,"platform is null");
            return;
        }
        Log.i(TAG, "authorize");
        platform.setPlatformActionListener(this);
        platform.SSOSetting(false);
        platform.showUser(null);
        platform.authorize();

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.sina_login_btn:
                Platform sina= ShareSDK.getPlatform(SinaWeibo.NAME);
                mType=MyUser.SINA;
                authorize(sina);
                break;
            case R.id.qq_login_btn:
                Platform qq= ShareSDK.getPlatform(QQ.NAME);
                mType=MyUser.QQ;
                authorize(qq);
                break;
            case R.id.signup_btn:
                FragmentManager fm = getSupportFragmentManager();
                SignUpFragment signUpFragment = SignUpFragment.newInstance(MyUtils.LOGIN);
                signUpFragment.show(fm, "signup");
                mType=MyUser.EMAIL;
                break;
        }
    }

    private void showLoginDialog() {
        FragmentManager fm = getSupportFragmentManager();
        LoginFragment loginFragment = LoginFragment.newInstance(LoginFragment.SIGN);
        loginFragment.show(fm, "login");
    }

    private class BackgroundTask extends AsyncTask<Void, Void, Boolean> {

        AppCompatActivity activity;
        String mResult;

        public BackgroundTask(AppCompatActivity activity) {
            this.activity = activity;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            super.onPostExecute(result);
            if (result) {
                Intent intent = new Intent(SignUpActivity.this, MainActivity.class);
                MyUser.setLoginType(mType);
                MyUser.setUserId(user.getUserId());
                SharedPreferences prefs = getSharedPreferences("wxm.com.androiddesign", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = prefs.edit();
                editor.putString("UserId", user.getUserId());
                PrefUtils.markUse(activity);
                editor.putString("LoginType", mType);
                editor.apply();
                startActivity(intent);
                finish();
                overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
            }else {
                Toast.makeText(activity,"注册失败",Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            try {
                JSONObject jsonObject=new JSONObject(JsonConnection.getJSON(new Gson().toJson(user)));
                mResult=jsonObject.getString("result");
            } catch (JSONException e) {
                e.printStackTrace();
            }

            if (mResult!=null&&!mResult.contains("false")) {
                SharedPreferences prefs = getSharedPreferences("wxm.com.androiddesign", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = prefs.edit();
                editor.putString("easemobId",mResult);
                Log.d("Test","Sign:"+mResult);
                editor.apply();
                MyUtils.signupHX(mResult,"7777777");
                return true;
            }
            else
                return false;
        }

        @Override
        protected void onPreExecute() {
            Log.d("Task", "onPreExecute");

        }
    }

    @Override
    public void onComplete(Platform platform, int action, HashMap<String, Object> res) {
        if (action==Platform.ACTION_USER_INFOR){
            Log.i(TAG,"onComplete");
            Message msg=new Message();
            msg.what=MSG_AUTH_COMPLETE;
            msg.obj=platform;
            handler.sendMessage(msg);
        }
    }

    @Override
    public void onError(Platform platform, int action, Throwable throwable) {
        if(action==Platform.ACTION_USER_INFOR){
            Log.i(TAG, "onError:" + throwable.getMessage() + "|" + throwable.toString());
            handler.sendEmptyMessage(MSG_AUTH_ERROR);
        }
        throwable.printStackTrace();
    }

    @Override
    public void onCancel(Platform platform, int action) {
        if (action==Platform.ACTION_USER_INFOR){
            Log.i(TAG,"onCancel");
            handler.sendEmptyMessage(MSG_AUTH_CANCEL);
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public boolean handleMessage(Message msg){
        switch(msg.what) {
            case MSG_AUTH_CANCEL: {
                //取消授权
                //materialDialog.dismiss();
                Log.i(TAG, "MSG_AUTH_CANCEL");
            } break;
            case MSG_AUTH_ERROR: {
                //授权失败
//                materialDialog.dismiss();
                Log.i(TAG, "MSG_AUTH_ERROR");
            } break;
            case MSG_AUTH_COMPLETE: {
                //授权成功

                Log.i(TAG, "MSG_AUTH_COMPLETE");
                Platform platform=(Platform)msg.obj;
                String accessToken=platform.getDb().getToken();
                String openId=platform.getDb().getUserId();
                String nickName=platform.getDb().get("nickname");
                String icon=platform.getDb().getUserIcon();
                String gender=platform.getDb().getUserGender();
                Log.i(TAG, accessToken + ":" + openId + ":" + nickName + ":" + icon + ":" + gender);
                user =new User();
                user.setUserId(openId);
                user.setUserName(nickName);
                user.setUserIcon(icon);
                user.setUserGender(gender);
                user.setAction("signup"+mType);
                new BackgroundTask(this).execute();
            } break;
        }
        return false;
    }

}
