package wxm.com.androiddesign.ui;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.ArrayList;

import wxm.com.androiddesign.R;
import wxm.com.androiddesign.adapter.MultipleItemAdapter;
import wxm.com.androiddesign.module.ActivityItemData;
import wxm.com.androiddesign.module.CommentData;

public class DetailActivity extends AppCompatActivity {

    public final static String ID = "ID";

    public ActivityItemData mContact;

// In the onCreate, after the setContentView method

    RecyclerView recyclerView;
    MultipleItemAdapter multipleItemAdapter;// = new MultipleItemAdapter(activityItemData,commentDatas);
   ActivityItemData activityItemData;
  //  static ActivityItemData activityItemData = new ActivityItemData(R.drawable.miao,"name","tag","time","atyname","atycontent",R.drawable.miao,"location","0","0");
  ArrayList<CommentData>commentDatas=new ArrayList<CommentData>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_detail);


       // activityItemData = new ActivityItemData(R.drawable.miao,"name","tag","time","atyname","atycontent",R.drawable.miao,"location","0","0");
//       activityItemData = getIntent().getBundleExtra("atyData").getParcelable("Data");
        //activityItemData = getIntent().getParcelableExtra("atyData");
        Bundle bundle=getIntent().getExtras();
        activityItemData=(ActivityItemData)(bundle.getParcelable("com.wxm.com.androiddesign.module.ActivityItemData"));

//        if(activityItemData==null) {
//            Toast.makeText(this,"null!!!!!",Toast.LENGTH_LONG).show();
//        }
        for (int i = 0; i < 5; i++) {
            commentDatas.add(new CommentData(R.drawable.miao,5,"I'm comment"));
        }
        multipleItemAdapter =  new MultipleItemAdapter(activityItemData,commentDatas);

        recyclerView=(RecyclerView)findViewById(R.id.recyclerview_activity);
        setupRecyclerView(recyclerView);
        Toolbar toolbar=(Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        final ActionBar actionBar=getSupportActionBar();

        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);
        addComment();


    }

    public void addComment()
    {
        ImageView cmt_comment=(ImageView)findViewById(R.id.cmt_comment);
        final EditText cmt_text = (EditText) findViewById(R.id.add_comment);
        cmt_comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(cmt_text.getText()!=null) {
                    commentDatas.add(new CommentData(R.drawable.miao, 5, cmt_text.getText().toString()));
                    cmt_text.setText(null);
                    multipleItemAdapter.notifyDataSetChanged();
                   // recyclerView.set
                }
            }
        });
    }

    private void setupRecyclerView(RecyclerView recyclerView){
        recyclerView.setLayoutManager(new LinearLayoutManager(recyclerView.getContext()));

        recyclerView.setHasFixedSize(true);
        //recyclerView.addItemDecoration(new SpacesItemDecoration(5));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(new MultipleItemAdapter(activityItemData,commentDatas));


        recyclerView.setAdapter(multipleItemAdapter);

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_detail, menu);
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
