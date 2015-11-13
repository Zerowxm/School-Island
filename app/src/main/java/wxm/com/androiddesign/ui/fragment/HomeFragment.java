package wxm.com.androiddesign.ui.fragment;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import wxm.com.androiddesign.module.AtyItem;
import wxm.com.androiddesign.adapter.MyRecycerAdapter;
import wxm.com.androiddesign.R;
import wxm.com.androiddesign.module.MyUser;
import wxm.com.androiddesign.network.JsonConnection;
import wxm.com.androiddesign.ui.MainActivity;
import wxm.com.androiddesign.utils.ACache;
import wxm.com.androiddesign.utils.ScrollManager;

/**
 * Created by zero on 2015/6/26.
 */
public class HomeFragment extends Fragment implements AppBarLayout.OnOffsetChangedListener{

    @Bind(R.id.recyclerview_activity)
    RecyclerView recyclerView;
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.appbar)
    AppBarLayout appBarLayout;
    @Bind(R.id.swipeRefreshLayout)
    SwipeRefreshLayout mSwipeRefreshLayout;

    static MyRecycerAdapter myRecycerAdapter;
    static List<AtyItem> activityItems = new ArrayList<AtyItem>();
    private String userId;
    @Bind(R.id.country)
    TextView country;

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

    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int i) {
        mSwipeRefreshLayout.setEnabled(i == 0);
    }

    @Override
    public void onPause() {
        super.onPause();
        appBarLayout.removeOnOffsetChangedListener(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        appBarLayout.addOnOffsetChangedListener(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setHasFixedSize(true);
        recyclerView.invalidate();
//        recyclerView.setAdapter(myRecycerAdapter);
//        myRecycerAdapter.notifyDataSetChanged();
        //recyclerView.getAdapter().notifyDataSetChanged();
        //setupRecyclerView(recyclerView);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        myRecycerAdapter.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
//        super.onSaveInstanceState(outState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup viewGroup, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.home_layout, viewGroup, false);
        ButterKnife.bind(this, v);
        MainActivity.toolbar=toolbar;
        setupToolBar();
        setupSwipeRefreshLayout(mSwipeRefreshLayout);
        setupRecyclerView(recyclerView);
        setRetainInstance(true);
        return v;
    }

    private void setupToolBar(){
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        final ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        actionBar.setHomeAsUpIndicator(R.drawable.ic_menu);
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayShowCustomEnabled(true);
    }

    private class GetAtyTask extends AsyncTask<String, Void, Boolean> {

        MaterialDialog materialDialog;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //mSwipeRefreshLayout.setRefreshing(true);
            materialDialog = new MaterialDialog.Builder(getActivity())
                    .title("Loading")
                    .progress(true, 0)
                    .progressIndeterminateStyle(false)
                    .show();
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            mSwipeRefreshLayout.setRefreshing(false);
            materialDialog.dismiss();
            if (aBoolean == true) {
                AppCompatActivity appCompatActivity=MainActivity.activityWeakReference.get();
                if (appCompatActivity!=null&&!appCompatActivity.isFinishing()){
                    myRecycerAdapter = new MyRecycerAdapter(activityItems,appCompatActivity, "HomeFragment");
                    recyclerView.setAdapter(myRecycerAdapter);
                    mSwipeRefreshLayout.setRefreshing(false);
                }
                //country.setText(LocationServices.City);

            } else {
                Snackbar.make(mSwipeRefreshLayout, "Error", Snackbar.LENGTH_SHORT).show();
            }
        }

        @Override
        protected Boolean doInBackground(String... params) {
            JSONObject object = new JSONObject();
            try {
                object.put("action", "showActivities");
                object.put("userId", MyUser.userId);
                String jsonarrys = JsonConnection.getJSON(object.toString());
                Log.d("jsonArrays",jsonarrys);
                activityItems = new Gson().fromJson(jsonarrys, new TypeToken<List<AtyItem>>() {
                }.getType());

                if (activityItems == null)
                    return false;
                for (int i = 0; i < activityItems.size(); i++) {
                    Log.d("Task", activityItems.get(i).toString());
                }
                Log.d("Task", jsonarrys);
                return true;

            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    private void setupRecyclerView(RecyclerView recyclerView) {
        recyclerView.setLayoutManager(new LinearLayoutManager(recyclerView.getContext()));

        //先读取缓存内容
        ACache activities = ACache.get(getActivity());
        JSONObject object = new JSONObject();
        try {
            object.put("action", "showActivities");
            object.put("userId", MyUser.userId);
            if(activities.getAsString(object.toString())!=null) {
                activityItems = new Gson().fromJson(activities.getAsString(object.toString()), new TypeToken<List<AtyItem>>() {
                }.getType());
                AppCompatActivity appCompatActivity = MainActivity.activityWeakReference.get();
                if (appCompatActivity != null && !appCompatActivity.isFinishing()) {
                    myRecycerAdapter = new MyRecycerAdapter(activityItems, appCompatActivity, "HomeFragment");
                    recyclerView.setAdapter(myRecycerAdapter);
                    mSwipeRefreshLayout.setRefreshing(false);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        new GetAtyTask().execute();

        RecyclerView.ItemAnimator animator = recyclerView.getItemAnimator();
        ScrollManager manager = new ScrollManager();
        manager.attach(recyclerView);
        manager.addView(getActivity().findViewById(R.id.fab), ScrollManager.Direction.DOWN);
        manager.setSwipeRefreshLayout(mSwipeRefreshLayout);
        animator.setAddDuration(2000);
        animator.setRemoveDuration(1000);
    }

    private void setupSwipeRefreshLayout(SwipeRefreshLayout swipeRefreshLayout) {
        swipeRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
        //swipeRefreshLayout.setProgressViewOffset(false, 0, 200);
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
                Log.d("home", "postDelayed");
                setupRecyclerView(recyclerView);
                //mSwipeRefreshLayout.setRefreshing(false);
            }
        }, 0);
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
}
