package wxm.com.androiddesign.module;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by zero on 2015/6/25.
 */
public class ActivityItemData implements Parcelable {
    public int photoId;
    public String name;
    public String tag;
    public String time;
    public String atyName;
    public String atyContent;
    public int atyImageId;
    public String location;
    public String plus;
    public String comment;

    public ActivityItemData(Parcel in)
    {
        readFromParcel(in);
    }

    public ActivityItemData(int mphotoId,String mname,String mtag,String mtime,String matyName,String matyContent
            ,int matyImageId,String mlocation,String mplus,String mcommet){
        photoId=mphotoId;
        name=mname;
        tag=mtag;
        time=mtime;
        atyName = matyName;
        atyContent = matyContent;
        atyImageId = matyImageId;
        location = mlocation;
        plus=mplus;
        comment=mcommet;
        photoId=mphotoId;
    }

    @Override
    public int describeContents() {
        return 0;
    }

   // int mphotoId,String mname,String mtag,String mtime,String matyName,String matyContent
  //  ,int matyImageId,String mlocation,String mplus,String mcommet
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(photoId);
        dest.writeString(name);
        dest.writeString(tag);
        dest.writeString(time);
        dest.writeString(atyName);
        dest.writeString(atyContent);
        dest.writeInt(atyImageId);
        dest.writeString(location);
        dest.writeString(plus);
        dest.writeString(comment);

    }

    public void readFromParcel(Parcel in){
        photoId = in.readInt();
        name = in.readString();
        tag = in.readString();
        time = in.readString();
        atyName = in.readString();
        atyContent = in.readString();
        atyImageId = in.readInt();
        location = in.readString();
        plus = in.readString();
        comment = in.readString();
    }

    public static final Parcelable.Creator CREATOR =
            new Parcelable.Creator() {
                public ActivityItemData createFromParcel(Parcel in) {
                    return new ActivityItemData(in);
                }

                public ActivityItemData[] newArray(int size) {
                    return new ActivityItemData[size];
                }
            };
}
