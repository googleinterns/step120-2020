package com.google.roomies;

import static com.google.roomies.CommentConstants.COMMENT_COLLECTION_NAME;
import static com.google.roomies.ListingConstants.CURRENCY_CODE;
import static com.google.roomies.ListingConstants.DATE_FORMAT;
import static com.google.roomies.ListingConstants.LISTING_COLLECTION_NAME;
import static com.google.roomies.ListingRequestParameterNames.DESCRIPTION;
import static com.google.roomies.ListingRequestParameterNames.END_DATE;
import static com.google.roomies.ListingRequestParameterNames.LEASE_TYPE;
import static com.google.roomies.ListingRequestParameterNames.NUM_BATHROOMS;
import static com.google.roomies.ListingRequestParameterNames.NUM_ROOMS;
import static com.google.roomies.ListingRequestParameterNames.NUM_SHARED;
import static com.google.roomies.ListingRequestParameterNames.NUM_SINGLES;
import static com.google.roomies.ListingRequestParameterNames.SHARED_ROOM_PRICE;
import static com.google.roomies.ListingRequestParameterNames.SINGLE_ROOM_PRICE;
import static com.google.roomies.ListingRequestParameterNames.LISTING_PRICE;
import static com.google.roomies.ListingRequestParameterNames.START_DATE;
import static com.google.roomies.ListingRequestParameterNames.TIMESTAMP;
import static com.google.roomies.ListingRequestParameterNames.TITLE;

import com.google.auto.value.AutoValue;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.FieldValue;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.cloud.Timestamp;
import com.google.common.base.Joiner;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.roomies.database.DatabaseFactory;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.io.IOException;
import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;
import javax.annotation.Nullable;
import javax.money.CurrencyUnit;
import javax.money.Monetary;
import javax.money.UnknownCurrencyException;
import javax.money.format.MonetaryParseException;
import javax.servlet.http.HttpServletRequest;
import org.javamoney.moneta.Money;

/** A listing made by a user. */
@AutoValue
public abstract class Listing implements Document, Serializable {
  /**
  * Describes the two different lease types for a listing.
  * If input is not one of these two options, servlet throws an error.
  */
  enum LeaseType {
    YEAR_LONG, 
    MONTH_TO_MONTH;
  }

  abstract Optional<String> documentId();
  abstract Optional<Timestamp> timestamp();
  abstract String title();
  abstract String description();
  abstract Date startDate();
  abstract Date endDate();
  abstract LeaseType leaseType();
  abstract int numRooms();
  abstract int numBathrooms();
  /**
  * "Shared" encompasses any room type that isn't a single.
  * Users with different types of shared rooms with different
  * prices should make separate posts or put an average cost of 
  * the shared room types as the shared price.
  */
  abstract int numShared();
  abstract int numSingles();
  abstract Money sharedPrice();
  abstract Money singlePrice();
  abstract Money listingPrice();
  abstract ImmutableList<Comment> comments();

  abstract Builder toBuilder();

  /**
  * Returns Builder for Listing with the default
  * value of the comments list set as an empty
  * list. 
  */
  public static Builder builder() {
    return new AutoValue_Listing.Builder()
      .setComments(ImmutableList.<Comment>of());
  }

  @AutoValue.Builder
  public abstract static class Builder implements Serializable {
    public abstract Builder setTimestamp(Optional<Timestamp> timestamp);
    public abstract Builder setDocumentId(Optional<String> documentId);
    public abstract Builder setTitle(String title);
    public abstract Builder setDescription(String description);
    abstract Builder setStartDate(Date startDate);
    abstract Builder setEndDate(Date endDate);
    abstract Builder setLeaseType(LeaseType leaseType);
    abstract Builder setNumRooms(int numRooms);
    abstract Builder setNumBathrooms(int numBathrooms);
    abstract Builder setNumShared(int numShared);
    abstract Builder setNumSingles(int numSingles);
    abstract Builder setSharedPrice(Money sharedPrice);
    abstract Builder setSinglePrice(Money singlePrice);
    abstract Builder setListingPrice(Money listingPrice);
    abstract Builder setComments(ImmutableList<Comment> comments);
    abstract LeaseType leaseType();
    abstract int numRooms();

    public abstract Listing build();
    
    /**
    * Sets the lease type to a Lease Type enum value given a string representation of
    * the lease type.
    * 
    * Input should match a LeaseType enum (ex. "YEAR_LONG").
    * @throws IllegalArgumentException if input does not match a LeaseType enum 
    * value (case sensistive).
    */
    public Builder setLeaseType(String leaseType) {
      setLeaseType(LeaseType.valueOf(leaseType));
      return this;
    }

    /**
    * Sets the number of bedrooms to an integer given a string representation of
    * the number.
    *
    * Input should be a non-negative integer (ex. "2").
    * @throws NumberFormatException if input is not parseable.
    */
    public Builder setNumRooms(String numRooms) {
      setNumRooms(Integer.parseInt(numRooms));
      return this;
    }

    /**
    * Sets the number of bathrooms to an integer given a string representation of
    * the number.
    *
    * Input should be a non-negative integer (ex. "2").
    * @throws NumberFormatException if input is not parseable.
    */
    public Builder setNumBathrooms(String numBathrooms) {
      setNumBathrooms(Integer.parseInt(numBathrooms));
      return this;     
    }

    /**
    * Sets the number of shared bedrooms to an integer given a string representation
    * of the number.
    *
    * Input should be a non-negative integer (ex. "2").
    * @throws NumberFormatException if input is not parseable.
    */
    public Builder setNumShared(String numShared) {
      setNumShared(Integer.parseInt(numShared));
      return this;     
    }

    /**
    * Sets the number of singles to an integer given a string representation of
    * the number.
    *
    * Input should be a non-negative integer (ex. "2").
    * @throws NumberFormatException if input is not parseable.
    */
    public Builder setNumSingles(String numSingles) {
      setNumSingles(Integer.parseInt(numSingles));
      return this;     
    }

    /**
    * Sets the start date to be a Date given a string representation of the start date.
    *
    * Input should be in the format "yyyy-MM-dd" (ex. "2020-07-20").
    * @throws ParseException if date is not in correct format.
    */
    public Builder setStartDate(String startDate) throws ParseException {
      setStartDate(StringConverter.stringToDate(startDate));
      return this;
    }

    /**
    * Sets the end date to be a Date given a string representation of the end date.
    *
    * Input should be in the format "yyyy-MM-dd" (ex. "2020-07-20").
    * @throws ParseException if date is not in correct format.
    */
    public Builder setEndDate(String endDate) throws ParseException {
      setEndDate(StringConverter.stringToDate(endDate));
      return this;
    }

    /**
    * Sets the shared room price (of type Money) given a string representation of
    * the price.  
    *
    * Input should be a postive number corresponding to a valid USD dollar amount,
    * without the $ sign (ex. "300")
    * @throws Exception if price is not in correct format.
    */
    public Builder setSharedPrice(String sharedPrice) throws UnknownCurrencyException,
        MonetaryParseException, NumberFormatException {
      setSharedPrice(StringConverter.stringToNonNegativeMoney(sharedPrice));
      return this;
    }

    /**
    * Sets the single room price (of type Money) given a string representation of
    * the price.  
    *
    * Input should be a postive number corresponding to a valid USD dollar amount,
    * without the $ sign (ex. "300")
    * @throws Exception if price is not in correct format.
    */
    public Builder setSinglePrice(String singlePrice) throws UnknownCurrencyException,
        MonetaryParseException, NumberFormatException {
      setSinglePrice(StringConverter.stringToNonNegativeMoney(singlePrice));
      return this;
    }

    /**
    * Sets the listing price (of type Money) given a string representation of
    * the price.  
    *
    * Input should be a postive number corresponding to a valid USD dollar amount,
    * without the $ sign (ex. "300")
    * @throws Exception if price is not in correct format.
    */
    public Builder setListingPrice(String listingPrice) throws UnknownCurrencyException,
        MonetaryParseException, NumberFormatException {
      setListingPrice(StringConverter.stringToNonNegativeMoney(listingPrice));
      return this;
    }
 
  }

  /**
  * Creates a map of <string key, value> of properties of a listing
  * that can be posted to the database.
  *
  * @return map of <string key (database key), value>   
  */
  public ImmutableMap<String, Object> toMap() {
    ImmutableMap<String, Object> listingData = ImmutableMap.<String, Object>builder()
      .put(TITLE, title())
      .put(DESCRIPTION, description())
      .put(START_DATE, startDate())
      .put(END_DATE, endDate())
      .put(LEASE_TYPE, leaseType().toString())
      .put(NUM_ROOMS, numRooms())
      .put(NUM_BATHROOMS, numBathrooms())
      .put(NUM_SHARED, numShared())
      .put(NUM_SINGLES, numSingles())
      .put(SHARED_ROOM_PRICE, sharedPrice().toString())
      .put(SINGLE_ROOM_PRICE, singlePrice().toString())
      .put(LISTING_PRICE, listingPrice().toString())
      .put(TIMESTAMP, FieldValue.serverTimestamp())
      .build();

    return listingData;
  }

    /**
    * Sets all listing values to the corresponding HTTP Servlet request parameter.
    *
    * Note: Using a map representation of listing data when fetched
    * (and posted) from Firestore due to serialization problem with JavaMoney,
    * which Firestore was not able to serialize.
    */
    public static Optional<Listing> fromFirestore(QueryDocumentSnapshot document) throws
       UnknownCurrencyException, MonetaryParseException, NumberFormatException, 
       ParseException, IOException, InterruptedException, ExecutionException {
      ImmutableMap<String, Object> listingData = ImmutableMap.copyOf(document.getData());
      
      return Optional.of(Listing.builder()
        .setTimestamp(Optional.of((Timestamp) listingData.get(TIMESTAMP)))
        .setDocumentId(Optional.of(document.getId()))
        .setTitle(listingData.get(TITLE).toString())
        .setDescription(listingData.get(DESCRIPTION).toString())
        .setStartDate(listingData.get(START_DATE).toString())
        .setEndDate(listingData.get(END_DATE).toString())
        .setLeaseType(listingData.get(LEASE_TYPE).toString())
        .setNumRooms(((Long) listingData.get(NUM_ROOMS)).intValue())
        .setNumBathrooms(((Long) listingData.get(NUM_BATHROOMS)).intValue())
        .setNumShared(((Long) listingData.get(NUM_SHARED)).intValue())
        .setNumSingles(((Long) listingData.get(NUM_SINGLES)).intValue())
        .setSharedPrice(listingData.get(SHARED_ROOM_PRICE).toString())
        .setSinglePrice(listingData.get(SINGLE_ROOM_PRICE).toString())
        .setListingPrice(listingData.get(LISTING_PRICE).toString())
        .setComments(getAllCommentsForListing(document.getId()))
        .build());
    }

  /**
  * Gets all comments from a given listing.
  * 
  * @return list of comment instances
  */
  private static ImmutableList<Comment> getAllCommentsForListing(String listingId)
      throws IOException, InterruptedException, ExecutionException {
    List<QueryDocumentSnapshot> documents = 
      DatabaseFactory.getDatabase().getAllCommentDocumentsForListing(listingId)
      .get().getDocuments();

    return StreamSupport.stream(documents.spliterator(), /* parallel= */ false)
      .map(document -> Comment.fromFirestore(document))
      .flatMap(Optional::stream)
      .collect(ImmutableList.toImmutableList());
  }

  /**
  * Sets all listing values to the corresponding HTTP Servlet request parameter.
  */
  public static Listing fromServletRequest(HttpServletRequest request) throws UnknownCurrencyException,
        MonetaryParseException, NumberFormatException, ParseException {
    return Listing.builder()
    .setTitle(request.getParameter(TITLE))
    .setDescription(request.getParameter(DESCRIPTION))
    .setStartDate(request.getParameter(START_DATE))
    .setEndDate(request.getParameter(END_DATE))
    .setLeaseType(request.getParameter(LEASE_TYPE))
    .setNumRooms(request.getParameter(NUM_ROOMS))
    .setNumBathrooms(request.getParameter(NUM_BATHROOMS))
    .setNumShared(request.getParameter(NUM_SHARED))
    .setNumSingles(request.getParameter(NUM_SINGLES))
    .setSharedPrice(request.getParameter(SHARED_ROOM_PRICE))
    .setSinglePrice(request.getParameter(SINGLE_ROOM_PRICE))
    .setListingPrice(request.getParameter(LISTING_PRICE))
    .build();
  }
}