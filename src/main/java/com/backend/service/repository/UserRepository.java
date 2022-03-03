package com.backend.service.repository;

import com.backend.service.models.entities.UserModel;
import com.backend.service.models.requests.users.BasedUserCreationRequest;
import com.backend.service.views.users.UserPublicView;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<UserModel, UUID>, JpaSpecificationExecutor<UserModel> {
  List<UserModel> findByDisplayName(String displayName);

  @Transactional
  @Modifying()
  @Query("update UserModel u set u.displayName=:#{#user.displayName}, u.bio=:#{#user.bio}, u.genres=:#{#user.genres} where u.id=:id")
  void update(@Param("user") BasedUserCreationRequest user, @Param("id") UUID id);

  Optional<UserModel> findByEmail(String email);

  Optional<UserModel> findByUid(String uid);

  boolean existsUserModelByUsername(String username);

  @Query("select u from UserModel u where u.id=?1")
  Optional<UserPublicView> findProfileById(UUID id);
}
