package com.chengfu.android.fuplayer.ui;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.SystemClock;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;

import com.chengfu.android.fuplayer.FuPlayer;
import com.chengfu.android.fuplayer.util.FuLog;
import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.audio.AudioListener;
import com.google.android.exoplayer2.util.RepeatModeUtil;
import com.google.android.exoplayer2.video.VideoListener;

public class DefaultControlView extends BaseControlView {

    private static final String TAG = "DefaultControlView";
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
     * The default progress update interval, in milliseconds.
     */
    public static final int DEFAULT_PROGRESS_UPDATE_INTERVAL_MS = 1000;
    /**
     * The default seek number.
     */
    public static final int DEFAULT_SEEK_NUMBER = 1000;

    public static final long MAX_POSITION_FOR_SEEK_TO_PREVIOUS = 3000;

    /**
     * The default repeat toggle modes.
     */
    public static final @RepeatModeUtil.RepeatToggleModes
    int DEFAULT_REPEAT_TOGGLE_MODES = RepeatModeUtil.REPEAT_TOGGLE_MODE_ALL;

    protected final Context mContext;
    protected FuPlayer mPlayer;
    protected final ComponentListener mComponentListener;
    protected final Timeline.Window mWindow;
    protected ProgressAdapter mProgressAdapter;

    protected View mContainerView;
    protected ImageButton mRepeatSwitchView;
    protected ImageButton mFastRewindView;
    protected ImageButton mPlayPauseSwitchView;
    protected ImageButton mFastForwardView;
    protected ImageButton mVolumeSwitchView;
    protected ImageButton mShuffleSwitchView;
    protected ImageButton mSkipPrevious;
    protected ImageButton mSkipNext;
    protected TextView mDurationView;
    protected SeekBar mSeekView;
    protected TextView mPositionView;

    protected int mRewindMs;
    protected int mFastForwardMs;
    protected int mShowTimeoutMs;
    protected int mProgressUpdateIntervalMs;
    protected int mSeekNumber;
    protected boolean mHideInBuffering;
    protected boolean mHideInEnded;
    protected boolean mHideInError;
    protected boolean mHideRepeatSwitch;
    protected boolean mHideVolumeSwitch;
    protected boolean mHideShuffleSwitch;
    protected boolean mShowAlwaysInPaused;

    protected boolean mAttachedToWindow;
    protected boolean mShowing;
    protected long mHideAtMs;
    protected boolean mTracking;
    private int mCurrentSeekProgress;

    protected @RepeatModeUtil.RepeatToggleModes
    int mRepeatToggleModes;

    private final Runnable mHideAction = () -> {
        if (!mShowAlwaysInPaused || !isInShowAlwaysInPausedState()) {
            hide();
        }
    };

    private final Runnable mUpdateProgressAction = () -> updateProgress();

    public DefaultControlView(Context context) {
        this(context, null);
    }

    public DefaultControlView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DefaultControlView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        mContext = context;

        mComponentListener = initComponentListener();
        mWindow = new Timeline.Window();

        int controllerLayoutId = R.layout.default_controller_view;
        mRewindMs = DEFAULT_REWIND_MS;
        mFastForwardMs = DEFAULT_FAST_FORWARD_MS;
        mShowTimeoutMs = DEFAULT_SHOW_TIMEOUT_MS;
        mProgressUpdateIntervalMs = DEFAULT_PROGRESS_UPDATE_INTERVAL_MS;
        mSeekNumber = DEFAULT_SEEK_NUMBER;
        mRepeatToggleModes = DEFAULT_REPEAT_TOGGLE_MODES;
        mHideInBuffering = false;
        mHideInEnded = false;
        mHideInError = false;
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
                mHideInBuffering = a.getBoolean(R.styleable.PlayerControlView_hide_in_buffering, mHideInBuffering);
                mHideInEnded = a.getBoolean(R.styleable.PlayerControlView_hide_in_ended, mHideInEnded);
                mHideInError = a.getBoolean(R.styleable.PlayerControlView_hide_in_ended, mHideInError);
                mHideRepeatSwitch = a.getBoolean(R.styleable.PlayerControlView_hide_repeat_switch, mHideRepeatSwitch);
                mHideVolumeSwitch = a.getBoolean(R.styleable.PlayerControlView_hide_volume_switch, mHideVolumeSwitch);
                mHideShuffleSwitch = a.getBoolean(R.styleable.PlayerControlView_hide_volume_switch, mHideShuffleSwitch);
            } finally {
                a.recycle();
            }
        }

        LayoutInflater.from(context).inflate(getLayoutResourcesId(controllerLayoutId), this);
        setDescendantFocusability(FOCUS_AFTER_DESCENDANTS);

        initView(context, attrs, defStyleAttr);

        setProgressAdapter(new DefaultProgressAdapter());

    }

    protected ComponentListener initComponentListener() {
        return new ComponentListener();
    }

    protected int getLayoutResourcesId(int layoutId) {
        return layoutId;
    }

    @SuppressLint("ClickableViewAccessibility")
    protected void initView(Context context, AttributeSet attrs, int defStyleAttr) {
        mContainerView = findViewById(R.id.fu_controller_container);
        if (mContainerView != null) {
            mContainerView.setVisibility(View.GONE);
        }
        mDurationView = findViewById(R.id.fu_controller_duration);
        mPositionView = findViewById(R.id.fu_controller_position);
        mSeekView = findViewById(R.id.fu_controller_seek);
        if (mSeekView != null) {
            mSeekView.setMax(mSeekNumber);
            mSeekView.setOnSeekBarChangeListener(mComponentListener);

            mSeekView.setOnTouchListener((v, event) -> {
                if (mProgressAdapter != null) {
                    return !mProgressAdapter.isCurrentWindowSeekable();
                }
                return false;
            });
        }
        mPlayPauseSwitchView = findViewById(R.id.fu_controller_play_pause_switch);
        if (mPlayPauseSwitchView != null) {
            mPlayPauseSwitchView.setOnClickListener(mComponentListener);
        }
        mFastRewindView = findViewById(R.id.fu_controller_fast_rewind);
        if (mFastRewindView != null) {
            mFastRewindView.setOnClickListener(mComponentListener);
        }
        mFastForwardView = findViewById(R.id.fu_controller_fast_forward);
        if (mFastForwardView != null) {
            mFastForwardView.setOnClickListener(mComponentListener);
        }
        mRepeatSwitchView = findViewById(R.id.fu_controller_repeat_switch);
        if (mRepeatSwitchView != null) {
            mRepeatSwitchView.setOnClickListener(mComponentListener);
        }
        mVolumeSwitchView = findViewById(R.id.fu_controller_volume_switch);
        if (mVolumeSwitchView != null) {
            mVolumeSwitchView.setOnClickListener(mComponentListener);
        }
        mShuffleSwitchView = findViewById(R.id.fu_controller_shuffle_switch);
        if (mShuffleSwitchView != null) {
            mShuffleSwitchView.setOnClickListener(mComponentListener);
        }
        mSkipPrevious = findViewById(R.id.fu_controller_skip_previous);
        if (mSkipPrevious != null) {
            mSkipPrevious.setOnClickListener(mComponentListener);
        }
        mSkipNext = findViewById(R.id.fu_controller_skip_next);
        if (mSkipNext != null) {
            mSkipNext.setOnClickListener(mComponentListener);
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

    public ProgressAdapter getProgressAdapter() {
        return mProgressAdapter;
    }

    public void setProgressAdapter(ProgressAdapter progressAdapter) {
        if (progressAdapter == null) {
            this.mProgressAdapter = new DefaultProgressAdapter();
        } else {
            this.mProgressAdapter = progressAdapter;
        }
        this.mProgressAdapter.setPlayer(getPlayer());
    }

    public boolean isHideInBuffering() {
        return mHideInBuffering;
    }

    public void setHideInBuffering(boolean hideInBuffering) {
        this.mHideInBuffering = hideInBuffering;
    }

    public boolean isHideInEnded() {
        return mHideInEnded;
    }

    public void setHideInEnded(boolean hideInEnded) {
        this.mHideInEnded = hideInEnded;
    }

    public boolean isHideInError() {
        return mHideInError;
    }

    public void setHideInError(boolean hideInError) {
        this.mHideInError = hideInError;
    }

    public boolean isHideRepeatSwitch() {
        return mHideRepeatSwitch;
    }

    public void setHideRepeatSwitch(boolean hideRepeatSwitch) {
        this.mHideRepeatSwitch = hideRepeatSwitch;
        updateRepeatView();
    }

    public boolean isHideVolumeSwitch() {
        return mHideVolumeSwitch;
    }

    public void setHideVolumeSwitch(boolean hideVolumeSwitch) {
        this.mHideVolumeSwitch = hideVolumeSwitch;
        updateVolumeView();
    }

    public boolean isHideShuffleSwitch() {
        return mHideShuffleSwitch;
    }

    public void setHideShuffleSwitch(boolean hideShuffleSwitch) {
        this.mHideShuffleSwitch = hideShuffleSwitch;
        updateVolumeView();
    }

    public boolean isShowAlwaysInPaused() {
        return mShowAlwaysInPaused;
    }

    public void setShowAlwaysInPaused(boolean showAlwaysInPaused) {
        this.mShowAlwaysInPaused = showAlwaysInPaused;
    }

    @Override
    public FuPlayer getPlayer() {
        return mPlayer;
    }

    @Override
    public void setPlayer(FuPlayer player) {
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
        if (mProgressAdapter != null) {
            mProgressAdapter.setPlayer(player);
        }
        updateAll();
    }

    protected void updateAll() {
        updateShowOrHide();
        updatePlayPauseView();
        updateRepeatView();
        updateNavigation();
        updateProgress();
        updateVolumeView();
        updateShuffleView();
    }

    protected void updateShowOrHide() {
        if (!isInShowState()) {
            hide();
            return;
        }
        if (mShowAlwaysInPaused && isInShowAlwaysInPausedState()) {
            removeCallbacks(mHideAction);
            show();
        }
    }

    protected void updatePlayPauseView() {

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
            imageButton.setImageResource(R.drawable.fu_ic_pause);
        } else {
            imageButton.setImageResource(R.drawable.fu_ic_play);
        }
    }

    protected void updateNavigation() {
        if (!isShowing() || !mAttachedToWindow) {
            return;
        }
        Timeline timeline = mPlayer != null ? mPlayer.getCurrentTimeline() : null;
        boolean haveNonEmptyTimeline = timeline != null && !timeline.isEmpty();
        boolean isSeekable = false;
        boolean enablePrevious = false;
        boolean enableNext = false;
        if (haveNonEmptyTimeline && !mPlayer.isPlayingAd()) {
            int windowIndex = mPlayer.getCurrentWindowIndex();
            timeline.getWindow(windowIndex, mWindow);
            isSeekable = mWindow.isSeekable;
            enablePrevious = isSeekable || !mWindow.isDynamic || mPlayer.hasPrevious();
            enableNext = mWindow.isDynamic || mPlayer.hasNext();
        }
        setViewEnabled(enablePrevious, mSkipPrevious);
        setViewEnabled(enableNext, mSkipNext);
        setViewEnabled(mFastForwardMs > 0 && isSeekable, mFastForwardView);
        setViewEnabled(mRewindMs > 0 && isSeekable, mFastRewindView);
        if (mSeekView != null) {
            mSeekView.setEnabled(isSeekable);
        }
    }

    protected void updateRepeatView() {
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
        switch (repeatMode) {
            case FuPlayer.REPEAT_MODE_OFF:
                imageButton.setImageResource(R.drawable.fu_ic_repeat_off);
                imageButton.setContentDescription("");
                break;
            case FuPlayer.REPEAT_MODE_ONE:
                imageButton.setImageResource(R.drawable.fu_ic_repeat_one);
                imageButton.setContentDescription("");
                break;
            case FuPlayer.REPEAT_MODE_ALL:
                imageButton.setImageResource(R.drawable.fu_ic_repeat_all);
                imageButton.setContentDescription("");
                break;
            default:
                imageButton.setImageResource(R.drawable.fu_ic_repeat_off);
                imageButton.setContentDescription("");
        }
    }

    protected void updateVolumeView() {
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
            imageButton.setImageResource(R.drawable.fu_ic_volume_up);
        } else {
            imageButton.setImageResource(R.drawable.fu_ic_volume_off);
        }
    }

    protected void updateShuffleView() {
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

    protected void updateProgress() {
        if (!mAttachedToWindow || mProgressAdapter == null) {
            return;
        }
        long position = mProgressAdapter.getCurrentPosition();
        long duration = mProgressAdapter.getDuration();
        int bufferedPercent = mProgressAdapter.getBufferedPercentage();

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
            mSeekView.setVisibility(mProgressAdapter.isCurrentWindowDynamic() ? INVISIBLE : VISIBLE);
        }

        if (mDurationView != null) {
            mDurationView.setText(mProgressAdapter.getPositionText(duration));
            mDurationView.setVisibility(mProgressAdapter.isCurrentWindowDynamic() ? INVISIBLE : VISIBLE);
        }
        if (mPositionView != null && !mTracking) {
            mPositionView.setText(mProgressAdapter.getPositionText(position));
            mPositionView.setVisibility(mProgressAdapter.isCurrentWindowDynamic() ? INVISIBLE : VISIBLE);
        }

        if (mProgressAdapter.isCurrentWindowDynamic()) {
            removeCallbacks(mUpdateProgressAction);
            return;
        }
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
        mShowTimeoutMs = showTimeoutMs;
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

            dispatchVisibilityChanged(true);
        }

        if (!mShowAlwaysInPaused || !isInShowAlwaysInPausedState()) {
            hideAfterTimeout();
        }
    }


    @Override
    public void hide() {
        if (mShowing) {
            mShowing = false;
            mContainerView.setVisibility(View.GONE);
//            removeCallbacks(mUpdateProgressAction);
            removeCallbacks(mHideAction);
            mHideAtMs = -1;

            onHideChanged(true);
            dispatchVisibilityChanged(false);
        }
    }

    protected void onHideChanged(boolean hide) {

    }

    protected boolean isInShowAlwaysInPausedState() {
        if (!isInShowState()) {
            return false;
        }
        if (mPlayer.getPlaybackState() == FuPlayer.STATE_READY && !mPlayer.getPlayWhenReady()) {
            return true;
        }
        return false;
    }

    protected boolean isInShowState() {
        if (mPlayer == null || !mAttachedToWindow) {
            return false;
        }
        switch (mPlayer.getPlaybackState()) {
            case FuPlayer.STATE_IDLE:
                return !mHideInError && mPlayer.getPlaybackError() != null;
            case FuPlayer.STATE_BUFFERING:
                return !mHideInBuffering;
            case FuPlayer.STATE_READY:
                return true;
            case FuPlayer.STATE_ENDED:
                return !mHideInEnded;
            default:
                return false;
        }
    }

    public void hideAfterTimeout() {
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

//    protected String stringForTime(long timeMs) {
//        if (timeMs <= 0) {
//            timeMs = 0;
//        }
//
//        long totalSeconds = (timeMs + 500) / 1000;
//        long seconds = totalSeconds % 60;
//        long minutes = (totalSeconds / 60) % 60;
//        long hours = totalSeconds / 3600;
//
//        mFormatBuilder.setLength(0);
//        if (hours > 0) {
//            return mFormatter.format("%d:%02d:%02d", hours, minutes, seconds).toString();
//        } else {
//            return mFormatter.format("%02d:%02d", minutes, seconds).toString();
//        }
//    }

    protected boolean isPlaying() {
        return mPlayer != null
                && mPlayer.getPlaybackState() != FuPlayer.STATE_ENDED
                && mPlayer.getPlaybackState() != FuPlayer.STATE_IDLE
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
            if (mPlayer.getPlaybackState() == FuPlayer.STATE_ENDED) {
                mPlayer.seekTo(0);
            }
            mPlayer.setPlayWhenReady(true);
        } else {
            mPlayer.setPlayWhenReady(false);
        }
    }

    protected void seekTo(long positionMs) {
        if (positionMs < 0 || mPlayer == null) {
            return;
        }
        mPlayer.seekTo(positionMs);
    }

    protected void seekTo(int windowIndex, long positionMs) {
        if (positionMs < 0 || mPlayer == null) {
            return;
        }
        mPlayer.seekTo(windowIndex, positionMs);
    }

    protected void previous() {
        if (mPlayer == null) {
            return;
        }
        Timeline timeline = mPlayer.getCurrentTimeline();
        if (timeline.isEmpty() || mPlayer.isPlayingAd()) {
            return;
        }
        int windowIndex = mPlayer.getCurrentWindowIndex();
        timeline.getWindow(windowIndex, mWindow);
        int previousWindowIndex = mPlayer.getPreviousWindowIndex();
        if (previousWindowIndex != C.INDEX_UNSET
                && (mPlayer.getCurrentPosition() <= MAX_POSITION_FOR_SEEK_TO_PREVIOUS
                || (mWindow.isDynamic && !mWindow.isSeekable))) {
            seekTo(previousWindowIndex, C.TIME_UNSET);
        } else {
            seekTo(0);
        }
    }

    protected void next() {
        if (mPlayer == null) {
            return;
        }
        Timeline timeline = mPlayer.getCurrentTimeline();
        if (timeline.isEmpty() || mPlayer.isPlayingAd()) {
            return;
        }
        int windowIndex = mPlayer.getCurrentWindowIndex();
        int nextWindowIndex = mPlayer.getNextWindowIndex();
        if (nextWindowIndex != C.INDEX_UNSET) {
            seekTo(nextWindowIndex, C.TIME_UNSET);
        } else if (timeline.getWindow(windowIndex, mWindow).isDynamic) {
            seekTo(windowIndex, C.TIME_UNSET);
        }
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
    public boolean onTouchEvent(MotionEvent ev) {
        if (ev.getActionMasked() != MotionEvent.ACTION_DOWN) {
            return false;
        }
        return performClick();
    }

    @Override
    public boolean performClick() {
        super.performClick();
        if (!isInShowState()) {
            return false;
        }
        if (isShowing()) {
            hide();
        } else {
            show();
        }
        return false;
    }

    @Override
    public boolean onTrackballEvent(MotionEvent event) {
        if (!isInShowState()) {
            return false;
        }
        if (!isShowing()) {
            show();
        }
        return true;
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

    private final class ComponentListener implements View.OnClickListener, SeekBar.OnSeekBarChangeListener, FuPlayer.EventListener, VideoListener, AudioListener {

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
                mPlayer.setRepeatMode(RepeatModeUtil.getNextRepeatMode(mPlayer.getRepeatMode(), mRepeatToggleModes));
            } else if (mVolumeSwitchView == v) {
                if (mPlayer.getAudioComponent() == null) {
                    return;
                }
                if (mPlayer.getAudioComponent().getVolume() > 0) {
                    mPlayer.getAudioComponent().setVolume(0.0f);
                } else {
                    mPlayer.getAudioComponent().setVolume(1.0f);
                }
            } else if (mShuffleSwitchView == v) {
                if (mPlayer.getShuffleModeEnabled()) {
                    mPlayer.setShuffleModeEnabled(false);
                } else {
                    mPlayer.setShuffleModeEnabled(true);
                }
            } else if (mSkipPrevious == v) {
                previous();
            } else if (mSkipNext == v) {
                next();
            }
            hideAfterTimeout();
        }

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
            mCurrentSeekProgress = progress;
            if (!isInShowState()) {
                return;
            }
            long duration = mPlayer.getDuration();
            long newPosition = (duration * progress) / mSeekNumber;
            if (mPositionView != null) {
                mPositionView.setText(mProgressAdapter.getPositionText(newPosition));
            }
        }

        @Override
        public void onStopTrackingTouch(SeekBar bar) {
            mTracking = false;
            long duration = mPlayer.getDuration();
            long newPosition = duration * mCurrentSeekProgress / mSeekNumber;
            seekTo(newPosition);

            hideAfterTimeout();
        }

        @Override
        public void onTimelineChanged(Timeline timeline, int reason) {
            FuLog.d(TAG, "onTimelineChanged : timeline=" + timeline + ",reason=" + reason);
            updateNavigation();
            updateProgress();
        }

        @Override
        public void onPlayWhenReadyChanged(boolean playWhenReady, int reason) {
            FuLog.d(TAG, "onPlayWhenReadyChanged : playWhenReady=" + playWhenReady + ",reason=" + reason);
            updateShowOrHide();
            updatePlayPauseView();
            updateProgress();
        }

        @Override
        public void onPlaybackStateChanged(int state) {
            FuLog.d(TAG, "onPlayerStateChanged : state=" + state);
            updateShowOrHide();
            updatePlayPauseView();
            updateProgress();
        }

        @Override
        public void onRepeatModeChanged(int repeatMode) {
            FuLog.d(TAG, "onRepeatModeChanged : repeatMode=" + repeatMode);
            updateNavigation();
            updateRepeatView();
        }

        @Override
        public void onShuffleModeEnabledChanged(boolean shuffleModeEnabled) {
            FuLog.d(TAG, "onShuffleModeEnabledChanged : shuffleModeEnabled=" + shuffleModeEnabled);
            updateNavigation();
            updateShuffleView();
        }

        @Override
        public void onVolumeChanged(float volume) {
            FuLog.d(TAG, "onVolumeChanged : volume=" + volume);
            updateNavigation();
            updateVolumeView();
        }
    }
}
