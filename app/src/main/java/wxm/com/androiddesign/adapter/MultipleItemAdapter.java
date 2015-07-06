package wxm.com.androiddesign.adapter;

import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;



import de.hdodenhof.circleimageview.CircleImageView;
import wxm.com.androiddesign.module.ActivityItem;
import wxm.com.androiddesign.R;
import wxm.com.androiddesign.module.ActivityItemData;
import wxm.com.androiddesign.module.CommentData;

/**
 * Created by zero on 2015/6/26.
 */
public class MultipleItemAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private ActivityItemData activityItemData;
    private ArrayList<CommentData> commentDatas;

    public static enum ITEM_TYPE {
        ATY_TYPE,
        COMMENT_TYPE
    }

    public MultipleItemAdapter(ActivityItemData ActData, ArrayList<CommentData> commentDatas1) {
        activityItemData = ActData;
        commentDatas = commentDatas1;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == ITEM_TYPE.ATY_TYPE.ordinal()) {
            return new AtyViewHolder(LayoutInflater.from(parent.getContext()).inflate(
                    R.layout.activity_item, parent, false
            ));
        } else if (viewType == ITEM_TYPE.COMMENT_TYPE.ordinal()) {
            return new CommentViewHolder(LayoutInflater.from(parent.getContext()).inflate(
                    R.layout.comment_item, parent, false
            ));
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof AtyViewHolder) {
          //  ViewCompat.setTransitionName( ((AtyViewHolder) holder).activityItem.user_name,"1");
            ((AtyViewHolder) holder).activityItem.user_name.setText(activityItemData.name);
            ((AtyViewHolder) holder).activityItem.user_photo.setImageResource(activityItemData.imageId);
            ((AtyViewHolder) holder).activityItem.total_comment.setText(activityItemData.commet);
            ((AtyViewHolder) holder).activityItem.total_plus.setText(activityItemData.plus);
            ((AtyViewHolder) holder).activityItem.publish_time.setText(activityItemData.time);
            ((AtyViewHolder) holder).activityItem.activity_tag.setText(activityItemData.tag);
        } else if (holder instanceof CommentViewHolder) {
            CommentData item = commentDatas.get(position);
            ((CommentViewHolder) holder).user_name.setText(item.name);
            ((CommentViewHolder) holder).time.setText(item.time);
            ((CommentViewHolder) holder).user_photo.setImageResource(item.imageId);
            ((CommentViewHolder) holder).user_comment.setText(item.comment);
        }
    }

    @Override
    public int getItemCount() {
        return commentDatas.size();
    }

    @Override
    public int getItemViewType(int position) {
        return position == 0 ? ITEM_TYPE.ATY_TYPE.ordinal() : ITEM_TYPE.COMMENT_TYPE.ordinal();
    }



    public class CommentViewHolder extends RecyclerView.ViewHolder{
        @Bind(R.id.user_photo) CircleImageView user_photo;
        @Bind(R.id.user_name) TextView user_name;
        @Bind(R.id.user_comment) TextView user_comment;
        @Bind(R.id.time) TextView time;

        public CommentViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public class AtyViewHolder extends RecyclerView.ViewHolder {
        ActivityItem activityItem;

        public AtyViewHolder(View itemView) {
            super(itemView);
            activityItem = new ActivityItem();
            activityItem.activity_tag = (TextView) itemView.findViewById(R.id.tag);
            activityItem.comment_fab = (FloatingActionButton) itemView.findViewById(R.id.fab_comment);
            activityItem.plus_fab = (FloatingActionButton) itemView.findViewById(R.id.fab_plus);
            activityItem.publish_image = (ImageView) itemView.findViewById(R.id.acitivity_iamge);
            activityItem.total_plus = (TextView) itemView.findViewById(R.id.total_plus);
            activityItem.publish_time = (TextView) itemView.findViewById(R.id.publish_time);
            activityItem.total_comment = (TextView) itemView.findViewById(R.id.total_comment);
            activityItem.user_name = (TextView) itemView.findViewById(R.id.user_name);
            activityItem.user_photo = (CircleImageView) itemView.findViewById(R.id.user_photo);


        }
    }
}
