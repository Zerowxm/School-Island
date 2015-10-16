package wxm.com.androiddesign.module;

/**
 * Created by zero on 2015/7/22.
 */
public class Group {
    String action;
    String userId;
    String ctyId;
    String ctyMembers;
    String ctyIcon;
    String ctyIsAttention;
    String ctyType;
    String ctyName;
    String ctyIntro;

    public Group(String action, String ctyId, String ctyMembers, String ctyIcon, String ctyIsAttention) {
        this.action = action;
        this.ctyId = ctyId;
        this.ctyMembers = ctyMembers;
        this.ctyIcon = ctyIcon;
        this.ctyIsAttention = ctyIsAttention;
    }

    public Group(String action, String userId, String ctyIcon, String ctyType, String ctyName, String ctyIntro) {
        this.action = action;
        this.userId = userId;
        this.ctyIcon = ctyIcon;
        this.ctyType = ctyType;
        this.ctyName = ctyName;
        this.ctyIntro = ctyIntro;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getCtyType() {
        return ctyType;
    }

    public void setCtyType(String ctyType) {
        this.ctyType = ctyType;
    }

    public String getCtyName() {
        return ctyName;
    }

    public void setCtyName(String ctyName) {
        this.ctyName = ctyName;
    }

    public String getCtyIntro() {
        return ctyIntro;
    }

    public void setCtyIntro(String ctyIntro) {
        this.ctyIntro = ctyIntro;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getCtyId() {
        return ctyId;
    }

    public void setCtyId(String ctyId) {
        this.ctyId = ctyId;
    }

    public String getCtyMembers() {
        return ctyMembers;
    }

    public void setCtyMembers(String ctyMembers) {
        this.ctyMembers = ctyMembers;
    }

    public String getCtyIcon() {
        return ctyIcon;
    }

    public void setCtyIcon(String ctyIcon) {
        this.ctyIcon = ctyIcon;
    }

    public String getCtyIsAttention() {
        return ctyIsAttention;
    }

    public void setCtyIsAttention(String ctyIsAttention) {
        this.ctyIsAttention = ctyIsAttention;
    }
}
