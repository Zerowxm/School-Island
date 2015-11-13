package wxm.com.androiddesign.ui;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.ImageView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.squareup.picasso.Picasso;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import wxm.com.androiddesign.R;
import wxm.com.androiddesign.adapter.MutiGroupAdapter;
import wxm.com.androiddesign.module.AtyItem;
import wxm.com.androiddesign.module.Group;
import wxm.com.androiddesign.module.MyUser;
import wxm.com.androiddesign.network.JsonConnection;;


public class GroupAcitivity extends AppCompatActivity {

    String groupId = "";
    String groupName = "";
    Group group;
    ArrayList<AtyItem> atyItems;
    MenuItem menuItem;
    List<MenuItem> menuItems = new ArrayList<>();
    Boolean flag = false;
    @Bind(R.id.cty_picture)
    ImageView ctyPicture;
    @Bind(R.id.recyclerview)
    RecyclerView recyclerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.group_detail);
        ButterKnife.bind(this);
        Bundle bundle = getIntent().getExtras();
        groupId = bundle.getString("groupId");
        groupName = bundle.getString("groupName");
        new GetUserInfo(this).execute();
        new GetGroupAty(this).execute();
    }

    private class GetUserInfo extends AsyncTask<Void, Void, Boolean> {
        Context context;

        public GetUserInfo(Context context) {
            this.context = context;
        }

        @Override
        protected void onPostExecute(Boolean reslut) {
            super.onPostExecute(reslut);
            setupToolBar();
            Picasso.with(context).load(group.getCtyIcon()).into(ctyPicture);
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            JSONObject object = new JSONObject();
            try {
                Log.d("Task2", "doInBackground"+"id"+groupId);
                object.put("action", "showCommunity");
                Log.d("groupIdd",groupId);
                object.put("ctyId", groupId);
                object.put("userId", MyUser.userId);
                Log.d("Task2", object.toString());
            } catch (JSONException e) {
                e.printStackTrace();
            }
            String json = JsonConnection.getJSON(object.toString());
            Log.d("Task2", json);
            group = new Gson().fromJson(json, Group.class);
            return true;
        }
    }

    private class GetGroupAty extends AsyncTask<Void, Void, Boolean> {
        Context context;

        public GetGroupAty(Context context) {
            this.context = context;
        }

        @Override
        protected void onPostExecute(Boolean reslut) {
            super.onPostExecute(reslut);
            setupRecyclerView(recyclerView);
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            JSONObject object = new JSONObject();
            try {
                object.put("action", "showGroupAty");
                object.put("ctyId", groupId);
                object.put("userId", MyUser.userId);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            String json = JsonConnection.getJSON(object.toString());
            Log.d("Task2", json);
            atyItems = new Gson().fromJson(json, new TypeToken<List<AtyItem>>() {
            }.getType());

            return true;
        }
        //new GetUserInfo(this).execute();


    }

    private void setupRecyclerView(RecyclerView recyclerView) {
        final LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(new MutiGroupAdapter(group,atyItems,menuItems,this));
        //recyclerView.setItemAnimator(new MyItemAnimator());
    }


    private void setupToolBar() {
        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        CollapsingToolbarLayout collapsingToolbar =
                (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        collapsingToolbar.setTitle(groupName);
        collapsingToolbar.setExpandedTitleColor(ContextCompat.getColor(this, android.R.color.transparent));
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_group, menu);
        menuItems.add(menu.findItem(R.id.publish));
        menuItems.add(menu.findItem(R.id.group_chat));
        menuItems.add(menu.findItem(R.id.group_exit));
        if(!MyUser.userId.equals(group.getUserId())){
            menuItem = menu.findItem(R.id.publish).setVisible(false);
            menuItem = menu.findItem(R.id.group_exit).setVisible(true);
        }else{
            menuItem = menu.findItem(R.id.publish).setVisible(true);
            menuItem = menu.findItem(R.id.group_exit).setVisible(false);
        }
        if ("false".equals(group.getCtyIsAttention())){
            menuItem = menu.findItem(R.id.group_exit).setVisible(false);
            menuItem = menu.findItem(R.id.group_chat).setVisible(false);
        }
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
                break;
            case R.id.action_settings:
                break;
            case R.id.group_chat:
                Intent intentGroup = new Intent(this, ChatActivity.class);
                intentGroup.putExtra("chatType",ChatActivity.GROUP_CHAT);
                intentGroup.putExtra("userName",group.getCtyName());
                intentGroup.putExtra("userIcon",group.getCtyIcon());
                intentGroup.putExtra("easemobId",group.getCtyGroupId());
                startActivity(intentGroup);
                break;
            case R.id.publish:
                Intent intent = new Intent(this,PublishActivity.class);
                intent.putExtra("groupId",groupId);
                intent.putExtra("groupName",groupName);
                this.startActivity(intent);
                break;
            case R.id.group_exit:
                group.setCtyMembers(Integer.parseInt(group.getCtyMembers()) - 1 + "");
                group.setCtyIsAttention("false");
                item.setVisible(false);
                menuItems.get(1).setVisible(false);
                new joinCmtTask().execute("notJoinCty");
                break;
        }
        return super.onOptionsItemSelected(item);
    }
    private class joinCmtTask extends AsyncTask<String, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            setupRecyclerView(recyclerView);
        }

        @Override
        protected Void doInBackground(String... params) {
            JSONObject object = new JSONObject();
            try {
                object = new JSONObject();
                object.put("action", params[0]);
                object.put("userId", MyUser.userId);
                object.put("easemobId", MyUser.easemobId);
                object.put("ctyId", group.getCtyId());
            } catch (JSONException e) {
                e.printStackTrace();
            }
            String json = JsonConnection.getJSON(object.toString());
//
            Log.i("mjson", json);
            return null;
        }

    }
}
