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

import com.google.gson.Gson;

import java.io.IOException;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import wxm.com.androiddesign.R;
import wxm.com.androiddesign.module.User;

public class SignUpActivity extends AppCompatActivity implements View.OnClickListener{

    @Bind(R.id.user_photo)CircleImageView user_photo;
    @Bind(R.id.username_edit_text)EditText user_name;
    @Bind(R.id.password_edit_text)EditText password;
    @Bind(R.id.emial_edit_text)EditText emial;
    @Bind(R.id.phone_edit_text)EditText phone;
    @Bind(R.id.checkbox_man)CheckBox checkBox_man;
    @Bind(R.id.checkbox_woman)CheckBox checkBox_woman;
    private String selectedImgPath;
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
        TextInputLayout usernameLayout = (TextInputLayout) findViewById(R.id.username_text_input_layout);
        usernameLayout.setErrorEnabled(true);

        //usernameLayout.setError("请输入用户名");

        TextInputLayout passwordLayout = (TextInputLayout) findViewById(R.id.password_text_input_layout);
        passwordLayout.setErrorEnabled(true);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().setExitTransition(new Explode());
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        setupTextInputLayout();
        ButterKnife.bind(this);
        setup();
        setupToolbar();
    }



    @OnClick(R.id.user_photo)
    public void chooseImg(CircleImageView v){
        Intent photoPickerIntent = new Intent(Intent.ACTION_GET_CONTENT);
        photoPickerIntent.setType("image/*");
        photoPickerIntent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        startActivityForResult(photoPickerIntent, 1);
        user_photo.setImageURI(selectedImgUri);
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
    @OnClick(R.id.fab)
    public void signup(){
        user=new User(
                user_name.getText().toString(),
                password.getText().toString(),
                emial.getText().toString(),
                phone.getText().toString(),
                grant
        );
        //GetJsonFromServer.getJsonObject();
        String json=user.createJson().toString();
        String data=
                "{\"number\":\"1111\",\"photo\":2130837598,\"email\":\"3232\",\"password\":\"3232\",\"name\":\"3232\"}";
        User user2=new Gson().fromJson(data,User.class);
        String json2=new Gson().toJson(user2);
        Log.d("json",json2);

        //finish();
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
                if(isChecked){
                    checkBox_man.setChecked(false);
                    grant=checkBox_woman.getText().toString();
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
            selectedImgPath=getPath(chosenImageUri);
            Bitmap mBitmap = null;
            try {
                mBitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), chosenImageUri);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public String getPath(Uri uri) {
        // just some safety built in
        if( uri == null ) {
            // TODO perform some logging or show user feedback
            return null;
        }
        // try to retrieve the image from the media store first
        // this will only work for images selected from gallery
        String[] projection = { MediaStore.Images.Media.DATA };
        Cursor cursor = managedQuery(uri, projection, null, null, null);
        if( cursor != null ){
            int column_index = cursor
                    .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        }
        // this is our fallback here
        return uri.getPath();
    }
}
