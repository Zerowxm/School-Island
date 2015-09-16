package wxm.com.androiddesign.ui;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;

import com.baidu.mapapi.SDKInitializer;
import com.easemob.chat.EMChat;
import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMChatOptions;
import com.easemob.chat.EMMessage;
import com.easemob.chat.OnMessageNotifyListener;
import com.easemob.chat.OnNotificationClickListener;

import cn.sharesdk.framework.ShareSDK;
import wxm.com.androiddesign.broadcastreceive.NewMessageBroadCastReceiver;
import wxm.com.androiddesign.utils.HXSDKHelper;
import wxm.com.androiddesign.utils.MyUtils;

/**
 * Created by Administrator on 2015/7/3.
 */
public class MyApplication extends Application {
    public static Context applicationContext;
    private static MyApplication instance;
    //public static HXSDKHelper hxsdkHelper;

    private static final String TAG="MyApplication";
    NewMessageBroadCastReceiver receiver=new NewMessageBroadCastReceiver();;
    @Override
    public void onCreate() {
        super.onCreate();
        applicationContext=this;
        instance=this;
        SDKInitializer.initialize(applicationContext);
        EMChat.getInstance().init(applicationContext);
        EMChat.getInstance().setDebugMode(true);
        Log.d(TAG, "onCreate");
        initSDK(this);
        //hxsdkHelper=new HXSDKHelper(applicationContext);
        //hxsdkHelper.initEventListener();
    }

    public static MyApplication getInstance(){
        return instance;
    }

//    public static void LoginHX(){
//        MyUtils.Login(getInstance());
//    }

    private void initSDK(Context context) {
        //初始化sharesdk,具体集成步骤请看文档：
        //http://wiki.mob.com/Android_%E5%BF%AB%E9%80%9F%E9%9B%86%E6%88%90%E6%8C%87%E5%8D%97
        ShareSDK.initSDK(context);
        IntentFilter filter = new IntentFilter(EMChatManager.getInstance().getNewMessageBroadcastAction());
        filter.setPriority(3);
        registerReceiver(receiver, filter);
        EMChat.getInstance().setAppInited();

        EMChatOptions options=EMChatManager.getInstance().getChatOptions();

        options.setNotificationEnable(true);
        options.setShowNotificationInBackgroud(true);
        options.setOnNotificationClickListener(new OnNotificationClickListener() {
            @Override
            public Intent onNotificationClick(EMMessage emMessage) {
                Intent intent=new Intent(applicationContext,ChatActivity.class);
                intent.putExtra("toChatUserId",emMessage.getFrom());
                return intent;
            }
        });
//        options.setNotifyText(new OnMessageNotifyListener() {
//            @Override
//            public String onNewMessageNotify(EMMessage emMessage) {
//                return null;
//            }
//
//            @Override
//            public String onLatestMessageNotify(EMMessage emMessage, int i, int i1) {
//                return null;
//            }
//
//            @Override
//            public String onSetNotificationTitle(EMMessage emMessage) {
//                return null;
//            }
//
//            @Override
//            public int onSetSmallIcon(EMMessage emMessage) {
//                return 0;
//            }
//        });
    }



    private static boolean activityVisible;
    public static boolean isActivityVisible(){
        return activityVisible;
    }
    public static void activityResumed(){
        activityVisible=true;
    }
    public static void activityPaused(){
        activityVisible=false;
    }
}
