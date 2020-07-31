package com.google.roomies;

import static com.google.roomies.CommentConstants.COMMENT_COLLECTION_NAME;
import static com.google.roomies.CommentRequestParameterNames.LISTING_ID;
import static com.google.roomies.ProjectConstants.INDEX_URL;

import com.google.api.core.ApiFuture;
import com.google.cloud.Timestamp;
import com.google.cloud.firestore.DocumentReference;
import com.google.common.flogger.FluentLogger;
import com.google.roomies.database.NoSQLDatabase;
import com.google.roomies.database.DatabaseFactory;
import java.io.IOException;
import java.util.Arrays;
import java.util.Optional;
import java.util.concurrent.ExecutionException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletException;

/** Servlet that posts comments. */
@WebServlet("/comments")
public class CommentsServlet extends HttpServlet {
  private NoSQLDatabase database;
  private static final FluentLogger logger = FluentLogger.forEnclosingClass();

  @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response) throws 
      IOException {
    try {
      database = DatabaseFactory.getDatabase();

      Comment comment = Comment.fromServletRequest(request);
      String listingId = request.getParameter(LISTING_ID);
      database.addCommentAsMapToListing(comment, listingId);

      response.sendRedirect(INDEX_URL);
    } catch (IllegalStateException | IllegalArgumentException | InterruptedException
         | ExecutionException e) {
        logger.atInfo().withCause(e).log("Error posting comment: %s", e);
        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
    }
  }
}