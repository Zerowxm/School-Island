package wxm.com.androiddesign.ui;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.afollestad.materialdialogs.MaterialDialog;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.sina.weibo.SinaWeibo;
import cn.sharesdk.wechat.friends.Wechat;
import cn.sharesdk.wechat.moments.WechatMoments;
import cn.sharesdk.onekeyshare.OnekeyShare;
import de.hdodenhof.circleimageview.CircleImageView;
import wxm.com.androiddesign.R;
import wxm.com.androiddesign.adapter.CommentAdapter;
import wxm.com.androiddesign.adapter.ListViewAdapter;
import wxm.com.androiddesign.module.AtyItem;
import wxm.com.androiddesign.module.CommentData;
import wxm.com.androiddesign.module.MyUser;
import wxm.com.androiddesign.network.JsonConnection;
import wxm.com.androiddesign.widget.MyTextView;

public class AtyDetailActivity extends BaseActivity {

    private static final int SHARE_SUCCESS = 30;
    private static final int SHARE_FAIL = 31;
    private String[] items = new String[] { "分享给好友", "分享到朋友圈" };

    private static final String TAG="AtyDetail";
    AtyItem atyItem;
    ArrayList<CommentData> commentDatas = new ArrayList<CommentData>();
    CommentAdapter commentAdapter;
    private boolean isUser=false;
    @Bind(R.id.sliding_layout)
    SlidingUpPanelLayout mLayout;
    @Bind(R.id.list)
    ListView lv;
    @Bind(R.id.fab)
    FloatingActionButton fab;
    @Bind(R.id.aty_photo)
    ImageView mAtyImage;
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
    @Bind(R.id.view_flipper)
    ViewFlipper flipper;
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
        new getCommentTask().execute(commentData);
        init();
    }

    private void init(){
        ShareSDK.initSDK(this);
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
        setupFlipper();
        setupSlidingPanel();
    }

    private void setupFab(){
        if (isUser){
            fab.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(this, R.color.primary)));
            fab.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_action_send_white));
        }else if(atyItem.getAtyJoined().equals("false")){
            fab.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.fab_add));
        }
        //fab.announceForAccessibility();
    }

    private void setupFlipper(){
        flipper.setAutoStart(true);
        flipper.setFlipInterval(3000);
        if(flipper.isAutoStart()&&!flipper.isFlipping()){
            flipper.startFlipping();
        }
    }

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
                            fab.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.fab_add));
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
                showChatDialog();
                break;
            case R.id.action_comment:
                mLayout.setPanelState(SlidingUpPanelLayout.PanelState.EXPANDED);
        }
        return super.onOptionsItemSelected(item);
    }

    public void share(String text, String photopath, String sharename) {
        Platform.ShareParams sp = new SinaWeibo.ShareParams();
        sp.text = text;
        if (photopath != null) {
            sp.imagePath = photopath;
        }

        Platform weibo = ShareSDK.getPlatform(this, sharename);
        weibo.setPlatformActionListener(new PlatformActionListener() {

            public void onError(Platform platform, int action, Throwable t) {
                Message m = handler.obtainMessage();
                m.what = SHARE_FAIL;
                handler.sendMessage(m);
            }

            public void onComplete(Platform platform, int action,
                                   HashMap<String, Object> res) {
                Message m = handler.obtainMessage();
                m.what = SHARE_SUCCESS;
                handler.sendMessage(m);
            }
            public void onCancel(Platform platform, int action) {
            }
        });
        weibo.share(sp);
    }

    private void showChatDialog() {
        new AlertDialog.Builder(this).setTitle("分享到")
                .setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                share(atyItem.getAtyName(), null, Wechat.NAME);
                                break;
                            case 1:
                                share(atyItem.getAtyName(), null, WechatMoments.NAME);
                                break;
                        }
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).show();
    }

    private Handler handler = new Handler() {
        /*
         * (non-Javadoc)
         *
         * @see android.os.Handler#handleMessage(android.os.Message)
         */
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
}
