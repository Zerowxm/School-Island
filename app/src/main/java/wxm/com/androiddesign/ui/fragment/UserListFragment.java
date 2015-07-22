package wxm.com.androiddesign.ui.fragment;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.afollestad.materialdialogs.MaterialDialog;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import wxm.com.androiddesign.R;
import wxm.com.androiddesign.adapter.MsgAdapter;
import wxm.com.androiddesign.adapter.UserAdapter;
import wxm.com.androiddesign.listener.RecyclerItemClickListener;
import wxm.com.androiddesign.module.User;
import wxm.com.androiddesign.network.JsonConnection;
import wxm.com.androiddesign.utils.SpacesItemDecoration;

/**
 * Created by zero on 2015/7/8.
 */
public class UserListFragment extends Fragment {
    RecyclerView recyclerView;

    List<User> UserList=new ArrayList<>();

    String cmtId;


    public static Fragment newInstance(String cmtId) {
        Fragment fragment = new UserListFragment();
        Bundle args = new Bundle();
        args.putString("cmtId",cmtId );
        fragment.setArguments(args);
        return fragment;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup viewGroup, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.list_layout, viewGroup, false);
        cmtId = getArguments().getString("cmtId");
        recyclerView = (RecyclerView) v;
        setupRecyclerView(recyclerView);
        return v;
    }

    private void setupRecyclerView(RecyclerView recyclerView) {
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setHasFixedSize(true);
        recyclerView.addItemDecoration(new SpacesItemDecoration(getResources()));
        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(recyclerView.getContext(), new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                
            }
        }));
        recyclerView.setAdapter(new UserAdapter(UserList,getActivity()));
    }

    private class GetMembers extends AsyncTask<User, Void, Boolean> {
        MaterialDialog materialDialog;
        Context context;

        public GetMembers(Context context) {
            this.context = context;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            materialDialog = new MaterialDialog.Builder(context)
                    .title("Loading")
                    .progress(true, 0)
                    .progressIndeterminateStyle(false)
                    .show();
        }

        @Override
        protected void onPostExecute(Boolean result) {
            super.onPostExecute(result);
            materialDialog.dismiss();
            setupRecyclerView(recyclerView);

        }

        @Override
        protected Boolean doInBackground(User... params) {
            JSONObject object = new JSONObject();
            try {
                object.put("action", "showCmtMembers");
                object.put("cmtId",cmtId);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            String json = JsonConnection.getJSON(object.toString());
            UserList = new Gson().fromJson(json, new TypeToken<List<User>>() {
            }.getType());
            return null;
        }
    }
}
