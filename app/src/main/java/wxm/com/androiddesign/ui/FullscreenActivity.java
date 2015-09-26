package wxm.com.androiddesign.ui;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import wxm.com.androiddesign.R;

public class FullscreenActivity extends Activity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fullscreen);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(1000);

                    SharedPreferences preferences=getSharedPreferences("wxm.com.androiddesign", MODE_PRIVATE);
                    boolean isSignup=preferences.getBoolean("isSignup", false);
                    Log.d("Test",String.valueOf(isSignup));
                    if(!isSignup){
                        startActivity(new Intent(FullscreenActivity.this, SignUpActivity.class));
                        overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
                    }
                    else {
                        startActivity(new Intent(FullscreenActivity.this, MainActivity.class).putExtra("isSignUp",true));
                        overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
                    }

                    finish();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
    }
}
