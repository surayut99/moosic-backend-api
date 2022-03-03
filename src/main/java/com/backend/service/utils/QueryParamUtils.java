package com.backend.service.utils;

import com.backend.service.exceptions.MoosicException;
import org.apache.http.message.BasicNameValuePair;
import org.springframework.http.HttpStatus;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class QueryParamUtils {
  private static QueryParamUtils cookieUtils;

  private QueryParamUtils() {
  }

  public static QueryParamUtils getInstance() {
    if (cookieUtils == null) {
      cookieUtils = new QueryParamUtils();
    }

    return cookieUtils;
  }

  public List<BasicNameValuePair> convertToNameValuePair(Object obj) {
    List<BasicNameValuePair> nameValuePairs = new ArrayList<>();

    for (Field field : obj.getClass().getDeclaredFields()) {
      field.setAccessible(true);
      try {
        nameValuePairs.add(new BasicNameValuePair(field.getName(), field.get(obj).toString()));
      } catch (IllegalAccessException e) {
        throw new MoosicException(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
      }
    }

    return nameValuePairs;
  }

  public String convertToString(List<BasicNameValuePair> pairs) {
    StringBuilder stringBuilder = new StringBuilder();
    BasicNameValuePair firstPair = pairs.get(0);

    stringBuilder.append(String.format("?%s=%s", firstPair.getName(), firstPair.getValue()));

    for (int i = 1; i < pairs.size(); i++) {
      stringBuilder.append(String.format("&%s=%s", pairs.get(i).getName(), pairs.get(i).getValue()));
    }

    return stringBuilder.toString();
  }
}
