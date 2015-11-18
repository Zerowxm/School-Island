package wxm.com.maca.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;
import wxm.com.maca.R;
import wxm.com.maca.module.User;

/**
 * Created by zero on 2015/6/30.
 */
public class ProfieAdapter extends RecyclerView.Adapter<ProfieAdapter.MyViewHolder> {

    User user;

    public ProfieAdapter(User user) {
        this.user = user;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyViewHolder(
                LayoutInflater.from(parent.getContext()).inflate(
                        R.layout.profile_item, parent, false
                ));
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        switch (position){
            case 0:
                holder.textViewId.setText("姓名");
                holder.profileText.setText(user.getUserRealName());
                break;
            case 1:
                holder.textViewId.setText("性别");
                holder.profileText.setText(user.getUserGender());
                break;
            case 2:
                holder.textViewId.setText("学院");
                holder.profileText.setText(user.getUserDepartment());
                break;
            case 3:
                holder.textViewId.setText("年级");
                holder.profileText.setText(user.getUserGrade());
                break;
            case 4:
                holder.textViewId.setText("籍贯");
                holder.profileText.setText(user.getUserLocation());
                break;
            case 5:
                holder.textViewId.setText("电话");
                holder.profileText.setText(user.getUserPhone());
                break;
            case 6:
                holder.textViewId.setText("邮箱");
                holder.profileText.setText(user.getUserEmail());
                break;
        }
    }

    @Override
    public int getItemCount() {
        return 7;
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.text_id)
        TextView textViewId;
        @Bind(R.id.profile_text)
        TextView profileText;

        public MyViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
}
