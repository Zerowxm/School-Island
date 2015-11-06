package wxm.com.androiddesign.ui;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import com.easemob.EMEventListener;
import com.easemob.EMNotifierEvent;
import com.easemob.chat.EMChat;
import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMMessage;
import com.easemob.exceptions.EaseMobException;

import java.util.ArrayList;
import java.util.List;

import cn.jpush.android.api.JPushInterface;
import cn.sharesdk.framework.ShareSDK;
import wxm.com.androiddesign.broadcastreceive.NewMessageBroadCastReceiver;
import wxm.com.androiddesign.listener.MyConnectionListener;
import wxm.com.androiddesign.notification.Notifications;
import wxm.com.androiddesign.utils.CrashHandler;

/**
 * Created by Administrator on 2015/7/3.
 */
public class MyApplication extends Application implements EMEventListener {
    public static Context applicationContext;
    private static MyApplication instance;

    private static final String TAG = "MyApplication";
    NewMessageBroadCastReceiver receiver = new NewMessageBroadCastReceiver();

    @Override
    public void onCreate() {
        super.onCreate();
        applicationContext = this;
        instance = this;
        CrashHandler crashHandler = CrashHandler.getInstance();
        crashHandler.init(getApplicationContext());
        EMChat.getInstance().setAutoLogin(false);
        //SDKInitializer.initialize(applicationContext);
        EMChat.getInstance().init(applicationContext);
        EMChat.getInstance().setDebugMode(true);
        Log.d(TAG, "ApplicationonCreate");
        initSDK(this);
        addConnectionListener();
        //throw new NullPointerException();
//        Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
//            public void uncaughtException(Thread thread, Throwable ex) {
//                Log.d("uncaughtException", "uncaughtException");
////                Intent intent = new Intent(getApplicationContext(),MainActivity.class);
////                startActivity(intent);
////                Toast.makeText(getApplicationContext(),"数据访问失败，请检查网络状态",Toast.LENGTH_SHORT).show();
//                final Intent intent = getPackageManager().getLaunchIntentForPackage(getPackageName());
//                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                startActivity(intent);
//            }
//        });

        JPushInterface.setDebugMode(true);
        JPushInterface.init(this);
    }

    public static MyApplication getInstance() {
        return instance;
    }

    private void initSDK(Context context) {
        //初始化sharesdk,具体集成步骤请看文档：
        ShareSDK.initSDK(context);
//        IntentFilter filter = new IntentFilter(EMChatManager.getInstance().getNewMessageBroadcastAction());
//        filter.setPriority(3);
//        registerReceiver(receiver, filter);
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