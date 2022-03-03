package com.backend.service.services.implementations;

import com.backend.service.services.entity.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserManagementService {
  @Autowired
  private UserService userService;

  public <T> Page<T> getUserByKeywords(int page, String keyword, Class<T> type) {
    final int KEYWORD_LIMIT = 9;

    List<String> keywords = Arrays.asList(keyword.split(" "));
    keywords = keywords.stream().limit(KEYWORD_LIMIT).collect(Collectors.toList());
    keywords.add(keyword);

    return userService.getByKeywords(page, keywords, type);
  }
}
