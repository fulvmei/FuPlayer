package com.chengfu.android.fuplayer;

public interface StateView extends PlayerView {

    interface VisibilityChangeListener {
        void onVisibilityChange(StateView stateView, boolean visibility);
    }
}
