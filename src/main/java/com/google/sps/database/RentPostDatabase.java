package com.google.sps;

import static com.google.sps.Constants.RENT_COLLECTION_NAME;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.FieldValue;
import com.google.cloud.firestore.Firestore;
import com.google.firebase.cloud.FirestoreClient;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.ServletException;

/** Handles database interaction (add, fetch) for rent posts. */
public class RentPostDatabase {
  /**
  * Add a rent post document to a collection using a map.
  * @param post New rent post. 
  */
  public void addPost(RentPost post) {
    Map<String, Object> postDocument = post.toMap();
    FirestoreUtil.getDatabase()
      .collection(RENT_COLLECTION_NAME)
      .add(postDocument);
  }
}