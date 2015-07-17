package wxm.com.androiddesign.ui;


import android.content.Context;

import android.os.AsyncTask;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import wxm.com.androiddesign.R;
import wxm.com.androiddesign.adapter.MultipleItemAdapter;
import wxm.com.androiddesign.module.AtyItem;
import wxm.com.androiddesign.module.CommentData;
import wxm.com.androiddesign.network.JsonConnection;
import wxm.com.androiddesign.ui.fragment.ActivityFragment;
import wxm.com.androiddesign.ui.fragment.HomeFragment;

public class DetailActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    MultipleItemAdapter multipleItemAdapter;// = new MultipleItemAdapter(activityItemData,commentDatas);
    AtyItem atyItem;
    ArrayList<CommentData> commentDatas = new ArrayList<CommentData>();
    int position;
    String fragment;
    getCommentTask mGetCommentTask;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_detail);

        Bundle bundle = getIntent().getExtras();
        atyItem = (bundle.getParcelable("com.wxm.com.androiddesign.module.ActivityItemData"));
        position = bundle.getInt("position");
        fragment = bundle.getString("fragment");
        mGetCommentTask=new getCommentTask(getApplicationContext());
        //mGetCommentTask.execute();
        multipleItemAdapter = new MultipleItemAdapter(atyItem, commentDatas,atyItem.getUserId(), this, position);

        recyclerView = (RecyclerView) findViewById(R.id.recyclerview_activity);
        setupRecyclerView(recyclerView);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        final ActionBar actionBar = getSupportActionBar();

        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);
        addComment();
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
            materialDialog = new MaterialDialog.Builder(context)
                    .title("Loading")
                    .progress(true, 0)
                    .progressIndeterminateStyle(true)
                    .show();
        }

        @Override
        protected void onPostExecute(Boolean result) {
            super.onPostExecute(result);
        }

        @Override
        protected Boolean doInBackground(CommentData... params) {
            if (params[0] == null) {
                JSONObject object = new JSONObject();
                try {
                    object.put("action", "showcomments");
                    object.put("atyId", atyItem.getAtyId());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                String json = JsonConnection.getJSON(object.toString());
                commentDatas = new Gson().fromJson(json, new TypeToken<ArrayList<AtyItem>>() {
                }.getType());
            }
            commentDatas.add(params[0]);
            multipleItemAdapter.notifyDataSetChanged();
            return null;
        }
    }

    public void addComment() {
        ImageView cmt_comment = (ImageView) findViewById(R.id.cmt_comment);
        final EditText cmt_text = (EditText) findViewById(R.id.add_comment);
        cmt_comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cmt_text.getText() != null && !cmt_text.getText().toString().equals("")) {
                    SimpleDateFormat oldFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    try {
                        Date oldDate = oldFormatter.parse("2015-07-14 16:56:00");
                        Date nowDate = new Date(System.currentTimeMillis());
                        long time = nowDate.getTime() - oldDate.getTime();
                        String str = getSubTime(time);
                        mGetCommentTask.execute(new CommentData("comment", "userId", str, cmt_text.getText().toString()));
                        cmt_text.setText(null);
                        //!json

                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                } else {
                    Toast.makeText(DetailActivity.this, "Please enter comment", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    private class UpDateTask extends AsyncTask<CommentData, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
        }

        @Override
        protected Void doInBackground(CommentData... params) {
            JsonConnection.getJSON(new Gson().toJson(params[0]));
            return null;
        }
    }

    private String getSubTime(long subTime) {
        long days = subTime / (1000 * 60 * 60 * 24);
        if (days < 1) {
            long hours = subTime / (1000 * 60 * 60);
            if (hours < 1) {
                long minutes = subTime / (1000 * 60);
                if (minutes < 1)
                    return "Moments ago";
                return (int) (minutes) == 1 ? String.format("%s minute", minutes) : String.format("%s minutes", minutes);
            }
            return (int) (hours) == 1 ? String.format("%s hour", hours) : String.format("%s hours", hours);
        }
        if (days >= 7) {
            return (int) (days / 7) == 1 ? String.format("%s week", (int) (days / 7)) : String.format("%s weeks", (int) (days / 7));
        } else
            return (int) (days) == 1 ? String.format("%s day", days) : String.format("%s days", days);
    }

    private void setupRecyclerView(RecyclerView recyclerView) {
        recyclerView.setLayoutManager(new LinearLayoutManager(recyclerView.getContext()));

        recyclerView.setHasFixedSize(true);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(new MultipleItemAdapter(atyItem, commentDatas,atyItem.getUserId(), this, position));
        recyclerView.setAdapter(multipleItemAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_detail, menu);
        return true;
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (fragment.equals("HomeFragment"))
            HomeFragment.refresh(atyItem, position);
        else if (fragment.equals("ActivityFragment"))
            ActivityFragment.refresh(atyItem, position);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
