package wxm.com.androiddesign.module;

/**
 * Created by zero on 2015/7/22.
 */
public class Notify {
    String atyName;
    String msgContent;
    String userName;
    String userIcon;
    String releaseTime;
    String atyId;

    public String getAtyId() {
        return atyId;
    }

    public void setAtyId(String atyId) {
        this.atyId = atyId;
    }

    public String getmTitle() {
        return atyName;
    }

    public void setmTitle(String mTitle) {
        this.atyName = mTitle;
    }

    public String getmContent() {
        return msgContent;
    }

    public void setmContent(String mContent) {
        this.msgContent = mContent;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserPhoto() {
        return userIcon;
    }

    public void setUserPhoto(String userPhoto) {
        this.userIcon = userPhoto;
    }

    public String getmTime() {
        return releaseTime;
    }

    public void setmTime(String mTime) {
        this.releaseTime = mTime;
    }

    public Notify(String mTitle, String mContent, String userName, String userPhoto, String mTime) {
        this.atyName = mTitle;
        this.msgContent = mContent;
        this.userName = userName;
        this.userIcon = userPhoto;

        this.releaseTime = mTime;
    }
}
