package com.threebird.recorder.models.schemas;

import com.threebird.recorder.models.MappableChar;

/**
 * Maps a key to a behavior. We use this in configuring a {@link Schema}
 */
public class KeyBehaviorMapping
{
  public final String uuid;
  public final MappableChar key;
  public final String behavior;
  public final boolean isContinuous;

  public KeyBehaviorMapping( String uuid,
                             MappableChar key,
                             String behavior,
                             boolean isContinuous )
  {
    this.uuid = uuid;
    this.key = key;
    this.behavior = behavior;
    this.isContinuous = isContinuous;
  }

  public KeyBehaviorMapping( String uuid,
                             String key,
                             String behavior,
                             boolean isContinuous )
  {
    this( uuid,
          MappableChar.getForChar( key.charAt( 0 ) ).get(),
          behavior,
          isContinuous );
  }

  @Override public String toString()
  {
    return "KeyBehaviorMapping [key=" + key + ", behavior=" + behavior
        + ", isContinuous=" + isContinuous + "]";
  }

  @Override public int hashCode()
  {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((uuid == null) ? 0 : uuid.hashCode());
    return result;
  }

  @Override public boolean equals( Object obj )
  {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    KeyBehaviorMapping other = (KeyBehaviorMapping) obj;
    if (uuid == null) {
      if (other.uuid != null)
        return false;
    } else if (!uuid.equals( other.uuid ))
      return false;
    return true;
  }
}
