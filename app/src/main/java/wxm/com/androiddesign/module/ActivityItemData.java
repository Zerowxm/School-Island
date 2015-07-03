package wxm.com.androiddesign.module;

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

    public ActivityItemData(String mname,String mtag,String mtime,String mplus,String mcommet,int mimageId){
        name=mname;
        tag=mtag;
        time=mtime;
        plus=mplus;
        commet=mcommet;
        imageId=mimageId;
    }
}
