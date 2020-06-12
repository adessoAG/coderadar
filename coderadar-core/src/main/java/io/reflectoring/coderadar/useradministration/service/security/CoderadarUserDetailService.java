package io.reflectoring.coderadar.useradministration.service.security;

import io.reflectoring.coderadar.useradministration.domain.User;
import io.reflectoring.coderadar.useradministration.port.driven.GetUserPort;
import java.util.Collections;
import java.util.List;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@Service
public class CoderadarUserDetailService implements UserDetailsService {

  private final GetUserPort getUserPort;

  public CoderadarUserDetailService(GetUserPort getUserPort) {
    this.getUserPort = getUserPort;
  }

  @Override
  public UserDetails loadUserByUsername(String username) {
    User user = getUserPort.getUserByUsername(username);
    // TODO add authorities to user
    List<SimpleGrantedAuthority> authorities =
        Collections.singletonList(new SimpleGrantedAuthority("USER"));
    return new CoderadarUserDetails(
        user.getUsername(), user.getPassword(), authorities, true, true, true, true);
  }
}
