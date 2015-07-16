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
import android.widget.Button;
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
import wxm.com.androiddesign.ui.fragment.HomeFragment;

/**
 * Created by zero on 2015/6/26.
 */
public class MultipleItemAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private AtyItem atyItem;
    private int position;
    private ArrayList<CommentData> commentDatas;
    public static AppCompatActivity activity;
    private int lastPosition = -1;

    public static enum ITEM_TYPE {
        ATY_TYPE,
        COMMENT_TYPE
    }

    public MultipleItemAdapter(AtyItem ActData, ArrayList<CommentData> commentDatas1, AppCompatActivity activity,int position) {
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
            // ((AtyViewHolder) holder).activityItem.user_name.setText(atyItem.getUserName());
            //  ((AtyViewHolder) holder).activityItem.user_photo.setImageResource(atyItem.getUserIcon());
            ((AtyViewHolder) holder).aty_name.setText(atyItem.getAtyName());
            ((AtyViewHolder) holder).aty_content.setText(atyItem.getAtyContent());
            ((AtyViewHolder) holder).total_comment.setText(atyItem.getAtyComment());
            ((AtyViewHolder) holder).totle_plus.setText(atyItem.getAtyPlus());
            ((AtyViewHolder) holder).publish_time.setText(atyItem.getAtyStartTime() + "-\n" + atyItem.getAtyEndTime());
            ((AtyViewHolder) holder).activity_tag.setText(atyItem.getAtyType());
            ((AtyViewHolder) holder).imageViewContainer.removeAllViews();
            ((AtyViewHolder) holder).atyPlace.setText(atyItem.getAtyPlace());
            ((AtyViewHolder) holder).total_member.setText(atyItem.getAtyMembers());
            if (atyItem.getAtyPlused().equals("false")) {
                ((AtyViewHolder) holder).plus_fab.setBackgroundTintList(ColorStateList.valueOf(activity.getResources().getColor(R.color.fab_gray)));
                ((AtyViewHolder) holder).plus_fab.setImageDrawable(activity.getResources().getDrawable(R.drawable.ic_action_plus_one));
            } else if (atyItem.getAtyPlused().equals("true")) {
                ((AtyViewHolder) holder).plus_fab.setBackgroundTintList(ColorStateList.valueOf(activity.getResources().getColor(R.color.primary)));
                ((AtyViewHolder) holder).plus_fab.setImageDrawable(activity.getResources().getDrawable(R.drawable.ic_action_plus_one_white));
            }
            if (atyItem.getAtyJoined().equals("true")) {
                ((AtyViewHolder) holder).mjoinBtn.setText("已加入");
                ((AtyViewHolder) holder).mjoinBtn.setTextColor(activity.getResources().getColor(R.color.primary));
            } else if (atyItem.getAtyJoined().equals("false")) {
                ((AtyViewHolder) holder).mjoinBtn.setText("加入");
                ((AtyViewHolder) holder).mjoinBtn.setTextColor(activity.getResources().getColor(R.color.black));
            }
            /*for (int i=0;i<atyItem.getAtyAlbum().size();i++){
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
                        //Uri uri = atyItem.getAtyAlbum().get((Integer)v.getTag());
                        MyDialog dialog = new MyDialog();
                        //dialog.setUri(uri);
                        dialog.show(activity.getSupportFragmentManager(), "showPicture");
                    }
                });
                imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                ((AtyViewHolder) holder).imageViewContainer.addView(imageView);
            }*/
            setAnimation(((AtyViewHolder) holder).cardView, position);

        } else if (holder instanceof CommentViewHolder) {
            CommentData item = commentDatas.get(position - 1);
            //  ((CommentViewHolder) holder).user_name.setText(item.getUserName());
            ((CommentViewHolder) holder).time.setText(item.getTime());
            // ((CommentViewHolder) holder).user_photo.setImageResource(item.getUserIcon());
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
        return commentDatas.size() + 1;
    }

    @Override
    public int getItemViewType(int position) {
        return position == 0 ? ITEM_TYPE.ATY_TYPE.ordinal() : ITEM_TYPE.COMMENT_TYPE.ordinal();
    }


    public class CommentViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.user_photo)
        CircleImageView user_photo;
        @Bind(R.id.user_name)
        TextView user_name;
        @Bind(R.id.user_comment)
        TextView user_comment;
        @Bind(R.id.time)
        TextView time;

        public CommentViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            user_photo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(activity, UserAcitivity.class);
                    ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(
                            activity, new Pair<View, String>(v, activity.getResources().getString(R.string.transition_user_photo))
                    );
                    ActivityCompat.startActivity(activity, intent, options.toBundle());
                }
            });
        }
    }

    public class AtyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
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
        @Bind(R.id.fab_share)
        FloatingActionButton share_fab;
        @Bind(R.id.member_num)
        TextView total_member;

        public AtyViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

            mjoinBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if ("加入".equals(mjoinBtn.getText().toString())) {
                        mjoinBtn.setText("已加入");
                        atyItem.setAtyJoined("true");
                        atyItem.setAtyMembers(String.valueOf(Integer.parseInt(atyItem.getAtyMembers()) + 1));
                        mjoinBtn.setTextColor(activity.getResources().getColor(R.color.primary));
                        notifyDataSetChanged();
                    } else {
                        mjoinBtn.setText("加入");
                        atyItem.setAtyJoined("false");
                        atyItem.setAtyMembers(String.valueOf(Integer.parseInt(atyItem.getAtyMembers()) - 1));
                        mjoinBtn.setTextColor(activity.getResources().getColor(R.color.black));
                        notifyDataSetChanged();
                    }
                }
            });

            share_fab.setOnClickListener(new View.OnClickListener() {
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
                    intent.putExtra(Intent.EXTRA_TEXT, "我要参加" + atyItem.getAtyName() + "，快来跟我一起吧！");
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    activity.startActivity(Intent.createChooser(intent, "Share"));
                }
            });

            plus_fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (atyItem.getAtyPlused().equals("true")) {
                        plus_fab.setBackgroundTintList(ColorStateList.valueOf(activity.getResources().getColor(R.color.fab_gray)));
                        plus_fab.setImageDrawable(activity.getResources().getDrawable(R.drawable.ic_action_plus_one));
                        atyItem.setAtyPlused("false");
                        atyItem.setAtyPlus(String.valueOf(Integer.parseInt(atyItem.getAtyPlus()) - 1));
                        notifyDataSetChanged();
                        try {
                            totle_plus.setText(Integer.parseInt(totle_plus.getText().toString()) - 1);
                        } catch (Exception e) {

                        }

                    } else {
                        plus_fab.setBackgroundTintList(ColorStateList.valueOf(activity.getResources().getColor(R.color.primary)));
                        plus_fab.setImageDrawable(activity.getResources().getDrawable(R.drawable.ic_action_plus_one_white));
                        atyItem.setAtyPlused("true");
                        atyItem.setAtyPlus(String.valueOf(Integer.parseInt(atyItem.getAtyPlus()) + 1));
                        notifyDataSetChanged();
                        try {
                            totle_plus.setText(Integer.parseInt(totle_plus.getText().toString()) + 1);
                        } catch (Exception e) {

                        }
                    }
                }
            });

            user_photo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(activity, UserAcitivity.class);
                    ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(
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
