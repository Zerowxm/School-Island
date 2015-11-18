package wxm.com.maca.module;

/**
 * Created by hdchen on 2015/7/1.
 */
public class CommentItem {
    public String action;
    public String userId;
    public String userName;
    public String userIcon;
    public String cmtTime;
    public String cmtContent;
    public String atyId;
    public String photoId;

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserIcon() {
        return userIcon;
    }

    public void setUserIcon(String userIcon) {
        this.userIcon = userIcon;
    }

    public String getCmtTime() {
        return cmtTime;
    }

    public void setCmtTime(String cmtTime) {
        this.cmtTime = cmtTime;
    }

    public String getCmtContent() {
        return cmtContent;
    }

    public void setCmtContent(String cmtContent) {
        this.cmtContent = cmtContent;
    }

    public String getAtyId() {
        return atyId;
    }

    public void setAtyId(String atyId) {
        this.atyId = atyId;
    }

    public String getPhotoId() {
        return photoId;
    }

    public void setPhotoId(String atyPhoto) {
        this.photoId = atyPhoto;
    }
}
