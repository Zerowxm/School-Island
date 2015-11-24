package wxm.com.androiddesign.ui;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import wxm.com.androiddesign.R;
import wxm.com.androiddesign.adapter.GroupManagerAdapter;
import wxm.com.androiddesign.anim.MyItemAnimator;
import wxm.com.androiddesign.listener.RecyclerItemClickListener;
import wxm.com.androiddesign.utils.ActivityStartHelper;
import wxm.com.androiddesign.utils.SpacesItemDecoration;

public class ManageGroupActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_with_toolbar);

        setToolBar();
        setupRecyclerView();
    }

    private void setToolBar(){
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("管理部落");
        setSupportActionBar(toolbar);
        final ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);
    }

    private void setupRecyclerView() {
        recyclerView = (RecyclerView) findViewById(R.id.recyclerview_activity);
        recyclerView.setLayoutManager(new LinearLayoutManager(recyclerView.getContext()));
        recyclerView.setItemAnimator(new MyItemAnimator());
        recyclerView.setAdapter(new GroupManagerAdapter());
    }

    public static void start(Context context){
        context.startActivity(new Intent(context,ManageGroupActivity.class));
    }
}
