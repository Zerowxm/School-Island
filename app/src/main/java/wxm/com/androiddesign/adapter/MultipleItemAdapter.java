package wxm.com.androiddesign.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
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


import com.bumptech.glide.Glide;

import java.io.File;
import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;



import de.hdodenhof.circleimageview.CircleImageView;
import wxm.com.androiddesign.MyDialog;
import wxm.com.androiddesign.module.ActivityItem;
import wxm.com.androiddesign.R;
import wxm.com.androiddesign.module.AtyItem;
import wxm.com.androiddesign.module.CommentData;
import wxm.com.androiddesign.ui.UserAcitivity;

/**
 * Created by zero on 2015/6/26.
 */
public class MultipleItemAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private AtyItem atyItem;
    private ArrayList<CommentData> commentDatas;
    public static AppCompatActivity activity;
    private int lastPosition = -1;

    public static enum ITEM_TYPE {
        ATY_TYPE,
        COMMENT_TYPE
    }

    public MultipleItemAdapter(AtyItem ActData, ArrayList<CommentData> commentDatas1,AppCompatActivity activity) {
        atyItem = ActData;
        commentDatas = commentDatas1;
        this.activity = activity;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == ITEM_TYPE.ATY_TYPE.ordinal()) {
            return new AtyViewHolder(LayoutInflater.from(parent.getContext()).inflate(
                    R.layout.activity_item, parent, false
            ));
        } else if (viewType == ITEM_TYPE.COMMENT_TYPE.ordinal()) {
            return new CommentViewHolder(LayoutInflater.from(parent.getContext()).inflate(
                    R.layout.comment_item, parent, false
            ));
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof AtyViewHolder) {


          //  ViewCompat.setTransitionName( ((AtyViewHolder) holder).activityItem.user_name,"1");
            ((AtyViewHolder) holder).activityItem.user_name.setText(atyItem.getUserName());
            ((AtyViewHolder) holder).activityItem.user_photo.setImageResource(atyItem.getUserIcon());
            ((AtyViewHolder) holder).activityItem.aty_name.setText(atyItem.getAtyName());
            ((AtyViewHolder) holder).activityItem.aty_content.setText(atyItem.getAtyContent());
            ((AtyViewHolder) holder).activityItem.total_comment.setText(atyItem.getAtyComment());
            ((AtyViewHolder) holder).activityItem.total_plus.setText(atyItem.getAtyPlus());
            ((AtyViewHolder) holder).activityItem.publish_time.setText(atyItem.getAtyStartTime());
            ((AtyViewHolder) holder).activityItem.activity_tag.setText(atyItem.getAtyType());
            ((AtyViewHolder) holder).imageViewContainer.removeAllViews();
            for (int i=0;i<atyItem.getAtyAlbum().size();i++){
                ImageView imageView=(ImageView)activity.getLayoutInflater().inflate(R.layout.image_item,null);
                WindowManager windowManager=activity.getWindowManager();
                DisplayMetrics dm=new DisplayMetrics();
                Display display=windowManager.getDefaultDisplay();
                int width=display.getWidth()-7;
                int height=display.getHeight();
                LinearLayout.LayoutParams layoutParams=new LinearLayout.LayoutParams(width,height*2/5);
                Glide.with(activity).load(atyItem.getAtyAlbum().get(i)).into(imageView);
                imageView.setLayoutParams(layoutParams);
                imageView.setTag(i);
                imageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Uri uri = atyItem.getAtyAlbum().get((Integer)v.getTag());
                        MyDialog dialog = new MyDialog();
                        dialog.setUri(uri);
                        dialog.show(activity.getSupportFragmentManager(), "showPicture");
                    }
                });
                imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                ((AtyViewHolder) holder).imageViewContainer.addView(imageView);
            }
            setAnimation(((AtyViewHolder) holder).cardView, position);

        } else if (holder instanceof CommentViewHolder) {
            CommentData item = commentDatas.get(position-1);
            ((CommentViewHolder) holder).user_name.setText(item.getUserName());
            ((CommentViewHolder) holder).time.setText(item.getTime());
            ((CommentViewHolder) holder).user_photo.setImageResource(item.getUserIcon());
            ((CommentViewHolder) holder).user_comment.setText(item.getComment());
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
        return commentDatas.size()+1;
    }

    @Override
    public int getItemViewType(int position) {
        return position == 0 ? ITEM_TYPE.ATY_TYPE.ordinal() : ITEM_TYPE.COMMENT_TYPE.ordinal();
    }



    public class CommentViewHolder extends RecyclerView.ViewHolder{
        @Bind(R.id.user_photo) CircleImageView user_photo;
        @Bind(R.id.user_name) TextView user_name;
        @Bind(R.id.user_comment) TextView user_comment;
        @Bind(R.id.time) TextView time;

        public CommentViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public class AtyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        ActivityItem activityItem;
        LinearLayout imageViewContainer;
        CardView cardView;

        public AtyViewHolder(View itemView) {
            super(itemView);
            activityItem = new ActivityItem();
            cardView = (CardView) itemView.findViewById(R.id.card_view);
            imageViewContainer=(LinearLayout)itemView.findViewById(R.id.imageViewContainer);
            activityItem.activity_tag = (TextView) itemView.findViewById(R.id.tag);
            activityItem.comment_fab = (FloatingActionButton) itemView.findViewById(R.id.fab_comment);
            activityItem.plus_fab = (FloatingActionButton) itemView.findViewById(R.id.fab_plus);
            activityItem.publish_image = (ImageView) itemView.findViewById(R.id.acitivity_iamge);
            activityItem.total_plus = (TextView) itemView.findViewById(R.id.total_plus);
            activityItem.publish_time = (TextView) itemView.findViewById(R.id.publish_time);
            activityItem.total_comment = (TextView) itemView.findViewById(R.id.total_comment);
            activityItem.user_name = (TextView) itemView.findViewById(R.id.user_name);
            activityItem.user_photo = (CircleImageView) itemView.findViewById(R.id.user_photo);
            activityItem.aty_name = (TextView)itemView.findViewById(R.id.aty_name);
            activityItem.aty_content = (TextView)itemView.findViewById(R.id.aty_content);
            activityItem.share_fab = (FloatingActionButton)itemView.findViewById(R.id.fab_share);

            activityItem.share_fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
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
            });

            activityItem.plus_fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SharedPreferences prefs=activity.getSharedPreferences("wxm.com.androiddesign", Context.MODE_PRIVATE);
                    Boolean isPlus=prefs.getBoolean("isPlus", false);
                    if(isPlus){
                        activityItem.plus_fab.setBackgroundTintList(ColorStateList.valueOf(activity.getResources().getColor(R.color.fab_gray)));
                        SharedPreferences.Editor editor=prefs.edit();
                        editor.putBoolean("isPlus", false);
                        editor.apply();
                        try {
                            activityItem.total_plus.setText(Integer.parseInt(activityItem.total_plus.getText().toString()) - 1);
                        }
                        catch (Exception e) {

                        }

                    }else {
                        activityItem.plus_fab.setBackgroundTintList(ColorStateList.valueOf(activity.getResources().getColor(R.color.primary)));
                        SharedPreferences.Editor editor=prefs.edit();
                        editor.putBoolean("isPlus",true);
                        editor.apply();
                        try {
                            activityItem.total_plus.setText(Integer.parseInt(activityItem.total_plus.getText().toString()) + 1);
                        }
                        catch (Exception e) {

                        }
                    }
                }
            });

            activityItem.user_photo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(activity, UserAcitivity.class);
                    ActivityOptionsCompat options=ActivityOptionsCompat.makeSceneTransitionAnimation(
                            activity, new Pair<View, String>(v, activity.getResources().getString(R.string.transition_user_photo))
                    );
                    ActivityCompat.startActivity(activity, intent, options.toBundle());
                }
            });


        }

        @Override
        public void onClick(View v) {

        }
    }
}
