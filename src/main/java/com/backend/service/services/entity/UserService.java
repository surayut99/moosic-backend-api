package com.backend.service.services.entity;

import com.backend.service.exceptions.MoosicException;
import com.backend.service.exceptions.users.DuplicatedUsernameException;
import com.backend.service.exceptions.users.NotFoundUserException;
import com.backend.service.models.entities.UserModel;
import com.backend.service.models.requests.auth.SignInRequest;
import com.backend.service.models.requests.users.BasedUserCreationRequest;
import com.backend.service.repository.UserRepository;
import com.backend.service.services.pages.UserPage;
import com.backend.service.services.specification.UserSpecification;
import com.backend.service.views.users.UserPublicView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.projection.ProjectionFactory;
import org.springframework.data.projection.SpelAwareProxyProjectionFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserService {
  private final ProjectionFactory projectionFactory = new SpelAwareProxyProjectionFactory();
  @Autowired
  private UserRepository userRepo;
  @Autowired
  private UserSpecification userSpecification;
  @Autowired
  private UserPage userPage;

  public UserModel createUser(SignInRequest data) {
    // set new user with SignInDTO data from client-side
    UserModel user = new UserModel();
    user.setUid(data.getUid());
    user.setDisplayName(data.getDisplayName());
    user.setEmail(data.getEmail());
    user.setPhotoUrl(data.getPhotoURL());

    return userRepo.saveAndFlush(user);
  }

  public UserModel updateUser(BasedUserCreationRequest data, UUID id) throws MoosicException {
    UserModel user = userRepo.findById(id).orElseThrow(NotFoundUserException::new);
    boolean isExistsUser = userRepo.existsUserModelByUsername(data.getUsername());

    if (isExistsUser) {
      throw new DuplicatedUsernameException();
    }

    user.setUsername(data.getUsername());
    user.setBio(data.getBio());
    user.setGenres(data.getGenres());
    user.setActive(true);
    user = userRepo.saveAndFlush(user);

    return user;
  }

  public void delete(UUID id) {
    userRepo.deleteById(id);
  }


  public List<UserModel> findByDisplayName(String displayName) {
    return userRepo.findByDisplayName(displayName);
  }

  public UserModel getByUid(String uid) throws NotFoundUserException {
    Optional<UserModel> user = userRepo.findByUid(uid);
    return user.orElseThrow(NotFoundUserException::new);
  }

  public UserModel getById(UUID id) throws NotFoundUserException {
    Optional<UserModel> user = userRepo.findById(id);
    return user.orElseThrow(NotFoundUserException::new);
  }

  public <T> T getById(UUID id, Class<T> type) throws NotFoundUserException {
    UserModel user = getById(id);

    return type == UserModel.class ? type.cast(user) : projectionFactory.createProjection(type, user);
  }

  public UserPublicView getProfileById(UUID id) throws NotFoundUserException {
    Optional<UserPublicView> user = userRepo.findProfileById(id);
    return user.orElseThrow(NotFoundUserException::new);
  }

  public List<UserModel> getAll() {
    return userRepo.findAll();
  }

  public Page<UserModel> getByKeywords(int page, List<String> keywords) {
    return userRepo.findAll(
        userSpecification.finder(keywords),
        userPage.createFeedPageRequest(page)
    );
  }

  public <T> Page<T> getByKeywords(int page, List<String> keywords, Class<T> type) {
    Page<UserModel> result = getByKeywords(page, keywords);

    return userPage.castAfterQuery(result, type);
  }
}
