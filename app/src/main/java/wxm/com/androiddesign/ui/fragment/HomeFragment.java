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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Interpolator;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

import wxm.com.androiddesign.module.AtyItem;
import wxm.com.androiddesign.adapter.MyRecycerAdapter;
import wxm.com.androiddesign.R;
import wxm.com.androiddesign.network.JsonConnection;
import wxm.com.androiddesign.utils.TransparentToolBar;

/**
 * Created by zero on 2015/6/26.
 */
public class HomeFragment extends Fragment{

    RecyclerView recyclerView;
    Toolbar toolbar;
    static List<AtyItem> activityItems=new ArrayList<AtyItem>();


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
        //String test="{\"atyContent\":\"1\",\"time\":\"1\",\"atyName\":\"1\",\"comment\":\"1\",\"image\":[\"aa\",\"bb\"],\"imageUri\":[{\"uriString\":\"http://www.baidu.com\",\"scheme\":\"NOT CACHED\",\"cachedSsi\":-2,\"cachedFsi\":-2,\"host\":\"NOT CACHED\",\"port\":-2}],\"location\":\"1\",\"plus\":\"1\",\"tag\":\"1\",\"atyImageId\":0,\"photoId\":0}\n";
        String jsonarrys="[{\"atyContent\":\"5\",\"time\":\"1\",\"atyName\":\"1\",\n" +
                "\"comment\":\"1\",\n" +
                "\"image\":[\"cc\",\"dd\"],\n" +
                "\"location\":\"1\",\"plus\":\"1\",\n" +
                "\"tag\":\"1\",\"atyImageId\":0,\"photoId\":0},\n" +
                "{\"atyContent\":\"6\",\"time\":\"1\",\"atyName\":\"1\",\n" +
                "\"comment\":\"1\",\n" +
                "\"image\":[\"ff\",\"dd\"],\n" +
                "\"location\":\"1\",\"plus\":\"1\",\n" +
                "\"tag\":\"1\",\"atyImageId\":0,\"photoId\":0}\n" +
                "]";
        String json="{\"atyContent\":\"1\",\"time\":\"2\",\"atyName\":\"3\",\n" +
                "\"comment\":\"4\",\n" +
                "\"image\":[\"cc\",\"dd\"],\n" +
                "\"location\":\"5\",\"plus\":\"6\",\n" +
                "\"tag\":\"7\",\"atyImageId\":8,\"photoId\":9}";
        String json2="{\"atyContent\":\"2\",\"time\":\"3\",\"atyName\":\"4\",\"comment\":\"5\",\"location\":\"6\",\"plus\":\"7\",\"tag\":\"8\",\"atyImageId\":9,\"photoId\":10}";
        Gson gson=new Gson();
        activityItems=gson.fromJson(jsonarrys,new TypeToken<List<AtyItem>>(){}.getType());
        Log.d("Gson",gson.fromJson(jsonarrys,new TypeToken<List<AtyItem>>(){}.getType()).toString());
        AtyItem atyItem=new AtyItem();
        atyItem=gson.fromJson(json2,AtyItem.class);
        Log.d("Gson",gson.fromJson(json,AtyItem.class).toString());
        //Log.d("Gson",gson.fromJson(test,AtyItem.class).toString());
        Log.d("Gson", atyItem.toString());
        Log.d("Gson",""+gson.toJson(activityItems));
        Log.d("Gson", "" + gson.toJson(new AtyItem()));
        recyclerView.setAdapter(new MyRecycerAdapter(activityItems, (AppCompatActivity) getActivity()));
        RecyclerView.ItemAnimator animator =recyclerView.getItemAnimator();
        animator.setAddDuration(2000);
        animator.setRemoveDuration(1000);
    }


    public static void addActivity(AtyItem atyItem) {
        activityItems.add(atyItem);
        //activityItems.notify();
    }



}
