package wxm.com.androiddesign.module;

/**
 * Created by zero on 2015/7/22.
 */
public class Notify {
    String mTitle;
    String mContent;
    String userName;
    String userPhoto;
    String mTime;
    String atyId;

    public String getmTitle() {
        return mTitle;
    }

    public void setmTitle(String mTitle) {
        this.mTitle = mTitle;
    }

    public String getmContent() {
        return mContent;
    }

    public void setmContent(String mContent) {
        this.mContent = mContent;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserPhoto() {
        return userPhoto;
    }

    public void setUserPhoto(String userPhoto) {
        this.userPhoto = userPhoto;
    }

    public String getmTime() {
        return mTime;
    }

    public void setmTime(String mTime) {
        this.mTime = mTime;
    }

    public Notify(String mTitle, String mContent, String userName, String userPhoto, String mTime) {
        this.mTitle = mTitle;
        this.mContent = mContent;
        this.userName = userName;
        this.userPhoto = userPhoto;

        this.mTime = mTime;
    }
}
