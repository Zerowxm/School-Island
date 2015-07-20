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
    public static final int Home = 0x0;
    public static final int Hot = 0x1;
    public static final int Nearby = 0x2;
    public static final int Hight = 0x3;
    public static final int Joined = 0x4;
    public static final int Release = 0x5;

    private int type;
    private String userId;

    RecyclerView recyclerView;


    static MyRecycerAdapter myRecycerAdapter;
    SwipeRefreshLayout mSwipeRefreshLayout;
    ScrollManager manager = new ScrollManager();

    public ActivityFragment() {

    }

    public static ActivityFragment newInstance(int type,String muserId) {
        ActivityFragment fragment = new ActivityFragment();
        Bundle args = new Bundle();
        args.putInt("Type", type);
        args.putString("UserId", muserId);
        fragment.setArguments(args);
        return fragment;
    }

    static ArrayList<AtyItem> activityItems = new ArrayList<AtyItem>();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        type = getArguments().getInt("Type", Home);
        userId = getArguments().getString("UserId");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup viewGroup, Bundle savedInstanceState) {
        View v;
        if (type == Joined || type == Release) {
            v = inflater.inflate(R.layout.activity_user_fragment, viewGroup, false);
            recyclerView = (RecyclerView) v;
            setupRecyclerView(recyclerView);
        } else {
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
                "[{\"atyContent\":\"content1\",\"atyStartTime\":\"starttime1\",\"atyEndTime\":\"endtime1\",\"atyName\":\"name1\",\n" +
                        "\"comment\":\"1\",\"atyPlused\":\"false\",\"atyJoined\":\"false\",\"userId\":\"aaa\",\n" +
                        "\"atyAlbum\":[\"http://imgsrc.baidu.com/forum/w%3D580/sign=b9fe30609158d109c4e3a9bae159ccd0/cee4762c11dfa9eceeb9050961d0f703908fc1d4.jpg\",\"http://imgsrc.baidu.com/forum/w%3D580/sign=b9fe30609158d109c4e3a9bae159ccd0/cee4762c11dfa9eceeb9050961d0f703908fc1d4.jpg\"],\n" +
                        "\"atyPlace\":\"place1\",\"atyPlus\":\"1\",\"atyComment\":\"1\",\"atyMembers\":\"1\",\n" +
                        "\"atyType\":\"type1\",\"atyImageId\":0,\"photoId\":0},\n" +

                        "{\"atyContent\":\"content2\",\"atyStartTime\":\"starttime2\",\"atyEndTime\":\"endtime2\",\"atyName\":\"name2\",\n" +
                        "\"comment\":\"1\",\"atyPlused\":\"false\",\"atyJoined\":\"false\",\"userId\":\"aaa\",\n" +
                        "\"atyAlbum\":[\"http://imgsrc.baidu.com/forum/w%3D580/sign=b9fe30609158d109c4e3a9bae159ccd0/cee4762c11dfa9eceeb9050961d0f703908fc1d4.jpg\",\"http://imgsrc.baidu.com/forum/w%3D580/sign=b9fe30609158d109c4e3a9bae159ccd0/cee4762c11dfa9eceeb9050961d0f703908fc1d4.jpg\"],\n" +

                        "\"atyPlace\":\"place2\",\"atyPlus\":\"1\",\"atyComment\":\"1\",\"atyMembers\":\"1\",\n" +
                        "\"atyType\":\"tyoe2\",\"atyImageId\":0,\"photoId\":0}" +
                        "]";
        String json = "{\"atyContent\":\"1\",\"time\":\"2\",\"atyName\":\"3\",\n" +
                "\"comment\":\"4\",\n" +
                "\"image\":[\"cc\",\"dd\"],\n" +
                "\"location\":\"5\",\"plus\":\"6\",\n" +
                "\"tag\":\"7\",\"atyImageId\":8,\"photoId\":9}";
        String json2 = "{\"atyContent\":\"2\",\"time\":\"3\",\"atyName\":\"4\",\"comment\":\"5\",\"location\":\"6\",\"plus\":\"7\",\"tag\":\"8\",\"atyImageId\":9,\"photoId\":10}";

        switch (type){
            case Hot:break;
            case Nearby:break;
            case Hight:break;
            case Joined:break;
            case Release:break;
        }

        activityItems = new Gson().fromJson(jsonarrys, new TypeToken<List<AtyItem>>() {
        }.getType());
        myRecycerAdapter = new MyRecycerAdapter(activityItems,userId, (AppCompatActivity) getActivity(), "ActivityFragment");
        recyclerView.setAdapter(myRecycerAdapter);
    }

    public static void refresh(AtyItem atyItem, int position) {
        activityItems.remove(position);
        activityItems.add(position, atyItem);
        myRecycerAdapter.notifyDataSetChanged();
    }
}
