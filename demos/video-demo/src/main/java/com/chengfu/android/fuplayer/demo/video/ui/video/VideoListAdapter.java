package com.chengfu.android.fuplayer.demo.video.ui.video;

import android.app.Activity;
import android.content.res.Configuration;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.OnScrollListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.chengfu.android.fuplayer.demo.video.R;
import com.chengfu.android.fuplayer.video.FuPlayerView;
import com.chengfu.android.fuplayer.demo.video.APP;
import com.chengfu.android.fuplayer.demo.video.bean.Video;
import com.chengfu.android.fuplayer.demo.video.player.PlayerAnalytics;
import com.chengfu.android.fuplayer.demo.video.player.FuPlayer;
import com.chengfu.android.fuplayer.demo.video.util.NetworkUtil;
import com.chengfu.android.fuplayer.ext.exo.FuExoPlayerFactory;
import com.chengfu.android.fuplayer.ext.video_plus.VideoEndedView;
import com.chengfu.android.fuplayer.ext.video_plus.VideoImageView;
import com.chengfu.android.fuplayer.ext.video_plus.VideoIdleView;
import com.chengfu.android.fuplayer.ext.video_plus.VideoBufferingView;
import com.chengfu.android.fuplayer.ext.video_plus.VideoControlView;
import com.chengfu.android.fuplayer.ext.video_plus.VideoPlayErrorView;
import com.chengfu.android.fuplayer.ext.video_plus.VideoPlayWithoutWifiView;
import com.chengfu.android.fuplayer.ext.video_plus.screen.ScreenRotationHelper;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;

import java.util.List;

public class VideoListAdapter extends RecyclerView.Adapter<VideoListAdapter.ViewHolder> implements ScreenRotationHelper.OnScreenChangedListener {

    private static final Integer PLAY_VH_PAYLOAD_ID = 1;
    private List<Video> dataList;
    private FuPlayer player;
    private int lastPlayPosition = -1;
    private ViewHolder currentPlayVH;
    private boolean autoPlayWhenItemVisible;
    private ViewGroup videoFullScreenContainer;
    private Activity activity;

    public final OnScrollListener scrollListener = new OnScrollListener() {
        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);

            if ((lastPlayPosition < 0 || lastPlayPosition > getItemCount() - 1)
                    && currentPlayVH == null) {
                return;
            }

            if (recyclerView.getLayoutManager() instanceof LinearLayoutManager) {
                LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                int firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition();
                int lastVisibleItemPosition = layoutManager.findLastVisibleItemPosition();

                if (currentPlayVH != null) {
                    int currentPlayPosition = currentPlayVH.getAdapterPosition();
                    if ((currentPlayPosition < firstVisibleItemPosition || currentPlayPosition > lastVisibleItemPosition)
                            && activity.getResources().getConfiguration().orientation != Configuration.ORIENTATION_LANDSCAPE) {
                        maybeStopPlay();
                    }
                } else if (autoPlayWhenItemVisible && (lastPlayPosition >= firstVisibleItemPosition && lastPlayPosition <= lastVisibleItemPosition)) {
                    notifyItemChanged(lastPlayPosition, PLAY_VH_PAYLOAD_ID);
                }
            }
        }
    };

    public VideoListAdapter(Activity activity) {
        this.activity = activity;
        if (activity instanceof IGetVideoContainer) {
            videoFullScreenContainer = ((IGetVideoContainer) activity).getVideoContainer();
        }
    }

    public void setData(List<Video> dataList) {
        this.dataList = dataList;
        lastPlayPosition = -1;
        notifyDataSetChanged();
    }

    public boolean isAutoPlayWhenItemVisible() {
        return autoPlayWhenItemVisible;
    }

    public void setAutoPlayWhenItemVisible(boolean autoPlayWhenItemVisible) {
        this.autoPlayWhenItemVisible = autoPlayWhenItemVisible;
    }

    public void maybeStartPlay() {
        if (currentPlayVH == null) {
            return;
        }
        currentPlayVH.startPlay();
    }


    public void onPause() {
        if (currentPlayVH == null) {
            return;
        }
        player.onPause();
    }

    public void onResume() {
        if (currentPlayVH == null) {
            return;
        }
        player.onResume();
    }

    public void maybeStopPlay() {
        if (currentPlayVH == null) {
            return;
        }
        player.stopPlay();
        currentPlayVH = null;
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);

        SimpleExoPlayer simpleExoPlayer = ExoPlayerFactory.newSimpleInstance(recyclerView.getContext());
        simpleExoPlayer.addAnalyticsListener(new PlayerAnalytics());
        player = new FuPlayer(activity,  new FuExoPlayerFactory(recyclerView.getContext()));


        ScreenRotationHelper screenRotationHelper = new ScreenRotationHelper(activity);
        screenRotationHelper.setDisableInPlayerStateEnd(true);
        screenRotationHelper.setDisableInPlayerStateError(false);
        screenRotationHelper.setToggleToPortraitInDisable(true);
        screenRotationHelper.setEnablePortraitFullScreen(true);
        screenRotationHelper.setAutoRotationMode(ScreenRotationHelper.AUTO_ROTATION_MODE_SYSTEM);

        player.setScreenRotation(screenRotationHelper);

        screenRotationHelper.setOnScreenChangedListener(this);

        recyclerView.addOnScrollListener(scrollListener);
    }

    @Override
    public void onDetachedFromRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onDetachedFromRecyclerView(recyclerView);

        player.onDestroy();

        recyclerView.removeOnScrollListener(scrollListener);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_video, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder vh, int position) {
        Video item = dataList.get(position);

        vh.title.setText(item.getName());
        Glide.with(vh.videoImageView).load(item.getImage()).into(vh.videoImageView.getImage());

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder vh, int position, @NonNull List<Object> payloads) {
        if (payloads.isEmpty()) {
            onBindViewHolder(vh, position);
        } else if (payloads.get(0) instanceof Integer) {
            Integer payloadId = (Integer) payloads.get(0);
            if (payloadId.intValue() == PLAY_VH_PAYLOAD_ID.intValue()) {
                vh.startPlay();
            }
        }
    }

    @Override
    public int getItemCount() {
        return dataList != null ? dataList.size() : 0;
    }

    public boolean onBackPressed() {
        return player.onBackPressed();
    }

    @Override
    public void onScreenChanged(boolean portraitFullScreen) {
        toggleScreen(portraitFullScreen);
    }

    public void onConfigurationChanged(Configuration newConfig) {
        if (currentPlayVH == null) {
            return;
        }
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            toggleScreen(true);
        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
            toggleScreen(false);
        }
    }

    public void toggleScreen(boolean fullScreen) {
        if (fullScreen) {
            currentPlayVH.videoRoot.clearAnimation();
            currentPlayVH.videoContainer.removeView(currentPlayVH.videoRoot);
            if (currentPlayVH.videoRoot.getParent() == null) {
                videoFullScreenContainer.addView(currentPlayVH.videoRoot);
            }
            currentPlayVH.bufferingView.setFullScreen(true);
            currentPlayVH.controlView.setFullScreen(true);
            currentPlayVH.controlView.setEnableGestureType(VideoControlView.Gesture.SHOW_TYPE_BRIGHTNESS | VideoControlView.Gesture.SHOW_TYPE_PROGRESS | VideoControlView.Gesture.SHOW_TYPE_VOLUME);


            activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        } else {
            currentPlayVH.videoRoot.clearAnimation();
            videoFullScreenContainer.removeView(currentPlayVH.videoRoot);
            if (currentPlayVH.videoRoot.getParent() == null) {
                currentPlayVH.videoContainer.addView(currentPlayVH.videoRoot);
            }
            currentPlayVH.bufferingView.setFullScreen(false);
            currentPlayVH.controlView.setFullScreen(false);
            currentPlayVH.controlView.setEnableGestureType(VideoControlView.Gesture.SHOW_TYPE_NONE);


            if (activity != null) {
                activity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

                WindowManager.LayoutParams lp = activity.getWindow().getAttributes();
                lp.screenBrightness = WindowManager.LayoutParams.BRIGHTNESS_OVERRIDE_NONE;
                activity.getWindow().setAttributes(lp);
            }
        }
    }


    class ViewHolder extends RecyclerView.ViewHolder {
        FrameLayout videoContainer;
        FrameLayout videoRoot;
        FuPlayerView playerView;
        VideoControlView controlView;
        TextView title;
        VideoBufferingView bufferingView;
        VideoPlayErrorView errorView;
        VideoIdleView playView;
        VideoImageView videoImageView;
        VideoPlayWithoutWifiView noWifiView;
        VideoEndedView endedView;


        ViewHolder(@NonNull View itemView) {
            super(itemView);

            videoContainer = itemView.findViewById(R.id.videoContainer);
            videoRoot = itemView.findViewById(R.id.videoRoot);
            playerView = itemView.findViewById(R.id.playerView);
            controlView = itemView.findViewById(R.id.controlView);
            title = itemView.findViewById(R.id.title);

            bufferingView = itemView.findViewById(R.id.bufferingView);
            errorView = itemView.findViewById(R.id.errorView);
            playView = itemView.findViewById(R.id.playView);
            videoImageView = itemView.findViewById(R.id.videoImageView);
            noWifiView = itemView.findViewById(R.id.noWifiView);
            endedView = itemView.findViewById(R.id.endedView);

            noWifiView.addVisibilityChangeListener((stateView, visibility) -> {
                playView.setDisable(visibility);
            });

            controlView.setShowAlwaysInPaused(true);
            controlView.setShowBottomProgress(true);
            controlView.setShowTopOnlyFullScreen(true);
            controlView.setHideInBuffering(true);
            controlView.setHideInEnded(true);
            controlView.setHideInError(true);
            controlView.setShowAlwaysInPaused(true);

            playView.setOnPlayerClickListener(v -> {
                maybeStopPlay();
                startPlay();
            });

        }

        boolean canPlay() {
            if (!NetworkUtil.isConnected(APP.application)) {
                Toast.makeText(APP.application, "网络不可用", Toast.LENGTH_SHORT).show();
                return false;
            }
            return true;
        }

        void startPlay() {
            currentPlayVH = this;
            lastPlayPosition = getAdapterPosition();

            if (!canPlay()) {
                return;
            }

            player.setPlayerView(playerView);
            player.setVideoControlView(controlView);

            player.clearStateViews();

            player.addStateView(videoImageView, true);
            player.addStateView(bufferingView, true);
            player.addStateView(playView, true);
            player.addStateView(errorView, true);
            player.addStateView(endedView, true);
            player.addStateView(noWifiView, true);

            player.prepare(dataList.get(getAdapterPosition()).getMediaSource());
        }
    }
}
