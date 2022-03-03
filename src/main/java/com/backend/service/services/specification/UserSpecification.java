package com.backend.service.services.specification;

import com.backend.service.models.entities.UserModel;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.Predicate;
import java.util.List;

@Service
public class UserSpecification {

  public Specification<UserModel> findByUsername(List<String> keywords) {
    return (root, query, criteriaBuilder) -> criteriaBuilder
        .or(keywords
            .stream()
            .map(k -> criteriaBuilder
                .like(criteriaBuilder
                    .lower(root.get("username")), "%" + k.toLowerCase() + "%"))
            .toArray(Predicate[]::new));
  }

  public Specification<UserModel> findByDisplayName(List<String> keywords) {
    return (root, query, criteriaBuilder) -> criteriaBuilder
        .or(keywords
            .stream()
            .map(k -> criteriaBuilder
                .like(criteriaBuilder
                    .lower(root.get("displayName")), "%" + k.toLowerCase() + "%"))
            .toArray(Predicate[]::new));
  }

  public Specification<UserModel> findByEmail(List<String> keywords) {
    return (root, query, criteriaBuilder) -> criteriaBuilder
        .or(keywords
            .stream()
            .map(k -> criteriaBuilder
                .like(criteriaBuilder
                    .lower(root.get("email")), "%" + k.toLowerCase() + "%"))
            .toArray(Predicate[]::new));
  }

  public Specification<UserModel> finder(List<String> keywords) {
    return Specification.where(findByUsername(keywords))
        .or(findByDisplayName(keywords))
        .or(findByEmail(keywords));
  }

}
