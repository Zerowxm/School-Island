package wxm.com.androiddesign.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import wxm.com.androiddesign.R;
import wxm.com.androiddesign.module.CommentData;
import wxm.com.androiddesign.ui.MyApplication;

/**
 * Created by Zero on 10/7/2015.
 */
public class ListViewAdapter extends ArrayAdapter<CommentData> {


    public ListViewAdapter(Context context,List<CommentData> commentDatas){
        super(context,R.layout.comment_item,commentDatas);
    }

    @Override
    public int getCount() {
        return super.getCount();
    }

    @Override
    public CommentData getItem(int position) {
        return super.getItem(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        CommentData  item=getItem(position);
        ViewHolder holder;
        if (convertView != null) {
            holder = (ViewHolder) convertView.getTag();
        } else {
            convertView = LayoutInflater.from(MyApplication.applicationContext).inflate(R.layout.comment_item, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        }

      /*  try {
            SimpleDateFormat oldFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date oldDate = null;
            oldDate = oldFormatter.parse(item.getTime());
            Date nowDate = new Date(System.currentTimeMillis());
            long time = nowDate.getTime() - oldDate.getTime();
            String str = getSubTime(time);
            holder.time.setText(str);
        } catch (ParseException e) {
            e.printStackTrace();
        }*/
        holder.time.setText(item.getCmtTime());
        Picasso.with(MyApplication.applicationContext).load(item.getUserIcon()).into(holder.user_photo);
        holder.user_comment.setText(item.getComment());
        holder.user_name.setText(item.getUserName());

        return convertView;
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

    static class ViewHolder{
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

        public ViewHolder(View view){
            ButterKnife.bind(this,view);
        }
    }
}
