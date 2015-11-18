package wxm.com.maca.jpush;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import cn.jpush.android.api.JPushInterface;
import wxm.com.maca.ui.NotificationActivity;

/**
 * Created by hd_chen on 2015/11/6.
 */
public class MyReceiver extends BroadcastReceiver {
    private NotificationManager nm;
    @Override
    public void onReceive(Context context, Intent intent) {
        if (null == nm) {
            nm = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        }

        Bundle bundle = intent.getExtras();
        //Logger.d(TAG, "onReceive - " + intent.getAction() + ", extras: " + AndroidUtil.printBundle(bundle));

        if (JPushInterface.ACTION_REGISTRATION_ID.equals(intent.getAction())) {
            //Logger.d(TAG, "JPush用户注册成功");

        } else if (JPushInterface.ACTION_MESSAGE_RECEIVED.equals(intent.getAction())) {
            //Logger.d(TAG, "接受到推送下来的自定义消息");

        } else if (JPushInterface.ACTION_NOTIFICATION_RECEIVED.equals(intent.getAction())) {
            //Logger.d(TAG, "接受到推送下来的通知");

            //receivingNotification(context,bundle);

        } else if (JPushInterface.ACTION_NOTIFICATION_OPENED.equals(intent.getAction())) {
           // Logger.d(TAG, "用户点击打开了通知");
            Log.d("JJJJ", "用户点开了通知");
            Intent i = new Intent(context, NotificationActivity.class);
            i.putExtras(bundle);
            i.putExtra("notification", true);
            //i.putExtra("easemobId", userId);
            i.putExtra("type", NotificationActivity.NOTIFY);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP );
            //i.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            context.startActivity(i);
        } else {
            //Logger.d(TAG, "Unhandled intent - " + intent.getAction());
        }
    }
}
