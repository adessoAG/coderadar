package io.reflectoring.coderadar.query.port.driver.deltatree;

import io.reflectoring.coderadar.domain.DeltaTree;

public interface GetDeltaTreeForTwoCommitsUseCase {

  /**
   * @param command The command containing the commit hashes.
   * @param projectId The id of the project.
   * @return A tree structure containing all the files/modules from both commits along with their
   *     metric values with any changes between them (add, delete, rename, modify) marked as such.
   */
  DeltaTree get(GetDeltaTreeForTwoCommitsCommand command, long projectId);
}
