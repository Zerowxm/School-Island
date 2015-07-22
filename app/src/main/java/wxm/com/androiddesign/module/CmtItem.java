package wxm.com.androiddesign.module;

/**
 * Created by zero on 2015/7/22.
 */
public class CmtItem {
    String action;
    String cmtId;
    String cmtMembers;
    String cmtIcon;

    public CmtItem(String action, String cmtId, String cmtMembers) {
        this.action = action;
        this.cmtId = cmtId;
        this.cmtMembers = cmtMembers;
    }

    @Override
    public String toString() {
        return "CmtItem{" +
                "action='" + action + '\'' +
                ", cmtId='" + cmtId + '\'' +
                ", cmtMembers='" + cmtMembers + '\'' +
                '}';
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getCmtId() {
        return cmtId;
    }

    public void setCmtId(String cmtId) {
        this.cmtId = cmtId;
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
