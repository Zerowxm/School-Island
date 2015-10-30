package wxm.com.androiddesign.module;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zero on 2015/6/25.
 */

public class AtyItem implements Parcelable {
    String action;
    String userId;
    String userName;
    String userIcon;
    String atyId;
    String atyName;
    String atyType;
    String atyCtyId;
    String atyCtyName;
    String atyStartTime;
    String atyEndTime;
    String atyPlace;
    String atyMembers;
    String atyContent;
    String atyLikes;
    String atyComments;
    String atyIsJoined;
    String atyIsLiked;
    String atyShares;
    String atyIsPublic;
    String releaseTime;
    List<String> atyAlbum = new ArrayList<String>();


    public String getAtyCtyId() {
        return atyCtyId;
    }

    public void setAtyCtyId(String atyCtyId) {
        this.atyCtyId = atyCtyId;
    }

    public String getAtyCtyName() {
        return atyCtyName;
    }

    public void setAtyCtyName(String atyCtyName) {
        this.atyCtyName = atyCtyName;
    }

    public String getAtyTpye() {
        return atyType;
    }

    public void setAtyTpye(String atyTpye) {
        this.atyType = atyTpye;
    }

    public String getReleaseTime() {
        return releaseTime;
    }

    public void setReleaseTime(String releaseTime) {
        this.releaseTime = releaseTime;
    }

    public String getAtyPublishTime() {
        return releaseTime;
    }

    public void setAtyPublishTime(String atyPublishTime) {
        this.releaseTime = atyPublishTime;
    }

    public String getAtyComments() {
        return atyComments;
    }

    public void setAtyComments(String atyComments) {
        this.atyComments = atyComments;
    }

    public String getAtyIsPublic() {
        return atyIsPublic;
    }

    public void setAtyIsPublic(String atyIsPublic) {
        this.atyIsPublic = atyIsPublic;
    }

    public String getAtyLikes() {
        return atyLikes;
    }

    public void setAtyLikes(String atyLikes) {
        this.atyLikes = atyLikes;
    }

    public String getAtyIsJoined() {
        return atyIsJoined;
    }

    public void setAtyIsJoined(String atyIsJoined) {
        this.atyIsJoined = atyIsJoined;
    }

    public String getAtyIsLiked() {
        return atyIsLiked;
    }

    public void setAtyIsLiked(String atyIsLiked) {
        this.atyIsLiked = atyIsLiked;
    }

    public String getAtyShares() {
        return atyShares;
    }

    public void setAtyShares(String atyShares) {
        this.atyShares = atyShares;
    }

    public String getAtyPlused() {
        return atyIsLiked;
    }

    public void setAtyPlused(String atyPlused) {
        this.atyIsLiked = atyPlused;
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

    public String getAtyStartTime() {
        return atyStartTime;
    }

    public void setAtyStartTime(String atyStartTime) {
        this.atyStartTime = atyStartTime;
    }

    public String getAtyEndTime() {
        return atyEndTime;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setUserIcon(String userIcon) {
        this.userIcon = userIcon;
    }

    public String getUserIcon() {
        return userIcon;
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
        return atyLikes;
    }

    public void setAtyPlus(String atyPlus) {
        this.atyLikes = atyPlus;
    }

    public String getAtyComment() {
        return atyComments;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public void setAtyComment(String atyComment) {
        this.atyComments = atyComment;
    }

    public String getAtyJoined() {
        return atyIsJoined;
    }

    public void setAtyJoined(String atyJoined) {
        this.atyIsJoined = atyJoined;
    }

    public String getAtyShare() {
        return atyShares;
    }

    public void setAtyShare(String atyShare) {
        this.atyShares = atyShare;
    }

    public List<String> getAtyAlbum() {
        return atyAlbum;
    }

    public void setAtyAlbum(List<String> atyAlbum) {
        this.atyAlbum = atyAlbum;
    }

    public AtyItem(Parcel in) {
        atyAlbum = new ArrayList<>();
        readFromParcel(in);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public AtyItem(String action, String userId, String userName, String userIcon,
                   String atyName, String atyCtyId, String atyStartTime, String atyEndTime,
                   String atyPlace, String atyMembers, String atyContent, String atyPlus,
                   String atyComment, String atyJoined, String atyPlused, String atyShare,
                   String atyIsPublic, List<String> atyAlbum , String atyType) {
        this.action = action;
        this.userId = userId;
        this.atyCtyId = atyCtyId;
        this.atyIsPublic = atyIsPublic;
        this.userName = userName;
        this.userIcon = userIcon;
        this.atyName = atyName;
        this.atyStartTime = atyStartTime;
        this.atyEndTime = atyEndTime;
        this.atyPlace = atyPlace;
        this.atyMembers = atyMembers;
        this.atyContent = atyContent;
        this.atyLikes = atyPlus;
        this.atyComments = atyComment;
        this.atyIsJoined = atyJoined;
        this.atyIsLiked = atyPlused;
        this.atyShares = atyShare;
        this.atyAlbum = atyAlbum;
        this.atyType = atyType;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public AtyItem(String action, String userId, String atyName,
                   String atyCtyId, String atyStartTime, String atyEndTime,
                   String atyPlace, String atyMembers, String atyContent,
                   String atyPlus, String atyComment, String atyJoined, String atyIsLiked,
                   String atyShare, String atyIsPublic, List<String> atyAlbum ,String atyType) {
        this.action = action;
        this.userId = userId;
        this.atyName = atyName;
        this.atyCtyId = atyCtyId;
        this.atyIsLiked = atyIsLiked;
        this.atyStartTime = atyStartTime;
        this.atyEndTime = atyEndTime;
        this.atyPlace = atyPlace;
        this.atyMembers = atyMembers;
        this.atyContent = atyContent;
        this.atyLikes = atyPlus;
        this.atyComments = atyComment;
        this.atyIsJoined = atyJoined;
        this.atyIsPublic = atyIsPublic;
        this.atyShares = atyShare;
        this.atyAlbum = atyAlbum;
        this.atyType = atyType;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(action);
        dest.writeString(userId);
        dest.writeString(userName);
        dest.writeString(userIcon);
        dest.writeString(atyId);
        dest.writeString(atyName);
        dest.writeString(atyCtyId);
        dest.writeString(atyCtyName);
        dest.writeString(atyStartTime);
        dest.writeString(atyEndTime);
        dest.writeString(atyPlace);
        dest.writeString(atyMembers);
        dest.writeString(atyContent);
        dest.writeString(atyLikes);
        dest.writeString(atyComments);
        dest.writeString(atyIsJoined);
        dest.writeString(atyIsLiked);
        dest.writeString(atyIsPublic);
        dest.writeString(atyShares);
        dest.writeString(atyType);
        dest.writeList(atyAlbum);
    }

    public void readFromParcel(Parcel in) {
        action = in.readString();
        userId = in.readString();
        userName = in.readString();
        userIcon = in.readString();
        atyId = in.readString();
        atyName = in.readString();
        atyCtyId = in.readString();
        atyCtyName = in.readString();
        atyStartTime = in.readString();
        atyEndTime = in.readString();
        atyPlace = in.readString();
        atyMembers = in.readString();
        atyContent = in.readString();
        atyLikes = in.readString();
        atyComments = in.readString();
        atyIsJoined = in.readString();
        atyIsLiked = in.readString();
        atyIsPublic = in.readString();
        atyShares = in.readString();
        atyType = in.readString();
        in.readList(atyAlbum, List.class.getClassLoader());
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

    @Override
    public String toString() {
        return "AtyItem{" +
                "action='" + action + '\'' +
                ", userId='" + userId + '\'' +
                ", userName='" + userName + '\'' +
                ", userIcon='" + userIcon + '\'' +
                ", atyId='" + atyId + '\'' +
                ", atyName='" + atyName + '\'' +
                ", atyCtyId='" + atyCtyId + '\'' +
                ", atyCtyName='" + atyCtyName + '\'' +
                ", atyStartTime='" + atyStartTime + '\'' +
                ", atyEndTime='" + atyEndTime + '\'' +
                ", atyPlace='" + atyPlace + '\'' +
                ", atyMembers='" + atyMembers + '\'' +
                ", atyContent='" + atyContent + '\'' +
                ", atyLikes='" + atyLikes + '\'' +
                ", atyComment='" + atyComments + '\'' +
                ", atyIsJoined='" + atyIsJoined + '\'' +
                ", atyIsLiked='" + atyIsLiked + '\'' +
                ", atyIsPublic='" + atyIsPublic + '\'' +
                ", atyShares='" + atyShares + '\'' +
                ", atyAlbum=" + atyAlbum +
                ", atyType=" + atyType +
                '}';
    }
}
