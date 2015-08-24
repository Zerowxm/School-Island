package wxm.com.androiddesign.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import wxm.com.androiddesign.R;
import wxm.com.androiddesign.module.CmtItem;

/**
 * Created by zero on 2015/6/30.
 */
public class CmyAdapter extends RecyclerView.Adapter<CmyAdapter.MyViewHolder> {

    List<CmtItem> cmtItems = new ArrayList<>();
    Context context;

    public CmyAdapter(List<CmtItem> cmtItems, Context context) {
        this.cmtItems = cmtItems;
        this.context = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyViewHolder(
                LayoutInflater.from(parent.getContext()).inflate(
                        R.layout.community_item, parent, false
                ));
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        CmtItem cmtItem = cmtItems.get(position);
        holder.community_name.setText(cmtItem.getCtyId());
        holder.member_num.setText("成员数" + cmtItem.getCtyMembers());
        Picasso.with(context).load(cmtItem.getCtyIcon()).into(holder.community_img);
    }

    @Override
    public int getItemCount() {
        return cmtItems.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.community_img)
        CircleImageView community_img;
        @Bind(R.id.member_num)
        TextView member_num;
        @Bind(R.id.community_name)
        TextView community_name;


        public MyViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            community_img.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
        }
    }
}
