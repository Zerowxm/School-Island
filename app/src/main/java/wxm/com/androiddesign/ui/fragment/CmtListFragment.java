package wxm.com.androiddesign.ui.fragment;

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

/**
 * Created by zero on 2015/7/8.
 */
public class CmtListFragment extends Fragment {
    RecyclerView recyclerView;
    List<CtyItem> ctyItems = new ArrayList<>();

    public View onCreateView(LayoutInflater inflater, ViewGroup viewGroup, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.cmt_layout, viewGroup, false);
        recyclerView = (RecyclerView) v;
        new GetAtyTask().execute();
        return v;
    }

    private void setupRecyclerView(RecyclerView recyclerView) {
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        recyclerView.setHasFixedSize(true);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(new CmyAdapter(ctyItems,getActivity()));
        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(recyclerView.getContext(), new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {

            }
        }));
    }

    private class GetAtyTask extends AsyncTask<String,Void,Boolean> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            setupRecyclerView(recyclerView);
        }

        @Override
        protected Boolean doInBackground(String... params) {
            JSONObject object=new JSONObject();
            try {
                object.put("action","showAllCommunity");
                object.put("userId", MyUser.userId);
                String jsonarrys = JsonConnection.getJSON(object.toString());
                //  Log.i("jsonarray",jsonarrys.toString());
                ctyItems = new Gson().fromJson(jsonarrys, new TypeToken<List<CtyItem>>() {
                }.getType());

                if(ctyItems==null)
                    return false;
                for (int i=0;i<ctyItems.size();i++){
                    Log.d("Task", ctyItems.get(i).toString());
                }
                Log.d("Task",jsonarrys);
                return true;

            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }
    }
}
