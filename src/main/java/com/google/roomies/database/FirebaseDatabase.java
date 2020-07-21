package com.google.roomies.database;

import static com.google.roomies.ProjectConstants.PROJECT_ID;
import static com.google.roomies.ListingRequestParameterNames.TIMESTAMP;

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
import com.google.roomies.Document;
import java.io.IOException;
import java.util.List;
import java.util.Map; 

public class FirebaseDatabase implements Database {
  private Firestore db;
  FirebaseDatabase() throws IOException {
    this.db = initializeDatabase();
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
  * Add a document to a collection using a map.
  *
  * @param collectionName name of collection in Firestore
  * @param doc document that implements the document interface
  */
  @Override
  public void addDocumentAsMap(String collectionName, Document doc) {
    Map<String, Object> data = doc.toMap();
    this.db.collection(collectionName).add(data);
  }

  /**
  * Add a document to a collection as a class.
  * All document fields must be serializable. The document class must implement
  * Serializable and have an empty constructor.
  *
  * @param collectionName name of collection in Firestore
  * @param doc document that implements the document interface
  */
  @Override
  public void addDocumentAsClass(String collectionName, Document doc) throws Exception {
    ApiFuture<DocumentReference> addedDocRef = this.db.collection(collectionName).add(doc);
    addedDocRef.get().update(TIMESTAMP, FieldValue.serverTimestamp());
  }

  /**
  * Update a document with the specified input fields.
  *
  * @param collectionName name of collection in Firestore
  * @param documentID ID of document to update in Firestore
  * @param fieldsToUpdate a map of <document key to update, new document value>. 
  */
  @Override
  public void updateDocument(String collectionName, String documentID, Map<String, Object> fieldsToUpdate) {
    DocumentReference docRef = this.db.collection(collectionName).document(documentID);
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
  public ApiFuture<DocumentSnapshot> getDocument(String collectionName, String documentID) throws Exception {
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
      String collectionName, String field, Object fieldValue) throws Exception {
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
  public ApiFuture<QuerySnapshot> getAllDocumentsInCollection(String collectionName) throws Exception { 
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