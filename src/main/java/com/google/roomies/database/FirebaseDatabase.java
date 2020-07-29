package com.google.roomies.database;

import static com.google.roomies.ListingConstants.LISTING_COLLECTION_NAME;
import static com.google.roomies.ListingRequestParameterNames.COMMENT_IDS;
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
import com.google.roomies.City;
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

  /**
  * Add a listing to a collection using a map.
  *
  * Note: Listings include JavaMoney variables that are currently not serializable 
  *       by Firestore. Adding listings as a custom class was explored and ultimately
  *       not used because of this restriction with JavaMoney.
  * 
  * @param collectionName name of collection in Firestore
  * @param listing an instance of Listing
  */
  @Override
  public void addListingAsMap(Listing listing) {
    Map<String, Object> data = listing.toMap();
    this.db.collection(LISTING_COLLECTION_NAME).add(data);
  }

  /**
  * Add a comment to a collection as a map.
  *
  * @param collectionName name of collection in database
  * @param comment a Comment instance 
  */
  @Override
  public void addCommentAsMap(String collectionName, Comment comment) {
    Map<String, Object> data = comment.toMap();
    this.db.collection(collectionName).add(data);
  }
  
  /**
  * Add comment ID to listing document.
  *
  * @param documentID ID of document to update in Firestore
  * @param commentId ID of comment to add under listing document 
  */
  @Override
  public void addCommentIDtoListing(String documentID, String commentId) {
    DocumentReference docRef = this.db.collection(LISTING_COLLECTION_NAME).document(documentID);
    docRef.update(COMMENT_IDS, FieldValue.arrayUnion(commentId));
  }

  /**
  * Update a listing document with the specified input fields.
  *
  * @param documentID ID of document to update in Firestore
  * @param fieldsToUpdate a map of <document key to update, new document value>. 
  */
  @Override
  public void updateListing(String documentID, Map<String, Object> fieldsToUpdate) {
    DocumentReference docRef = this.db.collection(LISTING_COLLECTION_NAME)
        .document(documentID);
    fieldsToUpdate.forEach((fieldName, fieldValue) -> 
        docRef.update(fieldName, fieldValue));
  }

  /**
  * Get a document from Firestore in a map of <key, value>.
  *
  * @param collectionName name of collection in Firestore
  * @param documentID ID of document to get from Firestore
  */
  @Override
  public ApiFuture<DocumentSnapshot> getDocument(String collectionName, String documentID) {
    DocumentReference docRef = this.db.collection(collectionName).document(documentID);
    return docRef.get();
  }

  /**
  * Get all documents with the input field value.
  *
  * @param collectionName name of collection in Firestore
  * @param field document field to search
  * @param fieldValue value of field
  */
  @Override
  public ApiFuture<QuerySnapshot> getDocumentsWithFieldValue(
      String collectionName, String field, Object fieldValue)  {
    ApiFuture<QuerySnapshot> future =
      db.collection(collectionName).whereEqualTo(field, fieldValue).get();
    return future;
  }
  
  /**
  * Get all documents in specified collection.
  *
  * @param collectionName name of collection in Firestore
  */
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