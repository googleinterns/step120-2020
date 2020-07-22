package com.google.roomies;

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
import com.google.common.collect.ImmutableMap;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import javax.annotation.Nullable;
import javax.money.CurrencyUnit;
import javax.money.Monetary;
import javax.servlet.http.HttpServletRequest;
import org.javamoney.moneta.Money;

/** A listing made by a user. */
@AutoValue
abstract class Listing implements Document, Serializable {
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

  static Builder builder() {
    return new AutoValue_Listing.Builder();
  }

  @AutoValue.Builder
  public abstract static class Builder implements Serializable {
    abstract Builder setTimestamp(Optional<Timestamp> timestamp);
    abstract Builder setDocumentId(Optional<String> documentId);
    abstract Builder setTitle(String title);
    abstract Builder setDescription(String description);
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

    abstract LeaseType leaseType();
    abstract int numRooms();

    abstract Listing build();
    
    /**
    * Throws IllegalArgumentException if input does not match a LeaseType enum 
    * value (case sensistive).
    */
    Builder setLeaseType(String leaseType) {
      setLeaseType(LeaseType.valueOf(leaseType));
      return this;
    }

    /**
    * Throws NumberFormatException if input is not parseable.
    */
    Builder setNumRooms(String numRooms) {
      setNumRooms(Integer.parseInt(numRooms));
      return this;
    }

    /**
    * Input should be a non-negative integer.
    * Throws NumberFormatException if input is not parseable.
    */
    Builder setNumBathrooms(String numBathrooms) {
      setNumBathrooms(Integer.parseInt(numBathrooms));
      return this;     
    }
    /**
    * Input should be a non-negative integer.
    * Throws NumberFormatException if input is not parseable.
    */
    Builder setNumShared(String numShared) {
      setNumShared(Integer.parseInt(numShared));
      return this;     
    }

    /**
    * Input should be a non-negative integer.
    * Throws NumberFormatException if input is not parseable.
    */
    Builder setNumSingles(String numSingles) {
      setNumSingles(Integer.parseInt(numSingles));
      return this;     
    }

    /**
    * Input should be in the format "yyyy-MM-dd."
    * Throws ParseException if date is not in correct format.
    */
    Builder setStartDate(String startDate) throws ParseException {
      setStartDate(StringConverter.stringToDate(startDate));
      return this;
    }

    /**
    * Input should be in the format "yyyy-MM-dd."
    * Throws ParseException if date is not in correct format.
    */
    Builder setEndDate(String endDate) throws ParseException {
      setEndDate(StringConverter.stringToDate(endDate));
      return this;
    }

    /**
    * Input should be a postive number corresponding to a valid USD dollar amount,
    * without the $ sign.
    * Throws Exception if price is not in correct format.
    */
    Builder setSharedPrice(String sharedPrice) throws Exception {
      setSharedPrice(StringConverter.stringToMoney(sharedPrice));
      return this;
    }

    /**
    * Input should be a postive number corresponding to a valid USD dollar amount,
    * without the $ sign.
    * Throws Exception if price is not in correct format.
    */
    Builder setSinglePrice(String singlePrice) throws Exception {
      setSinglePrice(StringConverter.stringToMoney(singlePrice));
      return this;
    }

    /**
    * Input should be a postive number corresponding to a valid USD dollar amount,
    * without the $ sign.
    * Throws Exception if price is not in correct format.
    */
    Builder setListingPrice(String listingPrice) throws Exception {
      setListingPrice(StringConverter.stringToMoney(listingPrice));
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
    */
    public static Listing fromFirestore(QueryDocumentSnapshot document) throws Exception {
      ImmutableMap<String, Object> listingData = ImmutableMap.copyOf(document.getData());
      System.out.println("listing data" + listingData);
      return Listing.builder()
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
      .build();
    }

    /**
    * Sets all listing values to the corresponding HTTP Servlet request parameter.
    */
    public static Listing fromServletRequest(HttpServletRequest request) throws Exception {
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