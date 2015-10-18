package wxm.com.androiddesign.ui.fragment;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import wxm.com.androiddesign.R;
import wxm.com.androiddesign.adapter.GroupAdapter;
import wxm.com.androiddesign.listener.RecyclerItemClickListener;
import wxm.com.androiddesign.module.Group;
import wxm.com.androiddesign.network.JsonConnection;
import wxm.com.androiddesign.ui.CmtAcitivity;

/**
 * Created by zero on 2015/7/8.
 */
public class CmtListFragment extends Fragment {

    @Bind(R.id.recyclerview_list)
    RecyclerView recyclerView;
    @Bind(R.id.swipeRefreshLayout)
    SwipeRefreshLayout mSwipeRefreshLayout;

    List<Group> groups = new ArrayList<>();
    String userId;
    public static CmtListFragment newInstance(String muserId) {
        CmtListFragment fragment = new CmtListFragment();
        Bundle args = new Bundle();
        args.putString("UserId", muserId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        userId = getArguments().getString("UserId");
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup viewGroup, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.refresh_list, viewGroup, false);
        ButterKnife.bind(this,v);
        setupRecyclerView(recyclerView);
        setupSwipeRefreshLayout(mSwipeRefreshLayout);
        return v;
    }

    private void setupRecyclerView(RecyclerView recyclerView) {
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        recyclerView.setHasFixedSize(true);
        new GetAtyTask().execute();

        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(recyclerView.getContext(), new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent = new Intent(getActivity(), CmtAcitivity.class);
                intent.putExtra("ctyId", groups.get(position).getCtyId());
                startActivity(intent);
            }
        }));
    }

    private class GetAtyTask extends AsyncTask<Void,Void,Boolean> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            recyclerView.setAdapter(new GroupAdapter(groups, getActivity()));
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            JSONObject object=new JSONObject();
            try {
                object.put("action","showJoinedCommunities");
                object.put("userId", userId);
                Log.d("jsonarray1", object.toString());
                String jsonarrys = JsonConnection.getJSON(object.toString());
                Log.d("jsonarray1",jsonarrys.toString());
                groups = new Gson().fromJson(jsonarrys, new TypeToken<List<Group>>() {
                }.getType());

                if(groups ==null)
                    return false;
                for (int i=0;i< groups.size();i++){
                    Log.d("Task1", groups.get(i).toString());
                }
                Log.d("Task1",jsonarrys);
                return true;

            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    private void setupSwipeRefreshLayout(SwipeRefreshLayout swipeRefreshLayout) {
        swipeRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_bright,
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
//        if (myRecycerAdapter != null) {
//            //load content
//            mSwipeRefreshLayout.setRefreshing(true);
//            new Handler().postDelayed(new Runnable() {
//                @Override
//                public void run() {
//
//                    myRecycerAdapter.notifyDataSetChanged();
//
//                    mSwipeRefreshLayout.setRefreshing(false);
//                }
//            }, 5000);
//
//        }
//        load complete
        onContentLoadComplete();
    }

    private void onContentLoadComplete() {
        mSwipeRefreshLayout.setRefreshing(false);
    }

    public SwipeRefreshLayout getmSwipeRefreshLayout() {
        return mSwipeRefreshLayout;
    }
}
