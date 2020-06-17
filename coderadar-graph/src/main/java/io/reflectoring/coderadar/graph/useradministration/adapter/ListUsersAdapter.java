package io.reflectoring.coderadar.graph.useradministration.adapter;

import io.reflectoring.coderadar.graph.useradministration.UserMapper;
import io.reflectoring.coderadar.graph.useradministration.domain.UserEntity;
import io.reflectoring.coderadar.graph.useradministration.repository.UserRepository;
import io.reflectoring.coderadar.useradministration.domain.User;
import io.reflectoring.coderadar.useradministration.port.driven.ListUsersPort;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class ListUsersAdapter implements ListUsersPort {

  private final UserRepository userRepository;
  private final UserMapper userMapper = new UserMapper();

  public ListUsersAdapter(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  @Override
  public List<User> listUsers() {
    List<UserEntity> userEntities = new ArrayList<>();
    userRepository.findAll(0).iterator().forEachRemaining(userEntities::add);
    return userMapper.mapNodeEntities(userEntities);
  }
}