package com.google.sps;

import static com.google.sps.Constants.DATE_FORMAT;
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

import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.FieldValue;
import com.google.cloud.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/** A rent post made by a user. */
public class RentPost {
  enum LeaseType {
    YEAR_LONG, 
    MONTH_TO_MONTH, 
    SUMMER_SUBLET;
  }
  enum RoomType {
    SINGLE,
    SHARED;
  }
  private String description;
  private Date endDate;
  private LeaseType leaseType;
  private int numRooms;
  private String price;
  private RoomType roomType;
  private Date startDate;
  private String title;

  public RentPost(String description, Date endDate, String leaseType,
        String numRooms, String price, String roomType, Date startDate, String title) {
    this.description = description;
    this.endDate = endDate;
    this.leaseType = LeaseType.valueOf(leaseType);
    this.numRooms = Integer.parseInt(numRooms);
    this.price = price;
    this.roomType = RoomType.valueOf(roomType);
    this.startDate = startDate;
    this.title = title;
  }

  public RentPost(String description, String endDate, String leaseType,
        String numRooms, String price, String roomType, String startDate, String title) {
    this.description = description;
    this.endDate = stringToDate(endDate);
    this.leaseType = LeaseType.valueOf(leaseType);
    this.numRooms = Integer.parseInt(numRooms);
    this.price = price;
    this.roomType = RoomType.valueOf(roomType);
    this.startDate = stringToDate(startDate);
    this.title = title;
  }
 
  public Map<String, Object> toMap() {
    Map<String, Object> rentData = new HashMap<>();
    rentData.put(REQUEST_DESCRIPTION, this.description);
    rentData.put(REQUEST_END_DATE, this.endDate);
    rentData.put(REQUEST_LEASE_TYPE, this.leaseType.toString());
    rentData.put(REQUEST_NUM_ROOMS, this.numRooms);
    rentData.put(REQUEST_PRICE, this.price);
    rentData.put(REQUEST_ROOM_TYPE, this.roomType.toString());
    rentData.put(REQUEST_START_DATE, this.startDate);
    rentData.put(REQUEST_TIMESTAMP, FieldValue.serverTimestamp());
    rentData.put(REQUEST_TITLE, this.title);

    return rentData;
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