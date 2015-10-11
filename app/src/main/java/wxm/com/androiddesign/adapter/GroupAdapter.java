package wxm.com.androiddesign.adapter;

import android.content.Context;
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
import wxm.com.androiddesign.R;
import wxm.com.androiddesign.module.Group;

/**
 * Created by zero on 2015/6/30.
 */
public class GroupAdapter extends RecyclerView.Adapter<GroupAdapter.MyViewHolder> {

    List<Group> groups = new ArrayList<>();
    Context context;

    public GroupAdapter(List<Group> groups, Context context) {
        this.groups = groups;
        this.context = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyViewHolder(
                LayoutInflater.from(parent.getContext()).inflate(
                        R.layout.community_item, parent, false
                ));
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Group group = groups.get(position);
        holder.community_name.setText(group.getCtyId());
        holder.member_num.setText("成员数" + group.getCtyMembers());
        Picasso.with(context).load(group.getCtyIcon()).into(holder.community_img);
    }

    @Override
    public int getItemCount() {
        return groups.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.group_img)
        ImageView community_img;
        @Bind(R.id.member_num)
        TextView member_num;
        @Bind(R.id.group_name)
        TextView community_name;


        public MyViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
