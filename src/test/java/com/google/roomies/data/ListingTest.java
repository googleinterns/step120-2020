package com.google.roomies;

import static com.google.roomies.ListingConstants.BERKELEY;
import static com.google.roomies.ListingRequestParameterNames.DESCRIPTION;
import static com.google.roomies.ListingRequestParameterNames.END_DATE;
import static com.google.roomies.ListingRequestParameterNames.GEOPOINT;
import static com.google.roomies.ListingRequestParameterNames.LATITUDE;
import static com.google.roomies.ListingRequestParameterNames.LEASE_TYPE;
import static com.google.roomies.ListingRequestParameterNames.LISTING_PRICE;
import static com.google.roomies.ListingRequestParameterNames.LONGITUDE;
import static com.google.roomies.ListingRequestParameterNames.MILES_TO_CAMPUS;
import static com.google.roomies.ListingRequestParameterNames.NUM_BATHROOMS;
import static com.google.roomies.ListingRequestParameterNames.NUM_ROOMS;
import static com.google.roomies.ListingRequestParameterNames.NUM_SHARED;
import static com.google.roomies.ListingRequestParameterNames.NUM_SINGLES;
import static com.google.roomies.ListingRequestParameterNames.SHARED_ROOM_PRICE;
import static com.google.roomies.ListingRequestParameterNames.SINGLE_ROOM_PRICE;
import static com.google.roomies.ListingRequestParameterNames.START_DATE;
import static com.google.roomies.ListingRequestParameterNames.TIMESTAMP;
import static com.google.roomies.ListingRequestParameterNames.TITLE;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;

import com.google.cloud.firestore.FieldValue;
import com.google.cloud.firestore.GeoPoint;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.cloud.Timestamp;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Iterables;
import com.google.common.collect.Maps;
import java.io.IOException;
import java.text.ParseException;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ExecutionException;
import javax.money.UnknownCurrencyException;
import javax.money.format.MonetaryParseException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

@RunWith(JUnit4.class)
public class ListingTest {
  @Mock HttpServletRequest request;
  @Mock QueryDocumentSnapshot queryDocumentSnapshotMock;

  private static final double DELTA = 1e-15;
  private static final String LISTING_DESCRIPTION = "Test description";
  private static final String LISTING_END_DATE = "2020-07-10";
  private static final String LISTING_LEASE_TYPE = "YEAR_LONG";
  private static final String LISTING_NUM_BATHROOMS = "3";
  private static final String LISTING_NUM_ROOMS = "2";
  private static final String LISTING_NUM_SHARED = "1";
  private static final String LISTING_NUM_SINGLES = "1";
  private static final String LISTING_SHARED_PRICE = "100";
  private static final String LISTING_SINGLE_PRICE = "1000";
  private static final String LISTING_LISTING_PRICE = "1100";
  private static final String LISTING_START_DATE = "2020-07-10";
  private static final String LISTING_TITLE = "Test title";
  private static final String LISTING_LATITUDE = "32.21";
  private static final String LISTING_LONGITUDE = "-102.12";
  private static final Double LISTING_MILES_TO_CAMPUS = 1201.6042324995394;
  private static final GeoPoint LISTING_GEOPOINT = 
    new GeoPoint(Double.parseDouble(LISTING_LATITUDE), Double.parseDouble(LISTING_LONGITUDE));
  private static Listing listing;

  @Before
  public void setUp() throws ParseException {
    MockitoAnnotations.initMocks(this);

    listing = Listing.builder()
    .setDescription(LISTING_DESCRIPTION)
    .setEndDate(LISTING_END_DATE)
    .setLeaseType(LISTING_LEASE_TYPE)
    .setNumBathrooms(LISTING_NUM_BATHROOMS)
    .setNumRooms(LISTING_NUM_ROOMS)
    .setNumShared(LISTING_NUM_SHARED)
    .setNumSingles(LISTING_NUM_SINGLES)
    .setSharedPrice(LISTING_SHARED_PRICE)
    .setSinglePrice(LISTING_SINGLE_PRICE)
    .setListingPrice(LISTING_LISTING_PRICE)
    .setStartDate(LISTING_START_DATE)
    .setTitle(LISTING_TITLE)
    .setLocationAndDistanceToCampus(LISTING_LATITUDE, LISTING_LONGITUDE, BERKELEY)
    .build();
  }

  @Test(expected = IllegalArgumentException.class)
  public void testSetLeaseType_leaseTypeIsInvalid_throwsIllegalArgumentException() {
    Listing.builder().setLeaseType("invalidLeaseType");
  }

  @Test(expected = NumberFormatException.class)
  public void testSetNumRooms_inputIsNotAnInteger_throwsNumberFormatException() {
    Listing.builder().setNumRooms("zero");
  }

  @Test(expected = NumberFormatException.class)
  public void testSetNumBathrooms_inputIsNotAnInteger_throwsNumberFormatException() {
    Listing.builder().setNumBathrooms("notAnInteger");
  }

  @Test(expected = NumberFormatException.class)
  public void testSetNumShared_inputIsNotAnInteger_throwsNumberFormatException() {
    Listing.builder().setNumShared("0.1");
  }

  @Test(expected = NumberFormatException.class)
  public void testSetNumSingles_inputIsNotAnInteger_throwsNumberFormatException() {
    Listing.builder().setNumSingles("9.6");
  }

  @Test(expected = ParseException.class)
  public void testSetStartDate_inputIsNotInCorrectFormat_throwsParseException() 
      throws ParseException {
    Listing.builder().setStartDate("2020202020");
  }

  @Test(expected = ParseException.class)
  public void testSetEndDate_inputIsNotInCorrectFormat_throwsParseException() 
      throws ParseException {
    Listing.builder().setEndDate("12/7/2020");
  }

  @Test(expected = NumberFormatException.class)
  public void testSetSharedPrice_inputIsNotInCorrectFormat_throwsNumberFormatException() {
    Listing.builder().setSharedPrice("$300");
  }

  @Test(expected = NumberFormatException.class)
  public void testSetSinglePrice_inputIsNotInCorrectFormat_throwsNumberFormatException() {
    Listing.builder().setSinglePrice("invalidPrice");
  }

  @Test(expected = IllegalArgumentException.class)
  public void testSetListingPrice_inputIsNotNonNegative_throwsIllegalArgumentException() {
    Listing.builder().setListingPrice("-300");
  }

  @Test(expected = IllegalStateException.class)
  public void testSetLocationAndDistanceToCampus_latitudeIsInvalid_throwsIllegalArgumentException() {
    Listing.builder().setLocationAndDistanceToCampus("100", LISTING_LONGITUDE, BERKELEY);
  }

  @Test(expected = IllegalStateException.class)
  public void testSetLocationAndDistanceToCampus_longitudeIsInvalid_throwsIllegalArgumentException() {
    Listing.builder().setLocationAndDistanceToCampus(LISTING_LATITUDE, "-200", BERKELEY);
  }

  @Test
  public void testFromServletRequest_returnsListingWithAllValuesSet() throws ParseException {
    when(request.getParameter(DESCRIPTION)).thenReturn(LISTING_DESCRIPTION);
    when(request.getParameter(END_DATE)).thenReturn(LISTING_END_DATE);
    when(request.getParameter(LATITUDE)).thenReturn(LISTING_LATITUDE);
    when(request.getParameter(LONGITUDE)).thenReturn(LISTING_LONGITUDE);
    when(request.getParameter(LEASE_TYPE)).thenReturn(LISTING_LEASE_TYPE);
    when(request.getParameter(NUM_BATHROOMS)).thenReturn(LISTING_NUM_BATHROOMS);
    when(request.getParameter(NUM_ROOMS)).thenReturn(LISTING_NUM_ROOMS);
    when(request.getParameter(NUM_SHARED)).thenReturn(LISTING_NUM_SHARED);
    when(request.getParameter(NUM_SINGLES)).thenReturn(LISTING_NUM_SINGLES);
    when(request.getParameter(SHARED_ROOM_PRICE)).thenReturn(LISTING_SHARED_PRICE);
    when(request.getParameter(SINGLE_ROOM_PRICE)).thenReturn(LISTING_SINGLE_PRICE);
    when(request.getParameter(LISTING_PRICE)).thenReturn(LISTING_LISTING_PRICE);
    when(request.getParameter(START_DATE)).thenReturn(LISTING_START_DATE);
    when(request.getParameter(TITLE)).thenReturn(LISTING_TITLE);

    Listing actualListing = Listing.fromServletRequest(request);
    Listing expectedListing = listing;

    assertEquals(actualListing, expectedListing);
  }

  @Test
  public void testToMap_returnsMapOfListingData() throws ParseException {
    Map<String, Object> actualData = listing.toMap();
    Map<String, Object> expectedData = ImmutableMap.<String, Object>builder()
      .put(TITLE, LISTING_TITLE)
      .put(DESCRIPTION, LISTING_DESCRIPTION)
      .put(START_DATE, StringConverter.stringToDate(LISTING_START_DATE))
      .put(END_DATE, StringConverter.stringToDate(LISTING_END_DATE))
      .put(LEASE_TYPE, LISTING_LEASE_TYPE)
      .put(NUM_ROOMS, Integer.parseInt(LISTING_NUM_ROOMS))
      .put(NUM_BATHROOMS, Integer.parseInt(LISTING_NUM_BATHROOMS))
      .put(NUM_SHARED, Integer.parseInt(LISTING_NUM_SHARED))
      .put(NUM_SINGLES, Integer.parseInt(LISTING_NUM_SINGLES))
      .put(SHARED_ROOM_PRICE, 
        StringConverter.stringToNonNegativeMoney(LISTING_SHARED_PRICE).toString())
      .put(SINGLE_ROOM_PRICE, 
        StringConverter.stringToNonNegativeMoney(LISTING_SINGLE_PRICE).toString())
      .put(LISTING_PRICE, 
        StringConverter.stringToNonNegativeMoney(LISTING_LISTING_PRICE).toString())
      .put(TIMESTAMP, FieldValue.serverTimestamp())
      .put(GEOPOINT, LISTING_GEOPOINT)
      .put(MILES_TO_CAMPUS, LISTING_MILES_TO_CAMPUS)
      .build();

    assertEquals(actualData, expectedData);
  }
  
  @Test
  public void testDistanceBetweenTwoCoordinates_returnsExpectedDistance() {
    double expectedDistance = 1201.6042324995394;
    double actualDistance = listing.milesToCampus();
    
    assertEquals(actualDistance, expectedDistance, DELTA);
  }

  @Test
  public void testFromFirestore_returnsOptionalContainingListing() throws 
      UnknownCurrencyException, MonetaryParseException, NumberFormatException, 
      ParseException, IOException, InterruptedException, ExecutionException {
    String documentId = "documentId";
    Timestamp timestamp = Timestamp.parseTimestamp("2016-09-18T00:00:00Z");
    Map<String, Object> listingData = ImmutableMap.<String, Object>builder()
      .put(TITLE, LISTING_TITLE)
      .put(DESCRIPTION, LISTING_DESCRIPTION)
      .put(START_DATE, LISTING_START_DATE)
      .put(END_DATE, LISTING_END_DATE)
      .put(LEASE_TYPE, LISTING_LEASE_TYPE)
      .put(NUM_ROOMS, Long.parseLong(LISTING_NUM_ROOMS))
      .put(NUM_BATHROOMS, Long.parseLong(LISTING_NUM_BATHROOMS))
      .put(NUM_SHARED, Long.parseLong(LISTING_NUM_SHARED))
      .put(NUM_SINGLES, Long.parseLong(LISTING_NUM_SINGLES))
      .put(SHARED_ROOM_PRICE, 
        StringConverter.stringToNonNegativeMoney(LISTING_SHARED_PRICE).toString())
      .put(SINGLE_ROOM_PRICE, 
        StringConverter.stringToNonNegativeMoney(LISTING_SINGLE_PRICE).toString())
      .put(LISTING_PRICE, 
        StringConverter.stringToNonNegativeMoney(LISTING_LISTING_PRICE).toString())
      .put(TIMESTAMP, timestamp)
      .put(GEOPOINT, LISTING_GEOPOINT)
      .put(MILES_TO_CAMPUS, LISTING_MILES_TO_CAMPUS)
      .build();
    when(queryDocumentSnapshotMock.getData()).thenReturn(listingData);
    when(queryDocumentSnapshotMock.getId()).thenReturn(documentId);
  
    Optional<Listing> actualListing = Listing.fromFirestore(queryDocumentSnapshotMock);
    Optional<Listing> expectedListing = Optional.of(listing.toBuilder()
      .setDocumentId(Optional.of(documentId))
      .setTimestamp(Optional.of(timestamp))
      .build());
  
    assertEquals(actualListing, expectedListing);
  }
}
