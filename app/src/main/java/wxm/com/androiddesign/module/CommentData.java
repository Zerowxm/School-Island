package wxm.com.androiddesign.module;

/**
 * Created by hdchen on 2015/7/1.
 */
public class CommentData extends User{
    User user;
    public String time;
    public String comment;

    public CommentData(String time,String comment) {
        this.time=time;
        this.comment = comment;
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
