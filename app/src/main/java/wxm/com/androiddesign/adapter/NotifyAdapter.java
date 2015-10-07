package wxm.com.androiddesign.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import wxm.com.androiddesign.R;
import wxm.com.androiddesign.module.Message;
import wxm.com.androiddesign.module.Notify;
import wxm.com.androiddesign.ui.MyApplication;

/**
 * Created by zero on 2015/6/30.
 */
public class NotifyAdapter extends RecyclerView.Adapter<NotifyAdapter.MyViewHolder> {

    List<Notify> mNotify = new ArrayList<>();

    public NotifyAdapter(List<Notify> mNotify) {
        this.mNotify = mNotify;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyViewHolder(
                LayoutInflater.from(parent.getContext()).inflate(
                        R.layout.msg_item, parent, false
                ));
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Notify item = mNotify.get(position);
        holder.mTitle.setText(item.getmTitle());
        holder.mContent.setText(item.getmContent());
        holder.userName.setText(item.getUserName());
        Picasso.with(MyApplication.applicationContext)
                .load(item.getUserPhoto())
                .into(holder.userPhoto);
    }

    @Override
    public int getItemCount() {
        return mNotify.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.content)
        TextView mContent;
        @Bind(R.id.user_name)
        TextView userName;
        @Bind(R.id.title)
        TextView mTitle;
        @Bind(R.id.user_photo)
        CircleImageView userPhoto;

        public MyViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
