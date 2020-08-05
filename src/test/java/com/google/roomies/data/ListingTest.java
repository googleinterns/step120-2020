package com.google.roomies;

import static com.google.roomies.ListingRequestParameterNames.DESCRIPTION;
import static com.google.roomies.ListingRequestParameterNames.END_DATE;
import static com.google.roomies.ListingRequestParameterNames.LISTING_PRICE;
import static com.google.roomies.ListingRequestParameterNames.LEASE_TYPE;
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

  private static final String description = "Test description";
  private static final String endDate = "2020-07-10";
  private static final String leaseType = "YEAR_LONG";
  private static final String numBathrooms = "3";
  private static final String numRooms = "2";
  private static final String numShared = "1";
  private static final String numSingles = "1";
  private static final String sharedPrice = "100";
  private static final String singlePrice = "1000";
  private static final String listingPrice = "1100";
  private static final String startDate = "2020-07-10";
  private static final String title = "Test title";
  private static Listing listing;

  @Before
  public void setUp() throws ParseException {
    MockitoAnnotations.initMocks(this);

    listing = Listing.builder()
    .setDescription(description)
    .setEndDate(endDate)
    .setLeaseType(leaseType)
    .setNumBathrooms(numBathrooms)
    .setNumRooms(numRooms)
    .setNumShared(numShared)
    .setNumSingles(numSingles)
    .setSharedPrice(sharedPrice)
    .setSinglePrice(singlePrice)
    .setListingPrice(listingPrice)
    .setStartDate(startDate)
    .setTitle(title)
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

  @Test
  public void testFromServletRequest_returnsListingWithAllValuesSet() throws ParseException {
    when(request.getParameter(DESCRIPTION)).thenReturn(description);
    when(request.getParameter(END_DATE)).thenReturn(endDate);
    when(request.getParameter(LEASE_TYPE)).thenReturn(leaseType);
    when(request.getParameter(NUM_BATHROOMS)).thenReturn(numBathrooms);
    when(request.getParameter(NUM_ROOMS)).thenReturn(numRooms);
    when(request.getParameter(NUM_SHARED)).thenReturn(numShared);
    when(request.getParameter(NUM_SINGLES)).thenReturn(numSingles);
    when(request.getParameter(SHARED_ROOM_PRICE)).thenReturn(sharedPrice);
    when(request.getParameter(SINGLE_ROOM_PRICE)).thenReturn(singlePrice);
    when(request.getParameter(LISTING_PRICE)).thenReturn(listingPrice);
    when(request.getParameter(START_DATE)).thenReturn(startDate);
    when(request.getParameter(TITLE)).thenReturn(title);

    Listing actualListing = Listing.fromServletRequest(request);
    Listing expectedListing = listing;

    assertEquals(actualListing, expectedListing);
  }

  @Test
  public void testToMap_returnsMapOfListingData() throws ParseException {
    Map<String, Object> actualData = listing.toMap();
    Map<String, Object> expectedData = ImmutableMap.<String, Object>builder()
      .put(TITLE, title)
      .put(DESCRIPTION, description)
      .put(START_DATE, StringConverter.stringToDate(startDate))
      .put(END_DATE, StringConverter.stringToDate(endDate))
      .put(LEASE_TYPE, leaseType)
      .put(NUM_ROOMS, Integer.parseInt(numRooms))
      .put(NUM_BATHROOMS, Integer.parseInt(numBathrooms))
      .put(NUM_SHARED, Integer.parseInt(numShared))
      .put(NUM_SINGLES, Integer.parseInt(numSingles))
      .put(SHARED_ROOM_PRICE, StringConverter.stringToNonNegativeMoney(sharedPrice).toString())
      .put(SINGLE_ROOM_PRICE,  StringConverter.stringToNonNegativeMoney(singlePrice).toString())
      .put(LISTING_PRICE,  StringConverter.stringToNonNegativeMoney(listingPrice).toString())
      .put(TIMESTAMP, FieldValue.serverTimestamp())
      .build();

    assertEquals(actualData, expectedData);
  }

  @Test
  public void testFromFirestore_returnsOptionalContainingListing() throws 
      UnknownCurrencyException, MonetaryParseException, NumberFormatException, 
      ParseException, IOException, InterruptedException, ExecutionException {
    String documentId = "documentId";
    Timestamp timestamp = Timestamp.parseTimestamp("2016-09-18T00:00:00Z");
    Map<String, Object> listingData = ImmutableMap.<String, Object>builder()
      .put(TITLE, title)
      .put(DESCRIPTION, description)
      .put(START_DATE, startDate)
      .put(END_DATE, endDate)
      .put(LEASE_TYPE, leaseType)
      .put(NUM_ROOMS, Long.parseLong(numRooms))
      .put(NUM_BATHROOMS, Long.parseLong(numBathrooms))
      .put(NUM_SHARED, Long.parseLong(numShared))
      .put(NUM_SINGLES, Long.parseLong(numSingles))
      .put(SHARED_ROOM_PRICE, StringConverter.stringToNonNegativeMoney(sharedPrice).toString())
      .put(SINGLE_ROOM_PRICE,  StringConverter.stringToNonNegativeMoney(singlePrice).toString())
      .put(LISTING_PRICE,  StringConverter.stringToNonNegativeMoney(listingPrice).toString())
      .put(TIMESTAMP, timestamp)
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
