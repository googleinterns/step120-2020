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
  abstract int numShared();
  abstract int numSingles();
  abstract Money sharedPrice();
  abstract Money singlePrice();
  abstract Money listingPrice();

  public static Builder builder() {
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
    public Builder setLeaseType(String leaseType) {
      setLeaseType(LeaseType.valueOf(leaseType));
      return this;
    }

    /**
    * Throws NumberFormatException if input is not parseable.
    */
    public Builder setNumRooms(String numRooms) {
      setNumRooms(Integer.parseInt(numRooms));
      return this;
    }

    /**
    * Throws NumberFormatException if input is not parseable.
    */
    public Builder setNumBathrooms(String numBathrooms) {
      setNumBathrooms(Integer.parseInt(numBathrooms));
      return this;     
    }

    /**
    * Throws NumberFormatException if input is not parseable.
    */
    public Builder setNumShared(String numShared) {
      setNumShared(Integer.parseInt(numShared));
      return this;     
    }

    /**
    * Throws NumberFormatException if input is not parseable.
    */
    public Builder setNumSingles(String numSingles) {
      setNumSingles(Integer.parseInt(numSingles));
      return this;     
    }

    public Builder setStartDate(String startDate) throws ParseException {
      setStartDate(StringConverterUtils.stringToDate(startDate));
      return this;
    }

    public Builder setEndDate(String endDate) throws ParseException {
      setEndDate(StringConverterUtils.stringToDate(endDate));
      return this;
    }

    public Builder setSharedPrice(String sharedPrice) {
      setSharedPrice(StringConverterUtils.stringToMoney(sharedPrice));
      return this;
    }

    public Builder setSinglePrice(String singlePrice) {
      setSinglePrice(StringConverterUtils.stringToMoney(singlePrice));
      return this;
    }

    public Builder setListingPrice(String listingPrice) {
      setListingPrice(StringConverterUtils.stringToMoney(listingPrice));
      return this;
    }

    /**
    * Sets all listing values to the corresponding HTTP Servlet request parameter.
    */
    public Builder fromServletRequest(HttpServletRequest request) throws ParseException {
      setTitle(request.getParameter(TITLE));
      setDescription(request.getParameter(DESCRIPTION));
      setStartDate(request.getParameter(START_DATE));
      setEndDate(request.getParameter(END_DATE));
      setLeaseType(request.getParameter(LEASE_TYPE));
      setNumRooms(request.getParameter(NUM_ROOMS));
      setNumBathrooms(request.getParameter(NUM_BATHROOMS));
      setNumShared(request.getParameter(NUM_SHARED));
      setNumSingles(request.getParameter(NUM_SINGLES));
      setSharedPrice(request.getParameter(SHARED_ROOM_PRICE));
      setSinglePrice(request.getParameter(SINGLE_ROOM_PRICE));
      setListingPrice(request.getParameter(LISTING_PRICE));
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
}