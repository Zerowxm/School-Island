package wxm.com.androiddesign;

import java.security.PublicKey;

/**
 * Created by zero on 2015/6/25.
 */
public class ActivityItemData {
    public String name;
    public String tag;
    public String time;
    public String plus;
    public String commet;
    public int imageId;

    public ActivityItemData(int imageId){
        name="zero";
        tag="anime";
        time="7h";
        plus="6";
        commet="7";
        this.imageId=imageId;
    }
}
