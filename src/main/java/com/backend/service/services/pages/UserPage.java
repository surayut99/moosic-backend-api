package com.backend.service.services.pages;

import com.backend.service.models.entities.UserModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.projection.ProjectionFactory;
import org.springframework.data.projection.SpelAwareProxyProjectionFactory;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Service
public class UserPage {
  private final ProjectionFactory projectionFactory = new SpelAwareProxyProjectionFactory();


  public PageRequest createFeedPageRequest(int page) {
    int PAGE_SIZE = 5;

    return PageRequest.of(
        page,
        PAGE_SIZE,
        Sort.by("username").ascending()
            .and(Sort.by("displayName").ascending())
            .and(Sort.by("email").ascending())
    );
  }

  public <T> Page<T> castAfterQuery(Page<UserModel> users, Class<T> type) {
    if (type.equals(UserModel.class)) {
      return (Page<T>) users;
    }

    return new PageImpl<>(
        users.getContent()
            .stream()
            .map(u -> projectionFactory.createProjection(type, u))
            .collect(Collectors.toList())
        ,
        users.getPageable(),
        users.getTotalElements());
  }
}
