package wxm.com.androiddesign.ui.fragment;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.support.design.widget.FloatingActionButton;


import java.util.ArrayList;
import wxm.com.androiddesign.module.ActivityItemData;
import wxm.com.androiddesign.adapter.MyRecycerAdapter;
import wxm.com.androiddesign.R;

import wxm.com.androiddesign.utils.ScrollManager;
import wxm.com.androiddesign.utils.SpacesItemDecoration;


/**
 * Created by zero on 2015/6/25.
 */
public class ActivityFragment extends Fragment{

    RecyclerView recyclerView;


    MyRecycerAdapter myRecycerAdapter;
    SwipeRefreshLayout mSwipeRefreshLayout;

    static ArrayList<ActivityItemData> datas=new ArrayList<ActivityItemData>();

    static {
        for (int i=0;i<5;i++){
            datas.add(new ActivityItemData(R.drawable.miao,"name","tag","time","atyname","atycontent",R.drawable.miao,"location","0","0"));
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup viewGroup,Bundle savedInstanceState){
        View v =inflater.inflate(R.layout.activity_fragment,viewGroup,false);
        recyclerView=(RecyclerView)v.findViewById(R.id.recyclerview_activity);
        setupRecyclerView(recyclerView);

        mSwipeRefreshLayout=(SwipeRefreshLayout)v.findViewById(R.id.swipeRefreshLayout);
        ScrollManager manager=new ScrollManager();
        manager.attach(recyclerView);
        manager.addView((FloatingActionButton)getActivity().findViewById(R.id.fab), ScrollManager.Direction.DOWN);
        setupSwipeRefreshLayout(mSwipeRefreshLayout);



        return v;
    }

    private void setupSwipeRefreshLayout(SwipeRefreshLayout swipeRefreshLayout){
        swipeRefreshLayout.setColorScheme(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener(){

            @Override
            public void onRefresh() {
                refreshContent();
            }
        });
    }

    private void refreshContent(){
        //load content
        mSwipeRefreshLayout.setRefreshing(true);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Snackbar.make(mSwipeRefreshLayout, "refresh", Snackbar.LENGTH_SHORT).show();
                mSwipeRefreshLayout.setRefreshing(false);
            }
        },5000);
        //load complete
        //onContentLoadComplete();
    }

    private void onContentLoadComplete(){
        mSwipeRefreshLayout.setRefreshing(false);
    }
    private void setupRecyclerView(RecyclerView recyclerView){
        recyclerView.setLayoutManager(new LinearLayoutManager(recyclerView.getContext()));
        recyclerView.setHasFixedSize(true);
        //recyclerView.addItemDecoration(new SpacesItemDecoration(getResources()));
        //recyclerView.setItemAnimator(new DefaultItemAnimator());
       // recyclerView.setAdapter(new MyRecycerAdapter(datas,(Context)getActivity()));
        recyclerView.setAdapter(new MyRecycerAdapter(datas,this));


    }

//    public static void addActivity(int mphotoId,String mname,String mtag,String mtime,String matyName,String matyContent
//            ,int matyImageId,String mlocation,String mplus,String mcommet)
//    {
//        datas.add(new ActivityItemData(mphotoId,mname,mtag,mtime,matyName,matyContent,matyImageId,mlocation,mplus,mcommet));
//    }

    public static void addActivity(ActivityItemData activityItemData)
    {
        datas.add(activityItemData);
    }
}
