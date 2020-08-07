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
package com.google.roomies;

import static com.google.roomies.ProjectConstants.INDEX_URL;
import static com.google.roomies.ListingConstants.LISTING_COLLECTION_NAME;
import static com.google.roomies.ListingConstants.RESPONSE_CONTENT_TYPE;
import static com.google.roomies.ListingRequestParameterNames.DESCRIPTION;
import static com.google.roomies.ListingRequestParameterNames.END_DATE;
import static com.google.roomies.ListingRequestParameterNames.LISTING_PRICE;
import static com.google.roomies.ListingRequestParameterNames.LEASE_TYPE;
import static com.google.roomies.ListingRequestParameterNames.MAXIMUM_DISTANCE_IN_MILES_FROM_CAMPUS;
import static com.google.roomies.ListingRequestParameterNames.NUM_BATHROOMS;
import static com.google.roomies.ListingRequestParameterNames.NUM_ROOMS;
import static com.google.roomies.ListingRequestParameterNames.NUM_SHARED;
import static com.google.roomies.ListingRequestParameterNames.NUM_SINGLES;
import static com.google.roomies.ListingRequestParameterNames.SHARED_ROOM_PRICE;
import static com.google.roomies.ListingRequestParameterNames.SINGLE_ROOM_PRICE;
import static com.google.roomies.ListingRequestParameterNames.START_DATE;
import static com.google.roomies.ListingRequestParameterNames.TIMESTAMP;
import static com.google.roomies.ListingRequestParameterNames.TITLE;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.FieldValue;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.common.base.Joiner;
import com.google.common.collect.Streams;
import com.google.common.flogger.FluentLogger;
import com.google.firebase.cloud.FirestoreClient;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.roomies.database.NoSQLDatabase;
import com.google.roomies.database.DatabaseFactory;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;
import javax.money.Monetary;
import javax.money.UnknownCurrencyException;
import javax.money.format.MonetaryParseException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletException;
import org.javamoney.moneta.Money;

/** Servlet that posts and fetches listings. */
@WebServlet("/listings")
public class ListingsServlet extends HttpServlet {
  private NoSQLDatabase database;
  private static final FluentLogger logger = FluentLogger.forEnclosingClass();

  @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
    database = DatabaseFactory.getDatabase();
    try {
      Listing post = Listing.fromServletRequest(request);

      database.addListingAsMap(post);

      response.sendRedirect(INDEX_URL);
    } catch (IllegalArgumentException | UnknownCurrencyException | 
          MonetaryParseException | ParseException e) {
        logger.atInfo().withCause(e).log("Error posting: %s", e);
        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
    }
  }

  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response)
      throws IOException {
    database = DatabaseFactory.getDatabase();
    try {
      List<Listing> listings = getListingsGivenRequestFilters(request);

      response.setContentType(RESPONSE_CONTENT_TYPE);
      response.getWriter().println(convertToJsonUsingGson(listings));
    } catch (InterruptedException | ExecutionException e) {
      logger.atInfo().withCause(e).log("Error fetching: %s", e);
      response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
      response.setContentType("text/html");
      response.getWriter().println("request failed");
    }
  }

  /**
  * Gets all listings given filter parameters from servlet request.
  */
  private List<Listing> getListingsGivenRequestFilters(HttpServletRequest request) 
      throws InterruptedException, ExecutionException {
    List<Listing> listings;
    String maximumDistanceInMilesFromCampus = 
      request.getParameter(MAXIMUM_DISTANCE_IN_MILES_FROM_CAMPUS);

    if (requestParameterHasValidInput(maximumDistanceInMilesFromCampus)) {
      Double distanceFromCampus = Double.parseDouble(maximumDistanceInMilesFromCampus);
      listings = getAllListingsUnderMaximumDistanceFromCampus(distanceFromCampus);
    } else {
      listings = getAllListingsFromCollection();
    }
    return listings;
  }

  private boolean requestParameterHasValidInput(String requestParameterInput) {
    Optional<String> parameterInput = Optional.ofNullable(requestParameterInput);
    
    return parameterInput.isPresent() && !parameterInput.equals("");
  }

  private String convertToJsonUsingGson(List data) {
    Gson gson = new GsonBuilder()
                  .registerTypeAdapter(Money.class, new MoneySerializer())
                  .create();
    return gson.toJson(data);
  }

  /**
  * Gets all listings from the listing collection in database.
  * 
  * @return list of Listing instances
  */
  private List<Listing> getAllListingsFromCollection() throws InterruptedException, ExecutionException {
    List<QueryDocumentSnapshot> documents = 
      database.getAllDocumentsInCollection(LISTING_COLLECTION_NAME).get().getDocuments();

    return documentsToListings(documents);
  }

  /**
  * Gets all listings under given max distance.
  */
  private List<Listing> getAllListingsUnderMaximumDistanceFromCampus(Double 
      maximumDistanceFromCampus) throws InterruptedException, ExecutionException {
    List<QueryDocumentSnapshot> documents = 
      database.getAllListingDocumentsUnderMaximumDistanceFromCampus(maximumDistanceFromCampus)
      .get()
      .getDocuments();

    return documentsToListings(documents);
  }

  private List<Listing> documentsToListings(List<QueryDocumentSnapshot> documents) {
    return StreamSupport.stream(documents.spliterator(), /* parallel= */ false)
      .map(this::getListingFromDocument)
      .flatMap(Optional::stream)
      .collect(Collectors.toList());
  }

  /**
  * Creates Listing instance given a document from the database and returns 
  * an optional of the instance.
  * 
  * @param document a QueryDocumentSnapshot from database
  * @return Optional containing the Listing instance or an empty optional 
  *   if Listing could not be created.
  */
  private Optional<Listing> getListingFromDocument(QueryDocumentSnapshot document) {
    try {
      return Listing.fromFirestore(document);
    } catch (UnknownCurrencyException | MonetaryParseException | IllegalArgumentException
       | ParseException | IOException | InterruptedException | ExecutionException e) {
      String errorMessage = String.format("Error fetching listing %s with exception: %s",
          mapToString(document.getData()), e);
      logger.atInfo().withCause(e).log(errorMessage);
      return Optional.<Listing>empty();
    }
  }

  private String mapToString(Map<String, Object> map) {
    return Joiner.on(",").withKeyValueSeparator("=").join(map);
  }
}
