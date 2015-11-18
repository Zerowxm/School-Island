package wxm.com.maca.services;

/**
 * Created by Curl on 2015/7/22.
 */
public class Message {
    private String content;
    private String Time;
    private String Title;
    private String Type;

    public String getType() {
        return Type;
    }

    public void setType(String type) {
        Type = type;
    }

    public Message() {
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public Message(String content, String time) {
        this.content = content;
        this.Time = time;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTime() {
        return Time;
    }

    public void setTime(String time) {
        Time = time;
    }
}
