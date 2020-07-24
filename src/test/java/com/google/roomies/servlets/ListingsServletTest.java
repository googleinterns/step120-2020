package com.google.roomies;

import static com.google.roomies.ListingConstants.LISTING_COLLECTION_NAME;
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
import static com.google.roomies.ProjectConstants.PROJECT_ID;
import static org.hamcrest.CoreMatchers.containsString;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.*;

import com.google.api.core.ApiFuture;
import com.google.api.core.ApiFutures;
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
  @Mock CollectionReference collectionMock;
  @Mock Firestore dbMock;
  @Mock HttpServletRequest request;
  @Mock HttpServletResponse response;
  @Mock NoSQLDatabase db;
  private Listing listing;
  private ListingsServlet listingsServlet;
  private StringWriter stringWriter;
  @Mock QueryDocumentSnapshot queryDocumentMock;
  @Mock ApiFuture<QuerySnapshot> futureMock;
  @Mock QuerySnapshot querySnapshotMock;
  @Mock List<QueryDocumentSnapshot> queryDocumentsMock;
  
  @Before
  public void setUp() throws Exception {    
    MockitoAnnotations.initMocks(this);

    listingsServlet = new ListingsServlet();
    listingsServlet.init();

    DatabaseFactory.setDatabaseForTest(db);
    setRequestParameters();

    defineDatabaseMockBehavior();

    stringWriter = new StringWriter();
    when(response.getWriter()).thenReturn(new PrintWriter(stringWriter));
  }
 
  @Test
  public void testGet_returnsSingleListing() throws Exception {
    listing = Listing.fromServletRequest(request);
    Map<String, Object> listingData = mapOfListingDataForGetTests(listing);
    List<QueryDocumentSnapshot> queryDocumentList = new ArrayList();
    queryDocumentList.add(queryDocumentMock);
    db.addListingAsMap(LISTING_COLLECTION_NAME, listing);
    when(queryDocumentsMock.spliterator()).thenReturn(queryDocumentList.spliterator());
    when(queryDocumentMock.getData()).thenReturn(listingData);
    when(queryDocumentMock.getId()).thenReturn("documentID");
 
    listingsServlet.doGet(request, response);
    String expectedWriterOutput =  "[{\"documentId\":{\"value\":\"documentID\"}," + 
      "\"timestamp\":{\"value\":{\"seconds\":1474156800,\"nanos\":0}},\"title\":\"Test title\"," +
      "\"description\":\"Test description\",\"startDate\":\"Jul 10, 2020, 12:00:00 AM\"," + 
      "\"endDate\":\"Jul 10, 2020, 12:00:00 AM\",\"leaseType\":\"YEAR_LONG\",\"numRooms\":2," +
      "\"numBathrooms\":2,\"numShared\":2,\"numSingles\":2,\"sharedPrice\":\"USD 100\","+
      "\"singlePrice\":\"USD 0\",\"listingPrice\":\"USD 100\"}]";

    verify(db, Mockito.times(1)).getAllDocumentsInCollection(LISTING_COLLECTION_NAME);
    verify(queryDocumentMock, Mockito.times(1)).getData();
    assertEquals(stringWriter.getBuffer().toString().trim(), expectedWriterOutput);
  }

  @Test
  public void testGet_returnsNoListingsWithNoneInFirestore() throws Exception {
    List<QueryDocumentSnapshot> queryDocumentList = new ArrayList();
    when(queryDocumentsMock.spliterator()).thenReturn(queryDocumentList.spliterator());

    listingsServlet.doGet(request, response);

    verify(db, Mockito.times(1)).getAllDocumentsInCollection(LISTING_COLLECTION_NAME);
    verify(queryDocumentMock, Mockito.times(0)).getData();
  }

  @Test
  public void testPost_postsSingleListing() throws Exception {
    listing = Listing.fromServletRequest(request);
    Map<String, Object> expectedData = listing.toMap();
    
    listingsServlet.doPost(request, response);
    
    verify(db, Mockito.times(1)).addListingAsMap(LISTING_COLLECTION_NAME, listing);
  }

  @Test
  public void testPost_requestHasUnparseableDates_servletResponseIsSetToBadRequest() throws Exception {
    String invalidEndDate = "202020/20/10";
    String invalidStartDate = "07/10/2020";
    when(request.getParameter(END_DATE)).thenReturn(invalidEndDate);
    when(request.getParameter(START_DATE)).thenReturn(invalidStartDate);

    listingsServlet.doPost(request, response);

    verify(db, Mockito.times(0)).addListingAsMap(eq(LISTING_COLLECTION_NAME), any(Listing.class));
    verify(response).setStatus(400);
  }

  @Test
  public void testPost_requestHasInvalidLeaseType_servletResponseIsSetToBadRequest() throws Exception {
    String invalidLeaseType = "yearlong";
    when(request.getParameter(LEASE_TYPE)).thenReturn(invalidLeaseType);
    
    listingsServlet.doPost(request, response);

    verify(db, Mockito.times(0)).addListingAsMap(eq(LISTING_COLLECTION_NAME), any(Listing.class));
    verify(response).setStatus(400);
  }

  @Test
  public void testPost_requestHasInvalidListingPrice_servletResponseIsSetToBadRequest() throws Exception {
    String invalidListingPrice = "price";
    when(request.getParameter(LISTING_PRICE)).thenReturn(invalidListingPrice);
 
    listingsServlet.doPost(request, response);

    verify(db, Mockito.times(0)).addListingAsMap(eq(LISTING_COLLECTION_NAME), any(Listing.class));
    verify(response).setStatus(400);
  }

  @Test
  public void testPost_requestHasInvalidSharedPrice_servletResponseIsSetToBadRequest() throws Exception {
    String invalidSharedPrice = "$3";
    when(request.getParameter(SHARED_ROOM_PRICE)).thenReturn(invalidSharedPrice);
    
    listingsServlet.doPost(request, response);

    verify(db, Mockito.times(0)).addListingAsMap(eq(LISTING_COLLECTION_NAME), any(Listing.class));
    verify(response).setStatus(400);
  }

  @Test
  public void testPost_requestHasInvalidSinglePrice_servletResponseIsSetToBadRequest() throws Exception {
    String invalidSinglePrice = "-.3";
    when(request.getParameter(SINGLE_ROOM_PRICE)).thenReturn(invalidSinglePrice);
  
    listingsServlet.doPost(request, response);

    verify(db, Mockito.times(0)).addListingAsMap(eq(LISTING_COLLECTION_NAME), any(Listing.class));
    verify(response).setStatus(400);
  }

  /**
  * Sets mock HTTP request's parameters to corresponding input values.
  */
  private void setRequestParameters() {
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
  }

  /**
  * Configure mock database instance behavior.
  */
  private void defineDatabaseMockBehavior() throws Exception {
    when(db.getAllDocumentsInCollection(LISTING_COLLECTION_NAME)).thenReturn(futureMock);
    when(futureMock.get()).thenReturn(querySnapshotMock);
    when(querySnapshotMock.getDocuments()).thenReturn(queryDocumentsMock);
  }

  /**
  * Creates and returns a map representation of a listing.
  * 
  * Note: Differs from Listing class's toMap() because this method is specifically
  * for get tests due to their specific requirements (expects a timestamp instead
  * of a FieldValue used in toMap(), expects certain date and number formats due to
  * Firestore's serialization process).
  */
  private Map<String, Object> mapOfListingDataForGetTests(Listing listing) {
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

}
