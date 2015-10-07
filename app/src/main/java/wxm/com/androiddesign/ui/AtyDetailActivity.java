package wxm.com.androiddesign.ui;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import wxm.com.androiddesign.R;
import wxm.com.androiddesign.adapter.CommentAdapter;
import wxm.com.androiddesign.adapter.ListViewAdapter;
import wxm.com.androiddesign.adapter.MultipleItemAdapter;
import wxm.com.androiddesign.listener.RecyclerItemClickListener;
import wxm.com.androiddesign.module.AtyItem;
import wxm.com.androiddesign.module.CommentData;
import wxm.com.androiddesign.module.MyUser;
import wxm.com.androiddesign.network.JsonConnection;
import wxm.com.androiddesign.utils.ScrollManager;
import wxm.com.androiddesign.utils.SpacesItemDecoration;

public class AtyDetailActivity extends AppCompatActivity {

    private static final String TAG="AtyDetail";
    AtyItem atyItem;
    ArrayList<CommentData> commentDatas = new ArrayList<CommentData>();
    CommentAdapter commentAdapter;
    @Bind(R.id.sliding_layout)
    SlidingUpPanelLayout mLayout;
    //@Bind(R.id.list)
    RecyclerView recyclerView;
    @Bind(R.id.list)
    ListView lv;
    @Bind(R.id.fab)
    FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test);
        ButterKnife.bind(this);
        mLayout.setPanelState(SlidingUpPanelLayout.PanelState.HIDDEN);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        final ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle("");
        setupSlidingPanel();
        addComment();
        Bundle bundle = getIntent().getExtras();
        atyItem = (bundle.getParcelable("com.wxm.com.androiddesign.module.ActivityItemData"));
        CommentData commentData=null;
        new getCommentTask().execute(commentData);



    }

    @OnClick(R.id.fab)
    public void sendNotify(){
        new MaterialDialog.Builder(this)
                .title("发送通知")
                .inputMaxLength(3,R.color.mdtp_red)
                .input(null, null, new MaterialDialog.InputCallback() {
                    @Override
                    public void onInput(MaterialDialog dialog, CharSequence input) {
                        Toast.makeText(AtyDetailActivity.this,input,Toast.LENGTH_SHORT);
                    }
                }).show();
    }

    private void setupSlidingPanel(){
        mLayout.setPanelSlideListener(new SlidingUpPanelLayout.PanelSlideListener() {
            @Override
            public void onPanelSlide(View panel, float slideOffset) {
                Log.i(TAG, "onPanelSlide, offset " + slideOffset);
                if (slideOffset == 0) {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            mLayout.setPanelState(SlidingUpPanelLayout.PanelState.HIDDEN);
                        }
                    }, 50);
                }
            }

            @Override
            public void onPanelExpanded(View panel) {
                Log.i(TAG, "onPanelExpanded");
            }

            @Override
            public void onPanelCollapsed(View panel) {
                Log.i(TAG, "onPanelCollapsed");
            }

            @Override
            public void onPanelAnchored(View panel) {
                Log.i(TAG, "onPanelAnchored");
            }

            @Override
            public void onPanelHidden(View panel) {
                Log.i(TAG, "onPanelHidden");
            }
        });
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
                    CommentData mcommentData = new CommentData("comment", MyUser.userId, atyItem.getAtyId(), time, cmt_text.getText().toString());
                    new getCommentTask().execute(mcommentData);
                    //commentDatas.add(mcommentData);
                    cmt_text.setText(null);
                } else {
                    //Toast.makeText(DetailActivity.this, "Please enter comment", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }


    private class NotifyMSG extends AsyncTask<String,Void,Boolean>{

        @Override
        protected Boolean doInBackground(String... params) {
            JSONObject object = new JSONObject();
            try {
                object.put("action", "notifyFromAty");
                object.put("atyId", atyItem.getAtyId());
                object.put("userId", MyUser.userId);
                object.put("easemobId",MyUser.getEasemobId());
                object.put("msgContent",params[0]);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            String json = JsonConnection.getJSON(object.toString());
            return null;
        }
    }

    private class getCommentTask extends AsyncTask<CommentData, Void, Boolean> {
        public getCommentTask() {

        }
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Boolean result) {
            super.onPostExecute(result);
            commentAdapter=new CommentAdapter(commentDatas,AtyDetailActivity.this);
            ListViewAdapter adapter=new ListViewAdapter(getApplicationContext(),commentDatas);
            //setupRecyclerView();
            //commentAdapter.notifyDataSetChanged();
            lv.setAdapter(adapter);
            adapter.notifyDataSetChanged();
        }

        @Override
        protected Boolean doInBackground(CommentData... params) {
            if (params[0]==null) {
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
                String json = JsonConnection.getJSON(new Gson().toJson(params[0]));
                CommentData commentData = new Gson().fromJson(json, CommentData.class);
//                Log.d("comment", commentData.toString());
                commentDatas.add(commentData);
                return true;
            }
        }
    }

    private void setupRecyclerView() {
        Log.d("setupRecyclerView","setupRecyclerView");
        final LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.addItemDecoration(new SpacesItemDecoration(getResources()));
        recyclerView.setAdapter(commentAdapter);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_detail, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        mLayout.setAnchorPoint(0.7f);
        mLayout.setPanelState(SlidingUpPanelLayout.PanelState.ANCHORED);
        return super.onOptionsItemSelected(item);
    }
}
