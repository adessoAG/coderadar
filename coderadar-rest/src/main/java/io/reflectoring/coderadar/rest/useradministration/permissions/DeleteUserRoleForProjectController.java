package io.reflectoring.coderadar.rest.useradministration.permissions;

import io.reflectoring.coderadar.rest.AbstractBaseController;
import io.reflectoring.coderadar.useradministration.port.driver.permissions.DeleteUserRoleForProjectUseCase;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Transactional
public class DeleteUserRoleForProjectController implements AbstractBaseController {
    private final DeleteUserRoleForProjectUseCase deleteUserRoleForProjectUseCase;

    public DeleteUserRoleForProjectController(DeleteUserRoleForProjectUseCase deleteUserRoleForProjectUseCase) {
        this.deleteUserRoleForProjectUseCase = deleteUserRoleForProjectUseCase;
    }

    @DeleteMapping(path = "/projects/{projectId}/users/{userId}")
    public ResponseEntity<HttpStatus> deleteUserRoleForProject(@PathVariable long projectId, @PathVariable long userId) {
        deleteUserRoleForProjectUseCase.deleteRole(projectId, userId);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
