package wxm.com.androiddesign.anim;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zero on 2015/7/7.
 */
public class MyItemAnimator extends RecyclerView.ItemAnimator {

    List<RecyclerView.ViewHolder> mViewHolders = new ArrayList<RecyclerView.ViewHolder>();
    List<RecyclerView.ViewHolder> mAnimationRemoveViewHolders = new ArrayList<RecyclerView.ViewHolder>();

    @Override
    public boolean animateDisappearance(RecyclerView.ViewHolder viewHolder, ItemHolderInfo preLayoutInfo, ItemHolderInfo postLayoutInfo) {
        return false;
    }

    @Override
    public boolean animateAppearance(RecyclerView.ViewHolder viewHolder, ItemHolderInfo preLayoutInfo, ItemHolderInfo postLayoutInfo) {
        return false;
    }

    @Override
    public boolean animatePersistence(RecyclerView.ViewHolder viewHolder, ItemHolderInfo preLayoutInfo, ItemHolderInfo postLayoutInfo) {
        return false;
    }

    @Override
    public boolean animateChange(RecyclerView.ViewHolder oldHolder, RecyclerView.ViewHolder newHolder, ItemHolderInfo preLayoutInfo, ItemHolderInfo postLayoutInfo) {
        return false;
    }

    @Override
    public void runPendingAnimations() {
        int animationDuration = 300;
        AnimatorSet animator;
        View target;
        for (final RecyclerView.ViewHolder viewHolder : mViewHolders) {
            target = viewHolder.itemView;
            target.setPivotX(target.getMeasuredWidth() / 2);
            target.setPivotY(target.getMeasuredHeight() / 2);

            animator = new AnimatorSet();

            animator.playTogether(
                    ObjectAnimator.ofFloat(target, "translationX", -target.getMeasuredWidth(), 0.0f),
                    ObjectAnimator.ofFloat(target, "alpha", target.getAlpha(), 1.0f)
            );

            animator.setTarget(target);
            animator.setDuration(animationDuration);
            animator.setInterpolator(new AccelerateDecelerateInterpolator());
            animator.setStartDelay((animationDuration * viewHolder.getPosition()) / 10);
            animator.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {

                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    mViewHolders.remove(viewHolder);
                }

                @Override
                public void onAnimationCancel(Animator animation) {

                }

                @Override
                public void onAnimationRepeat(Animator animation) {

                }
            });
            animator.start();
        }
    }



    @Override
    public void endAnimation(RecyclerView.ViewHolder item) {

    }

    @Override
    public void endAnimations() {

    }

    @Override
    public boolean isRunning() {
        return !mViewHolders.isEmpty();
    }
}
