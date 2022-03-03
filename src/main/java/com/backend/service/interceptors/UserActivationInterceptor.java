package com.backend.service.interceptors;

import com.backend.service.exceptions.users.InactiveUserException;
import com.backend.service.models.entities.UserModel;
import com.backend.service.services.entity.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.UUID;

@Component
public class UserActivationInterceptor implements HandlerInterceptor {
  @Autowired
  private UserService userService;

  @Override
  public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
    UUID uid = (UUID) request.getAttribute("user_id");
    UserModel user = userService.getById(uid);

    if (user.isActive()) {
      throw new InactiveUserException();
    }

    return true;
  }
}
