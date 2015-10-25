package wxm.com.androiddesign.adapter;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMConversation;
import com.easemob.chat.EMMessage;
import com.easemob.chat.TextMessageBody;
import com.easemob.exceptions.EaseMobException;
import com.squareup.picasso.Picasso;

import java.util.Arrays;
import java.util.Date;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Timer;

import butterknife.Bind;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import wxm.com.androiddesign.R;
import wxm.com.androiddesign.module.MyUser;
import wxm.com.androiddesign.ui.ChatActivity;
import wxm.com.androiddesign.utils.DateUtils;

/**
 * Created by Zero on 8/31/2015.
 */
public class ChatAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private static final String TAG="ChatAdapter";

    private static final int MESSAGE_TYPE_RECV_TXT = 0;
    private static final int MESSAGE_TYPE_SENT_TXT = 1;

    private static final int HANDLER_MESSAGE_REFRESH_LIST=0x1;
    private static final int HANDLER_MESSAGE_SELECT_LIST=0x2;

    private String userId;
    private String userIcon;
    private Activity activity;

    private EMConversation emConversation;
    EMMessage[] messages=null;

    private Context context;

    private Map<String,Timer> timers=new Hashtable<String,Timer>();

    public ChatAdapter(Context context, String userId ,String userIcon) {
        this.userId = userId;
        this.context = context;
        this.userIcon=userIcon;
        activity=(Activity)context;
        emConversation= EMChatManager.getInstance().getConversation(userId);
        Log.d("conversation","2"+emConversation.toString());

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(viewType==MESSAGE_TYPE_RECV_TXT){
            return new MsgReceiveViewHolder(LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.row_receive_msg,parent,false));
        }else if(viewType==MESSAGE_TYPE_SENT_TXT){
            return new MsgSendViewHolder(LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.row_send_msg,parent,false));
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        EMMessage message=messages[position];
        TextMessageBody textMessageBody=(TextMessageBody)message.getBody();
        if(holder instanceof MsgReceiveViewHolder){
            Picasso.with(activity).load(userIcon).into(((MsgReceiveViewHolder) holder).mPhoto);
            ((MsgReceiveViewHolder) holder).mContent.setText(textMessageBody.getMessage());
            ((MsgReceiveViewHolder) holder).mTime.setText(DateUtils.getTimestampString(
                    new Date(message.getMsgTime())));
        }if (holder instanceof MsgSendViewHolder){
            Picasso.with(activity).load(MyUser.userIcon).into(((MsgSendViewHolder) holder).mPhoto);
            ((MsgSendViewHolder) holder).mContent.setText(textMessageBody.getMessage());
            ((MsgSendViewHolder) holder).mTime.setText(DateUtils.getTimestampString(
                    new Date(message.getMsgTime())
            ));
        }
    }


    @Override
    public int getItemCount() {
        return messages==null?0:messages.length;
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    @Override
    public int getItemViewType(int position) {
        EMMessage message = messages[position];
        if (message == null) {
            return -1;
        }
        if (message.getType() == EMMessage.Type.TXT) {
//            if (message.getBooleanAttribute(Constant.MESSAGE_ATTR_IS_VOICE_CALL, false))
//                return message.direct == EMMessage.Direct.RECEIVE ? MESSAGE_TYPE_RECV_VOICE_CALL : MESSAGE_TYPE_SENT_VOICE_CALL;
//            else if (message.getBooleanAttribute(Constant.MESSAGE_ATTR_IS_VIDEO_CALL, false))
//                return message.direct == EMMessage.Direct.RECEIVE ? MESSAGE_TYPE_RECV_VIDEO_CALL : MESSAGE_TYPE_SENT_VIDEO_CALL;
//            else if(((DemoHXSDKHelper)HXSDKHelper.getInstance()).isRobotMenuMessage(message))
//                return message.direct == EMMessage.Direct.RECEIVE ? MESSAGE_TYPE_RECV_ROBOT_MENU : MESSAGE_TYPE_SENT_ROBOT_MENU;
//            else
            return message.direct == EMMessage.Direct.RECEIVE ? MESSAGE_TYPE_RECV_TXT : MESSAGE_TYPE_SENT_TXT;
        }
//        if (message.getType() == EMMessage.Type.IMAGE) {
//            return message.direct == EMMessage.Direct.RECEIVE ? MESSAGE_TYPE_RECV_IMAGE : MESSAGE_TYPE_SENT_IMAGE;
//
//        }
//        if (message.getType() == EMMessage.Type.LOCATION) {
//            return message.direct == EMMessage.Direct.RECEIVE ? MESSAGE_TYPE_RECV_LOCATION : MESSAGE_TYPE_SENT_LOCATION;
//        }
//        if (message.getType() == EMMessage.Type.VOICE) {
//            return message.direct == EMMessage.Direct.RECEIVE ? MESSAGE_TYPE_RECV_VOICE : MESSAGE_TYPE_SENT_VOICE;
//        }
//        if (message.getType() == EMMessage.Type.VIDEO) {
//            return message.direct == EMMessage.Direct.RECEIVE ? MESSAGE_TYPE_RECV_VIDEO : MESSAGE_TYPE_SENT_VIDEO;
//        }
//        if (message.getType() == EMMessage.Type.FILE) {
//            return message.direct == EMMessage.Direct.RECEIVE ? MESSAGE_TYPE_RECV_FILE : MESSAGE_TYPE_SENT_FILE;
//        }
        return -1;// invalid
    }

    Handler handler=new Handler(){
        private void refreshList(){
            List<EMMessage> list = emConversation.getAllMessages();
            /*for(int i = 0 ; i < list.size() ; i++){
                try {
                    if(list.get(i).getStringAttribute("identify").equals("notification")||
                            list.get(i).getStringAttribute("identify").equals("comment")){
                        list.remove(i);
                    }
                } catch (EaseMobException e) {
                    e.printStackTrace();
                }
            }*/
            Iterator<EMMessage> iterator = list.iterator();
            while (iterator.hasNext()){
                try {
                    EMMessage emMessage = iterator.next();
                    Log.d("identify",emMessage.getStringAttribute("identify"));
                    if(emMessage.getStringAttribute("identify").equals("notification")||
                            emMessage.getStringAttribute("identify").equals("comment")){
                        //iterator.remove();
                        Log.d("identify",emMessage.getStringAttribute("identify"));
                        iterator.remove();
                    }
                } catch (EaseMobException e) {
                    e.printStackTrace();
                }
            }
            messages=list.toArray(new EMMessage[emConversation.getAllMessages().size()]);
            Log.d(TAG, Arrays.toString(messages));
            for (int i=0;i<messages.length;i++){
                emConversation.getMessage(i);
            }
            notifyDataSetChanged();
        }
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case HANDLER_MESSAGE_REFRESH_LIST:
                    refreshList();
                    break;
                case HANDLER_MESSAGE_SELECT_LIST:
                    if(activity instanceof ChatActivity){
                        RecyclerView recyclerView=((ChatActivity)activity)
                                .getRecyclerView();
                        Log.d(TAG,""+messages.length);
                        if (messages.length>0){

                            recyclerView.scrollToPosition(messages.length-1);
                        }
                    }
                    break;
            }
        }
    };

    public void refresh(){
        if (handler.hasMessages(HANDLER_MESSAGE_REFRESH_LIST)){
            return;
        }
        Message msg=handler.obtainMessage(HANDLER_MESSAGE_REFRESH_LIST);
        handler.sendMessage(msg);
    }

    public void refreshSelectLast(){
        handler.sendMessage(handler.obtainMessage(HANDLER_MESSAGE_REFRESH_LIST));
        handler.sendMessage(handler.obtainMessage(HANDLER_MESSAGE_SELECT_LIST));
    }

    class MsgSendViewHolder extends RecyclerView.ViewHolder{
        @Bind(R.id.user_photo)
        CircleImageView mPhoto;
        @Bind(R.id.msg_content)
        TextView mContent;
        @Bind(R.id.msg_time)
        TextView mTime;
        public MsgSendViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
    class MsgReceiveViewHolder extends RecyclerView.ViewHolder{

        @Bind(R.id.user_photo)
        CircleImageView mPhoto;
        @Bind(R.id.msg_content)
        TextView mContent;
        @Bind(R.id.msg_time)
        TextView mTime;

        public MsgReceiveViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }

}
