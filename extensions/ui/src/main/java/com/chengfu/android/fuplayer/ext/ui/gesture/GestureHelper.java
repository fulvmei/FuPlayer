package com.chengfu.android.fuplayer.ext.ui.gesture;

import android.support.annotation.IntDef;
import android.view.MotionEvent;
import android.view.View;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public class GestureHelper {

    public static final String TAG = "GestureHelper";

    @Retention(RetentionPolicy.SOURCE)
    @IntDef({SLIDE_TYPE_NONE, SLIDE_TYPE_HORIZONTAL, SLIDE_TYPE_RIGHT_VERTICAL, SLIDE_TYPE_LEFT_VERTICAL})
    @interface SlideType {
    }

    public static final int SLIDE_TYPE_NONE = 0;
    public static final int SLIDE_TYPE_HORIZONTAL = 1;
    public static final int SLIDE_TYPE_RIGHT_VERTICAL = 2;
    public static final int SLIDE_TYPE_LEFT_VERTICAL = 3;

    private boolean firstTouch;
    private boolean horizontalSlide;
    private boolean rightVerticalSlide;
    private int viewWidth;
    private int viewHeight;
    private boolean scrolling;

    @SlideType
    private int slideType = SLIDE_TYPE_NONE;

    private OnSlideChangedListener onSlideChangedListener;

//    private

    public interface OnSlideChangedListener {

        void onStartSlide(@SlideType int slideType);

        void onProgressChanged(@SlideType int slideType, float progress);

        void onStopSlide(@SlideType int slideType);
    }

    public GestureHelper(View view) {
        view.getViewTreeObserver().addOnGlobalLayoutListener(() -> {
            viewWidth = view.getWidth();
            viewHeight = view.getHeight();
        });

//        view.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View view, MotionEvent motionEvent) {
//                if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
//                    if (scrolling) {
//                        scrolling = false;
//                        if (onSlideChangedListener != null) {
//                            onSlideChangedListener.onStopSlide(slideType);
//                        }
//                        return true;
//                    }
//                }
//                return false;
//            }
//        });
    }

    public void setOnSlideChangedListener(OnSlideChangedListener onSlideChangedListener) {
        this.onSlideChangedListener = onSlideChangedListener;
    }

    public void onDown(MotionEvent e) {
        firstTouch = true;
    }

    public void onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {

        float mOldX = e1.getX();
        float mOldY = e1.getY();
        float deltaY = mOldY - e2.getY();
        float deltaX = mOldX - e2.getX();

        if (firstTouch) {
            horizontalSlide = Math.abs(distanceX) >= Math.abs(distanceY);
            rightVerticalSlide = mOldX > viewWidth * 0.5f;
            firstTouch = false;
        }

        if (horizontalSlide) {
            slideType = SLIDE_TYPE_HORIZONTAL;
            onScrolling(slideType, -deltaX / viewWidth);
        } else {
            if (Math.abs(deltaY) > viewHeight)
                return;
            if (rightVerticalSlide) {
                slideType = SLIDE_TYPE_RIGHT_VERTICAL;
                onScrolling(slideType, deltaY / viewHeight);
            } else {
                slideType = SLIDE_TYPE_LEFT_VERTICAL;
                onScrolling(slideType, deltaY / viewHeight);
            }
        }
    }

    public void onUp(MotionEvent e) {
        if (scrolling) {
            scrolling = false;
            if (onSlideChangedListener != null) {
                onSlideChangedListener.onStopSlide(slideType);
            }
        }
    }

    private void onScrolling(@SlideType int slideType, float percent) {
        if (!scrolling) {
            if (onSlideChangedListener != null) {
                onSlideChangedListener.onStartSlide(slideType);
            }
            scrolling = true;
        }
        if (onSlideChangedListener != null) {
            onSlideChangedListener.onProgressChanged(slideType, percent);
        }
    }


}
