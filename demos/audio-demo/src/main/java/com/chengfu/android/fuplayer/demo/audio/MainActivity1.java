package com.chengfu.android.fuplayer.demo.audio;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.media.MediaBrowserCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;


import com.chengfu.android.fuplayer.audio.MusicContract;
import com.chengfu.android.fuplayer.demo.audio.ui.home.song.SongsFragment;
import com.chengfu.android.fuplayer.demo.audio.viewmodels.MainActivityViewModel;

import java.util.Arrays;
import java.util.List;

public class MainActivity1 extends AppCompatActivity {

    private final static String TAG = "MainActivity";

    private SectionsPagerAdapter mSectionsPagerAdapter;
    private MediaSessionConnection mediaSessionConnection;

    private TabLayout mTabLayout;
    private ViewPager mViewPager;

    private MainActivityViewModel viewModel;

    private MediaBrowserCompat mMediaBrowser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        viewModel = ViewModelProviders.of(this).get(MainActivityViewModel.class);

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);

        String[] tabs = getResources().getStringArray(R.array.home_page_values);

        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager(), Arrays.asList(tabs));

        mViewPager = findViewById(R.id.viewPager);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        mTabLayout = findViewById(R.id.tabs);
        mTabLayout.setupWithViewPager(mViewPager);

        getContentResolver().query(MusicContract.CONTENT_URI, null, null, null, null);
    }


    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        List<String> tabs;

        public SectionsPagerAdapter(FragmentManager fm, List<String> tabs) {
            super(fm);
            this.tabs = tabs;
        }

        @Override
        public Fragment getItem(int position) {
            String tab = tabs.get(position);
            return SongsFragment.newInstance(tab);
        }

        @Override
        public int getCount() {
            if (tabs != null) {
                return tabs.size();
            }
            return 0;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return tabs.get(position);
        }
    }

}
