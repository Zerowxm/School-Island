package wxm.com.androiddesign.adapter;

import android.graphics.Color;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import wxm.com.androiddesign.module.ActivityItem;
import wxm.com.androiddesign.module.ActivityItemData;
import wxm.com.androiddesign.R;

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
        //holder.activityItem.plus_fab.
        //holder.cardView.setClipToOutline(true);
        //holder.cardView.setElevation(holder.cardView.getContext().getResources().getDimension(R.dimen.cardview_elevation));
    }

    @Override
    public int getItemCount() {
        return activityItems.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        ActivityItem activityItem;
        MyViewHolderClicks mListener;
        CardView cardView;

        public MyViewHolder(View itemView) {
            super(itemView);
            cardView=(CardView)itemView.findViewById(R.id.card_view);
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
            activityItem.user_photo.setOnClickListener(this);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if(v instanceof CircleImageView){
                //mListener.onPhoto((CircleImageView)v);
                Toast.makeText(v.getContext(),"yo",Toast.LENGTH_LONG).show();
                Snackbar.make(v,"yo",Snackbar.LENGTH_SHORT).show();
            }
        }

        public static interface MyViewHolderClicks{
            public void onPhoto(CircleImageView imageView);
        }
    }
}
