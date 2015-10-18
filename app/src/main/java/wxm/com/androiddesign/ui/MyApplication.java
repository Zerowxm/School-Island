package wxm.com.androiddesign.ui;

import android.app.Application;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.media.audiofx.NoiseSuppressor;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.util.Log;

import com.baidu.mapapi.SDKInitializer;
import com.easemob.EMEventListener;
import com.easemob.EMNotifierEvent;
import com.easemob.chat.EMChat;
import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMChatOptions;
import com.easemob.chat.EMGroup;
import com.easemob.chat.EMGroupManager;
import com.easemob.chat.EMMessage;
import com.easemob.chat.OnMessageNotifyListener;
import com.easemob.chat.OnNotificationClickListener;
import com.easemob.chat.TextMessageBody;
import com.easemob.exceptions.EaseMobException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.sharesdk.framework.ShareSDK;
import wxm.com.androiddesign.broadcastreceive.NewMessageBroadCastReceiver;
import wxm.com.androiddesign.listener.MyConnectionListener;
import wxm.com.androiddesign.notification.Notifications;
import wxm.com.androiddesign.utils.HXSDKHelper;
import wxm.com.androiddesign.utils.MyUtils;

/**
 * Created by Administrator on 2015/7/3.
 */
public class MyApplication extends Application implements EMEventListener {
    public static Context applicationContext;
    private static MyApplication instance;
    //public static HXSDKHelper hxsdkHelper;

    public static Map<String,Integer> easemobId = new HashMap<>();
    private static final String TAG = "MyApplication";
    NewMessageBroadCastReceiver receiver = new NewMessageBroadCastReceiver();
    ;

    @Override
    public void onCreate() {
        super.onCreate();
        applicationContext = this;
        instance = this;
        EMChat.getInstance().setAutoLogin(false);
        SDKInitializer.initialize(applicationContext);
        EMChat.getInstance().init(applicationContext);
        EMChat.getInstance().setDebugMode(true);
        Log.d(TAG, "ApplicationonCreate");
        initSDK(this);
        addConnectionListener();
    }

    public static MyApplication getInstance() {
        return instance;
    }

    private void initSDK(Context context) {
        //初始化sharesdk,具体集成步骤请看文档：
        ShareSDK.initSDK(context);
        IntentFilter filter = new IntentFilter(EMChatManager.getInstance().getNewMessageBroadcastAction());
        filter.setPriority(3);
        registerReceiver(receiver, filter);
        EMChat.getInstance().setAppInited();
        EMChatManager.getInstance().registerEventListener(this);
    }

    private void addConnectionListener() {
        EMChatManager.getInstance().addConnectionListener(new MyConnectionListener());
    }

    private static boolean activityVisible;
    private static String id = "";

    public static String getId() {
        return id;
    }

    public static boolean isActivityVisible() {
        return activityVisible;
    }

    public static void activityResumed(String chatId) {
        activityVisible = true;
        id = chatId;
    }

    public static void activityPaused() {
        activityVisible = false;
        id = "";
    }

    private List<String> mNotList = new ArrayList<String>();

    public int getNotifyId(String id) {
        if (mNotList.contains(id)) {
            return mNotList.indexOf(id);
        }
        mNotList.add(id);
        return mNotList.size() - 1;
    }

    @Override
    public void onEvent(EMNotifierEvent event) {
        switch (event.getEvent()) {
            case EventNewMessage: {
                EMMessage message = (EMMessage) event.getData();
              /*  EMGroup group = EMGroupManager.getInstance().getGroup("");
                EMGroupManager.getInstance().createOrUpdateLocalGroup();*/
                try {
                    new Notifications(message.getStringAttribute("identify"),message);
                } catch (EaseMobException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}