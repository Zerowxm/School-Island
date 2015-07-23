package wxm.com.androiddesign.ui.fragment;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
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
import java.util.LinkedList;
import java.util.List;

import wxm.com.androiddesign.R;
import wxm.com.androiddesign.adapter.CmyAdapter;
import wxm.com.androiddesign.adapter.MyHomeRecycerAdapter;
import wxm.com.androiddesign.listener.RecyclerItemClickListener;
import wxm.com.androiddesign.module.AtyItem;
import wxm.com.androiddesign.module.CtyItem;
import wxm.com.androiddesign.module.MyUser;
import wxm.com.androiddesign.network.JsonConnection;
import wxm.com.androiddesign.services.LocationServices;
import wxm.com.androiddesign.ui.CtyAcitivity;

/**
 * Created by zero on 2015/7/8.
 */
public class CmtListFragment extends Fragment {
    RecyclerView recyclerView;
    List<CtyItem> ctyItems = new ArrayList<>();
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
        View v = inflater.inflate(R.layout.cmt_layout, viewGroup, false);
        recyclerView = (RecyclerView) v;
        setupRecyclerView(recyclerView);
        return v;
    }

    private void setupRecyclerView(RecyclerView recyclerView) {
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        recyclerView.setHasFixedSize(true);
        new GetAtyTask().execute();
        //recyclerView.setItemAnimator(new DefaultItemAnimator());

        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(recyclerView.getContext(), new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent = new Intent(getActivity(), CtyAcitivity.class);
                intent.putExtra("ctyId",ctyItems.get(position).getCtyId());
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
            recyclerView.setAdapter(new CmyAdapter(ctyItems, getActivity()));
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            JSONObject object=new JSONObject();
            try {
                object.put("action","showAllCommunities");
                object.put("userId", userId);
                Log.d("jsonarray1", object.toString());
                String jsonarrys = JsonConnection.getJSON(object.toString());
                Log.d("jsonarray1",jsonarrys.toString());
                ctyItems = new Gson().fromJson(jsonarrys, new TypeToken<List<CtyItem>>() {
                }.getType());

                if(ctyItems==null)
                    return false;
                for (int i=0;i<ctyItems.size();i++){
                    Log.d("Task1", ctyItems.get(i).toString());
                }
                Log.d("Task1",jsonarrys);
                return true;

            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }
    }
}
