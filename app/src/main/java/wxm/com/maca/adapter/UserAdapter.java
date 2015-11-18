package wxm.com.maca.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
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
import wxm.com.maca.R;
import wxm.com.maca.module.User;
import wxm.com.maca.ui.Dre_UserAcitivity;

/**
 * Created by zero on 2015/6/30.
 */
public class UserAdapter extends RecyclerView.Adapter<UserAdapter.MyViewHolder> {

    List<User> userList = new ArrayList<>();
    Context context;

    public UserAdapter(List<User> userList, Context context) {
        this.userList = userList;
        this.context = context;

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyViewHolder(
                LayoutInflater.from(parent.getContext()).inflate(
                        R.layout.user_item, parent, false
                ));
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        User item = userList.get(position);
        holder.user_name.setText(item.getUserName());
        Picasso.with(context).load(item.getUserIcon()).into(holder.user_photo);
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.user_photo)
        CircleImageView user_photo;
        @Bind(R.id.user_name)
        TextView user_name;


        public MyViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            user_photo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, Dre_UserAcitivity.class);
                    intent.putExtra("userId", userList.get(getAdapterPosition()).getUserId());
                    Log.d("user", "user:" + userList.get(getAdapterPosition()).getUserId());
                    context.startActivity(intent);
                }
            });
        }
    }
}
