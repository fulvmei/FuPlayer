package com.chengfu.android.fuplayer;

import android.support.annotation.IntDef;

import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.Renderer;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.util.Assertions;

import java.io.IOException;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Thrown when a non-recoverable playback failure occurs.
 */
public final class FuPlaybackException extends Exception {

  /**
   * The type of source that produced the error. One of {@link #TYPE_SOURCE}, {@link #TYPE_RENDERER}
   * or {@link #TYPE_UNEXPECTED}.
   */
  @Documented
  @Retention(RetentionPolicy.SOURCE)
  @IntDef({TYPE_SOURCE, TYPE_RENDERER, TYPE_UNEXPECTED})
  public @interface Type {}
  /**
   * The error occurred loading data from a {@link MediaSource}.
   * <p>
   * Call {@link #getSourceException()} to retrieve the underlying cause.
   */
  public static final int TYPE_SOURCE = 0;
  /**
   * The error occurred in a {@link Renderer}.
   * <p>
   * Call {@link #getRendererException()} to retrieve the underlying cause.
   */
  public static final int TYPE_RENDERER = 1;
  /**
   * The error was an unexpected {@link RuntimeException}.
   * <p>
   * Call {@link #getUnexpectedException()} to retrieve the underlying cause.
   */
  public static final int TYPE_UNEXPECTED = 2;

  /**
   * The type of the playback failure. One of {@link #TYPE_SOURCE}, {@link #TYPE_RENDERER} and
   * {@link #TYPE_UNEXPECTED}.
   */
  @Type public final int type;

  /**
   * If {@link #type} is {@link #TYPE_RENDERER}, this is the index of the renderer.
   */
  public final int rendererIndex;

  private final Throwable cause;

  /**
   * Creates an instance of type {@link #TYPE_SOURCE}.
   *
   * @param cause The cause of the failure.
   * @return The created instance.
   */
  public static FuPlaybackException createForSource(IOException cause) {
    return new FuPlaybackException(TYPE_SOURCE, cause, C.INDEX_UNSET);
  }

  /**
   * Creates an instance of type {@link #TYPE_RENDERER}.
   *
   * @param cause The cause of the failure.
   * @param rendererIndex The index of the renderer in which the failure occurred.
   * @return The created instance.
   */
  public static FuPlaybackException createForRenderer(Exception cause, int rendererIndex) {
    return new FuPlaybackException(TYPE_RENDERER, cause, rendererIndex);
  }

  /**
   * Creates an instance of type {@link #TYPE_UNEXPECTED}.
   *
   * @param cause The cause of the failure.
   * @return The created instance.
   */
  /* package */ static FuPlaybackException createForUnexpected(RuntimeException cause) {
    return new FuPlaybackException(TYPE_UNEXPECTED, cause, C.INDEX_UNSET);
  }

  private FuPlaybackException(@Type int type, Throwable cause, int rendererIndex) {
    super(cause);
    this.type = type;
    this.cause = cause;
    this.rendererIndex = rendererIndex;
  }

  /**
   * Retrieves the underlying error when {@link #type} is {@link #TYPE_SOURCE}.
   *
   * @throws IllegalStateException If {@link #type} is not {@link #TYPE_SOURCE}.
   */
  public IOException getSourceException() {
    Assertions.checkState(type == TYPE_SOURCE);
    return (IOException) cause;
  }

  /**
   * Retrieves the underlying error when {@link #type} is {@link #TYPE_RENDERER}.
   *
   * @throws IllegalStateException If {@link #type} is not {@link #TYPE_RENDERER}.
   */
  public Exception getRendererException() {
    Assertions.checkState(type == TYPE_RENDERER);
    return (Exception) cause;
  }

  /**
   * Retrieves the underlying error when {@link #type} is {@link #TYPE_UNEXPECTED}.
   *
   * @throws IllegalStateException If {@link #type} is not {@link #TYPE_UNEXPECTED}.
   */
  public RuntimeException getUnexpectedException() {
    Assertions.checkState(type == TYPE_UNEXPECTED);
    return (RuntimeException) cause;
  }

}
