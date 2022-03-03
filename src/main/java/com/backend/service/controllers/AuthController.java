package com.backend.service.controllers;

import com.backend.service.exceptions.MoosicException;
import com.backend.service.exceptions.auth.InvalidCustomTokenException;
import com.backend.service.exceptions.users.NotFoundUserException;
import com.backend.service.models.authentication.CustomTokenCredential;
import com.backend.service.models.entities.AuthTokenModel;
import com.backend.service.models.entities.UserModel;
import com.backend.service.models.requests.auth.SignInRequest;
import com.backend.service.models.requests.auth.TokenRegisterRequest;
import com.backend.service.models.responses.APIResponse;
import com.backend.service.services.entity.AuthTokenService;
import com.backend.service.services.entity.UserService;
import com.backend.service.utils.CookieUtils;
import com.backend.service.utils.TokenUtils;
import com.backend.service.views.users.UserPublicView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/auth/oauth")
public class AuthController {
  @Autowired
  private AuthTokenService authTokenService;
  @Autowired
  private UserService userService;

  private final Logger logger = LoggerFactory.getLogger(AuthController.class);

  @PostMapping("/sign-in")
  public ResponseEntity<APIResponse> signIn(@RequestBody SignInRequest data, HttpServletResponse response) throws FirebaseAuthException, MoosicException {
    // verify access token against Firebase.
    // if not passed, exception will be thrown
    FirebaseToken decodedToken = TokenUtils.getInstance().verifyToken(data.getAccessToken());
    UserModel user;

    // try to get user from db by uid (external uid)
    // if failed, go to create new user
    try {
      user = userService.getByUid(data.getUid());
      // revoke old token
      authTokenService.revokeToken(user.getId());
    } catch (NotFoundUserException e) {
      user = userService.createUser(data);
    }

    // create custom token as object
    String customToken = TokenUtils.getInstance().createCustomToken(decodedToken.getUid(), user);
    Map<String, Object> token = new HashMap<>();
    token.put("customToken", customToken);

    // set custom token as cookie for temporary communication
    CookieUtils.getInstance().addCookie(response, "custom_token", customToken);

    // build object for responding
    APIResponse res = APIResponse.builder()
        .success(true)
        .message("Custom token generated successfully")
        .data(token)
        .build();

    // also send custom token back as body response
    return ResponseEntity.status(HttpStatus.OK).body(res);
  }

  @PostMapping("/token/register")
  public ResponseEntity<APIResponse> registerToken(@RequestBody TokenRegisterRequest data, HttpServletRequest request, HttpServletResponse response) throws MoosicException, FirebaseAuthException {
    // retrieve token string from attribute request
    String customToken = request.getAttribute("custom_token").toString();

    // decode custom token for verification
    CustomTokenCredential customCredential = TokenUtils.getInstance().decodeToken(customToken, CustomTokenCredential.class);
    String customUid = customCredential.getUid();

    // verify cookie value with token dto
    FirebaseToken firebaseToken = FirebaseAuth.getInstance().verifyIdToken(data.getAccessToken(), true);
    String firebaseUid = firebaseToken.getUid();

    // if user id from different tokens are the same, throw new exception
    if (!customUid.equals(firebaseUid)) {
      throw new InvalidCustomTokenException();
    }

    // send token dto to create token entry in db
    AuthTokenModel token = authTokenService.createToken(data, customCredential.getClaims());

    // get user detail from db
    UserPublicView user = userService.getById(token.getUserId(), UserPublicView.class);

    // set new token data as cookie in response data
    CookieUtils.getInstance().removeCookie(response, "custom_token");
    CookieUtils.getInstance().addCookie(response, "access_token", token.getAccessToken());
    CookieUtils.getInstance().addCookie(response, "refresh_token", token.getRefreshToken());

    // build object to response
    APIResponse res = APIResponse.builder()
        .success(true)
        .message("Sign in successfully")
        .data(user)
        .build();

    return ResponseEntity.status(HttpStatus.OK).body(res);

  }

  @PostMapping("/sign-out")
  public ResponseEntity<APIResponse> singOut(HttpServletRequest request, HttpServletResponse response) throws MoosicException {
    // get token from attribute request
    UUID userId = (UUID) request.getAttribute("user_id");
    // revoke token for this user id, then go to remove token entry from db
    authTokenService.revokeToken(userId);

    // remove cookies from response data
    CookieUtils.getInstance().removeCookie(response, "access_token");
    CookieUtils.getInstance().removeCookie(response, "refresh_token");

    APIResponse res = APIResponse.builder()
        .success(true)
        .message("Sign out successfully")
        .build();

    return ResponseEntity.status(HttpStatus.OK).body(res);
  }

//    @GetMapping()
//    public ResponseEntity<APIResponse> index() {
//        List<AuthTokenModel> authTokenModels = authTokenService.getAll();
//        APIResponse apiResponse = new APIResponse(true, "Token retrieved successfully", authTokenModels,null);
//
//        return ResponseEntity.status(HttpStatus.OK).body(res);
//
//    }
}
