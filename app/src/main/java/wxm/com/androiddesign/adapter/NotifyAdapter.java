package wxm.com.androiddesign.adapter;

import android.provider.ContactsContract;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import wxm.com.androiddesign.R;
import wxm.com.androiddesign.module.Message;
import wxm.com.androiddesign.module.Notify;
import wxm.com.androiddesign.ui.MyApplication;

/**
 * Created by zero on 2015/6/30.
 */
public class NotifyAdapter extends RecyclerView.Adapter<NotifyAdapter.MyViewHolder> {

    List<Notify> mNotify = new ArrayList<>();

    public NotifyAdapter(List<Notify> mNotify) {
        this.mNotify = mNotify;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyViewHolder(
                LayoutInflater.from(parent.getContext()).inflate(
                        R.layout.notify_item, parent, false
                ));
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Notify item = mNotify.get(position);
        Log.d("item", item.getmTitle() + item.getmContent() + item.getUserName());
        SimpleDateFormat oldFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date oldDate = null;
        try {
            oldDate = oldFormatter.parse(item.getmTime());
            Date nowDate = new Date(System.currentTimeMillis());
            long time = nowDate.getTime() - oldDate.getTime();
            String str = getSubTime(time);
            //holder.releaseTime.setText(item.getmTime());
<<<<<<< Updated upstream
//            if (position==0){
//                holder.mTitle.setText("政府高层");
//            }else {
//                holder.mTitle.setText("怎样从政府高层的讲话中获取信息？");
//            }
//            holder.mContent.setText(item.getmContent());
=======
            holder.mTitle.setText(item.getmTitle());
            holder.mContent.setText(item.getmContent());
>>>>>>> Stashed changes
//            holder.userName.setText(item.getUserName());
//            Picasso.with(MyApplication.applicationContext)
//                    .load(item.getUserPhoto())
//                    .into(holder.userPhoto);
        } catch (ParseException e) {
            e.printStackTrace();
        }
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

    @Override
    public int getItemCount() {
        return mNotify.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.content)
        TextView mContent;
//        @Bind(R.id.user_name)
//        TextView userName;
        @Bind(R.id.title)
        TextView mTitle;
//        @Bind(R.id.user_photo)
//        CircleImageView userPhoto;
        @Bind(R.id.time)
        TextView releaseTime;

        public MyViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
