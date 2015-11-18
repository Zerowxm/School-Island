package wxm.com.maca.ui.fragment;

import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.MotionEventCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import butterknife.Bind;
import butterknife.ButterKnife;
import wxm.com.maca.R;
import wxm.com.maca.adapter.TabPagerAdapter;
import wxm.com.maca.module.MyUser;
import wxm.com.maca.ui.MainActivity;
import wxm.com.maca.utils.Config;


/**
 * Created by Wu on 2015/4/16.
 */
public class FragmentParent extends Fragment implements AppBarLayout.OnOffsetChangedListener{

    String userId;
    @Bind(R.id.appbar)
    AppBarLayout appBarLayout;
    MainActivity.MyOnTouchListener onTouchListener;
    private TabPagerAdapter adapter;
    @Bind(R.id.viewpager)
    ViewPager viewPager;
    @Bind(R.id.tabs)
    TabLayout tabLayout;
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.coordinatorLayout)
    CoordinatorLayout coordinatorLayout;

    public static Fragment newInstance() {
        Fragment fragment = new FragmentParent();
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragmentparent, container, false);
        ButterKnife.bind(this, rootView);
        appBarLayout.addOnOffsetChangedListener(this);
        if (viewPager != null) {
            setupViewPager();
        }

        setupToolBar();
        registerListener();
        return rootView;
    }
    private void registerListener(){
        ((MainActivity)getActivity()).registerMyOnTouchEvent(new MainActivity.MyOnTouchListener() {
            @Override
            public void onTouch(MotionEvent event) {
                final int action = MotionEventCompat.getActionMasked(event);
                switch (action) {
                    case MotionEvent.ACTION_DOWN:
                    case MotionEvent.ACTION_UP:
                    case MotionEvent.ACTION_CANCEL:

                        BaseFragment baseFragment = (BaseFragment) adapter.getItem(viewPager.getCurrentItem());
                        if (index == 0) {
                            baseFragment.getmSwipeRefreshLayout().setEnabled(true);
                        } else {
                            baseFragment.getmSwipeRefreshLayout().setEnabled(false);
                        }
                        break;
                }

            }
        });
    }
    private void setupToolBar(){
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        MainActivity.toolbar=toolbar;
        final ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        actionBar.setHomeAsUpIndicator(R.drawable.ic_menu);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);
    }

    private void setupViewPager() {
        Log.d("user", "setupViewPager" + userId);
        adapter = new TabPagerAdapter(getChildFragmentManager());
        adapter.addFragment(ActivityFragment.newInstance(ActivityFragment.HOT), "热门活动");
        adapter.addFragment(ActivityFragment.newInstance(ActivityFragment.LATEST), "最近活动");
        adapter.addFragment(GroupListFragment.newInstance(MyUser.userId, GroupListFragment.HOT,false), "热门部落");

        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onCreate(Bundle saveInstanceState) {
        super.onCreate(saveInstanceState);
        userId = MyUser.userId;
        setHasOptionsMenu(true);

    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onStop() {
        super.onStop();
        appBarLayout.removeOnOffsetChangedListener(this);
    }

    @Override
    public void onStart() {
        super.onStart();

    }

    //swipe to refresh fix
    int index=0;
    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int i) {
        index=i;
        Log.d(Config.appBar,"index:"+i);
//        if (index<-100){
//            resetAppBar();
//        }
    }

    private void resetAppBar(){
        CoordinatorLayout.LayoutParams params=(CoordinatorLayout.LayoutParams)appBarLayout.getLayoutParams();
        AppBarLayout.Behavior behavior=(AppBarLayout.Behavior)params.getBehavior();
        behavior.onNestedFling(coordinatorLayout,appBarLayout,null,0,-1000,true);
    }
}