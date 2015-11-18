package wxm.com.maca.module;

/**
 * Created by zero on 2015/7/22.
 */
public class Message {
    String msgTime;
    String msgContent;
    String msgId;
    String msgType;

    public Message(String msgTime, String msgContent, String msgId, String msgType) {
        this.msgTime = msgTime;
        this.msgContent = msgContent;
        this.msgId = msgId;
        this.msgType = msgType;
    }

    @Override
    public String toString() {
        return "Message{" +
                "msgTime='" + msgTime + '\'' +
                ", msgContent='" + msgContent + '\'' +
                ", msgId='" + msgId + '\'' +
                ", msgType='" + msgType + '\'' +
                '}';
    }

    public String getMsgTime() {
        return msgTime;
    }

    public void setMsgTime(String msgTime) {
        this.msgTime = msgTime;
    }

    public String getMsgContent() {
        return msgContent;
    }

    public void setMsgContent(String msgContent) {
        this.msgContent = msgContent;
    }

    public String getMsgId() {
        return msgId;
    }

    public void setMsgId(String msgId) {
        this.msgId = msgId;
    }

    public String getMsgType() {
        return msgType;
    }

    public void setMsgType(String msgType) {
        this.msgType = msgType;
    }
}
