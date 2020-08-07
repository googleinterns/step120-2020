package com.google.sps;

import static com.google.sps.Constants.CURRENCY_CODE;
import static com.google.sps.Constants.DATE_FORMAT;
import static com.google.sps.Constants.GEOPOINT;
import static com.google.sps.Constants.RENT_COLLECTION_NAME;
import static com.google.sps.Constants.REQUEST_DESCRIPTION;
import static com.google.sps.Constants.REQUEST_END_DATE;
import static com.google.sps.Constants.REQUEST_LAT;
import static com.google.sps.Constants.REQUEST_LEASE_TYPE;
import static com.google.sps.Constants.REQUEST_LNG;
import static com.google.sps.Constants.REQUEST_NUM_ROOMS;
import static com.google.sps.Constants.REQUEST_PRICE;
import static com.google.sps.Constants.REQUEST_ROOM_TYPE;
import static com.google.sps.Constants.REQUEST_START_DATE;
import static com.google.sps.Constants.REQUEST_TIMESTAMP;
import static com.google.sps.Constants.REQUEST_TITLE;

import com.google.auto.value.AutoValue;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.FieldValue;
import com.google.cloud.Timestamp;
import com.google.cloud.firestore.GeoPoint;
import java.lang.Math;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import javax.annotation.Nullable;
import javax.money.CurrencyUnit;
import javax.money.Monetary;
import org.javamoney.moneta.Money;

/** A rent post made by a user. */
@AutoValue
public abstract class RentPost {
  enum LeaseType {
    YEAR_LONG, 
    MONTH_TO_MONTH, 
    SUMMER_SUBLET;
  }
  enum RoomType {
    SINGLE,
    SHARED;
  }

  public abstract String description();
  @Nullable public abstract Date endDate();
  public abstract LeaseType leaseType();
  public abstract GeoPoint location();
  public abstract int numRooms();
  public abstract Money price();
  public abstract RoomType roomType();
  @Nullable public abstract Date startDate();
  public abstract String title();
  public abstract Double milesToSchool();

  public static Builder builder() {
    return new AutoValue_RentPost.Builder();
  }

  @AutoValue.Builder
  public abstract static class Builder {
    abstract Builder setDescription(String description);
    abstract Builder setEndDate(Date endDate);
    abstract Builder setLeaseType(LeaseType leaseType);
    abstract Builder setLocation(GeoPoint location);
    abstract Builder setNumRooms(int numRooms);
    abstract Builder setPrice(Money price);
    abstract Builder setRoomType(RoomType roomType);
    abstract Builder setStartDate(Date startDate);
    abstract Builder setTitle(String title);
    abstract Builder setMilesToSchool(Double milesToSchool);

    abstract RentPost build();
    
    public Builder setLeaseType(String leaseType) {
      setLeaseType(LeaseType.valueOf(leaseType));
      return this;
    }

    public Builder setRoomType(String roomType) {
      setRoomType(RoomType.valueOf(roomType));
      return this;
    }

    public Builder setNumRooms(String numRooms) {
      setNumRooms(Integer.parseInt(numRooms));
      return this;
    }

    public Builder setStartDate(String startDate) {
      setStartDate(stringToDate(startDate));
      return this;
    }

    public Builder setEndDate(String endDate) {
      setEndDate(stringToDate(endDate));
      return this;
    }

    public Builder setPrice(String price) {
      setPrice(stringToMoney(price));
      return this;
    }

    public Builder setLocation(String lat, String lng) {
      setLocation(latLngToGeoPoint(lat, lng));
      return this;
    }

    public Builder setMilesToSchool(String lat, String lng) {
      setMilesToSchool(distanceFromLatLngToBerkeley(Double.parseDouble(lat),
        Double.parseDouble(lng)));
      return this;
    }

    /**
    * Converts a string to a Date in the format "yyyy-MM-dd."
    * Returns null if string is not parseable.
    * @param date string date
    * @return a Date in the format "yyyy-MM-dd" or null if parsing
    *         the string fails. 
    */
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

    /**
    * Converts a string price to a Money instance with a USD currency code.
    * @param price string representation of a monetary amount
    * @return a Money instance of the price in USD.
    */
    private Money stringToMoney(String price) {
      CurrencyUnit usd = Monetary.getCurrency(CURRENCY_CODE);
      return Money.of(Integer.parseInt(price), usd);
    }

    private GeoPoint latLngToGeoPoint(String lat, String lng) {
      return new GeoPoint(Double.parseDouble(lat), Double.parseDouble(lng));
    }

    private Double distanceFromLatLngToBerkeley(Double lat, Double lng) {
      Double berkeleyLat = 37.8719;
      Double berkeleyLng = -122.2585;
      Double earthRadiusInMiles = 3958.8;
      Double latInRadians = lat * (Math.PI/180); // Convert degrees to radians
      Double berkeleyLatInRadians = berkeleyLat * (Math.PI/180); // Convert degrees to radians
      Double diffLat = berkeleyLatInRadians-latInRadians; // Radian difference (latitudes)
      Double diffLon = (berkeleyLng - lng) * (Math.PI/180); // Radian difference (longitudes)

      return 2 * earthRadiusInMiles * Math.asin(Math.sqrt(Math.sin(diffLat/2)*Math.sin(diffLat/2) + 
        Math.cos(latInRadians)*Math.cos(berkeleyLatInRadians)*Math.sin(diffLon/2)*Math.sin(diffLon/2)));
    }
  }

  /**
  * Creates a map of <string key, value> of properties of a rent post
  * that can be posted to the database.
  * @return map of <string key (database key), value>   
  */
  public Map<String, Object> toMap() {
    Map<String, Object> rentData = new HashMap<>();
    rentData.put(REQUEST_DESCRIPTION, description());
    rentData.put(REQUEST_END_DATE, endDate());
    rentData.put(REQUEST_LEASE_TYPE, leaseType().toString());
    rentData.put(GEOPOINT, location());
    rentData.put(REQUEST_NUM_ROOMS, numRooms());
    rentData.put(REQUEST_PRICE, price().toString());
    rentData.put(REQUEST_ROOM_TYPE, roomType().toString());
    rentData.put(REQUEST_START_DATE, startDate());
    rentData.put(REQUEST_TIMESTAMP, FieldValue.serverTimestamp());
    rentData.put(REQUEST_TITLE, title());
    rentData.put("Miles to campus", milesToSchool());
    return rentData;
  }
}