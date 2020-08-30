package com.google.roomies;

import static com.google.roomies.LocationConstants.RADIANS_PER_DEGREE;
import static com.google.roomies.LocationConstants.EARTH_RADIUS_IN_MILES;
import static java.lang.Math.asin;
import static java.lang.Math.cos;
import static java.lang.Math.sin;
import static java.lang.Math.sqrt;

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
  
  /**
  * Calculates the straight-line distance in miles between two locations.
  *
  * Uses the Haversine distance (angular distance between two points
  * on the surface of a sphere).
  *
  * Note: Driving/walking distance was not implemented at this point in time because using
  * the Google Places API requires displaying the results on a Google Map, which is 
  * out of scope for our MVP.
  */
  public double distanceTo(Location location) {
    double firstLocationLatitudeInRadians = this.latitude() * RADIANS_PER_DEGREE; 
    double secondLocationLatitudeInRadians = location.latitude() * RADIANS_PER_DEGREE; 
    double latitudeDifference = secondLocationLatitudeInRadians - firstLocationLatitudeInRadians;
    double longitudeDifference = (location.longitude() - this.longitude()) 
      * RADIANS_PER_DEGREE;

    return 2 * EARTH_RADIUS_IN_MILES * asin(sqrt(sin(latitudeDifference/2.0) *
      sin(latitudeDifference/2.0) + cos(firstLocationLatitudeInRadians) * 
      cos(secondLocationLatitudeInRadians) * sin(longitudeDifference/2.0) * 
      sin(longitudeDifference/2.0)));
  }
}
