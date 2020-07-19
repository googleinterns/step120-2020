package com.google.roomies;

import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import java.io.IOException;
import java.util.List;
import java.util.Map; 

interface Database {
  public Object getDatabase() throws IOException;
  public void setDatabaseForTest(Firestore database);
  public void addDocumentAsMap(String collectionName, Document doc);
  public void addDocumentAsClass(String collectionName, Document doc) throws Exception;
  public void updateDocument(String collectionName, String documentID, Map<String, Object> fieldsToUpdate);
  public Map<String, Object> getDocumentAsMap(String collectionName, String documentID) throws Exception;
  public List<QueryDocumentSnapshot> getDocumentsWithFieldValue(String collectionName, String field, Object fieldValue) throws Exception;
  public List<QueryDocumentSnapshot> getAllDocumentsInCollection(String collectionName) throws Exception;
}