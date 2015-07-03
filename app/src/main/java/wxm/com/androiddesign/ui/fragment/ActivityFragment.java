package wxm.com.androiddesign.ui.fragment;

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
import android.widget.ImageButton;

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
        for (int i=0;i<17;i++){
            datas.add(new ActivityItemData(R.drawable.miao));
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
        //manager.addView((ImageButton)getActivity().findViewById(R.id.fab), ScrollManager.Direction.DOWN);
        manager.addView((FloatingActionButton)getActivity().findViewById(R.id.fab), ScrollManager.Direction.DOWN);
        setupSwipeRefreshLayout(mSwipeRefreshLayout);

//        ScrollManager manager=new ScrollManager();
//        manager.attach(recyclerView);
//        manager.addView((ImageButton)getActivity().findViewById(R.id.fab), ScrollManager.Direction.DOWN);


        return v;
    }

    private void setupSwipeRefreshLayout(SwipeRefreshLayout swipeRefreshLayout){
        swipeRefreshLayout.setColorSchemeColors(R.color.blue,R.color.green);
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
            }
        },2000);
        //load complete
        onContentLoadComplete();
    }

    private void onContentLoadComplete(){
        mSwipeRefreshLayout.setRefreshing(false);
    }
    private void setupRecyclerView(RecyclerView recyclerView){
        recyclerView.setLayoutManager(new LinearLayoutManager(recyclerView.getContext()));
        recyclerView.setHasFixedSize(true);
        recyclerView.addItemDecoration(new SpacesItemDecoration(getResources()));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(new MyRecycerAdapter(datas,this));
    }
}
