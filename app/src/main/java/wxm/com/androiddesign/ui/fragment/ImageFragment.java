package wxm.com.androiddesign.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import wxm.com.androiddesign.R;

/**
 * Created by Zero on 11/4/2015.
 */
public class ImageFragment extends Fragment {

    String url;

    public static Fragment newInstance(String url) {
        ImageFragment imageFragment=new ImageFragment();
        Bundle args=new Bundle();
        args.putString("url",url);
        imageFragment.setArguments(args);
        return imageFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        url = getArguments().getString("url");
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup viewGroup, Bundle savedInstanceState) {
        ImageView v = (ImageView)inflater.inflate(R.layout.image, viewGroup, false);
        Picasso.with(viewGroup.getContext()).load(url).into(v);
        return v;
    }
}