package wxm.com.androiddesign.module;

import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zero on 2015/6/25.
 */
public class AtyItem {
    String action,
            userId,
            atyId,
            atyName,
            atyType,
            atyStartTime,
            atyEndTime,
            atyPlace,
            atyMembers,
            atyContent,
            atyPlus,
            atyComment,
            atyJoined,
            atyShare;
    List<String> atyAlbum=new ArrayList<String>();

    public AtyItem(String action, String userId,
                   String atyId, String atyName,
                   String atyType, String atyStartTime,
                   String atyEndTime, String atyPlace,
                   String atyMembers, String atyContent,
                   String atyPlus, String atyComment,
                   String atyJoined, String atyShare,
                   List<String> atyAlbum) {
        this.action = action;
        this.userId = userId;
        this.atyId = atyId;
        this.atyName = atyName;
        this.atyType = atyType;
        this.atyStartTime = atyStartTime;
        this.atyEndTime = atyEndTime;
        this.atyPlace = atyPlace;
        this.atyMembers = atyMembers;
        this.atyContent = atyContent;
        this.atyPlus = atyPlus;
        this.atyComment = atyComment;
        this.atyJoined = atyJoined;
        this.atyShare = atyShare;
        this.atyAlbum = atyAlbum;
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

    public String getAtyId() {
        return atyId;
    }

    public void setAtyId(String atyId) {
        this.atyId = atyId;
    }

    public String getAtyName() {
        return atyName;
    }

    public void setAtyName(String atyName) {
        this.atyName = atyName;
    }

    public String getAtyType() {
        return atyType;
    }

    public void setAtyType(String atyType) {
        this.atyType = atyType;
    }

    public String getAtyStartTime() {
        return atyStartTime;
    }

    public void setAtyStartTime(String atyStartTime) {
        this.atyStartTime = atyStartTime;
    }

    public String getAtyEndTime() {
        return atyEndTime;
    }

    public void setAtyEndTime(String atyEndTime) {
        this.atyEndTime = atyEndTime;
    }

    public String getAtyPlace() {
        return atyPlace;
    }

    public void setAtyPlace(String atyPlace) {
        this.atyPlace = atyPlace;
    }

    public String getAtyMembers() {
        return atyMembers;
    }

    public void setAtyMembers(String atyMembers) {
        this.atyMembers = atyMembers;
    }

    public String getAtyContent() {
        return atyContent;
    }

    public void setAtyContent(String atyContent) {
        this.atyContent = atyContent;
    }

    public String getAtyPlus() {
        return atyPlus;
    }

    public void setAtyPlus(String atyPlus) {
        this.atyPlus = atyPlus;
    }

    public String getAtyComment() {
        return atyComment;
    }

    public void setAtyComment(String atyComment) {
        this.atyComment = atyComment;
    }

    public String getAtyJoined() {
        return atyJoined;
    }

    public void setAtyJoined(String atyJoined) {
        this.atyJoined = atyJoined;
    }

    public String getAtyShare() {
        return atyShare;
    }

    public void setAtyShare(String atyShare) {
        this.atyShare = atyShare;
    }

    public List<String> getAtyAlbum() {
        return atyAlbum;
    }

    public void setAtyAlbum(List<String> atyAlbum) {
        this.atyAlbum = atyAlbum;
    }
    //    public AtyItem(Parcel in) {
//        atyAlbum = new ArrayList<>();
//        readFromParcel(in);
//    }

//    @Override
//    public int describeContents() {
//        return 0;
//    }
//
//
//   @Override
//   public void writeToParcel(Parcel dest, int flags) {
//       dest.writeInt(userIcon);
//       dest.writeString(userName);
//       dest.writeString(atyType);
//       dest.writeString(atyStartTime);
//       dest.writeString(atyName);
//       dest.writeString(atyContent);
//       dest.writeString(atyPlace);
//       dest.writeString(atyPlus);
//       dest.writeString(atyComment);
//       dest.writeList(atyAlbum);
//   }
//    public void readFromParcel(Parcel in){
//        userIcon = in.readInt();
//        userName = in.readString();
//        atyType = in.readString();
//        atyStartTime = in.readString();
//        atyName = in.readString();
//        atyContent = in.readString();
//        atyPlace = in.readString();
//        atyPlus = in.readString();
//        atyComment = in.readString();
//        in.readList(atyAlbum,List.class.getClassLoader());
//    }
//
//    public static final Parcelable.Creator<AtyItem> CREATOR =
//            new Parcelable.Creator<AtyItem>() {
//                public AtyItem createFromParcel(Parcel in) {
//                    return new AtyItem(in);
//                }
//
//                public AtyItem[] newArray(int size) {
//                    return new AtyItem[size];
//                }
//            };

}
