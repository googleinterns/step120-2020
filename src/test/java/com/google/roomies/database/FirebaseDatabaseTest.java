package com.google.roomies.database;

import static com.google.roomies.CommentConstants.COMMENT_COLLECTION_NAME;
import static com.google.roomies.CommentRequestParameterNames.TIMESTAMP;
import static com.google.roomies.ListingConstants.BERKELEY_LOCATION;
import static com.google.roomies.ListingConstants.LISTING_COLLECTION_NAME;
import static com.google.roomies.ListingRequestParameterNames.START_DATE;
import static com.google.roomies.ListingRequestParameterNames.TITLE;
import static com.google.roomies.ProjectConstants.PROJECT_ID;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.anyDouble;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;

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
import com.google.cloud.firestore.Query.Direction;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;
import com.google.cloud.firestore.WriteResult;
import com.google.cloud.Timestamp;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.roomies.Comment;
import com.google.roomies.Listing;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
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
import java.util.concurrent.ExecutionException;

@RunWith(JUnit4.class)
public class FirebaseDatabaseTest {
  @Mock CollectionReference collectionMock;
  @Mock Firestore firestoreMock;
  @Mock DocumentReference docReferenceMock;
  @Mock DocumentSnapshot docSnapshotMock;
  @Mock QuerySnapshot querySnapshotMock;
  private Listing listing;
  private FirebaseDatabase database;
  private SettableApiFuture<DocumentReference> docReferenceFuture;
  private SettableApiFuture<DocumentSnapshot> docSnapshotFuture;
  private SettableApiFuture<QuerySnapshot> querySnapshotFuture;
  private static final String listingId = "testId";
  private static final String commentText = "test comment";
  private static final Comment comment = Comment.builder()
    .setCommentMessage(commentText)
    .build();

  @Before
  public void setUp() throws Exception {    
    MockitoAnnotations.initMocks(this);

    database = new FirebaseDatabase();
    database.setDatabaseForTest(firestoreMock);
    DatabaseFactory.setDatabaseForTest(database);

    docReferenceFuture = SettableApiFuture.create();
    docSnapshotFuture = SettableApiFuture.create();
    querySnapshotFuture = SettableApiFuture.create();

    when(firestoreMock.collection(Mockito.anyString())).thenReturn(collectionMock);
    when(collectionMock.document(Mockito.anyString())).thenReturn(docReferenceMock);
    when(docReferenceMock.get()).thenReturn(docSnapshotFuture);
    when(collectionMock.get()).thenReturn(querySnapshotFuture);
    docSnapshotFuture.set(docSnapshotMock);
    querySnapshotFuture.set(querySnapshotMock);
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
      .setNumSingles("0")
      .setSharedPrice("100")
      .setSinglePrice("10")
      .setListingPrice("100")
      .setLocationAndDistanceToCampus("37.3861", "-122.0839", BERKELEY_LOCATION)
      .build();
    ImmutableMap<String, Object> listingData = listing.toMap();
    
    database.addListingAsMap(listing);

    verify(firestoreMock, Mockito.times(1)).collection(LISTING_COLLECTION_NAME);
    verify(collectionMock, Mockito.times(1)).add(listingData);
  }

  @Test
  public void testAddCommentToListing_addsSingleCommentToFirestore() throws Exception {
    when(docSnapshotMock.exists()).thenReturn(true);
    ImmutableMap<String, Object> commentData = comment.toMap();
    when(collectionMock.add(commentData)).thenReturn(docReferenceFuture);
    when(docReferenceMock.collection(COMMENT_COLLECTION_NAME)).thenReturn(collectionMock);

    database.addCommentToListing(comment, listingId);

    verify(collectionMock, Mockito.times(1)).add(commentData);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testAddCommentToListing_listingIdIsInvalid_throwsIllegalArgumentException()
      throws InterruptedException, ExecutionException {
    when(docSnapshotMock.exists()).thenReturn(false);
    ImmutableMap<String, Object> commentData = comment.toMap();
    when(collectionMock.add(commentData)).thenReturn(docReferenceFuture);

    when(docReferenceMock.collection(COMMENT_COLLECTION_NAME)).thenReturn(collectionMock);

    database.addCommentToListing(comment, listingId);
  }

  @Test
  public void testUpdateListing_updatesListingWithSpecifiedFields() {
    String documentId = "myDocument";
    String newTitle = "New title";
    String newStartDate = "2020-10-09";
    Map<String, Object> fieldsToUpdate = ImmutableMap.of(TITLE, newTitle,
        START_DATE, newStartDate);
    when(collectionMock.document(documentId)).thenReturn(docReferenceMock);
    
    database.updateListing(documentId, fieldsToUpdate);

    verify(docReferenceMock, Mockito.times(1)).update(TITLE, newTitle);
    verify(docReferenceMock, Mockito.times(1)).update(START_DATE, newStartDate);
  }

  @Test
  public void testGetListing_getsSingleDocument() throws InterruptedException, 
      ExecutionException {
    String listingId = "myListing";
    when(collectionMock.document(listingId)).thenReturn(docReferenceMock);
    docSnapshotFuture.set(docSnapshotMock);

    ApiFuture<DocumentSnapshot> actualDocSnapshotFuture = 
      database.getListing(listingId);

    assertEquals(actualDocSnapshotFuture.get(), docSnapshotMock);
  }

  @Test
  public void testGetDocumentWithFieldValue_getsDocumentsWithSpecifiedFieldValue() 
      throws InterruptedException, ExecutionException {
    String collectionName = LISTING_COLLECTION_NAME;
    String field = TITLE;
    String fieldValue = "Test title";
    when(collectionMock.whereEqualTo(field, fieldValue))
      .thenReturn(collectionMock);
    when(collectionMock.whereEqualTo(field, fieldValue).get())
      .thenReturn(querySnapshotFuture);
    querySnapshotFuture.set(querySnapshotMock);
    
    ApiFuture<QuerySnapshot> actualQuerySnapshotFuture = 
      database.getDocumentsWithFieldValue(collectionName, field, fieldValue);

    assertEquals(actualQuerySnapshotFuture.get(), querySnapshotMock);
  }

  @Test
  public void testGetAllDocumentsInCollection_getsAllDocuments() throws 
      InterruptedException, ExecutionException{
    String collectionName = LISTING_COLLECTION_NAME;

    ApiFuture<QuerySnapshot> actualQuerySnapshotFuture = 
      database.getAllDocumentsInCollection(collectionName);

    assertEquals(actualQuerySnapshotFuture.get(), querySnapshotMock);
  }

  @Test
  public void testGetAllListingDocumentsUnderMaximumDistanceFromCampus() throws InterruptedException,
      ExecutionException {
    double maxDistance = 1;
    when(collectionMock.whereLessThanOrEqualTo(anyString(), anyDouble()))
      .thenReturn(collectionMock);

    ApiFuture<QuerySnapshot> actualQuerySnapshotFuture = 
      database.getAllListingDocumentsUnderMaximumDistanceFromCampus(maxDistance);

    assertEquals(actualQuerySnapshotFuture.get(), querySnapshotMock);
  }

  @Test
  public void testGetAllCommentDocumentsForListing_getsAllComments() throws 
      InterruptedException, ExecutionException {
    String listingId = "testId";
    when(docReferenceMock.collection(COMMENT_COLLECTION_NAME)).thenReturn(collectionMock);
    when(collectionMock.orderBy(TIMESTAMP, Direction.ASCENDING)).thenReturn(collectionMock);

    ApiFuture<QuerySnapshot> actualQuerySnapshotFuture = 
      database.getAllCommentDocumentsForListing(listingId);

    assertEquals(actualQuerySnapshotFuture.get(), querySnapshotMock);
  }
}