package wxm.com.androiddesign.ui;

import android.app.NotificationManager;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Point;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mob.tools.utils.UIHandler;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OnekeyShare;
import de.hdodenhof.circleimageview.CircleImageView;
import wxm.com.androiddesign.MyDialog;
import wxm.com.androiddesign.R;
import wxm.com.androiddesign.adapter.CommentAdapter;
import wxm.com.androiddesign.adapter.ListViewAdapter;
import wxm.com.androiddesign.module.AtyItem;
import wxm.com.androiddesign.module.CommentData;
import wxm.com.androiddesign.module.MyUser;
import wxm.com.androiddesign.network.JsonConnection;
import wxm.com.androiddesign.utils.MyUtils;
import wxm.com.androiddesign.widget.MyTextView;

public class AtyDetailActivity extends BaseActivity implements PlatformActionListener,
        Handler.Callback {

    private static final int SHARE_SUCCESS = 30;
    private static final int SHARE_FAIL = 31;
    private static final int MSG_TOAST = 1;
    private static final int MSG_ACTION_CCALLBACK = 2;
    private static final int MSG_CANCEL_NOTIFY = 3;
    private static final String TAG="AtyDetail";
    AtyItem atyItem;
    ArrayList<CommentData> commentDatas = new ArrayList<>();
    CommentAdapter commentAdapter;
    private boolean isUser=false;
    @Bind(R.id.sliding_layout)
    SlidingUpPanelLayout mLayout;
    @Bind(R.id.list)
    ListView lv;
    @Bind(R.id.fab)
    FloatingActionButton fab;
    //    @Bind(R.id.aty_photo)
//    ImageView mAtyImage;
    @Bind(R.id.aty_name)
    TextView atyName;
    @Bind(R.id.aty_time)
    TextView atyTime;
    @Bind(R.id.aty_content)
    TextView atyContent;
    @Bind(R.id.people)
    TextView mPeople;
    @Bind(R.id.comment_numbers)
    MyTextView mNumber;
    @Bind(R.id.user_photo)
    CircleImageView userPhoto;
    @Bind(R.id.user_name)
    TextView userName;
    @Bind(R.id.imageViewContainer)
    LinearLayout imageViewContainer;
    @Bind(R.id.toolbar)
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.aty_detail);
        ButterKnife.bind(this);
        mLayout.setPanelState(SlidingUpPanelLayout.PanelState.HIDDEN);
        addComment();
        Bundle bundle = getIntent().getExtras();
        atyItem = (bundle.getParcelable("com.wxm.com.androiddesign.module.ActivityItemData"));
        if (MyUser.userId.equals(atyItem.getUserId()))
            isUser=true;
        CommentData commentData=null;

        Point size=MyUtils.getScreenSize(this);
        int screenWidth = size.x - 7;
        int screenHeight = size.y;
        if (atyItem.getAtyAlbum()==null||atyItem.getAtyAlbum().size()==0){
            ImageView imageView = (ImageView) LayoutInflater.from(this).inflate(R.layout.image, null);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(screenWidth, screenHeight * 2 / 5);
            imageView.setImageDrawable(ContextCompat.getDrawable(this,R.drawable.test));
            imageView.setLayoutParams(layoutParams);
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageViewContainer.addView(imageView);
        }
        else if (atyItem.getAtyAlbum() != null&&atyItem.getAtyAlbum().size()!=0) {
            for (int i = 0; i < atyItem.getAtyAlbum().size(); i++) {
                Log.d("imageuri",""+ atyItem.getAtyAlbum().size());
                ImageView imageView=new ImageView(this);
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(screenWidth, screenHeight * 2 /5);
                Log.d("image", atyItem.getAtyAlbum().get(i));
                imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                Picasso.with(this).load(atyItem.getAtyAlbum().get(i)).into(imageView);
                imageView.setLayoutParams(layoutParams);
                imageView.setTag(i);
                final List<String> album=atyItem.getAtyAlbum();
                imageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(final View v) {
                        MyDialog dialog = MyDialog.newInstance(album.get((Integer) v.getTag()));
                        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                        ft.add(dialog,"showPic");
                        ft.commitAllowingStateLoss();
                    }
                });
                imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                imageViewContainer.addView(imageView);
            }
            // }
        }
        new getCommentTask().execute(commentData);
        init();
    }

    private void init(){
        ShareSDK.initSDK(this);
        // 初始化图片路径
//        new Thread() {
//            public void run() {
//                initImagePath();
//            }
//        }.start();
        atyTime.setText(atyItem.getAtyStartTime());
        userName.setText(atyItem.getUserName());
        atyName.setText(atyItem.getAtyName());
        atyContent.setText(atyItem.getAtyContent());
        mPeople.setText("已有" + atyItem.getAtyMembers() + "人参加");
        mNumber.setText(atyItem.getAtyComments());
        Log.d("atyComments", atyItem.getAtyComments());
        Picasso.with(getApplicationContext()).load(MyUser.userIcon).into(userPhoto);
        setupToolBar(toolbar);
        setupFab();
        //setupFlipper();
        setupSlidingPanel();
    }

    private void setupFab(){
        if (isUser){
            fab.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(this, R.color.primary)));
            fab.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_action_send_white));
        }else if(atyItem.getAtyJoined().equals("false")){
            fab.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.fab_add));
        }else if(atyItem.getAtyJoined().equals("true")){
            fab.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_action_clear));
        }
        //fab.announceForAccessibility();
    }

//    private void setupFlipper(){
//        flipper.setAutoStart(true);
//        flipper.setFlipInterval(3000);
//        if(flipper.isAutoStart()&&!flipper.isFlipping()){
//            flipper.startFlipping();
//        }
//    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        ShareSDK.stopSDK(this);
    }

    @OnClick(R.id.user_photo)
    public void showUser(){
        Intent intent = new Intent(this, UserAcitivity.class);
        intent.putExtra("userId", atyItem.getUserId());
        startActivity(intent);
    }

    @OnClick(R.id.show_people)
    public void showPeople(){
        Intent showIntent = new Intent(this, UserListActivity.class);
        showIntent.putExtra("atyId", atyItem.getAtyId());
        startActivity(showIntent);
    }

    @OnClick(R.id.fab)
    public void sendNotify(){
        if(isUser) {
            new MaterialDialog.Builder(this)
                    .title("发送通知")
                    .inputMaxLength(30, R.color.mdtp_red)
                    .input(null, null, new MaterialDialog.InputCallback() {
                        @Override
                        public void onInput(MaterialDialog dialog, CharSequence input) {
                            new NotifyMSG().execute(input.toString());
                        }
                    }).callback(new MaterialDialog.ButtonCallback() {
                @Override
                public void onPositive(MaterialDialog dialog) {
                    super.onPositive(dialog);
                }
            }).show();
        }else if(atyItem.getAtyJoined().equals("false")){
            new MaterialDialog.Builder(this)
                    .title(R.string.add_title)
                    .content(atyItem.getAtyName())
                    .positiveText(R.string.OK)
                    .negativeText(R.string.cancel)
                    .callback(new MaterialDialog.ButtonCallback() {
                        @Override
                        public void onPositive(MaterialDialog dialog) {
                            atyItem.setAtyJoined("true");
                            atyItem.setAtyMembers(String.valueOf(Integer.parseInt(atyItem.getAtyMembers()) + 1));
                            fab.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_action_clear));
                            new UpDateTask().execute("join");
                        }
                        @Override
                        public void onNegative(MaterialDialog dialog) {

                        }
                    })
                    .show();

        } else {
            new MaterialDialog.Builder(this)
                    .title("主人你真的要退出瞄(´c_`)")
                    .content(atyItem.getAtyName())
                    .positiveText("是的")
                    .negativeText(R.string.cancel)
                    .callback(new MaterialDialog.ButtonCallback() {
                        @Override
                        public void onPositive(MaterialDialog dialog) {
                            atyItem.setAtyJoined("false");
                            atyItem.setAtyMembers(String.valueOf(Integer.parseInt(atyItem.getAtyMembers()) - 1));
                            fab.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.fab_add));
                            new UpDateTask().execute("notJoin");
                        }
                        @Override
                        public void onNegative(MaterialDialog dialog) {
                        }
                    })
                    .show();
        }
    }

    @Override
    public boolean handleMessage(Message msg) {
        switch (msg.what) {
            case MSG_TOAST: {
                String text = String.valueOf(msg.obj);
                Toast.makeText(AtyDetailActivity.this, text, Toast.LENGTH_SHORT).show();
            }
            break;
//            case MSG_ACTION_CCALLBACK: {
//                switch (msg.arg1) {
//                    case 1: // 成功后发送Notification
//                        showNotification(2000, "分享完成");
//                        break;
//                    case 2: // 失败后发送Notification
//                        showNotification(2000, "分享失败");
//                        break;
//                    case 3: // 取消
//                        showNotification(2000, "取消分享");
//                        break;
//                }
//            }
//            break;
            case MSG_CANCEL_NOTIFY:
                NotificationManager nm = (NotificationManager) msg.obj;
                if (nm != null) {
                    nm.cancel(msg.arg1);
                }
                break;
        }
        return false;
    }

    // 取消后的回调方法
    @Override
    public void onCancel(Platform platform, int action) {
        Message msg = new Message();
        msg.what = MSG_ACTION_CCALLBACK;
        msg.arg1 = 3;
        msg.arg2 = action;
        msg.obj = platform;
        UIHandler.sendMessage(msg, this);
    }

    // 完成后的回调方法
    @Override
    public void onComplete(Platform platform, int action,
                           HashMap<String, Object> arg2) {
        Message msg = new Message();
        msg.what = MSG_ACTION_CCALLBACK;
        msg.arg1 = 1;
        msg.arg2 = action;
        msg.obj = platform;
        UIHandler.sendMessage(msg, this);
    }

    // 出错后的回调方法
    @Override
    public void onError(Platform platform, int action, Throwable t) {
        t.printStackTrace();
        Message msg = new Message();
        msg.what = MSG_ACTION_CCALLBACK;
        msg.arg1 = 2;
        msg.arg2 = action;
        msg.obj = t;
        UIHandler.sendMessage(msg, this);
    }

//    // 根据传入的参数显示一个Notification
//    @SuppressWarnings("deprecation")
//    private void showNotification(long cancelTime, String text) {
//        try {
//            Context app = getApplicationContext();
//            NotificationManager nm = (NotificationManager) app
//                    .getSystemService(Context.NOTIFICATION_SERVICE);
//            final int id = Integer.MAX_VALUE / 13 + 1;
//            nm.cancel(id);
//            long when = System.currentTimeMillis();
//            Notification notification = new Notification(
//                    R.drawable.test, text, when);
//            PendingIntent pi = PendingIntent.getActivity(app, 0, new Intent(),
//                    0);
//            //notification.setLatestEventInfo(app, "sharesdk test", text, pi);
//            notification.flags = Notification.FLAG_AUTO_CANCEL;
//            //nm.notify(id, notification);
//
//            if (cancelTime > 0) {
//                Message msg = new Message();
//                msg.what = MSG_CANCEL_NOTIFY;
//                msg.obj = nm;
//                msg.arg1 = id;
//                UIHandler.sendMessageDelayed(msg, cancelTime, this);
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }


    private class UpDateTask extends AsyncTask<String, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
        }

        @Override
        protected Void doInBackground(String... params) {
            JSONObject object = new JSONObject();
            try {
                object = new JSONObject();
                object.put("action", params[0]);
                object.put("userId", MyUser.userId);
                object.put("userName",MyUser.userName);
                object.put("atyId", atyItem.getAtyId());
                object.put("easemobId",MyUser.getEasemobId());
                object.put("atyName", atyItem.getAtyName());
            } catch (JSONException e) {
                e.printStackTrace();
            }
            String json = JsonConnection.getJSON(object.toString());
            Log.i("mjson", json);
            return null;
        }
    }

    private void setupSlidingPanel(){
        mLayout.setPanelSlideListener(new SlidingUpPanelLayout.PanelSlideListener() {
            @Override
            public void onPanelSlide(View panel, float slideOffset) {
                Log.i(TAG, "onPanelSlide, offset " + slideOffset);
                if (slideOffset == 0) {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            mLayout.setPanelState(SlidingUpPanelLayout.PanelState.HIDDEN);
                        }
                    }, 50);
                }
            }

            @Override
            public void onPanelExpanded(View panel) {
                Log.i(TAG, "onPanelExpanded");
            }

            @Override
            public void onPanelCollapsed(View panel) {
                Log.i(TAG, "onPanelCollapsed");
            }

            @Override
            public void onPanelAnchored(View panel) {
                Log.i(TAG, "onPanelAnchored");
            }

            @Override
            public void onPanelHidden(View panel) {
                Log.i(TAG, "onPanelHidden");
            }
        });
    }

    public void addComment() {
        ImageView cmt_comment = (ImageView) findViewById(R.id.cmt_comment);
        final EditText cmt_text = (EditText) findViewById(R.id.add_comment);
        cmt_comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cmt_text.getText() != null && !cmt_text.getText().toString().equals("")) {
                    // SimpleDateFormat formatter = new SimpleDateFormat("MM月dd日 HH:mm");
                    // Date nowDate = new Date(System.currentTimeMillis());
                    // String time = formatter.format(nowDate);
                    CommentData mcommentData = new CommentData("comment", MyUser.userId, atyItem.getAtyId(),"moments ago", cmt_text.getText().toString());
                    new getCommentTask().execute(mcommentData);
                    //commentDatas.add(mcommentData);
                    cmt_text.setText(null);
                    mNumber.setText(Integer.parseInt(mNumber.getText().toString())+1+"");
                } else {
                    Toast.makeText(AtyDetailActivity.this, "Please enter comment", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    private class NotifyMSG extends AsyncTask<String,Void,Boolean>{

        @Override
        protected Boolean doInBackground(String... params) {
            JSONObject object = new JSONObject();
            try {
                //SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd  HH:mm:ss");
                object.put("action", "notifyFromAty");
                object.put("atyId", atyItem.getAtyId());
                object.put("atyName",atyItem.getAtyName());
                //object.put("releaseTime",formatter.format(new Date(System.currentTimeMillis()))+"");
                object.put("userId", MyUser.userId);
                object.put("userName", MyUser.userName);
                object.put("userIcon",MyUser.userIcon);
                object.put("easemobId",MyUser.getEasemobId());
                object.put("msgContent",params[0]);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            String json = JsonConnection.getJSON(object.toString());
            return null;
        }
    }

    private class getCommentTask extends AsyncTask<CommentData, Void, Boolean> {
        public getCommentTask() {

        }
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Boolean result) {
            super.onPostExecute(result);
            commentAdapter=new CommentAdapter(commentDatas,AtyDetailActivity.this);
            ListViewAdapter adapter=new ListViewAdapter(getApplicationContext(),commentDatas);
            lv.setAdapter(adapter);
            adapter.notifyDataSetChanged();
        }

        @Override
        protected Boolean doInBackground(CommentData... params) {
            if (params[0]==null) {
                JSONObject object = new JSONObject();
                try {
                    object.put("action", "showComments");
                    object.put("atyId", atyItem.getAtyId());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                String json = JsonConnection.getJSON(object.toString());
                commentDatas = new Gson().fromJson(json, new TypeToken<ArrayList<CommentData>>() {
                }.getType());
                return false;
            } else {
                try {
                    JSONObject object = new JSONObject(new Gson().toJson(params[0]));
                    object.put("userName", MyUser.userName);
                    object.put("atyName",atyItem.getAtyName());
                    object.put("easemobId",MyUser.getEasemobId());
                    String json = JsonConnection.getJSON(object.toString());
                    CommentData commentData = new Gson().fromJson(json, CommentData.class);
                    commentDatas.add(0,commentData);
                    return true;
                }
                catch (JSONException e) {
                    e.printStackTrace();
                }
                return  false;
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_detail, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //mLayout.setAnchorPoint(0.7f);
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                break;
            case R.id.action_share:
                share();
                break;
            case R.id.action_comment:
                mLayout.setPanelState(SlidingUpPanelLayout.PanelState.EXPANDED);
        }
        return super.onOptionsItemSelected(item);
    }
    public void share() {
        //实例化一个OnekeyShare对象
        OnekeyShare oks = new OnekeyShare();
        //设置Notification的显示图标和显示文字
        //oks.setNotification(R.drawable.ic_launcher, "ShareSDK demo");
        //设置短信地址或者是邮箱地址，如果没有可以不设置
        oks.setAddress("12345678901");
        //分享内容的标题
        oks.setTitle("我参加了校园岛的\"" + atyItem.getAtyName() + "\",一起来吧！");
        //标题对应的网址，如果没有可以不设置
        //oks.setTitleUrl("http://www.17heli.com");
        //设置分享的文本内容
        oks.setText("我参加了校园岛的\"" + atyItem.getAtyName() + "\",一起来吧！\n" + atyItem.getAtyContent());
        //设置分享照片的本地路径，如果没有可以不设置
        //oks.setImagePath(AtyDetailActivity.TEST_IMAGE);
        //设置分享照片的url地址，如果没有可以不设置
        oks.setImageUrl(MyDialog.getBigImage(atyItem.getAtyAlbum().get(0)));
        //微信和易信的分享的网络连接，如果没有可以不设置
        oks.setUrl("http://106.0.4.149:8081/bootStrap/ShareServlet?atyId="+atyItem.getAtyId());
        //人人平台特有的评论字段，如果没有可以不设置
        oks.setComment("添加评论");
        //程序的名称或者是站点名称
        oks.setSite("site");
        //程序的名称或者是站点名称的链接地址
        oks.setSiteUrl("http://www.baidu.com");
//        //设置纬度
//        oks.setLatitude(23.122619f);
//        //设置精度
//        oks.setLongitude(113.372338f);
        //设置是否是直接分享
        oks.setSilent(false);
        //显示
        oks.show(AtyDetailActivity.this);
    }
}
