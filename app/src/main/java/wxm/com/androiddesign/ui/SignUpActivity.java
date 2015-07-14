package wxm.com.androiddesign.ui;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.transition.Explode;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;

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

public class SignUpActivity extends AppCompatActivity implements View.OnClickListener{

    public static final int CHOOSE_PHOTO=0x1;

    @Bind(R.id.user_photo)CircleImageView user_photo;
    @Bind(R.id.username_edit_text)EditText user_name;
    @Bind(R.id.password_edit_text)EditText password;
    @Bind(R.id.emial_edit_text)EditText emial;
    @Bind(R.id.phone_edit_text)EditText phone;
    @Bind(R.id.checkbox_man)CheckBox checkBox_man;
    @Bind(R.id.checkbox_woman)CheckBox checkBox_woman;
    @Bind(R.id.password_edit_text_again)EditText password_again;
    @Bind(R.id.password_text_input_layout)TextInputLayout passwordInput;
    @Bind(R.id.phone_text_input_layout)TextInputLayout photoInput;
    @Bind(R.id.email_text_input_layout)TextInputLayout emailInput;
    @Bind(R.id.userid_edit_text)EditText user_id;
    private Uri selectedImgUri;
    String grant;
    User user;


    private void setupToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("SignUp");
        setSupportActionBar(toolbar);
        final ActionBar actionBar = getSupportActionBar();
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
        //setupToolbar();
    }



    @OnClick(R.id.user_photo)
    public void chooseImg(CircleImageView v){
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
        if(v instanceof CircleImageView){

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
    public void signup(){
        String checkEmail = "^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";
        String checkPhoto="^((13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$";
        if(password.getText().toString().length()<6){
            passwordInput.setErrorEnabled(true);
            passwordInput.setError("密码不得少于六位");

        }else {
            passwordInput.setErrorEnabled(false);
            if(!password.getText().toString().equals(password_again.getText().toString())){
                passwordInput.setErrorEnabled(true);
                passwordInput.setError("密码输入不一致");
            }else {
                passwordInput.setErrorEnabled(false);
            }
        }
        if(!phone.getText().toString().matches(checkPhoto)){
            photoInput.setErrorEnabled(true);
            photoInput.setError("电话号码错误");
        }else {
            photoInput.setErrorEnabled(false);
        }if(!emial.getText().toString().matches(checkEmail)){
            emailInput.setErrorEnabled(true);
            emailInput.setError("邮箱格式错误");
        }else {
            emailInput.setErrorEnabled(false);
        }
        user=new User("signup",user_id.getText().toString(),user_name.getText().toString(),
                password.getText().toString(),
                emial.getText().toString(),phone.getText().toString(),
                grant,R.drawable.miao);
        Gson gson=new Gson();
        Log.d("gson",gson.toJson(user));
        JsonConnection.getJsonObject(gson.toJson(user));
        JsonConnection.submitJson(gson.toJson(user));
    }
    private void setup(){
        checkBox_man.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    checkBox_woman.setChecked(false);
                    grant=checkBox_man.getText().toString();
                }
            }
        });
        checkBox_woman.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    checkBox_man.setChecked(false);
                    grant = checkBox_woman.getText().toString();
                }
            }
        });
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK)
        {
            Uri chosenImageUri = data.getData();
            selectedImgUri=chosenImageUri;
            Glide.with(this).load(selectedImgUri).into(user_photo);
        }
    }

}
