package com.chengfu.android.fuplayer.video;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.FrameLayout;

import java.util.concurrent.CopyOnWriteArraySet;


public abstract class BaseControlView extends FrameLayout implements PlayerControllerView {

    private final CopyOnWriteArraySet<VisibilityChangeListener> visibilityChangeListeners;

    public BaseControlView(@NonNull Context context) {
        this(context, null);
    }

    public BaseControlView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BaseControlView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        visibilityChangeListeners = new CopyOnWriteArraySet<>();
    }

    public boolean isFullScreen() {
        return false;
    }

    public void setFullScreen(boolean fullScreen) {

    }

    @Override
    public void addVisibilityChangeListener(VisibilityChangeListener l) {
        visibilityChangeListeners.add(l);
    }

    @Override
    public void removeVisibilityChangeListener(VisibilityChangeListener l) {
        visibilityChangeListeners.remove(l);
    }

    /**
     * Dispatch callbacks to {@link #addVisibilityChangeListener} down
     * the view hierarchy.
     */
    protected void dispatchVisibilityChanged(boolean visibility) {
        for (VisibilityChangeListener listener : visibilityChangeListeners) {
            if (listener != null) {
                listener.onVisibilityChange(this, visibility);
            }
        }
    }
}
