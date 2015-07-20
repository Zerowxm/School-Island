package wxm.com.androiddesign.adapter;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.Fragment;
import android.support.v4.util.Pair;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import com.google.gson.Gson;

import com.squareup.picasso.Picasso;


import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import wxm.com.androiddesign.MyDialog;
import wxm.com.androiddesign.module.ActivityItem;
import wxm.com.androiddesign.module.AtyItem;
import wxm.com.androiddesign.R;
import wxm.com.androiddesign.module.User;
import wxm.com.androiddesign.network.JsonConnection;
import wxm.com.androiddesign.ui.DetailActivity;

import wxm.com.androiddesign.ui.MainActivity;
import wxm.com.androiddesign.ui.UserAcitivity;
import wxm.com.androiddesign.ui.fragment.HomeFragment;
import wxm.com.androiddesign.utils.MyBitmapFactory;


/**
 * Created by zero on 2015/6/25.
 */
public class MyRecycerAdapter extends RecyclerView.Adapter<MyRecycerAdapter.MyViewHolder> {
    protected static List<AtyItem> activityItems;
    private int lastPosition = -1;
    private static AppCompatActivity activity;
    AtyItem item;
    String fragment;
    boolean isUser = false;

    public MyRecycerAdapter(List<AtyItem> activityItemArrayList,String userId, AppCompatActivity activity, String fragment) {
        activityItems = activityItemArrayList;
        this.activity = activity;
        this.fragment = fragment;
        if(userId.equals("001")){
            isUser = false;
        }else{
            isUser = true;
        }
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_item, parent, false);

        return new MyViewHolder(itemView, new MyViewHolder.MyViewHolderClicks() {
            @Override
            public void onUserPhoto(CircleImageView userPhoto,int position) {
                Intent intent = new Intent(activity, UserAcitivity.class);
                intent.putExtra("userId",activityItems.get(position).getUserId());
                activity.startActivity(intent);
//                ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(
//                        activity, new Pair<View, String>(userPhoto, activity.getResources().getString(R.string.transition_user_photo))
//                );
//                ActivityCompat.startActivity(activity, intent, options.toBundle());
            }

            @Override
            public void onPicture(ImageView picture,int position) {
            }

            @Override
            public void onCard(CardView cardView, int position) {

                if(isUser) {
                    Log.d("recyclerview", "onCard");
                    Intent intent = new Intent(activity, DetailActivity.class);
                    intent.putExtra("com.wxm.com.androiddesign.module.ActivityItemData", activityItems.get(position));
                    intent.putExtra("position", position);
                    intent.putExtra("fragment", fragment);
                    ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(
                            activity, new Pair<View, String>(cardView, activity.getResources().getString(R.string.transition_card))
                    );
                    ActivityCompat.startActivity(activity, intent, options.toBundle());
                }else{
                    Toast.makeText(activity,"请登录后查看",Toast.LENGTH_SHORT);
                }

                Log.d("recyclerview", "onCard");
                Intent intent = new Intent(activity, DetailActivity.class);
                intent.putExtra("com.wxm.com.androiddesign.module.ActivityItemData", activityItems.get(position));
                intent.putExtra("position", position);
                //intent.putExtra("fragment", fragment);
                activity.startActivity(intent);
//                ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(
//                        activity, new Pair<View, String>(cardView, activity.getResources().getString(R.string.transition_card))
//                );
//                ActivityCompat.startActivity(activity, intent, options.toBundle());

            }

            @Override
            public void onComment(FloatingActionButton fab, int adapterPosition) {
                if(isUser) {
                    Intent intent = new Intent(activity, DetailActivity.class);
                    intent.putExtra("com.wxm.com.androiddesign.module.ActivityItemData", activityItems.get(adapterPosition));
                    intent.putExtra("position", adapterPosition);
                    intent.putExtra("fragment", fragment);
                    activity.startActivity(intent);
                }else{
                    Toast.makeText(activity,"请登录后查看",Toast.LENGTH_SHORT);
                }
            }

            @Override
            public void onJoinBtn(Button button, int adapterPosition) {
                if(isUser) {
                    AtyItem atyItem = activityItems.get(adapterPosition);
                    if ("加入".equals(button.getText().toString())) {
                        button.setText("已加入");
                        atyItem.setAtyJoined("true");
                        atyItem.setAtyMembers(String.valueOf(Integer.parseInt(atyItem.getAtyMembers()) + 1));
                        button.setTextColor(activity.getResources().getColor(R.color.primary));
                        notifyDataSetChanged();
                    } else {
                        button.setText("加入");
                        atyItem.setAtyJoined("false");
                        atyItem.setAtyMembers(String.valueOf(Integer.parseInt(atyItem.getAtyMembers()) - 1));
                        button.setTextColor(activity.getResources().getColor(R.color.black));
                        notifyDataSetChanged();
                    }
                    new UpDateTask().execute(atyItem);
                }else{
                    Toast.makeText(activity,"请登录加入",Toast.LENGTH_SHORT);
                }
            }

            @Override
            public void onPlus(FloatingActionButton fab, int adapterPosition, TextView plus) {
                if(isUser) {
                    AtyItem atyItem = activityItems.get(adapterPosition);

                    if (atyItem.getAtyPlused().equals("true")) {
                        fab.setBackgroundTintList(ColorStateList.valueOf(activity.getResources().getColor(R.color.fab_gray)));
                        fab.setImageDrawable(activity.getResources().getDrawable(R.drawable.ic_action_plus_one));
                        atyItem.setAtyPlused("false");
                        atyItem.setAtyPlus(String.valueOf(Integer.parseInt(atyItem.getAtyPlus()) - 1));
                        notifyDataSetChanged();
                    } else {
                        fab.setBackgroundTintList(ColorStateList.valueOf(activity.getResources().getColor(R.color.primary)));
                        fab.setImageDrawable(activity.getResources().getDrawable(R.drawable.ic_action_plus_one_white));
                        atyItem.setAtyPlused("true");
                        atyItem.setAtyPlus(String.valueOf(Integer.parseInt(atyItem.getAtyPlus()) + 1));
                        notifyDataSetChanged();
                    }
                    new UpDateTask().execute(atyItem);
                }else{
                    Toast.makeText(activity,"请登录后点赞",Toast.LENGTH_SHORT);
                }
            }
        });
    }

    private class UpDateTask extends AsyncTask<AtyItem, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
        }

        @Override
        protected Void doInBackground(AtyItem... params) {
            params[0].setAction("Update");
            JsonConnection.getJSON(new Gson().toJson(params[0]));
            return null;
        }
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        item = activityItems.get(position);
        Log.d("recyclerview", "onBindViewHolder");
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

        if (isUser) {
            holder.imageViewContainer.setVisibility(View.GONE);


            holder.imageViewContainer.removeAllViews();
            Log.d("recyclerview", "item.getAtyAlbum().size()" + item.getAtyAlbum().size());
            for (int i = 0; i < item.getAtyAlbum().size(); i++) {
                ImageView imageView = (ImageView) LayoutInflater.from(activity).inflate(R.layout.image, null);
                WindowManager windowManager = activity.getWindowManager();
                DisplayMetrics dm = new DisplayMetrics();
                Display display = windowManager.getDefaultDisplay();
                int width = display.getWidth() - 7;
                int height = display.getHeight();
                //Glide.clear(imageView);
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(width, height * 1 / 3);
                Log.d("image", item.getAtyAlbum().get(i));
                Picasso.with(activity).load(item.getAtyAlbum().get(i)).into(imageView);
                //Glide.with(activity).load(item.getAtyAlbum().get(i)).into(imageView);
                imageView.setLayoutParams(layoutParams);
                imageView.setTag(i);
                imageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(final View v) {
                        new Handler().post(new Runnable() {
                            public void run() {
                                MyDialog dialog = new MyDialog();
                                dialog.setUri(item.getAtyAlbum().get((Integer) v.getTag()));
                                dialog.show(activity.getSupportFragmentManager(), "showPicture");
                            }
                        });

                    }
                });
                imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                holder.imageViewContainer.addView(imageView);

            }
            holder.imageViewContainer.removeAllViews();
//        Log.d("recyclerview", "item.getAtyAlbum().size()"+item.getAtyAlbum().size());
//        for (int i = 0; i < item.getAtyAlbum().size(); i++) {
//            ImageView imageView = (ImageView) LayoutInflater.from(activity).inflate(R.layout.image, null);
//            WindowManager windowManager = activity.getWindowManager();
//            DisplayMetrics dm = new DisplayMetrics();
//            Display display = windowManager.getDefaultDisplay();
//            int width = display.getWidth() - 7;
//            int height = display.getHeight();
//            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(width, height * 1 / 3);
//            //imageView.setImageBitmap(MyBitmapFactory.StringToBitmap(item.getAtyAlbum().get(i)));
//            Glide.with(activity).load(item.getAtyAlbum().get(i)).into(imageView);
//            imageView.setLayoutParams(layoutParams);
//            imageView.setTag(i);
//            imageView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    //Uri uri =Uri.parse(item.getAtyAlbum().get((Integer) v.getTag()));
//                    MyDialog dialog = new MyDialog();
//                    dialog.setUri(item.getAtyAlbum().get((Integer) v.getTag()));
//                    dialog.show(activity.getSupportFragmentManager(), "showPicture");
//                }
//            });
//            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
//            holder.imageViewContainer.addView(imageView);
//        }

            if (item.getAtyPlused().equals("false")) {
                holder.plus_fab.setBackgroundTintList(ColorStateList.valueOf(activity.getResources().getColor(R.color.fab_gray)));
                holder.plus_fab.setImageDrawable(activity.getResources().getDrawable(R.drawable.ic_action_plus_one));
            } else if (item.getAtyPlused().equals("true")) {
                holder.plus_fab.setBackgroundTintList(ColorStateList.valueOf(activity.getResources().getColor(R.color.primary)));
                holder.plus_fab.setImageDrawable(activity.getResources().getDrawable(R.drawable.ic_action_plus_one_white));
            }

            if (item.getAtyJoined().equals("true")) {
                holder.mjoinBtn.setText("已加入");
                holder.mjoinBtn.setTextColor(activity.getResources().getColor(R.color.primary));
            } else if (item.getAtyJoined().equals("false")) {
                holder.mjoinBtn.setText("加入");
                holder.mjoinBtn.setTextColor(activity.getResources().getColor(R.color.black));
            }

            setAnimation(holder.cardView, position);
        }
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
        ActivityItem activityItem;
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
            setTextView(aty_content);
        }

        public void setTextView(TextView tv) {
            tv.setSingleLine(false);
            tv.setEllipsize(TextUtils.TruncateAt.END);
            int n = 2; // the exact number of lines you want to display
            tv.setLines(n);
        }

        @Override
        public void onClick(View v) {
            if (v instanceof CircleImageView) {

                mListener.onUserPhoto((CircleImageView)v,getAdapterPosition());
            } else if ((v instanceof FloatingActionButton) && (v.getId() == R.id.fab_comment)) {
                mListener.onComment((FloatingActionButton) v, getAdapterPosition());
            } else if ((v instanceof FloatingActionButton) && (v.getId() == R.id.fab_plus)) {
                mListener.onPlus((FloatingActionButton) v, getAdapterPosition(), totle_plus);
            } else if (v instanceof ImageView) {
                mListener.onPicture((ImageView) v,getAdapterPosition());
            } else if (v instanceof CardView) {
                mListener.onCard((CardView) v, getLayoutPosition());
            } else if (v instanceof Button) {
                mListener.onJoinBtn((Button) v, getAdapterPosition());
            }
        }


        @OnClick(R.id.fab_share)
        public void onShare() {
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
            activity.startActivity(Intent.createChooser(intent, "Share"));
        }


        public interface MyViewHolderClicks {
            public void onUserPhoto(CircleImageView user_photo,int position);

            public void onPicture(ImageView picture,int position);

            public void onCard(CardView cardView, int position);

            //            public void onPlus(FloatingActionButton fab);
            public void onComment(FloatingActionButton fab, int adapterPosition);

            public void onJoinBtn(Button button, int adpterPosition);

            public void onPlus(FloatingActionButton fab, int adapterPosition, TextView plus);
        }
    }
}
