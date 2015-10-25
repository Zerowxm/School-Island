package wxm.com.androiddesign.module;

/**
 * Created by Zero on 10/6/2015.
 */
public class ChatItem {
    String fromUserIcon;
    String fromUserName;
    String fromUserId;
    String fromEasemobId;
    String sendTime;
    String msgContent;
    String newMsgCount;

    public ChatItem(String userPhoto, String userName, String userId, String mTime, String mMessage) {
        this.fromUserIcon = userPhoto;
        this.fromUserName = userName;
        this.fromUserId = userId;
        this.sendTime = mTime;
        this.msgContent = mMessage;
    }

    public ChatItem(String fromUserIcon, String fromUserName, String fromUserId, String sendTime, String msgContent, String newMsgCount) {
        this.fromUserIcon = fromUserIcon;
        this.fromUserName = fromUserName;
        this.fromUserId = fromUserId;
        this.sendTime = sendTime;
        this.msgContent = msgContent;
        this.newMsgCount = newMsgCount;
    }

    public String getFromEasemobId() {
        return fromEasemobId;
    }

    public void setFromEasemobId(String fromEasemobId) {
        this.fromEasemobId = fromEasemobId;
    }

    public String getFromUserIcon() {
        return fromUserIcon;
    }

    public void setFromUserIcon(String fromUserIcon) {
        this.fromUserIcon = fromUserIcon;
    }

    public String getFromUserName() {
        return fromUserName;
    }

    public void setFromUserName(String fromUserName) {
        this.fromUserName = fromUserName;
    }

    public String getFromUserId() {
        return fromUserId;
    }

    public void setFromUserId(String fromUserId) {
        this.fromUserId = fromUserId;
    }

    public String getSendTime() {
        return sendTime;
    }

    public void setSendTime(String sendTime) {
        this.sendTime = sendTime;
    }

    public String getMsgContent() {
        return msgContent;
    }

    public void setMsgContent(String msgContent) {
        this.msgContent = msgContent;
    }

    public String getNewMsgCount() {
        return newMsgCount;
    }

    public void setNewMsgCount(String newMsgCount) {
        this.newMsgCount = newMsgCount;
    }
}
