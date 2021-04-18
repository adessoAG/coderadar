package io.reflectoring.coderadar.graph.useradministration.adapter;

import io.reflectoring.coderadar.domain.User;
import io.reflectoring.coderadar.graph.useradministration.domain.UserEntity;
import io.reflectoring.coderadar.graph.useradministration.repository.UserRepository;
import io.reflectoring.coderadar.useradministration.UserNotFoundException;
import io.reflectoring.coderadar.useradministration.port.driven.ChangePasswordPort;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ChangePasswordAdapter implements ChangePasswordPort {
  private final UserRepository userRepository;

  @Override
  public void changePassword(User user) {
    Optional<UserEntity> userEntity = userRepository.findById(user.getId());
    if (userEntity.isPresent()) {
      userEntity.get().setPassword(user.getPassword());
      userRepository.save(userEntity.get());
    } else {
      throw new UserNotFoundException(user.getUsername());
    }
  }
}
