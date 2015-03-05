package com.threebird.recorder.utils.ioa;

import java.io.File;
import java.util.Arrays;
import java.util.Collections;
import java.util.Map;
import java.util.function.BiFunction;

import com.google.common.collect.Maps;
import com.google.common.collect.Multiset;
import com.google.common.collect.Sets;
import com.google.common.collect.Sets.SetView;

public class IoaCalculations
{

  private static int getNumIntervals( Multiset< Integer > times )
  {
    if (times == null) {
      return 0;
    }
    return Collections.max( times ) + 1;
  }

  private static class Intervals
  {
    public final Character c;
    public final int[] intervals1;
    public final int[] intervals2;
    public final double[] result;
    public final double avg;

    public Intervals( Character c, int[] intervals1, int[] intervals2, double[] result )
    {
      this.c = c;
      this.intervals1 = intervals1;
      this.intervals2 = intervals2;
      this.result = result;
      this.avg = Arrays.stream( result ).average().orElse( 0 );
    }
  }

  private static Map< Character, Intervals >
    getIntervals( KeyToTime data1,
                  KeyToTime data2,
                  BiFunction< Integer, Integer, Double > compare )
  {
    SetView< Character > common = Sets.union( data1.keySet(), data2.keySet() );
    Map< Character, Intervals > map = Maps.newHashMap();

    for (Character c : common) {
      int numIntervals1 = getNumIntervals( data1.get( c ) );
      int numIntervals2 = getNumIntervals( data2.get( c ) );
      int numIntervals = numIntervals1 > numIntervals2 ? numIntervals1 : numIntervals2;

      int[] intervals1 = new int[numIntervals];
      int[] intervals2 = new int[numIntervals];
      double[] result = new double[numIntervals];

      for (Integer i = 0; i < numIntervals; i++) {
        intervals1[i] += data1.get( c ) != null ? data1.get( c ).count( i ) : 0;
        intervals2[i] += data2.get( c ) != null ? data2.get( c ).count( i ) : 0;
      }

      for (int i = 0; i < numIntervals; i++) {
        int x = intervals1[i];
        int y = intervals2[i];
        result[i] = compare.apply( x, y );
      }

      map.put( c, new Intervals( c, intervals1, intervals2, result ) );
    }

    return map;
  }

  private static File printIntervals( Map< Character, Intervals > intervals )
  {
    return null;
  }

  static File exactAgreement( KeyToTime data1, KeyToTime data2 )
  {
    Map< Character, Intervals > intervals = getIntervals( data1, data2, ( x, y ) -> x == y ? 1.0 : 0.0 );
    return printIntervals( intervals );
  }

  static File partialAgreement( KeyToTime data1, KeyToTime data2 )
  {
    Map< Character, Intervals > intervals = getIntervals( data1, data2, ( x, y ) -> {
      if (x == y) {
        return 1.0;
      }
      if (x == 0 || y == 0) {
        return 0.0;
      }
      return (double) (x > y ? y / x : y / x);
    } );

    return printIntervals( intervals );
  }
}
