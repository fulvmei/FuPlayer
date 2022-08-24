package com.chengfu.android.fuplayer.demo;

import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.chengfu.android.fuplayer.FuPlayer;
import com.chengfu.android.fuplayer.demo.bean.Media;
import com.chengfu.android.fuplayer.ext.exo.FuExoPlayer;
import com.chengfu.android.fuplayer.ui.BaseControlView;
import com.chengfu.android.fuplayer.ui.DefaultControlView;
import com.chengfu.android.fuplayer.ui.DefaultTimeBar;
import com.chengfu.android.fuplayer.ui.FuPlayerView;
import com.chengfu.android.fuplayer.ui.SampleBufferingView;
import com.chengfu.android.fuplayer.ui.SampleEndedView;
import com.chengfu.android.fuplayer.ui.SampleErrorView;
import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.MediaMetadata;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.metadata.Metadata;
import com.google.android.exoplayer2.source.DefaultMediaSourceFactory;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSource;
import com.google.android.exoplayer2.util.Util;
import com.gyf.immersionbar.BarHide;
import com.gyf.immersionbar.ImmersionBar;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import timber.log.Timber;

public class PlayerActivity extends AppCompatActivity {

    public static final String TAG = "VideoPlayerActivity";
//    private Media media;

    private View playerRoot;
    private FuPlayer player;
    private SampleBufferingView loadingView;
    private SampleErrorView errorView;
    private SampleEndedView endedView;
    //    private PlayerNotificationManager playerNotificationManager;
    private Bitmap bigIcon;
    DefaultControlView controlView;
    //    ConcatenatingMediaSource mediaSource;
    FuPlayerView playerView;

    List<Media> dataList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setVolumeControlStream(AudioManager.STREAM_MUSIC);

//        media = (Media) getIntent().getParcelableExtra("media");

        setContentView(R.layout.activity_player);

        dataList = getIntent().getParcelableArrayListExtra("list");

        ImmersionBar.with(this)
                .statusBarColorInt(Color.BLACK)
                .fitsSystemWindows(true)
                .hideBar(BarHide.FLAG_SHOW_BAR)
                .init();

        findViewById(R.id.add1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (int i = 0; i < player.getAvailableCommands().size(); i++) {
                    Timber.e("AvailableCommands=" + player.getAvailableCommands().get(i));
                }

//                MediaMetadata mediaMetadata=new MediaMetadata.Builder()
//                        .setTitle("HHHHHHHHHHHHHHHHHHHH")
//                        .build();
//                MediaItem mediaItem = new MediaItem.Builder()
//                        .setMediaId("1")
//                        .setMediaMetadata(mediaMetadata)
//                        .setUri(media.getPath())
//                        .build();

//                player.clearMediaItems();

//                Timber.e("hasNextWindow=%s", player.hasNextWindow());
//                Timber.e("hasPreviousWindow=%s", player.hasPreviousWindow());

//                int contentType = C.TYPE_OTHER;
//                if (media.getTag().endsWith("m3u8")) {
//                    contentType = C.TYPE_HLS;
//                }
////                mediaSource.addMediaSource(MediaSourceUtil.getMediaSource(PlayerActivity.this, "http://mvoice.spriteapp.cn/voice/2016/1108/5821463c8ea94.mp3", contentType));
////                player.stop(true);
            }
        });

        findViewById(R.id.add2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                player.seekToNext();
                Timber.e("rrrrr hasNextWindow=" + player.hasNextWindow());
                Timber.e("rrrrr hasPreviousWindow=" + player.hasPreviousWindow());
                Timber.e("rrrrr isCurrentWindowDynamic=" + player.isCurrentWindowDynamic());
                Timber.e("rrrrr isCurrentWindowLive=" + player.isCurrentWindowLive());
                Timber.e("rrrrr isCurrentWindowSeekable=" + player.isCurrentWindowSeekable());
                Timber.e("rrrrr getCurrentLiveOffset=" + player.getCurrentLiveOffset());


                Timeline.Window window = new Timeline.Window();
                player.getCurrentTimeline().getWindow(player.getCurrentWindowIndex(), window);// timeline.getWindow(getCurrentWindowIndex(), window).windowStartTimeMs;
                Timber.e("window defaultPositionUs=" + window.defaultPositionUs);
                Timber.e("window durationUs=" + window.durationUs);
                Timber.e("window elapsedRealtimeEpochOffsetMs=" + window.elapsedRealtimeEpochOffsetMs);
                Timber.e("window firstPeriodIndex=" + window.firstPeriodIndex);
                Timber.e("window isDynamic=" + window.isDynamic);
                Timber.e("window isPlaceholder=" + window.isPlaceholder);
                Timber.e("window isSeekable=" + window.isSeekable);
                Timber.e("window lastPeriodIndex=" + window.lastPeriodIndex);
                Timber.e("window windowStartTimeMs=" + window.windowStartTimeMs);
                Timber.e("window manifest=" + window.manifest);
                Timber.e("window liveConfiguration.maxOffsetMs=" + window.liveConfiguration.maxOffsetMs);
                Timber.e("window liveConfiguration.maxPlaybackSpeed=" + window.liveConfiguration.maxPlaybackSpeed);
                Timber.e("window liveConfiguration.minOffsetMs=" + window.liveConfiguration.minOffsetMs);
                Timber.e("window liveConfiguration.minPlaybackSpeed=" + window.liveConfiguration.minPlaybackSpeed);
                Timber.e("window liveConfiguration.targetOffsetMs=" + window.liveConfiguration.targetOffsetMs);
//                if (windowStartTimeMs == C.TIME_UNSET) {
//                    return C.TIME_UNSET;
//                }
//                return window.getCurrentUnixTimeMs() - window.windowStartTimeMs - getContentPosition();
                int contentType = C.TYPE_OTHER;
//                if (media.getTag().endsWith("m3u8")) {
//                    contentType = C.TYPE_HLS;
//                }
//                mediaSource.addMediaSource(0,MediaSourceUtil.getMediaSource(PlayerActivity.this, "https://qn-live.gzstv.com/icvkuzqj/yinyue.m3u8", C.TYPE_HLS));

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

        controlView.addProgressUpdateListener(new BaseControlView.ProgressUpdateListener() {
            @Override
            public void onProgressUpdate(long position, long bufferedPosition) {
                Timber.e("onProgressUpdate,position=%d,bufferedPosition=%d", position, bufferedPosition);
            }
        });

        controlView.setShowAlwaysInPaused(true);

//        controlView.setProgressAdapter(new DynamicProgressAdapter(1000,6000));

//        mediaSource.addMediaSource(MediaSourceUtil.getMediaSource(this, media.getPath()));
//        mediaSource.addMediaSource(MediaSourceUtil.getMediaSource(this, media.getPath()));

        initPlayer();


//        player.retry();

//        playerNotificationManager = PlayerNotificationManager.createWithNotificationChannel(this, "com.chengfu.android.media.NOW_PLAYING", R.string.app_name, 2, new PlayerNotificationManager.MediaDescriptionAdapter() {
//            @Override
//            public String getCurrentContentTitle(FuPlayer player) {
//                return media.getName();
//            }
//
//            @Nullable
//            @Override
//            public PendingIntent createCurrentContentIntent(FuPlayer player) {
//                return null;
//            }
//
//            @Nullable
//            @Override
//            public String getCurrentContentText(FuPlayer player) {
//                return media.getTag();
//            }
//
//            @Nullable
//            @Override
//            public Bitmap getCurrentLargeIcon(FuPlayer player, PlayerNotificationManager.BitmapCallback callback) {
//
//                if (bigIcon != null) {
//                    return bigIcon;
//                }
//                Picasso.get().load("http://pic29.nipic.com/20130517/9252150_140653449378_2.jpg")
//                        .centerCrop()
//                        .resize(100, 100)
//                        .into(new Target() {
//                            @Override
//                            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
//                                bigIcon = bitmap;
//                                callback.onBitmap(bitmap);
//                            }
//
//                            @Override
//                            public void onBitmapFailed(Exception e, Drawable errorDrawable) {
//                                callback.onBitmap(bigIcon);
//                            }
//
//                            @Override
//                            public void onPrepareLoad(Drawable placeHolderDrawable) {
//
//                            }
//                        });
//                return null;
//            }
//        });
//
//        playerNotificationManager.setRewindIncrementMs(0);
//        playerNotificationManager.setFastForwardIncrementMs(0);
//        playerNotificationManager.setUseNavigationActions(true);
//        playerNotificationManager.setPlayer(player);
//        playerNotificationManager.setUseNavigationActionsInCompactView(true);
//        playerNotificationManager.setVisibility(NotificationCompat.VISIBILITY_PRIVATE);
//        playerNotificationManager.setUseStopAction(true);
//        playerNotificationManager.setPriority(NotificationCompat.PRIORITY_MAX);


        onOrientationChanged(getResources().getConfiguration().orientation);


        DefaultTimeBar timeBar = findViewById(R.id.timeBar);

        timeBar.setPosition(30000);
        timeBar.setDuration(60000);
    }

//    private final Runnable runnable = () -> {
//        handleMessage();
//    };
//
//    private void handleMessage(){
//        Timber.e("handleMessage bufferedPosition=" + player.getBufferedPosition());
//        handler.postDelayed(runnable,1000);
//    }
//
//    Handler handler = new Handler();


    private void initPlayer() {
        DefaultHttpDataSource.Factory dataSourceFactory = new DefaultHttpDataSource.Factory();
        dataSourceFactory.setUserAgent(Util.getUserAgent(this, getPackageName()));
        Map<String, String> requestProperties = new HashMap<>();
        requestProperties.put("referer", "y1w6kj.gzstv.com");
        dataSourceFactory.setDefaultRequestProperties(requestProperties);

        DefaultMediaSourceFactory mediaSourceFactory = new DefaultMediaSourceFactory(dataSourceFactory);

        SimpleExoPlayer exoPlayer = new SimpleExoPlayer.Builder(this).setMediaSourceFactory(mediaSourceFactory).build();

        player = new FuExoPlayer(exoPlayer);
        player.setRepeatMode(Player.REPEAT_MODE_OFF);
//        handleMessage();
        player.addListener(new Player.Listener() {

            @Override
            public void onTimelineChanged(Timeline timeline, int reason) {
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
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Timber.e("onTimelineChanged reason=" + reason + ",data" + ja.toString());
            }

            @Override
            public void onIsLoadingChanged(boolean isLoading) {

                Timber.e("handleMessage isLoading=" + isLoading);
            }

            @Override
            public void onIsPlayingChanged(boolean isPlaying) {
            }

            @Override
            public void onPositionDiscontinuity(int reason) {
                Timber.e("onPositionDiscontinuity reason=" + reason);
            }

            @Override
            public void onMetadata(Metadata metadata) {
                Timber.e("onMetadata metadata=" + metadata);
            }

            @Override
            public void onMediaMetadataChanged(MediaMetadata mediaMetadata) {
                Timber.e("onMediaMetadataChanged mediaMetadata=" + mediaMetadata);
            }

            @Override
            public void onMediaItemTransition(@Nullable MediaItem mediaItem, int reason) {
                Timber.e("onMediaItemTransition mediaItem=" + mediaItem);
            }
        });

//        mediaSource = new ConcatenatingMediaSource(false,false,new ShuffleOrder.DefaultShuffleOrder(0));
//        int contentType = C.TYPE_OTHER;
//        if (media.getTag().endsWith("m3u8")) {
//            contentType = C.TYPE_HLS;
//        }
//        mediaSource.addMediaSource(MediaSourceUtil.getMediaSource(this, media.getPath(), contentType));

        List<MediaItem> mediaItemList = new ArrayList<>();
        for (Media media : dataList) {
            MediaItem mediaItem = new MediaItem.Builder()
                    .setMediaId(media.getPath())
                    .setUri(media.getPath())
                    .build();
            mediaItemList.add(mediaItem);
        }

        player.setMediaItems(mediaItemList);
        player.prepare();
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
    protected void onResume() {
        super.onResume();
        playerView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        playerView.onPause();
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
