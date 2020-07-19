package com.google.roomies;

import java.util.Map; 

interface Document {
  /**
  * Converts document to a map of <database key, value> so that
  * it can be added to the database.
  */
  public Map<String, Object> toMap();
}