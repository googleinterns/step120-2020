package com.google.roomies;

import com.google.auto.value.AutoValue;
import com.google.cloud.firestore.GeoPoint;
import java.io.Serializable;

/** A class representing a location. */
@AutoValue
public abstract class Location {
  abstract double latitude();
  abstract double longitude();
  abstract Builder toBuilder();

  public static Builder builder() {
    return new AutoValue_Location.Builder();
  }
 
  @AutoValue.Builder
  public abstract static class Builder implements Serializable {
    public abstract Builder setLatitude(double latitude);
    public abstract Builder setLongitude(double longitude);
    public abstract Location build();
  }
  
  public GeoPoint toGeoPoint() {
    return new GeoPoint(latitude(), longitude());
  }
}
