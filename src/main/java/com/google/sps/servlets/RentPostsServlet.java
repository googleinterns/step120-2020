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

import static com.google.sps.Constants.PROJECT_ID;

import com.google.api.core.ApiFuture;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.Firestore;
import com.google.firebase.cloud.FirestoreClient;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletException;

/** Servlet that returns rent posts. */
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
    FirebaseOptions options = new FirebaseOptions.Builder()
      .setCredentials(credentials)
      .setProjectId(PROJECT_ID)
      .build();
    FirebaseApp.initializeApp(options);
    db = FirestoreClient.getFirestore();
  }

  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    response.setContentType("text/html;");
    response.getWriter().println("<h1>Hello world!</h1>");
  }

  @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
    Map<String, Object> data = new HashMap<>();
    data.put("name", "San Francisco");
    data.put("country", "United States");
    ApiFuture<DocumentReference> addedDocRef = db.collection("cities").add(data);
  }
}