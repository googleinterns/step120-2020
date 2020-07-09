// Copyright 2019 Google LLC
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     https://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package com.google.sps;

import static com.google.sps.Constants.DATE_FORMAT;
import static com.google.sps.Constants.INDEX_URL;
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

import com.google.api.core.ApiFuture;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.FieldValue;
import com.google.cloud.firestore.Firestore;
import com.google.common.collect.ImmutableList;
import com.google.firebase.cloud.FirestoreClient;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import java.io.IOException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletException;

/** Servlet that posts and fetches rent posts. */
@WebServlet("/rent-posts")
public class RentPostsServlet extends HttpServlet {
  private Firestore db;

  @Override
  public void init() throws ServletException {
    GoogleCredentials credentials;
    try {
      credentials = GoogleCredentials.getApplicationDefault();
    } catch (Exception e) {
      throw new ServletException("Error fetching credentials", e);
    }
    db = getFirestoreDatabase(credentials, PROJECT_ID);
  }

  @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response) 
      throws IOException {
    RentPostDocument document = 
    new RentPostDocument(request.getParameter(REQUEST_DESCRIPTION), 
      stringToDate(request.getParameter(REQUEST_END_DATE)),
      request.getParameter(REQUEST_LEASE_TYPE), 
      request.getParameter(REQUEST_NUM_ROOMS),
      request.getParameter(REQUEST_PRICE),
      request.getParameter(REQUEST_ROOM_TYPE),
      stringToDate(request.getParameter(REQUEST_START_DATE)),
      request.getParameter(REQUEST_TITLE));

    db.collection(RENT_COLLECTION_NAME).add(document.toMap());

    response.sendRedirect(INDEX_URL);
  }
  
  private Firestore getFirestoreDatabase(GoogleCredentials credentials, String projectID) {
    FirebaseOptions options = new FirebaseOptions.Builder()
      .setCredentials(credentials)
      .setProjectId(projectID)
      .build();
    FirebaseApp.initializeApp(options);
    return FirestoreClient.getFirestore();
  }

  private Date stringToDate(String date) {
    SimpleDateFormat format = new SimpleDateFormat(DATE_FORMAT);
    Date convertedDate;
    try {
      convertedDate = format.parse(date);
    } catch(ParseException e) {
      e.printStackTrace();
      convertedDate = null;
    }
    return convertedDate;
  }
}