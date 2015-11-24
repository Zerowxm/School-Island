package wxm.com.androiddesign.ui;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.afollestad.materialdialogs.MaterialDialog;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import wxm.com.androiddesign.R;
import wxm.com.androiddesign.adapter.UserAdapter;
import wxm.com.androiddesign.anim.MyItemAnimator;
import wxm.com.androiddesign.listener.RecyclerItemClickListener;
import wxm.com.androiddesign.module.User;
import wxm.com.androiddesign.network.JsonConnection;
import wxm.com.androiddesign.utils.ActivityStartHelper;
import wxm.com.androiddesign.utils.SpacesItemDecoration;

public class UserListActivity extends AppCompatActivity {
    RecyclerView recyclerView;

    List<User> mUserList = new ArrayList<User>();

    String id = "";
    Boolean isAty ;


    private void setupRecyclerView(RecyclerView recyclerView) {
        recyclerView.setLayoutManager(new LinearLayoutManager(recyclerView.getContext()));
        recyclerView.setItemAnimator(new MyItemAnimator());
        recyclerView.addItemDecoration(new SpacesItemDecoration(this));
        new GetAtyMembers(this).execute();
        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(recyclerView.getContext(), new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                ActivityStartHelper.startProfileActivity(view.getContext(), mUserList.get(position).getUserId(),0);
            }
        }));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getIntent().getExtras();
        isAty=bundle.getBoolean("isAty");
        if (isAty){
            id = bundle.getString("atyId");
        }else
            id = bundle.getString("ctyId");
        setContentView(R.layout.list_with_toolbar);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerview_activity);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        final ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);
        if (isAty)
            actionBar.setTitle("参加人员");
        else
            actionBar.setTitle("部落成员");
        setupRecyclerView(recyclerView);


    }

    public static void start(Context c,String id,boolean isAty) {
        Intent intent=new Intent(c, UserListActivity.class)
                .putExtra("isAty", isAty);
        if (isAty){
            intent.putExtra("atyId", id);
        }else
        intent.putExtra("ctyId", id);
        c.startActivity(intent);
    }

    private class GetAtyMembers extends AsyncTask<Void, Void, Boolean> {
        MaterialDialog materialDialog;
        Context context;

        public GetAtyMembers(Context context) {
            this.context = context;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            materialDialog = new MaterialDialog.Builder(context)
                    .title("Loading")
                    .progress(true, 0)
                    .progressIndeterminateStyle(false)
                    .show();
        }

        @Override
        protected void onPostExecute(Boolean result) {
            super.onPostExecute(result);
            materialDialog.dismiss();
            recyclerView.setAdapter(new UserAdapter(mUserList, context));
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            JSONObject object = new JSONObject();
            try {
                if (isAty){
                    object.put("action", "showAtyMembers");
                    object.put("atyId", id);
                }else {
                    object.put("action", "showCtyMembers");
                    object.put("ctyId", id);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            String json = JsonConnection.getJSON(object.toString());
            mUserList = new Gson().fromJson(json, new TypeToken<List<User>>() {
            }.getType());
            return null;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_user_list, menu);
        return true;
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
        if (id==android.R.id.home){
            finish();
        }

        return super.onOptionsItemSelected(item);
    }
}
