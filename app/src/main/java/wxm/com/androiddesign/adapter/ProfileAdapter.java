package wxm.com.androiddesign.adapter;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
public class ProfileAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public User user;

    public ProfileAdapter(User user) {
        this.user = user;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

            return new BaseInfoViewHolder(LayoutInflater.from(parent.getContext()).inflate(
                    R.layout.user_base_info, parent, false
            ));

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 1;
    }

    class BaseInfoViewHolder extends RecyclerView.ViewHolder {
//        @Bind(R.id.user_name)
//        EditText user_name;
//        @Bind(R.id.user_email)
//        EditText user_email;
//        @Bind(R.id.user_photo)
//        EditText user_photo;
//        @Bind(R.id.user_gender)
//        EditText user_gender;

        public BaseInfoViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

}
