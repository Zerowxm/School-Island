package wxm.com.androiddesign.ui;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import android.widget.ImageView;
import android.widget.TextView;

import android.view.WindowManager;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

import wxm.com.androiddesign.R;
import wxm.com.androiddesign.module.AtyItem;
import wxm.com.androiddesign.ui.fragment.ActivityFragment;
import wxm.com.androiddesign.ui.fragment.DatePickerFragment;

public class ReleaseActivity extends AppCompatActivity implements DatePickerFragment.DatePickCallBack {

    public static final int TAKE_PHOTO=1;
    public static final int CHOOSE_PHOTO=2;
    public static final int GET_LOCATION=3;

    private List<Uri> uriList=new ArrayList<Uri>();
    private Uri selectedImgUri;
    AtyItem atyItem=new AtyItem();
    @Bind(R.id.sendButton)
    ImageView send;
    @Bind(R.id.user_photo)
    ImageView userPhoto;
    @Bind(R.id.user_name)
    TextView userName;
//    @Bind(R.id.tag)
//    TextView tag;
    @Bind(R.id.time)
    TextView timeText;
    @Bind(R.id.aty_name)
    TextView atyName;
    @Bind(R.id.aty_content)
    TextView atyContent;
    @Bind(R.id.location)
    TextView locaton;
    @Bind(R.id.add_image)
    ImageView add_image;
    @Bind(R.id.imageViewContainer)
    LinearLayout imageContains;
    // @Bind(R.id.image_show)ViewFlipper viewFlipper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        super.onCreate(savedInstanceState);

        setContentView(R.layout.release_activity_layout);
        ButterKnife.bind(this);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data == null) {
            return;
        }
        if(requestCode == GET_LOCATION){
            String address = data.getStringExtra(LocationActivity.Address);
            Double lattitute = data.getDoubleExtra(LocationActivity.Latitude, 0);
            Double Longtitute = data.getDoubleExtra(LocationActivity.Longtitude, 0);
            locaton.setText(address + lattitute + Longtitute);
        }

        if (requestCode == CHOOSE_PHOTO) {
            Uri chosenImageUri = data.getData();
            selectedImgUri = chosenImageUri;
            Bitmap mBitmap = null;
            try {
                mBitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), chosenImageUri);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if(requestCode==TAKE_PHOTO){

        }
    }

    @OnClick(R.id.sendButton)
    public void send() {
        AtyItem atyItem = new AtyItem("tag",timeText.getText().toString()
                ,atyName.getText().toString(),atyContent.getText().toString(),
                R.drawable.miao,locaton.getText().toString(),"0","0",uriList);
        ActivityFragment.addActivity(atyItem);
        MainActivity.instance.finish();
        startActivity(new Intent(ReleaseActivity.this, MainActivity.class));
        finish();
    }

    @OnClick(R.id.add_time)
    public void addTime() {
        DatePickerFragment datePickerFragment = new DatePickerFragment();
        datePickerFragment.show(getSupportFragmentManager(), "date");
    }

    @OnClick(R.id.add_location)
    public void addLoc() {
        Intent intent = new Intent(ReleaseActivity.this, LocationActivity.class);
        startActivityForResult(intent, GET_LOCATION);


    }

    @OnClick(R.id.add_image)
    public void addImg() {
        Intent photoPickerIntent = new Intent(Intent.ACTION_GET_CONTENT);
        photoPickerIntent.setType("image/*");
        photoPickerIntent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        startActivityForResult(photoPickerIntent, CHOOSE_PHOTO);
        ImageView imageView = new ImageView(this);
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        Glide.with(this).load(selectedImgUri).into(imageView);
        imageContains.addView(imageView);
        uriList.add(selectedImgUri);
    }

    @OnClick(R.id.take_photo)
    public void takeImg(){
        Intent cameraIntent=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        File img=getOutPhotoFile();
        selectedImgUri=Uri.fromFile(getOutPhotoFile());
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT,selectedImgUri);
        startActivityForResult(cameraIntent, TAKE_PHOTO);
        ImageView imageView = new ImageView(this);
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        Glide.with(this).load(selectedImgUri).into(imageView);
        imageContains.addView(imageView);
    }

    private File getOutPhotoFile(){
        File directory=new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                getPackageName());
        if(!directory.exists()){
            if(!directory.mkdirs()) {
                Log.e("www", "eroo");
                return null;
            }
        }

        return new File(directory.getPath()+File.separator+"test");
    }

    @Override
    public void addTime(String time) {
        timeText.setText(time);
    }
}
