package wxm.com.androiddesign.adapter;

import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.ButterKnife;
import butterknife.InjectView;
import de.hdodenhof.circleimageview.CircleImageView;
import wxm.com.androiddesign.module.ActivityItem;
import wxm.com.androiddesign.R;

/**
 * Created by zero on 2015/6/26.
 */
public class MultipleItemAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    public static enum ITEM_TYPE{
        ATY_TYPE,
        COMMENT_TYPE
    }

    public MultipleItemAdapter(){

    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(viewType==ITEM_TYPE.ATY_TYPE.ordinal()){
            return new AtyViewHolder(LayoutInflater.from(parent.getContext()).inflate(
                    R.layout.activity_item,parent,false
            ));
        }
        else if(viewType==ITEM_TYPE.COMMENT_TYPE.ordinal()){
            return new AtyViewHolder(LayoutInflater.from(parent.getContext()).inflate(
                    R.layout.comment_item,parent,false
            ));
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if(holder instanceof AtyViewHolder){

        }
        else if(holder instanceof CommentViewHolder){

        }
    }

    @Override
    public int getItemCount() {
        return 10;
    }

    @Override
    public int getItemViewType(int position){
        return position == 0 ? ITEM_TYPE.ATY_TYPE.ordinal() : ITEM_TYPE.COMMENT_TYPE.ordinal();
    }

    public class CommentViewHolder extends RecyclerView.ViewHolder{
        @InjectView(R.id.user_photo) CircleImageView user_photo;
        @InjectView(R.id.user_name) TextView user_name;
        @InjectView(R.id.user_comment) TextView user_comment;
        @InjectView(R.id.time) TextView time;
        public CommentViewHolder(View itemView) {
            super(itemView);
            ButterKnife.inject(this,itemView);
        }
    }

    public class AtyViewHolder extends RecyclerView.ViewHolder{
        ActivityItem activityItem;

        public AtyViewHolder(View itemView) {
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
