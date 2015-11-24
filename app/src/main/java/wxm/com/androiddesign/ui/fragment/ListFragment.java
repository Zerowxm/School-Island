package wxm.com.androiddesign.ui.fragment;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.AppBarLayout;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.easemob.EMEventListener;
import com.easemob.EMNotifierEvent;
import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMContact;
import com.easemob.chat.EMConversation;
import com.easemob.chat.EMGroup;
import com.easemob.chat.EMGroupManager;
import com.easemob.chat.EMMessage;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import butterknife.Bind;
import butterknife.ButterKnife;
import wxm.com.androiddesign.R;
import wxm.com.androiddesign.adapter.ChatHistoryAdapter;
import wxm.com.androiddesign.adapter.CommentItemAdapter;
import wxm.com.androiddesign.adapter.GroupChatHistoryAdapter;
import wxm.com.androiddesign.adapter.NotifyAdapter;
import wxm.com.androiddesign.module.ChatItem;
import wxm.com.androiddesign.module.CommentItem;
import wxm.com.androiddesign.module.GroupChatItem;
import wxm.com.androiddesign.module.MyUser;
import wxm.com.androiddesign.module.Notify;
import wxm.com.androiddesign.network.JsonConnection;
import wxm.com.androiddesign.utils.SpacesItemDecoration;

/**
 * Created by zero on 2015/7/8.
 */
public class ListFragment extends Fragment implements EMEventListener,AppBarLayout.OnOffsetChangedListener{
    public static final int COMMENT=1;
    public static final int CHAT=2;
    public static final int NOTIFY=3;
    public static final int GROUP_CHAT=4;
    @Bind(R.id.recyclerview_list)
    RecyclerView recyclerView;
    @Bind(R.id.swipeRefreshLayout)
    SwipeRefreshLayout mSwipeRefreshLayout;

    ChatHistoryAdapter chatHistoryAdapter;
    GroupChatHistoryAdapter groupChatHistoryAdapter;
    List<Notify> mNotifyList;
    List<CommentItem> mCommentList;
    List<ChatItem> mChatItemList;
    List<GroupChatItem> mGroupChatItemList;
    private Map<String, EMContact> contactList;

    int type;
    boolean shouldRefresh = true;

    public static Fragment newInstance(int type) {
        Fragment fragment = new ListFragment();
        Bundle args = new Bundle();
        args.putInt("Type", type);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int i) {
        mSwipeRefreshLayout.setEnabled(i == 0);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup viewGroup, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.list_layout, viewGroup, false);
        type = getArguments().getInt("Type");
        ButterKnife.bind(this, v);
        setupSwipeRefreshLayout(mSwipeRefreshLayout);
        setupRecyclerView(recyclerView);
        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        setupRecyclerView(recyclerView);
    }

    @Override
    public void onPause() {
        super.onPause();
        shouldRefresh = true;
    }

    private void setupRecyclerView(RecyclerView recyclerView) {
        if(shouldRefresh) {
            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
            recyclerView.setHasFixedSize(true);
            recyclerView.addItemDecoration(new SpacesItemDecoration(getActivity()));
            new getMsg(getActivity()).execute();
            shouldRefresh = false;
        }
    }

    @Override
    public void onEvent(EMNotifierEvent emNotifierEvent) {
        Log.d("datachange", "change");
        if(chatHistoryAdapter!=null) {
            chatHistoryAdapter.notifyDataSetChanged();
        }
        if(groupChatHistoryAdapter!=null){
            groupChatHistoryAdapter.notifyDataSetChanged();
        }
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
//                    chatHistoryAdapter = new ChatHistoryAdapter(mChatItemList,loadUsersWithRecentChat(),getActivity());
                    chatHistoryAdapter = new ChatHistoryAdapter(mChatItemList,getActivity());
                    recyclerView.setAdapter(chatHistoryAdapter);
                    break;
                case ListFragment.GROUP_CHAT:
                    groupChatHistoryAdapter = new GroupChatHistoryAdapter(mGroupChatItemList,getActivity());
                    recyclerView.setAdapter(groupChatHistoryAdapter);
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
            String json="";
            try {
                switch (type){
                    case ListFragment.CHAT:
                        object.put("action","showChatlist");
                        object.put("easemobId", MyUser.getEasemobId());
                        json = JsonConnection.getJSON(object.toString());
                        mChatItemList = new Gson().fromJson(json, new TypeToken<List<ChatItem>>() {
                        }.getType());
                        break;
                    case ListFragment.GROUP_CHAT:
                        object.put("action","showGroupChatlist");
                        object.put("userId", MyUser.userId);
                        object.put("easemobId", MyUser.getEasemobId());
                        json = JsonConnection.getJSON(object.toString());
                        mGroupChatItemList = new Gson().fromJson(json, new TypeToken<List<GroupChatItem>>() {
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
                Log.d("jsonarray",json);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }
    }
    private void setupSwipeRefreshLayout(SwipeRefreshLayout swipeRefreshLayout) {
        swipeRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
        //swipeRefreshLayout.setProgressViewOffset(false, 0, 200);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {

            @Override
            public void onRefresh() {
                refreshContent();
            }
        });
    }

    private void refreshContent() {
        //load content
        mSwipeRefreshLayout.setRefreshing(true);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Log.d("home", "postDelayed");
                setupRecyclerView(recyclerView);
                Log.d("refresh", "freshListFragment");
                setupRecyclerView(recyclerView);
                //mSwipeRefreshLayout.setRefreshing(false);
            }
        }, 0);
        //load complete

        onContentLoadComplete();
    }

    private void onContentLoadComplete() {
        mSwipeRefreshLayout.setRefreshing(false);
    }

    /**
     * 获取有聊天记录的users和groups
     *
     */
    private List<EMContact> loadUsersWithRecentChat() {
        List<EMContact> resultList = new ArrayList<EMContact>();
        //获取有聊天记录的users，不包括陌生人
        //contactList = ((DemoHXSDKModel) new HXSDKModel()).getContactList();
        for (EMContact user : contactList.values()) {
            EMConversation conversation = EMChatManager.getInstance().getConversation(user.getUsername());
            if (conversation.getMsgCount() > 0) {
                resultList.add(user);
            }
        }
        for(EMGroup group : EMGroupManager.getInstance().getAllGroups()){
            EMConversation conversation = EMChatManager.getInstance().getConversation(group.getGroupId());
            if(conversation.getMsgCount() > 0){
                resultList.add(group);
            }

        }

        // 排序
        sortUserByLastChatTime(resultList);
        return resultList;
    }

    /**
     * 根据最后一条消息的时间排序
     *
     */
    private void sortUserByLastChatTime(List<EMContact> contactList) {
        Collections.sort(contactList, new Comparator<EMContact>() {
            @Override
            public int compare(final EMContact user1, final EMContact user2) {
                EMConversation conversation1 = EMChatManager.getInstance().getConversation(user1.getUsername());
                EMConversation conversation2 = EMChatManager.getInstance().getConversation(user2.getUsername());

                EMMessage user2LastMessage = conversation2.getLastMessage();
                EMMessage user1LastMessage = conversation1.getLastMessage();
                if (user2LastMessage.getMsgTime() == user1LastMessage.getMsgTime()) {
                    return 0;
                } else if (user2LastMessage.getMsgTime() > user1LastMessage.getMsgTime()) {
                    return 1;
                } else {
                    return -1;
                }
            }

        });
    }
}
