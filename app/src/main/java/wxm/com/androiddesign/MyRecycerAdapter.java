package wxm.com.androiddesign;

import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by zero on 2015/6/25.
 */
public class MyRecycerAdapter extends RecyclerView.Adapter<MyRecycerAdapter.MyViewHolder>{
        private ArrayList<ActivityItemData> activityItems;

    public MyRecycerAdapter(ArrayList<ActivityItemData> activityItemArrayList){
        activityItems=activityItemArrayList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView= LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_item,parent,false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        ActivityItemData item=activityItems.get(position);
        holder.activityItem.user_name.setText(item.name);
        holder.activityItem.user_photo.setImageResource(item.imageId);
        holder.activityItem.total_comment.setText(item.commet);
        holder.activityItem.total_plus.setText(item.plus);
        holder.activityItem.publish_time.setText(item.time);
        holder.activityItem.activity_tag.setText(item.tag);
    }

    @Override
    public int getItemCount() {
        return activityItems.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        ActivityItem activityItem;

        public MyViewHolder(View itemView) {
            super(itemView);
            activityItem=new ActivityItem();
            activityItem.activity_tag=(TextView)itemView.findViewById(R.id.tag);
            activityItem.comment_fab=(FloatingActionButton)itemView.findViewById(R.id.fab_comment);
            activityItem.plus_fab=(FloatingActionButton)itemView.findViewById(R.id.fab_plus);
            activityItem.publish_image=(ImageView)itemView.findViewById(R.id.acitivity_iamge);
            activityItem.total_plus=(TextView)itemView.findViewById(R.id.total_plus);
            activityItem.publish_time=(TextView)itemView.findViewById(R.id.publish_time);
            activityItem.total_comment=(TextView)itemView.findViewById(R.id.total_comment);
            activityItem.user_name=(TextView)itemView.findViewById(R.id.user_name);
            activityItem.user_photo=(CircleImageView)itemView.findViewById(R.id.user_photo);
        }
    }
}
