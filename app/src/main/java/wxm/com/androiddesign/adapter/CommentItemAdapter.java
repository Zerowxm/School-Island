package wxm.com.androiddesign.adapter;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import wxm.com.androiddesign.R;
import wxm.com.androiddesign.module.CommentItem;
import wxm.com.androiddesign.ui.MyApplication;

/**
 * Created by zero on 2015/6/30.
 */
public class CommentItemAdapter extends RecyclerView.Adapter<CommentItemAdapter.MyViewHolder> {

    List<CommentItem> mChatItemList = new ArrayList<>();

    public CommentItemAdapter(List<CommentItem> mChatItemList) {
        this.mChatItemList = mChatItemList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyViewHolder(
                LayoutInflater.from(parent.getContext()).inflate(
                        R.layout.comment, parent, false
                ));
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        CommentItem item = mChatItemList.get(position);
        Log.d("itemma",item.getCmtTime());
        holder.mTime.setText(item.getCmtTime());
        holder.mContent.setText(item.getCmtContent());
        holder.userName.setText(item.getUserName());
        Picasso.with(MyApplication.applicationContext)
                .load(item.getUserIcon())
                .into(holder.userPhoto);
        Picasso.with(MyApplication.applicationContext)
                .load(item.getPhotoId())
                .into(holder.atyPhoto);
    }

    @Override
    public int getItemCount() {
        return mChatItemList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.user_comment)
        TextView mContent;
        @Bind(R.id.user_name)
        TextView userName;
        @Bind(R.id.time)
        TextView mTime;
        @Bind(R.id.user_photo)
        CircleImageView userPhoto;
        @Bind(R.id.aty_photo)
        ImageView atyPhoto;

        public MyViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
