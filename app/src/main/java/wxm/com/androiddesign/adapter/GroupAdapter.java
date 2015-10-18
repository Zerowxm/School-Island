package wxm.com.androiddesign.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import wxm.com.androiddesign.R;
import wxm.com.androiddesign.module.Group;
import wxm.com.androiddesign.ui.MyApplication;
import wxm.com.androiddesign.utils.PaletteTransformation;

/**
 * Created by zero on 2015/6/30.
 */
public class GroupAdapter extends RecyclerView.Adapter<GroupAdapter.MyViewHolder> {

    static Palette mPalette;
    List<Group> groups = new ArrayList<>();
    Context context;

    public GroupAdapter(List<Group> groups, Context context) {
        this.groups = groups;
        this.context = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyViewHolder(
                LayoutInflater.from(parent.getContext()).inflate(
                        R.layout.community_item, parent, false
                ));
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        Group group = groups.get(position);
        holder.community_name.setText(group.getCtyId());
        holder.member_num.setText("成员数" + group.getCtyMembers());
//        Picasso.with(context).load(group.getCtyIcon()).transform(new PaletteGeneratorTransformation())
//                .into(holder.community_img, new PaletteGeneratorTransformation.Callback(holder.community_img) {
//                    @Override
//                    public void onPalette(Palette palette) {
//
//                       mPalette=palette;
//                        int vibrant = palette.getVibrantColor(0x000000);
//                        int vibrantLight = palette.getLightVibrantColor(0x000000);
//                        int vibrantDark = palette.getDarkVibrantColor(0x000000);
//                        int muted = palette.getMutedColor(0x000000);
//                        int mutedLight = palette.getLightMutedColor(0x000000);
//                        int mutedDark = palette.getDarkMutedColor(0x000000);
//                        Log.i("Palette", "" + vibrant + "/" + vibrantDark + "/" + vibrantLight);
//                    }
//                });
//        holder.cardView.setCardBackgroundColor(
//                        ContextCompat.getColor(MyApplication.applicationContext,
//                                R.color.primary));
        Picasso.with(context).load(group.getCtyIcon())
                .fit().centerCrop()
                .transform(PaletteTransformation.getInstance())
                .placeholder(R.drawable.test)
                .error(R.drawable.wu)
                .into(holder.community_img,new Callback.EmptyCallback(){
                    @Override
                    public void onSuccess() {
                        Bitmap bitmap=((BitmapDrawable)holder.community_img.getDrawable()).getBitmap();
                        holder.community_img.animate().alpha(1).start();
                        Palette palette=PaletteTransformation.getPalette(bitmap);
                        holder.cardView.setCardBackgroundColor(
                                palette.getVibrantColor(ContextCompat.getColor(MyApplication.applicationContext,
                                        R.color.primary))
                        );
                        holder.userPhoto.setVisibility(View.VISIBLE);
                    }
                });
    }

    @Override
    public int getItemCount() {
        return groups.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.group_img)
        ImageView community_img;
        @Bind(R.id.member_num)
        TextView member_num;
        @Bind(R.id.group_name)
        TextView community_name;
        @Bind(R.id.card_view)
        CardView cardView;
        @Bind(R.id.user_photo)
        ImageView userPhoto;

        public MyViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
