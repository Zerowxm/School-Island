package wxm.com.androiddesign.adapter;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.os.AsyncTask;
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

import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import wxm.com.androiddesign.R;
import wxm.com.androiddesign.module.AtyItem;
import wxm.com.androiddesign.module.MyUser;
import wxm.com.androiddesign.network.JsonConnection;
import wxm.com.androiddesign.ui.AtyDetailActivity;
import wxm.com.androiddesign.ui.GroupAcitivity;
import wxm.com.androiddesign.ui.ImageViewerActivity;
import wxm.com.androiddesign.utils.ActivityStartHelper;
import wxm.com.androiddesign.utils.MyUtils;


/**
 * Created by zero on 2015/6/25.
 */
public class UserAtyRecycerAdapter extends RecyclerView.Adapter<UserAtyRecycerAdapter.MyViewHolder> {
    protected List<AtyItem> activityItems;
    private int lastPosition = -1;
    private Context activity;
    AtyItem item;
    String fragment;

    public UserAtyRecycerAdapter(List<AtyItem> activityItemArrayList, Context activity, String fragment) {
        activityItems = activityItemArrayList;
        this.activity = activity;
        this.fragment = fragment;
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        notifyDataSetChanged();
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.aty_item_profile, parent, false);
        Log.d("recyclerview", "onCreateViewHolder");
        return new MyViewHolder(itemView, new MyViewHolder.MyViewHolderClicks() {


            @Override
            public void onCard(CardView cardView, int position) {
                Intent intent = new Intent(activity, AtyDetailActivity.class);
                intent.putExtra("com.wxm.com.androiddesign.module.ActivityItemData", activityItems.get(position));
                ((Activity) activity).startActivityForResult(intent, ((Activity) activity).RESULT_OK);
            }

            @Override
            public void onDelete(ImageView delete, int adapterPosition) {

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
                object.put("userName", MyUser.userName);
                object.put("atyId", item.getAtyId());
                object.put("easemobId", MyUser.getEasemobId());
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

        holder.atyPlace.setText(item.getAtyPlace());
        holder.atyMark.setColorFilter(ContextCompat.getColor(activity, R.color.gree));
        holder.atyMember.setText(item.getAtyMembers()+"人参加");
        holder.atyName.setText(item.getAtyName());
        holder.atyTime.setText(item.getAtyStartTime().replace("\n"," "));
    }

    private static boolean isFirst = true;

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
        @Bind(R.id.card_view)
        CardView cardView;
        @Bind(R.id.aty_mark)
        ImageView atyMark;
        @Bind(R.id.aty_name)
        TextView atyName;
        @Bind(R.id.aty_time)
        TextView atyTime;
        @Bind(R.id.aty_place)
        TextView atyPlace;
        @Bind(R.id.aty_member)
        TextView atyMember;
        @Bind(R.id.delete_btn)
        ImageView deleteBtn;

        public MyViewHolder(View itemView, MyViewHolderClicks listener) {
            super(itemView);
            mListener = listener;
            ButterKnife.bind(this, itemView);
            cardView.setOnClickListener(this);
            deleteBtn.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (v instanceof CardView) {
                mListener.onCard((CardView) v, getAdapterPosition());
            } else if (v instanceof ImageView) {
                mListener.onDelete((ImageView) v, getAdapterPosition());
            }
        }

        public interface MyViewHolderClicks {
            public void onCard(CardView cardView, int position);

            public void onDelete(ImageView delete, int adapterPosition);
        }
    }
}
