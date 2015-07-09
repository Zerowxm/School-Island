package wxm.com.androiddesign.module;

import android.util.Log;

import wxm.com.androiddesign.R;

/**
 * Created by zero on 2015/7/3.
 */
public class User {
    public static String name="wu";
    public String password;
    public String email;
    public String number;
    public String grant;
    public static int photoId= R.drawable.miao;

    public User(){

    }


    public User(String name,String password,String email,String number,String grant){
        this.name=name;
        this.password=password;
        this.email=email;
        this.number=number;
        this.grant=grant;
    }

    public String getEmail() {
        return email;
    }

    public String getNumber() {
        return number;
    }

    public String getName() {
        return name;
    }

    public String getGrant() {
        return grant;
    }

    public String getPassword() {
        return password;
    }
}
