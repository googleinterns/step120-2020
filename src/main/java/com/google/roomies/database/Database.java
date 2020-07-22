package com.google.roomies.database;

import com.google.api.core.ApiFuture;
import com.google.api.core.ApiFutures;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;
import com.google.roomies.Document;
import java.io.IOException;
import java.util.List;
import java.util.Map; 

public interface Database {

  public void setDatabaseForTest(Firestore db);
  /**
  * Add a document to a collection using a map.
  *
  * @param collectionName name of collection in database
  * @param doc document that implements the document interface
  */  
  public ApiFuture<DocumentReference> addDocumentAsMap(String collectionName, Document doc);

  /**
  * Add a document to a collection as a class.
  * All document fields must be serializable. The document class must implement
  * Serializable and have an empty constructor.
  *
  * @param collectionName name of collection in database
  * @param doc document that implements the document interface
  */
  public void addDocumentAsClass(String collectionName, Document doc) throws Exception;

  /**
  * Update a document with the specified input fields.
  *
  * @param collectionName name of collection in database
  * @param documentID ID of document to update in database
  * @param fieldsToUpdate a map of <document key to update, new document value>. 
  */
  public void updateDocument(String collectionName, String documentID, Map<String, Object> fieldsToUpdate);

  /**
  * Get a document from database in a map of <key, value>.
  *
  * @param collectionName name of collection in database
  * @param documentID ID of document to get from database
  */
  public ApiFuture<DocumentSnapshot> getDocument(String collectionName, String documentID) throws Exception;

  /**
  * Get all documents with the input field value.
  *
  * @param collectionName name of collection in database
  * @param field document field to search
  * @param fieldValue value of field
  */
  public ApiFuture<QuerySnapshot> getDocumentsWithFieldValue(String collectionName, String field, Object fieldValue) throws Exception;
  
  /**
  * Get all documents in specified collection.
  *
  * @param collectionName name of collection in database
  */
  public ApiFuture<QuerySnapshot> getAllDocumentsInCollection(String collectionName) throws Exception;
}