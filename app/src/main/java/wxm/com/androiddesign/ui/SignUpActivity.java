package wxm.com.androiddesign.ui;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.provider.MediaStore;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.transition.Explode;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;

import com.afollestad.materialdialogs.MaterialDialog;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;

import java.io.IOException;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import wxm.com.androiddesign.R;
import wxm.com.androiddesign.module.User;
import wxm.com.androiddesign.network.JsonConnection;
import wxm.com.androiddesign.utils.MyBitmapFactory;

public class SignUpActivity extends AppCompatActivity implements View.OnClickListener {

    public static final int CHOOSE_PHOTO = 0x1;

    @Bind(R.id.user_photo)
    CircleImageView user_photo;
    @Bind(R.id.username_edit_text)
    EditText user_name;
    @Bind(R.id.password_edit_text)
    EditText password;
    @Bind(R.id.emial_edit_text)
    EditText emial;
    @Bind(R.id.phone_edit_text)
    EditText phone;
    @Bind(R.id.checkbox_man)
    CheckBox checkBox_man;
    @Bind(R.id.checkbox_woman)
    CheckBox checkBox_woman;
    @Bind(R.id.password_edit_text_again)
    EditText password_again;
    @Bind(R.id.password_text_input_layout)
    TextInputLayout passwordInput;
    @Bind(R.id.phone_text_input_layout)
    TextInputLayout photoInput;
    @Bind(R.id.email_text_input_layout)
    TextInputLayout emailInput;
    @Bind(R.id.userid_edit_text)
    EditText user_id;
    private Uri selectedImgUri=null;
    String gender;
    User user;
    Bitmap bitmap = null;


    private void setupToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("SignUp");
        setSupportActionBar(toolbar);
        final ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setElevation(10);
    }

    private void setupTextInputLayout() {
        emailInput.setErrorEnabled(true);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().setExitTransition(new Explode());
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        ButterKnife.bind(this);
        setupTextInputLayout();
        setup();

        setupToolbar();
    }


    @OnClick(R.id.user_photo)
    public void chooseImg(CircleImageView v) {
        Intent photoPickerIntent = new Intent(Intent.ACTION_GET_CONTENT);
        photoPickerIntent.setType("image/*");
        Intent chooseImage = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        chooseImage.setType("image/*");
        Intent chooserIntent = Intent.createChooser(photoPickerIntent, "Select Image");
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[]{chooseImage});
        photoPickerIntent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        startActivityForResult(chooserIntent, CHOOSE_PHOTO);
    }

    @Override
    public void onClick(View v) {
        if (v instanceof CircleImageView) {

        }
    }

    public boolean isOnline() {
        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnected());
    }

    //    String action, String userName, String userPassword,
//    String userEmail, String userPhone,
//    String userGender, int userIcon
    @OnClick(R.id.signup_btn)
    public void signup() {
        String checkEmail = "^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";
        String checkPhoto = "^((13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$";
//        if(password.getText().toString().length()<6){
//            passwordInput.setErrorEnabled(true);
//            passwordInput.setError("密码不得少于六位");
//            return;
//        }else {
//            passwordInput.setErrorEnabled(false);
//            if(!password.getText().toString().equals(password_again.getText().toString())){
//                passwordInput.setErrorEnabled(true);
//                passwordInput.setError("密码输入不一致");
//                return;
//            }else {
//                passwordInput.setErrorEnabled(false);
//            }
//        }
//        if(!phone.getText().toString().matches(checkPhoto)){
//            photoInput.setErrorEnabled(true);
//            photoInput.setError("电话号码错误");
//            return;
//        }else {
//            photoInput.setErrorEnabled(false);
//        }if(!emial.getText().toString().matches(checkEmail)){
//            emailInput.setErrorEnabled(true);
//            emailInput.setError("邮箱格式错误");
//            return;
//        }else {
//            emailInput.setErrorEnabled(false);
//        }
//        if(bitmap==null){
//            Snackbar.make(user_photo,"photo",Snackbar.LENGTH_SHORT).show();
//            return;
//        }


        user = new User("signup", get(user_id), get(user_name), get(password), get(emial), get(phone),
                gender,"", "0");


        BackgroundTask task = new BackgroundTask(this);
        task.execute(user);
    }

    private class BackgroundTask extends AsyncTask<User, Void, Integer> {
        MaterialDialog materialDialog;
        AppCompatActivity activity;
        String mResult;

        public BackgroundTask(AppCompatActivity activity) {
            this.activity = activity;
        }

        @Override
        protected void onPreExecute() {
            Log.d("Task", "onPreExecute");
            materialDialog = new MaterialDialog.Builder(activity)
                    .title(R.string.signup_title)
                    .content(R.string.please_wait)
                    .progress(true, 0)
                    .progressIndeterminateStyle(false)
                    .show();

        }

        @Override
        protected void onPostExecute(Integer integer) {
            //super.onPostExecute(aBoolean);
            Log.d("Task", "onPostExecute");
            if (integer == 0) {
                materialDialog.dismiss();
                Intent intent=new Intent();
                intent.putExtra("userId",user.getUserId());
                intent.putExtra("userPassword",user.getUserPassword());
                setResult(RESULT_OK,intent);
                finish();
            } else if (integer==1){
                materialDialog.dismiss();
                new MaterialDialog.Builder(activity)
                        .title("注册失败")
                        .content("请设置头像")
                        .positiveText("确定")
                        .show();
            }else if(integer==2){
                materialDialog.dismiss();
                new MaterialDialog.Builder(activity)
                        .title("注册失败")
                        .content("用户名重复")
                        .positiveText("确定")
                        .show();
            }


        }

        @Override
        protected Integer doInBackground(User... params) {
            Log.d("Task", "doInBackground");
            try {
                Log.d("Task","Image");
                if (selectedImgUri==null){
                    bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.test);
                    //return 1;
                }else
                bitmap =MediaStore.Images.Media.getBitmap(activity.getContentResolver(),selectedImgUri);

            } catch (IOException e) {
                Log.d("Task","IOException");
                e.printStackTrace();
            }


            Log.d("Task", "doImage");
            String icon = MyBitmapFactory.BitmapToString(bitmap);
            Log.d("Task", "" + icon.length());
            Log.d("Task", "doImageEncode");
            if (!isOnline()) {
                return 2;
            }
            user.setUserIcon(icon);
            Gson gson = new Gson();
            Log.d("gson", gson.toJson(user));
            mResult = "";
            mResult = JsonConnection.getJSON(gson.toJson(user));
            Log.d("Task", "doConnection");
            if (mResult != "") {
                if (mResult.contains("false")) {
                    return 2;
                } else if (mResult.contains("true"))
                    return 0;
            }
            return 2;

        }
    }

    public static String get(EditText editText) {
        return editText.getText().toString();
    }

    private void setup() {
        checkBox_man.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    checkBox_woman.setChecked(false);
                    gender = checkBox_man.getText().toString();
                }
            }
        });
        checkBox_woman.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    checkBox_man.setChecked(false);
                    gender = checkBox_woman.getText().toString();
                }
            }
        });
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            Uri chosenImageUri = data.getData();
            selectedImgUri = chosenImageUri;

            Glide.with(this).load(selectedImgUri).into(user_photo);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_user_acitivity, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
