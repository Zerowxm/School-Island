package wxm.com.androiddesign.module;

/**
 * Created by hd_chen on 2015/11/23.
 */
public class GroupChatItem {
    String toGroupIcon;
    String toGroupName;
    String toCtyId;
    String toGroupId;
    String fromEasemobId;
    String sendTime;
    String msgContent;
    String newMsgCount;

    public GroupChatItem(String toGroupIcon, String toGroupName, String toCtyId,
                         String toGroupId, String fromEasemobId, String sendTime,
                         String msgContent, String newMsgCount) {
        this.toGroupIcon = toGroupIcon;
        this.toGroupName = toGroupName;
        this.toCtyId = toCtyId;
        this.toGroupId = toGroupId;
        this.fromEasemobId = fromEasemobId;
        this.sendTime = sendTime;
        this.msgContent = msgContent;
        this.newMsgCount = newMsgCount;
    }

    public String getToGroupId() {
        return toGroupId;
    }

    public void setToGroupId(String toGroupId) {
        this.toGroupId = toGroupId;
    }

    public String getToGroupIcon() {
        return toGroupIcon;
    }

    public void setToGroupIcon(String toGroupIcon) {
        this.toGroupIcon = toGroupIcon;
    }

    public String getToGroupName() {
        return toGroupName;
    }

    public void setToGroupName(String toGroupName) {
        this.toGroupName = toGroupName;
    }

    public String getToCtyId() {
        return toCtyId;
    }

    public void setToCtyId(String toCtyId) {
        this.toCtyId = toCtyId;
    }

    public String getFromEasemobId() {
        return fromEasemobId;
    }

    public void setFromEasemobId(String fromEasemobId) {
        this.fromEasemobId = fromEasemobId;
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
