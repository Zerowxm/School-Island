package wxm.com.androiddesign.module;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zero on 2015/6/25.
 */
public class AtyItem extends User implements Parcelable {
    //public int photoId;
    //public String name;
    public String tag;
    public String time;
    public String atyName;
    public String atyContent;
    public int atyImageId;
    public String location;
    public String plus;
    public String comment;
    public List<Uri> imageUri;

    public AtyItem(Parcel in) {
        imageUri = new ArrayList<>();
        readFromParcel(in);
    }

    public AtyItem(){;}

    public AtyItem(String mtag, String mtime, String matyName, String matyContent
            , int matyImageId, String mlocation, String mplus, String mcommet, List<Uri> uris){
        tag=mtag;
        time=mtime;
        atyName = matyName;
        atyContent = matyContent;
        atyImageId = matyImageId;
        location = mlocation;
        plus=mplus;
        comment=mcommet;
        imageUri=uris;
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
        dest.writeList(imageUri);
    }

    public void setImageUri(ArrayList<Uri> imageUri) {
        this.imageUri = imageUri;
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
        in.readList(imageUri,List.class.getClassLoader());
    }

    public static final Parcelable.Creator<AtyItem> CREATOR =
            new Parcelable.Creator<AtyItem>() {
                public AtyItem createFromParcel(Parcel in) {
                    return new AtyItem(in);
                }

                public AtyItem[] newArray(int size) {
                    return new AtyItem[size];
                }
            };


    public void setTag(String tag) {
        this.tag = tag;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public void setAtyName(String atyName) {
        this.atyName = atyName;
    }

    public void setAtyContent(String atyContent) {
        this.atyContent = atyContent;
    }

    public void setAtyImageId(int atyImageId) {
        this.atyImageId = atyImageId;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setPlus(String plus) {
        this.plus = plus;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
