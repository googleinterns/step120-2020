package com.google.roomies.database;

import com.google.api.core.ApiFuture;
import com.google.api.core.ApiFutures;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;
import com.google.common.collect.ImmutableList;
import com.google.roomies.Comment;
import com.google.roomies.Document;
import com.google.roomies.Listing;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

/** A NoSQL Database interface. Specifies required fetch, update, and get methods
    for a database. */
public interface NoSQLDatabase {

  public void setDatabaseForTest(Firestore db);
  /**
  * Add a listing document to a collection using a map.
  *
  * @param listing instance of Listing class
  */  
  public void addListingAsMap(Listing listing);

  /**
  * Add a comment to a collection as a map.
  *
  * @param collectionName name of collection in database
  * @param comment a Comment instance 
  */
  public void addCommentAsMapToListing(Comment comment, String listingId) throws 
      InterruptedException, ExecutionException;

  /**
  * Update a listing document with the specified input fields.
  *
  * @param documentID ID of document to update in database
  * @param fieldsToUpdate a map of <document key to update, new document value>. 
  */
  public void updateListing(String documentID, Map<String, Object> fieldsToUpdate);

  /**
  * Checks if listing with input id exists in database.
  */
  public boolean listingExists(String listingId) throws InterruptedException,
      ExecutionException;
      
  /**
  * Get a listing document from database in a map of <key, value>.
  *
  * @param documentID ID of document to get from database
  */
  public ApiFuture<DocumentSnapshot> getListing(String documentID);

  /**
  * Get all documents with the input field value.
  *
  * @param collectionName name of collection in database
  * @param field document field to search
  * @param fieldValue value of field
  */
  public ApiFuture<QuerySnapshot> getDocumentsWithFieldValue(String collectionName, String field, Object fieldValue);
  
  /**
  * Get all documents in specified collection.
  *
  * @param collectionName name of collection in database
  */
  public ApiFuture<QuerySnapshot> getAllDocumentsInCollection(String collectionName);
}
