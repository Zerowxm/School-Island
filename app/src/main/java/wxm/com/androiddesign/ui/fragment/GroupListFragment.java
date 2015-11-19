package wxm.com.androiddesign.ui.fragment;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import wxm.com.androiddesign.R;
import wxm.com.androiddesign.adapter.GroupListAdapter;
import wxm.com.androiddesign.listener.RecyclerItemClickListener;
import wxm.com.androiddesign.module.Group;
import wxm.com.androiddesign.network.JsonConnection;
import wxm.com.androiddesign.ui.GroupAcitivity;

/**
 * Created by zero on 2015/7/8.
 */
public class GroupListFragment extends BaseFragment {

    public static final int JOINED = 0x1;
    public static final int OWNED = 0x2;
    public static final int HOT=0x3;
    @Bind(R.id.recyclerview_list)
    RecyclerView recyclerView;

    List<Group> groups = new ArrayList<>();
    String userId;
    int type;
    boolean isProfile=false;
    public static GroupListFragment newInstance(String muserId,int type,boolean isUserProfile) {
        GroupListFragment fragment = new GroupListFragment();
        Bundle args = new Bundle();
        args.putString("UserId", muserId);
        args.putInt("type",type);
        args.putBoolean("isProfile",isUserProfile);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        userId = getArguments().getString("UserId");
        type = getArguments().getInt("type");
        isProfile=getArguments().getBoolean("isProfile");
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup viewGroup, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.refresh_list, viewGroup, false);
        ButterKnife.bind(this, v);
        setupRecyclerView(recyclerView);
        mSwipeRefreshLayout=(SwipeRefreshLayout)v.findViewById(R.id.swipeRefreshLayout);
        if (!isProfile){
            setupSwipeRefreshLayout();
        }else {
            mSwipeRefreshLayout.setEnabled(false);
            //mSwipeRefreshLayout.setVisibility(View.GONE);
        }

        return v;
    }

    private void setupRecyclerView(RecyclerView recyclerView) {
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, LinearLayout.VERTICAL));

        recyclerView.setHasFixedSize(true);
        new GetAtyTask().execute();

        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(recyclerView.getContext(), new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                int pos=position-2;
                Intent intent = new Intent(getActivity(), GroupAcitivity.class);
                Log.d("Task2",groups.get(pos).getCtyId());
                intent.putExtra("groupId", groups.get(pos).getCtyId());
                intent.putExtra("groupName", groups.get(pos).getCtyName());
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
            recyclerView.setAdapter(new GroupListAdapter(groups, getActivity(),type));
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            JSONObject object=new JSONObject();
            try {
                switch (type){
                    case JOINED:
                        object.put("action","showJoinedCommunities");
                        break;
                    case OWNED:
                        object.put("action","showOwnedCommunities");
                        break;
                    case HOT:
                        object.put("action","showHotCommunities");
                        break;
                }
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

    private void setupSwipeRefreshLayout() {
        mSwipeRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {

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


}
