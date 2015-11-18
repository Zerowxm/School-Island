package wxm.com.maca.module;

import com.google.gson.annotations.Expose;

/**
 * Created by Zero on 11/6/2015.
 */
public class Avator {
    @Expose
    public String userIcon;
    @Expose
    String userId;

    public Avator(String userIcon, String userId) {
        this.userIcon = userIcon;
        this.userId = userId;
    }

    public String getUserIcon() {
        return userIcon;
    }

    public void setUserIcon(String userIcon) {
        this.userIcon = userIcon;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
