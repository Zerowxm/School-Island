package wxm.com.androiddesign;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.v4.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

/**
 * Created by hdchen on 2015/6/30.
 */
public class MyDialog extends DialogFragment{

    private ImageView imageView;
    private Button savebutton;
    private Bitmap bitmap;
    private Uri uri;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.dialog_layout, null);
        imageView = (ImageView)view.findViewById(R.id.image_show);
        savebutton = (Button)view.findViewById(R.id.savaImage);
        //imageView.setImageBitmap(bitmap);
        Glide.with(this).load(uri).into(imageView);

        view.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                MyDialog.this.dismiss();
                return false;
            }
        });

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyDialog.this.dismiss();
            }
        });

        savebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(v.getContext(), "Save image", Toast.LENGTH_SHORT).show();
            }
        });

        imageView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                System.out.println("save image!");
                return false;
            }
        });
        return  view;
    }

    @Override
    public void onResume() {
        super.onResume();
//        Glide.with(this).load(uri).into(imageView);
}

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, android.R.style.Theme_Black_NoTitleBar_Fullscreen);
    }

    public void setBitmap(Bitmap bitmap){
       this.bitmap = bitmap;
    }

    public void setUri(Uri uri){
        this.uri = uri;
    }

}
