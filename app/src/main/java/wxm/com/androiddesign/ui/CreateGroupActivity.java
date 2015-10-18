package wxm.com.androiddesign.ui;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.io.IOException;

import butterknife.Bind;
import butterknife.OnClick;
import wxm.com.androiddesign.R;
import wxm.com.androiddesign.module.Group;
import wxm.com.androiddesign.module.MyUser;
import wxm.com.androiddesign.network.JsonConnection;
import wxm.com.androiddesign.utils.MyBitmapFactory;
import wxm.com.androiddesign.utils.MyUtils;

public class CreateGroupActivity extends AppCompatActivity {
    public static final int CHOOSE_IMAGE=1;

    private Group group;
    @Bind(R.id.group_img)
    ImageView groupImage;
    @Bind(R.id.group_name)
    EditText groupName;
    @Bind(R.id.group_brief_intro)
    EditText groupIntro;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_group);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        new CreateGroupTask().execute();
    }

    @OnClick(R.id.group_img)
    public void chooseImage(){
        MyUtils.chooseImage(this,CHOOSE_IMAGE);
    }
    private void InitGroup(){
        String ctyIcon;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            Uri chosenImageUri = data.getData();
            Picasso.with(this).load(chosenImageUri).into(groupImage);
        }
    }

    private class CreateGroupTask extends AsyncTask<Void,Integer,Boolean>{
        Bitmap bitmap;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            bitmap=((BitmapDrawable)groupImage.getDrawable()).getBitmap();
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            String icon = MyBitmapFactory.BitmapToString(bitmap);
            group=new Group("createCommunity",MyUser.userId,icon,"","黑色骑士团","中二组织");
            JsonConnection.getJSON(new Gson().toJson(group));
            return null;
        }
    }
}