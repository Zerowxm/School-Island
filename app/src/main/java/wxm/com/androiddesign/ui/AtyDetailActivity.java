package wxm.com.androiddesign.ui;


import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;

import android.app.NotificationManager;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
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
import wxm.com.androiddesign.adapter.AvatorAdapter;
import wxm.com.androiddesign.adapter.CommentAdapter;
import wxm.com.androiddesign.adapter.ListViewAdapter;
import wxm.com.androiddesign.adapter.MyRecycerAdapter;
import wxm.com.androiddesign.adapter.TabPagerAdapter;
import wxm.com.androiddesign.listener.RecyclerItemClickListener;
import wxm.com.androiddesign.module.AtyItem;
import wxm.com.androiddesign.module.Avator;
import wxm.com.androiddesign.module.CommentData;
import wxm.com.androiddesign.module.MyUser;
import wxm.com.androiddesign.network.JsonConnection;
import wxm.com.androiddesign.ui.fragment.ImageFragment;
import wxm.com.androiddesign.utils.ActivityStartHelper;
import wxm.com.androiddesign.utils.SpacesItemDecoration;
import wxm.com.androiddesign.widget.CirclePageIndicator;
import wxm.com.androiddesign.widget.MyTextView;
import wxm.com.androiddesign.widget.WrappingLinearLayoutManager;

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
    @Bind(R.id.image_pager)
    ViewPager pager;
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.comment_user_photo)
    ImageView userImg;
    @Bind(R.id.action_love)
    ImageView isLike;
    @Bind(R.id.avator_list)
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.aty_detail);
        ButterKnife.bind(this);
        Bundle bundle = getIntent().getExtras();
        if (bundle.getBoolean("isNotify")){
            new getDetailAll().execute(bundle.getString("atyId"));
        }else {
            atyItem = (bundle.getParcelable("com.wxm.com.androiddesign.module.ActivityItemData"));
            atyItem.setAtyStartTime(atyItem.getAtyStartTime().replace("\n", " "));
            atyItem.setAtyEndTime(atyItem.getAtyEndTime().replace("\n", " "));
            new GetIsJoin().execute();
            addComment();
        }

        mLayout.setPanelState(SlidingUpPanelLayout.PanelState.HIDDEN);

    }
    private void setupRecyclerView(final List<Avator> list) {
        final WrappingLinearLayoutManager layoutManager = new WrappingLinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(false);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setAdapter(new AvatorAdapter(list, getApplicationContext()));
        recyclerView.addItemDecoration(new SpacesItemDecoration(this));
        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(this, new RecyclerItemClickListener.OnItemClickListener() {
            @Override public void onItemClick(View view, int position) {
                ActivityStartHelper.startProfileActivity(AtyDetailActivity.this,list.get(position).getUserId());
            }
        }));
    }

    @OnClick(R.id.check_more)
    public void CheckMore(){
        UserListActivity.start(this, atyItem.getAtyId());
    }

    @OnClick(R.id.user_photo)
    public void UserDetail(){
        ActivityStartHelper.startProfileActivity(this, atyItem.getUserId());
    }

    private class GetIsJoin extends AsyncTask<Void, Void, Boolean> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Boolean result) {
            super.onPostExecute(result);
            init();
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            JSONObject object = new JSONObject();
            try {
                object.put("action", "showAtyDetails");
                object.put("atyId", atyItem.getAtyId());
                object.put("userId", MyUser.userId);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            String json = JsonConnection.getJSON(object.toString());
            Log.d("isJoined",json);
            try {
                JSONObject object1 = new JSONObject(json);
                String isJoin = object1.getString("isJoined");
                String isLiked = object1.getString("isLiked");
                atyItem.setAtyIsJoined(isJoin);
                atyItem.setAtyIsLiked(isLiked);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return true;
        }
    }

    private class getDetailAll extends AsyncTask<String, Void, Boolean> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Boolean result) {
            super.onPostExecute(result);
            atyItem.setAtyStartTime(atyItem.getAtyStartTime().replace("\n", " "));
            atyItem.setAtyEndTime(atyItem.getAtyEndTime().replace("\n", " "));
            init();
            addComment();

        }

        @Override
        protected Boolean doInBackground(String... params) {
            JSONObject object = new JSONObject();
            String atyId=params[0];
            try {
                object.put("action", "showAtyDetailsAll");
                object.put("atyId", atyId);
                object.put("userId", MyUser.userId);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            String json = JsonConnection.getJSON(object.toString());
            Log.d("isJoined",json);
                atyItem=new Gson().fromJson(json,AtyItem.class);
            return true;
        }
    }

    private class GetPeople extends AsyncTask<Void, Void, Boolean> {
        List<Avator> mUserList;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Boolean result) {
            super.onPostExecute(result);
            if (true){
                setupRecyclerView(mUserList);
            }
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            JSONObject object = new JSONObject();
            try {
                object.put("action", "showAtyMembers");
                object.put("atyId", atyItem.getAtyId());
//                object.put("")
            } catch (JSONException e) {
                e.printStackTrace();
            }
            String json = JsonConnection.getJSON(object.toString());
            mUserList = new Gson().fromJson(json, new TypeToken<List<Avator>>() {
            }.getType());
            if (mUserList!=null){
                return true;
            }else {
                return false;
            }
        }
    }



    private void setupViewPager() {
        TabPagerAdapter adapter = new TabPagerAdapter(getSupportFragmentManager());
        if (atyItem.getAtyAlbum()!=null&&atyItem.getAtyAlbum().size()!=0){
            for (String url:atyItem.getAtyAlbum()){
                adapter.addFragment(ImageFragment.newInstance(url,1),"AtyImg");
            }
        }
        pager.setAdapter(adapter);
        CirclePageIndicator circlePageIndicator=(CirclePageIndicator)findViewById(R.id.indicator);
        circlePageIndicator.setViewPager(pager);
        circlePageIndicator.setSnap(true);
    }


    @OnClick(R.id.action_love)
    public void MakeLove(View view){
        ImageView imageView=(ImageView)view;
        if(atyItem.getAtyIsLiked().equals("true")){
            imageView.setColorFilter(ContextCompat.getColor(this, R.color.white));
            atyItem.setAtyIsLiked("false");
            new UpDateTask().execute("notLike");
        }else {
            imageView.setColorFilter(ContextCompat.getColor(this, R.color.yellow));
            atyItem.setAtyIsLiked("true");
            new UpDateTask().execute("like");
        }
    }

    private void init(){
        atyTime.setText(atyItem.getAtyStartTime()+" 在"+atyItem.getAtyPlace()+"举行\n"+atyItem.getAtyEndTime()+" 活动结束");
        userName.setText(atyItem.getUserName());
        atyName.setText(atyItem.getAtyName());
        atyContent.setText(atyItem.getAtyContent());
        mPeople.setText("已有" + atyItem.getAtyMembers() + "人参加");
        mNumber.setText(atyItem.getAtyComments());
        Log.d("atyComments", atyItem.getAtyComments());
        Picasso.with(getApplicationContext()).load(atyItem.getUserIcon()).into(userPhoto);
        Picasso.with(getApplicationContext()).load(MyUser.userIcon).into(userImg);
        //setupToolBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        setupFab();
        setupLike();
        setupSlidingPanel();
        setupViewPager();
        new GetPeople().execute();
        new GetLatestAty().execute();
    }

    private void setupLike(){
        if(atyItem.getAtyIsLiked().equals("true")){
            isLike.setColorFilter(ContextCompat.getColor(this, R.color.yellow));
        }else if(atyItem.getAtyIsLiked().equals("false")){
            isLike.setColorFilter(ContextCompat.getColor(this, R.color.white));
        }
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


    @Override
    protected void onDestroy(){
        super.onDestroy();
        ShareSDK.stopSDK(this);
    }


//    @OnClick(R.id.show_people)
//    public void showPeople(){
//        Intent showIntent = new Intent(this, UserListActivity.class);
//        showIntent.putExtra("atyId", atyItem.getAtyId());
//        startActivity(showIntent);
//    }

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

    public static void start(Context c,AtyItem atyItem,Boolean isNotify) {
        c.startActivity(new Intent(c, AtyDetailActivity.class)
                .putExtra("com.wxm.com.androiddesign.module.ActivityItemData", atyItem)
        .putExtra("isNotify",isNotify));
    }
    public static void start(Context c,String atyId,Boolean isNotify) {
        c.startActivity(new Intent(c, AtyDetailActivity.class)
                .putExtra("atyId", atyId).putExtra("isNotify",isNotify));
    }
    @Bind(R.id.aty_other)
    LinearLayout otherList;
    private void setupOther(final List<AtyItem> aties){
        for (int i=0;i<aties.size();i++){
            final TextView textView =new TextView(this);
            textView.setText(aties.get(i).getAtyName());
            textView.setTag(i);
            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AtyDetailActivity.start(AtyDetailActivity.this,aties.get((int)textView.getTag()),false);
                }
            });
            otherList.addView(textView);
            if (i>3){
                TextView moreText =new TextView(this);
                moreText.setText("查看更多");
                otherList.addView(textView);
                break;
            }
        }
    }
    private class GetLatestAty extends AsyncTask<Void, Void, Boolean> {
        List<AtyItem> latestAties;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Boolean result) {
            super.onPostExecute(result);
            if (result){
                setupOther(latestAties);
            }
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            JSONObject object = new JSONObject();
            try {
                object = new JSONObject();
                object.put("action", "showOtherAty");
                object.put("userId", atyItem.getUserId());
                object.put("atyId", atyItem.getAtyId());
            } catch (JSONException e) {
                e.printStackTrace();
            }
            String json = JsonConnection.getJSON(object.toString());
            latestAties=new Gson().fromJson(json, new TypeToken<List<AtyItem>>() {
            }.getType());
            return true;
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
                    CommentData mcommentData = new CommentData("comment", MyUser.userId, atyItem.getAtyId(),"moments ago", cmt_text.getText().toString());
                    new getCommentTask().execute(mcommentData);
                    cmt_text.setText(null);
                    mNumber.setText(Integer.parseInt(mNumber.getText().toString())+1+"");
                } else {
                    Toast.makeText(AtyDetailActivity.this, "Please enter comment", Toast.LENGTH_SHORT).show();
                }
            }
        });

        if (MyUser.userId.equals(atyItem.getUserId()))
            isUser=true;
        CommentData commentData=null;
        new getCommentTask().execute(commentData);
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

    @OnClick(R.id.action_comment)
    public void comment(){
        mLayout.setPanelState(SlidingUpPanelLayout.PanelState.EXPANDED);
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub

            switch (msg.what) {
                case SHARE_SUCCESS:

                    Toast.makeText(AtyDetailActivity.this, "分享成功", Toast.LENGTH_SHORT)
                            .show();
                    break;
                case SHARE_FAIL:

                    Toast.makeText(AtyDetailActivity.this, "分享失败", Toast.LENGTH_SHORT)
                            .show();
                    break;

            }
        }

    };

    @OnClick(R.id.action_share)
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
        oks.setUrl("http://192.168.199.217:8080/bootStrap/ShareServlet?atyId="+atyItem.getAtyId());
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
