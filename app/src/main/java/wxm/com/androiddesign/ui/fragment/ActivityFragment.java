package wxm.com.androiddesign.ui.fragment;

import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.support.design.widget.FloatingActionButton;


import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.SocketHandler;

import wxm.com.androiddesign.anim.MyItemAnimator;
import wxm.com.androiddesign.module.AtyItem;
import wxm.com.androiddesign.adapter.MyRecycerAdapter;
import wxm.com.androiddesign.R;

import wxm.com.androiddesign.utils.ScrollManager;


/**
 * Created by zero on 2015/6/25.
 */
public class ActivityFragment extends Fragment {
    public static final int Home=0x0;
    public static final int Hot=0x1;
    public static final int Nearby=0x2;
    public static final int Hight=0x3;
    public static final int Joined=0x4;
    public static final int Release=0x5;

    private int type;

    RecyclerView recyclerView;


    MyRecycerAdapter myRecycerAdapter;
    SwipeRefreshLayout mSwipeRefreshLayout;
    ScrollManager manager = new ScrollManager();

    public ActivityFragment(){

    }

    public static ActivityFragment newInstance(int type){
        ActivityFragment fragment=new ActivityFragment();
        Bundle args=new Bundle();
        args.putInt("Type", type);
        fragment.setArguments(args);
        return fragment;
    }

    static ArrayList<AtyItem> activityItems = new ArrayList<AtyItem>();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        type=getArguments().getInt("Type",Home);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup viewGroup, Bundle savedInstanceState) {
        View v;
        if(type==Joined||type==Release){
            v = inflater.inflate(R.layout.activity_user_fragment, viewGroup, false);
            recyclerView = (RecyclerView) v.findViewById(R.id.recyclerview_activity);
            setupRecyclerView(recyclerView);
        }
        else {
            v = inflater.inflate(R.layout.activity_fragment, viewGroup, false);
            recyclerView = (RecyclerView) v.findViewById(R.id.recyclerview_activity);
            mSwipeRefreshLayout = (SwipeRefreshLayout) v.findViewById(R.id.swipeRefreshLayout);
            setupSwipeRefreshLayout(mSwipeRefreshLayout);
            setupRecyclerView(recyclerView);
            manager.setSwipeRefreshLayout(mSwipeRefreshLayout);
        }





        return v;
    }

    private void setupSwipeRefreshLayout(SwipeRefreshLayout swipeRefreshLayout) {
        swipeRefreshLayout.setColorScheme(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {

            @Override
            public void onRefresh() {
                refreshContent();
            }
        });
    }

    private void refreshContent() {
        //load content
        mSwipeRefreshLayout.setRefreshing(true);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                myRecycerAdapter.notifyDataSetChanged();

                mSwipeRefreshLayout.setRefreshing(false);
            }
        }, 5000);
        //load complete

        onContentLoadComplete();
    }

    private void onContentLoadComplete() {
        mSwipeRefreshLayout.setRefreshing(false);
    }

    private void setupRecyclerView(RecyclerView recyclerView) {
        recyclerView.setLayoutManager(new LinearLayoutManager(recyclerView.getContext()));
        recyclerView.setHasFixedSize(true);
        recyclerView.setItemAnimator(new MyItemAnimator());

        manager.attach(recyclerView);
        manager.addView(getActivity().findViewById(R.id.fab), ScrollManager.Direction.DOWN);

        String jsonarrys =
                "[{\"atyContent\":\"5\",\"time\":\"1\",\"atyName\":\"1\",\n" +
                        "\"comment\":\"1\",\n" +
                        "\"image\":[\"cc\",\"dd\"],\n" +
                        "\"location\":\"1\",\"plus\":\"1\",\n" +
                        "\"tag\":\"1\",\"atyImageId\":0,\"photoId\":0},\n" +
                        "{\"atyContent\":\"6\",\"time\":\"1\",\"atyName\":\"1\",\n" +
                        "\"comment\":\"1\",\n" +
                        "\"image\":[\"ff\",\"dd\"],\n" +
                        "\"location\":\"1\",\"plus\":\"1\",\n" +
                        "\"tag\":\"1\",\"atyImageId\":0,\"photoId\":0},\n" +
                        "{\"atyContent\":\"6\",\"time\":\"1\",\"atyName\":\"1\",\n" +
                        "\"comment\":\"1\",\n" +
                        "\"image\":[\"ff\",\"dd\"],\n" +
                        "\"location\":\"1\",\"plus\":\"1\",\n" +
                        "\"tag\":\"1\",\"atyImageId\":0,\"photoId\":0},\n" +
                        "{\"atyContent\":\"6\",\"time\":\"1\",\"atyName\":\"1\",\n" +
                        "\"comment\":\"1\",\n" +
                        "\"image\":[\"ff\",\"dd\"],\n" +
                        "\"location\":\"1\",\"plus\":\"1\",\n" +
                        "\"tag\":\"1\",\"atyImageId\":0,\"photoId\":0},\n" +
                        "{\"atyContent\":\"6\",\"time\":\"1\",\"atyName\":\"1\",\n" +
                        "\"comment\":\"1\",\n" +
                        "\"image\":[\"ff\",\"dd\"],\n" +
                        "\"location\":\"1\",\"plus\":\"1\",\n" +
                        "\"tag\":\"1\",\"atyImageId\":0,\"photoId\":0},\n" +
                        "{\"atyContent\":\"6\",\"time\":\"1\",\"atyName\":\"1\",\n" +
                        "\"comment\":\"1\",\n" +
                        "\"image\":[\"ff\",\"dd\"],\n" +
                        "\"location\":\"1\",\"plus\":\"1\",\n" +
                        "\"tag\":\"1\",\"atyImageId\":0,\"photoId\":0},\n" +
                        "{\"atyContent\":\"6\",\"time\":\"1\",\"atyName\":\"1\",\n" +
                        "\"comment\":\"1\",\n" +
                        "\"image\":[\"ff\",\"dd\"],\n" +
                        "\"location\":\"1\",\"plus\":\"1\",\n" +
                        "\"tag\":\"1\",\"atyImageId\":0,\"photoId\":0},\n" +
                        "{\"atyContent\":\"6\",\"time\":\"1\",\"atyName\":\"1\",\n" +
                        "\"comment\":\"1\",\n" +
                        "\"image\":[\"ff\",\"dd\"],\n" +
                        "\"location\":\"1\",\"plus\":\"1\",\n" +
                        "\"tag\":\"1\",\"atyImageId\":0,\"photoId\":0},\n" +
                        "{\"atyContent\":\"6\",\"time\":\"1\",\"atyName\":\"1\",\n" +
                        "\"comment\":\"1\",\n" +
                        "\"image\":[\"ff\",\"dd\"],\n" +
                        "\"location\":\"1\",\"plus\":\"1\",\n" +
                        "\"tag\":\"1\",\"atyImageId\":0,\"photoId\":0}\n" +
                        "]";
        activityItems = new Gson().fromJson(jsonarrys, new TypeToken<List<AtyItem>>() {
        }.getType());
        myRecycerAdapter=new MyRecycerAdapter(activityItems, (AppCompatActivity)getActivity());
        recyclerView.setAdapter(myRecycerAdapter);
    }
}
