package com.chengfu.android.fuplayer.demo.audio.ui.player;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.chengfu.android.fuplayer.audio.widget.AudioPlayView;
import com.chengfu.android.fuplayer.demo.audio.R;
import com.chengfu.android.fuplayer.demo.audio.viewmodels.MainActivityViewModel;


public class MusicControllerFragment extends Fragment {

    private AudioPlayView musicPlayView;

    private MainActivityViewModel viewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_music_controller, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        musicPlayView = view.findViewById(R.id.musicPlayView);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        viewModel = ViewModelProviders.of(this).get(MainActivityViewModel.class);


        viewModel.mediaSessionConnection.isConnected.observe(this, aBoolean -> {
            if (aBoolean) {
                musicPlayView.setSessionToken(viewModel.mediaSessionConnection.mediaBrowser.getSessionToken());
            } else {
                musicPlayView.setSessionToken(null);
            }
        });
    }
}
