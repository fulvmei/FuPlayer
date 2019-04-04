package com.chengfu.android.fuplayer.video;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.RectF;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.Surface;
import android.view.SurfaceView;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.chengfu.android.fuplayer.core.FuPlayer;
import com.chengfu.android.fuplayer.video.spherical.SingleTapListener;
import com.chengfu.android.fuplayer.video.spherical.SphericalSurfaceView;
import com.chengfu.android.fuplayer.video.util.FuLog;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.text.Cue;
import com.google.android.exoplayer2.text.TextOutput;
import com.google.android.exoplayer2.util.Assertions;
import com.google.android.exoplayer2.video.VideoListener;

import java.util.List;

public class FuPlayerView extends FrameLayout implements PlayerView {

    public static final String TAG = "FuPlayerView";

    private static final int SURFACE_TYPE_NONE = 0;
    public static final int SURFACE_TYPE_SURFACE_VIEW = 1;
    public static final int SURFACE_TYPE_TEXTURE_VIEW = 2;
    private static final int SURFACE_TYPE_MONO360_VIEW = 3;

    public static final int RESIZE_MODE_FIT = AspectRatioFrameLayout.RESIZE_MODE_FIT;
    public static final int RESIZE_MODE_FIXED_WIDTH = AspectRatioFrameLayout.RESIZE_MODE_FIXED_WIDTH;
    public static final int RESIZE_MODE_FIXED_HEIGHT = AspectRatioFrameLayout.RESIZE_MODE_FIXED_HEIGHT;
    public static final int RESIZE_MODE_FILL = AspectRatioFrameLayout.RESIZE_MODE_FILL;
    public static final int RESIZE_MODE_ZOOM = AspectRatioFrameLayout.RESIZE_MODE_ZOOM;

    private AspectRatioFrameLayout mSurfaceContainer;
    private View mSurfaceView;
    private ImageView shutterView;
    private SubtitleView subtitleView;
    //    private CopyOnWriteArraySet<BaseStateView> mStateViews = new CopyOnWriteArraySet<>();
//    private PlayerController playerController;
//    private FrameLayout overlayFrameLayout;

    private FuPlayer mPlayer;
    private ComponentListener componentListener;

    private int mTextureViewRotation;
    private int mSurfaceType = -1;
    private int mResizeMode = -1;

    public FuPlayerView(Context context) {
        this(context, null);
    }

    public FuPlayerView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FuPlayerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        if (isInEditMode()) {
            setBackgroundResource(R.color.player_view_edit_mode_bg);
            return;
        }

        int shutterColor = Color.BLACK;
        int surfaceType = SURFACE_TYPE_SURFACE_VIEW;
        int resizeMode = RESIZE_MODE_FIT;

        if (attrs != null) {
            TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.FuPlayerView, 0, 0);
            try {
                surfaceType = a.getInt(R.styleable.FuPlayerView_surface_type, SURFACE_TYPE_SURFACE_VIEW);
                resizeMode = a.getInt(R.styleable.FuPlayerView_resize_mode, RESIZE_MODE_FIT);
                shutterColor = a.getColor(R.styleable.FuPlayerView_shutter_background_color, shutterColor);
            } finally {
                a.recycle();
            }
        }
        LayoutInflater.from(context).inflate(R.layout.default_player_view, this);


        mSurfaceContainer = findViewById(R.id.surface_container);
        shutterView = findViewById(R.id.view_shutter);
        subtitleView = findViewById(R.id.subtitleView);
        if (subtitleView != null) {
            subtitleView.setUserDefaultStyle();
            subtitleView.setUserDefaultTextSize();
        }

        setDescendantFocusability(FOCUS_AFTER_DESCENDANTS);

        componentListener = new ComponentListener();

        setShutterBackgroundColor(shutterColor);

        setSurfaceViewType(surfaceType);

        setResizeMode(resizeMode);

        updateScreenOn();
    }

    private void updateScreenOn() {
        boolean keepScreenOn = false;
        if (mPlayer != null && mPlayer.getPlaybackState() != FuPlayer.STATE_IDLE
                && mPlayer.getPlaybackState() != FuPlayer.STATE_ENDED && mPlayer.getPlayWhenReady()) {
            keepScreenOn = true;
        }
        setKeepScreenOn(keepScreenOn);
    }


//    private void updateStateViews() {
//        for (BaseStateView stateView : mStateViews) {
//            stateView.setPlayer(mPlayer);
//        }
//    }

    public void setShutterBackgroundColor(int color) {
        if (shutterView != null) {
            shutterView.setBackgroundColor(color);
            FuLog.d(TAG, "setShutterBackgroundColor : color=" + color);
        }
    }

    public void setSurfaceViewType(int surfaceType) {
        if (mSurfaceType == surfaceType
                || (surfaceType != SURFACE_TYPE_NONE
                && surfaceType != SURFACE_TYPE_SURFACE_VIEW
                && surfaceType != SURFACE_TYPE_TEXTURE_VIEW
                && surfaceType != SURFACE_TYPE_MONO360_VIEW)) {
            return;
        }
        if (mSurfaceView != null && mSurfaceView.getParent() != null && mSurfaceView.getParent() instanceof AspectRatioFrameLayout) {
            mSurfaceContainer.removeView(mSurfaceView);
        }

        mSurfaceType = surfaceType;
        switch (surfaceType) {
            case SURFACE_TYPE_NONE:
                mSurfaceView = null;
                break;
            case SURFACE_TYPE_SURFACE_VIEW:
                mSurfaceView = new SurfaceView(getContext());
                break;
            case SURFACE_TYPE_TEXTURE_VIEW:
                mSurfaceView = new TextureView(getContext());
                break;
            case SURFACE_TYPE_MONO360_VIEW:
                SphericalSurfaceView sphericalSurfaceView = new SphericalSurfaceView(getContext());
                sphericalSurfaceView.setSurfaceListener(componentListener);
                sphericalSurfaceView.setSingleTapListener(componentListener);
                mSurfaceView = sphericalSurfaceView;
                break;
        }

        if (mSurfaceView != null) {
            ViewGroup.LayoutParams params =
                    new ViewGroup.LayoutParams(
                            ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            mSurfaceView.setLayoutParams(params);
            mSurfaceContainer.addView(mSurfaceView, 0);
        }

        if (mPlayer != null && mPlayer.getVideoComponent() != null) {
            if (mSurfaceView instanceof TextureView) {
                mPlayer.getVideoComponent().setVideoTextureView((TextureView) mSurfaceView);
            } else if (mSurfaceView instanceof SurfaceView) {
                mPlayer.getVideoComponent().setVideoSurfaceView((SurfaceView) mSurfaceView);
            }
        }

        FuLog.i(TAG, "setSurfaceView : surfaceType : " + (mSurfaceView == null ? "None" : mSurfaceView.getClass().getSimpleName()));

    }

    public int getSurfaceType() {
        return mSurfaceType;
    }

    public void setResizeMode(int resizeMode) {
        if (mResizeMode == resizeMode
                || (resizeMode != RESIZE_MODE_FIT
                && resizeMode != RESIZE_MODE_FIXED_WIDTH
                && resizeMode != RESIZE_MODE_FIXED_HEIGHT
                && resizeMode != RESIZE_MODE_FILL
                && resizeMode != RESIZE_MODE_ZOOM)) {
            return;
        }
        mResizeMode = resizeMode;
        mSurfaceContainer.setResizeMode(resizeMode);

        FuLog.i(TAG, "setResizeMode : resizeMode : " + resizeMode);
    }

    public int getResizeMode() {
        return mResizeMode;
    }

    /**
     * Switches the view targeted by a given {@link FuPlayer}.
     *
     * @param player        The player whose target view is being switched.
     * @param oldPlayerView The old view to detach from the player.
     * @param newPlayerView The new view to attach to the player.
     */
    public static void switchTargetView(
            FuPlayer player, @Nullable FuPlayerView oldPlayerView, @Nullable FuPlayerView newPlayerView) {
        if (oldPlayerView == newPlayerView) {
            return;
        }
        // We attach the new view before detaching the old one because this ordering allows the player
        // to swap directly from one surface to another, without transitioning through a state where no
        // surface is attached. This is significantly more efficient and achieves a more seamless
        // transition when using platform provided video decoders.
        if (newPlayerView != null) {
            newPlayerView.setPlayer(player);
        }
        if (oldPlayerView != null) {
            oldPlayerView.setPlayer(null);
        }
    }

    /**
     * Returns the player currently set on this view, or null if no player is set.
     */
    @Override
    public FuPlayer getPlayer() {
        return mPlayer;
    }

    /**
     * Set the {@link FuPlayer} to use.
     *
     * <p>To transition a {@link FuPlayer} from targeting one view to another, it's recommended to use
     * {@link #switchTargetView(FuPlayer, FuPlayerView, FuPlayerView)} rather than this method. If you do
     * wish to use this method directly, be sure to attach the player to the new view <em>before</em>
     * calling {@code setPlayer(null)} to detach it from the old one. This ordering is significantly
     * more efficient and may allow for more seamless transitions.
     *
     * @param player The {@link FuPlayer} to use, or {@code null} to detach the current player. Only
     *               players which are accessed on the main thread are supported ({@code
     *               player.getApplicationLooper() == Looper.getMainLooper()}).
     */
    @Override
    public void setPlayer(FuPlayer player) {
        Assertions.checkState(Looper.myLooper() == Looper.getMainLooper());
        Assertions.checkArgument(
                player == null || player.getApplicationLooper() == Looper.getMainLooper());
        if (mPlayer == player) {
            return;
        }
        if (mPlayer != null) {
            mPlayer.addListener(componentListener);
            FuPlayer.VideoComponent oldVideoComponent = mPlayer.getVideoComponent();
            if (oldVideoComponent != null) {
                oldVideoComponent.removeVideoListener(componentListener);
                if (mSurfaceView instanceof TextureView) {
                    oldVideoComponent.setVideoTextureView(null);
                } else if (mSurfaceView instanceof SurfaceView) {
                    oldVideoComponent.setVideoSurfaceView(null);
                }
            }
            FuPlayer.TextComponent oldTextComponent = mPlayer.getTextComponent();
            if (oldTextComponent != null) {
                oldTextComponent.removeTextOutput(componentListener);
            }
            if (subtitleView != null) {
                subtitleView.setCues(null);
            }
        }
        mPlayer = player;

        if (player != null) {
            FuPlayer.VideoComponent newVideoComponent = player.getVideoComponent();
            if (newVideoComponent != null) {
                if (mSurfaceView instanceof TextureView) {
                    newVideoComponent.setVideoTextureView((TextureView) mSurfaceView);
                } else if (mSurfaceView instanceof SurfaceView) {
                    newVideoComponent.setVideoSurfaceView((SurfaceView) mSurfaceView);
                }
                newVideoComponent.addVideoListener(componentListener);
            }
            FuPlayer.TextComponent newTextComponent = player.getTextComponent();
            if (newTextComponent != null) {
                newTextComponent.addTextOutput(componentListener);
            }
            player.addListener(componentListener);
        }
        updateScreenOn();
    }

//    public void addStateView(BaseStateView stateView) {
//        if (stateView == null || mStateViews.contains(stateView)) {
//            return;
//        }
//        addView(stateView, getChildCount());
//        mStateViews.add(stateView);
//        stateView.setPlayer(mPlayer);
//    }

//    public void removeStateView(BaseStateView stateView) {
//        if (stateView == null || !mStateViews.contains(stateView)) {
//            return;
//        }
//        removeView(stateView);
//        mStateViews.remove(stateView);
//        stateView.setPlayer(null);
//    }

    /**
     * Should be called when the player is visible to the user and if {@code surface_type} is {@code
     * spherical_view}. It is the counterpart to {@link #onPause()}.
     *
     * <p>This method should typically be called in {@link Activity#onStart()}, or {@link
     * Activity#onResume()} for API versions &lt;= 23.
     */
    public void onResume() {
        if (mSurfaceView instanceof SphericalSurfaceView) {
            ((SphericalSurfaceView) mSurfaceView).onResume();
        }
    }

    /**
     * Should be called when the player is no longer visible to the user and if {@code surface_type}
     * is {@code spherical_view}. It is the counterpart to {@link #onResume()}.
     *
     * <p>This method should typically be called in {@link Activity#onStop()}, or {@link
     * Activity#onPause()} for API versions &lt;= 23.
     */
    public void onPause() {
        if (mSurfaceView instanceof SphericalSurfaceView) {
            ((SphericalSurfaceView) mSurfaceView).onPause();
        }
    }

    /**
     * Applies a texture rotation to a {@link TextureView}.
     */
    private static void applyTextureViewRotation(TextureView textureView, int textureViewRotation) {
        float textureViewWidth = textureView.getWidth();
        float textureViewHeight = textureView.getHeight();
        if (textureViewWidth == 0 || textureViewHeight == 0 || textureViewRotation == 0) {
            textureView.setTransform(null);
        } else {
            Matrix transformMatrix = new Matrix();
            float pivotX = textureViewWidth / 2;
            float pivotY = textureViewHeight / 2;
            transformMatrix.postRotate(textureViewRotation, pivotX, pivotY);

            // After rotation, scale the rotated texture to fit the TextureView size.
            RectF originalTextureRect = new RectF(0, 0, textureViewWidth, textureViewHeight);
            RectF rotatedTextureRect = new RectF();
            transformMatrix.mapRect(rotatedTextureRect, originalTextureRect);
            transformMatrix.postScale(
                    textureViewWidth / rotatedTextureRect.width(),
                    textureViewHeight / rotatedTextureRect.height(),
                    pivotX,
                    pivotY);
            textureView.setTransform(transformMatrix);
        }
    }

    private final class ComponentListener implements FuPlayer.EventListener, VideoListener, TextOutput, OnLayoutChangeListener, SphericalSurfaceView.SurfaceListener, SingleTapListener {

        @Override
        public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
            FuLog.d(TAG, "onPlayerStateChanged : playWhenReady=" + playWhenReady + " ,playbackState=" + playbackState);
            updateScreenOn();
        }

        @Override
        public void onVideoSizeChanged(int width, int height, int unappliedRotationDegrees, float pixelWidthHeightRatio) {
            FuLog.d(TAG, "onVideoSizeChanged : width=" + width + ",height=" + height + ",unappliedRotationDegrees=" + unappliedRotationDegrees + ",pixelWidthHeightRatio=" + pixelWidthHeightRatio);
//            if (pixelWidthHeightRatio == 0) {
//                pixelWidthHeightRatio = 1.0f;
//            }

            float videoAspectRatio =
                    (height == 0 || width == 0) ? 1 : (width * pixelWidthHeightRatio) / height;

            if (mSurfaceView instanceof TextureView) {
                // Try to apply rotation transformation when our surface is a TextureView.
                if (unappliedRotationDegrees == 90 || unappliedRotationDegrees == 270) {
                    // We will apply a rotation 90/270 degree to the output texture of the TextureView.
                    // In this case, the output video's width and height will be swapped.
                    videoAspectRatio = 1 / videoAspectRatio;
                }
                if (mTextureViewRotation != 0) {
                    mSurfaceView.removeOnLayoutChangeListener(this);
                }
                mTextureViewRotation = unappliedRotationDegrees;
                if (mTextureViewRotation != 0) {
                    // The texture view's dimensions might be changed after layout step.
                    // So add an OnLayoutChangeListener to apply rotation after layout step.
                    mSurfaceView.addOnLayoutChangeListener(this);
                }
                applyTextureViewRotation((TextureView) mSurfaceView, mTextureViewRotation);
            }

            if (mSurfaceContainer != null && mSurfaceView != null) {
                mSurfaceContainer.setAspectRatio(
                        mSurfaceView instanceof SphericalSurfaceView ? 0 : videoAspectRatio);
            }
        }

        @Override
        public void onRenderedFirstFrame() {
            FuLog.d(TAG, "onRenderedFirstFrame");
            if (shutterView != null && shutterView.getVisibility() == VISIBLE) {
                shutterView.setVisibility(INVISIBLE);
                shutterView.setImageBitmap(null);
            }
        }

        @Override
        public void onSurfaceSizeChanged(int width, int height) {
            FuLog.d(TAG, "onSurfaceSizeChanged : width=" + width + ",height=" + height);
            if (width == 0 || height == 0) {
                if (shutterView != null) {
                    shutterView.setVisibility(VISIBLE);
                }
            }
        }

        @Override
        public void onPlayerError(ExoPlaybackException error) {

        }

        @Override
        public void onPositionDiscontinuity(int reason) {
            FuLog.d(TAG, "onPositionDiscontinuity : reason=" + reason);
            if (mSurfaceView instanceof TextureView) {
                TextureView surfaceView = (TextureView) mSurfaceView;
                shutterView.setImageBitmap(surfaceView.getBitmap());
            }
        }

        @Override
        public void onCues(List<Cue> cues) {
            if (subtitleView != null) {
                subtitleView.onCues(cues);
            }
        }

        @Override
        public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
            applyTextureViewRotation((TextureView) v, mTextureViewRotation);
        }

        @Override
        public void surfaceChanged(@Nullable Surface surface) {
            if (mPlayer != null) {
                FuPlayer.VideoComponent videoComponent = mPlayer.getVideoComponent();
                if (videoComponent != null) {
                    videoComponent.setVideoSurface(surface);
                }
            }
        }

        @Override
        public boolean onSingleTapUp(MotionEvent e) {
            return false;
        }
    }
}
