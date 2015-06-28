package wxm.com.androiddesign.ui;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import de.hdodenhof.circleimageview.CircleImageView;
import wxm.com.androiddesign.ui.fragment.FragmentParent;
import wxm.com.androiddesign.ui.fragment.HomeFragment;
import wxm.com.androiddesign.R;
import wxm.com.androiddesign.UiTestFg1;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    DrawerLayout drawerLayout;
    FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().add(R.id.content, new FragmentParent()).commit();

        }

        setupFab();
        setupNavigationView();
    }

    private void setupFab(){
        fab=(FloatingActionButton)findViewById(R.id.fb);
        fab.setOnClickListener(this);
    }


    private void setupDrawerContent(final NavigationView navigationView) {

        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        for(int index=0;index<navigationView.getMenu().size();index++){
                            navigationView.getMenu().getItem(index).setChecked(false);
                        }
                        menuItem.setChecked(true);
                        drawerLayout.closeDrawers();
                        switch (menuItem.getItemId()) {
                            case R.id.nav_home:
                                getSupportFragmentManager().beginTransaction().replace(R.id.content, new HomeFragment()).commit();

                                //mCurrentSelectedPosition = 0;
                                return true;
                            case R.id.nav_explore:
                                getSupportFragmentManager().beginTransaction().replace(R.id.content, new FragmentParent()).commit();
//                                Intent intent1=new Intent(MainActivity.this,FragmentParent.class);
//                                startActivity(intent1);

                                //mCurrentSelectedPosition = 1;
                                return true;
                            case R.id.nav_attention:
//
                                Snackbar.make( drawerLayout, "关注",
                                        Snackbar.LENGTH_SHORT).show();
                                return true;
                            case R.id.nav_user_setting:
                                Snackbar.make( drawerLayout, "个人设置",
                                        Snackbar.LENGTH_SHORT).show();
                                return true;
                            case R.id.nav_setting:
                                //getSupportFragmentManager().beginTransaction().replace(R.id.content, new HomeFragment()).commit();
                                Snackbar.make(drawerLayout, "设置",
                                        Snackbar.LENGTH_SHORT).show();
                                return true;
                            case R.id.user_photo:
                                Snackbar.make(drawerLayout, "个人信息",
                                        Snackbar.LENGTH_SHORT).show();
                                return true;
                            default:
                                return true;
                        }
                    }
                });
    }
    private void setupNavigationView(){

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        NavigationView navigationView=(NavigationView)findViewById(R.id.navigation_view);
        if(navigationView!=null){
            setupDrawerContent(navigationView);
        }

        CircleImageView userPhoto=(CircleImageView)findViewById(R.id.user_photo);


        userPhoto.setClickable(true);
        userPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this,UserAcitivity.class);
                startActivity(intent);
            }
        });
        userPhoto.setClickable(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                drawerLayout.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

int i=1;
    @Override
    public void onClick(View v) {

            Intent intent=new Intent(MainActivity.this,LoginActivity.class);
            startActivity(intent);

        }


}
