package com.threebird.recorder.models.behaviors;

import com.threebird.recorder.models.MappableChar;
import com.threebird.recorder.utils.EventRecorderUtil;

public class ContinuousBehavior extends Behavior
{
  private Integer duration;

  /**
   * @param key
   * @param description
   * @param start
   *          - start-time in millis
   * @param duration
   *          - duration of the behavior in millis
   */
  public ContinuousBehavior( MappableChar key, String description, Integer start, Integer duration )
  {
    super( key, description, start );
    this.duration = duration;
  }

  @Override public boolean isContinuous()
  {
    return true;
  }

  public int getDuration()
  {
    return duration;
  }

  @Override public String timeDisplay()
  {
    String start = EventRecorderUtil.millisToTimestamp( startTime );
    String end = EventRecorderUtil.millisToTimestamp( startTime + duration );
    return String.format( "%s - %s", start, end );
  }
}
