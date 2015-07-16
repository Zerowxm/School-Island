package wxm.com.androiddesign.adapter;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
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
import wxm.com.androiddesign.ui.DetailActivity;

import wxm.com.androiddesign.ui.MainActivity;
import wxm.com.androiddesign.ui.UserAcitivity;
import wxm.com.androiddesign.ui.fragment.HomeFragment;


/**
 * Created by zero on 2015/6/25.
 */
public class MyRecycerAdapter extends RecyclerView.Adapter<MyRecycerAdapter.MyViewHolder> {
    protected static List<AtyItem> activityItems;
    private int lastPosition = -1;
    private static AppCompatActivity activity;
    AtyItem item;

    public MyRecycerAdapter(List<AtyItem> activityItemArrayList, AppCompatActivity activity) {
        activityItems = activityItemArrayList;
        this.activity = activity;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_item, parent, false);

        return new MyViewHolder(itemView, new MyViewHolder.MyViewHolderClicks() {
            @Override
            public void onUserPhoto(CircleImageView userPhoto) {
                Intent intent = new Intent(activity, UserAcitivity.class);
                activity.startActivity(intent);
//                ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(
//                        activity, new Pair<View, String>(userPhoto, activity.getResources().getString(R.string.transition_user_photo))
//                );
//                ActivityCompat.startActivity(activity, intent, options.toBundle());
            }

            @Override
            public void onPicture(ImageView picture) {
            }

            @Override
            public void onCard(CardView cardView, int position) {
                Intent intent = new Intent(activity, DetailActivity.class);
                intent.putExtra("com.wxm.com.androiddesign.module.ActivityItemData", activityItems.get(position));
                ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(
                        activity, new Pair<View, String>(cardView, activity.getResources().getString(R.string.transition_card))
                );
                ActivityCompat.startActivity(activity, intent, options.toBundle());
            }

            @Override
            public void onComment(FloatingActionButton fab, int adapterPosition) {
                Intent intent = new Intent(activity, DetailActivity.class);
                intent.putExtra("com.wxm.com.androiddesign.module.ActivityItemData", activityItems.get(adapterPosition));
                activity.startActivity(intent);
            }

            @Override
            public void onJoinBtn(Button button, int adapterPosition) {
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
            }

            @Override
            public void onPlus(FloatingActionButton fab, int adapterPosition, TextView plus) {
                SharedPreferences prefs = activity.getSharedPreferences("wxm.com.androiddesign", Context.MODE_PRIVATE);
                Boolean isPlus = false;
                AtyItem atyItem = activityItems.get(adapterPosition);
                if (atyItem.getAtyPlused().equals("true"))
                    isPlus = prefs.getBoolean("isPlus", true);
                else if (atyItem.getAtyPlused().equals("false"))
                    isPlus = prefs.getBoolean("isPlus", false);
                if (isPlus) {
                    fab.setBackgroundTintList(ColorStateList.valueOf(activity.getResources().getColor(R.color.fab_gray)));
                    SharedPreferences.Editor editor = prefs.edit();
                    //activityItem.plus_fab.setBackgroundDrawable();
                    fab.setImageDrawable(activity.getResources().getDrawable(R.drawable.ic_action_plus_one));
                    atyItem.setAtyPlused("false");
                    atyItem.setAtyPlus(String.valueOf(Integer.parseInt(atyItem.getAtyPlus()) - 1));
                    editor.putBoolean("isPlus", false);
                    editor.apply();
                    notifyDataSetChanged();
                } else {
                    fab.setBackgroundTintList(ColorStateList.valueOf(activity.getResources().getColor(R.color.primary)));
                    fab.setImageDrawable(activity.getResources().getDrawable(R.drawable.ic_action_plus_one_white));
                    SharedPreferences.Editor editor = prefs.edit();
                    atyItem.setAtyPlused("true");
                    atyItem.setAtyPlus(String.valueOf(Integer.parseInt(atyItem.getAtyPlus()) + 1));
                    editor.putBoolean("isPlus", true);
                    editor.apply();
                    notifyDataSetChanged();
                }
            }
        });
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        item = activityItems.get(position);

        holder.total_comment.setText(item.getAtyComment());
        holder.aty_name.setText(item.getAtyName());
        holder.aty_content.setText(item.getAtyContent());
        holder.totle_plus.setText(item.getAtyPlus());
        holder.publish_time.setText(item.getAtyStartTime()+"-"+item.getAtyEndTime());
        holder.activity_tag.setText(item.getAtyType());
        holder.atyPlace.setText(item.getAtyPlace());
        holder.total_member.setText(item.getAtyMembers());
        holder.totle_plus.setText(item.getAtyPlus());
        holder.total_comment.setText(item.getAtyComment());
//        for (int i = 0; i < item.getAtyAlbum().size(); i++) {
//            ImageView imageView = (ImageView) activity.getLayoutInflater().inflate(R.layout.image_item, null);
//            WindowManager windowManager = activity.getWindowManager();
//            DisplayMetrics dm = new DisplayMetrics();
//            Display display = windowManager.getDefaultDisplay();
//            int width = display.getWidth() - 7;
//            int height = display.getHeight();
//            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(width, height * 2 / 5);
//            Glide.with(activity).load(Uri.parse(item.getAtyAlbum().get(i))).into(imageView);
//            imageView.setLayoutParams(layoutParams);
//            imageView.setTag(i);
//            imageView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    Uri uri =Uri.parse(item.getAtyAlbum().get((Integer) v.getTag()));
//                    MyDialog dialog = new MyDialog();
//                    dialog.setUri(uri);
//                    dialog.show(activity.getSupportFragmentManager(), "showPicture");
//                }
//            });
//            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
//            holder.imageViewContainer.addView(imageView);
        // }

        if (item.getAtyPlused().equals("false")) {
            holder.plus_fab.setBackgroundTintList(ColorStateList.valueOf(activity.getResources().getColor(R.color.fab_gray)));
        } else if (item.getAtyPlused().equals("true")) {
            holder.plus_fab.setBackgroundTintList(ColorStateList.valueOf(activity.getResources().getColor(R.color.primary)));
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
            int n = 3; // the exact number of lines you want to display
            tv.setLines(n);
        }

        @Override
        public void onClick(View v) {
            if (v instanceof CircleImageView) {

                mListener.onUserPhoto((CircleImageView) v);
            } else if ((v instanceof FloatingActionButton) && (v.getId() == R.id.fab_comment)) {
                mListener.onComment((FloatingActionButton) v, getAdapterPosition());
            } else if ((v instanceof FloatingActionButton) && (v.getId() == R.id.fab_plus)) {
                mListener.onPlus((FloatingActionButton) v, getAdapterPosition(), totle_plus);
            } else if (v instanceof ImageView) {
                mListener.onPicture((ImageView) v);
            } else if (v instanceof CardView) {
                mListener.onCard((CardView) v, getLayoutPosition());
            } else if (v instanceof Button) {
                mListener.onJoinBtn((Button) v,getAdapterPosition());
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
            public void onUserPhoto(CircleImageView user_photo);

            public void onPicture(ImageView picture);

            public void onCard(CardView cardView, int position);

            //            public void onPlus(FloatingActionButton fab);
            public void onComment(FloatingActionButton fab, int adapterPosition);

            public void onJoinBtn(Button button,int adpterPosition);

            public void onPlus(FloatingActionButton fab, int adapterPosition, TextView plus);
        }
    }
}
