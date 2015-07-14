package wxm.com.androiddesign.module;

/**
 * Created by hdchen on 2015/7/1.
 */
public class CommentData{
    public String action;
    public String userId;
    public String time;
    public String comment;

    public CommentData(String action, String userId, String time, String comment) {
        this.action = action;
        this.userId = userId;
        this.time = time;
        this.comment = comment;
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

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
