package wxm.com.androiddesign.adapter;

import android.content.Context;
import android.content.Intent;
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
import wxm.com.androiddesign.module.User;
import wxm.com.androiddesign.ui.UserAcitivity;
import wxm.com.androiddesign.widget.MyTextView;

/**
 * Created by zero on 2015/6/30.
 */
public class RankingAdapter extends RecyclerView.Adapter<RankingAdapter.MyViewHolder> {

    List<User> userList = new ArrayList<>();
    Context context;

    public RankingAdapter(List<User> userList, Context context) {
        this.userList = userList;
        this.context = context;

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyViewHolder(
                LayoutInflater.from(parent.getContext()).inflate(
                        R.layout.user_score_item, parent, false
                ));
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        User item = userList.get(position);
        int count = position + 1;
        holder.count.setText("" + count);
        if (count < 4) {
            holder.count.setTextColor(context.getResources().getColor(R.color.primary,null));

        } else {
            holder.count.setTextColor(context.getResources().getColor(R.color.secondary_text,null));
            holder.count.setTextSize(25);
        }
        holder.user_name.setText(item.getUserName());
        Picasso.with(context).load(item.getUserIcon()).into(holder.user_photo);
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.user_photo)
        ImageView user_photo;
        @Bind(R.id.user_name)
        TextView user_name;
        @Bind(R.id.count)
        MyTextView count;


        public MyViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
