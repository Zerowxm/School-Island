package wxm.com.androiddesign.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import wxm.com.androiddesign.module.ActivityItemData;
import wxm.com.androiddesign.adapter.MyRecycerAdapter;
import wxm.com.androiddesign.R;

/**
 * Created by zero on 2015/6/25.
 */
public class ActivityFragment extends Fragment{

    RecyclerView recyclerView;
    MyRecycerAdapter myRecycerAdapter;
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
        recyclerView=(RecyclerView)v;
        setupRecyclerView(recyclerView);
        return v;
    }

    private void setupRecyclerView(RecyclerView recyclerView){
        recyclerView.setLayoutManager(new LinearLayoutManager(recyclerView.getContext()));

        recyclerView.setAdapter(new MyRecycerAdapter(datas));
    }
}