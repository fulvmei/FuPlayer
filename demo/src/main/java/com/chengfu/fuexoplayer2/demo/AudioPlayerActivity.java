package com.chengfu.fuexoplayer2.demo;

import android.content.ComponentName;
import android.content.Intent;
import android.media.AudioManager;
import android.os.Bundle;
import android.os.RemoteException;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.media.MediaBrowserCompat;
import android.support.v4.media.MediaMetadataCompat;
import android.support.v4.media.session.MediaControllerCompat;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.chengfu.fuexoplayer2.MusicService;

import java.util.List;

public class AudioPlayerActivity extends AppCompatActivity {

    private static final String TAG = "AudioPlayerActivity";

    private MediaBrowserCompat mBrowser;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_music_player);

        setVolumeControlStream(AudioManager.STREAM_MUSIC);

        Bundle rootHints = new Bundle();
        rootHints.putString("321", "去死吧");
        mBrowser = new MediaBrowserCompat(
                this,
                new ComponentName(this, MusicService.class),//绑定浏览器服务
                BrowserConnectionCallback,//设置连接回调
                rootHints
        );

//        mBrowser.getItem("", new MediaBrowserCompat.ItemCallback() {
//            @Override
//            public void onItemLoaded(MediaBrowserCompat.MediaItem item) {
//                super.onItemLoaded(item);
//            }
//        });

//        mBrowser.unsubscribe("123456");

        mBrowser.subscribe("123456", new MediaBrowserCompat.SubscriptionCallback() {
            @Override
            public void onChildrenLoaded(@NonNull String parentId, @NonNull List<MediaBrowserCompat.MediaItem> children) {
                super.onChildrenLoaded(parentId, children);
                Log.e(TAG, "onChildrenLoaded------parentId=" + parentId + ",children=" + children);
            }
        });


        findViewById(R.id.connect).setOnClickListener((View.OnClickListener) v -> mBrowser.connect());

        findViewById(R.id.disconnect).setOnClickListener(v -> mBrowser.disconnect());

        findViewById(R.id.stop).setOnClickListener(v -> {
            Intent intent = new Intent(AudioPlayerActivity.this, MusicService.class);
            stopService(intent);
        });
    }

    private MediaBrowserCompat.ConnectionCallback BrowserConnectionCallback =
            new MediaBrowserCompat.ConnectionCallback() {
                @Override
                public void onConnected() {
                    super.onConnected();
                    Log.e(TAG, "onConnected------sessionToken=" + mBrowser.getSessionToken() + ",isConnected=" + mBrowser.isConnected() + "," + mBrowser.getExtras().getString("123"));
                    if (mBrowser.isConnected()) {
                        MediaControllerCompat mController = null;
                        try {
                            mController = new MediaControllerCompat(AudioPlayerActivity.this, mBrowser.getSessionToken());
                        } catch (RemoteException e) {
                            e.printStackTrace();
                        }
                        //注册回调
                        mController.registerCallback(ControllerCallback);
                    }
                }

                @Override
                public void onConnectionSuspended() {
                    super.onConnectionSuspended();
                    Log.e(TAG, "onConnected------");
                }

                @Override
                public void onConnectionFailed() {
                    super.onConnectionFailed();
                    Log.e(TAG, "onConnected------");
                }
            };

    private final MediaControllerCompat.Callback ControllerCallback =
            new MediaControllerCompat.Callback() {
                @Override
                public void onAudioInfoChanged(MediaControllerCompat.PlaybackInfo info) {
                    super.onAudioInfoChanged(info);
                    Log.e(TAG, "onAudioInfoChanged------info=" + info);
                }

                @Override
                public void onCaptioningEnabledChanged(boolean enabled) {
                    super.onCaptioningEnabledChanged(enabled);
                    Log.e(TAG, "onCaptioningEnabledChanged------enabled=" + enabled);
                }

                @Override
                public void onExtrasChanged(Bundle extras) {
                    super.onExtrasChanged(extras);
                    Log.e(TAG, "onExtrasChanged------extras=" + extras);
                }

                @Override
                public void onMetadataChanged(MediaMetadataCompat metadata) {
                    super.onMetadataChanged(metadata);
                    Log.e(TAG, "onMetadataChanged------metadata=" + metadata);
                }

                @Override
                public void onPlaybackStateChanged(PlaybackStateCompat state) {
                    super.onPlaybackStateChanged(state);
                    Log.e(TAG, "onPlaybackStateChanged------state=" + state);
                }

                @Override
                public void onQueueChanged(List<MediaSessionCompat.QueueItem> queue) {
                    super.onQueueChanged(queue);
                    Log.e(TAG, "onQueueChanged------queue=" + queue);
                }

                @Override
                public void onQueueTitleChanged(CharSequence title) {
                    super.onQueueTitleChanged(title);
                    Log.e(TAG, "onQueueTitleChanged------title=" + title);
                }

                @Override
                public void onRepeatModeChanged(int repeatMode) {
                    super.onRepeatModeChanged(repeatMode);
                    Log.e(TAG, "onRepeatModeChanged------repeatMode=" + repeatMode);
                }

                @Override
                public void onSessionDestroyed() {
                    super.onSessionDestroyed();
                    Log.e(TAG, "onSessionDestroyed------");
                }

                @Override
                public void onSessionEvent(String event, Bundle extras) {
                    super.onSessionEvent(event, extras);
                    Log.e(TAG, "onSessionEvent------event=" + event);
                }

                @Override
                public void onSessionReady() {
                    super.onSessionReady();
                    Log.e(TAG, "onSessionReady------");
                }

                @Override
                public void onShuffleModeChanged(int shuffleMode) {
                    super.onShuffleModeChanged(shuffleMode);
                    Log.e(TAG, "onShuffleModeChanged------shuffleMode=" + shuffleMode);
                }
            };
}
