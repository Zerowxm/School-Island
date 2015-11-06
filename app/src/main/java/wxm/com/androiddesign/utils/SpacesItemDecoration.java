package wxm.com.androiddesign.utils;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import wxm.com.androiddesign.R;

public class SpacesItemDecoration extends RecyclerView.ItemDecoration {
    //    private int space;
//
//    public SpacesItemDecoration(int space) {
//        this.space = space;
//    }
//
//    @Override
//    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
//        outRect.left = space;
//        outRect.right = space;
//        outRect.bottom = space;
//
//        // Add top margin only for the first item to avoid double space between items
//        if(parent.getChildPosition(view) == 0)
//            outRect.top = space;
//    }
    private Drawable mDivider;

    public SpacesItemDecoration(Context context) {
        mDivider = ContextCompat.getDrawable(context,R.drawable.line_divider);
    }

    public void onDrawOver(Canvas c, RecyclerView parent, RecyclerView.State state) {
        int left = parent.getPaddingLeft()+100;
        int right = parent.getWidth() - parent.getPaddingRight();

        int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View child = parent.getChildAt(i);

            RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();

            int top = child.getBottom() + params.bottomMargin;
            int bottom = top + mDivider.getIntrinsicHeight();

            mDivider.setBounds(left, top, right, bottom);
            mDivider.draw(c);
        }
    }
}