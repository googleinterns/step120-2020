package com.google.roomies;

/** Constants for a location. */
public final class LocationConstants {
  private LocationConstants() {}
  public static final Location BERKELEY = Location.builder()
    .setLatitude(37.8719)
    .setLongitude(-122.2585)
    .build();
  public static final double EARTH_RADIUS_IN_MILES = 3958.8;
  public static final double RADIANS_PER_DEGREE = Math.PI / 180;
}