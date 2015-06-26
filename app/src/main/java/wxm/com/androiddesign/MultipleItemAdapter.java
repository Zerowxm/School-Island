package wxm.com.androiddesign;

import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.InjectView;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by zero on 2015/6/26.
 */
public class MultipleItemAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class CommentViewHolder extends RecyclerView.ViewHolder{

        public CommentViewHolder(View itemView) {
            super(itemView);
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
