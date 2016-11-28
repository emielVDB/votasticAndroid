package com.vandenbussche.emiel.projectsbp.gui.activities;

import android.accounts.Account;
import android.content.ContentResolver;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import com.vandenbussche.emiel.projectsbp.R;
import com.vandenbussche.emiel.projectsbp.auth.AuthHelper;
import com.vandenbussche.emiel.projectsbp.database.provider.Contract;
import com.vandenbussche.emiel.projectsbp.gui.fragments.BlankFragment;
import com.vandenbussche.emiel.projectsbp.gui.fragments.MyPollsFragment;
import com.vandenbussche.emiel.projectsbp.gui.fragments.NewsFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class HomeActivity extends AppCompatActivity {
    private ViewPagerAdapter mAdapter;

    @InjectView(R.id.toolbar)
    protected Toolbar toolbar;
    @InjectView(R.id.tabs)
    protected TabLayout tabLayout;
    @InjectView(R.id.viewpager)
    protected ViewPager viewPager;
    @InjectView(R.id.appBarLayout)
    protected AppBarLayout appBarLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        ButterKnife.inject(this);

        setSupportActionBar(toolbar);

        FloatingActionButton fabNewPoll = (FloatingActionButton)findViewById(R.id.fabNewPoll);
        FloatingActionButton fabNewPage = (FloatingActionButton)findViewById(R.id.fabNewPage);
        final FloatingActionMenu fam = (FloatingActionMenu)findViewById(R.id.fam);
        fabNewPoll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fam.close(true);
                Intent intent = new Intent(HomeActivity.this, NewPollActivity.class);
                startActivity(intent);

            }
        });
        fabNewPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fam.close(true);
                Intent intent = new Intent(HomeActivity.this, NewPageActivity.class);
                startActivity(intent);
            }
        });


        setupViewPager(viewPager);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.getTabAt(0).setIcon(R.drawable.news);
        tabLayout.getTabAt(1).setIcon(R.drawable.my_polls);
        tabLayout.getTabAt(2).setIcon(R.drawable.notifications);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_home, menu);

        return  true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.action_settings){
            Account account = AuthHelper.getAccount(this);
            Bundle settingsBundle = new Bundle();
            settingsBundle.putBoolean(ContentResolver.SYNC_EXTRAS_MANUAL, true);
            settingsBundle.putBoolean(ContentResolver.SYNC_EXTRAS_EXPEDITED, true);
            this.getContentResolver().requestSync(account,
                    Contract.AUTHORITY, settingsBundle);
        }else if(item.getItemId() == R.id.action_search){
            startActivity(new Intent(this, RandomPollsActivity.class));
        }
        return super.onOptionsItemSelected(item);
    }

    private void setupViewPager(ViewPager viewPager) {
        mAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        mAdapter.addFragment(new NewsFragment(), "News");
        mAdapter.addFragment(new MyPollsFragment(), "My Polls");
        mAdapter.addFragment(BlankFragment.newInstance("partners", "tof"), "Notifications");
        viewPager.setAdapter(mAdapter);
        viewPager.addOnPageChangeListener(new ViewPagerListener());
    }


    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
//            return null;
        }
    }
    class ViewPagerListener implements ViewPager.OnPageChangeListener{

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {

        }

        @Override
        public void onPageScrollStateChanged(int state) {
            appBarLayout.setExpanded(true, true);
        }

    }
}
