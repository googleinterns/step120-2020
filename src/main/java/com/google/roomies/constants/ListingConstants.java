package com.google.roomies;

/** Constants for a listing. */
public final class ListingConstants {
  private ListingConstants() {}
  public static final Location BERKELEY = Location.builder()
    .setLatitude(37.8719)
    .setLongitude(-122.2585)
    .build();
  public static final double EARTH_RADIUS_IN_MILES = 3958.8;
  public static final double RADIANS_PER_DEGREE = Math.PI / 180;
  public static final String LISTING_COLLECTION_NAME = "listings";
  public static final String CURRENCY_CODE = "USD";
  public static final String DATE_FORMAT = "yyyy-MM-dd";
  public static final String RESPONSE_CONTENT_TYPE = "application/json";
}