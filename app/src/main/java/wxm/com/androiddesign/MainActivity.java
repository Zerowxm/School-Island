package wxm.com.androiddesign;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.design.widget.TextInputLayout;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    DrawerLayout drawerLayout;
    FloatingActionButton fab;
    //TabLayout tabLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if(savedInstanceState==null){
            getSupportFragmentManager().beginTransaction().add(R.id.content, new UiFgParentFg()).commit();

        }
       // setupToolbar();
        setupFab();
        //setupTablayout();
        setupNavigationView();
        //setupTextInputLayout();
    }

//    private void setupToolbar(){
//        Toolbar toolbar=(Toolbar)findViewById(R.id.toolbar);
//
//        setSupportActionBar(toolbar);
//        final ActionBar actionBar=getSupportActionBar();
//        actionBar.setHomeAsUpIndicator(R.drawable.ic_menu_white_24dp);
//        actionBar.setDisplayHomeAsUpEnabled(true);
//        actionBar.setHomeButtonEnabled(true);
//    }

    private void setupFab(){
        fab=(FloatingActionButton)findViewById(R.id.fb);
        fab.setOnClickListener(this);
    }

//    private void setupTablayout(){
//
//        tabLayout = (TabLayout) findViewById(R.id.tabLayout);
//        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
//        tabLayout.addTab(tabLayout.newTab().setText("Tab 1"));
//        tabLayout.addTab(tabLayout.newTab().setText("Tab 2"));
//        tabLayout.addTab(tabLayout.newTab().setText("Tab 3"));
//        tabLayout.addTab(tabLayout.newTab().setText("Tab 4"));
//    }

    private void setupDrawerContent( NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        //if(menuItem.getItemId()!=1){
                        //Snackbar.make(drawerLayout, "Hello", Snackbar.LENGTH_SHORT).show();

                       // }
                       // else {
                          //  getSupportFragmentManager().beginTransaction().replace(R.id.content, new UiTestFg1()).commit();
                      //  }
                       // menuItem.setChecked(true);
                        drawerLayout.closeDrawers();
                        switch (menuItem.getItemId()) {
                            case R.id.navigation_item_1:
                                getSupportFragmentManager().beginTransaction().replace(R.id.content, new UiFgParentFg()).commit();
                                Snackbar.make( drawerLayout, "Item One",
                                        Snackbar.LENGTH_SHORT).show();
                                //mCurrentSelectedPosition = 0;
                                return true;
                            case R.id.navigation_item_2:
                                getSupportFragmentManager().beginTransaction().replace(R.id.content, new UiTestFg1()).commit();
                                Snackbar.make(drawerLayout, "Item Two",
                                        Snackbar.LENGTH_SHORT).show();
                                //mCurrentSelectedPosition = 1;
                                return true;
                            default:
                                return true;
                        }


                        //return true;
                    }
                });
    }
    private void setupNavigationView(){

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        NavigationView navigationView=(NavigationView)findViewById(R.id.navigation_view);
        if(navigationView!=null){
            setupDrawerContent(navigationView);
        }
    }

//    private void setupTextInputLayout(){
//        TextInputLayout usernameTextInputLayout=(TextInputLayout)findViewById(R.id.text_input);
//        usernameTextInputLayout.setErrorEnabled(true);
//        //usernameTextInputLayout.setError("error");
//    }
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
//        if(v.getId()==R.id.fb){
//            if(i==1) {
//
//i--;
//                getSupportFragmentManager().beginTransaction().replace(R.id.content, new CheeseListFragment()).commit();
//            }
//            else {
//                i++;
//                getSupportFragmentManager().beginTransaction().replace(R.id.content, new UiTestFg1()).commit();
//            }
            Intent intent=new Intent(MainActivity.this,CheeseDetailActivity.class);
            startActivity(intent);
            //finish();
            //Snackbar.make(v,"Hello",Snackbar.LENGTH_SHORT).setAction("Action",this).show();
        }


}
