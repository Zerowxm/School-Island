package wxm.com.maca.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.GradientDrawable;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import wxm.com.maca.R;
import wxm.com.maca.module.Group;
import wxm.com.maca.ui.fragment.GroupListFragment;
import wxm.com.maca.utils.MyColorUtils;
import wxm.com.maca.utils.MyUtils;
import wxm.com.maca.utils.PaletteTransformation;

/**
 * Created by zero on 2015/6/30.
 */
public class GroupListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    List<Group> groups = new ArrayList<>();
    Context context;
    int type;

    public GroupListAdapter(List<Group> groups, Context context,int type) {
        this.groups = groups;
        this.context = context;
        this.type=type;
    }

    @Override
    public int getItemViewType(int position) {
        if (position==0||position==1){
            return 0;
        }
        else return 1;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType==0){
            return new MyHeaderHolder(
                    LayoutInflater.from(parent.getContext()).inflate(
                            R.layout.foo_item, parent, false
                    ));
        }else
            return new MyViewHolder(
                    LayoutInflater.from(parent.getContext()).inflate(
                            R.layout.community_item, parent, false
                    ));

    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int pos) {

        if (pos==0||pos==1){

        }else {
            int position=pos-2;
            if (type== GroupListFragment.HOT){
                if (position%3==0){
                    ((MyViewHolder)holder).community_img.getLayoutParams().height= MyUtils.getPixels(context,260);
                    Log.d("imageSize", "" + ((MyViewHolder)holder).community_img.getLayoutParams().height);
                    ((MyViewHolder)holder).community_img.requestLayout();
                }
            }else {
                Log.d("imageSize", "" + ((MyViewHolder)holder).community_img.getHeight());

            }
            Group group = groups.get(position);
            Picasso.with(context).load(group.getCtyIcon())
                    .fit().centerCrop()
                    .transform(PaletteTransformation.getInstance())
                    .into(((MyViewHolder)holder).community_img,new Callback.EmptyCallback(){
                        @Override
                        public void onSuccess() {
//                            Bitmap bitmap=((BitmapDrawable)((MyViewHolder)holder).community_img.getDrawable()).getBitmap();
//                            ((MyViewHolder)holder).community_img.animate().alpha(1).start();
//                            Palette palette=PaletteTransformation.getPalette(bitmap);
//                            Palette.Swatch vibrant=palette.getVibrantSwatch();
//                            Palette.Swatch vibrantLight=palette.getLightVibrantSwatch();
//                            Palette.Swatch vibrantDark=palette.getDarkVibrantSwatch();
//                            Palette.Swatch mutedSwatch=palette.getMutedSwatch();
//                            Palette.Swatch mutedLightSwatch=palette.getLightMutedSwatch();
//                            Palette.Swatch mutedDarkSwatch=palette.getDarkMutedSwatch();
//                            Palette.Swatch PopulerSwatch=MyColorUtils.getDominantSwatch(palette);
//                            Log.i("palette", "" + vibrant + vibrantDark + vibrantLight + mutedSwatch + mutedDarkSwatch + mutedLightSwatch);
//                            MyColorUtils.getINSTANCE();
//                            GradientDrawable gd=new GradientDrawable(GradientDrawable.Orientation.BOTTOM_TOP,
//                                    new int[]{PopulerSwatch.getRgb(),0xffffff});
//                            gd.setCornerRadius(0f);
//                            ((MyViewHolder)holder)._shadow.setBackground(gd);
                           // ((MyViewHolder)holder).shadow.setBackgroundColor(PopulerSwatch.getRgb());
                            //((MyViewHolder)holder).community_name.setTextColor(PopulerSwatch.getTitleTextColor());
                           // ((MyViewHolder)holder).member_num.setTextColor(PopulerSwatch.getBodyTextColor());
//
                        }
                    });
            Picasso.with(context).load(group.getUserIcon()).into( ((MyViewHolder)holder).userPhoto);
            ((MyViewHolder)holder).community_name.setText(group.getCtyName());
            ((MyViewHolder)holder).member_num.setText(group.getCtyMembers() + "个成员");
        }
    }

    @Override
    public int getItemCount() {
        return groups.size()+2;
    }

    class MyHeaderHolder extends RecyclerView.ViewHolder{

        public MyHeaderHolder(View itemView) {
            super(itemView);
        }
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.group_img)
        ImageView community_img;
        @Bind(R.id.member_num)
        TextView member_num;
        @Bind(R.id.group_name)
        TextView community_name;
       // @Bind(R.id.card_view)
//        CardView cardView;
//        @Bind(R.id.hot_tag)
//        TextView hotTag;
//        @Bind(R.id.shadow)
//        View shadow;
//        @Bind(R.id._shadow)
//        View _shadow;
       // FrameLayout shadow;
        @Bind(R.id.user_photo)
        ImageView userPhoto;

        public MyViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
