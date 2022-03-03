package com.backend.service.interceptors;

import com.backend.service.exceptions.MoosicException;
import com.backend.service.exceptions.auth.NotFoundAccessTokenException;
import com.backend.service.exceptions.auth.NotFoundRefreshTokenException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;

@Component
public class TokensCheckerInterceptor implements HandlerInterceptor {
  @Override
  public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws MoosicException {
    String tokenStr = request.getHeader("Authorization");
    String refreshTokenStr = request.getHeader("refresh_token");
    Cookie[] cookies = request.getCookies();

    request.setAttribute("access_token", getTokenValue(tokenStr, cookies, false));
    request.setAttribute("refresh_token", getTokenValue(refreshTokenStr, cookies, true));
    return true;
  }

  private String getTokenValue(String tokenStr, Cookie[] cookies, boolean isRefresh) throws MoosicException {
    String bearer = isRefresh ? "" : "Bearer ";
    String token;

    // check if header has valid field
    if (tokenStr != null && tokenStr.startsWith(bearer)) {
      // if header is valid. get value from substring
      token = tokenStr.substring(isRefresh ? 0 : 7);
    } else {

      // if cookie is null, throw exception out
      if (cookies == null) {
        if (isRefresh) {
          throw new NotFoundRefreshTokenException();
        }
        throw new NotFoundAccessTokenException();
      }

      // set name of cookie to get
      String cookieName = isRefresh ? "refresh_token" : "access_token";
      // get cookie by name.
      // if cookie with given name not found, throw exception out
      Cookie cookie = Arrays.stream(cookies)
          .filter(c -> c.getName().equals(cookieName))
          .findFirst()
          .orElseThrow(() -> new MoosicException("No token for authorization", HttpStatus.UNAUTHORIZED));

      token = cookie.getValue();
    }

    return token;
  }
}
