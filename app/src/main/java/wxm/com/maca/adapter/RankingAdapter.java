package wxm.com.maca.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
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
import wxm.com.maca.R;
import wxm.com.maca.module.User;
import wxm.com.maca.widget.MyTextView;

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
            holder.count.setTextColor(ContextCompat.getColor(context,R.color.primary));

        } else {
            holder.count.setTextColor(ContextCompat.getColor(context, R.color.secondary_text));
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
