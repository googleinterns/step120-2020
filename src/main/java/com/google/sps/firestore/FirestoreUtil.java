package com.google.sps;

import static com.google.sps.Constants.PROJECT_ID;

import com.google.api.core.ApiFuture;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.FieldValue;
import com.google.cloud.firestore.Firestore;
import com.google.firebase.cloud.FirestoreClient;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import javax.servlet.ServletException;

/** Util methods for Firestore. */
public class FirestoreUtil {

  private static Firestore db;
  private FirestoreUtil() {}

  public static Firestore getDatabase() throws ServletException {
    if (db == null) {
      db = initializeDatabase();
    }
    return db;
  }
  
  private static Firestore initializeDatabase() throws ServletException {
    GoogleCredentials credentials = getCredentials();
    FirebaseOptions options = new FirebaseOptions.Builder()
      .setCredentials(credentials)
      .setProjectId(PROJECT_ID)
      .build();
    FirebaseApp.initializeApp(options);
    return FirestoreClient.getFirestore();
  }

  private static GoogleCredentials getCredentials() throws ServletException {
    try {
      return GoogleCredentials.getApplicationDefault();
    } catch (Exception e) {
      throw new ServletException("Error fetching credentials", e);
    }
  }
}