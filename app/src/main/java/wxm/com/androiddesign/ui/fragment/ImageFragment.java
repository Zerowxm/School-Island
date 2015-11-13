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
import wxm.com.androiddesign.photoview.PhotoView;

/**
 * Created by Zero on 11/4/2015.
 */
public class ImageFragment extends Fragment {

    String url;
    int type;

    public static Fragment newInstance(String url,int type) {
        ImageFragment imageFragment=new ImageFragment();
        Bundle args=new Bundle();
        args.putString("url",url);
        args.putInt("type",type);
        imageFragment.setArguments(args);
        return imageFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        url = getArguments().getString("url");
        type=getArguments().getInt("type");
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup viewGroup, Bundle savedInstanceState) {
        ImageView v;
        if (type==0){
            v=new PhotoView(getContext());
        }
        else {
            v =new ImageView(getActivity());

        }
        Picasso.with(viewGroup.getContext()).load(getBigImage(url)).into(v);
        return v;
    }

    public static String getBigImage(String smallImage){
        String []splits = smallImage.split("/");
        splits[splits.length-1] = "i"+ splits[splits.length-1];
        String bitImage = "";
        for(int i = 0 ; i < splits.length ; i++){
            if(i == 0)
                bitImage = splits[i];
            else
                bitImage += "/"+splits[i];
        }
        return bitImage;
    }

}
