package com.chengfu.android.fuplayer.demo;

<<<<<<< HEAD
import android.app.PendingIntent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.media.AudioManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
=======
import android.content.res.Configuration;
import android.graphics.Color;
import android.media.AudioManager;
import android.os.Bundle;
>>>>>>> c83ea00c7e011345ba8d9b2fcc5a1dc569d04e88
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;

import com.chengfu.android.fuplayer.FuPlayer;
import com.chengfu.android.fuplayer.demo.bean.Media;
import com.chengfu.android.fuplayer.demo.util.MediaSourceUtil;
import com.chengfu.android.fuplayer.demo.util.ScreenTools;
import com.chengfu.android.fuplayer.ui.DefaultControlView;
import com.chengfu.android.fuplayer.ui.FuPlayerView;
import com.chengfu.android.fuplayer.ext.exo.FuExoPlayerFactory;
<<<<<<< HEAD
import com.chengfu.android.fuplayer.ui.PlayerNotificationManager;
import com.chengfu.android.fuplayer.ui.SampleBufferingView;
import com.chengfu.android.fuplayer.ui.SampleEndedView;
import com.chengfu.android.fuplayer.ui.SampleErrorView;
import com.google.android.exoplayer2.source.ConcatenatingMediaSource;
import com.gyf.barlibrary.BarHide;
import com.gyf.barlibrary.ImmersionBar;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;


public class PlayerActivity extends AppCompatActivity {

=======
import com.chengfu.android.fuplayer.ui.SampleBufferingView;
import com.chengfu.android.fuplayer.ui.SampleEndedView;
import com.chengfu.android.fuplayer.ui.SampleErrorView;
import com.google.android.exoplayer2.Player;
import com.gyf.barlibrary.BarHide;
import com.gyf.barlibrary.ImmersionBar;


public class PlayerActivity extends AppCompatActivity {
>>>>>>> c83ea00c7e011345ba8d9b2fcc5a1dc569d04e88
    public static final String TAG = "VideoPlayerActivity";
    private Media media;

    private View playerRoot;
    private FuPlayer player;
    private SampleBufferingView loadingView;
    private SampleErrorView errorView;
    private SampleEndedView endedView;
<<<<<<< HEAD
    private PlayerNotificationManager playerNotificationManager;
    private Bitmap bigIcon;
=======
>>>>>>> c83ea00c7e011345ba8d9b2fcc5a1dc569d04e88

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setVolumeControlStream(AudioManager.STREAM_MUSIC);

        media = (Media) getIntent().getSerializableExtra("media");

        setContentView(R.layout.activity_player);

        ImmersionBar.with(this)
                .statusBarColorInt(Color.BLACK)
                .fitsSystemWindows(true)
                .hideBar(BarHide.FLAG_SHOW_BAR)
                .init();

        playerRoot = findViewById(R.id.playerRoot);

        FuPlayerView playerView = findViewById(R.id.playerView);
        loadingView = findViewById(R.id.bufferingView);
        errorView = findViewById(R.id.errorView);
        endedView = findViewById(R.id.endedView);
        DefaultControlView controlView = findViewById(R.id.controlView);

        controlView.setShowAlwaysInPaused(true);

        player = new FuExoPlayerFactory(this).create();
<<<<<<< HEAD
        ConcatenatingMediaSource mediaSource = new ConcatenatingMediaSource();
        mediaSource.addMediaSource(MediaSourceUtil.getMediaSource(this, media.getPath()));
//        mediaSource.addMediaSource(MediaSourceUtil.getMediaSource(this, media.getPath()));
//        mediaSource.addMediaSource(MediaSourceUtil.getMediaSource(this, media.getPath()));
        player.prepare(mediaSource);
=======
        player.prepare(MediaSourceUtil.getMediaSource(this, media.getPath()));
>>>>>>> c83ea00c7e011345ba8d9b2fcc5a1dc569d04e88
        player.setPlayWhenReady(true);

        loadingView.setPlayer(player);
        errorView.setPlayer(player);
        endedView.setPlayer(player);
        playerView.setPlayer(player);
        controlView.setPlayer(player);

<<<<<<< HEAD

        playerNotificationManager = PlayerNotificationManager.createWithNotificationChannel(this, "com.chengfu.android.media.NOW_PLAYING", R.string.app_name, 2, new PlayerNotificationManager.MediaDescriptionAdapter() {
            @Override
            public String getCurrentContentTitle(FuPlayer player) {
                return media.getName();
            }

            @Nullable
            @Override
            public PendingIntent createCurrentContentIntent(FuPlayer player) {
                return null;
            }

            @Nullable
            @Override
            public String getCurrentContentText(FuPlayer player) {
                return media.getTag();
            }

            @Nullable
            @Override
            public Bitmap getCurrentLargeIcon(FuPlayer player, PlayerNotificationManager.BitmapCallback callback) {
                System.out.println("0000000000000");
                if (bigIcon != null) {
                    return bigIcon;
                }
                System.out.println("222222222222222");
                Picasso.get().load("http://pic29.nipic.com/20130517/9252150_140653449378_2.jpg")
                        .centerCrop()
                        .resize(100, 100)
                        .into(new Target() {
                            @Override
                            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                                System.out.println("1111111111111111111111111111111111111");
                                bigIcon = bitmap;
                                callback.onBitmap(bitmap);
                            }

                            @Override
                            public void onBitmapFailed(Exception e, Drawable errorDrawable) {
                                System.out.println("2222222222222222222222222222222222222" + e);
                                callback.onBitmap(bigIcon);
                            }

                            @Override
                            public void onPrepareLoad(Drawable placeHolderDrawable) {

                            }
                        });
                return null;
            }
        });

        playerNotificationManager.setRewindIncrementMs(0);
        playerNotificationManager.setFastForwardIncrementMs(0);
        playerNotificationManager.setUseNavigationActions(true);
        playerNotificationManager.setPlayer(player);
        playerNotificationManager.setUseNavigationActionsInCompactView(true);
        playerNotificationManager.setVisibility(NotificationCompat.VISIBILITY_PRIVATE);
        playerNotificationManager.setUseStopAction(true);
        playerNotificationManager.setPriority(NotificationCompat.PRIORITY_MAX);


=======
>>>>>>> c83ea00c7e011345ba8d9b2fcc5a1dc569d04e88
        onOrientationChanged(getResources().getConfiguration().orientation);
    }

    private void onOrientationChanged(int orientation) {
        if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            ViewGroup.LayoutParams layoutParams = playerRoot.getLayoutParams();
            layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
            layoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT;

            ImmersionBar.with(this)
                    .fitsSystemWindows(false)
                    .hideBar(BarHide.FLAG_HIDE_BAR)
                    .init();
        } else if (orientation == Configuration.ORIENTATION_PORTRAIT) {
            ViewGroup.LayoutParams layoutParams = playerRoot.getLayoutParams();
            layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
            layoutParams.height = (int) (ScreenTools.getScreenWidth(this) * 9 * 1.0f / 16);

            ImmersionBar.with(this)
                    .statusBarColorInt(Color.BLACK)
                    .fitsSystemWindows(true)
                    .hideBar(BarHide.FLAG_SHOW_BAR)
                    .init();
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        onOrientationChanged(newConfig.orientation);
    }


    @Override
    protected void onResume() {
        super.onResume();
<<<<<<< HEAD
        player.setPlayWhenReady(true);
//        player.retry();
=======
        player.retry();
>>>>>>> c83ea00c7e011345ba8d9b2fcc5a1dc569d04e88
    }

    @Override
    protected void onPause() {
        super.onPause();
<<<<<<< HEAD
        player.setPlayWhenReady(false);
//        if(player.getPlaybackState()!= Player.STATE_IDLE){
//            player.stop();
//        }
=======
        if(player.getPlaybackState()!= Player.STATE_IDLE){
            player.stop();
        }
>>>>>>> c83ea00c7e011345ba8d9b2fcc5a1dc569d04e88
    }

    @Override
    protected void onDestroy() {
        player.release();
        super.onDestroy();
    }
}
