package com.google.roomies;
 
import static com.google.roomies.CommentRequestParameterNames.COMMENT;
import static com.google.roomies.CommentRequestParameterNames.LISTING_ID;
import static com.google.roomies.CommentRequestParameterNames.TIMESTAMP;

import com.google.auto.value.AutoValue;
import com.google.cloud.firestore.FieldValue;
import com.google.cloud.Timestamp;
import com.google.common.collect.ImmutableMap;
import java.io.Serializable;
import java.util.Optional;
import javax.servlet.http.HttpServletRequest;
 
@AutoValue
public abstract class Comment implements Document {
  abstract Optional<String> documentId();
  abstract Optional<Timestamp> timestamp();
  abstract String listingId();
  abstract String comment();
 
  static Builder builder() {
    return new AutoValue_Comment.Builder();
  }
 
  @AutoValue.Builder
  public abstract static class Builder implements Serializable {
    abstract Builder setTimestamp(Optional<Timestamp> timestamp);
    abstract Builder setDocumentId(Optional<String> documentId);
    abstract Builder setListingId(String listingId);
    abstract Builder setComment(String comment);

    abstract Comment build();
  }

   public static Comment fromServletRequest(HttpServletRequest request) {
    return Comment.builder()
    .setListingId(request.getParameter(LISTING_ID))
    .setComment(request.getParameter(COMMENT))
    .build();
  }

  @Override
  public ImmutableMap<String, Object> toMap() {
    ImmutableMap<String, Object> commentData = ImmutableMap.<String, Object>builder()
      .put(LISTING_ID, listingId())
      .put(COMMENT, comment())
      .put(TIMESTAMP, FieldValue.serverTimestamp())
      .build();
    return commentData;
  }
}
 

