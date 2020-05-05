package io.reflectoring.coderadar.useradministration.port.driven;

import io.reflectoring.coderadar.useradministration.domain.User;

import java.util.List;

public interface ListUsersPort {

    /**
     * @return All users in the database.
     */
    List<User> listUsers();
}
