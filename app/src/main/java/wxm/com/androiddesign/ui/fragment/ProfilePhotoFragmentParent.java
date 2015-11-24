package wxm.com.androiddesign.ui.fragment;

import android.app.Fragment;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
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
 * Created by Zero on 11/23/2015.
 */
public class ProfilePhotoFragmentParent extends android.support.v4.app.Fragment{
    private String userId;
    public List<String> photoList = new ArrayList<>();

    public static ProfilePhotoFragmentParent newInstance(String muserId) {
        ProfilePhotoFragmentParent fragment = new ProfilePhotoFragmentParent();
        Bundle args = new Bundle();
        args.putString("UserId", muserId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        userId = getArguments().getString("UserId");
        new getPhoto(getActivity()).execute();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view =inflater.inflate(R.layout.profile_photo,container,false);
        return view;
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
            if (photoList==null||photoList.size()==0){
                getChildFragmentManager().beginTransaction().replace(R.id.content,new UpdatePhoto()).commitAllowingStateLoss();
            }else {
                getChildFragmentManager().beginTransaction().replace(R.id.content,PhotoFragment.newInstance(userId)).commitAllowingStateLoss();

            }
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
