package wxm.com.androiddesign.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import wxm.com.androiddesign.R;
import wxm.com.androiddesign.module.Avator;
import wxm.com.androiddesign.utils.ActivityStartHelper;

/**
 * Created by zero on 2015/6/30.
 */
public class AvatorAdapter extends RecyclerView.Adapter<AvatorAdapter.MyViewHolder> {

    List<Avator> avatorList;
    Context context;


    public AvatorAdapter(){

    }
    public AvatorAdapter(List<Avator> avatorList, Context context) {
        this.avatorList=avatorList;
        this.context=context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyViewHolder(
                LayoutInflater.from(parent.getContext()).inflate(
                        R.layout.avator_item, parent, false
                ));
    }

    public void AvatorDetail(View view){
        ActivityStartHelper.startProfileActivity(context
                ,avatorList.get((int)view.getTag()).getUserId());
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Picasso.with(context).load(avatorList.get(position).userIcon)
                .into(holder.avator);
        holder.avator.setTag(position);
    }

    @Override
    public int getItemCount() {
        return avatorList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.avator)
        ImageView avator;
        public MyViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
}
