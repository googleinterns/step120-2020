package com.google.roomies;

import static com.google.roomies.LocationConstants.BERKELEY;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;

import com.google.cloud.firestore.GeoPoint;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class LocationTest {

  private static final double DELTA = 1e-15;
  private static final double LATITUDE = 32.21;
  private static final double LONGITUDE = -102.12;
  private static Location location;

  @Before
  public void setUp() {
    location = Location.builder()
    .setLatitude(LATITUDE)
    .setLongitude(LONGITUDE)
    .build();
  }

  @Test(expected = IllegalStateException.class)
  public void testBuild_latidudeIsLessThanMinLatitude_throwsException() {
    double illegalLatitude = -120;
    Location.builder()
    .setLatitude(illegalLatitude)
    .setLongitude(LONGITUDE)
    .build();
  }
  
  @Test(expected = IllegalStateException.class)
  public void testBuild_latidudeIsGreaterThanMaxLatitude_throwsException() {
    double illegalLatitude = 120;
    Location.builder()
    .setLatitude(illegalLatitude)
    .setLongitude(LONGITUDE)
    .build();
  }
  
  @Test(expected = IllegalStateException.class)
  public void testBuild_longitudeIsLessThanMinLongitude_throwsException() {
    double illegalLongitude = -190;
    Location.builder()
    .setLatitude(LATITUDE)
    .setLongitude(illegalLongitude)
    .build();
  }
  
  @Test(expected = IllegalStateException.class)
  public void testBuild_longitudeIsGreaterThanMaxLongitude_throwsException() {
    double illegalLongitude = 190;
    Location.builder()
    .setLatitude(LATITUDE)
    .setLongitude(illegalLongitude)
    .build();
  }
  
  @Test
  public void testDistanceTo_returnsExpectedDistance() {
    Location testLocation = BERKELEY;
    
    double expectedDistance = 1201.6042324995394;
    double actualDistance = location.distanceTo(BERKELEY);
    
    assertEquals(actualDistance, expectedDistance, DELTA);
  }
  
  @Test
  public void testToGeoPoint_returnsGeoPointWithSameLatLngValues() {
    GeoPoint actualGeoPoint = location.toGeoPoint();
    GeoPoint expectedGeoPoint = new GeoPoint(LATITUDE, LONGITUDE);
    
    assertEquals(actualGeoPoint, expectedGeoPoint);
  }
}
