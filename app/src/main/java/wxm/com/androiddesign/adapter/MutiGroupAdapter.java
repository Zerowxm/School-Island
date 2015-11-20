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
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import butterknife.Bind;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import wxm.com.androiddesign.MyDialog;
import wxm.com.androiddesign.R;
import wxm.com.androiddesign.module.AtyItem;
import wxm.com.androiddesign.module.Group;
import wxm.com.androiddesign.module.MyUser;
import wxm.com.androiddesign.network.JsonConnection;
import wxm.com.androiddesign.ui.AtyDetailActivity;
import wxm.com.androiddesign.ui.CmtAcitivity;
import wxm.com.androiddesign.ui.Dre_UserAcitivity;
import wxm.com.androiddesign.ui.GroupAcitivity;
import wxm.com.androiddesign.ui.ImageViewerActivity;
import wxm.com.androiddesign.utils.ActivityStartHelper;
import wxm.com.androiddesign.utils.MyUtils;

/**
 * Created by Zero on 10/25/2015.
 */
public class MutiGroupAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int HEADER=1;
    private static final int ITEM=2;
    private Group group;
    AtyItem item;
    List<AtyItem> activityItems;
    List<MenuItem> menuItems;
    Context activity;

    public MutiGroupAdapter(Group group,List<AtyItem> atyItems,List<MenuItem> menuItems,Context activity){
        this.group = group;
        this.activityItems = atyItems;
        this.menuItems = menuItems;
        this.activity = activity;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType==HEADER){
            return new HeaderViewHolder(LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.group_header,parent,false));
        }
        else if (viewType==ITEM){
            final View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.new_activity_item, parent, false);
            Log.d("recyclerview", "onCreateViewHolder");
            return new AtyViewHolder(itemView, new AtyViewHolder.MyViewHolderClicks() {
                @Override
                public void onUserPhoto(CircleImageView userPhoto, int position) {
//                    Intent intent = new Intent(activity, Dre_UserAcitivity.class);
//                    intent.putExtra("userId", activityItems.get(position-1).getUserId());
//                    activity.startActivity(intent);
                    ActivityStartHelper.startProfileActivity(activity, activityItems.get(position - 1).getUserId());
                }

                @Override
                public void onCommunity(TextView community, int position) {
                    Intent intent = new Intent(activity, GroupAcitivity.class);
                    if (!activityItems.get(position).getAtyCtyId().equals("")) {
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
                        AtyDetailActivity.start(cardView.getContext(), item, false);
                    } else {
                        Toast.makeText(activity, "请登录后查看", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onPlus(ImageView fab, int adapterPosition, TextView plus) {
                    AtyItem atyItem = activityItems.get(adapterPosition-1);
                    item = atyItem;
                    if (atyItem.getAtyPlused().equals("true")) {
                        atyItem.setAtyPlused("false");
                        fab.setImageResource(R.drawable.ic_favorite_outline);
                        atyItem.setAtyPlus(String.valueOf(Integer.parseInt(atyItem.getAtyPlus()) - 1));
                        notifyDataSetChanged();
                        new UpDateTask().execute("notLike");
                    } else {
                        atyItem.setAtyPlused("true");
                        fab.setImageResource(R.drawable.ic_favorite_outline);
                        atyItem.setAtyPlus(String.valueOf(Integer.parseInt(atyItem.getAtyPlus()) + 1));
                        notifyDataSetChanged();
                        new UpDateTask().execute("like");
                    }
                }
            });
        }
        return null;
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
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if(holder instanceof HeaderViewHolder) {
            ((HeaderViewHolder) holder).peoples.setText(group.getCtyMembers() + "个成员");
            ((HeaderViewHolder) holder).groupName.setText(group.getCtyName() + "");
            ((HeaderViewHolder) holder).groupIntro.setText(group.getCtyIntro() + "");
            if(MyUser.userId.equals(group.getUserId())){
                ((HeaderViewHolder) holder).join.setVisibility(View.GONE);
            }else if (group.getCtyIsAttention().equals("true")) {
                ((HeaderViewHolder) holder).join.setVisibility(View.GONE);
                //((HeaderViewHolder) holder).join.setText("退出");
            }else{
                ((HeaderViewHolder) holder).join.setVisibility(View.VISIBLE);
                ((HeaderViewHolder) holder).join.setText("加入");
            }
        }else{
            item = activityItems.get(position-1);
            ((AtyViewHolder)holder).user_name.setText(item.getUserName());
            ((AtyViewHolder)holder).total_member.setText(item.getAtyMembers());
            ((AtyViewHolder)holder).aty_name.setText(item.getAtyName());
            ((AtyViewHolder)holder).totle_plus.setText(item.getAtyPlus());
            ((AtyViewHolder)holder).startTime.setText(item.getAtyStartTime());
            ((AtyViewHolder)holder).group.setText(group.getCtyName());
            ((AtyViewHolder)holder).atyPlace.setText(item.getAtyPlace());
            ((AtyViewHolder)holder).total_member.setText(item.getAtyMembers());
            ((AtyViewHolder)holder).totle_plus.setText(item.getAtyPlus());
            ((AtyViewHolder)holder).user_name.setText(item.getUserName());
            ((AtyViewHolder)holder).publishTime.setText(item.getAtyPublishTime());
            ((AtyViewHolder)holder).aty_tags.setText(item.getAtyTpye());
            Picasso.with(activity).load(item.getUserIcon()).into(((AtyViewHolder)holder).user_photo);
            Point size= MyUtils.getScreenSize(activity);
            int screenWidth = size.x - 7;
            int screenHeight = size.y;
            if (((AtyViewHolder)holder).imageViewContainer!=null){
                ((AtyViewHolder)holder).imageViewContainer.removeAllViews();
            }

            if (item.getAtyAlbum() != null&&item.getAtyAlbum().size()!=0) {
                for (int i = 0; i < item.getAtyAlbum().size(); i++) {
                    Log.d("imageuri", "" + item.getAtyAlbum().size());
                    ImageView imageView = new ImageView(activity);
                    LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(screenWidth, screenHeight * 1 / 3);
                    Log.d("image", item.getAtyAlbum().get(i));
                    Picasso.with(activity).load(item.getAtyAlbum().get(i)).into(imageView);
                    imageView.setLayoutParams(layoutParams);
                    imageView.setTag(i);
                    final ArrayList<String> album = item.getAtyAlbum();
                    imageView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(final View v) {
                            if (activity != null && !((AppCompatActivity) activity).isFinishing()) {
                                ImageViewerActivity.start(activity, album, (int) v.getTag());
                            } else {
                                Log.e("Error", activity.toString() + ((AppCompatActivity) activity).isFinishing() + ((AppCompatActivity) activity).isFinishing());
                            }
                        }
                    });
                    imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                    ((AtyViewHolder)holder).imageViewContainer.addView(imageView);
                }

            }
            ((AtyViewHolder)holder).plus.setImageResource(R.drawable.ic_favorite_outline);
            if (item.getAtyPlused().equals("false")) {
                ((AtyViewHolder)holder).plus.setColorFilter(ContextCompat.getColor(activity, R.color.gray));
            } else if (item.getAtyPlused().equals("true")) {
                ((AtyViewHolder)holder).plus.setColorFilter(ContextCompat.getColor(activity, R.color.red));
            }

            if (item.getAtyCtyId().equals("")) {
                ((AtyViewHolder)holder).groupLayout.setVisibility(View.GONE);
            } else {
                ((AtyViewHolder)holder).groupLayout.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    public int getItemCount() {
        return activityItems.size()+1;
    }

    @Override
    public int getItemViewType(int position) {
        return position == 0 ? HEADER : ITEM;

    }

    public class HeaderViewHolder extends RecyclerView.ViewHolder{

        @Bind(R.id.peoples)
        TextView peoples;
        @Bind(R.id.group_name)
        TextView groupName;
        @Bind(R.id.join)
        Button join;
        @Bind(R.id.read_more)
        ImageView readMore;
        @Bind(R.id.group_brief_intro)
        TextView groupIntro;

        public HeaderViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            readMore.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (groupIntro.getVisibility() == View.GONE) {
                        readMore.setImageResource(R.drawable.ic_expand_less);
                        groupIntro.setVisibility(View.VISIBLE);
                    } else {
                        readMore.setImageResource(R.drawable.ic_expand_more);
                        groupIntro.setVisibility(View.GONE);
                    }

                }
            });
            join.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if(join.getText().equals("加入")){
                        group.setCtyMembers(Integer.parseInt(group.getCtyMembers()) + 1 + "");
                        peoples.setText(group.getCtyMembers() + "个成员");
                        join.setVisibility(View.GONE);
                        menuItems.get(1).setVisible(true);
                        menuItems.get(2).setVisible(true);
                        new joinCmtTask().execute("joinCty");
                    }
                }
            });

            peoples.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //UserListActivity.start(activity,group.);

                }
            });
        }

        private class joinCmtTask extends AsyncTask<String, Void, Void> {
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
                    object.put("easemobId", MyUser.easemobId);
                    object.put("ctyId", group.getCtyId());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                String json = JsonConnection.getJSON(object.toString());
//
                Log.i("mjson", json);
                return null;
            }

        }
    }

    public static class AtyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
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
        @Bind(R.id.plus)
        ImageView plus;
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

        public AtyViewHolder(View itemView, MyViewHolderClicks listener) {
            super(itemView);
            Log.d("recyclerview", "AtyViewHolder");
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
        public interface MyViewHolderClicks {
            public void onUserPhoto(CircleImageView user_photo, int position);

            public void onCommunity(TextView community, int position);

            public void onPicture(ImageView picture, int position);

            public void onCard(CardView cardView, int position);

            public void onPlus(ImageView fab, int adapterPosition, TextView plus);
        }
    }
}
