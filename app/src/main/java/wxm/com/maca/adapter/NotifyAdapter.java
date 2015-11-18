package wxm.com.maca.adapter;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import wxm.com.maca.R;
import wxm.com.maca.module.Notify;
import wxm.com.maca.ui.AtyDetailActivity;

/**
 * Created by zero on 2015/6/30.
 */
public class NotifyAdapter extends RecyclerView.Adapter<NotifyAdapter.MyViewHolder> {

    static List<Notify> mNotify = new ArrayList<>();

    public NotifyAdapter(List<Notify> mNotify) {
        this.mNotify = mNotify;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyViewHolder(
                LayoutInflater.from(parent.getContext()).inflate(
                        R.layout.notify_item, parent, false
                ), new MyViewHolder.MyViewHolderCallBack() {

            @Override
            public void onCard(CardView cardView, int position) {
                if ("系统".equals(mNotify.get(position).getType())){

                }else {
                    AtyDetailActivity.start(cardView.getContext(),mNotify.get(position).getAtyId(),true);
                }
            }
        });
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Notify item = mNotify.get(position);
        holder.type.setText(item.getType());
        holder.releaseTime.setText(item.getReleaseTime());
        holder.mTitle.setText(item.getTitle());
        if ("".equals(item.getMsgContent())){
            holder.mContent.setVisibility(View.GONE);
        }else {
            holder.mContent.setText(item.getMsgContent());
        }

    }


    @Override
    public int getItemCount() {
        return mNotify.size();
    }

    static class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @Bind(R.id.content)
        TextView mContent;
        @Bind(R.id.title)
        TextView mTitle;
        @Bind(R.id.time)
        TextView releaseTime;
        @Bind(R.id.type)
        TextView type;
        @Bind(R.id.card_view)
        CardView cardView;
        MyViewHolderCallBack mListener;

        public MyViewHolder(View itemView,MyViewHolderCallBack listener) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            mListener=listener;
            cardView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
                mListener.onCard((CardView)v,getAdapterPosition());
        }

        public interface MyViewHolderCallBack{
            public void onCard(CardView cardView,int position);
        }
    }
}
