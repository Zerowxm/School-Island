package wxm.com.androiddesign.module;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import wxm.com.androiddesign.R;

/**
 * Created by zero on 2015/7/3.
 */
public class User {
    public String action;
    public String userName;
    public String userPassword;
    public String userEmail;
    public String userPhone;
    public String userGender;
    public int userIcon;
    public boolean userIsBaned;

    public User(String action, String userName, String userPassword, String userEmail, String userPhone, String userGender, int userIcon, boolean userIsBaned) {
        this.action = action;
        this.userName = userName;
        this.userPassword = userPassword;
        this.userEmail = userEmail;
        this.userPhone = userPhone;
        this.userGender = userGender;
        this.userIcon = userIcon;
        this.userIsBaned = userIsBaned;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
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

    public int getUserIcon() {
        return userIcon;
    }

    public void setUserIcon(int userIcon) {
        this.userIcon = userIcon;
    }

    public boolean isUserIsBaned() {
        return userIsBaned;
    }

    public void setUserIsBaned(boolean userIsBaned) {
        this.userIsBaned = userIsBaned;
    }

    public User() {

    }



}
