package wxm.com.androiddesign.services;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import wxm.com.androiddesign.R;
import wxm.com.androiddesign.module.MyUser;
import wxm.com.androiddesign.ui.MainActivity;


/**
 * Created by Curl on 2015/7/16.
 */
public class MessageService extends Service {
    private String TAG = "CJ";
    private MessageThread messageThread = null;
    private int messageNotificationID = 1000;
    private String userId;
    private ArrayList<Message> messagesList;

    public void onCreate() {
        super.onCreate();
        Log.i("CJ", "MessageService create");
        messageThread = new MessageThread();
        messageThread.isRunning = true;
        messageThread.start();
    }

    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        userId = MyUser.userId;
        Log.i("CJ", "onStartCommand " + userId);
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        messageThread.isRunning = false;
        super.onDestroy();
    }


    class MessageThread extends Thread {
        public boolean isRunning = true;

        public void run() {
            while (isRunning) {
                try {
                    Thread.sleep(6000);
                    messagesList = getServerMessage();
                    if (messagesList != null) {
                        for (Message message : messagesList) {
                            sendMessage(message, messageNotificationID);
                            messageNotificationID++;
                        }
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    void sendMessage(Message message, int mid) {

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.ic_discuss)
                        .setContentTitle(message.getTitle())
                        .setContentText(message.getContent());

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
        // mId allows you to update the notification later on
        mNotificationManager.notify(mid, mBuilder.build());
    }


    public ArrayList<Message> getServerMessage() {
        JSONObject json = new JSONObject();
        try {
            json.put("action", "message");
            json.put("userId", userId);
        } catch (Exception e) {
            return null;
        }

        String result = new FetchItems().fetchitems("http://101.200.191.149:8080/bootstrapRepository/ClientPostServlet"
                , json.toString());
        try {
            JSONArray jsonArray = new JSONArray(result);
            ArrayList<Message> netWorkmessagesList = new ArrayList<Message>();
            for (int i = 0; i < jsonArray.length(); i++) {
                Message message = new Message();
                message.setTime(jsonArray.getJSONObject(i).getString("msgTime"));
                message.setContent(jsonArray.getJSONObject(i).getString("msgContent"));
                message.setTitle(jsonArray.getJSONObject(i).getString("msgId"));
                message.setType(jsonArray.getJSONObject(i).getString("msgType"));
                netWorkmessagesList.add(message);
            }
            return netWorkmessagesList;
        } catch (Exception e) {
            return null;
        }
    }
}
