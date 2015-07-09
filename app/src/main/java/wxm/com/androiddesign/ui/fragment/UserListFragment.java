package wxm.com.androiddesign.ui.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import wxm.com.androiddesign.R;
import wxm.com.androiddesign.adapter.CmyAdapter;
import wxm.com.androiddesign.adapter.UserAdapter;
import wxm.com.androiddesign.utils.SpacesItemDecoration;

/**
 * Created by zero on 2015/7/8.
 */
public class UserListFragment extends Fragment {
    RecyclerView recyclerView;
    public View onCreateView(LayoutInflater inflater,ViewGroup viewGroup,Bundle savedInstanceState){
        View v =inflater.inflate(R.layout.cmt_layout,viewGroup,false);
        recyclerView=(RecyclerView)v;
        setupRecyclerView(recyclerView);
        return v;
    }

    private void setupRecyclerView(RecyclerView recyclerView){
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setHasFixedSize(true);
        recyclerView.addItemDecoration(new SpacesItemDecoration(getResources()));
        recyclerView.setAdapter(new UserAdapter());
    }
}
