package wxm.com.androiddesign.ui.test;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.AppBarLayout;
import android.support.v7.app.ActionBar;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.ButterKnife;
import wxm.com.androiddesign.R;
import wxm.com.androiddesign.module.User;
import wxm.com.androiddesign.ui.UserBaseAcitivity;

public class UserAcitvity extends UserBaseAcitivity {

    private static final int PERCENTAGE_TO_ANIMATE_AVATAR = 20;
    private boolean mIsAvatarShown = true;

    private int mMaxScrollSize;
    private String userId = null;
    private User user;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_material_up_concept);
        ButterKnife.bind(this);

        Toolbar toolbar=(Toolbar)findViewById(R.id.materialup_toolbar);
        AppBarLayout appBarLayout=(AppBarLayout)findViewById(R.id.materialup_appbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        ActionBar actionBar=getSupportActionBar();
        actionBar.setDefaultDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        appBarLayout.addOnOffsetChangedListener(this);
        mMaxScrollSize = appBarLayout.getTotalScrollRange();

        mProfileImage=(ImageView)findViewById(R.id.materialup_profile_image);
        mUserId=(TextView)findViewById(R.id.materialup_user_id);
        mBackDrop=(ImageView)findViewById(R.id.materialup_profile_backdrop);

        new GetUserInfo(this).execute();
    }

    public static void start(Context c,String userId,int index) {
        c.startActivity(new Intent(c, UserAcitvity.class).putExtra("userId",userId)
        .putExtra("index",index));
    }

    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int i) {
        if (mMaxScrollSize == 0)
            mMaxScrollSize = appBarLayout.getTotalScrollRange();

        int percentage = (Math.abs(i)) * 100 / mMaxScrollSize;

        if (percentage >= PERCENTAGE_TO_ANIMATE_AVATAR && mIsAvatarShown) {
            mIsAvatarShown = false;
            mProfileImage.animate().scaleY(0).scaleX(0).setDuration(200).start();
        }

        if (percentage <= PERCENTAGE_TO_ANIMATE_AVATAR && !mIsAvatarShown) {
            mIsAvatarShown = true;

            mProfileImage.animate()
                    .scaleY(1).scaleX(1)
                    .start();
        }
    }

}
