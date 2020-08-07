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

  private static final String TEST_DESCRIPTION = "Test description";
  private static final String TEST_END_DATE = "2020-07-10";
  private static final String TEST_LEASE_TYPE = "YEAR_LONG";
  private static final String TEST_NUM_BATHROOMS = "3";
  private static final String TEST_NUM_ROOMS = "2";
  private static final String TEST_NUM_SHARED = "1";
  private static final String TEST_NUM_SINGLES = "1";
  private static final String TEST_SHARED_PRICE = "100";
  private static final String TEST_SINGLE_PRICE = "1000";
  private static final String TEST_LISTING_PRICE = "1100";
  private static final String TEST_START_DATE = "2020-07-10";
  private static final String TEST_TITLE = "Test title";
  private static Listing listing;

  @Before
  public void setUp() throws ParseException {
    MockitoAnnotations.initMocks(this);

    listing = Listing.builder()
    .setDescription(TEST_DESCRIPTION)
    .setEndDate(TEST_END_DATE)
    .setLeaseType(TEST_LEASE_TYPE)
    .setNumBathrooms(TEST_NUM_BATHROOMS)
    .setNumRooms(TEST_NUM_ROOMS)
    .setNumShared(TEST_NUM_SHARED)
    .setNumSingles(TEST_NUM_SINGLES)
    .setSharedPrice(TEST_SHARED_PRICE)
    .setSinglePrice(TEST_SINGLE_PRICE)
    .setListingPrice(TEST_LISTING_PRICE)
    .setStartDate(TEST_START_DATE)
    .setTitle(TEST_TITLE)
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
    when(request.getParameter(DESCRIPTION)).thenReturn(TEST_DESCRIPTION);
    when(request.getParameter(END_DATE)).thenReturn(TEST_END_DATE);
    when(request.getParameter(LEASE_TYPE)).thenReturn(TEST_LEASE_TYPE);
    when(request.getParameter(NUM_BATHROOMS)).thenReturn(TEST_NUM_BATHROOMS);
    when(request.getParameter(NUM_ROOMS)).thenReturn(TEST_NUM_ROOMS);
    when(request.getParameter(NUM_SHARED)).thenReturn(TEST_NUM_SHARED);
    when(request.getParameter(NUM_SINGLES)).thenReturn(TEST_NUM_SINGLES);
    when(request.getParameter(SHARED_ROOM_PRICE)).thenReturn(TEST_SHARED_PRICE);
    when(request.getParameter(SINGLE_ROOM_PRICE)).thenReturn(TEST_SINGLE_PRICE);
    when(request.getParameter(LISTING_PRICE)).thenReturn(TEST_LISTING_PRICE);
    when(request.getParameter(START_DATE)).thenReturn(TEST_START_DATE);
    when(request.getParameter(TITLE)).thenReturn(TEST_TITLE);

    Listing actualListing = Listing.fromServletRequest(request);
    Listing expectedListing = listing;

    assertEquals(actualListing, expectedListing);
  }

  @Test
  public void testToMap_returnsMapOfListingData() throws ParseException {
    Map<String, Object> actualData = listing.toMap();
    Map<String, Object> expectedData = ImmutableMap.<String, Object>builder()
      .put(TITLE, TEST_TITLE)
      .put(DESCRIPTION, TEST_DESCRIPTION)
      .put(START_DATE, StringConverter.stringToDate(TEST_START_DATE))
      .put(END_DATE, StringConverter.stringToDate(TEST_END_DATE))
      .put(LEASE_TYPE, TEST_LEASE_TYPE)
      .put(NUM_ROOMS, Integer.parseInt(TEST_NUM_ROOMS))
      .put(NUM_BATHROOMS, Integer.parseInt(TEST_NUM_BATHROOMS))
      .put(NUM_SHARED, Integer.parseInt(TEST_NUM_SHARED))
      .put(NUM_SINGLES, Integer.parseInt(TEST_NUM_SINGLES))
      .put(SHARED_ROOM_PRICE, 
        StringConverter.stringToNonNegativeMoney(TEST_SHARED_PRICE).toString())
      .put(SINGLE_ROOM_PRICE, 
        StringConverter.stringToNonNegativeMoney(TEST_SINGLE_PRICE).toString())
      .put(LISTING_PRICE, 
        StringConverter.stringToNonNegativeMoney(TEST_LISTING_PRICE).toString())
      .put(TIMESTAMP, FieldValue.serverTimestamp())
      .build();

    assertEquals(actualData, expectedData);
  }
/*
  @Test
  public void testFromFirestore_returnsOptionalContainingListing() throws 
      UnknownCurrencyException, MonetaryParseException, NumberFormatException, 
      ParseException, IOException, InterruptedException, ExecutionException {
    String documentId = "documentId";
    Timestamp timestamp = Timestamp.parseTimestamp("2016-09-18T00:00:00Z");
    Map<String, Object> listingData = ImmutableMap.<String, Object>builder()
      .put(TITLE, TEST_TITLE)
      .put(DESCRIPTION, TEST_DESCRIPTION)
      .put(START_DATE, TEST_START_DATE)
      .put(END_DATE, TEST_END_DATE)
      .put(LEASE_TYPE, TEST_LEASE_TYPE)
      .put(NUM_ROOMS, Long.parseLong(TEST_NUM_ROOMS))
      .put(NUM_BATHROOMS, Long.parseLong(TEST_NUM_BATHROOMS))
      .put(NUM_SHARED, Long.parseLong(TEST_NUM_SHARED))
      .put(NUM_SINGLES, Long.parseLong(TEST_NUM_SINGLES))
      .put(SHARED_ROOM_PRICE, 
        StringConverter.stringToNonNegativeMoney(TEST_SHARED_PRICE).toString())
      .put(SINGLE_ROOM_PRICE, 
        StringConverter.stringToNonNegativeMoney(TEST_SINGLE_PRICE).toString())
      .put(LISTING_PRICE, 
        StringConverter.stringToNonNegativeMoney(TEST_LISTING_PRICE).toString())
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
*/
}
