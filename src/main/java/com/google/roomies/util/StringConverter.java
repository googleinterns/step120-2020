package com.google.roomies;

import static com.google.roomies.ListingConstants.CURRENCY_CODE;
import static com.google.roomies.ListingConstants.DATE_FORMAT;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.money.CurrencyUnit;
import javax.money.Monetary;
import javax.money.UnknownCurrencyException;
import javax.money.format.MonetaryParseException;
import org.javamoney.moneta.Money;

public final class StringConverter {
  private StringConverter() {}

  /**
  * Converts a string to a Date in the format "yyyy-MM-dd."
  *
  * @param date string date
  * @return a Date in the format "yyyy-MM-dd."
  * @throws ParseException if Date cannot be parsed into the format.
  */
  public static Date stringToDate(String date) throws ParseException {
    SimpleDateFormat format = new SimpleDateFormat(DATE_FORMAT);
    return format.parse(date);
  }

  /**
  * Converts a string price to a Money instance with a USD currency code.
  *
  * @param price string representation of a monetary amount
  * @return a Money instance of the price in USD.
  */
  public static Money stringToMoney(String price) throws Exception {
    Money convertedPrice;
    try {
      convertedPrice = Money.parse(price);
    } catch (MonetaryParseException | UnknownCurrencyException | NumberFormatException e) {
      try {
        CurrencyUnit usd = Monetary.getCurrency(CURRENCY_CODE);
        convertedPrice = Money.of(Integer.parseInt(price.toString()), usd);
      } catch (NumberFormatException numFormatException) {
        throw new Exception(numFormatException);
      }
    }
    return convertedPrice;
  }
}