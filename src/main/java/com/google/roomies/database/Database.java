package com.google.roomies;

import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import java.io.IOException;
import java.util.List;
import java.util.Map; 

interface Database {
  /**
  * Gets the database instance.
  *
  * @throws IOException if database cannot be initialized
  */
  public Object getDatabase() throws IOException;

  /**
  * Sets the database instance.
  * Used to set database to a mock for testing.
  *
  * @throws IOException if database cannot be initialized
  */
  public void setDatabaseForTest(Firestore database);
  
  /**
  * Add a document to a collection using a map.
  *
  * @param collectionName name of collection in database
  * @param doc document that implements the document interface
  */  
  public void addDocumentAsMap(String collectionName, Document doc);

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
  public Map<String, Object> getDocumentAsMap(String collectionName, String documentID) throws Exception;

  /**
  * Get all documents with the input field value.
  *
  * @param collectionName name of collection in database
  * @param field document field to search
  * @param fieldValue value of field
  */
  public List<QueryDocumentSnapshot> getDocumentsWithFieldValue(String collectionName, String field, Object fieldValue) throws Exception;
  
  /**
  * Get all documents in specified collection.
  *
  * @param collectionName name of collection in database
  */
  public List<QueryDocumentSnapshot> getAllDocumentsInCollection(String collectionName) throws Exception;
}