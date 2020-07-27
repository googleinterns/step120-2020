package com.google.roomies;

import com.google.cloud.Timestamp;
import com.google.cloud.firestore.annotation.DocumentId;
import com.google.cloud.firestore.annotation.ServerTimestamp;
import java.util.Optional;
import java.util.List;
import java.util.Objects;

public class City {
  @DocumentId
  private String documentId;

  @ServerTimestamp
  private Timestamp timestamp;
  private String listingId;
  private String comment;

  // [START fs_class_definition]
  public City() {
    // Must have a public no-argument constructor
  }

  // Initialize all fields of a city
  public City(String documentId, Timestamp timestamp, String listingId,
              String comment) {
    this.documentId = documentId;
    this.timestamp = timestamp;
    this.listingId = listingId;
    this.comment = comment;
  }
  // [END fs_class_definition]

  public City(String listingId, String comment) {
    this.listingId = listingId;
    this.comment = comment;
  }

  public String getDocumentId() {
    return documentId;
  }

  // public void setDocumentId(String documentId) {
  //   this.documentId = documentId;
  // }

  public Timestamp getTimestamp() {
    return timestamp;
  }

  // public void setTimestamp(Timestamp timestamp) {
  //   this.timestamp = timestamp;
  // }

  public String getlistingId() {
    return listingId;
  }

  public String getComment() {
    return comment;
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
        sb.append("City{");

    if (documentId != null) {
       sb.append("documentId=");
      sb.append(",");

      sb.append(documentId.toString());
    }
    if (timestamp != null) {
      sb.append("timestampId=");
      sb.append(timestamp);
      sb.append(",");

    }
    if (listingId != null) {
      sb.append("listingId=");
      sb.append(listingId);
      sb.append(",");

    }
    if (comment != null) {
      sb.append("comment=");
      sb.append(comment);
      sb.append(",");
    }
    sb.append("}");
    return sb.toString();
  }

  @Override
  public boolean equals(Object o) {
    if (o == this) {
      return true;
    }
    if (o instanceof City) {
      City that = (City) o;
      return this.documentId.equals(that.documentId)
          && this.timestamp.equals(that.timestamp)
          && this.listingId.equals(that.listingId)
          && this.comment.equals(that.comment);
    }
    return false;
  }

  @Override
  public int hashCode() {
    int h$ = 1;
    h$ *= 1000003;
    h$ ^= documentId.hashCode();
    h$ *= 1000003;
    h$ ^= timestamp.hashCode();
    h$ *= 1000003;
    h$ ^= listingId.hashCode();
    h$ *= 1000003;
    h$ ^= comment.hashCode();
    return h$;
  }

  public class CityBuilder {

  }
}