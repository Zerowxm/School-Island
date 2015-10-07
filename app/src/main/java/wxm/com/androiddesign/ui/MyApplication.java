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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.sharesdk.framework.ShareSDK;
import wxm.com.androiddesign.broadcastreceive.NewMessageBroadCastReceiver;
import wxm.com.androiddesign.listener.MyConnectionListener;
import wxm.com.androiddesign.utils.HXSDKHelper;
import wxm.com.androiddesign.utils.MyUtils;

/**
 * Created by Administrator on 2015/7/3.
 */
public class MyApplication extends Application implements EMEventListener {
    public static Context applicationContext;
    private static MyApplication instance;
    //public static HXSDKHelper hxsdkHelper;

    public static Map<String,Integer> message = new HashMap<>();
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
                String userId = null;
                userId = message.getFrom();
                Log.d("activityBD", "" + MyApplication.isActivityVisible() + userId + "/" + MyApplication.getId());
                if(this.message.containsKey(userId)){
                    this.message.put(userId,this.message.get(userId)+1);
                }else{
                    this.message.put(userId,1);
                }
                if (!userId.equals("admin")) {
                    //if (!MyApplication.isActivityVisible() || MyApplication.isActivityVisible() && !MyApplication.getId().equals(userId)) {
                    NotificationCompat.Builder mBuilder =
                            new NotificationCompat.Builder(this)
                                    .setSmallIcon(getApplicationContext().getApplicationInfo().icon)
                                    .setWhen(System.currentTimeMillis())
                                    .setContentTitle(userId+"给你发来"+this.message.get(userId)+"条信息")
                                    .setContentText(((TextMessageBody) message.getBody()).getMessage())
                                    .setAutoCancel(true)
                                    .setVibrate(new long[]{0, 200, 200, 200})
                                    .setLights(Color.BLUE, 1000, 1000)
                                    .setTicker(((TextMessageBody) message.getBody()).getMessage());
                    Intent resultIntent = new Intent(this, ChatActivity.class);
                    resultIntent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    //resultIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK| Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
                    resultIntent.putExtra("notification", true);
                    resultIntent.putExtra("easemobId", userId);
                /*resultIntent.setAction(Intent.ACTION_MAIN);
                resultIntent.addCategory(Intent.CATEGORY_LAUNCHER);*/
                    TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
                    stackBuilder.addParentStack(MainActivity.class);
                    stackBuilder.addNextIntent(resultIntent);
                    PendingIntent resultPendingIntent =
                            stackBuilder.getPendingIntent(0,
                                    PendingIntent.FLAG_UPDATE_CURRENT);

                /*PendingIntent contentIntent = PendingIntent.getActivities(
                        this,
                        0,
                        makeIntentStack(this),
                        PendingIntent.FLAG_UPDATE_CURRENT);
*/
                    mBuilder.setContentIntent(resultPendingIntent);

//                Intent notificationIntent = new Intent(this, ChatActivity.class);
//                notificationIntent.setAction(Intent.ACTION_MAIN);
//                notificationIntent.addCategory(Intent.CATEGORY_LAUNCHER);
//                PendingIntent contentIntent = PendingIntent.getActivity(
//                        applicationContext, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
                    //mBuilder.setContentIntent(contentIntent);
                    Notification notification = mBuilder.build();
                    // notification.flags |= Notification.DEFAULT_ALL;
                    notification.defaults = Notification.DEFAULT_SOUND;
                    NotificationManager mNotificationManager =
                            (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                    if(!this.easemobId.containsKey(userId))
                        this.easemobId.put(userId,(int) System.currentTimeMillis());
                    mNotificationManager.notify(this.easemobId.get(userId), notification);
                }else{
                    //if (!MyApplication.isActivityVisible() || MyApplication.isActivityVisible() && !MyApplication.getId().equals(userId)) {
                    NotificationCompat.Builder mBuilder =
                            new NotificationCompat.Builder(this)
                                    .setSmallIcon(getApplicationContext().getApplicationInfo().icon)
                                    .setWhen(System.currentTimeMillis())
                                    .setContentTitle(userId+"给你发来"+this.message.get(userId)+"条通知")
                                    .setContentText("notification")
                                    .setAutoCancel(true)
                                    .setVibrate(new long[]{0, 800, 800, 800})
                                    .setLights(Color.BLUE, 1000, 1000);
                    Intent resultIntent = new Intent(this, ChatActivity.class);
                    resultIntent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    //resultIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK| Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
                    resultIntent.putExtra("notification", true);
                    resultIntent.putExtra("easemobId", userId);
                /*resultIntent.setAction(Intent.ACTION_MAIN);
                resultIntent.addCategory(Intent.CATEGORY_LAUNCHER);*/
                    TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
                    stackBuilder.addParentStack(MainActivity.class);
                    stackBuilder.addNextIntent(resultIntent);
                    PendingIntent resultPendingIntent =
                            stackBuilder.getPendingIntent(0,
                                    PendingIntent.FLAG_UPDATE_CURRENT);

                /*PendingIntent contentIntent = PendingIntent.getActivities(
                        this,
                        0,
                        makeIntentStack(this),
                        PendingIntent.FLAG_UPDATE_CURRENT);
*/
                    mBuilder.setContentIntent(resultPendingIntent);

//                Intent notificationIntent = new Intent(this, ChatActivity.class);
//                notificationIntent.setAction(Intent.ACTION_MAIN);
//                notificationIntent.addCategory(Intent.CATEGORY_LAUNCHER);
//                PendingIntent contentIntent = PendingIntent.getActivity(
//                        applicationContext, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
                    //mBuilder.setContentIntent(contentIntent);
                    Notification notification = mBuilder.build();
                    // notification.flags |= Notification.DEFAULT_ALL;
                    notification.defaults = Notification.DEFAULT_SOUND;
                    NotificationManager mNotificationManager =
                            (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                    if(!this.easemobId.containsKey(userId))
                        this.easemobId.put(userId,(int) System.currentTimeMillis());
                     mNotificationManager.notify(this.easemobId.get(userId), notification);
                }
            }
        }
    }

    Intent[] makeIntentStack(Context context) {
        Intent[] intents = new Intent[2];
        intents[0] = Intent.makeRestartActivityTask(new ComponentName(context, wxm.com.androiddesign.ui.MyApplication.class));
        intents[1] = new Intent(context, wxm.com.androiddesign.ui.ChatActivity.class);
        return intents;
    }
}
//}


//else {
//        if (!MyApplication.isActivityVisible() || MyApplication.isActivityVisible() && !MyApplication.getId().equals(userId)) {
//        NotificationCompat.Builder mBuilder =
//        new NotificationCompat.Builder(this)
//        .setSmallIcon(getApplicationContext().getApplicationInfo().icon)
//        .setWhen(System.currentTimeMillis())
//        .setContentTitle("有新消息了!")
//        .setContentText("A+").setAutoCancel(true)
//        .setVibrate(new long[]{0, 800, 800, 800})
//        .setLights(Color.BLUE, 1000, 1000);
//        Intent resultIntent = new Intent(this, ChatActivity.class);
//        resultIntent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
//        resultIntent.putExtra("SystemNotification", true);
//        //resultIntent.putExtra("easemobId", userId);
//        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
//        stackBuilder.addParentStack(MainActivity.class);
//        stackBuilder.addNextIntent(resultIntent);
//        PendingIntent resultPendingIntent =
//        stackBuilder.getPendingIntent(0,
//        PendingIntent.FLAG_UPDATE_CURRENT);
//        mBuilder.setContentIntent(resultPendingIntent);
//        Notification notification = mBuilder.build();
//        notification.defaults = Notification.DEFAULT_SOUND;
//        NotificationManager mNotificationManager =
//        (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
//        mNotificationManager.notify(Integer.parseInt(userId.substring(1)), notification);
//        }