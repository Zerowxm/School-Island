package wxm.com.androiddesign.ui;


import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;


import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.support.v4.util.Pair;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.easemob.EMCallBack;
import com.easemob.EMEventListener;
import com.easemob.EMNotifierEvent;
import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMMessage;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.util.Timer;
import java.util.logging.LogRecord;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import wxm.com.androiddesign.module.MyUser;
import wxm.com.androiddesign.module.User;
import wxm.com.androiddesign.network.JsonConnection;
import wxm.com.androiddesign.services.LocationServices;
import wxm.com.androiddesign.ui.fragment.FragmentParent;
import wxm.com.androiddesign.ui.fragment.HomeFragment;
import wxm.com.androiddesign.R;
import wxm.com.androiddesign.ui.fragment.LoginFragment;
import wxm.com.androiddesign.ui.fragment.MsgListFragment;
import wxm.com.androiddesign.ui.fragment.RankingFragment;
import wxm.com.androiddesign.utils.Config;
import wxm.com.androiddesign.utils.MyUtils;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, LoginFragment.LoginCallBack,HomeFragment.CloseLocService {
    private static final String TAG="MainActivity";

    DrawerLayout drawerLayout;

    public static int SIGNUP = 0x1;

    public static MainActivity instance = null;
    public static WeakReference<AppCompatActivity> activityWeakReference=null;
    public static Context context;
    private long exitTime = 0;

    int flag;

    @Bind(R.id.fab)
    FloatingActionButton fab;
    User mUser = new User();
    @Bind(R.id.logout)
    TextView logout;
    @Bind(R.id.user_photo)
    CircleImageView user_photo;
    @Bind(R.id.user_name)
    TextView user_name;
    @Bind(R.id.user_email)
    TextView user_email;

    @Override
    public void close() {
        closeLocationServices();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        closeLocationServices();
        Log.d(TAG, "onDestroy");
    }

    private Handler mHandler=new Handler(){
        public void handleMeaasage(){

        }
    };

    public void Login() {
        EMChatManager.getInstance().login(MyUser.userId, MyUser.userId, new EMCallBack() {
            @Override
            public void onSuccess() {
                Log.i(TAG, "Login");

            }

            @Override
            public void onError(int i, String s) {
                Log.e(TAG, "LoginError");
            }

            @Override
            public void onProgress(int i, String s) {

            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        instance = this;
        context = getApplicationContext();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        setupFab();
        new LoginTask(this).execute(false);
        setupNavigationView();
        openLocationServices();
        Log.d(TAG + 1, "onCreate:" + this.toString());
        activityWeakReference=new WeakReference<AppCompatActivity>(this);
    }

    private void openLocationServices() {
        Intent i = new Intent();
        i.putExtra("userId", MyUser.userId);
        i.setClass(getApplicationContext(), LocationServices.class);
        startService(i);
    }

    private void closeLocationServices() {
        Intent i = new Intent();
        i.putExtra("userId", MyUser.userId);
        i.setClass(getApplicationContext(), LocationServices.class);
        stopService(i);
    }


    private void setupFab() {
        fab.setOnClickListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume");
    }




        public class LoginTask extends AsyncTask<Boolean, Void, User> {
            Context context;

            public LoginTask(Context context) {
                this.context = context;
            }

            @Override
            protected void onPostExecute(User user) {
                super.onPostExecute(user);
                if (user == null) {

                    return;
                }
                mUser = user;
                Log.d("user", mUser.toString());
                user_name.setText(mUser.getUserName());
                if ("001".equals(mUser.getUserId())) {
                    user_email.setText("点击头像登录");
                    logout.setText("");
                    logout.setClickable(false);
                } else {
                    user_email.setText(mUser.getUserEmail());
                    logout.setText("Logout");
                    logout.setClickable(true);
                }
                MyUser.userId = mUser.getUserId();
                MyUser.userName = mUser.getUserName();
                MyUser.userIcon = mUser.getUserIcon();
                Picasso.with(context).load(MyUser.userIcon).into(user_photo);

                getSupportFragmentManager().beginTransaction().replace(R.id.content, HomeFragment.newInstance(MyUser.userId)).commitAllowingStateLoss();
                flag=1;
                MyUtils.Login(getApplicationContext());
            }

            @Override
            protected User doInBackground(Boolean... params) {

                if (params[0]) {
                    JSONObject object = new JSONObject();
                    try {
                        object.put("action", "loginemail");
                        object.put("userEmail", "001");
                        object.put("userPassword", "001");
                        SharedPreferences prefs = getSharedPreferences("wxm.com.androiddesign", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = prefs.edit();
                        editor.putString("UserId", "001");
                        editor.putString("UserPassword", "001");
                        editor.putBoolean("isSignup",false);
                        editor.apply();
                        String userJson = JsonConnection.getJSON(object.toString());
                        return new Gson().fromJson(userJson, User.class);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                SharedPreferences prefs = getSharedPreferences("wxm.com.androiddesign", Context.MODE_PRIVATE);
                String type=prefs.getString("LoginType", "email");
                String name = prefs.getString("UserId", "001");
                String email=prefs.getString("UserEmail","");
                String password = prefs.getString("UserPassword", "001");
                JSONObject object = new JSONObject();
                try {
                    if(type.equals(MyUser.EMAIL)){
                        object.put("action", "loginemail");
                        object.put("userEmail", email);
                        object.put("userPassword", password);
                    }else if (MyUser.SINA.equals(type)){
                        object.put("action","loginsina");
                        object.put("userId",name);
                    }else if(MyUser.QQ.equals(type)){
                        object.put("action","loginqq");
                        object.put("userId",name);
                    }
                    Log.d(TAG,object.toString());

                    String userJson = JsonConnection.getJSON(object.toString());
                    Log.d(TAG,userJson);
                    if (userJson.contains("false")) {
                        return null;
                    }
                    return new Gson().fromJson(userJson, User.class);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return null;
            }
        }

        @Override
        public void onLongin(User user) {
            mUser = user;
            new LoginTask(this).execute(false);
        }

//        private class UpDateTask extends AsyncTask<String, Void, Void> {
//            @Override
//            protected void onPreExecute() {
//                super.onPreExecute();
//            }
//
//            @Override
//            protected void onPostExecute(Void aVoid) {
//                super.onPostExecute(aVoid);
//            }
//
//            @Override
//            protected Void doInBackground(String... params) {
//                JSONObject object = new JSONObject();
//                try {
//                    object = new JSONObject();
//                    object.put("action", "editAlbumRight");
//                    object.put("userId", MyUser.userId);
//                    object.put("userAlbumIsPublic", params[0]);
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//                String json = JsonConnection.getJSON(object.toString());
//                Log.i("mjson", json);
//                return null;
//            }
//        }

    private void setupDrawerContent(final NavigationView navigationView, final Context context) {

        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        for (int index = 0; index < navigationView.getMenu().size(); index++) {
                            navigationView.getMenu().getItem(index).setChecked(false);
                        }
                        menuItem.setChecked(true);
                        drawerLayout.closeDrawers();
                        switch (menuItem.getItemId()) {
                            case R.id.nav_home:
                                getSupportFragmentManager().beginTransaction().replace(R.id.content, HomeFragment.newInstance(mUser.getUserId())).commitAllowingStateLoss();
                                flag = 1;
                                return true;
                            case R.id.nav_explore:
                                getSupportFragmentManager().beginTransaction().replace(R.id.content, FragmentParent.newInstance(mUser.getUserId())).commitAllowingStateLoss();
                                flag = 2;
                                return true;
                            case R.id.nav_attention:
                                getSupportFragmentManager().beginTransaction().replace(R.id.content, new RankingFragment()).commitAllowingStateLoss();
                                Snackbar.make(drawerLayout, "积分排行",
                                        Snackbar.LENGTH_SHORT).show();
                                flag = 3;
                                return true;
                            case R.id.nav_messages:
                                getSupportFragmentManager().beginTransaction().replace(R.id.content, MsgListFragment.newInstance(mUser.getUserId())).commitAllowingStateLoss();
                                flag = 4;
                                return true;
                            case R.id.nav_user_setting:
//                                new MaterialDialog.Builder(context)
//                                        .title(R.string.permission)
//                                        .items(R.array.per_permission)
//                                        .itemsCallbackSingleChoice(0, new MaterialDialog.ListCallbackSingleChoice() {
//                                            @Override
//                                            public boolean onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {
//                                                if (which == 0) {
//                                                    new UpDateTask().execute("true");
//                                                } else if (which == 1) {
//                                                    new UpDateTask().execute("false");
//                                                } else {
//                                                    Log.d("userwxm", "countA" + which + text);
//                                                    return false;
//                                                }
//                                                Log.d("userwxm", "countB" + which + text);
//                                                return true;
//                                            }
//                                        }).positiveText(R.string.choose)
//                                        .show();
                                return true;
                            default:
                                return true;
                        }
                    }
                });
    }

    @OnClick(R.id.logout)
    public void Logout() {
        new MaterialDialog.Builder(this)
                .title("乃确定不是手滑了么")
                .positiveText("LOGOUT")
                .negativeText("是我手滑了")
                .callback(new MaterialDialog.ButtonCallback() {
                    @Override
                    public void onPositive(MaterialDialog dialog) {
                        new LoginTask(MainActivity.this).execute(true);
                        EMChatManager.getInstance().logout();
                        drawerLayout.closeDrawers();
                    }

                    @Override
                    public void onNegative(MaterialDialog dialog) {

                    }
                })
                .show();

    }


    private void setupNavigationView() {

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        NavigationView navigationView = (NavigationView) findViewById(R.id.navigation_view);
        if (navigationView != null) {
            setupDrawerContent(navigationView, this);
        }
        View header = findViewById(R.id.header);
        header.setClickable(true);
        final CircleImageView userPhoto = (CircleImageView) findViewById(R.id.user_photo);
        userPhoto.setClickable(true);
        userPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, MyUser.userId);
                if ("001".equals(MyUser.userId)) {
                    drawerLayout.closeDrawers();
                    showLoginDialog();
                } else {
                    Intent intent = new Intent(MainActivity.this, UserAcitivity.class);
                    intent.putExtra("userId", mUser.getUserId());
                    startActivity(intent);
                }
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                drawerLayout.openDrawer(GravityCompat.START);

                return true;
            case R.id.action_serch:
                Intent intent = new Intent(MainActivity.this, SearchResultsActivity.class);
                startActivity(intent);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (System.currentTimeMillis()-exitTime>2000){
            Toast.makeText(this,"再按一次退出程序",Toast.LENGTH_SHORT).show();
            exitTime = System.currentTimeMillis();
            return;
        }
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.

        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void onClick(View v) {
        if (!MyUser.userId.equals("001")) {
            Intent intent = new Intent(MainActivity.this, ReleaseActivity.class);
            intent.putExtra("userId", mUser.getUserId());
            intent.putExtra("userIcon",mUser.getUserIcon());
            startActivity(intent);
        } else {
            Toast.makeText(this, "请登录后发布", Toast.LENGTH_SHORT).show();
        }
    }

    private void showLoginDialog() {
        FragmentManager fm = getSupportFragmentManager();
        LoginFragment loginFragment = LoginFragment.newInstance(LoginFragment.MAIN);
        loginFragment.show(fm, "login");
    }

    private MyOnTouchListener onTouchListener;

    public void registerMyOnTouchEvent(MyOnTouchListener listener){
        onTouchListener=listener;
    }

    public interface MyOnTouchListener{
        public void onTouch(MotionEvent event);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (flag==2){
            onTouchListener.onTouch(ev);
        }

        return super.dispatchTouchEvent(ev);
    }
}
