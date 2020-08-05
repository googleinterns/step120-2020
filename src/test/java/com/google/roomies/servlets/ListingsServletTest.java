package com.google.roomies;

import static com.google.roomies.ListingConstants.LISTING_COLLECTION_NAME;
import static com.google.roomies.ListingRequestParameterNames.DESCRIPTION;
import static com.google.roomies.ListingRequestParameterNames.END_DATE;
import static com.google.roomies.ListingRequestParameterNames.LAT;
import static com.google.roomies.ListingRequestParameterNames.LISTING_PRICE;
import static com.google.roomies.ListingRequestParameterNames.LEASE_TYPE;
import static com.google.roomies.ListingRequestParameterNames.LNG;
import static com.google.roomies.ListingRequestParameterNames.MAX_DISTANCE;
import static com.google.roomies.ListingRequestParameterNames.NUM_BATHROOMS;
import static com.google.roomies.ListingRequestParameterNames.NUM_ROOMS;
import static com.google.roomies.ListingRequestParameterNames.NUM_SHARED;
import static com.google.roomies.ListingRequestParameterNames.NUM_SINGLES;
import static com.google.roomies.ListingRequestParameterNames.SHARED_ROOM_PRICE;
import static com.google.roomies.ListingRequestParameterNames.SINGLE_ROOM_PRICE;
import static com.google.roomies.ListingRequestParameterNames.START_DATE;
import static com.google.roomies.ListingRequestParameterNames.TIMESTAMP;
import static com.google.roomies.ListingRequestParameterNames.TITLE;
import static com.google.roomies.ProjectConstants.PROJECT_ID;
import static org.hamcrest.CoreMatchers.containsString;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.*;

import com.google.api.core.ApiFuture;
import com.google.api.core.ApiFutures;
import com.google.api.core.SettableApiFuture;
import com.google.cloud.firestore.CollectionReference;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.FieldValue;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.FirestoreOptions.Builder;
import com.google.cloud.firestore.Query;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;
import com.google.cloud.firestore.WriteResult;
import com.google.cloud.Timestamp;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.google.roomies.database.NoSQLDatabase;
import com.google.roomies.database.DatabaseFactory;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
public class ListingsServletTest {
  @Mock QueryDocumentSnapshot listingQueryDocumentMock;
  @Mock QuerySnapshot listingQuerySnapshotMock;
  @Mock List<QueryDocumentSnapshot> listingQueryDocumentsMock;
  @Mock QueryDocumentSnapshot commentQueryDocumentMock;
  @Mock QuerySnapshot commentQuerySnapshotMock;
  @Mock List<QueryDocumentSnapshot> commentQueryDocumentsMock;

  @Mock NoSQLDatabase database;

  @Mock HttpServletRequest request;
  @Mock HttpServletResponse response;

  private SettableApiFuture<QuerySnapshot> listingQueryFutureMock;
  private SettableApiFuture<QuerySnapshot> commentQueryFutureMock;

  private Listing listing;
  private ListingsServlet listingsServlet;
  private StringWriter stringWriter;

  @Before
  public void setUp() throws Exception {    
    MockitoAnnotations.initMocks(this);

    listingsServlet = new ListingsServlet();
    listingsServlet.init();

    DatabaseFactory.setDatabaseForTest(database);

    listingQueryFutureMock = SettableApiFuture.create();
    commentQueryFutureMock = SettableApiFuture.create();
    
    List<QueryDocumentSnapshot> listingQueryDocumentList = 
      List.of(listingQueryDocumentMock);
    when(database.getAllDocumentsInCollection(LISTING_COLLECTION_NAME))
      .thenReturn(listingQueryFutureMock);
    when(database.getAllListingsUnderMaxDistance(anyDouble()))
      .thenReturn(listingQueryFutureMock);
    listingQueryFutureMock.set(listingQuerySnapshotMock);
    when(listingQuerySnapshotMock.getDocuments()).thenReturn(listingQueryDocumentsMock);
    when(listingQueryDocumentMock.getId()).thenReturn("documentID");
    when(listingQueryDocumentsMock.spliterator())
      .thenReturn(listingQueryDocumentList.spliterator());

    List<QueryDocumentSnapshot> commentQueryDocumentList = 
      List.of(commentQueryDocumentMock);
    when(database.getAllCommentsInListing(anyString()))
      .thenReturn(commentQueryFutureMock);
    commentQueryFutureMock.set(commentQuerySnapshotMock);
    when(commentQuerySnapshotMock.getDocuments()).thenReturn(commentQueryDocumentsMock);
    when(commentQueryDocumentMock.getId()).thenReturn("commentID");
    when(commentQueryDocumentsMock.spliterator())
      .thenReturn(commentQueryDocumentList.spliterator());

    stringWriter = new StringWriter();
    when(response.getWriter()).thenReturn(new PrintWriter(stringWriter));
  }
 
  @Test
  public void testGet_returnsSingleListing() throws Exception {
    when(request.getParameter(DESCRIPTION)).thenReturn("Test description");
    when(request.getParameter(END_DATE)).thenReturn("2020-07-10");
    when(request.getParameter(LEASE_TYPE)).thenReturn("YEAR_LONG");
    when(request.getParameter(NUM_BATHROOMS)).thenReturn("3");
    when(request.getParameter(NUM_ROOMS)).thenReturn("2");
    when(request.getParameter(NUM_SHARED)).thenReturn("1");
    when(request.getParameter(NUM_SINGLES)).thenReturn("0");
    when(request.getParameter(SHARED_ROOM_PRICE)).thenReturn("100");
    when(request.getParameter(SINGLE_ROOM_PRICE)).thenReturn("0");
    when(request.getParameter(LISTING_PRICE)).thenReturn("100");
    when(request.getParameter(START_DATE)).thenReturn("2020-07-10");
    when(request.getParameter(TITLE)).thenReturn("Test title");
    when(request.getParameter(LAT)).thenReturn("32");
    when(request.getParameter(LNG)).thenReturn("-102");
    when(request.getParameter(MAX_DISTANCE)).thenReturn("100");
    Map<String, Object> listingData = mapOfListingDataForGetTests(request);
    Map<String, Object> commentData = 
      mapOfCommentDataForGetTests(/* commentText = */ "Test comment");
    when(listingQueryDocumentMock.getData()).thenReturn(listingData);
    when(commentQueryDocumentMock.getData()).thenReturn(commentData);
 
    listingsServlet.doGet(request, response);
    String expectedWriterOutput =  "[{\"documentId\":{\"value\":\"documentID\"}," + 
      "\"timestamp\":{\"value\":{\"seconds\":1474156800,\"nanos\":0}},\"title\":"+
      "\"Test title\",\"description\":\"Test description\",\"startDate\":\"Jul 10," + 
      " 2020, 12:00:00 AM\",\"endDate\":\"Jul 10, 2020, 12:00:00 AM\",\"leaseType\":"+
      "\"YEAR_LONG\",\"numRooms\":2,\"numBathrooms\":2,\"numShared\":2,\"numSingles"+
      "\":2,\"sharedPrice\":\"USD 100\",\"singlePrice\":\"USD 0\",\"listingPrice\":"+
      "\"USD 100\",\"comments\":[{\"commentId\":{\"value\":\"commentID\"},\"timestamp\":"+
      "{\"value\":{\"seconds\":1474156800,\"nanos\":0}},\"commentMessage\":\"Test comment\"}]," + 
      "\"location\":{\"latitude\":32.0,\"longitude\":-102.0},\"milesToCampus\":1214.0765251676996}]";

    assertEquals(stringWriter.getBuffer().toString().trim(), expectedWriterOutput);
  }

  @Test
  public void testGet_noMaxDistanceSet_returnsAllListings() throws UnknownCurrencyException, 
      MonetaryParseException, NumberFormatException, ParseException, IOException {
    when(request.getParameter(DESCRIPTION)).thenReturn("Test description");
    when(request.getParameter(END_DATE)).thenReturn("2020-07-10");
    when(request.getParameter(LEASE_TYPE)).thenReturn("YEAR_LONG");
    when(request.getParameter(NUM_BATHROOMS)).thenReturn("3");
    when(request.getParameter(NUM_ROOMS)).thenReturn("2");
    when(request.getParameter(NUM_SHARED)).thenReturn("1");
    when(request.getParameter(NUM_SINGLES)).thenReturn("0");
    when(request.getParameter(SHARED_ROOM_PRICE)).thenReturn("100");
    when(request.getParameter(SINGLE_ROOM_PRICE)).thenReturn("0");
    when(request.getParameter(LISTING_PRICE)).thenReturn("100");
    when(request.getParameter(START_DATE)).thenReturn("2020-07-10");
    when(request.getParameter(TITLE)).thenReturn("Test title");
    when(request.getParameter(LAT)).thenReturn("32");
    when(request.getParameter(LNG)).thenReturn("-102");
    Map<String, Object> commentData = 
      mapOfCommentDataForGetTests(/* commentText = */ "Test comment");
    Map<String, Object> listingData = mapOfListingDataForGetTests(request);
    when(listingQueryDocumentMock.getData()).thenReturn(listingData);
    when(commentQueryDocumentMock.getData()).thenReturn(commentData);

    listingsServlet.doGet(request, response);
    String expectedWriterOutput = "[{\"documentId\":{\"value\":\"documentID\"}," + 
      "\"timestamp\":{\"value\":{\"seconds\":1474156800,\"nanos\":0}},\"title\":"+
      "\"Test title\",\"description\":\"Test description\",\"startDate\":\"Jul 10," + 
      " 2020, 12:00:00 AM\",\"endDate\":\"Jul 10, 2020, 12:00:00 AM\",\"leaseType\":"+
      "\"YEAR_LONG\",\"numRooms\":2,\"numBathrooms\":2,\"numShared\":2,\"numSingles"+
      "\":2,\"sharedPrice\":\"USD 100\",\"singlePrice\":\"USD 0\",\"listingPrice\":"+
      "\"USD 100\",\"comments\":[{\"commentId\":{\"value\":\"commentID\"},\"timestamp\":"+
      "{\"value\":{\"seconds\":1474156800,\"nanos\":0}},\"commentMessage\":\"Test comment\"}]," + 
      "\"location\":{\"latitude\":32.0,\"longitude\":-102.0},\"milesToCampus\":1214.0765251676996}]";

    assertEquals(stringWriter.getBuffer().toString().trim(), expectedWriterOutput);
  }

  @Test
  public void testGet_returnsNoListingsWithNoneInDatabasae() throws Exception {
    when(request.getParameter(MAX_DISTANCE)).thenReturn("100");
    when(listingQueryDocumentsMock.spliterator()).thenReturn(new ArrayList().spliterator());

    listingsServlet.doGet(request, response);

    assertEquals(stringWriter.getBuffer().toString().trim(), "[]");
  }

  @Test
  public void testGet_fetchedListingHasInvalidParams_returnsNoListings() throws Exception {
    String invalidLeaseType = "yearlong";
    when(request.getParameter(DESCRIPTION)).thenReturn("Test description");
    when(request.getParameter(END_DATE)).thenReturn("2020-07-10");
    when(request.getParameter(LEASE_TYPE)).thenReturn("YEAR_LONG");
    when(request.getParameter(NUM_BATHROOMS)).thenReturn("3");
    when(request.getParameter(NUM_ROOMS)).thenReturn("2");
    when(request.getParameter(NUM_SHARED)).thenReturn("1");
    when(request.getParameter(NUM_SINGLES)).thenReturn("0");
    when(request.getParameter(SHARED_ROOM_PRICE)).thenReturn("100");
    when(request.getParameter(SINGLE_ROOM_PRICE)).thenReturn("0");
    when(request.getParameter(LISTING_PRICE)).thenReturn("100");
    when(request.getParameter(START_DATE)).thenReturn("2020-07-10");
    when(request.getParameter(TITLE)).thenReturn("Test title");
    when(request.getParameter(LAT)).thenReturn("32");
    when(request.getParameter(LNG)).thenReturn("-102");
    when(request.getParameter(MAX_DISTANCE)).thenReturn("100");
    Map<String, Object> listingData = mapOfListingDataForGetTests(request);
    listingData.put(LEASE_TYPE, invalidLeaseType);
    when(listingQueryDocumentMock.getData()).thenReturn(listingData);

    listingsServlet.doGet(request, response);
    
    assertEquals(stringWriter.getBuffer().toString().trim(), "[]");
  }

  @Test
  public void testPost_postsSingleListing() throws Exception {
    when(request.getParameter(DESCRIPTION)).thenReturn("Test description");
    when(request.getParameter(END_DATE)).thenReturn("2020-07-10");
    when(request.getParameter(LEASE_TYPE)).thenReturn("YEAR_LONG");
    when(request.getParameter(NUM_BATHROOMS)).thenReturn("3");
    when(request.getParameter(NUM_ROOMS)).thenReturn("2");
    when(request.getParameter(NUM_SHARED)).thenReturn("1");
    when(request.getParameter(NUM_SINGLES)).thenReturn("0");
    when(request.getParameter(SHARED_ROOM_PRICE)).thenReturn("100");
    when(request.getParameter(SINGLE_ROOM_PRICE)).thenReturn("0");
    when(request.getParameter(LISTING_PRICE)).thenReturn("100");
    when(request.getParameter(START_DATE)).thenReturn("2020-07-10");
    when(request.getParameter(TITLE)).thenReturn("Test title");
    when(request.getParameter(LAT)).thenReturn("32");
    when(request.getParameter(LNG)).thenReturn("-102");
    listing = Listing.fromServletRequest(request);
    Map<String, Object> expectedData = listing.toMap();
    
    listingsServlet.doPost(request, response);
    
    verify(database, Mockito.times(1)).addListingAsMap(listing);
  }

  @Test
  public void testPost_requestHasUnparseableDates_servletResponseIsSetToBadRequest() throws Exception {
    String invalidEndDate = "202020/20/10";
    String invalidStartDate = "07/10/2020";
    when(request.getParameter(END_DATE)).thenReturn(invalidEndDate);
    when(request.getParameter(START_DATE)).thenReturn(invalidStartDate);
    when(request.getParameter(DESCRIPTION)).thenReturn("Test description");
    when(request.getParameter(LEASE_TYPE)).thenReturn("YEAR_LONG");
    when(request.getParameter(NUM_BATHROOMS)).thenReturn("3");
    when(request.getParameter(NUM_ROOMS)).thenReturn("2");
    when(request.getParameter(NUM_SHARED)).thenReturn("1");
    when(request.getParameter(NUM_SINGLES)).thenReturn("0");
    when(request.getParameter(SHARED_ROOM_PRICE)).thenReturn("100");
    when(request.getParameter(SINGLE_ROOM_PRICE)).thenReturn("0");
    when(request.getParameter(LISTING_PRICE)).thenReturn("100");
    when(request.getParameter(TITLE)).thenReturn("Test title");
    when(request.getParameter(LAT)).thenReturn("32");
    when(request.getParameter(LNG)).thenReturn("-102");

    listingsServlet.doPost(request, response);

    verify(database, Mockito.times(0)).addListingAsMap(any(Listing.class));
    verify(response).setStatus(400);
  }

  @Test
  public void testPost_requestHasInvalidLeaseType_servletResponseIsSetToBadRequest() throws Exception {
    String invalidLeaseType = "yearlong";
    when(request.getParameter(LEASE_TYPE)).thenReturn(invalidLeaseType);
    when(request.getParameter(DESCRIPTION)).thenReturn("Test description");
    when(request.getParameter(END_DATE)).thenReturn("2020-07-10");
    when(request.getParameter(NUM_BATHROOMS)).thenReturn("3");
    when(request.getParameter(NUM_ROOMS)).thenReturn("2");
    when(request.getParameter(NUM_SHARED)).thenReturn("1");
    when(request.getParameter(NUM_SINGLES)).thenReturn("0");
    when(request.getParameter(SHARED_ROOM_PRICE)).thenReturn("100");
    when(request.getParameter(SINGLE_ROOM_PRICE)).thenReturn("0");
    when(request.getParameter(LISTING_PRICE)).thenReturn("100");
    when(request.getParameter(START_DATE)).thenReturn("2020-07-10");
    when(request.getParameter(TITLE)).thenReturn("Test title");
    when(request.getParameter(LAT)).thenReturn("32");
    when(request.getParameter(LNG)).thenReturn("-102");

    listingsServlet.doPost(request, response);

    verify(database, Mockito.times(0)).addListingAsMap(any(Listing.class));
    verify(response).setStatus(400);
  }

  @Test
  public void testPost_requestHasInvalidListingPrice_servletResponseIsSetToBadRequest() throws Exception {
    String invalidListingPrice = "price";
    when(request.getParameter(LISTING_PRICE)).thenReturn(invalidListingPrice);
    when(request.getParameter(DESCRIPTION)).thenReturn("Test description");
    when(request.getParameter(END_DATE)).thenReturn("2020-07-10");
    when(request.getParameter(LEASE_TYPE)).thenReturn("YEAR_LONG");
    when(request.getParameter(NUM_BATHROOMS)).thenReturn("3");
    when(request.getParameter(NUM_ROOMS)).thenReturn("2");
    when(request.getParameter(NUM_SHARED)).thenReturn("1");
    when(request.getParameter(NUM_SINGLES)).thenReturn("0");
    when(request.getParameter(SHARED_ROOM_PRICE)).thenReturn("100");
    when(request.getParameter(SINGLE_ROOM_PRICE)).thenReturn("0");
    when(request.getParameter(START_DATE)).thenReturn("2020-07-10");
    when(request.getParameter(TITLE)).thenReturn("Test title");
    when(request.getParameter(LAT)).thenReturn("32");
    when(request.getParameter(LNG)).thenReturn("-102");

    listingsServlet.doPost(request, response);

    verify(database, Mockito.times(0)).addListingAsMap(any(Listing.class));
    verify(response).setStatus(400);
  }

  @Test
  public void testPost_requestHasInvalidSharedPrice_servletResponseIsSetToBadRequest() throws Exception {
    String invalidSharedPrice = "$3";
    when(request.getParameter(SHARED_ROOM_PRICE)).thenReturn(invalidSharedPrice);
    when(request.getParameter(DESCRIPTION)).thenReturn("Test description");
    when(request.getParameter(END_DATE)).thenReturn("2020-07-10");
    when(request.getParameter(LEASE_TYPE)).thenReturn("YEAR_LONG");
    when(request.getParameter(NUM_BATHROOMS)).thenReturn("3");
    when(request.getParameter(NUM_ROOMS)).thenReturn("2");
    when(request.getParameter(NUM_SHARED)).thenReturn("1");
    when(request.getParameter(NUM_SINGLES)).thenReturn("0");
    when(request.getParameter(SINGLE_ROOM_PRICE)).thenReturn("0");
    when(request.getParameter(LISTING_PRICE)).thenReturn("100");
    when(request.getParameter(START_DATE)).thenReturn("2020-07-10");
    when(request.getParameter(TITLE)).thenReturn("Test title");
    when(request.getParameter(LAT)).thenReturn("32");
    when(request.getParameter(LNG)).thenReturn("-102");

    listingsServlet.doPost(request, response);

    verify(database, Mockito.times(0)).addListingAsMap(any(Listing.class));
    verify(response).setStatus(400);
  }

  @Test
  public void testPost_requestHasInvalidSinglePrice_servletResponseIsSetToBadRequest() throws Exception {
    String invalidSinglePrice = "-.3";
    when(request.getParameter(SINGLE_ROOM_PRICE)).thenReturn(invalidSinglePrice);
    when(request.getParameter(DESCRIPTION)).thenReturn("Test description");
    when(request.getParameter(END_DATE)).thenReturn("2020-07-10");
    when(request.getParameter(LEASE_TYPE)).thenReturn("YEAR_LONG");
    when(request.getParameter(NUM_BATHROOMS)).thenReturn("3");
    when(request.getParameter(NUM_ROOMS)).thenReturn("2");
    when(request.getParameter(NUM_SHARED)).thenReturn("1");
    when(request.getParameter(NUM_SINGLES)).thenReturn("0");
    when(request.getParameter(SHARED_ROOM_PRICE)).thenReturn("100");
    when(request.getParameter(LISTING_PRICE)).thenReturn("100");
    when(request.getParameter(START_DATE)).thenReturn("2020-07-10");
    when(request.getParameter(TITLE)).thenReturn("Test title");
    when(request.getParameter(LAT)).thenReturn("32");
    when(request.getParameter(LNG)).thenReturn("-102");

    listingsServlet.doPost(request, response);

    verify(database, Mockito.times(0)).addListingAsMap(any(Listing.class));
    verify(response).setStatus(400);
  }

  /**
  * Creates and returns a map representation of a listing.
  * 
  * Note: Differs from Listing class's toMap() because this method is specifically
  * for get tests due to their specific requirements (expects a timestamp instead
  * of a FieldValue used in toMap(), expects certain date and number formats due to
  * Firestore's serialization process).
  */
  private Map<String, Object> mapOfListingDataForGetTests(HttpServletRequest request) 
      throws UnknownCurrencyException, MonetaryParseException, NumberFormatException, ParseException {
    Listing listing = Listing.fromServletRequest(request);
    Map<String, Object> listingData = Maps.newHashMap(listing.toMap());

    listingData.put(TIMESTAMP, Timestamp.parseTimestamp("2016-09-18T00:00:00Z"));
    listingData.put(START_DATE, "2020-07-10");
    listingData.put(END_DATE, "2020-07-10");
    listingData.put(NUM_ROOMS, ((Integer) 2).longValue());
    listingData.put(NUM_BATHROOMS, ((Integer) 2).longValue());
    listingData.put(NUM_SHARED, ((Integer) 2).longValue());
    listingData.put(NUM_SINGLES, ((Integer) 2).longValue());
    
    return listingData;
  }

  /**
  * Creates and returns a map representation of a comment.
  * 
  * Note: Differs from Comment class's toMap() because getter tests expect
  * a timestamp instead of the FieldValue used in toMap().
  */
  private Map<String, Object> mapOfCommentDataForGetTests(String commentText) {
    Comment comment = Comment.builder().setCommentMessage(commentText).build();
    Map<String, Object> commentData = Maps.newHashMap(comment.toMap());

    commentData.put(TIMESTAMP, Timestamp.parseTimestamp("2016-09-18T00:00:00Z"));
    return commentData;
  }
}
