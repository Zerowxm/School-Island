package wxm.com.maca.notification;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.util.Log;

import com.easemob.chat.EMChat;
import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMChatOptions;
import com.easemob.chat.EMMessage;
import com.easemob.chat.OnNotificationClickListener;
import com.easemob.chat.TextMessageBody;
import com.easemob.exceptions.EaseMobException;

import wxm.com.maca.ui.ChatActivity;
import wxm.com.maca.ui.MainActivity;
import wxm.com.maca.ui.MyApplication;
import wxm.com.maca.ui.NotificationActivity;
import wxm.com.maca.utils.ACache;

/**
 * Created by hd_chen on 2015/10/11.
 */
public class Notifications {
    String identify;
    EMMessage message;
    String userId;
    public Notifications(String type,EMMessage emMessage){
        this.identify = type;
        this.message = emMessage;
        userId = message.getFrom();
        notificate();
    }

    private void notificate(){
        try {
            Log.d("identifyfff",identify);
            if (identify.equals("chat")) {
                // if (userId.equals("notification")) {
                //if (!MyApplication.isActivityVisible() || MyApplication.isActivityVisible() && !MyApplication.getId().equals(userId)) {
                NotificationCompat.Builder mBuilder =
                        new NotificationCompat.Builder(MyApplication.applicationContext)
                                .setSmallIcon(MyApplication.applicationContext.getApplicationInfo().icon)
                                .setWhen(System.currentTimeMillis())
                                .setContentTitle(message.getStringAttribute("userName")+"（"+message.getStringAttribute("newMsgCount")+"条新信息）")
                                .setContentText(((TextMessageBody) message.getBody()).getMessage())
                                .setAutoCancel(true)
                                .setVibrate(new long[]{0, 200, 200, 200})
                                .setLights(Color.GREEN, 1000, 1000)
                                .setTicker(((TextMessageBody) message.getBody()).getMessage());
                Intent resultIntent = new Intent(MyApplication.applicationContext, NotificationActivity.class);
                resultIntent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                //resultIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK| Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
                resultIntent.putExtra("type",NotificationActivity.CHAT);
                resultIntent.putExtra("notification", true);
                resultIntent.putExtra("easemobId", userId);
                    /*resultIntent.setAction(Intent.ACTION_MAIN);
                    resultIntent.addCategory(Intent.CATEGORY_LAUNCHER);*/
                TaskStackBuilder stackBuilder = TaskStackBuilder.create(MyApplication.applicationContext);
                stackBuilder.addParentStack(MainActivity.class);
                stackBuilder.addNextIntent(resultIntent);
                PendingIntent resultPendingIntent =
                        stackBuilder.getPendingIntent(2,
                                PendingIntent.FLAG_UPDATE_CURRENT);
                    /*PendingIntent contentIntent = PendingIntent.getActivities(
                            this,
                            0,
                            makeIntentStack(this),
                            PendingIntent.FLAG_UPDATE_CURRENT);*/
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
                notification.flags = Notification.FLAG_SHOW_LIGHTS;
                NotificationManager mNotificationManager =
                        (NotificationManager) MyApplication.applicationContext.getSystemService(Context.NOTIFICATION_SERVICE);
                mNotificationManager.notify(Integer.parseInt(userId.substring(1)), notification);
            }else if(identify.equals("notification")){
                NotificationCompat.Builder mBuilder =
                        new NotificationCompat.Builder(MyApplication.applicationContext)
                                .setSmallIcon(MyApplication.applicationContext.getApplicationInfo().icon)
                                .setWhen(System.currentTimeMillis())
                                .setContentTitle(message.getStringAttribute("userName")+"给你发来通知")
                                .setContentText(((TextMessageBody) message.getBody()).getMessage())
                                .setAutoCancel(true)
                                .setVibrate(new long[]{0, 800, 800, 800})
                                .setLights(Color.BLUE, 1000, 1000);
                Intent resultIntent = new Intent(MyApplication.applicationContext, NotificationActivity.class);
                resultIntent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                resultIntent.putExtra("notification", true);
                resultIntent.putExtra("easemobId", userId);
                resultIntent.putExtra("type", NotificationActivity.NOTIFY);
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
            }else if(identify.equals("join")){
                NotificationCompat.Builder mBuilder =
                        new NotificationCompat.Builder(MyApplication.applicationContext)
                                .setSmallIcon(MyApplication.applicationContext.getApplicationInfo().icon)
                                .setWhen(System.currentTimeMillis())
                                .setContentTitle(message.getStringAttribute("atyName"))
                                .setContentText(((TextMessageBody) message.getBody()).getMessage())
                                .setAutoCancel(true)
                                .setVibrate(new long[]{0, 200, 200, 200})
                                .setLights(Color.BLUE, 1000, 1000);
                Intent resultIntent = new Intent(MyApplication.applicationContext, NotificationActivity.class);
                resultIntent.putExtra("type",NotificationActivity.NOTIFY);
                resultIntent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                resultIntent.putExtra("notification", true);
                resultIntent.putExtra("easemobId", userId);
                resultIntent.putExtra("type", NotificationActivity.NOTIFY);
                TaskStackBuilder stackBuilder = TaskStackBuilder.create(MyApplication.applicationContext);
                stackBuilder.addParentStack(MainActivity.class);
                stackBuilder.addNextIntent(resultIntent);
                PendingIntent resultPendingIntent =
                        stackBuilder.getPendingIntent(5,
                                PendingIntent.FLAG_UPDATE_CURRENT);
                mBuilder.setContentIntent(resultPendingIntent);
                Notification notification = mBuilder.build();
                notification.defaults = Notification.DEFAULT_SOUND;
                notification.flags = Notification.FLAG_SHOW_LIGHTS;
                NotificationManager mNotificationManager =
                        (NotificationManager) MyApplication.applicationContext.getSystemService(Context.NOTIFICATION_SERVICE);
            }else if (identify.equals("comment")){
                NotificationCompat.Builder mBuilder =
                        new NotificationCompat.Builder(MyApplication.applicationContext)
                                .setSmallIcon(MyApplication.applicationContext.getApplicationInfo().icon)
                                .setWhen(System.currentTimeMillis())
                                .setContentTitle(message.getStringAttribute("userName")+"评论了你")
                                .setContentText(((TextMessageBody) message.getBody()).getMessage())
                                .setAutoCancel(true)
                                .setVibrate(new long[]{0, 200, 200, 200})
                                .setLights(Color.BLUE, 1000, 1000)
                                .setTicker(((TextMessageBody) message.getBody()).getMessage());
                Intent resultIntent = new Intent(MyApplication.applicationContext, NotificationActivity.class);
                resultIntent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                resultIntent.putExtra("notification", true);
                resultIntent.putExtra("easemobId", userId);
                resultIntent.putExtra("type", NotificationActivity.COMMENT);
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
            }
        } catch (EaseMobException e) {
            e.printStackTrace();
        }
    }
    public static EMChatOptions options;
    public static boolean isInBackgroud = true;
    public static boolean isNoticeBySound = true;
    public static boolean isNodiceByVibrate = true;
    public static boolean isUseSpeaker = true;
    public static void initNotification(){
        ACache aCache = ACache.get(MyApplication.applicationContext);
        isInBackgroud = Boolean.parseBoolean(aCache.getAsString("isInBackgroud"));
        isNoticeBySound = Boolean.parseBoolean(aCache.getAsString("isNoticeBySound"));
        isNodiceByVibrate = Boolean.parseBoolean(aCache.getAsString("isNodiceByVibrate"));
        isUseSpeaker = Boolean.parseBoolean(aCache.getAsString("isUseSpeaker"));
        EMChat.getInstance().setAppInited();
        // 获取到EMChatOptions对象
        options = EMChatManager.getInstance().getChatOptions();
        options.setShowNotificationInBackgroud(isInBackgroud);
        options.setNoticeBySound(isNoticeBySound); //是否开启声音提醒
        options.setNoticedByVibrate(isNodiceByVibrate); //是否开启震动提醒
        options.setUseSpeaker(isUseSpeaker); //是否开启扬声器播放
        //设置notification点击listener
        options.setOnNotificationClickListener(new OnNotificationClickListener() {
            @Override
            public Intent onNotificationClick(EMMessage message) {
                Intent intent = new Intent(MyApplication.applicationContext, ChatActivity.class);
                EMMessage.ChatType chatType = message.getChatType();
                if (chatType == EMMessage.ChatType.Chat) { //单聊信息
                    intent.putExtra("easemobId", message.getFrom());
                    intent.putExtra("chatType", ChatActivity.CHAT);
                } else { //群聊信息
                    //message.getTo()为群聊id
                    intent.putExtra("easemobId", message.getTo());
                    intent.putExtra("chatType", ChatActivity.GROUP_CHAT);
                }
                return intent;
            }
        });
    }

    public static void setShowNotificationInBackgroud(boolean n){
        isInBackgroud = true;
        options.setShowNotificationInBackgroud(n);
        ACache aCache = ACache.get(MyApplication.applicationContext);
        aCache.put("isInBackgroud",n);
    }
    public static void setNoticeBySound(boolean n){
        isNoticeBySound = true;
        options.setNoticeBySound(n);
        ACache aCache = ACache.get(MyApplication.applicationContext);
        aCache.put("isNoticeBySound",n);
    }
    public static void setNoticedByVibrate(boolean n){
        isNodiceByVibrate = true;
        options.setNoticedByVibrate(n);
        ACache aCache = ACache.get(MyApplication.applicationContext);
        aCache.put("isNodiceByVibrate",n);
    }
    public static void setUseSpeaker(boolean n){
        isUseSpeaker = true;
        options.setUseSpeaker(n);
        ACache aCache = ACache.get(MyApplication.applicationContext);
        aCache.put("isUseSpeaker", n);
    }

}
