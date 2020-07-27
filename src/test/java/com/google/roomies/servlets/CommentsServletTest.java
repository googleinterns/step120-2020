package com.google.roomies;

import static com.google.roomies.CommentConstants.COMMENT_COLLECTION_NAME;
import static com.google.roomies.CommentRequestParameterNames.COMMENT;
import static com.google.roomies.CommentRequestParameterNames.LISTING_ID;
import static com.google.roomies.CommentRequestParameterNames.TIMESTAMP;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.*;

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
  @Mock Firestore dbMock;
  @Mock CollectionReference collectionMock;
  @Mock QueryDocumentSnapshot queryDocumentMock;
  @Mock ApiFuture<QuerySnapshot> futureMock;
  @Mock QuerySnapshot querySnapshotMock;
  @Mock List<QueryDocumentSnapshot> queryDocumentsMock;

  @Mock NoSQLDatabase db;

  @Mock HttpServletRequest request;
  @Mock HttpServletResponse response;

  private Comment comment;
  private CommentsServlet commentsServlet;

  @Before
  public void setUp() throws ServletException {    
    MockitoAnnotations.initMocks(this);

    commentsServlet = new CommentsServlet();
    commentsServlet.init();

    DatabaseFactory.setDatabaseForTest(db);
    setRequestParameters();
  }

  @Test
  public void testPost_postsSingleComment() {
    comment = Comment.fromServletRequest(request);
    
    commentsServlet.doPost(request, response);
    
    verify(db, Mockito.times(1)).addCommentAsMap(COMMENT_COLLECTION_NAME, comment);
  }

  /**
  * Sets mock HTTP request's parameters to corresponding input values.
  */
  private void setRequestParameters() {
    when(request.getParameter(LISTING_ID)).thenReturn("7YDcsjQOTzVoUxeXiysT");
    when(request.getParameter(COMMENT)).thenReturn("Test comment");
  }
}

