package com.chengfu.android.fuplayer.demo.video.ui.video;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.chengfu.android.fuplayer.demo.video.R;

import java.util.ArrayList;
import java.util.List;

public class VideoListActivity extends AppCompatActivity implements IGetVideoContainer {

    private final static String TAG = "MediaSessionTest";

    private final static String TABS[] = {"在线", "本地", "全部", "音乐"};
//    private final static String TABS[] = {"热门"};

    private SectionsPagerAdapter mSectionsPagerAdapter;
    private List<VideoListFragment> videoListFragments;

    private TabLayout mTabLayout;
    private ViewPager mViewPager;
    private FrameLayout videoContainer;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_video_list);

        videoListFragments = new ArrayList<>();
        for (int i = 0; i < TABS.length; i++) {
            videoListFragments.add(VideoListFragment.newInstance(TABS[i]));
        }

        mSectionsPagerAdapter = new SectionsPagerAdapter();

        mViewPager = findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        mTabLayout = findViewById(R.id.tabs);
        mTabLayout.setupWithViewPager(mViewPager);

        videoContainer = findViewById(R.id.videoContainer);

    }

    @Override
    public void onBackPressed() {
        if (!videoListFragments.get(mTabLayout.getSelectedTabPosition()).onBackPressed()) {
            super.onBackPressed();
        }
    }

    @Override
    public ViewGroup getVideoContainer() {
        return videoContainer;
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter() {
            super(getSupportFragmentManager());
        }

        @Override
        public Fragment getItem(int position) {
            return videoListFragments.get(position);
        }

        @Override
        public int getCount() {
            return TABS.length;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return TABS[position];
        }
    }

}