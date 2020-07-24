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

/** Functions that convert strings to other data types. */
public final class StringConverter {
  private StringConverter() {}

  /**
  * Converts a string in the format "yyyy-MM-dd" to a Date.
  *
  * @param date string date
  * @return a Date object representing the input date
  * @throws ParseException if date cannot be parsed in the format.
  */
  public static Date stringToDate(String date) throws ParseException {
    SimpleDateFormat format = new SimpleDateFormat(DATE_FORMAT);
    return format.parse(date);
  }

  /**
  * Converts a string price to a Money instance. 
  *
  * If currency code is not specified, defaults to USD.
  * @param moneyRepresentation string representation of a monetary amount
  * @return a Money instance of the input price.
  */
  public static Money stringToMoney(String moneyRepresentation) throws
     UnknownCurrencyException,MonetaryParseException, NumberFormatException {
    Money convertedPrice;
    try {
      convertedPrice = priceAndCurrencyToMoney(moneyRepresentation);
    } catch (MonetaryParseException | NumberFormatException e) {
      convertedPrice = priceToMoney(moneyRepresentation);
    }
    return convertedPrice;
  }

  /**
  * Converts a string price and currency amount to Money.
  *
  * Input should be in the form of "CURRENCY_CODE price" (ex. "USD 28.87").
  * @param priceAndCurrency string representation of a monetary amount
  * @return a Money instance of the price in specified currency code
  */
  private static Money priceAndCurrencyToMoney(String priceAndCurrency) throws
    UnknownCurrencyException, MonetaryParseException, NumberFormatException {
    return Money.parse(priceAndCurrency);
  }

  /**
  * Converts a string price amount to Money in USD.
  *
  * Input should be in the form of "price" (ex. "28.87").
  * @param price string representation of a monetary amount
  * @return a Money instance of the price in USD
  */
  private static Money priceToMoney(String price) {
    CurrencyUnit usd = Monetary.getCurrency(CURRENCY_CODE);
    return Money.of(Integer.parseInt(price.toString()), usd);
  }
}
