package wxm.com.androiddesign.module;

/**
 * Created by zero on 2015/7/22.
 */
public class CtyItem {
    String action;
    String ctyId;
    String ctyMembers;
    String ctyIcon;
    String ctyIsAttention;

    public CtyItem(String action, String ctyId, String ctyMembers, String ctyIcon, String ctyIsAttention) {
        this.action = action;
        this.ctyId = ctyId;
        this.ctyMembers = ctyMembers;
        this.ctyIcon = ctyIcon;
        this.ctyIsAttention = ctyIsAttention;
    }

    @Override
    public String toString() {
        return "CtyItem{" +
                "action='" + action + '\'' +
                ", ctyId='" + ctyId + '\'' +
                ", ctyMembers='" + ctyMembers + '\'' +
                ", ctyIcon='" + ctyIcon + '\'' +
                ", ctyIsAttention='" + ctyIsAttention + '\'' +
                '}';
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
