package com.google.roomies;

import static com.google.roomies.CommentConstants.COMMENT_COLLECTION_NAME;
import static com.google.roomies.CommentRequestParameterNames.COMMENT;
import static com.google.roomies.CommentRequestParameterNames.LISTING_ID;
import static com.google.roomies.CommentRequestParameterNames.TIMESTAMP;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;

import com.google.api.core.ApiFuture;
import com.google.api.core.ApiFutures;
import com.google.cloud.firestore.CollectionReference;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.FieldValue;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.FirestoreOptions.Builder;
import com.google.cloud.firestore.Query;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;
import com.google.cloud.firestore.WriteResult;
import com.google.cloud.Timestamp;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.google.roomies.database.NoSQLDatabase;
import com.google.roomies.database.DatabaseFactory;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

@RunWith(JUnit4.class)
public class CommentsServletTest {
  @Mock NoSQLDatabase database;
  @Mock HttpServletRequest request;
  @Mock HttpServletResponse response;

  private Comment comment;
  private CommentsServlet commentsServlet;

  @Before
  public void setUp() throws ServletException, InterruptedException,
      ExecutionException {    
    MockitoAnnotations.initMocks(this);

    commentsServlet = new CommentsServlet();
    commentsServlet.init();

    DatabaseFactory.setDatabaseForTest(database);
  }

  @Test
  public void testPost_postsSingleComment() throws IOException, InterruptedException,
      ExecutionException {
    String listingId = "testListingId";
    when(request.getParameter(LISTING_ID)).thenReturn(listingId);
    when(request.getParameter(COMMENT)).thenReturn("Test comment");
    comment = Comment.fromServletRequest(request);
    
    commentsServlet.doPost(request, response);
    
    verify(database, Mockito.times(1)).addCommentToListing(comment, listingId);
  }

  @Test
  public void testPost_commentHasInvalidListingId_servletResponseIsSetToBadRequest() 
      throws IOException, InterruptedException, ExecutionException {
    String listingId = "invalidId";
    when(request.getParameter(LISTING_ID)).thenReturn(listingId);
    when(request.getParameter(COMMENT)).thenReturn("Test comment");
    doThrow(IllegalArgumentException.class).when(database)
      .addCommentToListing(any(Comment.class), eq(listingId));

    commentsServlet.doPost(request, response);

    verify(response).setStatus(400);
  }
}

