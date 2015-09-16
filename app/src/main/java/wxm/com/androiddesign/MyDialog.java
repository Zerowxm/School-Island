package wxm.com.androiddesign;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by hdchen on 2015/6/30.
 */
public class MyDialog extends DialogFragment {

    private static final String TAG="MyDialog";

    private ImageView imageView;
    private Button savebutton;
    private Bitmap bitmap;
    private String uri;


    public static MyDialog newInstance(String uri) {
        MyDialog myDialog = new MyDialog();
        Bundle bundle = new Bundle();
        bundle.putString("uri", uri);
        myDialog.setArguments(bundle);
        return myDialog;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.dialog_layout, null);
        imageView = (MatrixImageView) view.findViewById(R.id.image_show);
        savebutton = (Button) view.findViewById(R.id.savaImage);
        Log.d("dialog", uri + "onCreateView");
        Picasso.with(getActivity()).load(uri).into(imageView);

        DisplayMetrics displaymetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        int screenWidth = displaymetrics.widthPixels - 2;
        int screenHeight = displaymetrics.heightPixels;
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
                screenWidth, screenHeight * 2 / 3);
        imageView.setLayoutParams(layoutParams);
        Log.d(TAG,layoutParams.width+"/"+layoutParams.height);
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);

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
                imageView.setDrawingCacheEnabled(true);
                Bitmap bitmap = Bitmap.createBitmap(imageView.getDrawingCache());
                imageView.setDrawingCacheEnabled(false);
                File directory = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                        getActivity().getPackageName());
                if (!directory.exists())
                    directory.mkdirs();

                File f = new File(directory.getPath() + File.separator + getPhotoFileName());
                if (f.exists()) {
                    f.delete();
                }
                try {
                    FileOutputStream out = new FileOutputStream(f);
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);
                    out.flush();
                    out.close();
                    if (f.exists()) {
                        Toast.makeText(v.getContext(), "已保存到 " + "Pictures/" + getActivity().getPackageName(), Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(v.getContext(), "保存失败", Toast.LENGTH_SHORT).show();
                    }
                } catch (FileNotFoundException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                // 其次把文件插入到系统图库
                try {
                    MediaStore.Images.Media.insertImage(getActivity().getContentResolver(),
                            f.getAbsolutePath(), getPhotoFileName(), null);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                // 最后通知图库更新
                getActivity().sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://" + f.getPath())));

                //  MediaScannerConnection.scanFile(MainActivity.this, new String[]{f.getAbsolutePath().toString()}, null, null);
            }
        });

        imageView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                System.out.println("save image!");
                return false;
            }
        });
        return view;
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
        uri = getArguments().getString("uri");
        Log.d("dialog", uri);
    }

    private String getPhotoFileName() {
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat dateFormat = new SimpleDateFormat("'IMG'_yyyyMMdd_HHmmss");
        return dateFormat.format(date) + ".jpg";
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putString("WORKAROUND_FOR_BUG_19917_KEY", "WORKAROUND_FOR_BUG_19917_VALUE");
        super.onSaveInstanceState(outState);
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

}
