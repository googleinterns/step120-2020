package com.google.roomies;
 
import static com.google.roomies.CommentRequestParameterNames.COMMENT;
import static com.google.roomies.CommentRequestParameterNames.LISTING_ID;
import static com.google.roomies.CommentRequestParameterNames.TIMESTAMP;

import com.google.api.core.ApiFuture;
import com.google.auto.value.AutoValue;
import com.google.common.base.Preconditions;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.FieldValue;
import com.google.cloud.Timestamp;
import com.google.common.collect.ImmutableMap;
import com.google.roomies.database.DatabaseFactory;
import java.io.IOException;
import java.io.Serializable;
import java.util.Optional;
import java.util.concurrent.ExecutionException;
import javax.servlet.http.HttpServletRequest;
 
@AutoValue
public abstract class Comment implements Document {
  abstract Optional<String> documentId();
  abstract Optional<Timestamp> timestamp();
  abstract String listingId();
  abstract String comment();
 
  public static Builder builder() {
    return new AutoValue_Comment.Builder();
  }
 
  @AutoValue.Builder
  public abstract static class Builder implements Serializable {
    public abstract Builder setTimestamp(Optional<Timestamp> timestamp);
    public abstract Builder setDocumentId(Optional<String> documentId);
    public abstract Builder setListingId(String listingId);
    public abstract Builder setComment(String comment);

    abstract Comment autoBuild();

    public Comment build() throws IOException, InterruptedException,
        ExecutionException {
      Comment comment = autoBuild();
      String errorMessage = String.format("Listing with id %s does not exist in " +
        "database. Comment with message %s cannot be created.", comment.listingId(),
        comment.comment());
      Preconditions.checkState(listingWithIdExists(comment.listingId()), errorMessage);
      return comment;
    }
  }

  public static Comment fromServletRequest(HttpServletRequest request) throws
      IOException, InterruptedException, ExecutionException {
    String listingId = request.getParameter(LISTING_ID);
    String comment = request.getParameter(COMMENT);

    return Comment.builder()
      .setListingId(listingId)
      .setComment(comment)
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

  private static boolean listingWithIdExists(String listingId) throws IOException,
      InterruptedException, ExecutionException {
    ApiFuture<DocumentSnapshot> listing = 
      DatabaseFactory.getDatabase().getListing(listingId);
    return listing.get().exists();
  }
}
 

