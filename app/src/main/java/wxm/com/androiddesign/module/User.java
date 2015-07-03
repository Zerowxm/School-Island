package wxm.com.androiddesign.module;

import android.util.Log;

/**
 * Created by zero on 2015/7/3.
 */
public class User {
    String name;
    String password;
    String email;
    String number;
    String grant;


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
