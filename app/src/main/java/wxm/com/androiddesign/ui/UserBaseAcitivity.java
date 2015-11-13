package wxm.com.androiddesign.ui;

import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.MotionEventCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.squareup.picasso.Callback;
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
import wxm.com.androiddesign.ui.fragment.GroupListFragment;
import wxm.com.androiddesign.ui.fragment.PhotoFragment;
import wxm.com.androiddesign.ui.fragment.ProfileFragment;
import wxm.com.androiddesign.ui.fragment.UserActivityFragment;
import wxm.com.androiddesign.utils.Config;
import wxm.com.androiddesign.utils.MyUtils;
import wxm.com.androiddesign.widget.AvatarImageBehavior;


public class UserBaseAcitivity extends AppCompatActivity implements AppBarLayout.OnOffsetChangedListener {

    protected String userId;
    protected User user;
    protected TabLayout tabs;
    protected TextView mUserId;
    protected ImageView mProfileImage;
    protected TextView mUserSignature;
    protected ImageView mBackDrop;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getIntent().getExtras();
        userId = bundle.getString("userId");
    }

    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int offset) {
    }


    public static void startAlphaAnimation (View v, long duration, int visibility) {
        AlphaAnimation alphaAnimation = (visibility == View.VISIBLE)
                ? new AlphaAnimation(0f, 1f)
                : new AlphaAnimation(1f, 0f);
        alphaAnimation.setDuration(duration);
        alphaAnimation.setFillAfter(true);
        v.startAnimation(alphaAnimation);
    }

    protected class GetUserInfo extends AsyncTask<Void, Void, Boolean> {
        Context context;

        public GetUserInfo(Context context) {
            this.context = context;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Boolean reslut) {
            super.onPostExecute(reslut);
            if (reslut) {
                //setupToolBar();
                setupViewPager();

                mUserId.setText(user.getUserName());
                mUserSignature.setText(user.getUserSignature());
                Picasso.with(context).load(user.getUserIcon()).into(mProfileImage, new Callback() {
                    @Override
                    public void onSuccess() {
                        mProfileImage.animate().alpha(1f).setDuration(450).start();
                        mUserSignature.animate().alpha(1f).setDuration(475).start();
                        mUserId.animate().alpha(1f).setDuration(500).start();
                    }

                    @Override
                    public void onError() {

                    }
                });
            }
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            JSONObject object = new JSONObject();
            try {
                object.put("action", "showProfile");
                object.put("userId", userId);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            user = new Gson().fromJson(JsonConnection.getJSON(object.toString()), User.class);
            if (userId != null) {
                user.setUserId(userId);
            }

            return true;
        }
    }


    private void setupToolBar() {
        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void setupViewPager() {
        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
        TabPagerAdapter adapter = new TabPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(ProfileFragment.newInstance(userId), "个人信息");
        adapter.addFragment(UserActivityFragment.newInstance(UserActivityFragment.Release, userId), "已发布活动");
        adapter.addFragment(UserActivityFragment.newInstance(UserActivityFragment.Joined, userId), "参与活动");
        adapter.addFragment(GroupListFragment.newInstance(userId,GroupListFragment.OWNED,true),"个人部落");
        adapter.addFragment(GroupListFragment.newInstance(userId,GroupListFragment.JOINED,true),"参与部落");
        adapter.addFragment(PhotoFragment.newInstance(userId), "相册");
        viewPager.setAdapter(adapter);
        tabs=(TabLayout)findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_user_acitivity, menu);
        if (MyUser.userId.equals(userId)) {
            MenuItem menuItem = menu.findItem(R.id.action_send);
            menuItem.setVisible(false);
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
            case R.id.action_send:
                //打开聊天
                Intent chatIntent = new Intent(this, ChatActivity.class);
                chatIntent.putExtra("easemobId", user.getEasemobId());
                chatIntent.putExtra("userIcon", user.getUserIcon());
                chatIntent.putExtra("userName",user.getUserName());
                chatIntent.putExtra("chatType",ChatActivity.CHAT);
                startActivity(chatIntent);
                break;
            case R.id.action_settings:
                //Snackbar.make(user_photo, "举报", Snackbar.LENGTH_SHORT).show();
                break;

        }
        return super.onOptionsItemSelected(item);
    }
}
