package com.google.roomies;

import com.google.auto.value.AutoValue;
import com.google.cloud.firestore.GeoPoint;
import com.google.common.base.Preconditions;
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
    abstract Builder setLatitude(double latitude);
    abstract Builder setLongitude(double longitude);
    
    abstract Location autoBuild();
    
    Location build() {
      Location location = autoBuild();
      Preconditions.checkState(location.latitude() >= -90, 
        "Latitude is out of the range [-90, 90]");
      Preconditions.checkState(location.latitude() <= 90, 
        "Latitude is out of the range [-90, 90]");
      Preconditions.checkState(location.longitude() >= -180, 
        "Longitude is out of the range [-180, 180]"); 
      Preconditions.checkState(location.longitude() <= 180, 
        "Longitude is out of the range [-180, 180]");    
      return location; 
    }
  }
  
  /**
  * Returns a GeoPoint representation of location.
  */
  public GeoPoint toGeoPoint() {
    return new GeoPoint(latitude(), longitude());
  }
}
