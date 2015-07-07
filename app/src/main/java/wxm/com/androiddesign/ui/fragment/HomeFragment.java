package wxm.com.androiddesign.ui.fragment;

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

import java.util.ArrayList;

import wxm.com.androiddesign.module.ActivityItemData;
import wxm.com.androiddesign.adapter.MyRecycerAdapter;
import wxm.com.androiddesign.R;

/**
 * Created by zero on 2015/6/26.
 */
public class HomeFragment extends Fragment{

    RecyclerView recyclerView;
    static ArrayList<ActivityItemData> datas=new ArrayList<>();
    static {
        for (int i=0;i<5;i++){
            datas.add(new ActivityItemData(R.drawable.miao,"name","tag","time","atyname","atycontent",R.drawable.miao,"location","0","0"));
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup viewGroup,Bundle savedInstanceState){
        View v =inflater.inflate(R.layout.home_layout,viewGroup,false);
        recyclerView=(RecyclerView)v.findViewById(R.id.recyclerview_activity);
        setupRecyclerView(recyclerView);
        Toolbar toolbar=(Toolbar)v.findViewById(R.id.toolbar);

        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        final ActionBar actionBar=((AppCompatActivity) getActivity()).getSupportActionBar();
        actionBar.setHomeAsUpIndicator(R.drawable.ic_menu);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);
        return v;
    }

    private void setupRecyclerView(RecyclerView recyclerView){
        recyclerView.setLayoutManager(new LinearLayoutManager(recyclerView.getContext()));

        recyclerView.setAdapter(new MyRecycerAdapter(datas,this));
        RecyclerView.ItemAnimator animator =recyclerView.getItemAnimator();
        animator.setAddDuration(2000);
        animator.setRemoveDuration(1000);
    }
}
