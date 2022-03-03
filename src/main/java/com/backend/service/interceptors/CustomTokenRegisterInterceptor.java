package com.backend.service.interceptors;

import com.backend.service.exceptions.auth.NotFoundCustomTokenException;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;

@Component
public class CustomTokenRegisterInterceptor implements HandlerInterceptor {
  @Override
  public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
    String tokenStr = request.getHeader("Authorization");

    // check if token in header and starts with 'Bearer'
    if (tokenStr != null && tokenStr.startsWith("Bearer ")) {
      // re-assign token starts at character 7
      tokenStr = tokenStr.substring(7);
    } else {
      // for token not in header, go to check token in cookie
      Cookie[] cookies = request.getCookies();

      if (cookies == null) {
        throw new NotFoundCustomTokenException();
      }

      Cookie cookie = Arrays.stream(cookies).filter(c -> c.getName().equals("custom_token"))
          .findFirst()
          .orElseThrow(NotFoundCustomTokenException::new);
      tokenStr = cookie.getValue();
    }

    // set custom-cookie at attribute request
    request.setAttribute("custom_token", tokenStr);

    return true;
  }
}
