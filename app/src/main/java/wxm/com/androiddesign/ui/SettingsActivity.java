package wxm.com.androiddesign.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;

import butterknife.Bind;
import butterknife.ButterKnife;
import wxm.com.androiddesign.R;
import wxm.com.androiddesign.ui.fragment.SettingsFragment;

public class SettingsActivity extends BaseActivity {

    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        getFragmentManager().beginTransaction()
                .replace(R.id.content,new SettingsFragment())
                .commitAllowingStateLoss();
        ButterKnife.bind(this);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        setupToolBar(toolbar, "设置");
    }

    public static void start(Context context){
        context.startActivity(new Intent(context,SettingsActivity.class));
    }

    @Override
    public void onBackPressed() {
        finish();
        super.onBackPressed();
    }
}
