package wxm.com.androiddesign.notification;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.util.Log;

import com.easemob.chat.EMMessage;
import com.easemob.chat.TextMessageBody;
import com.easemob.exceptions.EaseMobException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import wxm.com.androiddesign.ui.ChatActivity;
import wxm.com.androiddesign.ui.MainActivity;
import wxm.com.androiddesign.ui.MyApplication;
import wxm.com.androiddesign.ui.NotificationActivity;

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
                                .setContentTitle(message.getStringAttribute("userName")+"（"+ message.getStringAttribute("newMsgCount")+"条新信息）")
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
                if(!MyApplication.easemobId.containsKey(userId))
                    MyApplication.easemobId.put(userId,(int) System.currentTimeMillis());

                Log.d("identifyfff",MyApplication.easemobId.get(userId)+"");
                mNotificationManager.notify(MyApplication.easemobId.get(userId), notification);
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
                if(!MyApplication.easemobId.containsKey(userId))
                    MyApplication.easemobId.put(userId,(int) System.currentTimeMillis());
                mNotificationManager.notify(MyApplication.easemobId.get(userId), notification);
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
                if(!MyApplication.easemobId.containsKey(userId))
                    MyApplication.easemobId.put(userId,(int) System.currentTimeMillis());
                mNotificationManager.notify(MyApplication.easemobId.get(userId), notification);
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
                if(!MyApplication.easemobId.containsKey(userId))
                    MyApplication.easemobId.put(userId,(int) System.currentTimeMillis());
                mNotificationManager.notify(MyApplication.easemobId.get(userId), notification);
            }
        } catch (EaseMobException e) {
            e.printStackTrace();
        }
    }

}
