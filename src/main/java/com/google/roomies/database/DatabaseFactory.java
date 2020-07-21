package com.google.roomies.database;

import com.google.cloud.firestore.Firestore;
import com.google.common.annotations.VisibleForTesting;
import com.google.roomies.database.Database;
import com.google.roomies.database.FirebaseDatabase;
import java.io.IOException;

public class DatabaseFactory {
  private static Database db;

  /**
  * Gets the database instance for this project.
  * Throws IOException if database cannot be initialized.
  */
  public static Database getDatabase() throws IOException {
    if (db == null) {
      db = new FirebaseDatabase();
    }
    return db;
  }
}