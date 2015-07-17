package wxm.com.androiddesign.ui;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.List;

import wxm.com.androiddesign.R;
import wxm.com.androiddesign.adapter.MultipleItemAdapter;
import wxm.com.androiddesign.adapter.MyRecycerAdapter;
import wxm.com.androiddesign.module.AtyItem;
import wxm.com.androiddesign.module.CommentData;

public class CmtDatailActivity extends AppCompatActivity {


    RecyclerView recyclerView;

    List<AtyItem> atyItemList = new ArrayList<AtyItem>();

    String userId;


    private void setupRecyclerView(RecyclerView recyclerView) {
        recyclerView.setLayoutManager(new LinearLayoutManager(recyclerView.getContext()));


        recyclerView.setAdapter(new MyRecycerAdapter(atyItemList, this, "CmtDatailActivity"));

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.community_detail);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerview_activity);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        userId = savedInstanceState.getString("userId");
        setSupportActionBar(toolbar);
        final ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);
        setupRecyclerView(recyclerView);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_cmt_datail, menu);
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

        return super.onOptionsItemSelected(item);
    }
}
