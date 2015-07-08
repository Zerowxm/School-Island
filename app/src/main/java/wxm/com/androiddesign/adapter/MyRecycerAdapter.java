package wxm.com.androiddesign.adapter;


import android.content.Context;


import android.content.Intent;
import android.content.res.ColorStateList;
import android.content.res.XmlResourceParser;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.Fragment;
import android.support.v4.util.Pair;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.io.File;
import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import wxm.com.androiddesign.MyDialog;
import wxm.com.androiddesign.module.ActivityItem;
import wxm.com.androiddesign.module.ActivityItemData;
import wxm.com.androiddesign.R;
import wxm.com.androiddesign.ui.DetailActivity;
import wxm.com.androiddesign.ui.UserAcitivity;

/**
 * Created by zero on 2015/6/25.
 */
public class MyRecycerAdapter extends RecyclerView.Adapter<MyRecycerAdapter.MyViewHolder> {
    protected static ArrayList<ActivityItemData> activityItems;
    private int lastPosition = -1;
    private static AppCompatActivity activity;
    public MyRecycerAdapter(ArrayList<ActivityItemData> activityItemArrayList,AppCompatActivity activity) {
        activityItems = activityItemArrayList;
        this.activity=activity;
    }
    public MyRecycerAdapter(ArrayList<ActivityItemData> activityItemArrayList) {
        activityItems = activityItemArrayList;

    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_item, parent, false);

        return new MyViewHolder(itemView, new MyViewHolder.MyViewHolderClicks() {
            @Override
            public void onUserPhoto(CircleImageView userPhoto) {
                Intent intent = new Intent(activity, UserAcitivity.class);
                ActivityOptionsCompat options=ActivityOptionsCompat.makeSceneTransitionAnimation(
                        activity, new Pair<View, String>(userPhoto, activity.getResources().getString(R.string.transition_user_photo))
                );
                ActivityCompat.startActivity(activity, intent, options.toBundle());
            }

            @Override
            public void onPicture(ImageView picture) {
                MyDialog dialog = new MyDialog();
                dialog.show(activity.getSupportFragmentManager(), "showPicture");
            }

            @Override
            public void onCard(CardView cardView,int position) {
                Intent intent = new Intent(activity, DetailActivity.class);
                intent.putExtra("com.wxm.com.androiddesign.module.ActivityItemData", activityItems.get(position));
                ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(
                        activity, new Pair<View, String>(cardView, activity.getResources().getString(R.string.transition_card))
                );
                ActivityCompat.startActivity(activity, intent, options.toBundle());
            }

            @Override
            public void onPlus(FloatingActionButton fab) {

            }
        });
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        ActivityItemData item = activityItems.get(position);
        holder.activityItem.user_name.setText(item.name);
        holder.activityItem.user_photo.setImageResource(item.photoId);
        holder.activityItem.total_comment.setText(item.comment);
        holder.activityItem.aty_name.setText(item.atyName);
        holder.activityItem.aty_content.setText(item.atyContent);
        holder.activityItem.total_plus.setText(item.plus);
        holder.activityItem.publish_time.setText(item.time);
        holder.activityItem.activity_tag.setText(item.tag);
        //holder.activityItem.publish_image.setImageResource(item.atyImageId);
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

    public static class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ActivityItem activityItem;
        MyViewHolderClicks mListener;
        CardView cardView;
        LinearLayout imageViewContainer;


        public MyViewHolder(View itemView,MyViewHolderClicks listener) {
            super(itemView);
            mListener=listener;
            final String imgPath = null;
            cardView = (CardView) itemView.findViewById(R.id.card_view);
            imageViewContainer=(LinearLayout)itemView.findViewById(R.id.imageViewContainer);
            activityItem = new ActivityItem();
            activityItem.activity_tag = (TextView) itemView.findViewById(R.id.tag);
            activityItem.comment_fab = (FloatingActionButton) itemView.findViewById(R.id.fab_comment);
            //activityItem.comment_fab.setBackgroundTintList(itemView.getResources().getColorStateList(R.color.color_state_list));
            activityItem.plus_fab = (FloatingActionButton) itemView.findViewById(R.id.fab_plus);
            //activityItem.publish_image = (ImageView) itemView.findViewById(R.id.acitivity_iamge);
            activityItem.total_plus = (TextView) itemView.findViewById(R.id.total_plus);
            activityItem.publish_time = (TextView) itemView.findViewById(R.id.publish_time);
            activityItem.total_comment = (TextView) itemView.findViewById(R.id.total_comment);
            activityItem.user_name = (TextView) itemView.findViewById(R.id.user_name);
            activityItem.user_photo = (CircleImageView) itemView.findViewById(R.id.user_photo);
            activityItem.aty_name = (TextView)itemView.findViewById(R.id.aty_name);
            activityItem.aty_content = (TextView)itemView.findViewById(R.id.aty_content);
           // activityItem.location = (TextView)itemView.findViewById(R.id.location);
            //ImageView imageView = new ImageView(activity);
            ImageView imageView=(ImageView)activity.getLayoutInflater().inflate(R.layout.image_item,null);
            //imageView.requestLayout();
            //android.view.ViewGroup.LayoutParams layoutParams=imageView.getLayoutParams();
            WindowManager windowManager=activity.getWindowManager();
            DisplayMetrics dm=new DisplayMetrics();
            Display display=windowManager.getDefaultDisplay();
            int width=display.getWidth();
            int height=display.getHeight();
            LinearLayout.LayoutParams layoutParams=new LinearLayout.LayoutParams(width,height*2/5);
           // layoutParams.height=200;
            //imageView.getLayoutParams().height=200;
            imageView.setLayoutParams(layoutParams);
            itemView.setOnClickListener(this);
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageViewContainer.addView(imageView);


            activityItem.user_photo.setOnClickListener(this);
            //activityItem.publish_image.setOnClickListener(this);
            activityItem.comment_fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(v.getContext(), "comment", Toast.LENGTH_SHORT).show();
                }
            });
            activityItem.plus_fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
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
                    Toast.makeText(v.getContext(), "replace share", Toast.LENGTH_SHORT).show();
                }
            });
        }

        @Override
        public void onClick(View v) {
            if (v instanceof CircleImageView) {
                mListener.onUserPhoto((CircleImageView)v);
            } else if (v instanceof ImageView) {
                mListener.onPicture((ImageView)v);
            }else if (v instanceof CardView) {
                mListener.onCard((CardView)v,getLayoutPosition());
            }
        }


        public interface MyViewHolderClicks {
            public void onUserPhoto(CircleImageView user_photo);
            public void onPicture(ImageView picture);
            public void onCard(CardView cardView,int position);
            public void onPlus(FloatingActionButton fab);
        }
    }
}
