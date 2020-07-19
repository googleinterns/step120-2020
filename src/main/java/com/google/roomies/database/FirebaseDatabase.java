package com.google.roomies;

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
import java.io.IOException;
import java.util.List;
import java.util.Map; 

public class FirebaseDatabase implements Database {
  private Firestore db;
  public FirebaseDatabase() throws IOException {
    this.db = initializeDatabase();
  }

  /**
  * Gets the database instance for this project. Initializes database if it
  * has not been initialized.
  *
  * @return Firestore database instance
  * @throws IOException if database initialization fails.
  */
  @Override
  public Firestore getDatabase() throws IOException {
    if (this.db == null) {
      this.db = initializeDatabase();
    }
    return this.db;
  }

  /**
  * Sets the database instance for this project.
  * Use for setting database to be a mock database for unit testing.
  */
  @Override
  @VisibleForTesting
  public void setDatabaseForTest(Firestore database) {
    this.db = database;
  }

  /**
  * Add a document to a collection using a map.
  *
  * @param collectionName
  * @param doc 
  */
  @Override
  public void addDocumentAsMap(String collectionName, Document doc) {
    Map<String, Object> data = doc.toMap();
    this.db.collection(collectionName).add(data);
  }

  @Override
  public void addDocumentAsClass(String collectionName, Document doc) throws Exception {
    ApiFuture<DocumentReference> addedDocRef = this.db.collection(collectionName).add(doc);
    addedDocRef.get().update(TIMESTAMP, FieldValue.serverTimestamp());
  }

  /**
  * Update a document with the specified input fields.
  */
  @Override
  public void updateDocument(String collectionName, String documentID, Map<String, Object> fieldsToUpdate) {
    DocumentReference docRef = this.db.collection(collectionName).document(documentID);
    fieldsToUpdate.forEach((fieldName, fieldValue) -> 
        docRef.update(fieldName, fieldValue));
  }

  @Override
  public Map<String, Object> getDocumentAsMap(String collectionName, String documentID) throws Exception {
    DocumentReference docRef = this.db.collection(collectionName).document(documentID);
    ApiFuture<DocumentSnapshot> future = docRef.get();
    DocumentSnapshot document = future.get();
    if(document.exists()) {
      return document.getData();
    } else {
      throw new Exception("Error getting document");
    }
  }

  @Override
  public List<QueryDocumentSnapshot> getDocumentsWithFieldValue(
      String collectionName, String field, Object fieldValue) throws Exception {
    ApiFuture<QuerySnapshot> future =
      db.collection(collectionName).whereEqualTo(field, fieldValue).get();
    List<QueryDocumentSnapshot> documents = future.get().getDocuments();
    return documents;
  }
  
  @Override
  public List<QueryDocumentSnapshot> getAllDocumentsInCollection(String collectionName) throws Exception { 
    ApiFuture<QuerySnapshot> future = db.collection(collectionName).get();
    List<QueryDocumentSnapshot> documents = future.get().getDocuments();
    return documents;
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
