package wxm.com.androiddesign.utils;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Path;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;

/**
 * Created by zero on 2015/7/8.
 */
public class TransparentToolBar extends RecyclerView.OnScrollListener {
    private int mToolBarOffset=0;
    private int mToolbarHeight=300;
    private static final int MIN_SCROLL_TO_HIDE = 10;
    private boolean hidden;
    private int accummulatedDy;
    private View view;
    Context context;

    public void addView(View view) {
        this.view=view;
    }

    @Override
    public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
        super.onScrollStateChanged(recyclerView, newState);
        Log.d("onScrollState",""+newState);
    }

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);
        Log.d("onScrolled","dx:"+dx+" dy:"+dy);
        mToolBarOffset+=dy;
        runTranslateAnimation(view,0,new DecelerateInterpolator(3));

        if (dy > 0) {
            accummulatedDy = accummulatedDy > 0 ? accummulatedDy + dy : dy;
            if (accummulatedDy > MIN_SCROLL_TO_HIDE) {
                hideView(view);
            }
        } else if (dy < 0) {
            accummulatedDy = accummulatedDy < 0 ? accummulatedDy + dy : dy;
            if (accummulatedDy < -MIN_SCROLL_TO_HIDE) {
                showView(view);
            }
        }
    }

    private void showView(View view) {
        runTranslateAnimation(view, 0, new DecelerateInterpolator(3));
    }
    private void hideView(View view) {
        //int height = calculateTranslation(view);
        //int translateY = direction == ScrollManager.Direction.UP ? -height : height;
        runTranslateAnimation(view, mToolBarOffset, new AccelerateInterpolator(3));
    }

    private int calculateTranslation(View view) {
        int height = view.getHeight();

        ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) view.getLayoutParams();
        int margins = params.topMargin + params.bottomMargin;

        return height + margins;
    }

    public TransparentToolBar(){

    }
    public TransparentToolBar(Context context) {
        super();
        this.context=context;
    }

    private void runTranslateAnimation(View view, int translateY, Interpolator interpolator) {
//        Animator slideInAnimation = ObjectAnimator.ofFloat(view, "alpha", translateY/100);
//        slideInAnimation.setDuration(view.getContext().getResources().getInteger(android.R.integer.config_mediumAnimTime));
//        slideInAnimation.setInterpolator(interpolator);
//        slideInAnimation.start();

        Drawable c = view.getBackground();
        c.setAlpha(Math.round(translateY * 255));
        view.setBackground(c);
    }
}
