package wxm.com.androiddesign.adapter;


import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Point;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.squareup.picasso.Picasso;


import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import wxm.com.androiddesign.MyDialog;
import wxm.com.androiddesign.module.AtyItem;
import wxm.com.androiddesign.R;
import wxm.com.androiddesign.module.MyUser;
import wxm.com.androiddesign.module.User;
import wxm.com.androiddesign.network.JsonConnection;
import wxm.com.androiddesign.ui.AtyDetailActivity;
import wxm.com.androiddesign.ui.CmtAcitivity;
import wxm.com.androiddesign.ui.DetailActivity;

import wxm.com.androiddesign.ui.MainActivity;
import wxm.com.androiddesign.ui.UserAcitivity;
import wxm.com.androiddesign.utils.MyUtils;


/**
 * Created by zero on 2015/6/25.
 */
public class MyRecycerAdapter extends RecyclerView.Adapter<MyRecycerAdapter.MyViewHolder> {
    protected List<AtyItem> activityItems;
    private int lastPosition = -1;
    private Context activity;
    static Context context;
    AtyItem item;
    String fragment;
    String userId;

    public MyRecycerAdapter(List<AtyItem> activityItemArrayList, String userId, Context activity, String fragment) {
        activityItems = activityItemArrayList;
        this.activity = activity;
        this.fragment = fragment;
        this.userId = userId;
        context=activity;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_item, parent, false);
        Log.d("recyclerview", "onCreateViewHolder");
        return new MyViewHolder(itemView, new MyViewHolder.MyViewHolderClicks() {
            @Override
            public void onUserPhoto(CircleImageView userPhoto, int position) {
                Intent intent = new Intent(activity, UserAcitivity.class);
                intent.putExtra("userId", activityItems.get(position).getUserId());
                activity.startActivity(intent);
            }

            @Override
            public void onCommunity(TextView community, int position) {
                Intent intent = new Intent(activity, CmtAcitivity.class);

                intent.putExtra("ctyId", community.getText().toString());

                activity.startActivity(intent);
            }

            @Override
            public void onPicture(ImageView picture, int position) {
            }

            @Override
            public void onCard(CardView cardView, int position) {
                if (!"001".equals(MyUser.userId)) {
                    Log.d("recyclerview", "onCard");
                    Intent intent = new Intent(activity, AtyDetailActivity.class);
                    intent.putExtra("com.wxm.com.androiddesign.module.ActivityItemData", activityItems.get(position));
                    intent.putExtra("position", position);
                    intent.putExtra("userId", userId);
                    activity.startActivity(intent);
                } else {
                    Toast.makeText(activity, "请登录后查看", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onComment(FloatingActionButton fab, int adapterPosition) {
                if (!"001".equals(MyUser.userId)) {
                    Intent intent = new Intent(activity, AtyDetailActivity.class);
                    intent.putExtra("com.wxm.com.androiddesign.module.ActivityItemData", activityItems.get(adapterPosition));
                    intent.putExtra("position", adapterPosition);
                    intent.putExtra("fragment", fragment);
                    intent.putExtra("userId", userId);
                    activity.startActivity(intent);
                } else {
                    Toast.makeText(activity, "请登录后查看", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onJoinBtn(final Button button,int adapterPosition) {

                if (!"001".equals(MyUser.userId)) {
                    final int index=adapterPosition;
                    item = activityItems.get(index);
                    if ("加入".equals(button.getText().toString())) {
                        new MaterialDialog.Builder(activity)
                                .title(R.string.add_title)
                                .content(activityItems.get(adapterPosition).getAtyName())
                                .positiveText(R.string.OK)
                                .negativeText(R.string.cancel)
                                .callback(new MaterialDialog.ButtonCallback() {
                                    AtyItem atyItem = activityItems.get(index);
                                    @Override
                                    public void onPositive(MaterialDialog dialog) {
                                        button.setText("已加入");
                                        atyItem.setAtyJoined("true");
                                        atyItem.setAtyMembers(String.valueOf(Integer.parseInt(atyItem.getAtyMembers()) + 1));
                                        button.setTextColor(ContextCompat.getColor(activity, R.color.primary));
                                        notifyDataSetChanged();
                                        Log.d("Test", "index" + index);
                                        Log.d("Test",atyItem.toString());
                                        new UpDateTask().execute("join");
                                    }

                                    @Override
                                    public void onNegative(MaterialDialog dialog) {

                                    }
                                })
                                .show();

                    } else {
                        new MaterialDialog.Builder(activity)
                                .title("主人你真的要退出瞄(´c_`)")
                                .content(activityItems.get(adapterPosition).getAtyName())
                                .positiveText("是的")
                                .negativeText(R.string.cancel)
                                .callback(new MaterialDialog.ButtonCallback() {
                                    AtyItem atyItem = activityItems.get(index);
                                    @Override
                                    public void onPositive(MaterialDialog dialog) {
                                        button.setText("加入");
                                        atyItem.setAtyJoined("false");
                                        atyItem.setAtyMembers(String.valueOf(Integer.parseInt(atyItem.getAtyMembers()) - 1));
                                        button.setTextColor(ContextCompat.getColor(activity, R.color.black));
                                        notifyDataSetChanged();
                                        new UpDateTask().execute("notJoin");
                                    }

                                    @Override
                                    public void onNegative(MaterialDialog dialog) {

                                    }
                                })
                                .show();

                    }
                } else {
                    Toast.makeText(activity, "请登录加入", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onPlus(FloatingActionButton fab, int adapterPosition, TextView plus) {
                if (!"001".equals(MyUser.userId)) {
                    AtyItem atyItem = activityItems.get(adapterPosition);
                    item = atyItem;
                    if (atyItem.getAtyPlused().equals("true")) {
                        fab.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(activity, R.color.fab_gray)));
                        fab.setImageDrawable(ContextCompat.getDrawable(activity, R.drawable.ic_action_plus_one));
                        atyItem.setAtyPlused("false");
                        atyItem.setAtyPlus(String.valueOf(Integer.parseInt(atyItem.getAtyPlus()) - 1));
                        notifyDataSetChanged();
                        new UpDateTask().execute("notLike");
                    } else {
                        fab.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(activity, R.color.primary)));
                        fab.setImageDrawable(ContextCompat.getDrawable(activity,R.drawable.ic_action_plus_one_white));
                        atyItem.setAtyPlused("true");
                        atyItem.setAtyPlus(String.valueOf(Integer.parseInt(atyItem.getAtyPlus()) + 1));
                        notifyDataSetChanged();
                        new UpDateTask().execute("like");
                    }

                } else {
                    Toast.makeText(activity, "请登录后点赞", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private class UpDateTask extends AsyncTask<String, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
        }

        @Override
        protected Void doInBackground(String... params) {
            JSONObject object = new JSONObject();
            try {
                object = new JSONObject();
                object.put("action", params[0]);
                object.put("userId", MyUser.userId);
                object.put("userName",MyUser.userName);
                object.put("atyId", item.getAtyId());
                object.put("easemobId",MyUser.getEasemobId());
                object.put("atyName", item.getAtyName());
            } catch (JSONException e) {
                e.printStackTrace();
            }
            String json = JsonConnection.getJSON(object.toString());
            Log.i("mjson", json);
            return null;
        }
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        item = activityItems.get(position);
        holder.user_name.setText(item.getUserName());
        holder.total_comment.setText(item.getAtyComment());
        holder.aty_name.setText(item.getAtyName());
        holder.aty_content.setText(item.getAtyContent());
        holder.totle_plus.setText(item.getAtyPlus());
        holder.publish_time.setText(item.getAtyStartTime() + "-\n" + item.getAtyEndTime());
        holder.activity_tag.setText(item.getAtyType());
        holder.atyPlace.setText(item.getAtyPlace());
        holder.total_member.setText(item.getAtyMembers());
        holder.totle_plus.setText(item.getAtyPlus());
        holder.total_comment.setText(item.getAtyComment());
        holder.user_name.setText(item.getUserName());
        Picasso.with(activity).load(item.getUserIcon()).into(holder.user_photo);

        Point size=MyUtils.getScreenSize(activity);
        int screenWidth = size.x - 7;
        int screenHeight = size.y;
        holder.imageViewContainer.removeAllViews();
        if (item.getAtyAlbum() != null&&item.getAtyAlbum().size()!=0) {
            if (item.getAtyIsJoined().equals("false") && item.getAtyIsPublic().equals("toMembers") || "001".equals(MyUser.userId) && !item.getAtyIsPublic().equals("toVisitors")) {
                ImageView imageView = (ImageView) LayoutInflater.from(activity).inflate(R.layout.image, null);
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(screenWidth, screenHeight * 1 / 3);
                Picasso.with(activity).load(R.drawable.wu).into(imageView);
                imageView.setLayoutParams(layoutParams);
                imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                holder.imageViewContainer.addView(imageView);

            } else if (item.getAtyIsJoined().equals("true") && item.getAtyIsPublic().equals("toMembers") || item.getAtyIsPublic().equals("toVisitors") || !"001".equals(MyUser.userId) && item.getAtyIsPublic().equals("toUsers")) {
                for (int i = 0; i < item.getAtyAlbum().size(); i++) {
                    Log.d("imageuri",""+ item.getAtyAlbum().size());
                    ImageView imageView=new ImageView(activity);
                    LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(screenWidth, screenHeight * 1 / 3);
                    Log.d("image", item.getAtyAlbum().get(i));
                    Picasso.with(activity).load(item.getAtyAlbum().get(i)).into(imageView);
                    imageView.setLayoutParams(layoutParams);
                    imageView.setTag(i);
                    final List<String> album=item.getAtyAlbum();
                    imageView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(final View v) {
                            if (activity!=null&&!((AppCompatActivity)activity).isFinishing()){
                                MyDialog dialog = MyDialog.newInstance(album.get((Integer) v.getTag()));
                                FragmentTransaction ft = ((AppCompatActivity)activity).getSupportFragmentManager().beginTransaction();
                                ft.add(dialog,"showPic");
                                ft.commitAllowingStateLoss();
                            }else {
                                Log.e("Error",activity.toString()+((AppCompatActivity)activity).isFinishing()+((AppCompatActivity)activity).isFinishing());
                            }
                        }
                    });
                    imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                    holder.imageViewContainer.addView(imageView);
                }
            }
        }
        if (item.getAtyPlused().equals("false")) {
            holder.plus_fab.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(activity,R.color.fab_gray)));
            holder.plus_fab.setImageDrawable(ContextCompat.getDrawable(activity,R.drawable.ic_action_plus_one));
        } else if (item.getAtyPlused().equals("true")) {
            holder.plus_fab.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(activity, R.color.primary)));
            holder.plus_fab.setImageDrawable(ContextCompat.getDrawable(activity,R.drawable.ic_action_plus_one_white));
        }
        if (item.getAtyJoined().equals("true")) {

            holder.mjoinBtn.setText("已加入");
            holder.mjoinBtn.setTextColor(ContextCompat.getColor(activity, R.color.primary));
        } else if (item.getAtyJoined().equals("false")) {
            holder.mjoinBtn.setText("加入");
            holder.mjoinBtn.setTextColor(ContextCompat.getColor(activity, R.color.black));
        }

        if(!item.getAtyType().equals("暂时没有社区")){
            holder.activity_tag.setVisibility(View.GONE);
            holder.tagImage.setVisibility(View.GONE);
        }else {

        }
        //setAnimation(holder.cardView, position);
    }


    private class getUserInfoTask extends AsyncTask<String, Void, Boolean> {

        @Override
        protected Boolean doInBackground(String... params) {
            User user = new User();
            user.setAction("");
            return null;
        }
    }


    public void setAnimation(View viewtoAnimate, int position) {
        if (position > lastPosition) {
            Animation animation = AnimationUtils.loadAnimation(activity, R.anim.item_anim);
            viewtoAnimate.startAnimation(animation);
            lastPosition = position;
        }
    }

    @Override
    public int getItemCount() {
        return activityItems.size();
    }

    public void setModels(List<AtyItem> models) {
        activityItems = models;
    }

    public AtyItem removeItem(int position) {
        final AtyItem atyItem = activityItems.remove(position);
        notifyItemRemoved(position);
        return atyItem;
    }

    public void addItem(int position, AtyItem atyItem) {
        activityItems.add(position, atyItem);
        notifyItemInserted(position);
    }

    public void moveItem(int fromPosition, int toPosition) {
        final AtyItem atyItem = activityItems.remove(fromPosition);
        activityItems.add(toPosition, atyItem);
        notifyItemMoved(fromPosition, toPosition);
    }

    public void animateTo(List<AtyItem> models) {
        applyAndAnimateRemovals(models);
        applyAndAnimateAdditions(models);
        applyAndAnimateMovedItems(models);
    }

    private void applyAndAnimateRemovals(List<AtyItem> newModels) {
        for (int i = activityItems.size() - 1; i >= 0; i--) {
            final AtyItem model = activityItems.get(i);
            if (!newModels.contains(model)) {
                removeItem(i);
            }
        }
    }

    private void applyAndAnimateAdditions(List<AtyItem> newModels) {
        for (int i = 0, count = newModels.size(); i < count; i++) {
            final AtyItem model = newModels.get(i);
            if (!activityItems.contains(model)) {
                addItem(i, model);
            }
        }
    }

    private void applyAndAnimateMovedItems(List<AtyItem> newModels) {
        for (int toPosition = newModels.size() - 1; toPosition >= 0; toPosition--) {
            final AtyItem model = newModels.get(toPosition);
            final int fromPosition = activityItems.indexOf(model);
            if (fromPosition >= 0 && fromPosition != toPosition) {
                moveItem(fromPosition, toPosition);
            }
        }
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        MyViewHolderClicks mListener;

        @Bind(R.id.join)
        Button mjoinBtn;
        @Bind(R.id.total_share)
        TextView total_share;
        @Bind(R.id.location)
        TextView atyPlace;
        @Bind(R.id.card_view)
        CardView cardView;
        @Bind(R.id.imageViewContainer)
        LinearLayout imageViewContainer;
        @Bind(R.id.tag)
        TextView activity_tag;
        @Bind(R.id.tag_image)
        ImageView tagImage;
        @Bind(R.id.fab_comment)
        FloatingActionButton comment_fab;
        @Bind(R.id.fab_plus)
        FloatingActionButton plus_fab;
        @Bind(R.id.total_plus)
        TextView totle_plus;
        @Bind(R.id.publish_time)
        TextView publish_time;
        @Bind(R.id.total_comment)
        TextView total_comment;
        @Bind(R.id.user_name)
        TextView user_name;
        @Bind(R.id.user_photo)
        CircleImageView user_photo;
        @Bind(R.id.aty_name)
        TextView aty_name;
        @Bind(R.id.aty_content)
        TextView aty_content;
        @Bind(R.id.member_num)
        TextView total_member;


        public MyViewHolder(View itemView, MyViewHolderClicks listener) {
            super(itemView);
            Log.d("recyclerview", "MyViewHolder");
            mListener = listener;
            ButterKnife.bind(this, itemView);
            cardView.setOnClickListener(this);
            comment_fab.setOnClickListener(this);
            user_photo.setOnClickListener(this);
            mjoinBtn.setOnClickListener(this);
            plus_fab.setOnClickListener(this);
            activity_tag.setOnClickListener(this);
            MyUtils.setTextView(aty_content);
        }

        @Override
        public void onClick(View v) {
            if (v instanceof CircleImageView) {
                mListener.onUserPhoto((CircleImageView) v, getAdapterPosition());
            } else if ((v instanceof FloatingActionButton) && (v.getId() == R.id.fab_comment)) {
                mListener.onComment((FloatingActionButton) v, getAdapterPosition());
            } else if ((v instanceof FloatingActionButton) && (v.getId() == R.id.fab_plus)) {
                mListener.onPlus((FloatingActionButton) v, getAdapterPosition(), totle_plus);
            } else if (v instanceof ImageView) {
                mListener.onPicture((ImageView) v, getAdapterPosition());
            } else if (v instanceof CardView) {
                mListener.onCard((CardView) v, getLayoutPosition());
            } else if (v instanceof Button) {
                mListener.onJoinBtn((Button) v, getAdapterPosition());
            } else if (v instanceof TextView && (v.getId() == R.id.tag)) {
                mListener.onCommunity((TextView) v, getAdapterPosition());
            }
        }


        @OnClick(R.id.fab_share)
        public void onShare() {
            if ("001".equals(MyUser.userId)) {
            } else {
                final String imgPath = null;
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");
                if (imgPath != null && !imgPath.equals("")) {
                    File f = new File(imgPath);
                    if (f != null && f.exists() && f.isFile()) {
                        intent.setType("image/*");
                        Uri u = Uri.fromFile(f);
                        intent.putExtra(Intent.EXTRA_STREAM, u);
                    }
                }
                intent.putExtra(Intent.EXTRA_TITLE, "Title");
                intent.putExtra(Intent.EXTRA_SUBJECT, "Share");
                intent.putExtra(Intent.EXTRA_TEXT, "You are sharing text!");
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(Intent.createChooser(intent, "Share"));
            }
        }


        public interface MyViewHolderClicks {
            public void onUserPhoto(CircleImageView user_photo, int position);

            public void onCommunity(TextView community, int position);

            public void onPicture(ImageView picture, int position);

            public void onCard(CardView cardView, int position);

            public void onComment(FloatingActionButton fab, int adapterPosition);

            public void onJoinBtn(Button button, int adpterPosition);

            public void onPlus(FloatingActionButton fab, int adapterPosition, TextView plus);
        }
    }
}
