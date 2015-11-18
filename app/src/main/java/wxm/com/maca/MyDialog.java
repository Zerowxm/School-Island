package wxm.com.maca;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
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
import android.widget.ImageView;
import android.widget.Toast;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.RequestCreator;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.OnClick;
import wxm.com.maca.photoview.PhotoView;

/**
 * Created by hdchen on 2015/6/30.
 */
public class MyDialog extends DialogFragment {

    private static final String TAG="MyDialog";

    private ImageView imageView;
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
        Log.d("dialog", uri + "onCreateView");
        imageView = (PhotoView) view.findViewById(R.id.image_show);
        Picasso.with(getActivity()).load(uri).into(imageView);
        new LoadImage().execute(uri);
//        DisplayMetrics displaymetrics = new DisplayMetrics();
//        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
//        int screenWidth = displaymetrics.widthPixels - 2;
//        ViewGroup.LayoutParams layoutParams = imageView.getLayoutParams();
//        layoutParams.height = layoutParams.height*screenWidth/layoutParams.width;
//        layoutParams.width = screenWidth;
//        imageView.setLayoutParams(layoutParams);
//        Log.d(TAG, layoutParams.width + "/" + layoutParams.height);
//        imageView.setScaleType(ImageView.ScaleType.FIT_XY);
        view.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                MyDialog.this.dismiss();
                return false;
            }
        });
        return view;
    }

    private class LoadImage extends AsyncTask<String, Void, Void> {

        String uri;
        RequestCreator creator;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            uri = getBigImage(uri);
            Log.d("bigImage", uri);
            Picasso.with(getActivity()).load(uri).into(imageView);
            DisplayMetrics displaymetrics = new DisplayMetrics();
            getActivity().getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
            int screenWidth = displaymetrics.widthPixels - 2;
            int screenHeight = displaymetrics.heightPixels;
            ViewGroup.LayoutParams layoutParams = imageView.getLayoutParams();
            //layoutParams.height = layoutParams.height*screenWidth/layoutParams.width;

            layoutParams.width = screenWidth;
            imageView.setLayoutParams(layoutParams);
            Log.d(TAG, layoutParams.width + "/" + layoutParams.height);
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        }

        @Override
        protected Void doInBackground(String... params) {
            this.uri = params[0];
            return null;
        }
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

    @OnClick(R.id.image_show)
    public void showImage(){
        MyDialog.this.dismiss();
    }
    @OnClick(R.id.savaImage)
    public void saveImage(){
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
            if (!f.exists()) {
                Toast.makeText(getContext(), "保存失败", Toast.LENGTH_SHORT).show();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            MediaStore.Images.Media.insertImage(getActivity().getContentResolver(),
                    f.getAbsolutePath(), getPhotoFileName(), null);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        getActivity().sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://" + f.getPath())));
        Toast.makeText(getContext(), "图片已保存", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onResume() {
        super.onResume();
//        uri = getBigImage(uri);
//        Log.d("bigImage",uri);
//        Picasso.with(getActivity()).load(uri).into(imageView);
//        DisplayMetrics displaymetrics = new DisplayMetrics();
//        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
//        int screenWidth = displaymetrics.widthPixels - 2;
//        int screenHeight = displaymetrics.heightPixels;
//        ViewGroup.LayoutParams layoutParams = imageView.getLayoutParams();
//        //layoutParams.height = layoutParams.height*screenWidth/layoutParams.width;
//
//        layoutParams.width = screenWidth;
//        imageView.setLayoutParams(layoutParams);
//        Log.d(TAG, layoutParams.width + "/" + layoutParams.height);
//        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
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

    @Override
    public void onSaveInstanceState(Bundle outState) {
        //outState.putString("WORKAROUND_FOR_BUG_19917_KEY", "WORKAROUND_FOR_BUG_19917_VALUE");
        //super.onSaveInstanceState(outState);
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

}
