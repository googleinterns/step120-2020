 
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
import com.google.roomies.database.FirebaseDatabase;
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
public class ListingsDatabaseTest {
  @Mock CollectionReference collectionMock;
  @Mock Firestore dbMock;
  @Mock HttpServletRequest request;
  @Mock HttpServletResponse response;
  private Listing listing;
  private Database db;
  
  @Before
  public void setUp() throws Exception {    
    MockitoAnnotations.initMocks(this);

    db = DatabaseFactory.getDatabase();
    db.setDatabaseForTest(dbMock);
    when(dbMock.collection(LISTING_COLLECTION_NAME)).thenReturn(collectionMock);

    setRequestParameters();
  }

  @Test
  public void testAddDocumentAsMap_postsSingleListing() throws Exception {
    listing = Listing.fromServletRequest(request);
    Map<String, Object> expectedData = listing.toMap();
    
    db.addDocumentAsMap(LISTING_COLLECTION_NAME, response);

    verify(dbMock, Mockito.times(1)).collection(LISTING_COLLECTION_NAME);
    verify(collectionMock, Mockito.times(1)).add(expectedData);
  }

  @Test
  public void testPost_requestHasUnparseableDates_servletResponseIsSetToBadRequest() throws Exception {
    String invalidEndDate = "202020/20/10";
    String invalidStartDate = "07/10/2020";
    when(request.getParameter(END_DATE)).thenReturn(invalidEndDate);
    when(request.getParameter(START_DATE)).thenReturn(invalidStartDate);

    listingsServlet.doPost(request, response);

    verify(response).setStatus(400);
  }

  @Test
  public void testPost_requestHasInvalidLeaseType_servletResponseIsSetToBadRequest() throws Exception {
    String invalidLeaseType = "yearlong";
    when(request.getParameter(LEASE_TYPE)).thenReturn(invalidLeaseType);
    
    listingsServlet.doPost(request, response);

    verify(response).setStatus(400);
  }

  @Test
  public void testPost_requestHasInvalidListingPrice_servletResponseIsSetToBadRequest() throws Exception {
    String invalidListingPrice = "price";
    when(request.getParameter(LISTING_PRICE)).thenReturn(invalidListingPrice);
    
    listingsServlet.doPost(request, response);

    verify(response).setStatus(400);
  }

  @Test
  public void testPost_requestHasInvalidSharedPrice_servletResponseIsSetToBadRequest() throws Exception {
    String invalidSharedPrice = "$3";
    when(request.getParameter(SHARED_ROOM_PRICE)).thenReturn(invalidSharedPrice);
    
    listingsServlet.doPost(request, response);

    verify(response).setStatus(400);
  }

  @Test
  public void testPost_requestHasInvalidSinglePrice_servletResponseIsSetToBadRequest() throws Exception {
    String invalidSinglePrice = "-.3";
    when(request.getParameter(SINGLE_ROOM_PRICE)).thenReturn(invalidSinglePrice);
    
    listingsServlet.doPost(request, response);

    verify(response).setStatus(400);
  }

  @Test
  public void testPost_databaseDoesNotExist_servletResponseIsSetToBadRequest() throws Exception {
    db.setDatabaseForTest(null);
    
    listingsServlet.doPost(request, response);

    verify(response).setStatus(400);
  }