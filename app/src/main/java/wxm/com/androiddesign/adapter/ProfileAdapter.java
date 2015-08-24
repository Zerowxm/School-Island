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

/**
 * Created by zero on 2015/6/30.
 */
public class ProfileAdapter extends RecyclerView.Adapter<ProfileAdapter.BaseInfoViewHolder> {

    public User user;

    public ProfileAdapter(User user) {
        Log.i("pro", "pro");
        this.user = user;
    }


    public ProfileAdapter.BaseInfoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        return new BaseInfoViewHolder(LayoutInflater.from(parent.getContext()).inflate(
                R.layout.user_base_info, parent, false
        ));

    }

    @Override
    public void onBindViewHolder(BaseInfoViewHolder holder, int position) {
//        holder.user_name.setText(user.getUserName());
//        holder.user_email.setText(user.getUserEmail());
//        holder.user_gender.setText(user.getUserGender());
//        holder.user_phone.setText(user.getUserPhone());
//        holder.user_place.setText(user.getUserLocation());
//        holder.user_qq.setText(user.getQq());
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
