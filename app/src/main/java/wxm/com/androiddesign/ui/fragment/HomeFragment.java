package wxm.com.androiddesign.ui.fragment;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.app.SearchManager;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Interpolator;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import wxm.com.androiddesign.listener.RecyclerItemClickListener;
import wxm.com.androiddesign.module.AtyItem;
import wxm.com.androiddesign.adapter.MyRecycerAdapter;
import wxm.com.androiddesign.R;
import wxm.com.androiddesign.network.JsonConnection;
import wxm.com.androiddesign.ui.ReleaseActivity;
import wxm.com.androiddesign.utils.ScrollManager;
import wxm.com.androiddesign.utils.TransparentToolBar;

/**
 * Created by zero on 2015/6/26.
 */
public class HomeFragment extends Fragment implements AdapterView.OnItemSelectedListener {

    RecyclerView recyclerView;
    Toolbar toolbar;
    static MyRecycerAdapter myRecycerAdapter;
    SwipeRefreshLayout mSwipeRefreshLayout;
    Spinner mSpinner;
    static List<AtyItem> activityItems = new ArrayList<AtyItem>();
    private String userId;


    @Override
    public void setHasOptionsMenu(boolean hasMenu) {
        super.setHasOptionsMenu(true);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        userId = getArguments().getString("UserId");
        setHasOptionsMenu(true);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        try {
            Field childFragmentManager = Fragment.class.getDeclaredField("mChildFragmentManager");
            childFragmentManager.setAccessible(true);
            childFragmentManager.set(this, null);

        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public static HomeFragment newInstance(String muserId) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putString("UserId", muserId);
        fragment.setArguments(args);
        return fragment;
    }
    private class GetAtyTask extends AsyncTask<String,Void,Boolean>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mSwipeRefreshLayout.setRefreshing(true);
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            if (aBoolean==true){

                myRecycerAdapter = new MyRecycerAdapter(activityItems,(AppCompatActivity) getActivity(), "HomeFragment");
                recyclerView.setAdapter(myRecycerAdapter);
                mSwipeRefreshLayout.setRefreshing(false);
            }
        }

        @Override
        protected Boolean doInBackground(String... params) {
            JSONObject object=new JSONObject();
            try {
                object.put("action","showActivities");
                String jsonarrys = JsonConnection.getJSON(object.toString());
                activityItems = new Gson().fromJson(jsonarrys, new TypeToken<List<AtyItem>>() {
                }.getType());
                for (int i=0;i<activityItems.size();i++){
                    Log.d("Task",activityItems.get(i).toString());
                }
                Log.d("Task",jsonarrys);
                return true;

            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup viewGroup, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.home_layout, viewGroup, false);
        recyclerView = (RecyclerView) v.findViewById(R.id.recyclerview_activity);
        mSpinner = (Spinner) v.findViewById(R.id.spinner);
        toolbar = (Toolbar) v.findViewById(R.id.toolbar);
        mSwipeRefreshLayout = (SwipeRefreshLayout) v.findViewById(R.id.swipeRefreshLayout);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        final ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        actionBar.setHomeAsUpIndicator(R.drawable.ic_menu);
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayShowCustomEnabled(true);

        setupSwipeRefreshLayout(mSwipeRefreshLayout);
        setupRecyclerView(recyclerView);
        setupSpinner(mSpinner);
        new GetAtyTask().execute();
        Log.d("home","onCreateView");
        return v;
    }

    private void setupData() {

    }

    private class getData extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
        }

        @Override
        protected Void doInBackground(Void... params) {
            myRecycerAdapter.notifyDataSetChanged();
            return null;
        }
    }

    private void setupSpinner(Spinner spinner) {
        List<String> spinnerarry = new ArrayList<String>();
        spinnerarry.add("厦门");

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), R.layout.spinner_item, spinnerarry);
        // ArrayAdapter<CharSequence>adapter=ArrayAdapter.createFromResource(getActivity(),R.array.action_bar_spinner,
        //         R.layout.spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);
    }

    private void setupRecyclerView(RecyclerView recyclerView) {
        recyclerView.setLayoutManager(new LinearLayoutManager(recyclerView.getContext()));

        //String test="{\"atyContent\":\"1\",\"time\":\"1\",\"atyName\":\"1\",\"comment\":\"1\",\"image\":[\"aa\",\"bb\"],\"imageUri\":[{\"uriString\":\"http://www.baidu.com\",\"scheme\":\"NOT CACHED\",\"cachedSsi\":-2,\"cachedFsi\":-2,\"host\":\"NOT CACHED\",\"port\":-2}],\"location\":\"1\",\"plus\":\"1\",\"tag\":\"1\",\"atyImageId\":0,\"photoId\":0}\n";
//        String jsonarrys =
//                "[{\"atyContent\":\"content1\",\"atyStartTime\":\"starttime1\",\"atyEndTime\":\"endtime1\",\"atyName\":\"name1\",\n" +
//                        "\"comment\":\"1\",\"atyPlused\":\"false\",\"atyJoined\":\"false\",\"userId\":\"aaa\",\n" +
//                        "\"atyAlbum\":[\"http://imgsrc.baidu.com/forum/w%3D580/sign=b9fe30609158d109c4e3a9bae159ccd0/cee4762c11dfa9eceeb9050961d0f703908fc1d4.jpg\",\"http://imgsrc.baidu.com/forum/w%3D580/sign=b9fe30609158d109c4e3a9bae159ccd0/cee4762c11dfa9eceeb9050961d0f703908fc1d4.jpg\"],\n" +
//                        "\"atyPlace\":\"place1\",\"atyPlus\":\"1\",\"atyComment\":\"1\",\"atyMembers\":\"1\",\n" +
//                        "\"atyType\":\"type1\",\"atyImageId\":0,\"photoId\":0},\n" +
//
//                        "{\"atyContent\":\"content2\",\"atyStartTime\":\"starttime2\",\"atyEndTime\":\"endtime2\",\"atyName\":\"name2\",\n" +
//                        "\"comment\":\"1\",\"atyPlused\":\"false\",\"atyJoined\":\"false\",\"userId\":\"aaa\",\n" +
//                        "\"atyAlbum\":[\"http://imgsrc.baidu.com/forum/w%3D580/sign=b9fe30609158d109c4e3a9bae159ccd0/cee4762c11dfa9eceeb9050961d0f703908fc1d4.jpg\",\"http://imgsrc.baidu.com/forum/w%3D580/sign=b9fe30609158d109c4e3a9bae159ccd0/cee4762c11dfa9eceeb9050961d0f703908fc1d4.jpg\"],\n" +
//
//                        "\"atyPlace\":\"place2\",\"atyPlus\":\"1\",\"atyComment\":\"1\",\"atyMembers\":\"1\",\n" +
//                        "\"atyType\":\"tyoe2\",\"atyImageId\":0,\"photoId\":0}" +
//                        "]";
        String json = "{\"atyContent\":\"1\",\"time\":\"2\",\"atyName\":\"3\",\n" +
                "\"comment\":\"4\",\n" +
                "\"image\":[\"cc\",\"dd\"],\n" +
                "\"location\":\"5\",\"plus\":\"6\",\n" +
                "\"tag\":\"7\",\"atyImageId\":8,\"photoId\":9}";
        String json2 = "{\"atyContent\":\"2\",\"time\":\"3\",\"atyName\":\"4\",\"comment\":\"5\",\"location\":\"6\",\"plus\":\"7\",\"tag\":\"8\",\"atyImageId\":9,\"photoId\":10}";
        Gson gson = new Gson();
        //activityItems = gson.fromJson(jsonarrys, new TypeToken<ArrayList<AtyItem>>() {
       // }.getType());
       // Log.d("Gson", gson.fromJson(jsonarrys, new TypeToken<List<AtyItem>>() {
        //}.getType()).toString());
        //AtyItem atyItem = new AtyItem();
        //atyItem = gson.fromJson(json2, AtyItem.class);
        //Log.d("Gson", gson.fromJson(json, AtyItem.class).toString());
        //Log.d("Gson",gson.fromJson(test,AtyItem.class).toString());
        //Log.d("Gson", atyItem.toString());
        Log.d("Gson", "" + gson.toJson(activityItems));
        //Log.d("Gson", "" + gson.toJson(new AtyItem()));
        myRecycerAdapter = new MyRecycerAdapter(activityItems,(AppCompatActivity) getActivity(), "HomeFragment");
        recyclerView.setAdapter(myRecycerAdapter);
        RecyclerView.ItemAnimator animator = recyclerView.getItemAnimator();
        ScrollManager manager = new ScrollManager();
        manager.attach(recyclerView);
        manager.addView(getActivity().findViewById(R.id.fab), ScrollManager.Direction.DOWN);
        manager.setSwipeRefreshLayout(mSwipeRefreshLayout);
        animator.setAddDuration(2000);
        animator.setRemoveDuration(1000);
    }

    private void setupSwipeRefreshLayout(SwipeRefreshLayout swipeRefreshLayout) {
        swipeRefreshLayout.setColorScheme(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
        swipeRefreshLayout.setProgressViewOffset(false, 0, 150);
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
                Log.d("home","postDelayed");

                //myRecycerAdapter.notifyDataSetChanged();

                mSwipeRefreshLayout.setRefreshing(false);
            }
        }, 5000);
        //load complete

        onContentLoadComplete();
    }

    private void onContentLoadComplete() {
        mSwipeRefreshLayout.setRefreshing(false);
    }

    public static void addActivity(AtyItem atyItem) {
        activityItems.add(0, atyItem);
        myRecycerAdapter.notifyDataSetChanged();
    }

    public static void refresh(AtyItem atyItem, int position) {
        activityItems.remove(position);
        activityItems.add(position, atyItem);
        myRecycerAdapter.notifyDataSetChanged();
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//        Log.d("spinner",view.toString()+position+"|"+id);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        Log.d("spinner", "nothing" + parent.getId());
    }
}
