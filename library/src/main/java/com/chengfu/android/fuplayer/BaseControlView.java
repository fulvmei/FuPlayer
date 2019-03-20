package com.chengfu.android.fuplayer;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.FrameLayout;


public abstract class BaseControlView extends FrameLayout implements PlayerControllerView {
    public BaseControlView(@NonNull Context context) {
        super(context);
    }

    public BaseControlView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public BaseControlView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

}
