package com.chengfu.android.fuplayer.demo.audio.viewmodels;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.content.ComponentName;
import android.support.annotation.NonNull;

import com.chengfu.android.fuplayer.audio.MusicService;
import com.chengfu.android.fuplayer.demo.audio.MediaSessionConnection;


public class MainActivityViewModel extends AndroidViewModel {

    public final MediaSessionConnection mediaSessionConnection;

    public MainActivityViewModel(@NonNull Application application) {
        super(application);
        mediaSessionConnection = new MediaSessionConnection(application, new ComponentName(application, MusicService.class));
    }


//    public MainActivityViewModel(MediaSessionConnection mediaSessionConnection) {
//        this.mediaSessionConnection = mediaSessionConnection;
//        mediaBrowserConnected = mediaSessionConnection.isConnected;
//    }
}
