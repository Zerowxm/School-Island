package wxm.com.androiddesign.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import wxm.com.androiddesign.R;
import wxm.com.androiddesign.module.AtyItem;

/**
 * Created by zero on 2015/7/22.
 */
public class CtyAtyAdapter extends RecyclerView.Adapter<CtyAtyAdapter.CmtAtyViewHodler> {
    List<AtyItem> atyItemList = new ArrayList<>();
    Context context;

    public CtyAtyAdapter(List<AtyItem> atyItemList, Context context) {
        this.atyItemList = atyItemList;
        this.context = context;
    }

    @Override
    public CmtAtyViewHodler onCreateViewHolder(ViewGroup parent, int viewType) {
        return new CmtAtyViewHodler(
                LayoutInflater.from(parent.getContext()).inflate(
                        R.layout.cmt_aty_item, parent, false
                ));
    }

    @Override
    public void onBindViewHolder(CmtAtyViewHodler holder, int position) {
        AtyItem atyItem = atyItemList.get(position);
        holder.aty_name.setText(atyItem.getAtyName());
        Picasso.with(context).load(atyItem.getUserIcon()).into(holder.user_photo);
    }

    @Override
    public int getItemCount() {
        return atyItemList.size();
    }

    class CmtAtyViewHodler extends RecyclerView.ViewHolder {
        @Bind(R.id.user_photo)
        ImageView user_photo;
        @Bind(R.id.aty_name)
        TextView aty_name;

        public CmtAtyViewHodler(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
