package wxm.com.androiddesign.ui.fragment;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import wxm.com.androiddesign.R;
import wxm.com.androiddesign.adapter.CmtAtyAdapter;
import wxm.com.androiddesign.adapter.CmyAdapter;
import wxm.com.androiddesign.adapter.MyRecycerAdapter;
import wxm.com.androiddesign.listener.RecyclerItemClickListener;
import wxm.com.androiddesign.module.AtyItem;
import wxm.com.androiddesign.module.MyUser;
import wxm.com.androiddesign.network.JsonConnection;
import wxm.com.androiddesign.ui.DetailActivity;

/**
 * Created by zero on 2015/7/22.
 */
public class CmtAtyFragment extends Fragment {

    RecyclerView recyclerView;
    List<AtyItem> atyItemList= new ArrayList<>();
    String cmtId="";

    public static CmtAtyFragment newInstance(String cmtId){
        CmtAtyFragment cmtAtyFragment=new CmtAtyFragment();
        Bundle args= new Bundle();
        args.putString("cmtId",cmtId);
        cmtAtyFragment.setArguments(args);
        return cmtAtyFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
         cmtId = getArguments().getString("cmtId");

    }

    public View onCreateView(LayoutInflater inflater, ViewGroup viewGroup, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.list_layout, viewGroup, false);
        recyclerView = (RecyclerView) v;
        setupRecyclerView(recyclerView);
        return v;
    }

    private void setupRecyclerView(RecyclerView recyclerView) {
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(),3));
        recyclerView.setHasFixedSize(true);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        new GetAtyTask().execute();
        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(recyclerView.getContext(), new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent=new Intent(getActivity(), DetailActivity.class);
                intent.putExtra("AtyId",atyItemList.get(position).getAtyId());
                intent.putExtra("com.wxm.com.androiddesign.module.ActivityItemData", atyItemList.get(position));
                intent.putExtra("position", position);
                intent.putExtra("userId", MyUser.userId);
                getActivity().startActivity(intent);
            }
        }));
    }

    private class GetAtyTask extends AsyncTask<Void, Void, Boolean> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            if (aBoolean == true) {
                CmtAtyAdapter myRecycerAdapter=new CmtAtyAdapter(atyItemList,getActivity());
                recyclerView.setAdapter(myRecycerAdapter);
            }
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            JSONObject object = new JSONObject();
            try {

                object.put("cmtId", cmtId);
                String jsonarrys = JsonConnection.getJSON(object.toString());
                atyItemList = new Gson().fromJson(jsonarrys, new TypeToken<List<AtyItem>>() {
                }.getType());
                if (atyItemList.size() == 0)
                    return false;
                for (int i = 0; i < atyItemList.size(); i++) {
                    Log.d("Task", atyItemList.get(i).toString());
                }
                return true;

            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }
    }
}
