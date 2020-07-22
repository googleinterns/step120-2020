package com.google.roomies.database;

import com.google.cloud.firestore.Firestore;
import com.google.common.annotations.VisibleForTesting;
import com.google.roomies.database.NoSQLDatabase;
import com.google.roomies.database.FirebaseDatabase;
import java.io.IOException;

/** Database factory that contains the single NoSQLDatabase instance for the project. */
public class DatabaseFactory {
  private static NoSQLDatabase db;

  /**
  * Gets the database instance for this project.
  * Throws IOException if database cannot be initialized.
  */
  public static NoSQLDatabase getDatabase() throws IOException {
    if (db == null) {
      db = new FirebaseDatabase();
    }
    return db;
  }
}