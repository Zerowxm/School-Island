package wxm.com.androiddesign.ui;

/**
 * Created by Zero on 10/16/2015.
 */

import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import wxm.com.androiddesign.R;
import wxm.com.androiddesign.module.AtyItem;
import wxm.com.androiddesign.module.MyUser;
import wxm.com.androiddesign.network.JsonConnection;
import wxm.com.androiddesign.ui.fragment.HomeFragment;
import wxm.com.androiddesign.utils.MyBitmapFactory;
import wxm.com.androiddesign.utils.MyUtils;


import android.support.v4.view.GravityCompat;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.time.RadialPickerLayout;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


public class PublishActivity extends BaseActivity implements TimePickerDialog.OnTimeSetListener,DatePickerDialog.OnDateSetListener{
    private int timeType;
    private String groupName ="";
    private String groupId="";
    private ArrayList<String> uriList = new ArrayList<>();
    //private ArrayList<String> tempUriList = new ArrayList<>();
    private String tagList = "";
    private Uri selectedImgUri;
    private long exitTime = 0;
    AtyItem atyItem;
    private RelativeLayout.LayoutParams layoutParams;

    public static final int START_TIME = 0x1;
    public static final int END_TIME = 0x2;
    public static final int TAKE_PHOTO = 1;
    public static final int CHOOSE_PHOTO = 2;
    public static final int GET_LOCATION = 3;

    float scale;
    String mTime;
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.edit_aty)
    EditText editAty;
    @Bind(R.id.content)
    TextView atyContent;
    @Bind(R.id.start_time)
    TextView startTime;
    @Bind(R.id.end_time)
    TextView endTime;
    @Bind(R.id.location)
    TextView location;
    @Bind(R.id.imageViewContainer)
    LinearLayout imageContains;
    @Bind(R.id.tag_container)
    LinearLayout tagContainer;
    @Bind(R.id.group_pannel)
    LinearLayout groupPannel;
    @Bind(R.id.tag_hint)
    TextView tag_hint;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.release_layout);
        ButterKnife.bind(this);
        setupToolBar(toolbar);
        Bundle bundle=getIntent().getExtras();
        groupId =bundle.getString("groupId");
        if (!"".equals(groupId)){
            groupName=bundle.getString("groupName");
        }

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
//        userName.setText(MyUser.userName);
//        Picasso.with(this).load(MyUser.userIcon).into(userPhoto);
        DisplayMetrics displaymetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        int screenWidth = displaymetrics.widthPixels - 7;
        int screenHeight = displaymetrics.heightPixels;
        layoutParams = new RelativeLayout.LayoutParams(screenWidth, screenHeight * 2 / 5);
        scale=getResources().getDisplayMetrics().density;
        setup();
    }

    private void setup(){
        if (!"".equals(groupId)){
            groupPannel.setVisibility(View.VISIBLE);
            TextView groupName=(TextView)groupPannel.getChildAt(1);
            groupName.setText(this.groupName);
        }
    }


    public void addTag(View view){
        new MaterialDialog.Builder(this)
                .title("标签")
                .items(R.array.tag_list)
                .itemsCallbackMultiChoice(null, new MaterialDialog.ListCallbackMultiChoice() {
                    @Override
                    public boolean onSelection(MaterialDialog dialog, Integer[] which, CharSequence[] text) {
                        int dpAsPixels = (int) (25 * scale + 0.5f);
                        if (text.length==0){
                            return false;
                        }else
                        for (CharSequence tagText : text) {
                            LinearLayout tagView = (LinearLayout) LayoutInflater.from(PublishActivity.this)
                                    .inflate(R.layout.tag_view, null);
                            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                                    dpAsPixels);
                            params.setMargins(0, 0, 20, 0);
                            tagView.setLayoutParams(params);
                            TextView tag = (TextView) tagView.getChildAt(0);
                            tag.setText(tagText);
                            tagView.setTag(tagContainer.getChildCount());
                            tagContainer.addView(tagView, tagContainer.getChildCount());
                            if (tagList.equals(""))
                                tagList += tagText;
                            else
                                tagList += "," + tagText;
                        }
                        tag_hint.setVisibility(View.GONE);
                        Log.d("Tag",tagList);
                        return true;
                    }
                })
                .positiveText("确定")
                .show();
    }

    public void removeTag(View view) {
        ((ViewGroup)view.getParent()).removeView(view);
    }

    public void removePicture(View view) {
        ((ViewGroup)view.getParent()).removeView(view);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == CHOOSE_PHOTO) {
                Uri chosenImageUri = data.getData();
                selectedImgUri = chosenImageUri;
                final RelativeLayout imageItem = (RelativeLayout) LayoutInflater.from(this).inflate(R.layout.image_item, null);
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
                    new AsyncTask<Void,Void,Void>(){
                        @Override
                        protected void onPostExecute(Void aVoid) {
                            super.onPostExecute(aVoid);
                            try {
                                Bitmap bitmap = MediaStore.Images.Media.getBitmap(PublishActivity.this.getContentResolver(), selectedImgUri);
                                imageContains.addView(imageItem);
                                uriList.add(MyBitmapFactory.BitmapToString(bitmap));
                                //tempUriList.add(selectedImgUri+"");
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                        @Override
                        protected Void doInBackground(Void... params) {
                            return null;
                        }
                    }.execute();
                }
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
                        //tempUriList.add(selectedImgUri+"");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    @OnClick(R.id.fab)
    public void send() {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        try {
            if (startTime.getText().toString().equals("没有开始时间")) {
                Toast.makeText(getApplicationContext(), "设置你的开始时间", Toast.LENGTH_SHORT).show();
            } else if (endTime.getText().toString().equals("没有结束时间")) {
                Toast.makeText(getApplicationContext(), "设置你的结束时间", Toast.LENGTH_SHORT).show();
            } else {
                Date d1 = df.parse(startTime.getText().toString());
                Date d2 = df.parse(endTime.getText().toString());
                long diff = d1.getTime() - d2.getTime();
                if (diff >= 0) {
                    Toast.makeText(getApplicationContext(), "结束时间必须迟于开始时间", Toast.LENGTH_SHORT).show();
                } else if (editAty.getText().toString().equals("")) {
                    Toast.makeText(getApplicationContext(), "设置你的活动标题", Toast.LENGTH_SHORT).show();
                } else if (atyContent.getText().toString().equals("没有活动内容")) {
                    Toast.makeText(getApplicationContext(), "设置你的活动内容", Toast.LENGTH_SHORT).show();
                } else if (location.getText().toString().equals("没有活动地点")) {
                    Toast.makeText(getApplicationContext(), "设置你的活动地点", Toast.LENGTH_SHORT).show();
                } else {
                    Log.d("zerowxm", "ok5");
//                    atyItem = new AtyItem("releaseByPerson", MyUser.userId, editAty.getText().toString(), "暂时没有社区", startTime.getText().toString(),
//                            endTime.getText().toString(), location.getText().toString(), "1",
//                            atyContent.getText().toString(), "0", "0",
//                            "true", "false", "0", uriList,tagList);
                    atyItem=new AtyItem("releaseByPerson",MyUser.userId, editAty.getText().toString(),
                            tagList,
                            startTime.getText().toString(),
                            endTime.getText().toString(), location.getText().toString(),
                            atyContent.getText().toString(),
                            uriList);
                    Log.d("zerowxm", "ok6");
                    new UpDateTask().execute(atyItem);
                    Log.d("zerowxm", "ok7");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), "错误!", Toast.LENGTH_SHORT).show();
        }

    }

    private class UpDateTask extends AsyncTask<AtyItem, Void, Void> {
        String id = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //HomeFragment.addActivity(atyItem);
            Toast.makeText(getApplicationContext(),"发布正在后台进行",Toast.LENGTH_SHORT).show();
//            Intent intent = new Intent(getApplicationContext(),MainActivity.class);
//            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
//            startActivity(intent);
            finish();
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            atyItem.setAtyId(id);
            Log.d("publishId", "id?");
            Log.d("publishId",id);
            Toast.makeText(getApplicationContext(),"发布成功",Toast.LENGTH_SHORT).show();
            //finish();
        }

        @Override
        protected Void doInBackground(AtyItem... params) {

            JSONObject object = new JSONObject();
            try {
                if(!groupId.equals("")) {
                    params[0].setAction("releaseByCty");
                }
                params[0].setAtyCtyId(groupId);
                params[0].setAtyAlbum(uriList);
                object = new JSONObject(new Gson().toJson(params[0]));
                object.put("easemobId",MyUser.getEasemobId());
            } catch (JSONException e) {
                e.printStackTrace();
            }
            id = JsonConnection.getJSON(object.toString());
            return null;
        }

    }

    @OnClick(R.id.set_start_time)
    public void addStartTime() {
        timeType = START_TIME;
        setDate();
    }

    @OnClick(R.id.set_end_time)
    public void addEndTime() {
        timeType = END_TIME;
        setDate();
    }

    @Override
    public void onDateSet(DatePickerDialog datePickerDialog, int year, int monthOfYear, int dayOfMonth) {
        mTime=year+"-"+(monthOfYear+1)+"-"+dayOfMonth;
        setTime();
    }

    @Override
    public void onTimeSet(RadialPickerLayout radialPickerLayout, int hourOfDay, int minute) {
        if (minute<10){
            mTime+=" "+hourOfDay+":"+"0"+minute;
        }else
            mTime+=" "+hourOfDay+":"+minute;
        if (timeType == START_TIME) {
            startTime.setText(mTime);
        } else {
            endTime.setText(mTime);
        }
    }

    private void setTime(){
        Calendar mNowTime=Calendar.getInstance();
        TimePickerDialog timePickerDialog= TimePickerDialog.newInstance(
                PublishActivity.this,
                mNowTime.get(Calendar.HOUR_OF_DAY),
                mNowTime.get(Calendar.MINUTE),
                false
        );
        timePickerDialog.setThemeDark(false);
        timePickerDialog.vibrate(true);
        timePickerDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {

            }
        });
        timePickerDialog.show(getFragmentManager(), "Timepickerdialog");
    }

    private void setDate(){
        Calendar mNowTime=Calendar.getInstance();
        DatePickerDialog datePickerDialog= DatePickerDialog.newInstance(
                PublishActivity.this,
                mNowTime.get(Calendar.YEAR),
                mNowTime.get(Calendar.MONTH),
                mNowTime.get(Calendar.DAY_OF_MONTH)
        );
        datePickerDialog.setThemeDark(false);
        datePickerDialog.vibrate(true);
        datePickerDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {

            }
        });
        datePickerDialog.show(getFragmentManager(), "Datepickerdialog");
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

    }

    @OnClick(R.id.add_image)
    public void addImage(){
        MyUtils.chooseImage(this, CHOOSE_PHOTO);
    }

    @Override
    public void onBackPressed() {
        new MaterialDialog.Builder(this)
                .title("主人你确定要舍弃该活动吗？")
                .positiveText("是的")
                .negativeText(R.string.cancel)
                .callback(new MaterialDialog.ButtonCallback() {
                    @Override
                    public void onPositive(MaterialDialog dialog) {
                        finish();
                    }

                    @Override
                    public void onNegative(MaterialDialog dialog) {
                    }
                })
                .show();
    }
    /*@OnClick(R.id.add_image)
    public void addImg() {
>>>>>>> Stashed changes


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
/*
    @OnClick(R.id.take_photo)
    public void takeImg() {
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        selectedImgUri = Uri.fromFile(getOutPhotoFile());
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, selectedImgUri);
        startActivityForResult(cameraIntent, TAKE_PHOTO);
    }*/

//    private File getOutPhotoFile() {
//        File directory = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
//                getPackageName());
//        return new File(directory.getPath() + File.separator + getPhotoFileName());
//    }
//
//    private String getPhotoFileName() {
//        Date date = new Date(System.currentTimeMillis());
//        SimpleDateFormat dateFormat = new SimpleDateFormat("'IMG'_yyyyMMdd_HHmmss");
//        return dateFormat.format(date) + ".jpg";
//    }
}
