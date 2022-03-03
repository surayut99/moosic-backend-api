package com.backend.service.services.specification;

import com.backend.service.models.entities.ExposedPostModel;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.Predicate;
import java.util.List;

@Service
public class ExposedPostSpecification {

  public Specification<ExposedPostModel> findByMood(List<String> keywords) {
    return (root, query, criteriaBuilder) -> criteriaBuilder
        .or(keywords
            .stream()
            .map(k -> criteriaBuilder
                .like(criteriaBuilder
                    .lower(root.get("playlist").get("mood")), "%" + k.toLowerCase() + "%"))
            .toArray(Predicate[]::new));
  }

  public Specification<ExposedPostModel> findByTitle(List<String> keywords) {
    return (root, query, criteriaBuilder) -> criteriaBuilder
        .or(keywords
            .stream()
            .map(k -> criteriaBuilder
                .like(criteriaBuilder
                    .lower(root.get("music").get("title")), "%" + k.toLowerCase() + "%"))
            .toArray(Predicate[]::new));
  }

  public Specification<ExposedPostModel> findByCreatedAtGE(String fromDate) {
    return (root, query, criteriaBuilder) -> criteriaBuilder
        .greaterThan(root.get("createdAt"), fromDate);
  }

//  public Specification<ExposedPostModel> finder(List<String> keywords, boolean isTitleType) {
//    return isTitleType ? null : findByMood(keywords);
//  }
}
