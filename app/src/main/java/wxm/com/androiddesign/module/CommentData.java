package wxm.com.androiddesign.module;

/**
 * Created by hdchen on 2015/7/1.
 */
public class CommentData{
    public String name;
    public String tag;
    public String time;
    public int imageId;
    public String comment;
    int number;

    public CommentData(int imageId,int number,String comment) {
        name="zero";
        tag="anime";
        time="7h";
        this.imageId=imageId;
        this.number = number;
        this.comment = comment;
    }
}
