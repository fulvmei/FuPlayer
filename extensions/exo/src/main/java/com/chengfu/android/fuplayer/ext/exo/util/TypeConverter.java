package com.chengfu.android.fuplayer.ext.exo.util;

import com.chengfu.android.fuplayer.FuPlaybackException;
import com.chengfu.android.fuplayer.PlaybackParameters;
import com.chengfu.android.fuplayer.source.TrackGroupArray;
import com.chengfu.android.fuplayer.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.source.TrackGroup;
import com.google.android.exoplayer2.trackselection.TrackSelection;

public class TypeConverter {


    public static PlaybackParameters PlaybackParametersExoToFu(com.google.android.exoplayer2.PlaybackParameters input) {
        if (input == null) {
            return null;
        }

        return new PlaybackParameters(input.speed, input.pitch, input.skipSilence);
    }

    public static com.google.android.exoplayer2.PlaybackParameters PlaybackParametersFuToExo(PlaybackParameters input) {
        if (input == null) {
            return null;
        }
        return new com.google.android.exoplayer2.PlaybackParameters(input.speed, input.pitch, input.skipSilence);
    }

    public static TrackGroupArray TrackGroupArrayExoToFu(com.google.android.exoplayer2.source.TrackGroupArray input) {
        if (input == null) {
            return null;
        }
        TrackGroup[] trackGroups = new TrackGroup[input.length];
        for (int i = 0; i < input.length; i++) {
            trackGroups[i] = input.get(i);
        }
        return new TrackGroupArray(trackGroups);
    }

    public static com.google.android.exoplayer2.source.TrackGroupArray TrackGroupArrayFuToExo(TrackGroupArray input) {
        if (input == null) {
            return null;
        }
        TrackGroup[] trackGroups = new TrackGroup[input.length];
        for (int i = 0; i < input.length; i++) {
            trackGroups[i] = input.get(i);
        }
        return new com.google.android.exoplayer2.source.TrackGroupArray(trackGroups);
    }

    public static TrackSelectionArray TrackSelectionArrayExoToFu(com.google.android.exoplayer2.trackselection.TrackSelectionArray input) {
        if (input == null) {
            return null;
        }
        TrackSelection[] trackSelections = new TrackSelection[input.length];
        for (int i = 0; i < input.length; i++) {
            trackSelections[i] = input.get(i);
        }
        return new TrackSelectionArray(trackSelections);
    }

    public static com.google.android.exoplayer2.trackselection.TrackSelectionArray TrackSelectionArrayFuToExo(TrackSelectionArray input) {
        if (input == null) {
            return null;
        }

        TrackSelection[] trackSelections = new TrackSelection[input.length];
        for (int i = 0; i < input.length; i++) {
            trackSelections[i] = input.get(i);
        }
        return new com.google.android.exoplayer2.trackselection.TrackSelectionArray(trackSelections);
    }

    public static FuPlaybackException PlaybackErrorExoToFu(com.google.android.exoplayer2.ExoPlaybackException input) {
        if (input == null) {
            return null;
        }
        switch (input.type) {
            case ExoPlaybackException.TYPE_RENDERER:
                return FuPlaybackException.createForRenderer(input.getRendererException(), input.rendererIndex);
            case ExoPlaybackException.TYPE_SOURCE:
                return FuPlaybackException.createForRenderer(input.getSourceException(), input.rendererIndex);
            default:
                return FuPlaybackException.createForRenderer(input.getUnexpectedException(), input.rendererIndex);
        }
    }

    public static com.google.android.exoplayer2.ExoPlaybackException PlaybackErrorFuToExo(FuPlaybackException input) {
        if (input == null) {
            return null;
        }
        switch (input.type) {
            case FuPlaybackException.TYPE_RENDERER:
                return com.google.android.exoplayer2.ExoPlaybackException.createForRenderer(input.getRendererException(), input.rendererIndex);
            case FuPlaybackException.TYPE_SOURCE:
                return com.google.android.exoplayer2.ExoPlaybackException.createForRenderer(input.getSourceException(), input.rendererIndex);
            default:
                return com.google.android.exoplayer2.ExoPlaybackException.createForRenderer(input.getUnexpectedException(), input.rendererIndex);
        }
    }
}
