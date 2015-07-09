package wxm.com.androiddesign.module;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import wxm.com.androiddesign.R;

/**
 * Created by zero on 2015/7/3.
 */
public class User {
    public String name;
    public String password;
    public String email;
    public String number;
    public String grant;
    public int photoId;

    public User() {

    }


    public User(String name, String password, String email, String number, String grant) {
        this.name = name;
        this.password = password;
        this.email = email;
        this.number = number;
        this.grant = grant;
    }

    public JSONObject createJson() {
        JSONObject user = new JSONObject();
        try {
            user.put("name", name);
            user.put("password", password);
            user.put("email", email);
            user.put("number", number);
            user.put("grant", grant);
            user.put("photo", photoId);
            return user;
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getGrant() {
        return grant;
    }

    public void setGrant(String grant) {
        this.grant = grant;
    }

    public int getPhotoId() {
        return photoId;
    }

    public void setPhotoId(int photoId) {
        this.photoId = photoId;
    }
}
