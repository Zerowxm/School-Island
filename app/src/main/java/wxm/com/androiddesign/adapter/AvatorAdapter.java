package wxm.com.androiddesign.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import wxm.com.androiddesign.R;
import wxm.com.androiddesign.module.Avator;

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

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 6;
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
