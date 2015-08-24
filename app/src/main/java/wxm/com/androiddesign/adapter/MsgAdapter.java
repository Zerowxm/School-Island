package wxm.com.androiddesign.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import wxm.com.androiddesign.R;
import wxm.com.androiddesign.module.Message;
import wxm.com.androiddesign.module.User;

/**
 * Created by zero on 2015/6/30.
 */
public class MsgAdapter extends RecyclerView.Adapter<MsgAdapter.MyViewHolder> {

    List<Message> msgList = new ArrayList<>();

    public MsgAdapter(List<Message> msgList) {
        this.msgList = msgList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyViewHolder(
                LayoutInflater.from(parent.getContext()).inflate(
                        R.layout.msg_item, parent, false
                ));
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Message item = msgList.get(position);
        holder.msg.setText(item.getMsgContent());
        holder.id.setText(item.getMsgId());
        holder.time.setText(item.getMsgTime());
        holder.type.setText(item.getMsgType());
    }

    @Override
    public int getItemCount() {
        return msgList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.msg_type)
        TextView type;
        @Bind(R.id.msg_id)
        TextView id;
        @Bind(R.id.msg_time)
        TextView time;
        @Bind(R.id.msg_content)
        TextView msg;


        public MyViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
