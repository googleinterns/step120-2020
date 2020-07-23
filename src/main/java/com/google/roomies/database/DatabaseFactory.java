package com.google.roomies.database;

import com.google.cloud.firestore.Firestore;
import com.google.common.annotations.VisibleForTesting;
import com.google.roomies.database.NoSQLDatabase;
import com.google.roomies.database.FirebaseDatabase;
import java.io.IOException;

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

  public static void setDatabaseForTest(NoSQLDatabase database) {
    db = database;
  }
}