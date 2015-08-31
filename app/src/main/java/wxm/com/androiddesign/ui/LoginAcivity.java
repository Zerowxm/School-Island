package wxm.com.androiddesign.ui;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.HashMap;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.sina.weibo.SinaWeibo;
import cn.sharesdk.tencent.qq.QQ;
import cn.sharesdk.tencent.weibo.TencentWeibo;
import wxm.com.androiddesign.R;
import wxm.com.androiddesign.listener.OnLoginListener;
import wxm.com.androiddesign.module.MyUser;
import wxm.com.androiddesign.module.User;
import wxm.com.androiddesign.network.JsonConnection;
import wxm.com.androiddesign.ui.fragment.LoginFragment;
import wxm.com.androiddesign.ui.fragment.SignUpFragment;
import wxm.com.androiddesign.utils.MyBitmapFactory;

public class LoginAcivity extends AppCompatActivity implements View.OnClickListener
        ,Handler.Callback,PlatformActionListener{
    private static final String TAG="LoginActivity";

    private static final int MSG_SMSSDK_CALLBACK=0x1;
    private static final int MSG_AUTH_CANCEL=0x2;
    private static final int MSG_AUTH_ERROR=0x3;
    private static final int MSG_AUTH_COMPLETE=0x4;

    private String smssdkAppkey;
    private String smssdkAppSecret;
    private Handler handler;
    private String mType;
    MaterialDialog materialDialog;
    private OnLoginListener signupListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_layout);
        ButterKnife.bind(this);
        ((Button)findViewById(R.id.sina_login_btn)).setOnClickListener(this);
        ((Button)findViewById(R.id.qq_login_btn)).setOnClickListener(this);
        ((Button)findViewById(R.id.signup_btn)).setOnClickListener(this);
        initSDK(this);
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
        Log.i(TAG,"authorize");
        materialDialog = new MaterialDialog.Builder(this)
                .title(R.string.signup_title)
                .content(R.string.please_wait)
                .progress(true, 0)
                .progressIndeterminateStyle(false)
                .show();
        platform.setPlatformActionListener(this);
        platform.authorize();
        platform.SSOSetting(true);
        platform.showUser(null);
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
                SignUpFragment loginFragment = new SignUpFragment();
                loginFragment.show(fm, "signup");
                mType=MyUser.EMAIL;
                break;
        }
    }

    private void showLoginDialog() {
        FragmentManager fm = getSupportFragmentManager();
        LoginFragment loginFragment = LoginFragment.newInstance(LoginFragment.SIGN);
        loginFragment.show(fm, "login");
    }

    private class BackgroundTask extends AsyncTask<User, Void, String> {

        AppCompatActivity activity;
        String mResult;

        public BackgroundTask(AppCompatActivity activity) {
            this.activity = activity;
        }

        @Override
        protected void onPostExecute(String Id) {
            super.onPostExecute(Id);
            materialDialog.dismiss();
            Intent intent = new Intent(LoginAcivity.this,MainActivity.class);
            intent.putExtra("loginType","sina");
            MyUser.setLoginType(mType);
            MyUser.setUserId(Id);
            SharedPreferences prefs = getSharedPreferences("wxm.com.androiddesign", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = prefs.edit();
            editor.putString("UserId", Id);
            editor.putString("LoginType",mType);
            editor.apply();
            startActivity(intent);
            finish();
        }

        @Override
        protected String doInBackground(User... params) {
            mResult=JsonConnection.getJSON(new Gson().toJson(params[0]));
            Log.i(TAG,"id"+params[0].getUserId());
            return params[0].getUserId();
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
//            msg.obj=new Object[]{platform.getName(),res};
            msg.obj=platform;
            handler.sendMessage(msg);
        }
    }

    @Override
    public void onError(Platform platform, int action, Throwable throwable) {
        if(action==Platform.ACTION_USER_INFOR){
            Log.i(TAG, "onError:" + throwable.getMessage() + "|" + throwable.toString());
            handler.sendEmptyMessage(MSG_AUTH_ERROR);
            //materialDialog.dismiss();
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
                materialDialog.dismiss();
                Log.i(TAG, "MSG_AUTH_CANCEL");
            } break;
            case MSG_AUTH_ERROR: {
                //授权失败
                materialDialog.dismiss();
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
                User user =new User();
                user.setUserId(openId);
                user.setUserName(nickName);
                user.setUserIcon(icon);
                user.setUserGender(gender);
                user.setAction("signup"+mType);
                new BackgroundTask(this).execute(user);
            } break;
        }
        return false;
    }



    private void initSDK(Context context) {
        //初始化sharesdk,具体集成步骤请看文档：
        //http://wiki.mob.com/Android_%E5%BF%AB%E9%80%9F%E9%9B%86%E6%88%90%E6%8C%87%E5%8D%97
        ShareSDK.initSDK(context);
    }
}
