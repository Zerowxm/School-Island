package wxm.com.androiddesign.broadcastreceive;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMConversation;
import com.easemob.chat.EMMessage;

/**
 * Created by Zero on 9/1/2015.
 */
public class NewMessageBroadCastReceiver extends BroadcastReceiver {

    private static final String TAG="BroadCast";

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("conversation","onReceive");
        //注册广播
        abortBroadcast();

        //消息id，sdk生成
        String msgId=intent.getStringExtra("msgid");
        //发送方
        String msgFrom=intent.getStringExtra("from");

        int msgType=intent.getIntExtra("type",0);

        Log.d(TAG,"new message id:"+msgId+" from:"+ msgFrom+" type:"+msgType);
        //
        EMMessage message= EMChatManager.getInstance().getMessage(msgId);
        EMConversation conversation=EMChatManager.getInstance().getConversation(msgFrom);
        Log.d("conversation","3"+conversation.toString());
        if (!msgFrom.equals(msgFrom))
            return;
    }
}
