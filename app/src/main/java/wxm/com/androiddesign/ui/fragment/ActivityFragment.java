package wxm.com.androiddesign.ui.fragment;

import android.net.Uri;
import android.os.AsyncTask;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.SocketHandler;

import wxm.com.androiddesign.anim.MyItemAnimator;
import wxm.com.androiddesign.module.AtyItem;
import wxm.com.androiddesign.adapter.MyRecycerAdapter;
import wxm.com.androiddesign.R;

import wxm.com.androiddesign.network.JsonConnection;
import wxm.com.androiddesign.utils.ScrollManager;


/**
 * Created by zero on 2015/6/25.
 */
public class ActivityFragment extends Fragment {

    public static final int Hot = 0x1;
    public static final int Nearby = 0x2;
    public static final int Hight = 0x3;
    private int type;
    private String userId;

    RecyclerView recyclerView;


    static MyRecycerAdapter myRecycerAdapter;
    SwipeRefreshLayout mSwipeRefreshLayout;
    ScrollManager manager = new ScrollManager();

    public ActivityFragment() {

    }

    public static ActivityFragment newInstance(int type, String muserId) {
        ActivityFragment fragment = new ActivityFragment();
        Bundle args = new Bundle();
        args.putInt("Type", type);
        args.putString("userId", muserId);
        fragment.setArguments(args);
        return fragment;
    }

    static ArrayList<AtyItem> activityItems = new ArrayList<AtyItem>();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        type = getArguments().getInt("Type");
        Log.d("wxm123",""+type);
        userId = getArguments().getString("userId");

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup viewGroup, Bundle savedInstanceState) {
        View v;
            v = inflater.inflate(R.layout.activity_fragment, viewGroup, false);
            recyclerView = (RecyclerView) v.findViewById(R.id.recyclerview_activity);
            Log.d("wxm123",""+recyclerView.toString());
            mSwipeRefreshLayout = (SwipeRefreshLayout) v.findViewById(R.id.swipeRefreshLayout);
            setupSwipeRefreshLayout(mSwipeRefreshLayout);
            setupRecyclerView(recyclerView);
        Log.d("wxm123", "" + recyclerView.toString());
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
        final LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        //recyclerView.setHasFixedSize(true);
        recyclerView.setItemAnimator(new MyItemAnimator());

        manager.attach(recyclerView);
        manager.addView(getActivity().findViewById(R.id.fab), ScrollManager.Direction.DOWN);
        switch (type) {
            case Hot:
                new GetAtyTask().execute(Hot);
                break;
            case Nearby:
                new GetAtyTask().execute(Nearby);
                break;
            case Hight:
                new GetAtyTask().execute(Hight);
                break;
        }


        //activityItems = new Gson().fromJson(jsonarrys, new TypeToken<List<AtyItem>>() {
        // }.getType());
//        myRecycerAdapter = new MyRecycerAdapter(activityItems,userId, (AppCompatActivity) getActivity(), "ActivityFragment");
//        recyclerView.setAdapter(myRecycerAdapter);
    }


    private class GetAtyTask extends AsyncTask<Integer,Void,Boolean> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //mSwipeRefreshLayout.setRefreshing(true);
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            if (aBoolean==true){
                for (int i=0;i<activityItems.size();i++){
                    Log.d("Task",activityItems.get(i).toString());
                }
                myRecycerAdapter = new MyRecycerAdapter(activityItems,userId, (AppCompatActivity) getActivity(), "ActivityFragment");
                recyclerView.setAdapter(myRecycerAdapter);
                //mSwipeRefreshLayout.setRefreshing(false);
            }
        }

        @Override
        protected Boolean doInBackground(Integer... params) {
            JSONObject object=new JSONObject();
            try {
                switch (params[0]) {
                    case Hot:object.put("action","showHotAty");
                        break;
                    case Nearby:object.put("action","showNearbyAty");
                        break;
                    case Hight:object.put("action","showHightAty");
                        break;
                }
                object.put("userId",userId);
                String jsonarrys = JsonConnection.getJSON(object.toString());
                  Log.i("jsonarray",jsonarrys.toString());
                activityItems = new Gson().fromJson(jsonarrys, new TypeToken<List<AtyItem>>() {
                }.getType());
                return true;

            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }


    }

    public static void refresh(AtyItem atyItem, int position) {
        activityItems.remove(position);
        activityItems.add(position, atyItem);
        myRecycerAdapter.notifyDataSetChanged();
    }

//    private class GetAtyTask extends AsyncTask<Void,Void,Boolean> {
//
//        @Override
//        protected void onPreExecute() {
//            super.onPreExecute();
////
//
//        }
//
//        @Override
//        protected void onPostExecute(Boolean aBoolean) {
//            super.onPostExecute(aBoolean);
//            if (aBoolean==true){
//
//            }
//        }
//
//        @Override
//        protected Boolean doInBackground(Void... params) {
//            String json="[{\"userId\":\"guangfan\",\"userName\":\"光凡\",\n" +
//                    "\"userIcon\":\"http://img.tuboshu.com/images/article/201410/11/1034/37/201410111034376744_600.jpeg\",\"atyId\":\"1001\",\"atyName\":\"活动1\",\n" +
//                    "\"atyType\":\"dota\",\"atyStartTime\":\"2015-07-01 19:20:18\",\n" +
//                    "\"atyEndTime\":\"2015-07-01 19:20:30\",\"atyPlace\":\"海韵2\",\n" +
//                    "\"atyMembers\":\"6\",\"atyContent\":\"刀塔来来来\",\"atyIsLiked\":\"false\",\n" +
//                    "\"atyIsJoined\":\"false\"},\n" +
//                    "{\"userId\":\"hdchen\",\"userName\":\"hdchen\",\n" +
//                    "\"userIcon\":\"http://p2.pstatp.com/large/6441/3854679476\",\"atyId\":\"1002\",\"atyName\":\"活动2\",\n" +
//                    "\"atyType\":\"dota\",\"atyStartTime\":\"2015-07-01 19:20:18\",\n" +
//                    "\"atyEndTime\":\"2015-07-01 19:20:30\",\"atyPlace\":\"公寓\",\n" +
//                    "\"atyMembers\":\"12\",\"atyContent\":\"自习\",\"atyIsLiked\":\"false\",\n" +
//                    "\"atyIsJoined\":\"false\"},\n" +
//                    "{\"userId\":\"wxm\",\"userName\":\"神大人\",\n" +
//                    "\"userIcon\":\"http://upload.shunwang.com/2013/1225/1387978438783.jpg\",\"atyId\":\"1003\",\"atyName\":\"活动3\",\n" +
//                    "\"atyType\":\"dota\",\"atyStartTime\":\"2015-07-01 19:20:18\",\n" +
//                    "\"atyEndTime\":\"2015-07-01 19:20:30\",\"atyPlace\":\"海韵\",\n" +
//                    "\"atyMembers\":\"12\",\"atyContent\":\"自习\",\"atyIsLiked\":\"false\",\n" +
//                    "\"atyIsJoined\":\"false\"}\n" +
//                    "]";
//            JSONObject object=new JSONObject();
//            try {
//                object.put("action","showActivities");
//                object.put("userId",userId);
//                //String jsonarrys = JsonConnection.getJSON(object.toString());
//                //  Log.i("jsonarray",jsonarrys.toString());
//                activityItems = new Gson().fromJson(json, new TypeToken<List<AtyItem>>() {
//                }.getType());
//                if(activityItems.size()==0)
//                    return false;
//                for (int i=0;i<activityItems.size();i++){
//                    Log.d("Task",activityItems.get(i).toString());
//                }
//                //Log.d("Task",jsonarrys);
//                return true;
//
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//            return null;
//        }
    //}
}
