package wxm.com.androiddesign.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import wxm.com.androiddesign.R;
import wxm.com.androiddesign.module.ChatItem;

/**
 * Created by zero on 2015/6/30.
 */
public class ChatHistoryAdapter extends RecyclerView.Adapter<ChatHistoryAdapter.MyViewHolder> {

    List<ChatItem> mChatItemList = new ArrayList<>();

    public ChatHistoryAdapter(List<ChatItem> mChatItemList) {
        this.mChatItemList = mChatItemList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyViewHolder(
                LayoutInflater.from(parent.getContext()).inflate(
                        R.layout.chat_item, parent, false
                ));
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        ChatItem item = mChatItemList.get(position);
        holder.mTime.setText(item.getSendTime());
        holder.mContent.setText(item.getMsgContent());
        holder.userName.setText(item.getFromUserName());
       /* Picasso.with(MyApplication.applicationContext)
                .load(item.getUserPhoto())
                .into(holder.userPhoto);*/
    }

    @Override
    public int getItemCount() {
        return mChatItemList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder{
        @Bind(R.id.chat_msg)
        TextView mContent;
        @Bind(R.id.user_name)
        TextView userName;
        @Bind(R.id.msg_time)
        TextView mTime;
        @Bind(R.id.user_photo)
        CircleImageView userPhoto;

        public MyViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
