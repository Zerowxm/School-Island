package wxm.com.maca.ui;


import android.content.Context;

import android.os.AsyncTask;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import wxm.com.maca.R;
import wxm.com.maca.adapter.MultipleItemAdapter;
import wxm.com.maca.module.AtyItem;
import wxm.com.maca.module.CommentData;
import wxm.com.maca.module.MyUser;
import wxm.com.maca.network.JsonConnection;

public class DetailActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    MultipleItemAdapter multipleItemAdapter;// = new MultipleItemAdapter(activityItemData,commentDatas);
    AtyItem atyItem;
    ArrayList<CommentData> commentDatas = new ArrayList<CommentData>();
    int position;
    String userId;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_detail);

        Bundle bundle = getIntent().getExtras();
        atyItem = (bundle.getParcelable("com.wxm.com.androiddesign.module.ActivityItemData"));
        Log.d("atyItem",atyItem.getAtyName());
        position = bundle.getInt("position");
        userId = bundle.getString("userId");
        CommentData temp = null;
        new getCommentTask(getApplicationContext()).execute(temp);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        final ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        //actionBar.setDisplayHomeAsUpEnabled(true);
        //actionBar.setHomeButtonEnabled(true);
        addComment();
    }

    @Override
    public View onCreateView(View parent, String name, Context context, AttributeSet attrs) {
        return super.onCreateView(parent, name, context, attrs);
    }

    private class getCommentTask extends AsyncTask<CommentData, Void, Boolean> {
        MaterialDialog materialDialog;
        Context context;

        public getCommentTask(Context context) {
            this.context = context;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Boolean result) {
            super.onPostExecute(result);
            multipleItemAdapter = new MultipleItemAdapter(atyItem, commentDatas, DetailActivity.this, position);

            recyclerView = (RecyclerView) findViewById(R.id.recyclerview_activity);
            setupRecyclerView(recyclerView);
            multipleItemAdapter.notifyDataSetChanged();
            if (result) {
                recyclerView.scrollToPosition(commentDatas.size());
            }

        }

        @Override
        protected Boolean doInBackground(CommentData... params) {
            if (params[0] == null) {
                JSONObject object = new JSONObject();
                try {
                    object.put("action", "showComments");
                    object.put("atyId", atyItem.getAtyId());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                String json = JsonConnection.getJSON(object.toString());
                commentDatas = new Gson().fromJson(json, new TypeToken<ArrayList<CommentData>>() {
                }.getType());
                return false;
            } else {
                try {
                    JSONObject object = new JSONObject(new Gson().toJson(params[0]));
                    object.put("userName", MyUser.userName);
                    object.put("atyName",atyItem.getAtyName());
                    object.put("easemobId",MyUser.getEasemobId());
                    String json = JsonConnection.getJSON(object.toString());
                    CommentData commentData = new Gson().fromJson(json, CommentData.class);
                    Log.d("comment", commentData.toString());
                    commentDatas.add(commentData);
                    return true;
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return false;

            }

        }
    }

    public void addComment() {
        ImageView cmt_comment = (ImageView) findViewById(R.id.cmt_comment);
        final EditText cmt_text = (EditText) findViewById(R.id.add_comment);
        cmt_comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cmt_text.getText() != null && !cmt_text.getText().toString().equals("")) {
                    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    Date nowDate = new Date(System.currentTimeMillis());
                    String time = formatter.format(nowDate);
                    CommentData mcommentData = new CommentData("comment", userId, atyItem.getAtyId(),"just moment", cmt_text.getText().toString());
                    new getCommentTask(getApplicationContext()).execute(mcommentData);
                    //commentDatas.add(mcommentData);
                    cmt_text.setText(null);
                } else {
                    Toast.makeText(DetailActivity.this, "Please enter comment", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    private void setupRecyclerView(RecyclerView recyclerView) {
        recyclerView.setLayoutManager(new LinearLayoutManager(recyclerView.getContext()));

        recyclerView.setHasFixedSize(true);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(new MultipleItemAdapter(atyItem, commentDatas, this, position));
        recyclerView.setAdapter(multipleItemAdapter);

    }


    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_detail, menu);
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
