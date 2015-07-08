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

import wxm.com.androiddesign.R;
import wxm.com.androiddesign.adapter.MultipleItemAdapter;
import wxm.com.androiddesign.module.AtyItem;
import wxm.com.androiddesign.module.CommentData;

public class CmtDatailActivity extends AppCompatActivity {


    RecyclerView recyclerView;
    static AtyItem atyItem;
    static ArrayList<CommentData> commentDatas=new ArrayList<CommentData>();
//    static {
//        activityItemData = new ActivityItemData(R.drawable.miao,"name","tag","time","atyname","atycontent",R.drawable.miao,"location","0","0");
//        for (int i = 0; i < 5; i++) {
//            commentDatas.add(new CommentData(R.drawable.miao,5,"I'm comment"));
//        }
//    }

    private void setupRecyclerView(RecyclerView recyclerView){
        recyclerView.setLayoutManager(new LinearLayoutManager(recyclerView.getContext()));

        recyclerView.setAdapter(new MultipleItemAdapter(atyItem,commentDatas));
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.community_detail);
        recyclerView=(RecyclerView)findViewById(R.id.recyclerview_activity);
        Toolbar toolbar=(Toolbar)findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        final ActionBar actionBar=getSupportActionBar();

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
