package com.backend.service.repository;

import com.backend.service.models.entities.AuthTokenModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface AuthTokenRepository extends JpaRepository<AuthTokenModel, UUID> {
  Optional<AuthTokenModel> findByUserId(UUID userId);

  @Transactional
  void deleteByUserId(UUID userId);
}
