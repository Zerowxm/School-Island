package wxm.com.androiddesign.ui;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

import wxm.com.androiddesign.R;
import wxm.com.androiddesign.module.AtyItem;
import wxm.com.androiddesign.module.MyUser;
import wxm.com.androiddesign.module.User;
import wxm.com.androiddesign.network.JsonConnection;
import wxm.com.androiddesign.ui.fragment.ActivityFragment;
import wxm.com.androiddesign.ui.fragment.DatePickerFragment;
import wxm.com.androiddesign.ui.fragment.FragmentParent;
import wxm.com.androiddesign.ui.fragment.HomeFragment;
import wxm.com.androiddesign.utils.MyBitmapFactory;

public class ReleaseActivity extends AppCompatActivity implements DatePickerFragment.DatePickCallBack {

    private int timeType;
    public static final int STARTTIME = 0x1;
    public static final int ENDTIME = 0x2;

    public static final int TAKE_PHOTO = 1;
    public static final int CHOOSE_PHOTO = 2;
    public static final int GET_LOCATION = 3;

    private List<String> uriList = new ArrayList<>();
    String Location;
    private Uri selectedImgUri;
    private String userId;


    @Bind(R.id.sendButton)
    ImageView send;
    @Bind(R.id.user_photo)
    ImageView userPhoto;
    @Bind(R.id.user_name)
    TextView userName;

    @Bind(R.id.start_time)
    TextView startTime;
    @Bind(R.id.end_time)
    TextView endTime;
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
    @Bind(R.id.community_name)
    TextView community_name;
    private RelativeLayout.LayoutParams layoutParams;
    // @Bind(R.id.image_show)ViewFlipper viewFlipper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        super.onCreate(savedInstanceState);

        setContentView(R.layout.release_activity_layout);
        ButterKnife.bind(this);
        userName.setText(MyUser.userName);
        Intent intent = getIntent();
        userId = intent.getExtras().getString("userId");
        WindowManager windowManager = this.getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        int width = display.getWidth() - 7;
        int height = display.getHeight();
        layoutParams = new RelativeLayout.LayoutParams(width, height * 2 / 5);
        //layoutParams = new RelativeLayout.LayoutParams(width, height * 2 / 5);
    }
    @OnClick(R.id.community_name)
    public void chooseCmt(){
        new MaterialDialog.Builder(this)
                .title(R.string.community)
                .items(R.array.communities)
                .itemsCallbackSingleChoice(0, new MaterialDialog.ListCallbackSingleChoice() {
                    @Override
                    public boolean onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {
                        community_name.setText(text);
                        return true;
                    }
                })
                .positiveText(R.string.choose)
                .show();
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
                Location=address+" "+lattitute+" "+Longtitute;
                locaton.setText(address);
            }

            if (requestCode == CHOOSE_PHOTO) {
                Uri chosenImageUri = data.getData();
                selectedImgUri = chosenImageUri;
                RelativeLayout imageItem = (RelativeLayout) LayoutInflater.from(this).inflate(R.layout.image_item, null);
                ImageView imageView = (ImageView) imageItem.getChildAt(0);
                ImageView removeImage = (ImageView) imageItem.getChildAt(1);
                removeImage.setTag(imageContains.getChildCount());
                Log.d("image", "" + imageView.toString());
                imageView.setLayoutParams(layoutParams);
                Log.d("image", "" + imageView.toString());
                imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                Log.d("image", "" + imageView.toString());
                Picasso.with(this).load(selectedImgUri).into(imageView);
                Log.d("image", "" + imageView.toString());
                if (selectedImgUri != null) {
                    try {
                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImgUri);
                        imageContains.addView(imageItem);
                        uriList.add(MyBitmapFactory.BitmapToString(bitmap));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
//                for (int i=0;i<uriList.size();i++){
//                    RelativeLayout imageItem1 = (RelativeLayout) LayoutInflater.from(this).inflate(R.layout.image_item, null);
//                    ImageView imageView1 = (ImageView) imageItem1.getChildAt(0);
//                    ImageView removeImage1 = (ImageView) imageItem1.getChildAt(1);
//                    removeImage1.setTag(imageContains.getChildCount());
//                    Log.d("image", "" + imageView1.toString());
//                    imageView.setLayoutParams(layoutParams);
//                    Log.d("image", "" + imageView1.toString());
//                    imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
//                    Log.d("image", "" + imageView1.toString());
//                    imageView1.setImageBitmap(MyBitmapFactory.StringToBitmap(uriList.get(i)));
//                    imageContains.addView(imageItem1);
//                }

            }
            if (requestCode == TAKE_PHOTO) {
                RelativeLayout imageItem = (RelativeLayout) LayoutInflater.from(this).inflate(R.layout.image_item, null);
                ImageView imageView = (ImageView) imageItem.getChildAt(0);
                ImageView removeImage = (ImageView) imageItem.getChildAt(1);
                removeImage.setTag(imageContains.getChildCount());
                Log.d("image", "" + imageView.toString());
                imageView.setLayoutParams(layoutParams);
                Log.d("image", "" + imageView.toString());
                imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                Log.d("image", "" + imageView.toString());
                Picasso.with(this).load(selectedImgUri).into(imageView);

                Log.d("image", "" + imageView.toString());

                if (selectedImgUri != null) {
                    try {
                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImgUri);
                        imageContains.addView(imageItem);
                        uriList.add(MyBitmapFactory.BitmapToString(bitmap));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

            }
        }
    }

    public void removePicture(View view) {
        Log.d("image", "" + view.getTag());
        int position = (int) view.getTag();
        imageContains.removeViewAt(position);
        uriList.remove(position);
        for (int i = 0; i < imageContains.getChildCount(); i++) {
            imageContains.getChildAt(i).findViewById(R.id.remove_image).setTag(i);
        }
    }

    @OnClick(R.id.sendButton)
    public void send() {
        new MaterialDialog.Builder(this)
                .title(R.string.permission)
                .items(R.array.permissions)
                .itemsCallbackSingleChoice(2, new MaterialDialog.ListCallbackSingleChoice() {
                    @Override
                    public boolean onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {
                        DateFormat df = new SimpleDateFormat("yyyy.MM.dd HH:mm");
                        String temp="toVisitors";
                        try {
                            if (which==0){
                                temp="toMembers";
                            }else if(which==1){
                                temp="toUsers";
                            }else if (which==2){
                                temp="toVisitors";
                            }
                            if (startTime.getText().toString().equals("开始时间")) {
                                Toast.makeText(getApplicationContext(), "set your start time", Toast.LENGTH_SHORT).show();
                            } else if (endTime.getText().toString().equals("结束时间")) {
                                Toast.makeText(getApplicationContext(), "set your end time", Toast.LENGTH_SHORT).show();
                            } else {
                                Date d1 = df.parse(startTime.getText().toString());
                                Date d2 = df.parse(endTime.getText().toString());
                                long diff = d1.getTime() - d2.getTime();
                                if (diff >= 0) {
                                    Toast.makeText(getApplicationContext(), "end time must be later than start time", Toast.LENGTH_SHORT).show();
                                }else if(community_name.getText().toString().equals("choose community")){
                                    Toast.makeText(getApplicationContext(), "set your activity name", Toast.LENGTH_SHORT).show();
                                }else if (atyName.getText().toString().equals("")) {
                                    Toast.makeText(getApplicationContext(), "set your activity name", Toast.LENGTH_SHORT).show();
                                } else if (atyContent.getText().toString().equals("")) {
                                    Toast.makeText(getApplicationContext(), "set your activity content", Toast.LENGTH_SHORT).show();
                                } else if (locaton.getText().toString().equals("add your location")) {
                                    Toast.makeText(getApplicationContext(), "set your location", Toast.LENGTH_SHORT).show();
                                } else {
                                    AtyItem atyItem = new AtyItem("release", MyUser.userId,atyName.getText().toString(), community_name.getText().toString(), startTime.getText().toString(),
                                            endTime.getText().toString(), locaton.getText().toString(), "1",
                                            atyContent.getText().toString(), "0", "0",
                                            "true", "false", "0",temp, uriList);
                                    new UpDateTask().execute(atyItem);
                                    atyItem.setUserName(MyUser.userName);
                                    HomeFragment.addActivity(atyItem);
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            Toast.makeText(getApplicationContext(), "error!", Toast.LENGTH_SHORT).show();
                        }
                        return true; // allow selection
                    }
                })
                .positiveText(R.string.choose)
                .show();


    }

    private class UpDateTask extends AsyncTask<AtyItem, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            finish();
        }

        @Override
        protected Void doInBackground(AtyItem... params) {

            JSONObject object = new JSONObject();
            try {
//                object.put("atyItem",new Gson().toJson(params[0]));
//                object.put("releaseTime", String.valueOf(System.currentTimeMillis()));
                object = new JSONObject(new Gson().toJson(params[0]));
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                object.put("releaseTime", formatter.format(new Date(System.currentTimeMillis())));
                object.put("longitude",Location.split(" ")[2]);
                object.put("latitude",Location.split(" ")[1]);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            JsonConnection.getJSON(object.toString());
            return null;
        }
    }


    @OnClick(R.id.add_start_time)
    public void addStartTime() {
        timeType = STARTTIME;
        DatePickerFragment datePickerFragment = new DatePickerFragment();
        datePickerFragment.show(getSupportFragmentManager(), "date");
    }

    @OnClick(R.id.add_end_time)
    public void addEndTime() {
        timeType = ENDTIME;
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
        if (timeType == STARTTIME) {
            startTime.setText(time);
        } else {
            endTime.setText(time);
        }
    }


}
