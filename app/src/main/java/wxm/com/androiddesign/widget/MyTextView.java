package wxm.com.androiddesign.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

import wxm.com.androiddesign.R;

/**
 * Created by zero on 2015/7/3.
 */
public class MyTextView extends TextView {
    public MyTextView(Context context, AttributeSet attrs) {
        super(context, attrs);

//        if (isInEditMode()) {
//            return;
//        }

        TypedArray styledAttrs = context.obtainStyledAttributes(attrs, R.styleable.MyTextView);
        String fontName = styledAttrs.getString(R.styleable.MyTextView_typeface);
        styledAttrs.recycle();


//        setTypeface(typeface);
        if (fontName != null) {
            Typeface typeface = Typeface.createFromAsset(context.getAssets(), "fonts/" + fontName);
            setTypeface(typeface);
        }
    }
}
