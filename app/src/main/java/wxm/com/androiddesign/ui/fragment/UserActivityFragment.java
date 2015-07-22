package wxm.com.androiddesign.ui.fragment;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
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

import wxm.com.androiddesign.R;
import wxm.com.androiddesign.adapter.MyRecycerAdapter;
import wxm.com.androiddesign.anim.MyItemAnimator;
import wxm.com.androiddesign.module.AtyItem;
import wxm.com.androiddesign.network.JsonConnection;
import wxm.com.androiddesign.utils.ScrollManager;


/**
 * Created by zero on 2015/6/25.
 */
public class UserActivityFragment extends Fragment {

    public static final int Joined = 0x4;
    public static final int Release = 0x5;

    private int type;
    private String userId;
    RecyclerView recyclerView;
    static MyRecycerAdapter myRecycerAdapter;

    public UserActivityFragment() {

    }

    public static UserActivityFragment newInstance(int type, String muserId) {
        UserActivityFragment fragment = new UserActivityFragment();
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
        type = getArguments().getInt("Type");
        userId = getArguments().getString("UserId");
        Log.d("wxm123", "" + type);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup viewGroup, Bundle savedInstanceState) {
        View v;
        v = inflater.inflate(R.layout.activity_user_fragment, viewGroup, false);
        recyclerView = (RecyclerView) v;
        setupRecyclerView(recyclerView);
        Log.d("wxm123", "" + recyclerView.toString());
        return v;
    }


    private void setupRecyclerView(RecyclerView recyclerView) {
        final LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setItemAnimator(new MyItemAnimator());
        new GetAtyTask().execute(type);

    }



    private class GetAtyTask extends AsyncTask<Integer, Void, Boolean> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            if (aBoolean == true) {
                myRecycerAdapter = new MyRecycerAdapter(activityItems,userId, (AppCompatActivity) getActivity(), "ActivityFragment");
                recyclerView.setAdapter(myRecycerAdapter);
            }
        }

        @Override
        protected Boolean doInBackground(Integer... params) {
            JSONObject object = new JSONObject();
            try {
                switch (params[0]) {
                    case Joined:object.put("action","showJoinedAty");
                        break;
                    case Release:object.put("action","showDistributedAty");
                        break;

                }
                object.put("userId", userId);
                String jsonarrys = JsonConnection.getJSON(object.toString());
                //  Log.i("jsonarray",jsonarrys.toString());
                activityItems = new Gson().fromJson(jsonarrys, new TypeToken<List<AtyItem>>() {
                }.getType());
                if (activityItems.size() == 0)
                    return false;
                for (int i = 0; i < activityItems.size(); i++) {
                    Log.d("Task", activityItems.get(i).toString());
                }
                //Log.d("Task", jsonarrys);
                return true;

            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }
    }
}
