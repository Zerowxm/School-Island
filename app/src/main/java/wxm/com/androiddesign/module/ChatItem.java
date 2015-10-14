package wxm.com.androiddesign.module;

/**
 * Created by Zero on 10/6/2015.
 */
public class ChatItem {
    String fromUserIcon;
    String fromUserName;
    String fromUserId;
    String sendTime;
    String msgContent;

    public ChatItem(String userPhoto, String userName, String userId, String mTime, String mMessage) {
        this.fromUserIcon = userPhoto;
        this.fromUserName = userName;
        this.fromUserId = userId;
        this.sendTime = mTime;
        this.msgContent = mMessage;
    }

    public String getUserPhoto() {
        return fromUserIcon;
    }

    public void setUserPhoto(String userPhoto) {
        this.fromUserIcon = userPhoto;
    }

    public String getUserName() {
        return fromUserName;
    }

    public void setUserName(String userName) {
        this.fromUserName = userName;
    }

    public String getUserId() {
        return fromUserId;
    }

    public void setUserId(String userId) {
        this.fromUserId = userId;
    }

    public String getmTime() {
        return sendTime;
    }

    public void setmTime(String mTime) {
        this.sendTime = mTime;
    }

    public String getmMessage() {
        return msgContent;
    }

    public void setmMessage(String mMessage) {
        this.msgContent = mMessage;
    }
}
