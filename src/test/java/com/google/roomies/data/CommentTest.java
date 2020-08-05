package com.google.roomies;

import static com.google.roomies.CommentRequestParameterNames.COMMENT;
import static com.google.roomies.CommentRequestParameterNames.TIMESTAMP;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;

import com.google.cloud.firestore.FieldValue;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.cloud.Timestamp;
import com.google.common.collect.ImmutableMap;
import java.util.Map;
import java.util.Optional;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

@RunWith(JUnit4.class)
public class CommentTest {
  @Mock HttpServletRequest request;
  @Mock QueryDocumentSnapshot queryDocumentSnapshotMock;

  private static final String commentMessage = "Test message";
  private static final Comment comment = Comment.builder()
    .setCommentMessage(commentMessage)
    .build();

  @Before
  public void setUp() {
    MockitoAnnotations.initMocks(this);
  }

  @Test
  public void testFromServletRequest_returnsCommentWithAllValuesSet() {
    when(request.getParameter(COMMENT)).thenReturn(commentMessage);

    Comment actualComment = Comment.fromServletRequest(request);
    Comment expectedComment = comment;

    assertEquals(actualComment, expectedComment);
  }

  @Test
  public void testToMap_returnsMapOfCommentData() {
    Map<String, Object> actualData = comment.toMap();
    Map<String, Object> expectedData = ImmutableMap.<String, Object>builder()
      .put(COMMENT, commentMessage)
      .put(TIMESTAMP, FieldValue.serverTimestamp())
      .build();

    assertEquals(actualData, expectedData);
  }

  @Test
  public void testFromFirestore_returnsOptionalContainingComment() {
    String commentId = "commentId";
    Timestamp timestamp = Timestamp.parseTimestamp("2016-09-18T00:00:00Z");
    Map<String, Object> commentData = ImmutableMap.<String, Object>builder()
      .put(COMMENT, commentMessage)
      .put(TIMESTAMP, timestamp)
      .build();
    when(queryDocumentSnapshotMock.getData()).thenReturn(commentData);
    when(queryDocumentSnapshotMock.getId()).thenReturn(commentId);

    Optional<Comment> actualComment = Comment.fromFirestore(queryDocumentSnapshotMock);
    Optional<Comment> expectedComment = Optional.of(comment.toBuilder()
      .setCommentId(Optional.of(commentId))
      .setTimestamp(Optional.of(timestamp))
      .build());

    assertEquals(actualComment, expectedComment);
  }
}