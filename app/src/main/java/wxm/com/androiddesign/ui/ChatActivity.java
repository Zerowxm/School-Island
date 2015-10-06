package wxm.com.androiddesign.ui;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.design.widget.Snackbar;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.easemob.EMCallBack;
import com.easemob.EMError;
import com.easemob.EMEventListener;
import com.easemob.EMNotifierEvent;
import com.easemob.chat.EMChat;
import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMConversation;
import com.easemob.chat.EMMessage;
import com.easemob.chat.TextMessageBody;
import com.easemob.exceptions.EaseMobException;

import org.jivesoftware.smack.Chat;
import org.json.JSONException;
import org.json.JSONObject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import wxm.com.androiddesign.R;
import wxm.com.androiddesign.adapter.ChatAdapter;
import wxm.com.androiddesign.broadcastreceive.NewMessageBroadCastReceiver;
import wxm.com.androiddesign.network.JsonConnection;
import wxm.com.androiddesign.utils.MyUtils;

public class ChatActivity extends AppCompatActivity implements EMEventListener {

    private static final String TAG = "ChatActivity";

    @Bind(R.id.recyclerview_chat)
    RecyclerView recyclerView;
    @Bind(R.id.add_msg)
    EditText mContent;
    @Bind(R.id.send_msg)
    ImageView mSendBtn;
    private ChatAdapter mChatAdapter;
    private String toChatUserId;
    private String userIcon;
    private int chatType;
    private EMConversation conversation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        ButterKnife.bind(this);

        Bundle bundle=getIntent().getExtras();
        Boolean notification=bundle.getBoolean("notification");
        Log.d("noti",notification+"");
        if (notification){
            toChatUserId=bundle.getString("easemobId");
            MyApplication.message.remove(toChatUserId);
            MyApplication.easemobId.remove(toChatUserId);
            onConversationInit();
            new getHX().execute();
        }else {
            toChatUserId=bundle.getString("toChatUserId");
            userIcon=bundle.getString("userIcon");
            onConversationInit();
            setupRecyclerview(recyclerView);
            onConversationInit();
        }
        setupToolBar();
        Log.d(TAG, "onPostResume");
    }

    private void setupToolBar(){
        Toolbar toolbar = (Toolbar) findViewById(R.id.chat_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @OnClick(R.id.send_msg)
    public void setmSendBtn() {
        sendMsg(mContent.getText().toString());
        mContent.setText("");
    }

    @Override
    protected void onPostResume() {

        super.onPostResume();
        if (mChatAdapter!=null){
            mChatAdapter.refresh();
        }
        EMChatManager.getInstance().registerEventListener(this);
        Log.d(TAG, "onPostResume");
        MyApplication.activityResumed(toChatUserId);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MyApplication.activityPaused();
    }

    @Override
    protected void onStop() {
        super.onStop();
        EMChatManager.getInstance().unregisterEventListener(this);
        MyApplication.activityPaused();
    }

    private void onConversationInit() {
        conversation = EMChatManager.getInstance().getConversationByType(toChatUserId
                , EMConversation.EMConversationType.Chat);
        conversation.markAllMessagesAsRead();
    }

    public void sendMsg(String content) {
        if (content.length() > 0) {
            EMMessage message = EMMessage.createSendMessage(EMMessage.Type.TXT);
            Log.d(TAG, "sendMessage");
            TextMessageBody textMessageBody = new TextMessageBody(content);
            message.addBody(textMessageBody);
            message.setReceipt(toChatUserId);
            conversation.addMessage(message);
            sendMsgInBackGround(message);
        }
    }

    public void sendMsgInBackGround(final EMMessage message){
        EMChatManager.getInstance().sendMessage(message, new EMCallBack() {
            @Override
            public void onSuccess() {
                Snackbar.make(recyclerView, "发送成功", Snackbar.LENGTH_SHORT).show();
                mChatAdapter.refreshSelectLast();
                Log.d(TAG,"onSuccess");
            }

            @Override
            public void onError(int i, String s) {
                Snackbar.make(recyclerView, "发送失败", Snackbar.LENGTH_SHORT).show();
                Log.d(TAG, "onError");
            }

            @Override
            public void onProgress(int i, String s) {
                Log.d(TAG,"onProgress"+" "+i+" "+s);
            }
        });
    }

    private void setupRecyclerview(RecyclerView recyclerView) {
        recyclerView.setLayoutManager(new LinearLayoutManager(recyclerView.getContext()));
        recyclerView.setHasFixedSize(true);
        mChatAdapter = new ChatAdapter(ChatActivity.this, toChatUserId, userIcon);
        recyclerView.setAdapter(mChatAdapter);
        mChatAdapter.refreshSelectLast();
    }

    public RecyclerView getRecyclerView() {
        return recyclerView;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_chat, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }if(id==android.R.id.home){
            finish();
        }

        return super.onOptionsItemSelected(item);
    }



    @Override
    public void onEvent(EMNotifierEvent event) {
        switch (event.getEvent()) {
            case EventNewMessage: {
                EMMessage message = (EMMessage) event.getData();
                String userId = null;
                userId = message.getFrom();
                Log.d(TAG,"EventNewMessage");
                if (userId.equals(toChatUserId)) {
                    refreshUIWithNewMessage();
                }else {
                    NotificationCompat.Builder mBuilder=
                            new NotificationCompat.Builder(this)
                                    .setSmallIcon(getApplicationContext().getApplicationInfo().icon)
                                    .setWhen(System.currentTimeMillis())
                                    .setContentTitle("有新消息了!")
                                    .setContentText("A+").setAutoCancel(true);
                    Intent resultIntent=new Intent(this,ChatActivity.class);
                    resultIntent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    resultIntent.putExtra("notification",true);
                    resultIntent.putExtra("easemobId",userId);
                    TaskStackBuilder stackBuilder=TaskStackBuilder.create(this);
                    stackBuilder.addParentStack(ChatActivity.class);
                    stackBuilder.addNextIntent(resultIntent);
                    PendingIntent resultPendingIntent=
                            stackBuilder.getPendingIntent(0,
                                    PendingIntent.FLAG_UPDATE_CURRENT);
                    mBuilder.setContentIntent(resultPendingIntent);
                    NotificationManager mNotificationManager=
                            (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);

                    mNotificationManager.notify(0,mBuilder.build());
                }
                break;
            }
            case EventDeliveryAck:
            {
                EMMessage message=(EMMessage)event.getData();
                refreshUI();
                Log.d(TAG, "EventDeliveryAck");
                break;
            }
            case EventOfflineMessage:
            {
                refreshUI();
                Log.d(TAG, "EventOfflineMessage");
                break;
            }
            default:
                break;
        }
    }

    private void refreshUIWithNewMessage() {
        Log.d(TAG,"refreshUIWithNewMessage");
        if (mChatAdapter != null) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mChatAdapter.refreshSelectLast();
                }
            });
        }
    }

    private void refreshUI(){
        if (mChatAdapter!=null){
            Log.d(TAG,"refreshUI");
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mChatAdapter.refresh();
                }
            });
        }
    }



    private class getHX extends AsyncTask<Void,Void,Boolean>{
        @Override
        protected Boolean doInBackground(Void... params) {
            JSONObject object=new JSONObject();
            try {
                object.put("action","huanxin");
                object.put("easemobId",toChatUserId);
                JSONObject object1=new JSONObject(JsonConnection.getJSON(object.toString()));
                userIcon= object1.getString("userIcon");
                return true;

            } catch (JSONException e) {
                e.printStackTrace();
            }
            return false;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            super.onPostExecute(result);
            if (!result){

            }
            setupRecyclerview(recyclerView);
            onConversationInit();
        }


    }
}