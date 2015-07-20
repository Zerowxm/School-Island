package wxm.com.androiddesign.adapter;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;
import wxm.com.androiddesign.R;
import wxm.com.androiddesign.module.User;

import static wxm.com.androiddesign.adapter.ProfileAdapter.ITEM_TYPE.*;

/**
 * Created by zero on 2015/6/30.
 */
public class ProfileAdapter extends RecyclerView.Adapter<ProfileAdapter.BaseInfoViewHolder> {

    public static enum ITEM_TYPE {
        INFO,
        CMT
    }
    public User user;

    public ProfileAdapter(User user) {
        Log.i("pro","pro");
        this.user = user;
    }

    @Override
    public int getItemViewType(int position) {
        //return position == 0? INFO.ordinal(): CMT.ordinal();
        return INFO.ordinal();
    }

    @Override
    public BaseInfoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == ITEM_TYPE.INFO.ordinal()) {
            return new BaseInfoViewHolder(LayoutInflater.from(parent.getContext()).inflate(
                    R.layout.user_base_info, parent, false
            ));
        }
//        if (viewType==ITEM_TYPE.CMT.ordinal()){
//            return new CmtsViewHolder(LayoutInflater.from(parent.getContext()).inflate(
//                    R.layout.communities,parent,false
//            ));
//        }
        return null;
    }

    @Override
    public void onBindViewHolder(BaseInfoViewHolder holder, int position) {
//        holder.user_name.setText(user.getUserName());
//        holder.user_email.setText(user.getUserEmail());
//        holder.user_gender.setText(user.getUserGender());
//        holder.user_phone.setText(user.getUserPhone());
//        holder.user_place.setText("厦大学生公寓");
//        holder.user_qq.setText("110");

    }

    @Override
    public int getItemCount() {
        return 1;
    }

    class BaseInfoViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.user_name)
        EditText user_name;
        @Bind(R.id.user_email)
        EditText user_email;
        @Bind(R.id.user_phone)
        EditText user_phone;
        @Bind(R.id.user_gender)
        EditText user_gender;
        @Bind(R.id.user_place)
        EditText user_place;
        @Bind(R.id.user_qq)
        EditText user_qq;



        public BaseInfoViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

}
