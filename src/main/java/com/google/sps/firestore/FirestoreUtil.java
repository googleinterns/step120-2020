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
import java.io.IOException;
import javax.servlet.ServletException;

/** Util methods for Firestore. */
public class FirestoreUtil {

  private static Firestore db;
  private FirestoreUtil() {}

  /**
  * Gets the database instance for this project. Initializes database if it
  * has not been initialized.
  * @return Firestore database instance
  * @throws IOException if database initialization fails.
  */
  public static Firestore getDatabase() {
    if (db == null) {
      try {
        db = initializeDatabase();
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
    return db;
  }

  /**
  * Sets the database instance for this project.
  * Use for setting database to be a mock database for unit testing.
  */
  public static void setDatabase(Firestore database) {
    db = database;
  }
  
  /**
  * Initializes database with the app's google credentials and project ID.
  * @return Firestore database instance
  */
  private static Firestore initializeDatabase() throws IOException {
    GoogleCredentials credentials = GoogleCredentials.getApplicationDefault();
    FirebaseOptions options = new FirebaseOptions.Builder()
      .setCredentials(credentials)
      .setProjectId(PROJECT_ID)
      .build();
    FirebaseApp.initializeApp(options);
    return FirestoreClient.getFirestore();
  }
}