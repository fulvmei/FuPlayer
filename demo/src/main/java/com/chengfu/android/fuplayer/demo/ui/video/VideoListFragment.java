package com.chengfu.android.fuplayer.demo.ui.video;

import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import com.chengfu.android.fuplayer.demo.R;
import com.chengfu.android.fuplayer.demo.util.VideoUtil;

public class VideoListFragment extends Fragment implements IBackPressed {

    private RecyclerView recyclerView;

    private VideoListAdapter adapter;

//    private ExoPlayer player;


    public static VideoListFragment newInstance(String title) {
        return new VideoListFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_video_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView = view.findViewById(R.id.recyclerView);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
//        player = ExoPlayerFactory.newSimpleInstance(getActivity());

        adapter = new VideoListAdapter(getActivity());

        adapter.setData(VideoUtil.getVideoList());

        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (adapter != null) {
            adapter.onConfigurationChanged(newConfig);
        }
    }

    @Override
    public void onPause() {
        super.onPause();

        if (adapter != null) {
            adapter.maybePausePlay();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (adapter != null) {
            adapter.maybeResumePlay();
        }
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (adapter != null) {
            if (hidden) {
                adapter.maybeStopPlay();
            } else {
//                adapter.maybeStartPlay();
            }
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (adapter != null) {
            if (!isVisibleToUser) {
                adapter.maybeStopPlay();
            } else {
//                adapter.maybeStartPlay();
            }
        }
    }

    @Override
    public boolean onBackPressed() {
        if (adapter!=null){
            return  adapter.onBackPressed();
        }
        return false;
    }
}
