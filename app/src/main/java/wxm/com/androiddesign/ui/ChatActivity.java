package wxm.com.androiddesign.ui;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.design.widget.Snackbar;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.afollestad.materialdialogs.MaterialDialog;
import com.easemob.EMCallBack;
import com.easemob.EMEventListener;
import com.easemob.EMNotifierEvent;
import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMConversation;
import com.easemob.chat.EMMessage;
import com.easemob.chat.TextMessageBody;
import com.easemob.exceptions.EaseMobException;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import wxm.com.androiddesign.R;
import wxm.com.androiddesign.adapter.ChatAdapter;
import wxm.com.androiddesign.adapter.ExpressionAdapter;
import wxm.com.androiddesign.adapter.ExpressionPagerAdapter;
import wxm.com.androiddesign.module.MyUser;
import wxm.com.androiddesign.network.JsonConnection;
import wxm.com.androiddesign.utils.SmileUtils;
import wxm.com.androiddesign.widget.ExpandGridView;
import wxm.com.androiddesign.widget.PasteEditText;

public class ChatActivity extends BaseActivity implements EMEventListener {

    private static final String TAG = "ChatActivity";
    public static final int CHAT = 0x1;
    public static final int GROUP_CHAT = 0x2;
    @Bind(R.id.recyclerview_chat)
    RecyclerView recyclerView;
    @Bind(R.id.add_msg)
    PasteEditText mContent;
    @Bind(R.id.send_msg)
    ImageView mSendBtn;
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.ll_face_container)
    LinearLayout emojiIconContainer;
    @Bind(R.id.iv_emoticons_checked)
    ImageView iv_emoticons_check;
    @Bind(R.id.iv_emoticons_normal)
    ImageView iv_emoticons_normal;
    @Bind(R.id.vPager)
    ViewPager expressionViewpager;
    private ChatAdapter mChatAdapter;
    private String toChatUserId;
    private String userIcon;
    private String userName;
    private int chatType;
    private EMConversation conversation;
    private InputMethodManager manager;
    private List<String> reslist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        ButterKnife.bind(this);
        Bundle bundle=getIntent().getExtras();
        manager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        reslist = getExpressionRes(35);
        List<View> views = new ArrayList<View>();
        View gv1 = getGridChildView(1);
        View gv2 = getGridChildView(2);
        views.add(gv1);
        views.add(gv2);
        expressionViewpager.setAdapter(new ExpressionPagerAdapter(views));
        userName = bundle.getString("userName");
        toChatUserId = bundle.getString("easemobId");
        userIcon = bundle.getString("userIcon");
        chatType = bundle.getInt("chatType");
        MaterialDialog materialDialog = new MaterialDialog.Builder(this)
                .title("Loading")
                .progress(true, 0)
                .progressIndeterminateStyle(false)
                .show();
        onConversationInit();
        materialDialog.dismiss();
        new getHX().execute();

        setupToolBar(toolbar);

        Log.d(TAG, "onPostResume");
    }

    @OnClick(R.id.send_msg)
    public void setmSendBtn() {
        sendMsg(mContent.getText().toString());
        mContent.setText("");
    }

    @OnClick(R.id.iv_emoticons_normal)
    public void chooseEmoticons(){
        emojiIconContainer.setVisibility(View.VISIBLE);
        hideKeyboard();
        iv_emoticons_normal.setVisibility(View.INVISIBLE);
        iv_emoticons_check.setVisibility(View.VISIBLE);
    }

    @OnClick(R.id.iv_emoticons_checked)
    public void notChooseEmoticons(){
        emojiIconContainer.setVisibility(View.GONE);
        iv_emoticons_check.setVisibility(View.INVISIBLE);
        iv_emoticons_normal.setVisibility(View.VISIBLE);
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
        if(chatType == CHAT) {
            conversation = EMChatManager.getInstance().getConversationByType(toChatUserId
                    , EMConversation.EMConversationType.Chat);
        }else if(chatType == GROUP_CHAT){
            conversation = EMChatManager.getInstance().getConversationByType(toChatUserId
                    , EMConversation.EMConversationType.GroupChat);
        }
        conversation.markAllMessagesAsRead();
    }

    private void hideKeyboard() {
        if (getWindow().getAttributes().softInputMode != WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN) {
            if (getCurrentFocus() != null)
                manager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    private View getGridChildView(int i) {
        View view = View.inflate(this, R.layout.expression_gridview, null);
        ExpandGridView gv = (ExpandGridView) view.findViewById(R.id.gridview);
        List<String> list = new ArrayList<String>();
        if (i == 1) {
            List<String> list1 = reslist.subList(0, 20);
            list.addAll(list1);
        } else if (i == 2) {
            list.addAll(reslist.subList(20, reslist.size()));
        }
        list.add("delete_expression");
        Log.d("list",list.toString());
        final ExpressionAdapter expressionAdapter = new ExpressionAdapter(this, 1, list);
        gv.setAdapter(expressionAdapter);
        gv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String filename = expressionAdapter.getItem(position);
                try {
                    // 文字输入框可见时，才可输入表情
                    // 按住说话可见，不让输入表情


                        if (!filename .equals("delete_expression") ) { // 不是删除键，显示表情
                            // 这里用的反射，所以混淆的时候不要混淆SmileUtils这个类
                            Class clz = Class.forName("wxm.com.androiddesign.utils.SmileUtils");
                            Field field = clz.getField(filename);
                            Log.d("filename2",filename);
                            mContent.append(SmileUtils.getSmiledText(ChatActivity.this,
                                    (String) field.get(null)));
                            Log.d("field",(String) field.get(null));
                        } else { // 删除文字或者表情
                            if (!TextUtils.isEmpty(mContent.getText())) {

                                int selectionStart = mContent.getSelectionStart();// 获取光标的位置
                                if (selectionStart > 0) {
                                    String body = mContent.getText().toString();
                                    String tempStr = body.substring(0, selectionStart);
                                    int i = tempStr.lastIndexOf("[");// 获取最后一个表情的位置
                                    if (i != -1) {
                                        CharSequence cs = tempStr.substring(i, selectionStart);
                                        if (SmileUtils.containsKey(cs.toString()))
                                            mContent.getEditableText().delete(i, selectionStart);
                                        else
                                            mContent.getEditableText().delete(selectionStart - 1,
                                                    selectionStart);
                                    } else {
                                        mContent.getEditableText().delete(selectionStart - 1, selectionStart);
                                    }
                                }
                            }

                        }

                } catch (Exception e) {
                }

            }
        });
        return view;
    }

    public void sendMsg(String content) {
        if (content.length() > 0) {
           /* EMMessage message = EMMessage.createSendMessage(EMMessage.Type.TXT);
            Log.d(TAG, "sendMessage");
            TextMessageBody textMessageBody = new TextMessageBody(content);
            message.addBody(textMessageBody);
            message.setReceipt(toChatUserId);
            message.setAttribute("identify", "chat");
            message.setAttribute("userName", MyUser.userName);
            conversation.addMessage(message);
            sendMsgInBackGround(message);*/
            new sendMessage().execute(content);
        }
    }

    public List<String> getExpressionRes(int getSum) {
        List<String> reslist = new ArrayList<String>();
        for (int x = 1; x <= getSum; x++) {
            String filename = "ee_" + x;

            reslist.add(filename);

        }
        return reslist;

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
        int id = item.getItemId();
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
                    try {
                        NotificationCompat.Builder mBuilder = null;
                        mBuilder = new NotificationCompat.Builder(MyApplication.applicationContext)
                                .setSmallIcon(MyApplication.applicationContext.getApplicationInfo().icon)
                                .setWhen(System.currentTimeMillis())
                                .setContentTitle(message.getStringAttribute("userName") + "（" + message.getStringAttribute("newMsgCount") + "条新信息）")
                                .setContentText(((TextMessageBody) message.getBody()).getMessage())
                                .setAutoCancel(true)
                                .setVibrate(new long[]{0, 200, 200, 200})
                                .setLights(Color.GREEN, 1000, 1000)
                                .setTicker(((TextMessageBody) message.getBody()).getMessage());
                        Intent resultIntent = new Intent(MyApplication.applicationContext, NotificationActivity.class);
                        resultIntent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                        resultIntent.putExtra("type",NotificationActivity.CHAT);
                        resultIntent.putExtra("notification", true);
                        resultIntent.putExtra("easemobId", userId);
                        TaskStackBuilder stackBuilder = TaskStackBuilder.create(MyApplication.applicationContext);
                        stackBuilder.addParentStack(MainActivity.class);
                        stackBuilder.addNextIntent(resultIntent);
                        PendingIntent resultPendingIntent =
                                stackBuilder.getPendingIntent(2,
                                        PendingIntent.FLAG_UPDATE_CURRENT);
                        mBuilder.setContentIntent(resultPendingIntent);
                        Notification notification = mBuilder.build();
                        notification.defaults = Notification.DEFAULT_SOUND;
                        notification.flags = Notification.FLAG_SHOW_LIGHTS;
                        NotificationManager mNotificationManager =
                                (NotificationManager) MyApplication.applicationContext.getSystemService(Context.NOTIFICATION_SERVICE);
                        mNotificationManager.notify(Integer.parseInt(userId.substring(1)), notification);
                    } catch (EaseMobException e) {
                        e.printStackTrace();
                    }
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


    private class sendMessage extends AsyncTask<String,Void,Boolean>{
        String content;
        String newMsgCount =null;
        @Override
        protected Boolean doInBackground(String... params) {
            JSONObject object=new JSONObject();
            try {
                object.put("action","sendMessage");
                object.put("easemobId",MyUser.getEasemobId());
                object.put("toEasemobId",toChatUserId);
                object.put("userId",MyUser.userId);
                object.put("msgContent",params[0]);
                String newMsg = JsonConnection.getJSON(object.toString());
                JSONObject newMsgJson = new JSONObject(newMsg);
                newMsgCount = newMsgJson.getString("newMsgCount");
                content = params[0];
                return true;
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return false;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            super.onPostExecute(result);
            EMMessage message = EMMessage.createSendMessage(EMMessage.Type.TXT);
            Log.d(TAG, "sendMessage");
            TextMessageBody textMessageBody = new TextMessageBody(content);
            message.addBody(textMessageBody);
            if(chatType == CHAT) {
                message.setChatType(EMMessage.ChatType.Chat);
            }else if (chatType == GROUP_CHAT){
                message.setChatType(EMMessage.ChatType.GroupChat);
            }
            message.setReceipt(toChatUserId);
            message.setAttribute("identify", "chat");
            message.setAttribute("userName", MyUser.userName);
            message.setAttribute("newMsgCount", newMsgCount);
            conversation.addMessage(message);
            sendMsgInBackGround(message);
            setupRecyclerview(recyclerView);
            onConversationInit();
        }
    }


    private class getHX extends AsyncTask<Void,Void,Boolean>{
        @Override
        protected Boolean doInBackground(Void... params) {
            JSONObject object=new JSONObject();
            try {
                object.put("action","userChat");
                object.put("toEasemobId",toChatUserId);
                object.put("fromEasemobId",MyUser.easemobId);
                JSONObject object1=new JSONObject(JsonConnection.getJSON(object.toString()));
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
