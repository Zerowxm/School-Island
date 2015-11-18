package wxm.com.maca.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.WindowManager;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import wxm.com.maca.R;
import wxm.com.maca.adapter.TabPagerAdapter;
import wxm.com.maca.ui.fragment.ImageFragment;
import wxm.com.maca.widget.CirclePageIndicator;

/**
 * Created by Zero on 11/8/2015.
 */
public class ImageViewerActivity extends AppCompatActivity {

    List<String> list;
    @Bind(R.id.image_pager)
    ViewPager pager;

    int index;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.image_viewpager);
        Bundle bundle=getIntent().getExtras();
        list=bundle.getStringArrayList("album");
        index=bundle.getInt("index");
        ButterKnife.bind(this);
        setupViewPager();
    }

    public static void start(Context context,ArrayList<String> arrayList,int index_){
        context.startActivity(new Intent(context,ImageViewerActivity.class)
        .putStringArrayListExtra("album",arrayList).putExtra("index",index_));

    }

    private void setupViewPager() {
        TabPagerAdapter adapter = new TabPagerAdapter(getSupportFragmentManager());
        if (list!=null&&list.size()!=0){
            for (String url:list){
                adapter.addFragment(ImageFragment.newInstance(url,0),"AtyImg");
            }
        }
        pager.setAdapter(adapter);
        CirclePageIndicator circlePageIndicator=(CirclePageIndicator)findViewById(R.id.indicator);
        circlePageIndicator.setViewPager(pager);
        circlePageIndicator.setSnap(true);
        pager.setCurrentItem(index);
    }
}
