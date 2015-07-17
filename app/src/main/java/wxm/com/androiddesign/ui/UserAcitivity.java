package wxm.com.androiddesign.ui;

import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import wxm.com.androiddesign.R;
import wxm.com.androiddesign.adapter.TabPagerAdapter;
import wxm.com.androiddesign.ui.fragment.ActivityFragment;
import wxm.com.androiddesign.ui.fragment.CmtListFragment;
import wxm.com.androiddesign.ui.fragment.HomeFragment;
import wxm.com.androiddesign.ui.fragment.PhotoFragment;
import wxm.com.androiddesign.ui.fragment.ProfileFragment;
import wxm.com.androiddesign.ui.fragment.UserListFragment;


public class UserAcitivity extends AppCompatActivity {

    ViewPager viewPager;
    String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_acitivity);
        Bundle bundle = getIntent().getExtras();
        userId = bundle.getString("userId");
        setupToolBar();
        setupViewPager();
        setupTabLayout();
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
        adapter.addFragment(ActivityFragment.newInstance(ActivityFragment.Release,userId), "已发布活动");
        adapter.addFragment(ActivityFragment.newInstance(ActivityFragment.Joined,userId), "参与活动");
        adapter.addFragment(new CmtListFragment(), "社区");
        adapter.addFragment(new UserListFragment(), "关注用户");
        adapter.addFragment(PhotoFragment.newInstance(userId),"相册");

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
