package com.chengfu.android.fuplayer.demo;

import android.app.PendingIntent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.media.AudioManager;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import com.bumptech.glide.Glide;
import com.chengfu.android.fuplayer.FuPlayer;
import com.chengfu.android.fuplayer.demo.bean.Media;
import com.chengfu.android.fuplayer.demo.util.MediaSourceUtil;
import com.chengfu.android.fuplayer.ui.DefaultControlView;
import com.chengfu.android.fuplayer.ui.DefaultTimeBar;
import com.chengfu.android.fuplayer.ui.FuPlayerView;
import com.chengfu.android.fuplayer.ext.exo.FuExoPlayerFactory;
import com.chengfu.android.fuplayer.ui.PlayerNotificationManager;
import com.chengfu.android.fuplayer.ui.SampleBufferingView;
import com.chengfu.android.fuplayer.ui.SampleEndedView;
import com.chengfu.android.fuplayer.ui.SampleErrorView;
import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.source.ConcatenatingMediaSource;
import com.google.android.exoplayer2.source.ShuffleOrder;
import com.google.android.exoplayer2.source.hls.HlsManifest;
import com.gyf.barlibrary.BarHide;
import com.gyf.barlibrary.ImmersionBar;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import timber.log.Timber;

public class PlayerActivity extends AppCompatActivity {

    public static final String TAG = "VideoPlayerActivity";
    private Media media;

    private View playerRoot;
    private FuPlayer player;
    private SampleBufferingView loadingView;
    private SampleErrorView errorView;
    private SampleEndedView endedView;
    private PlayerNotificationManager playerNotificationManager;
    private Bitmap bigIcon;
    DefaultControlView controlView;
    ConcatenatingMediaSource mediaSource;
    FuPlayerView playerView;

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

        findViewById(R.id.add1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int contentType = C.TYPE_OTHER;
                if (media.getTag().endsWith("m3u8")) {
                    contentType = C.TYPE_HLS;
                }
                mediaSource.addMediaSource(MediaSourceUtil.getMediaSource(PlayerActivity.this, "http://mvoice.spriteapp.cn/voice/2016/1108/5821463c8ea94.mp3", contentType));

//                player.stop(true);
            }
        });

        findViewById(R.id.add2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int contentType = C.TYPE_OTHER;
                if (media.getTag().endsWith("m3u8")) {
                    contentType = C.TYPE_HLS;
                }
                mediaSource.addMediaSource(0,MediaSourceUtil.getMediaSource(PlayerActivity.this, "https://qn-live.gzstv.com/icvkuzqj/yinyue.m3u8", C.TYPE_HLS));

//                player.stop(true);
            }
        });

        findViewById(R.id.previous).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                player.previous();
            }
        });

        findViewById(R.id.next).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                player.next();
            }
        });

        playerRoot = findViewById(R.id.playerRoot);

        playerView = findViewById(R.id.playerView);

        Glide.with(this)
                .asBitmap()
                .load("http://pic29.nipic.com/20130517/9252150_140653449378_2.jpg")
//                .bitmapTransform(new BlurTransformation(PlayerActivity.this,23,4))  // “23”：设置模糊度(在0.0到25.0之间)，默认”25";"4":图片缩放比例,默认“1”。
                .into(playerView.getUnderlayView());

        loadingView = findViewById(R.id.bufferingView);
        errorView = findViewById(R.id.errorView);
        endedView = findViewById(R.id.endedView);
        controlView = findViewById(R.id.controlView);

        controlView.setShowAlwaysInPaused(true);

//        controlView.setProgressAdapter(new DynamicProgressAdapter(1000,6000));

//        mediaSource.addMediaSource(MediaSourceUtil.getMediaSource(this, media.getPath()));
//        mediaSource.addMediaSource(MediaSourceUtil.getMediaSource(this, media.getPath()));

        initPlayer();


//        player.retry();

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

                if (bigIcon != null) {
                    return bigIcon;
                }
                Picasso.get().load("http://pic29.nipic.com/20130517/9252150_140653449378_2.jpg")
                        .centerCrop()
                        .resize(100, 100)
                        .into(new Target() {
                            @Override
                            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                                bigIcon = bitmap;
                                callback.onBitmap(bitmap);
                            }

                            @Override
                            public void onBitmapFailed(Exception e, Drawable errorDrawable) {
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


        onOrientationChanged(getResources().getConfiguration().orientation);


        DefaultTimeBar timeBar = findViewById(R.id.timeBar);

        timeBar.setPosition(30000);
        timeBar.setDuration(60000);
    }

    private void initPlayer() {
        player = new FuExoPlayerFactory(this).create();

        player.addListener(new Player.EventListener() {
            @Override
            public void onTimelineChanged(Timeline timeline, @Nullable Object manifest, int reason) {
                JSONObject ja = new JSONObject();
                Timeline.Window window = new Timeline.Window();
                Timeline.Period period = new Timeline.Period();
                if (timeline.isEmpty()) {
                    return;
                }
                timeline.getWindow(player.getCurrentWindowIndex(), window);
                timeline.getPeriod(player.getCurrentWindowIndex(), period);

                try {
                    ja.put("getWindowCount", timeline.getWindowCount());
                    ja.put("getPeriodCount", timeline.getPeriodCount());
                    ja.put("getCurrentPeriodIndex", player.getCurrentPeriodIndex());
                    ja.put("getCurrentWindowIndex", player.getCurrentWindowIndex());
//                    ja.put("defaultPositionUs", window.defaultPositionUs);
//                    ja.put("durationUs", window.durationUs);
//                    ja.put("firstPeriodIndex", window.firstPeriodIndex);
//                    ja.put("isDynamic", window.isDynamic);
//                    ja.put("isLive", window.isLive);
//                    ja.put("isSeekable", window.isSeekable);
//                    ja.put("lastPeriodIndex", window.lastPeriodIndex);
//                    ja.put("positionInFirstPeriodUs", window.positionInFirstPeriodUs);
//                    ja.put("presentationStartTimeMs", window.presentationStartTimeMs);
//                    ja.put("windowStartTimeMs", window.windowStartTimeMs);
//                    ja.put("manifest", window.manifest);
//                    ja.put("tag", window.tag);
//                    ja.put("uid", window.uid);
//                    ja.put("period_durationUs",period.durationUs);
//                    ja.put("period_windowIndex",period.windowIndex);
//                    ja.put("period_id",period.id);
//                    ja.put("period_uid",period.uid);

                    HlsManifest s;

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Timber.e("onTimelineChanged reason="+reason+",data"+ja.toString());
//                System.out.println(ja.toString());
            }

            @Override
            public void onPositionDiscontinuity(int reason) {
                Timber.e("onPositionDiscontinuity reason="+reason);
            }
        });

        mediaSource = new ConcatenatingMediaSource(false,false,new ShuffleOrder.DefaultShuffleOrder(0));
        int contentType = C.TYPE_OTHER;
        if (media.getTag().endsWith("m3u8")) {
            contentType = C.TYPE_HLS;
        }
        mediaSource.addMediaSource(MediaSourceUtil.getMediaSource(this, media.getPath(), contentType));

        player.prepare(mediaSource);
        player.setPlayWhenReady(true);

        loadingView.setPlayer(player);
        errorView.setPlayer(player);
        endedView.setPlayer(player);
        playerView.setPlayer(player);
        controlView.setPlayer(player);
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
//            layoutParams.height = (int) (ScreenTools.getScreenWidth(this) * 9 * 1.0f / 16);
            layoutParams.height = 1200;

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
    protected void onStart() {
        super.onStart();
        if (player == null) {
            initPlayer();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (player != null) {
            player.release();
            player = null;
        }
    }
}
