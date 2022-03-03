package com.backend.service.controllers;

import com.backend.service.exceptions.MoosicException;
import com.backend.service.models.entities.UserModel;
import com.backend.service.models.responses.APIResponse;
import com.backend.service.models.responses.searchs.SearchResponse;
import com.backend.service.services.implementations.PostManagementService;
import com.backend.service.services.implementations.UserManagementService;
import com.backend.service.views.users.UserPublicView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/search")
public class SearchController {
  @Autowired
  private PostManagementService postManagementService;
  @Autowired
  private UserManagementService userManagementService;

  @GetMapping("")
  public ResponseEntity<APIResponse> searchContent(
      @RequestParam(name = "page", required = false, defaultValue = "0") Integer page,
      @RequestParam(name = "type") String type,
      @RequestParam(name = "keyword") String keyword,
      HttpServletRequest request
  ) throws MoosicException {
    UserModel user = (UserModel) request.getAttribute("user");
    Page<?> result;

    switch (type) {
      case "post":
        result = postManagementService
            .getByKeywords(page, user, keyword, true);
        break;
      case "mood":
        result = postManagementService
            .getByKeywords(page, user, keyword, false);
        break;
      case "user":
        result = userManagementService
            .getUserByKeywords(page, keyword, UserPublicView.class);
        break;
      default:
        throw new MoosicException("Type for searching is invalid", HttpStatus.BAD_REQUEST);
    }

    assert result != null;
    APIResponse response = APIResponse.builder()
        .success(true)
        .message(String.format("Search post by `%s` with `%s` successfully", type, keyword))
        .data(SearchResponse.builder()
            .page(page)
            .totalPage(result.getTotalPages())
            .count(result.getNumberOfElements())
            .contents(result.getContent())
            .build())
        .build();

    return ResponseEntity.status(200).body(response);
  }
}
