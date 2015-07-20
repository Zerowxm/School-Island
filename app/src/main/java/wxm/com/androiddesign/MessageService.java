package wxm.com.androiddesign;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;

import org.json.JSONObject;

import wxm.com.androiddesign.ui.MainActivity;


/**
 * Created by Curl on 2015/7/16.
 */
public class MessageService extends Service {
    private String TAG = "CJ";
    private String userId;
    //获取消息线程
    private MessageThread messageThread = null;
    //通知栏消息ID
    private int messageNotificationID = -1;
    //请求获取消息

    public void onCreate() {
        super.onCreate();
    }

    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        //开启线程
        messageThread = new MessageThread();
        messageThread.isRunning = true;
        messageThread.start();
        userId = intent.getStringExtra("userId");
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        messageThread.isRunning = false;
        super.onDestroy();
    }

    /**
     * 从服务器端获取消息
     */
    class MessageThread extends Thread {
        //运行状态，下一步骤有大用
        public boolean isRunning = true;

        public void run() {
            while (isRunning) {
                try {
                    //休息yihui
                    Thread.sleep(60000);
                    //获取服务器消息
                    String serverMessage = getServerMessage();
                    if (serverMessage != null && !"".equals(serverMessage)) {
                        //更新通知栏
                        sendMessage(serverMessage + messageNotificationID, messageNotificationID);
                        messageNotificationID++;
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    void sendMessage(String text, int mid) {
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.miao)
                        .setContentTitle("My notification")
                        .setContentText(text);

        // Creates an explicit intent for an Activity in your app
        Intent resultIntent = new Intent(this, MainActivity.class);

        // The stack builder object will contain an artificial back stack for the
        // started Activity.
        // This ensures that navigating backward from the Activity leads out of
        // your application to the Home screen.
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        // Adds the back stack for the Intent (but not the Intent itself)
        stackBuilder.addParentStack(MainActivity.class);
        // Adds the Intent that starts the Activity to the top of the stack
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
        mBuilder.setContentIntent(resultPendingIntent);

        NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        // mId allows you to update the notification later on.
        mNotificationManager.notify(mid, mBuilder.build());
    }

    /**
     * 这里以此方法为服务器Demo，仅作示例
     *
     * @return 返回服务器要推送的消息，否则如果为空的话，不推送
     */
    public String getServerMessage() {
        JSONObject json = new JSONObject();
        try {
            json.put("action", "message");
            //todo
            json.put("userId", userId);
        } catch (Exception e) {
            return null;
        }

        String result = new FetchItems().fetchitems(null, json.toString());
        try {
            JSONObject jsonResult = new JSONObject(result);
            int id = jsonResult.getInt("messageID");
            if(id == messageNotificationID)
                return null;
            else
            messageNotificationID = id;
            String message = jsonResult.getString("message");
            return message;
        }
        catch (Exception e) {
            return null;
        }

    }
}
