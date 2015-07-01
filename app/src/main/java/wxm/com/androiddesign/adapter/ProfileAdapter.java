package wxm.com.androiddesign.adapter;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;
import wxm.com.androiddesign.R;

import static wxm.com.androiddesign.adapter.ProfileAdapter.ITEM_TYPE.*;

/**
 * Created by zero on 2015/6/30.
 */
public class ProfileAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public static enum ITEM_TYPE{
        INFO,
        CMT
    }

    public ProfileAdapter(){

    }

    @Override
    public int getItemViewType(int position){
        return position == 0? INFO.ordinal(): CMT.ordinal();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType==ITEM_TYPE.INFO.ordinal()){
            return new BaseInfoViewHolder(LayoutInflater.from(parent.getContext()).inflate(
                    R.layout.user_base_info,parent,false
            ));
        }
        if (viewType==ITEM_TYPE.CMT.ordinal()){
            return new CmtsViewHolder(LayoutInflater.from(parent.getContext()).inflate(
                    R.layout.communities,parent,false
            ));
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if(holder instanceof CmtsViewHolder){

        }
    }

    @Override
    public int getItemCount() {
        return 5;
    }

    class BaseInfoViewHolder extends RecyclerView.ViewHolder{
        @Bind(R.id.user_name)
        TextView user_name;
        @Bind(R.id.user_email)
        TextView user_email;
        @Bind(R.id.user_photo)
        TextView user_photo;
        public BaseInfoViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }

    class CmtsViewHolder extends RecyclerView.ViewHolder{



        public CmtsViewHolder(View itemView) {
            super(itemView);
           // ButterKnife.bind(this,itemView);


        }
    }
}
