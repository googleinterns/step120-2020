package com.google.roomies.database;

import static com.google.roomies.CommentConstants.COMMENT_COLLECTION_NAME;
import static com.google.roomies.CommentRequestParameterNames.LISTING_ID;
import static com.google.roomies.ListingConstants.LISTING_COLLECTION_NAME;
import static com.google.roomies.ListingRequestParameterNames.TIMESTAMP;
import static com.google.roomies.ProjectConstants.PROJECT_ID;

import com.google.api.core.ApiFuture;
import com.google.api.core.ApiFutures;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.CollectionReference;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.FieldValue;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;
import com.google.cloud.firestore.WriteResult;
import com.google.common.annotations.VisibleForTesting;
import com.google.firebase.cloud.FirestoreClient;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.roomies.Comment;
import com.google.roomies.Document;
import com.google.roomies.Listing;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

/** A Firestore Database that implements the NoSQLDatabase interface. 
    Allows for fetching and posting of data to Firestore. */
public class FirebaseDatabase implements NoSQLDatabase {
  private Firestore db;
  FirebaseDatabase() throws IOException {
    if (FirebaseApp.getApps().isEmpty()) {
      this.db = initializeDatabase();
    }
  }

  /**
  * Sets the database instance for this project.
  * Use for setting database to a mock database instance for testing.
  */
  @VisibleForTesting
  public void setDatabaseForTest(Firestore database) {
    this.db = database;
  }

  @Override
  public void addListingAsMap(Listing listing) {
    Map<String, Object> data = listing.toMap();
    this.db.collection(LISTING_COLLECTION_NAME).add(data);
  }

  @Override
  public void addCommentToListing(Comment comment, String listingId) throws 
      InterruptedException, ExecutionException {
    if (!listingExists(listingId)) {
        String errorMessage = String.format("Listing with id %s does not exist in " +
        "database. Comment with message %s cannot be created.", listingId,
        comment.commentMessage());
      throw new IllegalArgumentException(errorMessage);
    }

    Map<String, Object> commentData = comment.toMap();
    this.db.collection(LISTING_COLLECTION_NAME)
        .document(listingId)
        .collection(COMMENT_COLLECTION_NAME)
        .add(commentData);
  }
  
  /**
  * Checks if listing with given listingId exists in Firestore.
  */
  private boolean listingExists(String listingId) throws InterruptedException,
      ExecutionException {
    return getListing(listingId).get().exists();
  }

  @Override
  public void updateListing(String documentID, Map<String, Object> fieldsToUpdate) {
    DocumentReference docRef = this.db.collection(LISTING_COLLECTION_NAME)
        .document(documentID);
    fieldsToUpdate.forEach((fieldName, fieldValue) -> 
        docRef.update(fieldName, fieldValue));
  }

  @Override
  public ApiFuture<DocumentSnapshot> getListing(String documentID) {
    DocumentReference docRef = this.db.collection(LISTING_COLLECTION_NAME)
      .document(documentID);
    return docRef.get();
  }

  @Override
  public ApiFuture<QuerySnapshot> getDocumentsWithFieldValue(
      String collectionName, String field, Object fieldValue)  {
    ApiFuture<QuerySnapshot> future =
      db.collection(collectionName).whereEqualTo(field, fieldValue).get();
    return future;
  }
  
  @Override
  public ApiFuture<QuerySnapshot> getAllDocumentsInCollection(String collectionName) { 
    return db.collection(collectionName).get();
  }
  
  /**
  * Initializes database with the app's google credentials and project ID.
  *
  * @return Firestore database instance
  */
  private Firestore initializeDatabase() throws IOException {
    GoogleCredentials credentials;
    credentials = GoogleCredentials.getApplicationDefault();

    FirebaseOptions options = new FirebaseOptions.Builder()
      .setCredentials(credentials)
      .setProjectId(PROJECT_ID)
      .build();
    FirebaseApp.initializeApp(options);
    return FirestoreClient.getFirestore();
  }
}