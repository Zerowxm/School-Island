package wxm.com.maca.ui;

import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.view.MotionEventCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import wxm.com.maca.R;
import wxm.com.maca.utils.MyUtils;
import wxm.com.maca.widget.AvatarImageBehavior;


public class NewUserAcitivity extends UserBaseAcitivity {

    private static final float PERCENTAGE_TO_SHOW_TITLE_AT_TOOLBAR  = 0.9f;
    private static final float PERCENTAGE_TO_HIDE_TITLE_DETAILS     = 0.3f;
    private static final int ALPHA_ANIMATIONS_DURATION              = 200;

    private boolean mIsTheTitleVisible          = false;
    private boolean mIsTheTitleContainerVisible = true;

    private LinearLayout mTitleContainer;
    private FrameLayout mFrameParallax;
    private Toolbar mToolbar;
    private TextView mTitle;
    AppBarLayout appBarLayout;
    private void bindActivity() {
        appBarLayout=(AppBarLayout)findViewById(R.id.materialup_appbar);
        mProfileImage=(ImageView)findViewById(R.id.materialup_profile_image);
        mUserId=(TextView)findViewById(R.id.materialup_user_id);
        mUserSignature=(TextView)findViewById(R.id.materialup_user_signature);
        mBackDrop=(ImageView)findViewById(R.id.materialup_profile_backdrop);
        mTitle=(TextView)findViewById(R.id.main_textview_title);
        mToolbar        = (Toolbar) findViewById(R.id.toolbar);
        mTitleContainer = (LinearLayout) findViewById(R.id.main_linearlayout_title);
        mFrameParallax  = (FrameLayout) findViewById(R.id.main_framelayout_title);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_user_acitivity);
        bindActivity();

        appBarLayout.addOnOffsetChangedListener(this);
        new GetUserInfo(this).execute();

        mToolbar.setTitle("");
        setSupportActionBar(mToolbar);
        ActionBar actionBar=getSupportActionBar();
        actionBar.setDefaultDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        setupUser();
        startAlphaAnimation(mTitle, 0, View.INVISIBLE);
        initParallaxValues();
    }

    public static void start(Context c,String userId) {
        c.startActivity(new Intent(c, NewUserAcitivity.class).putExtra("userId", userId));
    }

    public void setupUser(){
        CoordinatorLayout.MarginLayoutParams marginLayoutParams=new ViewGroup.MarginLayoutParams(mProfileImage.getLayoutParams());
        Display display=getWindowManager().getDefaultDisplay();
        Point size=new Point();
        display.getSize(size);
        int width=size.x;
        int height=size.y;
        marginLayoutParams.setMargins(width * 2 / 5, MyUtils.getPixels(this, 160), 0, 0);
        CoordinatorLayout.LayoutParams layoutParams=new CoordinatorLayout.LayoutParams(marginLayoutParams);
        layoutParams.setBehavior(new AvatarImageBehavior(this));
        mProfileImage.setLayoutParams(layoutParams);
    }

    private void setup(int margin){
        AppBarLayout.MarginLayoutParams marginLayoutParams=new ViewGroup.MarginLayoutParams(tabs.getLayoutParams());
        marginLayoutParams.setMargins(0,margin,0,0);
        AppBarLayout.LayoutParams layoutParams=new AppBarLayout.LayoutParams(marginLayoutParams);
        tabs.setLayoutParams(layoutParams);
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
    }

    @Override
    protected void onResumeFragments() {
        super.onResumeFragments();
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        final int action = MotionEventCompat.getActionMasked(ev);
        return super.dispatchTouchEvent(ev);
    }





    private void initParallaxValues() {
        CollapsingToolbarLayout.LayoutParams petDetailsLp =
                (CollapsingToolbarLayout.LayoutParams) mBackDrop.getLayoutParams();

        CollapsingToolbarLayout.LayoutParams petBackgroundLp =
                (CollapsingToolbarLayout.LayoutParams) mFrameParallax.getLayoutParams();

        petDetailsLp.setParallaxMultiplier(0.9f);
        petBackgroundLp.setParallaxMultiplier(0.3f);

        mBackDrop.setLayoutParams(petDetailsLp);
        mFrameParallax.setLayoutParams(petBackgroundLp);
    }



    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int offset) {
        int maxScroll = appBarLayout.getTotalScrollRange();
        float percentage = (float) Math.abs(offset) / (float) maxScroll;
        handleAlphaOnTitle(percentage);
        handleToolbarTitleVisibility(percentage);

    }

    private void handleToolbarTitleVisibility(float percentage) {
        if (percentage >= PERCENTAGE_TO_SHOW_TITLE_AT_TOOLBAR) {

            if(!mIsTheTitleVisible) {
                startAlphaAnimation(mTitle, ALPHA_ANIMATIONS_DURATION, View.VISIBLE);
                mIsTheTitleVisible = true;
            }

        } else {

            if (mIsTheTitleVisible) {
                startAlphaAnimation(mTitle, ALPHA_ANIMATIONS_DURATION, View.INVISIBLE);
                mIsTheTitleVisible = false;
            }
        }
    }

    private void handleAlphaOnTitle(float percentage) {
        if (percentage >= PERCENTAGE_TO_HIDE_TITLE_DETAILS) {
            if(mIsTheTitleContainerVisible) {
                startAlphaAnimation(mTitleContainer, ALPHA_ANIMATIONS_DURATION, View.INVISIBLE);
                mIsTheTitleContainerVisible = false;
            }

        } else {

            if (!mIsTheTitleContainerVisible) {
                startAlphaAnimation(mTitleContainer, ALPHA_ANIMATIONS_DURATION, View.VISIBLE);
                mIsTheTitleContainerVisible = true;
            }
        }
    }

    public static void startAlphaAnimation (View v, long duration, int visibility) {
        AlphaAnimation alphaAnimation = (visibility == View.VISIBLE)
                ? new AlphaAnimation(0f, 1f)
                : new AlphaAnimation(1f, 0f);
        alphaAnimation.setDuration(duration);
        alphaAnimation.setFillAfter(true);
        v.startAnimation(alphaAnimation);
    }

    public static void startTabAnimation (View v, long duration, int visibility) {

        AlphaAnimation alphaAnimation = (visibility == View.VISIBLE)
                ? new AlphaAnimation(0f, 1f)
                : new AlphaAnimation(1f, 0f);
        alphaAnimation.setDuration(duration);
        alphaAnimation.setFillAfter(true);
        v.startAnimation(alphaAnimation);
    }

}
