package wxm.com.androiddesign.ui;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;

import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import wxm.com.androiddesign.anim.FadePageTransformer;
import wxm.com.androiddesign.adapter.PagerAdapter;
import wxm.com.androiddesign.R;

public abstract class AppIntro extends FragmentActivity {
    private PagerAdapter mPagerAdapter;
    private ViewPager pager;
    private List<Fragment> fragments = new Vector<>();
    private List<ImageView> dots;
    private int slidesNumber;
    private Vibrator mVibrator;
    private boolean isVibrateOn = false;
    private int vibrateIntensity = 20;

    private static final int FIRST_PAGE_NUM = 0;

    @Override
    final protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.intro_layout);

        final ImageView nextButton = (ImageView) findViewById(R.id.next);
        final ImageView doneButton = (ImageView) findViewById(R.id.done);
        mVibrator = (Vibrator) this.getSystemService(VIBRATOR_SERVICE);

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(@NonNull View v) {
                if (isVibrateOn) {
                    mVibrator.vibrate(vibrateIntensity);
                }
                pager.setCurrentItem(pager.getCurrentItem() + 1);
            }
        });

        doneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(@NonNull View v) {
                if (isVibrateOn) {
                    mVibrator.vibrate(vibrateIntensity);
                }
                onDonePressed();
            }
        });

        mPagerAdapter = new PagerAdapter(super.getSupportFragmentManager(), fragments);
        pager = (ViewPager) findViewById(R.id.view_pager);
        pager.setAdapter(this.mPagerAdapter);
        pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                selectDot(position);
                if (position == slidesNumber - 1) {
                    nextButton.setVisibility(View.GONE);
                    doneButton.setVisibility(View.VISIBLE);
                } else {
                    doneButton.setVisibility(View.GONE);
                    nextButton.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });

        init(savedInstanceState);
        loadDots();
    }

    private void loadDots() {
        LinearLayout dotLayout = (LinearLayout) findViewById(R.id.dotLayout);
        dots = new ArrayList<>();
        slidesNumber = fragments.size();

        for (int i = 0; i < slidesNumber; i++) {
            ImageView dot = new ImageView(this);
            dot.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.indicator_dot_grey));

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );
            dotLayout.addView(dot, params);

            dots.add(dot);
        }

        selectDot(FIRST_PAGE_NUM);
    }

    public void selectDot(int index) {
        Resources res = getResources();
        for (int i = 0; i < fragments.size(); i++) {
            int drawableId = (i == index) ? (R.drawable.indicator_dot_white) : (R.drawable.indicator_dot_grey);
            Drawable drawable = ContextCompat.getDrawable(getApplicationContext(),drawableId);
            dots.get(i).setImageDrawable(drawable);
        }
    }

    public void addSlide(@NonNull Fragment fragment, @NonNull Context context) {
        fragments.add(Fragment.instantiate(context, fragment.getClass().getName()));
        mPagerAdapter.notifyDataSetChanged();
    }

    @NonNull
    public List<Fragment> getSlides() {
        return mPagerAdapter.getFragments();
    }

    public void setVibrate(boolean vibrate) {
        this.isVibrateOn = vibrate;
    }

    public void setVibrateIntensity(int intensity) {
        this.vibrateIntensity = intensity;
    }

    public void setFadeAnimation() {
        pager.setPageTransformer(true, new FadePageTransformer());
    }

    public void setCustomTransformer(@Nullable ViewPager.PageTransformer transformer) {
        pager.setPageTransformer(true, transformer);
    }

    public abstract void init(@Nullable Bundle savedInstanceState);

    public abstract void onDonePressed();
}