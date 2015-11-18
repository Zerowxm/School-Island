package wxm.com.maca.utils;

import android.content.Context;

import com.easemob.EMEventListener;
import com.easemob.EMNotifierEvent;
import com.easemob.chat.EMMessage;

import java.util.List;

import wxm.com.maca.ui.MyApplication;

/**
 * Created by Zero on 9/11/2015.
 */
public class HXSDKHelper {
    private static HXSDKHelper hxsdkHelper=null;
    private static HXNotifier notifier=null;
    protected EMEventListener emEventListener;
    private Context context;

    public HXSDKHelper(Context context) {
        this.context = context;
        notifier=new HXNotifier();
        notifier.init(context);
    }


    public HXNotifier getNotifier(){
        return notifier;
    }
    public void initEventListener(){
        emEventListener=new EMEventListener() {
            @Override
            public void onEvent(EMNotifierEvent emNotifierEvent) {
                EMMessage message=null;
                if (emNotifierEvent.getData() instanceof EMMessage){
                    message=(EMMessage)emNotifierEvent.getData();
                }
                switch (emNotifierEvent.getEvent()){
                    case EventNewMessage:
                        if (!MyApplication.isActivityVisible()){
                            notifier.onNewMsg(message);
                        }
                        break;
                    case EventOfflineMessage:
                        if (!MyApplication.isActivityVisible()){
                            List<EMMessage> messages=(List<EMMessage>)emNotifierEvent.getData();
                            notifier.onNewMesg(messages);
                        }
                }
            }
        };
    }
}
