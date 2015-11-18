package wxm.com.maca.ui;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;

import wxm.com.maca.utils.NetState;
import wxm.com.maca.utils.PrefUtils;

public class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(NetState.getNetworkType()==0){
            Toast.makeText(MyApplication.applicationContext,"网络连接失败",Toast.LENGTH_SHORT).show();
        }
        if (PrefUtils.isFirstUse(this)){
            Intent intent=new Intent(this,SignUpActivity.class);
            startActivity(intent);
            finish();
        }
    }

    protected void setupToolBar(Toolbar toolbar){
        setSupportActionBar(toolbar);
        final ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);
    }
    protected void setupToolBar(Toolbar toolbar,String title){
        setSupportActionBar(toolbar);
        final ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);
        actionBar.setTitle(title);
    }
}
