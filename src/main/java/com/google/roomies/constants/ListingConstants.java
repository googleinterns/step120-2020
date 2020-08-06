package com.google.roomies;

import com.google.common.primitives.ImmutableDoubleArray;

public final class ListingConstants {
  private ListingConstants() {}
  public static final ImmutableDoubleArray BERKELEY_LOCATION = 
    ImmutableDoubleArray.of(37.8719, -122.2585); //[latitude, longitude]
  public static final double EARTH_RADIUS = 3958.8; //in miles
  public static final double DEGREE_TO_RADIAN_RATIO = Math.PI / 180;
  public static final String LISTING_COLLECTION_NAME = "listings";
  public static final String CURRENCY_CODE = "USD";
  public static final String DATE_FORMAT = "yyyy-MM-dd";
  public static final String RESPONSE_CONTENT_TYPE = "application/json";
}