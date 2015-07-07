package wxm.com.androiddesign.ui.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.Fragment;
import android.support.v4.util.Pair;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import android.support.design.widget.FloatingActionButton;


import java.util.ArrayList;

import wxm.com.androiddesign.anim.MyItemAnimator;
import wxm.com.androiddesign.listener.RecyclerItemClickListener;
import wxm.com.androiddesign.module.ActivityItemData;
import wxm.com.androiddesign.adapter.MyRecycerAdapter;
import wxm.com.androiddesign.R;

import wxm.com.androiddesign.ui.DetailActivity;
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
            datas.add(new ActivityItemData("name","tag","time","0","0",R.drawable.miao));
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
        recyclerView.setItemAnimator(new MyItemAnimator());
        recyclerView.setAdapter(new MyRecycerAdapter(datas,(Context)getActivity()));
        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(getActivity(), new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Snackbar.make(view,"carddetail",Snackbar.LENGTH_SHORT).show();
                Intent intent = new Intent(getActivity(), DetailActivity.class);
                intent.putExtra(DetailActivity.ID, datas.get(position).toString());
                ActivityOptionsCompat options=ActivityOptionsCompat.makeSceneTransitionAnimation(
                        getActivity(), new Pair<View, String>(view.findViewById(R.id.card_view), getResources().getString(R.string.transition_body))
                );
                ActivityCompat.startActivity(getActivity(),intent,options.toBundle());
            }
        })
        );

    }

    public static void addActivity(String mname,String mtag,String mtime,String mplus,String mcommet,int mimageId)
    {
        datas.add(new ActivityItemData(mname,mtag,mtime,mplus,mcommet,mimageId));
    }
}
