package wxm.com.androiddesign.adapter;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import wxm.com.androiddesign.R;

/**
 * Created by zero on 2015/7/15.
 */
public class PhotoAdapter extends RecyclerView.Adapter<PhotoAdapter.MyPhotoViewHolder> {

    List<String> ablum = new ArrayList<String>();
    Context context;

    public PhotoAdapter(List<String> ablum,Context context) {
        this.ablum = ablum;
        this.context=context;
    }

    @Override
    public MyPhotoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyPhotoViewHolder(
                LayoutInflater.from(parent.getContext()).inflate(
                        R.layout.image, parent, false
                ));
    }

    @Override
    public void onBindViewHolder(MyPhotoViewHolder holder, int position) {
        String uri=ablum.get(position);
        Picasso.with(context).load(uri).into(holder.photo);
    }

    @Override
    public int getItemCount() {
        return ablum.size();
    }

    public static class MyPhotoViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.photo)ImageView photo;
        public MyPhotoViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
}
