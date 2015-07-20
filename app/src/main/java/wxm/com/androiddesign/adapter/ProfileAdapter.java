package wxm.com.androiddesign.adapter;

import android.content.Context;
import android.media.Image;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import wxm.com.androiddesign.R;
import wxm.com.androiddesign.module.User;

/**
 * Created by zero on 2015/6/30.
 */
public class ProfileAdapter extends RecyclerView.Adapter<ProfileAdapter.BaseInfoViewHolder> {

    public User user;
    Boolean isEdit=false;
    Context context;
    public ProfileAdapter(User user) {
        Log.i("pro", "pro");
        this.user = user;
    }

    public ProfileAdapter(Context context){
        this.context=context;
    }

    public void changeEditState(List<EditText> list,Boolean isEdit){
        for(EditText editText:list){
            editText.setEnabled(isEdit);
        }
    }
    public ProfileAdapter.BaseInfoViewHolder onCreateViewHolder(ViewGroup parent, final int viewType) {

        return new BaseInfoViewHolder(LayoutInflater.from(parent.getContext()).inflate(
                R.layout.user_base_info, parent, false
        ), new BaseInfoViewHolder.MyViewHolderClicks() {
            @Override
            public void mEditProfile(ImageView edit,List<EditText> list) {
                if(!isEdit){
                    edit.setImageResource(R.drawable.ic_action_done_black);
                    isEdit=true;
                    changeEditState(list,isEdit);

                }else if(isEdit){
                    edit.setImageResource(R.drawable.ic_action_mode_edit_black);
                    isEdit=false;
                    changeEditState(list,isEdit);

                }
            }
        });



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

    public static class BaseInfoViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        List<EditText> editTextList=new ArrayList<>();
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
        @Bind(R.id.edit_info)
        ImageView edit;

        MyViewHolderClicks mListener;

        public BaseInfoViewHolder(View itemView,MyViewHolderClicks mListener) {
            super(itemView);
            this.mListener=mListener;
            ButterKnife.bind(this, itemView);
            editTextList.add(user_name);
            editTextList.add(user_email);
            editTextList.add(user_phone);
            editTextList.add(user_gender);
            editTextList.add(user_place);
            editTextList.add(user_qq);
            edit.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            mListener.mEditProfile((ImageView)v,editTextList);
        }

        public interface MyViewHolderClicks{
            public void mEditProfile(ImageView edit,List<EditText> list);
        }


    }

}
