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

/** A comment made by a user under a listing. */
@AutoValue
public abstract class Comment {
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

    /**
    * Builds Comment instance.
    * 
    * Verifies that listing id for the comment exists in database. If it does not,
    * Comment instance is not created and exception is thrown.
    */
    public Comment build() throws IOException, InterruptedException,
        ExecutionException {
      Comment comment = autoBuild();
      String errorMessage = String.format("Listing with id %s does not exist in " +
        "database. Comment with message %s cannot be created.", comment.listingId(),
        comment.comment());
      Preconditions.checkState(listingWithIdExists(comment.listingId()), errorMessage);
      return comment;
    }

    /**
    * Checks if listing ID exists in database.
    *
    * @return true if ID exists, false otherwise
    */
    private boolean listingWithIdExists(String listingId) throws IOException,
        InterruptedException, ExecutionException {
      ApiFuture<DocumentSnapshot> listing = 
        DatabaseFactory.getDatabase().getListing(listingId);
      return listing.get().exists();
    }
  }

  /**
  * Sets all comment values to the corresponding HTTP Servlet request parameter.
  */
  public static Comment fromServletRequest(HttpServletRequest request) throws
      IOException, InterruptedException, ExecutionException {
    return Comment.builder()
      .setListingId(request.getParameter(LISTING_ID))
      .setComment(request.getParameter(COMMENT))
      .build();
  }

  /**
  * Creates a map of <string key, value> of properties of a comment
  * that can be posted to the database.
  *
  * Note: putting Comment class in the database was explored but wasn't done
  * because database couldn't serialize timestamp/document IDs as optionals and 
  * would require moving away from Autovalue/value class to get timestamp and
  * document ID into the database.
  *
  * @return map of <string key (database key), value>   
  */
  public ImmutableMap<String, Object> toMap() {
    return ImmutableMap.<String, Object>builder()
      .put(LISTING_ID, listingId())
      .put(COMMENT, comment())
      .put(TIMESTAMP, FieldValue.serverTimestamp())
      .build();
  }
}
