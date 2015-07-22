package wxm.com.androiddesign.ui.fragment;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import wxm.com.androiddesign.adapter.PhotoAdapter;

import wxm.com.androiddesign.anim.MyItemAnimator;

import wxm.com.androiddesign.adapter.ScoreAdapter;
import wxm.com.androiddesign.anim.MyItemAnimator;
import wxm.com.androiddesign.module.Score;

import wxm.com.androiddesign.module.User;
import wxm.com.androiddesign.network.JsonConnection;
import wxm.com.androiddesign.utils.ScrollManager;

/**
 * Created by zero on 2015/7/15.
 */
public class ScoreFragment extends Fragment {

    List<Score> scoreList = new ArrayList<>();
    ScoreAdapter myRecycerAdapter;
    RecyclerView recyclerView;
    private String userId;


    public static ScoreFragment newInstance(String muserId) {
        ScoreFragment fragment = new ScoreFragment();
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

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.score_layout, container, false);

        recyclerView = (RecyclerView) v;
        setupRecyclerView(recyclerView);
        return v;

    }

    private void setupRecyclerView(RecyclerView recyclerView) {
        recyclerView.setLayoutManager(new LinearLayoutManager(recyclerView.getContext()));
        recyclerView.setHasFixedSize(true);
        new getScore(getActivity()).execute();

    }

    private class getScore extends AsyncTask<Void, Void, Boolean> {
        MaterialDialog materialDialog;
        Context context;

        public getScore(Context context) {

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
            materialDialog.dismiss();
            myRecycerAdapter = new ScoreAdapter(scoreList);
            recyclerView.setAdapter(myRecycerAdapter);

        }

        @Override
        protected Boolean doInBackground(Void... params) {


            JSONObject object = new JSONObject();
            try {
                object.put("action", "showCredit");

                object.put("userId", userId);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            String json = JsonConnection.getJSON(object.toString());

            scoreList = new Gson().fromJson(json, new TypeToken<List<Score>>() {
            }.getType());
            return null;
        }
    }
}
