package com.google.roomies.database;

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
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;

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
import com.google.roomies.Listing;
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
public class FirebaseDatabaseTest {
  @Mock CollectionReference collectionMock;
  @Mock Firestore firestoreMock;
  private Listing listing;
  private FirebaseDatabase database;
  
  @Before
  public void setUp() throws Exception {    
    MockitoAnnotations.initMocks(this);

    database = new FirebaseDatabase();
    database.setDatabaseForTest(firestoreMock);
    when(firestoreMock.collection(LISTING_COLLECTION_NAME)).thenReturn(collectionMock);
  }

  @Test
  public void testAddListingAsMap_addsSingleListingToFirestore() throws Exception {
    listing = Listing.builder()
      .setTitle("Test title")
      .setDescription("Test description")
      .setStartDate("2020-07-10")
      .setEndDate("2021-07-10")
      .setLeaseType("YEAR_LONG")
      .setNumRooms("2")
      .setNumBathrooms("3")
      .setNumShared("1")
      .setNumSingles("2")
      .setSharedPrice("100")
      .setSinglePrice("0")
      .setListingPrice("100")
      .build();
    Map<String, Object> expectedData = listing.toMap();
    
    database.addListingAsMap(LISTING_COLLECTION_NAME, listing);

    verify(firestoreMock, Mockito.times(1)).collection(LISTING_COLLECTION_NAME);
    verify(collectionMock, Mockito.times(1)).add(expectedData);
  }

  @Test(expected = ParseException.class)
  public void testAddListingAsMap_listingHasInvalidInput_exceptionThrownBeforeListingIsPosted()
     throws Exception {
    String invalidEndDate = "202020/20/10";
    String invalidStartDate = "07/10/2020";
    listing = Listing.builder()
      .setTitle("Test title")
      .setDescription("Test description")
      .setStartDate(invalidStartDate)
      .setEndDate(invalidEndDate)
      .setLeaseType("YEAR_LONG")
      .setNumRooms("2")
      .setNumBathrooms("3")
      .setNumShared("1")
      .setNumSingles("2")
      .setSharedPrice("100")
      .setSinglePrice("0")
      .setListingPrice("100")
      .build();

    database.addListingAsMap(LISTING_COLLECTION_NAME, listing);
  }
}