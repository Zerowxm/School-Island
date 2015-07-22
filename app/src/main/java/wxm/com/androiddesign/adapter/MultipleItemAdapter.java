package wxm.com.androiddesign.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.util.Pair;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
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
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import butterknife.Bind;
import butterknife.ButterKnife;


import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import wxm.com.androiddesign.MyDialog;
import wxm.com.androiddesign.module.ActivityItem;
import wxm.com.androiddesign.R;
import wxm.com.androiddesign.module.AtyItem;
import wxm.com.androiddesign.module.CommentData;
import wxm.com.androiddesign.module.MyUser;
import wxm.com.androiddesign.network.JsonConnection;
import wxm.com.androiddesign.ui.UserAcitivity;
import wxm.com.androiddesign.ui.UserListActivity;
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

    public MultipleItemAdapter(AtyItem ActData, ArrayList<CommentData> commentDatas1, AppCompatActivity activity, int position) {
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
            //Picasso.with(activity).load(atyItem.getUserPhoto()).into(((AtyViewHolder) holder).user_photo);
            ((AtyViewHolder) holder).user_name.setText(atyItem.getUserName());
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

            if (atyItem.getAtyIsJoined().equals("false") && atyItem.getAtyIsPublic().equals("")) {
                ((AtyViewHolder) holder).imageViewContainer.setVisibility(View.GONE);
            }

            ((AtyViewHolder) holder).imageViewContainer.removeAllViews();
            if (atyItem.getAtyAlbum() != null) {
                if (atyItem.getAtyIsJoined().equals("false") && atyItem.getAtyIsPublic().equals("toMembers")) {
                    ImageView imageView = (ImageView) LayoutInflater.from(activity).inflate(R.layout.image, null);
                    WindowManager windowManager = activity.getWindowManager();
                    DisplayMetrics dm = new DisplayMetrics();
                    Display display = windowManager.getDefaultDisplay();
                    int width = display.getWidth() - 7;
                    int height = display.getHeight();
                    LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(width, height * 1 / 3);
                    Picasso.with(activity).load(R.drawable.miao).into(imageView);
                    imageView.setLayoutParams(layoutParams);
                    imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                    ((AtyViewHolder) holder).imageViewContainer.addView(imageView);

                } else if (atyItem.getAtyIsJoined().equals("true") && atyItem.getAtyIsPublic().equals("toMembers") || atyItem.getAtyIsPublic().equals("toVisitors") || !"001".equals(MyUser.userId) && atyItem.getAtyIsPublic().equals("toUsers")) {
                    for (int i = 0; i < atyItem.getAtyAlbum().size(); i++) {
                        ImageView imageView = (ImageView) activity.getLayoutInflater().inflate(R.layout.image, null);
                        WindowManager windowManager = activity.getWindowManager();
                        DisplayMetrics dm = new DisplayMetrics();
                        Display display = windowManager.getDefaultDisplay();
                        int width = display.getWidth() - 7;
                        int height = display.getHeight();
                        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(width, height * 2 / 5);
                        Glide.with(activity).load(atyItem.getAtyAlbum().get(i)).into(imageView);
                        imageView.setLayoutParams(layoutParams);
                        imageView.setTag(i);
                        imageView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Log.d("imageuri", atyItem.getAtyAlbum().get((Integer) v.getTag()));
                                MyDialog dialog = MyDialog.newInstance(atyItem.getAtyAlbum().get((Integer) v.getTag()));
                                dialog.show(activity.getSupportFragmentManager(), "showPicture");
                            }
                        });
                        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                        ((AtyViewHolder) holder).imageViewContainer.addView(imageView);
                    }

                }

            }
        }
        else if (holder instanceof CommentViewHolder) {
            CommentData item = commentDatas.get(position - 1);
            //  ((CommentViewHolder) holder).user_name.setText(item.getUserName());
            try {
                SimpleDateFormat oldFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date oldDate = null;
                oldDate = oldFormatter.parse(item.getTime());
                Date nowDate = new Date(System.currentTimeMillis());
                long time = nowDate.getTime() - oldDate.getTime();
                String str = getSubTime(time);
                ((CommentViewHolder) holder).time.setText(str);
            } catch (ParseException e) {
                e.printStackTrace();
            }

            ((CommentViewHolder) holder).user_comment.setText(item.getComment());
            ((CommentViewHolder) holder).user_name.setText(item.getUserName());
            setAnimation(((CommentViewHolder) holder).relativeLayout, position);
        }
    }


    private String getSubTime(long subTime) {
        long days = subTime / (1000 * 60 * 60 * 24);
        if (days < 1) {
            long hours = subTime / (1000 * 60 * 60);
            if (hours < 1) {
                long minutes = subTime / (1000 * 60);
                if (minutes < 1)
                    return "Moments ago";
                return (int) (minutes) == 1 ? String.format("%s minute", minutes) : String.format("%s minutes", minutes);
            }
            return (int) (hours) == 1 ? String.format("%s hour", hours) : String.format("%s hours", hours);
        }
        if (days >= 7) {
            return (int) (days / 7) == 1 ? String.format("%s week", (int) (days / 7)) : String.format("%s weeks", (int) (days / 7));
        } else
            return (int) (days) == 1 ? String.format("%s day", days) : String.format("%s days", days);
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
    @Bind(R.id.comment_container)
    RelativeLayout relativeLayout;

    public CommentViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
        user_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity, UserAcitivity.class);
                intent.putExtra("userId", atyItem.getUserId());
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
                    new UpDateTask().execute("join");
                } else {
                    mjoinBtn.setText("加入");
                    atyItem.setAtyJoined("false");
                    atyItem.setAtyMembers(String.valueOf(Integer.parseInt(atyItem.getAtyMembers()) - 1));
                    mjoinBtn.setTextColor(activity.getResources().getColor(R.color.black));
                    notifyDataSetChanged();
                    new UpDateTask().execute("notJoin");
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
                    new UpDateTask().execute("notLike");

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
                    new UpDateTask().execute("like");
                }

            }
        });

        user_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity, UserAcitivity.class);
                intent.putExtra("userId", atyItem.getUserId());
                ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(
                        activity, new Pair<View, String>(v, activity.getResources().getString(R.string.transition_user_photo))
                );
                ActivityCompat.startActivity(activity, intent, options.toBundle());
            }
        });


    }

    @OnClick(R.id.show_people)
    public void showPeople() {
        Intent showIntent = new Intent(activity, UserListActivity.class);
        showIntent.putExtra("atyId", atyItem.getAtyId());
        activity.startActivity(showIntent);
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
                object.put("atyId", atyItem.getAtyId());
            } catch (JSONException e) {
                e.printStackTrace();
            }
            String json = JsonConnection.getJSON(object.toString());
            Log.i("mjson", json);
//            HomeFragment.addActivity(params[0]);
            return null;
        }
    }

    @Override
    public void onClick(View v) {

    }
}
}
