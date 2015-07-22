package wxm.com.androiddesign.module;

/**
 * Created by zero on 2015/7/22.
 */
public class CtyItem {
    String action;
    String ctyId;
    String cmtMembers;
    String cmtIcon;

    public CtyItem(String action, String ctyId, String cmtMembers) {
        this.action = action;
        this.ctyId = ctyId;
        this.cmtMembers = cmtMembers;
    }

    @Override
    public String toString() {
        return "CmtItem{" +
                "action='" + action + '\'' +
                ", ctyId='" + ctyId + '\'' +
                ", cmtMembers='" + cmtMembers + '\'' +
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

    public String getCmtIcon() {
        return cmtIcon;
    }

    public void setCmtIcon(String cmtIcon) {
        this.cmtIcon = cmtIcon;
    }

    public String getCmtMembers() {
        return cmtMembers;
    }

    public void setCmtMembers(String cmtMembers) {
        this.cmtMembers = cmtMembers;
    }
}
