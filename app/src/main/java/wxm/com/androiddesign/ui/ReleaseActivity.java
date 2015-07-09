package wxm.com.androiddesign.ui;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import android.view.Display;
import android.widget.ImageView;
import android.widget.TextView;

import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

import wxm.com.androiddesign.R;
import wxm.com.androiddesign.module.AtyItem;
import wxm.com.androiddesign.ui.fragment.ActivityFragment;
import wxm.com.androiddesign.ui.fragment.DatePickerFragment;
import wxm.com.androiddesign.ui.fragment.FragmentParent;
import wxm.com.androiddesign.ui.fragment.HomeFragment;

public class ReleaseActivity extends AppCompatActivity implements DatePickerFragment.DatePickCallBack {

    public static final int TAKE_PHOTO = 1;
    public static final int CHOOSE_PHOTO = 2;
    public static final int GET_LOCATION = 3;
    private LinearLayout.LayoutParams layoutParams;
    private ArrayList<Uri> uriList = new ArrayList<Uri>();
    private Uri selectedImgUri;
    AtyItem atyItem = new AtyItem();
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

        WindowManager windowManager = this.getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        int width = display.getWidth() - 7;
        int height = display.getHeight();
        layoutParams = new LinearLayout.LayoutParams(width, height * 2 / 5);

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
//        if (data == null) {
//            return;
//        }


        if (resultCode == RESULT_OK) {
            if (requestCode == GET_LOCATION) {
                String address = data.getStringExtra(LocationActivity.Address);
                Double lattitute = data.getDoubleExtra(LocationActivity.Latitude, 0);
                Double Longtitute = data.getDoubleExtra(LocationActivity.Longtitude, 0);
                locaton.setText(address + lattitute + Longtitute);
            }

            if (requestCode == CHOOSE_PHOTO) {
                Uri chosenImageUri = data.getData();
                selectedImgUri = chosenImageUri;
                ImageView imageView = new ImageView(this);
                imageView.setLayoutParams(layoutParams);
                imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                Glide.with(this).load(selectedImgUri).into(imageView);
                if (selectedImgUri != null) {
                    imageContains.addView(imageView);
                    uriList.add(selectedImgUri);
                }
            }
            if (requestCode == TAKE_PHOTO) {
                ImageView imageView = new ImageView(this);
                imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                Glide.with(this).load(selectedImgUri).into(imageView);
                imageView.setLayoutParams(layoutParams);
                imageContains.addView(imageView);
                uriList.add(selectedImgUri);
            }
        }
    }

    @OnClick(R.id.sendButton)
    public void send() {
        AtyItem atyItem = new AtyItem("tag", timeText.getText().toString()
                , atyName.getText().toString(), atyContent.getText().toString(),
                R.drawable.miao, locaton.getText().toString(), "0", "0", uriList);
        HomeFragment.addActivity(atyItem);
        MainActivity.instance.finish();
        Intent intent = new Intent(ReleaseActivity.this, MainActivity.class);
        intent.putExtra("aa", true);
        finish();
        startActivity(intent);

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
        Intent chooseImage = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        chooseImage.setType("image/*");
        Intent chooserIntent = Intent.createChooser(photoPickerIntent, "Select Image");
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[]{chooseImage});
        photoPickerIntent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        startActivityForResult(chooserIntent, CHOOSE_PHOTO);

    }

    @OnClick(R.id.take_photo)
    public void takeImg() {
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        selectedImgUri = Uri.fromFile(getOutPhotoFile());
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, selectedImgUri);
        startActivityForResult(cameraIntent, TAKE_PHOTO);
    }

    private File getOutPhotoFile() {
        File directory = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                getPackageName());
        return new File(directory.getPath() + File.separator + getPhotoFileName());
    }

    private String getPhotoFileName() {
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat dateFormat = new SimpleDateFormat("'IMG'_yyyyMMdd_HHmmss");
        return dateFormat.format(date) + ".jpg";
    }

    @Override
    public void addTime(String time) {
        timeText.setText(time);
    }
}
