package wxm.com.androiddesign.adapter;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import wxm.com.androiddesign.R;

/**
 * Created by Zero on 10/25/2015.
 */
public class MutiGroupAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int HEADER=1;
    private static final int ITEM=2;


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType==HEADER){
            return new HeaderViewHolder(LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.group_header,parent,false));
        }
        else if (viewType==ITEM){
            return new HeaderViewHolder(LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.new_activity_item,parent,false));
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 5;
    }

    @Override
    public int getItemViewType(int position) {
        return position == 0 ? HEADER : ITEM;

    }

    public class HeaderViewHolder extends RecyclerView.ViewHolder{
        //@Bind(R.id.)

        public HeaderViewHolder(View itemView) {
            super(itemView);
          //  ButterKnife.bind(this,itemView);
        }
    }

    public class AtyViewHolder extends RecyclerView.ViewHolder{
        @Bind(R.id.card_view)
        CardView cardView;
        @Bind(R.id.user_photo)
        CircleImageView userPhoto;
        @Bind(R.id.user_name)
        TextView userName;
        public AtyViewHolder(View itemView) {
            super(itemView);
        }
    }
}
