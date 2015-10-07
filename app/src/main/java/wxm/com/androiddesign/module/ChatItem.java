package wxm.com.androiddesign.module;

/**
 * Created by Zero on 10/6/2015.
 */
public class ChatItem {
    String userPhoto;
    String userName;
    String userId;
    String mTime;
    String mMessage;

    public ChatItem(String userPhoto, String userName, String userId, String mTime, String mMessage) {
        this.userPhoto = userPhoto;
        this.userName = userName;
        this.userId = userId;
        this.mTime = mTime;
        this.mMessage = mMessage;
    }

    public String getUserPhoto() {
        return userPhoto;
    }

    public void setUserPhoto(String userPhoto) {
        this.userPhoto = userPhoto;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getmTime() {
        return mTime;
    }

    public void setmTime(String mTime) {
        this.mTime = mTime;
    }

    public String getmMessage() {
        return mMessage;
    }

    public void setmMessage(String mMessage) {
        this.mMessage = mMessage;
    }
}
