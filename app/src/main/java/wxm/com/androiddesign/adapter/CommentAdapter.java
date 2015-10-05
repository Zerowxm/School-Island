package wxm.com.androiddesign.adapter;


import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.squareup.picasso.Picasso;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import butterknife.Bind;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import wxm.com.androiddesign.R;
import wxm.com.androiddesign.module.CommentData;

/**
 * Created by zero on 2015/6/26.
 */
public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.CommentViewHolder> {

    private int position;
    private ArrayList<CommentData> commentDatas;
    public static AppCompatActivity activity;
    private int lastPosition = -1;


    public CommentAdapter(ArrayList<CommentData> commentDatas, AppCompatActivity activity) {
        this.commentDatas = commentDatas;
        this.activity = activity;
        Log.d("CommentAdapter",commentDatas.toString());
    }

    @Override
    public CommentAdapter.CommentViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new CommentViewHolder(LayoutInflater.from(parent.getContext()).inflate(
                R.layout.comment_item, parent, false
        ));
    }

    @Override
    public void onBindViewHolder(CommentAdapter.CommentViewHolder holder, int position) {
        CommentData item = commentDatas.get(position);
        Log.d("connection", item.toString());
        try {
            SimpleDateFormat oldFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date oldDate = null;
            oldDate = oldFormatter.parse(item.getTime());
            Date nowDate = new Date(System.currentTimeMillis());
            long time = nowDate.getTime() - oldDate.getTime();
            String str = getSubTime(time);
            holder.time.setText(str);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Picasso.with(activity).load(item.getUserIcon()).into(holder.user_photo);
        holder.user_comment.setText(item.getComment());
        holder.user_name.setText(item.getUserName());
        //setAnimation(((CommentViewHolder) holder).relativeLayout, position);
    }


    private String getSubTime(long subTime) {
        long days = subTime / (1000 * 60 * 60 * 24);
        if (days < 1) {
            long hours = subTime / (1000 * 60 * 60);
            if (hours < 1) {
                long minutes = subTime / (1000 * 60);
                if (minutes < 1)
                    return "Moments ago";
                return (int) (minutes) == 1 ? String.format("%s minute", minutes) : String.format("%s minutes", minutes);
            }
            return (int) (hours) == 1 ? String.format("%s hour", hours) : String.format("%s hours", hours);
        }
        if (days >= 7) {
            return (int) (days / 7) == 1 ? String.format("%s week", (int) (days / 7)) : String.format("%s weeks", (int) (days / 7));
        } else
            return (int) (days) == 1 ? String.format("%s day", days) : String.format("%s days", days);
    }

    public void setAnimation(View viewtoAnimate, int position) {
        if (position > lastPosition) {
            Animation animation = AnimationUtils.loadAnimation(activity, R.anim.item_anim);
            viewtoAnimate.startAnimation(animation);
            lastPosition = position;
        }
    }

    @Override
    public int getItemCount() {
        return commentDatas.size();
    }

    public class CommentViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.user_photo)
        CircleImageView user_photo;
        @Bind(R.id.user_name)
        TextView user_name;
        @Bind(R.id.user_comment)
        TextView user_comment;
        @Bind(R.id.time)
        TextView time;
        @Bind(R.id.comment_container)
        RelativeLayout relativeLayout;

        public CommentViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
