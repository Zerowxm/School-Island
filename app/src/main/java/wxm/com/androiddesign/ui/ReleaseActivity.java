package wxm.com.androiddesign.ui;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import de.hdodenhof.circleimageview.CircleImageView;

import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ViewFlipper;
import android.view.ViewGroup.LayoutParams;

import com.bumptech.glide.Glide;

import java.io.File;
import java.io.IOException;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

import wxm.com.androiddesign.R;
import wxm.com.androiddesign.ui.fragment.ActivityFragment;
import wxm.com.androiddesign.ui.fragment.DatePickerFragment;

public class ReleaseActivity extends AppCompatActivity implements View.OnClickListener,
        DatePickerFragment.DatePickCallBack {

    public static final int TAKE_PHOTO=1;
    public static final int CHOOSE_PHOTO=2;
    public static final int GET_LOCATION=3;
    private Uri selectedImgUri;
    @Bind(R.id.time)
    TextView timeText;
    @Bind(R.id.location)
    TextView locaton;
    @Bind(R.id.add_image)
    ImageView add_image;
    @Bind(R.id.imageViewContainer)
    LinearLayout imageContains;
    @Bind(R.id.aty_content)
    EditText aty_content;
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
    public void onClick(View v) {
        //ActivityFragment.addActivity(textView.getText().toString(),"tag","time","0","0",R.drawable.miao);
        MainActivity.instance.finish();
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }

    @Override
    public void addTime(String time) {
        timeText.setText(time);
    }
}
