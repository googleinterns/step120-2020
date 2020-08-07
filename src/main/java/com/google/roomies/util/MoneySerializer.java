package com.google.roomies;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import java.lang.reflect.Type;
import org.javamoney.moneta.Money;

/**
 * JavaMoney serializer for Gson.
 * 
 * JavaMoney by default is not serializable by Gson and requires a custom serializer.
 */
public class MoneySerializer implements JsonSerializer<Money> {
  /**
  * Serializes JavaMoney by returning the string representation of money.
  *
  * @return JsonElement a json element representing JavaMoney
  */
  public JsonElement serialize(Money src, Type typeOfSrc, JsonSerializationContext context) {
    return new JsonPrimitive(src.toString());
  }
}