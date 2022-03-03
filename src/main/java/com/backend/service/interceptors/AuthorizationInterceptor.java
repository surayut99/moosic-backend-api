package com.backend.service.interceptors;

import com.backend.service.exceptions.MoosicException;
import com.backend.service.models.authentication.TokenCredential;
import com.backend.service.models.entities.AuthTokenModel;
import com.backend.service.services.entity.AuthTokenService;
import com.backend.service.services.entity.UserService;
import com.backend.service.utils.CookieUtils;
import com.backend.service.utils.TokenUtils;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
@Service
public class AuthorizationInterceptor implements HandlerInterceptor {
  @Autowired
  private AuthTokenService authTokenService;
  @Autowired
  private UserService userService;

  @Override
  public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
    String accessToken = (String) request.getAttribute("access_token");
    String refreshToken = (String) request.getAttribute("refresh_token");
    TokenCredential tokenCredential = TokenUtils.getInstance().decodeToken(accessToken, TokenCredential.class);

    // try to verity jwt token against firebase service
    try {
      FirebaseAuth.getInstance().verifyIdToken(accessToken, true);
    } catch (FirebaseAuthException e) {
      // if token is expired, go to extract user_id then refresh token
      if (e.getMessage().contains("expired")) {
        AuthTokenModel newToken = authTokenService.refreshToken(
            tokenCredential.getId(),
            refreshToken);

        accessToken = newToken.getAccessToken();
        refreshToken = newToken.getRefreshToken();
      } else {
        throw new MoosicException(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
      }
    }

    // set valid access token as cookie when response
    CookieUtils.getInstance().addCookie(response, "access_token", accessToken);
    CookieUtils.getInstance().addCookie(response, "refresh_token", refreshToken);

    // set attribute request for next interceptor
    request.setAttribute("user", userService.getById(tokenCredential.getId()));
//        request.setAttribute("user_id", tokenCredential.getMoosicId());
//        request.setAttribute("ex_uid", tokenCredential.getUserId());
//        request.setAttribute("access_token", tokenCredential);

    return true;
  }
}
