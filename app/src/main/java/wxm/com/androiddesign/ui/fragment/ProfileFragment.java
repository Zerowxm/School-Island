package wxm.com.androiddesign.ui.fragment;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.afollestad.materialdialogs.MaterialDialog;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import wxm.com.androiddesign.R;
import wxm.com.androiddesign.adapter.MyRecycerAdapter;
import wxm.com.androiddesign.adapter.ProfileAdapter;
import wxm.com.androiddesign.module.AtyItem;
import wxm.com.androiddesign.module.CommentData;
import wxm.com.androiddesign.module.User;
import wxm.com.androiddesign.network.JsonConnection;

/**
 * Created by zero on 2015/6/30.
 */
public class ProfileFragment extends Fragment {
    RecyclerView recyclerView;
    private String userId;
    User user;
    GetProfile getProfile;


    public static ProfileFragment newInstance(String muserId) {
        ProfileFragment fragment = new ProfileFragment();
        Bundle args = new Bundle();
        args.putString("UserId", muserId);
        fragment.setArguments(args);
        return fragment;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup viewGroup, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.profile_layout, viewGroup, false);
        recyclerView = (RecyclerView) v;
        getProfile = new GetProfile(getActivity());
        getProfile.execute(user);
        setupRecyclerView(recyclerView);
        userId = getArguments().getString("UserId");
        return v;
    }

    private void setupRecyclerView(RecyclerView recyclerView) {
        recyclerView.setLayoutManager(new LinearLayoutManager(recyclerView.getContext()));

        recyclerView.setAdapter(new ProfileAdapter(user));
    }

    private class GetProfile extends AsyncTask<User, Void, Boolean> {
        MaterialDialog materialDialog;
        Context context;

        public GetProfile(Context context) {
            this.context = context;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            materialDialog = new MaterialDialog.Builder(context)
                    .title("Loading")
                    .progress(true, 0)
                    .progressIndeterminateStyle(true)
                    .show();
        }

        @Override
        protected void onPostExecute(Boolean result) {
            super.onPostExecute(result);
        }

        @Override
        protected Boolean doInBackground(User... params) {
            JSONObject object = new JSONObject();
            try {
                object.put("action", "showprofile");
                object.put("userId", userId);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            String json = JsonConnection.getJSON(object.toString());
            Log.i("json", "json");
            user = new Gson().fromJson(json, User.class);
            return null;
        }
    }
}
