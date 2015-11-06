package wxm.com.androiddesign.utils;

import android.content.res.Resources;
import android.content.res.TypedArray;
import android.support.v7.graphics.Palette;

import java.util.Collections;
import java.util.Comparator;
import java.util.Random;

import wxm.com.androiddesign.R;
import wxm.com.androiddesign.ui.MyApplication;

/**
 * Created by Zero on 10/18/2015.
 */
public final class MyColorUtils {
    private static MyColorUtils INSTANCE=new MyColorUtils();
    static Resources resources;
    static TypedArray colors;
    public static MyColorUtils getINSTANCE(){
        return INSTANCE;
    }

    static {
        resources= MyApplication.applicationContext.getResources();
        colors=resources.obtainTypedArray(R.array.colors);
    }
    private MyColorUtils(){

    }

//    int[] colors={
//
//    };

    public static final Random random=new Random(System.currentTimeMillis());
    public static int getColor(){
        return colors.getColor(random.nextInt(colors.length()),0);
    }

    public static Palette.Swatch getDominantSwatch(Palette palette){
        return Collections.max(palette.getSwatches(), new Comparator<Palette.Swatch>() {
            @Override
            public int compare(Palette.Swatch lhs, Palette.Swatch rhs) {
                return lhs.getPopulation() < rhs.getPopulation() ? -1 : (lhs.getPopulation() == rhs.getPopulation() ? 0 : 1);
            }
        });
    }
}
