package com.chengfu.fuexoplayer2.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.GestureDetectorCompat;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.chengfu.fuexoplayer2.FuLog;
import com.chengfu.fuexoplayer2.R;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.audio.AudioAttributes;
import com.google.android.exoplayer2.audio.AudioListener;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.video.VideoListener;

import java.util.Formatter;
import java.util.Locale;

public class DefaultControlView extends BaseControlView implements View.OnClickListener {

    public static final String TAG = "DefaultControlView";
    /**
     * The default fast forward increment, in milliseconds.
     */
    public static final int DEFAULT_FAST_FORWARD_MS = 15000;
    /**
     * The default rewind increment, in milliseconds.
     */
    public static final int DEFAULT_REWIND_MS = 5000;
    /**
     * The default show timeout, in milliseconds.
     */
    public static final int DEFAULT_SHOW_TIMEOUT_MS = 5000;
    /**
     * The default progress updata interval, in milliseconds.
     */
    public static final int DEFAULT_PROGRESS_UPDATE_INTERVAL_MS = 1000;
    /**
     * The default seek number.
     */
    public static final int DEFAULT_SEEK_NUMBER = 1000;

    protected final Context mContext;
    protected Player mPlayer;
    protected final ComponentListener mComponentListener;

    protected GestureDetectorCompat mGestureDetector;
    protected final ControlGestureListener mGestureListener;

    protected View mContainerView;
    protected ImageButton mRepeatSwitchView;
    protected ImageButton mFastRewindView;
    protected ImageButton mPlayPauseSwitchView;
    protected ImageButton mFastForwardView;
    protected ImageButton mVolumeSwitchView;
    protected ImageButton mShuffleSwitchView;
    protected TextView mDurationView;
    protected SeekBar mSeekView;
    protected TextView mPositionView;

    protected int mRewindMs;
    protected int mFastForwardMs;
    protected int mShowTimeoutMs;
    protected int mProgressUpdateIntervalMs;
    protected int mSeekNumber;
    protected boolean mShowInBuffering;
    protected boolean mShowInEnded;
    protected boolean mHideRepeatSwitch;
    protected boolean mHideVolumeSwitch;
    protected boolean mHideShuffleSwitch;

    protected boolean mAttachedToWindow;
    protected boolean mShowing;
    protected long mHideAtMs;
    protected boolean mTracking;

    protected StringBuilder mFormatBuilder;
    protected Formatter mFormatter;

    protected boolean mSeekable = false;
    protected boolean mDynamic = false;
    protected float mVolume = 1.0f;

    private final Runnable mHideAction = new Runnable() {
        @Override
        public void run() {
            hide();
        }
    };

    private final Runnable mUpdateProgressAction = new Runnable() {
        @Override
        public void run() {
            updateProgress();
        }
    };

    public DefaultControlView(Context context) {
        this(context, null);
    }

    public DefaultControlView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DefaultControlView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        mContext = context;
        mComponentListener = new ComponentListener();

        int controllerLayoutId = R.layout.default_controller_view;
        mRewindMs = DEFAULT_REWIND_MS;
        mFastForwardMs = DEFAULT_FAST_FORWARD_MS;
        mShowTimeoutMs = DEFAULT_SHOW_TIMEOUT_MS;
        mProgressUpdateIntervalMs = DEFAULT_PROGRESS_UPDATE_INTERVAL_MS;
        mSeekNumber = DEFAULT_SEEK_NUMBER;
        mShowInBuffering = false;
        mShowInEnded = false;
        mHideRepeatSwitch = false;
        mHideVolumeSwitch = false;
        mHideShuffleSwitch = false;

        if (attrs != null) {
            TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.PlayerControlView, 0, 0);
            try {
                controllerLayoutId =
                        a.getResourceId(R.styleable.PlayerControlView_controller_layout_id, controllerLayoutId);
                mRewindMs = a.getInt(R.styleable.PlayerControlView_rewind_increment, DEFAULT_REWIND_MS);
                mFastForwardMs =
                        a.getInt(R.styleable.PlayerControlView_fast_forward_increment, DEFAULT_FAST_FORWARD_MS);
                mShowTimeoutMs = a.getInt(R.styleable.PlayerControlView_show_timeout, DEFAULT_SHOW_TIMEOUT_MS);
                mProgressUpdateIntervalMs = a.getInt(R.styleable.PlayerControlView_progress_update_interval, DEFAULT_PROGRESS_UPDATE_INTERVAL_MS);
                mSeekNumber = a.getInt(R.styleable.PlayerControlView_seek_number, DEFAULT_SEEK_NUMBER);
                mShowInBuffering = a.getBoolean(R.styleable.PlayerControlView_show_in_buffering, mShowInBuffering);
                mShowInEnded = a.getBoolean(R.styleable.PlayerControlView_show_in_ended, mShowInEnded);
                mHideRepeatSwitch = a.getBoolean(R.styleable.PlayerControlView_hide_repeat_switch, mHideRepeatSwitch);
                mHideVolumeSwitch = a.getBoolean(R.styleable.PlayerControlView_hide_volume_switch, mHideVolumeSwitch);
                mHideShuffleSwitch = a.getBoolean(R.styleable.PlayerControlView_hide_volume_switch, mHideShuffleSwitch);
            } finally {
                a.recycle();
            }
        }

        LayoutInflater.from(context).inflate(getLayoutResourcesId(controllerLayoutId), this);
        setDescendantFocusability(FOCUS_AFTER_DESCENDANTS);

        mFormatBuilder = new StringBuilder();
        mFormatter = new Formatter(mFormatBuilder, Locale.getDefault());

        mGestureListener = new ControlGestureListener();
        mGestureDetector = new GestureDetectorCompat(mContext, mGestureListener);

        initView(context, attrs, defStyleAttr);

    }


    protected int getLayoutResourcesId(int layoutId) {
        return layoutId;
    }

    protected void initView(Context context, AttributeSet attrs, int defStyleAttr) {

        mContainerView = findViewById(R.id.controller_container);
        if (mContainerView != null) {
            mContainerView.setVisibility(View.GONE);
        }
        mDurationView = findViewById(R.id.controller_duration);
        mPositionView = findViewById(R.id.controller_position);
        mSeekView = findViewById(R.id.controller_seek);
        if (mSeekView != null) {
            mSeekView.setMax(mSeekNumber);
            mSeekView.setOnSeekBarChangeListener(mSeekListener);
        }
        mPlayPauseSwitchView = findViewById(R.id.controller_play_pause_switch);
        if (mPlayPauseSwitchView != null) {
            mPlayPauseSwitchView.setOnClickListener(this);
        }
        mFastRewindView = findViewById(R.id.controller_rewind);
        if (mFastRewindView != null) {
            mFastRewindView.setOnClickListener(this);
        }
        mFastForwardView = findViewById(R.id.controller_fast_forward);
        if (mFastForwardView != null) {
            mFastForwardView.setOnClickListener(this);
        }
        mRepeatSwitchView = findViewById(R.id.controller_repeat_switch);
        if (mRepeatSwitchView != null) {
            mRepeatSwitchView.setOnClickListener(this);
        }
        mVolumeSwitchView = findViewById(R.id.controller_volume_switch);
        if (mVolumeSwitchView != null) {
            mVolumeSwitchView.setOnClickListener(this);
        }
        mShuffleSwitchView = findViewById(R.id.controller_shuffle_switch);
        if (mShuffleSwitchView != null) {
            mShuffleSwitchView.setOnClickListener(this);
        }
    }

    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        mAttachedToWindow = true;
        if (mHideAtMs != -1) {
            long delayMs = mHideAtMs - SystemClock.uptimeMillis();
            if (delayMs <= 0) {
                hide();
            } else {
                postDelayed(mHideAction, delayMs);
            }
        } else if (isShowing()) {
            hideAfterTimeout();
        }
        updateAll();
    }

    @Override
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        mAttachedToWindow = false;
        removeCallbacks(mUpdateProgressAction);
        removeCallbacks(mHideAction);
    }

    public boolean isHideRepeatSwitch() {
        return mHideRepeatSwitch;
    }

    public void setHideRepeatSwitch(boolean hide) {
        mHideRepeatSwitch = hide;
        updateRepeatView();
    }

    public boolean isHideVolumeSwitch() {
        return mHideVolumeSwitch;
    }

    public void setHideVolumeSwitch(boolean hide) {
        mHideVolumeSwitch = hide;
        updateVolumeView();
    }

    public boolean isHideShuffleSwitch() {
        return mHideShuffleSwitch;
    }

    public void setHideShuffleSwitch(boolean hide) {
        mHideShuffleSwitch = hide;
        updateVolumeView();
    }

    @Override
    public Player getPlayer() {
        return mPlayer;
    }

    @Override
    public void setPlayer(Player player) {
        if (mPlayer == player) {
            return;
        }
        if (mPlayer != null) {
            mPlayer.removeListener(mComponentListener);
            if (mPlayer.getVideoComponent() != null) {
                mPlayer.getVideoComponent().removeVideoListener(mComponentListener);
            }
            if (mPlayer.getAudioComponent() != null) {
                mPlayer.getAudioComponent().removeAudioListener(mComponentListener);
            }
        }
        mSeekable = false;
        mDynamic = false;
        mVolume = 1.0f;
        mPlayer = player;
        if (player != null) {
            player.addListener(mComponentListener);
            if (player.getVideoComponent() != null) {
                player.getVideoComponent().addVideoListener(mComponentListener);
            }
            if (player.getAudioComponent() != null) {
                player.getAudioComponent().addAudioListener(mComponentListener);
            }
        }
        updateAll();
    }

    protected void updateAll() {
        if (!isInShowState()) {
            hide();
            return;
        }
        updatePlayPauseView();
        updateRepeatView();
        updateSeekView();
        updateProgress();
        updateVolumeView();
        updateShuffleView();
    }

    private void updatePlayPauseView() {

        if (!isShowing() || !mAttachedToWindow || mPlayPauseSwitchView == null) {
            return;
        }
        if (mPlayer == null) {
            setViewEnabled(false, mPlayPauseSwitchView);
            return;
        }
        setViewEnabled(true, mPlayPauseSwitchView);

        updatePlayPauseViewResource(mPlayPauseSwitchView, mPlayer.getPlayWhenReady());
    }

    protected void updatePlayPauseViewResource(@NonNull ImageButton imageButton, boolean playWhenReady) {
        if (playWhenReady) {
            imageButton.setImageResource(R.drawable.ic_pause_white_24dp);
        } else {
            imageButton.setImageResource(R.drawable.ic_play_white_24dp);
        }
    }

    private void updateSeekView() {
        if (!isShowing() || !mAttachedToWindow) {
            return;
        }
        boolean canSeekable = mPlayer != null && this.mSeekable;
        setViewEnabled(mFastForwardMs > 0 && mSeekable, mFastForwardView);
        setViewEnabled(mRewindMs > 0 && mSeekable, mFastRewindView);
        if (mSeekView != null) {
            mSeekView.setEnabled(canSeekable);
        }
    }

    private void updateRepeatView() {
        if (!isShowing() || !mAttachedToWindow || mRepeatSwitchView == null) {
            return;
        }
        if (mHideRepeatSwitch) {
            mRepeatSwitchView.setVisibility(View.GONE);
            return;
        }
        if (mPlayer == null) {
            setViewEnabled(false, mRepeatSwitchView);
            return;
        }
        setViewEnabled(true, mRepeatSwitchView);

        mRepeatSwitchView.setVisibility(View.VISIBLE);

        updateRepeatViewResource(mRepeatSwitchView, mPlayer.getRepeatMode());
    }

    protected void updateRepeatViewResource(@NonNull ImageButton imageButton, int repeatMode) {
        if (imageButton == null) {
            return;
        }
        switch (repeatMode) {
            case Player.REPEAT_MODE_OFF:
                imageButton.setImageResource(R.drawable.default_controls_repeat_off);
                imageButton.setContentDescription("");
                break;
            case Player.REPEAT_MODE_ONE:
                imageButton.setImageResource(R.drawable.default_controls_repeat_one);
                imageButton.setContentDescription("");
                break;
            case Player.REPEAT_MODE_ALL:
                imageButton.setImageResource(R.drawable.default_controls_repeat_all);
                imageButton.setContentDescription("");
                break;
            default:
                imageButton.setImageResource(R.drawable.default_controls_repeat_off);
                imageButton.setContentDescription("");
        }
    }

    private void updateVolumeView() {
        if (!isShowing() || !mAttachedToWindow || mVolumeSwitchView == null) {
            return;
        }
        if (mHideVolumeSwitch) {
            mVolumeSwitchView.setVisibility(View.GONE);
            return;
        }
        if (mPlayer == null || mPlayer.getAudioComponent() == null) {
            setViewEnabled(false, mVolumeSwitchView);
            return;
        }
        setViewEnabled(true, mVolumeSwitchView);

        mVolumeSwitchView.setVisibility(View.VISIBLE);

        updateVolumeViewResource(mVolumeSwitchView, mPlayer.getAudioComponent().getVolume());
    }

    protected void updateVolumeViewResource(@NonNull ImageButton imageButton, float volume) {
        if (volume > 0.0f) {
            imageButton.setImageResource(R.drawable.ic_volume_up_white_24dp);
        } else {
            imageButton.setImageResource(R.drawable.ic_volume_off_white_24dp);
        }
    }

    private void updateShuffleView() {
        if (!isShowing() || !mAttachedToWindow || mShuffleSwitchView == null) {
            return;
        }
        if (mHideShuffleSwitch) {
            mShuffleSwitchView.setVisibility(View.GONE);
            return;
        }
        if (mPlayer == null) {
            setViewEnabled(false, mShuffleSwitchView);
            return;
        }
        setViewEnabled(true, mShuffleSwitchView);

        mShuffleSwitchView.setVisibility(View.VISIBLE);

        updateShuffleViewResource(mShuffleSwitchView, mPlayer.getShuffleModeEnabled());
    }

    protected void updateShuffleViewResource(ImageButton imageButton, boolean ended) {
        imageButton.setAlpha(ended ? 1f : 0.3f);
        imageButton.setEnabled(true);
        imageButton.setVisibility(View.VISIBLE);
    }

    private void updateProgress() {
        if (!mAttachedToWindow || mPlayer == null) {
            return;
        }

        long position = mPlayer.getCurrentPosition();
        long duration = mPlayer.getDuration();
        int bufferedPercent = mPlayer.getBufferedPercentage();

        if (!onProgressUpdate(position, duration, bufferedPercent) && !isShowing()) {
            return;
        }

        if (mSeekView != null) {
            if (duration > 0 && !mTracking) {
                // use long to avoid overflow
                long pos = mSeekNumber * position / duration;
                mSeekView.setProgress((int) pos);
            }
            mSeekView.setSecondaryProgress(bufferedPercent * 10);
        }

        if (mDurationView != null)
            mDurationView.setText(stringForTime(duration));
        if (mPositionView != null && !mTracking)
            mPositionView.setText(stringForTime(position));

        removeCallbacks(mUpdateProgressAction);
        postDelayed(mUpdateProgressAction, mProgressUpdateIntervalMs);
    }

    protected boolean onProgressUpdate(long position, long duration, int bufferedPercent) {
        return false;
    }


    protected void setViewEnabled(boolean enabled, View view) {
        if (view == null) {
            return;
        }
        view.setEnabled(enabled);
        view.setAlpha(enabled ? 1f : 0.3f);
        view.setVisibility(VISIBLE);
    }

    @Override
    public int getShowTimeoutMs() {
        return mShowTimeoutMs;
    }

    @Override
    public void setShowTimeoutMs(int showTimeoutMs) {

    }

    @Override
    public boolean isShowing() {
        return mShowing;
    }

    @Override
    public void show() {
        if (!isInShowState()) {
            return;
        }
        if (!mShowing) {
            mContainerView.setVisibility(View.VISIBLE);
            mShowing = true;
            updateAll();

            onHideChanged(false);
        }

        hideAfterTimeout();
    }

    @Override
    public void hide() {
        if (mShowing) {
            mShowing = false;
            mContainerView.setVisibility(View.GONE);
            removeCallbacks(mUpdateProgressAction);
            removeCallbacks(mHideAction);
            mHideAtMs = -1;

            onHideChanged(true);
        }
    }

    protected void onHideChanged(boolean hide) {

    }

    protected boolean isInShowState() {
        if (mPlayer == null || !mAttachedToWindow) {
            return false;
        }
        switch (mPlayer.getPlaybackState()) {
            case Player.STATE_BUFFERING:
                if (mShowInBuffering) {
                    return true;
                } else {
                    return false;
                }
            case Player.STATE_ENDED:
                if (mShowInEnded) {
                    return true;
                } else {
                    return false;
                }
            case Player.STATE_READY:
                return true;
            default:
                return false;
        }
    }

    private void hideAfterTimeout() {
        removeCallbacks(mHideAction);
        if (mShowTimeoutMs > 0) {
            mHideAtMs = SystemClock.uptimeMillis() + mShowTimeoutMs;
            if (mAttachedToWindow) {
                postDelayed(mHideAction, mShowTimeoutMs);
            }
        } else {
            mHideAtMs = -1;
        }
    }

    private String stringForTime(long timeMs) {
        if (timeMs <= 0) {
            timeMs = 0;
        }

        long totalSeconds = (timeMs + 500) / 1000;
        long seconds = totalSeconds % 60;
        long minutes = (totalSeconds / 60) % 60;
        long hours = totalSeconds / 3600;

        mFormatBuilder.setLength(0);
        if (hours > 0) {
            return mFormatter.format("%d:%02d:%02d", hours, minutes, seconds).toString();
        } else {
            return mFormatter.format("%02d:%02d", minutes, seconds).toString();
        }
    }

    protected boolean isPlaying() {
        return mPlayer != null
                && mPlayer.getPlaybackState() != Player.STATE_ENDED
                && mPlayer.getPlaybackState() != Player.STATE_IDLE
                && mPlayer.getPlayWhenReady();
    }

    protected void setPlayWhenReady(boolean playWhenReady) {
        if (mPlayer == null || mPlayer.getPlayWhenReady() == playWhenReady) {
            return;
        }
        togglePlayWhenReady();
    }

    protected void togglePlayWhenReady() {
        if (mPlayer == null) {
            return;
        }
        if (!mPlayer.getPlayWhenReady()) {
            if (mPlayer.getPlaybackState() == Player.STATE_ENDED) {
                mPlayer.seekTo(0);
            }
            mPlayer.setPlayWhenReady(true);
        } else {
            mPlayer.setPlayWhenReady(false);
        }
    }

    protected void seekTo(long positionMs) {
        if (positionMs <= 0 || mPlayer == null) {
            return;
        }
        mPlayer.seekTo(positionMs);
    }

    protected void rewind() {
        if (mRewindMs <= 0 || mPlayer == null) {
            return;
        }
        if (mPlayer.getCurrentPosition() > 0) {
            seekTo(Math.max(mPlayer.getCurrentPosition() - mRewindMs, 0));
        }
    }

    protected void fastForward() {
        if (mFastForwardMs <= 0 || mPlayer == null) {
            return;
        }
        long durationMs = mPlayer.getDuration();
        long currentPosition = mPlayer.getCurrentPosition();
        long seekPositionMs = currentPosition + mFastForwardMs;
        if (durationMs > 0) {
            seekPositionMs = Math.min(seekPositionMs, durationMs);
        }
        if (currentPosition < durationMs) {
            seekTo(seekPositionMs);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        mGestureDetector.onTouchEvent(event);
        if (isInShowState()) {
            return true;
        }
        return false;
    }

    @Override
    public boolean onTrackballEvent(MotionEvent event) {
        mGestureDetector.onTouchEvent(event);
        if (isInShowState()) {
            return true;
        }
        return false;
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        return dispatchMediaKeyEvent(event) || super.dispatchKeyEvent(event);
    }

    public boolean dispatchMediaKeyEvent(KeyEvent event) {
        int keyCode = event.getKeyCode();
        if (mPlayer == null || !isHandledMediaKey(keyCode)) {
            return false;
        }
        if (event.getAction() == KeyEvent.ACTION_DOWN) {
            if (keyCode == KeyEvent.KEYCODE_MEDIA_FAST_FORWARD) {
                fastForward();
            } else if (keyCode == KeyEvent.KEYCODE_MEDIA_REWIND) {
                rewind();
            } else if (event.getRepeatCount() == 0) {
                switch (keyCode) {
                    case KeyEvent.KEYCODE_MEDIA_PLAY_PAUSE:
                        togglePlayWhenReady();
                        break;
                    case KeyEvent.KEYCODE_MEDIA_PLAY:
                        setPlayWhenReady(true);
                        break;
                    case KeyEvent.KEYCODE_MEDIA_PAUSE:
                        setPlayWhenReady(false);
                        break;
                    default:
                        break;
                }
            }
        }
        return true;
    }

    @SuppressLint("InlinedApi")
    private static boolean isHandledMediaKey(int keyCode) {
        return keyCode == KeyEvent.KEYCODE_MEDIA_FAST_FORWARD
                || keyCode == KeyEvent.KEYCODE_MEDIA_REWIND
                || keyCode == KeyEvent.KEYCODE_MEDIA_PLAY_PAUSE
                || keyCode == KeyEvent.KEYCODE_MEDIA_PLAY
                || keyCode == KeyEvent.KEYCODE_MEDIA_PAUSE
                || keyCode == KeyEvent.KEYCODE_MEDIA_NEXT
                || keyCode == KeyEvent.KEYCODE_MEDIA_PREVIOUS;
    }

    @Override
    public void onClick(View v) {
        if (mPlayer == null) {
            return;
        }
        if (mPlayPauseSwitchView == v) {
            togglePlayWhenReady();
        } else if (mFastRewindView == v) {
            rewind();
        } else if (mFastForwardView == v) {
            fastForward();
        } else if (mRepeatSwitchView == v) {
            if (mPlayer == null) {
                return;
            }
            switch (mPlayer.getRepeatMode()) {
                case Player.REPEAT_MODE_OFF:
                    mPlayer.setRepeatMode(Player.REPEAT_MODE_ONE);
                    break;
                case Player.REPEAT_MODE_ONE:
                    mPlayer.setRepeatMode(Player.REPEAT_MODE_ALL);
                    break;
                case Player.REPEAT_MODE_ALL:
                    mPlayer.setRepeatMode(Player.REPEAT_MODE_OFF);
                    break;
            }
        } else if (mVolumeSwitchView == v) {
            if (mPlayer == null || mPlayer.getAudioComponent() == null) {
                return;
            }
            if (mPlayer.getAudioComponent().getVolume() > 0) {
                mPlayer.getAudioComponent().setVolume(0.0f);
            } else {
                mPlayer.getAudioComponent().setVolume(1.0f);
            }
        } else if (mShuffleSwitchView == v) {
            if (mPlayer == null) {
                return;
            }
            if (mPlayer.getShuffleModeEnabled()) {
                mPlayer.setShuffleModeEnabled(false);
            } else {
                mPlayer.setShuffleModeEnabled(true);
            }
        }
        hideAfterTimeout();
    }

    private final SeekBar.OnSeekBarChangeListener mSeekListener = new SeekBar.OnSeekBarChangeListener() {

        private int progress;

        @Override
        public void onStartTrackingTouch(SeekBar bar) {
            removeCallbacks(mHideAction);
            mTracking = true;
        }

        @Override
        public void onProgressChanged(SeekBar bar, int progress, boolean fromuser) {
            if (!fromuser) {
                return;
            }
            this.progress = progress;
            if (!isInShowState()) {
                return;
            }
            long duration = mPlayer.getDuration();
            long newPosition = (duration * progress) / mSeekNumber;
            if (mPositionView != null) {
                mPositionView.setText(stringForTime(newPosition));
            }
        }

        @Override
        public void onStopTrackingTouch(SeekBar bar) {
            mTracking = false;
            if (!isInShowState()) {
                return;
            }
            long duration = mPlayer.getDuration();
            long newPosition = duration * progress / mSeekNumber;
            seekTo(newPosition);

            hideAfterTimeout();
        }
    };

    private final class ComponentListener implements Player.EventListener, VideoListener, AudioListener {

        @Override
        public void onTimelineChanged(Timeline timeline, @Nullable Object manifest, int reason) {
            FuLog.d(TAG, "onTimelineChanged : timeline=" + timeline + ",manifest=" + manifest + ",reason=" + reason);
            boolean haveNonEmptyTimeline = timeline != null && !timeline.isEmpty();
            if (haveNonEmptyTimeline && !mPlayer.isPlayingAd()) {
                int windowIndex = mPlayer.getCurrentWindowIndex();
                Timeline.Window window = new Timeline.Window();
                timeline.getWindow(windowIndex, window);
                mSeekable = window.isSeekable;
                mDynamic = window.isDynamic;
            }
            updateSeekView();
        }

        @Override
        public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {
            FuLog.d(TAG, "onTracksChanged : trackGroups=" + trackGroups + ",trackSelections=" + trackSelections);
        }

        @Override
        public void onLoadingChanged(boolean isLoading) {
            FuLog.d(TAG, "onLoadingChanged : isLoading=" + isLoading);
        }


        @Override
        public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
            FuLog.d(TAG, "onPlayerStateChanged : playWhenReady=" + playWhenReady + ",playbackState=" + playbackState);
            updateAll();
        }

        @Override
        public void onRepeatModeChanged(int repeatMode) {
            FuLog.d(TAG, "onRepeatModeChanged : repeatMode=" + repeatMode);
            updateRepeatView();
        }

        @Override
        public void onShuffleModeEnabledChanged(boolean shuffleModeEnabled) {
            FuLog.d(TAG, "onShuffleModeEnabledChanged : shuffleModeEnabled=" + shuffleModeEnabled);
            updateShuffleView();
        }

        @Override
        public void onPlayerError(ExoPlaybackException error) {
            FuLog.d(TAG, "onPlayerError : error=" + error);
        }

        @Override
        public void onPositionDiscontinuity(int reason) {
            FuLog.d(TAG, "onPositionDiscontinuity : reason=" + reason);
        }

        @Override
        public void onPlaybackParametersChanged(PlaybackParameters playbackParameters) {
            FuLog.d(TAG, "onPlaybackParametersChanged : playbackParameters=" + playbackParameters);
        }

        @Override
        public void onSeekProcessed() {
            FuLog.d(TAG, "onSeekProcessed : ");
        }

        @Override
        public void onVideoSizeChanged(int width, int height, int unappliedRotationDegrees, float pixelWidthHeightRatio) {
            FuLog.d(TAG, "onVideoSizeChanged : width=" + width + ",height=" + height + ",unappliedRotationDegrees=" + unappliedRotationDegrees + ",pixelWidthHeightRatio=" + pixelWidthHeightRatio);
        }

        @Override
        public void onSurfaceSizeChanged(int width, int height) {
            FuLog.d(TAG, "onSurfaceSizeChanged : width=" + width + ",height=" + height);
        }

        @Override
        public void onRenderedFirstFrame() {
            FuLog.d(TAG, "onRenderedFirstFrame : ");
        }

        @Override
        public void onAudioSessionId(int audioSessionId) {
            FuLog.d(TAG, "onAudioSessionId : audioSessionId=" + audioSessionId);
        }

        @Override
        public void onAudioAttributesChanged(AudioAttributes audioAttributes) {
            FuLog.d(TAG, "onAudioAttributesChanged : audioAttributes=" + audioAttributes);
        }

        @Override
        public void onVolumeChanged(float volume) {
            FuLog.d(TAG, "onVolumeChanged : volume=" + volume);
            mVolume = volume;
            updateVolumeView();
        }
    }

    private final class ControlGestureListener extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onSingleTapUp(MotionEvent e) {
            if (isShowing()) {
                hide();
            } else {
                show();
            }
            return super.onSingleTapUp(e);
        }
    }

}
