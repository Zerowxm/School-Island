package wxm.com.androiddesign.ui.fragment;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
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

import wxm.com.androiddesign.R;
import wxm.com.androiddesign.adapter.PhotoAdapter;
import wxm.com.androiddesign.network.JsonConnection;

/**
 * Created by zero on 2015/7/15.
 */
public class PhotoFragment extends Fragment {

    PhotoAdapter myRecycerAdapter;
    RecyclerView recyclerView;
    private String userId;
    public List<String> photoList = new ArrayList<>();

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
        userId = getArguments().getString("UserId");
    }



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v;
        if (photoList==null||photoList.size()==0){
            v = inflater.inflate(R.layout.update_image, container, false);
        }
        else {
            v = inflater.inflate(R.layout.photos_layout, container, false);
            recyclerView = (RecyclerView) v;
            setupRecyclerView(recyclerView);
        }
        return v;
    }

    private void setupRecyclerView(RecyclerView recyclerView) {
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        recyclerView.setHasFixedSize(true);
        new getPhoto(getActivity()).execute();

    }

    private class getPhoto extends AsyncTask<Void, Void, Boolean> {
        Context context;

        public getPhoto(Context context) {
            this.context = context;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected void onPostExecute(Boolean result) {
            super.onPostExecute(result);
            myRecycerAdapter = new PhotoAdapter(photoList, getActivity());
            recyclerView.setAdapter(myRecycerAdapter);

        }

        @Override
        protected Boolean doInBackground(Void... params) {
            JSONObject object = new JSONObject();
            try {
                object.put("action", "showUserAlbum");
                object.put("userId", userId);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            String json = JsonConnection.getJSON(object.toString());
            photoList = new Gson().fromJson(json, new TypeToken<List<String>>() {
            }.getType());
            for (String i : photoList) {
                Log.d("list", i);
            }

            return null;
        }
    }
}
