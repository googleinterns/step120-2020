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
import com.google.roomies.database.Database;
import com.google.roomies.database.DatabaseFactory;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.ParseException;
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
  @Mock Database db;
  ApiFuture<DocumentReference> apiFuture;
  @Mock DocumentReference queryDocument;
  private Listing listing;
  private ListingsServlet listingsServlet;
 // private Database db;
 // private StringWriter stringWriter;
  
  @Before
  public void setUp() throws Exception {
    //DatabaseFactory.setDatabaseForTest(db);
    
    MockitoAnnotations.initMocks(this);

 //   stringWriter = new StringWriter();

    listingsServlet = new ListingsServlet();
    listingsServlet.init();
 //   db = new FirebaseDatabase();
    DatabaseFactory.setDatabaseForTest(db);
 //   db.setDatabaseForTest(dbMock);

    setRequestParameters();
  }

  // @Test
  // public void testGet_returnsSingleListing() throws Exception {
  //   listing = Listing.fromServletRequest(request);
  //       Map<String, Object> expectedData = listing.toMap();
  //   listingsServlet.doPost(request, response);
  //   when(response.getWriter()).thenReturn(new PrintWriter(stringWriter));

  //   listingsServlet.doGet(request, response);

  //   verify(dbMock, Mockito.times(2)).collection(LISTING_COLLECTION_NAME);
  //   verify(collectionMock, Mockito.times(1)).add(expectedData);

  //   verify(collectionMock, Mockito.times(1)).get();
  //   assertEquals(stringWriter.getBuffer(), "[\"Bob: Nice\"]");
  // }

  @Test
  public void testPost_postsSingleListing() throws Exception {
    listing = Listing.fromServletRequest(request);
    Map<String, Object> expectedData = listing.toMap();
    when(db.addDocumentAsMap(LISTING_COLLECTION_NAME, listing)).thenReturn(apiFuture);    
    
    listingsServlet.doPost(request, response);
    
    verify(db, Mockito.times(1)).addDocumentAsMap(LISTING_COLLECTION_NAME, listing);
    assertNotNull(apiFuture);
    assertNotNull(apiFuture.get().getId());
      //  when(apiFuture.get()).thenReturn(queryDocument);

 //   Map<String, Object> map = apiFuture.get().get();
  
//  assertEquals(db, null);
    // ApiFuture<QuerySnapshot> future = DatabaseFactory.getDatabase().getAllDocumentsInCollection(LISTING_COLLECTION_NAME);
    // assertEquals(future, null);
    // QuerySnapshot snapshot = future.get();
    // List<QueryDocumentSnapshot> documents = snapshot.getDocuments();

    // assertEquals(1, documents.size());
    // verify(dbMock, Mockito.times(1)).collection(LISTING_COLLECTION_NAME);
    // verify(collectionMock, Mockito.times(1)).add(expectedData);
  }

  // @Test
  // public void testPost_requestHasUnparseableDates_servletResponseIsSetToBadRequest() throws Exception {
  //   String invalidEndDate = "202020/20/10";
  //   String invalidStartDate = "07/10/2020";
  //   when(request.getParameter(END_DATE)).thenReturn(invalidEndDate);
  //   when(request.getParameter(START_DATE)).thenReturn(invalidStartDate);

  //   listingsServlet.doPost(request, response);

  //   verify(dbMock, Mockito.times(0)).collection(LISTING_COLLECTION_NAME);
  //   verify(response).setStatus(400);
  // }

  // @Test
  // public void testPost_requestHasInvalidLeaseType_servletResponseIsSetToBadRequest() throws Exception {
  //   String invalidLeaseType = "yearlong";
  //   when(request.getParameter(LEASE_TYPE)).thenReturn(invalidLeaseType);
    
  //   listingsServlet.doPost(request, response);

  //   verify(dbMock, Mockito.times(0)).collection(LISTING_COLLECTION_NAME);
  //   verify(response).setStatus(400);
  // }

  // @Test
  // public void testPost_requestHasInvalidListingPrice_servletResponseIsSetToBadRequest() throws Exception {
  //   String invalidListingPrice = "price";
  //   when(request.getParameter(LISTING_PRICE)).thenReturn(invalidListingPrice);
    
  //   listingsServlet.doPost(request, response);

  //   verify(dbMock, Mockito.times(0)).collection(LISTING_COLLECTION_NAME);
  //   verify(response).setStatus(400);
  // }

  // @Test
  // public void testPost_requestHasInvalidSharedPrice_servletResponseIsSetToBadRequest() throws Exception {
  //   String invalidSharedPrice = "$3";
  //   when(request.getParameter(SHARED_ROOM_PRICE)).thenReturn(invalidSharedPrice);
    
  //   listingsServlet.doPost(request, response);

  //   verify(dbMock, Mockito.times(0)).collection(LISTING_COLLECTION_NAME);
  //   verify(response).setStatus(400);
  // }

  // @Test
  // public void testPost_requestHasInvalidSinglePrice_servletResponseIsSetToBadRequest() throws Exception {
  //   String invalidSinglePrice = "-.3";
  //   when(request.getParameter(SINGLE_ROOM_PRICE)).thenReturn(invalidSinglePrice);
    
  //   listingsServlet.doPost(request, response);

  //   verify(dbMock, Mockito.times(0)).collection(LISTING_COLLECTION_NAME);
  //   verify(response).setStatus(400);
  // }

  // @Test
  // public void testPost_databaseDoesNotExist_servletResponseIsSetToBadRequest() throws Exception {
  //   db.setDatabaseForTest(null);
    
  //   listingsServlet.doPost(request, response);

  //   verify(dbMock, Mockito.times(0)).collection(LISTING_COLLECTION_NAME);
  //   verify(response).setStatus(400);
  // }

  /**
  * Sets mock HTTP request's parameters to corresponding input values.
  */
  private void setRequestParameters() {
    String description = "Test description";
    String endDate = "2020-07-10";
    String leaseType = "YEAR_LONG";
    String numBathrooms = "3";
    String numRooms = "2";
    String numShared = "1";
    String numSingles = "0";
    String sharedPrice = "100";
    String singlePrice = "0";
    String listingPrice = "100";
    String startDate = "2020-07-10";
    String title = "Test title";

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
  }
}