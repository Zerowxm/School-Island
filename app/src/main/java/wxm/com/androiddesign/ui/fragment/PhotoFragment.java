package wxm.com.androiddesign.ui.fragment;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
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

import java.util.List;

import wxm.com.androiddesign.R;
import wxm.com.androiddesign.adapter.MyRecycerAdapter;
import wxm.com.androiddesign.adapter.PhotoAdapter;
import wxm.com.androiddesign.anim.MyItemAnimator;
import wxm.com.androiddesign.module.AtyItem;
import wxm.com.androiddesign.module.User;
import wxm.com.androiddesign.network.JsonConnection;
import wxm.com.androiddesign.utils.ScrollManager;

/**
 * Created by zero on 2015/7/15.
 */
public class PhotoFragment extends Fragment {

    PhotoAdapter myRecycerAdapter;
    private String userId;
    public List<String> photoList;

    public static PhotoFragment newInstance(String muserId) {
        PhotoFragment fragment = new PhotoFragment();
        Bundle args = new Bundle();
        args.putString("UserId", muserId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        userId = getArguments().getString("UseId");
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    private void setupRecyclerView(RecyclerView recyclerView) {
        recyclerView.setLayoutManager(new LinearLayoutManager(recyclerView.getContext()));
        recyclerView.setHasFixedSize(true);
        recyclerView.setItemAnimator(new MyItemAnimator());
        ScrollManager manager = new ScrollManager();
        manager.attach(recyclerView);
        manager.addView(getActivity().findViewById(R.id.fab), ScrollManager.Direction.DOWN);


        myRecycerAdapter = new PhotoAdapter();
        recyclerView.setAdapter(myRecycerAdapter);
    }
    private class getPhoto extends AsyncTask<User, Void, Boolean> {
        MaterialDialog materialDialog;
        Context context;

        public getPhoto(Context context) {
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
                    object.put("action", "showphoto");
                    object.put("userId",userId);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                String json = JsonConnection.getJSON(object.toString());
                photoList = new Gson().fromJson(json, new TypeToken<List<String>>() {
                }.getType());
            return null;
        }
    }
}
