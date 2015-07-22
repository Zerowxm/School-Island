package wxm.com.androiddesign.ui;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;

import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import wxm.com.androiddesign.R;
import wxm.com.androiddesign.adapter.TabPagerAdapter;
import wxm.com.androiddesign.module.CmtItem;
import wxm.com.androiddesign.module.MyUser;
import wxm.com.androiddesign.module.User;
import wxm.com.androiddesign.network.JsonConnection;
import wxm.com.androiddesign.ui.fragment.CmtAtyFragment;
import wxm.com.androiddesign.ui.fragment.UserListFragment;


public class CmtAcitivity extends AppCompatActivity {

    ViewPager viewPager;
    String cmtId="";
    CmtItem cmtItem;
    Boolean flag=false;


    @Bind(R.id.cmt_name)TextView cmt_name;
    @Bind(R.id.cmt_member)TextView cmt_member;
    @Bind(R.id.cmt_photo)CircleImageView cmt_photo;
    @Bind(R.id.join)Button joinbtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.community_detail);
        ButterKnife.bind(this);
        Bundle bundle = getIntent().getExtras();
        cmtId = bundle.getString("cmtName");
        setupToolBar();
        setupViewPager();
        setupTabLayout();
        //new GetUserINfo(this).execute();
    }

    private class GetUserINfo extends AsyncTask<Void,Void,Boolean>{
        Context context;

        public GetUserINfo(Context context) {
            this.context = context;
        }

        @Override
        protected void onPostExecute(Boolean reslut) {
            super.onPostExecute(reslut);
            if (reslut){
                cmt_name.setText(cmtItem.getCmtId());
                cmt_member.setText(cmtItem.getCmtMembers());
                Picasso.with(context).load(cmtItem.getCmtIcon()).into(cmt_photo);
            }
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            JSONObject object=new JSONObject();
            try {
                object.put("action","showCmt");
                object.put("cmtId",cmtId);
                object.put("usrID", MyUser.userId);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            cmtItem=new Gson().fromJson(JsonConnection.getJSON(object.toString()),CmtItem.class);
            return true;
        }
    }

    @OnClick(R.id.join)
    public void joinCmt(){
        if (!flag){
            joinbtn.setText("已订阅");
            joinbtn.setTextColor(getResources().getColor(R.color.gray));
            joinbtn.setBackground(getResources().getDrawable(R.drawable.material_join_button));
            flag=true;
        }else {
            joinbtn.setText("+订阅");
            joinbtn.setTextColor(getResources().getColor(R.color.primary));
            joinbtn.setBackground(getResources().getDrawable(R.drawable.signup_button));
            flag=false;
        }
        new joinCmtTask().execute(joinbtn.getText().toString());
    }

    private class joinCmtTask extends AsyncTask<String, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
        }

        @Override
        protected Void doInBackground(String... params) {
            JSONObject object = new JSONObject();
            try {
                object = new JSONObject();
                object.put("action",params[0]);
                object.put("userId",MyUser.userId);
                object.put("cmtId",cmtId);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            String json = JsonConnection.getJSON(object.toString());
            Log.i("mjson", json);
            return null;
        }
    }

    private void setupToolBar() {
        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        CollapsingToolbarLayout collapsingToolbar =
                (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        collapsingToolbar.setTitle("");
    }

    private void setupViewPager() {
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        TabPagerAdapter adapter = new TabPagerAdapter(getSupportFragmentManager());

        adapter.addFragment(CmtAtyFragment.newInstance(cmtId),"活动");

        adapter.addFragment(UserListFragment.newInstance(cmtId),"用户");

        viewPager.setAdapter(adapter);
    }

    private void setupTabLayout() {
        TabLayout tab = (TabLayout) findViewById(R.id.tabs);
        //tab.setTabGravity(TabLayout.MODE_FIXED|TabLayout.MODE_SCROLLABLE);
        tab.setupWithViewPager(viewPager);
        tab.setBackgroundColor(getResources().getColor(R.color.tab_color));
        //tab.setTabTextColors(R.color.color_state_list);

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
