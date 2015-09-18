package wxm.com.androiddesign.ui;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
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
import wxm.com.androiddesign.ui.fragment.CmtListFragment;
import wxm.com.androiddesign.ui.fragment.FooFragment;
import wxm.com.androiddesign.ui.fragment.PhotoFragment;
import wxm.com.androiddesign.ui.fragment.ProfileFragment;
import wxm.com.androiddesign.ui.fragment.UserActivityFragment;


public class UserAcitivity extends AppCompatActivity {

    ViewPager viewPager;
    String userId = "";
    User user;

    @Bind(R.id.user_id)
    TextView user_id;
    @Bind(R.id.score)
    TextView score;
    @Bind(R.id.user_photo)
    CircleImageView user_photo;
    @Bind(R.id.collapsing_toolbar)
    CollapsingToolbarLayout collapsingToolbarLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_acitivity);
        ButterKnife.bind(this);
        Bundle bundle = getIntent().getExtras();
        userId = bundle.getString("userId");
        new GetUserInfo(this).execute();

    }

    private class GetUserInfo extends AsyncTask<Void, Void, Boolean> {
        Context context;

        public GetUserInfo(Context context) {
            this.context = context;
        }

        @Override
        protected void onPostExecute(Boolean reslut) {
            super.onPostExecute(reslut);
            if (reslut) {
                setupToolBar();
                setupViewPager();
                setupTabLayout();
                user_id.setText(user.getUserName());
                score.setText("积分：" + user.getUserCredit());
                user_photo.setVisibility(View.VISIBLE);
                Picasso.with(context).load(user.getUserIcon()).into(user_photo, new Callback() {
                    @Override
                    public void onSuccess() {
                        Bitmap bitmap = ((BitmapDrawable) user_photo.getDrawable()).getBitmap();
                        Palette.from(bitmap).generate(new Palette.PaletteAsyncListener() {
                            public void onGenerated(Palette palette) {
                                user_photo.animate().alpha(1f).setDuration(1000).start();
                                score.animate().alpha(1f).setDuration(1000).start();
                                user_id.animate().alpha(1f).setDuration(1000).start();
                                applyPalette(palette);
                            }
                        });
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
            user.setUserId(userId);
            return true;
        }
    }

    private void applyPalette(Palette palette) {
        int primaryDark = ContextCompat.getColor(this,R.color.primary_dark);
        int primary =ContextCompat.getColor(this, R.color.primary);
        collapsingToolbarLayout.setContentScrimColor(palette.getMutedColor(primary));
        collapsingToolbarLayout.setStatusBarScrimColor(palette.getDarkMutedColor(primaryDark));
        //updateBackground((FloatingActionButton) findViewById(R.id.fab), palette);
        supportStartPostponedEnterTransition();
    }

    private void setupToolBar() {
        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        CollapsingToolbarLayout collapsingToolbar =
                (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        collapsingToolbar.setTitle(user.getUserName());
        collapsingToolbar.setExpandedTitleColor(ContextCompat.getColor(this,android.R.color.transparent));
    }

    private void setupViewPager() {
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        TabPagerAdapter adapter = new TabPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(ProfileFragment.newInstance(userId), "个人信息");
        adapter.addFragment(UserActivityFragment.newInstance(UserActivityFragment.Release, userId), "已发布活动");
        adapter.addFragment(UserActivityFragment.newInstance(UserActivityFragment.Joined, userId), "参与活动");
        adapter.addFragment(CmtListFragment.newInstance(userId), "社区");
        if ("true".equals(user.getUserAlbumIsPublic()) || MyUser.userId.equals(user.getUserId())) {
            Log.i("publicuser", user.getUserId());
            Log.i("publicuser", MyUser.userId);
            adapter.addFragment(PhotoFragment.newInstance(userId), "相册");
        } else {
            adapter.addFragment(new FooFragment(), "相册");
        }
        viewPager.setAdapter(adapter);
    }

    private void setupTabLayout() {
        TabLayout tab = (TabLayout) findViewById(R.id.tabs);
        tab.setupWithViewPager(viewPager);
        tab.setBackgroundColor(ContextCompat.getColor(this,R.color.tab_color));

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_user_acitivity, menu);
        if (userId.equals(MyUser.userId)){
            MenuItem menuItem=menu.findItem(R.id.action_send);
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
                Intent chatIntent=new Intent(this,ChatActivity.class);
                chatIntent.putExtra("notification",false);
                chatIntent.putExtra("toChatUserId",user.getEasemobId());
                chatIntent.putExtra("userIcon",user.getUserIcon());
                startActivity(chatIntent);
                break;
            case R.id.action_settings:
                Snackbar.make(user_photo,"举报",Snackbar.LENGTH_SHORT).show();
                break;

        }
        return super.onOptionsItemSelected(item);
    }
}
