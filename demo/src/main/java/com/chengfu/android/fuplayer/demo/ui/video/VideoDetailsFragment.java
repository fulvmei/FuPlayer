package com.chengfu.android.fuplayer.demo.ui.video;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.chengfu.android.fuplayer.demo.R;
import com.chengfu.android.fuplayer.demo.bean.Video;
import com.chengfu.android.fuplayer.demo.ui.local.LocalVideoListAdapter;
import com.chengfu.android.fuplayer.demo.util.VideoUtil;

import java.util.List;

public class VideoDetailsFragment extends Fragment  {

    private RecyclerView recyclerView;

    private LocalVideoListAdapter adapter;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_video_details, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView = view.findViewById(R.id.recyclerView);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        List<Video> videos=VideoUtil.getVideoList();
        adapter = new LocalVideoListAdapter();
        recyclerView.setAdapter(adapter);
        adapter.setData(videos);
    }
}
