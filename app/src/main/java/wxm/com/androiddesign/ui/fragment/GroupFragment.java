package wxm.com.androiddesign.ui.fragment;

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
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import butterknife.Bind;
import butterknife.ButterKnife;
import wxm.com.androiddesign.R;
import wxm.com.androiddesign.adapter.TabPagerAdapter;
import wxm.com.androiddesign.module.MyUser;
import wxm.com.androiddesign.ui.MainActivity;
import wxm.com.androiddesign.utils.Config;


/**
 * Created by Wu on 2015/4/16.
 */
public class GroupFragment extends Fragment implements AppBarLayout.OnOffsetChangedListener{

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

    public static Fragment newInstance(String muserId) {
        Fragment fragment = new GroupFragment();
        Bundle args = new Bundle();
        args.putString("userId", muserId);
        fragment.setArguments(args);
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
                        CmtListFragment activityFragment = (CmtListFragment) adapter.getItem(viewPager.getCurrentItem());
                        if (index == 0) {
                            activityFragment.getmSwipeRefreshLayout().setEnabled(true);
                        } else {
                            activityFragment.getmSwipeRefreshLayout().setEnabled(false);
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
        adapter.addFragment(CmtListFragment.newInstance(MyUser.userId,CmtListFragment.JOINED), "加入的小组");
        adapter.addFragment(CmtListFragment.newInstance(MyUser.userId,CmtListFragment.OWNED), "我的小组");
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
        userId = getArguments().getString("userId");
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

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
    }
}