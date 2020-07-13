package com.google.sps;

import static com.google.sps.Constants.PROJECT_ID;
import static com.google.sps.Constants.RENT_COLLECTION_NAME;
import static com.google.sps.Constants.REQUEST_DESCRIPTION;
import static com.google.sps.Constants.REQUEST_END_DATE;
import static com.google.sps.Constants.REQUEST_LEASE_TYPE;
import static com.google.sps.Constants.REQUEST_NUM_ROOMS;
import static com.google.sps.Constants.REQUEST_PRICE;
import static com.google.sps.Constants.REQUEST_ROOM_TYPE;
import static com.google.sps.Constants.REQUEST_START_DATE;
import static com.google.sps.Constants.REQUEST_TIMESTAMP;
import static com.google.sps.Constants.REQUEST_TITLE;
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
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
public class RentPostsServletTest {
 
  @Mock HttpServletRequest request;
  @Mock HttpServletResponse response;
 
  private RentPostsServlet rentPostsServlet;
  @Mock Firestore dbMock;
  @Mock CollectionReference collectionMock;

  @Before
  public void setUp() throws Exception {    
    MockitoAnnotations.initMocks(this);

    rentPostsServlet = new RentPostsServlet();
    rentPostsServlet.init();
    FirestoreUtil.setDatabase(dbMock);
  }

  @Test
  public void testDatabaseMock_MockExistsAndIsUsedByServlet() throws Exception {
    assertNotNull(FirestoreUtil.getDatabase());
    assertEquals(dbMock, FirestoreUtil.getDatabase());
  }

  @Test
  public void testPost_postsSingleRentPost() throws Exception {
    String description = "Test description";
    String endDate = "2020-07-10";
    String leaseType = "YEAR_LONG";
    String numRooms = "1";
    String price = "100";
    String roomType = "SINGLE";
    String startDate = "2020-07-10";
    String title = "Test title";
    when(dbMock.collection(RENT_COLLECTION_NAME)).thenReturn(collectionMock);
    setRequestParameters(description, endDate, leaseType, numRooms,
      price, roomType, startDate, title);
    Map<String, Object> expectedData = getExpectedRentData(description, endDate,
      leaseType, numRooms, price, roomType, startDate, title);

    rentPostsServlet.doPost(request, response);

    verify(dbMock, Mockito.times(1)).collection(RENT_COLLECTION_NAME);
    verify(collectionMock, Mockito.times(1)).add(expectedData);
  }

  @Test
  public void testPost_requestHasUnparsableStartDate_postsWithNullDate() throws Exception {
    String description = "Test description";
    String endDate = "2020-07-10";
    String leaseType = "YEAR_LONG";
    String numRooms = "1";
    String price = "100";
    String roomType = "SINGLE";
    String startDate = "07/10/2020";
    String title = "Test title";
    when(dbMock.collection(RENT_COLLECTION_NAME)).thenReturn(collectionMock);
    setRequestParameters(description, endDate, leaseType, numRooms,
      price, roomType, startDate, title);
    Map<String, Object> expectedData = 
      getExpectedRentData(description, endDate, leaseType, numRooms, price, 
        roomType, startDate, title);
    expectedData.put(REQUEST_START_DATE, null);
    
    rentPostsServlet.doPost(request, response);

    verify(dbMock, Mockito.times(1)).collection(RENT_COLLECTION_NAME);
    verify(collectionMock, Mockito.times(1)).add(expectedData);
  }

  private void setRequestParameters(String description, String endDate, String leaseType,
      String numRooms, String price, String roomType, String startDate, String title) {
    when(request.getParameter(REQUEST_DESCRIPTION)).thenReturn(description);
    when(request.getParameter(REQUEST_END_DATE)).thenReturn(endDate);
    when(request.getParameter(REQUEST_LEASE_TYPE)).thenReturn(leaseType);
    when(request.getParameter(REQUEST_NUM_ROOMS)).thenReturn(numRooms);
    when(request.getParameter(REQUEST_PRICE)).thenReturn(price);
    when(request.getParameter(REQUEST_ROOM_TYPE)).thenReturn(roomType);
    when(request.getParameter(REQUEST_START_DATE)).thenReturn(startDate);
    when(request.getParameter(REQUEST_TITLE)).thenReturn(title);
  }

  private Map<String, Object> getExpectedRentData(String description, String endDate, String leaseType,
      String numRooms, String price, String roomType, String startDate, String title) {
    RentPost post = RentPost.builder()
      .setDescription(description)
      .setEndDate(endDate)
      .setLeaseType(leaseType)
      .setNumRooms(numRooms)
      .setPrice(price)
      .setRoomType(roomType)
      .setStartDate(startDate)
      .setTitle(title)
      .build();
    return post.toMap();
  }
}
