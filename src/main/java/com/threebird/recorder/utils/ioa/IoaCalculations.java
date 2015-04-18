package com.threebird.recorder.utils.ioa;

import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;

import com.google.common.base.Optional;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Multiset;
import com.google.common.collect.Sets;
import com.google.common.collect.Sets.SetView;

public class IoaCalculations
{
  private static double exactComparison( int x, int y )
  {
    return x == y ? 1.0 : 0.0;
  }

  private static double partialComparison( int x, int y )
  {
    if (x == y) {
      return 1.0;
    }
    if (x == 0 || y == 0) {
      return 0.0;
    }
    double _x = (double) x;
    double _y = (double) y;
    return (x > y ? _y / _x : _x / _y);
  }

  private static Map< Character, IntervalCalculations >
    getIntervals( KeyToInterval data1,
                  KeyToInterval data2,
                  BiFunction< Integer, Integer, Double > compare )
  {
    SetView< Character > common = Sets.union( data1.charToIntervals.keySet(), data2.charToIntervals.keySet() );
    Map< Character, IntervalCalculations > map = Maps.newHashMap();

    int numIntervals = Math.max( data1.totalIntervals, data2.totalIntervals );

    for (Character c : common) {
      int[] intervals1 = new int[numIntervals];
      int[] intervals2 = new int[numIntervals];
      double[] result = new double[numIntervals];

      for (Integer i = 0; i < numIntervals; i++) {
        intervals1[i] += data1.charToIntervals.get( c ) != null ? data1.charToIntervals.get( c ).count( i ) : 0;
        intervals2[i] += data2.charToIntervals.get( c ) != null ? data2.charToIntervals.get( c ).count( i ) : 0;
      }

      for (int i = 0; i < numIntervals; i++) {
        result[i] = compare.apply( intervals1[i], intervals2[i] );
      }

      map.put( c, new IntervalCalculations( c, intervals1, intervals2, result ) );
    }

    return map;
  }

  static Map< Character, IntervalCalculations > exactAgreement( KeyToInterval data1, KeyToInterval data2 )
  {
    return getIntervals( data1, data2, IoaCalculations::exactComparison );
  }

  static Map< Character, IntervalCalculations > partialAgreement( KeyToInterval data1, KeyToInterval data2 )
  {
    return getIntervals( data1, data2, IoaCalculations::partialComparison );
  }

  static double windowAgreement( Multiset< Integer > seconds, Multiset< Integer > comparison, int threshold )
  {
    List< Integer > _seconds = Lists.newArrayList( seconds );
    List< Integer > _comparison = Lists.newArrayList( comparison );
    int numMatched = 0;

    for (int k = 0; k < _seconds.size(); k++) {
      int s = _seconds.get( k );
      int min = s - threshold;
      int max = s + threshold;

      Optional< Integer > optMatch = Iterables.tryFind( _comparison, i -> i >= min && i <= max );
      for (Integer match : optMatch.asSet()) {
        _comparison.remove( match );
        numMatched++;
      }
    }

    return ((double) numMatched) / _seconds.size();
  }

  static Map< Character, Double > windowAgreement( KeyToInterval data1, KeyToInterval data2, int threshold )
  {
    SetView< Character > common = Sets.union( data1.charToIntervals.keySet(), data2.charToIntervals.keySet() );
    Map< Character, Double > result = Maps.newHashMap();

    for (Character c : common) {
      Multiset< Integer > seconds1 = data1.charToIntervals.get( c );
      Multiset< Integer > seconds2 = data2.charToIntervals.get( c );

      double result1 = windowAgreement( seconds1, seconds2, threshold );
      double result2 = windowAgreement( seconds2, seconds1, threshold );
      result.put( c, (result1 + result2) / 2 );
    }

    return result;
  }
}