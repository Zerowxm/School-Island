package wxm.com.androiddesign.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import wxm.com.androiddesign.R;

/**
 * Created by Zero on 11/23/2015.
 */
public class UpdatePhoto extends Fragment{
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.update_image,container,false);
        return v;
    }
}
