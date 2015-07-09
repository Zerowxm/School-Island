package wxm.com.androiddesign.ui.fragment;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Interpolator;

import java.util.ArrayList;

import wxm.com.androiddesign.module.AtyItem;
import wxm.com.androiddesign.adapter.MyRecycerAdapter;
import wxm.com.androiddesign.R;
import wxm.com.androiddesign.utils.TransparentToolBar;

/**
 * Created by zero on 2015/6/26.
 */
public class HomeFragment extends Fragment{

    RecyclerView recyclerView;
    Toolbar toolbar;
    static ArrayList<AtyItem> activityItems=new ArrayList<>();
    static {
        for (int i=0;i<10;i++){
            activityItems.add(new AtyItem("tag","time","atyname","atycontent",R.drawable.miao,"location","0","0",new ArrayList<Uri>()));
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup viewGroup,Bundle savedInstanceState){
        View v =inflater.inflate(R.layout.home_layout,viewGroup,false);
        recyclerView=(RecyclerView)v.findViewById(R.id.recyclerview_activity);

        toolbar=(Toolbar)v.findViewById(R.id.toolbar);

        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        final ActionBar actionBar=((AppCompatActivity) getActivity()).getSupportActionBar();
        actionBar.setHomeAsUpIndicator(R.drawable.ic_menu);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);
        setupRecyclerView(recyclerView);
        return v;
    }

    private void setupRecyclerView(RecyclerView recyclerView){
        recyclerView.setLayoutManager(new LinearLayoutManager(recyclerView.getContext()));

        recyclerView.setAdapter(new MyRecycerAdapter(activityItems,(AppCompatActivity)getActivity()));
        RecyclerView.ItemAnimator animator =recyclerView.getItemAnimator();
        //TransparentToolBar transparentToolBar=new TransparentToolBar();
        //transparentToolBar.addView(toolbar);
        //recyclerView.addOnScrollListener(transparentToolBar);
        animator.setAddDuration(2000);
        animator.setRemoveDuration(1000);
    }


}
