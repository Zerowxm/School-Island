package wxm.com.androiddesign.adapter;

import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zero on 2015/7/15.
 */
public class PhotoAdapter extends RecyclerView.Adapter<PhotoAdapter.MyPhotoViewHolder> {

    List<String> ablum = new ArrayList<String>();

    @Override
    public MyPhotoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(MyPhotoViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public static class MyPhotoViewHolder extends RecyclerView.ViewHolder {

        public MyPhotoViewHolder(View itemView) {
            super(itemView);
        }
    }
}
