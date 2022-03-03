package com.backend.service.utils;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;

import javax.servlet.http.HttpServletResponse;

public class CookieUtils {
  private static CookieUtils cookieUtils;

  private CookieUtils() {
  }

  public static CookieUtils getInstance() {
    if (cookieUtils == null) {
      cookieUtils = new CookieUtils();
    }

    return cookieUtils;
  }

  public void addCookie(HttpServletResponse response, String name, String value) {
    ResponseCookie cookie = ResponseCookie.from(name, value)
        .httpOnly(true)
        .maxAge(3600)
        .sameSite("None")
        .secure(true)
        .path("/")
        .domain("localhost")
        .build();

    response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
  }

  public void removeCookie(HttpServletResponse response, String name) {
    ResponseCookie cookie = ResponseCookie.from(name, null)
        .httpOnly(true)
        .maxAge(0)
        .sameSite("None")
        .secure(true)
        .path("/")
        .domain("localhost")
        .build();

    response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
  }
}
