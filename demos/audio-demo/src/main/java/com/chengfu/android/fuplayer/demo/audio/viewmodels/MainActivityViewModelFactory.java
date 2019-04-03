package com.chengfu.android.fuplayer.demo.audio.viewmodels;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

import com.chengfu.android.fuplayer.demo.audio.MediaSessionConnection;

import java.lang.reflect.InvocationTargetException;

public class MainActivityViewModelFactory implements ViewModelProvider.Factory {

    private MediaSessionConnection mediaSessionConnection;

    public MainActivityViewModelFactory(MediaSessionConnection mediaSessionConnection) {
        this.mediaSessionConnection = mediaSessionConnection;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        try {
            return modelClass.getConstructor(MediaSessionConnection.class).newInstance(mediaSessionConnection);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException("Cannot create an instance of " + modelClass, e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException("Cannot create an instance of " + modelClass, e);
        } catch (InstantiationException e) {
            throw new RuntimeException("Cannot create an instance of " + modelClass, e);
        } catch (InvocationTargetException e) {
            throw new RuntimeException("Cannot create an instance of " + modelClass, e);
        }
    }
}
