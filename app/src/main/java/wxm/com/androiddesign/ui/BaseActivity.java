package wxm.com.androiddesign.ui;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import wxm.com.androiddesign.R;
import wxm.com.androiddesign.utils.PrefUtils;

public class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (PrefUtils.isFirstUse(this)){
            Intent intent=new Intent(this,SignUpActivity.class);
            startActivity(intent);
            finish();
        }
        setContentView(R.layout.activity_base);
    }
}
