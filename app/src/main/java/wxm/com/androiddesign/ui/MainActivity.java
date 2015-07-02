package wxm.com.androiddesign.ui;

import android.content.Intent;
import android.os.Build;
import android.support.design.widget.FloatingActionButton;
//import com.melnykov.fab.FloatingActionButton;

import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import de.hdodenhof.circleimageview.CircleImageView;
import wxm.com.androiddesign.ui.fragment.DatePickerFragment;
import wxm.com.androiddesign.ui.fragment.FragmentParent;
import wxm.com.androiddesign.ui.fragment.HomeFragment;
import wxm.com.androiddesign.R;
import wxm.com.androiddesign.ui.fragment.LoginFragment;
import wxm.com.androiddesign.ui.fragment.ReleaseFragment;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    DrawerLayout drawerLayout;
    //FloatingActionButton fab;
    //ImageButton fab;
    //com.melnykov.fab.FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().add(R.id.content, new FragmentParent()).commit();

        }

        //setupFab();
        setupNavigationView();
    }

//    public ImageButton getFAB(){
//        return fab;
//    }

    private void setupFab(){
      //  fab=(ImageButton)findViewById(R.id.fab);
        //fab=(com.melnykov.fab.FloatingActionButton)findViewById(R.id.fab);

//        fab.post(new Runnable() {
//            @Override
//            public void run() {
//                ScrollManager manager=new ScrollManager();
//                View content=getLayoutInflater().inflate(R.layout.activity_fragment,null);
//                RecyclerView recyclerView=(RecyclerView)content.findViewById(R.id.recyclerview_activity);
//                manager.attach(recyclerView);
//                manager.addView(fab, ScrollManager.Direction.DOWN);
//                manager.setInitialOffset(46);
//            }
//        });


      //  fab.setOnClickListener(this);
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
                                //hideView();
                                return true;
                            case R.id.nav_setting:
                                Intent intent=new Intent(MainActivity.this,CmtDatailActivity.class);
                                startActivity(intent);
                                //getSupportFragmentManager().beginTransaction().replace(R.id.content, new HomeFragment()).commit();
                                Snackbar.make(drawerLayout, "设置",
                                        Snackbar.LENGTH_SHORT).show();
                                return true;
                            case R.id.user_photo:
                                //getSupportFragmentManager().beginTransaction().replace(R.id.content, new ReleaseFragment()).commit();

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
        RelativeLayout header=(RelativeLayout)findViewById(R.id.header);
        header.setClickable(true);
        //header.setElevation(0.1);

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

            //Intent intent=new Intent(MainActivity.this,SignUpActivity.class);
        if(Build.VERSION.SDK_INT>= 21){
            //startActivity(intent);
        }
        Intent intent =new Intent(MainActivity.this,ReleaseActivity.class);
        startActivity(intent);
//        DatePickerFragment datePicker=new DatePickerFragment();
//        datePicker.show(getSupportFragmentManager(),"datapicker");
        }

    private void showDialog(){
        //getSupportFragmentManager().beginTransaction().replace(R.id.content, new ReleaseFragment()).commit();
//        FragmentManager fm=getSupportFragmentManager();
//        ReleaseFragment releaseFragment=new ReleaseFragment();
//        releaseFragment.show(fm, "release");
    }


//    private void hideView(){
//        // previously visible view
//        final FloatingActionButton myView = (FloatingActionButton)findViewById(R.id.fab);
//
//// get the center for the clipping circle
//        int cx = (myView.getLeft() + myView.getRight()) / 2;
//        int cy = (myView.getTop() + myView.getBottom()) / 2;
//
//// get the initial radius for the clipping circle
//        int initialRadius = myView.getWidth();
//        if(Build.VERSION.SDK_INT>= 21) {
//// create the animation (the final radius is zero)
//            Animator anim =
//                    ViewAnimationUtils.createCircularReveal(myView, cx, cy, initialRadius, 0);
//
//// make the view invisible when the animation is done
//            anim.addListener(new AnimatorListenerAdapter() {
//                @Override
//                public void onAnimationEnd(Animator animation) {
//                    super.onAnimationEnd(animation);
//                    myView.setVisibility(View.INVISIBLE);
//                }
//
//            });
//
//// start the animation
//            anim.start();
//        }
//    }
//private void showView(){
//    // previously invisible view
//    FloatingActionButton myView = (FloatingActionButton)findViewById(R.id.fab);
//
//// get the center for the clipping circle
//    int cx = (myView.getLeft() + myView.getRight()) / 2;
//    int cy = (myView.getTop() + myView.getBottom()) / 2;
//
//// get the final radius for the clipping circle
//    int finalRadius = Math.max(myView.getWidth(), myView.getHeight());
//
//    if(Build.VERSION.SDK_INT>= 21){
//        Animator anim =
//                ViewAnimationUtils.createCircularReveal(myView, cx, cy, 0, finalRadius);
//        myView.setVisibility(View.VISIBLE);
//        anim.start();
//    }
//// create the animator for this view (the start radius is zero)
//
//
//// make the view visible and start the animation
//
//}

}
