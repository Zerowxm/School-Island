package wxm.com.androiddesign.ui.fragment;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.afollestad.materialdialogs.MaterialDialog;
import com.easemob.EMEventListener;
import com.easemob.EMNotifierEvent;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import wxm.com.androiddesign.R;
import wxm.com.androiddesign.adapter.ChatHistoryAdapter;
import wxm.com.androiddesign.adapter.CommentItemAdapter;
import wxm.com.androiddesign.adapter.NotifyAdapter;
import wxm.com.androiddesign.module.ChatItem;
import wxm.com.androiddesign.module.CommentItem;
import wxm.com.androiddesign.module.MyUser;
import wxm.com.androiddesign.module.Notify;
import wxm.com.androiddesign.network.JsonConnection;

/**
 * Created by zero on 2015/7/8.
 */
public class ListFragment extends Fragment implements EMEventListener {
    public static final int COMMENT=1;
    public static final int CHAT=2;
    public static final int NOTIFY=3;
    @Bind(R.id.recyclerview_list)
    RecyclerView recyclerView;

    ChatHistoryAdapter chatHistoryAdapter;
    List<Notify> mNotifyList;
    List<CommentItem> mCommentList;
    List<ChatItem> mChatItemList;

    int type;


    public static Fragment newInstance(int type) {
        Fragment fragment = new ListFragment();
        Bundle args = new Bundle();
        args.putInt("Type", type);
        fragment.setArguments(args);
        return fragment;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup viewGroup, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.list_layout, viewGroup, false);
        type = getArguments().getInt("Type");
        ButterKnife.bind(this, v);
        new getMsg(getActivity()).execute();
        setupRecyclerView(recyclerView);
        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        setupRecyclerView(recyclerView);
    }

    private void setupRecyclerView(RecyclerView recyclerView) {
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setHasFixedSize(true);
        //recyclerView.addItemDecoration(new SpacesItemDecoration(getResources()));
    }

    @Override
    public void onEvent(EMNotifierEvent emNotifierEvent) {
        Log.d("datachange", "change");
        chatHistoryAdapter.notifyDataSetChanged();
    }

    private class getMsg extends AsyncTask<Void, Void, Boolean> {
        Context context;

        public getMsg(Context context) {
            this.context = context;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Boolean result) {
            super.onPostExecute(result);
            switch (type){
                case ListFragment.CHAT:
                    chatHistoryAdapter = new ChatHistoryAdapter(mChatItemList,getActivity());
                    recyclerView.setAdapter(chatHistoryAdapter);
                    break;
                case COMMENT:
                    recyclerView.setAdapter(new CommentItemAdapter(mCommentList));
                    break;
                case NOTIFY:
                    recyclerView.setAdapter(new NotifyAdapter(mNotifyList));
                    break;
                default:
                    break;
            }
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            JSONObject object = new JSONObject();
//            try {
//
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//
            try {
                String json;
                switch (type){
                    case ListFragment.CHAT:
                        object.put("action","showChatlist");
                        object.put("easemobId", MyUser.getEasemobId());
                        json = JsonConnection.getJSON(object.toString());
                        mChatItemList = new Gson().fromJson(json, new TypeToken<List<ChatItem>>() {
                        }.getType());
                        break;
                    case COMMENT:
                        object.put("action","showUserComments");
                        object.put("userId",MyUser.userId);
                        object.put("easemobId", MyUser.getEasemobId());
                        json = JsonConnection.getJSON(object.toString());
                        mCommentList = new Gson().fromJson(json, new TypeToken<List<CommentItem>>() {
                        }.getType());
                        break;
                    case NOTIFY:
                        object.put("action","showAllNotifications");
                        object.put("easemobId", MyUser.getEasemobId());
                        json = JsonConnection.getJSON(object.toString());
                        mNotifyList = new Gson().fromJson(json, new TypeToken<List<Notify>>() {
                        }.getType());
                        break;
                    default:
                        break;
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }
    }
}
