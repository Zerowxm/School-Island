package wxm.com.androiddesign.module;

/**
 * Created by zero on 2015/7/22.
 */
public class Notify {
    String notiTitle;
    String msgContent;
    String releaseTime;
    String atyId;
    String notiType;

    public Notify(String title, String msgContent, String releaseTime, String atyId, String type) {
        notiTitle = title;
        this.msgContent = msgContent;
        this.releaseTime = releaseTime;
        this.atyId = atyId;
        this.notiType = type;
    }

    public String getTitle() {
        return notiTitle;
    }

    public void setTitle(String title) {
        notiTitle = title;
    }

    public String getMsgContent() {
        return msgContent;
    }

    public void setMsgContent(String msgContent) {
        this.msgContent = msgContent;
    }

    public String getReleaseTime() {
        return releaseTime;
    }

    public void setReleaseTime(String releaseTime) {
        this.releaseTime = releaseTime;
    }

    public String getAtyId() {
        return atyId;
    }

    public void setAtyId(String atyId) {
        this.atyId = atyId;
    }

    public String getType() {
        return notiType;
    }

    public void setType(String type) {
        this.notiType = type;
    }
}
