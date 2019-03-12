package com.chengfu.android.fuplayer.text;

import com.google.android.exoplayer2.text.Cue;

import java.util.List;

/**
 * Receives text output.
 */
public interface TextOutput {

    /**
     * Called when there is a change in the {@link Cue}s.
     *
     * @param cues The {@link Cue}s.
     */
    void onCues(List<Cue> cues);

}
