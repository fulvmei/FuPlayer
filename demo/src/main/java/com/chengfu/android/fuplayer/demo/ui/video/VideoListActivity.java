package com.chengfu.android.fuplayer.demo.ui.video;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.chengfu.android.fuplayer.demo.R;

import java.util.Arrays;
import java.util.List;

public class VideoListActivity extends AppCompatActivity {

    private final static String TAG = "MediaSessionTest";

    private final static String TABS[] = {"热门", "影视", "游戏", "搞笑"};

    private SectionsPagerAdapter mSectionsPagerAdapter;

    private TabLayout mTabLayout;
    private ViewPager mViewPager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_video_list);

        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager(), Arrays.asList(TABS));

        mViewPager = findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        mTabLayout = findViewById(R.id.tabs);
        mTabLayout.setupWithViewPager(mViewPager);

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
            return VideoListFragment.newInstance(tab);
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
