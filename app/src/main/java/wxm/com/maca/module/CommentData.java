package wxm.com.maca.module;

/**
 * Created by hdchen on 2015/7/1.
 */
public class CommentData {
    public String action;
    public String userId;
    public String userName;
    public String userIcon;
    public String cmtTime;
    public String cmtContent;
    public String atyId;

    @Override
    public String toString() {
        return "CommentData{" +
                "action='" + action + '\'' +
                ", userId='" + userId + '\'' +
                ", userName='" + userName + '\'' +
                ", userIcon='" + userIcon + '\'' +
                ", cmtTime='" + cmtTime + '\'' +
                ", cmtContent='" + cmtContent + '\'' +
                ", atyId='" + atyId + '\'' +
                '}';
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

    public CommentData(String action, String userId, String atyId, String time, String comment) {
        this.action = action;
        this.userId = userId;
        this.cmtTime = time;
        this.atyId = atyId;
        this.cmtContent = comment;
    }

    public String getAtyId() {
        return atyId;
    }

    public void setAtyId(String atyId) {
        this.atyId = atyId;
    }

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

    public String getTime() {
        return cmtTime;
    }

    public void setTime(String time) {
        this.cmtTime = time;
    }

    public String getComment() {
        return cmtContent;
    }

    public void setComment(String comment) {
        this.cmtContent = comment;
    }
}
