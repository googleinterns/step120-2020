package com.google.roomies;

import java.io.IOException;

public class DatabaseFactory {
  private static Database db;

  public static Database getDatabase() throws IOException {
    if (db == null) {
      db = new FirebaseDatabase();
    }
    return db;
  }
}