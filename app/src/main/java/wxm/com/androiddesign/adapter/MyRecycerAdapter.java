package wxm.com.androiddesign.adapter;


import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.os.AsyncTask;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;


import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import wxm.com.androiddesign.MyDialog;
import wxm.com.androiddesign.module.AtyItem;
import wxm.com.androiddesign.R;
import wxm.com.androiddesign.module.MyUser;
import wxm.com.androiddesign.module.User;
import wxm.com.androiddesign.network.JsonConnection;
import wxm.com.androiddesign.ui.AtyDetailActivity;

import wxm.com.androiddesign.ui.GroupAcitivity;
import wxm.com.androiddesign.ui.UserAcitivity;
import wxm.com.androiddesign.utils.MyUtils;


/**
 * Created by zero on 2015/6/25.
 */
public class MyRecycerAdapter extends RecyclerView.Adapter<MyRecycerAdapter.MyViewHolder> {
    protected List<AtyItem> activityItems;
    private int lastPosition = -1;
    private Context activity;
    AtyItem item;
    String fragment;

    public MyRecycerAdapter(List<AtyItem> activityItemArrayList, Context activity, String fragment) {
        activityItems = activityItemArrayList;
        this.activity = activity;
        this.fragment = fragment;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.new_activity_item, parent, false);
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
                Intent intent = new Intent(activity, GroupAcitivity.class);
                if(!activityItems.get(position).getAtyCtyId().equals("")) {
                    intent.putExtra("groupId", activityItems.get(position).getAtyCtyId());
                    intent.putExtra("groupName", activityItems.get(position).getAtyCtyId().
                            substring(0, activityItems.get(position).getAtyCtyId().length() - 14));
                }
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
                    activity.startActivity(intent);
                } else {
                    Toast.makeText(activity, "请登录后查看", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onPlus(ImageView fab, int adapterPosition, TextView plus) {
                    AtyItem atyItem = activityItems.get(adapterPosition);
                    item = atyItem;
                    if (atyItem.getAtyPlused().equals("true")) {
                        atyItem.setAtyPlused("false");
                        fab.setImageResource(R.drawable.ic_thumb_up_grey);
                        atyItem.setAtyPlus(String.valueOf(Integer.parseInt(atyItem.getAtyPlus()) - 1));
                        notifyDataSetChanged();
                        new UpDateTask().execute("notLike");
                    } else {
                        atyItem.setAtyPlused("true");
                        fab.setImageResource(R.drawable.ic_thumb_up_primary);
                        atyItem.setAtyPlus(String.valueOf(Integer.parseInt(atyItem.getAtyPlus()) + 1));
                        notifyDataSetChanged();
                        new UpDateTask().execute("like");
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
        holder.total_member.setText(item.getAtyMembers());
        holder.aty_name.setText(item.getAtyName());
        holder.totle_plus.setText(item.getAtyPlus());
        holder.startTime.setText(item.getAtyStartTime());
        if(!item.getAtyCtyId().equals("")){
            holder.group.setText(item.getAtyCtyId().substring(0,item.getAtyCtyId().length() - 14));
        }
        holder.atyPlace.setText(item.getAtyPlace());
        holder.total_member.setText(item.getAtyMembers());
        holder.totle_plus.setText(item.getAtyPlus());
        holder.user_name.setText(item.getUserName());
        holder.publishTime.setText(item.getAtyPublishTime());
        holder.aty_tags.setText(item.getAtyTpye());
        Log.d("aty_tags",item.getAtyTpye());
        Picasso.with(activity).load(item.getUserIcon()).into(holder.user_photo);
        Point size=MyUtils.getScreenSize(activity);
        int screenWidth = size.x - 7;
        int screenHeight = size.y;
        if (holder.imageViewContainer!=null){
            holder.imageViewContainer.removeAllViews();
        }
        if (item.getAtyAlbum()==null||item.getAtyAlbum().size()==0){
            ImageView imageView = (ImageView) LayoutInflater.from(activity).inflate(R.layout.image, null);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(screenWidth, screenHeight * 2 / 5);
            imageView.setImageDrawable(ContextCompat.getDrawable(activity,R.drawable.test));
            imageView.setLayoutParams(layoutParams);
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            holder.imageViewContainer.addView(imageView);
        }

       else if (item.getAtyAlbum() != null&&item.getAtyAlbum().size()!=0) {
//            if (item.getAtyIsJoined().equals("false") && item.getAtyIsPublic().equals("toMembers") || "001".equals(MyUser.userId) && !item.getAtyIsPublic().equals("toVisitors")) {
//                ImageView imageView = (ImageView) LayoutInflater.from(activity).inflate(R.layout.image, null);
//                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(screenWidth, screenHeight * 1 / 3);
//               // Picasso.with(activity).load(R.drawable.wu).into(imageView);
//                imageView.setImageDrawable(ContextCompat.getDrawable(activity,R.drawable.test));
//                imageView.setLayoutParams(layoutParams);
//                imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
//                holder.imageViewContainer.addView(imageView);

            //if (item.getAtyIsJoined().equals("true") && item.getAtyIsPublic().equals("toMembers") || item.getAtyIsPublic().equals("toVisitors") || !"001".equals(MyUser.userId) && item.getAtyIsPublic().equals("toUsers")) {
                for (int i = 0; i < item.getAtyAlbum().size(); i++) {
                    Log.d("imageuri",""+ item.getAtyAlbum().size());
                    ImageView imageView=new ImageView(activity);
                    LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(screenWidth, screenHeight * 2 /5);
                    Log.d("image", item.getAtyAlbum().get(i));
                    imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
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
           // }
        }
        if (item.getAtyPlused().equals("false")) {
            holder.plus.setImageResource(R.drawable.ic_thumb_up_grey);
        } else if (item.getAtyPlused().equals("true")) {
            holder.plus.setImageResource(R.drawable.ic_thumb_up_primary);
        }

        if(item.getAtyCtyId().equals("")){
            holder.groupLayout.setVisibility(View.GONE);

        }else {
            holder.groupLayout.setVisibility(View.VISIBLE);
        }
        //setAnimation(holder.cardView, position);
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
        @Bind(R.id.location)
        TextView atyPlace;
        @Bind(R.id.card_view)
        CardView cardView;
        @Bind(R.id.imageViewContainer)
        LinearLayout imageViewContainer;
        @Bind(R.id.group)
        TextView group;
        @Bind(R.id.group_layout)
        LinearLayout groupLayout;
        @Bind(R.id.total_plus)
        TextView totle_plus;
        @Bind(R.id.start_time)
        TextView startTime;
        @Bind(R.id.publish_time)
        TextView publishTime;
        @Bind(R.id.total_member)
        TextView total_member;
        @Bind(R.id.user_name)
        TextView user_name;
        @Bind(R.id.user_photo)
        CircleImageView user_photo;
        @Bind(R.id.aty_name)
        TextView aty_name;
        @Bind(R.id.tag)
        TextView aty_tags;
        @Bind(R.id.plus)
        ImageView plus;
        public MyViewHolder(View itemView, MyViewHolderClicks listener) {
            super(itemView);
            Log.d("recyclerview", "MyViewHolder");
            mListener = listener;
            ButterKnife.bind(this, itemView);
            cardView.setOnClickListener(this);
            //comment_fab.setOnClickListener(this);
            user_photo.setOnClickListener(this);
            plus.setOnClickListener(this);
            group.setOnClickListener(this);
           // MyUtils.setTextView(aty_content);
        }

        @Override
        public void onClick(View v) {
            if (v instanceof CircleImageView) {
                mListener.onUserPhoto((CircleImageView) v, getAdapterPosition());
            } /*else if ((v instanceof FloatingActionButton) && (v.getId() == R.id.fab_comment)) {
                mListener.onComment((FloatingActionButton) v, getAdapterPosition());
            } */else if ((v instanceof ImageView) && (v.getId() == R.id.plus)) {
                mListener.onPlus((ImageView) v, getAdapterPosition(), totle_plus);
            } else if (v instanceof ImageView) {
                mListener.onPicture((ImageView) v, getAdapterPosition());
            } else if (v instanceof CardView) {
                mListener.onCard((CardView) v, getLayoutPosition());
            } /*else if (v instanceof Button) {
                mListener.onJoinBtn((Button) v, getAdapterPosition());
            }*/ else if (v instanceof TextView && (v.getId() == R.id.group)) {
                mListener.onCommunity((TextView) v, getAdapterPosition());
            }
        }


//        @OnClick(R.id.fab_share)
//        public void onShare() {
//            if ("001".equals(MyUser.userId)) {
//            } else {
//                final String imgPath = null;
//                Intent intent = new Intent(Intent.ACTION_SEND);
//                intent.setType("text/plain");
//                if (imgPath != null && !imgPath.equals("")) {
//                    File f = new File(imgPath);
//                    if (f != null && f.exists() && f.isFile()) {
//                        intent.setType("image/*");
//                        Uri u = Uri.fromFile(f);
//                        intent.putExtra(Intent.EXTRA_STREAM, u);
//                    }
//                }
//                intent.putExtra(Intent.EXTRA_TITLE, "Title");
//                intent.putExtra(Intent.EXTRA_SUBJECT, "Share");
//                intent.putExtra(Intent.EXTRA_TEXT, "You are sharing text!");
//                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                context.startActivity(Intent.createChooser(intent, "Share"));
//            }
//        }


        public interface MyViewHolderClicks {
            public void onUserPhoto(CircleImageView user_photo, int position);

            public void onCommunity(TextView community, int position);

            public void onPicture(ImageView picture, int position);

            public void onCard(CardView cardView, int position);

//            public void onComment(FloatingActionButton fab, int adapterPosition);
//
//            public void onJoinBtn(Button button, int adpterPosition);

            public void onPlus(ImageView fab, int adapterPosition, TextView plus);
        }
    }
}
