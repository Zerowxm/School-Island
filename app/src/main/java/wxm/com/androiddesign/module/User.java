package wxm.com.androiddesign.module;

import com.google.gson.annotations.Expose;

/**
 * Created by zero on 2015/7/3.
 */
public class User {
    @Expose
    public String action;
    @Expose
    String userId;
    @Expose
    public String userName;
    @Expose
    public String userPassword;
    @Expose
    public String userEmail;
    @Expose
    public String userPhone;
    @Expose
    public String userGender;
    @Expose
    public String userIcon;

    public User(String action, String userId,
                String userName, String userPassword,
                String userEmail, String userPhone,
                String userGender, String userIcon) {
        this.action = action;
        this.userId = userId;
        this.userName = userName;
        this.userPassword = userPassword;
        this.userEmail = userEmail;
        this.userPhone = userPhone;
        this.userGender = userGender;
        this.userIcon = userIcon;
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

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getUserPhone() {
        return userPhone;
    }

    public void setUserPhone(String userPhone) {
        this.userPhone = userPhone;
    }

    public String getUserGender() {
        return userGender;
    }

    public void setUserGender(String userGender) {
        this.userGender = userGender;
    }

    public String getUserIcon() {
        return userIcon;
    }

    public void setUserIcon(String userIcon) {
        this.userIcon = userIcon;
    }
}
