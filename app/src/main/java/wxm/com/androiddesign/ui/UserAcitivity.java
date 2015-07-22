package wxm.com.androiddesign.ui;

import android.content.Context;
import android.os.AsyncTask;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.Bind;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import wxm.com.androiddesign.R;
import wxm.com.androiddesign.adapter.TabPagerAdapter;
import wxm.com.androiddesign.module.MyUser;
import wxm.com.androiddesign.module.User;
import wxm.com.androiddesign.network.JsonConnection;
import wxm.com.androiddesign.ui.fragment.ActivityFragment;
import wxm.com.androiddesign.ui.fragment.CmtListFragment;
import wxm.com.androiddesign.ui.fragment.FooFragment;
import wxm.com.androiddesign.ui.fragment.PhotoFragment;
import wxm.com.androiddesign.ui.fragment.ProfileFragment;
import wxm.com.androiddesign.ui.fragment.MsgListFragment;
import wxm.com.androiddesign.ui.fragment.ScoreFragment;
import wxm.com.androiddesign.ui.fragment.UserActivityFragment;


public class UserAcitivity extends AppCompatActivity {

    ViewPager viewPager;
    String userId="";
    User user;

    @Bind(R.id.user_id)TextView user_id;
    @Bind(R.id.score)TextView score;
    @Bind(R.id.user_photo)CircleImageView user_photo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_acitivity);
        ButterKnife.bind(this);
        Bundle bundle = getIntent().getExtras();
        userId = bundle.getString("userId");
        new GetUserINfo(this).execute();


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
                setupToolBar();
                setupViewPager();
                setupTabLayout();
                user_id.setText(user.getUserId());
                score.setText(user.getUserScore());
                Picasso.with(context).load(user.getUserIcon()).into(user_photo);
            }
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            JSONObject object=new JSONObject();
            try {
                object.put("action","showProfile");
                object.put("userId",userId);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            user=new Gson().fromJson(JsonConnection.getJSON(object.toString()),User.class);
            user.setUserId(userId);
            return true;
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
        adapter.addFragment(ProfileFragment.newInstance(userId), "个人信息");

        adapter.addFragment(UserActivityFragment.newInstance(UserActivityFragment.Release,userId), "已发布活动");
        adapter.addFragment(UserActivityFragment.newInstance(UserActivityFragment.Joined,userId), "参与活动");
       // adapter.addFragment(CmtListFragment.newInstance(userId), "社区");
        //adapter.addFragment(ScoreFragment.newInstance(userId), "积分");
        if(user.getIsPublic().equals("true") || MyUser.userId.equals(user.getUserId())) {
            adapter.addFragment(PhotoFragment.newInstance(userId), "相册");
        }
        else{
           adapter.addFragment(new FooFragment(), "相册");
        }
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
